package com.apollo.medgift.views.gifter;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.apollo.medgift.R;
import com.apollo.medgift.common.BaseActivity;
import com.apollo.medgift.common.Firebase;
import com.apollo.medgift.common.Util;
import com.apollo.medgift.databinding.ActivityAddrecipientBinding;
import com.apollo.medgift.models.Recipient;

public class AddRecipientActivity extends BaseActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private ActivityAddrecipientBinding addRecipientBinding;
    private Recipient recipient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addRecipientBinding = ActivityAddrecipientBinding.inflate(getLayoutInflater());
        setContentView(addRecipientBinding.getRoot());
        applyWindowInsetsListenerTo(this, addRecipientBinding.addRecipient);

        Intent intent = getIntent();
        recipient = (Recipient) intent.getSerializableExtra(Recipient.STORE);

        boolean exists = Util.exists(recipient);
        String title = getString(exists ? R.string.editRecipientTitle : R.string.addRecipientTitle);
        setupToolbar(addRecipientBinding.homeAppBar.getRoot(), title, true);
        if (!exists) {
            recipient.setCreatedBy(Firebase.currentUser().getUid());
        }
        setup();
    }

    private void setup() {
        addRecipientBinding.edtAddress.setText(recipient.getAddress());
        addRecipientBinding.edtDOB.setText(recipient.getDateOfBirth());
        addRecipientBinding.edtEmail.setText(recipient.getEmail());
        addRecipientBinding.edtPhone.setText(recipient.getPhone());
        addRecipientBinding.edtFirstName.setText(recipient.getFirstName());
        addRecipientBinding.edtLastName.setText(recipient.getLastName());
        addRecipientBinding.edtDOB.setInputType(InputType.TYPE_NULL);
        addRecipientBinding.edtDOB.setOnClickListener(this);
        addRecipientBinding.btnSave.setOnClickListener(this);
        addRecipientBinding.rgGender.setOnCheckedChangeListener(this);
        addRecipientBinding.rdMale.setChecked(false);
        addRecipientBinding.rdFemale.setChecked(false);
        String gender = recipient.getGender();
        if (gender != null) {
            if (gender.equals("M")) {
                addRecipientBinding.rdMale.setChecked(true);
            } else if (gender.equals("F")) {
                addRecipientBinding.rdFemale.setChecked(true);
            }
        }
    }


    // Clear previous errors
    private void clearErrors() {
        addRecipientBinding.lytFName.setError("");
        addRecipientBinding.lytLName.setError("");
        addRecipientBinding.lytPhone.setError("");
        addRecipientBinding.lytEmail.setError("");
        addRecipientBinding.lytDOB.setError("");
        addRecipientBinding.lytAddress.setError("");
    }

    private void saveRecipient() {
        clearErrors();

        boolean formIsValid = true;
        String firstName = Util.valueOf(addRecipientBinding.edtFirstName);
        String lastName = Util.valueOf(addRecipientBinding.edtLastName);
        String email = Util.valueOf(addRecipientBinding.edtEmail);
        String dob = Util.valueOf(addRecipientBinding.edtDOB);
        String phone = Util.valueOf(addRecipientBinding.edtPhone);
        String address = Util.valueOf(addRecipientBinding.edtAddress);

        if (firstName.isEmpty()) {
            addRecipientBinding.lytFName.setError("First name is required.");
            formIsValid = false;
        }
        if (lastName.isEmpty()) {
            addRecipientBinding.lytLName.setError("Last name is required.");
            formIsValid = false;
        }
        if (email.isEmpty() || !Util.isEmail(email)) {
            addRecipientBinding.lytEmail.setError("Email is required.");
            formIsValid = false;
        }
        if (phone.isEmpty()) {
            addRecipientBinding.lytPhone.setError("Phone is required.");
            formIsValid = false;
        }
        if (dob.isEmpty()) {
            addRecipientBinding.lytDOB.setError("Date of birth is required.");
            formIsValid = false;
        }

        if (address.isEmpty()) {
            addRecipientBinding.lytAddress.setError("Address is required.");
            formIsValid = false;
        }

        if (recipient.getGender() == null || recipient.getGender().isEmpty()) {
            Util.notify(this, "Gender is required");
            formIsValid = false;
        }

        if (formIsValid) {
            recipient.setFirstName(firstName);
            recipient.setLastName(lastName);
            recipient.setEmail(email);
            recipient.setPhone(phone);
            recipient.setAddress(address);
            recipient.setDateOfBirth(dob);

            boolean exists = Util.exists(recipient);
            Util.startProgress(addRecipientBinding.progress, "Adding recipient...");

            Firebase.save(recipient, Recipient.STORE, (task) -> {
                Util.stopProgress(addRecipientBinding.progress);
                if (task.isSuccessful()) {

                    recipient = null;
                    Util.notify(AddRecipientActivity.this, Util.success("Recipient", exists));
                    finish();

                } else {
                    Util.notify(AddRecipientActivity.this, Util.fail("Recipient", exists));
                }
            });
        }

    }

    @Override
    public void onClick(View view) {

        if (view == addRecipientBinding.btnSave) {
            // Handle save here
            saveRecipient();
        } else if (view == addRecipientBinding.edtDOB) {
            Util.showDatePickerFor(addRecipientBinding.edtDOB, this);
        }

    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int selectedId) {
        if (selectedId == addRecipientBinding.rdMale.getId()) {
            recipient.setGender("M");
        } else if (selectedId == addRecipientBinding.rdFemale.getId()) {
            recipient.setGender("F");
        }
    }

}