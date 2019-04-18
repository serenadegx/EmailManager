package com.example.emailmanager.account;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.example.emailmanager.NewEmailService;
import com.example.emailmanager.R;
import com.example.emailmanager.databinding.ActivityVerifyBinding;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.databinding.DataBindingUtil;

public class VerifyActivity extends AppCompatActivity {
    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityVerifyBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_verify);
        binding.setViewModel(new VerifyModel("imap.qq.com", this));
        Intent intentService = new Intent(this, NewEmailService.class);
        startService(intentService);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel("1001", "name1", NotificationManager.IMPORTANCE_LOW);
            notificationManager.createNotificationChannel(mChannel);
            notification = new Notification.Builder(this)
                    .setChannelId("1001")
                    .setContentTitle("new messages")
                    .setContentText("hahaha")
                    .setSmallIcon(R.mipmap.ic_launcher).build();
        } else {
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setContentTitle("5 new messages")
                    .setContentText("hahaha")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setOngoing(true);//无效
            notification = notificationBuilder.build();
        }
        notificationManager.notify(111123, notification);
    }
}
