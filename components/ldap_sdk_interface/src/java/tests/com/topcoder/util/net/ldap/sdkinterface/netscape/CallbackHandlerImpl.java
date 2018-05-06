/*
 * TCS LDAP SDK Interface 1.0
 *
 * CallbackHandlerImpl.java
 *
 * Copyright (c), 2004. TopCoder, Inc. All rights reserved.
 */
package com.topcoder.util.net.ldap.sdkinterface.netscape;

import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.callback.Callback;
import java.io.IOException;

/**
 *
 *
 * @author  TCSDESIGNER
 * @version 1.0 05/18/2004
 */
public class CallbackHandlerImpl implements CallbackHandler {

    public CallbackHandlerImpl() {
    }

    /**
     * <p> Retrieve or display the information requested in the
     * provided Callbacks.
     *
     * <p> The <code>handle</code> method implementation checks the
     * instance(s) of the <code>Callback</code> object(s) passed in
     * to retrieve or display the requested information.
     * The following example is provided to help demonstrate what an
     * <code>handle</code> method implementation might look like.
     * This example code is for guidance only.  Many details,
     * including proper error handling, are left out for simplicity.
     *
     * <pre>
     * public void handle(Callback[] callbacks)
     * throws IOException, UnsupportedCallbackException {
     *
     *	 for (int i = 0; i < callbacks.length; i++) {
     *	    if (callbacks[i] instanceof TextOutputCallback) {
     *
     *		// display the message according to the specified type
     *		TextOutputCallback toc = (TextOutputCallback)callbacks[i];
     *		switch (toc.getMessageType()) {
     *		case TextOutputCallback.INFORMATION:
     *		    System.out.println(toc.getMessage());
     *		    break;
     *		case TextOutputCallback.ERROR:
     *		    System.out.println("ERROR: " + toc.getMessage());
     *		    break;
     *		case TextOutputCallback.WARNING:
     *		    System.out.println("WARNING: " + toc.getMessage());
     *		    break;
     *		default:
     *		    throw new IOException("Unsupported message type: " +
     *					toc.getMessageType());
     *		}
     *
     *	    } else if (callbacks[i] instanceof NameCallback) {
     *
     *		// prompt the user for a username
     *		NameCallback nc = (NameCallback)callbacks[i];
     *
     *		// ignore the provided defaultName
     *		System.err.print(nc.getPrompt());
     *		System.err.flush();
     *		nc.setName((new BufferedReader
     *			(new InputStreamReader(System.in))).readLine());
     *
     *	    } else if (callbacks[i] instanceof PasswordCallback) {
     *
     *		// prompt the user for sensitive information
     *		PasswordCallback pc = (PasswordCallback)callbacks[i];
     *		System.err.print(pc.getPrompt());
     *		System.err.flush();
     *		pc.setPassword(readPassword(System.in));
     *
     *	    } else {
     *		throw new UnsupportedCallbackException
     *			(callbacks[i], "Unrecognized Callback");
     *	    }
     *	 }
     * }
     *
     * // Reads user password from given input stream.
     * private char[] readPassword(InputStream in) throws IOException {
     *    // insert code to read a user password from the input stream
     * }
     * </pre>
     *
     * @param callbacks an array of <code>Callback</code> objects provided
     *		by an underlying security service which contains
     *		the information requested to be retrieved or displayed.
     *
     * @exception IOException if an input or output error occurs. <p>
     *
     * @exception UnsupportedCallbackException if the implementation of this
     *		method does not support one or more of the Callbacks
     *		specified in the <code>callbacks</code> parameter.
     */
    public void handle(Callback[] callbacks)
            throws IOException, UnsupportedCallbackException {
    }
}
