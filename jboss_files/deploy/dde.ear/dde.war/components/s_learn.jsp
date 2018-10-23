<%@ include file="/includes/util.jsp" %>
<%@ include file="/includes/session.jsp" %>
<%@ include file="/includes/formclasses.jsp" %>

<%@ page import="com.topcoder.shared.util.ApplicationServer" %>
<%@ page import="com.topcoder.web.common.WebConstants" %>

<%
    // STANDARD PAGE VARIABLES
    String page_name = "s_learn.jsp";
    String action = request.getParameter("a");
    
    long tcsForumsID = WebConstants.TCS_FORUMS_ROOT_CATEGORY_ID;
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <title>TopCoder Software</title>
    <link rel="stylesheet" type="text/css" href="/includes/tcs_style.css"/>
    <script language="JavaScript" type="text/javascript" src="/scripts/javascript.js"></script>
    <jsp:include page="/includes/header-files.jsp" />
</head>

<body class="body">

<!-- Header begins -->
<jsp:include page="/includes/top.jsp">
<jsp:param name="TCDlevel" value="software" />
</jsp:include>
<jsp:include page="/includes/menu.jsp">
    <jsp:param name="isSoftwarePage" value="true"/>
</jsp:include>
<!-- Header ends -->

<!-- breadcrumb begins -->
<table width="100%" cellpadding="0" cellspacing="0" border="0">
    <tr><td width="100%" height="2" bgcolor="#000000">
        <img src="/images/clear.gif" alt="" width="10" height="2" border="0"/></td></tr>
</table>
<!-- breadcrumb ends -->

<table width="100%" border="0" cellpadding="0" cellspacing="0" align="center" class="middle">
<tr valign="top">

<!-- Left Column begins -->
<td width="165" class="leftColumn">
    <table border="0" cellspacing="0" cellpadding="0">
        <tr><td height="5"><img src="/images/clear.gif" alt="" width="10" height="5" border="0"/></td></tr>

        <!-- About Us Catagories -->
        <%@ include file="/includes/leftNavLearn.jsp" %>

    </table>
</td>
<td width="5" class="leftColumn"><img src="/images/clear.gif" alt="" width="5" height="10" border="0"/></td>
<!-- Left Column ends -->

<!-- Gutter 1 begins -->
<td width="15"><img src="/images/clear.gif" alt="" width="15" height="10" border="0"/></td>
<!-- Gutter 1 ends -->

