package com.android.crypto;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.List;


public class FilesRecyclerViewAdapter extends
        RecyclerView.Adapter<FilesRecyclerViewAdapter.ViewHolder> {

    private List<String> fileList;
    private Context context;
    private FirebaseStorage firebaseStorage;
    private FirebaseFirestore firestoreDB;
    private String userId;


    private String DOWNLOAD_DIR = Environment.getExternalStoragePublicDirectory
            (Environment.DIRECTORY_DOWNLOADS).getPath();


    public FilesRecyclerViewAdapter(List<String> list, Context ctx, String uid) {
        fileList = list;
        context = ctx;
        userId = uid;

    }
    @Override
    public int getItemCount() {
        return fileList.size();
    }

    @Override
    public FilesRecyclerViewAdapter.ViewHolder
    onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.file_item_layout, parent, false);

        firebaseStorage = FirebaseStorage.getInstance();
        firestoreDB = FirebaseFirestore.getInstance();

        FilesRecyclerViewAdapter.ViewHolder viewHolder =
                new FilesRecyclerViewAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final FilesRecyclerViewAdapter.ViewHolder holder, int position) {
        final int itemPos = position;
        final String fileName = fileList.get(position);
        holder.name.setText(fileName);
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((holder.download.getVisibility()==View.GONE) && (holder.delete.getVisibility()==(View.GONE)))
                {
                    holder.download.setVisibility(View.VISIBLE);
                    holder.delete.setVisibility(View.VISIBLE);
                }
                else if((holder.download.getVisibility()==View.VISIBLE) && (holder.delete.getVisibility()==(View.VISIBLE)))
                {
                    holder.download.setVisibility(View.GONE);
                    holder.delete.setVisibility(View.GONE);
                }
            }
        });

        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadFile(fileName);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteFile(fileName, itemPos);
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public Button download;
        public Button delete;

        public ViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.file_name_i);
            download = view.findViewById(R.id.download_file_b);
            delete = view.findViewById(R.id.delete_file_b);
        }
    }
    private void downloadFile(String fileName){
        StorageReference storageRef = firebaseStorage.getReference();
        StorageReference downloadRef = storageRef.child(userId).child(fileName);
        File fileNameOnDevice = new File(DOWNLOAD_DIR+"/"+fileName);

        downloadRef.getFile(fileNameOnDevice).addOnSuccessListener(
                new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Log.d("File RecylerView", "downloaded the file");
                        Toast.makeText(context,
                                "Downloaded the file",
                                Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e("File RecylerView", "Failed to download the file");
                Toast.makeText(context,
                        "Couldn't be downloaded",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void deleteFile(final String fileName, final int iPos){
        StorageReference storageRef = firebaseStorage.getReference();
        StorageReference deleteRef = storageRef.child(userId).child(fileName);
        deleteRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.d("File RecylerView", "delete the file");
                    Toast.makeText(context,
                            "File has been deleted",
                            Toast.LENGTH_SHORT).show();
                    fileList.remove(iPos);
                    notifyItemRemoved(iPos);
                    notifyItemRangeChanged(iPos, fileList.size());

                    deleteFileNameFromDB(fileName);

                }else{
                    Log.e("File RecylerView", "Failed to delete the file");
                    Toast.makeText(context,
                            "File Couldn't be deleted",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    private void deleteFileNameFromDB(String fileName){
        firestoreDB.collection("files").document(userId+fileName).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.e("File RecylerView", "File name deleted from db");
                    }
                });
    }
}