package com.example.emailmanager.msgsend;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListPopupWindow;
import android.widget.Toast;

import com.example.emailmanager.EMApplication;
import com.example.emailmanager.data.AccessoryDetail;
import com.example.emailmanager.data.Contacts;
import com.example.emailmanager.data.EmailDetail;
import com.example.emailmanager.data.source.EmailDataRepository;
import com.example.emailmanager.data.source.EmailDataSource;
import com.example.emailmanager.databinding.ActivityMsgSendBinding;
import com.example.emailmanager.msgsend.adapter.AccessoryListAdapter;
import com.example.multifile.XRMultiFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;

import static android.app.Activity.RESULT_OK;

public class SendMsgViewModel {
    public final ObservableList<AccessoryDetail> mItems = new ObservableArrayList<>();
    public final ObservableField<String> receiver = new ObservableField<>();
    public final ObservableField<String> copy = new ObservableField<>();
    public final ObservableField<String> secret = new ObservableField<>();
    public final ObservableField<String> send = new ObservableField<>();
    public final ObservableField<String> subject = new ObservableField<>();
    public final ObservableField<String> content = new ObservableField<>();
    public final ObservableField<String> snackBarText = new ObservableField<>();
    private final Context mContext;
    private final EmailDataRepository mEmailRepository;
    private final EmailDetail mDetail;
    private final ActivityMsgSendBinding binding;
    private List<AccessoryDetail> mAccessory;
    private AccessoryListAdapter mAdapter;
    private ProgressDialog dialog;
    private SendEmailNavigator mNavigator;
//    private Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            String hint;
//            if (msg.what == SUCCESS) {
//                if (dialog != null) {
//                    dialog.cancel();
//                }
//                hint = "发送成功";
//                ((Activity) mContext).finish();
//            } else if (msg.what == SAVE_SUCCESS) {
//                hint = "保存成功";
//                ((Activity) mContext).finish();
//            } else if (msg.what == REPLY_SUCCESS) {
//                if (dialog != null) {
//                    dialog.cancel();
//                }
//                hint = "回复成功";
//                ((Activity) mContext).finish();
//            } else if (msg.what == FORWARD_SUCCESS) {
//                if (dialog != null) {
//                    dialog.cancel();
//                }
//                hint = "转发成功";
//                ((Activity) mContext).finish();
//            } else if (msg.what == ERROR) {
//                hint = (String) msg.obj;
//            } else {
//                hint = "";
//            }
//            Toast.makeText(mContext, hint, Toast.LENGTH_SHORT).show();
//        }
//    };
    private ListPopupWindow popupWindow;
    private ArrayAdapter<Contacts> adapter;

    public SendMsgViewModel(Context mContext, EmailDataRepository mEmailRepository, EmailDetail detail, ActivityMsgSendBinding binding) {
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

    public void onActivityCreated(SendEmailNavigator navigator) {
        mNavigator = navigator;
    }

    @Nullable
    public String getSnackBarText() {
        return snackBarText.get();
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
        data.setAccessoryList(mItems);
        new Thread() {
            @Override
            public void run() {
                mEmailRepository.sendEmail(EMApplication.getAccount(), data, new EmailDataSource.GetResultCallBack() {
                    @Override
                    public void onSuccess() {
                        dialog.cancel();
                        snackBarText.set("发送成功");
                        SystemClock.sleep(500);
                        mNavigator.onSendEmailSuccess();
                    }

                    @Override
                    public void onError(String ex) {
                        dialog.cancel();
                        snackBarText.set("发送失败");
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
        data.setAccessoryList(mItems);
        new Thread() {
            @Override
            public void run() {
                mEmailRepository.save2Drafts(EMApplication.getAccount(), data, new EmailDataSource.GetResultCallBack() {
                    @Override
                    public void onSuccess() {
                        snackBarText.set("保存成功");
                        SystemClock.sleep(500);
                        mNavigator.onSendEmailSuccess();
                    }

                    @Override
                    public void onError(String ex) {
                        snackBarText.set("保存失败");
                    }
                });
            }
        }.start();
    }

    public void forward() {
        dialog = ProgressDialog.show(mContext, "", "正在转发...", false, false);
        final EmailDetail data = new EmailDetail();
        data.setFrom(TextUtils.isEmpty(send.get()) ? null : send.get());
        data.setTo(TextUtils.isEmpty(receiver.get()) ? null : receiver.get());
        data.setCc(TextUtils.isEmpty(copy.get()) ? null : copy.get());
        data.setBcc(TextUtils.isEmpty(secret.get()) ? null : secret.get());
        data.setSubject(subject.get());
        data.setContent(content.get());
        data.setId(mDetail.getId());
        data.setAccessoryList(mItems);
        new Thread() {
            @Override
            public void run() {
                mEmailRepository.forward(EMApplication.getAccount(), data, new EmailDataSource.GetResultCallBack() {
                    @Override
                    public void onSuccess() {
                        dialog.cancel();
                        snackBarText.set("转发成功");
                        SystemClock.sleep(500);
                        mNavigator.onSendEmailSuccess();
                    }

                    @Override
                    public void onError(String ex) {
                        dialog.cancel();
                        snackBarText.set("转发失败");
                    }
                });
            }
        }.start();
    }

    public void reply() {
        dialog = ProgressDialog.show(mContext, "", "正在回复...", false, false);
        final EmailDetail data = new EmailDetail();
        data.setFrom(TextUtils.isEmpty(send.get()) ? null : send.get());
        data.setTo(TextUtils.isEmpty(receiver.get()) ? null : receiver.get());
        data.setCc(TextUtils.isEmpty(copy.get()) ? null : copy.get());
        data.setBcc(TextUtils.isEmpty(secret.get()) ? null : secret.get());
        data.setSubject(subject.get());
        data.setContent(content.get());
        data.setId(mDetail.getId());
        data.setAccessoryList(mItems);
        new Thread() {
            @Override
            public void run() {
                mEmailRepository.reply(EMApplication.getAccount(), data, new EmailDataSource.GetResultCallBack() {
                    @Override
                    public void onSuccess() {
                        dialog.cancel();
                        snackBarText.set("回复成功");
                        SystemClock.sleep(500);
                        mNavigator.onSendEmailSuccess();
                    }

                    @Override
                    public void onError(String ex) {
                        dialog.cancel();
                        snackBarText.set("回复失败");
                    }
                });
            }
        }.start();
    }

    public void replyAll() {
        reply();
    }

    void handleOnActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 715 && data != null) {
            ArrayList<String> list = XRMultiFile.getSelectResult(data);
            for (String str : list) {
                Log.i("mango", str);
                AccessoryDetail accessory = new AccessoryDetail();
                accessory.setPath(str);
                accessory.setFileName(str.substring(str.lastIndexOf("/") + 1));
                accessory.setSize(getPrintSize(str));
//                mAdapter.mData.add(accessory);
                mItems.add(accessory);
            }
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

    public TextWatcher watcherCopy = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            showFilterContacts(s.toString(), binding.etCopy, copy);
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
            showFilterContacts(s.toString(), binding.etSecret, secret);
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
            showFilterContacts(s.toString(), binding.etSend, send);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public void delete(AccessoryDetail item, int position) {
        Log.i("mango", "delete");
        mItems.remove(position);
    }

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
        } else if (view == popupWindow.getAnchorView()) {
            adapter.clear();
            adapter.addAll(contacts);
            adapter.notifyDataSetChanged();
            if (!popupWindow.isShowing()) {
                popupWindow.show();
            }
        } else {
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
        }

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
