package com.example.mobil_final_proje.model;

import android.net.Uri;

public class PostModel {
    private Uri postUri;
    public PostModel() {}
    public PostModel(Uri postUri) {
        this.postUri = postUri;
    }
    public Uri getPostUri() {
        return postUri;
    }
    public void setPostUri(Uri postUri) {
        this.postUri = postUri;
    }
}
