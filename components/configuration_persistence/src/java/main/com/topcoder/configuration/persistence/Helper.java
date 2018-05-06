/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.configuration.persistence;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.WritableByteChannel;

/**
 * <p>
 * This is a helper class for the component to check argument.
 * </p>
 * <p>
 * <b>Thread Safety</b>: This class is thread safety because it is immutable.
 * </p>
 *
 * @author rainday
 * @version 1.0
 */
final class Helper {

    /**
     * The return string in current system.
     */
    static String RETURN_STRING = System.getProperty("line.separator");

    /**
     * <p>
     * The private constructor prevents creation.
     * </p>
     */
    private Helper() {
        // empty
    }

    /**
     * <p>
     * Checks whether the given argument is null.
     * </p>
     *
     * @param argument
     *            the argument to be checked
     * @param name
     *            the name of the argument
     * @throws IllegalArgumentException
     *             if the argument is null
     */
    static void checkNotNull(Object argument, String name) {
        if (argument == null) {
            throw new IllegalArgumentException(name + " should not be null.");
        }
    }

    /**
     * <p>
     * Checks whether the given string is null or empty.
     * </p>
     *
     * @param argument
     *            the string to be checked
     * @param name
     *            the name of the argument
     * @throws IllegalArgumentException
     *             if the argument is null or empty
     */
    static void checkNotNullOrEmpty(String argument, String name) {
        checkNotNull(argument, name);
        if (argument.trim().length() == 0) {
            throw new IllegalArgumentException(name + " should not be empty.");
        }
    }
    
    /**
     * <p>
     * Converts a path string written with the local system-dependent separator sequence to a path name written with
     * "/" as it separator
     * </p>
     * 
     * @param path the path String to process, written with the local separator character
     * @return a String representing the same path as the input string, but using "/" as its separator  
     */
    static String changeSeparator(String path) {
        if (File.separator.equals("/")) {
            return path;
        } else {
            /* 
             * Replace single backslashes in the file separator with quadruple backslashes, forming a string that can be
             * compiled into a regex pattern matching the separator as a literal.  Quoting with \Q...\E doesn't work for
             * this case under Java 1.4, and the Java 1.4 version of Pattern doesn't have a quote() method.
             */
            String pattern = File.separator.replaceAll("\\\\", "\\\\\\\\");
            
            return path.replaceAll(pattern, "/");
        }
    }
    
    /**
     * If possible, creates a RandomAccessFile representing the file referenced by the specified resource URL.
     *  
     * @param resourceUrl the URL of the file.  If not resolvable as a file accessible on the file system (e.g.
     * if it references a file inside a jar archive) then this method is likely to fail (return null).
     * 
     * @return a RandomAccessFile representing the resource file, or null if none can be created 
     */
    static RandomAccessFile getFile(URL resourceUrl) {
        if (resourceUrl.getProtocol() == "file") {
            try {
                String fileName = URLDecoder.decode(resourceUrl.getFile(), "UTF-8");

                return new RandomAccessFile(fileName, "rw");
            } catch (UnsupportedEncodingException uee) {
                // Can't happen: All Java implementations must support UTF-8
                // fall through
            } catch (FileNotFoundException e) {
                // fall through
            }
        }
        
        return null;
    }
    
    /**
     * Obtains an exclusive lock on the specified file and buffers its full contents in memory, returning an
     * InputStream from which the contents can be read.  The lock is released before this method returns.
     *  
     * @param file a RandomAccessFile to read
     * @return an InputStream from which the file's contents (at the time this method was called) may be read
     * @throws IOException if an exclusive lock cannot be obtained, or if an I/O error occurs while reading the file
     */
    static InputStream bufferFile(RandomAccessFile file) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FileChannel channel = file.getChannel();
        
        try {
            FileLock locker = channel.tryLock();
            
            if (locker == null) {
                throw new IOException("Could not lock configuration file");
            } else {
                try {
                    WritableByteChannel bufferChannel = Channels.newChannel(baos);
                    
                    channel.transferTo(0L, channel.size(), bufferChannel);
                    bufferChannel.close();  // closes the ByteArrayOutputStream as well, but that has no effect
                } finally {
                    locker.release();
                }
            }
        } finally {
            channel.close();
        }
        
        return new ByteArrayInputStream(baos.toByteArray());
    }
}