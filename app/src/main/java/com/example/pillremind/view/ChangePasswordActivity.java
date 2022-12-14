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
            oldPassword.setError("Vui l??ng nh???p m???t kh???u c??");
            return false;
        }
        if (newPassword.getText().toString().isEmpty()) {
            newPassword.setError("Vui l??ng nh???p m???t kh???u m???i");
            return false;
        }
        if (confirmPassword.getText().toString().isEmpty()) {
            confirmPassword.setError("Vui l??ng nh???p l???i m???t kh???u m???i");
            return false;
        }
        if (!newPassword.getText().toString().equals(confirmPassword.getText().toString())) {
            confirmPassword.setError("M???t kh???u kh??ng kh???p");
            return false;
        } else if (newPassword.getText().toString().equals(oldPassword.getText().toString())) {
            newPassword.setError("M???t kh???u m???i kh??ng ???????c tr??ng m???t kh???u c??");
            return false;
        } else if (!Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$").matcher(newPassword.getText().toString()).matches()) {
            errorMessage.setText("M???t kh???u ph???i c?? ??t nh???t 8 k?? t???, ch???a ??t nh???t 1 ch??? s???, 1 ch??? hoa, 1 ch??? th?????ng v?? 1 k?? t??? ?????c bi???t");
            return false;
        }
        return true;
    }

    @Override
    public void showSuccessMessage(@NonNull String message) {
        if (message.equals("?????i m???t kh???u th??nh c??ng")) {
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