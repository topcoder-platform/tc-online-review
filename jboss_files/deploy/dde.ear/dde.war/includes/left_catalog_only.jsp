            
            <%
                Set expand = null;
                for (int i = 0; i < cats.length; i++) {
                    expand = cats[i].findForTree(lngCategory);
                    if (expand != null) break;
                }
                if (expand == null) expand = new HashSet();
                out.print(buildTree(cats, expand, 2, 2));
            %>
            
