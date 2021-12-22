package com.crazyiter.nanonetprovicerapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.crazyiter.nanonetprovicerapp.db.Provider;
import com.crazyiter.nanonetprovicerapp.db.ServiceManager;
import com.crazyiter.nanonetprovicerapp.db.ServiceModel;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.UUID;

public class AddServiceActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 111;
    private static final int PERMISSION_REQUEST_CODE = 110;
    private Provider provider;
    private CardView rootCV;
    private TextInputLayout nameTIL;
    private TextInputLayout urlTIL;
    private ImageView serviceIV;
    private ImageView serviceIV2;
    private Uri selectedImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service);

        provider = Provider.getShared(this);

        rootCV = findViewById(R.id.rootCV);
        nameTIL = findViewById(R.id.nameTIL);
        urlTIL = findViewById(R.id.urlTIL);
        serviceIV = findViewById(R.id.serviceIV);
        serviceIV2 = findViewById(R.id.service2IV);

        serviceIV.setOnClickListener(v -> chooseImageRequest());

        Button addBTN = findViewById(R.id.addBTN);
        addBTN.setOnClickListener(v -> {
            String name = nameTIL.getEditText().getText().toString().trim();
            String url = urlTIL.getEditText().getText().toString().trim();

            if (name.isEmpty()) {
                Statics.showSnackBar(rootCV, getString(R.string.enter_name));
                return;
            }

            if (url.isEmpty()) {
                Statics.showSnackBar(rootCV, getString(R.string.enter_url));
                return;
            }

            if (selectedImagePath == null) {
                Statics.showSnackBar(rootCV, getString(R.string.choose_image));
                return;
            }

            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
            StorageReference storageReference = firebaseStorage.getReference();

            LoadingDialog loadingDialog = new LoadingDialog(this);
            loadingDialog.show();
            StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
            ref.putFile(selectedImagePath)
                    .addOnSuccessListener(taskSnapshot -> ref.getDownloadUrl().addOnSuccessListener(imageUri -> {
                        loadingDialog.dismiss();
                        ServiceModel serviceModel = new ServiceModel("", name, url, imageUri.toString(), provider.getId(), Statics.LocalDate.getCurrentDate());
                        ServiceManager.addService(serviceModel);
                        finish();
                    }))
                    .addOnFailureListener(e -> {
                        loadingDialog.dismiss();
                        Statics.showSnackBar(rootCV, getString(R.string.try_again));
                    });

        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            chooseImageRequest();
        }
    }

    private void chooseImageRequest() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                intent,
                PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            try {
                selectedImagePath = data.getData();
                Bitmap selectedImage = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImagePath);
                serviceIV.setImageBitmap(selectedImage);
                serviceIV2.setVisibility(View.GONE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}