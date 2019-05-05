package com.example.emailmanager.emaildetail;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import com.example.emailmanager.EMApplication;
import com.example.emailmanager.data.AccessoryDetail;
import com.example.emailmanager.data.EmailDetail;
import com.example.emailmanager.data.source.EmailDataSource;
import com.example.emailmanager.data.source.EmailRepository;
import com.example.emailmanager.emaildetail.adapter.AccessoryListAdapter;
import com.example.emailmanager.msgsend.SendMsgActivity;
import com.example.xrwebviewlibrary.XRWebView;

import java.io.File;
import java.io.FileOutputStream;

import androidx.core.content.ContextCompat;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

public class EmailDetailViewModel {
    private static final int DELETE_SUCCESS = 1;
    private static final int DELETE_ERROR = 2;
    private static final int SUCCESS = 3;
    private static final int READ_SUCCESS = 4;
    private static final int READ_ERROR = 5;
    public final ObservableField<String> receivers = new ObservableField<>();
    public final ObservableField<String> cc = new ObservableField<>();
    public final ObservableField<String> bcc = new ObservableField<>();
    public final ObservableField<String> subject = new ObservableField<>();
    public final ObservableField<String> date = new ObservableField<>();
    public final ObservableField<String> accessory = new ObservableField<>();
    public final ObservableBoolean isAttach = new ObservableBoolean();
    public final ObservableBoolean isCc = new ObservableBoolean();
    public final ObservableBoolean isBcc = new ObservableBoolean();
    private final int msgNum;
    private Context mContext;
    private EmailRepository mEmailRepository;
    private AccessoryListAdapter adapter;
    private WebView webview;
    private EmailDetail detail;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == SUCCESS) {
                detail = (EmailDetail) msg.obj;
                ((Activity) mContext).setTitle(TextUtils.isEmpty(detail.getPersonal()) ? detail.getFrom() : detail.getPersonal());
                receivers.set(detail.getTo());
                isCc.set(!TextUtils.isEmpty(detail.getCc()));
                if (!TextUtils.isEmpty(detail.getCc())) {
                    cc.set(detail.getCc());
                }
                isBcc.set(!TextUtils.isEmpty(detail.getBcc()));
                if (!TextUtils.isEmpty(detail.getBcc())) {
                    bcc.set(detail.getBcc());
                }
                subject.set(detail.getSubject());
                date.set(detail.getDate());
                isAttach.set(detail.getAccessoryList().size() > 0);
                accessory.set(detail.getAccessoryList().size() + "个附件");
                for (AccessoryDetail accessory : detail.getAccessoryList()) {
                    accessory.setDownload(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/EmailManager", accessory.getFileName()).exists());
                }
                adapter.refreshData(detail.getAccessoryList());

                XRWebView.with(webview).simple().build().loadHtml(detail.getContent(), "text/html", "utf-8");
            } else if (msg.what == DELETE_SUCCESS) {
                Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
                ((Activity) mContext).finish();
            } else if (msg.what == DELETE_ERROR) {
                Toast.makeText(mContext, (String) msg.obj, Toast.LENGTH_SHORT).show();
            }
        }
    };

    public EmailDetailViewModel(Context mContext, EmailRepository mEmailRepository, int msgnum) {
        this.mContext = mContext;
        this.mEmailRepository = mEmailRepository;
        this.msgNum = msgnum;
    }

    public void loadDataById() {
        new Thread() {
            @Override
            public void run() {
                EmailDetail emailDetail = mEmailRepository.loadRemoteDataById(EMApplication.getAccount(), msgNum);
                Message message = Message.obtain();
                message.what = SUCCESS;
                message.obj = emailDetail;
                mHandler.sendMessage(message);
//                saveHtml(emailDetail);
            }
        }.start();


    }

    void setWebView(WebView webView) {
        this.webview = webView;
    }

    public void setAdapter(AccessoryListAdapter listAdapter) {
        this.adapter = listAdapter;
    }

    private void saveHtml(EmailDetail emailDetail) {
        try {
            FileOutputStream fos = new FileOutputStream(new File(Environment.getExternalStorageDirectory()
                    .getAbsolutePath(), emailDetail.getSubject() + emailDetail.getId() + ".html"));
            fos.write(emailDetail.getContent().getBytes());
            fos.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Message message = Message.obtain();
        message.what = 1001;
        message.obj = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath(), emailDetail.getSubject() + emailDetail.getId() + ".html").getAbsolutePath();
        mHandler.sendMessage(message);
    }

    public void reply(View v) {
        SendMsgActivity.start2SendMsgActivity(mContext, detail, SendMsgActivity.REPLY);
    }

    public void replyAll(View v) {
        SendMsgActivity.start2SendMsgActivity(mContext, detail, SendMsgActivity.REPLY_ALL);
    }

    public void forward(View v) {
        SendMsgActivity.start2SendMsgActivity(mContext, detail, SendMsgActivity.FORWARD);
    }

    public void delete(View v) {
        new AlertDialog.Builder(mContext)
                .setTitle("提示信息")
                .setMessage("是否删除邮件")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        realDelete();
                    }
                }).show();

    }

    void handleRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (checkPermission(mContext, permissions)) {
            adapter.realDownloadOrOpen();
        } else {
            new AlertDialog.Builder(mContext)
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
        intent.setData(Uri.parse("package:" + mContext.getPackageName()));
        mContext.startActivity(intent);
    }

    private void realDelete() {
        new Thread() {
            @Override
            public void run() {
                mEmailRepository.deleteById(EMApplication.getAccount(), msgNum, new EmailDataSource.GetResultCallBack() {
                    @Override
                    public void onSuccess() {
                        mHandler.sendEmptyMessage(DELETE_SUCCESS);
                    }

                    @Override
                    public void onError(String ex) {
                        Message message = Message.obtain();
                        message.what = SUCCESS;
                        message.obj = ex;
                        mHandler.sendMessage(message);
                    }
                });
            }
        }.start();
    }
}
