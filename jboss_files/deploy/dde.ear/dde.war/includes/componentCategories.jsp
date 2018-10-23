<% // PAGE SPECIFIC DECLARATIONS %>
<%@ page import="com.topcoder.dde.catalog.*,
                 javax.rmi.PortableRemoteObject,
                 java.util.Hashtable,
                 java.util.Collection" %>
<%!
	class CategoryNode implements java.io.Serializable {
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
	}

	// VERTICAL MARKETS

%>

<%
	Object objTechTypes = CONTEXT.lookup(CatalogHome.EJB_REF_NAME);
	CatalogHome home = (CatalogHome) PortableRemoteObject.narrow(objTechTypes, CatalogHome.class);
	Catalog catalog = home.create();

	// Check session to see if we stored our categories in there
	boolean cameFromCache = false;
	Hashtable htCategories = new Hashtable();
	CategoryNode root = new CategoryNode(null);

	Object objCategories = session.getValue("Categories");
	Object objCacheTime = session.getValue("Categories_CacheTime");

    Collection colCategories = catalog.getCategories();

	if ((objCategories == null) || (objCacheTime != null) || ((System.currentTimeMillis() - ((Long)objCacheTime).longValue()) > 900000)) {
        debug.addMsg("showroom", "loading categories from ejb");
		// load categories and cache it
		Category categories[] = (Category[])colCategories.toArray(new Category[0]);
		for (int i=0; i < categories.length; i++) {
			CategoryNode node = new CategoryNode(categories[i]);
			root.addChild(node);
                        debug.addMsg("showroom", "adding category id " + node.getCategory().getId() + " with category " + categories[i].getId());
			htCategories.put("" + node.getCategory().getId(), node);
			node.loadChildren(htCategories);
		}
		//session.putValue("Categories", htCategories);
		//session.putValue("Categories_CacheTime", new Long(System.currentTimeMillis()));
		//cameFromCache = false;
    } else {
    	htCategories = (Hashtable)objCategories;
    	cameFromCache = true;
    }


	//Category arrVerticals[] = new Category[5];

    Category[] arrCategories = (Category[]) colCategories.toArray(new Category[0]);
    Category netCategory = null;
    Category javaCategory = null;
    Category flashCategory = null;
    for (int i = 0; i < arrCategories.length; i++) {
        if (arrCategories[i].getId() == Catalog.NET_CATALOG) netCategory = arrCategories[i];
        if (arrCategories[i].getId() == Catalog.JAVA_CATALOG) javaCategory = arrCategories[i];
        if (arrCategories[i].getId() == Catalog.FLASH_CATALOG) flashCategory = arrCategories[i];
    }
%>
