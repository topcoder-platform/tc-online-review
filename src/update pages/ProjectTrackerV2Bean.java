/*
 * Copyright (c) 2006 TopCoder, Inc. All rights reserved.
 */
package com.topcoder.apps.review.projecttracker;

import com.topcoder.apps.review.document.*;
import com.topcoder.apps.review.persistence.Common;
import com.topcoder.apps.review.security.*;

import com.topcoder.project.phases.Dependency;
import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.template.PhaseTemplate;

import com.topcoder.security.NoSuchUserException;
import com.topcoder.security.RolePrincipal;
import com.topcoder.security.TCSubject;
import com.topcoder.security.UserPrincipal;
import com.topcoder.security.admin.PolicyMgrRemote;
import com.topcoder.security.admin.PolicyMgrRemoteHome;
import com.topcoder.security.admin.PrincipalMgrRemote;
import com.topcoder.security.admin.PrincipalMgrRemoteHome;
import com.topcoder.security.policy.PermissionCollection;

import com.topcoder.shared.util.logging.Logger;

import com.topcoder.util.errorhandling.BaseException;
import com.topcoder.util.idgenerator.bean.IdGen;
import com.topcoder.util.idgenerator.bean.IdGenHome;

import java.rmi.RemoteException;

import java.sql.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import javax.rmi.PortableRemoteObject;

import javax.sql.DataSource;


/**
 * This is the concrete implementation of the ProjectTrackerV2 interface.
 *
 * @author brain_cn
 * @version 1.0
 */
public class ProjectTrackerV2Bean implements SessionBean {
    private static final int PHASE_TYPE_SUBMISSION = 2;
    private static final int PHASE_TYPE_SCREEN = 3;
    private static final int PHASE_TYPE_REVIEW = 4;
    private static final int DEVLOPEMENT_FORUM_TYPE = 2;
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy hh:mm", Locale.US);
    private Logger log;
    private SessionContext ejbContext;
    private DataSource dataSource;
    private DocumentManagerLocal documentManager;
    private IdGen idGen;

    /**
     * Retrieve project info with given project id and project_info_type.
     *
     * @param conn the jdbc connection
     * @param projectId the project id
     * @param type the info type
     *
     * @return info for given type
     *
     * @throws SQLException if error occurs while execute jdbc statement
     */
    private String getProjectInfo(Connection conn, long projectId, int type)
        throws SQLException {
        PreparedStatement ps = conn.prepareStatement(
                "SELECT value from project_info where project_id = ? and project_info_type_id = ?");
        ps.setLong(1, projectId);
        ps.setInt(2, type);

        ResultSet rs = ps.executeQuery();

        try {
            if (rs.next()) {
                return rs.getString("value");
            }
        } finally {
            Common.close(rs);
            Common.close(ps);
        }

        return null;
    }

    /**
     * Parse long type value from given string.
     *
     * @param value string representation
     *
     * @return 0 if the value is null or invalid format.
     */
    private static long parseLongValue(String value) {
        if (value == null) {
            return 0;
        } else {
            try {
                return Long.parseLong(value);
            } catch (Exception e) {
                return 0;
            }
        }
    }

