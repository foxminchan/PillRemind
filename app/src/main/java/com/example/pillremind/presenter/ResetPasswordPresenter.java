package com.example.pillremind.presenter;

import androidx.annotation.NonNull;

import com.example.pillremind.model.User;
import com.example.pillremind.presenter.base.IResetPassword;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class ResetPasswordPresenter implements IResetPassword.Presenter {
    IResetPassword.View view;

    public ResetPasswordPresenter(IResetPassword.View view) {
        this.view = view;
    }

    @Override
    public void resetPassword(@NonNull User resetPasswordModel) {
        if (resetPasswordModel.getEmail().isEmpty()) {
            view.onResetPasswordResult("Vui lòng nhập email");
        } else if (resetPasswordModel.isValidEmailSyntax(resetPasswordModel.getEmail())) {
            view.onResetPasswordResult("Email không hợp lệ");
        } else {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            mAuth.sendPasswordResetEmail(resetPasswordModel.getEmail()).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    view.onResetPasswordResult("Vui lòng kiểm tra email để đặt lại mật khẩu");
                    if (Objects.requireNonNull(mAuth.getCurrentUser()).isEmailVerified()) {
                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(mAuth.getCurrentUser().getUid()).child("password")
                                .setValue(resetPasswordModel.getPassword());
                    }
                } else {
                    view.onResetPasswordResult("Email không tồn tại");
                }
            });
        }
    }

}
