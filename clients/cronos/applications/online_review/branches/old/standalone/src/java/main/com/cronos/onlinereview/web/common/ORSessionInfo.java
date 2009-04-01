package com.cronos.onlinereview.web.common;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TimeZone;

import com.topcoder.security.RolePrincipal;
import com.topcoder.shared.dataAccess.CachedDataAccess;
import com.topcoder.shared.dataAccess.Request;
import com.topcoder.shared.dataAccess.resultSet.ResultSetContainer;
import com.topcoder.shared.security.SimpleUser;
import com.topcoder.shared.util.DBMS;
import com.topcoder.shared.util.logging.Logger;
import com.topcoder.web.common.TCRequest;
import com.topcoder.web.common.WebConstants;
import com.topcoder.web.common.security.WebAuthentication;

public class ORSessionInfo implements Serializable {
    private static Logger log = Logger.getLogger(ORSessionInfo.class);

    private static final String GROUP_PREFIX = "group_";
    private String handle = null;
    private long userid = SimpleUser.createGuest().getId();
    private String serverName = null;
    private String servletPath = null;
    private String absoluteServletPath = null;
    private String queryString = null;
    private String requestString = null;
    private Date date = null;
    private boolean isLoggedIn = false;
    private boolean knownUser = false;
    protected String timezone = null;

    /**
     * group may be:
     * 'G' guest
     * 'A' admin
     */
    private char group = 'G';

    public ORSessionInfo() {
        date = new Date();
    }

    public ORSessionInfo(TCRequest request, WebAuthentication authentication, Set groups) throws Exception {
        this();
        userid = authentication.getActiveUser().getId();
        handle = authentication.getActiveUser().getUserName();

        if (pruneGroups(groups).contains("Admin"))
            setGroup('A');
        else {
            setGroup('G');
        }

        serverName = request.getServerName();
        servletPath = request.getContextPath() + request.getServletPath();
        String query = request.getQueryString();
        queryString = (query == null) ? ("") : ("?" + query);
        StringBuffer buf = new StringBuffer(200);
        buf.append("http://");        //todo any better way than a hardcode?
        buf.append(request.getServerName());
        buf.append(servletPath);
        buf.append(queryString);
        requestString = buf.toString();

        absoluteServletPath = request.getServerName() + servletPath;

        isLoggedIn = !authentication.getUser().isAnonymous();

        if (log.isDebugEnabled()) {
            log.debug("servername: " + getServerName() + " servletpath:" + getServletPath() + " query: " +
                    getQueryString() + " request: " + getRequestString());
        }
        knownUser = authentication.isKnownUser();

    }


    public String getHandle() {
        return handle;
    }

    public long getUserId() {
        return userid;
    }

    private void setGroup(char group) {
        if (0 > "GA".indexOf(group))
            throw new IllegalArgumentException("no group class '" + group + "'");
        this.group = group;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public boolean isAnonymous() {
        return userid == SimpleUser.createGuest().getId();
    }

    public boolean isAdmin() {
        return group == 'A';
    }

    public String getServerName() {
        return serverName;
    }

    public String getServletPath() {
        return servletPath;
    }

    public String getAbsoluteServletPath() {
        return "http://" + absoluteServletPath;
    }

    public String getSecureAbsoluteServletPath() {
        return "https://" + absoluteServletPath;
    }

    public String getQueryString() {
        return queryString;
    }

    public String getRequestString() {
        return requestString;
    }

    public Date getDate() {
        return date;
    }

    public boolean isKnownUser() {
        return knownUser;
    }

    public String getTimezone() {
        if (timezone == null) {
            try {
                timezone = loadTimezone();
            } catch (Exception e) {
                timezone = TimeZone.getDefault().getID();
                log.error("Could not load timezone from db, using : " + timezone);
            }
        }
        return timezone;
    }

    private String loadTimezone() throws Exception {
        CachedDataAccess tzDai = new CachedDataAccess(DBMS.OLTP_DATASOURCE_NAME);
        Request tzReq = new Request();
        tzReq.setContentHandle("user_timezone");
        tzReq.setProperty(WebConstants.USER_ID, String.valueOf(getUserId()));
        return ((ResultSetContainer) tzDai.getData(tzReq).get("user_timezone")).getStringItem(0, "timezone_desc");
    }

    protected Set pruneGroups(Set groups) {
        Set groupnames = new HashSet();
        Iterator it = groups.iterator();
        while (it.hasNext()) {
            String rolename = ((RolePrincipal) it.next()).getName();
            if (rolename.startsWith(GROUP_PREFIX))
                groupnames.add(rolename.substring(GROUP_PREFIX.length()));
        }
        return groupnames;
    }


}