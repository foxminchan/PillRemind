package com.example.pillremind.presenter.base;

import android.graphics.Bitmap;

import com.example.pillremind.model.Pill;
import com.example.pillremind.model.PillTask;
import com.example.pillremind.model.SpecificDays;
import com.example.pillremind.model.TimeDoseList;

public interface IEditPill {
    interface View {
        void setPillEdit(Pill pill, PillTask pillTask, TimeDoseList timeDoseList, SpecificDays specificDays);
        void onUrlUploaded(String url);
        void onPillEdited(String result);
    }
    interface Presenter {
        void getPillEdit(String name);

        void uploadImage(Bitmap bitmap);

        void updateOnlyPill(String pillName, Pill pill);

        void updateEveryDayPill(String pillName, Pill pill, PillTask pillTask, TimeDoseList timeDoseList);

        void updateSpecificDayPill(String pillName, Pill pill, PillTask pillTask, TimeDoseList timeDoseList, SpecificDays specificDays);
    }
}
