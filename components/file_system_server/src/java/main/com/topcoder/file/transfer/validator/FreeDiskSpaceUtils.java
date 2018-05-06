/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.validator;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 *
 * Get the free space on a drive. It is cross plateform, can be used in Windows or *nix operating system.
 * @author Luca, FireIce
 * @version 1.0
 */
class FreeDiskSpaceUtils {

    /**
     * singlton instance.
     */
    private static final FreeDiskSpaceUtils INSTANCE = new FreeDiskSpaceUtils();

    /**
     * Operating system state flag for error.
     */
    private static final int INIT_PROBLEM = -1;

    /**
     * Operating system state flag for neither Unix nor Windows.
     */
    private static final int OTHER = 0;

    /**
     * Operating system state flag for Windows.
     */
    private static final int WINDOWS = 1;

    /**
     * Operating system state flag for Unix.
     */
    private static final int UNIX = 2;

    /**
     * The Unix separator character.
     */
    private static final char UNIX_SEPARATOR = '/';

    /**
     * The Windows separator character.
     */
    private static final char WINDOWS_SEPARATOR = '\\';

    /**
     * The system separator character.
     */
    private static final char SYSTEM_SEPARATOR = File.separatorChar;

    /**
     * The separator character that is the opposite of the system separator.
     */
    private static final char OTHER_SEPARATOR;
    static {
        if (SYSTEM_SEPARATOR == WINDOWS_SEPARATOR) {
            OTHER_SEPARATOR = UNIX_SEPARATOR;
        } else {
            OTHER_SEPARATOR = WINDOWS_SEPARATOR;
        }
    }

    /**
     * the count of TOKENS.
     */
    private static final int TOKENCOUNT = 4;

    /**
     * the constant that the lenght is three.
     */
    private static final int LENGTHTREE = 3;

    /**
     * The operating system flag.
     */
    private static final int OS;
    static {
        int os = OTHER;
        try {
            String osName = System.getProperty("os.name");
            if (osName == null) {
                throw new IOException("os.name not found");
            }
            osName = osName.toLowerCase();
            // match
            if (osName.indexOf("windows") != -1) {
                os = WINDOWS;
            } else if (osName.indexOf("linux") != -1 || osName.indexOf("sun os") != -1 || osName.indexOf("sunos") != -1
                    || osName.indexOf("solaris") != -1 || osName.indexOf("mpe/ix") != -1
                    || osName.indexOf("hp-ux") != -1 || osName.indexOf("aix") != -1 || osName.indexOf("freebsd") != -1
                    || osName.indexOf("irix") != -1 || osName.indexOf("digital unix") != -1
                    || osName.indexOf("unix") != -1 || osName.indexOf("mac os x") != -1) {
                os = UNIX;
            } else {
                os = OTHER;
            }
        } catch (IOException ex) {
            os = INIT_PROBLEM;
        }
        OS = os;
    }

    /**
     * private constructor preventing inheritence.
     */
    private FreeDiskSpaceUtils() {
    }

    /**
     * Returns the free space on a drive or volume in a cross-platform manner. The free space is calculated via the
     * command line. It uses 'dir /-c' on Windows and 'df' on *nix.
     * @param path
     *            the path to get free space for, not null, not empty on Unix
     * @return the amount of free drive space on the drive or volume
     * @throws IllegalArgumentException
     *             if the path is invalid
     * @throws IllegalStateException
     *             if an error occurred in initialisation
     * @throws IOException
     *             if an error occurs when finding the free space
     */
    public static long freeDiskSpace(String path) throws IOException {
        return INSTANCE.freeDiskSpaceOS(path, OS);
    }

    /**
     * Returns the free space on a drive or volume in a cross-platform manner. The free space is calculated via the
     * command line. It uses 'dir /-c' on Windows and 'df' on *nix.
     * @param path
     *            the path to get free space for, not null, not empty on Unix
     * @param os
     *            the operating system code
     * @return the amount of free drive space on the drive or volume
     * @throws IllegalArgumentException
     *             if the path is invalid
     * @throws IllegalStateException
     *             if an error occurred in initialisation
     * @throws IOException
     *             if an error occurs when finding the free space
     */
    private long freeDiskSpaceOS(String path, int os) throws IOException {
        if (path == null) {
            throw new IllegalArgumentException("Path must not be empty");
        }
        switch (os) {
        case WINDOWS:
            return freeDiskSpaceWindows(path);
        case UNIX:
            return freeDiskSpaceUnix(path);
        case OTHER:
            throw new IllegalStateException("Unsupported operating system");
        default:
            throw new IllegalStateException("Exception caught when determining operating system");
        }
    }

