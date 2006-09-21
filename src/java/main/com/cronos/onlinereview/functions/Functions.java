/*
 * Copyright (C) 2006 TopCoder Inc.  All Rights Reserved.
 */
package com.cronos.onlinereview.functions;

public final class Functions {

    /**
     * This is the hidden constructor of the <code>Functions</code> class to prevent this class's
     * instantiation.
     */
    private Functions() {
    }

    /**
     * This static method encodes parts of text passed to it as parameter. It encodes the symbols
     * that can be interpreted as a part of markup replacing them with HTML entities. Additionally,
     * this method encodes all line terminators encountered in the input string into the following
     * character sequence: <code>&lt;br /&gt;</code>. The line terminators are the ones specified
     * in the description of the class <code>java.util.regex.Pattern</code>.
     * <p>
     * This method is an implementeation of <code>htmlEncode</code> function used from EL
     * expressions in JSP pages.
     * </p>
     *
     * @return encoded string, or empty string if parameter <code>text</code> was
     *         <code>null</code>.
     * @param text
     *            a text to encode.
     */
    public static String htmlEncode(String text) {
        if (text == null || text.length() == 0) {
            return "";
        }

        StringBuffer stringBuffer = new StringBuffer();

        for (int i = 0; i < text.length(); ++i) {
            char ch = text.charAt(i);

            if (ch == ' ' && (i + 1 != text.length())) {
                char ch2 = text.charAt(i + 1);
                if (ch2 == ' ') {
                    stringBuffer.append("&#160; ");
                    ++i;
                } else {
                    stringBuffer.append(ch);
                }
            } else if (ch == '&') {
                stringBuffer.append("&amp;");
            } else if (ch == '<') {
                stringBuffer.append("&lt;");
            } else if (ch == '>') {
                stringBuffer.append("&gt;");
            } else if (ch == '"') {
                stringBuffer.append("&#034;");
            } else if (ch == '\'') {
                stringBuffer.append("&#039;");
            } else if ((ch == '\r' || ch == '\n') && (i + 1 != text.length())) {
                char ch2 = text.charAt(i + 1);
                if ((ch == '\r' && ch2 == '\n') || (ch == '\n' && ch2 == '\r')) {
                    ++i;
                }
                stringBuffer.append("<br />");
            } else if (ch == '\n' || ch == '\r' || ch == '\u0085' || ch == '\u2029') {
                stringBuffer.append("<br />");
            } else {
                stringBuffer.append(ch);
            }
        }

        return stringBuffer.toString();
    }
}
