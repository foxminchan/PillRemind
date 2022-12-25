package com.example.pillremind.presenter;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.pillremind.model.Pill;
import com.example.pillremind.model.PillTask;
import com.example.pillremind.model.SpecificDays;
import com.example.pillremind.model.TimeDoseList;
import com.example.pillremind.model.domain.TimeDose;
import com.example.pillremind.presenter.base.IEditPill;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PillEditPresenter implements IEditPill.Presenter {

    private final IEditPill.View view;

    public PillEditPresenter(IEditPill.View view) {
        this.view = view;
    }

    @Override
    public void getPillEdit(String name) {
        FirebaseDatabase.getInstance().getReference(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("User").child("Pill").child(name).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String frequency = Objects.requireNonNull(task.getResult()).child("frequency").getValue(String.class);
                String description = Objects.requireNonNull(task.getResult()).child("description").getValue(String.class);
                String startDate = Objects.requireNonNull(task.getResult()).child("startDate").getValue(String.class);
                String endDate = Objects.requireNonNull(task.getResult()).child("endDate").getValue(String.class);
                String drinkTime = Objects.requireNonNull(task.getResult()).child("drinkTime").getValue(String.class);
                String imageUri = Objects.requireNonNull(task.getResult()).child("imageUri").getValue(String.class);
                int amount = Objects.requireNonNull(task.getResult()).child("amount").getValue(Integer.class);
                String unit = Objects.requireNonNull(task.getResult()).child("unit").getValue(String.class);
                if (Objects.equals(frequency, "Khi cần thiết")) {
                    view.setPillEdit(new Pill(description, startDate, endDate, drinkTime, imageUri, amount, unit, frequency), new PillTask(), new TimeDoseList(), new SpecificDays());
                } else if (Objects.equals(frequency, "Hằng ngày")) {
                    List<TimeDose> list = new ArrayList<>();
                    FirebaseDatabase.getInstance().getReference(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("User").child("Pill").child(name).child("PillTask").child("TimeDose").get().addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            List<String> time = new ArrayList<>();
                            for (DataSnapshot dataSnapshot : Objects.requireNonNull(task1.getResult()).getChildren()) {
                                time.add(dataSnapshot.getKey());
                            }
                            for (String s : time) {
                                int dose = Objects.requireNonNull(task1.getResult()).child(s).getValue(Integer.class);
                                list.add(new TimeDose(s, dose));
                            }
                            int timesADayTaken = Objects.requireNonNull(task.getResult()).child("PillTask").child("timesADayTaken").getValue(Integer.class);
                            view.setPillEdit(new Pill(description, startDate, endDate, drinkTime, imageUri, amount, unit, frequency), new PillTask(timesADayTaken, 0), new TimeDoseList(list), null);
                        }
                    });

                } else if (Objects.equals(frequency, "Ngày cụ thể")) {
                    List<TimeDose> list = new ArrayList<>();
                    FirebaseDatabase.getInstance().getReference(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("User").child("Pill").child(name).child("PillTask").child("TimeDose").get().addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            List<String> time = new ArrayList<>();
                            for (DataSnapshot dataSnapshot : Objects.requireNonNull(task1.getResult()).getChildren()) {
                                time.add(dataSnapshot.getKey());
                            }
                            for (String s : time) {
                                int dose = Objects.requireNonNull(task1.getResult()).child(s).getValue(Integer.class);
                                list.add(new TimeDose(s, dose));
                            }
                            int timesADayTaken = Objects.requireNonNull(task.getResult()).child("PillTask").child("timesADayTaken").getValue(Integer.class);
                            boolean monday = Objects.requireNonNull(task.getResult()).child("PillTask").child("SpecificDays").child("monday").getValue(Boolean.class);
                            boolean tuesday = Objects.requireNonNull(task.getResult()).child("PillTask").child("SpecificDays").child("tuesday").getValue(Boolean.class);
                            boolean wednesday = Objects.requireNonNull(task.getResult()).child("PillTask").child("SpecificDays").child("wednesday").getValue(Boolean.class);
                            boolean thursday = Objects.requireNonNull(task.getResult()).child("PillTask").child("SpecificDays").child("thursday").getValue(Boolean.class);
                            boolean friday = Objects.requireNonNull(task.getResult()).child("PillTask").child("SpecificDays").child("friday").getValue(Boolean.class);
                            boolean saturday = Objects.requireNonNull(task.getResult()).child("PillTask").child("SpecificDays").child("saturday").getValue(Boolean.class);
                            boolean sunday = Objects.requireNonNull(task.getResult()).child("PillTask").child("SpecificDays").child("sunday").getValue(Boolean.class);
                            view.setPillEdit(new Pill(description, startDate, endDate, drinkTime, imageUri, amount, unit, frequency), new PillTask(timesADayTaken, 0), new TimeDoseList(getListTimeDose(name)), new SpecificDays(monday, tuesday, wednesday, thursday, friday, saturday, sunday));
                        }
                    });
                } else if (Objects.equals(frequency, "Khoảng thời gian cụ thể")) {
                    List<TimeDose> list = new ArrayList<>();
                    FirebaseDatabase.getInstance().getReference(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("User").child("Pill").child(name).child("PillTask").child("TimeDose").get().addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            List<String> time = new ArrayList<>();
                            for (DataSnapshot dataSnapshot : Objects.requireNonNull(task1.getResult()).getChildren()) {
                                time.add(dataSnapshot.getKey());
                            }
                            for (String s : time) {
                                int dose = Objects.requireNonNull(task1.getResult()).child(s).getValue(Integer.class);
                                list.add(new TimeDose(s, dose));
                            }
                            int timesADayTaken = Objects.requireNonNull(task.getResult()).child("PillTask").child("timesADayTaken").getValue(Integer.class);
                            int timesADay = Objects.requireNonNull(task.getResult()).child("PillTask").child("timesADay").getValue(Integer.class);
                            view.setPillEdit(new Pill(description, startDate, endDate, drinkTime, imageUri, amount, unit, frequency), new PillTask(timesADayTaken, timesADay), new TimeDoseList(getListTimeDose(name)), null);
                        }
                    });
                }
            } else {
                view.setPillEdit(null, null, null, null);
            }
        });
    }

    @Override
    public void uploadImage(@NonNull Bitmap bitmap) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference mountainsRef = storageRef.child("images/" + userID() + "/" + getCurrentDate() + ".jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(exception -> view.onPillEdited("Tải ảnh lên Server thất bại")).addOnSuccessListener(taskSnapshot -> {
            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
            while (!urlTask.isSuccessful()) ;
            Uri downloadUrl = urlTask.getResult();
            view.onUrlUploaded(downloadUrl.toString());
        });
    }

    @Override
    public void updateOnlyPill(String pillName, Pill pill) {
        FirebaseDatabase.getInstance().getReference(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("User").child("Pill").child(pillName).setValue(pill).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                view.onPillEdited("Cập nhật thành công");
            } else {
                view.onPillEdited("Cập nhật thất bại");
            }
        });
    }

    @Override
    public void updateEveryDayPill(String pillName, Pill pill, PillTask pillTask, TimeDoseList timeDoseList) {
        Log.d("PillAddingPresenter", "addEveryDayPill: " + pillName);
        FirebaseDatabase.getInstance().getReference(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("User").child("Pill").child(pillName).setValue(pill).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseDatabase.getInstance().getReference(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("User").child("Pill").child(pillName).child("PillTask").setValue(pillTask).addOnCompleteListener(task2 -> {
                    if (task2.isSuccessful()) {
                        for (int i = 0; i < timeDoseList.size(); i++) {
                            FirebaseDatabase.getInstance().getReference(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("User").child("Pill").child(pillName).child("PillTask").child("TimeDose").child(timeDoseList.getTimes(i)).setValue(timeDoseList.getDoses(i)).addOnCompleteListener(task3 -> {
                                if (task3.isSuccessful()) {
                                    view.onPillEdited("Cập nhật thành công");
                                } else {
                                    view.onPillEdited("Cập nhật thất bại");
                                }
                            });
                        }
                    } else {
                        view.onPillEdited("Cập nhật thất bại");
                    }
                });
            } else {
                view.onPillEdited("Cập nhật thất bại");
            }
        });
    }

    @Override
    public void updateSpecificDayPill(String pillName, Pill pill, PillTask pillTask, TimeDoseList timeDoseList, SpecificDays specificDays) {
        Log.d("PillAddingPresenter", "addEveryDayPill: " + pillName);
        FirebaseDatabase.getInstance().getReference(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("User").child("Pill").child(pillName).setValue(pill).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseDatabase.getInstance().getReference(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("User").child("Pill").child(pillName).child("PillTask").setValue(pillTask).addOnCompleteListener(task2 -> {
                    if (task2.isSuccessful()) {
                        FirebaseDatabase.getInstance().getReference(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("User").child("Pill").child(pillName).child("PillTask").child("SpecificDays").setValue(specificDays).addOnCompleteListener(task3 -> {
                            if (task3.isSuccessful()) {
                                for (int i = 0; i < timeDoseList.size(); i++) {
                                    FirebaseDatabase.getInstance().getReference(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("User").child("Pill").child(pillName).child("PillTask").child("TimeDose").child(timeDoseList.getTimes(i)).setValue(timeDoseList.getDoses(i)).addOnCompleteListener(task4 -> {
                                        if (task4.isSuccessful()) {
                                            view.onPillEdited("Cập nhật thành công");
                                        } else {
                                            view.onPillEdited("Cập nhật thất bại");
                                        }
                                    });
                                }
                            } else {
                                view.onPillEdited("Cập nhật thất bại");
                            }
                        });
                    } else {
                        view.onPillEdited("Cập nhật thất bại");
                    }
                });
            } else {
                view.onPillEdited("Cập nhật thất bại");
            }
        });
    }

    private String getCurrentDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    @Nullable
    private String userID() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            return user.getUid();
        }
        return null;
    }

    private List<TimeDose> getListTimeDose(String name) {
        List<TimeDose> list = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            String time = String.valueOf(i);
            if (i < 10) {
                time = "0" + i;
            }
            list.add(new TimeDose(name + "_" + time, 0));
        }
        return list;
    }
}
