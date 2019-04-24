package com.bmusic.bmusicworld.navigateitems;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.bmusic.bmusicworld.R;
import com.bmusic.bmusicworld.register.MainLogin;
import com.bmusic.bmusicworld.register.Register;
import com.bmusic.bmusicworld.sessionmagement.SessionManagement;
import com.bmusic.bmusicworld.sessionmagement.SharedPrefManager;
import com.facebook.login.LoginManager;

import java.util.ArrayList;
import java.util.List;

public class DrawerFragment extends Fragment implements View.OnClickListener {

    private static String TAG = DrawerFragment.class.getSimpleName();

    private RecyclerView recyclerView;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private NavigationDrawerAdapter adapter;
    public  List<NavDrawerItem> data = new ArrayList<>();
    private View containerView;
    private TextView link;

    private Button sign_in,sign_up,logout;
    private static String[] titles = null;
    private FragmentDrawerListener drawerListener;
    private SessionManagement session;

    public DrawerFragment() {

    }

    public void setDrawerListener(FragmentDrawerListener listener) {
        this.drawerListener = listener;
    }

    public static List<NavDrawerItem> getData() {
        List<NavDrawerItem> data = new ArrayList<>();


        // preparing navigation drawer items
        for (int i = 0; i < titles.length; i++) {
            NavDrawerItem navItem = new NavDrawerItem();
            navItem.setTitle(titles[i]);
            data.add(navItem);
        }
        return data;
    }

    private  List<NavDrawerItem> prepareDrawerList()
    {
    // Array of strings for
        String [] listviewTitle = new String[]{ "Songs","Playlist" ,"Albums","Artist",
          "Downloads","Offline Music"};
         int[] listviewImage = new int[]{R.drawable.songs,R.drawable.favlist,R.drawable.album,
         R.drawable.singer, R.drawable.download2,R.drawable.mob
         };
         for (int i = 0; i <listviewTitle.length; i++)
         {
         NavDrawerItem navItem = new NavDrawerItem();
         navItem.setTitle(listviewTitle[i]);
         navItem.setIcon(listviewImage[i]);
         data.add(navItem);
         }
//        if (SharedPrefManager.getInstance(getActivity()).isLoggedIn()) {
//
//        }else
//        {
//            data.remove(1);
//        }
         return data;
         }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // drawer labels
       // titles = getActivity().getResources().getStringArray(R.array.nav_drawer_labels);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflating view layout
        View layout = inflater.inflate(R.layout.fragment_drawer, container, false);
        session=new SessionManagement(getActivity());
        recyclerView = (RecyclerView) layout.findViewById(R.id.drawerList);
        sign_in = (Button) layout.findViewById(R.id.sign_in);
        sign_up = (Button) layout.findViewById(R.id.sign_up);
        logout = (Button) layout.findViewById(R.id.log_out);

        adapter = new NavigationDrawerAdapter(getActivity(), prepareDrawerList());
        recyclerView.setAdapter(adapter);

//        if (SharedPrefManager.getInstance(getActivity()).isLoggedIn()) {
//
//        }else
//        {
//          adapter.delete(1);
//          adapter.delete(5);
//        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                drawerListener.onDrawerItemSelected(view, position);
                mDrawerLayout.closeDrawer(containerView);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        sign_in.setOnClickListener(this);
        sign_up.setOnClickListener(this);
        logout.setOnClickListener(this);



        return layout;
    }


    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                toolbar.setAlpha(1 - slideOffset / 2);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in:
                Intent i=new Intent(getActivity(), MainLogin.class);
                startActivity(i);
                break;
            case R.id.sign_up:
                Intent i1=new Intent(getActivity(), Register.class);
                startActivity(i1);
                break;
            case R.id.log_out:
                SharedPrefManager.getInstance(getActivity()).logout();
                session.logoutUser();
                LoginManager.getInstance().logOut();
                break;
        }
    }

    public static interface ClickListener {
         void onClick(View view, int position);

         void onLongClick(View view, int position);
    }

    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }


    }

    public interface FragmentDrawerListener {
         void onDrawerItemSelected(View view, int position);
    }


}
