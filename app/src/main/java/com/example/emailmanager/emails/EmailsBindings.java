package com.example.emailmanager.emails;

import com.example.emailmanager.data.EmailDetail;
import com.example.emailmanager.emails.adapter.EmailListAdapter;

import java.util.List;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class EmailsBindings {
    @SuppressWarnings("unchecked")
    @BindingAdapter("android:items")
    public static void setItems(RecyclerView rv, List<EmailDetail> data){
        EmailListAdapter listAdapter = (EmailListAdapter) rv.getAdapter();
        listAdapter.refreshData(data);
    }
}
