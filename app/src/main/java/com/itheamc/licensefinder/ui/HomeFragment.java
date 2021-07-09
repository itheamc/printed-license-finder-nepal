package com.itheamc.licensefinder.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import com.itheamc.licensefinder.utils.NetworkUtil;
import com.itheamc.licensefinder.viewmodel.SharedViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    private FragmentHomeBinding homeBinding;
    private NavController navController;
    private SharedViewModel viewModel;
    private boolean is_fetching = false;
    private int y = 0;
    private int m = 0;
    private int d = 0;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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

        homeBinding.navigateToSearch.setOnClickListener(this::fetchData);

        homeBinding.navigateToLicense.setOnClickListener(this::fetchData);

        homeBinding.disclaimer.setOnClickListener(v -> {
            viewModel.setUrl("https://sites.google.com/view/license-checker/disclaimer");
            navController.navigate(R.id.action_homeFragment_to_webFragment);
        });

        homeBinding.privacyPolicy.setOnClickListener(v -> {
            viewModel.setUrl("https://sites.google.com/view/license-checker/privacy-policy");
            navController.navigate(R.id.action_homeFragment_to_webFragment);
        });

    }


    // Function to load data from the firestore database
    private void fetchData(View view) {
        if (getContext() == null) {
            return;
        }

        if (!NetworkUtil.isConnected(getContext())) {
            Toast.makeText(getContext(), "No internet", Toast.LENGTH_SHORT).show();
            return;
        }
        if (is_fetching) {
            return;
        }
        if (viewModel.isFetched()) {
            navigate(view);
            return;
        }
        is_fetching = true;
        FirebaseFirestore.getInstance()
                .collection("myapp")
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

    /**
     * FUnction overrided to handle the action menu
     * @param menu --
     * @param inflater --
     */

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.share_action_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.share_menu) {
            handleShare();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Function to handle share
    private void handleShare() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Hey, Have you ever checked this app?");
        intent.putExtra(Intent.EXTRA_TEXT, "के तपाई स्मार्ट लाइसेन्सको पर्खाईमा हुनुहुन्छ।  येदि हुनुहुन्छ भने आफ्नो लाइसेन्स प्रिन्ट भए नभएको थाहपाउन र  आफ्नो डेमो स्मार्ट लाइसेन्स हेर्न यो एप download गर्नुहोस। --> https://play.google.com/store/apps/details?id=com.itheamc.licensefinder");
        startActivity(Intent.createChooser(intent, "Share License Checker With"));
    }
}