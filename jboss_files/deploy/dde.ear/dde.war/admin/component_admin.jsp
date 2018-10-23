<%@ page import="javax.naming.*,
                 org.apache.log4j.lf5.viewer.categoryexplorer.CategoryNode" %>
<%@ page import="javax.ejb.CreateException" %>
<%@ page import="java.io.*" %>
<%@ page import="java.rmi.*" %>
<%@ page import="javax.rmi.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.lang.reflect.*" %>

<%@ page import="com.topcoder.dde.catalog.*" %>

<%@ include file="/includes/util.jsp" %>
<%@ include file="session.jsp" %>
<%@ include file="/includes/formclasses.jsp" %>

<%
    // STANDARD PAGE VARIABLES
    String page_name = "component_admin.jsp";
    String action = request.getParameter("a");
%>
<%!
    String masterList(Collection cats, String prefix) {
        String rv = "";
        if (cats == null) return rv;
        Category[] categories = (Category[]) cats.toArray(new Category[0]);
        for (int i = 0; i < categories.length; i++) {
            rv += "<option value=\"" + categories[i].getId() + "\">" + prefix + categories[i].getName() + "</option>\n";
            rv += masterList(categories[i].getSubcategories(), prefix + categories[i].getName() + "->");
        }
        return rv;
    }
%>


<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>TopCoder Software</title>

<link rel="stylesheet" type="text/css" href="/includes/tcs_style.css" />
<jsp:include page="/includes/header-files.jsp" />
<script language="JavaScript" type="text/javascript" src="/scripts/javascriptAdmin.js">
</script>

<%
class CategoryNode {
    CategoryNode parent = null;
    Category category;

    public CategoryNode(Category catIn) {
            category = catIn;
    }

    public Category getCategory() { return category; }

    public void setParent(CategoryNode parentIn) {
        parent = parentIn;
    }

    public CategoryNode getParent() { return parent; }

    public void addChild(CategoryNode childIn) {
        childIn.setParent(this);
    }

    public void loadChildren(Hashtable htIn) {
        Collection colSubcategories = category.getSubcategories();
        Category categories[] = (Category[])colSubcategories.toArray(new Category[0]);
        for (int i=0; i < categories.length; i++) {
            CategoryNode node = new CategoryNode(categories[i]);
            addChild(node);
            htIn.put("" + node.getCategory().getId(), node);
            node.loadChildren(htIn);
        }
    }

    public String buildBreadCrumb(boolean showAnchor) {
        if (parent == null) {
            return "<a href=\"c_showroom.jsp\" class=\"breadcrumbLinks\">Component Catalog</a>";
        } else {
            String str = parent.buildBreadCrumb(true) + " &gt; ";
            if (showAnchor) {
                    str += "<a href=\"c_showroom.jsp?cat=" + category.getId() + "\" class=\"breadcrumbLinks\">" + category.getName() + "</a>";
            } else {
                    str += "<strong>" + category.getName() + "</strong>";
            }
            return str;
        }
    }

    public String toFullString() {
        if (parent == null) {
            return category.getName();
        } else {
            return parent.getCategory().getName() + "->" + category.getName();
        }
    }
}
%>

<%
Object objTechTypes = CONTEXT.lookup(CatalogHome.EJB_REF_NAME);
CatalogHome home = (CatalogHome) PortableRemoteObject.narrow(objTechTypes, CatalogHome.class);
Catalog catalog = home.create();

Object objComponentMgr = CONTEXT.lookup(ComponentManagerHome.EJB_REF_NAME);
ComponentManagerHome component_manager_home = (ComponentManagerHome) PortableRemoteObject.narrow(objComponentMgr, ComponentManagerHome.class);
ComponentManager componentManager = null;

// Category stuff
Hashtable htCategories = new Hashtable();
CategoryNode root = new CategoryNode(null);

Collection colCategories = catalog.getCategories();
Category categories[] = (Category[])colCategories.toArray(new Category[0]);
for (int i=0; i < categories.length; i++) {
    CategoryNode node = new CategoryNode(categories[i]);
    root.addChild(node);
    htCategories.put("" + node.getCategory().getId(), node);
    node.loadChildren(htCategories);
}

long lngComponent = 0;

