package com.example.emailmanager.emaildetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.emailmanager.R;
import com.example.emailmanager.data.source.EmailDataRepository;
import com.example.emailmanager.data.source.EmailRepository;
import com.example.emailmanager.data.source.local.EmailLocalDataSource;
import com.example.emailmanager.data.source.remote.EmailRemoteDataSource;
import com.example.emailmanager.databinding.ActivityEmailDetailBinding;
import com.example.emailmanager.emaildetail.adapter.AccessoryListAdapter;

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
        viewModel = new EmailDetailViewModel(this, new EmailDataRepository(new EmailLocalDataSource()
                ,new EmailRemoteDataSource()), getIntent().getLongExtra("msgnum", 0));
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

    public static void start2EmailDetailActivity(Context context, Long msgNum) {
        context.startActivity(new Intent(context, EmailDetailActivity.class).putExtra("msgnum", msgNum));
    }
}
