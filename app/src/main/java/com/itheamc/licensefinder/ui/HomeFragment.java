package com.itheamc.licensefinder.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.itheamc.licensefinder.R;
import com.itheamc.licensefinder.databinding.FragmentHomeBinding;
import com.itheamc.licensefinder.viewmodel.SharedViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    private FragmentHomeBinding homeBinding;
    private NavController navController;
    private FirebaseFirestore firestore;
    private SharedViewModel viewModel;
    private boolean is_fetching = false;
    private int y = 0;
    private int m = 0;
    private int d = 0;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        homeBinding = FragmentHomeBinding.inflate(inflater, container, false);
        return homeBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        navController = Navigation.findNavController(view);
        firestore = FirebaseFirestore.getInstance();

        homeBinding.navigateToSearch.setOnClickListener(this::fetchData);

        homeBinding.navigateToLicense.setOnClickListener(this::fetchData);

        homeBinding.disclaimer.setOnClickListener(v -> navController.navigate(R.id.action_homeFragment_to_disclaimerFragment));

    }



    // Function to load data from the firestore database
    private void fetchData(View view) {
        if (is_fetching) {
            return;
        }
        if (viewModel.isFetched()) {
            navigate(view);
            return;
        }
        is_fetching = true;
        firestore.collection("myapp")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        is_fetching = false;
                        if (queryDocumentSnapshots != null) {
                            List<DocumentSnapshot> documentSnapshots = queryDocumentSnapshots.getDocuments();
                            if (documentSnapshots.size() > 0) {
                                viewModel.setActive(documentSnapshots.get(0).getBoolean("is_active"));
                                viewModel.setFetched(true);
                                navigate(view);
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        is_fetching = false;
                        if (view.getId() == homeBinding.navigateToSearch.getId()) {
                            navController.navigate(R.id.action_homeFragment_to_searchFragment);
                        } else {
                            navController.navigate(R.id.action_homeFragment_to_licenseFragment);
                        }
                        Log.d(TAG, "onFailure: " + e.getMessage());
                    }
                });
    }

    // Function to navigate to the another fragment as per the user click
    private void navigate(View view) {
        if (viewModel.isActive()) {
            if (view.getId() == homeBinding.navigateToSearch.getId()) {
                navController.navigate(R.id.action_homeFragment_to_searchFragment);
            } else {
                navController.navigate(R.id.action_homeFragment_to_licenseFragment);
            }
        } else {
            navController.navigate(R.id.action_homeFragment_to_noticeFragment);
        }
    }
}