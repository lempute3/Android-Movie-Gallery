package com.example.app;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import com.example.app.firebase.FirebaseHelper;
import com.example.app.uiutils.UIActivitySwitcher;
import com.example.app.uiutils.UIFragmentSwitcher;
import com.example.app.viewmodels.MovieListViewModel;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, TextView.OnEditorActionListener {

    private FirebaseHelper firebaseHelper = FirebaseHelper.getInstance();

    private FrameLayout mFragmentLayout;
    private DrawerLayout mDrawerLayout;
    private MovieListViewModel mMovieListViewModel;

    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private NavigationView mNavigationView;
    private Toolbar mToolbar;

    private TextView mNavUsernameLabel, mNavProfileCircleLabel;
    private EditText mSearchInput;
    private ImageView mSearchClearBtn;

    private UIActivitySwitcher mLoginActivity;
    private UIFragmentSwitcher mHomeFragment, mWatchlistFragment, mAboutFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMovieListViewModel = new ViewModelProvider(this).get(MovieListViewModel.class);

        /*ACTIVITIES*/
        mLoginActivity = new UIActivitySwitcher(this, LoginActivity.class);

        /*FRAGMENTS*/
        mFragmentLayout = findViewById(R.id.m_fragment_container_layout);
        mHomeFragment     =   new UIFragmentSwitcher (mFragmentLayout.getId(), getSupportFragmentManager(), new HomeFragment());
        mWatchlistFragment =   new UIFragmentSwitcher (mFragmentLayout.getId(), getSupportFragmentManager(), new WatchlistFragment());
        mAboutFragment    =   new UIFragmentSwitcher (mFragmentLayout.getId(), getSupportFragmentManager(), new AboutFragment());

        /*VIEWS*/
        mNavUsernameLabel = findViewById(R.id.m_username_label);
        mNavProfileCircleLabel = findViewById(R.id.m_profile_circle_label);;

        /*firebaseHelper.getCurrentUserData(new OnUserDataFetchListener() {
            @Override
            public void onFetchSuccess(User user) {

                String username = user.getUsername();
                String nameAlphabet = String.valueOf(user.getUsername().charAt(0)).toUpperCase();

                mNavUsernameLabel.setText(username);
                mNavProfileCircleLabel.setText(nameAlphabet);
            }

            @Override
            public void onFetchFailure(String message) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        });*/

        /*INPUTS*/
        mSearchInput = findViewById(R.id.m_search_view);

        /*BUTTONS*/
        mSearchClearBtn = findViewById(R.id.m_search_clear);

        /*LAYOUT*/
        mDrawerLayout = findViewById(R.id.m_drawer_layout);

        /*NAVIGATION VIEW*/
        mNavigationView = findViewById(R.id.m_nav_view);

        /*TOOLBAR*/
        mToolbar = findViewById(R.id.m_toolbar);
        setSupportActionBar(mToolbar);

        /*ACTION BAR*/
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.openNav, R.string.closeNav);

        /*EVENT LISTENERS*/
        mSearchInput.setOnEditorActionListener(this);
        mSearchClearBtn.setOnClickListener(this);
        mNavigationView.setNavigationItemSelectedListener(this);
        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);

        mActionBarDrawerToggle.syncState();

        mHomeFragment.setFragment();
        mNavigationView.setCheckedItem(R.id.nav_home);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_home:
                mHomeFragment.setFragment();
                break;

            case R.id.nav_watchlist:
                mWatchlistFragment.setFragment();
                break;

            case R.id.nav_about:
                mAboutFragment.setFragment();
                break;

            case R.id.nav_logout:
                Toast.makeText(getApplicationContext(), "Logout Successful!", Toast.LENGTH_LONG).show();
                firebaseHelper.signOutUser();
                mLoginActivity.setScene();
                break;

            default:
                break;
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.m_search_clear:
                mSearchInput.getText().clear();
                break;
            default:
                break;
        }

    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            String searchQuery = mSearchInput.getText().toString();
            mMovieListViewModel.searchMovieApi(searchQuery, 1);
            return true;
        }

        return false;
    }
}