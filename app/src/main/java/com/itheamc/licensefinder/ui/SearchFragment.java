package com.itheamc.licensefinder.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.os.HandlerCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.itheamc.licensefinder.R;
import com.itheamc.licensefinder.api.NetworkCallback;
import com.itheamc.licensefinder.api.NetworkRequest;
import com.itheamc.licensefinder.databinding.FragmentSearchBinding;
import com.itheamc.licensefinder.models.License;
import com.itheamc.licensefinder.models.Query;
import com.itheamc.licensefinder.utils.NetworkUtil;
import com.itheamc.licensefinder.viewmodel.SharedViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SearchFragment extends Fragment implements NetworkCallback {
    private static final String TAG = "SearchFragment";
    private FragmentSearchBinding searchBinding;
    private SharedViewModel viewModel;
    private ExecutorService executorService;
    private Handler mainThreadHandler;
    private NavController navController;


    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        searchBinding = FragmentSearchBinding.inflate(inflater, container, false);
        return searchBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        //ViewModel initialization
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);


        // OnClickListener on findButton
        searchBinding.findButton.setOnClickListener(v -> {
            if (!searchBinding.editTextName.getText().toString().trim().isEmpty() ||
                    !searchBinding.editTextDlNo.getText().toString().trim().isEmpty()) {

                String name = searchBinding.editTextName.getText().toString().trim();
                String dlNo = searchBinding.editTextDlNo.getText().toString().trim();
                if (!name.isEmpty()) {
                    name = name.replace(" ", ",");
                    if ((name.split(",")).length == 2) {
                        name = name.replace(",", "  ");
                    } else {
                        name = name.replace(",", " ");
                    }
                }
                Query query = new Query(
                        name,
                        dlNo
                );

                hideKeyboard();
                if (NetworkUtil.isConnected(requireContext())) {
                    requestApi(query);
                    searchBinding.progressBar.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getContext(), "You don't have active network connection.", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getContext(), "Please input name or license number !!", Toast.LENGTH_LONG).show();
            }

        });
    }


    // Function to make api request
    // Will be called whenever find button clicked
    private void requestApi(Query query) {
        if (executorService == null) executorService = Executors.newFixedThreadPool(4);
        if (mainThreadHandler == null)
            mainThreadHandler = HandlerCompat.createAsync(Looper.getMainLooper());
        NetworkRequest networkRequest = NetworkRequest.getInstance(this, executorService, mainThreadHandler);
        networkRequest.fetchLicense(query);
    }


    // Overrided method from Network Callback
    @Override
    public void onSuccess(JSONArray jsonArray) {
        searchBinding.progressBar.setVisibility(View.GONE);
        createList(jsonArray);
    }

    @Override
    public void onFailure(String error) {
        searchBinding.progressBar.setVisibility(View.GONE);
        Toast.makeText(requireContext(), "Something went wrong!!", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onFailure: " + error);
    }

    // Function to convert json array to List<License> lists
    private void createList(JSONArray jsonArray) {
        List<License> licenseList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                License license = new License(
                        jsonObject.getInt("DLId"),
                        jsonObject.getString("Name"),
                        "No Image",
                        jsonObject.getString("DINo"),
                        jsonObject.getBoolean("IsApproved"),
                        jsonObject.getString("SentBranch"),
                        jsonObject.getString("Type"),
                        jsonObject.getString("Remarks")
                        );

                licenseList.add(license);
            } catch (JSONException e) {
                Log.e(TAG, "createList: ", e.getCause());
            }

        }

        viewModel.setLicenseList(licenseList);
        viewModel.setTempList(licenseList);
        // Navigate to result fragment
        navController.navigate(R.id.action_searchFragment_to_resultsFragment);
    }

    // Function to hide the keyboard
    private void hideKeyboard() {

        View view = requireActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager manager = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }

}