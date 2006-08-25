package com.topcoder.dde.util.DWLoad;

import com.topcoder.shared.util.logging.Logger;
import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.config.ConfigManagerException;
import com.topcoder.util.config.UnknownNamespaceException;

import javax.naming.Context;
import java.sql.*;
import java.sql.Date;
import java.util.*;

public class ReliabilityRating {
    protected static final Logger log = Logger.getLogger(ReliabilityRating.class);

    public static final String DRIVER_KEY = "DriverClass";
    public static final String CONNECTION_URL_KEY = "ConnectionURL";
    public static final String HISTORY_LENGTH_KEY = "HistoryLength";
    public static final int MIN_PASSING_SCORE = 75;
    public static final int MIN_RELIABLE_SCORE = 75;

    /**
     * the date when the new rules go into effect
     */
    public static final Date START_DATE = getDate(2005, Calendar.OCTOBER, 5, 9, 0);
    //public static final Date START_DATE = getDate(2004, Calendar.JULY, 1, 9, 0);


    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        ReliabilityRating tmp = new ReliabilityRating();

        //Load our configuration
        String namespace = ReliabilityRating.class.getName();
        ConfigManager config = ConfigManager.getInstance();
        String historyLength;
        if (!config.existsNamespace(namespace)) {
            try {
                config.add(namespace, ConfigManager.CONFIG_XML_FORMAT);
            } catch (ConfigManagerException cme) {
                System.err.println("Couldn't add " + namespace.replace('.', '/') + ".xml to namespace");
                return;
            }
        } else {
            try {
                config.refresh(namespace);
            } catch (ConfigManagerException cme) {
                System.err.println("Exception while refreshing namespace: " + namespace);
                return;
            }
        }

        String jdbcDriver;
        String connectionURL;
        try {
            jdbcDriver = config.getString(namespace, DRIVER_KEY);
            connectionURL = config.getString(namespace, CONNECTION_URL_KEY);
            historyLength = config.getString(namespace, HISTORY_LENGTH_KEY);
            if (jdbcDriver == null) {
                System.err.println("No JDBC Driver specified.  (Config param '" + DRIVER_KEY + "')");
                return;
            }

            if (connectionURL == null) {
                System.err.println("No Connection URL specified.  (Config param '" + CONNECTION_URL_KEY + "')");
                return;
            }
        } catch (UnknownNamespaceException e) {
            System.err.println("Initialized ConfigManager and namespace '" + namespace + "' without trouble but could not retrieve resource bundle");
            return;
        }

