/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.dataaccess;

import com.topcoder.date.workdays.DefaultWorkdaysFactory;
import com.topcoder.date.workdays.Workdays;
import com.topcoder.project.phases.Dependency;
import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.PhaseStatus;
import com.topcoder.project.phases.PhaseType;
import com.topcoder.project.phases.Project;
import com.topcoder.shared.dataAccess.resultSet.ResultSetContainer;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * <p>A simple DAO for project phases backed up by Query Tool.</p>
 *
 * <p>
 * Version 1.1 (Impersonation Login Assembly 1.0) Change notes:
 *   <ol>
 *     <li>Renamed <code>searchInactiveProjectPhases</code> method to <code>searchDraftProjectPhases</code> method.</li>
 *   </ol>
 * </p>
 * 
 * @author isv
 * @version 1.1
 */
public class ProjectPhaseDataAccess extends BaseDataAccess {

    /**
     * <p>Constructs new <code>ProjectPhaseDataAccess</code> instance. This implementation does nothing.</p>
     */
    public ProjectPhaseDataAccess() {
    }

    /**
     * <p>Gets the phases for projects of <code>Active</code> status.</p>
     *
     * @param phaseStatuses a <code>PhaseStatus</code> array listing the available phase statuses.
     * @param phaseTypes a <code>PhaseType</code> array listing the available phase types.
     * @return a <code>Map</code> mapping project IDs to the project phases.
     */
    public Map<Long, Project> searchActiveProjectPhases(PhaseStatus[] phaseStatuses, PhaseType[] phaseTypes) {
        return searchProjectPhasesByQueryTool("tcs_project_phases_by_status", "stid",
                                              String.valueOf(PROJECT_STATUS_ACTIVE_ID), phaseStatuses, phaseTypes);
    }

    /**
     * <p>Gets the phases for projects of <code>Draft</code> status.</p>
     *
     * @param phaseStatuses a <code>PhaseStatus</code> array listing the available phase statuses.
     * @param phaseTypes a <code>PhaseType</code> array listing the available phase types.
     * @return a <code>Map</code> mapping project IDs to the project phases.
     */
    public Map<Long, Project> searchDraftProjectPhases(PhaseStatus[] phaseStatuses, PhaseType[] phaseTypes) {
        return searchProjectPhasesByQueryTool("tcs_project_phases_by_status", "stid",
                                              String.valueOf(PROJECT_STATUS_DRAFT_ID), phaseStatuses, phaseTypes);
    }

    /**
     * <p>Gets the phases for projects assigned to specified user.</p>
     *
     * @param userId a <code>long</code> providing the user ID.
     * @param phaseStatuses a <code>PhaseStatus</code> array listing the available phase statuses.
     * @param phaseTypes a <code>PhaseType</code> array listing the available phase types.
     * @return a <code>Map</code> mapping project IDs to the project phases.
     */
    public Map<Long, Project> searchUserProjectPhases(long userId, PhaseStatus[] phaseStatuses,
                                                      PhaseType[] phaseTypes) {
        return searchProjectPhasesByQueryTool("tcs_project_phases_by_user", "uid",
                                              String.valueOf(userId), phaseStatuses, phaseTypes);
    }

