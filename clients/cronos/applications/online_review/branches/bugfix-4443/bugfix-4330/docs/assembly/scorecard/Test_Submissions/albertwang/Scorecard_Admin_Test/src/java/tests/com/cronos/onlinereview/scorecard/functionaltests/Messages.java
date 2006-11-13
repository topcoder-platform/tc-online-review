/*
 * Copyright (C) 2006 TopCoder, Inc. All Rights Reserved.
 */
package com.cronos.onlinereview.scorecard.functionaltests;

/**
 * The messages used in the application. They are looked up from configuration.
 *
 * @author TCSTESTER
 * @version 1.0
 */
public class Messages {

	/**
	 * The configuration interface.
	 */
	private static Configuration config = new Configuration(Messages.class
			.getName());

	/**
	 * Private constructor.
	 */
	private Messages() {
	}

	/**
	 * <p>
	 * Return the error message indicating that "the user has no permission to access the scorecard list".
	 * </p>
	 * @return the error message indicating that "the user has no permission to access the scorecard list".
	 */
	public static String getNoPermissionScorecardList() {
		return config.getProperty("scorecard_list.no_permission");
	}

	/**
	 * <p>
	 * Return the error message indicating that "the user has no permission to access the scorecard".
	 * </p>
	 * @return the error message indicating that "the user has no permission to access the scorecard".
	 */
	public static String getNoPermissionScorecard() {
		return config.getProperty("scorecard.no_permission");
	}

	/**
	 * <p>
	 * Return the error message indicating that "the user has no permission to edit the scorecard".
	 * </p>
	 * @return the error message indicating that "the user has no permission to edit the scorecard".
	 */
	public static String getNoPermissionEditScorecard() {
		return config.getProperty("scorecard.no_edit_permission");
	}

	/**
	 * <p>
	 * Return the error message indicating that "the user has no permission to copy the scorecard".
	 * </p>
	 * @return the error message indicating that "the user has no permission to copy the scorecard".
	 */
	public static String getNoPermissionCopyScorecard() {
		return config.getProperty("scorecard.no_copy_permission");
	}

	/**
	 * <p>
	 * Return the error message indicating that "the user has no permission to create the scorecard".
	 * </p>
	 * @return the error message indicating that "the user has no permission to create the scorecard".
	 */
	public static String getNoPermissionCreateScorecard() {
		return config.getProperty("scorecard.no_create_permission");
	}

	/**
	 * <p>
	 * Return the error message indicating that "the scorecard name exists".
	 * </p>
	 * @return the error message indicating that "the scorecard name exists".
	 */
	public static String getValidationErrorScorecardNameExists() {
		return config.getProperty("scorecard.name_exists");
	}

	/**
	 * <p>
	 * Return the error message indicating that "The sum of group weights is not 100.".
	 * </p>
	 * @return the error message indicating that "The sum of group weights is not 100.".
	 */
	public static String getValidationErrorIncorrectGroupWeights() {
		return config.getProperty("scorecard.incorrect_group_weights");
	}

	/**
	 * <p>
	 * Return the error message indicating that "The sum of section weights is not 100.".
	 * </p>
	 * @return the error message indicating that "The sum of section weights is not 100.".
	 */
	public static String getValidationErrorIncorrectSectionWeights() {
		return config.getProperty("scorecard.incorrect_section_weights");
	}

	/**
	 * <p>
	 * Return the error message indicating that "The sum of question weights is not 100.".
	 * </p>
	 * @return the error message indicating that "The sum of question weights is not 100.".
	 */
	public static String getValidationErrorIncorrectQuestionWeights() {
		return config.getProperty("scorecard.incorrect_question_weights");
	}

	/**
	 * <p>
	 * Return the error message indicating that "Required field is missing.".
	 * </p>
	 * @return the error message indicating that "Required field is missing.".
	 */
	public static String getValidationErrorFieldMissing() {
		return config.getProperty("scorecard.filed_missing");
	}

	/**
	 * <p>
	 * Return the error message indicating that "The version is invalid.".
	 * </p>
	 * @return the error message indicating that "The version is invalid.".
	 */
	public static String getValidationErrorInvalidVersion() {
		return config.getProperty("scorecard.invalid_version");
	}

	/**
	 * <p>
	 * Return the error message indicating that "The active scorecard can not be edited.".
	 * </p>
	 * @return the error message indicating that "The active scorecard can not be edited.".
	 */
	public static String getActiveScorecardEditError() {
		return config.getProperty("scorecard.active_scorecard_edit_error");
	}

	/**
	 * <p>
	 * Return the error message indicating that "The scorecard is currently linked to a project, the name must be changed.".
	 * </p>
	 * @return the error message indicating that "The scorecard is currently linked to a project, the name must be changed.".
	 */
	public static String getScorecardInUseCopyError() {
		return config.getProperty("scorecard.scorecard_in_use_copy_error");
	}
}