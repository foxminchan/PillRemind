package com.example.pillremind.presenter.base;

public interface IProfile {
    interface View {
        void onSetVerifyPhoneNumberIconResult(boolean b);

        void onGetCurrentUserResult(String name, String phone);
    }
    interface Presenter {
        void getCurrentUser();
        void getVerifyPhoneSatus();
        void deleteAccount();
    }
}
