<%@ page import="javax.naming.*" %>
<%@ page import="javax.ejb.CreateException" %>
<%@ page import="java.io.*" %>
<%@ page import="java.rmi.*" %>
<%@ page import="javax.rmi.*" %>
<%@ page import="java.net.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.lang.reflect.*" %>

<%@ page import="com.topcoder.dde.catalog.*" %>

<%@ page import="com.topcoder.apps.review.document.*" %>

<%@ include file="/includes/util.jsp" %>
<%@ include file="session.jsp" %>
<%@ include file="/includes/formclasses.jsp" %>


<%
    // STANDARD PAGE VARIABLES
    String page_name = "scorecard.jsp";
    String action = request.getParameter("a");
    String sidString = request.getParameter("sid");
    long sid = -1;
    if (sidString != null) sid = Long.parseLong(sidString);
%>


<html>
<head>
<link rel="stylesheet" type="text/css" href="/includes/tcs_style.css" />
 <link rel="stylesheet" type="text/css" href="/includes/tcs_style.css"/> 
<script language="JavaScript" type="text/javascript" src="/scripts/javascriptAdmin.js">
</script>


<%

Object objDMB = CONTEXT.lookup("com.topcoder.apps.review.document.DocumentManagerLocalHome");
DocumentManagerLocalHome home = (DocumentManagerLocalHome) PortableRemoteObject.narrow(objDMB, DocumentManagerLocalHome.class);
DocumentManagerLocal dmb = home.create();

String strError = "";
String strMessage = "";

int destLink = -1;
int destGroup = -1;
int destSection = -1;
int destQuestion = -1;

ScorecardTemplate template = null;
TGroup[] groups = null;

