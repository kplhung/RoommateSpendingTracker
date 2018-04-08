package com.example.kallyruan.roommateexpense.EmailPkg;

import javax.activation.DataHandler;

import javax.activation.DataSource;

import javax.activation.FileDataSource;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Security;
import java.util.Properties;

/**
 * This class is adapted from Ravi Sharma's tutorial on OodlesTechnologies:
 * http://www.oodlestechnologies.com/blogs/Send-Mail-in-Android-without-Using-Intent
 */

public class GMailSender extends javax.mail.Authenticator {
    private String mailhost = "smtp.gmail.com";
    private String user;
    private String password;
    private Session session;

    private Multipart _multipart = new MimeMultipart();

    // security permissions so SSL content can be handled
    static {
        Security.addProvider(new JSSEProvider());
    }

    /**
     * Establishes connection to specified GMail account, given password
     * Warning: this will validate and send emails from the specified GMail account;
     * do not use a personal email
     * @param user gmail address to login from
     * @param password of gmail address
     */
    public GMailSender(String user, String password) {
        this.user = user;
        this.password = password;

        // declares port number and protocol to be used
        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.host", mailhost);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.quitwait", "false");

        session = Session.getDefaultInstance(props, this);
    }

    /**
     * Checks password authentication
     * @return PasswordAuthentication object
     */
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(user, password);
    }

    /**
     * Sends mail
     * @param subject of email
     * @param body of email
     * @param sender from which email will be sent; should be same as the one
     *               specified in constructor
     * @param recipients of email
     * @throws Exception if unexpected error occurs
     */
    public synchronized void sendMail(String subject, String body,
                                      String sender, String recipients) throws Exception {
        try {
            MimeMessage message = new MimeMessage(session);
            DataHandler handler = new DataHandler(new ByteArrayDataSource(
                    body.getBytes(), "text/plain"));
            message.setSender(new InternetAddress(sender));
            message.setSubject(subject);
            message.setDataHandler(handler);
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(body);
            _multipart.addBodyPart(messageBodyPart);

            // put parts in message
            message.setContent(_multipart);

            if (recipients.indexOf(',') > 0) {
                message.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(recipients));
            } else {
                message.setRecipient(Message.RecipientType.TO,
                        new InternetAddress(recipients));
            }

            // send message
            Transport.send(message);
        } catch (Exception e) { }
    }

    /**
     * Adds attachment to email. Utility method for future development.
     * @param filename to be sent
     * @throws Exception
     */
    public void addAttachment(String filename) throws Exception {
        BodyPart messageBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource(filename);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName("download image");
        _multipart.addBodyPart(messageBodyPart);
    }

    /**
     * Inner class; used for connecting data source
     */
    public class ByteArrayDataSource implements DataSource {
        private byte[] data;
        private String type;
        public ByteArrayDataSource(byte[] data, String type) {
            super();
            this.data = data;
            this.type = type;
        }

        public ByteArrayDataSource(byte[] data) {
            super();
            this.data = data;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getContentType() {
            if (type == null) {
                return "application/octet-stream";
            } else {
                return type;
            }
        }

        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(data);
        }

        public String getName() {
            return "ByteArrayDataSource";
        }

        public OutputStream getOutputStream() throws IOException {
            throw new IOException("Not Supported");
        }
    }
}