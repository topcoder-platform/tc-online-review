/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.login.stresstests;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.cronos.onlinereview.login.AuthCookieManagementException;
import com.cronos.onlinereview.login.AuthCookieManager;
import com.cronos.onlinereview.login.cookies.AuthCookieManagerImpl;
import com.topcoder.security.authenticationfactory.Principal;
import com.topcoder.util.config.ConfigManager;

import junit.framework.TestCase;

/**
 * The stress tests for the class {@link}
 * 
 * @author KLW
 * @version 1.0
 */
public class AuthCookieManagerStressTest extends TestCase {
    /**
     * the instance for testing.
     */
    private AuthCookieManager manager;
    /**
     * the namespace for testing.
     */
    private String namespace = "com.cronos.onlinereview.login.cookies.AuthCookieManagerImpl";

    /**
     * the database testing url.
     */
    private String dburl;

    /**
     * the database testing name.
     */
    private String username;

    /**
     * the database testing password.
     */
    private String pass;
    /**
     * the database testing driver.
     */
    private String driver;
    
    /**
     * the random for testing.
     */
    private Random seed = new Random();
    
    /**
     * the flag if the tests fails.
     */
    private boolean succ;

    /**
     * Sets up the test environment.
     * 
     * @throws Exception
     *             if any exceptions throw to JUnit.
     */
    protected void setUp() throws Exception {
        super.setUp();
        clearConfig();
        loadDbConfig();
        clearData();
        initData();
        loadConfig("test_files/stresstests/OnlineReviewLogin.xml");
        manager = new AuthCookieManagerImpl(namespace);
        assertNotNull("The instance should not be null.", manager);
        succ = true;
    }

    /**
     * tears down the test environment.
     */
    protected void tearDown() throws Exception {
        manager = null;
        clearData();
    }
    
    /**
     * Loads the database config from configuration file.
     * @throws Exception if any error occurs.
     */
    private void loadDbConfig() throws Exception{
        if(username!=null){
            return;
        }
        Properties p =  new Properties();
        InputStream in = null;
        try {
            in = new FileInputStream("test_files/stresstests/db.properties");
            p.load(in);
            dburl = p.getProperty("url");
            username = p.getProperty("user");
            pass = p.getProperty("password");
            driver = p.getProperty("driver");
        }finally{
            if(in!=null){
                in.close();
            }
        }
            
    }

