package com.example.emailmanager.emails;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.emailmanager.R;
import com.example.emailmanager.data.source.EmailDataRepository;
import com.example.emailmanager.databinding.FragmentInboxBinding;
import com.example.emailmanager.emails.adapter.EmailListAdapter;
import com.example.emailmanager.utils.EMDecoration;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

public class InboxFragment extends Fragment {
    public static final String FLAG = "flag";
    public static final String REFRESH = "refresh";
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

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentInboxBinding.inflate(inflater, container, false);
        binding.rv.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rv.addItemDecoration(new EMDecoration(getActivity(), EMDecoration.VERTICAL_LIST, R.drawable.list_divider, 0));
        viewModel = new EmailsViewModel(EmailDataRepository.provideRepository(), getContext());
//        viewModel.setRefresh(getArguments().getBoolean(REFRESH));
        binding.setViewModel(viewModel);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        switch (getArguments().getInt(FLAG)) {
            case INBOX:
                viewModel.setLoadType(INBOX);
                break;
            case SENT_MESSAGES:
                viewModel.setLoadType(SENT_MESSAGES);
                break;
            case DRAFTS:
                viewModel.setLoadType(DRAFTS);
                break;
            case DELETE:
                viewModel.setLoadType(DELETE);
                break;
            default:
                break;
        }
        viewModel.loadEmails();
    }


    private void setupListAdapter() {
        EmailListAdapter listAdapter = new EmailListAdapter(getContext());
        binding.rv.setAdapter(listAdapter);
    }

    public void setLoadType(int type) {
        switch (type) {
            case INBOX:
                viewModel.setLoadType(INBOX);
                break;
            case SENT_MESSAGES:
                viewModel.setLoadType(SENT_MESSAGES);
                break;
            case DRAFTS:
                viewModel.setLoadType(DRAFTS);
                break;
            case DELETE:
                viewModel.setLoadType(DELETE);
                break;
            default:
                break;
        }
        viewModel.loadEmails();
    }
}
