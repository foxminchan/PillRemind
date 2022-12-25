package com.example.pillremind.view;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.example.pillremind.R;
import com.example.pillremind.databinding.ActivityDrugStoreBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class DrugStoreActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityDrugStoreBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDrugStoreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.addMarker(new MarkerOptions().position(new LatLng(10.790136168922587, 106.68893064763436)).title("Nhà Thuốc FPT Long Châu"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(10.789999429962533, 106.68910953669922)).title("Nhà Thuốc Nhị Trưng 5"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(10.789924714711686, 106.6892455227357)).title("Nhà thuốc Liên Châu"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(10.790262582729914, 106.68932435448822)).title("HIỆU THUỐC SỐ 16"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(10.791079607365713, 106.68815764896324)).title("Nhà Thuốc Nhị Trưng"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(10.790101718414833, 106.68896759980841)).title("Nhà Thuốc Pharmacity"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(10.789915760923266, 106.68924176899115)).title("Nhà thuốc Liên Châu"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(10.78894820741699, 106.68918542477175)).title("Công Ty TNHH Dược Phẩm Bổn Nguyệt"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(10.786155835538855, 106.68817083071615)).title("Viện Pasteur TPHCM (cổng NKKN)"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(10.786328114464856, 106.68699983030913), 16));
    }
}