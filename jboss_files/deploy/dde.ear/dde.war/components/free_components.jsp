<%@ page import="javax.naming.*" %>
<%@ page import="javax.ejb.CreateException" %>
<%@ page import="java.io.*" %>
<%@ page import="java.rmi.*" %>
<%@ page import="javax.rmi.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.lang.reflect.*" %>

<%@ page import="com.topcoder.shared.util.ApplicationServer" %>

<%@ include file="/includes/util.jsp" %>
<%@ include file="/includes/session.jsp" %>
<%@ include file="/includes/formclasses.jsp" %>


<%
    // STANDARD PAGE VARIABLES
    String page_name = "s_index.jsp";
    String action = request.getParameter("a");

	long tcsForumsID = WebConstants.TCS_FORUMS_ROOT_CATEGORY_ID;
%>

<%
    String code = request.getParameter("code");
    String lastName = request.getParameter("lastName");
    String firstName = request.getParameter("firstName");
    String emailAddress = request.getParameter("emailAddress");
    String component = request.getParameter("s");
    String country = request.getParameter("country");
    String contactMe = request.getParameter("contactMe");
    if(contactMe == null || contactMe.equals("")){
      contactMe = "true";
    }
    int country_code = 0;
    try{
       country_code = Integer.parseInt(country);
       if(country_code == 434 ||
          country_code == 368 ||
          country_code == 408 ||
          country_code == 192 ||
          country_code == 736 ||
          country_code == 760 ||
          country_code ==  364)
       {
          //cannot export to these countries
          country_code = -2;
       }
    }
    catch(java.lang.NumberFormatException nfe){
       country_code = -1;
    }

    boolean error = false;

    if(lastName == null){
        lastName = "";
    }
    if(firstName == null){
        firstName = "";
    }
    boolean validEmail = false;
    if(emailAddress == null){
        emailAddress = "";
    }
    else{
        com.topcoder.message.email.validator.SyntaxValidator syntax =
              new com.topcoder.message.email.validator.SyntaxValidator();
        validEmail = syntax.validateSyntax(emailAddress);
    }
    if (code == null) {
        code = "";
    }
    if (action != null) {
        if (action.equalsIgnoreCase("post") && lastName != null && !lastName.equals("") &&
            firstName != null && !firstName.equals("") &&
            emailAddress != null && !emailAddress.equals("")  && validEmail
            && component != null && country_code != -1 && country_code != -2) {

                     int contactMeId = 0;
                     if(contactMe.equals("checked")){
                         contactMeId = 1;
                     }
                     boolean success = USER_MANAGER.sampleInquiry(firstName, lastName,
                              emailAddress, component, country_code, contactMeId);
                     if(success && component.equals("java")){
                          response.sendRedirect("/samples/TopCoder_Java_Samples.zip");
                     }

                     else if(success){
                          response.sendRedirect("/samples/TopCoder_.NET_Samples.zip");
                     }
                     //return;
        }
        else{
            error = true;
        }
    }
    else{
	action = "";
    }
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <title>FREE .NET&#8482; and Java&#8482; Software Components at TopCoder Software</title>
    <link rel="stylesheet" type="text/css" href="/includes/tcs_style.css" />
    <script language="JavaScript" type="text/javascript" src="/scripts/javascript.js"></script>
    <jsp:include page="/includes/header-files.jsp" />
</head>

<body class="body">
<script language="JavaScript" type="text/javascript">
   function submitForm(subscription)
   {
      document.Form1.action = "free_components.jsp?a=post&s=" + subscription;
      document.Form1.submit();
   }

</script>
<!-- Header begins -->
<jsp:include page="/includes/top.jsp">
<jsp:param name="TCDlevel" value="software" />
</jsp:include>
<jsp:include page="/includes/menu.jsp" >
    <jsp:param name="isSoftwarePage" value="true"/>
</jsp:include>
<!-- Header ends -->

<table width="100%" border="0" cellpadding="0" cellspacing="0" class="middle">
    <tr valign="top">

