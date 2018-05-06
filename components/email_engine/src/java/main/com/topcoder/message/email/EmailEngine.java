/*
 * Copyright (C) 2005-2012 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.message.email;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;
import java.util.Map.Entry;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileTypeMap;
import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Transport;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.topcoder.util.log.Level;
import com.topcoder.util.log.Log;
import com.topcoder.util.log.LogManager;

import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.config.ConfigManagerException;
import com.topcoder.util.config.ConfigManagerInterface;


/**
 * <p>The EmailEngine class is responsible for sending email.</p>
 *
 * <p>
 * Version 3.0 has added support for priority messages via message headers.</p>
 * <p>
 * In version 3.1, the send methods are updated to handle the content type property from TCSEmailMethod, and
 * an explicit default constructor is added to be consistent with current TC standards. The changes have no
 * effect on the thread safety of any method.
 * </p>
 * <p>
 * Version 3.2 update:
 *  - If the userName/password are both of empty value,
 *   null values for both will be used when doing Transport connection authentication.
 *  - Using javax.mail.Session.getInstance instead of javax.mail.Session.getDefaultInstance.
 * </p>
 * @author      sord, BEHiker57W, smell, TCSDEVELOPER
 * @created     April 17, 2002
 * @version     3.2
 * @internal    Log of Changes:
 *
 *           $Log: EmailEngine.java,v $
 *           Revision 1.3  2005/10/04 19:54:19  ivern
 *           no message
 *
 *           Revision 1.2  2005/10/04 14:38:57  mgmg
 *           *** empty log message ***
 *
 *
 *           Revision 1.1.1.1  2004/03/23 23:03:25  gpaul
 *           no message
 *
 *           Revision 1.3  2003/04/01 18:58:08  marcg
 *           Updates for 2.0
 *
 *           Revision 1.1.1.1  2003/01/09 22:31:19  marcg
 *           no message
 *
 *           Revision 1.12 2002/11/28 15:30 yellow_gecko
 *           added attachment functionality
 *
 *           Revision 1.1.1.1  2002/10/28 17:24:47  marcg
 *           no message
 *
 *           Revision 1.11  2002/10/08 16:19:12  marcg
 *           removed reference to BasicConfigurator
 *
 *           Revision 1.10  2002/09/30 16:11:58  seaniswise
 *           added javadocs
 *
 *           Revision 1.9  2002/09/30 15:07:57  seaniswise
 *           no message
 *
 *           Revision 1.8  2002/09/29 19:28:12  marcg
 *           changed
 *
 *           Revision 1.7  2002/09/27 23:40:44  marcg
 *           updated use of configmanager
 *
 *           Revision 1.6  2002/08/08 16:40:08  marcg
 *           minor updates
 *
 *           Revision 1.5  2002/08/08 01:16:41  marcg
 *           Added ConfigManager Interface and corresponding methods.
 *
 *           Revision 1.4  2002/06/27 14:32:04  marcg
 *           author update
 *
 *           Revision 1.3  2002/06/13 20:46:31  marcg
 *           Modified class to use the Configuration Manager component.
 *           Set default SMTP IP to 127.0.0.1, not Topcoders ip address
 *           Added configuration for smtp username/password instead of always passing empty strings.
 *
 *           Revision 1.2  2002/05/21 21:51:59  tcs
 *           change package name
 *
 *           Revision 1.1  2002/05/10 13:14:06  tcs
 *           no message
 *
 *           Revision 1.1.2.4  2002/05/01 05:48:52  apps
 *           SB
 *
 *           Revision 1.1.2.4  2002/04/30 23:58:57  chuck
 *           fixed line that calls send to use configurable host and port
 *           (not constants SMTP_HOST_PORT and SMPT_HOST_ADDR)
 *
 *           Revision 1.1.2.3  2002/04/27 01:45:57  sord
 *           Added test for at least on TO address.
 *
 *           Revision 1.1.2.2  2002/04/17 12:43:17  apps
 *           Fixed some compiler issues.
 *
 *           Revision 1.1.2.1  2002/04/17 12:34:11  apps
 *           Initial implementation.
 */
