package com.example.javaproject21;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * The class for Create post activity.
 */
public class CreatePostActivity extends AppCompatActivity {
    private static final String TAG = "CreatePostActivity";

    /**
     * The TextView for name.
     */
private TextView txtName;
    /**
     * The TextView for date.
     */
private TextView txtDate;
    /**
     * The CircularImageView for Profile pic.
     */
private CircleImageView profilePic;
    /**
     * The EditText for message.
     */
private EditText edtMessage;
    /**
     * The Button for add post.
     */
private Button btnAddPost;

    /**
     * The RequestQueue variable.
     */
private RequestQueue mRequestQue;
    /**
     * The String for Url.
     */
private String URL = "https://fcm.googleapis.com/fcm/send";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        txtDate=findViewById(R.id.txtDate);
        txtName=findViewById(R.id.txtName);
        profilePic=findViewById(R.id.postProfilePic);
        edtMessage=findViewById(R.id.edtMessage);
        btnAddPost=findViewById(R.id.btnPost);
        mRequestQue = Volley.newRequestQueue(this);
        Calendar calendar = Calendar.getInstance();
        int HOUR = calendar.get(Calendar.HOUR);
        int MINUTE = calendar.get(Calendar.MINUTE);
        int YEAR = calendar.get(Calendar.YEAR);
        int MONTH = calendar.get(Calendar.MONTH);
        int DATE = calendar.get(Calendar.DATE);

        String date =DateFormat.format("dd.MM.yy", calendar).toString();

        txtName.setText(Utils.getUserName());
        txtDate.setText(date);
        Glide.with(this).load(Utils.getImageUri()).placeholder(R.drawable.ic_profile_empty).into(profilePic);

        btnAddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg= edtMessage.getText().toString();
                if(msg.length()>1)
                addPost(msg);
            }
        });


    }

    /**
     * This method creates post by taking an object from the post class
     * and the post is being added to the database.
     *
     * @param msg the message for the post
     */
private void addPost(String msg) {
        Log.d(TAG, "addPost: started");
        Calendar calendar = Calendar.getInstance();
        String email= FirebaseAuth.getInstance().getCurrentUser().getEmail();
        int HOUR = calendar.get(Calendar.HOUR);
        int MINUTE = calendar.get(Calendar.MINUTE);
        int YEAR = calendar.get(Calendar.YEAR);
        int MONTH = calendar.get(Calendar.MONTH);
        int DATE = calendar.get(Calendar.DATE);
        int SECOND= calendar.get(Calendar.SECOND);
        long a=YEAR*100+MONTH;
        a=a*100+DATE;
        a=a*100+HOUR;
        a=a*100+MINUTE;
        a=a*100+SECOND;
        String time = DateFormat.format("h:mm a", calendar).toString();
        String date =DateFormat.format("dd.MM.yy", calendar).toString();
        String postId=email+ String.valueOf(a);
         date= time + " " + date;

        final Post post= new Post(Utils.getUserName(),Utils.getImageUri(),msg,date,postId,a);
        FirebaseFirestore.getInstance().collection("Classrooms").document(Utils.getClassName())
                .collection("Posts").document(postId).set(post)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        edtMessage.setText("");
                        sendNotificationData(Utils.getUserName()+" posted in classroom timeline",post.getDate(),post.getPriority(),post.getMessage());

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });


    }

    /**
     This method send data to an object of the notification class and another
     * method is called to send the notification to all the users.
     *
     * @param s        the title of the notification
     * @param date     the date of the notification
     * @param priority the priority of the notification
     * @param message  the message of the notification
     */
private void sendNotificationData(final String s, String date, long priority, final String message) {
        Notification notification= new Notification(s,date,Utils.getUserName(),Utils.getImageUri(),"TimelineFragment",priority);
        FirebaseFirestore.getInstance().collection("Classrooms").document(Utils.getClassName()).collection("Notifications")
                .add(notification).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                    sendNotification(s,message);
                    Intent intent=new Intent(getApplicationContext(),TimelineActivity.class);
                    intent.putExtra("EXTRA","TimelineFragment");
                    startActivity(intent);
                    finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * This method sends notification to all the users under the classroom.
     *
     * @param title the title of the notification
     * @param body  the body of the notification
     */
private void sendNotification(String title, String body) {

        JSONObject json = new JSONObject();
        try {
            json.put("to","/topics/"+Utils.getTopic());
            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title",title);
            notificationObj.put("body",body);

            JSONObject extraData = new JSONObject();
            extraData.put("category","classRoutine");



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
}
