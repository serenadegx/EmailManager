package com.example.emailmanager.data.source.remote;

import android.util.Log;

import com.example.emailmanager.EMApplication;
import com.example.emailmanager.data.AccessoryDetail;
import com.example.emailmanager.data.AccountDetail;
import com.example.emailmanager.data.Contacts;
import com.example.emailmanager.data.EmailDetail;
import com.example.emailmanager.data.source.EmailDataSource;
import com.sun.mail.smtp.SMTPTransport;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.mail.util.ByteArrayDataSource;

public class EmailRemoteDataSource implements EmailDataSource {
    private static EmailRemoteDataSource INSTANCE;

    private EmailRemoteDataSource() {
    }

    public static EmailRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            synchronized (EmailRemoteDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new EmailRemoteDataSource();
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void getEmails(final AccountDetail detail, GetEmailsCallBack callBack) {
        List<EmailDetail> data = new ArrayList<>();

        Properties props = System.getProperties();
        props.put(detail.getEmail().getReceiveHostKey(), detail.getEmail().getReceiveHostValue());
        props.put(detail.getEmail().getReceivePortKey(), detail.getEmail().getReceivePortValue());
        props.put(detail.getEmail().getReceiveEncryptKey(), detail.getEmail().getReceiveEncryptValue());
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        detail.getAccount(), detail.getPwd());
            }
        });
//        session.setDebug(true);
        Store store = null;
        Folder inbox = null;
        try {
            store = session.getStore(detail.getEmail().getReceiveProtocol());
            store.connect();
            inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);
            Message[] messages = inbox.getMessages();
            for (Message message : messages) {

                EmailDetail emailDetail = new EmailDetail();
                emailDetail.setId((long) message.getMessageNumber());
                Address[] recipients = message.getRecipients(Message.RecipientType.TO);
                if (recipients != null) {
                    StringBuffer sb = new StringBuffer();
                    for (Address recipient : recipients) {
                        sb.append(((InternetAddress) recipient).getAddress() + ";");
                    }
                    emailDetail.setTo(sb.toString());
                }
                Address[] ccs = message.getRecipients(Message.RecipientType.CC);
                if (ccs != null) {
                    StringBuffer sbCc = new StringBuffer();
                    for (Address recipient : ccs) {
                        sbCc.append(((InternetAddress) recipient).getAddress() + ";");
                    }
                    emailDetail.setCc(sbCc.toString());
                }
                Address[] bccs = message.getRecipients(Message.RecipientType.BCC);
                if (bccs != null) {
                    StringBuffer sbBcc = new StringBuffer();
                    for (Address recipient : bccs) {
                        sbBcc.append(((InternetAddress) recipient).getAddress() + ";");
                    }
                    emailDetail.setBcc(sbBcc.toString());
                }
                InternetAddress address = (InternetAddress) message.getFrom()[0];


                emailDetail.setFrom(address.getAddress());
                emailDetail.setPersonal(address.getPersonal());
                emailDetail.setSubject(message.getSubject());
                emailDetail.setDate(dateFormat(message.getReceivedDate()));
                //仅支持imap
                emailDetail.setRead(message.getFlags().contains(Flags.Flag.SEEN));
                emailDetail.setAccessoryList(new ArrayList<AccessoryDetail>());
                dumpPart(message, emailDetail);
                data.add(emailDetail);
            }
            //排序
        } catch (NoSuchProviderException e) {
            callBack.onDataNotAvailable();
            e.printStackTrace();
        } catch (MessagingException e) {
            callBack.onDataNotAvailable();
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inbox != null)
                    inbox.close();
                if (store != null)
                    store.close();
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
        Collections.reverse(data);
        callBack.onEmailsLoaded(data);

    }

    @Override
    public void getEmail(final AccountDetail detail, long msgNum, GetEmailCallBack callBack) {
        EmailDetail data = null;
        Properties props = System.getProperties();
        props.put(detail.getEmail().getReceiveHostKey(), detail.getEmail().getReceiveHostValue());
        props.put(detail.getEmail().getReceivePortKey(), detail.getEmail().getReceivePortValue());
        props.put(detail.getEmail().getReceiveEncryptKey(), detail.getEmail().getReceiveEncryptValue());
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        detail.getAccount(), detail.getPwd());
            }
        });
