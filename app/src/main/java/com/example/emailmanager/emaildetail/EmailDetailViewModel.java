package com.example.emailmanager.emaildetail;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;

import com.example.emailmanager.EMApplication;
import com.example.emailmanager.data.AccessoryDetail;
import com.example.emailmanager.data.EmailDetail;
import com.example.emailmanager.data.source.EmailDataRepository;
import com.example.emailmanager.data.source.EmailDataSource;
import com.example.emailmanager.emails.InboxFragment;
import com.example.emailmanager.msgsend.SendMsgActivity;

import java.io.File;

public class EmailDetailViewModel implements EmailDataSource.GetEmailCallBack {
    public final ObservableField<String> title = new ObservableField<>();
    public ObservableList<AccessoryDetail> mItems = new ObservableArrayList<>();
    public final ObservableField<String> receivers = new ObservableField<>();
    public final ObservableField<String> cc = new ObservableField<>();
    public final ObservableField<String> bcc = new ObservableField<>();
    public final ObservableField<String> subject = new ObservableField<>();
    public final ObservableField<String> date = new ObservableField<>();
    public final ObservableField<String> accessory = new ObservableField<>();
    public final ObservableBoolean isAttach = new ObservableBoolean();
    public final ObservableBoolean isCc = new ObservableBoolean();
    public final ObservableBoolean isBcc = new ObservableBoolean();
    public final ObservableField<String> html = new ObservableField<>();
    public final ObservableField<String> snackBarText = new ObservableField<>();
    private final long msgNum;
    private Context mContext;
    private EmailDataRepository mEmailRepository;
    private EmailDetail emailDetail;
    private EmailDetailNavigator mNavigator;

    public EmailDetailViewModel(Context mContext, EmailDataRepository mEmailRepository, long msgnum) {
        this.mContext = mContext;
        this.mEmailRepository = mEmailRepository;
        this.msgNum = msgnum;
    }

    public void onActivityCreated(EmailDetailNavigator navigator) {
        this.mNavigator = navigator;
    }

    @Nullable
    public String getSnackBarText() {
        return snackBarText.get();
    }

    public void loadDataById(final int type) {
        new Thread() {
            @Override
            public void run() {
                if (type == InboxFragment.INBOX) {
                    mEmailRepository.getEmail(EMApplication.getAccount(), msgNum, EmailDetailViewModel.this);
                } else {
                    mEmailRepository.loadSentMessage(EMApplication.getAccount(), msgNum, EmailDetailViewModel.this);
                }
            }
        }.start();


    }

    @Override
    public void onEmailLoaded(EmailDetail detail) {
        emailDetail = detail;
        title.set(TextUtils.isEmpty(detail.getPersonal()) ? detail.getFrom() : detail.getPersonal());
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
        mItems.addAll(detail.getAccessoryList());
        html.set(detail.getContent());
    }

    @Override
    public void onDataNotAvailable() {
        snackBarText.set("加载失败");
    }

    public void reply(View v) {
        SendMsgActivity.start2SendMsgActivity(mContext, emailDetail, SendMsgActivity.REPLY);
    }

    public void replyAll(View v) {
        SendMsgActivity.start2SendMsgActivity(mContext, emailDetail, SendMsgActivity.REPLY_ALL);
    }

    public void forward(View v) {
        SendMsgActivity.start2SendMsgActivity(mContext, emailDetail, SendMsgActivity.FORWARD);
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
                        dialog.cancel();
                        realDelete();
                    }
                }).show();

    }

    private void realDelete() {
        new Thread() {
            @Override
            public void run() {
                mEmailRepository.deleteEmail(EMApplication.getAccount(), msgNum, new EmailDataSource.GetResultCallBack() {
                    @Override
                    public void onSuccess() {
                        snackBarText.set("删除成功");
                        SystemClock.sleep(500);
                        mNavigator.onDeleteSuccess();
                    }

                    @Override
                    public void onError(String ex) {
                        snackBarText.set(ex);
                    }
                });
            }
        }.start();
    }

//    void handleRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        if (checkPermission(mContext, permissions)) {
////            adapter.realDownloadOrOpen();
//        } else {
//            new AlertDialog.Builder(mContext)
//                    .setTitle("提示信息")
//                    .setMessage("缺少必要权限，会造成app部分功能无法使用。如若需要，请单击【确定】按钮前往设置中心进行权限授权。")
//                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                        }
//                    })
//                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            startAppSettings();
//                        }
//                    }).show();
//        }

//    }

}
