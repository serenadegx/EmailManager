package com.example.emailmanager.emaildetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;

import com.example.emailmanager.EMApplication;
import com.example.emailmanager.R;
import com.example.emailmanager.data.AccountDetail;
import com.example.emailmanager.data.AccountDetailDao;
import com.example.emailmanager.data.Email;
import com.example.emailmanager.data.EmailDao;
import com.example.emailmanager.data.source.EmailRepository;
import com.example.emailmanager.databinding.ActivityEmailDetailBinding;
import com.example.emailmanager.emaildetail.adapter.AccessoryListAdapter;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

public class EmailDetailActivity extends AppCompatActivity {
    public static final int REQUEST_PERMISSIONS = 1;

    private EmailDetailViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityEmailDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_email_detail);
        initToolbar(binding);
        binding.rvAccessory.setLayoutManager(new LinearLayoutManager(this));
        AccessoryListAdapter listAdapter = new AccessoryListAdapter(this);
        binding.rvAccessory.setAdapter(listAdapter);
        viewModel = new EmailDetailViewModel(this, new EmailRepository(), getIntent().getIntExtra("msgnum", 0));
        binding.setViewModel(viewModel);
        viewModel.setAdapter(listAdapter);
        viewModel.setWebView(binding.webView);
        viewModel.loadDataById();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        viewModel.handleRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void initToolbar(ActivityEmailDetailBinding binding) {
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public static void start2EmailDetailActivity(Context context, int msgNum) {
        context.startActivity(new Intent(context, EmailDetailActivity.class).putExtra("msgnum", msgNum));
    }
}
