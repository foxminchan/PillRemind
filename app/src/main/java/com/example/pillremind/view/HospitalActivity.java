package com.example.pillremind.view;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.example.pillremind.R;
import com.example.pillremind.databinding.ActivityHospitalBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class HospitalActivity extends FragmentActivity implements OnMapReadyCallback {

    public static GoogleMap mMap;
    private ActivityHospitalBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHospitalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.addMarker(new MarkerOptions().position(new LatLng(10.758229988506203, 106.6593778355804)).title("Bệnh Viện Chợ Rẫy"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(10.75545235879995, 106.66462855595339)).title("Bệnh viện Đại học Y Dược TP.HCM"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(10.756019224089927, 106.66194549576313)).title("Bệnh viện Hùng Vương"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(10.774703000612384, 106.68137583976839)).title("Bệnh viện Bình Dân"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(10.785557592086874, 106.68388579949806)).title("Bệnh Viện Quận 3"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(10.790448739536247, 106.68969949220784)).title("Bệnh viện Quận 1"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(10.777157661138856, 106.68652808423151)).title("Bệnh viện Da Liễu TP.HCM"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(10.785795297833062, 106.68836880392318)).title("Bệnh viện Y Học Cổ Truyền TP.HCM"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(10.782254073267765, 106.68908763597192)).title("Bệnh Viện Quốc Tế Thận Và Lọc Thận Dialasie"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(10.778986176223595, 106.68502718170089)).title("Bệnh viện Mắt TP.HCM"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(10.786328114464856, 106.68699983030913), 16));
    }

}
