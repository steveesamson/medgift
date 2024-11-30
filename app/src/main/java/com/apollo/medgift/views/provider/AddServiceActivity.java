package com.apollo.medgift.views.provider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.apollo.medgift.R;
import com.apollo.medgift.common.BaseActivity;
import com.apollo.medgift.common.Firebase;
import com.apollo.medgift.common.Util;
import com.apollo.medgift.databinding.ActivityAddserviceBinding;
import com.apollo.medgift.models.HealthcareService;
import com.apollo.medgift.models.Role;

public class AddServiceActivity extends BaseActivity implements View.OnClickListener {

    private ActivityAddserviceBinding addserviceBinding;
    private HealthcareService healthcareService;
    private ActivityResultLauncher<Intent> serviceImageLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addserviceBinding = ActivityAddserviceBinding.inflate(getLayoutInflater());
        setContentView(addserviceBinding.getRoot());
        applyWindowInsetsListenerTo(this, addserviceBinding.main);

        Intent intent = getIntent();
        healthcareService = (HealthcareService) intent.getSerializableExtra(HealthcareService.STORE);

        boolean exists = Util.exists(healthcareService);
        String title = getString(exists ? R.string.editServiceTitle : R.string.createServiceTitle);
        setupToolbar(addserviceBinding.homeAppBar.getRoot(), title, true);
        if (!exists) {
            healthcareService.setCreatedBy(Firebase.currentUser().getUserId());
        } else {
            setup();
        }

        // initialize drop down
        initializeDropdown();

        // register image
        registerImageResult();

