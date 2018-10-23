<table border="0" cellpadding="0" cellspacing="2">
    <tr><form name="frmSiteSearch" action="/catalog/c_showroom_search.jsp" method="post">
        <input type="hidden" name="a" value="search"/>
        <td valign="middle"><input class="searchForm" type="text" size="12" name="keywords" value="Search" maxlength="40" onFocus="javascript: if (document.frmSiteSearch.keywords.value=='Search') document.frmSiteSearch.keywords.value = '';" onBlur="javascript: if (document.frmSiteSearch.keywords.value == '') document.frmSiteSearch.keywords.value = 'Search';"></td>

<!-- uncomment this option when entire site search is implemented -->
        <!-- <td valign="middle"><select name="target">
            <option value="entire_site">Entire Site</option>
            <option value="all_catalogs" selected>All Catalogs</option>
            <option value="net_catalog">.NET Catalog</option>
            <option value="java_catalog">Java Catalog</option> -->
            <!-- <option value="forums">Forums</option>
        </select></td>
        <td valign="middle"><img src="/images/clear.gif" alt="" width="3" border="0" /></td> -->
<!-- uncomment this option when forum search is implemented -->

        <td valign="middle" width="99%" align="left"><input class="searchButton" type="submit" name="go" value="Go"/></td>
    </tr>
    
    <tr>
        <td valign="middle" colspan="2" align="left">&nbsp;<a href="/search/c_advanced_search.jsp">Advanced Search</a></td></form>
    </tr>
</table>