    /**
     * Find free space on the Windows platform using the 'dir' command.
     * @param path
     *            the path to get free space for, including the colon
     * @return the amount of free drive space on the drive
     * @throws IOException
     *             if an error occurs
     */
    private long freeDiskSpaceWindows(String path) throws IOException {
        path = normalizePath(path);
        if (path.length() > 2 && path.charAt(1) == ':') {
            path = path.substring(0, 2); // seems to make it work
        }
        // build and run the 'dir' command
        String[] cmdAttrbs = new String[] {"cmd.exe", "/C", "dir /c " + path};
        // read in the output of the command to an ArrayList
        BufferedReader in = null;
        String line = null;
        List lines = new ArrayList();
        try {
            in = openProcessStream(cmdAttrbs);
            line = in.readLine();
            while (line != null) {
                line = line.toLowerCase().trim();
                lines.add(line);
                line = in.readLine();
            }
        } finally {
            if (in != null) {
                in.close();
            }
        }

        if (lines.size() == 0) {
            // unknown problem, throw exception
            throw new IOException("Command line 'dir /c' did not return any info " + "for command '" + cmdAttrbs[2]
                    + "'");
        }

        // now iterate over the lines we just read and find the LAST
        // non-empty line (the free space bytes should be in the last element
        // of the ArrayList anyway, but this will ensure it works even if it's
        // not, still assuming it is on the last non-blank line)
        long bytes = -1;
        int i = lines.size() - 1;
        int bytesStart = 0;
        int bytesEnd = 0;
    outerLoop:
        while (i > 0) {
            line = (String) lines.get(i);
            if (line.length() > 0) {
                // found it, so now read from the end of the line to find the
                // last numeric character on the line, then continue until we
                // find the first non-numeric character, and everything between
                // that and the last numeric character inclusive is our free
                // space bytes count
                int j = line.length() - 1;
            innerLoop1:
                while (j >= 0) {
                    char c = line.charAt(j);
                    if (Character.isDigit(c)) {
                        // found the last numeric character, this is the end of
                        // the free space bytes count
                        bytesEnd = j + 1;
                        break innerLoop1;
                    }
                    j--;
                }
            innerLoop2:
                while (j >= 0) {
                    char c = line.charAt(j);
                    if (!Character.isDigit(c) && c != ',' && c != '.') {
                        // found the next non-numeric character, this is the
                        // beginning of the free space bytes count
                        bytesStart = j + 1;
                        break innerLoop2;
                    }
                    j--;
                }
                break outerLoop;
            }
        }

        // remove commas and dots in the bytes count
        StringBuffer buf = new StringBuffer(line.substring(bytesStart, bytesEnd));
        removeCommasAndDots(buf);
        bytes = Long.parseLong(buf.toString());
        return bytes;
    }

    /**
     * remove commas and dots.
     * @param buf the the bytes.
     */
    private void removeCommasAndDots(StringBuffer buf) {
        for (int k = 0; k < buf.length(); k++) {
            if (buf.charAt(k) == ',' || buf.charAt(k) == '.') {
                buf.deleteCharAt(k--);
            }
        }
    }

    /**
     * Find free space on the *nix platform using the 'df' command.
     * @param path
     *            the path to get free space for
     * @return the amount of free drive space on the volume
     * @throws IOException
     *             if an error occurs
     */
    private long freeDiskSpaceUnix(String path) throws IOException {
        if (path.length() == 0) {
            throw new IllegalArgumentException("Path must not be empty");
        }
        path = normalizePath(path);

        // build and run the 'df' command
        String[] cmdAttribs = new String[] {"df", path};

        // read the output from the command until we come to the second line
        long bytes = -1;
        BufferedReader in = null;
        try {
            in = openProcessStream(cmdAttribs);
            String line1 = in.readLine(); // header line (ignore it)
            String line2 = in.readLine(); // the line we're interested in
            String line3 = in.readLine(); // possibly interesting line
            if (line2 == null) {
                // unknown problem, throw exception
                throw new IOException("Command line 'df' did not return info as expected " + "for path '" + path
                        + "'- response on first line was '" + line1 + "'");
            }
            line2 = line2.trim();

            // Now, we tokenize the string. The fourth element is what we want.
            StringTokenizer tok = new StringTokenizer(line2, " ");
            if (tok.countTokens() < TOKENCOUNT) {
                // could be long Filesystem, thus data on third line
                if (tok.countTokens() == 1 && line3 != null) {
                    line3 = line3.trim();
                    tok = new StringTokenizer(line3, " ");
                } else {
                    throw new IOException("Command line 'df' did not return data as expected " + "for path '" + path
                            + "'- check path is valid");
                }
            } else {
                tok.nextToken(); // Ignore Filesystem
            }
            tok.nextToken(); // Ignore 1K-blocks
            tok.nextToken(); // Ignore Used
            String freeSpace = tok.nextToken();
            try {
                bytes = Long.parseLong(freeSpace);
            } catch (NumberFormatException ex) {
                throw new IOException("Command line 'df' did not return numeric data as expected " + "for path '"
                        + path + "'- check path is valid");
            }

        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ioe) {
                // ignore
            }
        }

