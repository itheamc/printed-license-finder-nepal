package com.itheamc.licensefinder.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.itheamc.licensefinder.R;
import com.itheamc.licensefinder.adapters.LicenseAdapter;
import com.itheamc.licensefinder.adapters.LicenseCallback;
import com.itheamc.licensefinder.databinding.FragmentResultsBinding;
import com.itheamc.licensefinder.models.License;
import com.itheamc.licensefinder.viewmodel.SharedViewModel;

import java.util.ArrayList;
import java.util.List;


public class ResultsFragment extends Fragment implements LicenseCallback, Filterable {
    private static final String TAG = "ResultsFragment";
    private FragmentResultsBinding resultsBinding;
    private SharedViewModel viewModel;
    private LicenseAdapter licenseAdapter;
    private NavController navController;


    public ResultsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        resultsBinding = FragmentResultsBinding.inflate(inflater, container, false);
        return resultsBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        //ViewModel initialization
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        licenseAdapter = new LicenseAdapter(this);
        resultsBinding.resultsRecyclerView.setAdapter(licenseAdapter);

        List<License> licenses = viewModel.getLicenseList();
        submitLicenses(licenses);
        loadAds();
    }

    // Function to submit list
    private void submitLicenses(List<License> licenses) {
        if (licenses != null && !licenses.isEmpty()) {
            if (resultsBinding.noresultLayout.getVisibility() == View.VISIBLE)
                resultsBinding.noresultLayout.setVisibility(View.GONE);
            licenseAdapter.submitList(licenses);
//            viewModel.setTempList(licenses);
        } else {
            licenseAdapter.submitList(licenses);
            resultsBinding.noresultLayout.setVisibility(View.VISIBLE);
        }
    }


    // Function to load ads
    private void loadAds() {
        if (getContext() == null) return;

        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
                Log.d(TAG, "onInitializationComplete: " + initializationStatus.toString());
            }
        });

        AdView mAdView = resultsBinding.bannerAdView;
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }


    @Override
    public void onClick(int position) {
        License license = viewModel.getTempList().get(position);
        viewModel.setLicense(license);
        navController.navigate(R.id.action_resultsFragment_to_detailsFragment);
    }


    // Function to hide the keyboard
    private void hideKeyboard() {

        View view = requireActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager manager = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getFilter().filter(query);
                hideKeyboard();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                getFilter().filter(newText);
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public Filter getFilter() {
        return filter;
    }


    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            List<License> licenses = new ArrayList<>();
            for (License l : viewModel.getLicenseList()) {
                if (l.get_name().toLowerCase().contains(constraint.toString().toLowerCase()) ||
                        l.get_license_no().contains(constraint) ||
                        l.get_issuer().toLowerCase().contains(constraint.toString().toLowerCase()) ||
                        String.valueOf(l.get_id()).contains(constraint)) {
                    licenses.add(l);
                }
            }

            filterResults.values = licenses;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<License> licenses = (List<License>) results.values;
            viewModel.setTempList(licenses);
            submitLicenses(licenses);

        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        resultsBinding = null;
    }

}