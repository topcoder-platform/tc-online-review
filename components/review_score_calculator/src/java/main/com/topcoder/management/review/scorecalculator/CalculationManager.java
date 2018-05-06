/*
 * Copyright (C) 2006-2014 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.management.review.scorecalculator;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import com.topcoder.management.review.data.Item;
import com.topcoder.management.review.data.Review;
import com.topcoder.management.scorecard.data.Question;
import com.topcoder.management.scorecard.data.Scorecard;
import com.topcoder.util.cache.Cache;
import com.topcoder.util.cache.CacheException;
import com.topcoder.util.cache.SimpleCache;
import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.config.Property;
import com.topcoder.util.config.UnknownNamespaceException;

/**
 * <p>
 * A simple facade to the component, which provides an API to evaluate a given review item against a given
 * scorecard structure.
 * </p>
 *
 * <p>
 * To do this it manages a set of score calculators to use for review evaluation. One score calculator will be
 * registered for each used question type occurring in the scorecard. For performance reasons CalculationManager
 * may (optionally) cache intermediate results (ScorecardMatrix objects representing the result of scorecard
 * conversion to the MathMatrix compatible format).
 * </p>
 *
 * <p>
 * <b>Thread Safety</b>: This class is thread safe, because it uses a Hashtable (which is thread-safe) to store
 * calculators, and locks the ScorecardMatrix used in the getScore method (in case it is shared via the cache).
 * </p>
 *
 * @author      nicka81, UFP2161
 * @copyright   Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 * @version     1.0.4
 */
public class CalculationManager {

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Static Fields

    /**
     * The default configuration namespace, used by the default constructor.
     */
    private static final String DEFAULT_CONFIGURATION_NAMESPACE =
        "com.topcoder.management.review.scorecalculator.CalculationManager";

    /**
     * The default value of the use_caching property.
     */
    private static final boolean DEFAULT_USE_CACHING = true;

    /**
     * The configuration property name that controls whether or not caching is used.
     */
    private static final String USE_CACHING_PROPERTY_NAME = "use_caching";

    /**
     * The configuration property name that controls the name of the ScorecardMatrixBuilder class.
     */
    private static final String BUILDER_CLASS_PROPERTY_NAME = "builder_class";

    /**
     * The configuration property name that controls the configuration namespace used to create the configured
     * ScorecardMatrixBuilder class.
     */
    private static final String BUILDER_NAMESPACE_PROPERTY_NAME = "builder_namespace";

    /**
     * The configuration property name that controls how the calculators' map is initialized.
     */
    private static final String CALCULATORS_PROPERTY_NAME = "calculators";

    /**
     * The configuration property name that states which question type a particular calculator is for.
     */
    private static final String CALCULATOR_QUESTION_TYPE_PROPERTY_NAME = "question_type";

    /**
     * The configuration property name that states which calculator class a particular calculator uses.
     */
    private static final String CALCULATOR_CLASS_PROPERTY_NAME = "class";

    /**
     * The configuration property name that states which configuration namespace a particular calculator uses.
     */
    private static final String CALCULATOR_NAMESPACE_PROPERTY_NAME = "namespace";

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Static Fields

    /**
     * <p>
     * The mapping from a question type to a ScoreCalculator.
     * </p>
     *
     * <p>
     * <b>Invariant</b>: Cannot be null; keys are non-null, positive Long objects; values are non-null
     * ScoreCalculator objects.
     * </p>
     */
    private final Map calculators = new Hashtable();

    /**
     * <p>
     * The cache to use for ScorecardMatrix objects.
     * </p>
     *
     * <p>
     * If this field is null, no caching will be performed.
     * </p>
     */
    private final Cache scorecardMatrixCache;

    /**
     * <p>
     * The builder instance that will be used to convert scorecards to the format compatible with the Weighted
     * Calculator component.
     * </p>
     *
     * <p>
     * <b>Invariant</b>: Cannot be null.
     * </p>
     */
    private final ScorecardMatrixBuilder scorecardMatrixBuilder;

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Constructors

    /**
     * Creates a new CalculationManager, using the default configuration namespace.
     *
     * @throws  ConfigurationException
     *          The default configuration namespace was improperly configured.
     *
     * @see     CalculationManager#CalculationManager(String)
     */
    public CalculationManager() throws ConfigurationException {
        this(DEFAULT_CONFIGURATION_NAMESPACE);
    }

