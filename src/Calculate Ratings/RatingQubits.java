package com.topcoder.dde.util.DWLoad;

import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.config.ConfigManagerException;
import com.topcoder.util.config.UnknownNamespaceException;

import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Vector;

//import com.topcoder.netCommon.contest.*;
//import com.topcoder.server.common.*;

public class RatingQubits {

    public static final String DRIVER_KEY = "DriverClass";
    public static final String CONNECTION_URL_KEY = "ConnectionURL";
    public static final String HISTORY_LENGTH_KEY = "HistoryLength";

    public static void main(String[] args) {
        int numArgs = args.length;
        boolean isFinal = true;
        boolean runAll = false;
        RatingQubits tmp = new RatingQubits();

        //Load our configuration
        String namespace = RatingQubits.class.getName();
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

            c.setAutoCommit(true);
            tmp.runAllScores(c, historyLength);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (c != null) c.close();
            } catch (Exception e1) {
                System.out.println("exception B: " + e1);
            }
        }
    }

    public void runAllScores(Connection conn, String historyLength) {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            /*
            //nullout existing ratings
            String sqlStr = "update project_result set old_rating = null, new_rating = null";
            ps = conn.prepareStatement(sqlStr);
            ps.execute();
        */
            //design
            String sqlStr = "select distinct select pr.project_id, to_date(pi_vd.value, '%M/%d/%Y %H:%M') as ProjectDate, " +
                    "pi_vi.value as comp_vers_id " +
                    "p.comp_vers_id " +
                    "from project_result pr, project p, project_info pi_vi, project_info pi_rd, outer comp_version_dates cd " +
                    "where p.project_id = pr.project_id " +
                    "and p.project_status_id in (3, 4, 5, 6, 7)  " +
                    "and p.project_category_id = 1 " +
                    "and pr.rating_ind = 1 " +
                    "and pi_vi.project_id = pr.project_id  " +
                    "and pi_vi.project_info_type_id = 1 " +
                    "and pi_rd.project_id = p.project_id and pi_rd.project_info_type_id = 22 " +
                    "and cd.comp_vers_id = pi_vi.value and cd.phase_id = 112 " +
                    "order by 2,1";

            ps = conn.prepareStatement(sqlStr);
            rs = ps.executeQuery();

            this.rateProjects(conn, rs, 112, historyLength);

            rs.close();
            rs = null;
            ps.close();
            ps = null;

            //dev

            sqlStr = "select distinct select pr.project_id, to_date(pi_vd.value, '%M/%d/%Y %H:%M') as ProjectDate, " +
		            "pi_vi.value as comp_vers_id " +
		            "p.comp_vers_id " +
		            "from project_result pr, project p, project_info pi_vi, project_info pi_rd, outer comp_version_dates cd " +
		            "where p.project_id = pr.project_id " +
		            "and p.project_status_id in (3, 4, 5, 6, 7)  " +
		            "and p.project_category_id = 2 " +
		            "and pr.rating_ind = 1 " +
		            "and pi_vi.project_id = pr.project_id  " +
		            "and pi_vi.project_info_type_id = 1 " +
		            "and pi_rd.project_id = p.project_id and pi_rd.project_info_type_id = 22 " +
		            "and cd.comp_vers_id = pi_vi.value and cd.phase_id = 112 " +
		            "order by 2,1";


            ps = conn.prepareStatement(sqlStr);
            rs = ps.executeQuery();

            this.rateProjects(conn, rs, 113, historyLength);

            rs.close();
            rs = null;
            ps.close();
            ps = null;
        } catch (SQLException sqe) {
            sqe.printStackTrace();
        } catch (Exception sqe) {
            sqe.printStackTrace();
        } finally {
            if (rs != null)
                try {
                    rs.close();
                } catch (Exception e) {
                    System.err.println("could not close");
                }
            if (ps != null)
                try {
                    ps.close();
                } catch (Exception e) {
                    System.err.println("could not close");
                }
            ps = null;
            rs = null;
        }

    }

    private class rating {
        public long user_id;
        public double rating;
        public int num_ratings;
        public double vol;
        public double rating_no_vol;
        public int last_project_rated = 0;

        public rating(long user_id, double rating, int num_ratings, double vol, double rating_no_vol) {
            this.user_id = user_id;
            this.rating = rating;
            this.num_ratings = num_ratings;
            this.vol = vol;
            this.rating_no_vol = rating_no_vol;
        }
    }

    private class history {
        public long user_id;
        public double score;
        public double rating;
        public double vol;

        public history(long user_id, double score, double rating, double vol) {
            this.user_id = user_id;
            this.score = score;
            this.rating = rating;
            this.vol = vol;
        }
    }

    private void rateProjects(Connection conn, ResultSet rs, int phaseId, String historyLength) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs2 = null;
        StringBuffer sqlStr = new StringBuffer(400);
        int i,levelId;
        ArrayList resultsplusprov;
        Vector ratingsplusprov,namesplusprov,volatilitiesplusprov,timesplayedplusprov,scoresplusprov;

        Vector names, endratings, endvols, endratingsnovol, scores;

        levelId = -1;

        try {
            TreeMap ratings = new TreeMap(); //contains all rating info
            ArrayList histories = new ArrayList();

            //clear history
            histories = new ArrayList();

            for (i = 0; i < Integer.parseInt(historyLength); i++) {
                histories.add(null);
            }

            while (rs.next()) {
                /*if(rs.getInt("level_id") != levelId)
                {
                    //new level
                    levelId = rs.getInt("level_id");
                    System.out.println("Processing new level: " + levelId);

                    //clear history
                    histories = new ArrayList();

                    for(i = 0; i < Integer.parseInt(historyLength); i++)
                    {
                        histories.add(null);
                    }
                } */

                //new project
                int processed = 0;
                namesplusprov = new Vector();
                ratingsplusprov = new Vector();
                volatilitiesplusprov = new Vector();
                timesplayedplusprov = new Vector();
                scoresplusprov = new Vector();

                names = new Vector();
                endratings = new Vector();
                endvols = new Vector();
                endratingsnovol = new Vector();
                scores = new Vector();


                //get all submissions for this project
                sqlStr.replace(0, sqlStr.length(), "SELECT * ");
                sqlStr.append(" FROM project_result");
                sqlStr.append(" WHERE project_id = ? and rating_ind = 1");

                ps = conn.prepareStatement(sqlStr.toString());
                ps.setLong(1, rs.getLong("project_id"));
                rs2 = ps.executeQuery();

                while (rs2.next()) {
                    processed++;

                    namesplusprov.add("" + rs2.getLong("user_id"));
                    scoresplusprov.add(new Double(rs2.getDouble("final_score")));

                    if (!ratings.containsKey("" + rs2.getLong("user_id"))) {
                        rating r = new rating(rs2.getLong("user_id"), initialScore, 0, initialVolatility, initialScore);
                        ratings.put("" + rs2.getLong("user_id"), r);
                    }

                    rating r = (rating) ratings.get("" + rs2.getLong("user_id"));
                    ratingsplusprov.add(new Double(r.rating));
                    timesplayedplusprov.add(new Integer(r.num_ratings));
                    volatilitiesplusprov.add(new Double(r.vol));
                }

                rs2.close();
                rs2 = null;
                ps.close();
                ps = null;

                if (processed == 0) {
                    System.out.println("PROCESSING ERROR: NO RECORDS FOR PROJECT " + rs.getLong("project_id"));
                }

                System.out.println("Running ratings for project: " + rs.getLong("project_id") + " (" + processed + " ratings)");

                Vector n = new Vector();
                Vector er = new Vector();
                Vector ev = new Vector();
                Vector ernv = new Vector();
                Vector s = new Vector();

                for (int x = 0; x < namesplusprov.size(); x++) {
                    //lookup user info
                    String userId = (String) namesplusprov.get(x);
                    System.out.println("RATING " + userId);

                    Vector npp = new Vector();
                    Vector tppprov = new Vector();
                    Vector spp = new Vector();
                    Vector rpp = new Vector();
                    Vector vpp = new Vector();

                    for (i = 0; i < namesplusprov.size(); i++) {
                        if (((String) namesplusprov.get(i)).equals(userId)) {
                            npp.add(namesplusprov.get(i));
                        } else {
                            npp.add("0");
                        }
                        tppprov.add(timesplayedplusprov.get(i));
                        spp.add(scoresplusprov.get(i));
                        rpp.add(ratingsplusprov.get(i));
                        vpp.add(volatilitiesplusprov.get(i));
                    }

                    processed = 0;
                    for (i = 0; i < histories.size() && processed < Integer.parseInt(historyLength); i++) {
                        history h = (history) histories.get(i);
                        if (h == null) {
                            continue;
                        }
                        if (h.user_id == Integer.parseInt(userId)) {
                            System.out.println("THROWING OUT SAME USER");
                            continue;
                        }

                        processed++;

                        npp.add("0");
                        tppprov.add(new Integer(1));
                        spp.add(new Double(h.score));
                        vpp.add(new Double(h.vol));
                        rpp.add(new Double(h.rating));
                    }

                    System.out.println("History length is " + processed);

                    resultsplusprov = rateEvent(npp, rpp, vpp, tppprov, spp);

                    names = (Vector) resultsplusprov.get(3);
                    endratings = (Vector) resultsplusprov.get(2);
                    endvols = (Vector) resultsplusprov.get(1);
                    endratingsnovol = (Vector) resultsplusprov.get(0);
                    scores = (Vector) resultsplusprov.get(4);

                    for (i = 0; i < endratings.size(); i++) {
                        n.add(names.get(i));
                        s.add(scores.get(i));
                        ev.add(endvols.get(i));
                        ernv.add(endratingsnovol.get(i));
                        er.add(endratings.get(i));
                    }

                }

                while (er.size() > 0) {
                    int newrating = Math.round(((Double) er.remove(0)).floatValue());
                    int newratingnovol = Math.round(((Double) ernv.remove(0)).floatValue());
                    int newvol = Math.round(((Double) ev.remove(0)).floatValue());
                    int coder = (new Integer(n.remove(0).toString())).intValue();
                    double score = ((Double) s.remove(0)).floatValue();

                    rating r = (rating) ratings.get("" + coder);

                    //update project_result record with new and old rating
                    sqlStr.replace(0, sqlStr.length(), "UPDATE project_result SET old_rating = ?, new_rating = ? ");
                    sqlStr.append(" WHERE project_id = ? and user_id = ? ");

                    ps = conn.prepareStatement(sqlStr.toString());
                    if (r.num_ratings==0) {
                        ps.setNull(1, Types.DOUBLE);
                    } else {
                        ps.setDouble(1, r.rating);
                    }
                    ps.setInt(2, newrating);
                    ps.setInt(3, rs.getInt("project_id"));
                    ps.setInt(4, coder);

                    ps.execute();
                    ps.close();
                    ps = null;

                    //update user_rating
                    r.rating = newrating;
                    r.vol = newvol;
                    r.rating_no_vol = newratingnovol;
                    r.num_ratings++;
                    r.last_project_rated = rs.getInt("project_id");

                    ratings.put("" + coder, r);

                    //insert history
                    history h = new history(coder, score, newrating, newvol);
                    System.out.println("HISTORY UPDATE: " + coder + ", " + score + ", " + newrating + ", " + newvol);

                    //rotate history
                    histories.add(0, h);
                    //Collections.rotate(histories, 1);
                    //histories.set(0, h);

                    System.out.println("HISTORY IS NOW " + histories.size());
                }

            }

            //commit final ratings to DB
            Object[] vals = ratings.values().toArray();
            for (i = 0; i < vals.length; i++) {
                rating r = (rating) vals[i];

                //System.out.println(r.user_id + "\t" + r.rating);

                sqlStr.replace(0, sqlStr.length(), "UPDATE user_rating set rating = ?, vol = ?, rating_no_vol = ?, last_rated_project_id = ?, num_ratings = ?, mod_date_time = CURRENT ");
                sqlStr.append(" where phase_id = ? and user_id = ?");

                ps = conn.prepareStatement(sqlStr.toString());
                ps.setDouble(1, r.rating);
                ps.setDouble(2, r.vol);
                ps.setDouble(3, r.rating_no_vol);
                ps.setInt(4, r.last_project_rated);
                ps.setInt(5, r.num_ratings);
                ps.setInt(6, phaseId);
                ps.setDouble(7, r.user_id);

                int retVal = ps.executeUpdate();

                ps.close();
                ps = null;

                if (retVal == 0) {

                    sqlStr.replace(0, sqlStr.length(), "INSERT INTO user_rating (user_id, phase_id, rating, vol, rating_no_vol, last_rated_project_id, num_ratings, mod_date_time, create_date_time) ");
                    sqlStr.append(" values (?, ?, ?, ?, ?, ?, ?, CURRENT, CURRENT )");
                    ps = conn.prepareStatement(sqlStr.toString());
                    ps.setDouble(1, r.user_id);
                    ps.setInt(2, phaseId);
                    ps.setDouble(3, r.rating);
                    ps.setDouble(4, r.vol);
                    ps.setDouble(5, r.rating_no_vol);
                    ps.setInt(6, r.last_project_rated);
                    ps.setInt(7, r.num_ratings);

                    ps.execute();

                    ps.close();
                    ps = null;
                }

            }
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception ignore) {
            }
            try {
                if (rs2 != null) {
                    rs2.close();
                }
            } catch (Exception ignore) {
            }

        }

    }

    int STEPS = 100;
    double initialScore = 1200.0;
    double oneStdDevEquals = 1200.0; /* rating points */
    double initialVolatility = 515;
    double firstVolatility = 385;
    double initialWeight = 0.60;
    double finalWeight = 0.18;
    double volatilityWeight = 0;
    double matchStdDevEquals = 500.0;   /* rating points */
    int people = 0;
    double sqiv = 0.0;
    int people2 = 0;
    double sqfv = 0.0;
    double fv = 0.0;
    int people3 = 0;
    double sb = 0.0;
    int people4 = 0;
    double sqdf = 0.0;

    private ArrayList rateEvent(Vector names, Vector ratings, Vector volatilities, Vector timesPlayed, Vector scores) {
        //prov = false

        Vector eranks = new Vector();
        Vector eperf = new Vector();
        Vector ranks = new Vector();
        Vector perf = new Vector();
        Vector newVolatility = new Vector();
        Vector newRating = new Vector();
        Vector newRatingWithVol = new Vector();
        Vector newScores = new Vector();
        int i, j;
        double aveVol = 0,rating,vol, score;

        /* COMPUTE AVERAGE RATING */
        double rave = 0.0;
        for (i = 0; i < ratings.size(); i++) {
            rave += ((Double) ratings.elementAt(i)).doubleValue();
        }
        rave /= ratings.size();

        /* COMPUTE COMPETITION FACTOR */
        double rtemp = 0, vtemp = 0;
        for (i = 0; i < ratings.size(); i++) {
            vtemp += sqr(((Double) volatilities.elementAt(i)).doubleValue());
            rtemp += sqr(((Double) ratings.elementAt(i)).doubleValue() - rave);
        }
        if (ratings.size() > 1)
            matchStdDevEquals = Math.sqrt(vtemp / ratings.size() + rtemp / (ratings.size() - 1));
        else
            matchStdDevEquals = 0;
        /* COMPUTE EXPECTED RANKS */
        for (i = 0; i < names.size(); i++) {
            ranks.addElement(new Double(0));
            perf.addElement(new Double(0));
            double est = 0.5;
            double myskill = (((Double) ratings.elementAt(i)).doubleValue() - initialScore) / oneStdDevEquals;
            double mystddev = ((Double) volatilities.elementAt(i)).doubleValue() / oneStdDevEquals;
            for (j = 0; j < names.size(); j++) {
                est += winprobability(myskill, mystddev, (((Double) ratings.elementAt(j)).doubleValue() - initialScore) / oneStdDevEquals, ((Double) volatilities.elementAt(j)).doubleValue() / oneStdDevEquals);
            }
            eranks.addElement(new Double(est));
            eperf.addElement(new Double(-normsinv((est - .5) / names.size())));
        }

        /* COMPUTE ACTUAL RANKS */
        for (i = 0; i < names.size();) {
            double max = Double.NEGATIVE_INFINITY;
            int count = 0;

            for (j = 0; j < names.size(); j++) {
                if (((Double) scores.elementAt(j)).doubleValue() >= max && ((Double) ranks.elementAt(j)).doubleValue() == 0) {
                    if (((Double) scores.elementAt(j)).doubleValue() == max)
                        count++;
                    else
                        count = 1;
                    max = ((Double) scores.elementAt(j)).doubleValue();
                }
            }
            for (j = 0; j < names.size(); j++) {
                if (((Double) scores.elementAt(j)).doubleValue() == max) {
                    ranks.setElementAt(new Double(i + 0.5 + count / 2.0), j);
                    perf.setElementAt(new Double(-normsinv((i + count / 2.0) / names.size())), j);
                }
            }
            i += count;
        }

        //clear out history entries at this point
        for (i = 0; i < names.size(); i++) {
            if ("0".equals((String) names.elementAt(i))) {
                names.remove(i);
                timesPlayed.remove(i);
                ratings.remove(i);
                volatilities.remove(i);
                eranks.remove(i);
                eperf.remove(i);
                scores.remove(i);
                ranks.remove(i);
                perf.remove(i);
                i--;
            }
        }


        /* UPDATE RATINGS */
        for (i = 0; i < names.size(); i++) {
            double diff = ((Double) perf.elementAt(i)).doubleValue() - ((Double) eperf.elementAt(i)).doubleValue();
            sqdf += diff * diff;
            people4++;
            double oldrating = ((Double) ratings.elementAt(i)).doubleValue();
            double performedAs = oldrating + diff * matchStdDevEquals;
            double weight = (initialWeight - finalWeight) / (((Integer) timesPlayed.elementAt(i)).intValue() + 1) + finalWeight;

            //get weight - reduce weight for highly rated people
            weight = 1 / (1 - weight) - 1;
            if (oldrating >= 2000 && oldrating < 2500) weight = weight * 4.5 / 5.0;
            if (oldrating >= 2500) weight = weight * 4.0 / 5.0;

            double newrating = (oldrating + weight * performedAs) / (1 + weight);

            //get and inforce a cap
            double cap = 150 + 1500 / (2 + ((Integer) timesPlayed.elementAt(i)).intValue());
            if (oldrating - newrating > cap) newrating = oldrating - cap;
            if (newrating - oldrating > cap) newrating = oldrating + cap;
            if (newrating < 1) newrating = 1;

            newRating.addElement(new Double(newrating));

            if (((Integer) timesPlayed.elementAt(i)).intValue() != 0) {
                if (((Integer) timesPlayed.elementAt(i)).intValue() == 1) {
                    fv += (performedAs - oldrating);
                    sqfv += (performedAs - oldrating) * (performedAs - oldrating);
                    people2++;
                } else {
                    sb += (performedAs - oldrating) * (performedAs - oldrating);
                    people3++;
                }
                double oldVolatility = ((Double) volatilities.elementAt(i)).doubleValue();
                newVolatility.addElement(new Double(Math.sqrt((oldVolatility*oldVolatility) / (1+weight) + ((newrating-oldrating)*(newrating-oldrating))/ weight)));
            } else {
                sqiv += (performedAs - oldrating) * (performedAs - oldrating);
                people++;
                newVolatility.addElement(new Double(firstVolatility));
            }
        }
        aveVol = 385;

        for (i = 0; i < newVolatility.size(); i++) {
            rating = ((Double) newRating.elementAt(i)).doubleValue();
            vol = ((Double) newVolatility.elementAt(i)).doubleValue();
            score = ((Double) scores.elementAt(i)).doubleValue();
            newRatingWithVol.addElement(new Double(rating + volatilityWeight * (aveVol - vol)));
            newScores.addElement(new Double(score));
        }

        System.out.println("Handle   Player  # Rate Vol Es.R E.SD  Score  Ac.R A.SD D.SD N.RT N.V N.VR");
        for (i = 0; i < names.size(); i++) {
            System.out.println(
                    fS1((String) names.elementAt(i)) + " " +
                    ((String) names.elementAt(i)) + "  " +
                    ((Integer) timesPlayed.elementAt(i)).intValue() + " " +
                    in4((Double) ratings.elementAt(i)) + " " +
                    ((Double) volatilities.elementAt(i)).intValue() + " " +
                    rat((Double) eranks.elementAt(i)) + " " +
                    fD2((Double) eperf.elementAt(i)) + " " +
                    scr((Double) scores.elementAt(i)) + " " +
                    rat((Double) ranks.elementAt(i)) + " " +
                    fD2((Double) perf.elementAt(i)) + " " +
                    fD2(new Double(((Double) perf.elementAt(i)).doubleValue() -
                    ((Double) eperf.elementAt(i)).doubleValue())) +
                    " " + in4((Double) newRating.elementAt(i)) + " " +
                    ((Double) newVolatility.elementAt(i)).intValue()
                    + " " + in4((Double) newRatingWithVol.elementAt(i))
            );
        }
        ArrayList al = new ArrayList();
        al.add(newRating);
        al.add(newVolatility);
        al.add(newRatingWithVol);
        al.add(names);
        al.add(newScores);
        return al;
    }

    private String fS1(String in) {
        if (in.length() > 8) return in.substring(0, 8);
        while (in.length() < 8) in = in + " ";
        return in;
    }

    private String fD1(Double in) {
        DecimalFormat oneDigit = new DecimalFormat("0.0");
        return oneDigit.format(in.doubleValue());
    }

    private String fD2(Double in) {
        DecimalFormat oneDigit = new DecimalFormat("0.0");
        DecimalFormat twoDigits = new DecimalFormat("0.00");
        if (in.doubleValue() < 0)
            return oneDigit.format(in.doubleValue());
        return twoDigits.format(in.doubleValue());
    }

    private String scr(Double in) {
        DecimalFormat sixDigits = new DecimalFormat("0000.00");
        DecimalFormat fiveDigits = new DecimalFormat("000.00");
        if (in.doubleValue() < 0)
            return fiveDigits.format(in.doubleValue());
        return sixDigits.format(in.doubleValue());
    }

    private String rat(Double in) {
        DecimalFormat twoDigits = new DecimalFormat("00.0");
        DecimalFormat threeDigits = new DecimalFormat("000.");
        if (in.doubleValue() < 100)
            return twoDigits.format(in.doubleValue());
        return threeDigits.format(in.doubleValue());
    }

    private String in4(Double in) {
        DecimalFormat oneDigit = new DecimalFormat("0000");
        return oneDigit.format(in.doubleValue());
    }

    private double sqr(double j) {
        return j * j;
    }