        if (bytes < 0) {
            throw new IOException("Command line 'df' did not find free space in response " + "for path '" + path
                    + "'- check path is valid");
        }
        return bytes;
    }

    /**
     * Normalizes a path, removing double and single dot path steps.
     * <p>
     * This method normalizes a path to a standard format. The input may contain separators in either Unix or Windows
     * format. The output will contain separators in the format of the system.
     * <p>
     * A trailing slash will be retained. A double slash will be merged to a single slash (but UNC names are handled). A
     * single dot path segment will be removed. A double dot will cause that path segment and the one before to be
     * removed. If the double dot has no parent path segment to work with, <code>null</code> is returned.
     * <p>
     * The output will be the same on both Unix and Windows except for the separator character.
     *
     * <pre>
     *        /foo//               --&gt;   /foo/
     *        /foo/./              --&gt;   /foo/
     *        /foo/../bar          --&gt;   /bar
     *        /foo/../bar/         --&gt;   /bar/
     *        /foo/../bar/../baz   --&gt;   /baz
     *        //foo//./bar         --&gt;   /foo/bar
     *        /../                 --&gt;   null
     *        ../foo               --&gt;   null
     *        foo/bar/..           --&gt;   foo/
     *        foo/../../bar        --&gt;   null
     *        foo/../bar           --&gt;   bar
     *        //server/foo/../bar  --&gt;   //server/bar
     *        //server/../bar      --&gt;   null
     *        C:\foo\..\bar        --&gt;   C:\bar
     *        C:\..\bar            --&gt;   null
     *        &tilde;/foo/../bar/        --&gt;   &tilde;/bar/
     *        &tilde;/../bar             --&gt;   null
     * </pre>
     *
     * (Note the file separator returned will be correct for Windows/Unix)
     * @param filename
     *            the filename to normalize, null returns null
     * @return the normalized filename, or null if invalid
     */
    private String normalizePath(String filename) {
        if (filename == null) {
            return null;
        }
        int size = filename.length();
        if (size == 0) {
            return filename;
        }
        int prefix = getPrefixLength(filename);
        if (prefix < 0) {
            return null;
        }

        char[] array = new char[size + 2]; // +1 for possible extra slash, +2 for arraycopy
        filename.getChars(0, filename.length(), array, 0);

        // fix separators throughout
        fixSeparators(array);

        // add extra separator on the end to simplify code below
        boolean lastIsDirectory = true;
        if (array[size - 1] != SYSTEM_SEPARATOR) {
            array[size++] = SYSTEM_SEPARATOR;
            lastIsDirectory = false;
        }

        // adjoining slashes
        for (int i = prefix + 1; i < size; i++) {
            if (array[i] == SYSTEM_SEPARATOR && array[i - 1] == SYSTEM_SEPARATOR) {
                System.arraycopy(array, i, array, i - 1, size - i);
                size--;
                i--;
            }
        }

        // dot slash
        for (int i = prefix + 1; i < size; i++) {
            if (array[i] == SYSTEM_SEPARATOR && array[i - 1] == '.'
                    && (i == prefix + 1 || array[i - 2] == SYSTEM_SEPARATOR)) {
                if (i == size - 1) {
                    lastIsDirectory = true;
                }
                System.arraycopy(array, i + 1, array, i - 1, size - i);
                size -= 2;
                i--;
            }
        }

        // double dot slash
    outer:
        for (int i = prefix + 2; i < size; i++) {
            if (array[i] == SYSTEM_SEPARATOR && array[i - 1] == '.' && array[i - 2] == '.'
                    && (i == prefix + 2 || array[i - 2 - 1] == SYSTEM_SEPARATOR)) {
                if (i == prefix + 2) {
                    return null;
                } else if (i == size - 1) {
                    lastIsDirectory = true;
                }
                int j;
                for (j = i - 2 - 2; j >= prefix; j--) {
                    if (array[j] == SYSTEM_SEPARATOR) {
                        // remove b/../ from a/b/../c
                        System.arraycopy(array, i + 1, array, j + 1, size - i);
                        size -= (i - j);
                        i = j + 1;
                        continue outer;
                    }
                }
                // remove a/../ from a/../c
                System.arraycopy(array, i + 1, array, prefix, size - i);
                size -= (i + 1 - prefix);
                i = prefix + 1;
            }
        }

        if (size <= 0) { // should never be less than 0
            return "";
        } else if (size <= prefix) { // should never be less than prefix
            return new String(array, 0, size);
        } else if (lastIsDirectory) {
            return new String(array, 0, size); // keep trailing separator
        }
        return new String(array, 0, size - 1); // lose trailing separator
    }

    /**
     * fix separators.
     * @param array the filename's char array
     */
    private void fixSeparators(char[] array) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == OTHER_SEPARATOR) {
                array[i] = SYSTEM_SEPARATOR;
            }
        }
    }

    /**
     * Returns the length of the filename prefix, such as <code>C:/</code> or <code>~/</code>.
     * <p>
     * This method will handle a file in either Unix or Windows format.
     * <p>
     * The prefix length includes the first slash in the full filename if applicable. Thus, it is possible that the
     * length returned is greater than the length of the input string.
     *
     * <pre>
     *            Windows:
     *            a\b\c.txt           --&gt; &quot;&quot;          --&gt; relative
     *            \a\b\c.txt          --&gt; &quot;\&quot;         --&gt; current drive absolute
     *            C:a\b\c.txt         --&gt; &quot;C:&quot;        --&gt; drive relative
     *            C:\a\b\c.txt        --&gt; &quot;C:\&quot;       --&gt; absolute
     *            \\server\a\b\c.txt  --&gt; &quot;\\server\&quot; --&gt; UNC
     *            Unix:
     *            a/b/c.txt           --&gt; &quot;&quot;          --&gt; relative
     *            /a/b/c.txt          --&gt; &quot;/&quot;         --&gt; absolute
     *            &tilde;/a/b/c.txt         --&gt; &quot;&tilde;/&quot;        --&gt; current user
     *            &tilde;                   --&gt; &quot;&tilde;/&quot;        --&gt; current user (slash added)
     *            &tilde;user/a/b/c.txt     --&gt; &quot;&tilde;user/&quot;    --&gt; named user
     *            &tilde;user               --&gt; &quot;&tilde;user/&quot;    --&gt; named user (slash added)
     * </pre>
     *
     * <p>
     * The output will be the same irrespective of the machine that the code is running on. ie. both Unix and Windows
     * prefixes are matched regardless.
     * @param filename
     *            the filename to find the prefix in, null returns -1
     * @return the length of the prefix, -1 if invalid or null
     */
    public static int getPrefixLength(String filename) {
        if (filename == null) {
            return -1;
        }
        int len = filename.length();
        if (len == 0) {
            return 0;
        }
        char ch0 = filename.charAt(0);
        if (ch0 == ':') {
            return -1;
        }
        if (len == 1) {
            if (ch0 == '~') {
                return 2; // return a length greater than the input
            }
            return (isSeparator(ch0) ? 1 : 0);
        } else {
            if (ch0 == '~') {
                int posUnix = filename.indexOf(UNIX_SEPARATOR, 1);
                int posWin = filename.indexOf(WINDOWS_SEPARATOR, 1);
                if (posUnix == -1 && posWin == -1) {
                    return len + 1; // return a length greater than the input
                }
                posUnix = (posUnix == -1 ? posWin : posUnix);
                posWin = (posWin == -1 ? posUnix : posWin);
                return Math.min(posUnix, posWin) + 1;
            }
            char ch1 = filename.charAt(1);
            if (ch1 == ':') {
                ch0 = Character.toUpperCase(ch0);
                if (ch0 >= 'A' && ch0 <= 'Z') {
                    if (len == 2 || !isSeparator(filename.charAt(2))) {
                        return 2;
                    }
                    return LENGTHTREE;
                }
                return -1;

            } else if (isSeparator(ch0) && isSeparator(ch1)) {
                int posUnix = filename.indexOf(UNIX_SEPARATOR, 2);
                int posWin = filename.indexOf(WINDOWS_SEPARATOR, 2);
                if ((posUnix == -1 && posWin == -1) || posUnix == 2 || posWin == 2) {
                    return -1;
                }
                posUnix = (posUnix == -1 ? posWin : posUnix);
                posWin = (posWin == -1 ? posUnix : posWin);
                return Math.min(posUnix, posWin) + 1;
            } else {
                return (isSeparator(ch0) ? 1 : 0);
            }
        }
    }

    /**
     * Checks if the character is a separator.
     * @param ch
     *            the character to check
     * @return true if it is a separator character
     */
    private static boolean isSeparator(char ch) {
        return (ch == UNIX_SEPARATOR) || (ch == WINDOWS_SEPARATOR);
    }

    /**
     * Opens the stream to be operating system.
     * @param params
     *            the command parameters
     * @return a reader
     * @throws IOException
     *             if an error occurs
     */
    BufferedReader openProcessStream(String[] params) throws IOException {
        Process proc = Runtime.getRuntime().exec(params);
        return new BufferedReader(new InputStreamReader(proc.getInputStream()));
    }
}
