package com.example.pillremind.presenter.base;

import com.example.pillremind.model.User;

public interface IResetPassword {
    interface View {
        void onResetPasswordResult(String message);
    }
    interface Presenter {
        void resetPassword(User resetPasswordModel);
    }
}
