package com.justinqle.refresh.activities;

import android.os.Bundle;
import android.support.design.bottomappbar.BottomAppBar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;

import com.justinqle.refresh.R;
import com.justinqle.refresh.fragments.BottomNavigationDrawerFragment;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // BottomAppBar
        BottomAppBar bar = findViewById(R.id.bar);
        // Fragments can create custom options menu with setHasOptionsMenu()
        setSupportActionBar(bar);
        // BottomNavigationDrawerFragment
        BottomNavigationDrawerFragment bottomNavigationDrawerFragment = new BottomNavigationDrawerFragment();
        // Clicking on BottomAppBar Navigation Icon shows BottomNavigationDrawerFragment
        bar.setNavigationOnClickListener(view -> bottomNavigationDrawerFragment.show(getSupportFragmentManager(), bottomNavigationDrawerFragment.getTag()));

        // Floating Action Button
        FloatingActionButton fab = findViewById(R.id.fab);
        // TODO: Snackbars cover bottom app bar
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show());

        // Navigation Component
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        // Listener for every navigation change to hide/show/modify overarching UI elements
        navController.addOnNavigatedListener((controller, destination) -> {
            // Closes BottomNavigationDrawer when Navigating
            // TODO: There isn't a built-in way to close it on menu selection???
            if (bottomNavigationDrawerFragment.isVisible()) {
                bottomNavigationDrawerFragment.dismiss();
            }
            // Hides bottom app bar & floating action button on fragments that don't utilize it
            if (destination.getId() == R.id.loginFragment) {
                getSupportActionBar().hide();
                fab.hide();
            }
            // Makes sure bottom app bar gets shown again if hidden before
            else if (!getSupportActionBar().isShowing()) {
                getSupportActionBar().show();
            }
        });
    }

    // Closes sidebar on back press
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        return Navigation.findNavController(this, R.id.nav_host_fragment).navigateUp();
    }

}
