package com.itheamc.licensefinder.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.itheamc.licensefinder.R;
import com.itheamc.licensefinder.databinding.DatePickerViewBinding;
import com.itheamc.licensefinder.databinding.FragmentHomeBinding;
import com.itheamc.licensefinder.utils.StorageUtility;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;


public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    private FragmentHomeBinding homeBinding;
    private NavController navController;
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

        navController = Navigation.findNavController(view);


        homeBinding.navigateToSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_homeFragment_to_searchFragment);
            }
        });


        if (StorageUtility.getBirthDate(requireActivity()) == null) {
            showDatePicker();
        }
    }


    // Function to create custom dialog to edit wager amount
    public void showDatePicker() {
        // add listener to button
        final Dialog dialog = new Dialog(getContext());   // Create custom dialog object
        DatePickerViewBinding pickerViewBinding = DatePickerViewBinding.inflate(getLayoutInflater());
        dialog.setContentView(pickerViewBinding.getRoot());
        dialog.show();


        y = pickerViewBinding.datePicker.getYear();
        m = pickerViewBinding.datePicker.getMonth() + 1;
        d = pickerViewBinding.datePicker.getDayOfMonth();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            pickerViewBinding.datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    y = year;
                    m = monthOfYear + 1;
                    d = dayOfMonth;
                    Log.d(TAG, "onDateChanged: " + y + "-" + m + "-" + d);
                }
            });
        }

        pickerViewBinding.confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StorageUtility.setBirthDate(requireActivity(), y, m, d);
                dialog.dismiss();
            }
        });

        pickerViewBinding.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }
}