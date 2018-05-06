/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.collection.typesafeenum;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

/**
 * This class provides the demo usage of this component.
 *
 * @author TCSDEVELOPER
 * @version 1.1
 */
public class DemoTests extends TestCase {

    /**
     * Provides the demo usage of this component.
     *
     * @throws Exception throws to JUnit
     */
    public void testDemo() throws Exception {
        // First example is a basic usage scenario. The following Enum class is defined in the same package.
        // public class MyBool extends Enum {
        //     public static final MyBool FALSE = new MyBool();
        //     public static final MyBool TRUE = new MyBool();
        //     private MyBool() {}
        // }

        // Then the enumeration values can be accessed, compared, added to collections, serialized, etc:
        List list = new ArrayList();
        list.add(MyBool.TRUE);
        list.add(MyBool.FALSE);

        boolean notsame = MyBool.TRUE.equals(MyBool.FALSE);
        assertFalse("notsame must be false", notsame);
        int negative = MyBool.TRUE.compareTo(MyBool.FALSE);
        assertTrue("negative must be negative", negative < 0);

        // We can also get enum constant name (introduced in version 1.1):
        MyBool mybool = MyBool.FALSE;
        String enumConstantName = mybool.getEnumName();
        assertTrue("enum constant name must be 'FALSE'", enumConstantName.equals("FALSE"));

        // But the enumeration type can be more complex; in particular it can have JavaBean properties or any method at
        // all. In particular, a good toString() method is useful.

        // The following example also shows the usage of enum constants with constant-specifc class bodies, which is
        // supported by version 1.1. The following Enum class is defined in the same package.
        // public abstract class TypeEnum extends Enum {
        //     private final String name;
        //     public static final TypeEnum NUMBER = new TypeEnum("Number") {
        //         public boolean checkValue(Object val) {
        //             return val instanceof Number;
        //         }
        //     };
        //     public static final TypeEnum STRING = new TypeEnum("String") {
        //         public boolean checkValue(Object val) {
        //             return val instanceof String;
        //         }
        //     };
        //     private TypeEnum(String name) {
        //         super(TypeEnum.class);
        //         if (name == null) {
        //             throw new IllegalArgumentException("name shouldn't be null.");
        //         }
        //         this.name = name;
        //     }
        //     public abstract boolean checkValue(Object val);
        //     public String getName() {
        //         return this.name;
        //     }
        //     public String toString() {
        //         return getName();
        //     }
        // }

        // Enumeration classes can be looked up by ordinal value (for a given class, the first defined enumeration value
        // has ordinal 0 and each successive one is assigned the next higher ordinal value):
        TypeEnum numberType = (TypeEnum) Enum.getEnumByOrdinal(TypeEnum.NUMBER.getOrdinal(), TypeEnum.class);
        assertTrue("The TypeEnum is TypeEnum.NUMBER", numberType == TypeEnum.NUMBER);

        // Or even by String representation:
        TypeEnum stringType = (TypeEnum) Enum.getEnumByStringValue("String", TypeEnum.class);
        assertTrue("The stringType is TypeEnum.STRING", stringType == TypeEnum.STRING);

        // Or even by enum constant name (introduced in 1.1):
        stringType = (TypeEnum) Enum.getEnumByName("STRING", TypeEnum.class);
        assertTrue("The stringType is TypeEnum.STRING", stringType == TypeEnum.STRING);

        // Or all enumeration values can be retrieved as a sorted List:
        List typeList = Enum.getEnumList(TypeEnum.class);
        assertTrue("The typeList is [TypeEnum.NUMBER, TypeEnum.STRING]", typeList.size() == 2);
        assertTrue("The typeList is [TypeEnum.NUMBER, TypeEnum.STRING]",
            typeList.get(TypeEnum.NUMBER.getOrdinal()) == TypeEnum.NUMBER);
        assertTrue("The typeList is [TypeEnum.NUMBER, TypeEnum.STRING]",
            typeList.get(TypeEnum.STRING.getOrdinal()) == TypeEnum.STRING);
    }
}