public class EmailEngine implements ConfigManagerInterface {

    /**
     * AttachmentDataSource class implements DataSource to be wrapped with
     * a DataHandler for the creation of MimeBodyPart instances to add into
     * the Multipart instance that make up the body of the MimeMessage.
     *
     *@author      yellow_gecko
     *@created     November 28, 2002
     *@version     $Revision: 223122 $
     *@internal    Log of Changes:
     *           $Log: EmailEngine.java,v $
     *           Revision 1.3  2005/10/04 19:54:19  ivern
     *           no message
     *
     *           Revision 1.2  2005/10/04 14:38:57  mgmg
     *           *** empty log message ***
     *
     *           Revision 1.1.1.1  2004/03/23 23:03:25  gpaul
     *           no message
     *
     *           Revision 1.3  2003/04/01 18:58:08  marcg
     *           Updates for 2.0
     *
     *           Revision 1.1.1.1  2003/01/09 22:31:19  marcg
     *           no message
     *
     *           Revision 1.1 2002/11/28 15:30 yellow_gecko
     *           initial implementation
     */
    public static class AttachmentDataSource implements DataSource {

        /** Raw bytes of the attachment. */
        private byte [] bytes;

        /** Name of the data source. */
        private String name;

        /** Type of the attachment file. */
        private String type;


        /**
         * The constuctor extracts the raw bytes from the given <code>InputStream</code>
         * and finds the MIME type of the attachment.
         *
         *@param inputStream          the <code>InputStream</code> from which the attachment is read
         *@param name                 the name of the attachment
         *@exception                  throws IOException if the <code>InputStream</code> cannot be read
         */
        public AttachmentDataSource(InputStream inputStream, String name)
                throws IOException{
            /* Create a byte [] the length of the number of bytes in the input stream
               and read the data from the input stream into the byte []. */
            DataInputStream dis = new DataInputStream(new BufferedInputStream(inputStream));
            bytes = new byte[dis.available()];
            dis.readFully(bytes);

            this.name = name;
            this.type = FileTypeMap.getDefaultFileTypeMap().getContentType(name);
        }


        /**
         * Returns the MIME content type of this attachment.
         *
         *@return          the content type as a String
         */
        public String getContentType(){
            return type;
        }


        /**
         * Returns a new InputStream which is contructed from the raw bytes of the attachment.
         *
         *@return        the raw bytes of the attachment as an <code>InputStream</code>
         *@exception     IOException
         */
        public InputStream getInputStream() throws IOException{
            return new ByteArrayInputStream(bytes);
        }


        /**
         * Returns the name of the attachment.
         *
         *@return          the name of the attachment
         */
        public String getName(){
            return name;
        }


        /**
         * This method is not implemented and should never be called.
         *
         *@return        throws IOException only
         *@exception     IOException as this method is not meant to be used
         */
        public OutputStream getOutputStream() throws IOException{
            throw new IOException("This method is not implemented and should never be called.");
        }
    }
    /**
     *  SMTP Host Address constant.
     */
    public static final String SMTP_HOST_ADDR = "127.0.0.1";
    /**
     *  SMTP Host Port constant.
     */
    public static final int SMTP_HOST_PORT = 25;

    /**
     *  SMTP Host Type constant.
     */
    public static final String SMTP_HOST_TYPE = "smtp";

    /**
     * Default properties format.
     */
    static final String PROPERTIES_FORMAT = ConfigManager.CONFIG_XML_FORMAT;
    /**
     * String specifying location of Properties file.
     */
    static final String PROPERTIES_LOCATION = "com/topcoder/message/email/EmailEngine.xml";
    /**
     * Default configuration namespace.
     */
    static final String PROPERTIES_NAMESPACE = "com.topcoder.message.email.EmailEngine";
    /**
     * Property name for smtp host address.
     */
    static final String PROPERTY_SMTP_HOST_ADDR = "smtp_host_addr";
    /**
     * Property name for smtp host port.
     */
    static final String PROPERTY_SMTP_HOST_PORT = "smtp_host_port";
    /**
     * Property name for smtp password.
     */
    static final String PROPERTY_SMTP_PASSWORD = "password";
    /**
     * Property name for smtp user name.
     */
    static final String PROPERTY_SMTP_USER = "username";

