<%@ page import="com.topcoder.shared.dataAccess.Request,
                 com.topcoder.shared.dataAccess.DataAccessInt,
                 com.topcoder.web.common.CachedDataAccess,
                 com.topcoder.shared.dataAccess.resultSet.ResultSetContainer,
                 com.topcoder.shared.util.DBMS,
                 com.topcoder.web.common.cache.MaxAge,
                 com.topcoder.shared.util.ApplicationServer"%>
<%@ page language="java" %>
<%@ taglib uri="tc-webtags.tld" prefix="tc-webtag" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
        CachedDataAccess countDai = new CachedDataAccess(MaxAge.QUARTER_HOUR, DBMS.OLTP_DATASOURCE_NAME);
        Request countReq = new Request();
        countReq.setContentHandle("member_count");
        Integer memberCount = Integer.valueOf(((ResultSetContainer) countDai.getData(countReq).get("member_count")).getIntItem(0, "member_count"));
%>


<%-- LINKS BLOCK --%>
    <div id="links">
        <div class="wrapper">
            <div class="col">
                <h4>Customer Service</h4>
                <ul>
                    <li><a href="http://<%=ApplicationServer.SOFTWARE_SERVER_NAME%>/contact.jsp">Contact Us</a></li>
					<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=Static&amp;d1=about&amp;d2=privacy">Privacy Policy</a></li>
                    <li><a href="http://topcoderblogs.com/direct/?page_id=24">Terms &amp; Conditions</a></li>
                </ul>
            </div><%-- .col ends --%>
            
            <div class="col">
                <h4>About TopCoder</h4>
                <ul>
                    <li><a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=Static&amp;d1=pressroom&amp;d2=mediaRequestForm">Public Relations</a></li>
                    <li><a href="http://<%=ApplicationServer.SOFTWARE_SERVER_NAME%>/TCD/rss.jsp">RSS Feeds</a></li>
                    <li><a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=Static&amp;d1=pressroom&amp;d2=index">Press Room</a></li>
                    <li><a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=Static&amp;d1=about&amp;d2=jobs">Working at TopCoder</a></li>
                    <li><a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=Static&amp;d1=about&amp;d2=terms">Legal Information</a></li>
                </ul>
            </div><%-- .col ends --%>
            
            <div class="col">
                <h4>Platform Tools</h4>
                <ul><li><a href="http://<%=ApplicationServer.SERVER_NAME%>/wiki/display/tc/Upcoming+Contests">Pipeline</a></li>
                    <li><a href="http://<%=ApplicationServer.SERVER_NAME%>/wiki/display/tc/TopCoder+UML+Tool">UML Tool</a></li>
                    <li><a href="http://<%=ApplicationServer.FORUMS_SERVER_NAME%>/?module=Category&categoryID=22">TopCoder Forums</a></li>
                    <li><a href="http://<%=ApplicationServer.SOFTWARE_SERVER_NAME%>/catalog/index.jsp">Component Catalog</a></li>
                    <li><a href="http://<%=ApplicationServer.SERVER_NAME%>/wiki/">TopCoder Wiki</a></li>
                </ul>
          </div><%-- .col ends --%>
            
            <div class="col">
                <h4>TopCoder Community</h4>
                <ul>
                    <li><a href="http://<%=ApplicationServer.SERVER_NAME%>/tc">TopCoder Community Home</a></li>
                    <li><a href="http://<%=ApplicationServer.FORUMS_SERVER_NAME%>/">TopCoder Forums</a></li>
                    <li><a href="http://<%=ApplicationServer.STUDIO_SERVER_NAME%>/forums">Studio Forums</a></li>
                </ul>
            
                <h4>TopCoder Blogs</h4>
                <ul>
                    <li><a href="http://<%=ApplicationServer.SERVER_NAME%>/direct/blogs/">TopCoder Direct</a></li>
                    <li><a href="http://<%=ApplicationServer.STUDIO_SERVER_NAME%>/blog/">Studio TopCoder</a></li>
                </ul>
            </div><%-- .col ends --%>
            
            <div class="col">
                <h4>My Account</h4>
                <ul>
                    <li><a href="http://<%=ApplicationServer.SERVER_NAME%>/reg/">TopCoder Registration</a></li>
                    <li><a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=MyHome">Manage Profile</a></li>
                    <li><a href="http://<%=ApplicationServer.SERVER_NAME%>/dr">TopCoder Digital Run</a></li>
                    <li><a href="http://<%=ApplicationServer.STUDIO_SERVER_NAME%>/?module=Static&amp;d1=digitalrun&amp;d2=2008v2&amp;d3=home">Studio Cup</a></li>
                </ul>
            </div><%-- .col ends --%>
            
            <div class="col">
                <h4>Powered by TopCoder</h4>
                <ul>
                    <li><a href="http://<%=ApplicationServer.SOFTWARE_SERVER_NAME%>/">TopCoder Direct</a></li>
                    <li><a href="http://<%=ApplicationServer.SERVER_NAME%>/">TopCoder.com</a></li>
                    <li><a href="http://<%=ApplicationServer.STUDIO_SERVER_NAME%>/">Studio TopCoder</a></li>
                </ul>
            </div><%-- .col ends --%>
        </div><%-- .wrapper ends --%>
    </div><%-- #links block ends --%>

<%-- FOOTER BLOCK --%>
    <div id="footer">
        <div class="">
            <p id="footer_1800"><strong>1-866-TopCoder or answers@topcoder.com</strong></p>

            <p>TopCoder is the world's largest competitive software development community with more than <tc-webtag:format object="<%=memberCount%>" format="#,##0"/> developers representing over 200 countries.</p>
            
            <p>Copyright &copy;2001-2009, TopCoder, Inc. All rights reserved.</p>
        </div><%-- .wrapper ends --%>
    </div><%-- #footer ends --%>

<%-- ANALYTICS BLOCK --%>
<script type="text/javascript">
var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
document.write(unescape("%3Cscript src='" + gaJsHost + "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
</script>
<script type="text/javascript">
try {
var pageTracker = _gat._getTracker("UA-6340959-6");
pageTracker._trackPageview();
} catch(err) {}</script>