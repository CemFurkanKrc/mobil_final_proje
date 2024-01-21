package com.example.mobil_final_proje.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.mobil_final_proje.R;
import com.example.mobil_final_proje.model.LabelModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;

public class AddLabelFragment extends Fragment {
    private EditText etxLabel;
    private LinearLayout lblTargetLayout;
    private CollectionReference userLabelRef;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_addlabel, container, false);

        etxLabel = root.findViewById(R.id.etx_lbl);
        Button btnAddLabel = root.findViewById(R.id.btn_adlbl);
        lblTargetLayout = root.findViewById(R.id.lbl_target_layout);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {

            String userId = currentUser.getUid();

            userLabelRef = FirebaseFirestore.getInstance().collection("LABELS").document(userId).collection(userId);
        }
        btnAddLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewLabel();
            }
        });
        displayUserLabels();
        return root;
    }
    private void displayUserLabels(){
        userLabelRef.get().addOnSuccessListener(queryDocumentSnapshots ->{
            ArrayList<LabelModel> userLabels = new ArrayList<>();
            for (LabelModel label : queryDocumentSnapshots.toObjects(LabelModel.class)) {
                userLabels.add(label);
            }
            for (LabelModel label : userLabels) {
                addLabelToLayout(label.getLabel());
            }
        });
    }
    private void addNewLabel() {
        String labelName = etxLabel.getText().toString().trim();

        if (!labelName.isEmpty()) {

            LabelModel label = new LabelModel(labelName);
            userLabelRef.add(label);

            addLabelToLayout(labelName);

            etxLabel.setText("");
        }
    }
    private void addLabelToLayout(String labelName) {

        View labelCard = getLayoutInflater().inflate(R.layout.labelcard, null);

        TextView addedLabelName = labelCard.findViewById(R.id.addedlabelname);
        addedLabelName.setText(labelName);

        lblTargetLayout.addView(labelCard);
    }
}
