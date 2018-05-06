/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.servlet.request.failuretests;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.servlet.request.HttpRequestParser;
import com.topcoder.servlet.request.InvalidContentTypeException;
import com.topcoder.servlet.request.RequestParsingException;

/**
 * The class <code>HttpRequestParserFailureTests</code> contains tests for the class
 * {@link <code>HttpRequestParser</code>}.
 * @author FireIce
 * @version 2.0
 */
public class HttpRequestParserFailureTests extends TestCase {

    /**
     * An instance of the class being tested.
     * @see HttpRequestParser
     */
    private HttpRequestParser httpRequestParser = new HttpRequestParser();

    /**
     * Run the void parseRequest(ServletRequest) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testParseRequestRequestParsingExceptionInvalidContentType1() throws Exception {
        ServletRequest request = new MockServletRequest() {
            public String getContentType() {
                return null;
            };
        };
        try {
            httpRequestParser.parseRequest(request);
            fail("Should throw InvalidContentTypeException");
        } catch (InvalidContentTypeException e) {
            // good
        }
    }

    /**
     * Run the void parseRequest(ServletRequest) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testParseRequestRequestParsingExceptionInvalidContentType2() throws Exception {
        ServletRequest request = new MockServletRequest() {
            public String getContentType() {
                return "mock";
            };
        };
        try {
            httpRequestParser.parseRequest(request);
            fail("Should throw InvalidContentTypeException");
        } catch (InvalidContentTypeException e) {
            // good
        }
    }

    /**
     * Run the void parseRequest(ServletRequest) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testParseRequestRequestParsingExceptionInvalidContentType3() throws Exception {
        ServletRequest request = new MockServletRequest() {
            public String getContentType() {
                return "multipart/form-data;";
            };
        };
        try {
            httpRequestParser.parseRequest(request);
            fail("Should throw InvalidContentTypeException");
        } catch (InvalidContentTypeException e) {
            // good
        }
    }

    /**
     * Run the void parseRequest(ServletRequest) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testParseRequestRequestParsingExceptionIOException1() throws Exception {
        ServletRequest request = new MockServletRequest() {
            public String getContentType() {
                return "multipart/form-data;boundary=hello";
            };

            public ServletInputStream getInputStream() throws IOException {
                throw new IOException("mock");
            }
        };
        try {
            httpRequestParser.parseRequest(request);
            fail("Should throw RequestParsingException");
        } catch (RequestParsingException e) {
            // good
        }
    }

    /**
     * Run the void parseRequest(ServletRequest) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testParseRequestRequestParsingExceptionIOException2() throws Exception {
        ServletRequest request = new MockServletRequest() {
            public String getContentType() {
                return "multipart/form-data;boundary=hello";
            };

            public ServletInputStream getInputStream() throws IOException {
                return new ServletInputStream() {
                    public int read() throws IOException {
                        throw new IOException("mock");
                    };
                };
            }
        };
        try {
            httpRequestParser.parseRequest(request);
            fail("Should throw RequestParsingException");
        } catch (RequestParsingException e) {
            // good
        }
    }

    /**
     * Run the void parseRequest(ServletRequest) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testParseRequestRequestParsingExceptionInvalidBoundaryLine1() throws Exception {
        ServletRequest request = new MockServletRequest() {
            public String getContentType() {
                return "multipart/form-data;boundary=hello";
            };

            public ServletInputStream getInputStream() throws IOException {
                return new ServletInputStream() {
                    public int readLine(byte[] arg0, int arg1, int arg2) throws IOException {
                        return -1;
                    }

                    public int read() throws IOException {
                        return 1;
                    };
                };
            }
        };
        try {
            httpRequestParser.parseRequest(request);
            fail("Should throw RequestParsingException");
        } catch (RequestParsingException e) {
            // good
        }
    }

    /**
     * Run the void parseRequest(ServletRequest) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testParseRequestRequestParsingExceptionInvalidBoundaryLine2() throws Exception {
        ServletRequest request = new MockServletRequest() {
            public String getContentType() {
                return "multipart/form-data;boundary=hello";
            };

            public ServletInputStream getInputStream() throws IOException {
                return new ServletInputStream() {
                    public int readLine(byte[] arg0, int arg1, int arg2) throws IOException {
                        return 2;
                    }

                    public int read() throws IOException {
                        return 1;
                    };
                };
            }
        };
        try {
            httpRequestParser.parseRequest(request);
            fail("Should throw RequestParsingException");
        } catch (RequestParsingException e) {
            // good
        }
    }

    /**
     * Run the boolean hasNextFile() method test.
     * @throws Exception
     *             to JUnit
     */
    public void testHasNextFileIllegalStateException() throws Exception {
        try {
            httpRequestParser.hasNextFile();
            fail("expect throw IllegalStateException.");
        } catch (IllegalStateException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests whether the IllegalStateException is thrown properly.
     * </p>
     * @throws Exception
     *             any exception to JUnit.
     */
    public void testWriteNextFileIllegalStateException() throws Exception {
        try {
            httpRequestParser.writeNextFile(new ByteArrayOutputStream(), 100);
            fail("IllegalStateException should be thrown.");
        } catch (IllegalStateException e) {
            // good
        }
    }

    /**
     * Aggregates all tests in this class.
     * @return test suite aggregating all tests.
     */
    public static Test suite() {
        return new TestSuite(HttpRequestParserFailureTests.class);
    }
}
