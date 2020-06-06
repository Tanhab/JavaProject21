package com.example.javaproject21;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
 * The class for Create poll activity.
 */
public class CreatePollActivity extends AppCompatActivity {
    /**
     * The constant variable for logcat.
     */
    private static final String TAG = "CreatePollActivity";

    /**
     * The TextView for name.
     */
private TextView txtName;
    /**
     * TextView for date.
     */
private TextView txtDate;
    /**
     * TextView for options.
     */
private TextView txtOptions;
    /**
     * The CircularImageView for Profile pic.
     */
private CircleImageView profilePic;
    /**
     * The EditText for message.
     */
private EditText edtMessage;
    /**
     * The Edit Text for option.
     */
    private EditText edtOption;
    /**
     * The Button for add post.
     */
private Button btnAddPost;
    /**
     * The ImageButton for add option.
     */
private ImageButton btnAddOption;
    /**
     * The String for Finaltext.
     */
    String finaltext;
    /**
     * The String for Poll id.
     */
    String pollId;
    /**
     * The String for Email.
     */
    String email;
    /**
     * The String for Notification date.
     */
    String notificationDate;
    /**
     * The String for Notification priority.
     */
    long notificationPriority;
    /**
     * The ProgressDialog variable.
     */
    ProgressDialog pd;
    /**
     * The list variable for Final list.
     */
    List<PollOption> finalList;
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
        setContentView(R.layout.activity_create_poll);
        txtDate=findViewById(R.id.txtDate);
        txtName=findViewById(R.id.txtName);
        mRequestQue = Volley.newRequestQueue(this);
        pd=new ProgressDialog(this);
        pd.setCancelable(false);
        pd.setTitle("Please wait...");

        finaltext="";
        email= FirebaseAuth.getInstance().getCurrentUser().getEmail();
        finalList=new ArrayList<>();
        profilePic=findViewById(R.id.postProfilePic);
        edtMessage=findViewById(R.id.edtMessage);
        btnAddPost=findViewById(R.id.btnPost);
        edtOption=findViewById(R.id.edtPollOption);
        btnAddOption=findViewById(R.id.btnAddOption);
        txtOptions=findViewById(R.id.txtOptions);
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
        pollId=email+ String.valueOf(a);
        date= time + " " + date;

        txtName.setText(Utils.getUserName());
        txtDate.setText(date);
        Glide.with(this).load(Utils.getImageUri()).placeholder(R.drawable.ic_profile_empty).into(profilePic);

        btnAddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg= edtMessage.getText().toString();
                if(msg.length()>1&& finaltext.length()>1) {

                    addPoll(msg);
                }
                else Toast.makeText(CreatePollActivity.this, "Description and Options can not be empty", Toast.LENGTH_SHORT).show();
            }
        });
        btnAddOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String optionName= edtOption.getText().toString();
                if(optionName.length()>0)
                {
                    //public PollOption(String pollName, String optionId, String pollId, int count)
                    PollOption p= new PollOption(optionName,optionName,pollId,0);
                    finalList.add(p);
                    edtOption.setText("");
                    finaltext=finaltext+"Option "+finalList.size()+ " : "+ optionName +"\n";
                    txtOptions.setText(finaltext);

                }else{
                    Toast.makeText(CreatePollActivity.this, "Option Name can not be empty.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * This method creates poll by taking an object from the poll class
     * and the poll is being added to the database.
     *
     * @param msg the message for the poll
     */
private void addPoll(String msg) {
        pd.show();
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
        notificationDate=date;
        //pollId=email+ String.valueOf(a);
        date= time + " " + date;
        notificationPriority=a;

        final Poll poll= new Poll(Utils.getUserName(),msg,date,pollId,Utils.getImageUri(),a);
        FirebaseFirestore.getInstance().collection("Classrooms").document(Utils.getClassName()).collection("Polls")
                .document(pollId).set(poll).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                updatePollOptions(poll.getMessage());

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                e.printStackTrace();
            }
        });

    }

    /**
     * This method updates the options of the poll that has been created to the
     * database.
     *
     * @param message the message of the poll.
     */
private void updatePollOptions(final String message) {
        for (PollOption p : finalList){
            FirebaseFirestore.getInstance().collection("Classrooms").document(Utils.getClassName()).collection("Polls").document(pollId).
                    collection("Options")
                    .document(p.getOptionId()).set(p)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            finalList.clear();
                            finaltext="";
                           // startActivity(new Intent(getApplicationContext(),PollFragment.class));




                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pd.dismiss();
                    e.printStackTrace();
                }
            });

        }
        sendNotificationData(Utils.getUserName()+" created a poll",notificationDate,notificationPriority,message);
    }

    /**
     * This method send data to an object of the notification class and another
     * method is called to send the notification to all the users.
     *
     * @param s        the title of the notification
     * @param date     the date of the notification
     * @param priority the priority of the notification
     * @param message  the message of the notification
     */
private void sendNotificationData(final String s, String date, long priority, final String message) {
        Notification notification= new Notification(s,date,Utils.getUserName(),Utils.getImageUri(),"PollFragment",priority);
        FirebaseFirestore.getInstance().collection("Classrooms").document(Utils.getClassName()).collection("Notifications")
                .add(notification).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                sendNotification(s,message);
                pd.dismiss();
                Intent intent= new Intent(getApplicationContext(),TimelineActivity.class);
                intent.putExtra("EXTRA","PollFragment");
                startActivity(intent);
                finish();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
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
