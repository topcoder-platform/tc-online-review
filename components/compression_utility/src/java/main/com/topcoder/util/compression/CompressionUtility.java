/*
 * Copyright (C) 2005-2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.compression;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;


/**
 * <p>
 * This utility class provides access to compression and decompression functionality. Callers can use this class with
 * various <code>Codec</code> implementations, which encapsulate compression/decompression algorithms. Data can be
 * compressed and decompressed to and from files and streams.
 * </p>
 *
 * @author srowen
 * @version 2.0.2
 *
 * @see Codec
 * @since 1.0
 */
public class CompressionUtility {
    /**
     * <p>
     * Size of block of input data processed at each step during compression.
     * </p>
     */
    private static final int COMPRESS_BUFFER_SIZE = 2048;

    /**
     * <p>
     * Size of block of input data processed at each step during decompression. It is a multiple of 3 for the benefit
     * of the {@link LZ77Decoder}, whose compressed format consists of triplets of bytes.
     * </p>
     */
    private static final int DECOMPRESS_BUFFER_SIZE = COMPRESS_BUFFER_SIZE - (COMPRESS_BUFFER_SIZE % 3);

    /**
     * <p>
     * <code>Codec</code> implementation used by this utility.
     * </p>
     */
    protected Codec codec;

    /**
     * <p>
     * Stream to write output to.
     * </p>
     */
    protected OutputStream outputStream;

    /**
     * <p>
     * Stream to read input from.
     * </p>
     */
    protected InputStream inputStream;

    // Constructors ----------------------------------------------------------

    /**
     * <p>
     * Constructs a new <code>CompressionUtility</code> that utilizes the named <code>Codec</code>. Output is written
     * to the given <code>OutputStream</code>.
     * </p>
     *
     * @param className fully-qualified name of <code>Codec</code> implementation to use
     * @param output stream to write output to
     *
     * @throws IllegalArgumentException if either <code>className</code> or <code>output</code> is <code>null</code>
     * @throws ClassNotFoundException if the class cannot be located
     * @throws InstantiationException if the class is an abstract class, an interface, an array class, a primitive
     *         type, or void; or if the instantiation fails for some other reason
     * @throws IllegalAccessException if the class or initializer is not accessible
     * @throws LinkageError if the linkage fails
     * @throws ExceptionInInitializerError if the initialization provoked by this method fails
     *
     * @since 1.0
     */
    public CompressionUtility(final String className, final OutputStream output)
        throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        if (className == null) {
            throw new IllegalArgumentException("Class name cannot be null");
        }

        if (output == null) {
            throw new IllegalArgumentException("Output cannot be null");
        }

