package id.markirin.markirin.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.NavigationMenuPresenter;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;

import id.markirin.markirin.R;
import id.markirin.markirin.fragment.BookingHistoryListFragment;
import id.markirin.markirin.fragment.KantongParkirListFragment;
import id.markirin.markirin.fragment.KantongParkirMapsFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // Choose an arbitrary request code value
    private static final int RC_SIGN_IN = 123;
    private NavigationMenuPresenter navigationView;
    private FirebaseAuth auth;
    private View navHeaderView;

    /**
     * Called when application starts
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the content view
        setContentView(R.layout.activity_main);

        // Declare the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Declare floating action button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Declare the drawer layout
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Declare navigation view
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Set default currently checked item to near me
        navigationView.setCheckedItem(R.id.nav_near_me);

        navHeaderView = navigationView.getHeaderView(0);

        // Firebase auth initialization
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            // already signed in
            doAfterLogin();
        } else {
            // not signed in
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        Fragment fragment = KantongParkirListFragment.newInstance();
        getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).commit();
    }

    /**
     * Called when back button is pressed
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        // If drawer opens
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            // Close the drawer
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Called when optionsMenu is created
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * Called when optionsItem is selected
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_logout) {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            //Yes button clicked
                            logOut();
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Called when navigationItem is selected
     *
     * @param item
     * @return
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Declare fragment variable
        Fragment fragment = null;
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_near_me) {
            // Create nearest kantong parkir list fragment
            fragment = KantongParkirListFragment.newInstance();
        } else if (id == R.id.nav_maps) {
            // Create kantong parkir maps fragment
            fragment = KantongParkirMapsFragment.newInstance();
        } else if (id == R.id.nav_booking_history) {
            // Create kantong parkir maps fragment
            fragment = BookingHistoryListFragment.newInstance();
        } else if (id == R.id.nav_payment_history) {
            // Create kantong parkir maps fragment
            fragment = KantongParkirMapsFragment.newInstance();
        }

        // Close drawer layout after clicked
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        // Begin the fragment transaction
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
        return true;
    }

    public void doAfterLogin() {
        TextView headerNameTextView = (TextView) navHeaderView.findViewById(R.id.header_name);
        TextView headerEmailTextView = (TextView) navHeaderView.findViewById(R.id.header_email);

        headerNameTextView.setText(auth.getCurrentUser().getDisplayName());
        headerEmailTextView.setText(auth.getCurrentUser().getEmail());
    }

    public void logOut() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, LoginActivity.class));
        Toast.makeText(this, "Logout Success", Toast.LENGTH_SHORT).show();
        finish();
    }
}
