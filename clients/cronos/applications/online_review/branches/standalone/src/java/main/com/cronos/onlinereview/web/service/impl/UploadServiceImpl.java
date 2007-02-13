package com.cronos.onlinereview.web.service.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import javax.activation.DataHandler;

import com.cronos.onlinereview.actions.ActionsHelper;
import com.cronos.onlinereview.actions.ConfigHelper;
import com.cronos.onlinereview.actions.Constants;
import com.cronos.onlinereview.autoscreening.management.ScreeningManager;
import com.cronos.onlinereview.commons.OnlineReviewHelper;
import com.cronos.onlinereview.web.service.UploadService;
import com.topcoder.management.deliverable.Submission;
import com.topcoder.management.deliverable.SubmissionStatus;
import com.topcoder.management.deliverable.Upload;
import com.topcoder.management.deliverable.UploadManager;
import com.topcoder.management.deliverable.UploadStatus;
import com.topcoder.management.deliverable.UploadType;
import com.topcoder.management.deliverable.search.SubmissionFilterBuilder;
import com.topcoder.management.phase.PhaseManagementException;
import com.topcoder.management.phase.PhaseManager;
import com.topcoder.management.project.ConfigurationException;
import com.topcoder.management.project.PersistenceException;
import com.topcoder.management.project.Project;
import com.topcoder.management.project.ProjectManager;
import com.topcoder.management.resource.Resource;
import com.topcoder.project.phases.Phase;
import com.topcoder.search.builder.filter.AndFilter;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.util.errorhandling.BaseException;
import com.topcoder.util.generator.guid.Generator;
import com.topcoder.util.generator.guid.UUIDType;
import com.topcoder.util.generator.guid.UUIDUtility;

public class UploadServiceImpl implements UploadService {
	
	/**
     * <p>
     * The GUID Generator to generate unique ids (for filenames).
     * </p>
     */
    private static final Generator GENERATOR = UUIDUtility.getGenerator(UUIDType.TYPEINT32);
	
	/**
	 * @throws RemoteException 
	 * @see com.cronos.onlinereview.web.service.UploadService#uploadSubmission(long, long)
	 */
	
	/*
	 
	    CorrectnessCheckResult verification =
            ActionsHelper.checkForCorrectProjectId(mapping, getResources(request), request, Constants.PERFORM_SUBM_PERM_NAME);
        // If any error has occured, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Retrieve current project
        Project project = verification.getProject();
        // Get all phases for the current project
        Phase[] phases = ActionsHelper.getPhasesForProject(ActionsHelper.createPhaseManager(request, false), project);

        if (ActionsHelper.getPhase(phases, true, Constants.SUBMISSION_PHASE_NAME) == null) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.PERFORM_SUBM_PERM_NAME, "Error.IncorrectPhase");
        }

        // Determine if this request is a post back
        boolean postBack = (request.getParameter("postBack") != null);

        if (postBack != true) {
            // Retrieve some basic project info (such as icons' names) and place it into request
            ActionsHelper.retrieveAndStoreBasicProjectInfo(request, verification.getProject(), getResources(request));
            return mapping.findForward(Constants.DISPLAY_PAGE_FORWARD_NAME);
        }

        DynaValidatorForm uploadSubmissionForm = (DynaValidatorForm) form;
        FormFile file = (FormFile) uploadSubmissionForm.get("file");

        StrutsRequestParser parser = new StrutsRequestParser();
        parser.AddFile(file);

        // Obtain an instance of File Upload Manager
        FileUpload fileUpload = ActionsHelper.createFileUploadManager(request);

        FileUploadResult uploadResult = fileUpload.uploadFiles(request, parser);
        UploadedFile uploadedFile = uploadResult.getUploadedFile("file");

        // Get my resource
        Resource resource = ActionsHelper.getMyResourceForPhase(request, null);

        // Obtain an instance of Upload Manager
        UploadManager upMgr = ActionsHelper.createUploadManager(request);
        SubmissionStatus[] submissionStatuses = upMgr.getAllSubmissionStatuses();

        Filter filterProject = SubmissionFilterBuilder.createProjectIdFilter(project.getId());
        Filter filterResource = SubmissionFilterBuilder.createResourceIdFilter(resource.getId());
        Filter filterStatus = SubmissionFilterBuilder.createSubmissionStatusIdFilter(
                ActionsHelper.findSubmissionStatusByName(submissionStatuses, "Active").getId());

        Filter filter = new AndFilter(Arrays.asList(new Filter[] {filterProject, filterResource, filterStatus}));

        Submission[] submissions = upMgr.searchSubmissions(filter);
        Submission submission = (submissions.length != 0) ? submissions[0] : null;
        Upload upload = (submission != null) ? submission.getUpload() : null;
        Upload deletedUpload = null;

        UploadStatus[] uploadStatuses = upMgr.getAllUploadStatuses();

        if (upload == null) {
            upload = new Upload();

            UploadType[] uploadTypes = upMgr.getAllUploadTypes();

            upload.setProject(project.getId());
            upload.setOwner(resource.getId());
            upload.setUploadStatus(ActionsHelper.findUploadStatusByName(uploadStatuses, "Active"));
            upload.setUploadType(ActionsHelper.findUploadTypeByName(uploadTypes, "Submission"));
            upload.setParameter(uploadedFile.getFileId());

            submission = new Submission();
            submission.setUpload(upload);
            submission.setSubmissionStatus(ActionsHelper.findSubmissionStatusByName(submissionStatuses, "Active"));
        } else {
            deletedUpload = upload;

            upload = new Upload();
            upload.setProject(deletedUpload.getProject());
            upload.setOwner(deletedUpload.getOwner());
            upload.setUploadStatus(deletedUpload.getUploadStatus());
            upload.setUploadType(deletedUpload.getUploadType());
            upload.setParameter(uploadedFile.getFileId());

            submission.setUpload(upload);

            deletedUpload.setUploadStatus(ActionsHelper.findUploadStatusByName(uploadStatuses, "Deleted"));
        }

        // Obtain an instance of Screening Manager
        ScreeningManager scrMgr = ActionsHelper.createScreeningManager(request);
        // Get the name (id) of the user performing the operations
        String operator = Long.toString(AuthorizationHelper.getLoggedInUserId(request));

        if (deletedUpload != null) {
            upMgr.updateUpload(deletedUpload, operator);
        }
        upMgr.createUpload(upload, operator);

        if (submissions.length == 0) {
            upMgr.createSubmission(submission, operator);
        } else {
            upMgr.updateSubmission(submission, operator);
        }

        scrMgr.initiateScreening(upload.getId(), operator);
	 */
    
