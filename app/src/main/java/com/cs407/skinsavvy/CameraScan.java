package com.cs407.skinsavvy;

//imports
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
import com.theartofdev.edmodo.cropper.CropImage;


public class CameraScan extends AppCompatActivity {

    //activity main xml variables
    private Button cameraButton;
    private Button scanButton;
    private ShapeableImageView imageHolder;
//    private EditText recognizedText;

    //image URI var and accessing permissions
    private Uri imageUri = null;
    private static final int IMAGE_CAPTURE_CODE = 20;
    private String[] cameraPermissions;
    private static final int GALLERY_SEARCH_CODE = 21;
    private String[] galleryPermissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_scan);

        //initializations
        cameraButton = findViewById(R.id.photoButton);
        scanButton = findViewById(R.id.scanButton);
        imageHolder = findViewById(R.id.image);
//        recognizedText = findViewById(R.id.recognizedText);

        //permissions arrays
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        galleryPermissions = new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE};

        //if click photo button, it should let you take a pic or grab one from gallery
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopUpOptions();
            }
        });
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUri == null) {
                    //can't scan an empty image, also if image is not replaced it will scan old one
                    Toast.makeText(CameraScan.this, "Please Take or Choose an Image First!", Toast.LENGTH_SHORT).show();
                }
                else{
                    onText();
                }
            }
        });

    }
    private void showPopUpOptions() {
        PopupMenu popupMenu = new PopupMenu(this, cameraButton);
        popupMenu.getMenu().add(Menu.NONE,1,1,"CAMERA");
        popupMenu.getMenu().add(Menu.NONE,2,2,"GALLERY");

        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                //camera opening
                if (id == 1) {
                    if (checkCameraPermission()){
                        picFromCamera();
                    }
                    else{
                        reqCameraPermission();
                    }
                }
                //gallery opening
                else if (id == 2) {
                    if (checkExtStoragePermission()){
                        picFromGallery();
                    }
                    else{
                        reqExtStoragePermission();
                    }
                }
                return true;
            }
        });

    }

    //Going to gallery (have to set an intent)
    private void picFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galleryARL.launch(intent);

    }

    //ActivityResultLauncher for Gallery
    private ActivityResultLauncher<Intent> galleryARL = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            imageUri = result.getData().getData();
                            CropImage.activity(imageUri)
                                    .start(CameraScan.this);
                        }
                        else{
                            Toast.makeText(CameraScan.this, "Data Null Gallery", Toast.LENGTH_SHORT).show();

                        }

                    } else {
                        Toast.makeText(CameraScan.this, "Failed Gallery", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    //Using Camera (have to set an intent)
    private void picFromCamera() {
        ContentValues values= new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Sample Title");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Sample Desc");

        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent intent = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        cameraARL.launch(intent);
    }
    //ActivityResultLauncher for Camera
    private ActivityResultLauncher<Intent> cameraARL = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        CropImage.activity(imageUri)
                                .start(CameraScan.this);
                    } else {
                        Toast.makeText(CameraScan.this, "Failed Camera", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    //checking and requesting storage permissions
    private boolean checkExtStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }
    private void reqExtStoragePermission() {
        ActivityCompat.requestPermissions(this, galleryPermissions,GALLERY_SEARCH_CODE);
    }

    //checking and requesting camera permissions
    private boolean checkCameraPermission() {
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return result1 && result2;
    }
    private void reqCameraPermission() {
        ActivityCompat.requestPermissions(this, cameraPermissions,IMAGE_CAPTURE_CODE);
    }

    //converting image to text (OCR operation)
    public void onText() {
        Toast.makeText(this, "Image Processing", Toast.LENGTH_SHORT).show();

        try {
            InputImage image = InputImage.fromFilePath(this, imageUri);
            Toast.makeText(this, "Text Processing", Toast.LENGTH_SHORT).show();
            TextRecognizerOptions options = TextRecognizerOptions.DEFAULT_OPTIONS;
            TextRecognizer recognizer = TextRecognition.getClient(options);
            Task<Text> textResult = recognizer.process(image)
                    .addOnSuccessListener(new OnSuccessListener<Text>() {
                        @Override
                        public void onSuccess(Text text) {
                            String textRecognized = text.getText();
//                            recognizedText.setText(textRecognized);
                            int index = textRecognized.indexOf(':');

                            String result = textRecognized.substring(index + 1);
                            Intent intent = new Intent(CameraScan.this, ManualSearch.class);
                            intent.putExtra("source", "CameraClass");
                            intent.putExtra("ingredients",result);
                            startActivity(intent);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(CameraScan.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case IMAGE_CAPTURE_CODE:
                if (grantResults.length > 0)
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                        picFromCamera();
                    } else {
                        Toast.makeText(this, "Camera / Storage Permissions Needed", Toast.LENGTH_SHORT).show();
                    }
                break;
            case GALLERY_SEARCH_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    picFromGallery();
                }
                else {
                    Toast.makeText(this, "Storage Permissions Needed", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                imageHolder.setImageURI(resultUri);
                imageUri = resultUri;
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}

