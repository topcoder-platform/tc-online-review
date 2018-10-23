            <table border="0" cellpadding="0" cellspacing="0" width="100%">
<!-- Current Forums begin -->
                <tr><td><img src="/images/headBaseCompIncl.gif" alt="Current Forums" width="170" height="18" border="0" /></td></tr>
                <tr><td><hr width="170" size="1" noshade="noshade" /></td></tr>
                <tr>
                    <td width="100%">
                        <table border="0" cellspacing="0" cellpadding="0" width="100%">
<%  if (activeCollab != null) { %>
                            <tr><td class="rightColDisplay"><a href="c_forum.jsp?f=<%= activeCollab.getId() %>">Customer Forum</a></td></tr>
<%  } else { %>
                            <tr><td class="rightColOff">No active customer forum</td></tr>

<%  } if (activeSpec != null) { %>
                            <tr><td class="rightColDisplay"><a href="c_forum.jsp?f=<%= activeSpec.getId() %>">Developer Forum</a></td></tr>
<%  } else { %>
                            <tr><td class="rightColOff">No active developer forum</td></tr>
<%  } %>

                            <tr><td class="small">Participation in current forums requires user login and may require authorization</td></tr>

                            <tr><td width="170"><img src="/images/clear.gif" alt="" width="170" height="1" border="0" /></td></tr>
                        </table>
                    </td>
                </tr>
                <tr><td><hr width="245" size="1" noshade="noshade" /></td></tr>
                <tr><td height="15"><img src="/images/clear.gif" alt="" width="10" height="15" border="0" /></td></tr>
<!-- Current Forums end -->

<!-- Previous Forums begin -->
<%  if (hasPreviousForums) { %>
                <tr><td><img src="/images/headBaseCompIncl.gif" alt="Previous Forums" width="170" height="18" border="0" /></td></tr>
                <tr><td><hr width="170" size="1" noshade="noshade" /></td></tr>
                <tr>
                    <td width="100%">
                        <table border="0" cellspacing="0" cellpadding="0" width="100%">
<%  for (int i=0; i < versions.length; i++) {
            if (collaborations[i] != null) {  %>
                            <tr><td class="rightColDisplay"><a href="c_forum.jsp?f=<%= collaborations[i].getId() %>">Customer Forum <%= "" + versions[i].getVersion() %></a></td></tr>
            <%  }  if (specifications[i] != null) {  %>
                            <tr><td class="rightColDisplay"><a href="c_forum.jsp?f=<%= specifications[i].getId() %>">Developer Forum <%= "" + versions[i].getVersion() %></a></td></tr>
            <%  }
}  %>
                            <tr><td class="small">Previous forums are read only</td></tr>

                            <tr><td width="170"><img src="/images/clear.gif" alt="" width="170" height="1" border="0" /></td></tr>
                        </table>
                    </td>
                </tr>
                <tr><td><hr width="245" size="1" noshade="noshade" /></td></tr>
                <tr><td height="15"><img src="/images/clear.gif" alt="" width="10" height="15" border="0" /></td></tr>
<% } %>               
<!-- Previous Forums end -->
            </table>
