/*
 * Copyright (c) 2005, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.cache;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.regex.PatternSyntaxException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import com.topcoder.util.compression.CompressionUtility;

/**
 * This is a CompressionUtility based compression handler. This means that it utilizes internally
 * the CompressionUtility 2.0 TopCoder component to do the actual compression/decompression.
 * <p>The instance of this class have two ways to determine if it is authorized to process objects
 * of the given type in method compress. First is list of String objects representing names of acceptable class.
 * If given object is instance of any of this class or it's descendant, then it is accepted. Second way
 * is using regular expression. If class name matches regular expression then it is accepted. The list of
 * accepted class names and regular expression are set in constructor. Calling method compress with unacceptable
 * object leads to throwing TypeNotMatchedException.
 * <p>This class is thread-safe since there is no modifiable state.
 *
 * @author  AleaActaEst, rem
 * @version 2.0
 * @since   2.0
 */
public class DefaultCompressionHandler implements CompressionHandler {

    /**
     * Default codec class name.
     * <p>Access Details
     * - Used in the constructor for default intialization.
     */
    private static final String DEFAULT_CODEC = "com.topcoder.util.compression.DeflateCodec";

    /**
     * Default ObjectByteConverter instance to be used if one is not supplied. This is guaranteed to work
     * 'out of the box' since it relies on JVM's built-in serialization model.
     * <p>Access Details
     * - Used in the constructor for default initialization.
     */
    private static final ObjectByteConverter DEFAULT_OBJECTBYTE_CONVERTER = new SerializableObjectByteConverter();

    /**
     * Default accepted class type to be used if one is not supplied. This means that by default we can use
     * this handler to compress any object.
     * <p>Access Details
     * - Used in the constractor for default initialization.
     */
    private static final String DEFAULT_ACCEPTED_TYPE = "java.lang.Object";

    /**
     * This constant is used in constructor DefaultCompressionHandler(Map).
     * It represents name for parameter, containing name of codec class.
     */
    private static final String PARAMETER_CODEC = "CompressionCodecClass";

    /**
     * This constant is used in constructor DefaultCompressionHandler(Map).
     * It represents name for parameter, containing regular expression to match.
     */
    private static final String PARAMETER_TYPE_MATCH_REGEX = "AcceptedObjectTypesRegex";

    /**
     * This constant is used in constructor DefaultCompressionHandler(Map).
     * It represents name for parameter, containing name of class which implements
     * interface ObjectByteConverter.
     */
    private static final String PARAMETER_CONVERTER = "ObjectByteConverterClass";

    /**
     * This constant is used in constructor DefaultCompressionHandler(Map).
     * It represents name for parameter, containing list of accepted class names.
     */
    private static final String PARAMETER_ACCEPTED_TYPES = "AcceptedObjectTypes";

    /**
     * Represents the list of type names that will be accepted for compression by this handler. These are going to
     * be simply Strings such as "com.acme.MovieInfo" which describe a type. This list will be used to check
     * if this handler will process the compression of the input object.
     * <p>Valid input Range
     * - it can be set to null or an empty List.
     */
    private List acceptedTypeNameList = null;

    /**
     * Represents the codec to be used in compression/decompression process. During the
     * compression/decompression process a com.topcoder.util.compression.CompressionUtility object will be
     * created using this codec and will then be used to actually proicess the data coming in.
     * <p>Valid input Range
     * - It can not be set to null or an empty String must be a valid name of a codec.
     */
    private String codecClassName = null;

    /**
     * Represents the converter used to obtain a stream of bytes representation of an object which is needed
     * for compression. As well as obtaining an object back from a byte array representation.
     * <p>Valid input Range
     * - It can not be set to null and must point to a valid converter.
     */
    private ObjectByteConverter objectByteConverter = null;

    /**
     * Represents a precompiled regex pattern. This is done for efficiency. If the class will be called many
     * times then the regular expression will be compiled only once and remembered.
     */
    private Pattern pattern = null;

    /**
     * Creates a default, out-of-the-box compression handler.
     * Simply calls this(null, null, null, null).
     */
    public DefaultCompressionHandler() {
        init(null, null, null, null);
    }

