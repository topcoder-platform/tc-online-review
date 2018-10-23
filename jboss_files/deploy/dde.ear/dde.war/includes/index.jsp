<%@ page import="com.topcoder.shared.util.ApplicationServer" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
		"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
		
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<jsp:include page="/includes/header-files.jsp" />
<title>TopCoder Direct</title>
<script src="/scripts/RSSProcessor.js" type="text/javascript"></script>
<script src="/scripts/AJAXProcessor.js" type="text/javascript"></script>
<script src="/scripts/direct.js" type="text/javascript"></script>


</head>
<body>
<jsp:include page="/includes/top.jsp">
<jsp:param name="TCDlevel" value="home" />
</jsp:include>

<%-- CONTENT BLOCKS --%>
	<div id="content">
		<div class="wrapper">
		
<%-- TOPCODER DIRECT BANNER --%>
    <div id="direct_banner">
            <p><img src="images/plug_into_the_world_largest.png" width="556" height="68"
                    alt="Plug into the world largest community of competitive software developers and designers" /></p>
        </div>

        <div id="content_main">
            <div class="left_content">
                <ul>
                    <li><a class="button1" href="http://<%=ApplicationServer.SERVER_NAME%>/direct">WEB SITES</a></li>
                    <li><a class="button1" href="http://<%=ApplicationServer.SERVER_NAME%>/direct">LOGO DESIGN</a></li>
                    <li><a class="button2" href="http://<%=ApplicationServer.SERVER_NAME%>/direct">PRINT DESIGN</a></li>
                    <li><a class="button2" href="http://<%=ApplicationServer.SERVER_NAME%>/direct">APPLICATIONS</a></li>
                </ul>
                <a href="http://<%=ApplicationServer.SERVER_NAME%>/direct/cockpit/cockpit.jsp"><img class="startNow" src="/i/start_your_project_now.png" alt="start your project now" /></a>
                <a class="button" href="http://<%=ApplicationServer.SERVER_NAME%>/direct/cockpit/cockpit.jsp">LAUNCH DIRECT</a>
            </div>
            <div class="right_content">
                <ul>
                    <li><a class="button1" href="http://<%=ApplicationServer.SOFTWARE_SERVER_NAME%>/TCD/platform-tools.jsp">TOOLS</a></li>
                    <li><a class="button1" href="http://<%=ApplicationServer.SOFTWARE_SERVER_NAME%>/TCD/training.jsp">TRAINING</a></li>
                    <li><a class="button2" href="http://<%=ApplicationServer.SOFTWARE_SERVER_NAME%>/catalog/index.jsp">COMPONENTS</a></li>
                    <li><a class="button2" href="http://<%=ApplicationServer.SOFTWARE_SERVER_NAME%>/TCD/support.jsp">SUPPORT</a></li>
                </ul>
                <ul class="icons">
                    <li class="discovery"><a href="http://<%=ApplicationServer.SOFTWARE_SERVER_NAME%>/TCD/about.jsp">Discovery</a></li>
                    <li class="design"><a href="http://<%=ApplicationServer.SOFTWARE_SERVER_NAME%>/TCD/about.jsp">Design</a></li>
                    <li class="build"><a href="http://<%=ApplicationServer.SOFTWARE_SERVER_NAME%>/TCD/about.jsp">Build</a></li>
                    <li class="test"><a href="http://<%=ApplicationServer.SOFTWARE_SERVER_NAME%>/TCD/about.jsp">Test</a></li>
                </ul>
                <img class="findOut" src="images/find_out_more.png" alt="find out more" />
                <a class="button" href="http://<%=ApplicationServer.SOFTWARE_SERVER_NAME%>/contact.jsp">CONTACT US</a>
            </div>
            <div class="clear"></div>
        </div>

    </div>
    <%-- .wrapper ends --%>
</div>
<%-- #content ends --%>

<%--BANNER AD--%>
<div id="banner">
    <a href="http://<%=ApplicationServer.SOFTWARE_SERVER_NAME%>/TCD/about.jsp"><img src="images/learnmore.png" alt="Learn More About TopCoder Direct" /></a>
</div>
<%-- NEWS --%>

<div id="news">
    <%-- .wrapper ends --%>
</div>
<%-- #news ends --%>

		<script>
            var rss = "/direct/blogs/?feed=rss2";

            //A sample URL to get Template
            var template = "/TCD/WhatsNewTemplate.txt";

            var processor = new js.topcoder.rss.template.RSSProcessor(false, template);
            document.getElementById("news").innerHTML = (processor.transformRSSFeed(rss));
        </script>

<%-- Footer begins --%>
<jsp:include page="/includes/foot.jsp" flush="true" />
<%-- Footer ends --%>
	
       

</body>
</html>