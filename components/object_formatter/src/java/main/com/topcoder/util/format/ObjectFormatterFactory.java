/*
 * ObjectFormatterFactory.java
 *
 * Copyright (C) 2002, TopCoder, Inc. All rights reserved
 *
 */

package com.topcoder.util.format;

import java.util.Date;

/**
 * A factory class for creating <code>ObjectFormatter</code>s. <p>
 *
 * @author KurtSteinkraus
 * @author garyk
 * @version 1.0
 **/
public class ObjectFormatterFactory {
    
    /**
     * Private empty contructor to prevent this factory class from 
     * being instantiated
     **/
    private ObjectFormatterFactory() {}

    /**
     * Returns an empty formatter, which will not be able to format
     * any object without being supplied with the appropriate
     * <code>ObjectFormatMethod</code>.
     *
     * @return the empty formatter
     **/
    public static ObjectFormatter getEmptyFormatter() {
        return new ObjectFormatterImplementer();
    }
    
    /**
     * Returns a formatter that converts an object into a formatted
     * string by calling the object's <code>toString</code> method.
     * This formatter associates <code>Object</code>s with the format
     * method returned by
     * <code>FormatMethodFactory.getDefaultObjectFormatMethod</code>.
     *
     * @return the plain formatter
     **/
    public static ObjectFormatter getPlainFormatter() {
        ObjectFormatter of = new ObjectFormatterImplementer();
        of.setFormatMethodForClass(Object.class, 
                FormatMethodFactory.getDefaultObjectFormatMethod(), true);
		
        return of;
    }
    
    /**
     * Returns a formatter that converts an object into a prettified
     * formatted string.  This formatter returns the
     * <code>Object</code> equivalent of {@link
     * PrimitiveFormatterFactory#getPrettyFormatter}.  It creates an
     * association for each primitive type's wrapper class.  It also
     * creates an association for <code>Date</code>s, using the format
     * method returned by {@link
     * FormatMethodFactory#getDefaultDateFormatMethod} when using the
     * default formatting string.
     *
     * @return the pretty formatter
     **/
    public static ObjectFormatter getPrettyFormatter() {
        ObjectFormatter of = new ObjectFormatterImplementer();

        final PrimitiveFormatter pf =             
                PrimitiveFormatterFactory.getPrettyFormatter();

        /*
         * create the ObjectFormatMethod for each primitive type's wrapper 
         * class using the default format in the pretty PrimitiveFormatter
         */
        ObjectFormatMethod ofmInteger = new ObjectFormatMethod() {
            public String format(Object obj) {
                if (!(obj instanceof Integer)) {
                    throw new IllegalArgumentException("expected Integer");
                }

                return pf.format(((Integer)obj).intValue());
            }
        };

        ObjectFormatMethod ofmShort = new ObjectFormatMethod() {
            public String format(Object obj) {
                if (!(obj instanceof Short)) {
                    throw new IllegalArgumentException("expected Short");
                }

                return pf.format(((Short)obj).shortValue());
            }
        };

        ObjectFormatMethod ofmLong = new ObjectFormatMethod() {
            public String format(Object obj) {
                if (!(obj instanceof Long)) {
                    throw new IllegalArgumentException("expected Long");
                }

                return pf.format(((Long)obj).longValue());
            }
        };

        ObjectFormatMethod ofmByte = new ObjectFormatMethod() {
            public String format(Object obj) {
                if (!(obj instanceof Byte)) {
                    throw new IllegalArgumentException("expected Byte");
                }

                return pf.format(((Byte)obj).byteValue());
            }
        };

        ObjectFormatMethod ofmChar = new ObjectFormatMethod() {
            public String format(Object obj) {
                if (!(obj instanceof Character)) {
                    throw new IllegalArgumentException("expected Character");
                }

                return pf.format(((Character)obj).charValue());
            }
        };	

        ObjectFormatMethod ofmFloat = new ObjectFormatMethod() {
            public String format(Object obj) {
                if (!(obj instanceof Float)) {
                    throw new IllegalArgumentException("expected Float");
                }

                return pf.format(((Float)obj).floatValue());
            }
        };

        ObjectFormatMethod ofmDouble = new ObjectFormatMethod() {
            public String format(Object obj) {
                if (!(obj instanceof Double)) {
                    throw new IllegalArgumentException("expected Double");
                }

                return pf.format(((Double)obj).doubleValue());
            }
        };
		
        /*
         * associate each primitive type's wrapper class with its  
         * ObjectFormatMethod and Date class with its DefaultDateFormatMethod
         */
        of.setFormatMethodForClass(Integer.class, ofmInteger, true);
        of.setFormatMethodForClass(Short.class, ofmShort, true);
        of.setFormatMethodForClass(Long.class, ofmLong, true);
        of.setFormatMethodForClass(Byte.class, ofmByte, true);
        of.setFormatMethodForClass(Character.class, ofmChar, true);
        of.setFormatMethodForClass(Float.class, ofmFloat, true);
        of.setFormatMethodForClass(Double.class, ofmDouble, true);
        of.setFormatMethodForClass(Date.class, 
                FormatMethodFactory.getDefaultDateFormatMethod(null), true);

        return of;
    }
}