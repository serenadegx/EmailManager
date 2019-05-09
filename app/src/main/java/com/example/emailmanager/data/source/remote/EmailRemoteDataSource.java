package com.example.emailmanager.data.source.remote;

import android.util.Log;

import com.example.emailmanager.EMApplication;
import com.example.emailmanager.data.AccessoryDetail;
import com.example.emailmanager.data.AccountDetail;
import com.example.emailmanager.data.Contacts;
import com.example.emailmanager.data.EmailDetail;
import com.example.emailmanager.data.source.EmailDataSource;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;

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
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeUtility;

public class EmailRemoteDataSource implements EmailDataSource {
    @Override
    public void getEmails(final AccountDetail detail, GetEmailsCallBack callBack) {
        List<EmailDetail> data = new ArrayList<>();
        List<Contacts> contacts = new ArrayList<>();
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
                Address[] recipients = message.getRecipients(Message.RecipientType.TO);
                StringBuffer sb = new StringBuffer();
                for (Address recipient : recipients) {
                    sb.append(((InternetAddress) recipient).getAddress() + ";");
                }
                emailDetail.setTo(sb.toString());
                Address[] ccs = message.getRecipients(Message.RecipientType.CC);
                StringBuffer sbCc = new StringBuffer();
                if (ccs != null) {
                    for (Address recipient : ccs) {
                        sbCc.append(((InternetAddress) recipient).getAddress() + ";");
                    }
                    emailDetail.setCc(sbCc.toString());
                }
                StringBuffer sbBcc = new StringBuffer();
                Address[] bccs = message.getRecipients(Message.RecipientType.BCC);
                if (bccs != null) {
                    for (Address recipient : bccs) {
                        sbBcc.append(((InternetAddress) recipient).getAddress() + ";");
                    }
                    emailDetail.setBcc(sbBcc.toString());
                }
                InternetAddress address = (InternetAddress) message.getFrom()[0];

                Contacts contact = new Contacts(address.getPersonal(), address.getAddress());
                if (!contacts.contains(contact))
                    contacts.add(contact);

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
        EMApplication.setContacts(contacts);
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
            Address[] recipients = message.getRecipients(Message.RecipientType.TO);
            StringBuffer sb = new StringBuffer();
            for (Address recipient : recipients) {
                sb.append(((InternetAddress) recipient).getAddress() + ";");
            }
            data.setTo(sb.toString());
            Address[] ccs = message.getRecipients(Message.RecipientType.CC);
            StringBuffer sbCc = new StringBuffer();
            if (ccs != null) {
                for (Address recipient : ccs) {
                    sbCc.append(((InternetAddress) recipient).getAddress() + ";");
                }
                data.setCc(sbCc.toString());
            }
            StringBuffer sbBcc = new StringBuffer();
            Address[] bccs = message.getRecipients(Message.RecipientType.BCC);
            if (bccs != null) {
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
            dumpPartReal(message, data);
            callBack.onEmailLoaded(data);
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
            callBack.onDataNotAvailable();
        } catch (MessagingException e) {
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
    public void sendEmail(EmailDetail email) {

    }

    @Override
    public void deleteEmail(String id) {

    }

    @Override
    public void reply(EmailDetail emailDetail) {

    }

    @Override
    public void signRead(EmailDetail emailDetail) {

    }

    @Override
    public void forward(EmailDetail emailDetail) {

    }

    public static void dumpPart(Part p, EmailDetail data) throws Exception {
        String filename = p.getFileName();
        if (p.isMimeType("text/plain")) {
            System.out.println((String) p.getContent());
        } else if (p.isMimeType("multipart/*")) {
            Multipart mp = (Multipart) p.getContent();
            int count = mp.getCount();
            for (int i = 0; i < count; i++)
                dumpPart(mp.getBodyPart(i), data);
        } else if (p.isMimeType("message/rfc822")) {
            dumpPart((Part) p.getContent(), data);
        } else {
            Object o = p.getContent();
            if (o instanceof InputStream) {
                if (filename != null) {
                    data.getAccessoryList().add(new AccessoryDetail(data.getId(), MimeUtility
                            .decodeText(filename), getPrintSize(p.getSize())));
                }
            } else if (o instanceof String) {
                data.setContent((String) o);
            } else {
                Log.i("mango", "未知类型邮件");
            }
        }
    }

    public static void dumpPartReal(Part p, EmailDetail data) throws Exception {
        String filename = p.getFileName();
        if (p.isMimeType("text/plain")) {
            System.out.println((String) p.getContent());
        } else if (p.isMimeType("multipart/*")) {
            Multipart mp = (Multipart) p.getContent();
            int count = mp.getCount();
            for (int i = 0; i < count; i++)
                dumpPart(mp.getBodyPart(i), data);
        } else if (p.isMimeType("message/rfc822")) {
            dumpPart((Part) p.getContent(), data);
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
}
