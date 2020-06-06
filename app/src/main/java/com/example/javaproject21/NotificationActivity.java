package com.example.javaproject21;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * The class for Notification activity.
 */
public class NotificationActivity extends AppCompatActivity {
    /**
     * The constant variable for logcat.
     */
private static final String TAG = "NotificationActivity";
    /**
     * The Button for open dialog.
     */
    Button btnOpenDialog;
    /**
     * The RequestQueue variable.
     */
private RequestQueue mRequestQue;
    /**
     * The String for Url.
     */
private String URL = "https://fcm.googleapis.com/fcm/send";
    /**
     * The RecyclerView variable.
     */
    RecyclerView recyclerView;
    /**
     * The NotificationAdapter variable.
     */
    NotificationAdapter adapter;
    /**
     * The ImageButton for back.
     */
    ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        recyclerView=findViewById(R.id.recyclerView);
        mRequestQue = Volley.newRequestQueue(this);
        initRecView();

        btnBack=findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

    /**
     * This method sets adapter with the recycler view on the
     * notification activity.
     */
private void initRecView() {
        Query query=         FirebaseFirestore.getInstance().collection("Classrooms").document(Utils.getClassName()).collection("Notifications")
                .orderBy("priority",Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Notification> options=new FirestoreRecyclerOptions.Builder<Notification>()
                .setQuery(query,Notification.class)
                .build();
        adapter= new NotificationAdapter(options);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }






    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