try {
    lngComponent = Long.parseLong(request.getParameter("comp"));
    componentManager = component_manager_home.create(lngComponent);
} catch (Exception e) {
    response.sendRedirect("catalog.jsp");
}

ComponentInfo comp = componentManager.getComponentInfo();

String strError = "";
String strMessage = "";

if (request.getParameter("add.x") != null) {
    // Add category
    String strCategory = request.getParameter("selMasterCategory");
    if (strCategory != null) {
        try {
            componentManager.addCategory(Long.parseLong(strCategory));
            strMessage += "Category " + strCategory + " was assigned to component";
        } catch (Exception e) {
            debug.addMsg("component admin", "add category " + strCategory + " error: " + e.getMessage());
        }
    }
}

    if (request.getParameter("set.x") != null) {
        // Set Catalog
        String strCatalog = request.getParameter("selCatalog");
        if (strCatalog != null) {
            try {
                long lngCatalog = Long.parseLong(strCatalog);
                componentManager.setRootCategory(lngCatalog);
                Category compCategories[] = (Category[])componentManager.getCategories().toArray(new Category[0]);
                for (int i = 0; i < compCategories.length; i++) {
                    componentManager.removeCategory(compCategories[i].getId());
                }
                strMessage += "Catalog was changed and associated categories removed.";
            } catch (Exception e) {
                debug.addMsg("component admin", "change catalog error: " + e.getMessage());
            }
        }
    }

if (request.getParameter("remove.x") != null) {
    // Remove category
    String strCategory = request.getParameter("selComponentCategory");
    if (strCategory != null) {
        try {
            componentManager.removeCategory(Long.parseLong(strCategory));
            strMessage += "Category was unassigned from component";
        } catch (Exception e) {
            debug.addMsg("component admin", "remove category error: " + e.getMessage());
        }
    }
}


if (action != null) {
    strMessage += "action is " + action + "<BR>";
    if (action.equalsIgnoreCase("Approve")) {
        try {
            catalog.approveComponent(lngComponent);
            comp.setStatus(ComponentInfo.APPROVED);
            strMessage = "Component was approved";
        } catch (RemoteException re) {
            strError = "A remote exception occurred while approving component: " + re.getMessage();
        } catch (CatalogException ce) {
            strError = "A catalog exception occurred while approving component: " + ce.getMessage();
        }
    }

    if (action.equalsIgnoreCase("Decline")) {
        try {
            String isDuplicate = request.getParameter("isDuplicate");
            if (isDuplicate.equals("true")) {
                catalog.declineComponent(lngComponent, true);
                comp.setStatus(ComponentInfo.DECLINED);
                strMessage = "Component was a duplicate and was declined";
            } else {
                catalog.declineComponent(lngComponent, false);
                comp.setStatus(ComponentInfo.DECLINED);
                strMessage = "Component was declined";
            }
        } catch (RemoteException re) {
            strError = "A remote exception occurred while declining component: " + re.getMessage();
        } catch (CatalogException ce) {
            strError = "A catalog exception occurred while declining component: " + ce.getMessage();
        }
    }

    if (action.equalsIgnoreCase("Delete")) {
        try {
            catalog.removeComponent(lngComponent);
            comp.setStatus(ComponentInfo.DELETED);
            strMessage = "Component was deleted";
        } catch (RemoteException re) {
            strError = "A remote exception occurred while deleting component: " + re.getMessage();
        } catch (CatalogException ce) {
            strError = "A catalog exception occurred while deleting component: " + ce.getMessage();
        }
    }

    if (action.equalsIgnoreCase("Save")) {
        // Get fields and set component info
        String name =       request.getParameter("txtName");
        String shortDesc =  request.getParameter("txtShortDescription");
        String desc =       request.getParameter("txtDescription");
        String funcDesc =   request.getParameter("txtFunctionalDescription");
        String keywords =   request.getParameter("txtKeywords");
        //String status =     request.getParameter("selStatus");
        comp.setName(name);
        comp.setShortDescription(shortDesc);
        comp.setDescription(desc);
        comp.setFunctionalDescription(funcDesc);
        comp.setKeywords(keywords);
        //comp.setStatus(Long.parseLong(status));
        try {
            componentManager.updateComponentInfo(comp);
            strMessage += "Component info was saved.";
        } catch (Exception e) {
            strError += "An error occurred while updating component info.";
            comp = componentManager.getComponentInfo();
        }
    }
    if (action.equals(">>")) {
        // Add category
        String strCategory = request.getParameter("selMasterCategory");
        if (strCategory != null) {
            try {
		System.out.println( "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++" );
                componentManager.addCategory(Long.parseLong(strCategory));
                strMessage += "Category " + strCategory + " was assigned to component";
		System.out.println( strMessage );
            } catch (Exception e) {
                debug.addMsg("component admin", "add category " + strCategory + " error: " + e.getMessage());
            }
        }
    }

    if (action.equals("<<")) {
        // Remove category
        String strCategory = request.getParameter("selComponentCategory");
        if (strCategory != null) {
            try {
                componentManager.removeCategory(Long.parseLong(strCategory));
                strMessage += "Category was unassigned from component";
            } catch (Exception e) {
                debug.addMsg("component admin", "remove category error: " + e.getMessage());
            }
        }
    }

    if (action.equalsIgnoreCase("catalog")) {
        String strCatalog = request.getParameter("selCatalog");
        if (strCatalog != null) {
            try {
                long lngCatalog = Long.parseLong(strCatalog);
                componentManager.setRootCategory(lngCatalog);
                Category compCategories[] = (Category[])componentManager.getCategories().toArray(new Category[0]);
                for (int i = 0; i < compCategories.length; i++) {
                    componentManager.removeCategory(compCategories[i].getId());
                }
                strMessage += "Catalog was changed and associated categories removed.";
            } catch (Exception e) {
                debug.addMsg("component admin", "change catalog error: " + e.getMessage());
            }
        }
    }
}
%>

