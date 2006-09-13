/*
 * Copyright (C) 2006 TopCoder Inc.  All Rights Reserved.
 */
package com.cronos.onlinereview.actions;

import java.io.File;
import java.io.IOException;

import com.topcoder.file.transfer.FileSystemHandler;
import com.topcoder.file.transfer.persistence.FileSystemPersistence;
import com.topcoder.file.transfer.registry.FileSystemRegistry;
import com.topcoder.file.transfer.registry.FileSystemXmlRegistry;
import com.topcoder.file.transfer.search.FileIdGroupSearcher;
import com.topcoder.file.transfer.search.RegexFileSearcher;
import com.topcoder.file.transfer.search.RegexGroupSearcher;
import com.topcoder.file.transfer.search.SearchManager;
import com.topcoder.file.transfer.validator.FreeDiskSpaceNonNativeChecker;
import com.topcoder.file.transfer.validator.UploadRequestValidator;
import com.topcoder.processor.ipserver.IPServer;
import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.config.UnknownNamespaceException;
import com.topcoder.util.datavalidator.ObjectValidator;
import com.topcoder.util.errorhandling.BaseException;
import com.topcoder.util.idgenerator.IDGenerator;
import com.topcoder.util.idgenerator.IDGeneratorFactory;

/**
 * This class is a temporary solution which helps run the File System Server.
 *
 * @author George1
 * @author real_vg
 * @version 1.0
 */
final class FileSystemServerHelper {

    /**
     * This member variable is a string constant that defines the name of the configuration
     * namespace which contains the configurtaion parameters for the mssage factory.
     */
    public static final String MESSAGE_FACTORY_NAMESPACE = "com.topcoder.processor.ipserver.message";
    
    /**
     * This member variable is a string constant that defines the name of the ID-generation sequence
     * used by IP Server.
     */
    public static final String IDGENERATOR_SEQUENCE_NAME = "ip_server_id_seq";
    
    /**
     * 
     */
    private static FileSystemServerHelper fileSystemServerHelper = null;
    
    /**
     * 
     */
    private IPServer ipServer = null;
    
    /**
     * 
     *
     */
    private FileSystemServerHelper() {
        try {
            this.ipServer = createServer();
            startServer();
        } catch (BaseException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
            ioe.printStackTrace();
        }
    }
    
    /**
     * 
     * @return
     */
    public static synchronized FileSystemServerHelper getInstance() {
        if (fileSystemServerHelper == null) {
            fileSystemServerHelper = new FileSystemServerHelper();
        }
        return fileSystemServerHelper;
    }
    
    /**
     * 
     * @throws IOException
     */
    public void startServer() throws IOException {
        if (!ipServer.isStarted()) {
            ipServer.start();
        }
    }
    
    /**
     * 
     * @return
     * @throws BaseException
     */
    private static IPServer createServer() throws BaseException {
        ConfigManager cfgMgr = ConfigManager.getInstance();
        String location = "";
        
        try {
            location = cfgMgr.getString("com.cronos.OnlineReview", "FileStorePath");
        } catch (UnknownNamespaceException e) {
            // eat the exception
        }

        // create the IPServer
        IPServer ipServer = new IPServer("127.0.0.1", 9998, 100, 0, MESSAGE_FACTORY_NAMESPACE);

        File serverFilesFile = new File(location + "files.xml");
        File serverGroupsFile = new File(location + "groups.xml");
        
        // create a file registry
        IDGenerator idGenerator = IDGeneratorFactory.getIDGenerator(IDGENERATOR_SEQUENCE_NAME);
        FileSystemRegistry registry = new FileSystemXmlRegistry(
                serverFilesFile, serverGroupsFile, idGenerator);

        // create the file persistence
        FileSystemPersistence serverPersistence = new FileSystemPersistence();

        // create the upload request validator
        ObjectValidator validator = new UploadRequestValidator(
                new FreeDiskSpaceNonNativeChecker(location));

        // creat the search manager
        SearchManager searchManager = new SearchManager();
        searchManager.addFileSearcher("regex", new RegexFileSearcher(registry));
        searchManager.addGroupSearcher("regex", new RegexGroupSearcher(registry));
        searchManager.addGroupSearcher("fileId", new FileIdGroupSearcher(registry));

        // create the handler
        FileSystemHandler fileSystemHandler = new FileSystemHandler(100, registry,
                serverPersistence, location, validator, searchManager);
        ipServer.addHandler("HanderId", fileSystemHandler);

        return ipServer;
    }
}
