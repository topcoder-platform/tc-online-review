<%@ page import="com.topcoder.shared.util.ApplicationServer" %>
<%--<%@ page import="javax.naming.*,
           com.topcoder.dde.util.ApplicationServer" %>
<%@ page import="javax.ejb.CreateException" %>
<%@ page import="java.io.*" %>
<%@ page import="java.rmi.*" %>
<%@ page import="javax.rmi.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.lang.reflect.*" %>--%>

<%@ include file="/includes/util.jsp" %>
<%@ include file="/includes/session.jsp" %>
<%@ include file="/includes/formclasses.jsp" %>
<%@ taglib uri="/WEB-INF/tc-webtags.tld" prefix="tc-webtag" %>

<%
    // STANDARD PAGE VARIABLES
    String page_name = "s_definition.jsp";
    String action = request.getParameter("a");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <title>Software Components form the basis of the applications built by TopCoder</title>
    <link rel="stylesheet" type="text/css" href="/includes/tcs_style.css" />
    <script language="JavaScript" type="text/javascript" src="/scripts/javascript.js"></script>
    <jsp:include page="/includes/header-files.jsp" />
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

<table width="100%" border="0" cellpadding="0" cellspacing="0" class="middle">
    <tr valign="top">

<!-- Left Column begins -->
        <td width="165" class="leftColumn">
        <jsp:include page="/includes/left.jsp" >
            <jsp:param name="level1" value="customers"/>
            <jsp:param name="level2" value="customers"/>
        </jsp:include>
        </td>
<!-- Left Column ends -->

<!-- Gutter 1 begins -->
        <td width="25"><img src="/images/clear.gif" alt="" width="25" height="10" border="0" /></td>
<!-- Gutter 1 ends -->

<!-- Middle Column begins -->
        <td align="left">
            <img src="/images/clear.gif" width="1" height="15" alt="" border="0"><br />
            <img src="/images/hd_cust_our.png" alt="Our Customers" border="0" /><br />
        	
        	<br /><br />
            




           
            


<!-- AOL begins -->
            <table border="0" cellpadding="0" cellspacing="0" width="95%">
                <tr valign="top">
                    <td width="170"><img src="/images/clients/aol_logo.png" alt="AOL" border="0" /></td>
                    <td width="15"><img src="/images/clear.gif" alt="" width="15" height="10" border="0" /></td>
                    <td width="99%" class="bodyText">AOL and its subsidiaries operate a leading network of Web brands and the largest Internet access subscription service in the United States. Web brands include the AOL.com&#174; website, AIM&#174;, MapQuest&#174; and Netscape&#174;. AOL offers a range of digital services in the areas of education, safety and security, communications and music. The company also has operations in Europe and Canada. AOL LLC is a majority-owned subsidiary of Time Warner Inc. and is based in Dulles, Virginia.<br /><br />
                    <A href="<tc-webtag:linkTracking link="http://developer.aol.com/" refer="customer_index"/>" target="_blank">homepage</A>
                     </td>
                </tr>
            </table>
<!-- AOL ends -->

             <hr width="640" size="1px" noshade="noshade" />
             
<!-- Borders begins -->
            <table border="0" cellpadding="0" cellspacing="0" width="95%">
                <tr valign="top">
                    <td width="170"><img src="/images/clients/borders_logo.gif" alt="Borders" border="0" /></td>
                    <td width="15"><img src="/images/clear.gif" alt="" width="15" height="10" border="0" /></td>
                    <td width="99%" class="bodyText">With more than 32,000 employees worldwide, this company provides its customers with the books, music, movies, and other entertainment items they love.<br /><br />
                    <A href="<tc-webtag:linkTracking link="http://www.amazon.com/exec/obidos/tg/browse/-/577394/103-3590679-6259839" refer="customer_index"/>" target="_blank">homepage</A>
                     </td>
                </tr>
            </table>
<!-- Borders ends -->

             <hr width="640" size="1px" noshade="noshade" />

