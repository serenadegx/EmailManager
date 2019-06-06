package com.example.emailmanager.account;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.Observable;

import com.example.emailmanager.MainActivity;
import com.example.emailmanager.R;
import com.example.emailmanager.databinding.ActivityVerifyBinding;
import com.google.android.material.snackbar.Snackbar;

public class VerifyActivity extends AppCompatActivity implements VerityNavigator {

    private VerifyModel verifyModel;
    private Observable.OnPropertyChangedCallback callback;
    private ActivityVerifyBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_verify);
        verifyModel = new VerifyModel(getIntent().getLongExtra("category", 0));
        binding.setViewModel(verifyModel);
        verifyModel.onActivityCreated(this);
        setupSnackBar();
    }

    private void setupSnackBar() {
        callback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                Snackbar.make(binding.getRoot(), verifyModel.getSnackBarText(), Snackbar.LENGTH_SHORT).show();
            }
        };
        verifyModel.snackBarText.addOnPropertyChangedCallback(callback);
    }

    @Override
    public void onAccountVerify() {
        MainActivity.start2MainActivity(this);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        verifyModel.snackBarText.removeOnPropertyChangedCallback(callback);
    }

    public static void start2VerifyActivity(Context context, long categoryId) {
        context.startActivity(new Intent(context, VerifyActivity.class)
                .putExtra("category", categoryId));
    }
}