<!-- Left Column begins -->
        <td width="165" class="leftColumn">
        <jsp:include page="/includes/left.jsp" >
            <jsp:param name="level1" value="components"/>
            <jsp:param name="level2" value="free_components"/>
        </jsp:include>
        </td>
<!-- Left Column ends -->

<!-- Gutter 1 begins -->
        <td width="25"><img src="/images/clear.gif" alt="" width="25" height="10" border="0" /></td>
<!-- Gutter 1 ends -->

<!-- Middle Column begins -->
        <td width="99%">
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr><td height="15"><img src="/images/clear.gif" alt="" width="10" height="15" border="0" /></td></tr>
                <tr><td class="normal"><img src="/images/hd_components.png" alt="Components" border="0" /></td></tr>
                <tr><td align="center"><img src="/images/header_look_under_hood.gif" alt="Look under the hood. Kick the tires." width="442" height="53" border="0" /></td></tr>
            </table>

            <table width="95%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                    <td class="bodyText">
                        <strong>To illustrate the rigorous process that goes into every TopCoder component we have provided these sample
                        components for free. They are full working versions for your personal use. All documentation is included. We've also
                        provided <a href="#links">links</a> to the tools you need to get a .NET&#8482; or Java&#8482; environment up and running.</strong><br/>
                        <img src="/images/clear.gif" alt="" width="10" height="10" border="0" />
                    </td>
                </tr>
            </table>

            <table border="0" cellpadding="0" cellspacing="0">
                <tr valign="top">
                    <td width="70%" class="bodyText">
                        <p>These samples are only a few of the dozens of components available in our .NET&#8482; and Java&#8482; <a href="/catalog/c_showroom.jsp">component catalogs.</a> To
                        access all of our components or to use them in a commercial application, purchase a <a href="/components/subscriptions.jsp">subscription</a> to either of the catalogs.</p>

                        <p>Use our <a href="http://<%=ApplicationServer.FORUMS_SERVER_NAME%>/?module=Category&categoryID=<%=tcsForumsID%>">Software Forums</a> to discuss component features and usage or to report bugs. For issues with the downloads or to speak
                        with a component product manager, contact us at <a href="mailto:service@topcodersoftware.com">service@topcodersoftware.com.</a></p>

                        <hr width="100%" size="1" color="#999999" noshade>

                        <table border="0" cellpadding="0" cellspacing="0" width="360">
                            <tr valign="top">
                                <td class="registerTitle" width="99%">Register for the Free Components</td>
                                <td class="registerSmall" width="1%" nowrap><a href="/components/s_privacy.jsp">Privacy Policy</a></td>
                            </tr>
                        </table>

                        <div align="center">
                        <img src="/images/clear.gif" alt="" width="10" height="5" border="0" />
                        <form id=Form1 name=Form1 method="post"><table border="0" cellpadding="3" cellspacing="0" width="100%">

<!-- Possible error text goes here -->
                            <%
                            if(error){
                            %>
                                <tr valign="middle">
                                   <td class="errorSymbol"></td>
                                   <td class="registerError">You did not fill out a field correctly</td>
                               </tr>
                            <%
                              }
                            %>