    /**
     * <p>Gets the phases for projects of specified status.</p>
     *
     * @param queryName a <code>String</code> providing the name of the query to be run.
     * @param paramName a <code>String</code> providing the name of the query parameter for customization.
     * @param paramValue a <code>String</code> providing the value of the query parameter for customization.
     * @param phaseStatuses a <code>PhaseStatus</code> array listing the available phase statuses.
     * @param phaseTypes a <code>PhaseType</code> array listing the available phase types.
     * @return a <code>Project</code> array listing the project phases.
     * @throws DataAccessException if an unexpected error occurs while running the query via Query Tool.
     * @since 1.7
     */
    private Map<Long, Project> searchProjectPhasesByQueryTool(String queryName, String paramName, String paramValue,
                                                              PhaseStatus[] phaseStatuses, PhaseType[] phaseTypes) {

        // Build the cache of phase statuses for faster lookup by ID
        Map<Long, PhaseStatus> statusesMap = buildPhaseStatusesLookupMap(phaseStatuses);

        // Build the cache of phase types for faster lookup by ID
        Map<Long, PhaseType> typesMap = buildPhaseTypesLookupMap(phaseTypes);

        // Get project details by status using Query Tool
        Map<String, ResultSetContainer> results = runQuery(queryName, paramName, paramValue);

        // Convert returned data into Project objects
        Map<Long, Phase> cachedPhases = new HashMap<Long, Phase>();
        Map<Long, List<Object[]>> deferredDependencies = new HashMap<Long, List<Object[]>>();
        Workdays workdays = new DefaultWorkdaysFactory().createWorkdaysInstance();
        Map<Long, com.topcoder.project.phases.Project> phProjects
            = new HashMap<Long, com.topcoder.project.phases.Project>();
        com.topcoder.project.phases.Project currentPhProject = null;
        Phase currentPhase = null;
        ResultSetContainer phasesData = results.get(queryName);
        int recordNum = phasesData.size();
        for (int i = 0; i < recordNum; i++) {
            long projectId = phasesData.getLongItem(i, "project_id");
            if ((currentPhProject == null) || (currentPhProject.getId() != projectId)) {
                currentPhProject = new com.topcoder.project.phases.Project(new Date(Long.MAX_VALUE), workdays);
                currentPhProject.setId(projectId);
                phProjects.put(projectId, currentPhProject);
            }

            long phaseId = phasesData.getLongItem(i, "project_phase_id");
            if ((currentPhase == null) || (currentPhase.getId() != phaseId)) {
                currentPhase = new Phase(currentPhProject, phasesData.getLongItem(i, "duration"));
                currentPhase.setId(phaseId);
                currentPhase.setActualEndDate(phasesData.getTimestampItem(i, "actual_end_time"));
                currentPhase.setActualStartDate(phasesData.getTimestampItem(i, "actual_start_time"));
                currentPhase.setFixedStartDate(phasesData.getTimestampItem(i, "fixed_start_time"));
                currentPhase.setScheduledEndDate(phasesData.getTimestampItem(i, "scheduled_end_time"));
                currentPhase.setScheduledStartDate(phasesData.getTimestampItem(i, "scheduled_start_time"));
                currentPhase.setPhaseStatus(statusesMap.get(phasesData.getLongItem(i, "phase_status_id")));
                currentPhase.setPhaseType(typesMap.get(phasesData.getLongItem(i, "phase_type_id")));
                cachedPhases.put(phaseId, currentPhase);
                Date currentPhaseStartDate = currentPhase.getScheduledStartDate();
                Date currentProjectStartDate = currentPhProject.getStartDate();
                if (currentProjectStartDate.compareTo(currentPhaseStartDate) > 0) {
                    currentPhProject.setStartDate(currentPhaseStartDate);
                }
            }

            if (phasesData.getItem(i, "dependent_phase_id").getResultData() != null) {
                long dependencyId = phasesData.getLongItem(i, "dependency_phase_id");
                long dependentId = phasesData.getLongItem(i, "dependent_phase_id");
                long lagTime = phasesData.getLongItem(i, "lag_time");
                boolean dependencyStart = (phasesData.getIntItem(i, "dependency_start") == 1);
                boolean dependentStart = (phasesData.getIntItem(i, "dependent_start") == 1);
                if (cachedPhases.containsKey(dependencyId)) {
                    Dependency dependency = new Dependency(cachedPhases.get(dependencyId), currentPhase,
                                                           dependencyStart, dependentStart, lagTime);
                    currentPhase.addDependency(dependency);
                } else {
                    if (!deferredDependencies.containsKey(dependencyId)) {
                        deferredDependencies.put(dependencyId, new ArrayList<Object[]>());
                    }
                    deferredDependencies.get(dependencyId).add(
                        new Object[] {dependentId, lagTime, dependencyStart, dependentStart});
                }
            }
        }

        // Resolve deferred dependencies
        Iterator<Long> iterator = deferredDependencies.keySet().iterator();
        while (iterator.hasNext()) {
            Long dependencyId = iterator.next();
            List<Object[]> dependencies = deferredDependencies.get(dependencyId);
            for (Object[] dependency : dependencies) {
                long dependentId = (Long) dependency[0];
                long lagTime = (Long) dependency[1];
                boolean dependencyStart = (Boolean) dependency[2];
                boolean dependentStart = (Boolean) dependency[3];
                Phase dependentPhase = cachedPhases.get(dependentId);
                Dependency dep = new Dependency(cachedPhases.get(dependencyId), dependentPhase,
                                                dependencyStart, dependentStart, lagTime);
                dependentPhase.addDependency(dep);
            }
        }

        deferredDependencies.clear();
        cachedPhases.clear();
        statusesMap.clear();
        typesMap.clear();

        return phProjects;
    }
}
