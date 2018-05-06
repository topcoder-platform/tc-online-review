/*
 * Copyright (C) 2003, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.URL;

/**
 * <p>
 * An extension of <code>ConfigProperties</code> to be used to maintain the properties list using .properties files.
 * </p>
 *
 * <p>
 * <em>Changes in 2.2:</em>
 * <ol>
 * <li>Made save() and load() methods synchronized.</li>
 * <li>Added support for "IsRefreshable" configuration parameter.</li>
 * <li>Added thread safety information.</li>
 * </ol>
 * </p>
 *
 * <p>
 * <strong>Thread Safety: </strong> This class is mutable, but partially thread safe since its save() and load()
 * methods are synchronized. I.e. its save() and load() methods can be safely called from multiple threads at a time.
 * </p>
 *
 * @author ilya, isv, WishingBone, saarixx, sparemax
 * @version 2.2
 */
class PropConfigProperties extends ConfigProperties {

    /**
     * A source of properties.
     */
    private URL source = null;

    /**
     * An <code>PrintWriter</code> used to output the properties.
     */
    private PrintWriter writer = null;

    /**
     * A private no-arg constructor (for clone).
     */
    private PropConfigProperties() {
    }

    /**
     * Creates a new PropConfigProperties from data in the InputStream.
     * Reads a property list (key and element pairs) from the input stream. The
     * stream is assumed to be using the ISO 8859-1 character encoding.
     * Every property occupies one line of the input stream. Each line is
     * terminated by a line terminator (\n or \r or \r\n). Lines from the input
     * stream are processed until end of file is reached on the input stream.
     *
     * A line that contains only whitespace or whose first non-whitespace
     * character is an ASCII # or ! is ignored (thus, # or ! indicate comment
     * lines).
     *
     * @param  source a source of configuration properties
     * @throws ConfigParserException if any I/O error occurs during the reading
     *         data from given <code>InputStream</code>
     * @throws IOException if any I/O error occurs while reading from source
     *         file
     */
    PropConfigProperties(URL source) throws IOException {
        this.source = source;
        load();
    }

    /**
     * Write a property node to output.
     *
     * @param  property the property node
     * @param  key the parent key
     */
    private void writeProperty(Property property, String key) {
        // construct current key
        if (key.equals("")) {
            key = property.getName();
        } else {
            key += "." + property.getName();
        }

        // write comments
        List<String> comments = property.getComments();
        if (comments != null) {
            for (Iterator<String> itr = comments.iterator(); itr.hasNext();) {
                writer.println(itr.next());
            }
        }

        // write values
        String[] values = property.getValues();
        if (values != null && values.length > 0) {
            writer.print(key);
            writer.print(property.getSeparator());
            writer.println(mergeEscaped(values, getListDelimiter()));
        }

        // write sub properties
        List<Property> subproperties = property.list();
        for (Iterator<Property> itr = subproperties.iterator(); itr.hasNext();) {
            writeProperty(itr.next(), key);
        }
    }

    /**
     * <p>
     * Saves the data(properties and their values) from properties tree into source .properties file.
     * </p>
     *
     * <p>
     * <em>Changes in 2.2:</em>
     * <ol>
     * <li>Made this method synchronized.</li>
     * <li>Added steps for persisting "IsRefreshable" flag.</li>
     * </ol>
     * </p>
     *
     * @throws UnsupportedOperationException
     *             if the source is not a physical file.
     * @throws IOException
     *             if any I/O error occurs while writing to source file.
     */
    @Override
    protected synchronized void save() throws IOException {
        if (!source.getProtocol().equals("file")) {
            throw new UnsupportedOperationException("source is not a physical file");
        }

        try {
            writer = new PrintWriter(new FileWriter(Helper.decodeURL(source.getFile())));
            char listDelimiter = getListDelimiter();
            if (listDelimiter != ';') {
                writer.print("ListDelimiter=");
                writer.println(listDelimiter);
            }
            // Write "IsRefreshable" flag (NEW in 2.2)
            Boolean refreshable = isRefreshable();
            if (refreshable != null) {
                writer.print("IsRefreshable=");
                writer.println(refreshable.toString());
            }

            writeProperty(getRoot(), "");
        } finally {
            // Close the writer
            Helper.closeObj(writer);
        }
    }

    /**
     * <p>
     * Loads the properties and their values from source .properties file.
     * </p>
     *
     * <p>
     * <em>Changes in 2.2:</em>
     * <ol>
     * <li>Made this method synchronized.</li>
     * <li>Added steps for reading "IsRefreshable" flag.</li>
     * </ol>
     * </p>
     *
     * @throws IOException if any I/O error occurs while reading from source
     *         file.
     */
    @Override
    protected synchronized void load() throws IOException {
        char listDelimiter = Helper.DEFAULT_LIST_DELIMITER;
        Boolean refreshable = null;

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(source.openStream()));
            Property root = new Property();
            // comment cache
            List<String> comments = new ArrayList<String>();