String tmpAction = "";
if (action != null) {
    tmpAction = action;
    action = request.getParameter("action");

    template = (ScorecardTemplate) session.getAttribute("template");
    groups = template.getGroups();
    if (groups == null) groups = new TGroup[0];

    template.setName(request.getParameter("templateName"));
    template.setProjectType(Integer.parseInt(request.getParameter("projectType")));
    template.setScorecardType(Integer.parseInt(request.getParameter("scorecardType")));
    for (int grpIdx=0; grpIdx<groups.length; grpIdx++) {
        String groupPrefix = "group[" + grpIdx + "]";
        String groupName = request.getParameter(groupPrefix + ".groupName");
        groups[grpIdx].setGroupName(groupName);

        TSection[] sections = groups[grpIdx].getSections();
        if (sections == null) sections = new TSection[0];
        for (int sectionIdx = 0; sectionIdx<sections.length; sectionIdx++) {
            String sectionPrefix = groupPrefix + ".section[" + sectionIdx + "]";
            String sectionName = request.getParameter(sectionPrefix + ".sectionName");
            int sectionWeight = Integer.parseInt(request.getParameter(sectionPrefix + ".sectionWeight"));
            sections[sectionIdx].setSectionName(sectionName);
            sections[sectionIdx].setSectionWeight(sectionWeight);

            TQuestion[] questions = sections[sectionIdx].getQuestions();
            if (questions == null) questions = new TQuestion[0];
            for (int qIdx = 0; qIdx<questions.length; qIdx++) {
                String questionPrefix = sectionPrefix + ".question[" + qIdx + "]";
                String questionText = request.getParameter(questionPrefix + ".questionText");
                int questionWeight = Integer.parseInt(request.getParameter(questionPrefix + ".questionWeight"));
                int questionType = Integer.parseInt(request.getParameter(questionPrefix + ".questionType"));
                questions[qIdx].setQuestionText(questionText);
                questions[qIdx].setQuestionWeight(questionWeight);
                questions[qIdx].setQuestionType(questionType);
                questions[qIdx].setProjectType(template.getProjectType());
                questions[qIdx].setScorecardType(template.getScorecardType());
            }
        }
    }

    if (action.equalsIgnoreCase("addGroup")) {
        int gIdx = Integer.parseInt(request.getParameter("gIdx"));
        TGroup[] oldGroups = groups;
        if (oldGroups == null) oldGroups = new TGroup[0];
        groups = new TGroup[oldGroups.length + 1];
        for (int i=0; i<groups.length; i++) {
            if (i <= gIdx) groups[i] = oldGroups[i];
            else if (i == gIdx+1) groups[i] = new TGroup(null,-1,"New Groupname",0);
            else groups[i] = oldGroups[i-1];
        }
        destLink = 0;
        destGroup = gIdx+1;
    } else if (action.equalsIgnoreCase("deleteGroup")) {
        int gIdx = Integer.parseInt(request.getParameter("gIdx"));
        TGroup[] oldGroups = groups;
        groups = new TGroup[groups.length - 1];
        for (int i=0; i<oldGroups.length; i++) {
            if (i < gIdx) groups[i] = oldGroups[i];
            else if (i > gIdx) groups[i-1] = oldGroups[i];
        }
    } else if (action.equalsIgnoreCase("addSection")) {
        int gIdx = Integer.parseInt(request.getParameter("gIdx"));
        int sIdx = Integer.parseInt(request.getParameter("sIdx"));
        TSection[] oldSections = groups[gIdx].getSections();
        if (oldSections == null) oldSections = new TSection[0];
        TSection[] sections = new TSection[oldSections.length + 1];
        for (int i=0; i<sections.length; i++) {
            if (i <= sIdx) sections[i] = oldSections[i];
            else if (i == sIdx+1) sections[i] = new TSection(null,-1,"New Sectionname",0,0,0);
            else sections[i] = oldSections[i-1];
        }
        groups[gIdx].setSections(sections);
        destLink = 1;
        destGroup = gIdx;
        destSection = sIdx+1;
    } else if (action.equalsIgnoreCase("deleteSection")) {
        int gIdx = Integer.parseInt(request.getParameter("gIdx"));
        int sIdx = Integer.parseInt(request.getParameter("sIdx"));
        TSection[] oldSections = groups[gIdx].getSections();
        TSection[] sections = new TSection[oldSections.length - 1];
        for (int i=0; i<oldSections.length; i++) {
            if (i < sIdx) sections[i] = oldSections[i];
            else if (i > sIdx) sections[i-1] = oldSections[i];
        }
        groups[gIdx].setSections(sections);
    } else if (action.equalsIgnoreCase("addQuestion")) {
        int gIdx = Integer.parseInt(request.getParameter("gIdx"));
        int sIdx = Integer.parseInt(request.getParameter("sIdx"));
        int qIdx = Integer.parseInt(request.getParameter("qIdx"));
        TQuestion[] oldQuestions = groups[gIdx].getSections()[sIdx].getQuestions();
        if (oldQuestions == null) oldQuestions = new TQuestion[0];
        TQuestion[] questions = new TQuestion[oldQuestions.length + 1];
        for (int i=0; i<questions.length; i++) {
            if (i <= qIdx) questions[i] = oldQuestions[i];
            else if (i == qIdx+1) questions[i] = new TQuestion(-1,-1,template.getProjectType(),
            	template.getScorecardType(),"New Questionstext",0,0,sIdx,2,template.getId());
            else questions[i] = oldQuestions[i-1];
        }
        groups[gIdx].getSections()[sIdx].setQuestions(questions);
        destLink = 2;
        destGroup = gIdx;
        destSection = sIdx;
        destQuestion = qIdx+1;
    } else if (action.equalsIgnoreCase("deleteQuestion")) {
        int gIdx = Integer.parseInt(request.getParameter("gIdx"));
        int sIdx = Integer.parseInt(request.getParameter("sIdx"));
        int qIdx = Integer.parseInt(request.getParameter("qIdx"));
        TQuestion[] oldQuestions = groups[gIdx].getSections()[sIdx].getQuestions();
        TQuestion[] questions = new TQuestion[oldQuestions.length - 1];
        for (int i=0; i<oldQuestions.length; i++) {
            if (i < qIdx) questions[i] = oldQuestions[i];
            else if (i > qIdx) questions[i-1] = oldQuestions[i];
        }
        groups[gIdx].getSections()[sIdx].setQuestions(questions);
    }
    template.setGroups(groups);

    if (action.equalsIgnoreCase("save")) {
        dmb.saveScorecardTemplate(template, true, true);
    } else if (action.equalsIgnoreCase("saveOld")) {
        dmb.saveScorecardTemplate(template, false, true);
    }
    session.setAttribute("template", template);
} else {
    if (sid != -1) {
      template = dmb.getScorecardTemplate(sid);
      groups = template.getGroups();
    } else {
      template = new ScorecardTemplate(-1, "New Template Name", 0, 1, 1, null, false);
    }
    session.setAttribute("template", template);
}


