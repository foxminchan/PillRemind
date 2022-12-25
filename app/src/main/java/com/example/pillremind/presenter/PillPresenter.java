package com.example.pillremind.presenter;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.pillremind.model.domain.PillItem;
import com.example.pillremind.presenter.base.IPill;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Objects;

public class PillPresenter implements IPill.Presenter {
    private final IPill.View view;

    public PillPresenter(IPill.View view) {
        this.view = view;
    }

    @Override
    public void getPillList() {
        FirebaseDatabase.getInstance().getReference(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("User").child("Pill").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<String> pillName = new java.util.ArrayList<>();
                for (com.google.firebase.database.DataSnapshot snapshot : Objects.requireNonNull(task.getResult()).getChildren()) {
                    pillName.add(snapshot.getKey());
                }
                List<PillItem> pillItems = new java.util.ArrayList<>();
                for (String name : pillName) {
                    pillItems.add(new PillItem(Objects.requireNonNull(task.getResult()).child(name).child("imageUri").getValue(String.class), name, Objects.requireNonNull(task.getResult()).child(name).child("frequency").getValue(String.class)));
                }
                Log.d("PillPresenterSize", "getPillList: " + pillItems.size());
                view.showPillList(pillItems);
            } else {
                view.showPillList(null);
            }
        });
    }

    public void deletePill(@NonNull PillItem pillItem) {
        FirebaseDatabase.getInstance().getReference(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("User").child("Pill").child(pillItem.getPillName()).removeValue();
    }
}