    public int uploadTest(long projectId, long ownerId, String filename, DataHandler submissionDH) 
			throws RemoteException {
    	FileOutputStream out = null;
    	try {
			System.out.println("Project    : " + projectId);
			System.out.println("Owner      : " + ownerId);
			InputStream in = submissionDH.getInputStream();
			out = new FileOutputStream("/tmp/OR.test");
			byte[] buf = new byte[2048];
			int c = 0;
			while ((c = in.read(buf)) != -1) {
				out.write(buf, 0, c);
			}
			System.out.println("Submission : " + submissionDH.getName());
		} catch (IOException e) {
			throw new RemoteException(e.toString());
		} finally {
			System.out.println("deleting temp submission");
			if (out != null) {
				try {
					out.close();
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
			new File(submissionDH.getName()).delete();
		}
    	return 0;
    }
    
	public int uploadSubmission(long projectId, long ownerId, String filename, DataHandler submissionDH) 
			throws RemoteException {
		try {
			ProjectManager projectMgr = OnlineReviewHelper.createProjectManager();
			PhaseManager phaseMgr = OnlineReviewHelper.createPhaseManager(false);
			UploadManager uploadMgr = OnlineReviewHelper.createUploadManager();
			
			Project project = projectMgr.getProject(projectId);
			if (project == null) {
				throw new RemoteException("project doesn't exists");
			}
			Phase[] phases = ActionsHelper.getPhasesForProject(phaseMgr, project);
			Phase submissionPhase = null;
//			for (int i = 0; i < phases.length; i++) {
//				Phase p = phases[i];
//				System.out.println("id: " + p.getId() + " type: " + p.getPhaseType().getName() + " date: " + p.getScheduledEndDate());
//				if (Constants.SUBMISSION_PHASE_NAME.equalsIgnoreCase(p.getPhaseType().getName())) {
//					submissionPhase = p; 
//				}
//			}
			
			submissionPhase = ActionsHelper.getPhase(phases, true, Constants.SUBMISSION_PHASE_NAME);
			if (submissionPhase == null) {
				throw new IncorrectPhaseRemoteException("Error.IncorrectPhase");
	        }
			
//			List<Filter> filters = new ArrayList<Filter>(); 
//			filters.add(ResourceFilterBuilder.createPhaseIdFilter(submissionPhase.getId()));
			//filters.add(ResourceFilterBuilder.createProjectIdFilter(projectId));
			
//			Resource[] resources = resourceMgr.searchResources(ResourceFilterBuilder.createPhaseIdFilter(submissionPhase.getId()));
//			for (int i = 0; i < resources.length; i++) {
//				Resource r = resources[i];
//				System.out.println("phase_res id: " + r.getId() + " props: " + r.getAllProperties());
//			}
//			
//			resources = resourceMgr.searchResources(ResourceFilterBuilder.createProjectIdFilter(projectId));
//			Resource myResource = null;
//			for (int i = 0; i < resources.length; i++) {
//				Resource r = resources[i];
//				if (r.getAllProperties().)
//				System.out.println("project_res id: " + r.getId() + " props: " + r.getAllProperties());
//			}
			
			Resource[] resources = OnlineReviewHelper.findResourcesByProjectAndUser(projectId, ownerId);
			if ((resources == null) || (resources.length == 0)) {
				throw new RemoteException("cannot find resources for the user: " + ownerId + " project: " + projectId);
			}
			//Get the name (id) of the user performing the operations
			String operator = Long.toString(ownerId);
			Resource ownerResource = null;
			for (int i = 0; i < resources.length && ownerResource == null; i++) {
				Resource r = resources[i];
				for (Iterator j = r.getAllProperties().entrySet().iterator(); j.hasNext();) {
					Map.Entry entry = (Map.Entry) j.next();
					if (Constants.EXTERNAL_REFERNCE_ID.equals(entry.getKey()) && operator.equals(entry.getValue())) {
						ownerResource = r;
					}
				}
			}
			if (ownerResource == null) {
				throw new RemoteException("cannot find resources for the user: " + ownerId + " project: " + projectId);
			}
			System.out.println("resource for user: " + ownerResource.getId());
			
			//get submission statuses
			SubmissionStatus[] submissionStatuses = uploadMgr.getAllSubmissionStatuses();
			
			//find last submission
			Filter filterProject = SubmissionFilterBuilder.createProjectIdFilter(project.getId());
			Filter filterResource = SubmissionFilterBuilder.createResourceIdFilter(ownerResource.getId());
			Filter filterStatus = SubmissionFilterBuilder.createSubmissionStatusIdFilter(
					ActionsHelper.findSubmissionStatusByName(submissionStatuses, "Active").getId());
			Filter filter = new AndFilter(Arrays.asList(new Filter[] { filterProject, filterResource, filterStatus }));

			Submission[] submissions = uploadMgr.searchSubmissions(filter);
			Submission submission;
			UploadType[] uploadTypes = uploadMgr.getAllUploadTypes();
			UploadStatus[] uploadStatuses = uploadMgr.getAllUploadStatuses();
			
			if (submissions.length != 0) {
				submission = submissions[0];
				Upload uploadDelete = submission.getUpload();
				uploadDelete.setUploadStatus(ActionsHelper.findUploadStatusByName(uploadStatuses, "Deleted"));
				uploadMgr.updateUpload(uploadDelete, operator);
			} else {
				submission = new Submission();
			}
			
			Upload upload = new Upload();
			upload.setProject(project.getId());
			upload.setOwner(ownerResource.getId());
			upload.setUploadStatus(ActionsHelper.findUploadStatusByName(uploadStatuses, "Active"));
			upload.setUploadType(ActionsHelper.findUploadTypeByName(uploadTypes, "Submission"));
			//upload.setParameter(uploadedFile.getFileId());
			upload.setParameter(copyFileToUploadDir(submissionDH.getInputStream(), filename));

			submission.setUpload(upload);
			submission.setSubmissionStatus(ActionsHelper.findSubmissionStatusByName(submissionStatuses, "Active"));
			
			//Obtain an instance of Screening Manager
	        ScreeningManager scrMgr = OnlineReviewHelper.createScreeningManager();
	        
	        uploadMgr.createUpload(upload, operator);
	        
	        if (submissions.length == 0) {
	        	System.out.println("before createSubmission - operator: " + operator);
	            uploadMgr.createSubmission(submission, operator);
	            System.out.println("after createSubmission - submission id: " + submission.getId());
	        } else {
	            uploadMgr.updateSubmission(submission, operator);
	        }
	        scrMgr.initiateScreening(upload.getId(), operator);
		} catch (ConfigurationException e) {
			e.printStackTrace(System.out);
			throw new RemoteException(e.getMessage(), e);
		} catch (PersistenceException e) {
			e.printStackTrace(System.out);
			throw new RemoteException(e.getMessage(), e);
		} catch (PhaseManagementException e) {
			e.printStackTrace(System.out);
			throw new RemoteException(e.getMessage(), e);
		} catch (BaseException e) {
			e.printStackTrace(System.out);
			throw new RemoteException(e.getMessage(), e);
		} catch (IOException e) {
			e.printStackTrace(System.out);
			throw new RemoteException(e.getMessage(), e);
		} catch (Throwable e) {
			e.printStackTrace(System.out);
			throw new RemoteException(e.getMessage(), e);
		} finally {
			System.out.println("deleting attach: " + submissionDH.getName());
			new File(submissionDH.getName()).delete();
		}
		return 0; 
	}
	
	private String getUploadUniqueFilename(String filename) {
		return new StringBuilder(GENERATOR.getNextUUID().toString()).append('_').append(new File(filename).getName()).toString();
	}
	
	private String copyFileToUploadDir(InputStream in, String filename) throws IOException {
		String uploadDir = ConfigHelper.getLocalUploadDirectory() + '/';
		System.out.println("Obtaining upload filename");
		String uploadFilename = getUploadUniqueFilename(filename);
		while (new File(uploadDir + uploadFilename).exists()) {
			uploadFilename = getUploadUniqueFilename(filename);
		} 
		System.out.println("submission filename: " + uploadFilename); 
		OutputStream out = null;
		try {
			out = new BufferedOutputStream(new FileOutputStream(uploadDir + uploadFilename));
			int l = 0;
			byte[] buff = new byte[2048];
			while ((l = in.read(buff)) != -1) {
				out.write(buff, 0, l);
			}
			return uploadFilename;
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}
}
