package com.example.pillremind.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.example.pillremind.R;
import com.example.pillremind.presenter.ProfilePresenter;
import com.example.pillremind.presenter.base.IProfile;


public class ProfileFragment extends Fragment implements View.OnClickListener, IProfile.View {

    private RelativeLayout rlVersion;
    private RelativeLayout rlDeleteAccount;
    private RelativeLayout rlPrivacyPolicy;
    private RelativeLayout rlFamily;
    private RelativeLayout rlTerms;
    private RelativeLayout rlContactUs;
    private RelativeLayout rlSocialMedia;
    private AppCompatButton btnLogout;
    private ProfilePresenter profilePresenter;
    private TextView tvName;
    private TextView tvPhone;
    private SharedPreferences sharedPreferences;
    private ImageView ivVerifyPhoneNumber;
    private RelativeLayout rlVerifyPhoneNumber;
    private RelativeLayout rlChangePassword;

    public ProfileFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        registerListener();
        initPresenter();
        sharedPreferences = requireActivity().getSharedPreferences("profile", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("name") && sharedPreferences.contains("phone")) {
            tvName.setText(sharedPreferences.getString("name", ""));
            tvPhone.setText(sharedPreferences.getString("phone", ""));
        } else {
            profilePresenter.getCurrentUser();
        }
        profilePresenter.getVerifyPhoneSatus();
    }

    public void init(@NonNull View view) {
        rlVersion = view.findViewById(R.id.rlVersion);
        rlDeleteAccount = view.findViewById(R.id.rlDeleteAccount);
        btnLogout = view.findViewById(R.id.btnLogout);
        tvName = view.findViewById(R.id.tvFullName);
        tvPhone = view.findViewById(R.id.tvPhoneNumber);
        ivVerifyPhoneNumber = view.findViewById(R.id.ivGetVerify);
        rlPrivacyPolicy = view.findViewById(R.id.rlPrivacyPolicy);
        rlFamily = view.findViewById(R.id.rlFamily);
        rlTerms = view.findViewById(R.id.rlTerms);
        rlContactUs = view.findViewById(R.id.rlContactUs);
        rlSocialMedia = view.findViewById(R.id.rlLinkSocialAccount);
        rlVerifyPhoneNumber = view.findViewById(R.id.rlVerifyPhone);
        rlChangePassword = view.findViewById(R.id.rlChangePassword);
    }

    public void registerListener() {
        rlVersion.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
        rlDeleteAccount.setOnClickListener(this);
        rlPrivacyPolicy.setOnClickListener(this);
        rlFamily.setOnClickListener(this);
        rlTerms.setOnClickListener(this);
        rlContactUs.setOnClickListener(this);
        rlSocialMedia.setOnClickListener(this);
        rlVerifyPhoneNumber.setOnClickListener(this);
        rlChangePassword.setOnClickListener(this);
    }

    public void initPresenter() {
        profilePresenter = new ProfilePresenter(this);
    }

    @Override
    public void onClick(@NonNull View v) {
        if (v.getId() == rlVersion.getId()) {
            Toast.makeText(getContext(), "v1.0.00", Toast.LENGTH_SHORT).show();
        } else if (v.getId() == btnLogout.getId()) {
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
            clearData();
        } else if (v.getId() == rlDeleteAccount.getId()) {
            comfirmDeleteAccount();
        } else if (v.getId() == rlPrivacyPolicy.getId()) {
            Intent intent = new Intent(getContext(), PrivacyPolicyActivity.class);
            startActivity(intent);
        } else if (v.getId() == rlFamily.getId()) {
            Toast.makeText(getContext(), "Chức năng đang được phát triển", Toast.LENGTH_SHORT).show();
        } else if (v.getId() == rlTerms.getId()) {
            Intent intent = new Intent(getContext(), TermsActivity.class);
            startActivity(intent);
        } else if (v.getId() == rlContactUs.getId()) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + "nguyenxuannhan.dev@gmail.com"));
            intent.putExtra(Intent.EXTRA_SUBJECT, "Liên hệ với nhóm phát triển PillRemind");
            startActivity(intent);
        } else if (v.getId() == rlSocialMedia.getId()) {
            Toast.makeText(getContext(), "Chức năng đang được phát triển", Toast.LENGTH_SHORT).show();
        } else if (v.getId() == rlVerifyPhoneNumber.getId()) {
            Toast.makeText(getContext(), "Chức năng đang được phát triển", Toast.LENGTH_SHORT).show();
        } else if (v.getId() == rlChangePassword.getId()) {
            Intent intent = new Intent(getContext(), ChangePasswordActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onSetVerifyPhoneNumberIconResult(boolean b) {
        ivVerifyPhoneNumber.setImageResource(b ? R.drawable.check_ic : R.drawable.getin_ic);
    }

    @Override
    public void onGetCurrentUserResult(String name, String phone) {
        tvName.setText(name);
        tvPhone.setText(phone);
        sharedPreferences = requireActivity().getSharedPreferences("profile", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", name);
        editor.putString("phone", phone);
        editor.apply();
    }

    public void comfirmDeleteAccount() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Xác nhận");
        builder.setMessage("Bạn có chắc chắn muốn xóa tài khoản này không?");
        builder.setPositiveButton("Có", (dialog, which) -> {
            profilePresenter.deleteAccount();
            clearData();
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
            Toast.makeText(getContext(), "Đã xóa tài khoản thành công", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Không", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    public void clearData() {
        sharedPreferences = requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear().apply();
        sharedPreferences = requireActivity().getSharedPreferences("profile", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.clear().apply();
    }
}