package com.example.emailmanager.emails;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.emailmanager.R;
import com.example.emailmanager.data.source.EmailRepository;
import com.example.emailmanager.databinding.FragmentInboxBinding;
import com.example.emailmanager.emails.adapter.EmailListAdapter;
import com.example.emailmanager.utils.EMDecoration;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import static java.security.AccessController.getContext;

public class InboxFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    public static final String FLAG = "flag";
    public static final int INBOX = 1;
    public static final int SENT_MESSAGES = 2;
    public static final int DRAFTS = 3;
    public static final int DELETE = 4;
    private FragmentInboxBinding binding;
    private EmailsViewModel viewModel;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupListAdapter();
        switch (getArguments().getInt(FLAG)) {
            case INBOX:
                viewModel.loadEmails();
                break;
            case SENT_MESSAGES:
                viewModel.loadEmailsFromSent();
                break;
            case DRAFTS:
                viewModel.loadEmailsFromDraft();
                break;
            case DELETE:
                viewModel.loadEmailsFromDelete();
                break;
            default:
                break;
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentInboxBinding.inflate(inflater, container, false);
        binding.rv.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rv.addItemDecoration(new EMDecoration(getActivity(), EMDecoration.VERTICAL_LIST, R.drawable.list_divider, 0));
        binding.srl.setOnRefreshListener(this);
        viewModel = new EmailsViewModel(new EmailRepository(), getContext(), binding.srl);
        binding.setViewModel(viewModel);

        return binding.getRoot();
    }

    @Override
    public void onRefresh() {
        switch (getArguments().getInt(FLAG)) {
            case INBOX:
                viewModel.loadEmails();
                break;
            case SENT_MESSAGES:
                viewModel.loadEmailsFromSent();
                break;
            case DRAFTS:
                viewModel.loadEmailsFromDraft();
                break;
            case DELETE:
                viewModel.loadEmailsFromDelete();
                break;
            default:
                break;
        }
    }

    private void setupListAdapter() {
        EmailListAdapter listAdapter = new EmailListAdapter(getContext());
        binding.rv.setAdapter(listAdapter);
        viewModel.setAdapter(listAdapter);
    }
}
