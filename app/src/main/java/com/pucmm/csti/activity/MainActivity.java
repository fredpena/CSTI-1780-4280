package com.pucmm.csti.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.navigation.NavigationView;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.pucmm.csti.R;
import com.pucmm.csti.databinding.ActivityMainBinding;
import com.pucmm.csti.model.Userr;
import com.pucmm.csti.networksync.FirebaseNetwork;
import com.pucmm.csti.networksync.NetResponse;
import com.pucmm.csti.utils.UserSession;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    //to get user session data
    private UserSession session;
    private Userr user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //retrieve session values and display
        retrieveSession();

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_category, R.id.nav_product)
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        //mAppBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.getMenu().findItem(R.id.nav_logout).setOnMenuItemClickListener(item -> {
            session.logout();
            //After logout redirect user to Login Activity
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            //Staring Login Activity
            startActivity(intent);
            //Finish all the Activities
            finish();
            return true;
        });

        View headerLayout = navigationView.getHeaderView(0);
        CircularImageView profile = headerLayout.findViewById(R.id.profile);
        TextView titleMain = headerLayout.findViewById(R.id.titleMain);

        titleMain.setText(user.getFirstName() + " " + user.getLastName());

        if (user.getPhoto() != null && !user.getPhoto().isEmpty()) {
            FirebaseNetwork.obtain().download(user.getPhoto(), new NetResponse<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    profile.setImageBitmap(response);
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.e(TAG, t.getMessage());
                }
            });
        }
    }

    private void retrieveSession() {
        //create new session object by passing application context
        session = new UserSession(getApplicationContext());
        //get User details if logged in
        user = session.getUserSession();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = binding.drawerLayout;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_search) {
            return true;
        } else if (id == R.id.action_notifications) {
            return true;
        } else if (id == R.id.action_cart) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

}