    /**
     * Creates an instance of BaseCompressionHandler initialized with the input values.
     * It is allowed for every parameter to be null. In this case the default values are used.
     * <p>Default values for parameters are the following:
     * <ul>
     * <li>codec - if it is empty or null, then set to "com.topcoder.util.compression.DeflateCodec".
     * <li>typeMatchRegex - if it is empty or null, then set to null.
     * <li>converter - if it is null, then set to new SerializableObjectByteConverter().
     * <li>acceptedTypes - if it is null or empty, then
     *     <ul>
     *     <li> if typeMatchRegex is empty or null, then set to List containing one element
     *          "java.lang.Object", which means that method compress will accept objects of any type.
     *     <li> if typeMatchRegex is not empty and not null, then set to empty List.
     *     </ul>
     * </ul>
     *
     * @param  codec class name of a codec to use.
     * @param  typeMatchRegex regular expression to match.
     * @param  converter objectbyte converter.
     * @param  acceptedTypes a list of class names of classes that this compression handler can process.
     * @throws IllegalArgumentException if the typeMatchRegex string is an invalid regular expression.
     *         The java.util.regex.PatternSyntaxException instance will be wrapped into this exception.
     * @throws IllegalArgumentException if any element of acceptedTypes is not String.
     */
    public DefaultCompressionHandler(String codec, String typeMatchRegex, ObjectByteConverter converter,
            List acceptedTypes) {
        init(codec, typeMatchRegex, converter, acceptedTypes);
    }

    /**
     * Creates new instance of this class. This constructor is similar to previous except that all parameters
     * are stored in map.
     *
     * @param  map maps parameters' names to Strings and Lists of Strings.
     * @throws NullPointerException if map is null.
     * @throws IllegalArgumentException if the typeMatchRegex string is an invalid regular expression.
     *         The java.util.regex.PatternSyntaxException instance will be wrapped into this exception.
     */
    public DefaultCompressionHandler(Map map) {
        if (map == null) {
            throw new NullPointerException("'map' can not be null.");
        }

        Object obj;

        // find codec
        String codec = null;
        obj = map.get(PARAMETER_CODEC);
        if (obj != null && obj instanceof String) {
            codec = (String) obj;
        }

        // find typeMatchRegex
        String typeMatchRegex = null;
        obj = map.get(PARAMETER_TYPE_MATCH_REGEX);
        if (obj != null && obj instanceof String) {
            typeMatchRegex = (String) obj;
        }

        // find converter
        ObjectByteConverter converter = null;
        obj = map.get(PARAMETER_CONVERTER);
        if (obj != null && obj instanceof String) {
            String className = (String) obj;
            converter = null;
            try {
                Class cl = Class.forName(className);
                Constructor ctor = cl.getConstructor(new Class[0]);
                converter = (ObjectByteConverter) ctor.newInstance(new Object[0]);
            } catch (ClassNotFoundException ex) {
                // ignore this exception.
            } catch (NoSuchMethodException ex) {
                // ignore this exception.
            } catch (InstantiationException ex) {
                // ignore this exception.
            } catch (IllegalAccessException ex) {
                // ignore this exception.
            } catch (InvocationTargetException ex) {
                // ignore this exception.
            }
        }

        // find acceptedTypes
        List acceptedTypes = null;
        obj = map.get(PARAMETER_ACCEPTED_TYPES);
        if (obj != null && obj instanceof String) {
            List list = new ArrayList();
            list.add(obj);
            obj = list;
        } else if (obj != null && obj instanceof List) {
            acceptedTypes = new ArrayList((Collection) obj);
        }

        init(codec, typeMatchRegex, converter, acceptedTypes);
    }

    /**
     * This is utility method called from constructors.
     *
     * @param  codec class name of a codec to use.
     * @param  typeMatchRegex regular expression to match.
     * @param  converter objectbyte converter.
     * @param  acceptedTypes a list of class names of classes that this can process.
     * @throws IllegalArgumentException if the typeMatchRegex string is an invalid regular expression.
     *         The java.util.regex.PatternSyntaxException instance will be wrapped into this exception.
     * @throws IllegalArgumentException if any element of acceptedTypes is not String.
     */
    private void init(String codec, String typeMatchRegex, ObjectByteConverter converter,
            List acceptedTypes) {
        // set this.codecClassName
        if (codec == null || codec.trim().length() == 0) {
            this.codecClassName = DEFAULT_CODEC;
        } else {
            this.codecClassName = codec;
        }

        // set this.objectByteConverter
        if (converter == null) {
            this.objectByteConverter = DEFAULT_OBJECTBYTE_CONVERTER;
        } else {
            this.objectByteConverter = converter;
        }

        // set this.acceptedTypeNameList
        // If typeMatchRegex and acceptedTypes are both undefined,
        //     then initialize regex with null and acceptedTypes to list containing one value -
        //     DEFAULT_ACCEPTED_TYPE
        // else
        //     set defined parameters to passed values and undefined parameters to default values.
        boolean typeMatchRegexDefined, acceptedTypesDefined;
        typeMatchRegexDefined = (typeMatchRegex != null && typeMatchRegex.trim().length() > 0);
        acceptedTypesDefined = (acceptedTypes != null && !acceptedTypes.isEmpty());
        if (!typeMatchRegexDefined && !acceptedTypesDefined) {
            this.acceptedTypeNameList = new ArrayList();
            this.acceptedTypeNameList.add(DEFAULT_ACCEPTED_TYPE);
        } else {
            if (acceptedTypesDefined) {
                this.acceptedTypeNameList = new ArrayList();
                for (Iterator it = acceptedTypes.iterator(); it.hasNext();) {
                    Object obj = it.next();
                    if (!(obj instanceof String)) {
                        throw new IllegalArgumentException("'acceptedType' should only contain strings.");
                    }
                    this.acceptedTypeNameList.add(obj);
                }
            } else {
                this.acceptedTypeNameList = null;
            }
        }

        // if typeMatchRegex is defined, then set field this.pattern and check that regular expression is correct.
        if (typeMatchRegexDefined) {
            try {
                pattern = Pattern.compile(typeMatchRegex);
            } catch (PatternSyntaxException ex) {
                throw new IllegalArgumentException("'typeMatchRegex' should contain correct regular expression.");
            }
        } else {
            pattern = null;
        }
    }