<!-- Burlington begins -->
            <table border="0" cellpadding="0" cellspacing="0" width="95%">
                <tr valign="top">
                    <td width="170"><img src="/images/clients/burlington_logo.gif" alt="Burlington Coat Factory" border="0" /></td>
                    <td width="15"><img src="/images/clear.gif" alt="" width="15" height="10" border="0" /></td>
                    <td width="99%" class="bodyText">Burlington Coat Factory Warehouse Corporation is a national department store retail chain offering current, high quality, designer merchandise at up to 60% less than other department store prices.<br /><br />
                    <A href="<tc-webtag:linkTracking link="http://corporate.burlingtoncoatfactory.com/" refer="customer_index"/>" target="_blank">homepage</A>
                     </td>
                </tr>
            </table>
<!-- Burlington ends -->

             <hr width="640" size="1px" noshade="noshade" />
             
<!-- Caliper begins -->
            <table cellspacing="0" cellpadding="0" border="0" width="95%">
                <tr valign="top">
                    <td width="170"><img src="/images/clients/caliper_logo.gif" alt="Caliper" border="0" /></td>
                    <td width="15"><img src="/images/clear.gif" alt="" width="15" height="10" border="0" /></td>
                    <td width="99%" class="bodyText">Caliper has advised more than 25,000 companies - including FedEx, Avis and some of the fastest growing smaller firms - on Employee Selection, Employee Development, Team Building and Organizational Development. <br /><br />
                    <a href="<tc-webtag:linkTracking link="http://www.calipercorp.com/" refer="customer_index"/>" target="_blank">homepage</a>
                     </td>
                </tr>
            </table>
<!-- Caliper ends -->
             
			 <hr width="640" size="1px" noshade="noshade" />
             
<!-- Detroit Edison begins -->
            <table border="0" cellpadding="0" cellspacing="0" width="95%">
                <tr valign="top">
                    <td width="170"><img src="/images/clients/detroit_edison_logo.gif" alt="Detroit Edison" border="0" /></td>
                    <td width="15"><img src="/images/clear.gif" alt="" width="15" height="10" border="0" /></td>
                    <td width="99%" class="bodyText">Founded in 1903, this investor-owned electric utility generates, transmits, and distributes electricy to 2.1 million customers in Southeastern Michigan.<br /><br />
                    <A href="<tc-webtag:linkTracking link="http://my.dteenergy.com/main/index.do" refer="customer_index"/>" target="_blank">homepage</A>
                     </td>
                </tr>
            </table>
<!-- Detroit Edison ends -->

             <hr width="640" size="1px" noshade="noshade" />
             
<!-- Direct Energy begins -->
            <table border="0" cellpadding="0" cellspacing="0" width="95%">
                <tr valign="top">
                    <td width="170"><img src="/images/clients/directenergy_logo.gif" alt="Direct Energy" border="0" /></td>
                    <td width="15"><img src="/images/clear.gif" alt="" width="15" height="10" border="0" /></td>
                    <td width="99%" class="bodyText">Direct Energy is North America's largest competitive energy solutions provider, with over five million residential and commercial customer relationships. Direct Energy provides customers with choice and support in managing energy costs through a portfolio of innovative products and services.<br /><br />
                    <A href="<tc-webtag:linkTracking link="http://www.directenergy.com/" refer="customer_index"/>" target="_blank">homepage</A>
                     </td>
                </tr>
            </table>
<!-- Direct Energy ends -->

             <hr width="640" size="1px" noshade="noshade" />
             
<%-- ESPN begins 
            <table border="0" cellpadding="0" cellspacing="0" width="95%">
                <tr valign="top">
                    <td width="170"><img src="/images/clients/espn_logo.png" alt="ESPN" border="0" /></td>
                    <td width="15"><img src="/images/clear.gif" alt="" width="15" height="10" border="0" /></td>
                    <td width="99%" class="bodyText">ESPN, Inc. is the world�s leading multinational, multimedia sports entertainment company featuring a portfolio of over 40 multimedia sports assets. The company is comprised of seven domestic television networks (ESPN, ESPN2, ESPN Classic, ESPNEWS, ESPN Deportes, ESPN Now, ESPN Today), ESPN HD, ESPN Regional Television, ESPN International (networks and syndication), ESPN Radio, ESPN.com, ESPN The Magazine, SportsTicker, ESPN Enterprises, ESPN Zones (sports-themed restaurants), and other growing new businesses including ESPN Broadband, ESPN Video-on-Demand, ESPN Interactive and ESPN PPV.<br /><br />
                    <A href="<tc-webtag:linkTracking link="http://www.espn.com/" refer="customer_index"/>" target="_blank">homepage</A>
                     </td>
                </tr>
            </table>

             <hr width="640" size="1px" noshade="noshade" />
