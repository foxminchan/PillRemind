package com.example.pillremind.presenter;

import com.example.pillremind.model.User;
import com.example.pillremind.presenter.base.IHome;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class HomePresenter implements IHome.Presenter {
    private final IHome.View view;

    public HomePresenter(IHome.View view) {
        this.view = view;
    }

    public String getUserID() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return Objects.requireNonNull(user).getUid();
    }

    @Override
    public void getCurrentUser() {
        FirebaseDatabase.getInstance().getReference(getUserID()).child("User").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                User user = task.getResult().getValue(User.class);
                view.onGetCurrentUserResult(Objects.requireNonNull(user).getName());
            }
        });
    }
}