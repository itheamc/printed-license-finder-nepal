package com.itheamc.licensefinder.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.itheamc.licensefinder.databinding.FragmentNoticeBinding;
import com.itheamc.licensefinder.models.Notice;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public class NoticeFragment extends Fragment {
    private static final String TAG = "NoticeFragment";
    private FragmentNoticeBinding noticeBinding;
    private FirebaseFirestore firestore;


    public NoticeFragment() {
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
        noticeBinding = FragmentNoticeBinding.inflate(inflater, container, false);
        return noticeBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firestore = FirebaseFirestore.getInstance();

        fetchData();
    }




    // Function to load data from the firestore database
    private void fetchData() {
        firestore.collection("notices")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots != null) {
                            List<DocumentSnapshot> documentSnapshots = queryDocumentSnapshots.getDocuments();
                            if (documentSnapshots.size() > 0) {
                                Notice notice = documentSnapshots.get(0).toObject(Notice.class);
                                if (noticeBinding != null) noticeBinding.setNotice(notice);
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        if (getContext() != null) Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        noticeBinding = null;
    }
}