    /**
     * Get a Project from the datbase, given a specific projectId.
     *
     * @param projectId
     * @param requestor
     *
     * @return long array, first is winner id, second is forumId
     *
     * @throws RuntimeException if error occurs while retrieve winner id/forum id
     */
    public long[] getProjectWinnerIdForumId(long projectId, TCSubject requestor) {
        log.debug("PT.getProjectWinnerIdForumId(), projectId: " + projectId + ", requestId: " + requestor.getUserId());

        Connection conn = null;
        PreparedStatement psForum = null;
        ResultSet rsForum = null;
        long[] results = new long[2];

        try {
            conn = dataSource.getConnection();
            results[0] = parseLongValue(getProjectInfo(conn, projectId, 23)); // Winner

            long compVersId = parseLongValue(getProjectInfo(conn, projectId, 1)); // external id

            psForum = conn.prepareStatement("SELECT fm.forum_id " + "FROM forum_master fm, comp_forum_xref cfx " +
                    "WHERE fm.forum_id = cfx.forum_id AND " + "cfx.comp_vers_id = ? AND " + "cfx.forum_type = " +
                    DEVLOPEMENT_FORUM_TYPE + " AND " + "fm.status_id = 1");

            psForum.setLong(1, compVersId);
            rsForum = psForum.executeQuery();

            if (rsForum.next()) {
                results[1] = rsForum.getLong(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            Common.close(conn, psForum, rsForum);
        }

        return results;
    }

    /**
     * Retrieves the id of a project based on the component version id of a component and the project type
     *
     * @param compVersId the component's component version id
     * @param projectType the project type (design or development)
     *
     * @return the project id, -1 if no project exists
     *
     * @throws RuntimeException DOCUMENT ME!
     */
    public long getProjectIdByComponentVersionId(long compVersId, long projectType) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        long projectId = -1;

        try {
            conn = dataSource.getConnection();

            // Find projectId with compVersId and projectType
            ps = conn.prepareStatement("select max(p.project_id) from project p " +
                    "inner join project_info pi on pi.project_id = p.project_id " +
                    "where pi.value = ? and project_category_id = ? ");
            ps.setLong(1, compVersId);
            ps.setLong(2, projectType);
            rs = ps.executeQuery();

            if (rs.next()) {
                projectId = rs.getLong(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            Common.close(conn, ps, rs);
        }

        return projectId;
    }

    /**
     * Gets all the userroles for a project.
     *
     * @param projectId
     *
     * @return UserRole[]
     */
    private UserRole[] getUserRoles(long projectId) {
        return getUserRoles(projectId, null);
    }

    /**
     * Gets the userroles for a project for a specific user. If user == null then all userroles for the project are
     * returned.
     *
     * @param projectId
     * @param user
     *
     * @return UserRole[]
     *
     * @throws RuntimeException DOCUMENT ME!
     */
    private UserRole[] getUserRoles(long projectId, User user) {
        log.debug("PT.getUserRoles(), project: " + projectId + ", user: " + ((user == null) ? (-1) : user.getId()));

        List userRoleList = new LinkedList();

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = dataSource.getConnection();

            if (user == null) {
                ps = conn.prepareStatement("SELECT rur.r_user_role_id, rur.r_role_id, " + "rur.payment_info_id, " +
                        "rur.r_resp_id, rur.login_id, " + "rur.r_user_role_v_id, " +
                        "pi.payment, pi.payment_stat_id, " + "pi.payment_info_v_id, " + "su.user_id, " +
                        "u.first_name, u.last_name, e.address " + "FROM r_user_role rur, " +
                        "OUTER (security_user su, user u, email e), " + "OUTER payment_info pi " +
                        "WHERE rur.cur_version = 1 AND " + "pi.cur_version = 1 AND " +
                        "pi.payment_info_id = rur.payment_info_id AND " + "rur.project_id = ? AND " +
                        "su.login_id = rur.login_id AND " + "su.login_id = u.user_id AND " +
                        "su.login_id = e.user_id AND " + "e.primary_ind = 1 " +
                        "ORDER BY rur.r_role_id, rur.r_resp_id");
            } else {
                ps = conn.prepareStatement("SELECT rur.r_user_role_id, rur.r_role_id, " + "rur.payment_info_id, " +
                        "rur.r_resp_id, rur.login_id, " + "rur.r_user_role_v_id, " +
                        "pi.payment, pi.payment_stat_id, " + "pi.payment_info_v_id " +
                        "FROM r_user_role rur, OUTER payment_info pi " + "WHERE rur.cur_version = 1 AND " +
                        "pi.cur_version = 1 AND " + "pi.payment_info_id = rur.payment_info_id AND " +
                        "rur.project_id = ? AND " + "rur.login_id = ? " + "ORDER BY rur.r_role_id, rur.r_resp_id");
            }

            ps.setLong(1, projectId);

            if (user != null) {
                ps.setLong(2, user.getId());
            }

            rs = ps.executeQuery();

            while (rs.next()) {
                long userRoleId = rs.getLong(1);

                //info("PT.getUserRoles(): Found userRole, id: " + userRoleId);
                long rRoleId = rs.getLong(2);
                long paymentInfoId = rs.getLong(3);
                long rRespId = rs.getLong(4);
                long thisUserId = rs.getLong(5);
                long rVersionId = rs.getLong(6);

                float amount = rs.getFloat(7);
                long paymentStatusId = rs.getLong(8);
                long paymentVersionId = rs.getLong(9);

                User thisUser = user;

                if ((user == null) && (thisUserId != 0)) {
                    String handle = rs.getString(10);
                    String firstName = rs.getString(11);
                    String lastName = rs.getString(12);
                    String email = rs.getString(13);

                    thisUser = new User(thisUserId, handle, firstName, lastName, email);

                    // Retrieve user in sql instead of using the UserManagerBean
                    //thisUser = Common.getUser(dataSource, thisUserId);
                }

                PaymentStatusManager paymentStatusManager = (PaymentStatusManager) Common.getFromCache(
                        "PaymentStatusManager");
                PaymentStatus paymentStatus = paymentStatusManager.getPaymentStatus(paymentStatusId);

                PaymentInfo paymentInfo = null;

                if (paymentVersionId != 0) {
                    paymentInfo = new PaymentInfo(paymentInfoId, amount, paymentStatus, paymentVersionId);
                }

                //PaymentInfo paymentInfo = getPaymentInfo(paymentInfoId);
                RoleManager roleManager = (RoleManager) Common.getFromCache("RoleManager");
                Role role = roleManager.getRole(rRoleId);

                ReviewerResponsibilityManager respManager = (ReviewerResponsibilityManager) Common.getFromCache(
                        "ReviewerResponsibilityManager");
                ReviewerResponsibility rResp = respManager.getResponsibility(rRespId);

                UserRole userRole = new UserRole(userRoleId, role, thisUser, paymentInfo, rResp, rVersionId);
                userRoleList.add(userRole);

                //info("PT.getUserRoles(): Found userRole END");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            Common.close(conn, ps, rs);
        }

        return (UserRole[]) userRoleList.toArray(new UserRole[0]);
    }

    /**
     * Gets the PM for the project, or returns null if no PM is found.
     *
     * @param projectId the id of the project whose PM will be retrieved.
     *
     * @return the User for the PM
     */
    public User getPM(long projectId) {
        UserRole[] userRole = getUserRoles(projectId);

        for (int i = 0; i < userRole.length; i++) {
            if (userRole[i].getRole().getId() == Role.ID_PRODUCT_MANAGER) {
                return userRole[i].getUser();
            }
        }

        return null;
    }

    /**
     * Creates a new Online Review Project.
     *
     * @param projectName - the name of the project.
     * @param projectVersion - the version(text) for the project.
     * @param compVersId - the component version id for the project(from comp_versions.comp_vers_id).
     * @param projectTypeId - 1 for design-project, 2 for development-project
     * @param overview the overview
     * @param dates the dates
     * @param requestor the user requestor
     * @param levelId level id
     *
     * @return long - the projectId for the newly created project.
     *
     * @throws BaseException if error occurs
     * @throws RuntimeException if error occurs
     */
    public long createProject(String projectName, String projectVersion, long compVersId, long projectTypeId,
        String overview, Date[] dates, TCSubject requestor, long levelId)
        throws BaseException {
        log.debug("PT.createProject: compVersId: " + compVersId);

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        long projectId = -1;

        try {
            conn = dataSource.getConnection();

            ps = conn.prepareStatement("SELECT p.project_id " + "FROM project p " + "inner join project_info pi_vi " +
                    "on p.project_id = pi_vi.project_id " + "and pi_vi.project_info_type_id = 1 " + // 1 is external id
                    "inner join project_info pi_vt " + "on p.project_id = pi_vt.project_id " +
                    "and pi_vt.project_info_type_id = 7 " + // 7 is project_version
                    "where pi_vi.value = ? " + "	and pi_vt.value = ? " + "	and p.project_status_id in (1, 7) " + // 1 is active, 7 is complete
                    "	and p.project_category_id = ?");
            ps.setString(1, String.valueOf(compVersId)); // In project_info, value field is string type
            ps.setString(2, projectVersion);
            ps.setLong(3, projectTypeId);
            rs = ps.executeQuery();

            if (rs.next()) {
                throw new BaseException("Online Review: A project already exists! Terminate it before changing phase!");
            }

            Common.close(rs);
            Common.close(ps);

            log.debug("About to create new projectId!");
            projectId = idGen.nextId();

            long modUserId = requestor.getUserId();
            long firstPhaseInstanceId = idGen.nextId();
            log.debug("projectId: " + firstPhaseInstanceId);

            // Create project
            ps = conn.prepareStatement("INSERT INTO project " + "(project_id, " +
                    "project_status_id, project_category_id, " + "create_user, create_date, " +
                    "modify_user, modify_date) VALUES " + "(?, ?, ?, ?, CURRENT, ?, CURRENT)");

            long projectStatId = 1; // Active

            int index = 1;
            ps.setLong(index++, projectId);
            ps.setLong(index++, projectStatId);
            ps.setLong(index++, projectTypeId);
            ps.setString(index++, String.valueOf(modUserId));
            ps.setString(index++, String.valueOf(modUserId));

            ps.executeUpdate();
            Common.close(ps);

            // Prepare version_id
            ps = conn.prepareStatement("INSERT INTO project_info " +
                    "(project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date) " +
                    " values (?, 1, ?, ?, CURRENT, ?, CURRENT)");
            index = 1;
            ps.setLong(index++, projectId);
            ps.setString(index++, String.valueOf(compVersId));
            ps.setString(index++, String.valueOf(modUserId));
            ps.setString(index++, String.valueOf(modUserId));
            ps.executeUpdate();
            Common.close(ps);

            // Prepare project_version
            ps = conn.prepareStatement("INSERT INTO project_info " +
                    "(project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date) " +
                    " values (?, 7, ?, ?, CURRENT, ?, CURRENT)");
            index = 1;
            ps.setLong(index++, projectId);
            ps.setString(index++, projectVersion);
            ps.setString(index++, String.valueOf(modUserId));
            ps.setString(index++, String.valueOf(modUserId));
            ps.executeUpdate();
            Common.close(ps);

            // Prepare auto_pilot_ind
            ps = conn.prepareStatement("INSERT INTO project_info " +
                    "(project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date) " +
                    " values (?, 9, 1, ?, CURRENT, ?, CURRENT)");
            index = 1;
            ps.setLong(index++, projectId);
            ps.setString(index++, String.valueOf(modUserId));
            ps.setString(index++, String.valueOf(modUserId));
            ps.executeUpdate();
            Common.close(ps);

            // Prepare project_audit the modify reason is Created
            ps = conn.prepareStatement("INSERT INTO project_audit " +
                    "(project_id, project_audit_id, update_reason, create_user, create_date, modify_user, modify_date) " +
                    " values (?, ?, 'Created', ?, CURRENT, ?, CURRENT)");
            index = 1;

            long auditId = idGen.nextId();
            ps.setLong(index++, projectId);
            ps.setLong(index++, auditId);
            ps.setString(index++, String.valueOf(modUserId));
            ps.setString(index++, String.valueOf(modUserId));
            ps.executeUpdate();
            Common.close(ps);

            // Create phase instances for project
            log.debug("Creating phases ");

            PhaseTemplate template = Common.getPhaseTemplate();
            String templateName = (projectTypeId == 1) ? "Design" : "Development";
            com.topcoder.project.phases.Project project = template.applyTemplate(templateName);
            com.topcoder.project.phases.Phase[] phases = project.getAllPhases();
            Map phaseIds = new HashMap();
            Set dependencies = new HashSet();

            // insert default scorecards
            long screenTemplateId = documentManager.getDefaultScorecardTemplate(projectTypeId,
                    ScreeningScorecard.SCORECARD_TYPE).getId();
            long reviewTemplateId = documentManager.getDefaultScorecardTemplate(projectTypeId,
                    ReviewScorecard.SCORECARD_TYPE).getId();

            for (int i = 0; i < phases.length; i++) {
                long phaseId = idGen.nextId();
                createPhase(conn, projectId, phaseId, phases[i], modUserId);

                if (phases[i].getPhaseType().getId() == PHASE_TYPE_SCREEN) {
                    // Create scorecard id
                    // 1, 'Scorecard ID'
                    this.createPhaseCriteria(conn, phaseId, 1, String.valueOf(screenTemplateId), modUserId);
                }

                if (phases[i].getPhaseType().getId() == PHASE_TYPE_REVIEW) {
                    // Create scorecard id
                    // 1, 'Scorecard ID'
                    this.createPhaseCriteria(conn, phaseId, 1, String.valueOf(reviewTemplateId), modUserId);
                }

                if (phases[i].getPhaseType().getId() == PHASE_TYPE_SUBMISSION) {
                    // Manual Screening
                    // 5, 'Manual Screening'
                    this.createPhaseCriteria(conn, phaseId, 5, "Yes", modUserId);
                }

                phaseIds.put(phases[i], String.valueOf(phaseId));
                dependencies.addAll(Arrays.asList(phases[i].getAllDependencies()));
            }

            // Prepare phase dependency
            for (Iterator iter = dependencies.iterator(); iter.hasNext();) {
                Dependency d = (Dependency) iter.next();
                createPhaseDependency(conn, phaseIds, d, modUserId);
            }

            // Prepare resource for pm
            ps = conn.prepareStatement("INSERT INTO resource " + "(resource_id, resource_role_id, " + "project_id, " +
                    "create_user, create_date, " + "modify_user, modify_date) VALUES " +
                    "(?, 13, ?, ?, CURRENT, ?, CURRENT)"); // 13 is manager

            long resourceId = idGen.nextId();
            index = 1;
            ps.setLong(index++, resourceId);
            ps.setLong(index++, projectId);
            ps.setString(index++, String.valueOf(requestor.getUserId()));
            ps.setString(index++, String.valueOf(requestor.getUserId()));
            ps.executeUpdate();
            Common.close(rs);
            Common.close(ps);

            // External Reference ID
            ps = conn.prepareStatement("INSERT INTO resource_info " + "(resource_id, resource_info_type_id, " +
                    "value, create_user, create_date, " + "modify_user, modify_date) VALUES " +
                    "(?, ?, ?, ?, CURRENT, ?, CURRENT)");
            index = 1;
            ps.setLong(index++, resourceId);
            ps.setLong(index++, 1); // External Reference ID 1
            ps.setString(index++, String.valueOf(requestor.getUserId()));
            ps.setString(index++, String.valueOf(requestor.getUserId()));
            ps.setString(index++, String.valueOf(requestor.getUserId()));
            Common.close(rs);
            Common.close(ps);

            // Clean up this variable for reuse - bblais
            ps = null;

            // Create security manager roles for project
            log.debug("Creating security manager roles");

            PrincipalMgrRemote principalMgr;
            PolicyMgrRemote policyMgr;

            try {
                Context initial = new InitialContext();
                Object objref = initial.lookup(PrincipalMgrRemoteHome.EJB_REF_NAME);
                PrincipalMgrRemoteHome home = (PrincipalMgrRemoteHome) PortableRemoteObject.narrow(objref,
                        PrincipalMgrRemoteHome.class);
                principalMgr = home.create();

                objref = initial.lookup(PolicyMgrRemoteHome.EJB_REF_NAME);

                PolicyMgrRemoteHome home2 = (PolicyMgrRemoteHome) PortableRemoteObject.narrow(objref,
                        PolicyMgrRemoteHome.class);
                policyMgr = home2.create();
            } catch (ClassCastException e1) {
                throw new RuntimeException(e1);
            } catch (EJBException e1) {
                throw new RuntimeException(e1);
            } catch (NamingException e1) {
                throw new RuntimeException(e1);
            } catch (CreateException e1) {
                throw new RuntimeException(e1);
            }

            ProjectTypeManager projectTypeManager = (ProjectTypeManager) Common.getFromCache("ProjectTypeManager");

            String prefix = projectName + " " + projectVersion + " " +
                projectTypeManager.getProjectType(projectTypeId).getName() + " ";

            try {
                UserPrincipal userPrincipal = principalMgr.getUser(requestor.getUserId());
                RolePrincipal rp;
                long roleId;

                String roleName = prefix + "View Project " + projectId;
                roleId = getRoleId(roleName);

                if (roleId != -1) {
                    rp = principalMgr.getRole(roleId);
                    principalMgr.removeRole(rp, requestor);
                }

                rp = principalMgr.createRole(roleName, requestor);

                PermissionCollection pc = new PermissionCollection();
                pc.addPermission(new ViewProjectPermission(projectId));
                policyMgr.addPermissions(rp, pc, requestor);

                // Assign View Project to pm
                principalMgr.assignRole(userPrincipal, rp, requestor);

                roleName = prefix + "Submit " + projectId;
                roleId = getRoleId(roleName);

                if (roleId != -1) {
                    rp = principalMgr.getRole(roleId);
                    principalMgr.removeRole(rp, requestor);
                }

                rp = principalMgr.createRole(roleName, requestor);
                pc = new PermissionCollection();
                pc.addPermission(new SubmitPermission(projectId));
                policyMgr.addPermissions(rp, pc, requestor);

                roleName = prefix + "Screen " + projectId;
                roleId = getRoleId(roleName);

                if (roleId != -1) {
                    rp = principalMgr.getRole(roleId);
                    principalMgr.removeRole(rp, requestor);
                }

                rp = principalMgr.createRole(roleName, requestor);
                pc = new PermissionCollection();
                pc.addPermission(new ScreenPermission(projectId));
                policyMgr.addPermissions(rp, pc, requestor);

                roleName = prefix + "Review " + projectId;
                roleId = getRoleId(roleName);

                if (roleId != -1) {
                    rp = principalMgr.getRole(roleId);
                    principalMgr.removeRole(rp, requestor);
                }

                rp = principalMgr.createRole(roleName, requestor);
                pc = new PermissionCollection();
                pc.addPermission(new ReviewPermission(projectId));
                policyMgr.addPermissions(rp, pc, requestor);

                roleName = prefix + "Aggregation " + projectId;
                roleId = getRoleId(roleName);

                if (roleId != -1) {
                    rp = principalMgr.getRole(roleId);
                    principalMgr.removeRole(rp, requestor);
                }

                rp = principalMgr.createRole(roleName, requestor);
                pc = new PermissionCollection();
                pc.addPermission(new AggregationPermission(projectId));
                policyMgr.addPermissions(rp, pc, requestor);

                roleName = prefix + "Submit Final Fix " + projectId;
                roleId = getRoleId(roleName);

                if (roleId != -1) {
                    rp = principalMgr.getRole(roleId);
                    principalMgr.removeRole(rp, requestor);
                }

                rp = principalMgr.createRole(roleName, requestor);
                pc = new PermissionCollection();
                pc.addPermission(new SubmitFinalFixPermission(projectId));
                policyMgr.addPermissions(rp, pc, requestor);

                roleName = prefix + "Final Review " + projectId;
                roleId = getRoleId(roleName);

                if (roleId != -1) {
                    rp = principalMgr.getRole(roleId);
                    principalMgr.removeRole(rp, requestor);
                }

                rp = principalMgr.createRole(roleName, requestor);
                pc = new PermissionCollection();
                pc.addPermission(new FinalReviewPermission(projectId));
                policyMgr.addPermissions(rp, pc, requestor);
            } catch (com.topcoder.security.GeneralSecurityException e) {
                ejbContext.setRollbackOnly();

                SQLException sqle = (SQLException) e.getCause();
                int i = 1;
                System.err.println("*******************************");

                while (sqle != null) {
                    System.err.println("  Error #" + i + ":");
                    System.err.println("    SQLState = " + sqle.getSQLState());
                    System.err.println("    Message = " + sqle.getMessage());
                    System.err.println("    SQLCODE = " + sqle.getErrorCode());
                    sqle.printStackTrace();
                    sqle = sqle.getNextException();
                    i++;
                }

                throw new RuntimeException("GeneralSecurityException: " + e.getMessage());
            }
        } catch (SQLException e) {
            ejbContext.setRollbackOnly();
            throw new RuntimeException("SQLException: " + e.getMessage());
        } catch (RemoteException e) {
            ejbContext.setRollbackOnly();
            throw new RuntimeException("RemoteException: " + e.getMessage());
        } finally {
            Common.close(conn, ps, rs);
        }

        return projectId;
    }

    /**
     * Create phase with given project id, phase id and created user id.
     *
     * @param conn the jdbc connection
     * @param projectId the project id
     * @param phaseId the phase id
     * @param phase the create phase which contains start/end time and duration.
     * @param userId the created user id
     *
     * @throws SQLException if error occurs while execute jdbc statement
     */
    private void createPhase(Connection conn, long projectId, long phaseId, Phase phase, long userId)
        throws SQLException {
        PreparedStatement ps = conn.prepareStatement("INSERT INTO project_phase " + "(project_phase_id, project_id, " +
                "phase_type_id, phase_status_id, duration, fixed_start_time, scheduled_start_time, scheduled_end_time, " +
                "actual_start_time, actual_end_time, create_user, create_date, " + "modify_user, modify_date) VALUES " +
                "(?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ?, CURRENT, ?, CURRENT)"); // 1: Scheduled
        int index = 1;
        ps.setLong(index++, phaseId);
        ps.setLong(index++, projectId);
        ps.setLong(index++, phase.getPhaseType().getId());
        ps.setLong(index++, phase.getPhaseStatus().getId());
        ps.setLong(index++, phase.getLength());
        ps.setDate(index++, new Date(phase.getFixedStartDate().getTime()));
        ps.setDate(index++, new Date(phase.getScheduledStartDate().getTime()));
        ps.setDate(index++, new Date(phase.getScheduledEndDate().getTime()));
        ps.setDate(index++, new Date(phase.getActualStartDate().getTime()));
        ps.setDate(index++, new Date(phase.getActualEndDate().getTime()));
        ps.setString(index++, String.valueOf(userId));
        ps.setString(index++, String.valueOf(userId));
        Common.close(ps);
    }

    /**
     * Create PhaseCriteria with given phaseId, parameter and created user id.
     *
     * @param conn the jdbc connection
     * @param phaseId the phase id
     * @param type DOCUMENT ME!
     * @param parameter the Criteria parameter
     * @param userId the created user id
     *
     * @throws SQLException if error occurs while execute jdbc statement
     */
    private void createPhaseCriteria(Connection conn, long phaseId, int type, String parameter, long userId)
        throws SQLException {
        PreparedStatement ps = conn.prepareStatement("INSERT INTO phase_criteria " +
                "(project_phase_id, phase_criteria_type_id, parameter, " + "create_user, create_date, " +
                "modify_user, modify_date) VALUES " + "(?, ?, ?, ?, CURRENT, ?, CURRENT)"); // 1: Scheduled
        int index = 1;
        ps.setLong(index++, phaseId);
        ps.setLong(index++, type);
        ps.setString(index++, parameter);
        ps.setString(index++, String.valueOf(userId));
        ps.setString(index++, String.valueOf(userId));
        Common.close(ps);
    }

    /**
     * Create PhaseDependency with given Dependency, phaseIds and created user id.
     *
     * @param conn the jdbc connection
     * @param phaseIds that map which map phase to its phase id in persistence
     * @param d the denpendency and dependent instance
     * @param userId the created user id
     *
     * @throws SQLException if error occurs while execute jdbc statement
     */
    private void createPhaseDependency(Connection conn, Map phaseIds, Dependency d, long userId)
        throws SQLException {
        PreparedStatement ps = conn.prepareStatement("INSERT INTO phase_dependency " +
                "(dependency_phase_id, dependent_phase_id, dependency_start, " +
                "dependent_start, lag_time, create_user, create_date, " + "modify_user, modify_date) VALUES " +
                "(?, ?, ?, ?, ?, ?, CURRENT, ?, CURRENT)"); // 1: Scheduled

        Phase denpendency = d.getDependency();
        Phase denpendent = d.getDependent();
        String denpendencyId = (String) phaseIds.get(denpendency);
        String denpendentId = (String) phaseIds.get(denpendent);

        int index = 1;
        ps.setLong(index++, Long.parseLong(denpendencyId));
        ps.setLong(index++, Long.parseLong(denpendentId));
        ps.setDate(index++, new Date(denpendency.getActualEndDate().getTime()));
        ps.setDate(index++, new Date(denpendent.getActualEndDate().getTime()));
        ps.setLong(index++, d.getLagTime());
        ps.setString(index++, String.valueOf(userId));
        ps.setString(index++, String.valueOf(userId));
        Common.close(ps);
    }

    /**
     * DOCUMENT ME!
     *
     * @param userId DOCUMENT ME!
     * @param projectId DOCUMENT ME!
     *
     * @throws BaseException DOCUMENT ME!
     * @throws RuntimeException DOCUMENT ME!
     */
    public void userInquiry(long userId, long projectId)
        throws BaseException {
        log.debug("PT.userInquiry; userId: " + userId + " ,projectId: " + projectId);

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = dataSource.getConnection();

            Common.close(rs);
            Common.close(ps);

            // Prepare resource
            ps = conn.prepareStatement("INSERT INTO resource " + "(resource_id, resource_role_id, " + "project_id, " +
                    "create_user, create_date, " + "modify_user, modify_date) VALUES " +
                    "(?, 1, ?, ?, CURRENT, ?, CURRENT)");

            long resourceId = idGen.nextId();
            int index = 1;
            ps.setLong(index++, resourceId);
            ps.setLong(index++, projectId);
            ps.setString(index++, String.valueOf(userId));
            ps.setString(index++, String.valueOf(userId));

            int nr = ps.executeUpdate();

            if (nr != 1) {
                throw new RuntimeException("Could not create resource!");
            }

            Common.close(rs);
            Common.close(ps);

            // prepare rating/Reliability
            ps = conn.prepareStatement("SELECT rating from user_rating where user_id = ? and phase_id = " +
                    "(select 111+project_category_id from project where project_id = ?)");
            ps.setLong(1, userId);
            ps.setLong(2, projectId);
            rs = ps.executeQuery();

            double old_rating = 0;

            if (rs.next()) {
                old_rating = rs.getDouble(1);
            }

            Common.close(rs);
            Common.close(ps);

            ps = conn.prepareStatement("SELECT rating from user_reliability where user_id = ? and phase_id = " +
                    "(select 111+project_category_id from project where project_id = ?)");
            ps.setLong(1, userId);
            ps.setLong(2, projectId);
            rs = ps.executeQuery();

            double oldReliability = 0;

            if (rs.next()) {
                oldReliability = rs.getDouble(1);
            }

            Common.close(rs);
            Common.close(ps);

            // add reliability_ind and old_reliability
            ps = conn.prepareStatement("INSERT INTO project_result " +
                    "(project_id, user_id, rating_ind, reliability_ind, valid_submission_ind, old_rating, old_reliability) " +
                    "values (?, ?, ?, ?, ?, ?, ?)");

            ps.setLong(1, projectId);
            ps.setLong(2, userId);
            ps.setLong(3, 0);
            ps.setLong(4, 0);
            ps.setLong(5, 0);

            if (old_rating == 0) {
                ps.setNull(6, Types.DOUBLE);
            } else {
                ps.setDouble(7, old_rating);
            }

            if (old_rating == 0) {
                ps.setNull(7, Types.DOUBLE);
            } else {
                ps.setDouble(7, oldReliability);
            }

            ps.execute();

            Common.close(ps);

            // External Reference ID
            ps = conn.prepareStatement("INSERT INTO resource_info " + "(resource_id, resource_info_type_id, " +
                    "value, create_user, create_date, " + "modify_user, modify_date) VALUES " +
                    "(?, ?, ?, ?, CURRENT, ?, CURRENT)");
            index = 1;
            ps.setLong(index++, resourceId);
            ps.setLong(index++, 1); // External Reference ID 1
            ps.setString(index++, String.valueOf(userId));
            ps.setString(index++, String.valueOf(userId));
            ps.setString(index++, String.valueOf(userId));
            nr = ps.executeUpdate();

            if (nr != 1) {
                throw new RuntimeException("Could not create External Reference ID resourceinfo !");
            }

            Common.close(rs);
            Common.close(ps);

            // Rating 4, 
            ps = conn.prepareStatement("INSERT INTO resource_info " + "(resource_id, resource_info_type_id, " +
                    "value, create_user, create_date, " + "modify_user, modify_date) VALUES " +
                    "(?, ?, ?, ?, CURRENT, ?, CURRENT)");
            index = 1;
            ps.setLong(index++, resourceId);
            ps.setLong(index++, 4);
            ps.setString(index++, String.valueOf(old_rating));
            ps.setString(index++, String.valueOf(userId));
            ps.setString(index++, String.valueOf(userId));
            nr = ps.executeUpdate();

            if (nr != 1) {
                throw new RuntimeException("Could not create Rating resourceinfo !");
            }

            Common.close(rs);
            Common.close(ps);

            // Reliability 5 
            ps = conn.prepareStatement("INSERT INTO resource_info " + "(resource_id, resource_info_type_id, " +
                    "value, create_user, create_date, " + "modify_user, modify_date) VALUES " +
                    "(?, ?, ?, ?, CURRENT, ?, CURRENT)");
            index = 1;
            ps.setLong(index++, resourceId);
            ps.setLong(index++, 5);
            ps.setString(index++, String.valueOf(oldReliability));
            ps.setString(index++, String.valueOf(userId));
            ps.setString(index++, String.valueOf(userId));
            nr = ps.executeUpdate();

            if (nr != 1) {
                throw new RuntimeException("Could not create External Reference ID resourceinfo !");
            }

            Common.close(rs);
            Common.close(ps);

            // Registration Date 6
            ps = conn.prepareStatement("INSERT INTO resource_info " + "(resource_id, resource_info_type_id, " +
                    "value, create_user, create_date, " + "modify_user, modify_date) VALUES " +
                    "(?, ?, ?, ?, CURRENT, ?, CURRENT)");
            index = 1;
            ps.setLong(index++, resourceId);
            ps.setLong(index++, 5);
            ps.setString(index++, DATE_FORMAT.format(new java.util.Date()));
            ps.setString(index++, String.valueOf(userId));
            ps.setString(index++, String.valueOf(userId));
            nr = ps.executeUpdate();

            if (nr != 1) {
                throw new RuntimeException("Could not create External Reference ID resourceinfo !");
            }

            Common.close(rs);
            Common.close(ps);

            PrincipalMgrRemote principalMgr;

            try {
                Context initial = new InitialContext();
                Object objref = initial.lookup(PrincipalMgrRemoteHome.EJB_REF_NAME);
                PrincipalMgrRemoteHome home = (PrincipalMgrRemoteHome) PortableRemoteObject.narrow(objref,
                        PrincipalMgrRemoteHome.class);
                principalMgr = home.create();
            } catch (ClassCastException e1) {
                throw new RuntimeException(e1);
            } catch (EJBException e1) {
                throw new RuntimeException(e1);
            } catch (NamingException e1) {
                throw new RuntimeException(e1);
            } catch (CreateException e1) {
                throw new RuntimeException(e1);
            }

            Common.close(ps);
            ps = null;

            // Prepare component_name Project Name , version_text Project Version, project_type project_category_id
            ps = conn.prepareStatement(
                    "SELECT pi_n.value component_name, pi_v.value version_text, p.project_category_id project_type_id" +
                    "FROM project p " + "inner join project_info pi_n " + "on pi_n.project_id = p.project_id " +
                    "and pi_n.project_info_type_id = 6 " + "inner join project_info pi_v " +
                    "on pi_v.project_id = p.project_id " + "and pi_v.project_info_type_id = 7 " +
                    "where p.project_id = ? and " + "p.project_status_id = 1");
            ps.setLong(1, projectId);
            rs = ps.executeQuery();

            String projectName = null;
            String projectVersion = null;
            long projectTypeId = 0;

            if (rs.next()) {
                projectName = rs.getString("component_name");
                projectVersion = rs.getString("version_text").trim();
                projectTypeId = rs.getLong("project_type_id");
            } else {
                throw new BaseException("Missing component");
            }

            ProjectTypeManager projectTypeManager = (ProjectTypeManager) Common.getFromCache("ProjectTypeManager");

            String prefix = projectName + " " + projectVersion + " " +
                projectTypeManager.getProjectType(projectTypeId).getName() + " ";

            // TODO What user to assign roles?
            TCSubject requestor = new TCSubject(userId);

            try {
                UserPrincipal userPrincipal = principalMgr.getUser(userId);

                String roleName = prefix + "View Project " + projectId;
                long roleId = getRoleId(roleName);

                if (roleId == -1) {
                    throw new RuntimeException("Can't find roleName: " + roleName);
                }

                RolePrincipal rolePrincipal = principalMgr.getRole(roleId);
                principalMgr.unAssignRole(userPrincipal, rolePrincipal, requestor);
                principalMgr.assignRole(userPrincipal, rolePrincipal, requestor);

                roleName = prefix + "Submit " + projectId;
                roleId = getRoleId(roleName);

                if (roleId == -1) {
                    throw new RuntimeException("Can't find roleName: " + roleName);
                }

                rolePrincipal = principalMgr.getRole(roleId);
                principalMgr.unAssignRole(userPrincipal, rolePrincipal, requestor);
                principalMgr.assignRole(userPrincipal, rolePrincipal, requestor);
            } catch (NoSuchUserException e2) {
                throw new RuntimeException(e2);
            } catch (com.topcoder.security.GeneralSecurityException e2) {
                throw new RuntimeException(e2);
            }
        } catch (SQLException e) {
            ejbContext.setRollbackOnly();
            throw new RuntimeException("SQLException: " + e.getMessage());
        } catch (RemoteException e) {
            ejbContext.setRollbackOnly();
            throw new RuntimeException("RemoteException: " + e.getMessage());
        } finally {
            Common.close(conn, ps, rs);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param roleName
     *
     * @return
     *
     * @throws RuntimeException DOCUMENT ME!
     */
    private long getRoleId(String roleName) {
        log.debug("PT.getRoleId(), roleName: " + roleName);

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        long id = -1;

        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement("SELECT role_id " + "FROM security_roles " + "WHERE description = ?");
            ps.setString(1, roleName);
            rs = ps.executeQuery();

            if (rs.next()) {
                id = rs.getLong(1);
            } else {
                id = -1;
            }
        } catch (SQLException e) {
            ejbContext.setRollbackOnly();
            throw new RuntimeException("SQLException: " + e.getMessage());
        } finally {
            Common.close(conn, ps, rs);
        }

        return id;
    }

    /**
     * DOCUMENT ME!
     *
     * @param compVersId DOCUMENT ME!
     * @param oldVersion DOCUMENT ME!
     * @param newVersion DOCUMENT ME!
     */
    public void versionRename(long compVersId, String oldVersion, String newVersion) {
        ddeRename(-1, compVersId, null, null, oldVersion, newVersion);
    }

    /**
     * DOCUMENT ME!
     *
     * @param componentId DOCUMENT ME!
     * @param oldName DOCUMENT ME!
     * @param newName DOCUMENT ME!
     */
    public void componentRename(long componentId, String oldName, String newName) {
        ddeRename(componentId, -1, oldName, newName, null, null);
    }

    private void ddeRename(long componentId, long compVersId, String oldName, String newName, String oldVersion,
        String newVersion) {
        log.debug("PT.ddeRename(), componentId: " + componentId + "compVersId: " + compVersId);

        Connection conn = null;
        PreparedStatement ps = null;
        PreparedStatement psRoles = null;
        ResultSet rs = null;

        try {
            conn = dataSource.getConnection();

            List compVersList = new LinkedList();
            List projectTypeList = new LinkedList();
            List oldVersionList = new LinkedList();
            List newVersionList = new LinkedList();

            if (compVersId == -1) {
                // given componentId
                ps = conn.prepareStatement(
                        "SELECT pi_vi.value as comp_vers_id, pi_vt.value as version_text, p.project_category_id as project_type_id " +
                        "FROM project p " + "inner join project_info pi_vi " +
                        "on p.project_id = pi_vi.project_id and pi_vi.project_info_type_id = 1 " + // external id
                        "inner join project_info pi_vt " +
                        "on p.project_id = pi_vt.project_id and pi_vt.project_info_type_id = 7 " + // project_version
                        "inner join project_info pi_ci " +
                        "on p.project_id = pi_ci.project_id and pi_ci.project_info_type_id = 2 " + // component_id
                        "where pi_ci.value = ? ");
                ps.setLong(1, componentId);
                rs = ps.executeQuery();

                while (rs.next()) {
                    long cvId = rs.getLong(1);
                    String vText = rs.getString(2).trim();
                    long projectTypeId = rs.getLong(3);

                    compVersList.add(new Long(cvId));
                    projectTypeList.add(new Long(projectTypeId));
                    oldVersionList.add(vText);
                    newVersionList.add(vText);
                }

                Common.close(rs);
                Common.close(ps);
                rs = null;
                ps = null;
            } else {
                ps = conn.prepareStatement(
                        "SELECT pi_vi.value as comp_vers_id, pi_cn.value as component_name, p.project_category_id as project_type_id " +
                        "FROM project p " + "inner join project_info pi_vi " +
                        "on p.project_id = pi_vi.project_id and pi_vi.project_info_type_id = 1 " + // external id
                        "inner join project_info pi_cn " +
                        "on p.project_id = pi_cn.project_id and pi_cn.project_info_type_id = 6 " + // component_name
                        "where pi_vi.value = ? ");

                ps.setLong(1, compVersId);
                rs = ps.executeQuery();

                long projectTypeId;

                if (rs.next()) {
                    projectTypeId = rs.getLong(1);
                    oldName = rs.getString(2);
                    newName = oldName;
                } else {
                    ps = conn.prepareStatement("SELECT 1, cc.component_name " +
                            "FROM comp_catalog cc, comp_versions cv " + "WHERE cc.component_id = cv.component_id " +
                            "AND cv.component_id in ( " + "    select component_id " + "    from comp_versions cv " +
                            "    where cv.comp_vers_id = ? " + "    ) ");

                    ps.setLong(1, compVersId);
                    rs = ps.executeQuery();

                    if (rs.next()) {
                        projectTypeId = rs.getLong(1);
                        oldName = rs.getString(2);
                        newName = oldName;
                    } else {
                        throw new RuntimeException();
                    }
                }

                Common.close(rs);
                Common.close(ps);
                rs = null;
                ps = null;

                compVersList.add(new Long(compVersId));
                projectTypeList.add(new Long(projectTypeId));
                oldVersionList.add(oldVersion);
                newVersionList.add(newVersion);
            }

            Iterator iterProjectType = projectTypeList.iterator();
            Iterator iterOldVersion = oldVersionList.iterator();
            Iterator iterNewVersion = newVersionList.iterator();

            for (Iterator iter = compVersList.iterator(); iter.hasNext();) {
                long cvId = ((Long) iter.next()).longValue();
                long ptId = ((Long) iterProjectType.next()).longValue();
                String oldV = (String) iterOldVersion.next();
                String newV = (String) iterNewVersion.next();
                log.debug("PT.ddeRename(), renaming roles for project, compVersId: " + cvId);

                ProjectTypeManager projectTypeManager = (ProjectTypeManager) Common.getFromCache("ProjectTypeManager");
                ProjectType projectType = projectTypeManager.getProjectType(ptId);

                String prefixOld = oldName + " " + oldV + " " + projectType.getName() + " ";
                String prefixNew = newName + " " + newV + " " + projectType.getName() + " ";

                // TODO Add method to security manager instead of using custom sql
                psRoles = conn.prepareStatement("UPDATE security_roles " + "SET description = ? " +
                        "WHERE description = ?");

                String newRoleName = prefixNew + "View Project";
                String oldRoleName = prefixOld + "View Project";
                psRoles.setString(1, newRoleName);
                psRoles.setString(2, oldRoleName);

                int nr = psRoles.executeUpdate();

                if (nr != 1) {
                    log.error("Could not change rolename: " + oldRoleName);

                    //throw new RuntimeException("Could not change rolename: " + oldRoleName);
                }

                newRoleName = prefixNew + "Submit";
                oldRoleName = prefixOld + "Submit";
                psRoles.setString(1, newRoleName);
                psRoles.setString(2, oldRoleName);
                nr = psRoles.executeUpdate();

                if (nr != 1) {
                    log.error("Could not change rolename: " + oldRoleName);

                    //throw new RuntimeException("Could not change rolename: " + oldRoleName);
                }

                newRoleName = prefixNew + "Screen";
                oldRoleName = prefixOld + "Screen";
                psRoles.setString(1, newRoleName);
                psRoles.setString(2, oldRoleName);
                nr = psRoles.executeUpdate();

                if (nr != 1) {
                    log.error("Could not change rolename: " + oldRoleName);

                    //throw new RuntimeException("Could not change rolename: " + oldRoleName);
                }

                newRoleName = prefixNew + "Review";
                oldRoleName = prefixOld + "Review";
                psRoles.setString(1, newRoleName);
                psRoles.setString(2, oldRoleName);
                nr = psRoles.executeUpdate();

                if (nr != 1) {
                    log.error("Could not change rolename: " + oldRoleName);

                    //throw new RuntimeException("Could not change rolename: " + oldRoleName);
                }

                newRoleName = prefixNew + "Aggregation";
                oldRoleName = prefixOld + "Aggregation";
                psRoles.setString(1, newRoleName);
                psRoles.setString(2, oldRoleName);
                nr = psRoles.executeUpdate();

                if (nr != 1) {
                    log.error("Could not change rolename: " + oldRoleName);

                    //throw new RuntimeException("Could not change rolename: " + oldRoleName);
                }

                newRoleName = prefixNew + "Submit Final Fix";
                oldRoleName = prefixOld + "Submit Final Fix";
                psRoles.setString(1, newRoleName);
                psRoles.setString(2, oldRoleName);
                nr = psRoles.executeUpdate();

                if (nr != 1) {
                    log.error("Could not change rolename: " + oldRoleName);

                    //throw new RuntimeException("Could not change rolename: " + oldRoleName);
                }

                newRoleName = prefixNew + "Final Review";
                oldRoleName = prefixOld + "Final Review";
                psRoles.setString(1, newRoleName);
                psRoles.setString(2, oldRoleName);
                nr = psRoles.executeUpdate();

                if (nr != 1) {
                    log.error("Could not change rolename: " + oldRoleName);

                    //throw new RuntimeException("Could not change rolename: " + oldRoleName);
                }

                Common.close(psRoles);
                psRoles = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            Common.close(psRoles);
            Common.close(conn, ps, rs);
        }
    }

    /**
     * This method is required by the EJB Specification. Used to get the context ... for dynamic connection pools.
     *
     * @throws CreateException if error occurs while create ejb
     */
    public void ejbCreate() throws CreateException {
        dataSource = Common.getDataSource();

        try {
            InitialContext context = new InitialContext();

            DocumentManagerLocalHome dmHome = (DocumentManagerLocalHome) javax.rmi.PortableRemoteObject.narrow(context.lookup(
                        "com.topcoder.apps.review.document.DocumentManagerLocalHome"), DocumentManagerLocalHome.class);
            documentManager = dmHome.create();

            Object o = context.lookup("idgenerator/IdGenEJB");
            IdGenHome idGenHome = (IdGenHome) PortableRemoteObject.narrow(o, IdGenHome.class);
            idGen = idGenHome.create();
        } catch (Exception e) {
            throw new CreateException("Could not find bean!" + e);
        }

        log = Logger.getLogger(ProjectTrackerV2Bean.class);

        log.debug("ProjectTrackerBean created");
    }

    /**
     * @see javax.ejb.SessionBean#ejbRemove()
     */
    public void ejbRemove() throws EJBException {
    }

    /**
     * @see javax.ejb.SessionBean#ejbActivate()
     */
    public void ejbActivate() throws EJBException {
    }

    /**
     * @see javax.ejb.SessionBean#ejbPassivate()
     */
    public void ejbPassivate() throws EJBException {
    }

    /**
     * @see javax.ejb.SessionBean#setSessionContext(javax.ejb.SessionContext)
     */
    public void setSessionContext(SessionContext ctx) throws EJBException {
        this.ejbContext = ctx;
    }
}
