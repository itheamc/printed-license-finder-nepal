package com.itheamc.licensefinder.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.itheamc.licensefinder.databinding.ResultViewBinding;
import com.itheamc.licensefinder.models.License;

import static com.itheamc.licensefinder.models.License.licenseItemCallback;

public class LicenseAdapter extends ListAdapter<License, LicenseAdapter.LicenseViewHolder> {
    private final LicenseCallback callback;

    public LicenseAdapter(@NonNull LicenseCallback callback) {
        super(licenseItemCallback);
        this.callback = callback;
    }

    @NonNull
    @Override
    public LicenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ResultViewBinding viewBinding = ResultViewBinding.inflate(inflater, parent, false);
        return new LicenseViewHolder(viewBinding, callback);
    }

    @Override
    public void onBindViewHolder(@NonNull LicenseViewHolder holder, int position) {
        License license = getItem(position);
        holder.viewBinding.setLicense(license);

    }

    public static class LicenseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ResultViewBinding viewBinding;
        private final LicenseCallback callback;

        public LicenseViewHolder(@NonNull ResultViewBinding resultViewBinding, LicenseCallback licenseCallback) {
            super(resultViewBinding.getRoot());
            this.viewBinding = resultViewBinding;
            this.callback = licenseCallback;
            viewBinding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            callback.onClick(getAdapterPosition());
        }
    }
}
