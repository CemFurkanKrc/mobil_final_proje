package com.example.mobil_final_proje.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mobil_final_proje.R;
import com.example.mobil_final_proje.model.LabelModel;
import com.example.mobil_final_proje.model.PostModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class AddPhotoFragment extends Fragment {

    private static final int CAMERA_REQUEST_CODE = 1;
    private ImageView addphtImg;
    private CollectionReference userLabelRefpht;
    private LinearLayout lblTargetLayoutpht;
    private Button btnCmr, btnload;
    private String USERİD;
    private Uri capturedPhotoUri;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_addphoto, container, false);

        addphtImg = root.findViewById(R.id.addpht_img);
        lblTargetLayoutpht = root.findViewById(R.id.pht_target_layout);
        btnCmr = root.findViewById(R.id.btn_cmr);
        btnload = root.findViewById(R.id.btn_load);


        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {

            String userId = currentUser.getUid();
            USERİD = userId;

            userLabelRefpht = FirebaseFirestore.getInstance().collection("LABELS").document(userId).collection(userId);
        }

        btnCmr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCamera();
            }
        });

        btnload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPhoto(capturedPhotoUri);
            }
        });

        displayUserLabels();

        return root;
    }

    private void uploadPhoto(Uri fileUri) {
        if (fileUri != null) {

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();

            String uniqueFileName = System.currentTimeMillis() + "_" + fileUri.getLastPathSegment();

            StorageReference userMediaRef = storageRef.child("MEDİA").child(USERİD).child(uniqueFileName);

            userMediaRef.putFile(fileUri)
                    .addOnSuccessListener(taskSnapshot -> {

                        userMediaRef.getDownloadUrl().addOnSuccessListener(uri -> {

                            String downloadUrl = uri.toString();

                            PostModel postModel = new PostModel(Uri.parse(downloadUrl));

                        });
                    })
                    .addOnFailureListener(e -> {
                    });
        }
    }
    private void displayUserLabels() {
        userLabelRefpht.get().addOnSuccessListener(queryDocumentSnapshots -> {
            ArrayList<LabelModel> userLabels = new ArrayList<>();
            for (LabelModel label : queryDocumentSnapshots.toObjects(LabelModel.class)) {
                userLabels.add(label);
            }
            for (LabelModel label : userLabels) {
                addLabelToLayout(label.getLabel());
            }
        });
    }
    private void addLabelToLayout(String labelName) {

        View labelCard = getLayoutInflater().inflate(R.layout.labelcardbuton, null);

        TextView checklabelname = labelCard.findViewById(R.id.checklabelname);
        checklabelname.setText(labelName);

        lblTargetLayoutpht.addView(labelCard);
    }
    private void startCamera() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                Bitmap photoBitmap = (Bitmap) extras.get("data");
                addphtImg.setImageBitmap(photoBitmap);
                capturedPhotoUri = saveBitmapToFile(photoBitmap);
            }
        }
    }
    private Uri saveBitmapToFile(Bitmap bitmap) {
        File file = new File(requireContext().getCacheDir(), "temp.jpg");
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Uri.fromFile(file);
    }
}
