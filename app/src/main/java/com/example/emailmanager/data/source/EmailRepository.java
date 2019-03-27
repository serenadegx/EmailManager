package com.example.emailmanager.data.source;

import android.text.TextUtils;
import android.util.Log;

import com.example.emailmanager.data.AccessoryDetail;
import com.example.emailmanager.data.AccountDetail;
import com.example.emailmanager.data.EmailDetail;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
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
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeUtility;

public class EmailRepository {

    public void loadData(EmailDataSource.GetEmailsCallBack callBack) {
//        QueryBuilder<AccountDetail> queryBuilder = EMApplication.getDaoSession().getAccountDetailDao().queryBuilder();
//        Join accountJoin = queryBuilder.join(AccountDetailDao.Properties.CustomId, AccountDetail.class);
//        queryBuilder.join(accountJoin, SendDao.Properties.Id, Send.class, ReceiverDao.Properties.Id);
//        List<AccountDetail> accounts = queryBuilder.where(AccountDetailDao.Properties.Enable.eq(true)).list();
//        Log.i("mango", "loading:");
//        if (accounts != null && accounts.size() > 0) {
//            //远程数据
//        }
        remoteData(new AccountDetail(), callBack);
    }

    private void remoteData(final AccountDetail accountDetail, EmailDataSource.GetEmailsCallBack callBack) {
        List<EmailDetail> data = new ArrayList<>();
        Properties props = System.getProperties();
        props.put("mail.imap.host", "imap.qq.com");
        props.put("mail.imap.port", "993");
        props.put("mail.imap.ssl.enable", true);
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        "1099805713@qq.com", "pfujejqwrezxgbjj");
            }
        });
        session.setDebug(true);
        Store store = null;
        Folder inbox = null;
        try {
//            store = session.getStore(accountDetail.getReceiver().getProtocol());
            store = session.getStore("imap");
            store.connect();
            inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);
            Message[] messages = inbox.getMessages();
            for (Message message : messages) {
                InternetAddress address = (InternetAddress) message.getFrom()[0];
                EmailDetail emailDetail = new EmailDetail(message.getMessageNumber(), message.getSubject(),
                        dateFormat(message.getReceivedDate()), TextUtils.isEmpty(address.getPersonal())
                        ? address.getAddress() : address.getPersonal());
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

    public EmailDetail loadRemoteDataById(int msgNum) {
        EmailDetail data = null;
        Properties props = System.getProperties();
        props.put("mail.imap.host", "imap.qq.com");
        props.put("mail.imap.port", "993");
        props.put("mail.imap.ssl.enable", true);
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        "1099805713@qq.com", "pfujejqwrezxgbjj");
            }
        });
        session.setDebug(true);
        Store store = null;
        Folder inbox = null;
        try {
            store = session.getStore("imap");
            store.connect();
            inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);
            Message message = inbox.getMessage(msgNum);
            InternetAddress address = (InternetAddress) message.getFrom()[0];
            data = new EmailDetail(message.getMessageNumber(), message.getSubject(),
                    dateFormat(message.getReceivedDate()), TextUtils.isEmpty(address.getPersonal())
                    ? address.getAddress() : address.getPersonal());
            Address[] recipients = message.getRecipients(Message.RecipientType.TO);
            StringBuffer sb = new StringBuffer();
            for (Address recipient : recipients) {
                sb.append(((InternetAddress) recipient).getAddress() + ";");
            }
            data.setTo(sb.toString());
            dumpPart(message, data);
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
//                inbox.close();
                store.close();
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    public static String dateFormat(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }

    public static void dumpPart(Part p, EmailDetail data) throws Exception {

//        if (p instanceof Message)
//            dumpEnvelope((Message) p, data);

        /** Dump input stream ..

         InputStream is = p.getInputStream();
         // If "is" is not already buffered, wrap a BufferedInputStream
         // around it.
         if (!(is instanceof BufferedInputStream))
         is = new BufferedInputStream(is);
         int c;
         while ((c = is.read()) != -1)
         System.out.write(c);
         **/

        String ct = p.getContentType();
        String filename = p.getFileName();
        /*
         * Using isMimeType to determine the content type avoids
         * fetching the actual content data until we need it.
         */
        if (p.isMimeType("text/plain")) {
            System.out.println((String) p.getContent());
            Log.i("mango", "Content: " + p.getContent());
//            data.setContent((String) p.getContent());
        } else if (p.isMimeType("multipart/*")) {
            Multipart mp = (Multipart) p.getContent();
//            level++;
            int count = mp.getCount();
            for (int i = 0; i < count; i++)
                dumpPart(mp.getBodyPart(i), data);
//            level--;
        } else if (p.isMimeType("message/rfc822")) {
            dumpPart((Part) p.getContent(), data);
//            level--;
        } else {
            Object o = p.getContent();
            if (o instanceof InputStream) {
                InputStream is = (InputStream) o;
//                data.setContent(convertStreamToString(is));
                if (filename != null) {
                    data.getAccessoryList().add(new AccessoryDetail(MimeUtility.decodeText(filename), "", getPrintSize(p.getSize()), false));
                    Log.i("mango", "FILENAME: " + MimeUtility.decodeText(filename));
                }
                Log.i("mango", "未知类型邮件InputStream");
            } else if (o instanceof String) {
                data.setContent((String) o);
                Log.i("mango", "未知类型邮件string");
            } else {
                Log.i("mango", "未知类型邮件");
            }
        }

        /*
         * If we're saving attachments, write out anything that
         * looks like an attachment into an appropriately named
         * file.  Don't overwrite existing files to prevent
         * mistakes.
         */

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

    //    public static void dumpEnvelope(Message m, EmailDetail data) throws Exception {
//        Address[] a;
//        // FROM
//        if ((a = m.getFrom()) != null) {
//            for (int j = 0; j < a.length; j++)
//                pr("FROM: " + a[j].toString());
//        }
//
//        // REPLY TO
//        if ((a = m.getReplyTo()) != null) {
//            for (int j = 0; j < a.length; j++)
//                pr("REPLY TO: " + a[j].toString());
//        }
//
//        // TO
//        if ((a = m.getRecipients(Message.RecipientType.TO)) != null) {
//            for (int j = 0; j < a.length; j++) {
//                pr("TO: " + a[j].toString());
//                InternetAddress ia = (InternetAddress) a[j];
//                if (ia.isGroup()) {
//                    InternetAddress[] aa = ia.getGroup(false);
//                    for (int k = 0; k < aa.length; k++)
//                        pr("  GROUP: " + aa[k].toString());
//                }
//            }
//        }
//
//        // SUBJECT
//        m.getSubject();
//
//        // DATE
//        m.getSentDate();
//
//    }
    public void saveContent(String content) {
    }

    public void saveAttachment() {
    }

    public static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "/n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
