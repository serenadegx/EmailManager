package com.example.emailmanager.msgsend;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.emailmanager.R;
import com.example.emailmanager.data.source.EmailRepository;
import com.example.emailmanager.databinding.ActivityMsgSendBinding;

public class SendMsgActivity extends AppCompatActivity {

    private SendMsgViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMsgSendBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_msg_send);
        viewModel = new SendMsgViewModel(this, new EmailRepository());
        binding.setViewModel(viewModel);
    }

    public static void start2SendMsgActivity(Context context) {
        context.startActivity(new Intent(context, SendMsgActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.send, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_send) {
            viewModel.sendMsg();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