<!-- Middle Column begins -->
<td width="100%">
    <table width="100%" border="0" cellpadding="0" cellspacing="0">
        <tr><td height="15"><img src="/images/clear.gif" alt="" width="10" height="15" border="0"/></td></tr>
        <tr>
            <td class="normal"><img src="/images/hd_products.png" alt="Products" border="0"/>
            </td>
            <td class="normal" rowspan="2">
                <a href="s_subscriptions.jsp"><img src="/images/tcs_prod_buy_now.gif" alt="Buy now" width="200" height="55" border="0"/></a>
            </td>
        </tr>
        <tr><td><h3>Overview</h3></td></tr>
    </table>

    <table width="100%" border="0" cellpadding="0" cellspacing="0">
        <tr>
            <td class="normal">
                <h3>
                    <a href="c_showroom.jsp"><img src="/images/tcs_prod_catalog.gif" alt="TCS Component Catalog" width="419" height="223" border="0" align="right"/></a>Component
                    Catalog</h3>

                <p>The best way to drive down the cost of software development is through
                    <a href="s_reuse.jsp">re-use.</a> The
                    component catalog is a tool for increasing re-use and developer <a href="s_productivity.jsp">productivity</a>
                    within
                    an organization. The component catalog continues to grow on a weekly basis as component requirements
                    are generated
                    through requests from catalog customers, research from TopCoder Product Managers and from
                    application development.</p>
            </td>
        </tr>
    </table>

    <hr width="100%" color="#999999" size="1" noshade="noshade"/>

    <table width="100%" border="0" cellpadding="0" cellspacing="0">
        <tr valign="top">
            <td class="normal" width="99%">
                <h3>Component Catalog Subscriptions</h3>

                <p>Subscriptions are available in three levels to meet the needs of individual developers as well as
                    small and large enterprises:</p>

                <p><a href="s_subscriptions.jsp"><strong>Personal</strong></a>&#151;The personal subscription is
                    available for individuals who wish to use the catalog
                    for non-commercial use and for educational institutions to use for academic purposes. Developers can
                    improve their
                    software development skills by reviewing and using components that were designed, developed and
                    reviewed by accomplished
                    <a href="http://www.topcoder.com" target="_blank">TopCoder</a> members using our unique
                    <a href="s_methodology.jsp">Component Development Methodology.</a> Subscribers can also make use of
                    the
                    <a href="http://<%=ApplicationServer.FORUMS_SERVER_NAME%>/?module=Category&categoryID=<%=tcsForumsID%>">Customer Forums</a> to ask questions and request enhancements to
                    components. In addition
                    all subscribers can recommend <a href="c_comp_request.jsp">new component ideas.</a></p>

                <p><a href="s_subscriptions.jsp"><strong>Professional</strong></a>&#151;The professional subscription is
                    designed for small enterprises. This
                    subscription includes the benefits of the Personal Subscription plus the ability for all components
                    to be integrated with
                    internal production applications. Components cannot be redistributed in any way, including through a
                    services offering,
                    commercial sale, open source or freeware distribution.</p>

                <p><a href="s_subscriptions.jsp"><strong>Enterprise</strong></a>&#151;The enterprise subscription is
                    designed for organizations with more than 10
                    software developers or enterprises in need of additional services such as integration, training or
                    application
                    development. This subscription includes all of the benefits of the Professional and Personal
                    subscriptions bundled with
                    priority technical support and volume pricing. For more information on the enterprise subscription
                    call
                    877.867.2633 or email <a href="mailto:sales@topcodersoftware.com">sales@topcodersoftware.com.</a>
                </p>
            </td>

            <td width="200" align="center">
                <img src="/images/tcs_prod_comp_icon.gif" alt="Components" width="200" height="100" border="0"/></td>
        </tr>
    </table>

    <hr width="100%" color="#999999" size="1" noshade="noshade"/>

    <table width="100%" border="0" cellpadding="0" cellspacing="0">
        <tr valign="top">
            <td class="normal" width="99%">
                <h3>Application Development</h3>

                <p>TopCoder Software Application Development teams, consisting of rated
                    <a href="http://www.topcoder.com" target="_blank">TopCoder</a>
                    members, are available to assist companies in developing and integrating applications built from the
                    component catalog using the
                    TopCoder <a href="/applications/methodology.jsp">Application Development Methodology.</a>
                    For more information on application development call 877.867.2633 or email
                    <a href="mailto:sales@topcodersoftware.com">sales@topcodersoftware.com.</a></p>
            </td>

            <td width="200" align="center">
                <img src="/images/tcs_prod_appl_icon.gif" alt="Applications" width="200" height="95" border="0"/></td>
        </tr>
    </table>

    <hr width="100%" color="#999999" size="1" noshade="noshade"/>

    <table width="100%" border="0" cellpadding="0" cellspacing="0">
        <tr valign="top">
            <td class="normal" width="100%">
                <h3>
                    <a href="c_prodTools.jsp?comp=600191"><img src="../images/tcs_prod_prodtools.gif" alt="TCS Productivity Tools" width="317" height="239" border="0" align="right"/></a>Productivity
                    Tools</h3>

                <p>TopCoder Software is developing productivity tools to help improve the development process. The first
                    tool is the
                    <a href="c_prodTools.jsp?comp=600191">TopCoder Rules Engine.</a> The Rules Engine centralizes and
                    manages business
                    logic for use in multiple applications. The Rules Engine is available for purchase separately from
                    the component catalog.
                    To inquire about the TopCoder Rules Engine call 877.867.2633 or email
                    <a href="mailto:sales@topcodersoftware.com">sales@topcodersoftware.com.</a></p>
            </td>
        </tr>
    </table>

    <hr width="100%" color="#999999" size="1" noshade="noshade"/>

    <table width="100%" border="0" cellpadding="0" cellspacing="0">
        <tr valign="top">
            <td class="normal" width="100%">
                <p><strong>Interested in components?</strong><br/>
                    Weigh in with ideas and suggestions by <a href="http://<%=ApplicationServer.FORUMS_SERVER_NAME%>/?module=Category&categoryID=<%=tcsForumsID%>">collaborating</a> with others.
                </p>
            </td>
        </tr>
        <tr><td><img src="/images/clear.gif" alt="" width="10" height="40" border="0"/></td></tr>
    </table>
</td>

<!-- Middle Column ends -->

<!-- Gutter 3 begins -->
<td width="10"><img src="/images/clear.gif" alt="" width="10" height="10" border="0"/></td>
<!-- Gutter 3 ends -->
</tr>
</table>

<!-- Footer begins -->
<jsp:include page="/includes/foot.jsp" flush="true"/>
<!-- Footer ends -->

</body>
</html>