    /**
     * <p>
     * Creates a new CalculationManager, using the given configuration namespace.
     * </p>
     *
     * <p>
     * The given configuration namespace should contain the following properties:
     * <br>use_caching (optional): If true, a Simple Cache will be used to cache matrices.
     * <br>builder_class (required): The fully qualified name of the ScorecardMatrixBuilder class to use.
     * <br>builder_namespace (optional): The configuration namespace for the ScorecardMatrixBuilder.
     * <br>calculators (required): A list of calculators. Each sub-property should be configured like so:
     * <br>&nbsp;&nbsp;question_type (required): A positive long representing the question type that the calculator
     * is used for (cannot be a duplicate of another configured calculator).
     * <br>&nbsp;&nbsp;class (required): The fully qualified name of the ScoreCalculator class to use.
     * <br>&nbsp;&nbsp;namespace (optional): The configuration namespace for the ScoreCalculator.
     * </p>
     *
     * @param   namespace
     *          The configuration namespace to load settings from.
     *
     * @throws  ConfigurationException
     *          The configuration namespace was improperly configured.
     * @throws  IllegalArgumentException
     *          The namespace is null or an empty string.
     */
    public CalculationManager(String namespace) throws ConfigurationException {
        // Sanity check argument.
        Util.checkNotNullOrEmpty(namespace, "namespace");

        // Initialize member fields.
        scorecardMatrixCache = readAndCreateCache(namespace);
        scorecardMatrixBuilder = readAndCreateBuilder(namespace);

        readAndCreateCalculators(namespace);
    }

    /**
     * <p>
     * Creates a new CalculationManager, using the given arguments.
     * </p>
     *
     * <p>
     * If useCaching is true, a new SimpleCache will be used for caching matrices.
     * </p>
     *
     * @param   questionTypes
     *          The question types to initialize with.
     * @param   calculators
     *          The score calculators to initialize with.
     * @param   builder
     *          The matrix builder to initialize with.
     * @param   useCaching
     *          Whether or not caching should be performed.
     *
     * @throws  IllegalArgumentException
     *          One of the arguments is null, one of the array arguments contains a null, the questionTypes contains
     *          a non-positive value, the questionTypes array's length does not equal to calculators array's length,
     *          or questionTypes contains a duplicate value.
     */
    public CalculationManager(
            long[] questionTypes, ScoreCalculator[] calculators, ScorecardMatrixBuilder builder, boolean useCaching) {

        this(questionTypes, calculators, builder, createCache(useCaching), false);
    }

    /**
     * Creates a new CalculationManager, using the given arguments.
     *
     * @param   questionTypes
     *          The question types to initialize with.
     * @param   calculators
     *          The score calculators to initialize with.
     * @param   builder
     *          The matrix builder to initialize with.
     * @param   cache
     *          The caching mechanism to use.
     *
     * @throws  IllegalArgumentException
     *          One of the arguments is null, one of the array arguments contains a null, the questionTypes contains
     *          a non-positive value, the questionTypes array's length does not equal to calculators array's length,
     *          or questionTypes contains a duplicate value.
     */
    public CalculationManager(
            long[] questionTypes, ScoreCalculator[] calculators, ScorecardMatrixBuilder builder, Cache cache) {

        this(questionTypes, calculators, builder, cache, true);
    }

