<%@ page import="javax.naming.*" %>
<%@ page import="javax.ejb.CreateException" %>
<%@ page import="java.io.*" %>
<%@ page import="java.rmi.*" %>
<%@ page import="javax.rmi.*" %>
<%@ page import="java.net.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.lang.reflect.*" %>

<%@ page import="com.topcoder.forum.*" %>
<%@ page import="com.topcoder.dde.catalog.*" %>

<%@ include file="/includes/util.jsp" %>
<%@ include file="session.jsp" %>
<%@ include file="/includes/formclasses.jsp" %>


<HTML>
<HEAD>

<%
// ISSUE
// When adding a topic to a template, calling template.getTopics() includes the added topic but calling getId() returns 0

Object o = CONTEXT.lookup(ForumRemoteHome.EJB_REF_NAME);
ForumRemoteHome forumHome = (ForumRemoteHome) PortableRemoteObject.narrow(o, ForumRemoteHome.class); 

o = CONTEXT.lookup(ForumAdminRemoteHome.EJB_REF_NAME);
ForumAdminRemoteHome forumAdminHome = (ForumAdminRemoteHome) PortableRemoteObject.narrow(o, ForumAdminRemoteHome.class); 
ForumAdminRemote forumAdmin = forumAdminHome.create();

long lngTemplate = 0;

try {
    lngTemplate = Long.parseLong(request.getParameter("template"));
} catch (Exception e) {
    //response.sendRedirect("forum.jsp");
}

ForumTemplate templates[] = new ForumTemplate[0];
Hashtable hashTemplates = new Hashtable();
ForumTemplate selectedTemplate = null;

Hashtable hashTopics = new Hashtable();

try {
    templates = (ForumTemplate[])forumAdmin.getTemplates().toArray(templates);
    for (int i=0; i < templates.length; i++) {
        hashTemplates.put("" + templates[i].getId(), templates[i]);
    }
    selectedTemplate = (ForumTemplate)hashTemplates.get("" + lngTemplate);
    if (selectedTemplate != null) {
        // Load all template topics for selected template
        TemplateTopic topics[] = new TemplateTopic[0];
        topics = (TemplateTopic[])selectedTemplate.getTopics().toArray(topics);
        for (int i=0; i < topics.length; i++) {
            debug.addMsg("forum admin", "hash Adding topic " + topics[i].getId());
            hashTopics.put("" + topics[i].getId(), topics[i]);
            TemplateTopic subtopics[] = new TemplateTopic[0];
            subtopics = (TemplateTopic[])topics[i].getSubtopics().toArray(subtopics);
            for (int j=0; j < subtopics.length; j++) {
                debug.addMsg("forum admin", "hash Adding subtopic " + subtopics[j].getId());
                hashTopics.put("" + subtopics[j].getId(), subtopics[j]);
            }
        }
    }
} catch (Exception e) {
    System.out.println("An error occurred getting templates: " + e.getMessage());
}

String action = request.getParameter("a");
String strError = "";
String strMessage = "";