            String line;
            while ((line = reader.readLine()) != null) {

                String trim = line.trim();
                if (trim.length() == 0 || trim.charAt(0) == '#' || trim.charAt(0) == '!') {
                    // put comment in cache
                    comments.add(line);

                    // Next line
                    continue;
                }

                int pos = getPos(line);
                String key = line.substring(0, pos);
                if (key.equals("ListDelimiter")) {
                    String delim = line.substring(pos + 1);
                    if (delim.length() != 1) {
                        throw new ConfigParserException("invalid delimiter");
                    }
                    listDelimiter = delim.charAt(0);
                    continue;
                }
                // read "IsRefreshable" flag (NEW in 2.2)
                if (Helper.KEY_REFRESHABLE.equals(key)) {
                    String value = line.substring(pos + 1).trim();
                    refreshable = Boolean.parseBoolean(value);
                    continue;
                }

                // Parse
                parse(root, comments, line, pos, key, listDelimiter);

            }
            setRoot(root);

            // Set refreshable flag
            setRefreshable(refreshable);
            // Set list delimiter
            setListDelimiter(listDelimiter);
        } catch (IOException ioe) {
            throw ioe;
        } catch (IllegalArgumentException iae) {
            throw new ConfigParserException(iae.getMessage());
        } finally {
            // Close the reader
            Helper.closeObj(reader);
        }
    }

    /**
     * <p>
     * Parses values from the line.
     * </p>
     *
     * @param root
     *            the root.
     * @param comments
     *            the comments.
     * @param line
     *            the line.
     * @param pos
     *            the position.
     * @param key
     *            the key.
     * @param listDelimiter
     *            the list delimiter.
     *
     * @throws ConfigParserException
     *             if any error occurs.
     *
     * @since 2.2
     */
    private static void parse(Property root, List<String> comments, String line, int pos, String key,
        char listDelimiter) throws ConfigParserException {
        key = Helper.parseString(key);

        // parse value list
        List<String> valueList = Helper.parseValueString(line.substring(pos + 1), listDelimiter);

        // construct property
        Property property = root.find(key);
        if (property != null && property.getValue() != null) {
            throw new ConfigParserException("contains duplicate property " + key);
        }
        root.setProperty(key, (String[]) valueList.toArray(new String[valueList.size()]));
        property = root.find(key);
        property.setSeparator(line.charAt(pos));
        if (comments.size() > 0) {
            for (Iterator<String> itr = comments.iterator(); itr.hasNext();) {
                property.addComment(itr.next());
            }
            comments.clear();
        }
    }

    /**
     * <p>
     * Gets the position of delimiter ('=', ':' or ' ').
     * </p>
     *
     * @param line
     *            the line.
     *
     * @return the position of delimiter.
     *
     * @throws ConfigParserException
     *             if no delimiter found.
     *
     * @since 2.2
     */
    private static int getPos(String line) throws ConfigParserException {
        int pos = findDelimiter(line, '=');
        if (pos == -1) {
            pos = findDelimiter(line, ':');
        }
        if (pos == -1) {
            pos = findDelimiter(line, ' ');
        }
        if (pos == -1) {
            throw new ConfigParserException("unrecognized line : " + line);
        }

        return pos;
    }

    /**
     * Fine delimiter, and return the index of the found delimiter, return -1 if not found.
     *
     * @param s the string to find delimiter
     * @param delimiter the delimiter to find position for
     * @return the index of the delimiter, return -1 if not found
     */
    private static int findDelimiter(String s, char delimiter) {
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '\\') {
                i++;
            } else if (s.charAt(i) == delimiter) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Merge a list of values into a single value with specified list delimiter.
     * Possible occurance of list delimiter in the value itself should be escaped.
     *
     * @param  values an array of values to merge
     * @param  listDelimiter the list delimiter to use
     * @return the merged string
     */
    private static String mergeEscaped(String[] values, char listDelimiter) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < values.length; ++i) {
            if (i > 0) {
                buffer.append(listDelimiter);
            }
            for (int k = 0; k < values[i].length(); ++k) {
                char ch = values[i].charAt(k);
                String escapes = " !:=\\#";
                if (ch == listDelimiter || escapes.indexOf(ch) >= 0) {
                    buffer.append('\\');
                    buffer.append(ch);
                } else if (ch == '\n') {
                    buffer.append("\\n");
                } else if (ch == '\r') {
                    buffer.append("\\r");
                } else if (ch == '\t') {
                    buffer.append("\\t");
                } else {
                    buffer.append(ch);
                }
            }
        }
        return buffer.toString();
    }

    /**
     * <p>
     * Gets the clone copy of this object.
     * </p>
     *
     * <p>
     * <em>Changes in 2.2:</em>
     * <ol>
     * <li>Setting refreshable property.</li>
     * </ol>
     * </p>
     *
     * @return a clone copy of this object.
     */
    @Override
    public Object clone() {
        PropConfigProperties properties = new PropConfigProperties();
        properties.source = source;
        properties.setRoot((Property) getRoot().clone());

        properties.setRefreshable(isRefreshable()); // NEW in 2.2

        return properties;
    }

}
