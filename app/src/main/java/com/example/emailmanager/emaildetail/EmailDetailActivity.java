package com.example.emailmanager.emaildetail;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import com.example.emailmanager.R;
import com.example.emailmanager.data.source.EmailDataRepository;
import com.example.emailmanager.databinding.ActivityEmailDetailBinding;
import com.example.emailmanager.emaildetail.adapter.AccessoryListAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

public class EmailDetailActivity extends AppCompatActivity {
    public static final int REQUEST_PERMISSIONS = 1;

    private EmailDetailViewModel viewModel;
    private AccessoryListAdapter listAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityEmailDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_email_detail);
        initToolbar(binding);
        binding.rvAccessory.setLayoutManager(new LinearLayoutManager(this));
        listAdapter = new AccessoryListAdapter(this);
        binding.rvAccessory.setAdapter(listAdapter);
        viewModel = new EmailDetailViewModel(this, EmailDataRepository.provideRepository(), getIntent().getLongExtra("msgnum", 0));
        binding.setViewModel(viewModel);
        viewModel.setWebView(binding.webView);
        viewModel.loadDataById();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        viewModel.handleRequestPermissionsResult(requestCode, permissions, grantResults);
        if (checkPermission(this, permissions)) {
            listAdapter.realDownloadOrOpen();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("提示信息")
                    .setMessage("缺少必要权限，会造成app部分功能无法使用。如若需要，请单击【确定】按钮前往设置中心进行权限授权。")
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startAppSettings();
                        }
                    }).show();
        }
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

    private boolean checkPermission(Context context, String[] permissions) {
        boolean flag = true;
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(context, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                flag = !flag;
                break;
            }
        }
        return flag;
    }

    /**
     * 跳到app权限设置界面
     */
    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

    public static void start2EmailDetailActivity(Context context, Long msgNum) {
        context.startActivity(new Intent(context, EmailDetailActivity.class).putExtra("msgnum", msgNum));
    }
}
