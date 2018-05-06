/*
 * Copyright (C) 2006-2014 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.management.review.scorecalculator.calculators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.topcoder.management.review.scorecalculator.AbstractScoreCalculator;
import com.topcoder.management.review.scorecalculator.ConfigurationException;
import com.topcoder.management.review.scorecalculator.ScoreCalculatorException;
import com.topcoder.management.review.scorecalculator.Util;

/**
 * <p>
 * A simple score calculator for scaled items (those that accept integer answers scaled in the preconfigured
 * range).
 * </p>
 *
 * <p>
 * <b>Thread Safety</b>: This class is thread safe.
 * </p>
 *
 * @author      nicka81, UFP2161
 * @copyright   Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 * @version     1.0.4
 */
public class ScaledScoreCalculator extends AbstractScoreCalculator {

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Static Fields

    /**
     * The name of the configuration property containing the default scale.
     */
    private static final String DEFAULT_SCALE_PROPERTY_NAME = "default_scale";

    /**
     * The "uninitialized" value of the defaultScale field.
     */
    private static final long UNINITIALIZED_DEFAULT_SCALE = -1;

    /**
     * The regular expression pattern for answers that just contain a value (i.e. match "value").
     */
    private static final Pattern PATTERN_VALUE_ONLY = Pattern.compile("^(\\d+)$");

    /**
     * The regular expression pattern for answers that contain both a value and a scale (i.e. match "value/scale").
     */
    private static final Pattern PATTERN_VALUE_AND_SCALE = Pattern.compile("^(\\d+)/(\\d+)$");

    /**
     * The index of the match group in either of the regex patterns above.
     */
    private static final int MATCH_GROUP_VALUE = 1;

    /**
     * The index of the match group in the value/scale regex pattern above.
     */
    private static final int MATCH_GROUP_SCALE = 2;

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Member Fields

    /**
     * <p>
     * The default scale to use.
     * </p>
     *
     * <p>
     * <b>Invariant</b>: Must be positive or -1. A -1 indicates that no scale is preconfigured, and hence all
     * answers must contain its own scale.
     * </p>
     */
    private final long defaultScale;

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Constructors

    /**
     * <p>
     * Creates a new ScaledScoreCalculator with no preconfigured scale.
     * </p>
     *
     * <p>
     * By using this constructor, this implies that all answers must contain its own scale.
     * </p>
     */
    public ScaledScoreCalculator() {
        this.defaultScale = UNINITIALIZED_DEFAULT_SCALE;
    }

    /**
     * Creates a new ScaledScoreCalculator with the specified preconfigured scale.
     *
     * @param   defaultScale
     *          The preconfigured scale to initialize with.
     *
     * @throws  IllegalArgumentException
     *          The defaultScale is not a positive long.
     */
    public ScaledScoreCalculator(long defaultScale) {
        Util.checkNotNegative(defaultScale, "defaultScale");
        this.defaultScale = defaultScale;
    }

