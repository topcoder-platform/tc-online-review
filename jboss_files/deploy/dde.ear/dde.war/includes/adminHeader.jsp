<!--Header begins-->
    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="head">
    	<tr valign="top">
		<td width="219" height="57" align="left" rowspan="2"><a href="/"><img src="/images/logoTCSHome.gif" alt="TopCoder Software" width="216" height="57" border="0" /></a></td>
		<td width="155" height="57" align="left" rowspan="2"><img src="/images/homeBannerAdmin.gif" alt="Administration" width="155" height="57" border="0" /></td>
    		
		<td class="headRight">
			<table border="0" cellpadding="0" cellspacing="0" class="adminSearch" align="right">
				<tr valign="middle">
					<td width="24" height="25" valign="bottom"><img src="/images/searchHeaderLeft.gif" alt="" width="24" height="25" border="0"></td>
					<td><img src="/images/clear.gif" alt="" width="6" border="0" /></td>
    					<td class="login"><%= (tcUser == null ? "You are not logged in." : tcUser.getRegInfo().getUsername() + " is logged in.") %></td>
					<td><img src="/images/clear.gif" alt="" width="10" border="0" /></td>
<% if (tcUser == null) { %>
        				<td class="login"><a href="/admin/c_login.jsp" class="loginLinks">:: Login</a></td>
        				<td><img src="/images/clear.gif" alt="" width="10" border="0" /></td>
<% } else { %>
    					<td class="login"><a href="/admin/c_login.jsp?a=logout" class="loginLinks">:: Logout</a></td>
    					<td><img src="/images/clear.gif" alt="" width="10" border="0" /></td>
<% } %>
				</tr>
			</table>
		</td>
    	</tr>
    				
	<tr valign="bottom">
		<td class="headRight"><img src="/images/homeBannerHead.gif" alt="The Future of Software Development" width="510" height="13" border="0" /></td>
	</tr>
	
	<tr><td height="4" class="headStripe" colspan="3"><img src="/images/clear.gif" alt="" height="4" border="0" /></td></tr>
</table>
<!--Header ends -->
