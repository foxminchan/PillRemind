package com.example.pillremind.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import com.example.pillremind.R;
import com.example.pillremind.adapter.TimeDoseAdapter;
import com.example.pillremind.model.Pill;
import com.example.pillremind.model.PillTask;
import com.example.pillremind.model.SpecificDays;
import com.example.pillremind.model.TimeDoseList;
import com.example.pillremind.model.domain.TimeDose;
import com.example.pillremind.presenter.PillEditPresenter;
import com.example.pillremind.presenter.base.IEditPill;
import com.example.pillremind.service.AlarmReceiver;
import com.example.pillremind.service.AlarmService;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class PillEditActivity extends AppCompatActivity implements View.OnClickListener, IEditPill.View {
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private final String[] unitArray = {"mg", "ml", "g", "mg/ml", "mg/g", "ml/g", "ml/ml", "g/g", "g/ml", "g/mg", "ml/mg", "ml/ml"};
    private final String[] frequency = {"Khi c???n thi???t", "H???ng ng??y", "Ng??y c??? th???", "Kho???ng th???i gian c??? th???"};
    private final String[] time = {"Tr?????c khi ??n", "Sau khi ??n", "Tr?????c khi ng???"};
    private EditText edtTimes;
    private EditText edtName;
    private EditText edtDescription;
    private EditText edtStrength;
    private EditText edtOften;
    private EditText edtStartDate;
    private EditText edtEndDate;
    private CheckBox ckMonday;
    private CheckBox ckTuesday;
    private CheckBox ckWednesday;
    private CheckBox ckThursday;
    private CheckBox ckFriday;
    private CheckBox ckSaturday;
    private CheckBox ckSunday;
    private ImageView pillImage;
    private ImageView btnOcr;
    private AppCompatSpinner unitSpinner;
    private AppCompatSpinner frequencySpinner;
    private AppCompatSpinner spMeal;
    private LinearLayout lnTime;
    private LinearLayout lnOften;
    private LinearLayout lnDuration;
    private LinearLayout lnDate;
    private ListView lvTimeAndDose;
    private TextView tvHideWarning;
    private ImageView pillImageCamera;
    private PillEditPresenter pillEditPresenter;
    private Button btnAddPhoto;
    ActivityResultLauncher<Intent> cameraActivityForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                pillImageCamera.setImageBitmap(Objects.requireNonNull(data).getParcelableExtra("data"));
                pillImageCamera.setRotation(-90);
                if (pillImageCamera.getDrawable() != null) {
                    btnAddPhoto.setText("Thay ?????i ???nh");
                } else {
                    btnAddPhoto.setText("Th??m ???nh");
                }
            }
        }
    });
    ActivityResultLauncher<Intent> galleryActivityForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                if (data != null && data.getData() != null) {
                    Uri uri = data.getData();
                    try {
                        MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    } catch (IOException e) {
                        Log.e("PillActivity", "Error getting bitmap", e);
                    }
                    pillImageCamera.setImageURI(uri);
                    if (pillImageCamera.getDrawable() != null) {
                        btnAddPhoto.setText("Thay ?????i ???nh");
                    } else {
                        btnAddPhoto.setText("Th??m ???nh");
                    }
                }
            }
        }
    });
    private Button btnSave;
    private List<Integer> listTimeAndDose;
    private TimeDoseAdapter timeDoseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pill_edit);
        initViews();
        initPresenter();
        registerListeners();
        loadUnitSpinner();
        loadFrequencySpinner();
        loadMealSpinner();
        initTimeAndDose();
        Intent intent = getIntent();
        String name = intent.getStringExtra("Name");
        edtName.setText(name);
        pillEditPresenter.getPillEdit(edtName.getText().toString());
    }

    public void loadUnitSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, unitArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitSpinner.setAdapter(adapter);
    }

    public void loadFrequencySpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, frequency);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        frequencySpinner.setAdapter(adapter);
    }

    public void loadMealSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, time);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMeal.setAdapter(adapter);
    }

    public void loadContent() {
        if (frequencySpinner.getSelectedItem().toString().equals("Khi c???n thi???t")) {
            tvHideWarning.setVisibility(View.VISIBLE);
            lnTime.setVisibility(View.GONE);
            lnOften.setVisibility(View.GONE);
            lnDuration.setVisibility(View.GONE);
            lnDate.setVisibility(View.GONE);
        } else if (frequencySpinner.getSelectedItem().toString().equals("H???ng ng??y")) {
            tvHideWarning.setVisibility(View.GONE);
            lnTime.setVisibility(View.VISIBLE);
            lnOften.setVisibility(View.GONE);
            lnDuration.setVisibility(View.VISIBLE);
            lnDate.setVisibility(View.GONE);
        } else if (frequencySpinner.getSelectedItem().toString().equals("Ng??y c??? th???")) {
            tvHideWarning.setVisibility(View.GONE);
            lnTime.setVisibility(View.VISIBLE);
            lnOften.setVisibility(View.GONE);
            lnDuration.setVisibility(View.VISIBLE);
            lnDate.setVisibility(View.VISIBLE);
        } else if (frequencySpinner.getSelectedItem().toString().equals("Kho???ng th???i gian c??? th???")) {
            tvHideWarning.setVisibility(View.GONE);
            lnTime.setVisibility(View.VISIBLE);
            lnOften.setVisibility(View.VISIBLE);
            lnDuration.setVisibility(View.VISIBLE);
            lnDate.setVisibility(View.GONE);
        }
    }

    public void initTimeAndDose() {
        listTimeAndDose = new ArrayList<>();
        for (int i = 0; i < Integer.parseInt(edtTimes.getText().toString()); i++) {
            listTimeAndDose.add(i);
        }
        timeDoseAdapter = new TimeDoseAdapter(this, listTimeAndDose);
        lvTimeAndDose.setAdapter(timeDoseAdapter);
        getListViewSize(lvTimeAndDose);
    }

    private void getListViewSize(@NonNull ListView lvTimeAndDose) {
        ListAdapter listAdapter = lvTimeAndDose.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, lvTimeAndDose);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = lvTimeAndDose.getLayoutParams();
        params.height = totalHeight + (lvTimeAndDose.getDividerHeight() * (listAdapter.getCount() - 1));
        lvTimeAndDose.setLayoutParams(params);
        lvTimeAndDose.requestLayout();
    }

    public void initViews() {
        pillImage = findViewById(R.id.btnBack);
        unitSpinner = findViewById(R.id.spUnit);
        frequencySpinner = findViewById(R.id.spFrequency);
        btnAddPhoto = findViewById(R.id.btnAddPhoto);
        pillImageCamera = findViewById(R.id.imgPill);
        lnTime = findViewById(R.id.lnTimes);
        tvHideWarning = findViewById(R.id.hideWarning);
        lnOften = findViewById(R.id.lnOften);
        lnDuration = findViewById(R.id.lnDuration);
        lnDate = findViewById(R.id.lnDate);
        btnOcr = findViewById(R.id.ocr);
        spMeal = findViewById(R.id.spMeal);
        lvTimeAndDose = findViewById(R.id.lvTimeAndDose);
        edtTimes = findViewById(R.id.edtTimes);
        btnSave = findViewById(R.id.btnSave);
        edtName = findViewById(R.id.edtPillname);
        edtDescription = findViewById(R.id.edtDescription);
        edtStrength = findViewById(R.id.edtStrength);
        ckMonday = findViewById(R.id.ckMonday);
        ckTuesday = findViewById(R.id.ckTuesday);
        ckWednesday = findViewById(R.id.ckWednesday);
        ckThursday = findViewById(R.id.ckThursday);
        ckFriday = findViewById(R.id.ckFriday);
        ckSaturday = findViewById(R.id.ckSaturday);
        ckSunday = findViewById(R.id.ckSunday);
        edtOften = findViewById(R.id.edtOften);
        edtStartDate = findViewById(R.id.edtStartDay);
        edtEndDate = findViewById(R.id.edtEndDay);
    }

    public void registerListeners() {
        pillImage.setOnClickListener(this);
        btnAddPhoto.setOnClickListener(this);
        frequencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadContent();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(PillEditActivity.this, "Vui l??ng ch???n t???n su???t", Toast.LENGTH_SHORT).show();
            }
        });
        btnOcr.setOnClickListener(this);
        edtTimes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d("beforeTextChanged", "beforeTextChanged: " + s);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    initTimeAndDose();
                } else {
                    listTimeAndDose.clear();
                    timeDoseAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("afterTextChanged", "afterTextChanged: " + s);
            }
        });
        btnSave.setOnClickListener(this);
        edtStartDate.setOnClickListener(this);
        edtEndDate.setOnClickListener(this);
    }

    public void initPresenter() {
        pillEditPresenter = new PillEditPresenter(this);
    }

    @Override
    public void onClick(@NonNull View v) {
        if (v.getId() == R.id.btnBack) {
            finish();
        } else if (v.getId() == btnAddPhoto.getId()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Ch???n ???nh");
            builder.setItems(new CharSequence[]{"Ch???p ???nh", "Ch???n t??? th?? vi???n", "H???y"}, (dialog, which) -> {
                if (which == 0) {
                    if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                    } else {
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        cameraActivityForResult.launch(cameraIntent);
                    }
                } else if (which == 1) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    galleryActivityForResult.launch(intent);
                } else {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        } else if (v.getId() == btnOcr.getId()) {
            Toast.makeText(this, "T??nh n??ng ??ang ph??t tri???n", Toast.LENGTH_SHORT).show();
        } else if (v.getId() == btnSave.getId() && validate()) {
            updateAction();
        } else if (v.getId() == edtStartDate.getId() || v.getId() == edtEndDate.getId()) {
            showDatePickerDialog(v);
        }
    }

    private void updateAction() {
        if (pillImageCamera.getDrawable() != null) {
            pillImageCamera.setDrawingCacheEnabled(true);
            pillImageCamera.buildDrawingCache();
            Bitmap bitmap = pillImageCamera.getDrawingCache();
            pillEditPresenter.uploadImage(bitmap);
        }
    }


    private boolean validate() {
        if (edtName.getText().toString().isEmpty()) {
            edtName.setError("Vui l??ng nh???p t??n thu???c");
            return false;
        }
        if (edtDescription.getText().toString().isEmpty()) {
            edtDescription.setError("Vui l??ng nh???p m?? t???");
            return false;
        }
        if (edtStrength.getText().toString().isEmpty()) {
            edtStrength.setError("Vui l??ng nh???p s??? l?????ng");
            return false;
        }
        if (lnTime.getVisibility() == View.VISIBLE && edtTimes.getText().toString().isEmpty()) {
            edtTimes.setError("Vui l??ng nh???p s??? l???n");
            return false;
        }
        if (lnDuration.getVisibility() == View.VISIBLE) {
            for (int i = 0; i < listTimeAndDose.size(); i++) {
                if (getEdtTime().get(i).getText().toString().isEmpty()) {
                    getEdtTime().get(i).setError("Vui l??ng nh???p th???i gian");
                    return false;
                }
                if (getEdtDose().get(i).getText().toString().isEmpty()) {
                    getEdtDose().get(i).setError("Vui l??ng nh???p li???u l?????ng");
                    return false;
                }
            }
        }
        if (lnDate.getVisibility() == View.VISIBLE && (!ckFriday.isChecked() && !ckMonday.isChecked() && !ckSaturday.isChecked() && !ckSunday.isChecked() && !ckThursday.isChecked() && !ckTuesday.isChecked() && !ckWednesday.isChecked())) {
            Toast.makeText(this, "Vui l??ng ch???n ??t nh???t m???t ng??y", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (lnOften.getVisibility() == View.VISIBLE && edtOften.getText().toString().isEmpty()) {
            edtOften.setError("Vui l??ng nh???p t???n su???t");
            return false;
        }
        if (edtStartDate.getText().toString().isEmpty()) {
            edtStartDate.setError("Vui l??ng nh???p ng??y b???t ?????u");
            return false;
        }
        if (edtEndDate.getText().toString().isEmpty()) {
            edtEndDate.setError("Vui l??ng nh???p ng??y k???t th??c");
            return false;
        }
        if (edtStartDate.getText().toString().compareTo(edtEndDate.getText().toString()) > 0) {
            edtEndDate.setError("Ng??y k???t th??c ph???i l???n h??n ng??y b???t ?????u");
            return false;
        }
        if (lnDuration.getVisibility() == View.VISIBLE) {
            int sum = 0;
            for (int i = 0; i < listTimeAndDose.size(); i++) {
                sum += Integer.parseInt(getEdtDose().get(i).getText().toString());
            }
            if (sum > Integer.parseInt(edtStrength.getText().toString())) {
                Toast.makeText(this, "T???ng li???u l?????ng ph???i nh??? h??n s??? l?????ng", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        if (edtEndDate.getText().toString().compareTo(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))) < 0) {
            edtEndDate.setError("Ng??y k???t th??c ph???i l???n h??n ng??y hi???n t???i");
            return false;
        }
        if (!edtStartDate.getText().toString().matches("\\d{1,2}/\\d{1,2}/\\d{4}")) {
            edtStartDate.setError("Ng??y b???t ?????u kh??ng ????ng ?????nh d???ng");
            return false;
        }
        if (!edtEndDate.getText().toString().matches("\\d{1,2}/\\d{1,2}/\\d{4}")) {
            edtEndDate.setError("Ng??y k???t th??c kh??ng ????ng ?????nh d???ng");
            return false;
        }
        if (edtStartDate.getText().toString().compareTo(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))) < 0) {
            edtStartDate.setError("Ng??y b???t ?????u ph???i l???n h??n ho???c b???ng ng??y hi???n t???i");
            return false;
        }
        if (lnDuration.getVisibility() == View.VISIBLE) {
            for (int i = 0; i < listTimeAndDose.size(); i++) {
                if (!getEdtTime().get(i).getText().toString().matches("\\d{1,2}:\\d{1,2}")) {
                    getEdtTime().get(i).setError("Th???i gian kh??ng ????ng ?????nh d???ng");
                    return false;
                }
            }
        }
        return true;
    }

    @NonNull
    private List<EditText> getEdtDose() {
        List<EditText> list = new ArrayList<>();
        for (int i = 0; i < listTimeAndDose.size(); i++) {
            list.add(lvTimeAndDose.getChildAt(i).findViewById(R.id.edtDose));
        }
        return list;
    }

    @NonNull
    private List<EditText> getEdtTime() {
        List<EditText> list = new ArrayList<>();
        for (int i = 0; i < listTimeAndDose.size(); i++) {
            View view = lvTimeAndDose.getChildAt(i);
            EditText edtTime = view.findViewById(R.id.edtTime);
            list.add(edtTime);
        }
        return list;
    }

    private void showDatePickerDialog(View v) {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        @SuppressLint("SetTextI18n") DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, month1, dayOfMonth) -> {
            if (v.getId() == edtStartDate.getId()) {
                edtStartDate.setText(dayOfMonth + "/" + (month1 + 1) + "/" + year1);
            } else if (v.getId() == edtEndDate.getId()) {
                edtEndDate.setText(dayOfMonth + "/" + (month1 + 1) + "/" + year1);
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void setPillEdit(@NonNull Pill pill, PillTask pillTask, TimeDoseList timeDoseList, SpecificDays specificDays) {
        frequencySpinner.setSelection(getIndex(frequencySpinner, pill.getFrequency()));
        loadContent();
        edtDescription.setText(pill.getDescription());
        edtStartDate.setText(pill.getStartDate());
        edtEndDate.setText(pill.getEndDate());
        spMeal.setSelection(getIndex(spMeal, pill.getDrinkTime()));
        if (!Objects.equals(pill.getImageUri(), "")) {
            Picasso.get().load(pill.getImageUri()).placeholder(R.drawable.loading_circle).into(pillImageCamera);
            btnAddPhoto.setText("Thay ?????i ???nh");
        }
        edtStrength.setText(String.valueOf(pill.getAmount()));
        unitSpinner.setSelection(getIndex(unitSpinner, pill.getUnit()));
        if (frequencySpinner.getSelectedItem().toString().equals("H???ng ng??y")) {
            edtTimes.setText(String.valueOf(pillTask.getTimesADayTaken()));
            initTimeAndDose();
            int size = timeDoseList.getTimeDoses().size();
            for (int i = 0; i < size; i++) {
                View view = timeDoseAdapter.getView(i, null, lvTimeAndDose);
                EditText edtTime = view.findViewById(R.id.edtTime);
                EditText edtDose = view.findViewById(R.id.edtDose);
                edtTime.setText(timeDoseList.getTimeDoses().get(i).getTime());
                edtDose.setText(String.valueOf(timeDoseList.getTimeDoses().get(i).getDose()));
            }
        } else if (frequencySpinner.getSelectedItem().toString().equals("Ng??y c??? th???")) {
            edtTimes.setText(String.valueOf(pillTask.getTimesADayTaken()));
            initTimeAndDose();
            int size = timeDoseList.getTimeDoses().size();
            for (int i = 0; i < size; i++) {
                View view = timeDoseAdapter.getView(i, null, lvTimeAndDose);
                EditText edtTime = view.findViewById(R.id.edtTime);
                EditText edtDose = view.findViewById(R.id.edtDose);
                edtTime.setText(timeDoseList.getTimeDoses().get(i).getTime());
                edtDose.setText(String.valueOf(timeDoseList.getTimeDoses().get(i).getDose()));
            }
            ckMonday.setChecked(specificDays.isMonday());
            ckTuesday.setChecked(specificDays.isTuesday());
            ckWednesday.setChecked(specificDays.isWednesday());
            ckThursday.setChecked(specificDays.isThursday());
            ckFriday.setChecked(specificDays.isFriday());
            ckSaturday.setChecked(specificDays.isSaturday());
            ckSunday.setChecked(specificDays.isSunday());
        } else if (frequencySpinner.getSelectedItem().toString().equals("Kho???ng th???i gian c??? th???")) {
            edtTimes.setText(String.valueOf(pillTask.getTimesADayTaken()));
            initTimeAndDose();
            int size = timeDoseList.getTimeDoses().size();
            for (int i = 0; i < size; i++) {
                View view = timeDoseAdapter.getView(i, null, lvTimeAndDose);
                EditText edtTime = view.findViewById(R.id.edtTime);
                EditText edtDose = view.findViewById(R.id.edtDose);
                edtTime.setText(timeDoseList.getTimeDoses().get(i).getTime());
                edtDose.setText(String.valueOf(timeDoseList.getTimeDoses().get(i).getDose()));
            }
            edtOften.setText(String.valueOf(pillTask.getTimesADay()));
        }
    }

    @Override
    public void onUrlUploaded(String url) {
        Log.d("url", url);
        if (frequencySpinner.getSelectedItem().toString().equals("Khi c???n thi???t")) {
            pillEditPresenter.updateOnlyPill(edtName.getText().toString(), new Pill(edtDescription.getText().toString(), edtStartDate.getText().toString(), edtEndDate.getText().toString(), spMeal.getSelectedItem().toString(), url, Integer.parseInt(edtStrength.getText().toString()), unitSpinner.getSelectedItem().toString(), frequencySpinner.getSelectedItem().toString()));
        } else if (frequencySpinner.getSelectedItem().toString().equals("H???ng ng??y")) {
            List<TimeDose> list = new ArrayList<>();
            for (int i = 0; i < listTimeAndDose.size(); i++) {
                list.add(new TimeDose(getEdtTime().get(i).getText().toString(), Integer.parseInt(getEdtDose().get(i).getText().toString())));
            }
            pillEditPresenter.updateEveryDayPill(edtName.getText().toString(), new Pill(edtDescription.getText().toString(), edtStartDate.getText().toString(), edtEndDate.getText().toString(), spMeal.getSelectedItem().toString(), url, Integer.parseInt(edtStrength.getText().toString()), unitSpinner.getSelectedItem().toString(), frequencySpinner.getSelectedItem().toString()), new PillTask(Integer.parseInt(edtTimes.getText().toString()), 0), new TimeDoseList(list));
        } else if (frequencySpinner.getSelectedItem().toString().equals("Ng??y c??? th???")) {
            List<TimeDose> list = new ArrayList<>();
            for (int i = 0; i < listTimeAndDose.size(); i++) {
                list.add(new TimeDose(getEdtTime().get(i).getText().toString(), Integer.parseInt(getEdtDose().get(i).getText().toString())));
            }
            pillEditPresenter.updateSpecificDayPill(edtName.getText().toString(), new Pill(edtDescription.getText().toString(), edtStartDate.getText().toString(), edtEndDate.getText().toString(), spMeal.getSelectedItem().toString(), url, Integer.parseInt(edtStrength.getText().toString()), unitSpinner.getSelectedItem().toString(), frequencySpinner.getSelectedItem().toString()), new PillTask(Integer.parseInt(edtTimes.getText().toString()), 0), new TimeDoseList(list), new SpecificDays(ckMonday.isChecked(), ckTuesday.isChecked(), ckWednesday.isChecked(), ckThursday.isChecked(), ckFriday.isChecked(), ckSaturday.isChecked(), ckSunday.isChecked()));
        } else if (frequencySpinner.getSelectedItem().toString().equals("Kho???ng th???i gian c??? th???")) {
            List<TimeDose> list = new ArrayList<>();
            for (int i = 0; i < listTimeAndDose.size(); i++) {
                list.add(new TimeDose(getEdtTime().get(i).getText().toString(), Integer.parseInt(getEdtDose().get(i).getText().toString())));
            }
            pillEditPresenter.updateEveryDayPill(edtName.getText().toString(), new Pill(edtDescription.getText().toString(), edtStartDate.getText().toString(), edtEndDate.getText().toString(), spMeal.getSelectedItem().toString(), url, Integer.parseInt(edtStrength.getText().toString()), unitSpinner.getSelectedItem().toString(), frequencySpinner.getSelectedItem().toString()), new PillTask(Integer.parseInt(edtTimes.getText().toString()), Integer.parseInt(edtOften.getText().toString())), new TimeDoseList(list));
        }
    }

    @Override
    public void onPillEdited(String result) {
        Log.d("resultPillAction", result);
        if (result.equals("C???p nh???t th??nh c??ng")) {
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
            stopAlarm();
            setAlarmNotification();
            clearView();
            finish();
        } else {
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        }
    }

    private void stopAlarm() {
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
        Intent serviceIntent = new Intent(this, AlarmService.class);
        this.stopService(serviceIntent);
    }

    private void setAlarmNotification() {
        if (frequencySpinner.getSelectedItem().toString().equals("H???ng ng??y")) {
            LocalDate startDate = LocalDate.parse(edtStartDate.getText().toString(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            LocalDate endDate = LocalDate.parse(edtEndDate.getText().toString(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            int days = (int) ChronoUnit.DAYS.between(startDate, endDate);
            for (int i = 0; i <= days; i++) {
                LocalDate date = startDate.plusDays(i);
                createAlarm(date);
            }
            Toast.makeText(this, "???? ?????t l???ch nh???c v??o h??ng n??y", Toast.LENGTH_SHORT).show();
        } else if (frequencySpinner.getSelectedItem().toString().equals("Ng??y c??? th???")) {
            LocalDate startDate = LocalDate.parse(edtStartDate.getText().toString(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            LocalDate endDate = LocalDate.parse(edtEndDate.getText().toString(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            int days = (int) ChronoUnit.DAYS.between(startDate, endDate);
            for (int i = 0; i <= days; i++) {
                LocalDate date = startDate.plusDays(i);
                if ((ckMonday.isChecked() && date.getDayOfWeek().equals(DayOfWeek.MONDAY)) ||
                        (ckTuesday.isChecked() && date.getDayOfWeek().equals(DayOfWeek.TUESDAY)) ||
                        (ckWednesday.isChecked() && date.getDayOfWeek().equals(DayOfWeek.WEDNESDAY)) ||
                        (ckThursday.isChecked() && date.getDayOfWeek().equals(DayOfWeek.THURSDAY)) ||
                        (ckFriday.isChecked() && date.getDayOfWeek().equals(DayOfWeek.FRIDAY)) ||
                        (ckSaturday.isChecked() && date.getDayOfWeek().equals(DayOfWeek.SATURDAY)) ||
                        (ckSunday.isChecked() && date.getDayOfWeek().equals(DayOfWeek.SUNDAY))) {
                    createAlarm(date);
                }
            }
        } else if (frequencySpinner.getSelectedItem().toString().equals("Kho???ng th???i gian c??? th???")){
            LocalDate startDate = LocalDate.parse(edtStartDate.getText().toString(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            LocalDate endDate = LocalDate.parse(edtEndDate.getText().toString(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            int days = (int) ChronoUnit.DAYS.between(startDate, endDate);
            for (int i = 0; i <= days; i+=Integer.parseInt(edtOften.getText().toString())) {
                LocalDate date = startDate.plusDays(i);
                createAlarm(date);
            }
        }
    }

    private void createAlarm(LocalDate date) {
        for (int j = 0; j < listTimeAndDose.size(); j++) {
            String timePill = getEdtTime().get(j).getText().toString();
            Log.d("timePill", timePill);
            String dose = getEdtDose().get(j).getText().toString();
            Log.d("timePill", dose);
            String content = "H??y u???ng " + dose + " vi??n " + edtName.getText().toString() + " v??o l??c " + timePill;
            int requestCode = 0;
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, AlarmReceiver.class);
            intent.putExtra("content", content);
            intent.putExtra("requestCode", requestCode);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            String[] timeSplit = timePill.split(":");
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeSplit[0]));
            calendar.set(Calendar.MINUTE, Integer.parseInt(timeSplit[1]));
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            calendar.set(Calendar.YEAR, date.getYear());
            calendar.set(Calendar.MONTH, date.getMonthValue() - 1);
            calendar.set(Calendar.DAY_OF_MONTH, date.getDayOfMonth());
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }


    private void clearView() {
        edtName.setText("");
        edtDescription.setText("");
        edtStrength.setText("");
        edtTimes.setText("");
        edtOften.setText("");
        edtStartDate.setText("");
        edtEndDate.setText("");
        ckFriday.setChecked(false);
        ckMonday.setChecked(false);
        ckSaturday.setChecked(false);
        ckSunday.setChecked(false);
        ckThursday.setChecked(false);
        ckTuesday.setChecked(false);
        ckWednesday.setChecked(false);
        pillImageCamera.setImageResource(R.drawable.foam_add_ic);
        listTimeAndDose.clear();
        timeDoseAdapter.notifyDataSetChanged();
    }

    private int getIndex(@NonNull AppCompatSpinner spinner, String myString) {
        int index = 0;
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).equals(myString)) {
                index = i;
            }
        }
        return index;
    }
}