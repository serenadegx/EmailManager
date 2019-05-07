package com.example.emailmanager.msgsend;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListPopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emailmanager.EMApplication;
import com.example.emailmanager.data.AccessoryDetail;
import com.example.emailmanager.data.Contacts;
import com.example.emailmanager.data.EmailDetail;
import com.example.emailmanager.data.source.EmailDataSource;
import com.example.emailmanager.data.source.EmailRepository;
import com.example.emailmanager.databinding.ActivityMsgSendBinding;
import com.example.emailmanager.msgsend.adapter.AccessoryListAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.activation.DataHandler;
import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.util.ByteArrayDataSource;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ObservableField;

public class SendMsgViewModel {
    private static final int SUCCESS = 1;
    private static final int ERROR = 2;
    private static final int SAVE_SUCCESS = 3;
    private static final int SAVE_ERROR = 4;
    private static final int FORWARD_SUCCESS = 5;
    private static final int FORWARD_ERROR = 6;
    private static final int REPLY_SUCCESS = 7;
    private static final int REPLY_ERROR = 8;

    public final ObservableField<String> receiver = new ObservableField<>();
    public final ObservableField<String> copy = new ObservableField<>();
    public final ObservableField<String> secret = new ObservableField<>();
    public final ObservableField<String> send = new ObservableField<>();
    public final ObservableField<String> subject = new ObservableField<>();
    public final ObservableField<String> content = new ObservableField<>();
    private final Context mContext;
    private final EmailRepository mEmailRepository;
    private final EmailDetail mDetail;
    private final ActivityMsgSendBinding binding;
    private List<AccessoryDetail> mAccessory;
    private AccessoryListAdapter mAdapter;
    private ProgressDialog dialog;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String hint;
            if (msg.what == SUCCESS) {
                if (dialog != null) {
                    dialog.cancel();
                }
                hint = "发送成功";
                ((Activity) mContext).finish();
            } else if (msg.what == ERROR) {
                hint = (String) msg.obj;
            } else if (msg.what == SAVE_SUCCESS) {
                hint = "保存成功";
                ((Activity) mContext).finish();
            } else if (msg.what == ERROR) {
                hint = (String) msg.obj;
            } else if (msg.what == FORWARD_SUCCESS) {
                if (dialog != null) {
                    dialog.cancel();
                }
                hint = "转发成功";
                ((Activity) mContext).finish();
            } else if (msg.what == FORWARD_ERROR) {
                hint = (String) msg.obj;
            } else {
                hint = "";
            }
            Toast.makeText(mContext, hint, Toast.LENGTH_SHORT).show();
        }
    };
    private ListPopupWindow popupWindow;
    private ArrayAdapter<Contacts> adapter;

    public SendMsgViewModel(Context mContext, EmailRepository mEmailRepository, EmailDetail detail, ActivityMsgSendBinding binding) {
        this.binding = binding;
        this.mContext = mContext;
        this.mEmailRepository = mEmailRepository;
        this.mDetail = detail;
        mAccessory = new ArrayList<>();
        send.set(EMApplication.getAccount().getAccount());
        int flag = ((Activity) mContext).getIntent().getIntExtra("flag", -1);
        if (flag == SendMsgActivity.SEND) {
            subject.set("");
        } else if (flag == SendMsgActivity.FORWARD) {
            subject.set("转发:" + detail.getSubject());
        } else if (flag == SendMsgActivity.REPLY) {
            receiver.set(detail.getFrom());
            subject.set("回复:" + detail.getSubject());
        } else if (flag == SendMsgActivity.REPLY_ALL) {
            receiver.set(detail.getFrom());
            subject.set("回复:" + detail.getSubject());
        }

    }


    public void sendMsg() {
        dialog = ProgressDialog.show(mContext, "", "正在发送...", false, false);
        final EmailDetail data = new EmailDetail();
        data.setFrom(TextUtils.isEmpty(send.get()) ? null : send.get());
        data.setTo(TextUtils.isEmpty(receiver.get()) ? null : receiver.get());
        data.setCc(TextUtils.isEmpty(copy.get()) ? null : copy.get());
        data.setBcc(TextUtils.isEmpty(secret.get()) ? null : secret.get());
        data.setSubject(subject.get());
        data.setContent(content.get());
        data.setAccessoryList(mAccessory);
        new Thread() {
            @Override
            public void run() {
                mEmailRepository.sendMsg(EMApplication.getAccount(), data, new EmailDataSource.GetResultCallBack() {
                    @Override
                    public void onSuccess() {
                        mHandler.sendEmptyMessage(SUCCESS);
                    }

                    @Override
                    public void onError(String ex) {
                        Message message = Message.obtain();
                        message.what = ERROR;
                        message.obj = ex;
                        mHandler.sendMessage(message);
                    }
                });
            }
        }.start();

    }

    public void saveDraft() {
        final EmailDetail data = new EmailDetail();
        data.setFrom(TextUtils.isEmpty(send.get()) ? null : send.get());
        data.setTo(TextUtils.isEmpty(receiver.get()) ? null : receiver.get());
        data.setCc(TextUtils.isEmpty(copy.get()) ? null : copy.get());
        data.setBcc(TextUtils.isEmpty(secret.get()) ? null : secret.get());
        data.setSubject(subject.get());
        data.setContent(content.get());
        List<AccessoryDetail> attach = new ArrayList<>();
        attach.add(new AccessoryDetail(Environment.getExternalStorageDirectory() + "/example.jpeg"));
        data.setAccessoryList(attach);
        new Thread() {
            @Override
            public void run() {
                mEmailRepository.save2Draft(EMApplication.getAccount(), data, new EmailDataSource.GetResultCallBack() {
                    @Override
                    public void onSuccess() {
                        mHandler.sendEmptyMessage(SAVE_SUCCESS);
                    }

                    @Override
                    public void onError(String ex) {
                        Message message = Message.obtain();
                        message.what = SAVE_ERROR;
                        message.obj = ex;
                        mHandler.sendMessage(message);
                    }
                });
            }
        }.start();
    }

    public void forward() {
        final EmailDetail data = new EmailDetail();
        data.setFrom(TextUtils.isEmpty(send.get()) ? null : send.get());
        data.setTo(TextUtils.isEmpty(receiver.get()) ? null : receiver.get());
        data.setCc(TextUtils.isEmpty(copy.get()) ? null : copy.get());
        data.setBcc(TextUtils.isEmpty(secret.get()) ? null : secret.get());
        data.setSubject(subject.get());
        data.setContent(content.get());
        List<AccessoryDetail> attach = new ArrayList<>();
        attach.add(new AccessoryDetail(Environment.getExternalStorageDirectory() + "/example.jpeg"));
        data.setAccessoryList(attach);
        new Thread() {
            @Override
            public void run() {
                mEmailRepository.forward(EMApplication.getAccount(), mDetail.getId(), data, new EmailDataSource.GetResultCallBack() {
                    @Override
                    public void onSuccess() {
                        mHandler.sendEmptyMessage(FORWARD_SUCCESS);
                    }

                    @Override
                    public void onError(String ex) {
                        Message message = Message.obtain();
                        message.what = FORWARD_ERROR;
                        message.obj = ex;
                        mHandler.sendMessage(message);
                    }
                });
            }
        }.start();
    }

    public void reply() {
        final EmailDetail data = new EmailDetail();
        data.setFrom(TextUtils.isEmpty(send.get()) ? null : send.get());
        data.setTo(TextUtils.isEmpty(receiver.get()) ? null : receiver.get());
        data.setCc(TextUtils.isEmpty(copy.get()) ? null : copy.get());
        data.setBcc(TextUtils.isEmpty(secret.get()) ? null : secret.get());
        data.setSubject(subject.get());
        data.setContent(content.get());
        List<AccessoryDetail> attach = new ArrayList<>();
        attach.add(new AccessoryDetail(Environment.getExternalStorageDirectory() + "/example.jpeg"));
        data.setAccessoryList(attach);
        new Thread() {
            @Override
            public void run() {
                mEmailRepository.reply(EMApplication.getAccount(), mDetail.getId(), data, new EmailDataSource.GetResultCallBack() {
                    @Override
                    public void onSuccess() {
                        mHandler.sendEmptyMessage(REPLY_SUCCESS);
                    }

                    @Override
                    public void onError(String ex) {
                        Message message = Message.obtain();
                        message.what = REPLY_ERROR;
                        message.obj = ex;
                        mHandler.sendMessage(message);
                    }
                });
            }
        }.start();
    }

    public void replyAll() {
        reply();
    }

    void handleOnActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            Uri uri = data.getData();
            String path = getPath(mContext, uri);
            Log.i("mango", "path:" + path);
            mAccessory.add(new AccessoryDetail(getFileName(path), path, getPrintSize(path)));
            mAdapter.refreshData(mAccessory);
        }
    }

    public TextWatcher watcherReceiver = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            showFilterContacts(s.toString(), binding.etReceiver, receiver);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void showFilterContacts(String str, View view, final ObservableField<String> ob) {

        final List<Contacts> contacts = new ArrayList<>();
        for (Contacts contact :
                EMApplication.getContacts()) {
            if (contact.getAddress().contains(str) || contact.getPersonal().contains(str)) {
                contacts.add(contact);
                Log.i("mango", "contact:" + contact.getPersonal());
            }
        }
        if (popupWindow == null) {
            popupWindow = new ListPopupWindow(mContext);
            adapter = new ArrayAdapter<>(mContext,
                    android.R.layout.simple_list_item_1, contacts);
            popupWindow.setAdapter(adapter);
            popupWindow.setModal(false);
            popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ob.set(contacts.get(position).getAddress());
                    popupWindow.dismiss();
                }
            });
            popupWindow.setAnchorView(view);
            popupWindow.show();
        } else {
            adapter.clear();
            adapter.addAll(contacts);
            adapter.notifyDataSetChanged();
            if (!popupWindow.isShowing()){
                popupWindow.show();
            }
        }

    }

    public TextWatcher watcherCopy = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    public TextWatcher watcherSecret = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    public TextWatcher watcherSend = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public void setAdapter(AccessoryListAdapter listAdapter) {
        this.mAdapter = listAdapter;
        mAdapter.refreshData(mAccessory);
    }

    public static String getPath(Context context, Uri uri) {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null)
                    cursor.close();
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static String getFileName(String path) {
        return path.substring(path.lastIndexOf("/") + 1);
    }

    public static long getSize(String path) {
        long size = 0;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(new File(path));
            size = fis.getChannel().size();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null)
                    fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return size;
    }

    public static String getPrintSize(String path) {
        long size = getSize(path);
        //如果字节数少于1024，则直接以B为单位，否则先除于1024，后3位因太少无意义
        if (size < 1024) {
            return String.valueOf(size) + " B";
        } else {
            size = size / 1024;
        }
        //如果原字节数除于1024之后，少于1024，则可以直接以KB作为单位
        //因为还没有到达要使用另一个单位的时候
        //接下去以此类推
        if (size < 1024) {
            return String.valueOf(size) + " KB";
        } else {
            size = size / 1024;
        }
        if (size < 1024) {
            //因为如果以MB为单位的话，要保留最后1位小数，
            //因此，把此数乘以100之后再取余
            size = size * 100;
            return String.valueOf((size / 100)) + "."
                    + String.valueOf((size * 100 / 1024 % 100)) + "MB";
        } else {
            //否则如果要以GB为单位的，先除于1024再作同样的处理
            size = size * 100 / 1024;
            return String.valueOf((size / 100)) + "."
                    + String.valueOf((size % 100)) + " GB";
        }
    }

    public void addReceiver(View view) {
    }

    public void addCopy(View view) {
    }

    public void addSecret(View view) {
    }
}
