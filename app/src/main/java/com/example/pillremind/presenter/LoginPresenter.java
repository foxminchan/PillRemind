package com.example.pillremind.presenter;

import androidx.annotation.NonNull;

import com.example.pillremind.model.User;
import com.example.pillremind.presenter.base.ILogin;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LoginPresenter implements ILogin.Presenter {
    ILogin.View view;

    public LoginPresenter(ILogin.View view) {
        this.view = view;
    }

    @Override
    public void performLogin(@NonNull User loginModel) {
        if (loginModel.getEmail().isEmpty() || Objects.requireNonNull(loginModel.getPassword()).isEmpty()) {
            view.onLoginResult("Vui lòng nhập đầy đủ thông tin");
        } else if (loginModel.isValidEmailSyntax(loginModel.getEmail()) || loginModel.isValidPasswordSyntax(loginModel.getPassword())) {
            view.onLoginResult("Thông tin đăng nhập không hợp lệ");
        } else {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            mAuth.signInWithEmailAndPassword(loginModel.getEmail(), loginModel.getPassword()).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user != null) {
                        if (user.isEmailVerified()) {
                            view.onLoginResult("Đăng nhập thành công");
                        } else {
                            user.sendEmailVerification();
                            view.onLoginResult("Vui lòng xác thực email");
                        }
                    }
                } else if (Objects.equals(Objects.requireNonNull(task.getException()).getMessage(), "The user account has been disabled by an administrator.")) {
                    view.onLoginResult("Tài khoản đã bị vô hiệu hóa");
                } else {
                    view.onLoginResult("Tài khoản không tồn tại");
                }
            });
        }
    }

}


