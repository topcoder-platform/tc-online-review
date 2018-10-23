<%@ page import="com.topcoder.shared.util.ApplicationServer" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
		"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
		
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<jsp:include page="/includes/header-files.jsp" />
<title>TopCoder Direct - Contact TopCoder</title>

<script src="/scripts/direct.js" type="text/javascript"></script>
<style>
#email-table {width:268px;margin:0;border-spacing:2px;text-align:left;background-color:none;}
#email-table td {border:none;background-color:#FFF;}
</style>
</head>
<body>
<jsp:include page="/includes/top.jsp">
<jsp:param name="TCDlevel" value="contact"/>
</jsp:include>


<%-- CONTENT BLOCKS --%>
	<div id="content">
		<div class="wrapper">
		
			<div id="content_main">
<%-- TABS BUTTON --%>
				<div id="tabs_button">
					<h3 class="hide">Tabs Navigation</h3>
					<ul>
						<li id="tabs_intro" class="tabs_bar first"><a href="#tabsContent01"><span>Contact Us</span></a></li>
					</ul>
				</div>
				
<%-- TABS CONTENT --%>
				<div id="tabs_panel" style="min-height:700px;">
					
					<%-- *************************************************
					TABS 01
					************************************************** --%>
					<div id="tabsContent01" class="tabs_content">
					
						<div class="content_columns">
					 <div>
           
							<p align="center"><strong><span style="font:12px arial;">However big or small your company, however big or small your problem,</span><br />
							<img style="margin:10px;" src="/images//bnr_tc_solution.png" alt="TopCoder can help you find the solution." /><br />
							<span style="font:12px arial;">Every day we work with Fortune 500 companies, small businesses and individuals alike, <br />
							helping them tap into the resources of our Global Community to tackle issues just like yours.</span><br /><br />
							<span style="font:14px arial;">Send us an email, and let's talk about how TopCoder can work for you!</span></strong></p>
							
							<br />
							
							<div style="width:268px; float:left; margin-right:100px;">
			
								<div align="center"><img src="/images//hdr_contact_form.png" alt="Contact Form" /></div>
								<br />
								<form action="https://www.salesforce.com/servlet/servlet.WebToLead?encoding=utf-8" method="post">
								
									<input type=hidden name="oid" value="00D300000001UY4">
									<input type=hidden name="retURL" value="http://www.topcoder.com/tc?module=Static&amp;d1=about&amp;d2=contactusSuccess">
									<input type=hidden name="lead_source" id="lead_source" value="Web" />
			   
									<table align="center" id="email-table">
										<tr>
											<td style="font-size:11px;"><label for="first_name">First Name</label></td><td style="font-size:11px;"><label for="last_name">Last Name</label></td>
			
										</tr>
										<tr>
											<td><input id="first_name" maxlength="40" name="first_name" type="text" style="width:90px;" /></td><td><input id="last_name" maxlength="80" name="last_name" type="text" style="width:160px;" /><br /></td>
										</tr>
										<tr>
											<td colspan="2" style="font-size:11px;"><label for="email">Your Email Address</label></td>
										</tr>
										<tr>
			
											<td colspan="2"><input id="email" maxlength="80" name="email" type="text" style="width:260px;" /><br /></td>
										</tr>
                                        <tr>
                                <td colspan="2" style="font-size:11px;"><label for="company">Company</label></td>
                            </tr>
                            <tr>
                                <td colspan="2"><input  id="company" maxlength="40" name="company" size="20" type="text" style="width:260px;" /><br /></td>
                            </tr>
                            <tr>
                            <tr>
                                <td colspan="2" style="font-size:11px;"><label for="state">State/Province</label></td>
                            </tr>
                            <tr>
                                <td colspan="2"><input  id="state" maxlength="20" name="state" size="20" type="text" style="width:260px;" /><br /></td>
                            </tr>
                            <tr>
                            <tr>
                                <td colspan="2" style="font-size:11px;"><label for="country">Country</label></td>
                            </tr>
                            <tr>
                                <td colspan="2"><input  id="country" maxlength="40" name="country" size="20" type="text" style="width:260px;" /><br /></td>
                            </tr>
                            <tr>
										<tr>
											<td colspan="2" style="font-size:11px;">How Can We Help You?</td>
										</tr>
										<tr>
											<td colspan="2"><textarea id="00N40000001mBlp" name="00N40000001mBlp" type="text" wrap="soft" style="width:260px; height:100px;"></textarea><br /></td>
										</tr>
			
										<tr>
											<td colspan="2" style="font-size:11px;"><label for="phone"><span style="font-size:10px;">(optional)</span> Leave a phone number and we'll call you</label></td>
										</tr>
										<tr>
											<td colspan="2"><input id="phone" maxlength="40" name="phone" type="text" style="width:260px;" /><br /></td>
										</tr>
									</table>
			
									<br />
									
									<p align="right"><input type="image" name="submit" img src="/images//btn_submit.png" alt="Submit"></p>
								</form>
							</div>
							
							<div style="width:314px; float:left;">
								<div align="center"><img src="/images//hdr_corporate.png" alt="Corporate" /></div>
								<p style="font:arial 14px;"><strong>TopCoder, Inc.</strong><br />
								95 Glastonbury Blvd.<br />
			
								Glastonbury, CT 06033 U.S.A.</p>
								
								<div align="center"><img src="/images//hdr_phonefax.png" alt="Phone and Fax" /></div>
								<p style="font:arial 14px;"><strong>Speak to a TopCoder Representative Today</strong><br />
								866.867.2633 or 860.633.5540<br />
								Fax: 860.657.4276</p>
								
								<div align="center"><img src="/images//hdr_email.png" alt="Email" /></div>
								<p style="font:arial 14px;"><a href="mailto:service@topcoder.com?subject=TopCoder Support &amp; Service">TopCoder Support &amp; Service</a><br />
								<a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=Static&amp;d1=pressroom&amp;d2=mediaRequestForm">Public Relations</a><br />
								<a href="mailto:service@topcoder.com?subject=Member Questions">Member Questions</a></p>
							</div>
							<p></p>
							<p></p>
                            <p></p>
                            <p></p>
                            <p></p>
                            <p></p>
                            <p></p>
						</div>


					  </div><%-- .content_columns --%>
						
					</div><%-- .tabs_content #01 --%>
						
				</div><%-- #tabs_panel ends --%>
			
			</div><%-- #content_main --%>
			
		</div><%-- .wrapper ends --%>
	</div><%-- #content ends --%>
	
<%-- Footer begins --%>
<jsp:include page="/includes/foot.jsp" flush="true" />
<%-- Footer ends --%>
	
<script type="text/javascript">
//<![CDATA[
var tabsBar = ["tabs_button"];
var tabsPanel = ["tabs_panel"];
var defaultTab = 0;	// index of default tab started from 0

window.onload = initDocument;
//]]>
</script>
	
</body>
</html>