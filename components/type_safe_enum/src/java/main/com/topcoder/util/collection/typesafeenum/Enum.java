/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.collection.typesafeenum;

import java.io.Serializable;
import java.lang.reflect.Field;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * This class provides as much support as is possible in Java for correct implementation of the "typesafe enum" pattern.
 * With some care, Java programs can make use of C-style enums, with all the benefits of type safety, but with even more
 * functionality. The way to accomplish this is well-understood; this class encapsulates as much of these best practices
 * as possible, so that applications create enumerated types with minimal work by subclassing Enum.
 * </p>
 * <p>
 * An enumerated type can be created simply by subclassing Enum, making the constructor private, and providing a few
 * <code>public static final</code> instances of the type:
 * </p>
 * <p>
 * <pre>
 * public class MyBool extends Enum {
 *     public static final MyBool TRUE = new MyBool();
 *     public static final MyBool FALSE = new MyBool();
 *     private MyBool() {
 *     }
 * }
 * </pre>
 * </p>
 * <p>
 * But more is possible; the enumerated type classes can include properties, methods, logic, etc. In fact as long as the
 * class is written to be immutable, anything is allowed. Note also that the ordering of the declaration of the
 * enumerated values corresponds to their sort ordering, which also resembles the behavior of C-style enums:
 * </p>
 * <p>
 * <pre>
 *    public class Suit extends Enum {
 *       public static final Suit CLUBS = new Suit("Clubs", SuitColor.BLACK);
 *       public static final Suit DIAMONDS = new Suit("Diamonds", SuitColor.RED);
 *       public static final Suit HEARTS = new Suit("Hearts", SuitColor.RED);
 *       public static final Suit SPADES = new Suit("Spades" SuitColor.BLACK);
 *       private final String name;
 *       private final SuitColor color;
 *       private Suit(String name, SuitColor color) {
 *          this.name = name;
 *          this.color = color;
 *       }
 *       public String getName() { return name; }
 *       public SuitColor getColor() { return color; }
 *       public String toString() { return getName(); }
 *    }
 * </pre>
 * </p>
 * <p>
 * <pre>
 * public class SuitColor extends Enum {
 *     public static final SuitColor RED = new SuitColor("red");
 *     public static final SuitColor BLACK = new SuitColor("black");
 *     private final String color;
 *     private SuitColor(String color) {
 *         this.color = color;
 *     }
 *     public String getColor() {
 *         return color;
 *     }
 *     public String toString() {
 *         return getColor();
 *     }
 * }
 * </pre>
 * </p>
 * <p>
 * Enum does not override equals() or hashCode(), since the default implementation in java.lang.Object is sufficient.
 * Subclasses may override these methods, though this is typically not necessary.
 * </p>
 * <p>
 * Note that JDK 1.5 will support enumerations in the Java language itself; this class is intended for use with JDK 1.4
 * and earlier.
 * </p>
 * <p>
 * <b>New in version 1.1:</b>
 * <ul>
 * <li>1. A new constructor is added to the Enum class to accept a Class instance, which is used as the type identifier
 * for the enum value. This could allow subclasses to use internal sub-classing to differentiate behavior based on
 * enumeration type while maintaining proper ordinal counts. To understand it more clearly, you could see demo and
 * java.util.lang.Enum#getDeclaringClass(), which is introduced in java 1.5.</li>
 * <li>2. Another feature introduced in this version is a new utility method. This method could be used to get enum
 * value by its name.</li>
 * <li>3. Thread-safety is still assured in this version.
 * </ul>
 * </p>
 * <p>
 * <b>Thread Safety</b>: This class is thread safe.
 * </p>
 *
 * @author TAG, maone, vividmxx
 * @version 1.1
 * @since 1.0
 */
public abstract class Enum implements Comparable, Serializable {

