package com.example.emailmanager.emails.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.emailmanager.BR;
import com.example.emailmanager.R;
import com.example.emailmanager.data.EmailDetail;
import com.example.emailmanager.emaildetail.EmailDetailActivity;
import com.example.emailmanager.utils.BaseAdapter;
import com.example.emailmanager.utils.BaseViewHolder;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

public class EmailListAdapter extends BaseAdapter<EmailDetail, BaseViewHolder> {

    public EmailListAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder onCreateVH(ViewGroup parent, int viewType) {
        ViewDataBinding dataBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.item_email, parent, false);
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

    public void goNext(EmailDetail item, int position) {
        item.setRead(true);
        notifyDataSetChanged();
        EmailDetailActivity.start2EmailDetailActivity(mContext, item.getId());
    }
}
