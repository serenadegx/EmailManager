package com.example.emailmanager.msgsend;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.emailmanager.R;
import com.example.emailmanager.data.source.EmailRepository;
import com.example.emailmanager.databinding.ActivityMsgSendBinding;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

public class SendMsgActivity extends AppCompatActivity {
    public static final int SEND = 1;
    public static final int FORWARD = 2;
    public static final int REPLY = 3;
    public static final int REPLY_ALL = 4;

    private SendMsgViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMsgSendBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_msg_send);
        viewModel = new SendMsgViewModel(this, new EmailRepository(), getIntent().getIntExtra("id", -1));
        binding.setViewModel(viewModel);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.send, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_send) {
            int flag = getIntent().getIntExtra("flag", -1);
            if (flag == SEND) {
                viewModel.sendMsg();
            } else if (flag == FORWARD) {
                viewModel.forward();
            } else if (flag == REPLY) {
                viewModel.reply();
            } else if (flag == REPLY_ALL) {
                viewModel.replyAll();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("提示信息")
                .setMessage("是否存入草稿箱")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        viewModel.saveDraft();
                    }
                }).show();

    }

    public static void start2SendMsgActivity(Context context) {
        context.startActivity(new Intent(context, SendMsgActivity.class));
    }

    public static void start2SendMsgActivity(Context context, int msgNum) {
        context.startActivity(new Intent(context, SendMsgActivity.class)
                .putExtra("id", msgNum));
    }

    public static void start2SendMsgActivity(Context context, int msgNum, int flag) {
        context.startActivity(new Intent(context, SendMsgActivity.class)
                .putExtra("id", msgNum)
                .putExtra("flag", flag));
    }
}
