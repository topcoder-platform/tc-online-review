/*
 * Copyright (C) 2006, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.objectfactory.impl;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.config.Property;
import com.topcoder.util.config.UnknownNamespaceException;
import com.topcoder.util.objectfactory.ObjectFactoryHelper;
import com.topcoder.util.objectfactory.ObjectSpecification;
import com.topcoder.util.objectfactory.SpecificationFactory;
import com.topcoder.util.objectfactory.UnknownReferenceException;

/**
 * <p>
 * Concrete implementation of the {@link SpecificationFactory} backed by the
 * {@link ConfigManager} as the source of the configuration. All specifications
 * are loaded on startup, and make ready in a map. When called, the map will be
 * queried for the specification that matches the requested type and identifier.
 * </p>
 * <p>
 * <strong>Changes in 1.1:</strong>
 * <ol>
 * <li>Made all private methods static.</li>
 * <li>Specified generic parameters for all generic types in the code.</li>
 * </ol>
 * </p>
 * <p>
 * <strong>Thread Safety:</strong> This class holds mutable ObjectSpecification
 * instances that are exposed to the called. Assuming that these
 * ObjectSpecification instances are used by the caller in thread safe manner
 * (and this is true for Object Factory component), this class is thread safe.
 * </p>
 *
 * @author codedoc, saarixx, mgmg, TCSDEVELOPER
 * @version 1.1
 */
public class ConfigManagerSpecificationFactory implements SpecificationFactory {
    /**
     * Indicates '"'.
     */
    private static final String QUOTE = "\"";

    /**
     * Indicates ','.
     */
    private static final String COMMA = ",";

    /**
     * Indicates '}'.
     */
    private static final String RIGHT_BRACKET = "}";

    /**
     * Indicates '{'.
     */
    private static final String LEFT_BRACKET = "{";

    /**
     * indicates null.
     */
    private static final String NULL_ITEM = "null";

    /**
     * The delimiter.
     */
    private static final String ID_DELIMITER = ":";

    /**
     * Property name.
     */
    private static final String PROPERTY_NAME = "name";

    /**
     * Property value.
     */
    private static final String PROPERTY_VALUE = "value";

    /**
     * Property param.
     */
    private static final String PROPERTY_PARAM = "param";

    /**
     * Property values.
     */
    private static final String PROPERTY_VALUES = "values";

    /**
     * Property params.
     */
    private static final String PROPERTY_PARAMS = "params";

    /**
     * Property dimension.
     */
    private static final String PROPERTY_DIMENSION = "dimension";

    /**
     * Property arrayType.
     */
    private static final String PROPERTY_ARRAYTYPE = "arrayType";

    /**
     * Property type.
     */
    private static final String PROPERTY_TYPE = "type";

    /**
     * Property "jar".
     */
    private static final String PROPERTY_JAR = "jar";

    /**
     * <p>
     * Represents the mapping of types to ObjectSpecifications. Actually, the
     * mapping is of a key to a List of the specifications, which are then
     * distinguished by the identifier. In other words, a key is used to
     * retrieve a List. The list is iterated until the first specification with
     * the desired identifier is found. This map of Lists is created in the
     * constructor and will never change.
     * </p>
     * <p>
     * <strong>Changes in 1.1:</strong>
     * <ol>
     * <li>Specified generic parameters for the types.</li>
     * </ol>
     * </p>
     */
    private final Map<String, List<ObjectSpecification>> specifications =
        new HashMap<String, List<ObjectSpecification>>();

