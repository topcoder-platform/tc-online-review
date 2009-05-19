<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="html" uri="/tags/struts-html" %>
<%@ taglib prefix="bean" uri="/tags/struts-bean" %>
<%@ taglib prefix="tc-webtag" uri="/tags/tc-webtags" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>

<table class="scorecard" width="100%" cellpadding="0" cellspacing="0" style="border-collapse:collapse;">
		<tr>
			<td class="title" colspan="2"><bean:message key="viewProjectDetails.box.Links" /></td>
		</tr>
		<tr>
			<td class="header"><b><bean:message key="viewProjectDetails.Link.Project" /></b></td>
			<td class="header"><b><bean:message key="viewProjectDetails.Link.LinkType" /></b></td>
    </tr>
		<tr>
			<td class="lastRowTD" colspan='2'><!-- @ --></td>
		</tr>
	</table><br />    	  
</table>

<table class="scorecard" width="100%" cellpadding="0" cellspacing="0" style="border-collapse:collapse;">
		<tr>
			<td class="title" colspan="2"><bean:message key="viewProjectDetails.box.LinkTos" /></td>
		</tr>
		<tr>
			<td class="header"><b><bean:message key="viewProjectDetails.LinkTo.Project" /></b></td>
			<td class="header"><b><bean:message key="viewProjectDetails.LinkTo.LinkType" /></b></td>
    </tr>
		<tr>
			<td class="lastRowTD" colspan='2'><!-- @ --></td>
		</tr>
	</table><br />    	  
</table>