/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.file.fieldconfig;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.topcoder.util.file.Util;

/**
 * <p>
 * This is a simple utility class that is used to conveniently populate the NodeList
 * and Loops of a Template.
 * </p>
 *
 * <p>
 * There is a method for conveniently populating a NodeList from an existing Java Objects
 * (the class currently supports Maps and Java beans), and a method for conveniently
 * populating a Loop using a list of existing Java Objects.
 * </p>
 *
 * <p>
 * Thread Safety: This class is thread-safe due to its statelessness.
 * </p>
 *
 * @author ShindouHikaru, biotrail
 * @version 2.1
 * @since 2.1
 */
public class NodeListUtility {
    /**
     * <p>
     * Private constructor.
     * </p>
     */
    private NodeListUtility() {
        // empty
    }

    /**
     * <p>
     * This is a convenience method that is provided to populate a nodeList with
     * data that is retrieved from a data Objects.
     * </p>
     *
     * <p>
     * This method supports two types of data retrieval - one is from a Map of data objects, the other
     * from a set of JavaBean objects.
     * </p>
     *
     * <p>
     * Population from Map Objects:
     * You can consult {@link #populateNodeList(NodeList, Map, boolean)}.
     * </p>
     *
     * <p>
     * Population from JavaBean Objects:
     * In this case, the data object should follow the JavaBean conventions for defining
     * readable properties. For example, a "reviewerPayment" Field Name should be
     * accessible by data.getReviewerPayment() method.
     * </p>
     *
     * <p>
     * The utility will make use of toString() method to convert the value returned from the
     * getter method to a String for populating in the Field name.
     * </p>
     *
     * <p>
     * In the case of a Loop node, the getter is expected to return either a List, or an array
     * of objects. The utility then delegates the given loop to populateLoop method.
     * Note, <code>IllegalArgumentException</code> is thrown if the object corresponding
     * to the Loop is not a <code>List</code> or array.
     * </p>
     *
     * @param nodeList The nodeList to populate.
     * @param data The data that is used to populate the nodeList.
     * @param ignoreNulls Whether any null field data should be ignored (simply not set).
     *
     * @throws IllegalArgumentException if either argument is null, or if the data does not contain
     * the proper elements for the nodeList.
     * @throws SecurityException if access to the information is denied
     */
    public static void populateNodeList(NodeList nodeList, Object data, boolean ignoreNulls) {
        Util.checkNull(data, "data");

        if (data instanceof Map) {
            // delegate the populateNodeList method taking a Map argument
            populateNodeList(nodeList, (Map) data, ignoreNulls);
        } else {
            Util.checkNull(nodeList, "nodeList");

            Node[] nodes = nodeList.getNodes();

            for (int i = 0; i < nodes.length; i++) {
                // get the name of the node
                String name = getNodeName(nodes[i]);

                Class type = data.getClass();
                try {
                    // compose the getter method name
                    Method method = type.getMethod("get" + upperCaseFirstCharacter(name), (Class[])null);
                    Object object = method.invoke(data, (Object[])null);

                    // update the value of the node
                    setValue(nodes[i], name, object, ignoreNulls);

                    if (nodes[i] instanceof Condition) {
                        NodeList subNodes = ((Condition) nodes[i]).getSubNodes();
                        populateNodeList(subNodes, data, ignoreNulls);
                    }

                } catch (NoSuchMethodException e) {
                    if (!ignoreNulls) {
                        throw new IllegalArgumentException("Unable to obtain the getter method, caused by "
                            + e.getMessage());
                    }
                } catch (IllegalAccessException e) {
                    if (!ignoreNulls) {
                        throw new IllegalArgumentException("Unable to obtain the getter method, caused by "
                            + e.getMessage());
                    }
                } catch (InvocationTargetException e) {
                    throw new IllegalArgumentException("Unable to set the value via reflection, caused by "
                        + e.getMessage());
                }
            }
        }
    }

    /**
     * <p>
     * This method upper-case the first character in the given value.
     * </p>
     *
     * @param value the given value to upper-case its first character
     * @return the original value with the first character upper-cased
     */
    private static String upperCaseFirstCharacter(String value) {
        StringBuffer sb = new StringBuffer();

        // the first character is upper cased
        sb.append(Character.toUpperCase(value.charAt(0)));

        if (value.length() > 1) {
            sb.append(value.substring(1));
        }

        return sb.toString();
    }

