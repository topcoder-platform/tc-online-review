<%@ page import="com.topcoder.shared.util.ApplicationServer" %>
<%@ page import="com.topcoder.web.common.WebConstants" %>

<%	long tcsForumsID = WebConstants.TCS_FORUMS_ROOT_CATEGORY_ID; %>

<!-- Nav Bar begins -->
<table width="100%" border="0" cellpadding="3" cellspacing="0">
    <tr>
        <td width="49%" class="topNav"><a href="s_index.jsp"><img src="/images/clear.gif" alt="" width="10" height="1" border="0" /></td>

<% if (page_name == "s_index.jsp") { %>

        <td align="center" class="topNavOn" nowrap>&nbsp;&nbsp;<a class="topNavLinks" href="s_index.jsp">Home</a>&nbsp;&nbsp;</td>
        <td align="center" class="topNav" nowrap>&nbsp;&nbsp;<a class="topNavLinks" href="s_about.jsp">About Us</a>&nbsp;&nbsp;</td>
        <td align="center" class="topNav" nowrap>&nbsp;&nbsp;<a class="topNavLinks" href="c_showroom.jsp">Browse Catalogs</a>&nbsp;&nbsp;</td>
        <td align="center" class="topNav" nowrap>&nbsp;&nbsp;<a class="topNavLinks" href="s_learn.jsp">Products</a>&nbsp;&nbsp;</td>
        <td align="center" class="topNav" nowrap>&nbsp;&nbsp;<a class="topNavLinks" href="c_prodTools.jsp?comp=600191">Productivity Tools</a>&nbsp;&nbsp;</td>
        <td align="center" class="topNav" nowrap>&nbsp;&nbsp;<a class="topNavLinks" href="http://<%=ApplicationServer.FORUMS_SERVER_NAME%>/?module=Category&categoryID=<%=tcsForumsID%>">Forums</a>&nbsp;&nbsp;</td>
        <td align="center" class="topNav" nowrap>&nbsp;&nbsp;<a class="topNavLinks" href="c_comp_request.jsp">Suggest a Component</a>&nbsp;&nbsp;</td>
<!--        <td align="center" class="topNav" nowrap>&nbsp;&nbsp;<a class="topNavLinks" href="s_advanced_search.jsp">Advanced Search</a>&nbsp;&nbsp;</td> -->
            
<% } else if ((page_name == "s_about.jsp") || (page_name == "s_standards.jsp") || (page_name == "s_resuse.jsp")) { %>

        <td align="center" class="topNav" nowrap>&nbsp;&nbsp;<a class="topNavLinks" href="s_index.jsp">Home</a>&nbsp;&nbsp;</td>
        <td align="center" class="topNavOn" nowrap>&nbsp;&nbsp;<a class="topNavLinks" href="s_about.jsp">About Us</a>&nbsp;&nbsp;</td>
        <td align="center" class="topNav" nowrap>&nbsp;&nbsp;<a class="topNavLinks" href="c_showroom.jsp">Browse Catalogs</a>&nbsp;&nbsp;</td>
        <td align="center" class="topNav" nowrap>&nbsp;&nbsp;<a class="topNavLinks" href="s_learn.jsp">Products</a>&nbsp;&nbsp;</td>
        <td align="center" class="topNav" nowrap>&nbsp;&nbsp;<a class="topNavLinks" href="c_prodTools.jsp?comp=600191">Productivity Tools</a>&nbsp;&nbsp;</td>
        <td align="center" class="topNav" nowrap>&nbsp;&nbsp;<a class="topNavLinks" href="http://<%=ApplicationServer.FORUMS_SERVER_NAME%>/?module=Category&categoryID=<%=tcsForumsID%>">Forums</a>&nbsp;&nbsp;</td>
        <td align="center" class="topNav" nowrap>&nbsp;&nbsp;<a class="topNavLinks" href="c_comp_request.jsp">Suggest a Component</a>&nbsp;&nbsp;</td>
<!--        <td align="center" class="topNav" nowrap>&nbsp;&nbsp;<a class="topNavLinks" href="s_advanced_search.jsp">Advanced Search</a>&nbsp;&nbsp;</td> -->
            
<% } else if ((page_name == "c_showroom.jsp") || (page_name == "c_component.jsp")) { %>

        <td align="center" class="topNav" nowrap>&nbsp;&nbsp;<a class="topNavLinks" href="s_index.jsp">Home</a>&nbsp;&nbsp;</td>
        <td align="center" class="topNav" nowrap>&nbsp;&nbsp;<a class="topNavLinks" href="s_about.jsp">About Us</a>&nbsp;&nbsp;</td>
        <td align="center" class="topNavOn" nowrap>&nbsp;&nbsp;<a class="topNavLinks" href="c_showroom.jsp">Browse Catalogs</a>&nbsp;&nbsp;</td>
        <td align="center" class="topNav" nowrap>&nbsp;&nbsp;<a class="topNavLinks" href="s_learn.jsp">Products</a>&nbsp;&nbsp;</td>
        <td align="center" class="topNav" nowrap>&nbsp;&nbsp;<a class="topNavLinks" href="c_prodTools.jsp?comp=600191">Productivity Tools</a>&nbsp;&nbsp;</td>
        <td align="center" class="topNav" nowrap>&nbsp;&nbsp;<a class="topNavLinks" href="http://<%=ApplicationServer.FORUMS_SERVER_NAME%>/?module=Category&categoryID=<%=tcsForumsID%>">Forums</a>&nbsp;&nbsp;</td>
        <td align="center" class="topNav" nowrap>&nbsp;&nbsp;<a class="topNavLinks" href="c_comp_request.jsp">Suggest a Component</a>&nbsp;&nbsp;</td>
<!--        <td align="center" class="topNav" nowrap>&nbsp;&nbsp;<a class="topNavLinks" href="s_advanced_search.jsp">Advanced Search</a>&nbsp;&nbsp;</td> -->
            
<% } else if ((page_name == "s_learn.jsp") || (page_name == "s_definition.jsp") || (page_name == "s_subscriptions.jsp") || (page_name == "s_methodology.jsp")) { %>

        <td align="center" class="topNav" nowrap>&nbsp;&nbsp;<a class="topNavLinks" href="s_index.jsp">Home</a>&nbsp;&nbsp;</td>
        <td align="center" class="topNav" nowrap>&nbsp;&nbsp;<a class="topNavLinks" href="s_about.jsp">About Us</a>&nbsp;&nbsp;</td>
        <td align="center" class="topNav" nowrap>&nbsp;&nbsp;<a class="topNavLinks" href="c_showroom.jsp">Browse Catalogs</a>&nbsp;&nbsp;</td>
        <td align="center" class="topNavOn" nowrap>&nbsp;&nbsp;<a class="topNavLinks" href="s_learn.jsp">Products</a>&nbsp;&nbsp;</td>
        <td align="center" class="topNav" nowrap>&nbsp;&nbsp;<a class="topNavLinks" href="c_prodTools.jsp?comp=600191">Productivity Tools</a>&nbsp;&nbsp;</td>
        <td align="center" class="topNav" nowrap>&nbsp;&nbsp;<a class="topNavLinks" href="http://<%=ApplicationServer.FORUMS_SERVER_NAME%>/?module=Category&categoryID=<%=tcsForumsID%>">Forums</a>&nbsp;&nbsp;</td>
        <td align="center" class="topNav" nowrap>&nbsp;&nbsp;<a class="topNavLinks" href="c_comp_request.jsp">Suggest a Component</a>&nbsp;&nbsp;</td>
<!--        <td align="center" class="topNav" nowrap>&nbsp;&nbsp;<a class="topNavLinks" href="s_advanced_search.jsp">Advanced Search</a>&nbsp;&nbsp;</td> -->
            
<% } else if ((page_name.startsWith("c_prodTools")) || (page_name == "s_about_tools.jsp")) { %>

        <td align="center" class="topNav" nowrap>&nbsp;&nbsp;<a class="topNavLinks" href="s_index.jsp">Home</a>&nbsp;&nbsp;</td>
        <td align="center" class="topNav" nowrap>&nbsp;&nbsp;<a class="topNavLinks" href="s_about.jsp">About Us</a>&nbsp;&nbsp;</td>
        <td align="center" class="topNav" nowrap>&nbsp;&nbsp;<a class="topNavLinks" href="c_showroom.jsp">Browse Catalogs</a>&nbsp;&nbsp;</td>
        <td align="center" class="topNav" nowrap>&nbsp;&nbsp;<a class="topNavLinks" href="s_learn.jsp">Products</a>&nbsp;&nbsp;</td>
        <td align="center" class="topNavOn" nowrap>&nbsp;&nbsp;<a class="topNavLinks" href="c_prodTools.jsp?comp=600191">Productivity Tools</a>&nbsp;&nbsp;</td>
        <td align="center" class="topNav" nowrap>&nbsp;&nbsp;<a class="topNavLinks" href="http://<%=ApplicationServer.FORUMS_SERVER_NAME%>/?module=Category&categoryID=<%=tcsForumsID%>">Forums</a>&nbsp;&nbsp;</td>
        <td align="center" class="topNav" nowrap>&nbsp;&nbsp;<a class="topNavLinks" href="c_comp_request.jsp">Suggest a Component</a>&nbsp;&nbsp;</td>
<!--        <td align="center" class="topNav" nowrap>&nbsp;&nbsp;<a class="topNavLinks" href="s_advanced_search.jsp">Advanced Search</a>&nbsp;&nbsp;</td> -->
            
<% } else if ((page_name == "c_active_collab.jsp") || (page_name.startsWith("c_forum"))) { %>

        <td align="center" class="topNav" nowrap>&nbsp;&nbsp;<a class="topNavLinks" href="s_index.jsp">Home</a>&nbsp;&nbsp;</td>
        <td align="center" class="topNav" nowrap>&nbsp;&nbsp;<a class="topNavLinks" href="s_about.jsp">About Us</a>&nbsp;&nbsp;</td>
        <td align="center" class="topNav" nowrap>&nbsp;&nbsp;<a class="topNavLinks" href="c_showroom.jsp">Browse Catalogs</a>&nbsp;&nbsp;</td>
        <td align="center" class="topNav" nowrap>&nbsp;&nbsp;<a class="topNavLinks" href="s_learn.jsp">Products</a>&nbsp;&nbsp;</td>
        <td align="center" class="topNav" nowrap>&nbsp;&nbsp;<a class="topNavLinks" href="c_prodTools.jsp?comp=600191">Productivity Tools</a>&nbsp;&nbsp;</td>
        <td align="center" class="topNavOn" nowrap>&nbsp;&nbsp;<a class="topNavLinks" href="http://<%=ApplicationServer.FORUMS_SERVER_NAME%>/?module=Category&categoryID=<%=tcsForumsID%>">Forums</a>&nbsp;&nbsp;</td>
        <td align="center" class="topNav" nowrap>&nbsp;&nbsp;<a class="topNavLinks" href="c_comp_request.jsp">Suggest a Component</a>&nbsp;&nbsp;</td>
<!--        <td align="center" class="topNav" nowrap>&nbsp;&nbsp;<a class="topNavLinks" href="s_advanced_search.jsp">Advanced Search</a>&nbsp;&nbsp;</td> -->
            
<% } else if (page_name == "c_comp_request.jsp") { %>

        <td align="center" class="topNav" nowrap>&nbsp;&nbsp;<a class="topNavLinks" href="s_index.jsp">Home</a>&nbsp;&nbsp;</td>
        <td align="center" class="topNav" nowrap>&nbsp;&nbsp;<a class="topNavLinks" href="s_about.jsp">About Us</a>&nbsp;&nbsp;</td>
        <td align="center" class="topNav" nowrap>&nbsp;&nbsp;<a class="topNavLinks" href="c_showroom.jsp">Browse Catalogs</a>&nbsp;&nbsp;</td>
        <td align="center" class="topNav" nowrap>&nbsp;&nbsp;<a class="topNavLinks" href="s_learn.jsp">Products</a>&nbsp;&nbsp;</td>
        <td align="center" class="topNav" nowrap>&nbsp;&nbsp;<a class="topNavLinks" href="c_prodTools.jsp?comp=600191">Productivity Tools</a>&nbsp;&nbsp;</td>
        <td align="center" class="topNav" nowrap>&nbsp;&nbsp;<a class="topNavLinks" href="http://<%=ApplicationServer.FORUMS_SERVER_NAME%>/?module=Category&categoryID=<%=tcsForumsID%>">Forums</a>&nbsp;&nbsp;</td>
        <td align="center" class="topNavOn" nowrap>&nbsp;&nbsp;<a class="topNavLinks" href="c_comp_request.jsp">Suggest a Component</a>&nbsp;&nbsp;</td>
<!--        <td align="center" class="topNav" nowrap>&nbsp;&nbsp;<a class="topNavLinks" href="s_advanced_search.jsp">Advanced Search</a>&nbsp;&nbsp;</td> -->
            
<% } else { %>

        <td align="center" class="topNav" nowrap>&nbsp;&nbsp;<a class="topNavLinks" href="s_index.jsp">Home</a>&nbsp;&nbsp;</td>
        <td align="center" class="topNav" nowrap>&nbsp;&nbsp;<a class="topNavLinks" href="s_about.jsp">About Us</a>&nbsp;&nbsp;</td>
        <td align="center" class="topNav" nowrap>&nbsp;&nbsp;<a class="topNavLinks" href="c_showroom.jsp">Browse Catalogs</a>&nbsp;&nbsp;</td>
        <td align="center" class="topNav" nowrap>&nbsp;&nbsp;<a class="topNavLinks" href="s_learn.jsp">Products</a>&nbsp;&nbsp;</td>
        <td align="center" class="topNav" nowrap>&nbsp;&nbsp;<a class="topNavLinks" href="c_prodTools.jsp?comp=600191">Productivity Tools</a>&nbsp;&nbsp;</td>
        <td align="center" class="topNav" nowrap>&nbsp;&nbsp;<a class="topNavLinks" href="http://<%=ApplicationServer.FORUMS_SERVER_NAME%>/?module=Category&categoryID=<%=tcsForumsID%>">Forums</a>&nbsp;&nbsp;</td>
        <td align="center" class="topNav" nowrap>&nbsp;&nbsp;<a class="topNavLinks" href="c_comp_request.jsp">Suggest a Component</a>&nbsp;&nbsp;</td>
<!--        <td align="center" class="topNav" nowrap>&nbsp;&nbsp;<a class="topNavLinks" href="s_advanced_search.jsp">Advanced Search</a>&nbsp;&nbsp;</td> -->
            
<% } %>

        <td width="49%" class="topNav"><a href="s_index.jsp"><img src="/images/clear.gif" alt="" width="10" height="1" border="0" /></td>
    </tr>
</table>
<!-- Nav Bar ends -->

