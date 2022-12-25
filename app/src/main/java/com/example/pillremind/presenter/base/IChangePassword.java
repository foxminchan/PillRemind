package com.example.pillremind.presenter.base;

public interface IChangePassword {
    interface View {
        void showSuccessMessage(String message);
        void showErrorMessage(String message);
    }
    interface Presenter {
        void changePassword(String oldPassword, String newPassword, String confirmPassword);
    }
}
