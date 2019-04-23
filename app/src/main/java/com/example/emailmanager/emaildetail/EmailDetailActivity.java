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
import com.example.emailmanager.data.EmailDao;
import com.example.emailmanager.data.source.EmailRepository;
import com.example.emailmanager.databinding.ActivityEmailDetailBinding;
import com.example.emailmanager.emaildetail.adapter.AccessoryListAdapter;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

public class EmailDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        List<AccountDetail> accountDetails = EMApplication.getDaoSession().getAccountDetailDao().queryDeep("where T0." + EmailDao.Properties.CategoryId.columnName + "=?", new String[]{"1"});
        if (accountDetails != null && accountDetails.size() > 0) {
            AccountDetail accountDetail = accountDetails.get(0);
            Log.i("mango", "account:" + accountDetail.getAccount() + "  ReceiveProtocol:"
                    + accountDetail.getEmail().getReceiveProtocol() + "  ReceiveHost:" + accountDetail.getEmail().getReceiveHostValue());
            Log.i("mango", "account:" + accountDetail.getAccount() + "  ReceiveProtocol:"
                    + accountDetail.getEmail().getReceiveProtocol() + "  ReceiveHost:" + accountDetail.getEmail().getReceiveHostValue());
        }
        ActivityEmailDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_email_detail);
        initToolbar(binding);
        binding.rvAccessory.setLayoutManager(new LinearLayoutManager(this));
        AccessoryListAdapter listAdapter = new AccessoryListAdapter(this);
        binding.rvAccessory.setAdapter(listAdapter);
        EmailDetailViewModel viewModel = new EmailDetailViewModel(this, new EmailRepository(), getIntent().getIntExtra("msgnum", 0));
        viewModel.setAdapter(listAdapter);
        WebView webView = new WebView(this);
        binding.flCon.addView(webView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));
        binding.setViewModel(viewModel);
        viewModel.setWebView(webView);
        viewModel.loadDataById();
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