ESPN ends 
--%>

<!-- Ferguson begins -->
            <table border="0" cellpadding="0" cellspacing="0" width="95%">
                <tr valign="top">
                    <td width="170"><img src="/images/clients/ferguson_logo.gif" alt="Ferguson" border="0" /></td>
                    <td width="15"><img src="/images/clear.gif" alt="" width="15" height="10" border="0" /></td>
                    <td width="99%" class="bodyText">Ferguson distributes plumbing, pipe, valves and fittings, heating and cooling, and waterworks products to the construction industry through a coast-to-coast network of showrooms, counters and distribution centers.<br /><br />
                    <A href="<tc-webtag:linkTracking link="http://www.ferguson.com/" refer="customer_index"/>" target="_blank">homepage</A>
                     </td>
                </tr>
            </table>
<!-- Ferguson ends -->

             <hr width="640" size="1px" noshade="noshade" />

<!-- Franklin Mortgage begins -->
            <table border="0" cellpadding="0" cellspacing="0" width="95%">
                <tr valign="top">
                    <td width="170"><img src="/images/clients/frankin_mortgage_logo.gif" alt="Franklin Mortgage" border="0" /></td>
                    <td width="15"><img src="/images/clear.gif" alt="" width="15" height="10" border="0" /></td>
                    <td width="99%" class="bodyText">Acquiring and selling non-conforming mortgage loans on residential properties, this firm is one of the largest privately held lenders in the nation.<br /><br />
                     </td>
                </tr>
            </table>
<!-- Franklin Mortgage ends -->

             <hr width="640" size="1px" noshade="noshade" />

<!--Gentiva begins -->
            <table border="0" cellpadding="0" cellspacing="0" width="95%">
                <tr valign="top">
                    <td width="170"><img src="/images/clients/gentiva_logo.gif" alt="Gentiva" border="0" /></td>
                    <td width="15"><img src="/images/clear.gif" alt="" width="15" height="10" border="0" /></td>
                    <td width="99%" class="bodyText">With their vast network of licensed and certified home health care agencies, Gentiva employs an expansive team of caregivers, and provide health services for a diverse group of patients each year.<br /><br />
                    <A href="<tc-webtag:linkTracking link="http://www.gentiva.com/" refer="customer_index"/>" target="_blank">homepage</A>
                     </td>
                </tr>
            </table>
<!-- Gentiva ends -->

             <hr width="640" size="1px" noshade="noshade" />
             
<!--IMS Health begins -->
            <table border="0" cellpadding="0" cellspacing="0" width="95%">
                <tr valign="top">
                    <td width="170"><img src="/images/clients/ims_logo.gif" alt="IMS Health" border="0" /></td>
                    <td width="15"><img src="/images/clear.gif" alt="" width="15" height="10" border="0" /></td>
                    <td width="99%" class="bodyText">IMS is the global source for pharmaceutical market intelligence, providing critical information, analysis and services that drive decisions and shape strategies. Just about every major pharmaceutical and biotech company in the world is a customer of IMS. The company's unique mix of experience and expertise makes it the right choice for help in optimizing portfolios, ensuring successful launches, managing brands, and improving the effectiveness of sales teams. A key component of the company's market intelligence is its ability to capture and analyze detailed information on prescriptions. TopCoder is working with IMS to build an automated application that improves this process.<br /><br />
                    <A href="<tc-webtag:linkTracking link="http://www.imshealth.com/" refer="customer_index"/>" target="_blank">homepage</A>
                     </td>
                </tr>
            </table>
<!-- IMS Health ends -->

             <hr width="640" size="1px" noshade="noshade" />
             
