package com.example.data_entry;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Drawable drawable;
    Drawable warnDrawable;
    EditText editName;
    EditText editCode;
    EditText editID;
    EditText editPhone;
    EditText editEmail;
    EditText editHometown;
    EditText editAddress;
    ConstraintLayout layoutDate;
    RadioGroup radioGroup;
    LinearLayout layoutLanguage;
    CheckBox boxAgree;
    String date = "";
    int numberIsCheck = 0;
    boolean isCheckRadio = false;
    boolean isChooseDate = false;
    int numberNonOk = 0;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawable = getDrawable(R.drawable.view_background);
        warnDrawable = getDrawable(R.drawable.view_warn_background);

        editName = findViewById(R.id.edit_name);
        editCode = findViewById(R.id.edit_code);
        editID = findViewById(R.id.edit_ID);
        editPhone = findViewById(R.id.edit_phone_number);
        editEmail = findViewById(R.id.edit_email_address);
        editHometown = findViewById(R.id.edit_hometown);
        editAddress = findViewById(R.id.edit_staying_address);
        match(editName);
        match(editCode);
        match(editID);
        match(editPhone);
        match(editEmail);
        match(editHometown);
        match(editAddress);

        radioGroup = findViewById(R.id.radio_group);
        ((RadioButton)findViewById(R.id.radio_computer_engineering)).
                setOnCheckedChangeListener((v, b) -> isCheck());
        ((RadioButton)findViewById(R.id.radio_computer_science)).
                setOnCheckedChangeListener((v, b) -> isCheck());

        layoutDate = findViewById(R.id.linearLayout_date);
        CalendarView calendar = findViewById(R.id.calendar);
        calendar.setVisibility(View.GONE);
        calendar.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            date = dayOfMonth > 9 ? dayOfMonth + "/" : "0" + dayOfMonth + "/";
            date += month > 9 ? month + "/" + year: "0" + month + "/" + year;
        });
        ((ToggleButton)findViewById(R.id.toggle_choose_date)).setOnCheckedChangeListener((v, b) -> {

            if (b) {
                calendar.setVisibility(View.VISIBLE);
            }
            else {
                calendar.setVisibility(View.GONE);

                if (date.equals("")) {
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    date = format.format(new Date(calendar.getDate()));
                }

                ((TextView) findViewById(R.id.text_date)).
                        setText(getResources().getString(R.string.text_date, date));

                if (!isChooseDate) {
                    layoutDate.setBackground(drawable);
                    isChooseDate = true;
                }
            }
        });

        layoutLanguage = findViewById(R.id.linearLayout_check);
        ((CheckBox) findViewById(R.id.check_C)).setOnCheckedChangeListener((v, b) -> isCheck(b));
        ((CheckBox) findViewById(R.id.check_Java)).setOnCheckedChangeListener((v, b) -> isCheck(b));
        ((CheckBox) findViewById(R.id.check_Python)).setOnCheckedChangeListener((v, b) -> isCheck(b));
        ((CheckBox) findViewById(R.id.check_others)).setOnCheckedChangeListener((v, b) -> isCheck(b));

        boxAgree = findViewById(R.id.check_agree);
        boxAgree.setOnCheckedChangeListener((v, b) -> {
            if (numberNonOk > 0)
                v.setBackground(b ? drawable : warnDrawable);
        });

        findViewById(R.id.button_ok).setOnClickListener(v -> handleOk());
    }

    private void match(EditText edit) {

        if (edit != null) {

            edit.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    if (numberNonOk > 0)
                        edit.setBackground(s.toString().equals("") ? warnDrawable : drawable);
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    if (numberNonOk > 0)
                        edit.setBackground(s.toString().equals("") ? warnDrawable : drawable);
                }

                @Override
                public void afterTextChanged(Editable s) {

                    if (numberNonOk > 0)
                        edit.setBackground(s.toString().equals("") ? warnDrawable : drawable);
                }
            });
        }
    }

    private void isCheck() {

        if (radioGroup != null) {

            radioGroup.setBackground(drawable);

            isCheckRadio = true;
        }
    }

    private void isCheck(boolean isCheck) {

        numberIsCheck = isCheck ? numberIsCheck  + 1 : numberIsCheck  - 1;

        if (numberNonOk > 0)
            layoutLanguage.setBackground(numberIsCheck  > 0 ? drawable : warnDrawable);
    }

    private void handleOk() {

        numberNonOk = 0;

        numberNonOk += setBackground(editName, !editName.getText().toString().equals(""));
        numberNonOk += setBackground(editCode, !editCode.getText().toString().equals(""));
        numberNonOk += setBackground(editID, !editID.getText().toString().equals(""));
        numberNonOk += setBackground(editPhone, !editPhone.getText().toString().equals(""));
        numberNonOk += setBackground(editEmail, !editEmail.getText().toString().equals(""));
        numberNonOk += setBackground(editHometown, !editHometown.getText().toString().equals(""));
        numberNonOk += setBackground(editAddress, !editAddress.getText().toString().equals(""));
        numberNonOk += setBackground(layoutDate, isChooseDate);
        numberNonOk += setBackground(radioGroup, isCheckRadio);
        numberNonOk += setBackground(layoutLanguage, numberIsCheck > 0);
        numberNonOk += setBackground(boxAgree, boxAgree.isChecked());

        if (numberNonOk == 0)
            Toast.makeText(this, R.string.toast_successful, Toast.LENGTH_LONG).show();
    }

    private int setBackground(View view, boolean isNonWarn) {
        if (isNonWarn) {
            view.setBackground(drawable);
            return 0;
        }
        else {
            view.setBackground(warnDrawable);
            return 1;
        }
    }
}