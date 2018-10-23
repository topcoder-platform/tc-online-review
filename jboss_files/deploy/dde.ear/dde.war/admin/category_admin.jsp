<%@ page import="javax.naming.*" %>
<%@ page import="javax.ejb.CreateException" %>
<%@ page import="java.io.*" %>
<%@ page import="java.rmi.*" %>
<%@ page import="javax.rmi.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.lang.reflect.*" %>

<%@ page import="com.topcoder.dde.catalog.*" %>

<%@ include file="/includes/util.jsp" %>
<%@ include file="/includes/session.jsp" %>

<%
    // STANDARD PAGE VARIABLES
    String page_name = "category_admin.jsp";
    String action = request.getParameter("a");
%>

<%

    if (session.getValue(/* "TCSUBJECT" */ "AdminTCSubject") == null) {
        session.putValue("nav_redirect", "/admin/category_admin.jsp");
        session.putValue("nav_redirect_msg", "You must login first.");
        response.sendRedirect("" + "/admin/c_login.jsp");
        return;
    }

    Object objCatalog = CONTEXT.lookup(CatalogHome.EJB_REF_NAME);
    CatalogHome home = (CatalogHome) PortableRemoteObject.narrow(objCatalog, CatalogHome.class);
    Catalog catalog = home.create();

    class CategoryNode {
            CategoryNode parent = null;
            Category category;
            ArrayList children = new ArrayList();

            public CategoryNode(Category catIn) {
                    category = catIn;
            }

            public Category getCategory() { return category; }

            public void setParent(CategoryNode parentIn) {
                    parent = parentIn;
            }

            public CategoryNode getParent() {
                return parent;
            }

            public void addChild(CategoryNode childIn) {
                    childIn.setParent(this);
                    children.add(childIn);
            }

            public ArrayList getChildren() { return children; }

            public boolean isAncestor(CategoryNode nodeIn) {
                if (nodeIn == null) return false;
                return (nodeIn.getParent() != null && nodeIn.getParent().getCategory().getId() == getCategory().getId()) || isAncestor(nodeIn.getParent());
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
    }
%>

<%
    long lngCategory = -1;
    long lngSubcategory = -1;

    try {
            lngCategory = Long.parseLong(request.getParameter("cat"));
    } catch (NumberFormatException nfe) {
            lngCategory = -1;
    }

    try {
        lngSubcategory = Long.parseLong(request.getParameter("subcategory"));
    } catch (NumberFormatException nfe) {
        lngSubcategory = -1;
    }

    if (lngSubcategory == -1) {
        try {
                lngSubcategory = Long.parseLong(request.getParameter("subcat"));
        } catch (NumberFormatException nfe) {
                lngSubcategory = -1;
        }
    }
%>

<%
    if (action != null) {
        if (action.equalsIgnoreCase("Select Market")) {
            lngCategory = Long.parseLong(request.getParameter("selLevel1"));
        }

        if (action.equalsIgnoreCase("Select Category")) {
            lngSubcategory = Long.parseLong(request.getParameter("selCategory"));
        }

        if (action.equalsIgnoreCase("Create New Market")) {
            String catName = request.getParameter("txtVerticalCategory");
            String catDesc = request.getParameter("taVerticalDescription");
            if (catName != null && catName.trim().length() > 0 && catDesc != null && catDesc.trim().length() > 0) {
                Category tmpCat = new Category(catName, catDesc);
                lngCategory = (catalog.addCategory(-1, tmpCat)).getId();
            }
        }

        if (action.equalsIgnoreCase("Update Market")) {
            String catName = request.getParameter("txtVerticalCategory");
            String catDesc = request.getParameter("taVerticalDescription");
            if (catName != null && catName.trim().length() > 0 && catDesc != null && catDesc.trim().length() > 0) {
                Category tmpCat = catalog.getCategory(lngCategory);
                tmpCat.setName(catName);
                tmpCat.setDescription(catDesc);
                catalog.updateCategory(tmpCat);
            }
        }

        if (action.equalsIgnoreCase("Delete Market")) {
            Category tmpCat = catalog.getCategory(lngCategory);
            catalog.removeCategory(lngCategory);
            lngCategory = -1;
        }

        if (action.equalsIgnoreCase("Create New Category")) {
            String catName = request.getParameter("txtCategoryName");
            String catDesc = request.getParameter("taCategoryDescription");
            if (catName != null && catName.trim().length() > 0 && catDesc != null && catDesc.trim().length() > 0) {
                Category tmpCat = new Category(catName, catDesc);
                lngSubcategory = (catalog.addCategory(lngCategory, tmpCat)).getId();
            }
        }

        if (action.equalsIgnoreCase("Update Category")) {
            String catName = request.getParameter("txtCategoryName");
            String catDesc = request.getParameter("taCategoryDescription");
            if (catName != null && catName.trim().length() > 0 && catDesc != null && catDesc.trim().length() > 0) {
                Category tmpCat = catalog.getCategory(lngSubcategory);
                tmpCat.setName(catName);
                tmpCat.setDescription(catDesc);
                catalog.updateCategory(tmpCat);
            }
        }

        if (action.equalsIgnoreCase("Delete Category")) {
            // get all checked categories
            Enumeration keys = request.getParameterNames();
            ArrayList alThreads = new ArrayList();
            while (keys.hasMoreElements()) {
                String key = "" + keys.nextElement();
                String val = request.getParameter(key);
                if (key.startsWith("selCategory_") && val != null) {
                    catalog.removeCategory(Long.parseLong(key.substring(key.indexOf("_") + 1, key.length())));
                }
            }
            lngSubcategory = -1;
        }
    }
%>

<%
    Hashtable htCategoryNodes = new Hashtable();
    CategoryNode root = new CategoryNode(null);
    Collection colCategories = catalog.getCategories();
    Category categories[] = (Category[])colCategories.toArray(new Category[0]);
    for (int i=0; i < categories.length; i++) {
            CategoryNode node = new CategoryNode(categories[i]);
            root.addChild(node);
            htCategoryNodes.put("" + node.getCategory().getId(), node);
            node.loadChildren(htCategoryNodes);
    }
%>

<%
    CategoryNode verticalMarketNode = (CategoryNode)htCategoryNodes.get("" + lngCategory);
    while (verticalMarketNode != null && verticalMarketNode.getParent().getCategory() != null) {
        verticalMarketNode = verticalMarketNode.getParent();
    }

    CategoryNode subcategoryNode = (CategoryNode)htCategoryNodes.get("" + lngSubcategory);
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/2000/REC-xhtml1-20000126/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>TopCoder Software</title>

<link rel="stylesheet" type="text/css" href="/includes/tcs_style.css" />
<jsp:include page="/includes/header-files.jsp" />
<script language="JavaScript" type="text/javascript" src="/scripts/javascriptAdmin.js">
</script>

</head>

<body class="body">

<!-- Header begins -->
<%@ include file="/includes/adminHeader.jsp" %>
<%@ include file="/includes/adminNav.jsp" %>
<!-- Header ends -->

<!-- breadcrumb begins -->
<table width="100%" cellpadding="0" cellspacing="0" border="0">
	<tr>
		<td valign="middle" width="1%" class="adminBreadcrumb"><img src="/images/clear.gif" alt="" width="10" height="1" border="0" /></td>
		<td valign="middle" width="1%" class="adminBreadcrumb" nowrap="nowrap"><strong>Category Admin</strong></td>
		<td valign="middle" width="98%" class="adminBreadcrumb"><img src="/images/clear.gif" alt="" width="10" height="1" border="0" /></td>
	</tr>
</table>
<!-- breadcrumb ends -->

<table width="100%" border="0" cellpadding="0" cellspacing="0" align="center" class="middle">
	<tr valign="top">

<!-- Left Column begins -->
		<td width="165" class="leftColumn">
			<table border="0" cellspacing="0" cellpadding="0">
				<tr><td height="5"><img src="/images/clear.gif" alt="" width="165" height="5" border="0" /></td></tr>
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
				<tr><td class="normal"><img src="/images/headCategoryAdmin.gif" alt="Category Admin" width="545" height="35" border="0" /></td></tr>
				<tr><td class="adminSubhead"><img src="/images/clear.gif" alt="" width="5" height="1" border="0" /></td></tr>
			</table>

			<table width="100%" cellpadding="0" cellspacing="6" align="center" border="0">
				<tr valign="top">
					<td align="center">

<!-- Vertical Markets begins -->
						<table cellpadding="0" cellspacing="0" border="0">
							<tr><td width="500" height="29"><img src="/images/headVertMarkAdmin.gif" alt="Vertical Markets" width="500" height="29" border="0" /></td></tr>
						</table>

<!-- Select a Market -->
						<table width="500" border="0" cellspacing="8" cellpadding="0" align="center" class="admin">
							<tr valign="middle">
								<td width="48%"><img src="/images/clear.gif" alt="" width="5" height="1" border="0" /></td>
								<td width="1%" class="adminTitleCenter" colspan="2" nowrap="nowrap">Choose a Market</td>
								<td width="48%"><img src="/images/clear.gif" alt="" width="5" height="1" border="0" /></td>
							</tr>

							<tr valign="middle">
								<td width="48%">
									<form name="frmVerticalMarket" action="category_admin.jsp" method="post">
									<input type="hidden" name="cat" value="<%= lngCategory %>"></input>
									<img src="/images/clear.gif" alt="" width="5" height="1" border="0" /></td>
								<td width="1%" class="adminText">
									<select class="adminForm" name="selLevel1">
                							<option value="-1">Create New Vertical Market</option>
										<% for (int i=0; i < categories.length; i++) { %>
										    <option value="<%= categories[i].getId() %>" <%= (categories[i].getId() == lngCategory ? "selected" : "") %>><%= categories[i].getName() %></option>
										<% } %>
									</select></td>
								<td width="1%" class="adminText"><input class="adminButton" type="submit" name="a" value="Select Market"></input></td>
								<td width="48%"><img src="/images/clear.gif" alt="" width="5" height="1" border="0" /></td>
							</tr>
						</table>

<% if (verticalMarketNode != null) { %>

<!-- Market Name -->
						<table width="500" border="0" cellspacing="8" cellpadding="0" align="center" class="admin">
							<tr valign="middle">
								<td width="48%"><img src="/images/clear.gif" alt="" width="5" height="1" border="0" /></td>
								<td width="1%" class="adminLabel" nowrap="nowrap">Market Name</td>
								<td width="1%" class="adminText"><input class="adminSearchForm" type="text" size="30" name="txtVerticalCategory" value="<%= verticalMarketNode.getCategory().getName() %>"></input></td>
								<td width="48%"><img src="/images/clear.gif" alt="" width="5" height="1" border="0" /></td>
							</tr>

<!-- Market Description -->
							<tr valign="top">
								<td width="48%"><img src="/images/clear.gif" alt="" width="5" height="1" border="0" /></td>
								<td width="1%" class="adminLabel" nowrap="nowrap">Market Description</td>
								<td width="1%" class="adminText"><textarea class="compSearchForm" name="taVerticalDescription" rows="5" cols="35"><%= verticalMarketNode.getCategory().getDescription() %></textarea></td>
								<td width="48%"><img src="/images/clear.gif" alt="" width="5" height="1" border="0" /></td>
							</tr>
						</table>

<!-- Submit Buttons -->
						<table width="500" border="0" cellspacing="8" cellpadding="0" align="center" class="admin">
							<tr valign="middle">
								<td width="48%"><img src="/images/clear.gif" alt="" width="5" height="1" border="0" /></td>
								<td width="1%" class="adminText"><input class="adminButton" type="reset" value="Clear Fields"></input></td>
								<td width="1%" class="adminText"><input class="adminButton" type="submit" name="a" value="Update Market"></input></td>
								<td width="1%" class="adminText"><input class="adminButton" type="submit" name="a" value="Delete Market"></input></td>
								<td width="48%"><img src="/images/clear.gif" alt="" width="5" height="1" border="0" /></td></tr>
						</table>

						<table width="500" cellpadding="0" cellspacing="0" border="0" align="center" class="admin">
							<tr><td><img src="/images/adminFoot.gif" alt="" width="500" height="11" border="0" /></td></tr>
						</table>
<!-- Vertical Markets Ends -->

<% } else { %>

						<table width="500" border="0" cellspacing="8" cellpadding="0" align="center" class="admin">
							<tr valign="middle">
								<td width="48%"><img src="/images/clear.gif" alt="" width="5" height="1" border="0" /></td>
								<td width="1%" class="adminTitleCenter" colspan="2" nowrap="nowrap">or Create a new one</td>
								<td width="48%"><img src="/images/clear.gif" alt="" width="5" height="1" border="0" /></td>
							</tr>

<!-- Market Name -->
							<tr valign="middle">
								<td width="48%"><img src="/images/clear.gif" alt="" width="5" height="1" border="0" /></td>
								<td width="1%" class="adminLabel" nowrap="nowrap">Market Name</td>
								<td width="1%" class="adminText"><input class="adminSearchForm" type="text" size="30" name="txtVerticalCategory" value=""></input></td>
								<td width="48%"><img src="/images/clear.gif" alt="" width="5" height="1" border="0" /></td>
							</tr>

<!-- Market Description -->
							<tr valign="top">
								<td width="48%"><img src="/images/clear.gif" alt="" width="5" height="1" border="0" /></td>
								<td width="1%" class="adminLabel" nowrap="nowrap">Market Description</td>
								<td width="1%" class="adminText"><textarea class="compSearchForm" name="taVerticalDescription" rows="5" cols="35"></textarea></td>
								<td width="48%"><img src="/images/clear.gif" alt="" width="5" height="1" border="0" /></td>
							</tr>
						</table>

<!-- Submit Button -->
						<table width="500" border="0" cellspacing="8" cellpadding="0" align="center" class="admin">
							<tr valign="middle">
								<td width="48%"><img src="/images/clear.gif" alt="" width="5" height="1" border="0" /></td>
								<td width="1%" class="adminText"><input class="adminButton" type="submit" name="a" value="Create New Market"></input></td>
								<td width="48%"><img src="/images/clear.gif" alt="" width="5" height="1" border="0" /></td>
							</tr>
						</table>

						<table width="500" cellpadding="0" cellspacing="0" border="0" align="center" class="admin">
							<tr><td><img src="/images/adminFoot.gif" alt="" width="500" height="11" border="0" /></td></tr>
						</table>
<% } %></form>
<!-- Vertical Markets Ends -->

<% if (verticalMarketNode != null) { %>
<!-- Spacer -->
						<table cellpadding="0" cellspacing="0" border="0" align="center">
							<tr><td height="15"><img src="/images/clear.gif" alt="" width="10" height="15" border="0" /></td></tr>
						</table>

<!-- Categories Begins -->
						<table cellpadding="0" cellspacing="0" border="0">
							<tr><td width="445" height="29"><img src="/images/headCategoriesAdmin.gif" alt="Categories" width="500" height="29" border="0" /></td></tr>
						</table>

<!-- Choose a Category -->
						<table width="500" border="0" cellspacing="8" cellpadding="0" align="center" class="admin">
							<tr valign="middle">
								<td width="48%"><img src="/images/clear.gif" alt="" width="5" height="1" border="0" /></td>
								<td width="1%" class="adminTitleCenter" colspan="2" nowrap="nowrap">Choose a Category</td>
								<td width="48%"><img src="/images/clear.gif" alt="" width="5" height="1" border="0" /></td>
							</tr>

							<tr valign="middle">
								<td width="48%">
									<form name="frmCategories" action="category_admin.jsp" method="post">
									<input type="hidden" name="cat" value="<%= lngCategory %>"></input>
									<input type="hidden" name="subcat" value="<%= lngSubcategory %>"></input>
									<img src="/images/clear.gif" alt="" width="5" height="1" border="0" /></td>

	<% CategoryNode children[] = (CategoryNode[])((verticalMarketNode.getChildren()).toArray(new CategoryNode[0])); %>

								<td width="1%" class="adminText">
									<select class="adminForm" name="selCategory">
                							<option value="-1">Create New Category</option>
										<% for (int i=0; i < children.length; i++) { %>
										<option value="<%= children[i].getCategory().getId() %>" <%= (children[i].getCategory().getId() == lngSubcategory ? "selected" : "") %>><%= children[i].getCategory().getName() %></option>
										<% } %>
									</select></td>
								<td width="1%" class="adminText"><input class="adminButton" type="submit" name="a" value="Select Category"></input></td>
								<td width="48%"><img src="/images/clear.gif" alt="" width="5" height="1" border="0" /></td>
							</tr>
						</table>

	<% if (subcategoryNode == null) { %>

						<table width="500" border="0" cellspacing="8" cellpadding="0" align="center" class="admin">
							<tr valign="middle">
								<td width="48%"><img src="/images/clear.gif" alt="" width="5" height="1" border="0" /></td>
								<td width="1%" class="adminTitleCenter" colspan="2" nowrap="nowrap">or Create a new one</td>
								<td width="48%"><img src="/images/clear.gif" alt="" width="5" height="1" border="0" /></td>
							</tr>

<!-- Category Name -->
							<tr valign="middle">
								<td width="48%"><img src="/images/clear.gif" alt="" width="5" height="1" border="0" /></td>
								<td width="1%" class="adminLabel" nowrap="nowrap">Category Name</td>
								<td width="1%" class="adminText"><input class="adminSearchForm" type="text" size="30" name="txtCategoryName" value=""></input></td>
								<td width="48%"><img src="/images/clear.gif" alt="" width="5" height="1" border="0" /></td>
							</tr>

<!-- Category Description -->
							<tr valign="top">
								<td width="48%"><img src="/images/clear.gif" alt="" width="5" height="1" border="0" /></td>
								<td width="1%" class="adminLabel" nowrap="nowrap">Market Description</td>
								<td width="1%" class="adminText"><textarea class="compSearchForm" name="taCategoryDescription" rows="5" cols="35"></textarea></td>
								<td width="48%"><img src="/images/clear.gif" alt="" width="5" height="1" border="0" /></td>
							</tr>
						</table>

<!-- Submit Button -->
						<table width="500" border="0" cellspacing="8" cellpadding="0" align="center" class="admin">
							<tr valign="middle">
								<td width="48%"><img src="/images/clear.gif" alt="" width="5" height="1" border="0" /></td>
								<td width="1%" class="adminText"><input class="adminButton" type="submit" name="a" value="Create New Category"></input></td>
								<td width="48%"><img src="/images/clear.gif" alt="" width="5" height="1" border="0" /></td></tr>
						</table>

						<table width="500" cellpadding="0" cellspacing="0" border="0" align="center" class="admin">
							<tr><td><img src="/images/adminFoot.gif" alt="" width="500" height="11" border="0" /></td></tr>
						</table>
<!-- Categories Ends -->

	<% } else { %>

<!-- Category Name -->
						<table width="500" border="0" cellspacing="8" cellpadding="0" align="center" class="admin">
							<tr valign="middle">
								<td width="48%"><img src="/images/clear.gif" alt="" width="5" height="1" border="0" /></td>
								<td width="1%" class="adminLabel" nowrap="nowrap">Category Name</td>
								<td width="1%" class="adminText"><input class="adminSearchForm" type="text" size="30" name="txtCategoryName" value="<%= subcategoryNode.getCategory().getName() %>"></input></td>
								<td width="48%"><img src="/images/clear.gif" alt="" width="5" height="1" border="0" /></td>
							</tr>

<!-- Category Description -->
							<tr valign="top">
								<td width="48%"><img src="/images/clear.gif" alt="" width="5" height="1" border="0" /></td>
								<td width="1%" class="adminLabel" nowrap="nowrap">Category Description</td>
								<td width="1%" class="adminText"><textarea class="compSearchForm" name="taCategoryDescription" rows="5" cols="35"><%= subcategoryNode.getCategory().getDescription() %></textarea></td>
								<td width="48%"><img src="/images/clear.gif" alt="" width="5" height="1" border="0" /></td>
							</tr>
						</table>

<!-- Submit Buttons -->
						<table width="500" border="0" cellspacing="8" cellpadding="0" align="center" class="admin">
							<tr valign="middle">
								<td width="48%"><img src="/images/clear.gif" alt="" width="5" height="1" border="0" /></td>
								<td width="1%" class="adminText"><input class="adminButton" type="submit" name="a" value="Delete Category"></input></td>
								<td width="1%" class="adminText"><input class="adminButton" type="reset" name="clear" value="Clear Fields"></input></td>
								<td width="1%" class="adminText"><input class="adminButton" type="submit" name="a" value="Update Category"></input></td>
								<td width="48%"><img src="/images/clear.gif" alt="" width="5" height="1" border="0" /></td>
							</tr>
						</table>

						<table width="500" cellpadding="0" cellspacing="0" border="0" align="center" class="admin">
							<tr><td><img src="/images/adminFoot.gif" alt="" width="500" height="11" border="0" /></td></tr>
						</table>
<!-- Categories Ends -->

	<% } %></form>

<% } %>

						<table width="500" cellpadding="0" cellspacing="0" border="0" align="center">
							<tr><td height="40"><img src="/images/clear.gif" alt="" width="10" height="40" border="0" /></td></tr>
						</table>
					</td>
				</tr>
			</table>
		</td>
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
