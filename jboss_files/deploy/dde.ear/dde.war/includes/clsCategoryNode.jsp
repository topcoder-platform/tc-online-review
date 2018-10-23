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

%>