package com.bmusic.bmusicworld;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bmusic.bmusicworld.base.CustomeActivity;
import com.bmusic.bmusicworld.extra.Setting;
import com.bmusic.bmusicworld.fragment.AlbumFragment;
import com.bmusic.bmusicworld.fragment.ArtistFragment;
import com.bmusic.bmusicworld.fragment.DownloadsFragment;
import com.bmusic.bmusicworld.fragment.HomeFragment;
import com.bmusic.bmusicworld.fragment.OfflineMusicFragment;
import com.bmusic.bmusicworld.fragment.PlaylistFragment;
import com.bmusic.bmusicworld.fragment.SearchExpandFragment;
import com.bmusic.bmusicworld.navigateitems.DrawerFragment;
import com.bmusic.bmusicworld.sessionmagement.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public  class MainActivity extends CustomeActivity implements DrawerFragment.FragmentDrawerListener {

    @BindView(R.id.main_toolbar)
    Toolbar mainToolbar;
    @BindView(R.id.main_content)
    FrameLayout mainContent;
    @BindView(R.id.main_drawer)
    DrawerLayout main_drawer;
    public ActionBarDrawerToggle drawerToggle;
    DrawerFragment drawerFragment;
    @BindView(R.id.search)
    EditText search;
    @BindView(R.id.mysearch)
    Button mysearch;
    @BindView(R.id.searchlayout)
    RelativeLayout searchlayout;
    LinearLayout ll12,ll13;
    static MainActivity mainActivity;

    String[] permissions= new String[]{
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};
    private static final int MULTIPLE_PERMISSIONS = 10;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_activitymain);
        mainActivity=this;

        if (checkPermissions())
        {

            //  permissions  granted.
        }else {
            checkPermissions();
        }
        ll12 = (LinearLayout)this.findViewById(R.id.ll12);
        ll13 = (LinearLayout)this.findViewById(R.id.ll13);

        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            ll12.setVisibility(View.GONE);
            ll13.setVisibility(View.VISIBLE);
        }else
        {
            ll12.setVisibility(View.VISIBLE);
            ll13.setVisibility(View.GONE);
        }

        ButterKnife.bind(this);
        drawerFragment = (DrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        setupDrawerAndToggle();
        drawerFragment.setUp(R.id.fragment_navigation_drawer, main_drawer, mainToolbar);
        drawerFragment.setDrawerListener(this);
        add(HomeFragment.newInstance());
    }
    public static MainActivity getInstance(){
        return   mainActivity;
    }

    public void transition(){
       // add(PaymentScreenPremiumFragment.newInstance());
    }
    private void setupDrawerAndToggle() {
        setSupportActionBar(mainToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        drawerToggle = new ActionBarDrawerToggle(this, main_drawer, mainToolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                setDrawerIndicatorEnabled(true);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        main_drawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

    @Override
    protected ActionBarDrawerToggle getDrawerToggle() {
        return drawerToggle;
    }

    @Override
    protected DrawerLayout getDrawer() {
        return main_drawer;
    }

    public void onDrawerItemSelected(View view, int position) {

        switch (position) {
            case 0:
                add(HomeFragment.newInstance());
                break;
            case 1:
                add(PlaylistFragment.newInstance());
                break;
            case 2:
                add(AlbumFragment.newInstance());
                break;
            case 3:
                add(ArtistFragment.newInstance());
                break;
            case 4:
                add(DownloadsFragment.newInstance());
                break;
            case 5:
                add(OfflineMusicFragment.newInstance());
                break;
            default:
                break;
        }
    }

    @OnClick(R.id.mysearch)
    public void onViewClicked() {
        String fn= search.getText().toString();
        if (fn.length()==0||search.getText().toString().isEmpty())
        {
            Toasty.info(getApplicationContext(), "What you want to search?", Toast.LENGTH_LONG).show();
            searchlayout.setVisibility(View.GONE);
            return;
        }
        else if(fn.length()!=0) {
            String value = search.getText().toString().trim();

            Bundle bundle = new Bundle();
            bundle.putString("data",value);
            add(SearchExpandFragment.newInstance(bundle));
            search.setText("");
            searchlayout.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            if(searchlayout.getVisibility()==View.VISIBLE) {
                searchlayout.setVisibility(View.GONE);
            }
            else
            {
                searchlayout.setVisibility(View.VISIBLE);
            }
            return true;
        }

        // user is in notifications fragment
        // and selected 'Mark all as Read'
        if (id == R.id.action_notification) {
            Toast.makeText(getApplicationContext(), "All notifications marked as read!", Toast.LENGTH_LONG).show();
        }

        // user is in notifications fragment
        // and selected 'Clear All'
        if (id == R.id.action_settings) {

           Intent i=new Intent(MainActivity.this, Setting.class);
           startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }
    private  boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p:permissions) {
            result = ContextCompat.checkSelfPermission(getApplicationContext(),p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),MULTIPLE_PERMISSIONS );
            return false;
        }
        return true;
    }


}
