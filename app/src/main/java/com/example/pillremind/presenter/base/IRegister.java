package com.example.pillremind.presenter.base;

import com.example.pillremind.model.User;

public interface IRegister {
    interface View {
        void onRegisterResult(String message);
    }

    interface Presenter {
        void performRegister(User registerModel, String passwordConfirm);
    }
}
