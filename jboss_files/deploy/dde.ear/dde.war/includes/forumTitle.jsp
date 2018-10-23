<%@ page import="com.topcoder.dde.catalog.Catalog"%>
<%@ page import="com.topcoder.shared.util.ApplicationServer" %>
<%@ page import="com.topcoder.web.common.WebConstants" %>

<%	long tcsForumsID = WebConstants.TCS_FORUMS_ROOT_CATEGORY_ID; %>

<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr><td height="15" colspan="3"><img src="/images/clear.gif" alt="" width="10" height="15" border="0" /></td></tr>
    <tr valign="top">
        <td width="98%" align="left">
            <table border="0" cellpadding="0" cellspacing="0">
                <tr valign="middle">
                    <td class="normal"><img src="/images/headForums.gif" alt="Forums" width="100" height="22" border="0"></td>
    <TD>
    <%
				if( Catalog.JAVA_CATALOG == forumComponent.getRootCategory( ) )
				{
				%>
                                <img src="/images/javaSm.gif" width="33" height="17" alt="Java Catalogue" border="0" />
				<%
				}
				else if( Catalog.NET_CATALOG == forumComponent.getRootCategory( ) )
				{
				%>
                                <img src="/images/netSm.gif" width="33" height="17" alt=".NET Catalogue" border="0" />
				<%
				}
				else if( Catalog.FLASH_CATALOG == forumComponent.getRootCategory( ) )
				{
				%>
                                <img src="/images/flashSm.gif" width="33" height="17" alt=".NET Catalogue" border="0" />
				<%
				}
				%>
				</TD>
                                <td><img src="/images/spacer.gif" alt="" width="5" height="17" border="0" /></td>
                                <td class="forumCompName"><a href="/catalog/c_component.jsp?comp=<%=forumComponent.getComponentId()%>"><%=forumComponent.getName()%> <span class="version">version <%=forumComponent.getVersionText()%></span></a></td>
                            </tr>
                        </table>
                    </td>

                    <td width="15"><img src="/images/clear.gif" alt="" width="10" height="15" border="0" /></td>

                    <td width="2%" align="right">
                        <table border="0" cellpadding="0" cellspacing="0">
                            <tr valign="middle"><form name="frmForumSearch" action="c_forum_search.jsp" method="post">
                                <input type="hidden" name="a" value="search" />
                                <input type="hidden" name="f" value="<%=forumId%>" />
                                <td><img src="/images/clear.gif" alt="" width="1" height="1" border="0"/></td>
                                <td class="forumSearch"><input type="text" size="24" name="keywords" value="Search This Forum" maxlength="40" onFocus="this.select();" class="forumSearchForm"></td>
                                <td class="forumSearch" align="left"><input class="forumSearchButton" type="submit" name="Go" value="&nbsp;Go&nbsp;"></td></form>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr><td height="10" colspan="3"><img src="/images/clear.gif" alt="" width="10" height="10" border="0" /></td></tr>
            </table>
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr valign="bottom">
<%  if (forumType == com.topcoder.dde.catalog.ForumCategory.COLLABORATION) { %>
                    <td width="1%" class="normal"><a class="normal" href="c_forum.jsp?f=<%=collabForumId%>"><img src="/images/tabCustForum_on.gif" alt="Customer Forum" name="tabCustForum" width="142" height="22" border="0"></a></td>
    <% if (specForumId > 0) { %>
                    <td width="1%" class="normal"><a onmouseover="document.images['tabDevForum'].src = tabDevForumon.src; window.status='Developer Forum'; return true;" onmouseout="document.images['tabDevForum'].src = tabDevForumoff.src" class="normal" href="c_forum.jsp?f=<%=specForumId%>"><img src="/images/tabDevForum_off.gif" alt="Developer Forum" name="tabDevForum" width="142" height="22" border="0"></a></td>
    <% }

    } else {
        if (collabForumId > 0) { %>
                    <td width="1%" class="normal"><a onmouseover="document.images['tabCustForum'].src = tabCustForumon.src; window.status='Customer Forum'; return true;" onmouseout="document.images['tabCustForum'].src = tabCustForumoff.src" class="normal" href="c_forum.jsp?f=<%=collabForumId%>"><img src="/images/tabCustForum_off.gif" alt="Customer Forum" name="tabCustForum" width="142" height="22" border="0"></a></td>
    <% } %>
                    <td width="1%" class="normal"><a class="normal" href="c_forum.jsp?f=<%=specForumId%>"><img src="/images/tabDevForum_on.gif" alt="Developer Forum" name="tabDevForum" width="142" height="22" border="0"></a></td>
<% } %>
<!-- Customer or Developer Forum Tabs end -->

<!-- Forums Sub Nav begins -->
                    <td width="98%" class="breadcrumb" nowrap>
                        <a class="breadcrumbLinks" href="/catalog/c_component.jsp?comp=<%=forumComponent.getComponentId()%>">Component Description</a> &nbsp;|&nbsp;
                        <a class="breadcrumbLinks" href="http://<%=ApplicationServer.FORUMS_SERVER_NAME%>/?module=Category&categoryID=<%=tcsForumsID%>">Forums Home</a>
<%  if (threadId > 0) { %>
                        &nbsp;|&nbsp; <a class="breadcrumbLinks" href="c_forum.jsp?f=<%=forumId%>">All Threads</a>
<%      if (prevThreadId > 0) { %>
                        &nbsp;|&nbsp; <a class="breadcrumbLinks" href="c_forum_message.jsp?f=<%=forumId%>&r=<%=prevThreadId%>">Previous Thread</a>
<%      } else { %>
                        &nbsp;|&nbsp; Previous Thread
<%      } %>
<%      if (nextThreadId > 0) { %>
                        &nbsp;|&nbsp; <a class="breadcrumbLinks" href="c_forum_message.jsp?f=<%=forumId%>&r=<%=nextThreadId%>">Next Thread</a>
<%      } else { %>
                        &nbsp;|&nbsp; Next Thread
<%      } %>
<%  } %>
                    </td>
<!-- Forums Sub Nav ends -->
                </tr>

                <tr valign="top">
                    <td colspan="3" class="forumHeadFoot"><img src="/images/clear.gif" alt="" width="10" height="7" border="0"></td>
                </tr>
            </table>
