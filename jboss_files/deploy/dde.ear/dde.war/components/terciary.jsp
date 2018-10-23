<%
    String level1 = request.getParameter("level1")==null?"":request.getParameter("level1");

%>
                            <tr>
                                <td class="terciaryNav">
                                    <a href="/catalog/c_showroom.jsp" class="<%=level1.equals("browse")?"terciaryOn":"terciaryOff"%>">View All Catalogs</a>&nbsp;&nbsp;|&nbsp;&nbsp;
                                    <a href="/catalog/c_showroom.jsp?a=all" class="<%=level1.equals("viewall")?"terciaryOn":"terciaryOff"%>">View All Components</a>&nbsp;&nbsp;|&nbsp;&nbsp;
                                    <a href="/catalog/c_showroom.jsp?cat=5801776" class="<%=level1.equals("java")?"terciaryOn":"terciaryOff"%>">Java&#8482; Catalog</a>&nbsp;&nbsp;|&nbsp;&nbsp;
                                    <a href="/catalog/c_showroom.jsp?cat=5801777" class="<%=level1.equals("net")?"terciaryOn":"terciaryOff"%>">.NET&#8482; Catalog</a>
                                </td>
                            </tr>
