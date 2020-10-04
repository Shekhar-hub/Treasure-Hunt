package com.sk.textrecog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.languageid.FirebaseLanguageIdentification;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;
    private static final int IMAGE_PICK_GALLERY_CODE = 300;
    private static final int IMAGE_PICK_CAMERA_CODE = 400;

    private EditText mresultEt;
    private ImageView mPreviewIv;
    private TextView mSourceLang;
    private  int langCode,targetLangCode;

    private String[] cameraPermissions;
    private String[] storagePermission;
    private Uri imageUri = null;
    private Spinner spinner;
    private static final String[] paths = {"---Select---","English", "Hindi"};
    private String sourceText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FirebaseApp.initializeApp(this);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setSubtitle("Click image button to insert image");
        mresultEt = findViewById(R.id.resultEt);
        mPreviewIv = findViewById(R.id.imageIv);
        mSourceLang=findViewById(R.id.sourceLang);

        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        spinner = (Spinner)findViewById(R.id.LangSpin);
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_spinner_item,paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        // Whatever you want to happen when the first item gets selected
                        break;

                    case 1:
                        // Whatever you want to happen when the second item gets selected
                        targetLangCode=FirebaseTranslateLanguage.EN;
                        translateText(langCode,targetLangCode,"English");
                        break;
                    case 2:
                        // Whatever you want to happen when the thrid item gets selected
                        targetLangCode=FirebaseTranslateLanguage.HI;
                        translateText(langCode,targetLangCode,"Hindi");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.addImage) {
            showImageImportDialog();
        }
        if (id == R.id.settings) {
            Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && writeStorageAccepted) {
                        pickFromCamera();
                    } else {
                        Toast.makeText(this, "Please allow camera and storage permission...", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case STORAGE_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean writeStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (writeStorageAccepted) {
                        pickFromGallery();
                    } else {
                        Toast.makeText(this, "Please allow storage permission...", Toast.LENGTH_SHORT).show();
                    }


                }
                break;
        }
    }


    private void showImageImportDialog() {
        String items[] = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Image from");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    if (!checkCameraPermission()) {
                        requestCameraPermission();
                    } else {
                        pickFromCamera();
                    }

                } else if (which == 1) {

                    if (!checkStoragePermission()) {
                        requestStoragePermission();
                    } else {
                        pickFromGallery();
                    }
                }
            }
        });
        builder.create().show();
    }

    private void pickFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, IMAGE_PICK_GALLERY_CODE);
    }

    private void pickFromCamera() {

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Pic");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image To Text");
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);

    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        FirebaseApp.initializeApp(this);
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_CAMERA_CODE) {

                CropImage.activity(imageUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);

            }
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                imageUri = data.getData();
                CropImage.activity(imageUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);

            }
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                FirebaseApp.initializeApp(this);
                Uri resultUri = result.getUri();
                mPreviewIv.setImageURI(resultUri);

                spinner.setVisibility(View.GONE);
                try {
                    FirebaseVisionImage image = FirebaseVisionImage
                            .fromFilePath(MainActivity.this, resultUri);
                    FirebaseVisionTextRecognizer recognizer = FirebaseVision.getInstance().getOnDeviceTextRecognizer();
                    recognizer.processImage(image).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                        @Override
                        public void onSuccess(FirebaseVisionText firebaseVisionText) {
                            String text = firebaseVisionText.getText();
                            mresultEt.setText("");
                            for (FirebaseVisionText.TextBlock block : firebaseVisionText.getTextBlocks()) {
                                mresultEt.append("\n\n" + block.getText());
                            }

                            identifyLanguage();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }
    }

    private void identifyLanguage() {
        sourceText=mresultEt.getText().toString();
        FirebaseLanguageIdentification identifier= FirebaseNaturalLanguage.getInstance()
                .getLanguageIdentification();

        mSourceLang.setText("Detecting...");
        identifier.identifyLanguage(sourceText).addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String s) {

                if (s.equals("und"))
                {
                    Toast.makeText(MainActivity.this, "Language not identified...", Toast.LENGTH_SHORT).show();

                }
                else
                {

                    getLanguageCode(s);

                }
            }
        });

    }

    private void getLanguageCode(String language) {

        switch(language)
        {
            case "en":
                langCode= FirebaseTranslateLanguage.EN;
                mSourceLang.setText("English");
                break;
            case "hi":
                langCode= FirebaseTranslateLanguage.HI;
                mSourceLang.setText("Hindi");
                break;

            case "ar":
                langCode=FirebaseTranslateLanguage.AR;
                mSourceLang.setText("Arabic");
                break;
            default:
                langCode=0;
        }
       spinner.setVisibility(View.VISIBLE);
    }

    private void translateText(int langCode, final int targetLangCode, final String targetLang) {
        Log.i("j",targetLangCode+"");
        mSourceLang.setText("Translating...");
        FirebaseTranslatorOptions options=new FirebaseTranslatorOptions.Builder()
                .setSourceLanguage(langCode)
                .setTargetLanguage(targetLangCode)
                .build();
        final FirebaseTranslator translator=FirebaseNaturalLanguage.getInstance()
                .getTranslator(options);

        FirebaseModelDownloadConditions conditions=new FirebaseModelDownloadConditions.Builder()
                .build();

        translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                translator.translate(sourceText).addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {

                       mresultEt.setText(s);
                        mSourceLang.setText(targetLang);
                    }
                });
            }
        });
    }

    }
