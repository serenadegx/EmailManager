package com.example.emailmanager.account;

public class EmailConstant {
    public static final int QQ_EMAIL = 1;
    public static final int EMAIL_163 = 2;
    public static final int ALIYUN_EMAIL = 3;

    public static class QQEmail {
        /**
         * 接受服务器配置
         */
        public static final String receive_host_Key = "mail.imap.host";
        public static final String receive_host_Value = "imap.qq.com";
        public static final String receive_port_Key = "mail.imap.port";
        public static final String receive_port_Value = "993";
        public static final String receive_encrypt_Key = "mail.imap.ssl.enable";
        public static final boolean receive_encrypt_Value = true;
        /**
         * 发送服务器配置
         */
        public static final String send_host_Key = "mail.smtp.host";
        public static final String send_host_Value = "smtp.qq.com";
        public static final String send_port_Key = "mail.smtp.port";
        public static final String send_port_Value = "465";
        public static final String send_encrypt_Key = "mail.smtp.ssl.enable";
        public static final boolean send_encrypt_Value = true;
    }
}