    /**
     * <p>
     * Configures the specifications from the configuration.
     * </p>
     * <p>
     * <strong>Changes in 1.1:</strong>
     * <ol>
     * <li>Specified generic parameters for all generic types.</li>
     * <li>Using For-Each loop instead of iterator.</li>
     * </ol>
     * </p>
     *
     * @param namespace
     *            Namespace to the Configuration
     * @throws IllegalArgumentException
     *             If param is null.
     * @throws IllegalReferenceException
     *             if the config is invalid somewhere, for example contains
     *             loop.
     * @throws SpecificationConfigurationException
     *             the error occurs when getting from ConfigManager.
     */
    public ConfigManagerSpecificationFactory(String namespace) throws SpecificationConfigurationException,
            IllegalReferenceException {
        ObjectFactoryHelper.checkStringNotNullOrEmpty(namespace, "namespace");

        ConfigManager cm = ConfigManager.getInstance();

        try {
            // the temporary Map to store all the ObjectSpecification without
            // resolving.
            Map<String, List<ObjectSpecification>> objects = new HashMap<String, List<ObjectSpecification>>();

            // another temporary Map to store the name relationship in order to
            // check the loop.
            Map<String, Set<String>> nameRelation = new HashMap<String, Set<String>>();

            // iterate through all the top-level names in the namespace.
            for (Enumeration<String> nsEnum = cm.getPropertyNames(namespace); nsEnum.hasMoreElements();) {
                String propertyName = nsEnum.nextElement();

                // add current property name to the name relationship and create
                // an empty set for this name.
                addPairToMap(nameRelation, propertyName, null);

                // get current property.
                Property p = cm.getPropertyObject(namespace, propertyName);

                // get the key and identifier of the property.
                String[] keys = propertyName.split(ID_DELIMITER, 2);
                String key = keys[0];
                String identifier = keys.length == 2 ? keys[1] : null;

                // get other sub-properties.
                String jar = p.getValue(PROPERTY_JAR);
                String type = p.getValue(PROPERTY_TYPE);
                String arrayType = p.getValue(PROPERTY_ARRAYTYPE);
                String dimension = p.getValue(PROPERTY_DIMENSION);
                String values = p.getValue(PROPERTY_VALUES);
                Property params = p.getProperty(PROPERTY_PARAMS);

                if (type != null && arrayType == null && dimension == null && values == null) {
                    // the specType is complex.
                    addObjectSpecification(objects, key, new ObjectSpecification(
                            ObjectSpecification.COMPLEX_SPECIFICATION, jar, type, identifier, null, 1,
                            getParametersArray(nameRelation, p.getName(), params)));
                } else if (type == null && params == null && arrayType != null
                    && dimension != null && values != null) {
                    // the specType is array.
                    addObjectSpecification(
                            objects,
                            key,
                            new ObjectSpecification(ObjectSpecification.ARRAY_SPECIFICATION, jar, arrayType,
                                    identifier, null, Integer.parseInt(dimension), getMultiDimArrayFromString(
                                            nameRelation, propertyName, arrayType, values.trim())));
                } else {
                    // the properties are not expected to recognize the
                    // specType.
                    throw new IllegalReferenceException("some properties are missing for property " + propertyName
                            + " or invalid that the specType can not be recognized.");
                }
            }

            // check if the config file contains loop.
            checkTopological(nameRelation);

            // resolve all the objects and add to the private Map.
            for (Entry<String, List<ObjectSpecification>> entry : objects.entrySet()) {
                for (ObjectSpecification spec : entry.getValue()) {
                    addObjectSpecification(specifications, entry.getKey(), resolveObjectSpecification(spec, objects));
                }
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalReferenceException("invalid config data during the parsing.", e);
        } catch (UnknownNamespaceException e) {
            throw new SpecificationConfigurationException("error occurs when getting the namespace.", e);
        }
    }

    /**
     * <p>
     * Obtains an ObjectSpecification from the Source, using a key and an
     * identifier. Typically the key will be the class name, and the identifier
     * would identify the specific configuration for this class. However, the
     * can be any String that the Source understands, and the identifier then
     * becomes optional. In other words, the source can use one or two keys to
     * uniquely identify an configuration for an object.
     * </p>
     *
     * @return object specification
     * @param key
     *            key to the object specification
     * @param identifier
     *            additional identifier to an object specification
     * @throws UnknownReferenceException
     *             If the key/identifier combination is not recognize
     * @throws IllegalArgumentException
     *             if key is null or empty
     */
    public ObjectSpecification getObjectSpecification(String key, String identifier) throws UnknownReferenceException {
        ObjectFactoryHelper.checkStringNotNullOrEmpty(key, "key");

        return getObjectSpecification(specifications, key.replace('.', '/'), identifier);
    }

    /**
     * <p>
     * Add ObjectSpecification object to the map.
     * </p>
     * <p>
     * <strong>Changes in 1.1:</strong>
     * <ol>
     * <li>Specified generic parameters for all generic types.</li>
     * <li>Made method static.</li>
     * </ol>
     * </p>
     *
     * @param objects
     *            the Map to store the ObjectSpecification.
     * @param key
     *            the key of the object.
     * @param spec
     *            the ObjectSpecification instance to be added.
     */
    private static void addObjectSpecification(Map<String, List<ObjectSpecification>> objects, String key,
            ObjectSpecification spec) {
        if (objects.containsKey(key)) {
            // key exists, just add to the corresponding List.
            objects.get(key).add(spec);
        } else {
            // key doesn't exist, create a new List and add it.
            List<ObjectSpecification> list = new ArrayList<ObjectSpecification>();
            list.add(spec);
            objects.put(key, list);
        }
    }

    /**
     * <p>
     * Add a pair of names to the Map.
     * </p>
     * <p>
     * <strong>Changes in 1.1:</strong>
     * <ol>
     * <li>Specified generic parameters for all generic types.</li>
     * <li>Made method static.</li>
     * </ol>
     * </p>
     *
     * @param names
     *            the Map to be added.
     * @param keyName
     *            the key name.
     * @param valueName
     *            the value to the key. Note maybe two or more value to one key.
     */
    private static void addPairToMap(Map<String, Set<String>> names, String keyName, String valueName) {
        // add the name pair to the Map. null valueName indicates create a new
        // empty set for the key name.
        if (names.containsKey(keyName)) {
            if (valueName != null) {
                names.get(keyName).add(valueName);
            }
        } else {
            Set<String> set = new HashSet<String>();

            // if valueName is not null, then add it.
            if (valueName != null) {
                set.add(valueName);
            }

            names.put(keyName, set);
        }
    }

    /**
     * <p>
     * Check the topological map to see if there exists loop.
     * </p>
     * <p>
     * <strong>Changes in 1.1:</strong>
     * <ol>
     * <li>Specified generic parameters for all generic types.</li>
     * <li>Using For-Each loop instead of iterator.</li>
     * <li>Made method static.</li>
     * </ol>
     * </p>
     *
     * @param names
     *            the Map of all the name relationships.
     * @throws IllegalReferenceException
     *             if the Map contains loop.
     */
    private static void checkTopological(Map<String, Set<String>> names) throws IllegalReferenceException {
        // this method uses toposort algorithm to check if the configuration
        // object contains loop. in the parameter Map, each entry maps one key
        // name to a Set of value names indicates the key object has been
        // referenced by value object. Each time, one of the objects with zero
        // size Set in the map will be chosen and deleted. if such objects can
        // not be found and the Map still has some elements, the configuration
        // contains loop.
        while (names.size() > 0) {
            String selectName = null;

            // find one of the zero in-degree element.
            for (Entry<String, Set<String>> entry : names.entrySet()) {
                String currentName = entry.getKey();

                if (entry.getValue().size() == 0) {
                    selectName = currentName;
                    break;
                }
            }

            // if found remove the entry of this name and all this name in the
            // sets to other names in the Map.
            if (selectName != null) {
                names.remove(selectName);

                for (Entry<String, Set<String>> entry : names.entrySet()) {
                    entry.getValue().remove(selectName);
                }
            } else {
                // if no such name found and the map still has elements, throw
                // exception.
                throw new IllegalReferenceException("The config file contains loop");
            }
        }
    }

    /**
     * <p>
     * Get the string from index 1 to the last but 1.
     * </p>
     * <p>
     * <strong>Changes in 1.1:</strong>
     * <ol>
     * <li>Made method static.</li>
     * </ol>
     * </p>
     *
     * @param value
     *            the string to process.
     * @return the processed string.
     */
    private static String getInnerString(String value) {
        return value.substring(1, value.length() - 1);
    }

    /**
     * <p>
     * Get multi-dimension array from a string.
     * </p>
     * <p>
     * <strong>Changes in 1.1:</strong>
     * <ol>
     * <li>Specified generic parameters for all generic types.</li>
     * <li>Made method static.</li>
     * </ol>
     * </p>
     *
     * @param names
     *            the Map to store the topological relationship of the
     *            ObejctSpecification.
     * @param specName
     *            the name of the ObjectSpecification.
     * @param arrayType
     *            the type in the array.
     * @param values
     *            the string representing of the array.
     * @return the multi-dimension array from the string.
     * @throws IllegalReferenceException
     *             if the values string doesn't indicates a valid array.
     */
    private static Object[] getMultiDimArrayFromString(Map<String, Set<String>> names, String specName,
            String arrayType, String values) throws IllegalReferenceException {
        boolean simpleType = ObjectFactoryHelper.checkSimpleType(arrayType);
        boolean stringType = ObjectFactoryHelper.checkStringSimpleType(arrayType);

        if (stringType) {
            arrayType = ObjectSpecification.STRING_FULL;
        }

        // the string must start with '{' and end with '}'.
        if (values.startsWith(LEFT_BRACKET) && values.endsWith(RIGHT_BRACKET)) {
            List<Object> result = new ArrayList<Object>();

            int sectionStart = -1;
            int level = 0;
            boolean isQuote = false;

            for (int i = 0; i < values.length(); i++) {
                // process each '"', '{' and '}'.
                if (values.charAt(i) == QUOTE.charAt(0)) {
                    // All the key characters inside quote should be ignored..
                    isQuote = !isQuote;
                } else if (!isQuote && values.charAt(i) == LEFT_BRACKET.charAt(0)) {
                    // for '{', add 1 to current level.
                    level++;

                    if (level == 2) {
                        // find the start of one sub-item. This item should be
                        // separated by one ',' to the former item. Otherwise
                        // throw exception.
                        if (sectionStart >= 0 && !values.substring(sectionStart, i).trim().equals(COMMA)) {
                            throw new IllegalReferenceException("The string is not well-format.");
                        }

                        // record the start of current sub-item.
                        sectionStart = i;
                    }
                } else if (!isQuote && values.charAt(i) == RIGHT_BRACKET.charAt(0)) {
                    // for '}', subtract 1 to current level.
                    level--;

                    if (level < 0) {
                        // '{' and '}' aren't compatible.
                        throw new IllegalReferenceException("The string is not well-format.");
                    } else if (level == 1) {
                        // current sub-item ends. recursively process this item.
                        result.add(getMultiDimArrayFromString(names, specName, arrayType,
                                values.substring(sectionStart, i + 1)));
                        sectionStart = i + 1;
                    }
                }
            }

            // level is not 0 indicates '{' and '}' aren't compatible. isQuote
            // is not 0 indicates odd quotes.
            if (level != 0 || isQuote) {
                throw new IllegalReferenceException("The string is not well-format.");
            }

            // if only one level, it means the dimension of current string
            // representing array is 1.
            if (sectionStart == -1) {
                // split the string with comma.
                String[] items = splitString(getInnerString(values), COMMA.charAt(0));

                // iterate through all the items.
                for (int i = 0; i < items.length; i++) {
                    if (items[i].length() == 0) {
                        throw new IllegalReferenceException("the item string can not be empty.");
                    }

                    if (items[i].equals(NULL_ITEM)) {
                        // if null keyword is used. add an ObjectSpecification
                        // with NULL_SPECIFICATION. Note that simple type should
                        // not be null.
                        if (simpleType && !stringType) {
                            throw new IllegalReferenceException("Simple type can not be null.");
                        } else {
                            result.add(new ObjectSpecification(ObjectSpecification.NULL_SPECIFICATION, null, arrayType,
                                    null, null, 1, null));
                        }
                    } else if (simpleType) {
                        if (stringType || arrayType.equals(ObjectSpecification.CHAR)) {
                            // if the type is string or char, should distinguish
                            // if it is a reference or not.
                            if (isReference(items[i])) {
                                // add the reference to the object.
                                result.add(items[i]);

                                // add the name relationship to the Map.
                                addPairToMap(names, items[i], specName);
                            } else {
                                // add ObjectSpecification with simple type.
                                result.add(new ObjectSpecification(ObjectSpecification.SIMPLE_SPECIFICATION, null,
                                        arrayType, null, getInnerString(items[i]), 1, null));
                            }
                        } else {
                            // add ObjectSpecification with simple type.
                            result.add(new ObjectSpecification(ObjectSpecification.SIMPLE_SPECIFICATION, null,
                                    arrayType, null, items[i], 1, null));
                        }
                    } else {
                        // add the reference to the object.
                        result.add(items[i]);

                        // add the name relationship to the Map.
                        addPairToMap(names, items[i], specName);
                    }
                }
            }

            // return the whole array.
            return result.toArray();
        } else {
            throw new IllegalReferenceException("The string is not well-format.");
        }
    }

    /**
     * <p>
     * Get the parameter list in the property.
     * </p>
     * <p>
     * <strong>Changes in 1.1:</strong>
     * <ol>
     * <li>Specified generic parameters for all generic types.</li>
     * <li>Made method static.</li>
     * </ol>
     * </p>
     *
     * @param names
     *            the Map to store the topological relationship of the
     *            ObejctSpecification.
     * @param specName
     *            the name of the ObjectSpecification.
     * @param params
     *            the property contains the parameters.
     * @return the array contains objects.
     * @throws IllegalReferenceException
     *             if any error occurs during the process.
     */
    private static Object[] getParametersArray(Map<String, Set<String>> names, String specName, Property params)
        throws IllegalReferenceException {
        if (params == null) {
            return null;
        }

        List<Object> list = new ArrayList<Object>();

        // get the property starts from param1.
        for (int n = 1;; n++) {
            Property p = params.getProperty(PROPERTY_PARAM + n);

            if (p == null) {
                break;
            }

            // get the sub-properties.
            String type = p.getValue(PROPERTY_TYPE);
            String value = p.getValue(PROPERTY_VALUE);
            String name = p.getValue(PROPERTY_NAME);

            if (type == null && name != null && value == null) {
                // this is a link to other ObjectSpecification.
                list.add(name);
                addPairToMap(names, name, specName);
            } else if (type != null && name == null) {
                // this is a simple type.
                try {
                    list.add(new ObjectSpecification(value == null ? ObjectSpecification.NULL_SPECIFICATION
                            : ObjectSpecification.SIMPLE_SPECIFICATION, null, type, null, value, 1, null));
                } catch (IllegalArgumentException e) {
                    throw new IllegalReferenceException("the creation to the object is failed.", e);
                }
            } else {
                // the specType can not be recognized.
                throw new IllegalReferenceException(
                        "some properties are missing or invalid that the specType can not be recognized.");
            }
        }

        return list.toArray();
    }

    /**
     * <p>
     * Get the ObjectSpecification from a specified Map.
     * </p>
     * <p>
     * <strong>Changes in 1.1:</strong>
     * <ol>
     * <li>Specified generic parameters for all generic types.</li>
     * <li>Made method static.</li>
     * </ol>
     * </p>
     *
     * @param objects
     *            the Map to get the ObjectSpecification.
     * @param key
     *            the key to the ObjectSpecification.
     * @param identifier
     *            the additional identifier to the ObjectSpecification.
     * @return the ObjectSpecification found.
     * @throws UnknownReferenceException
     *             if ObjectSpecification can not be found.
     */
    private static ObjectSpecification getObjectSpecification(Map<String, List<ObjectSpecification>> objects,
            String key, String identifier) throws UnknownReferenceException {
        // get the List according to the key.
        List<ObjectSpecification> list = objects.get(key);

        if (list == null) {
            throw new UnknownReferenceException("The list with the specified key " + key + " doesn't exist");
        }

        // iterate through the array list to find the object with specified
        // identified.
        for (int i = 0; i < list.size(); i++) {
            ObjectSpecification spec = list.get(i);

            if (identifier == null) {
                // if identifier is null, return the object with null
                // identifier.
                if (spec.getIdentifier() == null) {
                    return spec;
                }
            } else if (spec.getIdentifier().equals(identifier)) {
                // otherwise return the object with expected identifier.
                return spec;
            }
        }

        // no object found.
        throw new UnknownReferenceException("The list with the specified key doesn't exist");
    }

    /**
     * <p>
     * Get the ObjectSpecification with the specified name.
     * </p>
     * <p>
     * <strong>Changes in 1.1:</strong>
     * <ol>
     * <li>Specified generic parameters for all generic types.</li>
     * <li>Made method static.</li>
     * </ol>
     * </p>
     *
     * @param objects
     *            the Map to get the ObjectSpecification.
     * @param name
     *            the name of the object to get.
     * @return the object with the specified name.
     * @throws UnknownReferenceException
     *             if the specified ObjectSpecification doesn't exist.
     */
    private static ObjectSpecification getObjectSpecification(Map<String, List<ObjectSpecification>> objects,
            String name) throws UnknownReferenceException {
        // get the object by name. name will be split to key and identifier.
        String[] keys = name.split(ID_DELIMITER, 2);

        if (keys.length == 2) {
            // delimiter exists, identifier is not null.
            return getObjectSpecification(objects, keys[0], keys[1]);
        } else {
            // delimiter exists, identifier is null.
            return getObjectSpecification(objects, keys[0], null);
        }
    }

    /**
     * <p>
     * Check if the name is a reference.
     * </p>
     * <p>
     * <strong>Changes in 1.1:</strong>
     * <ol>
     * <li>Made method static.</li>
     * </ol>
     * </p>
     *
     * @param name
     *            the name to check.
     * @return true if the name is a reference.
     */
    private static boolean isReference(String name) {
        return !(name.startsWith(QUOTE) && name.endsWith(QUOTE));
    }

    /**
     * <p>
     * Resolve the name in an array to an ObjectSpecification.
     * </p>
     * <p>
     * <strong>Changes in 1.1:</strong>
     * <ol>
     * <li>Specified generic parameters for all generic types.</li>
     * <li>Made method static.</li>
     * </ol>
     * </p>
     *
     * @param params
     *            the array indicates the parameters.
     * @param objects
     *            the Map of all the objects.
     * @throws IllegalReferenceException
     *             if some name can not be resolved.
     */
    private static void resolveObjectSpecification(Object[] params, Map<String, List<ObjectSpecification>> objects)
        throws IllegalReferenceException {
        for (int i = 0; i < params.length; i++) {
            if (params[i].getClass().getComponentType() != null) {
                // if the input params is a multi-dimension array, recursively
                // call this method.
                resolveObjectSpecification((Object[]) params[i], objects);
            } else {
                if (params[i] instanceof String) {
                    // if the item is a string, it means this is a reference to
                    // other object.
                    try {
                        // get the object by name.
                        params[i] = getObjectSpecification(objects, (String) params[i]);
                    } catch (UnknownReferenceException e) {
                        throw new IllegalReferenceException("The object name " + (String) params[i]
                                + " can not be resolved.", e);
                    }
                }

                if (params[i] instanceof ObjectSpecification) {
                    // if the item is ObjectSpecification, it should also be
                    // resolved.
                    params[i] = resolveObjectSpecification((ObjectSpecification) params[i], objects);
                }
            }
        }
    }

    /**
     * <p>
     * Resolve the name in an ObjectSpecification to an ObjectSpecification.
     * </p>
     * <p>
     * <strong>Changes in 1.1:</strong>
     * <ol>
     * <li>Specified generic parameters for all generic types.</li>
     * <li>Made method static.</li>
     * </ol>
     * </p>
     *
     * @param spec
     *            the ObjectSpecification to resolve.
     * @param objects
     *            the Map of all the objects.
     * @throws IllegalReferenceException
     *             if some name can not be resolved.
     * @return the object resolved.
     */
    private static ObjectSpecification resolveObjectSpecification(ObjectSpecification spec,
            Map<String, List<ObjectSpecification>> objects) throws IllegalReferenceException {
        Object[] params = spec.getParameters();

        if (params != null) {
            // if parameters is not null, resolve it.
            resolveObjectSpecification(params, objects);
        }

        // create a new resolved ObjectSpecification and return it.
        return new ObjectSpecification(spec.getSpecType(), spec.getJar(), spec.getType(), spec.getIdentifier(),
                spec.getValue(), spec.getDimension(), params);
    }

    /**
     * <p>
     * Split a string and all the keyword inside quote should be ignored.
     * </p>
     * <p>
     * <strong>Changes in 1.1:</strong>
     * <ol>
     * <li>Renamed from "spliteString" to "splitString".</li>
     * <li>Specified generic parameters for all generic types.</li>
     * <li>Made method static.</li>
     * </ol>
     * </p>
     *
     * @param value
     *            the string to be split.
     * @param delimiter
     *            the delimiter.
     * @return the string array after splitting.
     * @throws IllegalReferenceException
     *             if the string is not well-formatted.
     */
    private static String[] splitString(String value, char delimiter) throws IllegalReferenceException {
        List<String> result = new ArrayList<String>();
        boolean isQuote = false;
        int i;
        int sectionStart = 0;

        // process the character one by one.
        for (i = 0; i < value.length(); i++) {
            if (value.charAt(i) == QUOTE.charAt(0)) {
                isQuote = !isQuote;
            } else if (!isQuote && value.charAt(i) == delimiter) {
                // the split delimiter should not inside a pair of quote.
                if (i <= sectionStart) {
                    throw new IllegalReferenceException("The string is not well-format for '" + value + "'.");
                }

                // add the result string.
                result.add(value.substring(sectionStart, i).trim());
                sectionStart = i + 1;
            }
        }

        // if the count of the quote is odd, throw exception.
        if (isQuote) {
            throw new IllegalReferenceException("The string is not well-format for '" + value + "'.");
        }

        // add the last string.
        result.add(value.substring(sectionStart, i).trim());

        return result.toArray(new String[0]);
    }
}