    /**
     * <p>
     * Gets the name of the given node.
     * </p>
     *
     * @param node the node to get its name
     *
     * @return the name of the given node.
     *
     * @throws IllegalArgumentException if the node is not Field, Condition or Loop
     */
    private static String getNodeName(Node node) {
        if (node instanceof Field) {
            // the node is a field
            return ((Field) node).getName();
        } else if (node instanceof Condition) {
            // the node is a condition
            return ((Condition) node).getName();
        } else if (node instanceof Loop) {
            // the node is a loop
            return ((Loop) node).getLoopElement();
        } else {
            // unknown node type
            throw new IllegalArgumentException("The node is not of Field, Condition or Loop type, its type is "
                + node.getClass().getName());
        }
    }

    /**
     * <p>
     * This is a convenience method that is provided to populate a nodeList with
     * data that is retrieved from a <code>Map</code> of data.
     * </p>
     *
     * <p>
     * In this case, the keys of the <code>Map</code> are the field names, and the values of
     * the <code>Map</code> are the field values (if the value is not a <code>String</code>,
     * the value is converted to a <code>String</code> by calling <code>toString</code> on the
     * object, the <code>Map</code> may not contain null field values).
     * </p>
     *
     * <p>
     * If a Loop is found in the <code>NodeList</code>, then the corresponding <code>Map</code>
     * value should be a List of data, or an array of Objects for the given <code>NodeList</code>.
     * </p>
     *
     * <p>
     * It will then delegate to populateLoop with given Loop.  <code>IllegalArgumentException</code>
     * is thrown if the object corresponding to the <code>Loop</code> is not a <code>List</code> or array.
     * </p>
     *
     * @param nodeList The nodeList to populate.
     * @param data A Map of data that is used to populate the nodeList.
     * @param ignoreNulls Whether any null field data should be ignored (simply not set).
     *
     * @throws IllegalArgumentException if either argument is null, or if the data does not contain the
     * proper elements for the nodeList.
     */
    public static void populateNodeList(NodeList nodeList, Map data, boolean ignoreNulls) {
        Util.checkNull(nodeList, "nodeList");
        Util.checkNull(data, "data");

        Node[] nodes = nodeList.getNodes();

        for (int i = 0; i < nodes.length; i++) {
            // get the name of the node
            String name = getNodeName(nodes[i]);

            // update the node value with the data got from the given map
            setValue(nodes[i], name, data.get(name), ignoreNulls);
            if (nodes[i] instanceof Condition) {
                NodeList subNodes = ((Condition) nodes[i]).getSubNodes();
                populateNodeList(subNodes, data, ignoreNulls);
            }
        }
    }

    /**
     * <p>
     * This method updates the node value with the help of the given value.
     * </p>
     *
     * @param node The node to update value
     * @param nodeName the name of the node
     * @param value the new value to update the node
     * @param ignoreNulls Whether any null field data should be ignored (simply not set).
     *
     * @throws IllegalArgumentException if the value is null but ignoreNulls is false or the
     * value is not List or Object[] when the node is a Loop
     */
    private static void setValue(Node node, String nodeName, Object value, boolean ignoreNulls) {
        if (value == null) {
            // the new value is null
            if (!ignoreNulls) {
                throw new IllegalArgumentException("The value to set the node [" + nodeName + "] is null.");
            }
        } else {
            if (node instanceof Condition) {
                // the node is a condition
                ((Condition) node).setValue(value.toString());
            } else if (node instanceof Field) {
                // the node is a field
                ((Field) node).setValue(value.toString());
            } else if (node instanceof Loop) {
                // the node is a loop, the value must be of List or Object[] type
                if (value instanceof List) {
                    populateLoop((Loop) node, (List) value, ignoreNulls);
                } else if (value instanceof Object[]) {
                    populateLoop((Loop) node, (Object[]) value, ignoreNulls);
                } else {
                    // the value is invalid
                    throw new IllegalArgumentException("The value is not an Object array or List, its type is "
                        + value.getClass().getName());
                }
            }
        }
    }

    /**
     * <p>
     * This populates a <code>NodeList</code> with data from the specified Object.
     * </p>
     *
     * <p>
     * Nulls in the data are simply ignored, and the resulting Document generated will have blanks
     * for the specified fields.
     * </p>
     *
     * <p>
     * For detail information, please consult {@link #populateNodeList(NodeList, Object, boolean)}.
     * </p>
     *
     * @param nodeList The nodeList to populate.
     * @param data The data that is used to populate the nodeList.
     *
     * @throws IllegalArgumentException if either argument is null, or if the data does not contain the proper
     * elements for the nodeList.
     */
    public static void populateNodeList(NodeList nodeList, Object data) {
        populateNodeList(nodeList, data, true);
    }