</head>

<body class="body">

<!-- Header begins -->
<%@ include file="/includes/adminHeader.jsp" %>
<%@ include file="/includes/adminNav.jsp" %>
<!-- Header ends -->

<!-- breadcrumb begins -->
<table width="100%" cellpadding="0" cellspacing="0" border="0">
	<tr>
		<td valign="middle" width="1%" class="adminBreadcrumb"><img src="/images/clear.gif" alt="" width="10" height="1" border="0"/></td>
		<td valign="middle" width="1%" class="adminBreadcrumb" nowrap><a class="breadcrumbLinks" href="catalog.jsp">Catalog Admin</a> > <strong>Component Admin</strong></td>
		<td valign="middle" width="98%" class="adminBreadcrumb"><img src="/images/clear.gif" alt="" width="10" height="1" border="0"/></td>
	</tr>
</table>
<!-- breadcrumb ends -->

<% if (strError.length() > 0) { %>
    <font color="RED"><%= strError %></font>
    <hr><br>
<% } %>

<% if (strMessage.length() > 0) { %>
    <%= strMessage %>
    <hr><br>
<% } %>

<table width="100%" border="0" cellpadding="0" cellspacing="0" align="center" class="middle">
	<tr valign="top">

<!-- Left Column begins -->
		<td width="165" class="leftColumn">
			<table border="0" cellspacing="0" cellpadding="0">
				<tr><td height="5"><img src="/images/clear.gif" alt="" width="165" height="5" border="0" /></td></tr>
<!-- Catalog Admin Navigation -->
				<tr valign="top">
					<td class="adminLeftNav">
						<table border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td width="5" class="adminLeftHead"><img src="/images/clear.gif" alt="" width="5" height="10" border="0"></td>
								<td width="155" class="adminLeftHead" colspan="2">Catalog Admin</td>
								<td width="10" class="adminLeftHead" valign="top"><img src="/images/leftNavHead.gif" alt="" width="10" height="22" border="0" /></td>
							</tr>
							<tr valign="top">
								<td><img src="/images/clear.gif" alt="" width="5" height="5" border="0"></td>
								<td class="adminLeftNavText">::&nbsp;</td>
								<td class="adminLeftNavText"><a href="catalog.jsp">Request Component</a></td>
								<td><img src="/images/clear.gif" alt="" width="10" height="5" border="0" /></td>
							</tr>
							<tr valign="top">
								<td><img src="/images/clear.gif" alt="" width="5" height="5" border="0"></td>
								<td class="adminLeftNavText">::&nbsp;</td>
								<td class="adminLeftNavText"><a href="comp_status.jsp">View Components by Status</a></td>
								<td><img src="/images/clear.gif" alt="" width="10" height="5" border="0" /></td>
							</tr>
							<tr><td colspan="4"><img src="/images/adminLeftNavFoot.gif" alt="" width="165" height="11" border="0" /></td></tr>
						</table>
					</td>
				</tr>
				<tr><td height="5"><img src="/images/clear.gif" alt="" width="10" height="5" border="0" /></td></tr>
			</table>
		</td>
		<td width="5" class="leftColumn"><img src="/images/clear.gif" alt="" width="5" height="10" border="0" /></td>
