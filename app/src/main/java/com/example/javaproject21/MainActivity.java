package com.example.javaproject21;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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

/**
 * The class for Main activity.
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    /**
     * The constant variable for logcat.
     */
private static final String TAG = "MainActivity";
    /**
     * The DrawerLayout variable.
     */
private DrawerLayout drawer;
    /**
     * The NavigationView variable.
     */
private NavigationView navigationView;
    /**
     * The FrameLayout variable.
     */
private FrameLayout frameLayout;
    /**
     * The ProgressDialog variable.
     */
private ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pd=new ProgressDialog(this);
        pd.setTitle("Please wait...");
        pd.setCancelable(false);
        init();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,
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
          pd.show();
          checkForClassName();
      }else
      {

          checkForCR();
      }

    }

    /**
     * This method checks whether the user is included in a class or not.If the class name is empty the
     * user is taken to choose class activity otherwise if the user has not filled his profile,
     * he is taken to the profile activity.
     */
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
                            String name = Objects.requireNonNull(document.getData().get("name")).toString();
                            String nickName = Objects.requireNonNull(document.getData().get("nickName")).toString();
                            if(className.equals("empty")){
                                pd.dismiss();
                                startActivity(new Intent(getApplicationContext(),ChooseClassActivity.class));
                                finish();
                            }else if(name.equals("user")||nickName.equals("user")){
                                Toast.makeText(MainActivity.this, "Please provide required information in profile.", Toast.LENGTH_SHORT).show();
                                Intent intent= new Intent(getApplicationContext(),ProfileActivity.class);
                                intent.putExtra("fromActivity","mainActivity");
                                startActivity(intent);
                                finish();
                            }

                            else {

                                Utils.setClassName(className);
                                Utils.setUserName(name);
                                Utils.setNickName(nickName);


                                String imageUri=document.getData().get("imageUri").toString();
                                Utils.setImageUri(imageUri);

                                checkForCR();
                            }

                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                        pd.dismiss();
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

    /**
     * This method finds the navigation drawer,view and frame layout variable.
     */
private void init(){
        Log.d(TAG, "init: mainactivity started");
        drawer=findViewById(R.id.drawer);
        navigationView= findViewById(R.id.navigationLayer);
        frameLayout= findViewById(R.id.fragmentContainer);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.crZone:
                /*Intent intent= new Intent(getApplicationContext(),CRActivity.class);
                intent.putExtra("fromActivity","MainActivity");
                startActivity(intent);*/
                if(isCR())
                    startActivity(new Intent(getApplicationContext(),CRActivity.class));
                else{
                    Toast.makeText(getApplicationContext(), "Sorry, but you are not a CR.", Toast.LENGTH_LONG).show();
                }
                //Toast.makeText(this, "profile selected", Toast.LENGTH_SHORT).show();
                break;
            case R.id.logout:
                unsubscribeToClass();

                //Toast.makeText(this, "logout selected", Toast.LENGTH_SHORT).show();
                break;
            case R.id.profile:
               // Toast.makeText(this, "About us selected", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getApplicationContext(),ProfileActivity.class);
                intent.putExtra("fromActivity","MainActivity");
                startActivity(intent);
                break;
            case R.id.contact:
                Toast.makeText(this, "About us selected", Toast.LENGTH_SHORT).show();

                break;

            default:
                break;
        }
        return false;
    }

    /**
     * This method handles the logging out of the user.The user is taken
     * back to the login activity.
     */
