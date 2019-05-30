package com.example.emailmanager;

import com.example.emailmanager.emails.EmailsViewModel;

import androidx.databinding.BindingAdapter;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class SwipeRefreshLayoutBindings {
    @BindingAdapter("android:onRefresh")
    public static void setOnRefreshListener(SwipeRefreshLayout swipeRefreshLayout, final EmailsViewModel viewModel){
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.refresh();
            }
        });
    }
}