<!-- Left Column ends -->

<!-- Gutter 1 begins -->
		<td width="15"><img src="/images/clear.gif" alt="" width="15" height="10" border="0" /></td>
<!-- Gutter 1 ends -->

<!-- Middle Column begins -->
		<td width="100%">
			<table width="100%" border="0" cellpadding="0" cellspacing="0" align="center">
				<tr><td height="15"><img src="/images/clear.gif" alt="" width="10" height="15" border="0" /></td></tr>
				<tr><td class="normal"><img src="/images/headUserAdmin.gif" alt="User Admin" width="545" height="35" border="0" /></td></tr>

<% if (comp != null) { %>

				<tr><td class="adminSubhead"><%= comp.getName() %></td></tr>
			</table>

			<table width="100%" cellpadding="0" cellspacing="0" align="center" border="0">
				<tr valign="top">
					<td align="center">

						<table cellpadding="0" cellspacing="0" border="0">
							<tr><td width="445" height="29" colspan="2"><img src="/images/adminNameDescHead.gif" alt="Name and Description" width="500" height="29" border="0" /></td></tr>
						</table>

						<table width="500" border="0" cellspacing="8" cellpadding="0" align="center" class="admin">

<!-- Component Name -->
<form name="frmComponent" action="component_admin.jsp" method="POST">
<input type="HIDDEN" name="comp" value="<%= comp.getId() %>">
							<tr valign="middle">
								<td width="48%">
                                                                <img src="/images/clear.gif" alt="" width="5" height="1" border="0"/></td>
								<td width="1%" class="adminLabel" nowrap>Component Name</td>
								<td width="1%" class="adminText"><input class="adminSearchForm" type="text" size="35" maxlength="40" name="txtName" value="<%= comp.getName() %>"></input></td>
								<td width="48%"><img src="/images/clear.gif" alt="" width="5" height="1" border="0"/></td>
							</tr>

<!-- Keywords -->
							<tr valign="top">
								<td width="48%"><img src="/images/clear.gif" alt="" width="5" height="1" border="0"/></td>
								<td width="1%" class="adminLabel" nowrap>Keywords</td>
								<td width="1%" class="adminText"><textarea class="compSearchForm" name="txtKeywords" rows="4" cols="35"><%= comp.getKeywords() %></textarea></td>
								<td width="48%"><img src="/images/clear.gif" alt="" width="5" height="1" border="0"/></td>
							</tr>

<!-- Functional Description -->
							<tr valign="top">
								<td width="48%"><img src="/images/clear.gif" alt="" width="5" height="1" border="0"/></td>
								<td width="1%" class="adminLabel" nowrap>Functional Description</td>
								<td width="1%" class="adminText">
									<textarea class="compSearchForm" name="txtFunctionalDescription" rows="8" cols="35"><%= comp.getFunctionalDescription() %></textarea></td>
								<td width="48%"><img src="/images/clear.gif" alt="" width="5" height="1" border="0"/></td>
							</tr>

<!-- Long Description -->
							<tr valign="top">
								<td width="48%"><img src="/images/clear.gif" alt="" width="5" height="1" border="0"/></td>
								<td width="1%" class="adminLabel" nowrap>Long Description</td>
								<td width="1%" class="adminText">
									<textarea class="compSearchForm" name="txtDescription" rows="12" cols="35"><%= comp.getDescription() %></textarea></td>
								<td width="48%"><img src="/images/clear.gif" alt="" width="5" height="1" border="0"/></td>
							</tr>

<!-- Short Description -->
							<tr valign="top">
								<td width="48%"><img src="/images/clear.gif" alt="" width="5" height="1" border="0"/></td>
								<td width="1%" class="adminLabel" nowrap>Short Description</td>
								<td width="1%" class="adminText">
									<textarea class="compSearchForm" name="txtShortDescription" rows="4" cols="35"><%= comp.getShortDescription() %></textarea></td>
								<td width="48%"><img src="/images/clear.gif" alt="" width="5" height="1" border="0"/></td>
							</tr>

<!-- Status -->
<tr valign="middle">
    <td width="48%"><img src="/images/clear.gif" alt="" width="5" height="1" border="0"/></td>
    <td width="1%" class="adminLabel" nowrap>Status</td>
    <td width="1%" class="adminText">
        <INPUT TYPE="HIDDEN" NAME="isDuplicate" VALUE="<%= (comp.getStatus() == ComponentInfo.DUPLICATE ? "true" : "false") %>">
        <%= (comp.getStatus() == ComponentInfo.APPROVED ? " APPROVED" : "<INPUT TYPE=\"SUBMIT\" NAME=\"a\" VALUE=\"Approve\">") %>
        <%= (comp.getStatus() == ComponentInfo.DECLINED ? " DECLINED" : "<INPUT TYPE=\"SUBMIT\" NAME=\"a\" VALUE=\"Decline\">") %>
        <%= (comp.getStatus() == ComponentInfo.DELETED ? " DELETED" : "<INPUT TYPE=\"SUBMIT\" NAME=\"a\" VALUE=\"Delete\">") %>
        <%= (comp.getStatus() == ComponentInfo.DUPLICATE ? " DUPLICATE" : "") %>
        <%= (comp.getStatus() == ComponentInfo.REQUESTED ? " REQUESTED" : "") %>
    <td width="48%"><img src="/images/clear.gif" alt="" width="5" height="1" border="0"/></td>
</tr>

<!-- Submit Button -->
							<tr valign="middle">
								<td width="48%"><img src="/images/clear.gif" alt="" width="5" height="1" border="0"/></td>
								<td colspan="2"><img src="/images/clear.gif" alt="" width="5" height="5" border="0"/></td>
								<td width="48%"><img src="/images/clear.gif" alt="" width="5" height="1" border="0"/></td>
							</tr>
							<tr valign="middle">
								<td width="48%"><img src="/images/clear.gif" alt="" width="5" height="1" border="0"/></td>
								<td colspan="2" class="adminTextCenter"><input class="adminButton" type="submit" name="a" value="Save"></td>
								<td width="48%"><img src="/images/clear.gif" alt="" width="5" height="1" border="0"/></td>
							</tr>
						</table>

						<table width="500" cellpadding="0" cellspacing="0" border="0" align="center" class="admin">
							<tr><td><img src="/images/adminFoot.gif" alt="" width="500" height="11" border="0" /></td></tr>
						</table>
<!-- User Info Ends -->

						<table width="500" cellpadding="0" cellspacing="0" border="0" align="center">
							<tr><td height="15"><img src="/images/clear.gif" alt="" width="10" height="15" border="0"/></td></tr>
						</table>
					</td>
				</tr>
			</table>

			<table width="100%" cellpadding="0" cellspacing="6" align="center" border="0">
				<tr valign="top">
					<td align="center">

						<table cellpadding="0" cellspacing="0" border="0">
							<tr><td width="445" height="29" colspan="2"><img src="/images/headCatalogAdmin.gif" alt="Catalog" width="500" height="29" border="0" /></td></tr>
						</table>

						<table width="500" border="0" cellspacing="8" cellpadding="0" align="center" class="admin">
							<tr valign="middle">
                                <td></td>
								<td class="adminTextCenter">Master Catalog List</td>
                                <td></td>
							</tr>

							<tr valign="middle">
                                <td></td>
								<td width="1%" class="adminTextCenter">
									<select name="selCatalog" size="8" multiple="multiple">
                                    			<option value="0">None</option>
<% for (int i = 0; i < categories.length; i++) {
%>
                                                <option value="<%= categories[i].getId() %>"><%= categories[i].getName() %></option>
<% }
%>
									</select></td>
                                <td></td>
							</tr>

<!-- Navigation Buttons -->
							<tr valign="middle">
                                <td></td>
								<td width="1%" class="adminTextCenter"><input type="image" src="../images/buttonAdd.gif" alt="\/" name="set" value="catalog" /></td>
                                <td></td>
							</tr>

<!-- Associated Categories -->
							<tr valign="middle">
                                <td></td>
								<td class="adminTextCenter">
<%
    Category rootCategory = null;
    for (int i = 0; i < categories.length; i++) {
        if (categories[i].getId() == componentManager.getRootCategory()) {
            rootCategory = categories[i];
        }
    }
    out.print(rootCategory == null ? "None" : rootCategory.getName());
%>
                                </td>
                                <td></td>
							</tr>

							<tr valign="middle">
                                <td></td>
								<td class="adminTextCenter">Associated Catalog for <strong><%= comp.getName() %></strong></td>
                                <td></td>
							</tr>
						</table>

						<table width="500" cellpadding="0" cellspacing="0" border="0" align="center" class="admin">
							<tr><td><img src="/images/adminFoot.gif" alt="" width="500" height="11" border="0" /></td></tr>
						</table>

						<table width="500" cellpadding="0" cellspacing="0" border="0" align="center">
							<tr><td height="15"><img src="/images/clear.gif" alt="" width="10" height="15" border="0"/></td></tr>
						</table>
					</td>
				</tr>
                <tr><td>Warning: changing the catalog to which a component belongs will disassociate it with all associated categories.</td></tr>
			</table>

			<table width="100%" cellpadding="0" cellspacing="6" align="center" border="0">
				<tr valign="top">
					<td align="center">

						<table cellpadding="0" cellspacing="0" border="0">
							<tr><td width="445" height="29" colspan="2"><img src="/images/adminAssocCateHead.gif" alt="Associated Categories" width="500" height="29" border="0" /></td></tr>
						</table>

						<table width="500" border="0" cellspacing="8" cellpadding="0" align="center" class="admin">

        <%
            // List all categories
            ArrayList alLeafCategories = new ArrayList();
            for (int i=0; i < categories.length; i++) {
                Category subCats[] = (Category[])categories[i].getSubcategories().toArray(new Category[0]);
                for (int j=0; j < subCats.length; j++) {
                    alLeafCategories.add(subCats[j]);
                }
            }

            // List categories for this component
            Category compCategories[] = (Category[])componentManager.getCategories().toArray(new Category[0]);
            Hashtable htHits = new Hashtable();
            for (int i=0; i< compCategories.length; i++) {
                htHits.put("" + compCategories[i].getId(), "hit");
            }
        %>

<!-- Master List -->
							<tr valign="middle">
								<td width="48%"><img src="/images/clear.gif" alt="" width="5" height="1" border="0"/></td>
								<td width="1%" class="adminTextCenter">Master Category List</td>
								<td width="48%"><img src="/images/clear.gif" alt="" width="5" height="1" border="0"/></td>
							</tr>

							<tr valign="middle">
								<td width="48%"><img src="/images/clear.gif" alt="" width="5" height="1" border="0"/></td>
								<td width="1%" class="adminTextCenter">
									<select name="selMasterCategory" size="8" multiple="multiple">
            <%
                            for (int i=0; i < categories.length; i++) {
                                if (rootCategory != null && rootCategory.getId() == categories[i].getId()) out.print(masterList(categories[i].getSubcategories(), ""));
                            }
            %>
									</select></td>
								<td width="48%"><img src="/images/clear.gif" alt="" width="5" height="1" border="0"/></td>
							</tr>

<!-- Navigation Buttons -->
							<tr valign="middle">
								<td width="48%"><img src="/images/clear.gif" alt="" width="5" height="1" border="0"/></td>
								<td width="1%" class="adminTextCenter"><input type="image" src="../images/buttonAdd.gif" alt="\/" name="add" value=">>" />&nbsp;&nbsp;&nbsp;<input type="image" src="../images/buttonRemove.gif" alt="/\" name="remove" value="<<" /></td>
								<td width="48%"><img src="/images/clear.gif" alt="" width="5" height="1" border="0"/></td>
							</tr>

<!-- Associated Categories -->
							<tr valign="middle">
								<td width="48%"><img src="/images/clear.gif" alt="" width="5" height="1" border="0"/></td>
								<td width="1%" class="adminTextCenter">
									<select name="selComponentCategory"size="4" multiple="multiple">
<%
                    for (int i=0; i < compCategories.length; i++) {
                        debug.addMsg("component admin", "adding comp category " + compCategories[i].getId());
                        if (htCategories.get("" + compCategories[i].getId()) != null) {
%>
                            				<option value="<%= compCategories[i].getId() %>"><%= ((CategoryNode)htCategories.get("" + compCategories[i].getId())).toFullString() %></option>
<%
                        } else {
                            debug.addMsg("component admin", "comp category " + compCategories[i].getId() + " doesn't exist anymore");
                        }
                    }
%>
									</select></td>
								<td width="48%"><img src="/images/clear.gif" alt="" width="5" height="1" border="0"/></td>
							</tr>

							<tr valign="middle">
								<td width="48%"><img src="/images/clear.gif" alt="" width="5" height="1" border="0"/></td>
								<td width="1%" class="adminTextCenter">Associated Categories for <strong><%= comp.getName() %></strong></td>
								<td width="48%"><img src="/images/clear.gif" alt="" width="5" height="1" border="0"/></td>
							</tr>
						</table>

						<table width="500" cellpadding="0" cellspacing="0" border="0" align="center" class="admin">
							<tr><td><img src="/images/adminFoot.gif" alt="" width="500" height="11" border="0" /></td></tr>
						</table>

						<table width="500" cellpadding="0" cellspacing="0" border="0" align="center">
							<tr><td height="15"><img src="/images/clear.gif" alt="" width="10" height="15" border="0"/></td></tr>
						</table>
					</td>
				</tr>
			</table>

<!-- Versions begins -->
			<table width="100%" border="0" cellpadding="0" cellspacing="0" align="center">
				<tr><td class="adminSubhead">Versions</td></tr>
			</table>

			<table width="100%" border="0" cellpadding="0" cellspacing="1" align="center" bgcolor="#FFFFFF">
				<tr valign="top">
					<td width="20%" class="adminTitle">Version</td>
					<td width="70%" class="adminTitle">Comments</td>
					<td width="10%" class="adminTitleCenter">Action</td>
				</tr>

<%
    Collection colVersions = componentManager.getAllVersionInfo();
    ComponentVersionInfo versions[] = (ComponentVersionInfo[])colVersions.toArray(new ComponentVersionInfo[0]);
%>

	<%
		for (int i=0; i < versions.length; i++) {
	%>
				<tr valign="top">
					<td class="forumText"><strong><a href="component_version_admin.jsp?comp=<%= lngComponent %>&ver=<%= versions[i].getVersion() %>">Version <%= versions[i].getVersionLabel() %></a></strong></td>
					<td class="forumText"><%= versions[i].getComments() %></td>
					<td class="forumTextCenter"><a class="edit" href="component_version_admin.jsp?comp=<%= lngComponent %>&ver=<%= versions[i].getVersion() %>">EDIT</a></td>
				</tr>

	<% } %>

				<tr><td class="adminTitleCenter" colspan="4"><strong>[<a href="component_version_admin.jsp?comp=<%= lngComponent %>&ver=0&a=Create">Create New Version</a>]</strong></td></tr>
<!-- Versions ends -->

			</table>

<% } %>

		</td>
</form>
<!-- Middle Column ends -->

<!-- Gutter 2 begins -->
		<td width="15"><img src="/images/clear.gif" alt="" width="15" height="10" border="0" /></td>
<!-- Gutter 2 ends -->

<!-- Right Column begins -->
		<td width="245">
			<table border="0" cellpadding="0" cellspacing="0">
				<tr><td height="15"><img src="/images/clear.gif" alt="" width="245" height="15" border="0" /></td></tr>
			</table>
		</td>
<!--Right Column ends -->

<!-- Gutter 3 begins -->
		<td width="10"><img src="/images/clear.gif" alt="" width="10" height="10" border="0" /></td>
<!-- Gutter 3 ends -->
	</tr>
</table>

<!-- Footer begins -->
<jsp:include page="/includes/foot.jsp" flush="true" />
<!-- Footer ends -->

</body>
</html>