    /**
     * <p>
     * This populates a <code>NodeList</code> with data from the specified <code>Map</code>.
     * </p>
     *
     * <p>
     * Nulls in the data are simply ignored, and the resulting Document generated will have blanks
     * for the specified fields.
     * </p>
     *
     * <p>
     * For detail information, please consult {@link #populateNodeList(NodeList, Map, boolean)}.
     * </p>
     *
     * @param nodeList The nodeList to populate.
     * @param data A Map of data that is used to populate the nodeList.
     *
     * @throws IllegalArgumentException if either argument is null, or if the data does not contain the proper
     * elements for the nodeList.
     */
    public static void populateNodeList(NodeList nodeList, Map data) {
        populateNodeList(nodeList, data, true);
    }

    /**
     * <p>
     * This populates a <code>Loop</code> with the data from the given <code>List</code>.
     * </p>
     *
     * <p>
     * A new <code>LoopItem</code> will be inserted into the <code>Loop</code> for each element in the
     * dataset.
     * </p>
     *
     * <p>
     * The data for the given <code>LoopItem</code> will be retrieved from the element in the dataset,
     * by delegating the <code>LoopItem</code>'s <code>NodeList</code> back to <code>populateNodeList</code>.
     * </p>
     *
     * @param loop The loop to populate.
     * @param data A list of data objects. Each element in the data represents a single data object
     * for a LoopItem.
     * @param ignoreNulls Whether nulls are ignored or not.
     *
     * @throws IllegalArgumentException if loop or data is null. Or data contains null elements and
     * ignoreNulls is false, or data contains elements that are not appropriate for the LoopItem's
     * NodeLists.
     */
    public static void populateLoop(Loop loop, List data, boolean ignoreNulls) {
        Util.checkNull(loop, "loop");
        Util.checkNull(data, "data");

        for (Iterator it = data.iterator(); it.hasNext();) {
            Object element = it.next();

            if (element == null && !ignoreNulls) {
                throw new IllegalArgumentException("There are null elements in the given data.");
            }

            NodeList nodeList = loop.addLoopItem();
            if (element != null) {
                populateNodeList(nodeList, element, ignoreNulls);
            }
        }
    }

    /**
     * <p>
     * This populates a <code>Loop</code> with the data from the given List.
     * </p>
     *
     * <p>
     * A new <code>LoopItem</code> will be inserted into the <code>Loop</code> for each element in the dataset.
     * The data for the given <code>LoopItem</code> will be retrieved from the element in the dataset,
     * by delegating the <code>LoopItem</code>'s <code>NodeList</code> back to populateNodeList.
     * </p>
     *
     * @param loop The loop to populate.
     * @param data A list of data objects.  Each element in the data represents a single data object
     * for a LoopItem.
     * @param ignoreNulls Whether nulls are ignored or not.
     *
     * @throws IllegalArgumentException if loop or data is null. Or data contains null elements
     * and ignoreNulls is false, or data contains elements that are not appropriate for the
     * LoopItem's NodeLists.
     */
    public static void populateLoop(Loop loop, Object[] data, boolean ignoreNulls) {
        Util.checkNull(data, "data");

        populateLoop(loop, Arrays.asList(data), ignoreNulls);
    }

    /**
     * <p>
     * This will populate the Loop with data from the given list.
     * </p>
     *
     * <p>
     * Nulls in the data are simply ignored, and the resulting Document generated will have blanks
     * for the specified fields.
     * </p>
     *
     * <p>
     * For detail information, please consult {@link #populateLoop(Loop, List, boolean)}.
     * </p>
     *
     * @param loop The loop to populate with data.
     * @param data A list of data objects. Each element in the data represents a single data object
     * for a LoopItem.
     *
     * @throws IllegalArgumentException if loop or data is null. Or data contains null elements
     * and ignoreNulls is false, or data contains elements that are not appropriate for the
     * LoopItem's NodeLists.
     */
    public static void populateLoop(Loop loop, List data) {
        populateLoop(loop, data, true);
    }

    /**
     * <p>
     * This will populate the Loop with data from the given array.
     * </p>
     *
     * <p>
     * Nulls in the data are simply ignored, and the resulting Document generated will have blanks
     * for the specified fields.
     * </p>
     *
     * <p>
     * For detail information, please consult {@link #populateLoop(Loop, Object[], boolean)}.
     * </p>
     *
     * @param loop The loop to populate.
     * @param data An array of data objects. Each element in the data represents a single data
     * object for a LoopItem.
     *
     * @throws IllegalArgumentException if loop or data is null. Or data contains null elements
     * and ignoreNulls is false, or data contains elements that are not appropriate for the
     * LoopItem's NodeLists.
     */
    public static void populateLoop(Loop loop, Object[] data) {
        populateLoop(loop, data, true);
    }
}