    /**
     * <p>
     * This data structure ([declaringClass : [enumInstance1, enumInstance2...]]) is used to store all enumeration
     * values used by all declaring class of Enum. It maps declaring classes (represented by Class objects) to ArrayList
     * objects. Each ArrayList has all enumeration values for that declaring class, ordered by ordinal value; These
     * ArrayList objects are used frequently to look up enumeration values and return ordered collections of enumeration
     * values.
     * </p>
     * <p>
     * This Map is synchronized, to be safe, to avoid problems when two Enum subclasses are simultaneously loaded.
     * </p>
     * <p>
     * ArrayList objects in this Map, however, do not need to be synchronized; they are each modified by exactly one
     * thread (the one that loads a given Enum subclass) when the subclass is initialized, and never modified again.
     * </p>
     * <p>
     * This variable reference will never be changed after initialized as an empty map. All keys and values shouldn't be
     * null. The value lists can't contain null elements either.
     * </p>
     *
     * @see #getEnumByOrdinal(int, Class)
     * @associates ArrayList
     */
    private static final Map enumsByClass = Collections.synchronizedMap(new HashMap());

    /**
     * <p>
     * This data structure ([declaringClass : [toString-Represent : enumInstance]]) is used to store Map's of toString()
     * -&gt; Enum values used by all declaring classes of Enum. It maps declaring classes (represented by Class objects)
     * to Map objects. Each Map has all enumeration values for that declaring class, keyed by toString() value; that is,
     * the string representation values (String's) are mapped to the actual instances of the Enum declaring classes.
     * These Maps are used and filled only during getEnumByStringValue() to look up enumeration instances for
     * corresponding Classes.
     * </p>
     * <p>
     * This Map is synchronized, to be safe, to avoid problems when two searches for the same Enum declaring classes are
     * simultaneously started in different threads.
     * </p>
     * <p>
     * By default it's not filled with data, but each time <code>getEnumByStringValue(String, Class)</code> called for
     * a new class it create a Map to answer all future queries for the same class.
     * </p>
     * <p>
     * This variable reference will never be changed after initialized as an empty map. All keys and values shouldn't be
     * null. The value map can't contain null/empty keys/value either.
     * </p>
     *
     * @see #getEnumByStringValue(String, Class)
     * @associates Map
     */
    private static final Map stringSearchByClass = Collections.synchronizedMap(new HashMap());

    /**
     * <p>
     * This data structure ([declaringClass : [enumConstantName : enumInstance]]) is used to store Map's of
     * getEnumName() -&gt; Enum values used by all declaring classes of Enum. It maps declaring classes (represented by
     * Class objects) to Map objects. Each Map has all enumeration values for that declaring class, keyed by
     * getEnumName() value; that is, the enum constant names (String's) are mapped to the actual instances of the Enum
     * declaring class. These Maps are used and filled only during getEnumByName() to look up enumeration instances for
     * corresponding Classes.
     * </p>
     * <p>
     * This Map is synchronized, to be safe, to avoid problems when two searches for the same Enum declaring classes are
     * simultaneously started in different threads.
     * </p>
     * <p>
     * By default it's not filled with data, but each time <code>getEnumByName(String, Class)</code> called for a new
     * class it create a Map to answer all future queries for the same class.
     * </p>
     * <p>
     * This variable reference will never be changed after initialized as an empty map. All keys and values shouldn't be
     * null. The value map can't contain null/empty keys/value either.
     * </p>
     *
     * @see #getEnumByName(String, Class)
     * @associates Map
     */
    private static final Map nameSearchByClass = Collections.synchronizedMap(new HashMap());

    /**
     * <p>
     * This data structure ([declaringClass : [enumInstance : enumConstantName]]) is used to store Map's of Enum values
     * -&gt; enum constant names used by all declaring classes of Enum. It maps declaring classes (represented by Class
     * objects) to Map objects. Each Map has all enumeration values for that declaring class, keyed by actual instances
     * of the Enum declaring class (Enum instances) are mapped to the enum constant names (String's). These Maps are
     * used and filled only during getEnumName() to look up enum constant name for enumeration instances of
     * corresponding declaring class.
     * </p>
     * <p>
     * This Map is synchronized, to be safe, to avoid problems when two queries for the same Enum declaring classes are
     * simultaneously started in different threads.
     * </p>
     * <p>
     * By default it's not filled with data, but each time <code>getEnumName()</code> called for a new class it create
     * a Map to answer all future queries for the same class.
     * </p>
     * <p>
     * This variable reference will never be changed after initialized as an empty map. All keys and values shouldn't be
     * null. The value map can't contain null/empty keys/value either.
     * </p>
     *
     * @see #getEnumName()
     * @associates Map
     */
    private static final Map enumNamesByClass = Collections.synchronizedMap(new HashMap());

