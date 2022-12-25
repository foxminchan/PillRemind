package com.example.pillremind.presenter;

import androidx.annotation.NonNull;

import com.example.pillremind.model.User;
import com.example.pillremind.presenter.base.IRegister;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.mindrot.jbcrypt.BCrypt;

import java.util.Objects;

public class RegisterPresenter implements IRegister.Presenter {
    IRegister.View view;

    public RegisterPresenter(IRegister.View view) {
        this.view = view;
    }

    @Override
    public void performRegister(@NonNull User registerModel, String passwordConfirm) {
        if (registerModel.getEmail().isEmpty() || Objects.requireNonNull(registerModel.getPassword()).isEmpty() || passwordConfirm.isEmpty() || registerModel.getName().isEmpty() || registerModel.getPhone().isEmpty() || registerModel.getBirthday().isEmpty()) {
            view.onRegisterResult("Vui lòng nhập đầy đủ thông tin");
        } else if (!registerModel.getPassword().equals(passwordConfirm)) {
            view.onRegisterResult("Mật khẩu không khớp");
        } else if (registerModel.isValidEmailSyntax(registerModel.getEmail())) {
            view.onRegisterResult("Email không hợp lệ");
        } else if (registerModel.isValidPasswordSyntax(registerModel.getPassword())) {
            view.onRegisterResult("Mật khẩu phải có ít nhất 8 ký tự, bao gồm chữ hoa, chữ thường, số và ký tự đặc biệt");
        } else if (!registerModel.isValidPhoneSyntax(registerModel.getPhone())) {
            view.onRegisterResult("Số điện thoại không hợp lệ");
        } else {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(registerModel.getEmail(), registerModel.getPassword()).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    User user = new User(registerModel.getName(), registerModel.getPhone(), registerModel.getBirthday(), registerModel.getEmail(), BCrypt.hashpw(registerModel.getPassword(), BCrypt.gensalt()), "", "", "", false);
                    FirebaseDatabase.getInstance().getReference(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("User").setValue(user).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            FirebaseDatabase.getInstance().getReference(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("User").child("phone").child("isVerified").setValue(false);
                            FirebaseDatabase.getInstance().getReference(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("User").child("phone").child("phonenumber").setValue(user.getPhone()).addOnCompleteListener(task2 -> {
                                if (task2.isSuccessful()) {
                                    view.onRegisterResult("Đăng ký thành công");
                                } else {
                                    view.onRegisterResult("Đăng ký thất bại");
                                }
                            });

                        } else {
                            view.onRegisterResult("Đăng ký thất bại");
                        }
                    });
                } else {
                    view.onRegisterResult("Tài khoản đã tồn tại");
                }
            });
        }

    }
}
