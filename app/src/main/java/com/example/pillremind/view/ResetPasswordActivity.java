package com.example.pillremind.view;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pillremind.R;
import com.example.pillremind.model.User;
import com.example.pillremind.presenter.base.IResetPassword;
import com.example.pillremind.presenter.ResetPasswordPresenter;

public class ResetPasswordActivity extends AppCompatActivity implements IResetPassword.View, android.view.View.OnClickListener {

    private EditText edtEmail;
    private Button btnResetPassword;
    private ImageButton btnBack;
    private TextView tvWarning;
    private ResetPasswordPresenter resetPasswordPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        initView();
        registerListener();
        initPresenter();
    }

    public void initView() {
        edtEmail = findViewById(R.id.edtUsername);
        btnResetPassword = findViewById(R.id.btnResetPassword);
        btnBack = findViewById(R.id.btnBack);
        tvWarning = findViewById(R.id.tvWarming);
    }

    public void registerListener() {
        btnResetPassword.setOnClickListener(this);
        btnBack.setOnClickListener(this);
    }

    public void initPresenter() {
        resetPasswordPresenter = new ResetPasswordPresenter(this);
    }

    @Override
    public void onClick(@NonNull View v) {
        if (v.getId() == R.id.btnResetPassword) {
            if (isNetworkConnected()) {
                resetPasswordEvent();
            } else {
                onNetworkErrorDialog();
            }
        } else if (v.getId() == R.id.btnBack) {
            finish();
        }
    }

    private void resetPasswordEvent() {
        String email = edtEmail.getText().toString();
        resetPasswordPresenter.resetPassword(new User(email, ""));
    }

    @Override
    public void onResetPasswordResult(@NonNull String message) {
        if (message.equals("Vui lòng kiểm tra email để đặt lại mật khẩu")) {
            onCreateDialog(message);
        } else {
            tvWarning.setText(message);
        }
    }

    public void onCreateDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ResetPasswordActivity.this);
        builder.setTitle("Thông báo");
        builder.setMessage(message);
        builder.setPositiveButton("Chấp nhận", (dialog, which) -> {
            Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
            startActivity(intent);
        }).setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
        builder.create();
        builder.show();
    }

    public void onNetworkErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thông báo");
        builder.setMessage("Vui lòng kiểm tra kết nối internet");
        builder.setPositiveButton("Chấp nhận", (dialog, which) -> dialog.dismiss());
        builder.create();
        builder.show();
    }

    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}