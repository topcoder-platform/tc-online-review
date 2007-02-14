package com.cronos.onlinereview.web.service.impl;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.cronos.onlinereview.actions.ActionsHelper;
import com.cronos.onlinereview.actions.Constants;
import com.cronos.onlinereview.commons.OnlineReviewHelper;
import com.cronos.onlinereview.external.ConfigException;
import com.cronos.onlinereview.external.ExternalUser;
import com.cronos.onlinereview.external.RetrievalException;
import com.cronos.onlinereview.external.UserRetrieval;
import com.cronos.onlinereview.web.service.UsersService;
import com.topcoder.management.project.ConfigurationException;
import com.topcoder.management.project.PersistenceException;
import com.topcoder.management.project.Project;
import com.topcoder.management.resource.NotificationType;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.resource.ResourceManager;
import com.topcoder.management.resource.ResourceRole;
import com.topcoder.management.resource.persistence.ResourcePersistenceException;
import com.topcoder.util.errorhandling.BaseException;

public class UsersServiceImpl implements UsersService {

	
	   private void saveResources(long projectId, long userId) throws RemoteException {
		try {
			if (OnlineReviewHelper.findExternalUserResourceForProject(projectId, userId) != null) {
				throw new RemoteException("the user id: " + userId + " already has a role in the project");
			}
			Project project = OnlineReviewHelper.createProjectManager().getProject(projectId);
			// Obtain the instance of the User Retrieval
			UserRetrieval userRetrieval = OnlineReviewHelper.createUserRetrieval();

			// Obtain the instance of the Resource Manager
			ResourceManager resourceManager = OnlineReviewHelper.createResourceManager();

			// Get all types of resource roles
			ResourceRole[] resourceRoles = resourceManager.getAllResourceRoles();

			ResourceRole submitterRole = null;
			for (int i = 0; i < resourceRoles.length && submitterRole == null; i++) {
				ResourceRole role = resourceRoles[i];
				if ("Submitter".equals(role.getName())) {
					submitterRole = role;
				}
			}

			// Get all types of notifications
			NotificationType[] types = resourceManager.getAllNotificationTypes();
			long timelineNotificationId = Long.MIN_VALUE;

			// get the id for the timelineNotification
			for (int i = 0; i < types.length; ++i) {
				if (types[i].getName().equals("Timeline Notification")) {
					timelineNotificationId = types[i].getId();
					break;
				}
			}

			// TODO: we assume timelineNotifictionId exists here, need to do the check

			// HashSet used to identify resource of new user
			Set newUsers = new HashSet();
			Set newSubmitters = new HashSet();

			// Get info about user with the specified userId
			ExternalUser user = userRetrieval.retrieveUser(userId);

			// TODO: Actually no updates should be done at all in the case validation fails!!!
			// If there is no user with such handle, indicate an error
			if (user == null) {
				throw new RemoteException("the user with id: " + userId + " doesn't exists");
			}

			Resource resource = new Resource();

			newUsers.add(new Long(user.getId()));

			// Set resource properties
			resource.setProject(new Long(project.getId()));

			ResourceRole role = ActionsHelper.findResourceRoleById(resourceRoles, 1);
			resource.setResourceRole(submitterRole);

			resource.setProperty("Handle", user.getHandle());
			resource.setProperty("Payment", null);
			resource.setProperty("Payment Status", "No");
			// Set resource properties copied from external user
			resource.setProperty(Constants.EXTERNAL_REFERENCE_ID, new Long(userId));
			resource.setProperty("Email", user.getEmail());

			//String resourceRole = resource.getResourceRole().getName();
			// If resource is a submitter, we need to store appropriate rating and reliability
			// Note, that it is done only in the case resource is added or resource role is changed
			if ("Design".equals(project.getProjectCategory().getName())) {
				resource.setProperty("Rating", user.getDesignRating());
				resource.setProperty("Reliability", user.getDesignReliability());
			} else if ("Development".equals(project.getProjectCategory().getName())) {
				resource.setProperty("Rating", user.getDevRating());
				resource.setProperty("Reliability", user.getDevReliability());
			}
			resource.setProperty("Registration Date", new Date());

			// Save the resource in the persistence level
			resourceManager.updateResource(resource, Long.toString(userId));

			newSubmitters.add(new Long(user.getId()));

			//	        for (Iterator itr = oldUsers.iterator(); itr.hasNext();) {
			//	        	Object obj = itr.next();
			//	        	newUsers.remove(obj);
			//	        	newSubmitters.remove(obj);
			//	        }

			// Populate project_result and component_inquiry for new submitters
			ActionsHelper.populateProjectResult(project, newSubmitters);

			// Update all the timeline notifications
			if ("On".equals(project.getProperty("Timeline Notification"))) {
				long[] userIds = new long[newUsers.size()];
				int i = 0;
				for (Iterator itr = newUsers.iterator(); itr.hasNext();) {
					userIds[i++] = ((Long) itr.next()).longValue();
				}

				resourceManager.addNotifications(userIds, project.getId(), timelineNotificationId, Long
						.toString(userId));
			}
		} catch (ConfigException e) {
			e.printStackTrace(System.out);
			throw new RemoteException(e.getMessage(), e);
		} catch (RetrievalException e) {
			e.printStackTrace(System.out);
			throw new RemoteException(e.getMessage(), e);
		} catch (PersistenceException e) {
			e.printStackTrace(System.out);
			throw new RemoteException(e.getMessage(), e);
		} catch (ConfigurationException e) {
			e.printStackTrace(System.out);
			throw new RemoteException(e.getMessage(), e);
		} catch (ResourcePersistenceException e) {
			e.printStackTrace(System.out);
			throw new RemoteException(e.getMessage(), e);
		} catch (BaseException e) {
			e.printStackTrace(System.out);
			throw new RemoteException(e.getMessage(), e);
		}
	}
	
	public void addSubmitter(long projectId, long userId) throws RemoteException {
		saveResources(projectId, userId);
	}

}