    /**
     * the default ctor.
     * @since 3.1
     */
    public EmailEngine(){
        //does nothing
    }

    /**
     * <p>Sends an email message.</p>
     *
     * <p>The email message must contain at least one primary address, otherwise IllegalArgumentException will be
     * thrown. The method attempts to send the message via the internally configured SMTP settings.</p>
     *
     * <p>Version 3.0 of Email Engine have added message header support for priority messages.</p>
     * <p>Version 3.1 of Email Engine have added content type support.</p>
     * <p>
     * Version 3.2 update:
     * - if the userName/password are both of empty value,
     *   null values for both will be used when doing Transport connection authentication.
     * - Using javax.mail.Session.getInstance instead of javax.mail.Session.getDefaultInstance.
     * </p>
     * @param  message     the email message to be sent
     *
     * @throws NullPointerException     if <code>message</code> is <code>null</code>
     * @throws IllegalArgumentException if there is no to address or no from address associated with the message
     * @throws ConfigManagerException   if there is a problem with configuration
     * @throws SendingException         if message could not be send properly to the remote server
     */
    public static void send(TCSEmailMessage message) throws ConfigManagerException, SendingException {
        checkNull(message, "message");

        Address from = message.getFromAddress();
        Address[] to = message.getToAddress(TCSEmailMessage.TO);
        Address[] cc = message.getToAddress(TCSEmailMessage.CC);
        Address[] bcc = message.getToAddress(TCSEmailMessage.BCC);
        String subject = message.getSubject();
        String data = message.getBody();
        String contentType = message.getContentType();
        Map attachments = message.getAttachments();

        // Check the addresses.
        if (to.length < 1) {
            throw new IllegalArgumentException("There must be at least one TO: address");
        }
        if (from == null) {
            throw new IllegalArgumentException("There must be one FROM: address");
        }

        Map headers = message.getHeadersMap();

        send(from, to, cc, bcc, subject, data, contentType, attachments, headers);
    }


