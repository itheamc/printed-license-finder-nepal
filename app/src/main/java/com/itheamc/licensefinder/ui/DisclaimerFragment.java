package com.itheamc.licensefinder.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.itheamc.licensefinder.databinding.FragmentDisclaimerBinding;
import com.itheamc.licensefinder.models.Disclaimer;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public class DisclaimerFragment extends Fragment {
    private static final String TAG = "DisclaimerFragment";
    private FragmentDisclaimerBinding disclaimerBinding;


    public DisclaimerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        disclaimerBinding = FragmentDisclaimerBinding.inflate(inflater, container, false);
        return disclaimerBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fetchData();
    }


    // Function to load data from the firestore database
    private void fetchData() {
        FirebaseFirestore.getInstance()
                .collection("disclaimers")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots != null) {
                        List<DocumentSnapshot> documentSnapshots = queryDocumentSnapshots.getDocuments();
                        if (documentSnapshots.size() > 0) {
                            Disclaimer disclaimer = documentSnapshots.get(0).toObject(Disclaimer.class);
                            disclaimerBinding.setDisclaimer(disclaimer);
                            Log.d(TAG, "onSuccess: "+ disclaimer);
                        }
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show());
    }
}