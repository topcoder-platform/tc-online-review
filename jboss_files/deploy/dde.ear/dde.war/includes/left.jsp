<%@ page import="com.topcoder.shared.util.ApplicationServer" %>
<%@ page import="com.topcoder.web.common.WebConstants" %>
<%
    String level1 = request.getParameter("level1")==null?"":request.getParameter("level1");
    String level2 = request.getParameter("level2")==null?"":request.getParameter("level2");
	
	long tcsForumsID = WebConstants.TCS_FORUMS_ROOT_CATEGORY_ID; 
%>

            <table width="148" style="margin-left:48px;"  cellspacing="0" cellpadding="0" border="0">
                <tr><td id="leftNavTitle">Component Search:</td></tr>
                <tr>
                    <td id="leftNavTitle">
                        <%@ include file="/includes/componentSearch.jsp" %>
                    </td>
                </tr>
               </table>
              
  <div id="nav-sidebar">
    <ul id="side-navigation">
    	<li class="top-li"><a href="#" class="expand-group">Applications</a>
			<ul <% if (level1.equals("applications")) { %> style="display:block;" <% } %> class="submenu">
            	<li><a href="/applications/index.jsp" <% if (level2.equals("overview")) { %> class="sub-active" <% } %>>Overview</a></li>
                <li><a href="/applications/methodology.jsp" <% if (level2.equals("methodology")) { %> class="sub-active" <% } %>>Methodology</a></li>
            </ul> 
        </li>
        <li><a href="#"class="expand-group">Components</a>
       	<ul <% if (level1.equals("components")) { %> style="display:block;" <% } %> class="submenu">
            	<li><a href="/catalog/index.jsp" <% if (level2.equals("find")) { %> class="sub-active" <% } %>>Find Components</a></li>
                <li><a href="/components/index.jsp" <% if (level2.equals("comp-overview")) { %> class="sub-active" <% } %>>What Are Components</a></li>
                <li><a href="/components/methodology.jsp" <% if (level2.equals("comp-methodology")) { %> class="sub-active" <% } %>>Methodology</a></li>
                <li><a href="/components/subscriptions.jsp" <% if (level2.equals("subscriptions")) { %> class="sub-active" <% } %>>Subscriptions</a></li>
                <li><a href="/components/request.jsp" <% if (level2.equals("suggest")) { %> class="sub-active" <% } %>>Suggest a Component</a></li>
            </ul>

        </li>
        <li><a href="#" class="expand-group">Customers</a>
            <ul <% if (level1.equals("customers")) { %> style="display:block;" <% } %> class="submenu">
            	<li><a href="/customers/index.jsp" <% if (level2.equals("customers")) { %> class="sub-active" <% } %>>Our Customers</a></li>
                <li><a href="/customers/casestudies.jsp" <% if (level2.equals("cases")) { %> class="sub-active" <% } %>>Case Studies</a></li>
            </ul>
        </li>
        <li><a href="#" class="expand-group">Forums</a>
        	<ul class="submenu">
            	<li><a href="http://<%=ApplicationServer.FORUMS_SERVER_NAME%>/?module=Category&amp;categoryID=<%=tcsForumsID%>">Software Forums</a></li>
                <li><a href="http://<%=ApplicationServer.FORUMS_SERVER_NAME%>">Member Forums</a></li>
                <li><a href="http://<%=ApplicationServer.STUDIO_SERVER_NAME%>/forums">Studio Forums</a></li>
            </ul>
        </li>
    </ul>
</div>

            
           