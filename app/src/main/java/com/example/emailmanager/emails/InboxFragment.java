package com.example.emailmanager.emails;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.emailmanager.data.source.EmailRepository;
import com.example.emailmanager.databinding.FragmentInboxBinding;
import com.example.emailmanager.emails.adapter.EmailListAdapter;

public class InboxFragment extends Fragment {

    private FragmentInboxBinding binding;
    private EmailsViewModel viewModel;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupListAdapter();
        viewModel.loadEmails();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentInboxBinding.inflate(inflater, container, false);
        binding.rv.setLayoutManager(new LinearLayoutManager(getContext()));
        viewModel = new EmailsViewModel(new EmailRepository(), getContext());
        binding.setViewModel(viewModel);

        return binding.getRoot();
    }

    private void setupListAdapter() {
        EmailListAdapter listAdapter = new EmailListAdapter(getContext());
        binding.rv.setAdapter(listAdapter);
        binding.srl.setEnabled(false);
        viewModel.setAdapter(listAdapter);
    }
}