        initialize(className, output);
    }

    /**
     * <p>
     * Constructs a new <code>CompressionUtility</code> that utilizes the named <code>Codec</code>. Output is written
     * to the given file.
     * </p>
     *
     * @param className fully-qualified name of <code>Codec</code> implementation to use
     * @param output file to write output to
     *
     * @throws IllegalArgumentException if either <code>className</code> or <code>output</code> is <code>null</code>
     * @throws FileNotFoundException if the given output file does not exist
     * @throws ClassNotFoundException if the class cannot be located
     * @throws InstantiationException if the class is an abstract class, an interface, an array class, a primitive
     *         type, or void; or if the instantiation fails for some other reason
     * @throws IllegalAccessException if the class or initializer is not accessible
     * @throws LinkageError if the linkage fails
     * @throws ExceptionInInitializerError if the initialization provoked by this method fails
     *
     * @since 1.0
     */
    public CompressionUtility(final String className, final File output)
        throws ClassNotFoundException, InstantiationException, IllegalAccessException, FileNotFoundException {
        if (className == null) {
            throw new IllegalArgumentException("Class name cannot be null");
        }

        if (output == null) {
            throw new IllegalArgumentException("Output file cannot be null");
        }

        initialize(className, new FileOutputStream(output));
    }

    private void initialize(final String className, final OutputStream outputStream)
        throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        // Constructors call this method instead of each other, since each
        // needs to check illegal arguments first, instead of calling
        // "this()"
        this.codec = createCodec(className);
        this.outputStream = outputStream;
    }

    /**
     * <p>
     * Returns an instance of the indicated <code>Codec</code> implementation.
     * </p>
     *
     * @param className fully-qualified name of <code>Codec</code> implementation to use
     *
     * @return an instance of the named <code>Codec</code> implementation
     *
     * @throws IllegalArgumentException if <code>className</code> is <code>null</code>, or does not name a class that
     *         implements <code>Codec</code>
     * @throws ClassNotFoundException if the class cannot be located
     * @throws InstantiationException if the class is an abstract class, an interface, an array class, a primitive
     *         type, or void; or if the instantiation fails for some other reason
     * @throws IllegalAccessException if the class or initializer is not accessible
     * @throws LinkageError if the linkage fails
     * @throws ExceptionInInitializerError if the initialization provoked by this method fails
     */
    public static Codec createCodec(final String className)
        throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        if (className == null) {
            throw new IllegalArgumentException("Class name cannot be null");
        }

        final Object codec = Class.forName(className).newInstance();

        if (!(codec instanceof Codec)) {
            throw new IllegalArgumentException(className + " is not a Codec");
        }

        return (Codec) codec;
    }

    // Compression methods --------------------------------------------------

    /**
     * <p>
     * Initiates compression of the data from the given input stream.
     * </p>
     *
     * @param input stream whose data is to be compressed
     *
     * @throws IllegalArgumentException if <code>input</code> is <code>null</code>
     * @throws IOException if an I/O exception occurs while compressing, either while reading or writing data
     */
    public void compress(final InputStream input) throws IOException {
        if (input == null) {
            throw new IllegalArgumentException("Input stream cannot be null");
        }

        this.inputStream = input;
        compress();
    }

    /**
     * <p>
     * Initiates compression of the data from the given file.
     * </p>
     *
     * @param input to compress
     *
     * @throws IllegalArgumentException if <code>input</code> is <code>null</code>
     * @throws IOException if an I/O exception occurs while compressing, either while reading or writing data
     * @throws FileNotFoundException if the given input file does not exist
     */
    public void compress(final File input) throws IOException {
        if (input == null) {
            throw new IllegalArgumentException("Input file cannot be null");
        }

        this.inputStream = new FileInputStream(input);
        compress();
        this.inputStream.close();
    }

    /**
     * <p>
     * Initiates compression of the data from the given <code>StringBuffer</code>. Its characters are converted to
     * bytes according to the default character encoding, and compressed.
     * </p>
     *
     * @param input <code>StringBuffer</code> to compress
     *
     * @throws IllegalArgumentException if <code>input</code> is <code>null</code>
     * @throws IOException if an I/O exception occurs while compressing, while writing data
     */
    public void compress(final StringBuffer input) throws IOException {
        if (input == null) {
            throw new IllegalArgumentException("Input cannot be null");
        }

        compress(new ByteArrayInputStream(input.toString().getBytes()));
    }

    // Decompression methods ------------------------------------------------

    /**
     * <p>
     * Initiates decompression of the data from the given input stream.
     * </p>
     *
     * @param input stream whose data is to be decompressed
     *
     * @throws IllegalArgumentException if <code>input</code> is <code>null</code>
     * @throws IOException if an I/O exception occurs while decompressing, either while reading or writing data
     * @throws DataFormatException if the input data is not valid
     */
    public void decompress(final InputStream input) throws IOException, DataFormatException {
        if (input == null) {
            throw new IllegalArgumentException("Input stream cannot be null");
        }

        this.inputStream = input;
        decompress();
    }

    /**
     * <p>
     * Initiates decompression of the data from the given file.
     * </p>
     *
     * @param input file to decompress
     *
     * @throws IllegalArgumentException if <code>input</code> is <code>null</code>
     * @throws IOException if an I/O exception occurs while decompressing, either while reading or writing data
     * @throws FileNotFoundException if the given input file does not exist
     * @throws DataFormatException if the input data is not valid
     */
    public void decompress(final File input) throws IOException, DataFormatException {
        if (input == null) {
            throw new IllegalArgumentException("Input file cannot be null");
        }

        this.inputStream = new FileInputStream(input);
        decompress();
        this.inputStream.close();
    }

    /**
     * <p>
     * Initiates decompression of the data from the given <code>StringBuffer</code>. Its characters are converted to
     * bytes according to the default character encoding, and decompressed.
     * </p>
     *
     * @param input <code>StringBuffer</code> to decompress
     *
     * @throws IllegalArgumentException if <code>input</code> is <code>null</code>
     * @throws IOException if an I/O exception occurs while decompressing, while writing data
     * @throws DataFormatException if the input data is not valid
     */
    public void decompress(final StringBuffer input) throws IOException, DataFormatException {
        if (input == null) {
            throw new IllegalArgumentException("Input cannot be null");
        }

        decompress(new ByteArrayInputStream(input.toString().getBytes()));
    }

    /**
     * <p>
     * Closes the underlying <code>OutputStream</code>.
     * </p>
     *
     * @throws IOException if an I/O exception occurs while closing the stream
     */
    public void close() throws IOException {
        this.outputStream.flush();
        this.outputStream.close();
    }

    /**
     * <p>
     * Worker method that compresses all data from the internal <code>InputStream</code> and writes it to the internal
     * <code>OutputStream</code>, using this object's <code>Codec</code>.
     * </p>
     *
     * @throws IOException if an I/O exception occurs while compressing, while reading or writing data
     */
    protected void compress() throws IOException {
        final Deflater deflater = codec.createDeflater();
        final byte[] in = new byte[COMPRESS_BUFFER_SIZE];
        final byte[] out = new byte[COMPRESS_BUFFER_SIZE];

        boolean finish = false;
        while (!finish) {
            int bytesRead = inputStream.read(in);

            if (bytesRead > 0) {
                deflater.setInput(in, 0, bytesRead);
            }

            if (bytesRead < in.length) {
                // last buffer
                deflater.finish();
                finish = true;
            }

            // Keep encoding until input is exhausted, since it's possible
            // that it does not compress into the allocated buffer during
            // the first pass
            int bytesEncoded;

            while ((bytesEncoded = deflater.deflate(out)) > 0) {
                outputStream.write(out, 0, bytesEncoded);
            }
        }

        deflater.end();
    }

    /**
     * <p>
     * Worker method that decompresses all data from the internal <code>InputStream</code> and writes it to the
     * internal <code>OutputStream</code>, using this object's <code>Codec</code>.
     * </p>
     *
     * @throws IOException if an I/O exception occurs while decompressing, while reading or writing data
     * @throws DataFormatException if the input data is not valid
     */
    protected void decompress() throws IOException, DataFormatException {
        final Inflater inflater = codec.createInflater();
        final byte[] in = new byte[DECOMPRESS_BUFFER_SIZE];
        final byte[] out = new byte[DECOMPRESS_BUFFER_SIZE];

        int bytesRead;

        while ((bytesRead = inputStream.read(in)) > 0) {
            inflater.setInput(in, 0, bytesRead);

            // Keep decoding until input is exhausted, since it's possible
            // that it does not decompress into the allocated buffer during
            // the first pass
            int bytesDecoded;

            while ((bytesDecoded = inflater.inflate(out)) > 0) {
                outputStream.write(out, 0, bytesDecoded);
            }
        }

        inflater.end();
    }
}