%>
</head>
<body>

<!-- Header begins -->
<%@ include file="/includes/adminHeader.jsp" %>
<%@ include file="/includes/adminNav.jsp" %>
<!-- Header ends -->


<% if (strError.length() > 0) { %>
<H3><FONT COLOR="RED"><%= strError %></FONT></H3>
<HR>
<% } %>

<% if (strMessage.length() > 0) { %>
<H3><%= strMessage %></H3>
<HR>
<% } %>

<H2> Scorecard </H2>
<%--
<% for (int i=0; i < licenseLevels.length; i++) { %>
<% } %>
--%>
<script>
    function set(form, what, gIdx, sIdx, qIdx) {
        form.action.value = what;
        form.gIdx.value = gIdx;
        form.sIdx.value = sIdx;
        form.qIdx.value = qIdx;
    }
</script>
<form name="scorecardForm" action="scorecard.jsp#GO" method="POST">
  <input type="hidden" name="id" value="8501356">
  <input type="hidden" name="action" value="save">
  <input type="hidden" name="gIdx" value="0">
  <input type="hidden" name="sIdx" value="0">
  <input type="hidden" name="qIdx" value="0">

<strong>Id: </strong><%=template.getId()%><br>
<strong>ScorecardTemplate Name: </strong>
<input type="text" name="templateName" value='<%=template.getName()%>' size=50></td>
<br>
<strong>Project Type:</strong><select name="projectType">
<%
String[] ptOptions = {"Design","Development"};
for (int ptIdx=0; ptIdx<ptOptions.length; ptIdx++) {
%>
      <option value="<%=ptIdx+1%>"<%if(template.getProjectType()==ptIdx+1){%> selected="selected"<%}%>> <%=ptOptions[ptIdx]%></option>
<%}%>
</select>
<br>
<strong>Scorecard Type:</strong><select name="scorecardType">
<%
String[] stOptions = {"Screening","Review"};
for (int stIdx=0; stIdx<stOptions.length; stIdx++) {
%>
      <option value="<%=stIdx+1%>"<%if(template.getScorecardType()==stIdx+1){%> selected="selected"<%}%>> <%=stOptions[stIdx]%></option>
<%}%>
</select>
<br><br>


<table border>

<%-- Possibility to insert a group before the first group --%>
  <tr>
    <td colspan="2" bgcolor="#00FFFF">
      <strong>Place for new group</strong></td>
    <td bgcolor="#00FFFF"><input type="submit" name="a" value="Add Group" onClick="<%="set(this.form,'addGroup',-1,0,0)"%>"></td>
  </tr>