    /**
     * <p>
     * The ordinal value that uniquely identifies an instance of a subclass of Enum among all other instances of the
     * same class. This value is assigned in the Enum constructor, which ensures that the value is unique. Furthermore,
     * the values assigned are always increasing, starting from an initial value of 0. This means that the enumeration
     * values defined first sort before enumeration values assigned later, which makes the ordering of enumeration
     * values natural and consistent with C/C++ enums.
     * </p>
     * <p>
     * This value is used throughout Enum to efficiently compare Enum subclass instances.
     * </p>
     * <p>
     * It is initialized as a non-negative value in the constructor and never be changed later. It can be accessed via
     * getOrdinal().
     * </p>
     *
     * @see #getOrdinal()
     */
    private final int ordinal;

    /**
     * <p>
     * Represents the declaring class of Enum instance.
     * <p>
     * </p>
     * A declaring class is the class in which this enum value is declared as a static final field. It is added in
     * version 1.1 to support internal sub-classing. It will be used instead of this.getClass() in most places of
     * version 1.1. It has the similar meaning as java.lang.Enum#getDeclaringClass().
     * </p>
     * <p>
     * It is initialized as a non-null value in constructor and never be changed later. It can be accessed via
     * getDeclaringClass().
     * </p>
     *
     * @see #getDeclaringClass()
     */
    private final Class declaringClass;

    /**
     * <p>
     * This default constructor uses the default declaring class (this.getClass()) to create an enum.
     * </p>
     * <p>
     * This constructor is kept for backward compatibility. And it can also be used for the enum which don't have
     * sub-classes, including internal sub-classes.
     * </p>
     * @since 1.0
     */
    protected Enum() {
        this.ordinal = init(this.getClass());
        this.declaringClass = this.getClass();
    }

    /**
     * <p>
     * This constructor uses the given declaring class to create an enum.
     * </p>
     * <p>
     * It is added in version 1.1 to support internal sub-classing.
     * </p>
     *
     * @param declaringClass
     *            the declaring class of this enum
     * @throws IllegalArgumentException
     *             if <code>declaringClass</code> is null or is not a sub-class of Enum
     * @since 1.1
     */
    protected Enum(Class declaringClass) {
        validateEnumClass(declaringClass);
        this.ordinal = init(declaringClass);
        this.declaringClass = declaringClass;
    }

    /**
     * <p>
     * Returns this object's ordinal value, which is a nonnegative number unique among all instances of the same Enum
     * subclass.
     * </p>
     *
     * @return this object's ordinal value
     * @since 1.0
     */
    public int getOrdinal() {
        return this.ordinal;
    }

    /**
     * <p>
     * Returns this object's declaring class, in which this enum value is declared as a static final field.
     * </p>
     *
     * @return this object's declaring class
     * @since 1.1
     */
    public Class getDeclaringClass() {
        return this.declaringClass;
    }

    /**
     * <p>
     * Returns this object's constant's name, which is the public static field name defined for this enum instance.
     * </p>
     * <p>
     * <b>Note</b>: This algorithm requires that this enum instance is defined as a public static field of its declaring
     * class, otherwise, this algorithm will return null.
     * </p>
     *
     * @return this object's constant's name
     * @throws SecurityException
     *             if some security error happens (@see {@link Class#getFields()})
     * @throws IllegalAccessException
     *             if error occurs while accessing the filed representing this enum (@see {@link Field#get(Object)})
     * @throws ExceptionInInitializerError
     *             if the initialization provoked by Field#get(Object) fails (@see {@link Field#get(Object)})
     * @since 1.1
     */
    public String getEnumName() throws IllegalAccessException {
        final List allEnums = (List) enumsByClass.get(declaringClass);
        // the returned allEnums will never be null
        Map instanceToName = (Map) enumNamesByClass.get(declaringClass);
        // If not found - add safely to map
        if (instanceToName == null) {
            synchronized (enumNamesByClass) {
                if (enumNamesByClass.containsKey(declaringClass)) {
                    // probably somebody has added just already in another thread
                    instanceToName = (Map) enumNamesByClass.get(declaringClass);
                } else {
                    instanceToName = new Hashtable(allEnums.size());
                    enumNamesByClass.put(declaringClass, instanceToName);
                }
            }
        }
        // In case if our Map was able to answer - return value ASAP without using additional synchronization
        final String ret = (String) instanceToName.get(this);
        if (ret != null) {
            return ret;
        }
        // If not - verify if our cache is up to date and try again
        // Worth case is that we will search twice for an item not available :o(
        synchronized (instanceToName) {
            if (instanceToName.size() != allEnums.size()) {
                // iterate through every fields in declaringClass
                Field[] fields = declaringClass.getFields();
                for (int i = 0; i < fields.length; ++i) {
                    try {
                        // if the field value is an Enum of this declaring class, add to the cache
                        Object value = fields[i].get(null);
                        if (value instanceof Enum && ((Enum) value).getDeclaringClass() == declaringClass) {
                            instanceToName.put(value, fields[i].getName());
                        }
                    } catch (NullPointerException e) {
                        // silently ignore, this can only occur when the field is not a STATIC field
                    }
                }
            }
        }

        // return the enum name from cache
        return (String) instanceToName.get(this);
    }

