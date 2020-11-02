package com.example.ketabeman21.Activity;


import android.graphics.Typeface;
import android.os.Bundle;

import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.ketabeman21.Fragments.BookFragment;
import com.example.ketabeman21.Fragments.OfflineFragment;
import com.example.ketabeman21.Network.Network;
import com.example.ketabeman21.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {
    private Fragment book_fragment,private_fragment
            ,my_book_fragment,offline_mode,profile,about,suggest;
    private Network nw;
    public static Typeface my_font;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nw = new Network(MainActivity.this);
        my_font = Typeface.createFromAsset(getAssets(),"font/IRAN Sans Light.ttf");

        book_fragment = new BookFragment();
        offline_mode = new OfflineFragment();
        loadFragment(book_fragment);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.nav_home);
        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        if (nw.isOnline()) {
                            loadFragment(book_fragment);
                        } else {
                            loadFragment(offline_mode);
                        }
                        return true;
                    case R.id.nav_conversation:
                        loadFragment(offline_mode);

                        //loadFragment(private_fragment);
                        return true;
                    case R.id.nav_mybooks:
                        // Toast.makeText(BookAndM.this, "clicked", Toast.LENGTH_SHORT).show();
                        //loadFragment(my_book_fragment);
                        return true;


                }

                return false;
            }
        };
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }
    private void loadFragment(Fragment fragment) {
        // load fragment
        androidx.fragment.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
