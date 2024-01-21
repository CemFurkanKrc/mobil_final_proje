package com.example.mobil_final_proje.ui;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.mobil_final_proje.R;
import com.example.mobil_final_proje.model.PostModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class GalleryFragment extends Fragment {

    private LinearLayout galleryTargetLayout;
    private StorageReference userMediaRefpht;
    String USERİD;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        galleryTargetLayout = root.findViewById(R.id.gallerytargetlayout);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {

            String userId = currentUser.getUid();
            USERİD = userId;

            userMediaRefpht = FirebaseStorage.getInstance().getReference().child("MEDİA").child(USERİD);
        }
        displayUserPhoto();

        return root;

    }
    private void displayUserPhoto() {
        userMediaRefpht.listAll().addOnSuccessListener(listResult -> {
            ArrayList<PostModel> userpost = new ArrayList<>();
            for (StorageReference item : listResult.getItems()) {
                // Her bir dosyanın download URL'sini almak için getDownloadUrl metodu kullanılır
                item.getDownloadUrl().addOnSuccessListener(uri -> {
                    PostModel postModel = new PostModel(uri);
                    userpost.add(postModel);
                    addPhotoToLayout(postModel);
                });
            }
        });
    }

    private void addPhotoToLayout(PostModel postModel) {
        View galleryCard = getLayoutInflater().inflate(R.layout.gallerycard, null);

        ImageView imageView = galleryCard.findViewById(R.id.gallery_img);

        // Picasso kütüphanesi ile resmi yükle
        Picasso.get()
                .load(postModel.getPostUri())
                .into(imageView);

        galleryTargetLayout.addView(galleryCard);
    }
}
