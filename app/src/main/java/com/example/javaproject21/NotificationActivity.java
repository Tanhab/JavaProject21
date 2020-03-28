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

public class NotificationActivity extends AppCompatActivity {
    private static final String TAG = "NotificationActivity";
    Button btnOpenDialog;
    private RequestQueue mRequestQue;
    private String URL = "https://fcm.googleapis.com/fcm/send";
    RecyclerView recyclerView;
    NotificationAdapter adapter;
    ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        btnOpenDialog = findViewById(R.id.btnOpenDialog);
        recyclerView=findViewById(R.id.recyclerView);
        mRequestQue = Volley.newRequestQueue(this);
        subscribeToClass();
        initRecView();
        btnOpenDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNotiDialog();

            }
        });
        btnBack=findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }
    private void initRecView() {
        Query query=         FirebaseFirestore.getInstance().collection(Utils.getClassName()).document("Data").collection("Notifications")
                .orderBy("priority",Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Notification> options=new FirestoreRecyclerOptions.Builder<Notification>()
                .setQuery(query,Notification.class)
                .build();
        adapter= new NotificationAdapter(options);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }
    private void openNotiDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.add_notification, null);

        Button acceptButton = view.findViewById(R.id.btnSend);
        Button cancelButton = view.findViewById(R.id.btnCancel);
        final EditText edtTitle, edtBodey;

        edtTitle = view.findViewById(R.id.txtTitleInput);
        edtBodey = view.findViewById(R.id.txtNotificationBodyInput);
        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "onClick: accept button");
                if (edtTitle.getText().toString().length() < 1) {
                    edtTitle.setError("Can't be empty");

                    edtTitle.setFocusable(true);
                } else if (edtBodey.getText().toString().length() < 1) {
                    edtBodey.setError("Can't be empty");
                    edtBodey.setFocusable(true);
                }else
                {
                    String title = edtTitle.getText().toString().trim();
                    String body = edtBodey.getText().toString().trim();

                    addToDatabase(title,body);
                    alertDialog.dismiss();
                }


            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "onClick: cancel button");

                alertDialog.dismiss();
            }
        });

        alertDialog.show();


    }

    private void addToDatabase(final String title, final String body) {
        String email= FirebaseAuth.getInstance().getCurrentUser().getEmail();
        Calendar calendar = Calendar.getInstance();
        int HOUR = calendar.get(Calendar.HOUR);
        int MINUTE = calendar.get(Calendar.MINUTE);
        int YEAR = calendar.get(Calendar.YEAR);
        int MONTH = calendar.get(Calendar.MONTH);
        int DATE = calendar.get(Calendar.DATE);
        long a=YEAR*100+MONTH;
        a=a*100+DATE;
        a=a*100+HOUR;
        a=a*100+MINUTE;
        String time = DateFormat.format("h:mm a", calendar).toString();
        String date =DateFormat.format("dd.MM.yy", calendar).toString();
        date= time + " " + date;
        Notification notification= new Notification(title,body,date,email,a);
        FirebaseFirestore.getInstance().collection(Utils.getClassName()).document("Data").collection("Notifications")
                .add(notification).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG, "onSuccess: "+documentReference.toString());
                sendNotification(title,body);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
                Toast.makeText(NotificationActivity.this, "Notification sending failed.Please check your internet connection.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendNotification(String title, String body) {

        JSONObject json = new JSONObject();
        try {
            json.put("to","/topics/"+Utils.getClassName());
            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title",title);
            notificationObj.put("body",body);

            JSONObject extraData = new JSONObject();
            extraData.put("brandId","puma");
            extraData.put("category","Shoes");



            json.put("notification",notificationObj);
            json.put("data",extraData);


            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL,
                    json,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("MUR", "onResponse: ");
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("MUR", "onError: "+error.networkResponse);
                }
            }
            ){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> header = new HashMap<>();
                    header.put("content-type","application/json");
                    header.put("authorization","key=AIzaSyBJDuo2BaJ9bjqLpnGcE_BY4oD282gF64M");
                    return header;
                }
            };
            mRequestQue.add(request);
        }
        catch (JSONException e)

        {
            e.printStackTrace();
        }
    }
    private void subscribeToClass(){
        FirebaseMessaging.getInstance().subscribeToTopic(Utils.getClassName())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Subscribe to cse18 done";
                        if (!task.isSuccessful()) {
                            msg = "Subscription to cse18 failed";
                        }
                        Log.d(TAG, msg);
                        //Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    }
                });
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
