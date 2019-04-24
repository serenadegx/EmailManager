package com.example.emailmanager.account;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.emailmanager.EMApplication;
import com.example.emailmanager.R;
import com.example.emailmanager.account.adapter.EmailCategoryAdapter;
import com.example.emailmanager.data.Email;
import com.example.emailmanager.databinding.ActivityEmailCategoryBinding;
import com.example.emailmanager.databinding.ActivityEmailDetailBinding;
import com.example.emailmanager.utils.EMDecoration;

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
        binding.rv.addItemDecoration(new EMDecoration(this, EMDecoration.VERTICAL_LIST, R.drawable.list_divider, 0));
        initToolbar(binding);
        initAdapter();
        iniData();
    }

    private void initToolbar(ActivityEmailCategoryBinding binding) {
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void iniData() {
        listAdapter.refreshData(EMApplication.getDaoSession().getEmailDao().loadAll());
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