    /**
     * <p>
     * Find an instance of the given Enum declaring class whose ordinal equals the given value. If there is no such
     * instance, null is returned.
     * </p>
     *
     * @param ordinal
     *            value to search for
     * @param enumClass
     *            Class of Enum to search for
     * @return an instance of the given Enum subclass whose ordinal equals the given value, or null if there is no such
     *         instance
     * @throws IllegalArgumentException
     *             if enumClass is null or does not specify a subclass of Enum
     * @since 1.0
     */
    public static Enum getEnumByOrdinal(int ordinal, Class enumClass) {
        validateEnumClass(enumClass);
        final List allEnums = (List) enumsByClass.get(enumClass);
        if (allEnums == null) {
            return null;
        }
        if ((ordinal >= allEnums.size()) || (ordinal < 0)) {
            return null;
        }
        return (Enum) allEnums.get(ordinal);
    }

    /**
     * <p>
     * Find an instance of the given Enum declaring class whose String representation (as given by toString()) equals
     * the given String. If there is no such instance, this method returns null.
     * </p>
     * <p>
     * This method runs in <i>O(n)</i> for a first call for each class of Enums and construct a Map of toString() -&gt;
     * Enum values, next time it will answer queries for the same class in <i>O(1)</i>.
     * </p>
     *
     * @param stringValue
     *            String representation to search for
     * @param enumClass
     *            Class of Enum to search for
     * @return an instance of the given Enum subclass whose String representation equals the given String, or null if
     *         there is no such instance
     * @throws IllegalArgumentException
     *             if string value is null, if enumClass is null or does not specify a subclass of Enum
     * @since 1.0
     */
    public static Enum getEnumByStringValue(String stringValue, Class enumClass) {
        if (stringValue == null) {
            throw new IllegalArgumentException("Argument [stringValue] should not be null.");
        }
        try {
            return getEnum(stringValue, enumClass, false);
        } catch (IllegalAccessException e) {
            // this exception will never be thrown, we just ignore it and return a null
            return null;
        }
    }

    /**
     * <p>
     * Find an instance of the given Enum declaring class whose constant name (as given by getEnumName()) equals the
     * given String. If there is no such instance, this method returns null.
     * </p>
     * <p>
     * This method runs in <i>O(n)</i> for a first call for each class of Enums and construct a Map of getEnumName()
     * -&gt; Enum values, next time it will answer queries for the same class in <i>O(1)</i>.
     * </p>
     * <p>
     * <b>Note</b>: This algorithm requires that this enum instance is defined as a public static field of its declaring
     * class, otherwise, this algorithm will return null.
     * </p>
     *
     * @return an instance of the given Enum subclass whose constant name equals the given String, or null if there is
     *         no such instance
     * @param name
     *            the name of enum constant value
     * @param enumClass
     *            Class of Enum to search for
     * @throws IllegalArgumentException
     *             if string value is null or empty (trimed), if enumClass is null or does not specify a subclass of
     *             Enum
     * @throws SecurityException
     *             if some security error happens (@see {@link Class#getFields()})
     * @throws IllegalAccessException
     *             if error occurs while accessing the filed representing this enum (@see {@link Field#get(Object)})
     * @throws ExceptionInInitializerError
     *             if the initialization provoked by Field#get(Object) fails (@see {@link Field#get(Object)})
     * @since 1.1
     */
    public static Enum getEnumByName(String name, Class enumClass) throws IllegalAccessException {
        if (name == null) {
            throw new IllegalArgumentException("Argument [name] should not be null.");
        }
        if (name.trim().length() == 0) {
            throw new IllegalArgumentException("Argument [name] should not be empty (trimed).");
        }
        return getEnum(name, enumClass, true);
    }

