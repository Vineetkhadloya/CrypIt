package com.android.crypto;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Information extends AppCompatActivity {
    //TextView tv;
    Button bt1,bt2;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        //tv=findViewById(R.id.textView3);
        bt1=findViewById(R.id.button1);
        bt2=findViewById(R.id.button2);




    }


    public void ed(View view)
    {
       /* bt1.setVisibility(View.INVISIBLE);
        tv.setVisibility(View.VISIBLE);
        tv.setText("1. Choose the file you wish to Encrypt\n"+
                "2. Enter a password which is 8 characters long that you can easily remember.\n"+
                "3. Click on Encrypt.\n"+
                "4. Your File is encrypted.\n"+
                "5. You will receive 4 files as output,one is your original file,\n"+
                "second is your loss-lessly encrypted file,third and fourth are other secure files generated that are crucial for your decryption process\n"+
                "6. Now when you go to the Decrypt Tab,choose the appropriate encrypted file(Make sure the 2 secure files are also present)\n"+
                "7. Enter the password used for encryption(Wrong password leads to loss of data)\n"+
                "8. Click on Decrypt.\n"+
                "9. Your File is securely Decrypted.\n");*/

        final AlertDialog.Builder alert = new AlertDialog.Builder(this,R.style.MyDialogTheme);
        alert.setTitle("Encrypt/Decrypt\n\n");

// Create TextView
        final TextView input = new TextView (this);
        Typeface typeface= ResourcesCompat.getFont(this, R.font.nunito_semibold);
        input.setTypeface(typeface);
        input.setAllCaps(true);
        input.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        input.setPadding(5,5,5,5);
        input.setText("\n1. Choose the file you wish to Encrypt.(Any File of the type given in the extensions list can be choosen)\n"+
                "2. Enter a password which is 8 characters long that you can easily remember.\n"+
                "3. Click on Encrypt.\n"+
                "4. Your File is encrypted.\n"+
                "5. You will receive 4 files as output,one is your original file,\n"+
                "second is your loss-lessly encrypted file,third and fourth are other secure files generated that are crucial for your decryption process.\n"+
                "6. Now when you go to the Decrypt Tab,choose the appropriate encrypted file.(Make sure the 2 secure files are also present)\n"+
                "7. Enter the password used for encryption.(Wrong password leads to loss of data)\n"+
                "8. Click on Decrypt.\n"+
                "9. Your File is securely Decrypted.\n");
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
                // Do something with value!
            }
        });


        alert.show();
    }

    public void ud(View view)
    {



        final AlertDialog.Builder alert = new AlertDialog.Builder(this,R.style.MyDialogTheme);
        alert.setTitle("\tUpload/Download\n");

// Create TextView
        final TextView input = new TextView (this);
        Typeface typeface= ResourcesCompat.getFont(this, R.font.nunito_semibold);
        input.setTypeface(typeface);
        input.setAllCaps(true);
        input.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        input.setPadding(5,5,5,5);
        input.setText("\n1. To upload files, click on the upload button present.\n"+
                "2. Select more than one file to upload.\n"+
                "3. As soon as you select the files, they start to upload, the files are uploaded successfully when a tick mark appears beside their names.\n"+
                "4. In Your Files Tab, you can find the files uploaded.\n"+
                "5. These files can be downloaded or deleted.\n"+
                "6. The file downloaded will be stored in 'Download' folder of your phone storage.\n"+
                "7. If the file is deleted, it cannot be recovered back.");
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
                // Do something with value!
            }
        });


        alert.show();


    }

    public void lex(View view)
    {
        List<String> mExt = new ArrayList<String>();
        mExt.add(".PDF");
        mExt.add(".PPT");
        mExt.add(".TXT");
        mExt.add(".XLS");
        mExt.add(".XLSX");
        mExt.add(".CSV");
        mExt.add(".DOCX");
        mExt.add(".JPG");
        mExt.add(".JPEG");
        mExt.add(".PNG");
        mExt.add(".GIF");
        mExt.add(".MP3");
        mExt.add(".MP4");
        mExt.add(".MKV");
        //Create sequence of items
        final CharSequence[] Extensions = mExt.toArray(new String[mExt.size()]);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Extensions");
        dialogBuilder.setItems(Extensions, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
               // String selectedText = Extensions[item].toString();  //Selected item in listview
            }
        });
        dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
                // Do something with value!
            }
        });
        //Create alert dialog object via builder
        AlertDialog alertDialogObject = dialogBuilder.create();
        //Show the dialog
        alertDialogObject.show();
    }

}
