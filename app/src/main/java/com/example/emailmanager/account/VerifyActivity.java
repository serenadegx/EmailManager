package com.example.emailmanager.account;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
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
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityVerifyBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_verify);
        binding.setViewModel(new VerifyModel("imap.qq.com", this, getIntent().getLongExtra("category", 0)));
    }

    public static void start2VerifyActivity(Context context, long categoryId) {
        context.startActivity(new Intent(context, VerifyActivity.class)
                .putExtra("category", categoryId));
    }
}