    /**
     * <p>
     * Returns a sorted, unmodifiable List of all enumeration value instances of the given Enum subclass. Ordering is
     * determined by the compareTo() method; the default implementation here bases the ordering on ordinal value.
     * </p>
     * <p>
     * This method use <code>java.util.Collections.sort()</code> to sort value on each invocation as result it will
     * run in <i>O(n log n)</i>
     * </p>
     *
     * @param enumClass
     *            Class of Enum to get List
     * @return an sorted, unmodifiable List of all enumeration value instances of the given Enum subclass
     * @throws IllegalArgumentException
     *             if enumClass is null or does not specify a subclass of Enum
     * @since 1.0
     */
    public static List getEnumList(Class enumClass) {
        validateEnumClass(enumClass);
        final List allEnums = (List) enumsByClass.get(enumClass);
        if (allEnums == null) {
            return Collections.unmodifiableList(new ArrayList());
        }
        List retList = new ArrayList(allEnums);
        Collections.sort(retList);
        return Collections.unmodifiableList(retList);
    }

    /**
     * <p>
     * Defines the relative order of this enumeration value and another one of the same declaring class.
     * </p>
     * <p>
     * This implementation defines an ordering that is determined by ordinal values, and this method returns a negative
     * value, positive value or zero as this enumeration value's ordinal is less than, greater than, or equal to the
     * other's ordinal.
     * </p>
     * <p>
     * This method only defines an ordering between two enumeration values that are instances of the same declaring
     * class. Attempts to compare different classes, or non-Enum values, will result in a ClassCastException.
     * </p>
     * <p>
     * Subclasses may override this method if desired.
     * </p>
     * <p>
     * <b>Note</b>: In 1.1, new constraint that the two objects should have same declaring class is added.
     * </p>
     *
     * @param o
     *            enumeration Object to compare to
     * @return a negative value, positive value or zero as this enumeration value's ordinal is less than, greater than,
     *         or equal to the given enumeration value's ordinal
     * @throws ClassCastException
     *             if the given object is not an instance of the same class as this object
     * @since 1.0
     */
    public int compareTo(Object o) {
        if (!(o instanceof Enum) || ((Enum) o).declaringClass != declaringClass) {
            throw new ClassCastException("Unable to compare Enum for different types.");
        }
        int ret = ordinal - ((Enum) o).ordinal;
        return ret == 0 ? 0 : (ret < 0 ? -1 : 1);
    }

    /**
     * <p>
     * This method is defined so that Enum subclasses will work properly with Java's serialization/deserialization
     * mechanism.
     * </p>
     * <p>
     * Without it, deserializing a serialized instance of an Enum subclass would create a new instance separate from the
     * enumerated set of values, which is undesirable. This method ensures that the serialization mechanism actually
     * returns one of the existing instances, instead of some new object.
     * </p>
     * <p>
     * After deserialization, this object has the same class and ordinal value as the original serialized object. This
     * is enough information to look up the currently-existing instance that represents the same enumeration value. So,
     * this method simply looks up and returns that existing instance using getEnumByOrdinal().
     * </p>
     * <p>
     * This method can't be private or else it will not be available to the deserialization mechanism when deserializing
     * subclasses.
     * </p>
     * <p>
     * <b>Note</b>: In 1.1, getDeclaringClass() is used instead of getClass() in this method.
     * </p>
     *
     * @return an existing enumeration value corresponding to a serialized enumeration value
     * @see #getEnumByOrdinal(int, Class)
     * @since 1.0
     */
    protected final Object readResolve() {
        try {
            return Enum.getEnumByOrdinal(ordinal, declaringClass);
        } catch (IllegalArgumentException iae) {
            return null;
        }
    }

