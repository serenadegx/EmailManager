package com.example.emailmanager;

import com.example.emailmanager.data.AccessoryDetail;
import com.example.emailmanager.utils.BaseAdapter;

import java.util.List;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class AccessoriesBindings {
    @BindingAdapter("android:items")
    public static void setItems(RecyclerView recyclerView, List<AccessoryDetail> data) {
        ((BaseAdapter) recyclerView.getAdapter()).refreshData(data);
    }
}
