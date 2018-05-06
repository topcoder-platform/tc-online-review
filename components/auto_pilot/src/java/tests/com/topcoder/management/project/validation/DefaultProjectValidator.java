/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.management.project.validation;

import com.topcoder.management.project.Project;
import com.topcoder.management.project.ProjectValidator;
import com.topcoder.management.project.ValidationException;

/**
 * This is the default implementation of the ProjectValidator interface to provide project
 * validation functions.
 * <p>
 * It validates the project base on the following rules:
 * </p>
 * <ul>
 * <li>Project type/status/category name: Length must be less than 64</li>
 * <li>Project type/status/category description: Length must be less than 256</li>
 * <li>Project property key: Length must be less than 64</li>
 * <li>Project property value: Length must be less than 4096</li>
 * </ul>
 * <p>
 * Thread safety: This class is immutable and thread safe.
 * </p>
 * @author TCSDESIGNER, TCSDEVELOPER
 * @version 1.0
 */
public class DefaultProjectValidator implements ProjectValidator {

    /**
     * Create a new instance of DefaultProjectValidator. This class does not have any configuration
     * settings. But the namespace parameter is provided to comply with the contract defined in
     * ScorecardValidator interface.
     * <p>
     * Implementation notes:
     * </p>
     * <ul>
     * <li>This is a blank constructor.</li>
     * </ul>
     * @param namespace The namespace to load configuration settings.
     */
    public DefaultProjectValidator(String namespace) {
        // your code here
    }

    /**
     * Validate the given project base on the rules defined in the class documentation. This method
     * will throw ValidationException on the first problem it found. The exception should contains
     * meaningful error message about the validation problem.
     * <p>
     * Implementation notes:
     * </p>
     * <ul>
     * <li>Examine the input project instance</li>
     * <li>Use the rules defined in class documentation to check items</li>
     * </ul>
     * @throws ValidationException with meaningful message for the first problem found.
     * @param project The project to validate.
     * @throws ValidationException if validation fails.
     */
    public void validateProject(Project project) throws ValidationException {
        // your code here
    }
}
