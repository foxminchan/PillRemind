package com.example.pillremind.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pillremind.R;
import com.example.pillremind.presenter.ChangePasswordPresenter;
import com.example.pillremind.presenter.base.IChangePassword;

import java.util.regex.Pattern;

public class ChangePasswordActivity extends AppCompatActivity implements IChangePassword.View, View.OnClickListener {

    private EditText oldPassword;
    private EditText newPassword;
    private EditText confirmPassword;
    private Button changePassword;
    private ImageButton back;
    private TextView errorMessage;
    private ChangePasswordPresenter changePasswordPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        initViews();
        registerListeners();
        initPresenter();
    }

    public void initViews() {
        oldPassword = findViewById(R.id.edtOldPassword);
        newPassword = findViewById(R.id.edtPassword);
        confirmPassword = findViewById(R.id.edtPasswordRepeat);
        changePassword = findViewById(R.id.btnChangePassword);
        errorMessage = findViewById(R.id.tvWarming);
        back = findViewById(R.id.btnBack);
    }

    public void registerListeners() {
        changePassword.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    public void initPresenter() {
        changePasswordPresenter = new ChangePasswordPresenter(this);
    }

    @Override
    public void onClick(@NonNull View v) {
        if (v.getId() == R.id.btnChangePassword && validate()) {
            changePasswordPresenter.changePassword(oldPassword.getText().toString(), newPassword.getText().toString(), confirmPassword.getText().toString());
        } else if (v.getId() == R.id.btnBack) {
            finish();
        }
    }

    @SuppressLint("SetTextI18n")
    private boolean validate() {
        if (oldPassword.getText().toString().isEmpty()) {
            oldPassword.setError("Vui lòng nhập mật khẩu cũ");
            return false;
        }
        if (newPassword.getText().toString().isEmpty()) {
            newPassword.setError("Vui lòng nhập mật khẩu mới");
            return false;
        }
        if (confirmPassword.getText().toString().isEmpty()) {
            confirmPassword.setError("Vui lòng nhập lại mật khẩu mới");
            return false;
        }
        if (!newPassword.getText().toString().equals(confirmPassword.getText().toString())) {
            confirmPassword.setError("Mật khẩu không khớp");
            return false;
        } else if (newPassword.getText().toString().equals(oldPassword.getText().toString())) {
            newPassword.setError("Mật khẩu mới không được trùng mật khẩu cũ");
            return false;
        } else if (!Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$").matcher(newPassword.getText().toString()).matches()) {
            errorMessage.setText("Mật khẩu phải có ít nhất 8 ký tự, chứa ít nhất 1 chữ số, 1 chữ hoa, 1 chữ thường và 1 ký tự đặc biệt");
            return false;
        }
        return true;
    }

    @Override
    public void showSuccessMessage(@NonNull String message) {
        if (message.equals("Đổi mật khẩu thành công")) {
            finish();
            clearData();
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    private void clearData() {
        oldPassword.setText("");
        newPassword.setText("");
        confirmPassword.setText("");
        errorMessage.setText("");
    }

    @Override
    public void showErrorMessage(String message) {
        errorMessage.setText(message);
    }
}