package com.android.crypto;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.AlgorithmParameters;
import java.security.Key;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import static com.android.crypto.FileChooser.isDownloadsDocument;


public class Decrypt extends Fragment implements View.OnClickListener
{

    //private StorageReference storageReference;
    String s;
    String s1;
    String str;
    String str1;
    String str2;
    EditText editText;
    public Uri filePath;
    private Button buttonChoose;

    private Button buttonDecrypt;
    ProgressDialog pd;
    String extension;
    int n=0;
    int n1=0;
    private RecyclerView mUploadList;

    private List<String> fileNameList;
    private List<String> fileDoneList;

    private UploadListAdapter uploadListAdapter;

    private StorageReference mStorage;
    private FirebaseUser user;
    private FragmentManager fm;
    private String userPath;
    private FirebaseStorage firebaseStorage;
    private FirebaseFirestore firestoreDB;
    private static FragmentManager fragmentManager;



    public Decrypt() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        fragmentManager = getActivity().getSupportFragmentManager();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userPath = "user/" + user.getUid() + "/";
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }
    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.file_menu, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.upload_file:
                addFileFrgmt();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void addFileFrgmt()
    {

        uploader();
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v= inflater.inflate(R.layout.fragment_decrypt, container, false);
        buttonChoose = (Button) v.findViewById(R.id.buttonChoose);
        buttonDecrypt = (Button) v.findViewById(R.id.buttonDecrypt);
        editText=v.findViewById(R.id.editText);
        //attaching listener
        buttonChoose.setOnClickListener(this);
        mStorage = FirebaseStorage.getInstance().getReference();
        firebaseStorage = FirebaseStorage.getInstance();
        firestoreDB = FirebaseFirestore.getInstance();

        mUploadList = (RecyclerView) v.findViewById(R.id.upload_list);

        fileNameList = new ArrayList<>();
        fileDoneList = new ArrayList<>();

        uploadListAdapter = new UploadListAdapter(fileNameList, fileDoneList);

        //RecyclerView

        mUploadList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mUploadList.setHasFixedSize(true);
        mUploadList.setAdapter(uploadListAdapter);
        mUploadList.setNestedScrollingEnabled(false);

        fm = getActivity().getSupportFragmentManager();




        buttonDecrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str2 = editText.getText().toString();
                if (str2.matches("") && str2.length()<8) {
                    Toast.makeText(getActivity(), "Enter a Password 8 characters long", Toast.LENGTH_LONG).show();
                    return;
                }
                else if(str2.length()>=8)
                        openDialog1();
                    else
                        Toast.makeText(getActivity(),"Enter a Password 8 characters long. ",Toast.LENGTH_LONG).show();

                }
        });

        return v;
    }



    public class LoadViewTask1 extends AsyncTask<Void, Integer, Void>
    {

        @Override
        protected Void doInBackground(Void... voids) {

            //FirebaseDatabase database = FirebaseDatabase.getInstance();
            //DatabaseReference myRef = database.getReference("File");

            File file=new File(str);

            str2 = editText.getText().toString();
            FileInputStream fis = null;
            FileOutputStream fos = null;
            File f000=null;
            File f001=null;

            String s0="";
            s0=str.replace("."+extension,"");
            s0.trim();
            n1= Integer.parseInt((s0.substring(s0.length()-1)));
            System.out.println(n1);

            str=str.substring(0,s0.length()-1);
            str=str+"."+extension;
            //str.trim();
            s0=s0.substring(0,s0.length()-1);

            try {
                FileInputStream f01=new FileInputStream(file);
                File file1=new File(str);
                FileOutputStream f02=new FileOutputStream(file1);
                doCopy(f01,f02);
                file.delete();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //System.out.println(s0);
            System.out.println(str);
            s0=(s0+"-decrypted"+"."+extension).trim();

            try {
                f000=new File(str);
                f001=new File(s0);
                fis = new FileInputStream(f000);
                fos = new FileOutputStream(f001);


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                str2=editText.getText().toString();
                switch(n1)
                {
                    case 1:

                        desdecrypt(str2,fis,fos);
                        bfdecrypt(f000,f001);
                        aesdecrypt(0);
                        break;

                    case 2:

                        desdecrypt(str2,fis,fos);
                        aesdecrypt(0);
                        bfdecrypt(f000,f001);
                        break;

                    case 3:

                        bfdecrypt(f000,f001);
                        aesdecrypt(0);
                        desdecrypt(str2,fis,fos);
                        break;

                    case 4:

                        aesdecrypt(0);
                        bfdecrypt(f000,f001);
                        desdecrypt(str2,fis,fos);
                        break;

                }
                String s0002=str.replace("."+extension,"").trim();
                s0002=(s0002+"-decrypted"+"."+extension).trim();
                File fd=new File(s0002);
                fd.delete();

            } catch (Throwable throwable) {
                throwable.printStackTrace();
                pd.dismiss();
                Toast.makeText(getActivity(),"File not Decrypted",Toast.LENGTH_LONG).show();
            }
            return null;
        }



        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(getActivity(),R.style.SpinnerTheme);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.setMessage("Decrypting");
            pd.show();
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            pd.dismiss();
            Toast.makeText(getActivity(),"File Decrypted.",Toast.LENGTH_LONG).show();
        }
    }



    public void openDialog1() {

        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();

        // Set Custom Title
        TextView title = new TextView(getActivity());
        // Title Properties
        title.setText("Dialog Box");
        title.setPadding(10, 10, 10, 10);   // Set Position
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.BLACK);
        title.setTextSize(20);
        alertDialog.setCustomTitle(title);

        // Set Message
        TextView msg = new TextView(getActivity());
        // Message Properties
        msg.setText("Is the Password correct?"+"\nIncorrect Password can lead to loss of data");
        msg.setGravity(Gravity.CENTER_HORIZONTAL);
        msg.setTextColor(Color.BLACK);
        alertDialog.setView(msg);

        // Set Button
        // you can more buttons
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL,"OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Perform Action on Button
                if(str!=null)
                    try {
                        alertDialog.dismiss();
                        new LoadViewTask1().execute();
                        //aesencrypt(1);
                        // aesencrypt(2);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                else
                {
                    Toast.makeText(getActivity(),"Select a file to Decrypt",Toast.LENGTH_LONG).show();
                }

            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,"CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Perform Action on Button
                Toast.makeText(getContext(),"Enter the password again",Toast.LENGTH_LONG).show();
            }
        });

        new Dialog(getContext());
        alertDialog.show();

        // Set Properties for OK Button
        final Button okBT = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
        LinearLayout.LayoutParams neutralBtnLP = (LinearLayout.LayoutParams) okBT.getLayoutParams();
        neutralBtnLP.gravity = Gravity.FILL_HORIZONTAL;
        okBT.setPadding(50, 10, 10, 10);   // Set Position
        okBT.setTextColor(Color.BLUE);
        okBT.setLayoutParams(neutralBtnLP);

        final Button cancelBT = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        LinearLayout.LayoutParams negBtnLP = (LinearLayout.LayoutParams) okBT.getLayoutParams();
        negBtnLP.gravity = Gravity.FILL_HORIZONTAL;
        cancelBT.setTextColor(Color.RED);
        cancelBT.setLayoutParams(negBtnLP);
    }

    public void chooser() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                    filePath = data.getData();
                    extension = getfileExtension(filePath);

                    if (isDownloadsDocument(filePath)) {
                        if(extension.equals("jpg") || extension.equals("jpeg") || extension.equals("png") ||extension.equals("gif") ||extension.equals("mp3") || extension.equals("mp4")) {
                            //Toast.makeText(getActivity(),filePath.toString(),Toast.LENGTH_LONG).show();
                            final String[] split = filePath.getPath().split(":");//split the path.
                            str = split[1];//assign it to a string(your choice)
                            String ext0 = str.substring(str.lastIndexOf("."));
                            ext0 = ext0.replace(".", "");
                            ext0.trim();
                            if (ext0 == extension) {
                            } else {
                                extension = ext0;
                            }
                            // Toast.makeText(getActivity(),ext0,Toast.LENGTH_LONG).show();
                            Toast.makeText(getActivity(), str, Toast.LENGTH_LONG).show();
                            s = str.substring(str.lastIndexOf("/"));
                            s = s.replace("/", "");
                            s1 = str.replace(s, "encrypted-");
                            str1 = s1;
                        }
                        else
                        {
                            str = Environment.getExternalStorageDirectory().toString()+ File.separator + Environment.DIRECTORY_DOWNLOADS+File.separator+getFileName(filePath);
                            String ext0 = str.substring(str.lastIndexOf("."));
                            ext0 = ext0.replace(".", "");
                            ext0.trim();
                            if (ext0 == extension) {
                            } else {
                                extension = ext0;
                            }
                            s = str.substring(str.lastIndexOf("/"));
                            s = s.replace("/", "");
                            s1 = str.replace(s, "encrypted-");
                            str1 = s1;
                            Toast.makeText(getActivity(), str, Toast.LENGTH_LONG).show();

                        }

                    } else if(extension.equals("jpg") || extension.equals("jpeg") || extension.equals("png") ||extension.equals("gif") ||extension.equals("mp3") || extension.equals("mp4"))
                    {
                        str = FileChooser.getPath(getContext(), filePath);
                        String ext0 = str.substring(str.lastIndexOf("."));
                        ext0 = ext0.replace(".", "");
                        ext0.trim();
                        if (ext0 == extension) {
                        } else {
                            extension = ext0;
                        }
                        // Toast.makeText(getActivity(),ext0,Toast.LENGTH_LONG).show();
                        Toast.makeText(getActivity(), str, Toast.LENGTH_LONG).show();
                        s = str.substring(str.lastIndexOf("/"));
                        s = s.replace("/", "");
                        s1 = str.replace(s, "encrypted-");
                        str1 = s1;
                    }
                    else
                    {
                        final String[] split = filePath.getPath().split(":");//split the path.
                        str = "/storage/emulated/0/" + split[1];//assign it to a string(your choice)
                        Toast.makeText(getActivity(), str, Toast.LENGTH_LONG).show();
                        s = str.substring(str.lastIndexOf("/"));
                        s = s.replace("/", "");
                        s1 = str.replace(s, "encrypted-");
                        str1 = s1;
                    }
                }
                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    if (data.getClipData() != null) {

                        int totalItemsSelected = data.getClipData().getItemCount();

                        if (totalItemsSelected > 3) {
                            Toast.makeText(getActivity(), "Maximum 3 files can be selected", Toast.LENGTH_LONG).show();
                            return;
                        } else {
                            if (!fileNameList.isEmpty()) {
                                fileNameList.removeAll(fileNameList);
                                fileDoneList.removeAll(fileDoneList);
                            }

                            for (int i = 0; i < totalItemsSelected; i++) {

                                Uri fileUri = data.getClipData().getItemAt(i).getUri();

                                final String fileName = getFileName(fileUri);


                                fileNameList.add(fileName);
                                fileDoneList.add("uploading");
                                uploadListAdapter.notifyDataSetChanged();
                                FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                StorageReference fileToUpload = mStorage.child(currentFirebaseUser.getUid()).child(fileName);

                                final int finalI = i;
                                fileToUpload.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                        fileDoneList.remove(finalI);
                                        fileDoneList.add(finalI, "done");
                                        addFileNameToDB(fileName);
                                        uploadListAdapter.notifyDataSetChanged();

                                    }
                                });
                                fileToUpload.putFile(fileUri).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getActivity(), "Fail", Toast.LENGTH_LONG).show();
                                    }
                                });

                            }
                        }

                    }
                }
                break;
        }
        /*if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            extension = getfileExtension(filePath);

            if (isDownloadsDocument(filePath)) {
                //Toast.makeText(getActivity(),filePath.toString(),Toast.LENGTH_LONG).show();
                final String[] split0 = filePath.getPath().split(":");//split the path.
                str00 = "/storage/emulated/0/" + split0[1];//assign it to a string(your choice)
                Toast.makeText(getActivity(), str00, Toast.LENGTH_LONG).show();
            } else {


                //if(extension.equals("jpg") || extension.equals("jpeg") || extension.equals("png") ||extension.equals("gif") ||extension.equals("mp3") || extension.equals("mp4"))
                //{
                str = FileChooser.getPath(getContext(), filePath);
                String ext0 = str.substring(str.lastIndexOf("."));
                ext0 = ext0.replace(".", "");
                ext0.trim();
                if (ext0 == extension) {
                } else {
                    extension = ext0;
                }
                // Toast.makeText(getActivity(),ext0,Toast.LENGTH_LONG).show();
                Toast.makeText(getActivity(), str, Toast.LENGTH_LONG).show();
                s = str.substring(str.lastIndexOf("/"));
                s = s.replace("/", "");
                s1 = str.replace(s, "encrypted-");
                str1 = s1;
            }*/


    }
    private void addFileNameToDB(final String fileNAME) {
        String docKey = user.getUid()+fileNAME;
        Map<String, String> mp = new HashMap<String, String>();
        mp.put("storagePath", fileNAME);
        mp.put("userId", user.getUid());

        firestoreDB.collection("files").document(docKey)
                .set(mp)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "file name has been added to firestore db ");
                        } else {
                            Log.e(TAG, "failed to add file name to db " + fileNAME);
                        }
                    }
                });
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private String getfileExtension(Uri uri) {
        String extension;
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        extension= mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
        return extension;
    }



    public void aesdecrypt(int i) throws Exception {


        str2=editText.getText().toString();
        // reading the salt
        // user should have secure mechanism to transfer the
        // salt, iv and password to the recipient
        //System.out.println(i);
        //Toast.makeText(MainActivity.this,i,Toast.LENGTH_LONG).show();
        File fs=new File(str+"-"+i+"salt.enc");
        FileInputStream saltFis = new FileInputStream(fs);
        byte[] salt = new byte[16];
        saltFis.read(salt);
        saltFis.close();
        fs.delete();


        // reading the iv
        File fi=new File(str+"-"+i+"iv.enc");
        FileInputStream ivFis = new FileInputStream(fi);
        byte[] iv = new byte[16];
        ivFis.read(iv);
        ivFis.close();
        fi.delete();


        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec keySpec = new PBEKeySpec(str2.toCharArray(), salt, 65536,
                256);
        SecretKey tmp = factory.generateSecret(keySpec);
        SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

        // file decryption
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv));
        File f00=new File(str);
        FileInputStream fis = new FileInputStream(f00);
        String s03=str.replace("."+extension,"").trim();
        s03=(s03+"-decrypted"+"."+extension).trim();
        File file5=new File(s03);
        file5.createNewFile();
        FileOutputStream fos = new FileOutputStream(file5,false);
        byte[] in = new byte[1024];
        byte[] output=new byte[1024];
        int read;
        while ((read = fis.read(in)) != -1) {
            output = cipher.update(in, 0, read);
            if (output != null)
                fos.write(output);
        }

        try
        {
            output = cipher.doFinal();
        }
        catch (BadPaddingException b)
        {
            System.out.println(b.getLocalizedMessage());
        }
        if (output != null) fos.write(output);
        fis.close();
        fos.flush();
        fos.close();

        String s02=str.replace("."+extension,"").trim();
        s02=(s02+"-decrypted"+"."+extension).trim();
        FileInputStream inp = new FileInputStream(s02);

        FileOutputStream out = new FileOutputStream(str);

        doCopy(inp,out);

        System.out.println("File Decrypted.");
    }





    public void desdecrypt(String key, InputStream is, OutputStream os) throws Throwable {
        encryptOrDecrypt(key, Cipher.DECRYPT_MODE, is, os);
    }

    public void encryptOrDecrypt(String key, int mode, InputStream is, OutputStream os) throws Throwable {

        DESKeySpec dks = new DESKeySpec(key.getBytes());
        SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
        SecretKey desKey = skf.generateSecret(dks);
        Cipher cipher = Cipher.getInstance("DES"); // DES/ECB/PKCS5Padding for SunJCE

        if (mode == Cipher.ENCRYPT_MODE) {
            cipher.init(Cipher.ENCRYPT_MODE, desKey);
            CipherInputStream cis = new CipherInputStream(is, cipher);
            doCopy(cis, os);

            File file004=new File(str1+s);
            file004.createNewFile();
            FileInputStream inp = new FileInputStream(file004);
            File file005=new File(str);
            file005.createNewFile();
            FileOutputStream out = new FileOutputStream(file005);
            doCopy(inp,out);


            System.out.println("File Encrypted.");
        } else if (mode == Cipher.DECRYPT_MODE) {
            cipher.init(Cipher.DECRYPT_MODE, desKey);
            CipherOutputStream cos = new CipherOutputStream(os, cipher);
            doCopy(is, cos);
            String s01=str.replace("."+extension,"").trim();
            s01=(s01+"-decrypted"+"."+extension).trim();
            File file006=new File(s01);
            file006.createNewFile();
            FileInputStream inp = new FileInputStream(file006);
            File file007=new File(str);
            file007.createNewFile();
            FileOutputStream out = new FileOutputStream(file007);

            doCopy(inp,out);
            System.out.println("File Decrypted.");

        }
    }

    public void doCopy(InputStream is, OutputStream os) throws IOException {
        byte[] bytes = new byte[1024];
        int numBytes;
        while ((numBytes = is.read(bytes)) != -1) {
            os.write(bytes, 0, numBytes);
        }
        os.flush();
        os.close();
        is.close();
    }




    public void bfdecrypt(File inputFile, File outputFile) throws Exception {
        doCrypto(Cipher.DECRYPT_MODE, inputFile, outputFile);

        String s002=str.replace("."+extension,"").trim();
        s002=(s002+"-decrypted"+"."+extension).trim();
        File file002=new File(s002);
        file002.createNewFile();
        FileInputStream inp = new FileInputStream(file002);
        File file003=new File(str);
        file003.createNewFile();
        FileOutputStream out = new FileOutputStream(file003);

        doCopy(inp,out);
        System.out.println("File decrypted successfully!");
    }

    private void doCrypto(int cipherMode, File inputFile, File outputFile) throws Exception {

        str2=editText.getText().toString();
        Key secretKey = new SecretKeySpec(str2.getBytes(), "Blowfish");
        Cipher cipher = Cipher.getInstance("Blowfish");
        cipher.init(cipherMode, secretKey);

        FileInputStream inputStream = new FileInputStream(inputFile);
        byte[] inputBytes = new byte[(int) inputFile.length()];
        inputStream.read(inputBytes);

        byte[] outputBytes = cipher.doFinal(inputBytes);

        FileOutputStream outputStream = new FileOutputStream(outputFile);
        outputStream.write(outputBytes);

        inputStream.close();
        outputStream.close();

    }


    @Override
    public void onClick(View v) {
        if(v==buttonChoose)
        {
            chooser();
        }

    }

    private void uploader() {
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), 1);
    }



}