private void logout() {
        FirebaseAuth.getInstance().signOut();
        Utils.setCR(null);
        Utils.setClassName(null);
        Utils.setUserName(null);
        Utils.setCR2(null);

        Utils.setImageUri("empty");
        Intent intent= new Intent(getApplicationContext(),LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }


    /**
     * This method checks for the CR of the class.
     */
private void checkForCR() {
        Log.d(TAG, "checkForCR: started");
        if(Utils.getCR()==null){

            String email= FirebaseAuth.getInstance().getCurrentUser().getEmail();
            final DocumentReference docRef = FirebaseFirestore.getInstance().collection("Classrooms").document(Utils.getClassName());
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
                             Utils.setCR2("n/a");
                            if(!cr2.equals("n/a")){
                                Utils.setCR2(cr2);
                            }
                            Utils.setClassDescription(Desc);
                            Utils.setInvitationCode(code);
                            checkForNames();

                        } else {
                            Log.d(TAG, "No such document");
                            checkForCR();
                        }
                    } else {
                        pd.dismiss();
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
        }
        }

    /**
     * This method checks whether the user has filled up the name and nickname
     * of the profile activity.Subscription to class is called if those are filled.
     */
private void checkForNames() {

        String email= FirebaseAuth.getInstance().getCurrentUser().getEmail();
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("Users").document(email);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        String className = document.getData().get("currentClass").toString();
                        String name = Objects.requireNonNull(document.getData().get("name")).toString();
                        String nickName = Objects.requireNonNull(document.getData().get("nickName")).toString();
                         if (name.equals("user") || nickName.equals("user")) {
                            Toast.makeText(MainActivity.this, "Please provide required information in profile.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                            intent.putExtra("fromActivity", "mainActivity");
                            startActivity(intent);
                            finish();
                        } else {

                            Utils.setClassName(className);
                            Utils.setUserName(name);
                            Utils.setNickName(nickName);


                            String imageUri = document.getData().get("imageUri").toString();
                            Utils.setImageUri(imageUri);

                            subscribeToClass();
                        }

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                    pd.dismiss();
                }
            }
    });
    }

    /**
     * This method converts the class name into a unique hash value.
     *
     * @param s the class name
     * @return the string of the hashed value
     */
    String  compute_hash(String s) {
     int p = 31;
     int m =(int) 1e9 + 9;
        long  hash_value = 0;
        long  p_pow = 1;
        int n=s.length();
        for (int i=0;i<n;i++) {
            char c=s.charAt(i);
            hash_value = (hash_value + (c - 'a' + 1) * p_pow) % m;
            p_pow = (p_pow * p) % m;
        }
        hash_value=Math.abs(hash_value);
        return String.valueOf(hash_value);
    }

    /*
     *This method handles subscription to a class and sends a message to the user informing the success or failure of
     * subscription to a particular task.
     */
    private void subscribeToClass(){
        String topic=compute_hash(Utils.getClassName());
        Log.d(TAG, "subscribeToClass: topic "+ topic);
        Utils.setTopic(topic);
        FirebaseMessaging.getInstance().subscribeToTopic(topic)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Subscribe to "+Utils.getTopic()+" done";

                        pd.dismiss();
                        if (!task.isSuccessful()) {
                            pd.dismiss();
                            msg = "Subscription to cse18 failed";
                        }
                        Log.d(TAG, msg);
                       // Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }


    /**
     * This method handles the unsubscribe from a class and sends message informing
     * about it.
     */
private void unsubscribeToClass(){
        String topic=Utils.getTopic();
        Log.d(TAG, "unsubscribeToClass: topic "+ topic);

        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "unSubscribe to "+Utils.getTopic()+" done";
                        Utils.setTopic("");
                        logout();

                        if (!task.isSuccessful()) {

                            msg = "unSubscription to cse18 failed";
                        }
                        Log.d(TAG, msg);
                       // Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * This method checks the net connection type of the user.
     *
     * @param context the context
     * @return the connection type
     */
    @IntRange(from = 0, to = 2)
    //public
    static int getConnectionType(Context context) {
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

    /**
     * This method checks whether the user is CR or not .
     *
     * @return the boolean
     */
private boolean isCR() {
        String email= FirebaseAuth.getInstance().getCurrentUser().getEmail();
        if(Utils.getCR().equals(email)|| Utils.getCR2().equals(email))
            return  true;
        else return  false;
    }
}
