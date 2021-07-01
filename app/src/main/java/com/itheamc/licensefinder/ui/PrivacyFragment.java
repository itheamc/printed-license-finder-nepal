package com.itheamc.licensefinder.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.itheamc.licensefinder.R;
import com.itheamc.licensefinder.databinding.FragmentPrivacyBinding;
import com.itheamc.licensefinder.models.Disclaimer;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public class PrivacyFragment extends Fragment {
    private static final String TAG = "PrivacyFragment";
    private FragmentPrivacyBinding privacyBinding;


    public PrivacyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        privacyBinding = FragmentPrivacyBinding.inflate(inflater, container, false);
        return privacyBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fetchData();
    }


    // Function to load data from the firestore database
    private void fetchData() {
        FirebaseFirestore.getInstance()
                .collection("privacy_policy")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots != null) {
                        List<DocumentSnapshot> documentSnapshots = queryDocumentSnapshots.getDocuments();
                        if (documentSnapshots.size() > 0) {
                            String privacy = documentSnapshots.get(0).getString("privacy");
                            assert privacy != null;
                            privacyBinding.privacyWebView.loadData(privacy, "text/html; charset=utf-8", "UTF-8");
                        }
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show());
    }
}