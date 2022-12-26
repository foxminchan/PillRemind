package com.example.pillremind.presenter;

import com.example.pillremind.model.domain.Task;
import com.example.pillremind.presenter.base.IPresciption;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class PresciptionPresenter implements IPresciption.Presenter {
    private final IPresciption.View view;

    public PresciptionPresenter(IPresciption.View view) {
        this.view = view;
    }

    @Override
    public void getPillList(String date) {
        FirebaseDatabase.getInstance().getReference(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("User").child("Pill").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<String> pillName = new java.util.ArrayList<>();
                for (com.google.firebase.database.DataSnapshot snapshot : Objects.requireNonNull(task.getResult()).getChildren()) {
                    pillName.add(snapshot.getKey());
                }
                List<Task> pillItems = new java.util.ArrayList<>();
                for (String name : pillName) {
                    if (Objects.requireNonNull(task.getResult()).child(name).child("frequency").getValue(String.class).equals("Hằng ngày") && Objects.requireNonNull(task.getResult()).child(name).child("endDate").getValue(String.class).compareTo(date) >= 0) {
                        List<String> times = new java.util.ArrayList<>();
                        for (com.google.firebase.database.DataSnapshot snapshot : Objects.requireNonNull(task.getResult()).child(name).child("PillTask").child("TimeDose").getChildren()) {
                            times.add(snapshot.getKey());
                        }
                        for (String time : times) {
                            pillItems.add(new Task(time, name, Objects.requireNonNull(task.getResult()).child(name).child("description").getValue(String.class), Objects.requireNonNull(task.getResult()).child(name).child("drinkTime").getValue(String.class)));
                        }
                    } else if (Objects.requireNonNull(task.getResult()).child(name).child("frequency").getValue(String.class).equals("Ngày cụ thể") && Objects.requireNonNull(task.getResult()).child(name).child("endDate").getValue(String.class).compareTo(date) >= 0) {
                        Calendar cal = Calendar.getInstance();
                        cal.set(Integer.parseInt(date.substring(6)), Integer.parseInt(date.substring(3, 5)) - 1, Integer.parseInt(date.substring(0, 2)));
                        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
                        String dayOfWeekString = "";
                        switch (dayOfWeek) {
                            case 1:
                                dayOfWeekString = "sunday";
                                break;
                            case 2:
                                dayOfWeekString = "monday";
                                break;
                            case 3:
                                dayOfWeekString = "tuesday";
                                break;
                            case 4:
                                dayOfWeekString = "wednesday";
                                break;
                            case 5:
                                dayOfWeekString = "thursday";
                                break;
                            case 6:
                                dayOfWeekString = "friday";
                                break;
                            case 7:
                                dayOfWeekString = "saturday";
                                break;
                            default:
                                break;
                        }
                        List<String> times = new java.util.ArrayList<>();
                        List<String> dateList = new java.util.ArrayList<>();
                        for (com.google.firebase.database.DataSnapshot snapshot : Objects.requireNonNull(task.getResult()).child(name).child("PillTask").child("TimeDose").getChildren()) {
                            times.add(snapshot.getKey());
                        }
                        for (com.google.firebase.database.DataSnapshot snapshot : Objects.requireNonNull(task.getResult()).child(name).child("PillTask").child("SpecificDays").getChildren()) {
                            if (snapshot.getValue(Boolean.class).equals(true)) {
                                dateList.add(snapshot.getKey());
                            }
                        }
                        for (String dateItem : dateList) {
                            if (dateItem.equals(dayOfWeekString)) {
                                for (String time : times) {
                                    pillItems.add(new Task(time, name, Objects.requireNonNull(task.getResult()).child(name).child("description").getValue(String.class), Objects.requireNonNull(task.getResult()).child(name).child("drinkTime").getValue(String.class)));
                                }
                            }
                        }
                    } else if (Objects.requireNonNull(task.getResult()).child(name).child("frequency").getValue(String.class).equals("Khoảng thời gian cụ thể") && Objects.requireNonNull(task.getResult()).child(name).child("endDate").getValue(String.class).compareTo(date) >= 0) {
                        List<String> times = new java.util.ArrayList<>();
                        for (com.google.firebase.database.DataSnapshot snapshot : Objects.requireNonNull(task.getResult()).child(name).child("PillTask").child("TimeDose").getChildren()) {
                            times.add(snapshot.getKey());
                        }
                        int daysAway = Objects.requireNonNull(task.getResult()).child(name).child("PillTask").child("timesADay").getValue(Integer.class);
                        String startDate = Objects.requireNonNull(task.getResult()).child(name).child("startDate").getValue(String.class);
                        int startDay = Integer.parseInt(startDate.substring(0, 2));
                        int startMonth = Integer.parseInt(startDate.substring(3, 5));
                        int startYear = Integer.parseInt(startDate.substring(6));
                        int endDay = Integer.parseInt(date.substring(0, 2));
                        int endMonth = Integer.parseInt(date.substring(3, 5));
                        int endYear = Integer.parseInt(date.substring(6));
                        int days = (endYear - startYear) * 365 + (endMonth - startMonth) * 30 + (endDay - startDay);
                        if (days % daysAway == 0) {
                            for (String time : times) {
                                pillItems.add(new Task(time, name, Objects.requireNonNull(task.getResult()).child(name).child("description").getValue(String.class), Objects.requireNonNull(task.getResult()).child(name).child("drinkTime").getValue(String.class)));
                            }
                        }
                    }
                }
                view.showPillList(pillItems);
            } else {
                view.showPillList(null);
            }
        });
    }

}
