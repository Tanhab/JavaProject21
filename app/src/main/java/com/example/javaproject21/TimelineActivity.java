package com.example.javaproject21;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

/**
 * The class for Timeline activity.
 */
public class TimelineActivity extends AppCompatActivity {
    /**
     * The constant variable for logcat.
     */
private static final String TAG = "TimelineActivity";
    /**
     * The TextView for toolbar.
     */
private TextView mainToolbar;
    /**
     * The FirebaseAuth variable.
     */
private FirebaseAuth mAuth;
    /**
     * The FirebaseFirestore variable.
     */
private FirebaseFirestore firebaseFirestore;
    /**
     * The String for Current user id.
     */
private String current_user_id;
    /**
     * The Btn add post.
     */
private FloatingActionButton btnAddPost;
    /**
     * The BottomNavigationView variable.
     */
private BottomNavigationView mainbottomNav;
    /**
     * The ImageButton for back.
     */
private ImageButton btnBack;

    /**
     * Declaration of Timeline fragment.
     */
private TimelineFragment timelineFragment;
    /**
     * Declaration of Poll fragment.
     */
private PollFragment pollFragment;
    /**
     * Declaration of Events fragment.
     */
private EventsFragment eventsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        mAuth = FirebaseAuth.getInstance();
        mainToolbar=findViewById(R.id.main_toolbar);
        firebaseFirestore = FirebaseFirestore.getInstance();
        timelineFragment = new TimelineFragment();
        eventsFragment = new EventsFragment();
        pollFragment = new PollFragment();
        btnBack=findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        /*mainToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);
        getSupportActionBar().setTitle("Classroom Timeline");*/
        


        if (mAuth.getCurrentUser() != null) {

            mainbottomNav = findViewById(R.id.mainBottomNav);

            // FRAGMENTS

            initializeFragment();

            mainbottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.main_container);

                    switch (item.getItemId()) {

                        case R.id.bottom_action_timeline:

                            replaceFragment(timelineFragment, currentFragment);
                            return true;

                        case R.id.bottom_action_poll:

                            replaceFragment(pollFragment, currentFragment);
                            return true;

                        case R.id.bottom_action_notif:

                            replaceFragment(eventsFragment, currentFragment);
                            return true;

                        default:
                            return false;


                    }

                }
            });


        }
        if(getIntent().hasExtra("EXTRA")) {
            if ("PollFragment".equals(Objects.requireNonNull(getIntent().getStringExtra("EXTRA")))) {
                Log.d(TAG, "onCreate: pollFragment found");
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.main_container);
                mainbottomNav.setSelectedItemId(R.id.bottom_action_poll);
                replaceFragment(pollFragment,currentFragment);

            }else if ("TimelineFragment".equals(Objects.requireNonNull(getIntent().getStringExtra("EXTRA")))) {
                Log.d(TAG, "onCreate: timelineFragment found");
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.main_container);
                replaceFragment(timelineFragment,currentFragment);

            }else if ("EventsFragment".equals(Objects.requireNonNull(getIntent().getStringExtra("EXTRA")))) {
                Log.d(TAG, "onCreate: eventFragment found");
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.main_container);
                replaceFragment(eventsFragment,currentFragment);
                mainbottomNav.setSelectedItemId(R.id.bottom_action_notif);


            }
        }

    }

    /**
     *This method handles the initialization of timeline, poll
     * and events fragments .
     */
private void initializeFragment(){
        Log.d(TAG, "initializeFragment: called");

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.add(R.id.main_container, timelineFragment);
        fragmentTransaction.add(R.id.main_container, eventsFragment);
        fragmentTransaction.add(R.id.main_container, pollFragment);


        fragmentTransaction.hide(eventsFragment);
        fragmentTransaction.hide(pollFragment);

        fragmentTransaction.commit();

    }

    /**
     * This method handles the transaction of the fragments.When
     * one fragment is under view then other fragments are kept hiden.
     *
     * @param fragment        the fragment
     * @param currentFragment the current fragment
     */
private void replaceFragment(Fragment fragment, Fragment currentFragment){

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if(fragment == timelineFragment){
            mainToolbar.setText("Classroom Timeline");

            fragmentTransaction.hide(pollFragment);
            fragmentTransaction.hide(eventsFragment);

        }

        if(fragment == pollFragment){
            mainToolbar.setText("Classroom Polls");
            fragmentTransaction.hide(timelineFragment);
            fragmentTransaction.hide(eventsFragment);

        }

        if(fragment == eventsFragment){

            mainToolbar.setText("Classroom Events");

            fragmentTransaction.hide(pollFragment);
            fragmentTransaction.hide(timelineFragment);

        }
        fragmentTransaction.show(fragment);

        fragmentTransaction.replace(R.id.main_container, fragment);
        fragmentTransaction.commit();

    }




}