        addserviceBinding.addImageButton.setOnClickListener(v -> openGallery());
        addserviceBinding.btnCreateServiceSave.setOnClickListener(this);
        addserviceBinding.btnDeleteService.setOnClickListener(this);
    }

    // Register activity result and load image from gallery
    private void registerImageResult() {
        serviceImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();

                        if (imageUri != null) {
                            healthcareService.bannerUri = imageUri;
                            displayImage(imageUri);
                        } else {
                            Toast.makeText(this, getString(R.string.no_image_selected), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }

    // load the selected image
    private void displayImage(Uri imageUri) {
        addserviceBinding.serviceImage.setImageURI(imageUri);
        addserviceBinding.serviceImage.setVisibility(View.VISIBLE); // Show the image
        addserviceBinding.addImageButtonText.setText(getString(R.string.change_image));
    }

    private void initializeDropdown() {
        String[] serviceTypes = getResources().getStringArray(R.array.service_types);

        // Set up the Spinner
        ArrayAdapter<String> adapterItems = new ArrayAdapter<String>(this, R.layout.servicetype_item, serviceTypes);
        addserviceBinding.autoCompleteTxt.setAdapter(adapterItems);
        addserviceBinding.autoCompleteTxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
    }

    private void setup() {
        addserviceBinding.edtServiceName.setText(healthcareService.getServiceName());
        addserviceBinding.edtPrice.setText(String.valueOf(healthcareService.getPrice()));
        addserviceBinding.edtDescription.setText(healthcareService.getDescription());
        addserviceBinding.btnCreateServiceSave.setOnClickListener(this);

        String currentServiceType = healthcareService.getServiceType();
        if (currentServiceType != null && !currentServiceType.isEmpty()) {
            addserviceBinding.autoCompleteTxt.setText(currentServiceType, false);
        }

        if (healthcareService.getBannerUrl() != null && !healthcareService.getBannerUrl().isEmpty()) {
            addserviceBinding.serviceImage.setVisibility(View.VISIBLE);
            Util.loadImageUri(addserviceBinding.serviceImage, healthcareService.getBannerUrl(), this);
            Log.e("Banner URI: ", "URI: " + healthcareService.getBannerUrl());
            addserviceBinding.addImageButtonText.setText(getString(R.string.change_image));
        } else {
            addserviceBinding.addImageButtonText.setText(getString(R.string.add_image));
        }

        addserviceBinding.btnDeleteService.setVisibility(View.VISIBLE);

        // Restrict editing for "Gifter" role
        if (Role.GIFTER.equals(Firebase.currentUser().getUserRole())) {
            addserviceBinding.btnCreateServiceSave.setVisibility(View.GONE);
            addserviceBinding.edtServiceName.setEnabled(false);
            addserviceBinding.edtPrice.setEnabled(false);
            addserviceBinding.edtDescription.setEnabled(false);
            addserviceBinding.autoCompleteTxt.setEnabled(false);
            addserviceBinding.addImageButton.setEnabled(false);
        } else {
            // enable buttons for other roles
            addserviceBinding.btnCreateServiceSave.setVisibility(View.VISIBLE);
        }
    }

    private void clearErrors() {
        addserviceBinding.lytServiceName.setError("");
        addserviceBinding.lytPrice.setError("");
        addserviceBinding.lytType.setError("");
        addserviceBinding.lytDescription.setError("");
    }

    private void saveService() {
        boolean exists = Util.exists(healthcareService);
        Log.e("TEST DATA: ", "SaveService: " + healthcareService.getServiceName());
        Util.startProgress(addserviceBinding.progress, "Saving Service...");

        // Save service data in Firebase Database
        Firebase.save(healthcareService, HealthcareService.STORE, (task, key) -> {
            Util.stopProgress(addserviceBinding.progress);
            if (task.isSuccessful()) {
                Util.notify(AddServiceActivity.this, Util.success("Service", exists));
                finish();
            } else {
                Util.notify(AddServiceActivity.this, Util.fail("Service", exists));
            }
        });

        clearFields();

        Util.stopProgress(addserviceBinding.progress);
    }

    private void clearFields() {
        addserviceBinding.edtServiceName.setText("");
        addserviceBinding.edtPrice.setText("0.0");
        addserviceBinding.edtDescription.setText("");
        addserviceBinding.serviceImage.setVisibility(View.INVISIBLE);
        addserviceBinding.addImageButtonText.setText(getString(R.string.add_image));
    }

    @Override
    public void onClick(View v) {
        if (v == addserviceBinding.btnCreateServiceSave) {
            createService();
        }
        if (v == addserviceBinding.btnDeleteService) {
            deleteService();
        }
    }

    private void createService() {
        clearErrors();

        boolean formIsValid = true;
        String serviceName = Util.valueOf(addserviceBinding.edtServiceName);
        String priceStr = Util.valueOf(addserviceBinding.edtPrice);
        String description = Util.valueOf(addserviceBinding.edtDescription);
        String serviceType = addserviceBinding.autoCompleteTxt.getText().toString();

        if (serviceName.isEmpty()) {
            addserviceBinding.lytServiceName.setError("Service name is required.");
            formIsValid = false;
        }
        if (priceStr.isEmpty()) {
            addserviceBinding.lytPrice.setError("Price is required.");
            formIsValid = false;
        }
        if (description.isEmpty()) {
            addserviceBinding.lytDescription.setError("Description is required.");
            formIsValid = false;
        }
        if (serviceType.isEmpty()) {
            Toast.makeText(this, "Please select a service type.", Toast.LENGTH_SHORT).show();
            formIsValid = false;
        }

        if (formIsValid) {
            double price = Double.parseDouble(priceStr);
            Log.e("Check Data", "Data: " + serviceName + " : " + price + " : " + description + " : " + serviceType);
            healthcareService.setServiceName(serviceName);
            healthcareService.setPrice(price);
            healthcareService.setDescription(description);
            healthcareService.setServiceType(serviceType);

            if (healthcareService.bannerUri != null) {
                Util.startProgress(addserviceBinding.progress, "Saving Service...");
                uploadImage();
            } else {
                Util.startProgress(addserviceBinding.progress, "Saving Service...");
                saveService();
            }

        }
    }

    private void deleteService() {
        Util.showConfirm(this, "Delete", "Do your really want to delete this service?", (dialog, which) -> {
            // Implement delete
            Firebase.delete(healthcareService, HealthcareService.STORE, (task) -> {
                Util.startProgress(addserviceBinding.progress, "Deleting Service...");
                if (task.isSuccessful()) {
                    Util.notify(this, "Service deleted!");
                    Util.stopProgress(addserviceBinding.progress);
                    finish();
                }
            });
            dialog.dismiss();
        });
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        serviceImageLauncher.launch(intent);
    }

    private void uploadImage() {
        boolean exists = Util.exists(healthcareService);

        Firebase.uploadImageToFirebase(healthcareService.bannerUri, this, (uri) -> {
            healthcareService.setBannerUrl(uri.toString());
            Log.e("URI Check: ", uri.toString());
            saveService();
        }, (error) -> {
        });

        Util.stopProgress(addserviceBinding.progress);
    }
}