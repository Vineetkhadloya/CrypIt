package com.android.crypto;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class YourFiles extends Fragment {
    private static final String TAG = "ViewFilesFragment";

    private FirebaseFirestore firestoreDB;
    private String userId;
    private FirebaseUser user;


    private RecyclerView filesRecyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //userId = getArguments().getString("userId");
        user = FirebaseAuth.getInstance().getCurrentUser();
        userId=user.getUid();
        View view = inflater.inflate(R.layout.fragment_your_files,
                container, false);

        firestoreDB = FirebaseFirestore.getInstance();

        filesRecyclerView = (RecyclerView) view.findViewById(R.id.files_list);

        getFileNamesFromFirestoreDb();

        LinearLayoutManager recyclerLayoutManager =
                new LinearLayoutManager(getActivity().getApplicationContext());
        filesRecyclerView.setLayoutManager(recyclerLayoutManager);
        filesRecyclerView.setNestedScrollingEnabled(false);

        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(filesRecyclerView.getContext(),
                        recyclerLayoutManager.getOrientation());
        filesRecyclerView.addItemDecoration(dividerItemDecoration);

        return view;
    }

    private void getFileNamesFromFirestoreDb() {
        firestoreDB.collection("files")
                .whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<String> fileList = new ArrayList<String>();
                            Log.d(TAG, "DOCS SIZE "+task.getResult().size());
                            for(DocumentSnapshot doc : task.getResult()){
                                fileList.add(doc.getString("storagePath"));
                            }
                            FilesRecyclerViewAdapter recyclerViewAdapter = new FilesRecyclerViewAdapter(fileList,
                                    getActivity(), userId);
                            filesRecyclerView.setAdapter(recyclerViewAdapter);

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

}
