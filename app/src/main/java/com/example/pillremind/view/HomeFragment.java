package com.example.pillremind.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pillremind.R;
import com.example.pillremind.presenter.HomePresenter;
import com.example.pillremind.presenter.base.IHome;

public class HomeFragment extends Fragment implements View.OnClickListener, IHome.View {

    ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
        if (Boolean.TRUE.equals(isGranted)) {
            makeCall();
        } else {
            Toast.makeText(getContext(), "Quyền truy cập bị từ chối", Toast.LENGTH_SHORT).show();
        }
    });
    private TextView userName;
    private LinearLayout lnEmergency;
    private LinearLayout lnDrugStore;
    private LinearLayout lnHospital;
    private LinearLayout lnDonate;
    private HomePresenter homePresenter;
    private SharedPreferences sharedPreferences;

    public HomeFragment() {

    }

    @NonNull
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    private void makeCall() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:115"));
        try {
            startActivity(callIntent);
        } catch (SecurityException e) {
            Toast.makeText(getContext(), "Bạn không có quyền thực hiện cuộc gọi", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        registerListener();
        initPresenter();
        sharedPreferences = requireActivity().getSharedPreferences("homepage", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("name")) {
            userName.setText(sharedPreferences.getString("name", ""));
        } else {
            homePresenter.getCurrentUser();
        }
    }

    public void init(@NonNull View view) {
        userName = view.findViewById(R.id.username);
        lnEmergency = view.findViewById(R.id.lnEmergency);
        lnDrugStore = view.findViewById(R.id.lnDrugStore);
        lnHospital = view.findViewById(R.id.lnHospital);
        lnDonate = view.findViewById(R.id.lnDonation);
    }

    public void registerListener() {
        lnEmergency.setOnClickListener(this);
        lnDrugStore.setOnClickListener(this);
        lnHospital.setOnClickListener(this);
        lnDonate.setOnClickListener(this);
    }

    public void initPresenter() {
        homePresenter = new HomePresenter(this);
    }

    @Override
    public void onClick(@NonNull View v) {
        if (v.getId() == lnEmergency.getId()) {
            requestPermissionLauncher.launch(android.Manifest.permission.CALL_PHONE);
        } else if (v.getId() == lnDrugStore.getId()) {
            try {
                if (getContext().checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(getContext(), DrugStoreActivity.class);
                    startActivity(intent);
                } else {
                    requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }
            } catch (Exception e) {
                Log.e("ErrorDD", e.getMessage());
            }
        } else if (v.getId() == lnHospital.getId()) {
            try {
                if (getContext().checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(getContext(), HospitalActivity.class);
                    startActivity(intent);
                } else {
                    requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }
            } catch (Exception e) {
                Log.e("ErrorDD", e.getMessage());
            }
        } else if (v.getId() == lnDonate.getId()) {
            Toast.makeText(getContext(), "Chức năng đang được phát triển", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onGetCurrentUserResult(String name) {
        userName.setText(name);
        sharedPreferences = requireActivity().getSharedPreferences("homepage", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", name);
        editor.apply();
    }
}