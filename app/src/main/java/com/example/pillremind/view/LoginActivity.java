package com.example.pillremind.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.pillremind.presenter.base.ILogin;
import com.example.pillremind.presenter.LoginPresenter;

public class LoginActivity extends AppCompatActivity implements ILogin.View, android.view.View.OnClickListener {
    private EditText email;
    private EditText password;
    private Button login;
    private ImageButton googleLogin;
    private ImageButton facebookLogin;
    private ImageButton appleLogin;
    private TextView register;
    private TextView forgotPassword;
    private TextView warning;
    private LoginPresenter loginPresenter;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        registerListener();
        initPresenter();
        getSession();
    }

    public void getSession() {
        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        if ((sharedPreferences.contains("email") && sharedPreferences.contains("password"))) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void initView() {
        email = findViewById(R.id.edtUsername);
        password = findViewById(R.id.edtPassword);
        login = findViewById(R.id.btnLogin);
        register = findViewById(R.id.tvSignUp);
        forgotPassword = findViewById(R.id.tvForgotPassword);
        warning = findViewById(R.id.tvWarming);
        googleLogin = findViewById(R.id.btnLoginWithGoogle);
        facebookLogin = findViewById(R.id.btnLoginWithFacebook);
        appleLogin = findViewById(R.id.btnLoginWithApple);
    }

    public void registerListener() {
        login.setOnClickListener(this);
        register.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);
        googleLogin.setOnClickListener(this);
        facebookLogin.setOnClickListener(this);
        appleLogin.setOnClickListener(this);
    }

    public void initPresenter() {
        loginPresenter = new LoginPresenter(this);
    }

    @Override
    public void onClick(@NonNull View v) {
        if (v.getId() == login.getId()) {
            if (isNetworkConnected()) {
                loginEvent();
            } else {
                onNetworkErrorDialog();
            }
        } else if (v.getId() == register.getId()) {
            Intent intent = new Intent(this, RegisterAcitivity.class);
            startActivity(intent);
        } else if (v.getId() == forgotPassword.getId()) {
            Intent intent = new Intent(this, ResetPasswordActivity.class);
            startActivity(intent);
        } else if (v.getId() == googleLogin.getId()) {
            loginWithGoogle();
        } else if (v.getId() == facebookLogin.getId()) {
            loginWithFacebook();
        } else if (v.getId() == appleLogin.getId()) {
            loginWithApple();
        } else {
            Toast.makeText(this, "Lỗi", Toast.LENGTH_SHORT).show();
        }
    }

    private void loginWithApple() {
        Toast.makeText(this, "Tính năng đang phát triển", Toast.LENGTH_SHORT).show();
    }

    private void loginWithFacebook() {
        Toast.makeText(this, "Tính năng đang phát triển", Toast.LENGTH_SHORT).show();
    }

    private void loginWithGoogle() {
        Toast.makeText(this, "Tính năng đang phát triển", Toast.LENGTH_SHORT).show();
    }


    private void loginEvent() {
        String emailValue = this.email.getText().toString();
        String passwordValue = this.password.getText().toString();
        loginPresenter.performLogin(new User(emailValue, passwordValue));
    }

    @Override
    public void onLoginResult(@NonNull String message) {
        switch (message) {
            case "Đăng nhập thành công":
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                warning.setText("");
                email.setText("");
                password.setText("");
                sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("email", email.getText().toString());
                editor.putString("password", password.getText().toString());
                editor.apply();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case "Vui lòng xác thực email":
                warning.setText(message);
                onCreateDialog();
                break;
            default:
                warning.setText(message);
                password.setText("");
                break;
        }
    }


    public void onCreateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thông báo");
        builder.setMessage("Một email đã được gửi đến email của bạn. Vui lòng kiểm tra email để xác thực tài khoản. Lưu ý: Email có thể nằm trong thư rác");
        builder.setPositiveButton("Chấp nhận", (dialog, which) -> dialog.dismiss());
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