    /**
     * init the testing data.
     * 
     * @throws Exception
     *             if any exception occurs.
     */
    private void initData() throws Exception {
        Connection conn = null;
        try {
            conn = getConnection();
            //add 100 users to database
            String sql = "insert into security_user(login_id, user_id, password) values(?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            for(int i=1; i<101; i++){
                ps.setLong(1, i);
                ps.setString(2, "stress_user_"+i);
                ps.setString(3, "stress_pass_"+i);
                ps.execute();
            }
        } finally {
            closeConnection(conn);
        }
    }

    /**
     * clear the testing data.
     * 
     * @throws Exception
     *             if any exception occurs.
     */
    private void clearData()  throws Exception{
        Connection conn = null;
        try {
            conn = getConnection();
            String sql = "delete from security_user";
            conn.prepareStatement(sql).execute();
        } finally {
            closeConnection(conn);
        }
    }

    /**
     * get connection to the database.
     * 
     * @return the connection to the database.
     * @throws Exception
     *             if any exception occurs.
     */
    private Connection getConnection() throws Exception {
        Class.forName(driver);
        return DriverManager.getConnection(dburl, username, pass);
    }

    /**
     * close the connection to the database.
     * 
     * @param conn
     *            the connection to close.
     * @throws Exception
     *             if any error occurs.
     */
    private void closeConnection(Connection conn) throws Exception {
        if (conn != null) {
            conn.close();
        }
    }

    /**
     * load config from file.
     * 
     * @param filePath
     *            the config file
     * @throws Exception
     *             if any exceptions occurs.
     */
    private static void loadConfig(String filePath) throws Exception {
        ConfigManager cm = ConfigManager.getInstance();
        cm.add(new File(filePath).getAbsolutePath());
    }

    /**
     * Clears the configurations in the config manager.
     * 
     * @throws Exception
     *             if any exceptions occurs.
     */
    @SuppressWarnings("unchecked")
    private static void clearConfig() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();

        for (Iterator itr = cm.getAllNamespaces(); itr.hasNext();) {
            cm.removeNamespace((String) itr.next());
        }
    }

    /**
     * Test method for {@link AuthCookieManagerImpl#setAuthCookie(Principal, HttpServletRequest, HttpServletResponse)}.
     */
    public void testSetAuthCookie() {
        ThreadGroup tg = new ThreadGroup("stress-test");
        succ = true;
        for(int i=1; i<101; i++){
            new Thread(tg, new Runnable() {
                //do the setAuthCookie method
                @SuppressWarnings("unchecked")
                public void run() {
                    HttpServletRequest request = new MockHttpServletRequest();
                    HttpServletResponse response = new MockHttpServletResponse();
                    long id = seed.nextInt(100)+1;
                    Principal principal = new Principal("id");
                    principal.addMapping("userName", "stress_user_"+id);
                    try {
                        manager.setAuthCookie(principal, request, response);
                    } catch (AuthCookieManagementException e) {
                        succ = false;
                        fail("fail to set auth cookie.");
                    }
                    //check the result.
                    try {
                        Field field = response.getClass().getDeclaredField("cookies");
                        field.setAccessible(true);
                        List<Cookie> cookies = (List<Cookie>) field.get(response);
                        assertNotNull("The cookie should be set.", cookies);
                        assertEquals("Only one cookie should be set.", 1, cookies.size());
                        Cookie cookie = cookies.get(0);
                        assertEquals("Name is incorrect.", "topcoderCookie", cookie.getName());
                        String value = cookie.getValue();
                        assertTrue("The value is incorrect.", value.indexOf(id+"|")>=0);
                        assertEquals("MaxAge is incorrect.", Integer.MAX_VALUE, cookie.getMaxAge());
                    } catch (Exception e) {
                        succ = false;
                        fail("fail to set auth cookie.");
                    }catch(Error e){
                        //to catch the JUnit Error
                        succ = false;
                        fail("fail to set auth cookie.");
                    }
                }
            }).start();
        }
        while(tg.activeCount()>0){
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                // ignore
            }
        }
        if(!succ){
            fail("fail to set auth cookie.");
        }
    }

    /**
     * Test method for {@link AuthCookieManagerImpl#removeAuthCookie(HttpServletRequest, HttpServletResponse)}
     * .
     */
    public void testRemoveAuthCookie() {
        ThreadGroup tg = new ThreadGroup("stress-test");
        succ = true;
        for(int i=1; i<101; i++){
            new Thread(tg, new Runnable() {
                //do the setAuthCookie method
                @SuppressWarnings("unchecked")
                public void run() {
                    HttpServletRequest request = new MockHttpServletRequest();
                    HttpServletResponse response = new MockHttpServletResponse();
                    try {
                      //remove the cookie
                        manager.removeAuthCookie(request, response);
                    } catch (AuthCookieManagementException e) {
                        succ = false;
                        fail("fail to set auth cookie.");
                       
                    }
                    
                    //check the result.
                    try {
                        Field field = response.getClass().getDeclaredField("cookies");
                        field.setAccessible(true);
                        List<Cookie> cookies = (List<Cookie>) field.get(response);
                        assertNotNull("The cookie should be set.", cookies);
                        assertEquals("Only one cookie should be set.", 1, cookies.size());
                        Cookie cookie = cookies.get(0);
                        assertEquals("Name is incorrect.", "topcoderCookie", cookie.getName());
                        assertEquals("Value is incorrect.", "", cookie.getValue());
                        assertEquals("Name is incorrect.", "", cookie.getValue());
                        assertEquals("MaxAge is incorrect.", 0, cookie.getMaxAge());
                    } catch (Exception e) {
                        succ = false;
                        fail("fail to set auth cookie.");
                    }catch(Error e){
                        //to catch the JUnit Error
                        succ = false;
                        fail("fail to set auth cookie.");
                    }
                }
            }).start();
        }
        while(tg.activeCount()>0){
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                // ignore
            }
        }
        if(!succ){
            fail("fail to set auth cookie.");
        }
    }

    /**
     * Test method for
     * {@link com.cronos.onlinereview.login.cookies.AuthCookieManagerImpl#checkAuthCookie(javax.servlet.http.HttpServletRequest)}
     * .
     */
    public void testCheckAuthCookie() {
        ThreadGroup tg = new ThreadGroup("stress-test");
        succ = true;
        for(int i=1; i<101; i++){
            new Thread(tg, new Runnable() {
                //do the checkAuthCookie method
                public void run() {
                    HttpServletRequest request = new MockHttpServletRequest();
                    request.getSession();
                    long id = seed.nextInt(100)+1;
                    String value = id+"|"+hashPassword("stress_pass_"+id);
                    Cookie cookie = new Cookie("topcoderCookie", value);
                    cookie.setMaxAge(Integer.MAX_VALUE);
                    cookie.setDomain("localhost");
                    cookie.setPath("/");
                    try {
                        Field field = request.getClass().getDeclaredField("cookies");
                        field.setAccessible(true);
                        field.set(request, new Cookie[] {cookie});
                      //remove the cookie
                        assertEquals("The id is incorrect", id, (long) manager.checkAuthCookie(request));
                    } catch (Exception e) {
                        succ = false;
                        fail("fail to set auth cookie.");
                    }catch(Error e){
                        //to catch the JUnit Error
                        succ = false;
                        fail("fail to set auth cookie.");
                    }
                }
            }).start();
        }
        while(tg.activeCount()>0){
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                // ignore
            }
        }
        if(!succ){
            fail("fail to set auth cookie.");
        }
    }
    
    /**
     * Compute a one-way hash of a password.
     *
     * @param password
     *            the password to hash
     * @return the hashed password (not null)
     */
    private static String hashPassword(String password) {
        MessageDigest md = null;

        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {

            // ignore exceptions, assume that "MD5" is always available
        }

        String secretString = "30A669BA1C7E4d4887459B3136129857";
        String data = secretString + password;
        byte[] plain = data.getBytes();
        byte[] raw = md.digest(plain);
        StringBuffer sb = new StringBuffer();

        for (byte rawByte : raw) {
            String hex = Integer.toHexString(rawByte & 0xFF);

            if (hex.length() == 1) {
                sb.append('0');
            }

            sb.append(hex);
        }

        return sb.toString();
    }

}
