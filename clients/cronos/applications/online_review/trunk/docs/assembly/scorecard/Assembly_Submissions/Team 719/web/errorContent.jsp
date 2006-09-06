<%-- 
   (#) errorContent.jsp
   ------------------------------------------------------------------
   @copyright Copyright (C) 2006, TopCoder Inc. All Rights Reserved.
   @author albertwang, flying2hk
   @version 1.0
   ------------------------------------------------------------------
   This is the content page of "error.jsp".
   It is displayed to the user when a general error
   (non-form-validation errors, e.g. server-side exception) occurs.
   ------------------------------------------------------------------
--%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<table width="100%" border="0" cellpadding="0" cellspacing="1" class="forumBkgd">
    <tr>
        <td class="errorText" style="height: 100%">
            <html:errors />
        </td>
    </tr>
</table>
<p align="center">
<form action="javascript:history.back()"> 
    <html:submit style="width:125px; float:center;" styleClass="Buttons">
        <bean:message key="global.button.back"/>
    </html:submit>
</form>
            