if (action != null) {
    if (action.equalsIgnoreCase("Create Template")) {
        selectedTemplate = forumAdmin.addTemplate(new ForumTemplate(request.getParameter("txtTemplateText"), Long.parseLong(request.getParameter("selTemplateType"))));
        templates = (ForumTemplate[])forumAdmin.getTemplates().toArray(new ForumTemplate[0]);
        hashTemplates.put("" + selectedTemplate.getId(), selectedTemplate);
        lngTemplate = selectedTemplate.getId();
    }

    if (action.equalsIgnoreCase("Delete Template")) {
        try {
            templates = (ForumTemplate[])forumAdmin.getTemplates().toArray(new ForumTemplate[0]);
            hashTemplates.put("" + selectedTemplate.getId(), null);
            selectedTemplate = null;
            forumAdmin.removeTemplate(lngTemplate);
            lngTemplate = 0;
        } catch (Exception e) {
            strError += "Forum template was not deleted successfully: " + e.getMessage();
        }
    }

    if (action.equalsIgnoreCase("Update Template Name")) {
        String templateText = request.getParameter("templateText");
        debug.addMsg("forum admin", "templateText is " + templateText);
        selectedTemplate.setText(templateText);
    }

    if (action.equalsIgnoreCase("Update Topic")) {
        TemplateTopic topic = (TemplateTopic)hashTopics.get(request.getParameter("topic"));
        debug.addMsg("forum admin", "updating topic " + topic.getId());

        String name = request.getParameter("templateTopic");
        String text = request.getParameter("taTemplateTopic");
        topic.setName(name);
        topic.setText(text);
    }

    if (action.equalsIgnoreCase("Remove Topic")) {
        TemplateTopic topic = (TemplateTopic)hashTopics.get(request.getParameter("topic"));
        debug.addMsg("forum admin", "removing topic " + topic.getId());

        selectedTemplate.removeTopic(topic);
    }

    if (action.equalsIgnoreCase("Update Subtopic")) {
        debug.addMsg("forum admin", "updating subtopic");
        TemplateTopic topic = (TemplateTopic)hashTopics.get(request.getParameter("topic"));
        debug.addMsg("forum admin", "topic is " + topic.getId());
        TemplateTopic subtopic = (TemplateTopic)hashTopics.get(request.getParameter("subtopic"));
        debug.addMsg("forum admin", "subtopic is " + subtopic.getId());

        String name = request.getParameter("templateTopic");
        String text = request.getParameter("taTemplateTopic");

        subtopic.setName(name);
        subtopic.setText(text);
    }

    if (action.equalsIgnoreCase("Remove Subtopic")) {
        debug.addMsg("forum admin", "removing topic " + request.getParameter("topic") + " subtopic " + request.getParameter("subtopic"));
        TemplateTopic topic = (TemplateTopic)hashTopics.get(request.getParameter("topic"));
        TemplateTopic subtopic = (TemplateTopic)hashTopics.get(request.getParameter("subtopic"));
        debug.addMsg("forum admin", "removing topic " + topic.getId() + " subtopic " + subtopic.getId());

        topic.removeSubtopic(subtopic);
    }

    if (action.equalsIgnoreCase("Add Topic")) {
        debug.addMsg("forum admin", "getting parameters");
        String name = request.getParameter("templateTopic");
        String text = request.getParameter("taTemplateTopic");
        debug.addMsg("forum admin", "creating topic");
        TemplateTopic topic = new TemplateTopic(text, name);
        debug.addMsg("forum admin", "adding topic");
        selectedTemplate.addTopic(topic);
    }

    if (action.equalsIgnoreCase("Add Subtopic")) {
        debug.addMsg("forum admin", "getting parameters");
        TemplateTopic topic = (TemplateTopic)hashTopics.get(request.getParameter("topic"));
        debug.addMsg("forum admin", "topic is " + topic.getId());

        String name = request.getParameter("templateTopic");
        String text = request.getParameter("taTemplateTopic");
        debug.addMsg("forum admin", "creating subtopic");
        TemplateTopic subtopic = new TemplateTopic(text, name);
        
        debug.addMsg("forum admin", "adding subtopic");
        topic.addSubtopic(subtopic);
    }

    if (selectedTemplate != null) {
        try {
            debug.addMsg("forum admin", "updating template");
            selectedTemplate = forumAdmin.updateTemplate(selectedTemplate);
            strMessage = "Template was updated";
        } catch (RemoteException re) {
            strError = "Template was not updated successfully - RemoteException: " + re.getMessage();
        } catch (ForumException fe) {
            strError = "Template was not updated successfully - ForumException: " + fe.getMessage();
        }
    }
}

%>
</HEAD>
<BODY>

<% if (strError.length() > 0) { %>
<H3><FONT COLOR="RED"><%= strError %></FONT></H3>
<HR>
<% } %>

<% if (strMessage.length() > 0) { %>
<H3><%= strMessage %></H3>
<HR>
<% } %>

<FORM NAME="frmTemplate" ACTION="forum.jsp" METHOD="POST">

<SELECT NAME="template">
<%
    for (int i=0; i < templates.length; i++) {
        String type = "";
        if (templates[i].getTemplateType() == 0) {
            type = "Collab";
        }

        if (templates[i].getTemplateType() == 1) {
            type = "Spec";
        }
%>
    <OPTION VALUE="<%= templates[i].getId() %>"<%= (lngTemplate == templates[i].getId() ? " SELECTED" : "") %>><%= templates[i].getText() %> (<%= type %>)
<% } %>
</SELECT>
<INPUT TYPE="SUBMIT" NAME="a" VALUE="Select">
<INPUT TYPE="SUBMIT" NAME="a" VALUE="Delete Template">
</FORM>

<HR>

<FORM NAME="frmCreateTemplate" ACTION="forum.jsp" METHOD="POST">
<INPUT TYPE="TEXT" NAME="txtTemplateText" VALUE="">
<SELECT NAME="selTemplateType">
    <OPTION VALUE="<%= TemplateType.COLLABORATION %>">Collaboration
    <OPTION VALUE="<%= TemplateType.SPECIFICATION %>">Specification
</SELECT>
<INPUT TYPE="SUBMIT" NAME="a" VALUE="Create Template">
</FORM>

<HR>