//        session.setDebug(true);
        Store store = null;
        Folder inbox = null;
        try {
            store = session.getStore(detail.getEmail().getReceiveProtocol());
            store.connect();
            inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);
            Message message = inbox.getMessage((int) msgNum);
            //标记已读
            message.setFlag(Flags.Flag.SEEN, true);
            data = new EmailDetail();
            data.setId((long) message.getMessageNumber());
            Address[] recipients = message.getRecipients(Message.RecipientType.TO);
            if (recipients != null) {
                StringBuffer sb = new StringBuffer();
                for (Address recipient : recipients) {
                    sb.append(((InternetAddress) recipient).getAddress() + ";");
                }
                data.setTo(sb.toString());
            }
            Address[] ccs = message.getRecipients(Message.RecipientType.CC);
            if (ccs != null) {
                StringBuffer sbCc = new StringBuffer();
                for (Address recipient : ccs) {
                    sbCc.append(((InternetAddress) recipient).getAddress() + ";");
                }
                data.setCc(sbCc.toString());
            }
            Address[] bccs = message.getRecipients(Message.RecipientType.BCC);
            if (bccs != null) {
                StringBuffer sbBcc = new StringBuffer();
                for (Address recipient : bccs) {
                    sbBcc.append(((InternetAddress) recipient).getAddress() + ";");
                }
                data.setBcc(sbBcc.toString());
            }
            InternetAddress address = (InternetAddress) message.getFrom()[0];
            data.setFrom(address.getAddress());
            data.setPersonal(address.getPersonal());
            data.setSubject(message.getSubject());
            data.setDate(dateFormat(message.getReceivedDate()));
            data.setAccessoryList(new ArrayList<AccessoryDetail>());
            dumpPartReal(message, data);
            callBack.onEmailLoaded(data);
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
            callBack.onDataNotAvailable();
        } catch (Exception e) {
            e.printStackTrace();
            callBack.onDataNotAvailable();
        } finally {
//            try {
//                if (inbox != null)
//                    inbox.close();
//                if (store != null)
//                    store.close();
//            } catch (MessagingException e) {
//                e.printStackTrace();
//            }
        }

    }

    @Override
    public void sendEmail(final AccountDetail detail, final EmailDetail data, EmailDataSource.GetResultCallBack callBack) {
        Properties props = System.getProperties();
        props.put(detail.getEmail().getSendHostKey(), detail.getEmail().getSendHostValue());
        props.put(detail.getEmail().getSendPortKey(), detail.getEmail().getSendPortValue());
        props.put(detail.getEmail().getSendEncryptKey(), detail.getEmail().getSendEncryptValue());
        props.put(detail.getEmail().getAuthKey(), detail.getEmail().getAuthValue());
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        detail.getAccount(), detail.getPwd());
            }
        });
        SMTPTransport t = null;
        try {
            Message msg = new MimeMessage(session);
            if (data.getFrom() != null) {
                try {
                    msg.setFrom(new InternetAddress(data.getFrom(), "果心蕊菜牙xr"));
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }

            msg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(data.getTo(), false));
            if (data.getCc() != null)
                //抄送人
                msg.setRecipients(Message.RecipientType.CC,
                        InternetAddress.parse(data.getCc(), false));
            if (data.getBcc() != null)
                //秘密抄送人
                msg.setRecipients(Message.RecipientType.BCC,
                        InternetAddress.parse(data.getBcc(), false));

            msg.setSubject(data.getSubject());

            MimeMultipart mp = new MimeMultipart();
            MimeBodyPart mbp1 = new MimeBodyPart();
            mbp1.setText(data.getContent());
            mp.addBodyPart(mbp1);
            if (data.getAccessoryList() != null && data.getAccessoryList().size() > 0) {
                for (AccessoryDetail detail1 : data.getAccessoryList()) {
                    MimeBodyPart mbp2 = new MimeBodyPart();
                    mbp2.attachFile(detail1.getPath());
                    mp.addBodyPart(mbp2);
                }
            }
            msg.setContent(mp);
            msg.setSentDate(new Date());
            t = (SMTPTransport) session.getTransport(detail.getEmail().getSendProtocol());
            t.connect();
            t.sendMessage(msg, msg.getAllRecipients());
            callBack.onSuccess();
        } catch (SendFailedException e) {
            e.printStackTrace();
            callBack.onError("发送失败");
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                t.close();
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void reply(final AccountDetail detail, EmailDetail data, GetResultCallBack callBack) {
        Properties props = System.getProperties();
        props.put(detail.getEmail().getSendHostKey(), detail.getEmail().getSendHostValue());
        props.put(detail.getEmail().getSendPortKey(), detail.getEmail().getSendPortValue());
        props.put(detail.getEmail().getSendEncryptKey(), detail.getEmail().getSendEncryptValue());
        props.put(detail.getEmail().getAuthKey(), detail.getEmail().getAuthValue());
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        detail.getAccount(), detail.getPwd());
            }
        });
        Folder folder = null;
        Store store = null;
        SMTPTransport t = null;
        try {
            store = session.getStore(detail.getEmail().getReceiveProtocol());
            store.connect();
            folder = store.getFolder("inbox");
            folder.open(Folder.READ_ONLY);
            Message message = folder.getMessage((int) data.getId().longValue());
            Message forward = new MimeMessage(session);
            if (data.getFrom() != null) {
                forward.setFrom(new InternetAddress(data.getFrom(), "果心蕊菜牙xr"));
            }

            forward.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(data.getTo(), false));
            if (data.getCc() != null)
                //抄送人
                forward.setRecipients(Message.RecipientType.CC,
                        InternetAddress.parse(data.getCc(), false));
            if (data.getBcc() != null)
                //秘密抄送人
                forward.setRecipients(Message.RecipientType.BCC,
                        InternetAddress.parse(data.getBcc(), false));

            forward.setSubject(data.getSubject());

            MimeMultipart mp = new MimeMultipart();
            MimeBodyPart mbp1 = new MimeBodyPart();
            mbp1.setDataHandler(collect(data, message));
            mp.addBodyPart(mbp1);
            if (data.getAccessoryList() != null && data.getAccessoryList().size() > 0) {
                for (AccessoryDetail detail1 : data.getAccessoryList()) {
                    MimeBodyPart mbp2 = new MimeBodyPart();
                    mbp2.attachFile(detail1.getFileName());
                    mp.addBodyPart(mbp2);
                }
            }
            forward.setContent(mp);
            forward.saveChanges();
            forward.setSentDate(new Date());
            t = (SMTPTransport) session.getTransport(detail.getEmail().getSendProtocol());
            t.connect();
            t.sendMessage(forward, forward.getAllRecipients());
            callBack.onSuccess();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            callBack.onError(e.toString());
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            callBack.onError(e.toString());
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                t.close();
                folder.close();
                store.close();
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void forward(AccountDetail detail, EmailDetail email, GetResultCallBack callBack) {
        reply(detail, email, callBack);
    }

    @Override
    public void save2Drafts(final AccountDetail detail, EmailDetail data, GetResultCallBack callBack) {
        Properties props = System.getProperties();
        props.put(detail.getEmail().getSendHostKey(), detail.getEmail().getSendHostValue());
        props.put(detail.getEmail().getSendPortKey(), detail.getEmail().getSendPortValue());
        props.put(detail.getEmail().getSendEncryptKey(), detail.getEmail().getSendEncryptValue());
        props.put(detail.getEmail().getAuthKey(), detail.getEmail().getAuthValue());
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        detail.getAccount(), detail.getPwd());
            }
        });
        MimeMessage msg = new MimeMessage(session);
        Folder drafts = null;
        Store store = null;
        try {
            //打开草稿箱
            store = session.getStore(detail.getEmail().getReceiveProtocol());
            store.connect();
            drafts = store.getFolder("Drafts");

            if (data.getFrom() != null) {
                msg.setFrom(new InternetAddress(data.getFrom(), "果心蕊菜牙xr"));
            }
            msg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(data.getTo(), false));
            if (data.getCc() != null)
                //抄送人
                msg.setRecipients(Message.RecipientType.CC,
                        InternetAddress.parse(data.getCc(), false));
            if (data.getBcc() != null)
                //秘密抄送人
                msg.setRecipients(Message.RecipientType.BCC,
                        InternetAddress.parse(data.getBcc(), false));

            msg.setSubject(data.getSubject());

            MimeMultipart mp = new MimeMultipart();
            MimeBodyPart mbp1 = new MimeBodyPart();
            mbp1.setText(data.getContent());
            mp.addBodyPart(mbp1);
            if (data.getAccessoryList() != null && data.getAccessoryList().size() > 0) {
                for (AccessoryDetail detail1 : data.getAccessoryList()) {
                    MimeBodyPart mbp2 = new MimeBodyPart();
                    mbp2.attachFile(detail1.getFileName());
                    mp.addBodyPart(mbp2);
                }
            }
            msg.setContent(mp);
            msg.setSentDate(new Date());

            //保存到草稿箱
            msg.saveChanges();
            msg.setFlag(Flags.Flag.DRAFT, true);
            MimeMessage[] draftMessages = {msg};
            drafts.appendMessages(draftMessages);
            callBack.onSuccess();
        } catch (MessagingException e) {
            e.printStackTrace();
            callBack.onError("保存失败");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
//                drafts.close();
                store.close();
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void deleteEmail(final AccountDetail detail, long id, GetResultCallBack callBack) {
        Properties props = System.getProperties();
        props.put(detail.getEmail().getReceiveHostKey(), detail.getEmail().getReceiveHostValue());
        props.put(detail.getEmail().getReceivePortKey(), detail.getEmail().getReceivePortValue());
        props.put(detail.getEmail().getReceiveEncryptKey(), detail.getEmail().getReceiveEncryptValue());
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        detail.getAccount(), detail.getPwd());
            }
        });