    /**
     * <p>
     * Creates a new ScaledScoreCalculator with the specified configuration namespace.
     * </p>
     *
     * <p>
     * This constructor reads in the optional 'default_scale' property from the given configuration namespace. If
     * the property does not exist, then the undefined value (-1) will be used.
     * </p>
     *
     * @param   namespace
     *          The configuration namespace to read the preconfigured scale setting from.
     *
     * @throws  ConfigurationException
     *          The namespace does not exist, the property exists but is not a positive or valid long.
     * @throws  IllegalArgumentException
     *          The namespace is null or an empty string.
     */
    public ScaledScoreCalculator(String namespace) throws ConfigurationException {
        // Sanity check argument.
        Util.checkNotNullOrEmpty(namespace, "namespace");

        // Attempt to read the property value as a String.
        final String defaultScalePropertyValue = Util.getOptionalProperty(namespace, DEFAULT_SCALE_PROPERTY_NAME);

        // Use default value if the property is not configured.
        if (defaultScalePropertyValue == null) {
            this.defaultScale = UNINITIALIZED_DEFAULT_SCALE;
        } else {
            // Otherwise, attempt to convert the property value to a long.
            try {
                this.defaultScale = Long.parseLong(defaultScalePropertyValue);
            } catch (NumberFormatException ex) {
                throw new ConfigurationException(
                        "The property '" + DEFAULT_SCALE_PROPERTY_NAME + "' is not a valid long.");
            }

            // Ensure property is positive.
            if (this.defaultScale <= 0) {
                throw new ConfigurationException(
                        "The property '" + DEFAULT_SCALE_PROPERTY_NAME + "' must be a positive long.");
            }
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // AbstractScoreCalculator Overridden Methods

    /**
     * <p>
     * Evalutes the given answer and returns a value between 0 and 1, inclusive.
     * </p>
     *
     * <p>
     * The answer must be in the format "value" or "value/scale", where "value" and "scale" must both be parsable
     * as positive long data types. The latter format must be used when there is no preconfigured scale. The
     * "value" must be less than or equal to the "scale" (or the preconfigured scale, if there is no "scale").
     * </p>
     *
     * @param   answer
     *          The answer to be evaluated into a score between 0 and 1, inclusive.
     *
     * @return  The score for the specified answer, which is just "value" divided by "scale" (where scale is
     *          either provided in the answer or by the default set in the constructor).
     *
     * @throws  IllegalArgumentException
     *          The answer is a null reference or an empty string.
     * @throws  ScoreCalculatorException
     *          The answer is not in a recognized format (see above) or is otherwise invalid.
     */
    protected double evaluateAnswer(String answer) throws ScoreCalculatorException {
        // Sanity check argument.
        Util.checkNotNullOrEmpty(answer, "answer");

        // Parse the answer into its individual parts.
        final String[] parts = parseValueScaleParts(answer);
        final String valueString = parts[0];
        final String scaleString = parts[1];

        // Attempt to parse the value string into a long.
        final long value;

        try {
            value = Long.parseLong(valueString);
        } catch (NumberFormatException ex) {
            throw new ScoreCalculatorException(
                    "The value for the answer '" + answer + "' is not a valid long.", ex, null, null);
        }

        // Attempt to parse the scale string into a long.
        final long scale;

        if (scaleString != null) {
            // Only parse if the second format was encountered.
            try {
                scale = Long.parseLong(scaleString);
            } catch (NumberFormatException ex) {
                throw new ScoreCalculatorException(
                        "The scale for the answer '" + answer + "' is not a valid long.", ex, null, null);
            }
        } else {
            // First format encountered; use the preconfigured scale.
            scale = defaultScale;
        }

        // Make sure the value and scale won't return something less than 0 or greater than 1. We avoid a
        // negativeness check because the regular expressions only allow digits.
        if (value > scale) {
            throw new ScoreCalculatorException(
                    "The value '" + value + "' cannot be greater than the scale '" + scale + "'.", null, null);
        } 
        if (scale == 0) {
        	if (value == 0) {
        		return 0;
        	}
            throw new ScoreCalculatorException("The scale cannot be zero.", null, null);
        }

        // Finally got all the data we need; calculate the simple result.
        return ((double) value) / ((double) scale);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Helper Methods

    /**
     * Parses the answer into its individual 'value' and 'scale' parts as need be, and returns a 2-element array
     * containing the value and scale parts as indices 0 and 1 respectively.
     *
     * @param   answer
     *          The answer that needs to be parsed into its individual parts (is non-null and non-empty).
     *
     * @return  A 2-element array, where the first element is the 'value' part, and the second element is the
     *          'scale' part (if it exists).
     *
     * @throws  ScoreCalculatorException
     *          The answer is not properly formatted.
     */
    private String[] parseValueScaleParts(String answer) throws ScoreCalculatorException {
        // The individual parts.
        final String valueString;
        final String scaleString;

        // Check to see if the answer is for the format 'value'.
        final Matcher valueOnlyMatcher = PATTERN_VALUE_ONLY.matcher(answer);

        if (valueOnlyMatcher.matches()) {
            // Ensure that the default scale was set.
            if (defaultScale == UNINITIALIZED_DEFAULT_SCALE) {
                throw new ScoreCalculatorException(
                        "The answer '" + answer + "' cannot be used because there was no preconfigured scale.",
                        null, null);
            }

            valueString = valueOnlyMatcher.group(MATCH_GROUP_VALUE);
            scaleString = null;
        } else {
            // Check to see if the answer is for the format 'value/scale'.
            final Matcher valueScaleMatcher = PATTERN_VALUE_AND_SCALE.matcher(answer);

            if (valueScaleMatcher.matches()) {
                valueString = valueScaleMatcher.group(MATCH_GROUP_VALUE);
                scaleString = valueScaleMatcher.group(MATCH_GROUP_SCALE);
            } else {
                // Answer is not properly formatted; throw an exception.
                throw new ScoreCalculatorException(
                        "The answer '"
                            + answer
                            + "' is not properly formatted as 'value' or 'value/scale', where "
                            + "value and scale are both positive integers.", null, null);
            }
        }

        // Return the result.
        return new String[] {valueString, scaleString};
    }
}
