package com.example.pillremind.presenter.base;

import android.graphics.Bitmap;

import com.example.pillremind.model.Pill;
import com.example.pillremind.model.PillTask;
import com.example.pillremind.model.SpecificDays;
import com.example.pillremind.model.TimeDoseList;

public interface IPillAdding {
    interface View {
        void onPillAdded(String result);

        void onUrlUploaded(String url);
    }

    interface Presenter {
        void uploadImage(Bitmap bitmap);

        void addOnlyPill(String name, Pill pill);

        void addEveryDayPill(String toString, Pill pill, PillTask pillTask, TimeDoseList timeDoseList);

        void addSpecificDayPill(String toString, Pill pill, PillTask pillTask, TimeDoseList timeDoseList, SpecificDays specificDays);
    }
}
