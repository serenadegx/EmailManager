package com.example.emailmanager.account.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.example.emailmanager.BR;
import com.example.emailmanager.R;
import com.example.emailmanager.account.VerifyActivity;
import com.example.emailmanager.data.Email;
import com.example.emailmanager.utils.BaseAdapter;
import com.example.emailmanager.utils.BaseViewHolder;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

public class EmailCategoryAdapter extends BaseAdapter<Email, BaseViewHolder> {

    public EmailCategoryAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder onCreateVH(ViewGroup parent, int viewType) {
        ViewDataBinding inflate = DataBindingUtil.inflate(inflater, R.layout.item_email_catogory, parent, false);
        return new BaseViewHolder(inflate);
    }

    @Override
    public void onBindVH(BaseViewHolder baseViewHolder, int position) {
        ViewDataBinding binding = baseViewHolder.getBinding();
        binding.setVariable(BR.item, mData.get(position));
        binding.setVariable(BR.adapter, this);
        binding.setVariable(BR.position, position);
        binding.executePendingBindings(); //防止闪烁
    }

    public void goNext(Email item, int position) {
        VerifyActivity.start2VerifyActivity(mContext, item.getCategoryId());
    }
}