<!-- Possible error text goes here -->

                            <tr valign="middle">
                                <td class="errorSymbol">

                                <% if(action.equals("post") && firstName.equals("")){ %>
                                      <img src="../images/errorSymbol.gif" alt="&gt;" width="22" height="22" hspace="10" border="0" />
                                <%
                                    }
                                %>
                                </td>
                                <td class="registerLabel">First Name <span class="registerLabelRequired">(required)</span><br/>
                                    <input type="text" size="33" name="firstName" value="<%=firstName%>" class="registerElement" /></td>
                            </tr>

                            <tr valign="middle">
                                <td class="errorSymbol">
                                <% if(action.equals("post") && lastName.equals("")){ %>
                                    <img src="/images/errorSymbol.gif" alt="&gt;" width="22" height="22" hspace="10"
                                    border="0" />

                                <%
                                    }
                                %>
                                </td>
                                <td class="registerLabel">Last Name <span class="registerLabelRequired">(required)</span><br/>
                                    <input type="text" size="33" name="lastName" value="<%=firstName%>" class="registerElement" /></td>
                            </tr>

                            <tr valign="middle">
                                <td class="errorSymbol">

                                <% if(action.equals("post") && (!validEmail || emailAddress.equals(""))){ %>
                                     <img src="/images/errorSymbol.gif" alt="&gt;" width="22" height="22" hspace="10"
                                         border="0" />
                                <%
                                    }
                                %>
                                </td>
                                <td class="registerLabel">Email Address <span class="registerLabelRequired">(required)</span><br/>
                                    <input type="text" size="33" name="emailAddress" value="<%=emailAddress%>" class="registerElement" /></td>
                            </tr>

                            <tr valign="middle">
                                <td class="errorSymbol"></td>
                                <td class="registerLabel"><input type="checkbox" size="5" name="contactMe" value="checked" checked="<%=contactMe%>" class="registerElement" /> I would like to receive news, updates, and other special offers from TopCoder.</td>
                            </tr>

                            <% if(action.equals("post") && country_code==-2){%>
                            <tr valign="middle">
                                <td class="errorSymbol"></td>
                                <td class="registerError">Your country does not have download permissions</td>
                            </tr>
                           <% country_code = -1;
                             } %>

                            <tr valign="middle">
                                <td class="errorSymbol">
                                <% if(action.equals("post") && country_code==-1){%>
                                    <img src="/images/errorSymbol.gif" alt="&gt;" width="22" height="22" hspace="10" border="0" />
                                <%
                                    }
                                %>
                                </td>
                                <td class="registerElement">
                                   <%@ include file="/includes/countryList.jsp" %>
                                </td>
                            </tr>
                        </table>
                        <img src="/images/clear.gif" alt="" width="10" height="10" border="0" />
                        </div>

                        <strong>Select Download:</strong>

                        <table border="0" cellpadding="0" cellspacing="8" width="100%">
                            <tr valign="top">
                                <td class="small" width="168" align="center"><a href="javascript:submitForm('dotnet')">
                                    <img src="/images/button_samp_comp_dotnet.gif" alt=".NET Components" width="145" height="66" border="0" vspace="2" /></a><br/>
                                    File Size: 1.1MB<br/><br/></td>

                                <td width="1" bgcolor="#999999" rowspan="3"><img src="/images/clear.gif" alt="" width="1" border="0" /></td>

                                <td class="small" width="167" align="center"><a href="javascript:submitForm('java')">
                                    <img src="/images/button_samp_comp_java.gif" alt="java Components" width="145" height="66" border="0" vspace="2" /></a><br/>
                                    File Size: 1.5MB<br/><br/></td>
                            </tr>

                            <tr valign="top">
                                <td class="bodyText">
                                    <strong>Components included in the .NET&#8482; package:</strong><br/>
                                    <a href="/catalog/c_component.jsp?comp=6511227">Priority Queue</a><br/>
                                    <a href="/catalog/c_component.jsp?comp=4311341">Logging Wrapper</a><br/>
                                    <a href="/catalog/c_component.jsp?comp=4208949">Email Engine</a><br/>
                                    <a href="/catalog/c_component.jsp?comp=5904431">Math Expression Evaluator</a><br/><br/>
                                </td>

                                <td class="bodyText">
                                    <strong>Components included in the Java&#8482; package:</strong><br/>
                                    <a href="/catalog/c_component.jsp?comp=7311989">Priority Queue</a><br/>
                                    <a href="/catalog/c_component.jsp?comp=5710093">Base Exception</a><br/>
                                    <a href="/catalog/c_component.jsp?comp=2300015">Logging Wrapper</a><br/>
                                    <a href="/catalog/c_component.jsp?comp=2804505">Simple Cache</a><br/><br/>
                                </td>
                            </tr>

                            <tr valign="top">
                                <td class="bodyText">
                                    <a name="links"><strong>.NET&#8482; Requirements</strong></a><br/>
                                    <a href="http://msdn.microsoft.com/netframework/productinfo" target="_blank">Microsoft.NET Framework 1.0</a><br/>
                                    <a href="http://nant.sourceforge.net/" target="_blank">NAnt 0.7.9+</a><br/>
                                    <a href="http://ndoc.sourceforge.net/" target="_blank">NDoc 1.1+</a><br/>
                                    <a href="http://sourceforge.net/projects/nunit" target="_blank">NUnit 2.0+</a><br/>
                                    <a href="http://nunit2report.sourceforge.net/" target="_blank">NUnitReport 1.2+</a><br/>
                                </td>

                                <td class="bodyText">
                                    <strong>Java&#8482; Requirements</strong><br/>
                                    <a href="http://java.sun.com/j2ee/download.html" target="_blank">JDK 1.3+</a><br/>
                                    <a href="http://ant.apache.org/" target="_blank">Apache Ant 1.5.1+</a><br/>
                                    <a href="http://www.junit.org" target="_blank">JUnit 3.8+</a><br/>
                                </td>
                            </tr>
                         </table></form>

                        <hr width="100%" size="1" color="#999999" noshade>

                       <strong>Other helpful links:</strong>

                        <table border="0" cellpadding="0" cellspacing="8" width="100%">
                            <tr valign="top">
                                <td class="bodyText">
                                    <a href="http://www.gentleware.com/products/download.php4" target="_blank">Poseidon 2.0</a><br/>
                                </td>
                            </tr>
                        </table>

                        <img src="/images/clear.gif" alt="" width="360" height="1" border="0" />
                    </td>

                    <td width="2%"><img src="/images/clear.gif" alt="" width="10" height="10" border="0" /></td>

                    <td width="20%" class="bodyText">
                        <table border="0" cellpadding="3" cellspacing="0" class="sidebarFrame" width="175">
                            <tr><td class="sidebarTitle">About the Components</td></tr>

                            <tr valign="top">
                                <td class="sidebarText">
                                    <p>TopCoder Components are built using the TopCoder Software Component Development Methodology. The <a href="/components/methodology.jsp">methodology</a>
                                    requires that all components have the following consistent and standard deliverables:</p>

                                    <strong>Documentation:</strong>
                                    <ul>
                                        <li>Component Specification</li>
                                        <li>Use Case Diagrams</li>
                                        <li>Class Diagrams</li>
                                        <li>Sequence Diagrams</li>
                                    </ul>

                                    <strong>Test Suite:</strong>
                                    <ul>
                                        <li>Unit Test Cases</li>
                                        <li>Accuracy Test Cases</li>
                                        <li>Failure Test Cases</li>
                                        <li>Stress Test Cases</li>
                                    </ul>

                                    <strong>Source:</strong>
                                    <ul>
                                        <li>Documented Source Code</li>
                                        <li>Documented Test Suite Source Code</li>
                                    </ul>

                                    <strong>Build Environment:</strong>
                                    <ul>
                                        <li>Build script for compiling, testing and deploying</li>
                                        <li>Common directory structure for component source, libraries, documentation, test suite reports and configuration files</li>
                                    </ul>
                                </td>
                            </tr>
                        </table>

                        <img src="/images/clear.gif" alt="" width="175" height="1" border="0" />

                    </td>
                </tr>
            </table>

            <p><br></p>

        </td>
<!-- Middle Column ends -->

<!-- Gutter 2 begins -->
        <td width="15"><img src="/images/clear.gif" alt="" width="15" height="10" border="0" /></td>
<!-- Gutter 2 ends -->

<!-- Right Column begins -->
        <td width="170">
        <jsp:include page="/includes/right.jsp" >
            <jsp:param name="level1" value="free_components"/>
        </jsp:include>
        </td>
<!--Right Column ends -->

<!-- Gutter 3 begins -->
        <td width="10"><img src="/images/clear.gif" alt="" width="10" height="10" border="0" /></td>
<!-- Gutter 3 ends -->

    </tr>
</table>

<!-- Footer begins -->
<jsp:include page="/includes/foot.jsp" flush="true" />
<!-- Footer ends -->

</body>
</html>
