/*
 * Copyright (c) 2006, TopCoder, Inc. All rights reserved.
 */
package com.cronos.onlinereview.deliverables;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * This the the proxy class that monitors the method calls on the PreparedStatement and logs
 * all the call to the setXXX methods. The arguments of this calls can be obtained by the getValues
 * of getValue calls.
 * </p>
 *
 * @author kr00tki
 * @version 1.0
 */
public class TestProxy implements InvocationHandler {

    /**
     * The object to monitor.
     */
    private final PreparedStatement pstmt;

    /**
     * The values set to the prepared statement.
     */
    private final List values = new ArrayList();

    /**
     * Creates new TestProxy instance.
     *
     * @param pstmt the object to monitor.
     */
    public TestProxy(PreparedStatement pstmt) {
        this.pstmt = pstmt;
    }

    /**
     * This method will create a proxy object.
     *
     * @return a proxied object. Please see <code>java.lang.reflect.Proxy</code> javadocs for details on the
     *         returned object.
     */
    public PreparedStatement getProxy() {
        final ArrayList interfaces = new ArrayList();
        Class current = pstmt.getClass();

        while (current != null) {
            final Class[] curIfs = current.getInterfaces();

            for (int i = 0; i < curIfs.length; i++) {
                if (!interfaces.contains(curIfs[i])) {
                    interfaces.add(curIfs[i]);
                }
            }

            current = current.getSuperclass();
        }

        return (PreparedStatement) java.lang.reflect.Proxy.newProxyInstance(pstmt.getClass().getClassLoader(),
                (Class[]) interfaces.toArray(new Class[interfaces.size()]), this);
    }

    /**
     * The invoke method as described in <code>InvocationHandler</code> javadocs. This method is called when a
     * method is being invoked on the proxy object.
     *
     * @param proxy the proxy object (not the original object). Ignored.
     * @param method the method being invoked on the proxy object.
     * @param args the arguments used when invoking the object
     *
     * @return the return from the invocation
     *
     * @throws Throwable if the method throws any exception (as defined by the <code>method.invoke()</code>
     *         method).
     */
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        if ((method.getName().startsWith("set")) && (args.length == 2)) {
            values.add(args[1]);
        }

        return method.invoke(this.pstmt, args);
    }

    /**
     * Returns the value that was set at the idx position to the statement.
     *
     * @param idx the vlues index.
     * @return the values at index.
     */
    public Object getValue(int idx) {
        return values.get(idx);
    }

    /**
     * Returns all the values.
     *
     * @return the values set to statement.
     */
    public Object[] getValues() {
        return values.toArray();
    }
}
