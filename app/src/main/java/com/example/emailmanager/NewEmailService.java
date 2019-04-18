package com.example.emailmanager;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.sun.mail.imap.IMAPFolder;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.event.MessageCountAdapter;
import javax.mail.event.MessageCountEvent;
import javax.mail.internet.InternetAddress;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class NewEmailService extends Service {


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        new Thread() {
            @Override
            public void run() {
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
                try {
                    store = session.getStore("imap");
                    store.connect();
                    Folder folder = store.getFolder("INBOX");
                    folder.open(Folder.READ_WRITE);
                    folder.addMessageCountListener(new MessageCountAdapter() {
                        @Override
                        public void messagesAdded(MessageCountEvent e) {
                            final Message[] messages = e.getMessages();
                            if (messages != null && messages.length > 0) {
                                Log.i("Mango", "您有" + messages.length + "条新邮件");
                                try {
                                    notifyNewEmail(messages);
                                } catch (MessagingException e1) {
                                    e1.printStackTrace();
                                }
                            }
                            super.messagesAdded(e);
                        }
                    });
                    boolean supportsIdle = false;
                    if (folder instanceof IMAPFolder) {
                        IMAPFolder f = (IMAPFolder) folder;
                        f.idle();
                        supportsIdle = true;
                    }
                    for (; ; ) {
                        if (supportsIdle && folder instanceof IMAPFolder) {
                            IMAPFolder f = (IMAPFolder) folder;
                            f.idle();
                        } else {
                            Thread.sleep(1000); // sleep for freq milliseconds
                            //这将强制IMAP服务器发送给我们通知
                            folder.getMessageCount();
                        }
                    }
                } catch (NoSuchProviderException e) {
                    e.printStackTrace();
                } catch (MessagingException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Log.i("Mango", "NewEmailService --> end");
        super.onDestroy();
    }

    public void notifyNewEmail(Message[] messages) throws MessagingException {
        for (Message message : messages) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            Notification notification = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel mChannel = new NotificationChannel(message.getMessageNumber() + "", "name1", NotificationManager.IMPORTANCE_LOW);
                notificationManager.createNotificationChannel(mChannel);
                notification = new Notification.Builder(this)
                        .setChannelId(message.getMessageNumber() + "")
                        .setContentTitle(((InternetAddress) message.getFrom()[0]).getAddress())
                        .setContentText(message.getSubject())
                        .setSmallIcon(R.mipmap.ic_launcher).build();
            } else {
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                        .setContentTitle(((InternetAddress) message.getFrom()[0]).getAddress())
                        .setContentText(message.getSubject())
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setOngoing(true);//无效
                notification = notificationBuilder.build();
            }
            notificationManager.notify(message.getMessageNumber(), notification);
        }


    }

}
