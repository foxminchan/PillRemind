package com.example.pillremind.view;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pillremind.R;
import com.example.pillremind.model.User;
import com.example.pillremind.presenter.base.IRegister;
import com.example.pillremind.presenter.RegisterPresenter;

public class RegisterAcitivity extends AppCompatActivity implements IRegister.View, android.view.View.OnClickListener {

    private EditText edtName;
    private EditText edtPhone;
    private EditText edtBirthday;
    private EditText edtEmail;
    private EditText edtPassword;
    private EditText edtConfirmPassword;
    private Button btnRegister;
    private ImageButton btnBack;
    private TextView tvWarning;
    private RegisterPresenter registerPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        registerListener();
        initPresenter();
    }

    public void initView() {
        edtName = findViewById(R.id.edtFullname);
        edtBirthday = findViewById(R.id.edtBirthday);
        edtPhone = findViewById(R.id.edtPhone);
        edtEmail = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtPasswordRepeat);
        btnRegister = findViewById(R.id.btnRegister);
        btnBack = findViewById(R.id.btnBack);
        tvWarning = findViewById(R.id.tvWarming);
    }

    public void registerListener() {
        btnRegister.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        edtBirthday.setOnClickListener(this);
    }

    public void initPresenter() {
        registerPresenter = new RegisterPresenter(this);
    }

    @Override
    public void onClick(@NonNull View v) {
        if (v.getId() == btnRegister.getId()) {
            if (isNetworkConnected()){
                registerEvent();
            }else {
                onNetworkErrorDialog();
            }
        } else if (v.getId() == btnBack.getId()) {
            Intent intent = new Intent(RegisterAcitivity.this, LoginActivity.class);
            startActivity(intent);
        } else if (v.getId() == edtBirthday.getId()) {
            showDatePickerDialog();
        }
    }

    private void registerEvent() {
        String name = edtName.getText().toString();
        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();
        String confirmPassword = edtConfirmPassword.getText().toString();
        String birthday = edtBirthday.getText().toString();
        String phone = edtPhone.getText().toString();
        registerPresenter.performRegister(new User(name, phone, birthday, email, password, "", "", "", false), confirmPassword);
    }

    private void showDatePickerDialog() {
        @SuppressLint("SetTextI18n") DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> edtBirthday.setText(dayOfMonth + "/" + month + "/" + year), 1999, 1, 1);
        datePickerDialog.show();
    }
    @Override
    public void onRegisterResult(@NonNull String message) {
        if (message.equals("Đăng ký thành công")) {
            setNull();
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(RegisterAcitivity.this, LoginActivity.class);
            startActivity(intent);
        } else {
            tvWarning.setText(message);
        }
    }

    private void setNull() {
        edtName.setText("");
        edtBirthday.setText("");
        edtPhone.setText("");
        edtEmail.setText("");
        edtPassword.setText("");
        edtConfirmPassword.setText("");
        tvWarning.setText("");
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