    /**
     * Compresses the value passed in to it and returns the compressed value.
     * Uses CompressionUtility 2.0 TopCoder component to do the actual compression.
     * <p>Returns empty array for null values.
     *
     * @param  value object to be compressed.
     * @return byte array representing the compression results.
     * @throws CompressionException with any cause chained if during the compression process we
     *         encounter any exceptions.
     * @throws TypeNotMatchedException if we can not match the input object to a 'known' type.
     */
    public byte[] compress(Object value) throws CompressionException {
        if (value == null) {
            return new byte[0];
        }
        // determine if we can compress the value.
        // 1. Check acceptedTypeNameList;
        // 2. Check typeMatchRegex.
        boolean canCompress = false;
        if (acceptedTypeNameList != null) {
            for (Iterator it = acceptedTypeNameList.iterator(); it.hasNext(); ) {
                String className = (String) it.next();
                // wrap any exeption into CompressionException.
                try {
                    if (Class.forName(className).isInstance(value)) {
                        canCompress = true;
                        break;
                    }
                } catch (ClassNotFoundException ex) {
                    // ignore exception.
                }
            }
        }
        if (!canCompress && pattern != null) {
            Matcher matcher = pattern.matcher(value.getClass().getName());
            canCompress = matcher.matches();
        }

        // if can't compress object, then throw TypeNotMatchedException.
        if (!canCompress) {
            throw new TypeNotMatchedException("Can't compress given object.");
        }

        byte[] result = null;
        // wrap any exeption into CompressionException.
        try {
            // turn value into byte array using ObjectByteConverter.
            byte[] valueAsByteArray = objectByteConverter.getBytes(value);
            // define input stream for compression.
            InputStream inputStream = new ByteArrayInputStream(valueAsByteArray);
            // define destination of the compression.
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            // create deflater.
            CompressionUtility cu = new CompressionUtility(codecClassName, outputStream);
            // do compression.
            cu.compress(inputStream);
            // get compressed value.
            result = outputStream.toByteArray();
        } catch (Exception ex) {
            throw new CompressionException("Exeption was thrown when trying to compress given value.", ex);
        }
        return result;
    }

    /**
     * Decompresses the value passed in to it and returns the original object.
     * <p>Returns null for null or empty array.
     *
     * @param  compressedValue a byte array representing a compressed value.
     * @return the original uncompressed object.
     * @throws CompressionException with any cause chained if during the decompression process we
     *         encounter any exceptions.
     */
    public Object decompress(byte[] compressedValue) throws CompressionException {
        if (compressedValue == null || compressedValue.length == 0) {
            return null;
        }
        Object result = null;
        // wrap any exception into CompressionException.
        try {
            // create output stream for decompressed value.
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            // create input stream for compression utility.
            InputStream inputStream = new ByteArrayInputStream(compressedValue);
            // create compression utility instance.
            CompressionUtility cu = new CompressionUtility(codecClassName, outputStream);
            // do decompression.
            cu.decompress(inputStream);
            // get decompressed value as byte array.
            byte[] decompressedValue = outputStream.toByteArray();
            // turn value from byte array to Object.
            result = objectByteConverter.getObject(decompressedValue);
        } catch (Exception ex) {
            throw new CompressionException("Exception was thrown when trying to decompress given value.", ex);
        }
        return result;
    }
}
