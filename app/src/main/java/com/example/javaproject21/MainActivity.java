package com.example.javaproject21;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private static final String TAG = "MainActivity";
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private FrameLayout frameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        ActionBarDrawerToggle toggle=   new ActionBarDrawerToggle(this,drawer,
                R.string.drawer_oprn,R.string.drawer_closed);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        FragmentTransaction transaction= getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer,new MainFragment());
        transaction.commit();

    }

    @Override
    protected void onStart() {
        super.onStart();
      if(Utils.getClassName()==null) {
          checkForClassName();
      }else
      {
          checkForCR();
      }

    }

    private void checkForClassName() {

            String email= FirebaseAuth.getInstance().getCurrentUser().getEmail();
            DocumentReference docRef = FirebaseFirestore.getInstance().collection("Users").document(email);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            String className=document.getData().get("currentClass").toString();
                            if(className.equals("empty")){
                                startActivity(new Intent(getApplicationContext(),ChooseClassActivity.class));
                                finish();
                            }else {
                                Utils.setClassName(className);
                                String name = Objects.requireNonNull(document.getData().get("name")).toString();
                                Utils.setUserName(name);
                                checkForCR();
                            }

                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                       if(getConnectionType(MainActivity.this)==0){
                           AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                           alertDialogBuilder.setTitle("No Internet").setMessage("Please connect to internet for full functionality")
                                   .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                       public void onClick(DialogInterface dialog, int which) {
                                           dialog.dismiss();
                                       }
                                   }).show();
                       }
                        checkForClassName();
                    }
                }
            });

    }
    private void init(){
        Log.d(TAG, "init: mainactivity started");
        drawer=findViewById(R.id.drawer);
        navigationView= findViewById(R.id.navigationLayer);
        frameLayout= findViewById(R.id.fragmentContainer);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.profile:
                Intent intent= new Intent(getApplicationContext(),ProfileActivity.class);
                intent.putExtra("fromActivity","MainActivity");
                startActivity(intent);
                //Toast.makeText(this, "profile selected", Toast.LENGTH_SHORT).show();
                break;
            case R.id.logout:
                logout();
                //Toast.makeText(this, "logout selected", Toast.LENGTH_SHORT).show();
                break;
            case R.id.about:
                Toast.makeText(this, "about selected", Toast.LENGTH_SHORT).show();
                break;
            case R.id.contact:
                Toast.makeText(this, "contact us selected", Toast.LENGTH_SHORT).show();

                break;

            default:
                break;
        }
        return false;
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Utils.setCR(null);
        Utils.setClassName(null);
        Utils.setUserName(null);
        Utils.setCR2(null);
        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        finish();
    }


    private void checkForCR() {
        Log.d(TAG, "checkForCR: started");
        if(Utils.getCR()==null){

            String email= FirebaseAuth.getInstance().getCurrentUser().getEmail();
            final DocumentReference docRef = FirebaseFirestore.getInstance().collection(Utils.getClassName()).document("classroomDetails");
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            String CRName= Objects.requireNonNull(document.getData()).get("currentCR").toString();
                            String Desc=document.getData().get("description").toString();
                            String code=document.getData().get("invitationCode").toString();
                            String cr2=document.getData().get("currentCR2").toString();
                             Utils.setCR(CRName);
                            if(!cr2.equals("n/a")){
                                Utils.setCR2(cr2);
                            }
                            Utils.setClassDescription(Desc);
                            Utils.setInvitationCode(code);
                            subscribeToClass();
                        } else {
                            Log.d(TAG, "No such document");
                            checkForCR();
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
        }
        }



    private void subscribeToClass(){
        FirebaseMessaging.getInstance().subscribeToTopic(Utils.getClassName())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Subscribe to "+Utils.getClassName()+" done";
                        if (!task.isSuccessful()) {
                            msg = "Subscription to cse18 failed";
                        }
                        Log.d(TAG, msg);
                       // Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }
    @IntRange(from = 0, to = 2)
    public static int getConnectionType(Context context) {
        int result = 0; // Returns connection type. 0: none; 1: mobile data; 2: wifi
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (cm != null) {
                NetworkCapabilities capabilities = cm.getNetworkCapabilities(cm.getActiveNetwork());
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        result = 2;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        result = 1;
                    }
                }
            }
        } else {
            if (cm != null) {
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                if (activeNetwork != null) {
                    // connected to the internet
                    if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                        result = 2;
                    } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                        result = 1;
                    }
                }
            }
        }
        return result;
    }
}