    /**
     * <p>
     * This method actually contacts a SMTP server and transmits the message.
     * </p>
     * <p>
     * It first tries to get the configuration of the SMTP server via ConfigManager. In case the search for
     * configuration fails, the default configuration will be used instead.
     * </p>
     * <p>
     * Version 3.0 of Email Engine have added support for priority messages. The priority of message is implemented
     * as message headers and the headers are passed as parameter in the headers <code>Map</code>.
     * </p>
     * <p>
     * Version 3.1 of Email Engine have added content type support.
     * </p>
     * <p>
     * Version 3.2 update:
     * - if the userName/password are both of empty value,
     *   null values for both will be used when doing Transport connection authentication.
     * - Using javax.mail.Session.getInstance instead of javax.mail.Session.getDefaultInstance.
     * </p>
     * @param from          the From address
     * @param to            the To addresses
     * @param cc            the CC addresses
     * @param bcc           the BCC addresses
     * @param subject       the message subject
     * @param data          the message body
     * @param attachments   all message attachments
     * @param headers       the custom headers to set for the message, as defined in TCSEmailMessage. No headers if the
     *                      map is <code>null</code> or empty
     * @param contentType   the message content type, for example "text/plain
     * @throws NullPointerException if any of from, to, subject, contentType, data or attachments is <code>null</code>
     * @throws ConfigManagerException if the engine cannot load properties from the ConfigManager
     * @throws SendingException  if there is a problem sending the message to the server
     *
     * @since 3.0
     */
    protected static void send(Address from, Address[] to, Address[] cc, Address[] bcc, String subject, String data,
        String contentType, Map attachments, Map headers) throws ConfigManagerException, SendingException {
        checkNull(from, "from");
        checkNull(to, "to");
        checkNull(subject, "subject");
        checkNull(data, "data");
        checkNull(attachments, "attachments");
        checkNull(contentType, "contentType");

        javax.mail.Session eMailSession = null;
        Transport eMailTransport = null;
        javax.mail.Message eMailMessage = null;
        //BasicConfigurator.configure();
        //Retrieve configuration properties
        String host = "";
        int port = -1;
        ConfigManager cm = ConfigManager.getInstance();

        Log log = LogManager.getLog(EmailEngine.class.getName());
        log.log(Level.DEBUG, "Getting configuration for Email Engine...");
        if (cm.existsNamespace(PROPERTIES_NAMESPACE)) {
            cm.refresh(PROPERTIES_NAMESPACE);
        } else {
            cm.add(PROPERTIES_NAMESPACE, PROPERTIES_LOCATION, PROPERTIES_FORMAT);
        }
        if (!cm.existsNamespace(PROPERTIES_NAMESPACE)) {
            log.log(Level.ERROR, "Cannot add the namespace.");
            throw new ConfigManagerException("Cannot load the namespace: " + PROPERTIES_NAMESPACE);
        }

        try {
            host = cm.getString(PROPERTIES_NAMESPACE, PROPERTY_SMTP_HOST_ADDR);
        } catch (Exception e) {
            host = null;
            log.log(Level.DEBUG, PROPERTY_SMTP_HOST_ADDR + " cannot be found in the EmailEngine configuration file");
        }
        if (host == null || host.equals("")) {
            log.log(Level.DEBUG, PROPERTY_SMTP_HOST_ADDR + " has an invalid value in the EmailEngine configuration file:" + host);
            host = SMTP_HOST_ADDR;
            log.log(Level.DEBUG, PROPERTY_SMTP_HOST_ADDR + " set to default:" + host);
        }
        String tempPort = "";
        try {
            tempPort = cm.getString(PROPERTIES_NAMESPACE, PROPERTY_SMTP_HOST_PORT);
        } catch (Exception e) {
            log.log(Level.DEBUG, PROPERTY_SMTP_HOST_PORT + " cannot be found in the EmailEngine configuration file");
        }
        try {
            port = Integer.parseInt(tempPort);
        } catch (Exception e) {
            log.log(Level.DEBUG, PROPERTY_SMTP_HOST_PORT + " has an invalid value in the EmailEngine configuration file:" + port);
            port = SMTP_HOST_PORT;
            log.log(Level.DEBUG, PROPERTY_SMTP_HOST_PORT + " set to default:" + port);
        }

        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.transport.protocol", SMTP_HOST_TYPE);
        props.put("mail.host", host);
        props.put("mail.from", from);
        try {
            eMailSession = javax.mail.Session.getInstance(props, null);
            eMailMessage = new MimeMessage(eMailSession);
            eMailTransport = eMailSession.getTransport(SMTP_HOST_TYPE);
            eMailTransport.connect(host, port, getProperty(cm, PROPERTY_SMTP_USER, log),
                                               getProperty(cm, PROPERTY_SMTP_PASSWORD, log));
            eMailMessage.setRecipients(javax.mail.Message.RecipientType.TO, to);
            if (cc != null) {
                eMailMessage.setRecipients(javax.mail.Message.RecipientType.CC, cc);
            }
            if (bcc != null) {
                eMailMessage.setRecipients(javax.mail.Message.RecipientType.BCC, bcc);
            }
            eMailMessage.setFrom(from);
            eMailMessage.setSubject(subject);
            Date sentDate = new Date();
            eMailMessage.setSentDate(sentDate);

            // Create a mulitpart to add the body text and attachments
            Multipart multipart = new MimeMultipart();
            MimeBodyPart bodyPart = new MimeBodyPart();
            bodyPart.setContent(data, contentType); // added in ver3.1
            multipart.addBodyPart(bodyPart);
            Iterator attachmentsIterator = attachments.keySet().iterator();
            Iterator namesIterator = attachments.values().iterator();

            // Iterate through the attachments
            while (namesIterator.hasNext()) {

                // File object is created for use of getName() method, the file is not accessed.
                File file = new File(namesIterator.next().toString());
                String attachmentName = file.getName();

                /*
                 * Create a datasource containing the attachment which will be used to
                 * create the datahandler set to this MimeBodyPart.
                 */
                DataSource dataSource = new AttachmentDataSource((InputStream) attachmentsIterator.next(),
                                                                 attachmentName);
                bodyPart = new MimeBodyPart();
                bodyPart.setDataHandler(new DataHandler(dataSource));
                bodyPart.setFileName(attachmentName);
                multipart.addBodyPart(bodyPart);
            }


            if (headers != null) {
                log.log(Level.DEBUG, "Adding message headers...");
                // Iterate through the attachments when headers exists.
                Iterator headerIterator = headers.entrySet().iterator();
                while (headerIterator.hasNext()) {
                    Map.Entry each = (Entry) headerIterator.next();
                    String headerName = (String) each.getKey();
                    String headerValue = (String) each.getValue();
                    eMailMessage.addHeader(headerName, headerValue);
                }
            }

            // Add the multipart containing the body text and attachments to the message
            eMailMessage.setContent(multipart);

            // Log the email sending
            StringBuffer buffer = new StringBuffer("Sending email to ");
            for (Address address : to) {
                buffer.append(address+",");
            }
            for (Address address : cc) {
                buffer.append(address+",");
            }
            for (Address address : bcc) {
                buffer.append(address+",");
            }
            log.log(Level.INFO, buffer.substring(0, buffer.length()-1));

            // Send the email
            Transport.send(eMailMessage);
        } catch (NoSuchProviderException e) {
            log.log(Level.ERROR, "SMTP transport type not accepted", e);
            throw new SendingException("Internal configuration error. SMTP transport not accepted." + props, e);
        } catch (MessagingException e) {
            log.log(Level.ERROR, "Failed to contact SMTP server", e);
            throw new SendingException("Possible configuration error. SMTP server is not responding." + props, e);
        } catch (IOException e) {
            log.log(Level.ERROR, "Failed to get attachment", e);
            throw new SendingException("Cannot get attachment", e);
        } finally {
            if (eMailTransport != null) {
                try {
                    eMailTransport.close();
                } catch (Exception ignore) {
                }
            }
        }
    }


