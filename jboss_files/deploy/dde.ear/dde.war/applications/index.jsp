<%@ include file="/includes/util.jsp" %>
<%@ include file="/includes/session.jsp" %>
<%@ include file="/includes/formclasses.jsp" %>


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
<jsp:include page="/includes/menu.jsp">
    <jsp:param name="isSoftwarePage" value="true" />
</jsp:include>
<!-- Header ends -->


<table width="100%" border="0" cellpadding="0" cellspacing="0" align="center" class="middle">
    <tr valign="top">

<!-- Left Column begins -->
        <td width="165" class="leftColumn">
        	<jsp:include page="/includes/left.jsp">
        		<jsp:param name="level1" value="applications" />
        		<jsp:param name="level2" value="overview" />
    		</jsp:include>
		</td>
<!-- Left Column ends -->

<!-- Middle Column begins -->
        <td width="100%">
                <div class="platform_row">
                    	<h3 id="platform-header-appoverview">Applications Overview</h3>
                        <p><strong>We know you've probably become conditioned to tune out the claims made by other software companies. TopCoder is on a mission to prove that we can develop high-quality software applications differently from those companies. How do we do it?</strong></p>
                        <p>We utilize our member base of software developers who are differentiated by talent through competition. Combining this talent with our competition-based methodology and our catalogs of re-usable components is what allows us to develop faster, cheaper, and better.</p>
                </div>
                <div class="platform_row_dark">
                        <div class="image">
                        	<a href="http://software.topcoder.com/catalog/index.jsp"><img src="/i/component_catalog.png" alt="Component Catalog" class="platform_img" /></a>
                        </div>
                        <h3 id="platform-header-componentcatalog"><a href="http://software.topcoder.com/catalog/index.jsp">Component Catalog</a></h3>
                        <p>In 2001, we began developing substantial catalogs of re-usable Java and .NET components. Reusing these bits of software over and over on client projects allows us to substantially shorten timelines and decrease costs. We then pass these savings onto our clients.</p>
                </div><%-- platform_row_dark ends--%>

                <div class="platform_row">
                      <div class="image right">
                      	<img src="/i/no_shore.png" alt="No-Shore Development" />
                     	</div>   
                      <h3 id="platform-header-noshore">No-Shore Development</h3>
                      <p>TopCoder's member base is a distributed resource that spans over 150 countries. Unlike strictly off-shore development firms, it doesn't matter what country our members call home, as long as they've proven that they can deliver a better solution than their peers. Since our members work for TopCoder as contractors, not employees, our fixed costs are much lower than our competition.</p>
                </div><%-- platform_row ends--%>

                <div class="platform_row_dark">
                        <div class="image">
                        	<img src="/i/uncompromising_quality.png" alt="Uncompromising Quality" class="platform_img" />
                        </div>
                        <h3 id="platform-header-uncompromisingquality"><span>Uncompromising Quality</span></h3>
                        <p>Differentiating our members by talent through competition insures that only quality developers are working on our code. Each week, hundreds of TopCoder members compete in programming competitions, where they are rated based on the results. Only members with acceptable ratings are allowed to develop software for TopCoder.</p>
                        <p>Our Component Catalogs are made up of the extremely high quality components developed by our members. These components are of such high quality due to the fact that they have been used and re-used in multiple applications. TopCoder strives to develop as many applications with re-usable components as possible.</p>
                </div><%-- platform_row_dark ends--%>
    
                <div class="platform_row">
                <div class="image right">
                	<img src="/i/app_methodology.png" alt="Application Methodology" />
               	</div>
                <h3 id="platform-header-appmethodology">Application Methodology</h3>                
                <p>The TopCoder Application Development Methodology is designed to deliver a repeatable and consistent 
                solution to our customers. The TopCoder methodology is comprised of phases and deliverables. The 
                six-step methodology is administered by a TopCoder Project Manager skilled in Component Based 
                Development (CBD) techniques, requirements gathering, and project management. During the specification 
                phase, the Project Manager works hand-in-hand with our customers to develop specifications that satisfy 
                all of our customers' requirements. In the component architecture phase, the Project Manager and 
                TopCoder Component Architect identify and design the components required to build the application. Next, 
                the component production phase uses TopCoder's unique component methodology, to create all of the components. During the application assembly phase, the Project Manager hand-selects only proven members to participate in the assembly of the application. The application assemblers must sign a non-disclosure agreement to protect our clients. During this phase, the components are linked together to build the application. Once assembled, the certification phase begins. Prior to deployment at the customer's site, the application is rigorously tested at TopCoder. After certification, the application is delivered to our customers and deployed to their quality assurance environment, as part of the deployment phase. Our rigorous application methodology, combined with our competition-tested members and our re-usable components, allow TopCoder to deliver applications of the highest quality.</p>
            </div><%-- platform_row ends--%>
        </td>
    </tr>
</table>
<!-- Footer begins -->
<jsp:include page="/includes/foot.jsp" flush="true" />
<!-- Footer ends -->

</body>
</html>
