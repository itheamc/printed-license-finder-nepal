package com.itheamc.licensefinder.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.itheamc.licensefinder.R;
import com.itheamc.licensefinder.databinding.ActivityMainBinding;
import com.itheamc.licensefinder.viewmodel.SharedViewModel;

public class MainActivity extends AppCompatActivity implements NavController.OnDestinationChangedListener {
    private static final String TAG = "MainActivity";
    private ActivityMainBinding mainBinding;
    private NavController navController;
    private SharedViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());

        viewModel = new ViewModelProvider(this).get(SharedViewModel.class);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
            navController.addOnDestinationChangedListener(this);
            NavigationUI.setupActionBarWithNavController(this, navController);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        navController.navigateUp();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
        if (destination.getId() != R.id.webFragment) return;

        if (viewModel.getUrl().contains("disclaimer")) {
            destination.setLabel("Disclaimer");
        } else {
            destination.setLabel("Privacy Policy");
        }

    }
}