    /**
     * <p>
     * Retrieves the property from the default namespace.
     * Return null if it is empty value or any error occurs during retrieving.
     * This method is introduced in version 3.2.
     * </p>
     * @param cm the configuration manager
     * @param property the property name to retrieve
     * @return the value of the property, could be null, not empty
     */
    private static String getProperty(ConfigManager cm, String property, Log log) {
        String value = null;
        try {
            value = cm.getString(PROPERTIES_NAMESPACE, property);
        } catch (Exception e) {
            log.log(Level.DEBUG, property + " cannot be found in the EmailEngine configuration file");
        }
        if (value != null && value.trim().length() == 0) {
            value = null;
        }
        if (value == null) {
            log.log(Level.DEBUG, "EmailEngine configuration property " + property + " is null.");
        }
        return value;
    }


    /**
     * <p>Checks whether object is <code>null</code>, <code>NullPointerException</code> is thrown when it is.<p>
     *
     * @param object    the <code>Object</code> to check
     * @param name      the name of the object
     *
     * @throws NullPointerException if object is <code>null</code>
     *
     * @since 3.0
     */
    private static void checkNull(Object object, String name) {
        if (object == null) {
            throw new NullPointerException(name + " should not be null");
        }
    }

    /**
     * Implement <code>ConfigManagerInterface.getConfigPropNames</code>.  Return an the configuration property names.
     *
     * @return    all known property keys for this Component (EmailEngine)
     */
    public Enumeration getConfigPropNames() {
        Vector props = new Vector();
        props.addElement(PROPERTY_SMTP_HOST_ADDR);
        props.addElement(PROPERTY_SMTP_HOST_PORT);
        props.addElement(PROPERTY_SMTP_USER);
        props.addElement(PROPERTY_SMTP_PASSWORD);
        return props.elements();
    }

    /**
     * Implement <code>ConfigManagerInterface.getNamespace</code>.  Return the configuration namespace.
     *
     * @return    ConfigManager namespace for this Component (EmailEngine)
     */
    public String getNamespace() {
        return PROPERTIES_NAMESPACE;
    }

}

