<%@ page import="javax.naming.*,
                 javax.ejb.FinderException,
                 com.topcoder.dde.submission.Submission,
                 com.topcoder.dde.submission.Submitter,
                 com.topcoder.dde.submission.Submitters,
                 com.topcoder.dde.submission.Utility,
                 com.topcoder.dde.persistencelayer.interfaces.LocalDDEDocTypesHome,
                 com.topcoder.dde.persistencelayer.interfaces.LocalDDEDocTypes,
                 com.topcoder.file.TCSFile,
                 com.topcoder.dde.persistencelayer.interfaces.LocalDDECompCatalog,
                 com.topcoder.dde.persistencelayer.interfaces.LocalDDECategories,
                 com.topcoder.dde.persistencelayer.interfaces.LocalDDECompCatalogHome,
                 com.topcoder.dde.persistencelayer.interfaces.LocalDDECategoriesHome" %>
<%@ page import="javax.ejb.CreateException" %>
<%@ page import="java.io.*" %>
<%@ page import="java.rmi.*" %>
<%@ page import="javax.rmi.*" %>
<%@ page import="java.net.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.lang.reflect.*" %>
<%@ page import="javax.naming.Context" %>

<%@ page import="com.topcoder.dde.catalog.*" %>
<%@ page import="com.topcoder.web.ejb.forums.*" %>
<%@ page import="com.topcoder.util.config.*" %>
<%@ page import="com.topcoder.servlet.request.*" %>
<%@ page import="com.topcoder.shared.util.TCContext" %>
<%@ page import="com.topcoder.shared.util.ApplicationServer" %>

<%@ include file="/includes/util.jsp" %>
<%@ include file="session.jsp" %>
<%@ include file="/includes/formclasses.jsp" %>

<%
    // STANDARD PAGE VARIABLES
    String page_name = "component_version_admin.jsp";
    String action = request.getParameter("a");
    String namespace = "com.topcoder.servlet.request.FileUpload";
    java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("MM/dd/yyyy");

    // Logger instance.
    org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger("component_version_admin");
    
    Context context = TCContext.getInitial(ApplicationServer.FORUMS_HOST_URL);
	Forums forums = null;
	try {
		ForumsHome forumsHome = (ForumsHome) context.lookup(ForumsHome.EJB_REF_NAME);
		forums = forumsHome.create();
	} catch (Exception e) { 
    	debug.addMsg("user admin", "error initializing Forums EJB");
    }
%>


<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>TopCoder Software</title>

<link rel="stylesheet" type="text/css" href="/includes/tcs_style.css" />
<jsp:include page="/includes/header-files.jsp" />
<script language="JavaScript" type="text/javascript" src="/scripts/javascriptAdmin.js">
</script>
<script language="JavaScript" type="text/javascript" >
    	function hide()
		{
                        //alert('here');
			//if (Exists("date_row1"))
			//	document.all("date_row1").style.display = "none";
		}

</script>
<%!
public Object[] parseDocumentNameAndType(String componentName, String fileName, Map docTypesMap) {

    // Set the default values for document name and document type
    String name = "Other (misc)";
    long lngType = 6;

    // Parse the document name and document type from file name
    if (fileName.startsWith(componentName + "_")) {

        // Strip out the component name from the file name
        fileName = fileName.substring(componentName.length() + 1);

        // Strip out the filename extension
        fileName = fileName.substring(0, fileName.lastIndexOf('.'));

        // Iterate over each existing document type and check if the file corresponds to that type
        String docTypeName;
        Long docTypeId;
        Iterator iterator = docTypesMap.keySet().iterator();
        while (iterator.hasNext()) {
            docTypeId = (Long) iterator.next();
            docTypeName = ((String) docTypesMap.get(docTypeId)).replace(' ', '_');

            // If the file corresponds to current document type then try to parse the name of the
            // document which is expected to follow after the type
            if (fileName.startsWith(docTypeName)) {
                lngType = docTypeId.longValue();
                name = docTypeName;

                // Strip out the document type
                fileName = fileName.substring(docTypeName.length()).trim();

                // If something has left then that's the document name
                if (fileName.length() > 0) {
                    name += " - " + fileName.trim();
                }
            }
        }
    } else if (fileName.equalsIgnoreCase("javadocs.jar")) {
        lngType = Document.JAVADOCS;
        name = "Javadocs";
    } else if (fileName.equalsIgnoreCase("msdndocs.zip")) {
        lngType = Document.JAVADOCS;
        name = "XML Documentation";
    }

    name = name.replace('_', ' ').trim();

    return new Object[] {name, new Long(lngType)};
}
%>

<%
Object objTechTypes = CONTEXT.lookup(CatalogHome.EJB_REF_NAME);
CatalogHome home = (CatalogHome) PortableRemoteObject.narrow(objTechTypes, CatalogHome.class);
Catalog catalog = home.create();

Collection colTechnology = catalog.getTechnologies();

Object objComponentMgr = CONTEXT.lookup(ComponentManagerHome.EJB_REF_NAME);
ComponentManagerHome component_manager_home = (ComponentManagerHome) PortableRemoteObject.narrow(objComponentMgr, ComponentManagerHome.class);
ComponentManager componentManager = null;

// Obtain a reference to Doc Types CMP EJB
Object objDocTypes = CONTEXT.lookup(LocalDDEDocTypesHome.EJB_REF_NAME);
LocalDDEDocTypesHome docTypesHome = (LocalDDEDocTypesHome) PortableRemoteObject.narrow(objDocTypes, LocalDDEDocTypesHome.class);

// Get the list of existing active document types and build a hash table to perform look-up
LocalDDEDocTypes docType;
Collection docTypes = docTypesHome.findAllActive();
Map docTypesMap = new TreeMap();
Iterator iterator = docTypes.iterator();
while (iterator.hasNext()) {
    docType = (LocalDDEDocTypes) iterator.next();
    docTypesMap.put(docType.getPrimaryKey(), docType.getDescription());
}

// Free unused resources
docTypes.clear();
docTypes = null;
iterator = null;
docType = null;

// A general info for current component
ComponentInfo component = null;

long lngComponent = 0;
long lngVersion = 0;

String strError = "";
String strMessage = "";

if (request.getMethod().equals("POST")) {
    try {
        // File Upload - Config manager
        FileUpload fu = new LocalFileUpload(namespace);
        FileUploadResult upload = fu.uploadFiles(request);
        lngComponent = Long.parseLong(upload.getParameter("comp"));
    	lngVersion = Long.parseLong(upload.getParameter("ver"));
        UploadedFile[] fileUploads = upload.getAllUploadedFiles();
        action = upload.getParameter("a");

        // File Upload - parse request
        strMessage += "File was uploaded.";
        if (lngVersion > 0) {
        	componentManager = component_manager_home.create(lngComponent, lngVersion);
    	} else {
        	componentManager = component_manager_home.create(lngComponent);
   	 	}

        // String rootDir = upload.getDir();
        String rootDir = ((LocalFileUpload)fu).getDir();
        if (!rootDir.endsWith("/")) {
            rootDir += "/";
        }
        String dir = "" + lngComponent + "/";
        dir += componentManager.getVersionInfo().getVersionId() + "/";
        //Create the directories if they do not already exist.

        if (action != null) {
            // Get the component details
            //component = componentManager.getComponentInfo();

            // Documents
            if (action.equals("Add Document")) {

                if (fileUploads.length > 0) {
                    UploadedFile uf = fileUploads[0];
                    String fileName = uf.getRemoteFileName();

                    component = componentManager.getComponentInfo();
                    String componentName = component.getName().replace(' ', '_');

                    // Check if that's an archive with bundled documentation files
                    if (fileName.equals(componentName.toLowerCase() + "_docs.jar")
                            || fileName.equals(componentName.toLowerCase() + "_docs.zip")) {

                        // Generate the name for temporary directory and create that directory
                        TCSFile tmpDir = new TCSFile(rootDir, "tmp" + System.currentTimeMillis());
                        while (tmpDir.exists()) {
                            tmpDir = new TCSFile(rootDir, "tmp" + System.currentTimeMillis());
                        }
                        tmpDir.mkdirs();

                        // Upload file to temporary directory
                        File url = new File(tmpDir, uf.getRemoteFileName());
                        FileOutputStream fos = new FileOutputStream(url);

                        InputStream is = uf.getInputStream();
                        int b = is.read();
                        while (b != -1) {
                            fos.write(b);
                            b = is.read();
                        }
                        fos.close();
                        is.close();

                        // Extract the documents from archive
                        log.debug("Executing: jar -xf " + url.getAbsolutePath() + " in " + tmpDir.getAbsolutePath());
                        Process process = Runtime.getRuntime().exec("jar -xf " + url.getAbsolutePath(), new String[0], tmpDir);
                        process.waitFor();

                        // Delete the uploaded documentation archive
                        url.delete();

                        File componentDir = new File(rootDir, dir);
                        componentDir.mkdirs();

                        // Register documents to component and move the files to appropriate directory
                        File[] docFiles = tmpDir.listFiles();
                        for (int i = 0; i < docFiles.length; i++) {
                            if (docFiles[i].isDirectory()) {
                                continue;
                            }

                            File targetDocFile = new File(componentDir, docFiles[i].getName());

                            // Check if the target file already exists, if so then report an error
                            if (targetDocFile.exists()) {
                                strError += "The file " + targetDocFile.getName() + " already exists<BR>";
                            } else {
                                // Otherwise move the documentation file from temporary directory to component directory
                                // and register document to component
                                docFiles[i].renameTo(targetDocFile);

                                // Parse the name and type of the document
                                Object[] nameType = parseDocumentNameAndType(componentName,
                                                                             targetDocFile.getName(),
                                                                             docTypesMap);

                                // Extract the Javadocs
                                if (((Long) nameType[1]).longValue() == Document.JAVADOCS) {
                                    File jDocDir = new File(targetDocFile.getParent(), "javadoc");
                                    jDocDir.mkdir();
                                    log.debug("Executing: jar -xf " + targetDocFile.getAbsolutePath() + " in " + jDocDir.getAbsolutePath());
                                    Runtime.getRuntime().exec("jar -xf " + targetDocFile.getAbsolutePath(), new String[0], jDocDir).waitFor();
                                }

                                // Create document and register it to component
                                Document document = new Document((String) nameType[0],
                                                                 dir + docFiles[i].getName(),
                                                                 ((Long) nameType[1]).longValue());
                                componentManager.addDocument(document);
                            }
                        }

                        // Delete temporary directory
                        tmpDir.deleteTree();

                    } else {
                        // Get the name and type of the document
                        Object[] nameType = parseDocumentNameAndType(componentName, fileName, docTypesMap);
                        String name = (String) nameType[0];
                        long lngType = ((Long) nameType[1]).longValue();

                        // Upload file
                        String url = dir + uf.getRemoteFileName();;
                        InputStream is = uf.getInputStream();
                        new File(rootDir + dir).mkdirs();
                        File f = new File(rootDir + url);
                        if (!f.exists()) {
                            FileOutputStream fos = new FileOutputStream(f);
                            int b = is.read();
                            while (b != -1) {
                                fos.write(b);
                                b = is.read();
                            }
                            fos.close();
                            is.close();

                            // Extract the Javadocs
                            if (lngType == Document.JAVADOCS) {
                                File jDocDir = new File(f.getParent(), "javadoc");
                                jDocDir.mkdir();
                                log.debug("Executing: jar -xf " + f.getAbsolutePath() + " in " + jDocDir.getAbsolutePath());
                                Runtime.getRuntime().exec("jar -xf " + f.getAbsolutePath(), new String[0], jDocDir);
//                                sun.tools.jar.Main.main(new String[]{"-xf",f.getAbsolutePath(),"-C",jDocDir.getAbsolutePath()});
                            }

                            // Add document to component
                            com.topcoder.dde.catalog.Document document = new com.topcoder.dde.catalog.Document(name, url, lngType);
                            componentManager.addDocument(document);
                        } else {
                            strError += "File: " + f.getName() + " already exists<BR>";
                        }
                    }
                }
            }

            if (action.equals("Upload Document")) {
                com.topcoder.dde.catalog.Document document = catalog.getDocument(Long.parseLong(upload.getParameter("lngDocument")));
                if (fileUploads.length > 0) {
                    try {
                        new File(rootDir + document.getURL()).delete();
                    } catch (Exception e) {}
                    UploadedFile uf = fileUploads[0];
                    String url = dir + uf.getRemoteFileName();
                    InputStream is = uf.getInputStream();
                    new File(rootDir + dir).mkdirs();
                    File f = new File(rootDir + url);
                    if (!f.exists()) {
                        FileOutputStream fos = new FileOutputStream(f);
                        int b = is.read();
                        while (b != -1) {
                            fos.write(b);
                            b = is.read();
                        }
                        fos.close();
                        is.close();
                        if (document.getType() == Document.JAVADOCS) {
                            File jDocDir = new File(f.getParent(),"javadoc");
                            jDocDir.mkdir();
                            log.debug("Executing: jar -xf " + f.getAbsolutePath() + " in " + jDocDir.getAbsolutePath());
                            Runtime.getRuntime().exec("jar -xf "+f.getAbsolutePath(), new String[0], jDocDir);
//                                sun.tools.jar.Main.main(new String[]{"-xf",f.getAbsolutePath(),"-C",jDocDir.getAbsolutePath()});
                        }
                        document.setURL(url);
                        componentManager.updateDocument(document);
                    } else {
                        strError += "File: " + f.getName() + " already exists.<BR>";
                    }
                }
            }

            // Downloads
            if (action.equals("Add Download")) {
                String desc = upload.getParameter("txtDownloadDescription");
                if (desc.trim().length() == 0) {
                    strError += "Name cannot be blank.<BR>";
                } else {
                    if (fileUploads.length > 0) {
                        UploadedFile uf = fileUploads[0];
                        String url = dir + uf.getRemoteFileName();
                        InputStream is = uf.getInputStream();
                        new File(rootDir + dir).mkdirs();
                        File f = new File(rootDir + url);
                        if (!f.exists()) {
                            FileOutputStream fos = new FileOutputStream(f);
                            int b = is.read();
                            while (b != -1) {
                                fos.write(b);
                                b = is.read();
                            }
                            fos.close();
                            is.close();
                            Download download = new Download(desc, url);
                            componentManager.addDownload(download);
                        } else {
                            strError += "File: " + f.getName() + " already exists.<BR> ";
                        }
                    }
                }
            }

            if (action.equals("Upload Download")) {
                Download download = catalog.getDownload(Long.parseLong(upload.getParameter("lngDownload")));
                if (fileUploads.length > 0) {
                    try {
                        new File(rootDir + download.getURL()).delete();
                    } catch (Exception e) {}
                    UploadedFile uf = fileUploads[0];
                    String url = dir + uf.getRemoteFileName();
                    InputStream is = uf.getInputStream();
                    new File(rootDir + dir).mkdirs();
                    File f = new File(rootDir + url);
                    if (!f.exists()) {
                        FileOutputStream fos = new FileOutputStream(f);
                        int b = is.read();
                        while (b != -1) {
                            fos.write(b);
                            b = is.read();
                        }
                        fos.close();
                        is.close();
                        download.setURL(url);
                        componentManager.updateDownload(download);
                    } else {
                        strError += "File: " + f.getName() + " already exists.<BR>";
                    }
                }
            }

            // Handle "Update Document" requests
            if (action.equalsIgnoreCase("Update Document")) {
                String name = upload.getParameter("txtDocumentName");
                String type = upload.getParameter("selDocType");
                String strDocId = upload.getParameter("lngDocument");

                long lngType = -1;
                try {
                    lngType = Long.parseLong(type);
                    if (!docTypesMap.containsKey(new Long(lngType))) {
                        strError += "Non-existing document type ID - " + type + "<BR>";
                        lngType = -1;
                    }
                } catch (Exception ignore) {
                    strError += "Invalid document type ID - " + type + "<BR>";
                }

                long lngDocId = -1;
                try {
                    lngDocId = Long.parseLong(strDocId);
                } catch (Exception ignore) {
                    strError += "Invalid document ID - " + strDocId + "<BR>";
                }

                if (name == null || name.trim().length() == 0) {
                    strError += "Name cannot be blank.<BR>";
                } else if (lngType != -1 && lngDocId != -1) {
                    com.topcoder.dde.catalog.Document document = catalog.getDocument(lngDocId);

                    // Update the document only if the document name or type have been really modified
                    if (document.getType() != lngType || !name.equals(document.getName())) {
                        document.setType(lngType);
                        document.setName(name);
                        componentManager.updateDocument(document);
                    }
                }
            }
        }
    } catch (ConfigManagerException e) {
        strError += "ConfigManager exception occurred: " + e.getMessage();
    } catch (InvalidContentTypeException e) {
    	// this error will be thrown whenever a file is not uploaded with the following message:
    	// 	 "contentType application/x-www-form-urlencoded is not multipart/form-data"
    	//strError += "InvalidContentTypeException exception occurred: " + e.getMessage();
    	lngComponent = Long.parseLong(request.getParameter("comp"));
	    lngVersion = Long.parseLong(request.getParameter("ver"));
	    if (lngVersion > 0) {
	        componentManager = component_manager_home.create(lngComponent, lngVersion);
	    } else if (lngComponent > 0) {
	        componentManager = component_manager_home.create(lngComponent);
	    }
    }
}

