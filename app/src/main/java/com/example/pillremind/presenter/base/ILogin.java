package com.example.pillremind.presenter.base;

import com.example.pillremind.model.User;

public interface ILogin {
    interface View {
        void onLoginResult(String message);
    }

    interface Presenter {
        void performLogin(User loginModel);
    }
}
