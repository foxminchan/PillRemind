package com.example.pillremind.presenter.base;

import com.example.pillremind.model.domain.Task;

import java.util.List;

public interface IPresciption {
    interface View {
        void showPillList(List<Task> tasks);
    }

    interface Presenter {
        void getPillList(String date);
    }
}
