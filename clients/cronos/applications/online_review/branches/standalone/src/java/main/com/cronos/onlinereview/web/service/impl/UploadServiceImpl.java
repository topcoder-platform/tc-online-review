package com.cronos.onlinereview.web.service.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.util.Arrays;

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


/**
 * Implementation of the upload service
 * 
 * @author Bauna
 */ 
public class UploadServiceImpl implements UploadService {
	
	/**
     * The GUID Generator to generate unique ids (for filenames).
     */
    private static final Generator GENERATOR = UUIDUtility.getGenerator(UUIDType.TYPEINT32);
    
    /**
	 * Adds a new submission for an user on a specific project marking as deleted the previous submussion if it exists.   
	 * 
	 * @param projectId the project's id
	 * @param ownerId the user's id
	 * @param filename the filename that will be used to store the submission. 
	 * @param submission the uploaded file
	 * @return returns 0 (zero) if the process finalizes correctly
	 * @throws IncorrectPhaseRemoteException if the submission phase is not open.
	 * @throws RemoteException if any error occurs.
	 * 
	 * @see DataHandler
	 */
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
			
			submissionPhase = ActionsHelper.getPhase(phases, true, Constants.SUBMISSION_PHASE_NAME);
			if (submissionPhase == null) {
				throw new IncorrectPhaseRemoteException("Error.IncorrectPhase");
	        }
						
			//Get the name (id) of the user performing the operations
			String operator = Long.toString(ownerId);
			Resource ownerResource = OnlineReviewHelper.findExternalUserResourceForProject(projectId, ownerId);
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
	
	/**
	 * Generates a unique filename for the submission adding an UUID prefix 
	 * 
	 * @param filename the filename to concatenate the UUID prefix
	 * @return a new filename with the UUID the prefix appended
	 */
	private String getUploadUniqueFilename(String filename) {
		return new StringBuilder(GENERATOR.getNextUUID().toString()).append('_').append(new File(filename).getName()).toString();
	}
	
	/**
	 * Stores the data obtained from the <code>InputStream</code> on file system.
	 * The file will be stored on the upload directory configured for the application. 
	 * 
	 * @param in the data to store in the file system
	 * @param filename the filename to use to store the date
	 * @return the unique file name generated to store the data
	 * @throws IOException if any error occurs reading or saving the date.
	 */
	private String copyFileToUploadDir(InputStream in, String filename) throws IOException, BaseException {
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
