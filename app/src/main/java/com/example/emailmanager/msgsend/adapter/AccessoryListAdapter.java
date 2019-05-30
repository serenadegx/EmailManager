package com.example.emailmanager.msgsend.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.example.emailmanager.BR;
import com.example.emailmanager.R;
import com.example.emailmanager.data.AccessoryDetail;
import com.example.emailmanager.utils.BaseAdapter;
import com.example.emailmanager.utils.BaseViewHolder;

import java.util.List;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

public class AccessoryListAdapter extends BaseAdapter<AccessoryDetail, BaseViewHolder> {
    public AccessoryListAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder onCreateVH(ViewGroup parent, int viewType) {
        ViewDataBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.item_accessory_send, parent, false);
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

    public void delete(AccessoryDetail item, int position) {
        mData.remove(position);
        notifyDataSetChanged();
    }

    public void add(List<AccessoryDetail> data) {
        mData.addAll(data);
        notifyDataSetChanged();
    }
}