    /**
     * <p>
     * Creates a new CalculationManager, using the given arguments.
     * </p>
     *
     * <p>
     * This is a private constructor that is mainly used to reduce redundancy between the two constructors above.
     * It just adds a checkCache argument that avoids a null check for the useCaching constructor.
     * </p>
     *
     * @param   questionTypes
     *          The question types to initialize with.
     * @param   calculators
     *          The score calculators to initialize with.
     * @param   builder
     *          The matrix builder to initialize with.
     * @param   cache
     *          The caching mechanism to use.
     * @param   checkCache
     *          Whether or not the cache argument should be checked.
     *
     * @throws  IllegalArgumentException
     *          One of the arguments is null, one of the array arguments contains a null, the questionTypes contains
     *          a non-positive value, the questionTypes array's length does not equal to calculators array's length,
     *          or questionTypes contains a duplicate value.
     */
    private CalculationManager(long[] questionTypes, ScoreCalculator[] calculators, ScorecardMatrixBuilder builder,
            Cache cache, boolean checkCache) {

        // Sanity check arguments.
        Util.checkNotNonPositiveArray(questionTypes, "questionTypes");
        Util.checkNotNullArray(calculators, "calculators");
        Util.checkNotNull(builder, "builder");

        if (checkCache) {
            Util.checkNotNull(cache, "cache");
        }

        if (questionTypes.length != calculators.length) {
            throw new IllegalArgumentException(
                    "The questionTypes array's length must be equal to the calculators array's length.");
        }

        // Initialize member fields.
        for (int i = 0; i < questionTypes.length; ++i) {
            final Long key = new Long(questionTypes[i]);

            if (this.calculators.containsKey(key)) {
                throw new IllegalArgumentException(
                        "The questionTypes array's contained the same question type (" + key + ") twice.");
            }

            this.calculators.put(key, calculators[i]);
        }

        this.scorecardMatrixBuilder = builder;
        this.scorecardMatrixCache = cache;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Member Accessors

    /**
     * <p>
     * Evaluates the given review against the given scorecard and produces a score for it.
     * </p>
     *
     * <p>
     * Please refer to section 1.3.2 of the Component Specifications for a more detailed explanation of how the
     * score is generated.
     * </p>
     *
     * @param   scorecard
     *          The scorecard that the review is based off of.
     * @param   review
     *          The review to be evaluated.
     *
     * @return  The evaluated review score.
     *
     * @throws  CalculationException
     *          If anything not mentioned in the other exception descriptions occurs.
     * @throws  IllegalArgumentException
     *          The scorecard or review is a null reference.
     * @throws  ReviewStructureException
     *          The review structure is invalid.
     * @throws  ScoreCalculatorException
     *          The score calculator could not generate a review score.
     * @throws  ScorecardStructureException
     *          The scorecard structure is invalid.
     */
    public double getScore(Scorecard scorecard, Review review) throws CalculationException {

        // Sanity check arguments.
        Util.checkNotNull(scorecard, "scorecard");
        Util.checkNotNull(review, "review");

        // Grab the matrix associated with the scorecard (from cache or newly created).
        final ScorecardMatrix scMatrix = getScorecardMatrix(scorecard);
        final Set seenQuestionIds = new HashSet();

        // Generate the score.
        synchronized (scMatrix) {
            // Get the list of items.
            final Item[] items = review.getAllItems();

            if (items.length == 0) {
                throw new ReviewStructureException("The review has no items!", review);
            }

            /*if (items.length != scMatrix.getNumberOfQuestions()) {
                throw new ReviewStructureException(
                        "The review has does not have the same number of questions (" + items.length
                        + ") as the scorecard matrix (" + scMatrix.getNumberOfQuestions() + ")!", review);
            }*/

            // For each review item.
            for (int i = 0; i < items.length; ++i) {
                // Variables for readability.
                final Item item = items[i];
                final long questionId = item.getQuestion();
                final Question question = scMatrix.getQuestion(questionId);
                if (question == null) {
                    continue;
                }

                // Ensure the question is valid.
                checkForValidQuestion(question, review, i, seenQuestionIds);

                // Fetch the calculator to apply.
                final long questionTypeId = question.getQuestionType().getId();
                final ScoreCalculator calculator = getScoreCalculator(questionTypeId);

                if (calculator == null) {
                    throw new ScorecardStructureException(
                            "A calculator was not configured for the question type id " + questionTypeId + ".",
                            scorecard);
                }

                // Evaluate the score.
                final double itemScore = calculator.evaluateItem(item, question);

                // Set the line item score.
                scMatrix.getLineItem(questionId).setActualScore(itemScore);
            }

            // Return the final score.
            return (double) scMatrix.getMathMatrix().getWeightedScore();
        }
    }

    /**
     * Gets the ScoreCalculator associated with the given question type.
     *
     * @param   questionType
     *          The question type whose associated ScoreCalculator should be returned.
     *
     * @return  The ScoreCalculator associated with the given question type, or null if no ScoreCalculator is
     *          associated.
     *
     * @throws  IllegalArgumentException
     *          The questionType is not a positive long.
     */
    public ScoreCalculator getScoreCalculator(long questionType) {
        Util.checkNotNonPositive(questionType, "questionType");
        return (ScoreCalculator) calculators.get(new Long(questionType));
    }

    /**
     * Returns the ScorecardMatrix associated with the given scorecard.
     *
     * @param   scorecard
     *          The scorecard to get the ScorecardMatrix for.
     *
     * @return  The ScorecardMatrix associated with the given scorecard.
     *
     * @throws  ScorecardStructureException
     *          If caching is not used, the scorecard matrix could not be built. If caching is used, either the
     *          scorecard has some uninitialized values, or the scorecard matrix could not be built (if not
     *          already in the cache).
     */
    private ScorecardMatrix getScorecardMatrix(Scorecard scorecard) throws ScorecardStructureException {
        // Quick exit if no cache is being used.
        if (scorecardMatrixCache == null) {
            return scorecardMatrixBuilder.buildScorecardMatrix(scorecard);
        }

        // Sanity check the arguments.
        if ((scorecard.getName() == null) || Util.isEmpty(scorecard.getName())) {
            throw new ScorecardStructureException("The scorecard has an uninitialized name.", scorecard);
        }

        if ((scorecard.getVersion() == null) || Util.isEmpty(scorecard.getVersion())) {
            throw new ScorecardStructureException("The scorecard has an uninitialized version.", scorecard);
        }

        final String cacheKey;

        try {
            cacheKey = scorecard.getId() + "," + scorecard.getName() + "," + scorecard.getVersion();
        } catch (IllegalStateException ex) {
            throw new ScorecardStructureException("The scorecard has an uninitialized identifier.", ex, scorecard);
        }

        // Lookup the scorecard matrix from the cache (lock to prevent creating a matrix twice).
        ScorecardMatrix scMatrix;

        synchronized (scorecardMatrixCache) {
            scMatrix = (ScorecardMatrix) scorecardMatrixCache.get(cacheKey);

            if (scMatrix == null) {
                scMatrix = scorecardMatrixBuilder.buildScorecardMatrix(scorecard);

                try {
                    scorecardMatrixCache.put(cacheKey, scMatrix);
                } catch (CacheException ex) {
                    throw new ScorecardStructureException(
                            "The scorecard matrix could not be cached properly", ex, scorecard);
                }
            }
        }

        // Return the (now) cached matrix.
        return scMatrix;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Member Mutators

    /**
     * <p>
     * Associates the given ScoreCalculator with the given question type.
     * </p>
     *
     * <p>
     * If an association has already been made, the original association will be overwritten by this one being
     * requested.
     * </p>
     *
     * @param   questionType
     *          The question type being associated.
     * @param   scoreCalculator
     *          The calculator being associated.
     *
     * @throws  IllegalArgumentException
     *          The questionType is not a positive long, or the scoreCalculator is null.
     */
    public void addScoreCalculator(long questionType, ScoreCalculator scoreCalculator) {
        Util.checkNotNonPositive(questionType, "questionType");
        Util.checkNotNull(scoreCalculator, "scoreCalculator");

        calculators.put(new Long(questionType), scoreCalculator);
    }

    /**
     * Removes the ScoreCalculator associated with the given question type, and returns the ScoreCalculator.
     *
     * @param   questionType
     *          The question type being disassociated.
     *
     * @return  The ScoreCalculator that was disassociated, or null if no association for the given questionType
     *          was previously made.
     *
     * @throws  IllegalArgumentException
     *          The questionType is not a positive long.
     */
    public ScoreCalculator removeScoreCalculator(long questionType) {
        Util.checkNotNonPositive(questionType, "questionType");
        return (ScoreCalculator) calculators.remove(new Long(questionType));
    }

    /**
     * Clears all associations between question types and score calculators.
     */
    public void clearScoreCalculators() {
        calculators.clear();
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Static Helper Methods - Constructor Helpers

    /**
     * Creates and returns a new SimpleCache (if needed).
     *
     * @param   useCaching
     *          Whether or not a caching mechanism should be used.
     *
     * @return  A new SimpleCache if useCaching is true; null otherwise.
     */
    private static Cache createCache(boolean useCaching) {
        return useCaching ? new SimpleCache() : null;
    }

    /**
     * Reads the use_caching property from the given namespace, and returns the caching mechanism that should
     * be used.
     *
     * @param   namespace
     *          The configuration namespace to load settings from.
     *
     * @return  The caching mechanism to be used, or null if no caching mechanism was configured.
     *
     * @throws  ConfigurationException
     *          The configuration namespace is missing.
     */
    private static Cache readAndCreateCache(String namespace) throws ConfigurationException {
        // Attempt to read the optional use_caching property.
        final String useCachingPropertyValue = Util.getOptionalProperty(namespace, USE_CACHING_PROPERTY_NAME);

        // Convert the string (if set) to a boolean; otherwise use a default.
        final boolean useCaching;

        if (useCachingPropertyValue == null) {
            useCaching = DEFAULT_USE_CACHING;
        } else {
            useCaching = Boolean.getBoolean(useCachingPropertyValue);
        }

        // Create a cache, if needed.
        return createCache(useCaching);
    }

    /**
     * Reads the builder configuration from the given namespace, and returns the configured builder instance.
     *
     * @param   namespace
     *          The configuration namespace to load settings from.
     *
     * @return  The ScorecardMatrixBuilder to be used by the new instance.
     *
     * @throws  ConfigurationException
     *          The configuration namespace is missing, the builder_class property is missing or an empty string
     *          (trimmed), the builder_namespace is configured but an empty string (trimmed), or the configured
     *          builder cannot be created for various reasons.
     */
    private static ScorecardMatrixBuilder readAndCreateBuilder(String namespace) throws ConfigurationException {
        final String builderClassPropertyValue = Util.getRequiredProperty(namespace, BUILDER_CLASS_PROPERTY_NAME);
        final Class builderClass = getClass(builderClassPropertyValue, ScorecardMatrixBuilder.class);
        final String builderNSPropertyValue = Util.getOptionalProperty(namespace, BUILDER_NAMESPACE_PROPERTY_NAME);

        return (ScorecardMatrixBuilder) createInstance(builderClass, builderNSPropertyValue);
    }

    /**
     * Creates an instance of the given clas, using the given configuration namespace.
     *
     * @param   theClass
     *          The class to be created.
     * @param   namespace
     *          The configuration namespace to use (may be null).
     *
     * @return  A new instance of the specified class, created using the proper constructor.
     *
     * @throws  ConfigurationException
     *          The constructor for the given class could not be found, or an error occurred during the creation
     *          of the new class instance.
     */
    private static Object createInstance(Class theClass, String namespace) throws ConfigurationException {
        // Attempt to grab the appropriate constructor.
        final Constructor ctor;
        final Object[] ctorArgs;

        try {
            if (namespace == null) {
                ctor = theClass.getConstructor((Class<?>[]) null);
                ctorArgs = null;
            } else {
                ctor = theClass.getConstructor(new Class[] {String.class});
                ctorArgs = new Object[] {namespace};
            }
        } catch (NoSuchMethodException ex) {
            throw new ConfigurationException(
                    "The constructor for '" + theClass.toString() + "' could not be found.", ex);
        } catch (SecurityException ex) {
            throw new ConfigurationException(
                    "The constructor for '" + theClass.toString() + "' caused a security error.", ex);
        }

        // Attempt to invoke the constructor.
        try {
            return ctor.newInstance(ctorArgs);
        } catch (IllegalAccessException ex) {
            throw new ConfigurationException(
                    "The constructor for '" + theClass.toString() + "' is inaccessible.", ex);
        } catch (IllegalArgumentException ex) {
            throw new ConfigurationException(
                    "The constructor for '" + theClass.toString()
                    + "' used illegal arguments (this should not occur).", ex);
        } catch (InstantiationException ex) {
            throw new ConfigurationException(
                    "The constructor for '" + theClass.toString() + "' is for an abstract class.", ex);
        } catch (InvocationTargetException ex) {
            throw new ConfigurationException(
                    "The constructor for '" + theClass.toString() + "' could not be invoked properly.", ex);
        } catch (ExceptionInInitializerError ex) {
            throw new ConfigurationException(
                    "The constructor for '" + theClass.toString() + "' failed class initialization.", ex);
        }
    }

    /**
     * Gets a Class object for the given class name, that must derive from the interface expectedBaseClass.
     *
     * @param   className
     *          The fully qualified name of the class to get.
     * @param   expectedBaseClass
     *          The expected interface of the class being retrieved.
     *
     * @return  The Class object for the given class name.
     *
     * @throws  ConfigurationException
     *          The className does not exist, could not initialized, does not derive from the interface class,
     *          or is the interface class.
     */
    private static Class getClass(String className, Class expectedBaseClass) throws ConfigurationException {
        // First, attempt to get the class.
        final Class theClass;

        try {
            theClass = Class.forName(className);
        } catch (ClassNotFoundException ex) {
            throw new ConfigurationException("Class '" + className + "' does not exist.");
        } catch (ExceptionInInitializerError ex) {
            throw new ConfigurationException("Initialization for class '" + className + "' failed.");
        } catch (LinkageError ex) {
            throw new ConfigurationException("Linkage for class '" + className + "' failed.");
        }

        // Then, verfiy the class hierarchy.
        if (expectedBaseClass.equals(theClass) || !expectedBaseClass.isAssignableFrom(theClass)) {
            throw new ConfigurationException("Class '" + className
                    + "' does not derive from (or is) class '" + expectedBaseClass.toString() + "'.");
        }

        // Return the class.
        return theClass;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Static Helper Methods - Checkers

    /**
     * Ensures that the given question is valid (i.e. not null, has a valid id, has a valid type and id, and was
     * not seen before).
     *
     * @param   question
     *          The question to be checked.
     * @param   review
     *          The review that is currently being scored.
     * @param   itemIndex
     *          The index of the item that is currently being scored.
     * @param   seenQuestionIds
     *          The set of previously seen question identifiers.
     *
     * @throws  ReviewStructureException
     *          The question is null, does not have a valid id, does not have a type set, does not have a valid
     *          type id, or the question's id was seen previously during this scoring.
     */
    private static void checkForValidQuestion(Question question, Review review, int itemIndex, Set seenQuestionIds)
        throws ReviewStructureException {

        // Check that question exists.
        if (question == null) {
            throw new ReviewStructureException("No question associated with item index " + itemIndex, review);
        }

        // Check that question id is not a duplicate.
        try {
            if (seenQuestionIds.contains(new Long(question.getId()))) {
                throw new ReviewStructureException(
                        "The question id associated with item index " + itemIndex + " was already seen.", review);
            }
        } catch (IllegalStateException ex) {
            throw new ReviewStructureException(
                    "The question id associated with item index " + itemIndex + " was not initialized.", ex, review);
        }

        // Check the question type was initialized.
        if (question.getQuestionType() == null) {
            throw new ReviewStructureException(
                    "The question associated with item index " + itemIndex + " has no question type.", review);
        }

        // Check the question type's id was initialized.
        try {
            question.getQuestionType().getId();
        } catch (IllegalStateException ex) {
            throw new ReviewStructureException(
                    "The question type associated with item index " + itemIndex + " has an uninitialized id.",
                    review);
        }

        // Question looks good; add it to the set.
        seenQuestionIds.add(new Long(question.getId()));
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Member Helper Methods - Constructor Helpers

    /**
     * <p>
     * Reads all the configured calculators from the given namespace.
     * </p>
     *
     * <p>
     * This member method is called in the namespace constructor, and assumes that the calculators member field
     * has already been initialized.
     * </p>
     *
     * @param   namespace
     *          The configuration namespace to load settings from.
     *
     * @throws  ConfigurationException
     *          The namespace is missing, the calculators property is missing, or one of the calculators is
     *          misconfigured.
     */
    private void readAndCreateCalculators(String namespace) throws ConfigurationException {
        // Attempt to read the calculators property.
        final Property property;

        try {
            property = ConfigManager.getInstance().getPropertyObject(namespace, CALCULATORS_PROPERTY_NAME);
        } catch (UnknownNamespaceException ex) {
            throw new ConfigurationException("The namespace '" + namespace + "' does not exist.", ex);
        }

        if (property == null) {
            throw new ConfigurationException("The property '" + CALCULATORS_PROPERTY_NAME
                    + "' does not exist in the namespace '" + namespace + "'.");
        }

        // Attempt to create each configured calculator.
        final Enumeration propertyNames = property.propertyNames();

        while (propertyNames.hasMoreElements()) {
            readAndCreateCalculator(property.getProperty((String) propertyNames.nextElement()));
        }
    }

    /**
     * Reads the configured calculator from the given property.
     *
     * @param   calculatorProperty
     *          The calculator property that needs to be read.
     *
     * @throws  ConfigurationException
     *          The calculator is missing a question_type, the question_type is not a valid long, the
     *          question_type is a duplicate of a previous calculator, the question_type is not a positve long,
     *          the class is missing (or an empty string), the namespace is configured, but an empty string, or
     *          the calculator class cannot be created for various reasons.
     */
    private void readAndCreateCalculator(Property calculatorProperty) throws ConfigurationException {
        // Read the question_type sub-property.
        final long questionType = readQuestionType(calculatorProperty);

        // Read the class sub-property.
        final String calculatorClassPropertyValue = calculatorProperty.getValue(CALCULATOR_CLASS_PROPERTY_NAME);

        if (calculatorClassPropertyValue == null) {
            throw new ConfigurationException("The sub-property '" + CALCULATOR_CLASS_PROPERTY_NAME
                    + "' was not configured for the calculator '" + calculatorProperty.getName() + "'.");
        } else if (Util.isEmpty(calculatorClassPropertyValue)) {
            throw new ConfigurationException("The sub-property '" + CALCULATOR_CLASS_PROPERTY_NAME
                    + "' cannot be an empty string for the calculator '" + calculatorProperty.getName() + "'.");
        }

        final Class calculatorClass = getClass(calculatorClassPropertyValue, ScoreCalculator.class);

        // Read the namespace sub-property.
        final String calculatorNSPropertyValue = calculatorProperty.getValue(CALCULATOR_NAMESPACE_PROPERTY_NAME);

        // Attempt to create the instance and add it (don't really need to cast the value, but we do it as a
        // precaution in case the createInstance method created some other interface).
        calculators.put(
                new Long(questionType),
                (ScoreCalculator) createInstance(calculatorClass, calculatorNSPropertyValue));
    }

    /**
     * Reads the question_type sub-property from the given proeprty.
     *
     * @param   calculatorProperty
     *          The calculator property that needs to be read.
     *
     * @return  The configured question type.
     *
     * @throws  ConfigurationException
     *          The question_type is missing, not a valid long, a duplicate of some previous calculator's question
     *          type, or is not a positive long.
     */
    private long readQuestionType(Property calculatorProperty) throws ConfigurationException {
        // Attempt to read the question_type sub-property.
        final String questionTypePropertyValue = calculatorProperty.getValue(CALCULATOR_QUESTION_TYPE_PROPERTY_NAME);

        if (questionTypePropertyValue == null) {
            throw new ConfigurationException("The sub-property '" + CALCULATOR_QUESTION_TYPE_PROPERTY_NAME
                    + "' was not configured for the calculator '" + calculatorProperty.getName() + "'.");
        }

        // Attempt to parse the question_type property into a long.
        final long questionType;

        try {
            questionType = Long.parseLong(questionTypePropertyValue);
        } catch (NumberFormatException ex) {
            throw new ConfigurationException("The sub-property '" + CALCULATOR_QUESTION_TYPE_PROPERTY_NAME
                    + "' for the calculator '" + calculatorProperty.getName() + "' is not a valid long.", ex);
        }

        // Ensure that the question type is a positive integer.
        if (questionType <= 0) {
            throw new ConfigurationException("The sub-property '" + CALCULATOR_QUESTION_TYPE_PROPERTY_NAME
                    + "' for the calculator '" + calculatorProperty.getName() + "' must be a positive long.");
        }

        // Ensure that the question type has not yet been seen yet.
        if (calculators.containsKey(new Long(questionType))) {
            throw new ConfigurationException("The sub-property '" + CALCULATOR_QUESTION_TYPE_PROPERTY_NAME
                    + "' for the calculator '" + calculatorProperty.getName() + "' was already configured.");
        }

        // Good question type; return it!
        return questionType;
    }
}
