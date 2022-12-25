package com.example.pillremind.presenter;

import com.example.pillremind.presenter.base.IChangePassword;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.mindrot.jbcrypt.BCrypt;

public class ChangePasswordPresenter implements IChangePassword.Presenter {
    private final IChangePassword.View view;

    public ChangePasswordPresenter(IChangePassword.View view) {
        this.view = view;
    }

    @Override
    public void changePassword(String oldPassword, String newPassword, String confirmPassword) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(auth.getCurrentUser().getEmail(), oldPassword)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        auth.getCurrentUser().updatePassword(confirmPassword)
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        FirebaseDatabase.getInstance().getReference(auth.getCurrentUser().getUid()).child("Users").child("password").setValue(BCrypt.hashpw(confirmPassword, BCrypt.gensalt())).addOnCompleteListener(task2 -> {
                                            if (task2.isSuccessful()) {
                                                view.showSuccessMessage("Đổi mật khẩu thành công");
                                            } else {
                                                view.showErrorMessage("Đổi mật khẩu thất bại");
                                            }
                                        });
                                    } else {
                                        view.showErrorMessage("Thay đổi mật khẩu thất bại");
                                    }
                                });
                    } else {
                        view.showErrorMessage("Mật khẩu cũ không đúng");
                    }
                });
    }
}