<% if (selectedTemplate != null) { %>

<%
    TemplateTopic topics[] = new TemplateTopic[0];
    topics = (TemplateTopic[])selectedTemplate.getTopics().toArray(topics);
%>

<TABLE>
    <FORM NAME="frmUpdateTopics" ACTION="forum.jsp" METHOD="POST">
    <INPUT TYPE="HIDDEN" NAME="template" VALUE="<%= selectedTemplate.getId() %>">
    <TR>
        <TD>
            Template Name
        </TD>
        <TD>
            <INPUT TYPE="TEXT" NAME="templateText" VALUE="<%= selectedTemplate.getText() %>">
            <INPUT TYPE="SUBMIT" NAME="a" VALUE="Update Template Name">
        </TD>
    </TR>
    </FORM>
    <TR>
        <TD>
            Topics
        </TD>
        <TD>
            <TABLE>
            <% for (int i=0; i < topics.length; i++) { %>
                <FORM NAME="frmUpdateTopics" ACTION="forum.jsp" METHOD="POST">
                <INPUT TYPE="HIDDEN" NAME="template" VALUE="<%= selectedTemplate.getId() %>">
                <INPUT TYPE="HIDDEN" NAME="topic" VALUE="<%= topics[i].getId() %>">
                <TR>
                    <TD COLSPAN=2>
                        <INPUT TYPE="TEXT" NAME="templateTopic" VALUE="<%= topics[i].getName() %>"><BR>
                        <TEXTAREA NAME="taTemplateTopic" ROWS=4 COLS=50><%= topics[i].getText() %></TEXTAREA><BR>
                        <INPUT TYPE="SUBMIT" NAME="a" VALUE="Update Topic">
                        <INPUT TYPE="SUBMIT" NAME="a" VALUE="Remove Topic">
                    </TD>
                </TR>
                </FORM>
                <%
                    TemplateTopic subtopics[] = new TemplateTopic[0];
                    subtopics = (TemplateTopic[])topics[i].getSubtopics().toArray(subtopics);
                    for (int j=0; j < subtopics.length; j++) {
                %>
                    <FORM NAME="frmUpdateTopics" ACTION="forum.jsp" METHOD="POST">
                    <INPUT TYPE="HIDDEN" NAME="template" VALUE="<%= selectedTemplate.getId() %>">
                    <INPUT TYPE="HIDDEN" NAME="topic" VALUE="<%= topics[i].getId() %>">
                    <INPUT TYPE="HIDDEN" NAME="subtopic" VALUE="<%= subtopics[j].getId() %>">
                    <TR>
                        <TD WIDTH=20>
                        </TD>
                        <TD>
                            <INPUT TYPE="TEXT" NAME="templateTopic" VALUE="<%= subtopics[j].getName() %>"><BR>
                            <TEXTAREA NAME="taTemplateTopic" ROWS=4 COLS=50><%= subtopics[j].getText() %></TEXTAREA><BR>
                            <INPUT TYPE="SUBMIT" NAME="a" VALUE="Update Subtopic">
                            <INPUT TYPE="SUBMIT" NAME="a" VALUE="Remove Subtopic">
                        </TD>
                    </TR>
                    </FORM>
                <% } %>
                    <FORM NAME="frmAddSubTopic" ACTION="forum.jsp" METHOD="POST">
                    <INPUT TYPE="HIDDEN" NAME="template" VALUE="<%= selectedTemplate.getId() %>">
                    <INPUT TYPE="HIDDEN" NAME="topic" VALUE="<%= topics[i].getId() %>">
                    <TR>
                        <TD WIDTH=20>
                        </TD>
                        <TD>
                            <INPUT TYPE="TEXT" NAME="templateTopic" VALUE=""><BR>
                            <TEXTAREA NAME="taTemplateTopic" ROWS=4 COLS=50></TEXTAREA><BR>
                            <INPUT TYPE="SUBMIT" NAME="a" VALUE="Add Subtopic">
                        </TD>
                    </TR>
                    </FORM>
            <% } %>
                <FORM NAME="frmAddTopic" ACTION="forum.jsp" METHOD="POST">
                <INPUT TYPE="HIDDEN" NAME="template" VALUE="<%= selectedTemplate.getId() %>">
                <TR>
                    <TD COLSPAN=2>
                        <INPUT TYPE="TEXT" NAME="templateTopic" VALUE=""><BR>
                        <TEXTAREA NAME="taTemplateTopic" ROWS=4 COLS=50></TEXTAREA><BR>
                        <INPUT TYPE="SUBMIT" NAME="a" VALUE="Add Topic">
                    </TD>
                </TR>
                </FORM>
            </TABLE>
        </TD>
    </TR>
</TABLE>

<% } %>

</BODY>
</HTML>