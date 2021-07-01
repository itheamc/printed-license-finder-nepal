package com.itheamc.licensefinder.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.itheamc.licensefinder.databinding.FragmentDetailsBinding;
import com.itheamc.licensefinder.viewmodel.SharedViewModel;

public class DetailsFragment extends Fragment {
    private static final String TAG = "DetailsFragment";
    private FragmentDetailsBinding detailsBinding;
    private SharedViewModel viewModel;


    public DetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        detailsBinding = FragmentDetailsBinding.inflate(inflater, container, false);
        return detailsBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        detailsBinding.setLicense(viewModel.getLicense());
    }
}