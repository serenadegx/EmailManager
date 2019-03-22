package com.example.emailmanager.account;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.emailmanager.R;
import com.example.emailmanager.databinding.ActivityVerifyBinding;

public class VerifyActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityVerifyBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_verify);
        binding.setViewModel(new VerifyModel("imap.qq.com", this));
    }
}