if (componentManager == null) {
    debug.addMsg("component version admin", "Component = " + request.getParameter("comp"));
    debug.addMsg("component version admin", "Version = " + request.getParameter("ver"));
    try {
        lngComponent = Long.parseLong(request.getParameter("comp"));
    } catch (Exception e) {
        response.sendRedirect("catalog.jsp");
        return;
    }

    try {
        lngVersion = Long.parseLong(request.getParameter("ver"));
    } catch (Exception e) {
        debug.addMsg("component version admin", "no version found - must be in create mode");
    }

    if (lngVersion > 0) {
        componentManager = component_manager_home.create(lngComponent, lngVersion);
    } else {
        componentManager = component_manager_home.create(lngComponent);
    }
}

ComponentVersionInfo ver = null;
VersionDateInfo verDateInfo = null;
Hashtable technologies = new Hashtable();
String postingDate = "";
long postingStatusId = 0L;
long levelId = 0L;
String initialSubmissionDate = "";
String finalSubmissionDate = "";
String winnerAnnouncedDate = "";
String estimatedDevDate = "";
String screeningCompleteDate = "";
String reviewCompleteDate = "";
String aggregationCompleteDate = "";
String phaseCompleteDate = "";
String productionDate = "";
Double phasePrice = new Double(0d);
String aggregationCompleteDateComment = "";
String phaseCompleteDateComment= "";
String productionDateComment= "";
String reviewCompleteDateComment = "";
String winnerAnnouncedDateComment= "";
String initialSubmissionDateComment= "";
String screeningCompleteDateComment = "";
String finalSubmissionDateComment = "";
//GT Added this to allow for public forums;
String publicForum = "";
if (lngVersion > 0) {
    ver = componentManager.getVersionInfo();

    try{
        verDateInfo = componentManager.getVersionDateInfo(ver.getVersionId(), ver.getPhase());
        postingDate = dateFormat.format(verDateInfo.getPostingDate());
        initialSubmissionDate = dateFormat.format(verDateInfo.getInitialSubmissionDate());
        finalSubmissionDate = dateFormat.format(verDateInfo.getFinalSubmissionDate());

        postingStatusId =  verDateInfo.getStatusId();
        levelId = verDateInfo.getLevelId();

	if(verDateInfo.getEstimatedDevDate() != null ){
           estimatedDevDate = dateFormat.format(verDateInfo.getEstimatedDevDate());
        }
	if(verDateInfo.getScreeningCompleteDate() != null){
           screeningCompleteDate = dateFormat.format(verDateInfo.getScreeningCompleteDate());
        }
	if(verDateInfo.getReviewCompleteDate() != null){
           reviewCompleteDate = dateFormat.format(verDateInfo.getReviewCompleteDate());
        }
	if(verDateInfo.getAggregationCompleteDate() != null){
           aggregationCompleteDate = dateFormat.format(verDateInfo.getAggregationCompleteDate());
           winnerAnnouncedDate = dateFormat.format(verDateInfo.getAggregationCompleteDate());
        }
	if(verDateInfo.getPhaseCompleteDate() != null){
           phaseCompleteDate = dateFormat.format(verDateInfo.getPhaseCompleteDate());
        }
	if(verDateInfo.getProductionDate() != null){
           productionDate = dateFormat.format(verDateInfo.getProductionDate());
        }


        phasePrice = verDateInfo.getPrice();

        aggregationCompleteDateComment = verDateInfo.getAggregationCompleteDateComment();
        phaseCompleteDateComment= verDateInfo.getPhaseCompleteDateComment();
        productionDateComment= verDateInfo.getProductionDateComment();
        reviewCompleteDateComment = verDateInfo.getReviewCompleteDateComment();
        winnerAnnouncedDateComment= null; //verDateInfo.getWinnerAnnouncedDateComment();
        initialSubmissionDateComment= verDateInfo.getInitialSubmissionDateComment();
        screeningCompleteDateComment    = verDateInfo.getScreeningCompleteDateComment();
        finalSubmissionDateComment = verDateInfo.getFinalSubmissionDateComment();
    }
    catch(CatalogException ce)
    {
        //version date info has not been added yet
        verDateInfo = new VersionDateInfo();
    }
}

Collection colVerTechs = componentManager.getTechnologies();

Iterator iterVerTechs = colVerTechs.iterator();
while (iterVerTechs.hasNext()) {
    Technology tech = (Technology)iterVerTechs.next();
    technologies.put("" + tech.getId(), "hit");
}

if (request.getParameter("add.x") != null) {
    // Add dependency
    String strDependency = request.getParameter("selMasterDependency");
    debug.addMsg("component version admin", "adding dependency " + strDependency);
    if (strDependency != null) {
        try {
            componentManager.addDependency(Long.parseLong(strDependency));
        } catch (Exception e) {
            debug.addMsg("component version admin", "error: " + e.getMessage());
        }
    }
}

if (request.getParameter("remove.x") != null) {
    // Remove dependency
    String strDependency = request.getParameter("selVersionDependency");
    debug.addMsg("component version admin", "removing dependency " + strDependency);
    try {
    componentManager.removeDependency(Long.parseLong(strDependency));
    } catch (Exception e) {
        debug.addMsg("component version admin", "error: " + e.getMessage());
    }
}

