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

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.apollo.medgift.R;
import com.apollo.medgift.common.BaseActivity;
import com.apollo.medgift.common.Firebase;
import com.apollo.medgift.common.Util;
import com.apollo.medgift.databinding.ActivityCreateserviceBinding;
import com.apollo.medgift.models.HealthcareService;
import com.apollo.medgift.models.Service;
import com.apollo.medgift.models.User;

public class CreateServiceActivity extends BaseActivity implements View.OnClickListener {

    ActivityCreateserviceBinding createserviceBinding;
    private HealthcareService healthcareService;
    private ActivityResultLauncher<Intent> serviceImageLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        createserviceBinding = ActivityCreateserviceBinding.inflate(getLayoutInflater());
        setContentView(createserviceBinding.getRoot());
        applyWindowInsetsListenerTo(this, createserviceBinding.main);

        Intent intent = getIntent();
        healthcareService = (HealthcareService) intent.getSerializableExtra(HealthcareService.STORE);

        boolean exists = Util.exists(healthcareService);
        String title = getString(exists ? R.string.editServiceTitle : R.string.createServiceTitle);
        setupToolbar(createserviceBinding.homeAppBar.getRoot(), title, true);
        if (!exists) {
            healthcareService.setCreatedBy(Firebase.currentUser().getUserId());
            healthcareService.setCreatedByName(Firebase.currentUser().getEmail());
        }
        setup();

        // initialize drop down
        initializeDropdown();

        // register image
        registerImageResult();

        createserviceBinding.addImageButton.setOnClickListener(v -> openGallery());
        createserviceBinding.btnCreateServiceSave.setOnClickListener(this);
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
        createserviceBinding.serviceImage.setImageURI(imageUri);
        createserviceBinding.serviceImage.setVisibility(View.VISIBLE); // Show the image
        createserviceBinding.addImageButtonText.setText(getString(R.string.change_image));
    }

    private void initializeDropdown() {
        String[] serviceTypes = getResources().getStringArray(R.array.service_types);

        // Set up the Spinner
        ArrayAdapter<String> adapterItems = new ArrayAdapter<String>(this, R.layout.servicetype_item, serviceTypes);
        createserviceBinding.autoCompleteTxt.setAdapter(adapterItems);
        createserviceBinding.autoCompleteTxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
    }

    private void setup() {
        createserviceBinding.edtServiceName.setText(healthcareService.getServiceName());
        createserviceBinding.edtServiceAssignee.setText(healthcareService.getProviderId());
        createserviceBinding.edtPrice.setText(String.valueOf(healthcareService.getPrice()));
        createserviceBinding.edtDescription.setText(healthcareService.getDescription());
        createserviceBinding.btnCreateServiceSave.setOnClickListener(this);

        // Restrict editing for "Gifter" role
        if (User.Role.GIFTER.name().equals(Firebase.currentUser().getUserRole())) {
            createserviceBinding.btnCreateServiceSave.setVisibility(View.GONE);
            createserviceBinding.edtServiceName.setEnabled(false);
            createserviceBinding.edtServiceAssignee.setEnabled(false);
            createserviceBinding.edtPrice.setEnabled(false);
            createserviceBinding.edtDescription.setEnabled(false);
        } else {
            // enable buttons for other roles
            createserviceBinding.btnCreateServiceSave.setVisibility(View.VISIBLE);
        }
    }

    private void clearErrors() {
        createserviceBinding.lytServiceName.setError("");
        createserviceBinding.lytServiceAssignee.setError("");
        createserviceBinding.lytPrice.setError("");
        createserviceBinding.lytType.setError("");
        createserviceBinding.lytDescription.setError("");
    }

    private void saveService() {
        boolean exists = Util.exists(healthcareService);
        Util.startProgress(createserviceBinding.progress, "Saving Service...");

        // Save service data in Firebase Database
        Firebase.save(healthcareService, Service.STORE, task -> {
            Util.stopProgress(createserviceBinding.progress);
            if (task.isSuccessful()) {
                Util.notify(CreateServiceActivity.this, Util.success("Service", exists));
                finish();
            } else {
                Util.notify(CreateServiceActivity.this, Util.fail("Service", exists));
            }
        });

        Util.stopProgress(createserviceBinding.progress);
    }

    @Override
    public void onClick(View v) {
        if (v == createserviceBinding.btnCreateServiceSave) {
            clearErrors();

            boolean formIsValid = true;
            String serviceName = Util.valueOf(createserviceBinding.edtServiceName);
            String serviceAssignee = Util.valueOf(createserviceBinding.edtServiceAssignee);
            String priceStr = Util.valueOf(createserviceBinding.edtPrice);
            String description = Util.valueOf(createserviceBinding.edtDescription);
            String serviceType = createserviceBinding.autoCompleteTxt.getText().toString();

            if (serviceName.isEmpty()) {
                createserviceBinding.lytServiceName.setError("Service name is required.");
                formIsValid = false;
            }
            if (serviceAssignee.isEmpty()) {
                createserviceBinding.lytServiceAssignee.setError("Service assignee is required.");
                formIsValid = false;
            }
            if (priceStr.isEmpty()) {
                createserviceBinding.lytPrice.setError("Price is required.");
                formIsValid = false;
            }
            if (description.isEmpty()) {
                createserviceBinding.lytDescription.setError("Description is required.");
                formIsValid = false;
            }
            if (serviceType.isEmpty()) {
                Toast.makeText(this, "Please select a service type.", Toast.LENGTH_SHORT).show();
                formIsValid = false;
            }

            if (formIsValid) {
                double price = Double.parseDouble(priceStr);
                Log.e("Check Data", "Data: " + serviceName + " : " + serviceAssignee + " : " + price + " : " + description + " : " + serviceType + " : " + healthcareService.getBannerUrl());
                healthcareService.setServiceName(serviceName);
                healthcareService.setProviderId(serviceAssignee);
                healthcareService.setPrice(price);
                healthcareService.setDescription(description);
                healthcareService.setServiceType(serviceType);

                if (healthcareService.bannerUri != null) {
                    Util.startProgress(createserviceBinding.progress, "Saving Service...");
                    uploadImage();
                } else {
                    Util.startProgress(createserviceBinding.progress, "Saving Service...");
                    saveService();
                }

            }
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        serviceImageLauncher.launch(intent);
    }

    private void uploadImage() {
        boolean exists = Util.exists(healthcareService);

        Firebase.uploadImageToFirebase(healthcareService.bannerUri, this, (uri) -> {
            healthcareService.setBannerUrl(uri.toString());
            saveService();
            Util.notify(CreateServiceActivity.this, Util.success("Health Tip", exists));
        }, (error) -> {
            Util.notify(CreateServiceActivity.this, Util.fail("Health Tip", exists));
        });

        Util.stopProgress(createserviceBinding.progress);
    }
}