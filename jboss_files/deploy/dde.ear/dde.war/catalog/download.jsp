<%@ page import="java.util.Collection"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="com.topcoder.dde.catalog.Download"%>
<%@ page import="com.topcoder.dde.util.Constants"%>
<%@ include file="/includes/util.jsp" %>
<%@ include file="/includes/session.jsp" %>

<jsp:useBean id="componentInfo" class="com.topcoder.dde.catalog.ComponentInfo" scope="request" />
<jsp:useBean id="versionInfo" class="com.topcoder.dde.catalog.ComponentVersionInfo" scope="request" />
<jsp:useBean id="sessionInfo" class="com.topcoder.web.common.SessionInfo" scope="request" />
<% Collection downloads = (Collection)request.getAttribute("downloads");%>

<html>

<head>
<title>Download software from the Component Catalog at TopCoder</title>

<link rel="stylesheet" type="text/css" href="/includes/tcs_style.css">
<jsp:include page="/includes/top.jsp" />
<script language="JavaScript" type="text/javascript" src="/scripts/javascript.js">
</script>

</head>

<body class="body">

<!-- Header begins -->
<jsp:include page="/includes/top.jsp">
<jsp:param name="TCDlevel" value="software" />
</jsp:include>
<jsp:include page="/includes/menu.jsp" >
    <jsp:param name="isSoftwarePage" value="true"/>
</jsp:include>
<!-- Header ends -->

<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr valign="top">

<!-- Left Column begins -->
        <td width="165" class="leftColumn">
            <jsp:include page="/includes/left.jsp" >
                <jsp:param name="level1" value="catalog"/>
                <jsp:param name="level2" value="download"/>
            </jsp:include>
        </td>
<!-- Left Column ends -->

<!-- Gutter begins -->
        <td width="15"><img src="/images/clear.gif" alt="" width="15" height="10" border="0"></td>
<!-- Gutter ends -->

<!-- Center Column begins -->
        <td width="99%">
            <table width="100%" border="0" cellpadding="0" cellspacing="0" align="center">
                <tr><td height="15"><img src="/images/clear.gif" alt="" width="10" height="15" border="0"/></td></tr>
                <tr><td class="normal"><img src="/images/headDownloadNow.gif" alt="Download Now" width="545" height="32" border="0" /></td></tr>
                <tr><td><h3><jsp:getProperty name="componentInfo" property="name"/> <span class="version"><jsp:getProperty name="versionInfo" property="versionLabel"/></span></h3></td></tr>
                <tr><td height="15"><img src="/images/clear.gif" alt="" width="10" height="15" border="0"/></td></tr>
            </table>

            <table width="100%" cellpadding="0" cellspacing="6" align="center" border="0">
                <tr valign="top">
                    <td align="center">
                        <table width="500" cellpadding="0" cellspacing="0" border="0" align="center">
                            <tr><td><img src="/images/downloadPackageHead.gif" alt="Download Package" width="500" height="29" border="0" /></td></tr>
                        </table>

                        <table width="500" border="0" cellspacing="4" cellpadding="0" align="center" class="register">
<!-- Column Setting Row -->
                            <tr valign="middle">
                                <td width="5"><img src="/images/clear.gif" width="5" height="1" border="0"/></td>
                                <td width="44%"><img src="/images/clear.gif" width="20" height="1" border="0"/></td>
                                <td width="2%"><img src="/images/clear.gif" width="20" height="1" border="0" /></td>
                                <td width="44%" ><img src="/images/clear.gif" width="20" height="1" border="0" /></td>
                                <td width="5"><img src="/images/clear.gif" width="5" height="1" border="0"/></td>
                            </tr>

<!--
    #### BEGIN FORM HEADER
    Note: The begin and end form tag has to wrap BOTH license loop and download loops
-->
<form name="frmDownload" action="/catalog/download" method="POST">
<input type="HIDDEN" name="comp" value="<jsp:getProperty name="componentInfo" property="id"/>"/>
<input type="HIDDEN" name="ver" value="<jsp:getProperty name="versionInfo" property="versionId"/>"/>
<!--
    END FORM HEADER