    /**
     * <p>
     * This private helper method is used to add this newly created enum to <code>enumsByClass</code> map, and return
     * the ordinal value assigned for it.
     * </p>
     *
     * @param declaringClass
     *            the declaringClass of this enum
     * @return the ordinal value assigned for this enum
     */
    private int init(Class declaringClass) {
        // ensure the list for specific Enum type exists
        synchronized (enumsByClass) {
            if (!enumsByClass.containsKey(declaringClass)) {
                enumsByClass.put(declaringClass, new ArrayList());
            }
        }
        // add [this], and return the ordinal value
        List allEnums = (List) enumsByClass.get(declaringClass);
        allEnums.add(this);
        return allEnums.size() - 1;
    }

    /**
     * <p>
     * This private helper method is used to get the Enum by name or string value. It is used in getEnumByName() and
     * getEnumByStringValue() methods.
     * </p>
     *
     * @param key
     *            the name or string value
     * @param enumClass
     *            the enum class
     * @param byName
     *            the flag indicating whether to use name as the key (when true), or use string value as the key (when
     *            false)
     * @return the Enum object specified by name or string value
     * @throws IllegalArgumentException
     *             if enumClass is null or does not specify a subclass of Enum
     * @throws SecurityException
     *             if some security error happens (@see {@link Class#getFields()})
     * @throws IllegalAccessException
     *             if error occurs while accessing the filed representing this enum (@see {@link Field#get(Object)})
     * @throws ExceptionInInitializerError
     *             if the initialization provoked by Field#get(Object) fails (@see {@link Field#get(Object)})
     * @see #getEnumByName(String, Class), #getEnumByStringValue(String, Class)
     */
    private static Enum getEnum(String key, Class enumClass, boolean byName) throws IllegalAccessException {
        validateEnumClass(enumClass);
        final List allEnums = (List) enumsByClass.get(enumClass);
        if (allEnums == null) {
            return null;
        }
        Map nameOrStringearchByClass = byName ? nameSearchByClass : stringSearchByClass;
        Map searchTable = (Map) nameOrStringearchByClass.get(enumClass);
        // If not found - add safely to map
        if (searchTable == null) {
            synchronized (nameOrStringearchByClass) {
                if (nameOrStringearchByClass.containsKey(enumClass)) {
                    // probably somebody has added just already in another thread
                    searchTable = (Map) nameOrStringearchByClass.get(enumClass);
                } else {
                    // Add new
                    searchTable = new Hashtable(allEnums.size());
                    nameOrStringearchByClass.put(enumClass, searchTable);
                }
            }
        }
        // In case if our Map was able to answer - return value ASAP without using additional synchronization
        final Enum ret = (Enum) searchTable.get(key);
        if (ret != null) {
            return ret;
        }
        // If not - verify if our cache is up to date and try again
        // Worth case is that we will search twice for an item not available :o(
        synchronized (searchTable) {
            if (searchTable.size() != allEnums.size()) {
                for (Iterator itr = allEnums.iterator(); itr.hasNext();) {
                    final Enum elem = (Enum) itr.next();
                    final String elemKey = byName ? elem.getEnumName() : elem.toString();
                    if (elemKey != null) {
                        searchTable.put(elemKey, elem);
                    }
                }
            }
        }
        return (Enum) searchTable.get(key);
    }

    /**
     * <p>
     * This method check if <code>enumClass</code> parameter value provided is valid. This mean that it can not be
     * <code>null</code> and must specify a subclass of Enum.
     * </p>
     *
     * @param enumClass
     *            the Class object to check if valid parameter for methods
     * @throws IllegalArgumentException
     *             if <code>enumClass</code> is <code>null</code> or does not specify a subclass of Enum
     * @see #Enum(Class), #getEnumByStringValue(String stringValue, Class enumClass) #getEnumByOrdinal(int, Class),
     *      #getEnumByName(String, Class), #getEnumList(Class enumClass),
     */
    private static final void validateEnumClass(Class enumClass) {
        if (enumClass == null) {
            throw new IllegalArgumentException("Argument [enumClass] should not be null.");
        }
        if (!(Enum.class.isAssignableFrom(enumClass))) {
            throw new IllegalArgumentException("Argument [enumClass] is not a subclass of Enum.");
        }
    }
}
