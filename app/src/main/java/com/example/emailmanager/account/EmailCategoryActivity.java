package com.example.emailmanager.account;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.emailmanager.EMApplication;
import com.example.emailmanager.R;
import com.example.emailmanager.account.adapter.EmailCategoryAdapter;
import com.example.emailmanager.data.Email;
import com.example.emailmanager.databinding.ActivityEmailCategoryBinding;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

public class EmailCategoryActivity extends AppCompatActivity {

    private ActivityEmailCategoryBinding binding;
    private EmailCategoryAdapter listAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_email_category);
        binding.rv.setLayoutManager(new LinearLayoutManager(this));
        initAdapter();
        iniData();
    }

    private void iniData() {
        List<Email> emails = EMApplication.getDaoSession().getEmailDao().loadAll();
        for (Email email : emails) {
            Log.i("mango", "id:" + email.getCategoryId() + "  ReceiveProtocol:" + email.getReceiveProtocol() + "  ReceiveHost:" + email.getReceiveHostValue());
        }
        listAdapter.refreshData(emails);
    }

    private void initAdapter() {
        listAdapter = new EmailCategoryAdapter(this);
        binding.rv.setAdapter(listAdapter);
//        listAdapter.refreshData();
    }

    public static void start2EmailCategoryActivity(Context context) {
        context.startActivity(new Intent(context, EmailCategoryActivity.class));
    }
}
