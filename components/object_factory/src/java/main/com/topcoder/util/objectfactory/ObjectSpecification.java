/*
 * Copyright (C) 2006, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.objectfactory;

/**
 * <p>
 * The object that contains the specification, or the metadata, that the
 * ObjectFactory will use to instantiate objects. This object will be created in
 * the {@link SpecificationFactory}. The ConfigManagerSpecificationFactory
 * creates these from configuration and stores them in a map.
 * </p>
 * <p>
 * <strong>Changes in 2.2:</strong>
 * <ol>
 * <li>Fix the java doc of {@link #getIdentifier()} and {@link #getValue()}.</li>
 * </ol>
 * </p>
 * <p>
 * <strong>Thread Safety:</strong> This class is almost immutable, with only the
 * getParams method exposing the inner array structure, but since this class is
 * meant to be used internally, this is not a concern. This class is thread
 * safe.
 * </p>
 *
 * @author codedoc, saarixx, mgmg, TCSDEVELOPER
 * @version 2.2
 */
public class ObjectSpecification {
    /**
     * <p>
     * Represents a spec type of simple. Used in the specType member if the
     * types will be primitive or a String.
     * </p>
     */
    public static final String SIMPLE_SPECIFICATION = "simple";

    /**
     * <p>
     * Represents a spec type of array. Used in the specType member if the types
     * will be arrays.
     * </p>
     */
    public static final String ARRAY_SPECIFICATION = "array";

    /**
     * <p>
     * Represents a spec type of complex. Used in the specType member if the
     * types will be Objects but not arrays and not String.
     * </p>
     */
    public static final String COMPLEX_SPECIFICATION = "complex";

    /**
     * <p>
     * Represents a simple type of boolean. Used in the type member if the type
     * is a primitive boolean.
     * </p>
     */
    public static final String BOOLEAN = "boolean";

    /**
     * <p>
     * Represents a simple type of byte. Used in the type member if the type is
     * a primitive byte.
     * </p>
     */
    public static final String BYTE = "byte";

    /**
     * <p>
     * Represents a simple type of char. Used in the type member if the type is
     * a primitive char.
     * </p>
     */
    public static final String CHAR = "char";

    /**
     * <p>
     * Represents a simple type of double. Used in the type member if the type
     * is a primitive double.
     * </p>
     */
    public static final String DOUBLE = "double";

    /**
     * <p>
     * Represents a simple type of float. Used in the type member if the type is
     * a primitive float.
     * </p>
     */
    public static final String FLOAT = "float";

    /**
     * <p>
     * Represents a simple type of int. Used in the type member if the type is a
     * primitive int.
     * </p>
     */
    public static final String INT = "int";

    /**
     * <p>
     * Represents a simple type of long. Used in the type member if the type is
     * a primitive long.
     * </p>
     */
    public static final String LONG = "long";

    /**
     * <p>
     * Represents a simple type of short. Used in the type member if the type is
     * a primitive short.
     * </p>
     */
    public static final String SHORT = "short";

    /**
     * <p>
     * Represents a simple type of String. Used in the type member if the type
     * is a java.lang.String.
     * </p>
     */
    public static final String STRING = "String";

    /**
     * <p>
     * Represents an alternate identifier for a simple type of String. Used in
     * the type member if the type is a java.lang.String, so it is not treated
     * as a complex type.
     * </p>
     */
    public static final String STRING_FULL = "java.lang.String";

    /**
     * <p>
     * Represents a spec type of null. Used in the specType member if the types
     * will be a null Object.
     * </p>
     */
    public static final String NULL_SPECIFICATION = "null";

    /**
     * <p>
     * Represents a spec type. Will be one of the four specified types: simple,
     * complex, array, or null. Set in the constructor. and will not be null or
     * change.
     * </p>
     */
    private final String specType;

    /**
     * <p>
     * Represents a jar name and path. Used by the ObjectFactory to specify
     * which JAR to use to load the class. If null, then it is ignored. Set in
     * the constructor and will not change.
     * </p>
     */
    private final String jar;

    /**
     * <p>
     * Represents a fully-qualified name of Object or name of supported simple
     * type. If simple, it will be one of the ten specified types. Set in the
     * constructor. Will not be null or change.
     * </p>
     */
    private final String type;

    /**
     * <p>
     * Represents an additional identifier to an object specification. Set in
     * the constructor and will not change. Can be any value, including null.
     * </p>
     */
    private final String identifier;

    /**
     * <p>
     * Represents the value of a simple object, and only a simple object.
     * Complex and array specTypes are not allowed to set this param, and must
     * be set to null. Otherwise can be any value, including null. Set in the
     * constructor and will not change.
     * </p>
     * <p>
     * It will represent as an actual null value of a Object type if and only if
     * the params[] is null..
     * </p>
     */
    private final String value;

    /**
     * <p>
     * Represents how many dimensions the array has.
     *
     * <pre>
     * For example:
     * int[] has dimension of 1
     * int[][] has dimension of 2
     * int[][][] has dimension of 3
     * </pre>
     *
     * </p>
     * <p>
     * Will be a integer >= 1. Set in the constructor and will not change.
     * Unless the specType is array, this value will be set to 1.
     * </p>
     */
    private final int dimension;