private double normsinv(double p) {
        return normsinvnew(p);
    }
    
    private double normsinvnew(double p) {
    /* ********************************************
     * Original algorythm and Perl implementation can
     * be found at:
     * http://www.math.uio.no/~jacklam/notes/invnorm/index.html
     * Author:
     *  Peter J. Acklam
     *  jacklam@math.uio.no
     * ****************************************** */
        
        // Define break-points.
        // variable for result
        if(p <= 0) return Double.NEGATIVE_INFINITY;
        else if(p >= 1) return Double.POSITIVE_INFINITY;
        
        double z = 0;

        // Rational approximation for lower region:
        if( p < P_LOW )
        {
          double q  = Math.sqrt(-2*Math.log(p));
          z = (((((NORMINV_C[0]*q+NORMINV_C[1])*q+NORMINV_C[2])*q+NORMINV_C[3])*q+NORMINV_C[4])*q+NORMINV_C[5]) / ((((NORMINV_D[0]*q+NORMINV_D[1])*q+NORMINV_D[2])*q+NORMINV_D[3])*q+1);
        }
        // Rational approximation for upper region:
        else if ( P_HIGH < p )
        {
          double q  = Math.sqrt(-2*Math.log(1-p));
          z = -(((((NORMINV_C[0]*q+NORMINV_C[1])*q+NORMINV_C[2])*q+NORMINV_C[3])*q+NORMINV_C[4])*q+NORMINV_C[5]) / ((((NORMINV_D[0]*q+NORMINV_D[1])*q+NORMINV_D[2])*q+NORMINV_D[3])*q+1);
        }
        // Rational approximation for central region:
        else
        {
          double q = p - 0.5D;
          double r = q * q;
          z = (((((NORMINV_A[0]*r+NORMINV_A[1])*r+NORMINV_A[2])*r+NORMINV_A[3])*r+NORMINV_A[4])*r+NORMINV_A[5])*q / (((((NORMINV_B[0]*r+NORMINV_B[1])*r+NORMINV_B[2])*r+NORMINV_B[3])*r+NORMINV_B[4])*r+1);
        }
        
        z = refine(z, p);
        return z;
    }
    

    private static final double P_LOW  = 0.02425D;
    private static final double P_HIGH = 1.0D - P_LOW;

    // Coefficients in rational approximations.
    private static final double NORMINV_A[] =
    { -3.969683028665376e+01,  2.209460984245205e+02,
    -2.759285104469687e+02,  1.383577518672690e+02,
    -3.066479806614716e+01,  2.506628277459239e+00 };

    private static final double NORMINV_B[] =
    { -5.447609879822406e+01,  1.615858368580409e+02,
    -1.556989798598866e+02,  6.680131188771972e+01,
    -1.328068155288572e+01 };

    private static final double NORMINV_C[] =
    { -7.784894002430293e-03, -3.223964580411365e-01,
    -2.400758277161838e+00, -2.549732539343734e+00,
    4.374664141464968e+00,  2.938163982698783e+00 };

    private static final double NORMINV_D[] =
    { 7.784695709041462e-03,  3.224671290700398e-01,
    2.445134137142996e+00,  3.754408661907416e+00 };

    public static double erf(double z) {
        double t = 1.0 / (1.0 + 0.5 * Math.abs(z));

        // use Horner's method
        double ans = 1 - t * Math.exp( -z*z   -   1.26551223 +
                                            t * ( 1.00002368 +
                                            t * ( 0.37409196 + 
                                            t * ( 0.09678418 + 
                                            t * (-0.18628806 + 
                                            t * ( 0.27886807 + 
                                            t * (-1.13520398 + 
                                            t * ( 1.48851587 + 
                                            t * (-0.82215223 + 
                                            t * ( 0.17087277))))))))));
        if (z >= 0) return  ans;
        else        return -ans;
    }
    
    public static double erfc(double z) {
        return 1.0 - erf(z);
    }

    public static double refine(double x, double d)
    {
        if( d > 0 && d < 1)
        {
          double e = 0.5D * erfc(-x/Math.sqrt(2.0D)) - d;
          double u = e * Math.sqrt(2.0D*Math.PI) * Math.exp((x*x)/2.0D);
          x = x - u/(1.0D + x*u/2.0D);
        }
        return x;
    }

    private double norminv(double p, double mean, double stddev) {
        return mean + normsinv(p) * stddev;
    }
    private double snormdens(double v) {
        return Math.exp(-v * v / 2) / Math.sqrt(2 * Math.PI);
    }

    private double snorm(double v) {
        if (v >= 0) {
            double t = 1 / (1 + 0.33267 * v);
            return 1 - snormdens(v) * t * (0.4361836 + t * (-0.1201676 + 0.9372980 * t));
        } else {
            double t = 1 / (1 - 0.33267 * v);
            return snormdens(-v) * t * (0.4361836 + t * (-0.1201676 + 0.9372980 * t));
        }
    }

    private double winprobability(double meana, double stddeva, double meanb, double stddevb) {
        if (1 == 1)
            return snorm((meanb - meana) / Math.sqrt(stddeva * stddeva + stddevb * stddevb));
        double count = 0;
        double a = 0, b = 0;
        double fa = norminv((a + 0.5) / STEPS, meana, stddeva);
        double fb = norminv((b + 0.5) / STEPS, meanb, stddevb);
        while (a < STEPS) {
            if (fa < fb) {
                count += STEPS - b;
                a++;
                fa = norminv((a + 0.5) / STEPS, meana, stddeva);
            } else if (fa == fb) {
                count += STEPS - b - 0.5;
                a++;
                b++;
                fa = norminv((a + 0.5) / STEPS, meana, stddeva);
                fb = norminv((b + 0.5) / STEPS, meanb, stddevb);
            } else {
                b++;
                if (b == STEPS) break;
                fb = norminv((b + 0.5) / STEPS, meanb, stddevb);
            }
        }
        return count / (STEPS * STEPS);
    }
//----------------------END RATING FUNCTIONS0-----------------------------------------------

}

