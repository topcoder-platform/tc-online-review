/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.onlinereview.migration;

import java.util.Collection;
import java.util.Iterator;

import com.topcoder.onlinereview.migration.dto.newschema.project.ProjectNew;
import com.topcoder.onlinereview.migration.dto.oldschema.ProjectOld;
import com.topcoder.onlinereview.migration.dto.oldschema.ScorecardOld;
import com.topcoder.onlinereview.migration.dto.oldschema.deliverable.SubmissionOld;
import com.topcoder.onlinereview.migration.dto.oldschema.review.AggResponse;
import com.topcoder.onlinereview.migration.dto.oldschema.review.ScorecardQuestion;
import com.topcoder.onlinereview.migration.dto.oldschema.review.SubjectiveResp;
import com.topcoder.util.log.Level;
import com.topcoder.util.log.Log;
import com.topcoder.util.log.LogFactory;

/**
 * The test of LogDebug.
 *
 * @author brain_cn
 * @version 1.0
 */
public class LogDebug {
	private static Log logger = LogFactory.getLog();
	static void log(ProjectOld instance) {
		logInstance(instance.getCompVersionDates(), "getCompVersionDates");
		logInstance(instance.getAggWorksheet(), "getAggWorksheet");
		logInstance(instance.getCompCatalog(), "getCompCatalog");
		logInstance(instance.getCompForumXref(), "getCompForumXref");
		logInstance(instance.getCompVersionDates(), "getCompVersionDates");
		logInstance(instance.getCompVersions(), "getCompVersions");
		logInstance(instance.getModifiyReasons(), "getModifiyReasons");
		logInstance(instance.getTestcases(), "getTestcases");
		logInstance(instance.getSubmissions(), "getSubmissions");
		logInstance(instance.getRUserRoles(), "getRUserRoles");
		logInstance(instance.getRboardApplications(), "getRboardApplications");
		logInstance(instance.getProjectResults(), "getProjectResults");
		logInstance(instance.getPhaseInstances(), "getPhaseInstances");
	}

	private static void logInstance(Object col, String text) {
		if (col instanceof Collection) {
			logCollection((Collection) col, text);
		} else {
			logger.log(Level.DEBUG, text + " : " + col);
		}
	}

	static void logCollection(Collection col, String text) {
		logger.log(Level.DEBUG, text + " size: " + ((Collection) col).size());
		for (Iterator iter = col.iterator(); iter.hasNext();) {
			Object instance = iter.next();
			if (instance instanceof ScorecardOld) {
				log((ScorecardOld) instance);
			} else if (instance instanceof ScorecardQuestion) {
				log((ScorecardQuestion) instance);
			} else if (instance instanceof SubjectiveResp) {
				log((SubjectiveResp) instance);
			} else if (instance instanceof AggResponse) {
				log((AggResponse) instance);
			} else if (instance instanceof SubmissionOld) {
				log((SubmissionOld) instance);
			}
		}
	}

	static void log(SubmissionOld instance) {
		logCollection(instance.getScorecards(), "SubmitterId:" + instance.getSubmitterId() + ", getScorecards");
	}

	static void log(ScorecardOld instance) {
		logger.log(Level.DEBUG, "scorecard, id: " + instance.getScorecardId() + " hasAggResponse: " + instance.hasAggResponse() + " hasFinalFix: " 
				+ instance.hasFinalFix());
		logCollection(instance.getScorecardQuestions(), "submission_id:" + instance.getSubmissionId() + ", getScorecardQuestions");
	}

	static void log(ScorecardQuestion instance) {
		logCollection(instance.getSubjectiveResps(), "ScorecardId:" + instance.getScorecardId() + ", getSubjectiveResps");
	}

	static void log(SubjectiveResp instance) {
		logInstance(instance.getAggResponse(), "QuestionId:" + instance.getQuestionId() + ", getAggResponses");
	}

	static void log(AggResponse instance) {
		logInstance(instance.getFixItem(), "AggResponseId:" + instance.getAggResponseId() + ", getAggResponses");
	}
	
	static void log(ProjectNew instance) {
		logInstance(instance.getPhaseDependencys(), "getPhaseDependencys");
		logInstance(instance.getPhases(), "getPhases");
		logInstance(instance.getProjectAudits(), "getProjectAudits");
		logInstance(instance.getResourceInfos(), "getResourceInfos");
		logInstance(instance.getProjectInfos(), "getProjectInfos");
		logInstance(instance.getResources(), "getResources");
		logInstance(instance.getScreeningTasks(), "getScreeningTasks");
		logInstance(instance.getSubmissions(), "getSubmissions");
		logInstance(instance.getUploads(), "getUploads");	
	}
}
