package com.example.pillremind.presenter.base;

public interface IHome {
    interface View {
        void onGetCurrentUserResult(String name);
    }

    interface Presenter {
        void getCurrentUser();
    }
}
