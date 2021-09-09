package com.android.crypto;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static FragmentManager fragmentManager;

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener
            =new BottomNavigationView.OnNavigationItemSelectedListener()
    {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment fragment=null;

            switch (menuItem.getItemId())
            {
                case R.id.encryptdecrypt:
                    fragment=new Encrypt_Decrypt();

                    fragmentManager.beginTransaction().replace(R.id.content,fragment).commit();
                    return true;
                case R.id.decrypt:
                    fragment=new Decrypt();

                    fragmentManager.beginTransaction().replace(R.id.content,fragment).commit();
                    return true;
                case R.id.files:
                    fragment=new YourFiles();

                    fragmentManager.beginTransaction().replace(R.id.content,fragment).commit();
                    return true;
            }


            return false;
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        //getSupportActionBar().setTitle("CryptIT");
        //getSupportActionBar().setIcon(getResources().getDrawable(R.drawable.cit1));
        getSupportActionBar().setLogo(getResources().getDrawable(R.drawable.cit1));

        fragmentManager = getSupportFragmentManager();
        Fragment fragment = null;
        fragment = new Encrypt_Decrypt();
        fragmentManager.beginTransaction().replace(R.id.content, fragment).commit();

        if (savedInstanceState == null) {
            fragmentManager.beginTransaction().replace(R.id.content, new Encrypt_Decrypt(), "Encrypt").commit();
        }

        BottomNavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.add(0, 1, 1, menuIconWithText(getResources().getDrawable(R.mipmap.sett1), getResources().getString(R.string.AccSet)));
        menu.add(0, 2, 2, menuIconWithText(getResources().getDrawable(R.mipmap.in2), getResources().getString(R.string.Information)));
        menu.add(0, 3, 3, menuIconWithText(getResources().getDrawable(R.mipmap.exit1), getResources().getString(R.string.exit)));
        menu.add(0, 4, 4, menuIconWithText(getResources().getDrawable(R.mipmap.contac_us), getResources().getString(R.string.cu)));


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        switch (id) {

            case 1:

                startActivity(new Intent(this, Account_Modifications.class));

                return true;
            case 2:

                startActivity(new Intent(this, Information.class));

                return true;

            case 3:

                finishAndRemoveTask();

                return true;

            case 4:

                startActivity(new Intent(this, Contact.class));

                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private CharSequence menuIconWithText(Drawable r, String title) {

        r.setBounds(0, 0, r.getIntrinsicWidth(), r.getIntrinsicHeight());
        SpannableString sb = new SpannableString("    " + title);
        ImageSpan imageSpan = new ImageSpan(r, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return sb;
    }


    protected void replaceEncrypt()
    {
        fragmentManager.beginTransaction().replace(R.id.content, new Encrypt_Decrypt(),"Encrypt").commit();
    }


    @Override
    public void onBackPressed(){
        replaceEncrypt();
    }
}
