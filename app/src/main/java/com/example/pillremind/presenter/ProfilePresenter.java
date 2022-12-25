package com.example.pillremind.presenter;

import com.example.pillremind.presenter.base.IProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class ProfilePresenter implements IProfile.Presenter {
    private final IProfile.View view;

    public ProfilePresenter(IProfile.View view) {
        this.view = view;
    }

    @Override
    public void getCurrentUser() {
        FirebaseDatabase.getInstance().getReference(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("User").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String name = Objects.requireNonNull(task.getResult()).child("name").getValue(String.class);
                String phone = Objects.requireNonNull(task.getResult()).child("phone").child("phonenumber").getValue(String.class);
                view.onGetCurrentUserResult(name, phone);
            } else {
                view.onGetCurrentUserResult("Lỗi", "Đã xảy ra lỗi");
            }
        });
    }

    @Override
    public void getVerifyPhoneSatus() {
        FirebaseDatabase.getInstance().getReference(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("User").child("phone").child("isVerified").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                boolean b = Boolean.TRUE.equals(Objects.requireNonNull(task.getResult()).getValue(Boolean.class));
                view.onSetVerifyPhoneNumberIconResult(b);
            } else {
                view.onSetVerifyPhoneNumberIconResult(false);
            }
        });
    }

    @Override
    public void deleteAccount() {
        FirebaseDatabase.getInstance().getReference(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).removeValue();
        FirebaseAuth.getInstance().getCurrentUser().delete();
    }

}
