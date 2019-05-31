package com.example.emailmanager.emaildetail.adapter;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.emailmanager.BR;
import com.example.emailmanager.R;
import com.example.emailmanager.data.AccessoryDetail;
import com.example.emailmanager.utils.BaseAdapter;
import com.example.emailmanager.utils.BaseViewHolder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import static com.example.emailmanager.emaildetail.EmailDetailActivity.REQUEST_PERMISSIONS;

public class AccessoryListAdapter extends BaseAdapter<AccessoryDetail, BaseViewHolder> {
    private static final int PROGRESS = 1;
    private static final int FINISH = 2;
    private static final int ERROR = 3;
    private ProgressDialog progressDialog;
    private AccessoryDetail item;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PROGRESS:
                    float percent = (float) msg.obj;
                    if (progressDialog != null) {
                        progressDialog.setProgress((int) (percent * 100));
                    }
                    break;
                case FINISH:
                    if (progressDialog != null) {
                        progressDialog.cancel();
                    }
                    if (item != null) {
                        item.setDownload(true);
                        notifyDataSetChanged();
                    }
                    break;
                case ERROR:
                    if (progressDialog != null) {
                        progressDialog.cancel();
                    }
                    Toast.makeText(mContext, "下载失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    public AccessoryListAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder onCreateVH(ViewGroup parent, int viewType) {
        ViewDataBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.item_accessory, parent, false);
        return new BaseViewHolder(dataBinding);
    }

    @Override
    public void onBindVH(BaseViewHolder baseViewHolder, int position) {
        ViewDataBinding binding = baseViewHolder.getBinding();
        binding.setVariable(BR.item, mData.get(position));
        binding.setVariable(BR.adapter, this);
        binding.setVariable(BR.position, position);
        binding.executePendingBindings(); //防止闪烁
    }

    public void downloadOrOpen(AccessoryDetail item, int position) {
        this.item = item;
        //判断是否有存储权限(6.0适配)
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            if (!item.isDownload()) {
                realDownloadOrOpen();
            } else {
                Toast.makeText(mContext, "/EmailManager/" + item.getFileName(), Toast.LENGTH_LONG).show();
            }
        } else {
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSIONS);
        }

    }

    public void realDownloadOrOpen() {
        File dir = new File(Environment.getExternalStorageDirectory(), "EmailManager");
        if (!dir.exists()) {
            dir.mkdir();
        }
        final File file = new File(dir, item.getFileName());
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("下载中...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setProgress(0);
        progressDialog.setCancelable(false);
        progressDialog.show();
        new Thread() {
            @Override
            public void run() {
                downLoad(file);
            }
        }.start();


    }

    private void downLoad(File file) {

        InputStream is = item.getIs();
        FileOutputStream fos = null;
        int len;
        long sum = 0;
        long total = item.getTotal();
        byte[] bys = new byte[1024 * 2];
        try {
            fos = new FileOutputStream(file);
            while ((len = is.read(bys)) != -1) {
                sum += len;
                fos.write(bys, 0, len);
                Message message = Message.obtain();
                message.what = PROGRESS;
                message.obj = sum * 1.0f / total;
                Log.i("mango", "percent:" + sum * 1.0f / total);
                mHandler.sendMessage(message);
            }
            fos.flush();
            mHandler.sendEmptyMessage(FINISH);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            mHandler.sendEmptyMessage(ERROR);
        } catch (IOException e) {
            e.printStackTrace();
            mHandler.sendEmptyMessage(ERROR);
        } finally {
            try {
                if (fos != null)
                    fos.close();
                if (is != null)
                    is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