        Connection c = null;
        try {
            Class.forName(jdbcDriver);
            c = DriverManager.getConnection(connectionURL);

            int incExMarked = tmp.markForInclusionAndExclusion(c);
            log.info(incExMarked + " records marked for inclusion/exclusion");

            Set developers = tmp.getIncludedUsers(c, 113);
            log.info("there are " + developers.size() + " developers included");

            Set designers = tmp.getIncludedUsers(c, 112);
            log.info("there are " + designers.size() + " designers included");

            int newMarked = tmp.markNewReliableResults(c);
            log.info(newMarked + " new records marked");

            int oldMarked = tmp.markOldReliableResults(c);
            log.info(oldMarked + " old records marked");

            int oldUpdated = tmp.updateOldProjectResult(c);
            log.info(oldUpdated + " old records updated");

            int designersUpdated = tmp.updateReliability(c, designers, Integer.parseInt(historyLength), 112);
            log.info(designersUpdated + " new project result designer records updated");

            int developersUpdated = tmp.updateReliability(c, developers, Integer.parseInt(historyLength), 113);
            log.info(developersUpdated + " new project result developer records updated");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (c != null) c.close();
            } catch (Exception e1) {
                log.info("exception B: " + e1);
            }
        }
        log.info("ran in " + (float) (System.currentTimeMillis() - start) / (float) 1000 + " seconds");
    }

    private static final String updateProjectResult =
            "UPDATE project_result SET old_reliability = ?, new_reliability = ?, current_reliability_ind = ? " +
                    " WHERE project_id = ? and user_id = ? ";

    private static final String updateCurrentReliability = "update project_result set current_reliability_ind = ? " +
            " WHERE project_id = ? and user_id = ? ";

    private static final String updateUserReliability =
            "update user_reliability set rating = ? where user_id = ? and phase_id = ?";

    private static final String insertUserReliability =
            "insert into user_reliability (rating, user_id, phase_id) values (?,?,?)";

    private static final String clearCurrentReliability = "update project_result set current_reliability_ind = 0 where project_id in " +
            "(select project_id from project where project_category_id+111 = ?)";

    /**
     * go through the list of users and do two things.
     * 1.  update project result with reliability information
     * 2.  update user_reliability with current data
     *
     * @param conn
     * @param users
     * @return the number of records updated/inserted
     */
    public int updateReliability(Connection conn, Set users, int historyLength, long phaseId) throws SQLException {
        //log.info("updateReliability(conn, users, " + historyLength + ", " + phaseId + ") called");
        int ret = 0;
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;
        PreparedStatement insert = null;
        PreparedStatement update = null;
        PreparedStatement clear = null;
        PreparedStatement currUpdate = null;
        ResultSet rs = null;

        try {

            ps = conn.prepareStatement(reliabilityData);
            ps2 = conn.prepareStatement(updateProjectResult);
            insert = conn.prepareStatement(insertUserReliability);
            update = conn.prepareStatement(updateUserReliability);
            currUpdate = conn.prepareStatement(updateCurrentReliability);
            clear = conn.prepareStatement(clearCurrentReliability);
            clear.setLong(1, phaseId);

            clear.executeUpdate();

            long userId;
            for (Iterator it = users.iterator(); it.hasNext();) {
                try {
                    userId = ((Long) it.next()).longValue();
                    ReliabilityHistory rh = new ReliabilityHistory(conn, userId, phaseId, historyLength);

                    ReliabilityInstance instance = null;
                    for (Iterator records = rh.getHistory(); records.hasNext();) {
                        instance = (ReliabilityInstance) records.next();
                        //log.info(" xxx " + instance.toString());
                        if (instance.isAfterStart()) {
                            ps2.clearParameters();
                            if (instance.isFirst()) {
                                ps2.setNull(1, Types.DOUBLE);
                            } else {
                                ps2.setDouble(1, instance.getRecentOldReliability());
                            }
                            ps2.setDouble(2, instance.getRecentNewReliability());
                            ps2.setInt(3, instance.isIncluded() ? 1 : 0);
                            ps2.setLong(4, instance.getProjectId());
                            ps2.setLong(5, userId);
                            ret += ps2.executeUpdate();
                        } else {
                            currUpdate.clearParameters();
                            currUpdate.setInt(1, instance.isIncluded() ? 1 : 0);
                            currUpdate.setLong(2, instance.getProjectId());
                            currUpdate.setLong(3, userId);
                            ret += currUpdate.executeUpdate();
                        }

                    }
                    //update or create a user_reliability record for everyone that is included
                    //that would be whether they are included because of the old way, or the new way
                    if (instance != null) {
                        update.clearParameters();
                        update.setDouble(1, instance.getRecentNewReliability());
                        update.setLong(2, userId);
                        update.setLong(3, phaseId);
                        int num = update.executeUpdate();
                        if (num == 0) {
                            insert.clearParameters();
                            insert.setDouble(1, instance.getRecentNewReliability());
                            insert.setLong(2, userId);
                            insert.setLong(3, phaseId);
                            insert.executeUpdate();
                        }
                        //log.info("reliability for " + userId + " set to " + instance.getRecentNewReliability());
                    }

                } finally {
                    close(rs);
                }
            }
        } finally {
            close(rs);
            close(ps);
            close(ps2);
            close(insert);
            close(update);
            close(currUpdate);
            close(clear);
        }

        return ret;
    }

    private static final String reliabilityData =
            " select pr.reliable_submission_ind" +
                    " , ci.create_time" +
                    " , pr.project_id" +
                    " , case when pi.scheduled_start_time >= ? then 1 else 0 end as after_start_flag" +
                    " from project_result pr" +
                    " , component_inquiry ci" +
                    " , phase pi" +
                    " , project p" +
                    " where ci.project_id = pr.project_id" +
                    " and pr.user_id = ci.user_id" +
                    " and pr.project_id = p.project_id" +
                    " and pr.user_id = ?" +
                    " and p.project_category_id+111 = ?" +
                    " and pr.project_id = pi.project_id" +
                    " and pi.phase_type_id = 2" + // phase type 2 is submission
                    " and pr.reliability_ind = 1" +
                    " and pr.reliable_submission_ind is not null" +
                    " order by ci.create_time asc";

    private class ReliabilityHistory {
        private List history = new ArrayList(10000);

        private ReliabilityHistory(Connection conn, long userId, long phaseId, int historyLength) throws SQLException {

            PreparedStatement ps = null;
            ResultSet rs = null;

            try {
                ps = conn.prepareStatement(reliabilityData);
                ps.setDate(1, START_DATE);
                ps.setLong(2, userId);
                ps.setLong(3, phaseId);
                rs = ps.executeQuery();
                while (rs.next()) {
                    history.add(new ReliabilityInstance(rs.getLong("project_id"),
                            userId, rs.getInt("reliable_submission_ind") == 1, rs.getInt("after_start_flag") == 1));
                }

                if (!history.isEmpty()) {
                    ((ReliabilityInstance) history.get(0)).setFirst(true);
                }

                int size = history.size();

                //set the most recent historyLength records as included in the current reliablity calculation
                for (int i = size - 1, j = 0; i >= 0 && j < historyLength; i--, j++) {
                    ((ReliabilityInstance) history.get(i)).setIncluded(true);
                }

                //calculate/populate reliabilities for the given history length. that means only incuode historyLength records
                ReliabilityInstance cur;
                double fullNewRel;
                int fullReliableCount = 0;
                for (int i = 0; i < size; i++) {
                    if (((ReliabilityInstance) history.get(i)).isReliable()) {
                        fullReliableCount++;
                    }
                    fullNewRel = (double) fullReliableCount / (double) (i + 1);

                    double newRel = 0.0d;
                    int reliableCount = 0;
                    int projectCount = 0;

                    int j = (i - historyLength + 1) < 0 ? 0 : i - historyLength + 1;
                    for (; j <= i; j++) {
                        //iterate through the records that count based on the
                        //history length and calculate the reliability information
                        projectCount++;
                        cur = (ReliabilityInstance) history.get(j);
                        if (cur.isReliable()) {
                            reliableCount++;
                        }
                        newRel = (double) reliableCount / (double) (projectCount);
                    }

                    if (i > 0) {
                        ((ReliabilityInstance) history.get(i)).setRecentOldReliability(
                                ((ReliabilityInstance) history.get(i - 1)).getRecentNewReliability());
                        ((ReliabilityInstance) history.get(i)).setOldReliability(
                                ((ReliabilityInstance) history.get(i - 1)).getNewReliability());
                    }
                    ((ReliabilityInstance) history.get(i)).setRecentNewReliability(newRel);
                    ((ReliabilityInstance) history.get(i)).setNewReliability(fullNewRel);

                }

            } finally {
                close(rs);
                close(ps);
            }

        }

        /**
         * return the history of reliability data.  it will only include
         * records that are marked as part of the reliability calculation
         *
         * @return the history
         */
        Iterator getHistory() {
            return history.iterator();
        }
    }

    private class ReliabilityInstance {
        private long projectId = 0;
        private long userId = 0;
        private boolean reliable = false;
        private boolean afterStart = false;
        private double oldReliability = 0.0d;
        private double newReliability = 0.0d;
        private double recentOldReliability = 0.0d;
        private double recentNewReliability = 0.0d;
        private boolean first = false;
        private boolean included = false;


        private ReliabilityInstance(long projectId, long userId, boolean reliable, boolean afterStart) {
            this.projectId = projectId;
            this.userId = userId;
            this.reliable = reliable;
            this.afterStart = afterStart;
        }

        public long getProjectId() {
            return projectId;
        }

        public long getUserId() {
            return userId;
        }

        public boolean isReliable() {
            return reliable;
        }

        public boolean isAfterStart() {
            return afterStart;
        }

        public double getOldReliability() {
            return oldReliability;
        }

        public void setOldReliability(double oldReliability) {
            this.oldReliability = oldReliability;
        }

        public double getRecentNewReliability() {
            return recentNewReliability;
        }

        public void setRecentNewReliability(double recentNewReliability) {
            this.recentNewReliability = recentNewReliability;
        }

        public double getNewReliability() {
            return newReliability;
        }

        public void setNewReliability(double newReliability) {
            this.newReliability = newReliability;
        }

        public double getRecentOldReliability() {
            return recentOldReliability;
        }

        public void setRecentOldReliability(double recentOldReliability) {
            this.recentOldReliability = recentOldReliability;
        }

        public boolean isFirst() {
            return first;
        }

        public void setFirst(boolean first) {
            this.first = first;
        }

        public boolean isIncluded() {
            return included;
        }

        public void setIncluded(boolean included) {
            this.included = included;
        }

        public String toString() {
            StringBuffer buf = new StringBuffer(1000);
            buf.append(projectId);
            buf.append(" ");
            buf.append(userId);
            buf.append(" ");
            buf.append(reliable);
            buf.append(" ");
            buf.append(afterStart);
            buf.append(" ");
            buf.append(oldReliability);
            buf.append(" ");
            buf.append(newReliability);
            buf.append(" ");
            buf.append(recentOldReliability);
            buf.append(" ");
            buf.append(recentNewReliability);
            buf.append(" ");
            buf.append(first);
            buf.append(" ");
            buf.append(included);
            return buf.toString();


        }

    }

    //
    private static final String oldReliabilityData =
            " select pr.reliable_submission_ind" +
                    " , ci.create_time" +
                    " , pr.project_id" +
                    " from project_result pr" +
                    " , component_inquiry ci" +
                    " , phase pi" +
                    " where ci.project_id = pr.project_id" +
                    " and pr.user_id = ci.user_id" +
                    " and pr.user_id = ?" +
                    " and pi.phase_type_id = 2" +
                    " and pi.scheduled_start_time < ?" +
                    " and pi.project_id = pr.project_id" +
                    " and pr.reliability_ind = 1" +
                    " and pr.reliable_submission_ind is not null" +
                    " order by ci.create_time asc";

    //all the people that became part of the reliability process prior to the change date
    private static final String oldReliabilityUsers =
            " select distinct pr.user_id" +
                    " from project_result pr" +
                    " , phase pi" +
                    " where pi.phase_type_id = 2" +
                    " and pi.scheduled_start_time < ?" +
                    " and pi.project_id = pr.project_id" +
                    " and pr.reliable_submission_ind is not null" +
                    " and pr.reliability_ind = 1";

    /**
     * calculate and set all the reliability information for projects before the change date
     * <p/>
     * this can be sped up if there is a speed issue.  we'll need to trim what is gettig updated
     * there is no reason to update all this old data repeatedly.
     *
     * @param conn
     * @return
     * @throws SQLException
     */
    public int updateOldProjectResult(Connection conn) throws SQLException {
        int ret = 0;
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;
        PreparedStatement ps3 = null;
        ResultSet rs = null;
        ResultSet rs3 = null;

        try {

            ps = conn.prepareStatement(oldReliabilityData);
            ps2 = conn.prepareStatement(updateProjectResult);
            ps3 = conn.prepareStatement(oldReliabilityUsers);
            ps3.setDate(1, START_DATE);
            rs3 = ps3.executeQuery();
            long userId;
            while (rs3.next()) {
                try {
                    userId = rs3.getLong("user_id");
                    ps.clearParameters();
                    ps.setLong(1, userId);
                    ps.setDate(2, START_DATE);
                    rs = ps.executeQuery();
                    int projectCount = 0;
                    int reliableCount = 0;
                    double oldReliability;
                    double newReliability = 0.0d;
                    while (rs.next()) {
                        projectCount++;
                        reliableCount += rs.getInt("reliable_submission_ind");
                        oldReliability = newReliability;
                        newReliability = (double) reliableCount / (double) projectCount;
                        ps2.clearParameters();
                        //if it's their first project,then old is null
                        if (projectCount > 1) {
                            ps2.setDouble(1, oldReliability);
                        } else {
                            ps2.setNull(1, Types.DOUBLE);
                        }
                        ps2.setDouble(2, newReliability);
                        ps2.setInt(3, 0);
                        ps2.setLong(4, rs.getLong("project_id"));
                        ps2.setLong(5, userId);
                        ret += ps2.executeUpdate();
                    }
                } finally {
                    close(rs);
                }
            }
        } finally {
            close(rs3);
            close(ps);
            close(ps2);
            close(ps3);
        }

        return ret;
    }


    /**
     * this first query is for projects before our reliability rule change.
     * in this case, anyone that has made a submission where reliabilty was in effect
     * will have a reliability rating.  the second query is for the new way.
     * in this case, anyone that has scored over the minimum review
     * score will be included.
     * <p/>
     * this is the list of people that will have a reliability rating in user_reliability
     * that may not be the same list as the list of people who have a non-empty reliability
     * history.  if people submitted prior to the new rules and were not successful, those
     * submissions will count against them the next time they submit.
     */
    private static final String includedUsers =
            " select pr.user_id" +
                    " from project_result pr" +
                    " , phase pi" +
                    " , project p" +
                    " where pr.project_id = pi.project_id" +
                    " and pi.phase_type_id = 2" +
                    " and pi.scheduled_start_time < ?" +
                    " and pr.reliability_ind = 1" +
                    " and pr.project_id = p.project_id" +
                    " and p.project_category_id+111=?" +
                    " union" +
                    " select pr.user_id" +
                    " from project_result pr" +
                    " , phase pi" +
                    " , project p" +
                    " where pr.project_id = pi.project_id" +
                    " and pi.phase_type_id = 2" +
                    " and pi.scheduled_start_time >= ?" +
                    " and pr.reliability_ind = 1" +
                    " and pr.final_score >= ?" +
                    " and pr.project_id = p.project_id" +
                    " and p.project_category_id+111=?";

    private Set getIncludedUsers(Connection conn, long phaseId) throws SQLException {

        PreparedStatement ps = null;
        ResultSet rs = null;
        HashSet ret = new HashSet();

        try {

            ps = conn.prepareStatement(includedUsers);
            ps.setDate(1, START_DATE);
            ps.setLong(2, phaseId);
            ps.setDate(3, START_DATE);
            ps.setInt(4, MIN_PASSING_SCORE);
            ps.setLong(5, phaseId);
            rs = ps.executeQuery();

            while (rs.next()) {
                ret.add(new Long(rs.getLong("user_id")));
            }
        } finally {
            close(rs);
            close(ps);
        }
        return ret;
    }


    private static final String getNewRecordsToMark =
            " select pr.user_id" +
                    " , pr.project_id" +
                    " , pr.final_score" +
                    " from project_result pr" +
                    " , phase pi" +
                    " where pr.project_id = pi.project_id" +
                    " and pi.phase_type_id = 2" +
                    " and pi.scheduled_start_time >= ?" +
                    " and pr.reliability_ind = 1" +
                    " and pr.reliable_submission_ind is null";

    private static final String updateReliableSubmission =
            "update project_result set reliable_submission_ind = ?" +
                    "where user_id = ? and project_id = ?";

    /**
     * mark all the project result records after the change date
     * as reliable or not reliable as appropriate.
     * <p/>
     * that means mark everyone that did a project that started
     * after the change date, that has a final score populated
     * that is greater than or equal to the min reliability score,
     * that should be included inthe calc (reliability_ind)
     * and has reliable_submission_ind flag that is null set the
     * reliable_submission_ind flag to 1.  if the record
     * meets three of those criteria but scores less than the min
     * reliable score, then set to 0.
     *
     * @return the number of records marked
     */
    private int markNewReliableResults(Connection conn) throws SQLException {
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;
        int ret = 0;
        try {

            ps = conn.prepareStatement(getNewRecordsToMark);
            ps2 = conn.prepareStatement(updateReliableSubmission);
            ps.setDate(1, START_DATE);
            rs = ps.executeQuery();
            while (rs.next()) {
                ps2.clearParameters();

                int reliableSubmissionInd = 0;
                if (rs.getObject("final_score") != null && Double.compare(rs.getDouble("final_score"), MIN_RELIABLE_SCORE) >= 0)
                {
                    reliableSubmissionInd = 1;
                }

                ps2.setInt(1, reliableSubmissionInd);
                ps2.setLong(2, rs.getLong("user_id"));
                ps2.setLong(3, rs.getLong("project_id"));
                ret += ps2.executeUpdate();
            }

        } finally {
            close(rs);
            close(ps);
            close(ps2);
        }
        return ret;

    }

    private static final String getOldRecordsToMark =
            " select pr.user_id" +
                    " , pr.project_id" +
                    " , pr.valid_submission_ind" +
                    " from project_result pr" +
                    " , phase pi" +
                    " where pr.project_id = pi.project_id" +
                    " and pi.phase_type_id = 2" +
                    " and pi.scheduled_start_time < ?" +
                    " and pr.reliability_ind = 1" +
                    " and pr.reliable_submission_ind is null";

    /**
     * mark all the project result records before the change date
     * as reliable or not reliable as appropriate.
     * <p/>
     * that means mark everyone that did a project that started
     * before the change date that should be included in the calculation
     * to 1 if it's a valid submission and 0 if not.
     *
     * @param conn
     * @return the number of records marks
     */
    private int markOldReliableResults(Connection conn) throws SQLException {
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;
        int ret = 0;
        try {

            ps = conn.prepareStatement(getOldRecordsToMark);
            ps2 = conn.prepareStatement(updateReliableSubmission);
            ps.setDate(1, START_DATE);
            rs = ps.executeQuery();
            while (rs.next()) {
                ps2.clearParameters();
                ps2.setInt(1, rs.getInt("valid_submission_ind"));
                ps2.setLong(2, rs.getLong("user_id"));
                ps2.setLong(3, rs.getLong("project_id"));
                ret += ps2.executeUpdate();
            }
        } finally {
            close(rs);
            close(ps);
            close(ps2);
        }
        return ret;

    }


    private final static String markIncluded =
            "update project_result " +
                    "set reliability_ind = 1 " +
                    "where reliability_ind is null " +
                    "and final_score >= ?";

    private final static String getUnmarked =
            "select pr.user_id, pr.project_id, p.project_category_id, ci.create_time " +
                    " from project_result pr " +
                    " , project p " +
                    " , component_inquiry ci " +
                    "where ((pr.final_score is not null " +
                    "and pr.final_score < ?) " +
                    "or (pr.final_score is null and p.project_status_id in (3,4,5,6,7))) " +
                    "and pr.reliability_ind is null  " +
                    "and pr.project_id = p.project_id " +
                    "and ci.project_id = pr.project_id " +
                    "and ci.user_id = pr.user_id " +
                    " order by ci.create_time";

    private final static String setReliability =
            "update project_result set reliability_ind = ? where project_id = ? and user_id = ?";

    /**
     * mark all the records that should be included in the reliability calculations
     * <p/>
     * NOTE:  nothing will be marked if it is not currently unmarked, meaning
     * it is unmarked if reliability_ind = null
     * <p/>
     * the simple case is when they score at least the min passing score.  in this
     * case, they get marked for inclusion.
     * <p/>
     * it gets trickier for those that have not reached the min passing score.  in
     * that case, if they have a prior project that is included, then this project
     * will also be included.  however, if all prior projects (based on register date)
     * have not been included this this one should not be included either.
     *
     * @param conn
     * @return how many records we marked
     */
    private int markForInclusionAndExclusion(Connection conn) throws SQLException {


        PreparedStatement ps = null;
        PreparedStatement ps2 = null;
        PreparedStatement ps3 = null;
        ResultSet unmarked = null;
        int ret = 0;
        try {

            //mark the easy ones..people that scored over the min
            ps = conn.prepareStatement(markIncluded);
            ps.setInt(1, MIN_PASSING_SCORE);
            ret = ps.executeUpdate();


            ps2 = conn.prepareStatement(getUnmarked);
            ps2.setInt(1, MIN_PASSING_SCORE);
            unmarked = ps2.executeQuery();

            ps3 = conn.prepareStatement(setReliability);
            while (unmarked.next()) {
                ps3.clearParameters();
                ps3.setLong(2, unmarked.getLong("project_id"));
                ps3.setLong(3, unmarked.getLong("user_id"));
                int[] info = getPriorProjects(conn, unmarked.getLong("user_id"), unmarked.getLong("project_id"), unmarked.getInt("project_type_id"));
                if (info[RELIABLE_COUNT_IDX] > 0) {
                    //if they have previously had projects that were reliable, then this one counts
                    ps3.setInt(1, 1);
                    ret += ps3.executeUpdate();
                } else if (info[PROJECT_COUNT_IDX] == info[MARKED_COUNT_IDX]) {
                    //if all prior projects are not included in reliability, this one shouldn't either
                    ps3.setInt(1, 0);
                    ret += ps3.executeUpdate();
                } else {
                    //we don't know enough yet to mark them as either included or excluded.  basically, they have at least
                    //one project prior to this one that isn't complete, so we can't decide on this one yet.
                    //log.info("got " + info[RELIABLE_COUNT_IDX] + " " + info[PROJECT_COUNT_IDX] + " " + info[MARKED_COUNT_IDX] + " " + unmarked.getLong("user_id") + " " + unmarked.getLong("project_id"));
                }
            }
        } finally {
            close(unmarked);
            close(ps);
            close(ps2);
            close(ps3);
        }
        return ret;


    }

    private static final String priorProjects =
            "select pr.reliability_ind, pr.project_id, pr.user_id " +
                    "from component_inquiry ci " +
                    ", project_result pr " +
                    ", project p " +
                    "where ci.user_id = ? " +
                    "and p.project_id = pr.project_id " +
                    "and pr.user_id = ci.user_id " +
                    "and p.project_category_id = ? " +
                    "and pr.project_id = ci.project_id " +
                    "and ci.create_time < (select create_time " +
                    "from component_inquiry " +
                    "where user_id = ci.user_id " +
                    "and project_id = ?)";

    private static final int RELIABLE_COUNT_IDX = 0;
    private static final int PROJECT_COUNT_IDX = 1;
    private static final int MARKED_COUNT_IDX = 2;

    private int[] getPriorProjects(Connection conn, long userId, long projectId, int projectTypeId) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        int[] ret = new int[3];
        Arrays.fill(ret, 0);
        try {

            ps = conn.prepareStatement(priorProjects);
            ps.setLong(1, userId);
            ps.setInt(2, projectTypeId);
            ps.setLong(3, projectId);
            rs = ps.executeQuery();
            while (rs.next()) {
                ret[PROJECT_COUNT_IDX]++;
                ret[RELIABLE_COUNT_IDX] += rs.getInt("reliability_ind");
                if (rs.getString("reliability_ind") != null) {
                    ret[MARKED_COUNT_IDX]++;
                }
            }
        } finally {
            close(rs);
            close(ps);
        }
        return ret;
    }

    private static Date getDate(int year, int month, int day, int hour, int minute) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        return new Date(cal.getTime().getTime());
    }


    protected void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (Exception ignore) {
                ignore.printStackTrace();
            }
        }
    }

    protected void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (Exception ignore) {
                ignore.printStackTrace();
            }
        }

    }

    protected void close(Context ctx) {
        if (ctx != null) {
            try {
                ctx.close();
            } catch (Exception ignore) {
                ignore.printStackTrace();
            }
        }

    }

    protected void close(PreparedStatement ps) {
        if (ps != null) {
            try {
                ps.close();
            } catch (Exception ignore) {
                ignore.printStackTrace();
            }
        }

    }


}