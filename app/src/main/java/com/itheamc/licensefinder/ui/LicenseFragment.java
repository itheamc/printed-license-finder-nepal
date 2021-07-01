package com.itheamc.licensefinder.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.itheamc.licensefinder.R;
import com.itheamc.licensefinder.databinding.FragmentLicenseBinding;
import com.itheamc.licensefinder.viewmodel.SharedViewModel;


public class LicenseFragment extends Fragment {
    private static final String TAG = "LicenseFragment";
    private FragmentLicenseBinding licenseBinding;
    private NavController navController;
    private SharedViewModel viewModel;

    public LicenseFragment() {
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
        licenseBinding = FragmentLicenseBinding.inflate(inflater, container, false);
        return licenseBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        //ViewModel initialization
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        licenseBinding.generateDemoButton.setOnClickListener(v -> handleGenerateButton());

    }

    // Function to handle generate button click
    private void handleGenerateButton() {
        String licNo = licenseBinding.editTextDlcNo.getText().toString();
        String dob = licenseBinding.editTextBirthDate.getText().toString();

        if (licNo.trim().isEmpty() && dob.trim().isEmpty()) {
            licenseBinding.editTextDlcNo.setError("Please provide your License Number");
            licenseBinding.editTextBirthDate.setError("Please provide your Birth Date");
            return;
        }

        if (licNo.trim().isEmpty()) {
            licenseBinding.editTextDlcNo.setError("Please provide your License Number");
            return;
        }

        if (dob.trim().isEmpty()) {
            licenseBinding.editTextBirthDate.setError("Please provide your Birth Date");
            return;
        }

        if (licNo.length() < 14 && dob.length() < 8) {
            licenseBinding.editTextDlcNo.setError("Please correct your License Number");
            licenseBinding.editTextBirthDate.setError("Please correct your Birth Date");
            return;
        }

        if (licNo.length() < 14) {
            licenseBinding.editTextDlcNo.setError("Please correct your License Number");
            return;
        }

        if (dob.length() < 8) {
            licenseBinding.editTextBirthDate.setError("Please correct your Birth Date");
            return;
        }

        // Splitting the string
        String[] splitsLicNo = licNo.split("-");
        String[] splitsDob = licNo.split("-");

        if (splitsLicNo.length < 3 && splitsDob.length < 3) {
            licenseBinding.editTextDlcNo.setError("Please correct your License Number");
            licenseBinding.editTextBirthDate.setError("Please correct your Birth Date");
            return;
        }

        if (splitsLicNo.length < 3) {
            licenseBinding.editTextDlcNo.setError("Please correct your License Number");
            return;
        }

        if (splitsDob.length < 3) {
            licenseBinding.editTextBirthDate.setError("Please correct your Birth Date");
            return;
        }



        viewModel.setLcNo(licNo);
        viewModel.setBirthDate(dob);

        navController.navigate(R.id.action_licenseFragment_to_demoFragment);
    }




}