package com.apollo.medgift.views.provider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
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
import com.apollo.medgift.models.Service;
import com.apollo.medgift.models.User;

import java.io.ByteArrayOutputStream;

public class CreateServiceActivity extends BaseActivity implements View.OnClickListener {

    ActivityCreateserviceBinding createserviceBinding;
    private Service service;
    private ActivityResultLauncher<Intent> serviceImageLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        createserviceBinding = ActivityCreateserviceBinding.inflate(getLayoutInflater());
        setContentView(createserviceBinding.getRoot());
        applyWindowInsetsListenerTo(this, createserviceBinding.main);

        Intent intent = getIntent();
        service = (Service) intent.getSerializableExtra(Service.STORE);

        boolean exists = Util.exists(service);
        String title = getString(exists ? R.string.editServiceTitle : R.string.createServiceTitle);
        setupToolbar(createserviceBinding.homeAppBar.getRoot(), title, true);
        if (!exists) {
            service.setCreatedBy(Firebase.currentUser().getUserId());
            service.setCreatedByName(Firebase.currentUser().getEmail());
        }
        setup();

        // initialize drop down
        initializeDropdown();

        // register image
        registerImageResult();

        createserviceBinding.addImageButton.setOnClickListener(v -> openGallery());
        createserviceBinding.btnCreateServiceSave.setOnClickListener(v -> saveService());
    }

    // Register activity result and load image from gallery
    private void registerImageResult() {
        serviceImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        if (imageUri != null) {
                            loadImage(imageUri);
                        } else {
                            Toast.makeText(this, getString(R.string.no_image_selected), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }

    // load the selected image
    private void loadImage(Uri imageUri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);

            // Reduce file size
            int targetWidth = 800;
            int targetHeight = (int) ((double) bitmap.getHeight() / bitmap.getWidth() * targetWidth);
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, true);

            // Compress the bitmap
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 75, byteArrayOutputStream); // Compress with 75% quality

            // Convert to smaller bitmap
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            Bitmap compressedBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

            createserviceBinding.serviceImage.setImageBitmap(compressedBitmap);
            createserviceBinding.serviceImage.setVisibility(View.VISIBLE); // Show the image
            createserviceBinding.addImageButtonText.setText(getString(R.string.change_image));
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, getString(R.string.failed_to_load_image_error), Toast.LENGTH_SHORT).show();
        }
    }

    private void initializeDropdown() {
        String[] serviceTypes = getResources().getStringArray(R.array.service_types);

        // Set up the Spinner
        ArrayAdapter<String> adapterItems = new ArrayAdapter<String>(this, R.layout.servicetype_item, serviceTypes);
        createserviceBinding.autoCompleteTxt.setAdapter(adapterItems);
        createserviceBinding.autoCompleteTxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //IMPLEMENT FUNCTIONS HERE
                String selectedItem = parent.getItemAtPosition(position).toString();
                Toast.makeText(CreateServiceActivity.this, "Item: " + selectedItem, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setup() {
        createserviceBinding.edtServiceName.setText(service.getServiceName());
        createserviceBinding.edtServiceAssignee.setText(service.getServiceAssignee());
        createserviceBinding.edtHospital.setText(service.getHospital());
        createserviceBinding.edtPrice.setText(String.valueOf(service.getPrice()));
        createserviceBinding.edtDescription.setText(service.getDescription());
        createserviceBinding.btnCreateServiceSave.setOnClickListener(this);

        // Restrict editing for "Gifter" role
        if (User.Role.GIFTER.name().equals(Firebase.currentUser().getUserRole())) {
            createserviceBinding.btnCreateServiceSave.setVisibility(View.GONE);
            createserviceBinding.edtServiceName.setEnabled(false);
            createserviceBinding.edtServiceAssignee.setEnabled(false);
            createserviceBinding.edtHospital.setEnabled(false);
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
        createserviceBinding.lytHospital.setError("");
        createserviceBinding.lytPrice.setError("");
        createserviceBinding.lytDescription.setError("");
    }

    private void saveService() {
        clearErrors();

        boolean formIsValid = true;
        String serviceName = Util.valueOf(createserviceBinding.edtServiceName);
        String serviceAssignee = Util.valueOf(createserviceBinding.edtServiceAssignee);
        String hospital = Util.valueOf(createserviceBinding.edtHospital);
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
        if (hospital.isEmpty()) {
            createserviceBinding.lytHospital.setError("Hospital is required.");
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

        // Validate Service Image
        Bitmap serviceImageBitmap = ((BitmapDrawable) createserviceBinding.serviceImage.getDrawable()).getBitmap();
        if (serviceImageBitmap == null) {
            Toast.makeText(this, "Please upload a service image.", Toast.LENGTH_SHORT).show();
            formIsValid = false;
        }

        if (formIsValid) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            serviceImageBitmap.compress(Bitmap.CompressFormat.JPEG, 75, byteArrayOutputStream);
            byte[] imageBytes = byteArrayOutputStream.toByteArray();

            double price = Double.parseDouble(priceStr);
            Log.e("Check Data", "Data: "+serviceName+" : "+serviceAssignee+" : "+hospital+" : "+price+" : "+description+" : "+serviceType+" : "+imageBytes);
            service.setServiceName(serviceName);
            service.setServiceAssignee(serviceAssignee);
            service.setHospital(hospital);
            service.setPrice(price);
            service.setDescription(description);
            service.setServiceType(serviceType);
            service.setImage(imageBytes);

            boolean exists = Util.exists(service);
            Util.startProgress(createserviceBinding.progress, "Saving Service...");

            Firebase.save(service, Service.STORE, task -> {
                Util.stopProgress(createserviceBinding.progress);
                if (task.isSuccessful()) {
                    Util.notify(CreateServiceActivity.this, Util.success("Service", exists));
                    finish();
                } else {
                    Util.notify(CreateServiceActivity.this, Util.fail("Service", exists));
                }
            });
        }
    }

    @Override
    public void onClick(View v) {

    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        serviceImageLauncher.launch(intent);
    }
}