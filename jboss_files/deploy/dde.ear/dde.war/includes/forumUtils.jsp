<%@ page import="java.text.SimpleDateFormat,
                 com.topcoder.forum.ForumRemote,
                 com.topcoder.forum.ForumRemoteHome,
                 javax.rmi.PortableRemoteObject,
                 com.topcoder.dde.forum.DDEForum,
                 com.topcoder.dde.forum.DDEForumHome,
                 com.topcoder.dde.forum.ForumComponent,
                 java.util.GregorianCalendar,
                 java.util.Calendar" %>
<%@ page import="java.io.PrintWriter" %>
<%!
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("E, MMM d, yyyy hh:mm a");

    String textToHtml(String in) {
        String text = rtrim(in);
        StringBuffer out = new StringBuffer();
        boolean leading = true;
        for (int i=0; i<text.length(); i++) {
            char ch = text.charAt(i);
            switch(ch) {
                case ' ':
                    if (leading) {
                        out.append("&nbsp;");
                    } else {
                        out.append(ch);
                        leading = false;
                    }
                    break;
                case '<':
                    out.append("&lt;");
                    leading = false;
                    break;
                case '>':
                    out.append("&gt;");
                    leading = false;
                    break;
                case '&':
                    out.append("&amp;");
                    leading = false;
                    break;
                case '\n':
                    out.append("<br />");
                    leading = true;
                    break;
                case '\r':
                    break;
                default:
                    out.append(ch);
                    leading = false;
                    break;
            }
        }
        return out.toString();
    }

    String rtrim(String s) {
        int i = s.length()-1;
        LOOP: for (; i>=0; i--) {
            switch(s.charAt(i)) {
                case ' ':
                case '\t':
                case '\r':
                case '\n':
                    break;
                default:
                    break LOOP;
            }
        }
        return (i==s.length()) ? s : s.substring(0,i+1);
    }

%>
<%
    /////////////////////////////////////////////
    //Get forum Id
    /////////////////////////////////////////////
    long forumId = 0;
    try {
        forumId = Long.parseLong(request.getParameter("f"));
        if (forumId == 24637535) {
            response.setContentType("text/html");
            response.setStatus(404);
            out.println("<html><head><title>Page Not Found</title></head>");
            out.println("<body><h4>Page not found.</h4>");
            out.println("</body></html>");
            out.flush();
            return;
        }
    } catch (NumberFormatException nfe) {
        response.sendRedirect("c_active_collab.jsp");
        return;
    }

    long topicId = 0;
    try {
        topicId = Long.parseLong(request.getParameter("t"));
    } catch (NumberFormatException nfe) {
    }

    long threadId = 0;
    try {
        threadId = Long.parseLong(request.getParameter("r"));
    } catch (NumberFormatException nfe) {
    }

    long postId = 0;
    try {
        postId = Long.parseLong(request.getParameter("p"));
    } catch (NumberFormatException nfe) {
    }

    long replyId = 0;
    try {
        replyId = Long.parseLong(request.getParameter("rp"));
    } catch (NumberFormatException nfe) {
    }


    DDEForumHome ddeforumhome = (DDEForumHome) PortableRemoteObject.narrow(
            CONTEXT.lookup(DDEForumHome.EJB_REF_NAME), DDEForumHome.class);
    DDEForum ddeforum = ddeforumhome.create();

    ForumRemoteHome forumHome = (ForumRemoteHome) PortableRemoteObject.narrow(
            CONTEXT.lookup(ForumRemoteHome.EJB_REF_NAME), ForumRemoteHome.class);
    ForumRemote forumBean = forumHome.create();

    /////////////////////////////////////////////
    //Check for permissions
    /////////////////////////////////////////////
    boolean canPost = false;
    boolean canModerate = false;
    boolean loggedOn = (tcUser != null);
    int forumType = 0;
    long specForumId = 0;
    long collabForumId = 0;
    long prevThreadId = 0;
    long nextThreadId = 0;

    if (loggedOn) {
        canPost = ddeforum.canPost(forumId, tcSubject);
        canModerate = ddeforum.canModerate(forumId, tcSubject);
    }

    /////////////////////////////////////////////
    //Get linked component information
    /////////////////////////////////////////////
    ForumComponent forumComponent = ddeforum.getLinkedComponent(forumId);
    collabForumId = forumComponent.getCollabForumId();
    specForumId = forumComponent.getSpecForumId();
    if (forumId == collabForumId) {
        forumType = com.topcoder.dde.catalog.ForumCategory.COLLABORATION;
    } else {
        forumType = com.topcoder.dde.catalog.ForumCategory.SPECIFICATION;
    }

    if (forumType == com.topcoder.dde.catalog.ForumCategory.SPECIFICATION) {
        if (!loggedOn) {
            //Redirect to logon page
            session.putValue("nav_redirect_msg", "You must login to view the specification forum");
            response.sendRedirect("/login.jsp");
            return;
        } else if (!canPost) {
            //Redirect to permission page
            response.sendRedirect("s_spec_permission.jsp");
            return;
        }
    } else if (forumType == com.topcoder.dde.catalog.ForumCategory.COLLABORATION) {
        //Handle any collaboration specific logic here.
    } else {
        //Handle Error here
    }

    /////////////////////////////////////////////
    // Set up the date filter.
    /////////////////////////////////////////////
    Calendar date = new GregorianCalendar();
    date.add(Calendar.DATE, -1);

    long newSinceTime = date.getTime().getTime();
%>