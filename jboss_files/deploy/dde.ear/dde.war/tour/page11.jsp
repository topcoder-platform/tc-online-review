<%@ page contentType="text/html;charset=utf-8" %> 
<%@ page import="com.topcoder.shared.util.ApplicationServer" %>
<%@ taglib uri="tc-webtags.tld" prefix="tc-webtag" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" 
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>

<head>
    <title>TopCoder Software Tour</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link type="image/x-icon" rel="shortcut icon" href="/i/favicon.ico"/>
    <link type="text/css" rel="stylesheet" href="/css/tour.css" />
    <script type="text/javascript" src="/js/popup.js"></script>
    <script language="JavaScript" type="text/javascript">
    <!--
    function postPopUpText(myText) {
        document.getElementById("popUpText").innerHTML = myText;
    }
    //-->
    </script>
</head>

<body>

<div align="center">

    <div style="width: 700px; background: transparent;">

        <div id="pageHeader">
            <div style="float: left; margin: 10px 0px 0px 0px;">
                <a href="http://<%=ApplicationServer.SERVER_NAME%>/"><img src="/i/tour/brandingLogo.png" alt="TopCoder" /></a>
            </div>
            <div style="float: right; margin: 10px 0px 0px 0px;">
                <img src="/i/tour/takeATour.png" alt="Take a tour." style="display: block;" />
            </div>
        </div>

        <jsp:include page="nav.jsp">
            <jsp:param name="node" value="page11"/>
        </jsp:include>

        <div style="width: 698px; height: 398px; border: 1px solid #eeeeee;">
            <div style="margin: 40px 20px 80px 20px;">
                <img src="/i/tour/page11title.png" alt="What to do now" />
            </div>
            <p align="center" style="margin-top: 20px;">
            <strong><a href="<tc-webtag:linkTracking link='/tc?module=Static&d1=about&d2=contactus' refer='software_tour_11' />">Call us today and let us build your next application.</a></strong>
            </p>
            <p align="center">
            <strong><a href="<tc-webtag:linkTracking link='/reg/' refer='software_tour_11' />">I'd like to register for a TopCoder account.</a></strong>
            </p>
            <p align="center">
            <strong><a href="<tc-webtag:linkTracking link='/' refer='software_tour_11' />">Go back to the homepage.</a></strong>
            </p>
        </div>

        <%--
        <div id="slide">
            <a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=Static&amp;d1=about&amp;d2=contactus"><img src="/i/tour/page11.png" alt="" /></a>
        </div>
        --%>

        <div align="center">
            <div style="width: 60px; height: 28px; margin: 10px;">
                <div style="float: left;">
                    <a href="/tcs?module=Static&amp;d1=tour&amp;d2=page10" onfocus="this.blur();"><img src="/i/tour/prev.png" alt="Previous page" /></a>
                </div>
                <div style="float: right;">
                    <img src="/i/tour/nextNA.png" alt="Next page" />
                </div>
            </div>
        </div>

        <div class="popUp" id="myPopUp">
            <div id="popUpText">&nbsp;</div>
        </div>

        <%--
        <p align="center" style="margin-top: 20px;">
        <strong><a href="<tc-webtag:linkTracking link='/tc?module=Static&d1=about&d2=contactus' refer='software_tour_11' />">Call us today and let us build your next application.</a></strong>
        </p>
        <p align="center">
        <strong><a href="<tc-webtag:linkTracking link='/reg/' refer='software_tour_11' />">I'd like to register for a TopCoder account.</a></strong>
        </p>
        <p align="center">
        <strong><a href="<tc-webtag:linkTracking link='/' refer='software_tour_11' />">Go back to the homepage.</a></strong>
        </p>
        --%>

        

    </div>
</div>

<jsp:include page="foot.jsp" />

</body>

</html>