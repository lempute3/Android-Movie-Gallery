package com.example.app;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.app.firebase.FirebaseRepository;
import com.example.app.firebase.FirebaseUserModel;
import com.example.app.firebase.OnUserDataFetchListener;
import com.example.app.uiutils.UIActivitySwitcher;
import com.example.app.uiutils.UIFragmentSwitcher;
import com.example.app.viewmodels.MovieListViewModel;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.slider.RangeSlider;

import java.util.concurrent.atomic.AtomicReference;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, TextView.OnEditorActionListener {

    /*DATA*/
    private FirebaseRepository mFirebaseRepository = FirebaseRepository.getInstance();
    private MovieListViewModel mMovieListViewModel;

    /*DRAWER NAVIGATION*/
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private TextView mNavUsernameLabel, mNavProfileCircleLabel;

    /*TOOLBAR NAVIGATION*/
    private Toolbar mToolbar;
    private LinearLayout mSearchLayout;
    private ImageView mSearchClearBtn, mFilterBtn;
    private EditText mSearchInput;

    /*BOTTOM-SHEET NAVIGATION*/
    private RatingBar mMovieRatingBar;
    private RangeSlider mMovieRatingSlider;
    private Button mApplyFilterBtn;

    /*FRAGMENT SWITCHERS*/
    private FrameLayout mFragmentLayout;
    private UIActivitySwitcher mLoginActivity;
    private UIFragmentSwitcher mHomeFragment, mWatchlistFragment, mAboutFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*DATA*/
        mMovieListViewModel = new ViewModelProvider(this).get(MovieListViewModel.class);

        /*ACTIVITIES*/
        mLoginActivity = new UIActivitySwitcher(this, LoginActivity.class);

        /*FRAGMENTS*/
        mFragmentLayout = findViewById(R.id.m_fragment_container_layout);
        mHomeFragment     =   new UIFragmentSwitcher (mFragmentLayout.getId(), getSupportFragmentManager(), new HomeFragment());
        mWatchlistFragment =   new UIFragmentSwitcher (mFragmentLayout.getId(), getSupportFragmentManager(), new WatchlistFragment());
        mAboutFragment    =   new UIFragmentSwitcher (mFragmentLayout.getId(), getSupportFragmentManager(), new AboutFragment());

        /*TOOLBAR*/
        mToolbar = findViewById(R.id.m_toolbar);

        /*DRAWER VIEWS*/
        mNavUsernameLabel = findViewById(R.id.m_username_label);
        mNavProfileCircleLabel = findViewById(R.id.m_profile_circle_label);

        /*--------------*/
        mFirebaseRepository.getCurrentUserData(new OnUserDataFetchListener() {
            @Override
            public void onFetchSuccess(FirebaseUserModel user) {

                String username = user.getUsername();
                String nameAlphabet = String.valueOf(user.getUsername().charAt(0)).toUpperCase();

                mNavUsernameLabel.setText(username);
                mNavProfileCircleLabel.setText(nameAlphabet);
            }

            @Override
            public void onFetchFailure(String message) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        });

        /*INPUTS*/
        mSearchInput = findViewById(R.id.m_search_view);

        /*BUTTONS*/
        mSearchClearBtn = findViewById(R.id.m_search_clear);
        mFilterBtn = findViewById(R.id.m_filter);

        /*VIEWS*/
        mNavigationView = findViewById(R.id.m_nav_view);

        /*LAYOUTS*/
        mSearchLayout = findViewById(R.id.m_search_container);
        mDrawerLayout = findViewById(R.id.m_drawer_layout);

        /*EVENT LISTENERS*/
        mSearchInput.setOnEditorActionListener(this);
        mSearchClearBtn.setOnClickListener(this);
        mFilterBtn.setOnClickListener(this);

        configureDrawerNavigation();

    }

    private void configureDrawerNavigation() {
        setSupportActionBar(mToolbar);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.openNav, R.string.closeNav);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        mNavigationView.setCheckedItem(R.id.nav_home);
                        mSearchLayout.setVisibility(View.VISIBLE);
                        mHomeFragment.setFragment();
                        break;

                    case R.id.nav_watchlist:
                        mNavigationView.setCheckedItem(R.id.nav_watchlist);
                        mSearchLayout.setVisibility(View.VISIBLE);
                        mWatchlistFragment.setFragment();
                        break;

                    case R.id.nav_about:
                        mNavigationView.setCheckedItem(R.id.nav_about);
                        mToolbar.setTitle("ABOUT");
                        mSearchLayout.setVisibility(View.GONE);
                        mAboutFragment.setFragment();
                        break;

                    case R.id.nav_logout:
                        Toast.makeText(getApplicationContext(), "Logout Successful!", Toast.LENGTH_LONG).show();
                        mFirebaseRepository.signOutUser();
                        mLoginActivity.setScene();
                        break;

                    default:
                        break;
                }

                mDrawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);
        mActionBarDrawerToggle.syncState();
        mHomeFragment.setFragment();
        mNavigationView.setCheckedItem(R.id.nav_home);
    }

    private void showFilterDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.movie_filter_nav_sheet);

        AtomicReference<Float> selectedRatingValue = new AtomicReference<>(0.0f);

        mMovieRatingBar = dialog.findViewById(R.id.movie_rating_bar);
        mMovieRatingSlider = dialog.findViewById(R.id.filter_rating_slider);
        mApplyFilterBtn = dialog.findViewById(R.id.apply_filter_btn);

        mMovieRatingSlider.addOnChangeListener((slider, value, fromUser) -> {
            mMovieRatingBar.setRating(value);
            selectedRatingValue.set(value);
        });

        mApplyFilterBtn.setOnClickListener(view -> {
            mMovieListViewModel.sortMoviesByVote(selectedRatingValue.get());
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.BottomSheetAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.m_search_clear:
                mSearchInput.getText().clear();
                break;

            case R.id.m_filter:
                showFilterDialog();
                break;

            default:
                break;
        }

    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

        Fragment currentFragment = getSupportFragmentManager().findFragmentById(mFragmentLayout.getId());
        String searchQuery = mSearchInput.getText().toString();

        if (currentFragment instanceof HomeFragment) {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                mMovieListViewModel.searchMovieApi(searchQuery, 0,1);
                return true;
            }
        }

        if (currentFragment instanceof WatchlistFragment) {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                mMovieListViewModel.searchUserWatchlist(searchQuery);
                return true;
            }
        }

        return false;
    }
}