//        session.setDebug(true);
        Store store = null;
        Folder inbox = null;
        try {
            store = session.getStore(detail.getEmail().getReceiveProtocol());
            store.connect();
            inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_WRITE);
            Message message = inbox.getMessage((int) id);
            message.setFlag(Flags.Flag.DELETED, true);
            callBack.onSuccess();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            callBack.onError("删除失败");
            e.printStackTrace();
        } finally {
            try {
                inbox.close();
                store.close();
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void signRead(AccountDetail detail, EmailDetail email) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public void saveAll(List<EmailDetail> emails) {

    }

    public static void dumpPart(Part p, EmailDetail data) {
        try {
            if (p.isMimeType("text/plain")) {
//                System.out.println((String) p.getContent());
            } else if (p.isMimeType("multipart/*")) {
                if (p.isMimeType("multipart/report")) {
                    //                MimeMessage cmsg = new MimeMessage((MimeMessage) p);
                    //                int count = cmsg.getCount();
                    //                for (int i = 0; i < count; i++)
                    //                    dumpPart(mp.getBodyPart(i), data);
                    return;
                } else {
                    Multipart mp = (Multipart) p.getContent();
                    int count = mp.getCount();
                    for (int i = 0; i < count; i++)
                        dumpPart(mp.getBodyPart(i), data);
                }

            } else if (p.isMimeType("message/rfc822")) {
                dumpPart((Part) p.getContent(), data);
            } else {
                Object o = p.getContent();
                if (o instanceof InputStream) {
                    if (p.getFileName() != null) {
                        data.getAccessoryList().add(new AccessoryDetail(data.getId(), MimeUtility
                                .decodeText(p.getFileName()), getPrintSize(p.getSize())));
                    }
                } else if (o instanceof String) {
                    data.setContent((String) o);
                } else {
                    Log.i("mango", "未知类型邮件");
                }
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void dumpPartReal(Part p, EmailDetail data) {
        String filename = null;
        try {
            filename = p.getFileName();
            if (p.isMimeType("text/plain")) {
                data.setContent((String) p.getContent());
            } else if (p.isMimeType("multipart/*")) {
                Multipart mp = (Multipart) p.getContent();
                int count = mp.getCount();
                for (int i = 0; i < count; i++)
                    dumpPartReal(mp.getBodyPart(i), data);
            } else if (p.isMimeType("message/rfc822")) {
                dumpPartReal((Part) p.getContent(), data);
            } else {
                Object o = p.getContent();
                if (o instanceof InputStream) {
                    if (filename != null) {
                        InputStream is = (InputStream) o;
                        data.getAccessoryList().add(new AccessoryDetail(MimeUtility.decodeText(filename), getPrintSize(p.getSize()), p.getSize(), is));
                    }
                } else if (o instanceof String) {
                    data.setContent((String) o);
                } else {
                    Log.i("mango", "未知类型邮件");
                }
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String getPrintSize(long size) {
        //如果字节数少于1024，则直接以B为单位，否则先除于1024，后3位因太少无意义
        if (size < 1024) {
            return String.valueOf(size) + " B";
        } else {
            size = size / 1024;
        }
        //如果原字节数除于1024之后，少于1024，则可以直接以KB作为单位
        //因为还没有到达要使用另一个单位的时候
        //接下去以此类推
        if (size < 1024) {
            return String.valueOf(size) + " KB";
        } else {
            size = size / 1024;
        }
        if (size < 1024) {
            //因为如果以MB为单位的话，要保留最后1位小数，
            //因此，把此数乘以100之后再取余
            size = size * 100;
            return String.valueOf((size / 100)) + "."
                    + String.valueOf((size * 100 / 1024 % 100)) + "MB";
        } else {
            //否则如果要以GB为单位的，先除于1024再作同样的处理
            size = size * 100 / 1024;
            return String.valueOf((size / 100)) + "."
                    + String.valueOf((size % 100)) + " GB";
        }
    }

    public static String dateFormat(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }

    public static DataHandler collect(EmailDetail data, Message msg) throws MessagingException, IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(data.getContent() + "<br>");
        sb.append("<div style=\"line-height:1.5\"><br><br>-------- 原始邮件 --------<br>");
        sb.append("主题：" + msg.getSubject() + "<br>");
        sb.append("发件人：" + ((InternetAddress) msg.getFrom()[0]).getAddress() + "<br>");

        if (msg.getRecipients(Message.RecipientType.TO) != null) {
            sb.append("收件人：");
            for (Address recipient : msg.getRecipients(Message.RecipientType.TO)) {
                sb.append(((InternetAddress) recipient).getAddress() + ";");
            }
            sb.append("<br>");
        }

        if (msg.getRecipients(Message.RecipientType.CC) != null) {
            sb.append("抄送：");
            for (Address recipient : msg.getRecipients(Message.RecipientType.CC)) {
                sb.append(((InternetAddress) recipient).getAddress() + ";");
            }
            sb.append("<br>");
        }

        if (msg.getRecipients(Message.RecipientType.BCC) != null) {
            sb.append("密送：");
            for (Address recipient : msg.getRecipients(Message.RecipientType.BCC)) {
                sb.append(((InternetAddress) recipient).getAddress() + ";");
            }
            sb.append("<br>");
        }
        sb.append("发件时间：" + dateFormat(msg.getReceivedDate()) + "<br>");
        sb.append("</div><br>");
        sb.append(data.getContent());
        return new DataHandler(
                new ByteArrayDataSource(sb.toString(), "text/html"));
    }

    public void loadSentMessage(final AccountDetail detail, GetEmailsCallBack callBack) {
        List<EmailDetail> data = new ArrayList<>();
        Properties props = System.getProperties();
        props.put(detail.getEmail().getReceiveHostKey(), detail.getEmail().getReceiveHostValue());
        props.put(detail.getEmail().getReceivePortKey(), detail.getEmail().getReceivePortValue());
        props.put(detail.getEmail().getReceiveEncryptKey(), detail.getEmail().getReceiveEncryptValue());
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        detail.getAccount(), detail.getPwd());
            }
        });
//        session.setDebug(true);
        Store store = null;
        Folder inbox = null;
        try {
            store = session.getStore(detail.getEmail().getReceiveProtocol());
            store.connect();
            inbox = store.getFolder("Sent Messages");
            inbox.open(Folder.READ_ONLY);
            Message[] messages = inbox.getMessages();
            for (Message message : messages) {
                InternetAddress address = (InternetAddress) message.getFrom()[0];
                InternetAddress to = (InternetAddress) message.getRecipients(Message.RecipientType.TO)[0];
                StringBuilder sb = new StringBuilder();
                for (Address toAddress : message.getRecipients(Message.RecipientType.TO)) {
                    sb.append(((InternetAddress) toAddress).getPersonal() + ",");
                }
                sb.replace(sb.length() - 1, sb.length(), "");
                EmailDetail emailDetail = new EmailDetail((long) message.getMessageNumber(), message.getSubject(),
                        dateFormat(message.getReceivedDate()), sb.toString());
                //发件箱默认已读
                emailDetail.setRead(true);
                data.add(emailDetail);
            }
            Collections.reverse(data);
            callBack.onEmailsLoaded(data);
        } catch (NoSuchProviderException e) {
            callBack.onDataNotAvailable();
            e.printStackTrace();
        } catch (MessagingException e) {
            callBack.onDataNotAvailable();
            e.printStackTrace();
        } finally {
            try {
                if (inbox != null)
                    inbox.close();
                if (store != null)
                    store.close();
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
    }

    public void loadDrafts(final AccountDetail detail, GetEmailsCallBack callBack) {
        List<EmailDetail> data = new ArrayList<>();
        Properties props = System.getProperties();
        props.put(detail.getEmail().getReceiveHostKey(), detail.getEmail().getReceiveHostValue());
        props.put(detail.getEmail().getReceivePortKey(), detail.getEmail().getReceivePortValue());
        props.put(detail.getEmail().getReceiveEncryptKey(), detail.getEmail().getReceiveEncryptValue());
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        detail.getAccount(), detail.getPwd());
            }
        });
//        session.setDebug(true);
        Store store = null;
        Folder inbox = null;
        try {
            store = session.getStore(detail.getEmail().getReceiveProtocol());
            store.connect();
            inbox = store.getFolder("Drafts");
            inbox.open(Folder.READ_ONLY);
            Message[] messages = inbox.getMessages();
            for (Message message : messages) {
                InternetAddress address = (InternetAddress) message.getFrom()[0];
                InternetAddress to = (InternetAddress) message.getRecipients(Message.RecipientType.TO)[0];
                StringBuilder sb = new StringBuilder();
                for (Address toAddress : message.getRecipients(Message.RecipientType.TO)) {
                    sb.append(((InternetAddress) toAddress).getPersonal() + ",");
                }
                sb.replace(sb.length() - 1, sb.length(), "");
                EmailDetail emailDetail = new EmailDetail((long) message.getMessageNumber(), message.getSubject(),
                        dateFormat(message.getReceivedDate()), sb.toString());
                //发件箱默认已读
                emailDetail.setRead(true);
                data.add(emailDetail);
            }
            Collections.reverse(data);
            callBack.onEmailsLoaded(data);
        } catch (NoSuchProviderException e) {
            callBack.onDataNotAvailable();
            e.printStackTrace();
        } catch (MessagingException e) {
            callBack.onDataNotAvailable();
            e.printStackTrace();
        } finally {
            try {
                inbox.close();
                store.close();
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
    }
}
