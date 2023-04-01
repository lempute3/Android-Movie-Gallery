package com.example.app;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.example.app.repositories.firebase.FirebaseRepository;
import com.example.app.repositories.firebase.OnDataFetchListener;
import com.example.app.repositories.firebase.OnTaskCompletionListener;
import com.example.tmdb.models.MovieModel;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {


    /*@Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.app", appContext.getPackageName());
    }*/

    private FirebaseRepository mFirebaseRepository;
    private Context mContext;

    @Before
    public void setup() {
        mFirebaseRepository = FirebaseRepository.getInstance();
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
    }

    @Test
    public void testLoginUser() throws ExecutionException, InterruptedException, TimeoutException {

        String email = "lukas@example.com";
        String pwd = "Test*409";

        CompletableFuture<Boolean> future = new CompletableFuture<>();
        mFirebaseRepository.loginUser(email, pwd, new OnTaskCompletionListener() {
            @Override
            public void onSuccess() {
                future.complete(true);
            }

            @Override
            public void onFailure(String message) {
                future.complete(false);
            }
        });

        assertTrue(future.get(4, TimeUnit.SECONDS));
    }

    @Test
    public void testRegisterUser() throws ExecutionException, InterruptedException, TimeoutException {

        // Already exist in db.
        String email = "lukas@example.com";
        String username = "Lukiss";
        String pwd = "Test*409";

        CompletableFuture<Boolean> future = new CompletableFuture<>();
        mFirebaseRepository.registerUser(email, username, pwd, new OnTaskCompletionListener() {
            @Override
            public void onSuccess() {
                future.complete(true);
            }

            @Override
            public void onFailure(String message) {
                future.complete(false);
            }
        });

        assertFalse(future.get(4, TimeUnit.SECONDS));
    }

    @Test
    public void testResetPassword() throws ExecutionException, InterruptedException, TimeoutException {
        String email = "invalid@example.com";

        CompletableFuture<Boolean> future = new CompletableFuture<>();
        mFirebaseRepository.resetPassword(email, new OnTaskCompletionListener() {
            @Override
            public void onSuccess() {
                future.complete(true);
            }

            @Override
            public void onFailure(String message) {
                future.complete(false);
            }
        });

        assertFalse(future.get(5, TimeUnit.SECONDS));
    }

    @Test
    public void testGetCurrentUserData() throws ExecutionException, InterruptedException, TimeoutException {

        CompletableFuture<Object> future = new CompletableFuture<>();
        mFirebaseRepository.getCurrentUserData(new OnDataFetchListener() {
            @Override
            public void onFetchSuccess(Object obj) {
                future.complete(obj);
            }

            @Override
            public void onFetchFailure(String message) {
                future.complete(null);
            }
        });

        assertNotNull(future.get(4, TimeUnit.SECONDS));
    }

    @Test
    public void testGenerateTempAccessKey() throws ExecutionException, InterruptedException, TimeoutException {
        CompletableFuture<Object> future = new CompletableFuture<>();
        mFirebaseRepository.generateTempAccessKey(new OnDataFetchListener() {
            @Override
            public void onFetchSuccess(Object obj) {
                future.complete(obj);
            }

            @Override
            public void onFetchFailure(String message) {
                future.complete(null);
            }
        });

        assertNotNull(future.get(4, TimeUnit.SECONDS));
    }

    @Test
    public void testAddMovieToFavourites() throws ExecutionException, InterruptedException, TimeoutException {
        MovieModel testMovieModel = new MovieModel();

        CompletableFuture<Boolean> future = new CompletableFuture<>();
        mFirebaseRepository.addMovieToFavourites(testMovieModel, new OnTaskCompletionListener() {
            @Override
            public void onSuccess() {
                future.complete(true);
            }

            @Override
            public void onFailure(String message) {
                future.complete(false);
            }
        });

        assertFalse(future.get(5, TimeUnit.SECONDS));
    }

    @Test
    public void testSignOut() {
        mFirebaseRepository.signOutUser();
        assertNull(mFirebaseRepository.getCurrentUser());
    }
}