<!-- NEJ begins -->
            <table border="0" cellpadding="0" cellspacing="0" width="95%">
                <tr valign="top">
                    <td width="170"><img src="/images/clients/nejLogo.jpg" alt="NEJ" border="0" /></td>
                    <td width="15"><img src="/images/clear.gif" alt="" width="15" height="10" border="0" /></td>
                    <td width="99%" class="bodyText">NEJ, Inc. is an apparel and merchandise solutions company that operates three major divisions: Wholesale/Off-Price, Corporate Identity Merchandise, and Licensing. NEJ delivers straightforward, timely merchandising solutions to help companies build and protect their brands. With multiple product lines under multiple brand names, and 200,000 square feet in facilities to manage, immediate access to inventory data is critical to the success of the company. TopCoder is working with NEJ to implement a new application that effectively manages and tracks its inventory.<br /><br />
                    <A href="<tc-webtag:linkTracking link="http://nejinc.com/about/about.htm" refer="customer_index"/>" target="_blank">homepage</A>
                     </td>
                </tr>
            </table>
<!-- NEJ ends -->

             <hr width="640" size="1px" noshade="noshade" />
             
<!-- Oxford begins -->
            <table border="0" cellpadding="0" cellspacing="0" width="95%">
                <tr valign="top">
                    <td width="170"><img src="/images/clients/oxford_logo.gif" alt="Oxford" border="0" /></td>
                    <td width="15"><img src="/images/clear.gif" alt="" width="15" height="10" border="0" /></td>
                    <td width="99%" class="bodyText">Learn about Oxford programs, products and services including Access to Care, Preventive and Practical resources.<br /><br />
                    <A href="<tc-webtag:linkTracking link="https://www.oxhp.com/" refer="customer_index"/>" target="_blank">homepage</A>
                     </td>
                </tr>
            </table>
<!-- Oxford ends -->

             <hr width="640" size="1px" noshade="noshade" />

<!-- Philip Morris begins -->
            <table border="0" cellpadding="0" cellspacing="0" width="95%">
                <tr valign="top">
                    <td width="170"><img src="/images/clients/philipmorris_logo.gif" alt="Philip Morris" border="0" /></td>
                    <td width="15"><img src="/images/clear.gif" alt="" width="15" height="10" border="0" /></td>
                    <td width="99%" class="bodyText">Hiring the best people, producing the highest quality products, and giving back to the community helped grow this 101-year-old company into the leading manufacturer of cigarettes in the United States.<br /><br />
                    <A href="<tc-webtag:linkTracking link="http://www.philipmorrisusa.com/en/home.asp" refer="customer_index"/>" target="_blank">homepage</A>
                     </td>
                </tr>
            </table>
<!-- Philip Morris ends -->

             <hr width="640" size="1px" noshade="noshade" />

<!-- Praxair begins -->
            <table border="0" cellpadding="0" cellspacing="0" width="95%">
               <tr valign="top">
                    <td width="170"><img src="/images/clients/praxair_logo.gif" alt="Praxair" border="0" /></td>
                    <td width="15"><img src="/images/clear.gif" alt="" width="15" height="10" border="0" /></td>
                    <td width="99%" class="bodyText">With annual sales of $5.6 billion, Praxair, Inc. is a global, Fortune 500 company that supplies atmospheric, process and specialty gases, high-performance coatings, and related services and technologies.<br /><br />
                    <A href="<tc-webtag:linkTracking link="http://www.praxair.com/" refer="customer_index"/>" target="_blank">homepage</A><%-- | 
                     <A href="/" target="_blank">case study</A>--%>
                     </td>
                </tr>
            </table>
<!-- Praxair ends -->
    
            <hr width="640" size="1px" noshade="noshade" />

<!-- Rodale begins -->
            <table border="0" cellpadding="0" cellspacing="0" width="95%">
                <tr valign="top">
                    <td width="170"><img src="/images/clients/rodale_logo.gif" alt="Rodale" border="0" /></td>
                    <td width="15"><img src="/images/clear.gif" alt="" width="15" height="10" border="0" /></td>
                    <td width="99%" class="bodyText">Rodale today is a global leader in healthy, active living information. Rodale's nine magazine properties, all veteran publications in their categories, include the brands Prevention, Men's Health and Runner's World, which are published in 36 countries.<br /><br />
                    <A href="<tc-webtag:linkTracking link="http://www.rodale.com/" refer="customer_index"/>" target="_blank">homepage</A>
                     </td>
                </tr>
            </table>