<%
if (groups == null) groups = new TGroup[0];
for (int groupIdx=0; groupIdx<groups.length; groupIdx++) {
  TGroup group = groups[groupIdx];
  String groupPrefix = "group[" + groupIdx + "]";
%>
<%-- Group --%>
  <tr>
    <td colspan="2" rowspan="2" bgcolor="#00FFFF">
<% if (destLink == 0 && destGroup == groupIdx) {%>
  <a name="GO"></a>
<%}%>
      <strong>Group: </strong><input type="text" name='<%=groupPrefix%>.groupName' value='<%=group.getGroupName()%>' size=50></td>
    <td bgcolor="#00FFFF"><input type="submit" name="a" value="Delete Group" onClick="<%="set(this.form,'deleteGroup',"+groupIdx+",0,0)"%>"></td>
  </tr>
  <tr>
    <td bgcolor="#00FFFF"><input type="submit" name="a" value="Add Group" onClick="<%="set(this.form,'addGroup'," +groupIdx+",0,0)"%>"></td>
  </tr>

<%-- Possibility to insert a section before the first section --%>
  <tr>
    <td colspan="2" bgcolor="#FF99FF"><strong>Place for new section</strong></td>
    <td bgcolor="#FF99FF"><input type="submit" name="a" value="Add Section" onClick="<%="set(this.form,'addSection',"+groupIdx+",-1,0)"%>"></td>
  </tr>

<%
TSection[] sections = group.getSections();
if (sections == null) sections = new TSection[0];
for (int sectionIdx=0; sectionIdx<sections.length; sectionIdx++) {
  TSection section = sections[sectionIdx];
  String sectionPrefix = groupPrefix + ".section[" + sectionIdx + "]";
%>
<%-- Section --%>
  <tr>
    <td rowspan="2" bgcolor="#FF99FF">
<% if (destLink == 1 && destGroup == groupIdx && destSection == sectionIdx) {%>
  <a name="GO"></a>
<%}%>
      <strong>Section:</strong><br><textarea name='<%=sectionPrefix%>.sectionName' cols="65" rows="1"><%=section.getSectionName()%></textarea></td>
    <td rowspan="2" bgcolor="#FF99FF"><strong>Weight:</strong><input type="text" name='<%=sectionPrefix%>.sectionWeight' value='<%=section.getSectionWeight()%>'></td>
    <td bgcolor="#FF99FF"><input type="submit" name="a" value="Delete Section" onClick="<%="set(this.form,'deleteSection',"+groupIdx+","+sectionIdx+",0)"%>"></td>
  </tr>
  <tr>
    <td bgcolor="#FF99FF"><input type="submit" name="a" value="Add Section" onClick="<%="set(this.form,'addSection',"+groupIdx+","+sectionIdx+",0)"%>"></td>
  </tr>

<%-- Possibility to insert a question before the first question --%>
  <tr>
    <td colspan="2"><strong>Place for new question</strong></td>
    <td><input type="submit" name="a" value="Add Question" onClick="<%="set(this.form,'addQuestion',"+groupIdx+","+sectionIdx+",-1)"%>"></td>
  </tr>

<%
TQuestion[] questions = section.getQuestions();
if (questions == null) questions = new TQuestion[0];
for (int questionIdx=0; questionIdx<questions.length; questionIdx++) {
  TQuestion question = questions[questionIdx];
  String questionPrefix = sectionPrefix + ".question[" + questionIdx + "]";
%>
<%-- Question --%>
  <tr>
    <td rowspan="2">
<% if (destLink == 2 && destGroup == groupIdx && destSection == sectionIdx && destQuestion == questionIdx) {%>
  <a name="GO"></a>
<%}%>
      <strong>Question:</strong><br><textarea name="<%=questionPrefix%>.questionText" cols="65" rows="2"><%=question.getQuestionText()%></textarea></td>
    <td><strong>Weight:</strong><input type="text" name="<%=questionPrefix%>.questionWeight" value='<%=question.getQuestionWeight()%>'></td>
    <td><input type="submit" name="a" value="Delete Question" onClick="<%="set(this.form,'deleteQuestion',"+groupIdx+","+sectionIdx+","+questionIdx+")"%>"></td>
  </tr>
  <tr>
    <td><strong>Question Type:</strong><select name="<%=questionPrefix%>.questionType">
<%
String[] qOptions = {"Objective(Yes/No)","Subjective(1-4)","Testcase","Subjective(Yes/No)"};
for (int optIdx=0; optIdx<qOptions.length; optIdx++) {
%>
      <option value="<%=optIdx+1%>"<%if(question.getQuestionType()==optIdx+1){%> selected="selected"<%}%>> <%=qOptions[optIdx]%></option>
<%}%>
    </select></td>
    <td><input type="submit" name="a" value="Add Question" onClick="<%="set(this.form,'addQuestion',"+groupIdx+","+sectionIdx+","+questionIdx+")"%>"></td>
  </tr>
<%-- End Question --%>
<%-- End Section --%>
<%-- End Group --%>
<% }}} %>

</table>
<input type="submit" name="a" value="Save To NEW Scorecard" onClick="set(this.form,'save',0,0,0)"><br>
<% if (template.getStatus() == 0 && template.getId() != -1) { %>
<input type="submit" name="a" value="Save Existing Scorecard" onClick="set(this.form,'saveOld',0,0,0)"><br>
<% } %>
</form>

</body>
</html>
