<%@ page contentType="text/html;charset=utf-8" %> 
<%@ page import="com.topcoder.shared.util.ApplicationServer" %>
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
            <jsp:param name="node" value="page5"/>
        </jsp:include>

        <div id="slide">
            <a href="/tcs?module=Static&amp;d1=tour&amp;d2=page6" onfocus="this.blur();"><img src="/i/tour/page5.jpg" alt="" /></a>
        </div>

        <%--<h2 align="center">
            Let the world compete on your next application.
        </h2>--%>

        <div align="center">
            <div style="width: 60px; height: 28px; margin: 10px;">
                <div style="float: left;">
                    <a href="/tcs?module=Static&amp;d1=tour&amp;d2=page4" onfocus="this.blur();"><img src="/i/tour/prev.png" alt="Previous page" /></a>
                </div>
                <div style="float: right;">
                    <a href="/tcs?module=Static&amp;d1=tour&amp;d2=page6" onfocus="this.blur();"><img src="/i/tour/next.png" alt="Next page" /></a>
                </div>
            </div>
        </div>

        <div class="popUp" id="myPopUp">
            <div id="popUpText">&nbsp;</div>
        </div>

        

    </div>
</div>

<jsp:include page="foot.jsp" />

</body>

</html>