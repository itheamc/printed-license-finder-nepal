package com.itheamc.licensefinder.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.itheamc.licensefinder.R;
import com.itheamc.licensefinder.databinding.FragmentWebBinding;
import com.itheamc.licensefinder.utils.NetworkUtil;
import com.itheamc.licensefinder.viewmodel.SharedViewModel;

import org.jetbrains.annotations.NotNull;


public class WebFragment extends Fragment {
    private static final String TAG = "WebFragment";
    private FragmentWebBinding webBinding;
    private SharedViewModel viewModel;
    private NavController navController;


    public WebFragment() {
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
        webBinding = FragmentWebBinding.inflate(inflater, container, false);
        return webBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        if (NetworkUtil.isConnected(requireContext())) {
            webBinding.webView.loadUrl(viewModel.getUrl());
        } else {
            Toast.makeText(requireContext(), "Something went wrong!!", Toast.LENGTH_LONG).show();
        }
    }
}