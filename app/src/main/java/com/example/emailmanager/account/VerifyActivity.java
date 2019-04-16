package com.example.emailmanager.account;

import android.os.Bundle;

import com.example.emailmanager.R;
import com.example.emailmanager.databinding.ActivityVerifyBinding;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

public class VerifyActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityVerifyBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_verify);
        binding.setViewModel(new VerifyModel("imap.qq.com", this));
    }
}
