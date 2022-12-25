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
import com.example.pillremind.presenter.base.IPillAdding;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class PillAddingPresenter implements IPillAdding.Presenter {
    private final IPillAdding.View view;

    public PillAddingPresenter(IPillAdding.View view) {
        this.view = view;
    }

    @Override
    public void uploadImage(@NonNull Bitmap bitmap) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference mountainsRef = storageRef.child("images/" + userID() + "/" + getCurrentDate() + ".jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(exception -> view.onPillAdded("Tải ảnh lên Server thất bại")).addOnSuccessListener(taskSnapshot -> {
            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
            while (!urlTask.isSuccessful()) ;
            Uri downloadUrl = urlTask.getResult();
            view.onUrlUploaded(downloadUrl.toString());
        });
    }

    @Override
    public void addOnlyPill(String name, Pill pill) {
        FirebaseDatabase.getInstance().getReference(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("User").child("Pill").child(name).setValue(pill).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                view.onPillAdded("Thêm thuốc thành công");
            } else {
                view.onPillAdded("Thêm thuốc thất bại");
            }
        });
    }

    @Override
    public void addEveryDayPill(String toString, Pill pill, PillTask pillTask, TimeDoseList timeDoseList) {
        Log.d("PillAddingPresenter", "addEveryDayPill: " + toString);
        FirebaseDatabase.getInstance().getReference(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("User").child("Pill").child(toString).setValue(pill).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseDatabase.getInstance().getReference(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("User").child("Pill").child(toString).child("PillTask").setValue(pillTask).addOnCompleteListener(task2 -> {
                    if (task2.isSuccessful()) {
                        for (int i = 0; i < timeDoseList.size(); i++) {
                            FirebaseDatabase.getInstance().getReference(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("User").child("Pill").child(toString).child("PillTask").child("TimeDose").child(timeDoseList.getTimes(i)).setValue(timeDoseList.getDoses(i)).addOnCompleteListener(task3 -> {
                                if (task3.isSuccessful()) {
                                    view.onPillAdded("Thêm thuốc thành công");
                                } else {
                                    view.onPillAdded("Thêm thuốc thất bại");
                                }
                            });
                        }
                    } else {
                        view.onPillAdded("Thêm thuốc thất bại");
                    }
                });
            } else {
                view.onPillAdded("Thêm thuốc thất bại");
            }
        });
    }

    @Override
    public void addSpecificDayPill(String toString, Pill pill, PillTask pillTask, TimeDoseList timeDoseList, SpecificDays specificDays) {
        Log.d("PillAddingPresenter", "addEveryDayPill: " + toString);
        FirebaseDatabase.getInstance().getReference(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("User").child("Pill").child(toString).setValue(pill).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseDatabase.getInstance().getReference(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("User").child("Pill").child(toString).child("PillTask").setValue(pillTask).addOnCompleteListener(task2 -> {
                    if (task2.isSuccessful()) {
                        FirebaseDatabase.getInstance().getReference(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("User").child("Pill").child(toString).child("PillTask").child("SpecificDays").setValue(specificDays).addOnCompleteListener(task3 -> {
                            if (task3.isSuccessful()) {
                                for (int i = 0; i < timeDoseList.size(); i++) {
                                    FirebaseDatabase.getInstance().getReference(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("User").child("Pill").child(toString).child("PillTask").child("TimeDose").child(timeDoseList.getTimes(i)).setValue(timeDoseList.getDoses(i)).addOnCompleteListener(task4 -> {
                                        if (task4.isSuccessful()) {
                                            view.onPillAdded("Thêm thuốc thành công");
                                        } else {
                                            view.onPillAdded("Thêm thuốc thất bại");
                                        }
                                    });
                                }
                            } else {
                                view.onPillAdded("Thêm thuốc thất bại");
                            }
                        });
                    } else {
                        view.onPillAdded("Thêm thuốc thất bại");
                    }
                });
            } else {
                view.onPillAdded("Thêm thuốc thất bại");
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
}
