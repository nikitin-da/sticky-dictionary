package com.example.dmitry.handheld_dictionary.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.dmitry.handheld_dictionary.R;
import com.example.dmitry.handheld_dictionary.ui.fragment.BaseFragment;
import com.example.dmitry.handheld_dictionary.ui.navigation_drawer.NavigationDrawerFragment;
import com.example.dmitry.handheld_dictionary.ui.navigation_drawer.NavigationDrawerItem;
import com.example.dmitry.handheld_dictionary.util.Loggi;

/**
 * Main activity class of the application.
 */
public class MainActivity extends BaseActivity implements NavigationDrawerFragment.Listener {

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private DrawerLayout mDrawerLayout;

    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private boolean mDrawerOpened;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    // region fragment lifecycle
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        mActionBarDrawerToggle = new AppActionBarDrawerToggle(
                this,
                mDrawerLayout,
                toolbar,
                R.string.navigation_drawer_open_drawer_description,
                R.string.navigation_drawer_close_drawer_description);
        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
    }

    @Override
    protected void onPostCreate(final Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if (mDrawerLayout != null && mActionBarDrawerToggle != null) {
            mActionBarDrawerToggle.syncState();
        }

        /**
         * Adding {@link com.example.dmitry.handheld_dictionary.ui.navigation_drawer.NavigationDrawerFragment}
         * in code, because it is need to set listener, before it started.
         */
        FragmentManager fm = getSupportFragmentManager();
        if (savedInstanceState == null) {
            mNavigationDrawerFragment = (NavigationDrawerFragment) Fragment.instantiate(this,
                    NavigationDrawerFragment.class.getName());
            fm.beginTransaction().add(R.id.navigation_drawer_container, mNavigationDrawerFragment)
                    .commit();
        } else {
            mNavigationDrawerFragment = (NavigationDrawerFragment)
                    fm.findFragmentById(R.id.navigation_drawer_container);
        }

        mNavigationDrawerFragment.setListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mNavigationDrawerFragment.setListener(this);
        if (mDrawerLayout.isDrawerOpen(Gravity.START)) {
            updateActionBarState(true);
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mNavigationDrawerFragment.setListener(null);
    }
    // endregion

    // region options menu
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        // If the nav drawer is open, hide action items related to the content view
        // http://stackoverflow.com/questions/18135214/hide-actionbar-menuitems-when-navigation-drawer-slides-for-any-amount
        hideMenuItems(menu, !mDrawerOpened);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //noinspection SimplifiableIfStatement
        if (mActionBarDrawerToggle != null && mActionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void hideMenuItems(Menu menu, boolean visible) {
        for (int i = 0; i < menu.size(); i++) {
            menu.getItem(i).setVisible(visible);
        }
    }
    // endregion

    /**
     * Custom class to notify, when navigation drawer changing it state (open/close).
     */
    class AppActionBarDrawerToggle extends ActionBarDrawerToggle {

        private float mPreviousOffset = 0f;

        public AppActionBarDrawerToggle(
                Activity activity,
                DrawerLayout drawerLayout,
                Toolbar toolbar,
                int openDrawerContentDescRes,
                int closeDrawerContentDescRes) {

            super(activity,
                    drawerLayout,
                    toolbar,
                    openDrawerContentDescRes,
                    closeDrawerContentDescRes);
        }

        @Override
        public void onDrawerClosed(View view) {
            super.onDrawerClosed(view);
            updateActionBarState(false);
        }

        @Override
        public void onDrawerOpened(View view) {
            super.onDrawerOpened(view);

            hideKeyboardIfOpened();
            updateActionBarState(true);
        }

        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {
            super.onDrawerSlide(drawerView, slideOffset);

            if (slideOffset > mPreviousOffset && !mDrawerOpened) {
                updateActionBarState(true);
            } else if (mPreviousOffset > slideOffset && slideOffset < 0.5f && mDrawerOpened) {
                updateActionBarState(false);
            }
            mPreviousOffset = slideOffset;
        }
    }

    @Override
    public void onNavigationItemSelected(final NavigationDrawerItem selectedItem) {
        mDrawerOpened = false;
        if (mNavigationDrawerFragment.getLastSelectedItem() != selectedItem) {
            selectContentFragment(selectedItem);
        }

        /**
         * Closing navigation drawer with delay to prevent decrease
         * of fps while fragment transaction.
         */
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    closeNavigationDrawer();
                } catch (Exception e) {
                    Loggi.e(MainActivity.class.getSimpleName(), "onNavigationItemSelected", e);
                }
            }
        }, 250);
    }

    private void closeNavigationDrawer() {
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawers();
        }
    }

    private void updateActionBarState(boolean drawerOpened) {
        mDrawerOpened = drawerOpened;
        supportInvalidateOptionsMenu();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            if (drawerOpened) {
                actionBar.setTitle(R.string.app_name);
            } else {
                BaseFragment fragment = (BaseFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.main_content);
                if (fragment != null && fragment.getActionBarTitle() != null) {
                    /**
                     * Possible duplicate call (first in {@link BaseFragment#onStart()}),
                     * but we need to update title, when drawer will close without fragment
                     * changing.
                     */
                    actionBar.setTitle(fragment.getActionBarTitle());
                }
            }
        }
    }

    private void selectContentFragment(final NavigationDrawerItem selectedItem) {

        Fragment fragment = Fragment.instantiate(this, selectedItem.clazz.getName());

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.main_content, fragment)
                .commit();
    }

    @Override
    protected boolean notifyAllFragmentsAboutActivityResult() {
        return true;
    }

    @Override protected boolean finishWithAnimation() {
        return false;
    }
}
