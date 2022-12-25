package com.example.pillremind.presenter.base;

import com.example.pillremind.model.domain.PillItem;

import java.util.List;

public interface IPill {
    interface View {
        void showPillList(List<PillItem> pillItems);
    }

    interface Presenter {
        void getPillList();
    }
}