if (action != null) {
    //strMessage += "action is " + action + "<BR>";
    if (action.equalsIgnoreCase("Save")) {
        // Get fields and set component info

        String versionLabel =       request.getParameter("txtVersionLabel");
        String comments =  request.getParameter("taComments");
        postingDate =   "5/5/1976"; //request.getParameter("txtPostingDate");

        //GT Added this to allow for public forums;
        publicForum = request.getParameter("public_forum");
        //if (publicForum != null && publicForum.equalsIgnoreCase("on")) {
        if (publicForum != null && publicForum.equalsIgnoreCase("1")) {
            ver.setPublicForum(true);
        } else {
            ver.setPublicForum(false);
        }
  	    initialSubmissionDate = "01/01/2000";
        finalSubmissionDate = initialSubmissionDate;
        estimatedDevDate = initialSubmissionDate;
        screeningCompleteDate = initialSubmissionDate;
        reviewCompleteDate = initialSubmissionDate;
        winnerAnnouncedDate = initialSubmissionDate;
        aggregationCompleteDate = initialSubmissionDate;
        phaseCompleteDate = initialSubmissionDate;

		if(request.getParameter("txtProductionDate")!=""){
	        productionDate= request.getParameter("txtProductionDate");
        }
        else{
            productionDate = null;
        }

        String phaseVersionPrice =   request.getParameter("txtPhaseVersionPrice");
        phasePrice = new Double((phaseVersionPrice == "") ? "0" : phaseVersionPrice);
        String strPostingStatus =   request.getParameter("selPostingStatus");
        postingStatusId = Long.parseLong(request.getParameter("selPostingStatus"));
        levelId = Long.parseLong(request.getParameter("selLevelId"));

        aggregationCompleteDateComment = null;
        phaseCompleteDateComment = null;
        productionDateComment = request.getParameter("txtProductionDateComment");
        reviewCompleteDateComment = null;
        winnerAnnouncedDateComment = null;
        initialSubmissionDateComment = null;
        screeningCompleteDateComment = null;
        finalSubmissionDateComment = null;

        String phase =   request.getParameter("selPhase");
        ver.setVersionLabel(versionLabel);
        ver.setComments(comments);
        ver.setPrice(phasePrice.doubleValue());
//        ver.setSuspended(request.getParameter("suspended") != null);
	    ver.setSuspended(false);

		com.topcoder.dde.catalog.ForumCategory activeSpec = null;
        try {
            activeSpec = componentManager.getForumCategory();
            if (activeSpec != null) {
            	forums.updateComponentVersion(activeSpec.getId(), versionLabel);
            }
        } catch (CatalogException ce) {
            debug.addMsg("component version admin", "catalog exception occurred");
        }

        //StringTokenizer stDate = new StringTokenizer(phaseDate, "/");
        String month = "5";
        String date = "4";
        String year = "1976";
        GregorianCalendar gcDate = new GregorianCalendar(Integer.parseInt(year), Integer.parseInt(month)-1, Integer.parseInt(date));
        ver.setPhaseDate(gcDate.getTime());
        ver.setPhase(Long.parseLong(phase));

        try {
            if (colTechnology != null) {
                Iterator iter = colTechnology.iterator();
                while (iter.hasNext()) {
                    Object obj = iter.next();
                    if (obj instanceof Technology) {
                        String tmp = request.getParameter("tech_" + ((Technology)obj).getId());
                        if (tmp != null && technologies.get("" + ((Technology)obj).getId()) == null) {
                            componentManager.addTechnology(((Technology)obj).getId());
                        } else {
                            if (tmp == null && technologies.get("" + ((Technology)obj).getId()) != null) {
                                componentManager.removeTechnology(((Technology)obj).getId());
                            }
                        }
                    }
                }
            }
            log.debug("Public: " + ver.getPublicForum());            
            componentManager.updateVersionInfo(ver, tcSubject, levelId);
            if(verDateInfo != null && (ver.getPhase() == ComponentVersionInfo.DEVELOPMENT || ver.getPhase() == ComponentVersionInfo.SPECIFICATION || ver.getPhase() == ComponentVersionInfo.COMPLETED))
            {

                verDateInfo.setComponentVersionId(ver.getVersionId());
                verDateInfo.setPhaseId(ver.getPhase());
                verDateInfo.setPostingDate(dateFormat.parse(postingDate));
                verDateInfo.setInitialSubmissionDate(dateFormat.parse(initialSubmissionDate));
                verDateInfo.setFinalSubmissionDate(dateFormat.parse(finalSubmissionDate));

                if(estimatedDevDate != null && !estimatedDevDate.equals("")){
                   verDateInfo.setEstimatedDevDate(dateFormat.parse(estimatedDevDate));
                }

                verDateInfo.setPrice(phasePrice);
                verDateInfo.setStatusId(Long.parseLong(strPostingStatus));
                verDateInfo.setLevelId(levelId);

                verDateInfo.setAggregationCompleteDateComment(aggregationCompleteDateComment);
                verDateInfo.setPhaseCompleteDateComment(phaseCompleteDateComment);
                verDateInfo.setProductionDateComment(productionDateComment);
                verDateInfo.setReviewCompleteDateComment(reviewCompleteDateComment);
                verDateInfo.setWinnerAnnouncedDateComment(null);
                verDateInfo.setInitialSubmissionDateComment(initialSubmissionDateComment);
                verDateInfo.setScreeningCompleteDateComment(screeningCompleteDateComment);
                verDateInfo.setFinalSubmissionDateComment(finalSubmissionDateComment);

                if (estimatedDevDate != null && !estimatedDevDate.equals("")) {
                    verDateInfo.setEstimatedDevDate(dateFormat.parse(estimatedDevDate));
                } else {
                    verDateInfo.setEstimatedDevDate(null);
                }

                if(screeningCompleteDate != null && !screeningCompleteDate.equals("")) {
                    verDateInfo.setScreeningCompleteDate(dateFormat.parse(screeningCompleteDate));
                } else {
                    verDateInfo.setScreeningCompleteDate(null);
                }

                if (phaseCompleteDate != null && !phaseCompleteDate.equals("")) {
                    verDateInfo.setPhaseCompleteDate(dateFormat.parse(phaseCompleteDate));
                } else {
                    verDateInfo.setPhaseCompleteDate(null);
                }

                if (productionDate != null && !productionDate.equals("")) {
                    verDateInfo.setProductionDate(dateFormat.parse(productionDate));
                } else {
                    verDateInfo.setProductionDate(null);
                }

                if (reviewCompleteDate != null && !reviewCompleteDate.equals("")) {
                    verDateInfo.setReviewCompleteDate(dateFormat.parse(reviewCompleteDate));
                } else {
                    verDateInfo.setReviewCompleteDate(null);
                }

                if (aggregationCompleteDate != null && !aggregationCompleteDate.equals("")) {
                    verDateInfo.setAggregationCompleteDate(dateFormat.parse(aggregationCompleteDate));
                    verDateInfo.setWinnerAnnouncedDate(dateFormat.parse(aggregationCompleteDate));
                } else {
                    verDateInfo.setAggregationCompleteDate(null);
                }

                componentManager.updateVersionDatesInfo(verDateInfo);
            }

            strMessage += "Version info was saved.";
            colVerTechs = componentManager.getTechnologies();
            iterVerTechs = colVerTechs.iterator();
            technologies.clear();

            while (iterVerTechs.hasNext()) {
                Technology tech = (Technology)iterVerTechs.next();
                technologies.put("" + tech.getId(), "hit");
            }
        } catch (Exception e) {
            log.error("An error occurred while updating version info", e);
			strError += "An error occurred while updating version info: " + e.getMessage();
            ver = componentManager.getVersionInfo();
        }
    }
    if (action.equalsIgnoreCase("Create") && tcUser != null) {

        // Get fields and request version info
        String versionLabel     =  "";//request.getParameter("txtVersionLabel");
        String comments         =  "";//request.getParameter("taComments");

        try {
            debug.addMsg("component version admin", "creating request");
            debug.addMsg("component version admin", "ComponentVersionRequest versionRequest = new ComponentVersionRequest(" + comments + ", " + versionLabel + ", " + tcUser.getId() + ");");
            ComponentVersionRequest versionRequest = new ComponentVersionRequest(comments, versionLabel, tcUser.getId());
            debug.addMsg("component version admin", "created request");

            if (colTechnology != null) {
                Iterator iter = colTechnology.iterator();
                while (iter.hasNext()) {
                    Object obj = iter.next();
                    if (obj instanceof Technology) {
                        String tmp = request.getParameter("tech_" + ((Technology)obj).getId());
                        if (tmp != null) {
                            versionRequest.addTechnology(((Technology)obj).getId());
                        }
                    }
                }
            }

            debug.addMsg("component version admin", "creating new version");
            lngVersion = componentManager.createNewVersion(versionRequest);


            debug.addMsg("component version admin", "created new version");
            componentManager = component_manager_home.create(lngComponent, lngVersion);
            debug.addMsg("component version admin", "retrieved new version");
            ver = componentManager.getVersionInfo();
            strMessage += "Version request was created.";


            if(verDateInfo == null && (ver.getPhase() == ComponentVersionInfo.DEVELOPMENT || ver.getPhase() == ComponentVersionInfo.SPECIFICATION || ver.getPhase() == ComponentVersionInfo.COMPLETED))
            {
        		java.util.Date dtEstimatedDevDate = null;
        		java.util.Date dtScreeningCompleteDate = null;
                java.util.Date dtPhaseCompleteDate = null;
                java.util.Date dtProductionDate = null;
        		java.util.Date dtReviewCompleteDate = null;
        		java.util.Date dtAggregationCompleteDate = null;
        	    if(estimatedDevDate != null ){
                    dtEstimatedDevDate =  dateFormat.parse(estimatedDevDate);
        		}
        	    if(screeningCompleteDate != null ){
                    dtScreeningCompleteDate =  dateFormat.parse(screeningCompleteDate);
        		}
        	    if(phaseCompleteDate != null ){
                    dtPhaseCompleteDate =  dateFormat.parse(phaseCompleteDate);
        		}
        	    if(reviewCompleteDate != null ){
                    dtReviewCompleteDate =  dateFormat.parse(reviewCompleteDate);
        		}
        	    if(aggregationCompleteDate != null ){
                    dtAggregationCompleteDate =  dateFormat.parse(aggregationCompleteDate);
        		}
                if(productionDate != null ){
                    dtProductionDate =  dateFormat.parse(productionDate);
        		}
                verDateInfo = new VersionDateInfo(ver.getVersionId(),
                                                  ver.getPhase(),
                                                  dateFormat.parse(postingDate),
                                                  dateFormat.parse(initialSubmissionDate),
                                                  dateFormat.parse(finalSubmissionDate),
                                                  dateFormat.parse(winnerAnnouncedDate),
                                                  dtEstimatedDevDate ,
                                                  request.getParameter("txtPhaseVersionPrice") == "" ? null : 
                                                  new Double(request.getParameter("txtPhaseVersionPrice")),
                                                  Long.parseLong(request.getParameter("selPostingStatus")),
                                                  Long.parseLong(request.getParameter("selLevelId")),
                                                  dtScreeningCompleteDate, dtPhaseCompleteDate,
                                                  dtAggregationCompleteDate,dtReviewCompleteDate,
                                                  null,
                                                  null,
                                                  null,
                                                  null,
                                                  null,
                                                  null,
                                                  null,
                                                  dtProductionDate,
                                                  request.getParameter("txtProductionDateComment")
                                                );

                componentManager.updateVersionDatesInfo(verDateInfo);
            }

            colVerTechs = componentManager.getTechnologies();
            iterVerTechs = colVerTechs.iterator();
            technologies.clear();



            while (iterVerTechs.hasNext()) {
                Technology tech = (Technology)iterVerTechs.next();
                technologies.put("" + tech.getId(), "hit");
            }
        } catch (Exception e) {
            debug.addMsg("component version admin", "error: " + e.getMessage());
            response.sendRedirect("component_admin.jsp?comp=" + lngComponent);
            return;
        }
    }
    if (action.equals(">>")) {
        // Add dependency
        String strDependency = request.getParameter("selMasterDependency");
        debug.addMsg("component version admin", "adding dependency " + strDependency);
        if (strDependency != null) {
            try {
                componentManager.addDependency(Long.parseLong(strDependency));
            } catch (Exception e) {
                debug.addMsg("component version admin", "error: " + e.getMessage());
            }
        }
    }

    if (action.equals("<<")) {
        // Remove dependency
        String strDependency = request.getParameter("selVersionDependency");
        debug.addMsg("component version admin", "removing dependency " + strDependency);
        try {
        componentManager.removeDependency(Long.parseLong(strDependency));
        } catch (Exception e) {
            debug.addMsg("component version admin", "error: " + e.getMessage());
        }
    }

    // Example
    if (action.equals("Add Example")) {
        String desc = request.getParameter("txtExampleDescription");
        String url = request.getParameter("txtExampleURL");
        Example example = new Example(desc, url);
        componentManager.addExample(example);
        //response.sendRedirect("component_version_admin.jsp?comp=" + lngComponent + "ver=" + lngVersion);
    }

    if (action.equals("DeleteExample")) {
        String strExample = request.getParameter("example");
        long exampleId = Long.parseLong(strExample);
        componentManager.removeExample(exampleId);
        //response.sendRedirect("component_version_admin.jsp?comp=" + lngComponent + "ver=" + lngVersion);
    }

    // Assign a user a role that has permissions to download this component
    if (action.equals("Download Role")) {
        String txtUsername = request.getParameter("txtHandle");
        DownloadPermission permission = new DownloadPermission(lngComponent);
        long lngRole = 0;
        try {
            debug.addMsg("component version admin", "getting principal for '" + txtUsername + "'");
            com.topcoder.security.UserPrincipal selectedPrincipal = PRINCIPAL_MANAGER.getUser(txtUsername);
            debug.addMsg("component version admin", "got principal for '" + txtUsername + "'");
            RolePrincipal roles[] = (RolePrincipal[])PRINCIPAL_MANAGER.getRoles(null).toArray(new RolePrincipal[0]);
            for (int i=0; i < roles.length && lngRole == 0; i++) {
                if (roles[i].getName().equals("DDEComponentDownload " + lngComponent)) {
                    lngRole = roles[i].getId();
                }
            }
            debug.addMsg("component version admin", "assigning role " + lngRole);
            PRINCIPAL_MANAGER.assignRole(selectedPrincipal, PRINCIPAL_MANAGER.getRole(lngRole), tcSubject);
            debug.addMsg("component version admin", "assigned role " + lngRole);
            strMessage += "Download role was assigned to " + txtUsername;
        } catch (RemoteException re) {
            strError += "RemoteException occurred while assigning role: " + re.getMessage();
        } catch (GeneralSecurityException gse) {
            strError += "GeneralSecurityException occurred while assigning role: " + gse.getMessage();
        } catch (Exception e) {
            strError += "Principal user could not be found.<BR>";
        }
    }

    // Assign a user a role that has permissions to participate in developer forum for this component
    if (action.equals("Developer Forum User")) {
        String txtUsername = request.getParameter("txtHandle");
        com.topcoder.dde.catalog.ForumCategory activeSpec = null;
        try {
            activeSpec = componentManager.getForumCategory();
        } catch (CatalogException ce) {
        }

		if (activeSpec != null) {
			try {
	     		long userID = PRINCIPAL_MANAGER.getUser(txtUsername).getId();
	     		String groupName = "Software_Users_"+activeSpec.getId();
	    		forums.assignRole(userID, groupName);
	    		strMessage += txtUsername + " successfully added as developer forum user.";
	    	} catch (Exception e) {
	    		strError += "Error occurred while assigning forums role: " + e.getMessage();
	    	}
    	} else {
    		strMessage += "No developer forum found for this component version.";
    	}
    }

    // Assign a user a role that has permissions to moderate developer forum for this component
    if (action.equals("Developer Forum Moderator")) {
        String txtUsername = request.getParameter("txtHandle");
        com.topcoder.dde.catalog.ForumCategory activeSpec = null;
        try {
            activeSpec = componentManager.getForumCategory();
        } catch (CatalogException ce) {
        }

		if (activeSpec != null) {
	       	try {
	     		long userID = PRINCIPAL_MANAGER.getUser(txtUsername).getId();
	     		String groupName = "Software_Moderators_"+activeSpec.getId();
	    		forums.assignRole(userID, groupName);
	    		strMessage += txtUsername + " successfully added as developer forum moderator.";
	    	} catch (Exception e) {
	    		strError += "Error occurred while assigning forums role: " + e.getMessage();
	    	}
	    } else {
	    	strMessage += "No developer forum found for this component version.";
	    }
    }

    // Review
    if (action.equals("Add Review")) {
        String username = request.getParameter("txtReviewUsername");
        String rating = request.getParameter("txtReviewRating");
        String comments = request.getParameter("txtReviewComments");
        // Find username
        try {
            User user = USER_MANAGER.getUser(username);
            Review review = new Review(user, Integer.parseInt(rating), comments);
            componentManager.addReview(review);
            strMessage += "Review was added.";
            //response.sendRedirect("component_version_admin.jsp?comp=" + lngComponent + "ver=" + lngVersion);
        } catch (com.topcoder.dde.user.NoSuchUserException nsue) {
            strError = "User '" + username + "' was not found.";
        } catch (NumberFormatException nfe) {
            strError = "Rating must be an integer.";
        }
    }

    if (action.equals("DeleteReview")) {
        String strReview = request.getParameter("review");
        long reviewId = Long.parseLong(strReview);
        componentManager.removeReview(reviewId);
        //response.sendRedirect("component_version_admin.jsp?comp=" + lngComponent + "ver=" + lngVersion);
    }

    if (action.equals("Watch Developer Forums")) {
        String strUsername = request.getParameter("txtTCHandle");
        if (strUsername == null || strUsername.trim().length() == 0) {
            strError = "User handle must not be empty.";
        } else {
            try {
                log.debug("Locating the user for handle '" + strUsername + "' ...");
                long userID = PRINCIPAL_MANAGER.getUser(strUsername).getId();
		long forumCategoryId = componentManager.getForumCategory().getId();
		boolean canReadCategory = forums.canReadCategory(userID, forumCategoryId);
		if (!canReadCategory) {
			strError = "User " + strUsername + " must have permission to read this component's forums before this watch can be assigned.";
		} else {
	        	// Assign watch
	                forums.createCategoryWatch(userID, forumCategoryId);
	                log.info("Assigning watch on category " + forumCategoryId + " to user " + userID);
			strMessage += "User " + strUsername + " is now watching developer forums for this component. ";
		}
            } catch (Exception e) {
                log.error("An error occurred while assigning watch to user", e);
                strError = "An error occurred while assigning watch to user : " + e;
            }
        }
    }

    // Team Member Role
    if (action.equals("Add Role")) {
        String strUsername = request.getParameter("txtTeamMemberRoleUsername");
        String strRole = request.getParameter("selRole");
        String strDescription = request.getParameter("txtTeamMemberDescription");
        
        // validate both description and username to be filled.
        if (strUsername == null || strUsername.trim().length() == 0) {
            strError = "Username cannot be blank.";
        }

        if (strDescription == null || strDescription.trim().length() == 0) {
            strError = "Description cannot be blank.";
        }

        if (strError.trim().length() == 0) {
            try {
                User user = USER_MANAGER.getUser(strUsername);
                TeamMemberRole role = new TeamMemberRole(user, catalog.getRole(Long.parseLong(strRole)),
                    strDescription);
                componentManager.addTeamMemberRole(role);
                strMessage += "Role " + strRole + " was assigned.";
                //response.sendRedirect("component_version_admin.jsp?comp=" + lngComponent + "ver=" + lngVersion);
            } catch (com.topcoder.dde.user.NoSuchUserException nsue) {
                strError = "User '" + strUsername + "' was not found.";
            } catch (NumberFormatException nfe) {
                strError = "Rating must be an integer.";
            }
        }
    }

    if (action.equals("DeleteRole")) {
        String strRole = request.getParameter("role");
        long roleId = Long.parseLong(strRole);
        componentManager.removeTeamMemberRole(roleId);
        //response.sendRedirect("component_version_admin.jsp?comp=" + lngComponent + "ver=" + lngVersion);
    }

    String rootDir = "";
    try {
        ConfigManager cm = ConfigManager.getInstance();
        if (!cm.existsNamespace(namespace)) {
            cm.add(namespace, ConfigManager.CONFIG_XML_FORMAT);
        }
        rootDir = (String)cm.getProperty(namespace, "default_dir");
        if (!rootDir.endsWith("/")) {
            rootDir += "/";
        }
    } catch (ConfigManagerException cme) {
        log.error("Error reading configuration", cme);
    }

    if (action.equals("DeleteDocument")) {
        String strDocument = request.getParameter("document");
        long documentId = Long.parseLong(strDocument);
        com.topcoder.dde.catalog.Document document = catalog.getDocument(documentId);
        String deletedDocURL = document.getURL();
        componentManager.removeDocument(documentId);
        try{
            log.debug("Deleting " + deletedDocURL);
            java.io.File file = new java.io.File(rootDir + deletedDocURL);
            file.delete();
        }
        catch(Exception e)
        {
            strError = e.getMessage();
        }
        //response.sendRedirect("component_version_admin.jsp?comp=" + lngComponent + "ver=" + lngVersion);
        File f = new File(rootDir + document.getURL());
        f.delete();
    }

    if (action.equals("DeleteDownload")) {
        String strDownload = request.getParameter("download");
        long downloadId = Long.parseLong(strDownload);
        com.topcoder.dde.catalog.Download download = catalog.getDownload(downloadId);
        componentManager.removeDownload(downloadId);
        //response.sendRedirect("component_version_admin.jsp?comp=" + lngComponent + "ver=" + lngVersion);
        File f = new File(rootDir + download.getURL());
        f.delete();
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
		<td valign="middle" width="1%" class="adminBreadcrumb" nowrap><a class="breadcrumbLinks" href="catalog.jsp">Catalog Admin</a> > <a class="breadcrumbLinks" href="component_admin.jsp?comp=<%=lngComponent%>">Component Admin</a> > <strong>Component Version</strong></td>
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

<% if (ver != null) { %>
<table width="100%" border="0" cellpadding="0" cellspacing="0" align="center">
    <tr><td height="15"><img src="/images/clear.gif" alt="" width="10" height="15" border="0" /></td></tr>
    <tr><td class="normal"><img src="/images/headUserAdmin.gif" alt="User Admin" width="545" height="35" border="0" /></td></tr>
    <tr><td class="adminSubhead"><%= componentManager.getComponentInfo().getName() %> version <%= ver.getVersionLabel() %></td></tr>
</table>

<table width="100%" cellpadding="0" cellspacing="0" align="center" border="0">
    <tr valign="top">
        <td align="center">

            <table width="100%" cellpadding="0" cellspacing="6" align="center" border="0">
                <tr valign="top">
                    <td align="center">
                        <table cellpadding="0" cellspacing="0" border="0">
                                <tr><td width="445" height="29" colspan="2"><img src="../images/adminNameDescHead.gif" alt="Name and Description" width="500" height="29" border="0" /></td></tr>
                        </table>
                        <table width="500" border="0" cellspacing="8" cellpadding="0" align="center" class="admin">
<!-- Version Label Name -->
<form name="frmComponent" action="component_version_admin.jsp" method="POST">
<input type="hidden" name="comp" value="<%= lngComponent %>">
<input type="hidden" name="ver" value="<%= ver.getVersion() %>">
                            <tr valign="middle">
                                <td width="48%">
                                    <img src="../images/clear.gif" alt="" width="5" height="1" border="0"/></td>
                                <td width="1%" class="adminLabel" nowrap>Version Label</td>
                                <td width="1%" class="adminText"><input class="adminSearchForm" type="text" size="8" maxlength="20" name="txtVersionLabel"  value="<%= ver.getVersionLabel() %>"></input></td>
                                <td width="48%"><img src="../images/clear.gif" alt="" width="5" height="1" border="0"/></td>
                            </tr>
<!-- Comments -->
                            <tr valign="top">
                                <td width="48%"><img src="../images/clear.gif" alt="" width="5" height="1" border="0"/></td>
                                <td width="1%" class="adminLabel" nowrap>Comments</td>
                                <td width="1%" class="adminText"><textarea class="compSearchForm" name="taComments" rows="4" cols="35"><%= ver.getComments() %></textarea></td>
                                <td width="48%"><img src="../images/clear.gif" alt="" width="5" height="1" border="0"/></td>
                            </tr>
<!--
<!-- Phase -->
                            <tr valign="middle">
                                <td width="48%"><img src="../images/clear.gif" alt="" width="5" height="1" border="0"/></td>
                                <td width="1%" class="adminLabel" nowrap>Phase</td>
                                <td width="1%" class="adminText">
                                    <select class="adminForm" type="text" name="selPhase" onChange="hide()">
                                        <option value="<%= ComponentVersionInfo.COLLABORATION %>"<%= (ver.getPhase() == ComponentVersionInfo.COLLABORATION ? " SELECTED" : "") %>>Collaboration</option>
                                        <option value="<%= ComponentVersionInfo.COMPLETED %>"<%= (ver.getPhase() == ComponentVersionInfo.COMPLETED ? " SELECTED" : "") %>>Completed</option>
                                        <option value="<%= ComponentVersionInfo.DEVELOPMENT %>"<%= (ver.getPhase() == ComponentVersionInfo.DEVELOPMENT ? " SELECTED" : "") %>>Development</option>
                                        <option value="<%= ComponentVersionInfo.SPECIFICATION %>"<%= (ver.getPhase() == ComponentVersionInfo.SPECIFICATION ? " SELECTED" : "") %>>Specification</option>
                                    </select></td>
                                <td width="48%"><img src="../images/clear.gif" alt="" width="5" height="1" border="0"/></td>
                            </tr>

<!-- Phase Date
                            <tr valign="top">
                                <td width="48%"><img src="../images/clear.gif" alt="" width="5" height="1" border="0"/></td>
                                <td width="1%" class="adminLabel" nowrap>Phase Date</td>
                                <td width="1%" class="adminText"><input class="adminSearchForm" type="text" size="20" maxlength="40" name="txtPhaseDate" value="<%= dateFormat.format(ver.getPhaseDate()) %>"><br/><span class="adminSmall">MM/DD/YYYY</span></td>
                                <td width="48%"><img src="../images/clear.gif" alt="" width="5" height="1" border="0"/></td>
                            </tr>

<!-- Posting Date
                            <tr valign="top" ID="date_row1">
                                <td width="48%"><img src="../images/clear.gif" alt="" width="5" height="1" border="0"/></td>
                                <td width="1%" class="adminLabel" nowrap>Posting Date</td>
                                <td width="1%" class="adminText"><input class="adminSearchForm" type="text" size="20" maxlength="40" name="txtPostingDate" value="<%= postingDate %>"><br/><span class="adminSmall">MM/DD/YYYY</span></td>
                                <td width="48%"><img src="../images/clear.gif" alt="" width="5" height="1" border="0"/></td>
                            </tr>

<!-- Initial Submission Date
                            <tr valign="top" ID="date_row">
                                <td width="48%"><img src="../images/clear.gif" alt="" width="5" height="1" border="0"/></td>
                                <td width="1%" class="adminLabel" nowrap>Initial Submission Date</td>
                                <td width="1%" class="adminText"><input class="adminSearchForm" type="text" size="20" maxlength="40" name="txtInitialSubmissionDate" value="<%= initialSubmissionDate %>"><br/><span class="adminSmall">MM/DD/YYYY</span></td>
                                <td width="48%"><img src="../images/clear.gif" alt="" width="5" height="1" border="0"/></td>
                            </tr>


                           <tr valign="top">
                                <td width="48%"><img src="../images/clear.gif" alt="" width="5" height="1" border="0"/></td>
                                <td width="1%" class="adminLabel" nowrap>Comments</td>
                                <td width="1%" class="adminText"><textarea class="compSearchForm" name="txtInitialSubmissionDateComment" rows="4" cols="35"><%=initialSubmissionDateComment  %></textarea></td>
                                <td width="48%"><img src="../images/clear.gif" alt="" width="5" height="1" border="0"/></td>
                            </tr>

<!-- screening Complete date
                            <tr valign="top" ID="date_row">
                                <td width="48%"><img src="../images/clear.gif" alt="" width="5" height="1" border="0"/></td>
                                <td width="1%" class="adminLabel" nowrap>Screening Complete Date</td>
                                <td width="1%" class="adminText"><input class="adminSearchForm" type="text" size="20" maxlength="40" name="txtScreeningCompleteDate" value="<%= screeningCompleteDate %>"><br/><span class="adminSmall">MM/DD/YYYY</span></td>
                                <td width="48%"><img src="../images/clear.gif" alt="" width="5" height="1" border="0"/></td>
                            </tr>
                            <tr valign="top">
                                <td width="48%"><img src="../images/clear.gif" alt="" width="5" height="1" border="0"/></td>
                                <td width="1%" class="adminLabel" nowrap>Comments</td>
                                <td width="1%" class="adminText"><textarea class="compSearchForm" name="txtScreeningCompleteDateComment" rows="4" cols="35"><%=screeningCompleteDateComment  %></textarea></td>
                                <td width="48%"><img src="../images/clear.gif" alt="" width="5" height="1" border="0"/></td>
                            </tr>

<!-- Review  Complete Phase
                            <tr valign="top" ID="date_row">
                                <td width="48%"><img src="../images/clear.gif" alt="" width="5" height="1" border="0"/></td>
                                <td width="1%" class="adminLabel" nowrap>Review Complete Date</td>
                                <td width="1%" class="adminText"><input class="adminSearchForm" type="text" size="20" maxlength="40" name="txtReviewCompleteDate" value="<%= reviewCompleteDate %>"><br/><span class="adminSmall">MM/DD/YYYY</span></td>
                                <td width="48%"><img src="../images/clear.gif" alt="" width="5" height="1" border="0"/></td>
                            </tr>
                            <tr valign="top">
                                <td width="48%"><img src="../images/clear.gif" alt="" width="5" height="1" border="0"/></td>
                                <td width="1%" class="adminLabel" nowrap>Comments</td>
                                <td width="1%" class="adminText"><textarea class="compSearchForm" name="txtReviewCompleteDateComment" rows="4" cols="35"><%=reviewCompleteDateComment  %></textarea></td>
                                <td width="48%"><img src="../images/clear.gif" alt="" width="5" height="1" border="0"/></td>
                            </tr>

<!-- Aggregation Complete Phase
                            <tr valign="top" ID="date_row">
                                <td width="48%"><img src="../images/clear.gif" alt="" width="5" height="1" border="0"/></td>
                                <td width="1%" class="adminLabel" nowrap>Aggregation Complete Date</td>
                                <td width="1%" class="adminText"><input class="adminSearchForm" type="text" size="20" maxlength="40" name="txtAggregationCompleteDate" value="<%= aggregationCompleteDate %>"><br/><span class="adminSmall">MM/DD/YYYY</span></td>
                                <td width="48%"><img src="../images/clear.gif" alt="" width="5" height="1" border="0"/></td>
                            </tr>
                            <tr valign="top">
                                <td width="48%"><img src="../images/clear.gif" alt="" width="5" height="1" border="0"/></td>
                                <td width="1%" class="adminLabel" nowrap>Comments</td>
                                <td width="1%" class="adminText"><textarea class="compSearchForm" name="txtAggregationCompleteDateComment" rows="4" cols="35"><%=aggregationCompleteDateComment %></textarea></td>
                                <td width="48%"><img src="../images/clear.gif" alt="" width="5" height="1" border="0"/></td>
                            </tr>

<!--
<!-- Winner Announced
                            <tr valign="top" ID="date_row">
                                <td width="48%"><img src="../images/clear.gif" alt="" width="5" height="1" border="0"/></td>
                                <td width="1%" class="adminLabel" nowrap>Winner Announced</td>
                                <td width="1%" class="adminText"><input class="adminSearchForm" type="text" size="20" maxlength="40" name="txtWinnerAnnouncedDate" value="<%= winnerAnnouncedDate %>"><br/><span class="adminSmall">MM/DD/YYYY</span></td>
                                <td width="48%"><img src="../images/clear.gif" alt="" width="5" height="1" border="0"/></td>
                            </tr>
                           <tr valign="top">
                                <td width="48%"><img src="../images/clear.gif" alt="" width="5" height="1" border="0"/></td>
                                <td width="1%" class="adminLabel" nowrap>Comments</td>
                                <td width="1%" class="adminText"><textarea class="compSearchForm" name="txtWinnerAnnouncedDateComment" rows="4" cols="35"><%=winnerAnnouncedDateComment  %></textarea></td>
                                <td width="48%"><img src="../images/clear.gif" alt="" width="5" height="1" border="0"/></td>
                            </tr>

<!-- Final Submission
                            <tr valign="top" ID="date_row">
                                <td width="48%"><img src="../images/clear.gif" alt="" width="5" height="1" border="0"/></td>
                                <td width="1%" class="adminLabel" nowrap>Final Submission Due Date</td>
                                <td width="1%" class="adminText"><input class="adminSearchForm" type="text" size="20" maxlength="40" name="txtFinalSubmissionDate" value="<%= finalSubmissionDate %>"><br/><span class="adminSmall">MM/DD/YYYY</span></td>
                                <td width="48%"><img src="../images/clear.gif" alt="" width="5" height="1" border="0"/></td>
                            </tr>
                           <tr valign="top">
                                <td width="48%"><img src="../images/clear.gif" alt="" width="5" height="1" border="0"/></td>
                                <td width="1%" class="adminLabel" nowrap>Comments</td>
                                <td width="1%" class="adminText"><textarea class="compSearchForm" name="txtFinalSubmissionDateComment" rows="4" cols="35"><%=finalSubmissionDateComment  %></textarea></td>
                                <td width="48%"><img src="../images/clear.gif" alt="" width="5" height="1" border="0"/></td>
                            </tr>
<!-- Estimated Development Phase
                            <tr valign="top" ID="date_row">
                                <td width="48%"><img src="../images/clear.gif" alt="" width="5" height="1" border="0"/></td>
                                <td width="1%" class="adminLabel" nowrap>Estimated Next Phase Date</td>
                                <td width="1%" class="adminText"><input class="adminSearchForm" type="text" size="20" maxlength="40" name="txtEstimatedDevDate" value="<%= estimatedDevDate %>"><br/><span class="adminSmall">MM/DD/YYYY</span></td>
                                <td width="48%"><img src="../images/clear.gif" alt="" width="5" height="1" border="0"/></td>
                            </tr>


<!-- Phase Complete Phase
                            <tr valign="top" ID="date_row">
                                <td width="48%"><img src="../images/clear.gif" alt="" width="5" height="1" border="0"/></td>
                                <td width="1%" class="adminLabel" nowrap>Phase Complete Date</td>
                                <td width="1%" class="adminText"><input class="adminSearchForm" type="text" size="20" maxlength="40" name="txtPhaseCompleteDate" value="<%= phaseCompleteDate %>"><br/><span class="adminSmall">MM/DD/YYYY</span></td>
                                <td width="48%"><img src="../images/clear.gif" alt="" width="5" height="1" border="0"/></td>
                            </tr>
                            <tr valign="top">
                                <td width="48%"><img src="../images/clear.gif" alt="" width="5" height="1" border="0"/></td>
                                <td width="1%" class="adminLabel" nowrap>Comments</td>
                                <td width="1%" class="adminText"><textarea class="compSearchForm" name="txtPhaseCompleteDateComment" rows="4" cols="35"><%=phaseCompleteDateComment  %></textarea></td>
                                <td width="48%"><img src="../images/clear.gif" alt="" width="5" height="1" border="0"/></td>
                            </tr>
			    -->
<!-- Production Phase -->
                            <tr valign="top" ID="date_row">
                                <td width="48%"><img src="../images/clear.gif" alt="" width="5" height="1" border="0"/></td>
                                <td width="1%" class="adminLabel" nowrap>Production Date</td>
                                <td width="1%" class="adminText"><input class="adminSearchForm" type="text" size="20" maxlength="40" name="txtProductionDate" value="<%= productionDate %>"><br/><span class="adminSmall">MM/DD/YYYY</span></td>
                                <td width="48%"><img src="../images/clear.gif" alt="" width="5" height="1" border="0"/></td>
                            </tr>
                            <tr valign="top">
                                <td width="48%"><img src="../images/clear.gif" alt="" width="5" height="1" border="0"/></td>
                                <td width="1%" class="adminLabel" nowrap>Comments</td>
                                <td width="1%" class="adminText"><textarea class="compSearchForm" name="txtProductionDateComment" rows="4" cols="35"><%=productionDateComment  %></textarea></td>
                                <td width="48%"><img src="../images/clear.gif" alt="" width="5" height="1" border="0"/></td>
                            </tr>


<!-- Phase Version Price -->
                            <tr valign="middle" ID="date_row">
                                <td width="48%"><img src="../images/clear.gif" alt="" width="5" height="1" border="0"/></td>
                                <td width="1%" class="adminLabel" nowrap>Phase Version Price</td>
                                <td width="1%" class="adminText"><input class="adminSearchForm" type="text" size="20" maxlength="40" name="txtPhaseVersionPrice" value="<%= ((phasePrice == null) ? "" : phasePrice.toString()) %>"> * if the project has already been created, this value won't be updated</td>
                                <td width="48%"><img src="../images/clear.gif" alt="" width="5" height="1" border="0"/></td>
                            </tr>
<!-- component status -->
                            <tr valign="middle" ID="date_row">
                                <td width="48%"><img src="../images/clear.gif" alt="" width="5" height="1" border="0"/></td>
                                <td width="1%" class="adminLabel" nowrap>Posting Status</td>
                                <td width="1%" class="adminText">
                                    <select name="selPostingStatus">
                                        <option value="301" <%= (postingStatusId == 301L ? " SELECTED" : "") %>>NEW POST</option>
                                        <option value="302"<%= (postingStatusId == 302L ? " SELECTED" : "") %>>REPOST</option>
<%--
                                        <option value="303"<%= (postingStatusId == 303L ? " SELECTED" : "") %>>Tournament</option>
--%>
                                    </select>
                                </td>
                                <td width="48%"><img src="../images/clear.gif" alt="" width="5" height="1" border="0"/></td>
                            </tr>
<!-- component status -->
                            <tr valign="middle" ID="date_row">
                                <td width="48%"><img src="../images/clear.gif" alt="" width="5" height="1" border="0"/></td>
                                <td width="1%" class="adminLabel" nowrap>Component Level</td>
                                <td width="1%" class="adminText">
                                    <select name="selLevelId">
                                        <option value="100" <%= (levelId == 100L ? " SELECTED" : "") %>>Level 1</option>
                                        <option value="200"<%= (levelId == 200L ? " SELECTED" : "") %>>Level 2</option>
                                        <option value="300" <%= (levelId == 300L ? " SELECTED" : "") %>>Level 3</option>
                                        <option value="400"<%= (levelId == 400L ? " SELECTED" : "") %>>Level 4</option>\
                                        <option value="500"<%= (levelId == 500L ? " SELECTED" : "") %>>Level 5</option>\
                                    </select>
                                </td>
                                <td width="48%"><img src="../images/clear.gif" alt="" width="5" height="1" border="0"/></td>
                            </tr>

<!-- Suspended -->
         <!--                    <tr valign="middle" ID="date_row">
                                <td width="48%"><img src="../images/clear.gif" alt="" width="5" height="1" border="0"/></td>
                                <td width="1%" class="adminLabel" nowrap>Stop attempting to get this built</td>
                                <td width="1%" class="adminText">
                                   <input type="checkbox" name="suspended" value="1" <%= ( ver.getSuspended()? " CHECKED" : "") %> > </input>
                                   <br />
                                </td>
                                <td width="48%"><img src="../images/clear.gif" alt="" width="5" height="1" border="0"/></td>
                            </tr>
 -->
<!-- Public Forums -->
                            <tr valign="middle" ID="date_row">
                                <td width="48%"><img src="../images/clear.gif" alt="" width="5" height="1" border="0"/></td>
                                <td width="1%" class="adminLabel" nowrap>Public Developer Forums</td>
                                <td width="1%" class="adminText">
                                   <!-- <input type="checkbox" name="public_forum" value ="1" checked> </input> -->
                                   <input type="checkbox" name="public_forum" value ="1" <%= ( ver.getPublicForum() == true ? " CHECKED" : "") %> > </input>
                                   <br />
                                </td>
                                <td width="48%"><img src="../images/clear.gif" alt="" width="5" height="1" border="0"/></td>
                            </tr>
<!-- Technologies-->
<%
	int technologyCNT = 0;
	if (colTechnology != null) {
		Iterator iter = colTechnology.iterator();
		while (iter.hasNext()) {
			Object obj = iter.next();
			if (obj instanceof Technology) {
				technologyCNT++;
%>
                            <tr valign="middle">
                                <td width="48%"><img src="../images/clear.gif" alt="" width="5" height="1" border="0"/></td>
                                <td width="1%" class="adminLabel"><% if (technologyCNT == 1) { %>Technologies Used<% } %></td>
                                <td width="1%" class="adminText">
                                    <input type="checkbox" name="tech_<%= ((Technology)obj).getId() %>" value ="<%= ((Technology)obj).getId() %>"<%= ( technologies.get("" + ((Technology)obj).getId()) != null ? " CHECKED" : "") %>> <%= ((Technology)obj).getName() %></input><br />
                                </td>
                                <td width="48%"><img src="../images/clear.gif" alt="" width="5" height="1" border="0"/></td>
                            </tr>
<%                      }
		}
	}
%>

<!-- Submit Button -->
                            <tr valign="middle">
                                    <td width="48%"><img src="../images/clear.gif" alt="" width="5" height="1" border="0"/></td>
                                    <td colspan="2"><img src="../images/clear.gif" alt="" width="5" height="5" border="0"/></td>
                                    <td width="48%"><img src="../images/clear.gif" alt="" width="5" height="1" border="0"/></td>
                            </tr>
                            <tr valign="middle">
                                    <td width="48%"><img src="../images/clear.gif" alt="" width="5" height="1" border="0"/></td>
                                    <td colspan="2" class="adminTextCenter"><input class="adminButton" type="submit" name="a" value="Save"></td>
                                    <td width="48%"><img src="../images/clear.gif" alt="" width="5" height="1" border="0"/></td>
                            </tr>
                        </table>

                        <table width="500" cellpadding="0" cellspacing="0" border="0" align="center" class="admin">
                           <tr><td><img src="../images/adminFoot.gif" alt="" width="500" height="11" border="0" /></td></tr>
                        </table>
<!-- User Info Ends -->

                        <table width="500" cellpadding="0" cellspacing="0" border="0" align="center">
                            <tr><td height="15"><img src="../images/clear.gif" alt="" width="10" height="15" border="0"/></td></tr>
                        </table>
                    </td>
                </tr>
            </table>

            <table width="100%" cellpadding="0" cellspacing="6" align="center" border="0">
                <tr valign="top">
                    <td align="center">
                        <table cellpadding="0" cellspacing="0" border="0">
                            <tr><td width="445" height="29" colspan="2"><img src="../images/adminBaseDependHead.gif" alt="Base Component Dependencies" width="500" height="29" border="0" /></td></tr>
                        </table>

                        <table width="500" border="0" cellspacing="8" cellpadding="0" align="center" class="admin">
    <!-- Master List -->
                            <tr valign="middle">
                                <td width="48%">
                                    <img src="../images/clear.gif" alt="" width="5" height="1" border="0"/></td>
                                <td width="1%" class="adminTextCenter">Base Components Master List</td>
                                <td width="48%"><img src="../images/clear.gif" alt="" width="5" height="1" border="0"/></td>
                            </tr>
<%
    // List all completed components
    ComponentSummary approvedComps[];
    boolean showCatalog = false;
    if (componentManager.getRootCategory() == Catalog.APPLICATION_CATALOG) {
        showCatalog = true;
        approvedComps = (ComponentSummary[])catalog.getComponentsByStatusAndCatalogs(ComponentInfo.APPROVED, Arrays.asList(
            new Long[] {new Long(Catalog.NET_CATALOG), new Long(Catalog.JAVA_CATALOG),
            new Long(Catalog.NET_CUSTOM_CATALOG), new Long(Catalog.JAVA_CUSTOM_CATALOG)})).toArray(new ComponentSummary[0]);
    } else {
        approvedComps = (ComponentSummary[])catalog.getComponentsByStatusAndCatalog(ComponentInfo.APPROVED, componentManager.getRootCategory()).toArray(new ComponentSummary[0]);
    }
    //debug.addMsg("component version admin", "got approved summaries");

    // List components for this version
    ComponentSummary versionComps[] = (ComponentSummary[])componentManager.getDependencies().toArray(new ComponentSummary[0]);
    //debug.addMsg("component version admin", "got component's summaries");
    Hashtable htHits = new Hashtable();
    for (int i=0; i< versionComps.length; i++) {
        htHits.put("" + versionComps[i].getVersionId(), "hit");
    }
%>

                            <tr valign="middle">
                                <td width="48%"><img src="../images/clear.gif" alt="" width="5" height="1" border="0"/></td>
                                <td width="1%" class="adminTextCenter">
                                    <select name="selMasterDependency" size="8" multiple="multiple">
<%
                            for (int i=0; i < approvedComps.length; i++) {
                                //debug.addMsg("component version admin", "got master component " + (i+1) + "/" + approvedComps.length);
                                if (htHits.get("" + approvedComps[i].getVersionId()) == null) {
                                    String catalogName = "";
                                    if (showCatalog) {
                                        if (approvedComps[i].getRootCategory() == Catalog.NET_CATALOG) {
                                            catalogName = ".net -- ";
                                        } else if (approvedComps[i].getRootCategory() == Catalog.JAVA_CATALOG) {
                                            catalogName = "java -- ";
                                        } else if (approvedComps[i].getRootCategory() == Catalog.NET_CUSTOM_CATALOG) {
                                            catalogName = "custom .net -- ";
                                        } else if (approvedComps[i].getRootCategory() == Catalog.JAVA_CUSTOM_CATALOG) {
                                            catalogName = "custom java -- ";
                                        }
                                    }
%>
                                        <option value="<%= approvedComps[i].getVersionId() %>"><%= catalogName%> <%= approvedComps[i].getName() %> v.<%= approvedComps[i].getVersionLabel() %></option>
<%                              }
                            }
%>
                                    </select>
                                </td>
                                <td width="48%"><img src="../images/clear.gif" alt="" width="5" height="1" border="0"/></td>
                            </tr>

<!-- Navigation Buttons -->
                            <tr valign="middle">
                                <td width="48%"><img src="../images/clear.gif" alt="" width="5" height="1" border="0"/></td>
                                <td width="1%" class="adminTextCenter"><input type="image" src="../images/buttonAdd.gif" alt="\/" name="add" value=">>" /></input>&nbsp;&nbsp;&nbsp;<input type="image" src="../images/buttonRemove.gif" alt="/\" name="remove" value="<<" /></input></td>
                                <td width="48%"><img src="../images/clear.gif" alt="" width="5" height="1" border="0"/></td>
                            </tr>

<!-- Associated Categories -->
                            <tr valign="middle">
                                <td width="48%"><img src="../images/clear.gif" alt="" width="5" height="1" border="0"/></td>
                                <td width="1%" class="adminTextCenter">
                                    <select name="selVersionDependency" size="4" multiple="multiple">
    <% for (int i=0; i < versionComps.length; i++) {
        //debug.addMsg("component version admin", "got version component " + (i+1) + "/" + versionComps.length);
            String catalogName = "";
            if (showCatalog) {
                if (versionComps[i].getRootCategory() == Catalog.NET_CATALOG) {
                    catalogName = ".net -- ";
                } else if (versionComps[i].getRootCategory() == Catalog.JAVA_CATALOG) {
                    catalogName = "java -- ";
                } else if (versionComps[i].getRootCategory() == Catalog.NET_CUSTOM_CATALOG) {
                    catalogName = "custom .net -- ";
                } else if (versionComps[i].getRootCategory() == Catalog.JAVA_CUSTOM_CATALOG) {
                    catalogName = "custom java -- ";
                }
            }
    %>
                                        <option value="<%= versionComps[i].getVersionId() %>"><%= catalogName%><%= versionComps[i].getName() %> v.<%= versionComps[i].getVersionLabel() %></option>
    <% } %>
                                    </select></td>
                                <td width="48%"><img src="../images/clear.gif" alt="" width="5" height="1" border="0"/></td>
                            </tr>

                            <tr valign="middle">
                                <td width="48%"><img src="../images/clear.gif" alt="" width="5" height="1" border="0"/></td>
                                <td width="1%" class="adminTextCenter" nowrap>Base Components for <strong><%= componentManager.getComponentInfo().getName() %> version <%= ver.getVersionLabel() %></strong></td>
                                <td width="48%"><img src="../images/clear.gif" alt="" width="5" height="1" border="0"/></td>
                            </tr>
                        </table>

                        <table width="500" cellpadding="0" cellspacing="0" border="0" align="center" class="admin">
                            <tr><td><img src="../images/adminFoot.gif" alt="" width="500" height="11" border="0" /></td></tr>
                        </table>

                        <table width="500" cellpadding="0" cellspacing="0" border="0" align="center">
                            <tr><td height="15"><img src="../images/clear.gif" alt="" width="10" height="15" border="0"/></td></tr>
                        </table>
                    </td>
                </tr>
</form>
            </table>

<!-- Documentation begins -->
            <table width="100%" border="0" cellpadding="0" cellspacing="0" align="center">
                <tr><td class="adminSubhead">Documentation</td></tr>
            </table>

            <table width="100%" border="0" cellpadding="0" cellspacing="1" align="center" bgcolor="#FFFFFF">
                <tr valign="top">
                    <td width="30%" class="adminTitle">Name</td>
                    <td width="30%" class="adminTitle">Type</td>
                    <td width="30%" class="adminTitle">URL</td>
                    <td width="10%" class="adminTitleCenter">Action</td>
                </tr>

	<%
        Long docTypeId;
        String selected;
        String docTypeName;

		com.topcoder.dde.catalog.Document documents[] = (com.topcoder.dde.catalog.Document[])componentManager.getDocuments().toArray(new com.topcoder.dde.catalog.Document[0]);
		for (int i=0; i < documents.length; i++) {
	%>
<form action="<%= page_name %>" enctype="multipart/form-data" method="POST">
<input type="hidden" name="comp" value="<%= lngComponent %>">
<input type="hidden" name="ver" value="<%= ver.getVersion() %>">
<input type="hidden" name="lngDocument" value="<%= documents[i].getId() %>">
                <tr valign="top">
                    <td class="forumText">
                       <input type="text" name="txtDocumentName" value="<%=documents[i].getName()%>" size="25" />
                    </td>
                    <td class="forumText">
                        <select name="selDocType">
                            <%
                                // Bill the drop-down list of available document types
                                iterator = docTypesMap.keySet().iterator();
                                while (iterator.hasNext()) {
                                    docTypeId = (Long) iterator.next();
                                    docTypeName = (String) docTypesMap.get(docTypeId);
                                    selected = "";
                                    if (docTypeId.longValue() == documents[i].getType()) {
                                        selected = "selected";
                                    }
                            %>
                            <option value="<%=docTypeId%>" <%=selected%>><%=docTypeName%></option>
                            <%
                                }
                            %>
                        </select>
                    </td>
                    <td class="forumText">
                        <A HREF="/pages/c_component_doc.jsp?path=<%= documents[i].getURL() %>"><%= documents[i].getURL() %></A>
                    </td>
                    <td class="forumTextCenter" nowrap>
                        <input type="file" name="file1" value="" maxlength="250" size="25">
                        <BR>
                        <input type="submit" class="adminButton" name="a" value="Upload Document">
                        <input type="submit" class="adminButton" name="a" value="Update Document">
                        <br>
                        <strong><a href="component_version_admin.jsp?comp=<%=lngComponent%>&ver=<%=lngVersion%>&document=<%=documents[i].getId()%>&a=DeleteDocument">Delete Document</a></strong>
                    </td>
                </tr>
</form>
	<% } %>

<form action="<%= page_name %>" enctype="multipart/form-data" method="POST">
<input type="hidden" name="comp" value="<%= lngComponent %>">
<input type="hidden" name="ver" value="<%= ver.getVersion() %>">
                <tr valign="top">
                <%--
                    <td class="forumText"><input class="adminSearchForm" type="text" size="25" maxlength="128" name="txtDocumentName" value=""></input></td>
                    <td class="forumText">
                    <select name="selDocType">
                        <option value="0">Requirement Specification</option>
                        <option value="1">Component Specification</option>
                        <option value="2">Use Case Diagram</option>
                        <option value="3">Class Diagram</option>
                        <option value="4">Test Scorecard</option>
                        <option value="5">Deployment</option>
                        <option value="6">Other (misc.)</option>
                        <option value="7">Specification Forum documents</option>
                        <option value="8">Screen shots</option>
                        <option value="9">Screen shots Thumbnail</option>
                        <option value="10">Process Flow</option>
                        <option value="11">Class Diagram Cell Level</option>
                        <option value="12">Class Diagram Top Level</option>
                        <option value="13">Component Diagram</option>
                        <option value="14">Developer Guide</option>
                        <option value="15">Sequence Diagram</option>
                        <option value="16">Interaction Diagram</option>
                        <option value="22">Aggregated Scorecard</option>
                        <option value="23">Javadocs</option>
                    </select></td>
                --%>
                    <td class="forumText" colspan="2">&nbsp;<!--input class="adminSearchForm" type="text" size="25" maxlength="64" name="txtDocumentURL" value=""></input--></td>
                    <td class="forumTextCenter" colspan="2">
                        <input type="file" name="file1" value="" maxlength="250" size="50">
                        <input class="adminButton" type="submit" name="a" value="Add Document"></input>
                    </td>
                </tr>

                <tr><td class="adminTitle" colspan="4"><img src="../images/clear.gif" alt="" width="10" height="1" border="0"/></td></tr>
<!-- Documentation ends -->

                <tr><td colspan="2" height="15"><img src="../images/clear.gif" alt="" width="10" height="15" border="0"/></td></tr>
</form>
            </table>

<!-- Downloads begins -->
            <table width="100%" border="0" cellpadding="0" cellspacing="0" align="center">
                <tr><td class="adminSubhead">Downloads</td></tr>
            </table>

            <table width="100%" border="0" cellpadding="0" cellspacing="1" align="center" bgcolor="#FFFFFF">
                <tr valign="top">
                    <td width="30%" class="adminTitle">Name</td>
                    <td width="60%" class="adminTitle">URL</td>
                    <td width="10%" class="adminTitleCenter">Action</td>
                </tr>

<%
    Download downloads[] = (Download[])componentManager.getDownloads().toArray(new Download[0]);
    for (int i=0; i < downloads.length; i++) {
%>
<form name="frmComponent" action="<%= page_name %>" enctype="multipart/form-data" method="POST">
<input type="hidden" name="comp" value="<%= lngComponent %>">
<input type="hidden" name="ver" value="<%= ver.getVersion() %>">
<input type="hidden" name="lngDownload" value="<%= downloads[i].getId() %>">
                <tr valign="top">
                    <td class="forumText"><%= downloads[i].getDescription() %></td>
                    <td class="forumText">
                        <A HREF="/pages/c_component_doc.jsp?path=<%= downloads[i].getURL() %>"><%= downloads[i].getURL() %></A>
                    </td>
                    <td class="forumTextCenter" nowrap>
                        <input type="file" name="file1" value="" maxlength="250" size="25">
                        <BR>
                        <input class="adminButton" type="submit" name="a" value="Upload Download">
                        <strong><a href="component_version_admin.jsp?comp=<%= lngComponent %>&ver=<%= lngVersion %>&download=<%= downloads[i].getId() %>&a=DeleteDownload">Delete Download</a></strong>
                </td>
                </tr>
</form>
<% } %>

<form action="<%= page_name %>" enctype="multipart/form-data" method="POST">
<input type="hidden" name="comp" value="<%= lngComponent %>">
<input type="hidden" name="ver" value="<%= ver.getVersion() %>">

                <tr valign="top">
                    <td class="forumText"><input class="adminSearchForm" type="text" size="25" maxlength="64" name="txtDownloadDescription" value=""></input></td>
                    <td class="forumText">&nbsp;<!--input class="adminSearchForm" type="text" size="45" name="txtDownloadURL" value=""></input--></td>
                    <td class="forumTextCenter"><input type="file" name="file1" value="" maxlength="250" size="25"><input class="adminButton" type="submit" name="a" value="Add Download"></input></td>
                </tr>

                <tr><td class="adminTitle" colspan="3"><img src="../images/clear.gif" alt="" width="10" height="1" border="0"/></td></tr>
</form>
<!-- Downloads ends -->

                <tr><td colspan="2" height="15"><img src="../images/clear.gif" alt="" width="10" height="15" border="0"/></td></tr>
            </table>

<!-- Submissions begins -->

<%--            <table width="100%" border="0" cellpadding="0" cellspacing="0" align="center">
                <tr><td class="adminSubhead">Submissions</td></tr>
            </table>

            <table width="100%" border="0" cellpadding="0" cellspacing="1" align="center" bgcolor="#FFFFFF">
                <tr valign="top">
                    <td class="adminTitle">Phase</td>
                    <td class="adminTitle">Submitter</td>
                    <td class="adminTitle">Date</td>
                    <td class="adminTitle">Action</td>
                </tr>

<%

    Submission submissions[] = Utility.getSubmissions(componentManager.getVersionInfo().getVersionId());
    Submitters submittersEJB = Utility.getSubmitters();
    for (int i=0; i < submissions.length; i++) {
        Submitter submitter = submittersEJB.getSubmitter(submissions[i].getSubmitterId());
        User u = USER_MANAGER.getUser(submitter.getLoginId());
        RegistrationInfo inf = u.getRegInfo();
        String userName = inf.getUsername();
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(submissions[i].getDate().getTime());
%>
<form name="frmComponent" action="<%= "/catalog/submission" %>" method="POST">
<input type="hidden" name="submission_id" value="<%= submissions[i].getSubmissionId() %>">
<input type="hidden" name="Project" value="<%= component.getName() %>">
<input type="hidden" name="ver" value="<%= ver.getVersion() %>">
<%      boolean comment = submissions[i].getComment() != null && !submissions[i].getComment().equals("");
        if (comment) {
%>
<SCRIPT LANGUAGE="JavaScript">
    function comment<%= submissions[i].getSubmissionId() %>() {
        newWindow = window.open('','newWin','toolbar=no,location=no,scrollbars=no,status=no,width=630,height=400');
        newWindow.document.write("<HTML><HEAD><TITLE>Comment from <%= userName %></TITLE></HEAD><BODY><PRE><%= Utility.stripHTML(submissions[i].getComment()) %></PRE></BODY></HTML>");
        newWindow.document.close();
    }
</SCRIPT>
<%      }
%>
                <tr valign="top">
                    <td class="forumText"><%= ComponentVersionInfo.getPhaseName(submitter.getPhaseId()) %></td>
                    <td class="forumText"><%= userName %></td>
                    <td class="forumText" nowrap><%= ""+cal.get(Calendar.DAY_OF_MONTH)+"/"+(1+cal.get(Calendar.MONTH))+"/"+cal.get(Calendar.YEAR)+" "+cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE)+":"+cal.get(Calendar.SECOND) %></td>
<%      if (comment) {
%>
                    <td><input class="adminButton" type="submit" name="a" value="Download Submission"><input class="adminButton" type="button" onClick="comment<%= submissions[i].getSubmissionId() %>()" value="View Comment"></td>
<%      } else {
%>
                    <td><input class="adminButton" type="submit" name="a" value="Download Submission"> No Comment</td>
<%      }
%>
                </tr>
</form>
<% } %>

<form action="<%= page_name %>" enctype="multipart/form-data" method="POST">
<input type="hidden" name="comp" value="<%= lngComponent %>">
<input type="hidden" name="ver" value="<%= ver.getVersion() %>">

                <tr><td class="adminTitle" colspan="3"><img src="../images/clear.gif" alt="" width="10" height="1" border="0"/></td></tr>
</form>
<!-- Submissions ends -->

                <tr><td colspan="2" height="15"><img src="../images/clear.gif" alt="" width="10" height="15" border="0"/></td></tr>
            </table>--%>

<form action="<%= page_name %>" method="POST">
<input type="hidden" name="comp" value="<%= lngComponent %>">
<input type="hidden" name="ver" value="<%= ver.getVersion() %>">
<%--
<!-- Examples begins -->
            <table width="100%" border="0" cellpadding="0" cellspacing="0" align="center">
                <tr><td class="adminSubhead">Examples</td></tr>
            </table>

            <table width="100%" border="0" cellpadding="0" cellspacing="1" align="center" bgcolor="#FFFFFF">
                <tr valign="top">
                    <td width="30%" class="adminTitle">Name</td>
                    <td width="60%" class="adminTitle">URL</td>
                    <td width="10%" class="adminTitleCenter">Action</td>
                </tr>

<%
    Example examples[] = (Example[])componentManager.getExamples().toArray(new Example[0]);
    for (int i=0; i < examples.length; i++) {
    debug.addMsg("component version admin", "got example " + (i+1) + "/" + examples.length);
%>
                <tr valign="top">
                    <td class="forumText"><%= examples[i].getDescription() %></td>
                    <td class="forumText"><%= examples[i].getURL() %></td>
                    <td class="forumTextCenter" nowrap><strong><a href="component_version_admin.jsp?comp=<%= lngComponent %>&ver=<%= lngVersion %>&example=<%= examples[i].getId() %>&a=DeleteExample">Delete Example</a></strong></td>
                </tr>
<% } %>

                <tr valign="top">
                    <td class="forumText"><input class="adminSearchForm" type="text" size="25" maxlength="64" name="txtExampleDescription" value=""></td>
                    <td class="forumText"><input class="adminSearchForm" type="text" size="45" name="txtExampleURL" value=""></td>
                    <td class="forumTextCenter"><input class="adminButton" type="submit" name="a" value="Add Example"></input></td>
                </tr>

                <tr><td class="adminTitle" colspan="3"><img src="../images/clear.gif" alt="" width="10" height="1" border="0"/></td></tr>
<!-- Examples ends -->

                <tr><td colspan="2" height="15"><img src="../images/clear.gif" alt="" width="10" height="15" border="0"/></td></tr>
            </table>
--%>

<!-- Reviews begins -->
<%--
            <table width="100%" border="0" cellpadding="0" cellspacing="0" align="center">
                <tr><td class="adminSubhead">Reviews</td></tr>
            </table>

            <table width="100%" border="0" cellpadding="0" cellspacing="1" align="center" bgcolor="#FFFFFF">
                <tr valign="top">
                    <td width="25%" class="adminTitle">Username</td>
                    <td width="20%" class="adminTitle">Rating</td>
                    <td width="15%" class="adminTitle">Review Date</td>
                    <td width="30%" class="adminTitle">Comments</td>
                    <td width="10%" class="adminTitleCenter">Action</td>
                </tr>

<%
    Review reviews[] = (Review[])componentManager.getReviews().toArray(new Review[0]);
    for (int i=0; i < reviews.length; i++) {
%>
                <tr valign="top">
                    <td class="forumText"><%= reviews[i].getUsername() %></td>
                    <td class="forumText"><%= reviews[i].getRating() %></td>
                    <td class="forumText"><% //= reviews[i].getReviewDate() %></td>
                    <td class="forumText"><%= reviews[i].getComments() %></td>
                    <td class="forumTextCenter" nowrap><strong><a href="component_version_admin.jsp?comp=<%= lngComponent %>&ver=<%= lngVersion %>&review=<%= reviews[i].getId() %>&a=DeleteReview">Delete Review</a></strong></td>
                </tr>
<% } %>

                <tr valign="top">
                    <td class="forumText"><input class="adminSearchForm" type="text" size="25" maxlength="64" name="txtReviewUsername" value=""></input></td>
                    <td class="forumText"><input class="adminSearchForm" type="text" size="10" name="txtReviewRating" value=""></input></td>
                    <td class="forumText">&nbsp;</td>
                    <td class="forumText"><input class="adminSearchForm" type="text" size="30" name="txtReviewComments" value=""></input></td>
                    <td class="forumTextCenter"><input class="adminButton" type="submit" name="a" value="Add Review"></input></td>
                </tr>

                <tr><td class="adminTitle" colspan="5"><img src="../images/clear.gif" alt="" width="10" height="1" border="0"/></td></tr>
<!-- Reviews ends -->

                <tr><td colspan="2" height="15"><img src="../images/clear.gif" alt="" width="10" height="15" border="0"/></td></tr>
            </table>
--%>

<!-- Team Member Roles begins -->
            <table width="100%" border="0" cellpadding="0" cellspacing="0" align="center">
                <tr><td class="adminSubhead">Team Member Roles</td></tr>
            </table>

<%
    Role roles[] = (Role[])catalog.getRoles().toArray(new Role[0]);
%>
            <table width="100%" border="0" cellpadding="0" cellspacing="1" align="center" bgcolor="#FFFFFF">
                <tr valign="top">
                    <td width="40%" class="adminTitle">Username</td>
                    <td width="50%" class="adminTitle">Role</td>
                    <td width="10%" class="adminTitleCenter">Action</td>
                </tr>
                <tr valign="top">
                    <td class="adminTitle" colspan="3">Description</td>
                </tr>

<%
    TeamMemberRole teamMemberRoles[] = (TeamMemberRole[])componentManager.getTeamMemberRoles().toArray(new TeamMemberRole[0]);
    for (int i=0; i < teamMemberRoles.length; i++) {
%>
                <tr valign="top">
                    <td class="forumText"><%= teamMemberRoles[i].getUsername() %></td>
                    <td class="forumText"><%= teamMemberRoles[i].getRoleName() %></td>
                    <td class="forumTextCenter" nowrap><strong><a href="component_version_admin.jsp?comp=<%= lngComponent %>&ver=<%= lngVersion %>&role=<%= teamMemberRoles[i].getId() %>&a=DeleteRole">Delete Role</a></strong></td>
                </tr>
                <%
                    if (teamMemberRoles[i].getDescription() != null) {
                %>
                            <tr valign="top">
                                <td class="forumText" colspan="3"><%= teamMemberRoles[i].getDescription() %></td>
                            </tr>
                <%
                    }
                %>
<% } %>

                <tr valign="top">
                    <td class="forumText"><input class="adminSearchForm" type="text" size="64" maxlength="64" name="txtTeamMemberRoleUsername" value=""></td>
                    <td class="forumText">
                        <select name="selRole">
<% for (int i=0; i < roles.length; i++) { %>
                            <option value="<%= roles[i].getId() %>"><%= roles[i].getName() %></option>
<% } %>
                        </select></td>
                </tr>
                <tr valign="top">
                    <td class="forumText" colspan="2"><input class="adminSearchForm" type="text" size="150" maxlength="254" name="txtTeamMemberDescription" value=""></td>
                    <td class="forumTextCenter"><input class="adminButton" type="submit" name="a" value="Add Role"></input></td>
                </tr>
            </table>

<!-- Permission Roles begins -->
            <table width="100%" border="0" cellpadding="0" cellspacing="0" align="center">
                <tr><td class="adminSubhead">Permissions</td></tr>
            </table>

            <table width="100%" border="0" cellpadding="0" cellspacing="1" align="center" bgcolor="#FFFFFF">
                <tr valign="top">
                    <td>
                        Handle: &#160;<input class="adminSearchForm" type="text" size="20" name="txtHandle">
                    </td>
                    <td>
<%if (ver.getPhase() != ver.COLLABORATION) {%>
                        <input class="adminButton" type="submit" name="a" value="Developer Forum User">
                        <input class="adminButton" type="submit" name="a" value="Developer Forum Moderator">
<%}%>
                        <input class="adminButton" type="submit" name="a" value="Download Role">
                    </td>
                </tr>
            </table>

<!-- Notifications begins -->
<%if (ver.getPhase() != ver.COLLABORATION) {%>
            <table width="100%" border="0" cellpadding="0" cellspacing="0" align="center">
                <tr><td class="adminSubhead">Forum Watches</td></tr>
            </table>

            <table width="100%" border="0" cellpadding="0" cellspacing="1" align="center" bgcolor="#FFFFFF">
                <tr valign="top">
                    <td>
                        Handle: &#160;<input class="adminSearchForm" type="text" size="20" name="txtTCHandle">
                    </td>
                    <td>
                        <input class="adminButton" type="submit" name="a" value="Watch Developer Forums">
                    </td>
                </tr>
            </table>
<%}%>
<!-- Notifications ends -->

        </td>
</form>
<!-- Middle Column ends -->

<% } else { %>

<!-- Middle Column begins -->
    <td width="100%">
        <table width="100%" border="0" cellpadding="0" cellspacing="0" align="center">
            <tr><td height="15"><img src="/images/clear.gif" alt="" width="10" height="15" border="0" /></td></tr>
            <tr><td class="normal"><img src="/images/headUserAdmin.gif" alt="User Admin" width="545" height="35" border="0" /></td></tr>
            <tr><td class="adminSubhead"><%= componentManager.getComponentInfo().getName() %></td></tr>
        </table>

        <table width="100%" cellpadding="0" cellspacing="0" align="center" border="0">
            <tr valign="top">
                <td align="center">

                    <table cellpadding="0" cellspacing="0" border="0">
                        <tr><td width="445" height="29" colspan="2"><img src="../images/adminNameDescHead.gif" alt="Name and Description" width="500" height="29" border="0" /></td></tr>
                    </table>

                    <table width="500" border="0" cellspacing="8" cellpadding="0" align="center" class="admin">

<!-- Version Label Name -->
                        <tr valign="middle">
<form name="frmComponent" action="<%= page_name %>" method="POST">
<input type="hidden" name="comp" value="<%= lngComponent %>">
<input type="hidden" name="ver" value="<%= ver.getVersion() %>">
                            <td width="48%"><img src="../images/clear.gif" alt="" width="5" height="1" border="0"/></td>
                            <td width="1%" class="adminLabel" nowrap>Version Label</td>
                            <td width="1%" class="adminText"><input class="adminSearchForm" type="text" size="8" maxlength="8" name="txtVersionLabel"  value=""></input></td>
                            <td width="48%"><img src="../images/clear.gif" alt="" width="5" height="1" border="0"/></td>
                        </tr>

<!-- Comments -->
                        <tr valign="top">
                            <td width="48%"><img src="../images/clear.gif" alt="" width="5" height="1" border="0"/></td>
                            <td width="1%" class="adminLabel" nowrap>Comments</td>
                            <td width="1%" class="adminText"><textarea class="compSearchForm" name="taComments" rows="4" cols="35"></textarea></td>
                            <td width="48%"><img src="../images/clear.gif" alt="" width="5" height="1" border="0"/></td>
                        </tr>

<!-- Technology -->
                        <tr valign="top">
                            <td width="48%"><img src="../images/clear.gif" alt="" width="5" height="1" border="0"/></td>
                            <td width="1%" class="adminLabel" nowrap>Technologies Used</td>
                            <td width="1%" class="adminText"></td>
                            <td width="48%"><img src="../images/clear.gif" alt="" width="5" height="1" border="0"/></td>
                        </tr>

<%
if (colTechnology != null) {
    Iterator iter = colTechnology.iterator();
    while (iter.hasNext()) {
            Object obj = iter.next();
            if (obj instanceof Technology) {
%>
                        <tr valign="top">
                            <td width="48%"><img src="../images/clear.gif" alt="" width="5" height="1" border="0"/></td>
                            <td width="1%" class="adminLabel"></td>
                            <td width="1%" class="adminText">
                                <input type="checkbox" name="tech_<%= ((Technology)obj).getId() %>" value ="<%= ((Technology)obj).getId() %>"<%= ( technologies.get("" + ((Technology)obj).getId()) != null ? " CHECKED" : "") %>> <%= ((Technology)obj).getName() %></input><br />
                            </td>
                            <td width="48%"><img src="../images/clear.gif" alt="" width="5" height="1" border="0"/></td>
                        </tr>
<%
            }
    }
}
%>

<!-- Submit Button -->
                        <tr valign="middle">
                            <td width="48%"><img src="../images/clear.gif" alt="" width="5" height="1" border="0"/></td>
                            <td colspan="2"><img src="../images/clear.gif" alt="" width="5" height="5" border="0"/></td>
                            <td width="48%"><img src="../images/clear.gif" alt="" width="5" height="1" border="0"/></td>
                        </tr>

                        <tr valign="middle">
                            <td width="48%"><img src="../images/clear.gif" alt="" width="5" height="1" border="0"/></td>
                            <td colspan="2" class="adminTextCenter"><input class="adminButton" type="submit" name="a" value="Create"></td>
                            <td width="48%"><img src="../images/clear.gif" alt="" width="5" height="1" border="0"/></td>
                        </tr>
                    </table>

                    <table width="500" cellpadding="0" cellspacing="0" border="0" align="center" class="admin">
                        <tr><td><img src="../images/adminFoot.gif" alt="" width="500" height="11" border="0" /></td></tr>
                    </table>

                    <table width="500" cellpadding="0" cellspacing="0" border="0" align="center">
                        <tr><td height="40"><img src="../images/clear.gif" alt="" width="10" height="40" border="0"/></td></tr>
                    </table>
                </td>
<!-- Middle Column Ends -->

            </tr>
        </table>
    </td>
</form>
<% } %>

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