    /**
     * <p>
     * Represents the parameter specifications. If the specType is simple, this
     * member will be null. If complex, this member will be a 1-dimensional
     * array, and if array, it could be arbitrary-dimensional array, and must
     * correspond to the dimension member value.
     * </p>
     */
    private final Object[] parameters;

    /**
     * <p>
     * Creates an instance of the specification. Must follow the following
     * rules:
     *
     * <pre>
     * if specType is simple
     *    type must be one of the simple types defined in this class
     *    dimension is set to 1
     *    parameters is set to null
     * if specType is array
     *    type must correspond to the type of the params
     *    dimension must correspond to the dimension of the params
     *    value is set to null
     * if specType is complex
     *    type must not be one of the simple types defined in this class
     *    dimension is set to 1
     *    value is set to null
     * if specType is null
     *    type must be a valid Object type
     *    dimension is set to 1
     *    value is set to null
     * </pre>
     *
     * If these rules are not followed, the result is a IllegalArgumentException
     * </p>
     *
     * @param specType
     *            spec type
     * @param jar
     *            jar name and path
     * @param type
     *            fully-qualified name of Object or name of supported simple
     *            type, or type of array
     * @param identifier
     *            additional identifier to an object specification
     * @param value
     *            value of the simple type
     * @param dimension
     *            dimension of the params
     * @param params
     *            parameter specifications to be converted to objects for the
     *            constructor/array
     * @throws IllegalArgumentException
     *             if specType is not one of the three types, type is
     *             null/empty, dimension < 1, or the rules specified in the text
     *             are not followed.
     */
    public ObjectSpecification(String specType, String jar, String type, String identifier, String value,
            int dimension, Object[] params) {
        ObjectFactoryHelper.checkStringNotNullOrEmpty(type, "type");

        if (SIMPLE_SPECIFICATION.equals(specType)) {
            // simple specType, should check type.
            if (!ObjectFactoryHelper.checkSimpleType(type)) {
                throw new IllegalArgumentException("type is not the expected string in this class");
            }

            ObjectFactoryHelper.checkObjectNotNull(value, "value");

            if (dimension < 1) {
                throw new IllegalArgumentException("dimension should be positive.");
            }

            dimension = 1;
            params = null;
        } else if (ARRAY_SPECIFICATION.equals(specType)) {
            // array specType, should check the type and the class type.
            if (!ObjectFactoryHelper.checkSimpleType(type)) {
                ObjectFactoryHelper.checkClassValid(jar, type);
            }

            // the dimension of this array should be checked.
            ObjectFactoryHelper.checkArrayDimension(params, dimension);
            value = null;
        } else if (NULL_SPECIFICATION.equals(specType) || COMPLEX_SPECIFICATION.equals(specType)) {
            // null/complex specType. should check the type and the class type.
            if (ObjectFactoryHelper.checkSimpleType(type) && !type.equals(STRING_FULL) && !type.equals(STRING)) {
                throw new IllegalArgumentException("type should not be one of the simple types in this class");
            }

            if (!type.equals(STRING)) {
                ObjectFactoryHelper.checkClassValid(jar, type);
            }

            dimension = 1;
            value = null;
        } else {
            throw new IllegalArgumentException("specType is invalid");
        }

        // assign the private field.
        this.specType = specType;
        this.jar = jar;
        this.type = type;
        this.identifier = identifier;
        this.value = value;
        this.dimension = dimension;

        // make a shallow copy of the parameters.
        if (params != null) {
            parameters = params.clone();
        } else {
            parameters = null;
        }
    }

    /**
     * <p>
     * Gets the jar name and path. Value could be null.
     * </p>
     *
     * @return jar name and path
     */
    public String getJar() {
        return jar;
    }

    /**
     * <p>
     * Gets fully-qualified name of Object or name of supported simple type, or
     * type of array.
     * </p>
     *
     * @return fully-qualified name of Object or name of supported simple type
     */
    public String getType() {
        return type;
    }

    /**
     * <p>
     * Gets the additional identifier to an object specification. Value could be
     * null.
     * </p>
     * <p>
     * <strong>Changes in 2.2:</strong>
     * <ol>
     * <li>Fix the java doc.</li>
     * </ol>
     * </p>
     *
     * @return additional identifier to an object specification
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * <p>
     * Gets the value of the simple type. Value could be null.
     * </p>
     * <p>
     * <strong>Changes in 2.2:</strong>
     * <ol>
     * <li>Fix the java doc.</li>
     * </ol>
     * </p>
     *
     * @return value of the simple type
     */
    public String getValue() {
        return value;
    }

    /**
     * <p>
     * Gets the params. Value could be null.
     * </p>
     *
     * @return array of parameter specifications to be converted to objects for
     *         the constructor/array
     */
    public Object[] getParameters() {
        if (parameters == null) {
            return null;
        } else {
            return parameters.clone();
        }
    }

    /**
     * <p>
     * Gets the type of specification that this object is.
     * </p>
     *
     * @return type of specification that this object
     */
    public String getSpecType() {
        return specType;
    }

    /**
     * <p>
     * Gets the dimension of the param array.
     * </p>
     *
     * @return dimension of the param array
     */
    public int getDimension() {
        return dimension;
    }
}