-->
    <textarea name="terms" rows="20" cols="80" readonly ><%=request.getAttribute(Constants.TERMS)%></textarea>

<!-- Download Package Instructions -->
                            <tr valign="middle">
                                <td><img src="/images/clear.gif" width="5" height="1" border="0"/></td>
                                <td colspan="3" class="registerText">Some TopCoder Software is packaged to run in certain development environments.
                                    Choose the Package below that best suits your environment. Installation instructions and other information are included with each Package.</td>
                                <td><img src="/images/clear.gif" width="5" height="1" border="0"/></td>
                            </tr>

<!-- Download Package Error Text -->
                            <tr valign="middle">
                                <td><img src="/images/clear.gif" width="5" height="1" border="0"/></td>
                                <td class="registerText"></td>
                                <td class="errorText"></td>
                                <td class="registerText"></td>
                                <td><img src="/images/clear.gif" width="5" height="1" border="0"/></td>
                            </tr>

<!-- Download Package -->
<% Download d;
int i = 0;
for (Iterator it = downloads.iterator(); it.hasNext();) {
  d = (Download)it.next();%>
        <!-- LOOP CONTENT BEGIN -->
                            <tr valign="middle">
                                <td><img src="/images/clear.gif" width="5" height="1" border="0"/></td>
                                <td class="registerText"></td>
                                <td class="registerText" nowrap><input type="RADIO" name="id" value="<%=d.getId()%>" <%=(i==0)?"checked":""%>>&nbsp;<%=d.getDescription()%></td>
                                <td class="registerText"></td>
                                <td><img src="/images/clear.gif" width="5" height="1" border="0"/></td>
                            </tr>
        <!-- LOOP CONTENT END -->
<% i++;} %>
                            <tr><td height="15" colspan="5"><img src="/images/clear.gif" alt="" width="10" height="15" border="0"/></td></tr>

                            <tr valign="middle">
                                <td><img src="/images/clear.gif" width="5" height="1" border="0"/></td>
                                <td class="registerText"></td>
                                <td class="registerTextCenter" ><input type="submit" name="a" value="Download"></td>
                                <td class="registerText"></td>
                                <td><img src="/images/clear.gif" width="5" height="1" border="0"/></td>
                            </tr>

                            <tr><td height="15" colspan="5"><img src="/images/clear.gif" alt="" width="10" height="15" border="0"/></td></tr>

                        </table>

                        <table width="500" cellpadding="0" cellspacing="0" border="0" align="center">
                            <tr><td><img src="/images/regFoot.gif" alt="" width="500" height="10" border="0" /></td></tr>
                            <tr><td height="15"><img src="/images/clear.gif" alt="" width="10" height="15" border="0"/></td></tr>
                        </table>
                    </td>
                </tr>

                <tr>
                    <td class="normal">If you experience any download problems, contact us at <a href="mailto:service@topcodersoftware.com?subject=Download Problems">service@topcodersoftware.com</a></td>
                </tr>

                <tr><td height="40"><img src="/images/clear.gif" alt="" width="10" height="40" border="0"/></td></tr>
            </table>
<!-- Center Column begins -->

<!-- Gutter begins -->
        <td width="15"><img src="/images/clear.gif" alt="" width="15" height="10" border="0"></td>
<!-- Gutter ends -->

<!--Right Column begins -->
        <td width="170">
            <table border="0" cellpadding="0" cellspacing="0">
                <tr><td height="15"><img src="/images/clear.gif" alt="" width="170" height="15" border="0" /></td></tr>
            </table>
        </td>
<!--Right Column ends -->

<!-- Gutter begins -->
        <td width="10"><img src="/images/clear.gif" alt="" width="10" height="10" border="0"></td>
<!-- Gutter ends -->

    </tr>
</table>

<!-- Footer begins -->
<jsp:include page="/includes/foot.jsp" flush="true" />
<!-- Footer ends -->

</form>
</body>
</html>
