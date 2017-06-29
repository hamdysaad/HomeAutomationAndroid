package com.gp.smarthome.hamdy.smarthomegp.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.gp.smarthome.hamdy.smarthomegp.Fragments.AddDeviceDialog;
import com.gp.smarthome.hamdy.smarthomegp.Fragments.GroupsFragment;
import com.gp.smarthome.hamdy.smarthomegp.Fragments.HomeFragment;
import com.gp.smarthome.hamdy.smarthomegp.Fragments.IShowAddBehavoirDialog;
import com.gp.smarthome.hamdy.smarthomegp.Fragments.NotificationsFragment;
import com.gp.smarthome.hamdy.smarthomegp.Fragments.ProfileFragment;
import com.gp.smarthome.hamdy.smarthomegp.Fragments.SettingsFragment;
import com.gp.smarthome.hamdy.smarthomegp.R;
import com.gp.smarthome.hamdy.smarthomegp.SmartHomeApp;
import com.gp.smarthome.hamdy.smarthomegp.User;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        HomeFragment.OnFragmentInteractionListener,
        ProfileFragment.OnFragmentInteractionListener,
        GroupsFragment.OnFragmentInteractionListener,
        NotificationsFragment.OnFragmentInteractionListener,
        SettingsFragment.OnFragmentInteractionListener,
        AddDeviceDialog.OnFragmentInteractionListener{

    private  final String TAG = "MainActivity";
    private User user;
    public CircleImageView userImageCIV;
    public TextView userNameTV;
    public Menu menu;
    private IShowAddBehavoirDialog iShowAddBehavoirDialog;
    public HomeFragment mHomeFragment;
    public AddDeviceDialog mAddDeviceDialog;
    public GroupsFragment mGroupsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        user = ((SmartHomeApp)getApplication()).user;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        ){
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                Log.e(TAG , slideOffset + "");

                userImageCIV =  (CircleImageView)findViewById(R.id.navHeaderMain_userImage_civ);
                userNameTV =  (TextView)findViewById(R.id.navHeaderMain_userName_tv);

                userNameTV.setText(user.fName + " " + user.lName);

            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        HomeFragment fragment = HomeFragment.newInstance(null);
        showFragment(fragment ,HomeFragment.TAG );
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        Fragment fragment;

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id){
            case R.id.nav_home:
                fragment = HomeFragment.newInstance(null);
                showFragment(fragment , "HomeFragment" );
                break;
            case R.id.nav_profile:
                fragment = ProfileFragment.newInstance();
                showFragment(fragment , "ProfileFragment");
                break;
            case R.id.nav_groups:
                fragment = GroupsFragment.newInstance();
                showFragment(fragment , "GroupsFragment");
                break;
            case R.id.nav_notifications:
                fragment = NotificationsFragment.newInstance();
                showFragment(fragment , "NotificationsFragment");
                break;
            case R.id.nav_setting:
                fragment = SettingsFragment.newInstance();
                showFragment(fragment , "SettingsFragment");
                break;
            case R.id.nav_logout:
                user.logout(this);
                break;
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);



        return true;
    }

    private void showFragment(Fragment fragment , String tag) {

        FragmentTransaction bt = getSupportFragmentManager().beginTransaction();

        bt.replace(R.id.content_main , fragment );

//        bt.addToBackStack(tag);

        bt.commit();
    }


    public static void start(Context context) {

        Intent i = new Intent(context, MainActivity.class);
        context.startActivity(i);
        ((AppCompatActivity) context).finish();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        this.menu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        MenuItem item = menu.findItem(R.id.ic_add);

        if(user.userType.equals("admin"))
            item.setVisible(true);
        else
            item.setVisible(false);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.ic_add:
                iShowAddBehavoirDialog.showAddBehavoirDialog();
                break;
        }

        return true;
    }

    private void showADevicFormeDialog() {

        Log.e(TAG , "showADevicFormeDialog");
    }

    public IShowAddBehavoirDialog getiShowAddBehavoirDialog() {
        return iShowAddBehavoirDialog;
    }

    public void setiShowAddBehavoirDialog(IShowAddBehavoirDialog iShowAddBehavoirDialog) {
        this.iShowAddBehavoirDialog = iShowAddBehavoirDialog;
    }

    @Override
    public void setHomefragment(HomeFragment fragment) {
        mHomeFragment = fragment;
    }

    @Override
    public void setAddDeviceDialog(AddDeviceDialog fragment) {
        mAddDeviceDialog = fragment;
    }

    public void displayToastError() {
        Toast.makeText(this, getString(R.string.error_occured), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setGroupsfragment(GroupsFragment groupsFragment) {
        mGroupsFragment = groupsFragment;
    }
}