<!-- Rodale ends -->

             <hr width="640" size="1px" noshade="noshade" />
<!-- Talbots begins -->
            <table border="0" cellpadding="0" cellspacing="0" width="95%">
                <tr valign="top">
                    <td width="170"><img src="/images/clients/talbots_logo.gif" alt="Talbots" border="0" /></td>
                    <td width="15"><img src="/images/clear.gif" alt="" width="15" height="10" border="0" /></td>
                    <td width="99%" class="bodyText">With hundreds of locations in the US, Canada, and the UK, Talbots is a leading specialty retailer, cataloger, and e-tailer of women's classic apparel.<br /><br />
                    <A href="<tc-webtag:linkTracking link="http://www1.talbots.com/talbotsonline/index.asp" refer="customer_index"/>" target="_blank">homepage</A>
                     </td>
                </tr>
            </table>
<!-- Talbots ends -->

             <hr width="640" size="1px" noshade="noshade" />
             
<!-- UBS begins -->
            <table border="0" cellpadding="0" cellspacing="0" width="95%">
                <tr valign="top">
                    <td width="170"><img src="/images/clients/ubs_logo.png" alt="UBS" border="0" /></td>
                    <td width="15"><img src="/images/clear.gif" alt="" width="15" height="10" border="0" /></td>
                    <td width="99%" class="bodyText">UBS is one of the world's leading financial firms. UBS is: the world's leading wealth management business; a global investment banking and securities firm; a key asset manager; and the market leader in Swiss corporate and individual client banking. As an organization, UBS combines financial strength with a culture that embraces change. As an integrated firm, UBS creates added value for clients by drawing on the combined resources and expertise of all its businesses.<br /><br />
                    <A href="<tc-webtag:linkTracking link="http://www.ubs.com" refer="customer_index"/>" target="_blank">homepage</A>
                     </td>
                </tr>
            </table>
<!-- UBS ends -->

             <hr width="640" size="1px" noshade="noshade" />
             
<!-- Verisign begins -->
            <table border="0" cellpadding="0" cellspacing="0" width="95%">
                <tr valign="top">
                    <td width="170"><img src="/images/clients/verisign_logo.png" alt="Verisign" border="0" /></td>
                    <td width="15"><img src="/images/clear.gif" alt="" width="15" height="10" border="0" /></td>
                    <td width="99%" class="bodyText">VeriSign Inc operates intelligent infrastructure services that enable and protect billions of interactions every day across the world's voice and data networks. Every day, we process as many as 18 billion Internet interactions and support over 100 million phone calls. We also provide the services that help over 3,000 enterprises and 500,000 Web sites to operate securely, reliably, and efficiently. VeriSign is a global enterprise with offices throughout the Asia-Pacific region, Europe, Latin America, and North America, supported by a widespread international network of data centers and operations centers.<br /><br />
                    <A href="<tc-webtag:linkTracking link="http://www.verisign.com" refer="customer_index"/>" target="_blank">homepage</A>
                     </td>
                </tr>
            </table>
<!-- Verisign ends -->

             <hr width="640" size="1px" noshade="noshade" />
             
             
            <table border="0" cellpadding="0" cellspacing="0" width="95%">
                <tr>
                    <td colspan="2" class="bodyText" align="center">
                        <p class="learn_more"><a href="http://www.topcoder.com/tc?module=Static&d1=about&d2=contactus">Contact us</a> today so we can get started developing your next application.</p>
                    </td>
                </tr>
            </table>
            <p><br /></p>
        </td>
<!-- Middle Column ends -->

<!-- Gutter 2 begins -->
        <td width="15"><img src="/images/clear.gif" alt="" width="15" height="10" border="0" /></td>
<!-- Gutter 2 ends -->

<!-- Right Column begins -->
        <td width="170">
        <jsp:include page="/includes/right.jsp" >
            <jsp:param name="level1" value="customers"/>
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
