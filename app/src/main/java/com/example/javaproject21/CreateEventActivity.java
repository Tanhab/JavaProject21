package com.example.javaproject21;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

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
import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * The class for Create event activity.
 */
public class CreateEventActivity extends AppCompatActivity {
    /**
     * The constant variable for logcat.
     */
    private static final String TAG = "CreateEventActivity";


    /**
     * The Edit Text for event name.
     */
    private   EditText edtEventName;

    /**
     * The Edit Text for event place.
     */
    private EditText edtEventPlace;

    /**
     * The Edit Text for message.
     */
    private EditText edtMsg;

    /**
     * The TextView for show date.
     */
    private  TextView txtShowDate;

    /**
     * The TextView for show time.
     */
    private  TextView txtShowTime;

    /**
     * The TextView for Username.
     */
    private TextView username;

    /**
     * The TextView for date.
     */
    private TextView mdate;

    /**
     * The ImageButton for date.
     */
    private ImageButton btnDate;
    /**
     * The ImageButton for time.
     */
    private ImageButton btnTime;

    /**
     * The String for Event date.
     */
    private String eventDate;
    /**
     * The String for time.
     */
    private String eventTime;

    /**
     * The Button for post.
     */
    private Button btnPost;

    /**
     * The RequestQueue variable.
     */
    private RequestQueue mRequestQue;

    /**
     * The String for Url.
     */
    private String URL = "https://fcm.googleapis.com/fcm/send";

    /**
     * The CircularImageView for Profile pic.
     */
    private CircleImageView profilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        username=findViewById(R.id.txtName);
        mdate=findViewById(R.id.txtDate);
        profilePic=findViewById(R.id.postProfilePic);
        edtEventName=findViewById(R.id.edtEventName);
        mRequestQue = Volley.newRequestQueue(this);
        edtEventPlace=findViewById(R.id.edtEventPlace);
        edtMsg=findViewById(R.id.edtMessage);
        edtEventName=findViewById(R.id.edtEventName);
        txtShowDate=findViewById(R.id.txtDateChosen);
        txtShowTime=findViewById(R.id.txtTimeChosen);
        btnDate=findViewById(R.id.btnDatePicker);
        btnTime=findViewById(R.id.btnTimePicker);
        btnPost=findViewById(R.id.btnPost);

        username.setText(Utils.getUserName());
        Calendar calendar = Calendar.getInstance();

        String time = DateFormat.format("h:mm a", calendar).toString();
        String date =DateFormat.format("dd.MM.yy", calendar).toString();

        date= time + " " + date;
        mdate.setText(date);
        Glide.with(this).load(Utils.getImageUri()).placeholder(R.drawable.prof).into(profilePic);

        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleTimeButton();
            }
        });
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleDateButton();
            }
        });
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=edtEventName.getText().toString();
                String place= edtEventPlace.getText().toString();
                String msg=edtMsg.getText().toString();
                if(name.length()<1){
                    Toast.makeText(CreateEventActivity.this, "Event Name can not be empty", Toast.LENGTH_SHORT).show();
                    edtEventName.setFocusable(true);
                }else if(place.length()<1){
                    Toast.makeText(CreateEventActivity.this, "Event Place can not be empty", Toast.LENGTH_SHORT).show();
                    edtEventPlace.setFocusable(true);
                }else if(eventDate.length()<1||eventTime.length()<1){
                    Toast.makeText(CreateEventActivity.this, "Please select Date and Time ", Toast.LENGTH_SHORT).show();
                }
                    else{
                    createEvent(name,place,msg);
                }
            }
        });


    }


    /**
     * This method creates event by taking an object from the event class
     * and the event is being added to the database.
     *
     * @param name  the name of the event
     * @param place the place of the event
     * @param msg   the message of the event
     */
   private void createEvent(String name, String place, final String msg) {
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
        String postId= email+ a;
        date= time + " " + date;
        Event event= new Event(name,place,Utils.getUserName(),Utils.getImageUri(),msg,eventTime,eventDate,date,postId,a);
        final String finalDate = date;
        final long finalA = a;
        FirebaseFirestore.getInstance().collection("Classrooms").document(Utils.getClassName()).collection("Events")
                .document(postId).set(event).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                sendNotificationData(Utils.getUserName()+" created an Event", finalDate, finalA,msg);

            }
        });



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
        Notification notification= new Notification(s,date,Utils.getUserName(),Utils.getImageUri(),"EventsFragment",priority);
        FirebaseFirestore.getInstance().collection("Classrooms").document(Utils.getClassName()).collection("Notifications")
                .add(notification).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                sendNotification(s,message);
                Intent intent= new Intent(getApplicationContext(),TimelineActivity.class);
                intent.putExtra("EXTRA","EventsFragment");
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



    /**
     * This method handles the date button by taking an object from date picker dialog.
     */
    private void handleDateButton() {
        Calendar calendar = Calendar.getInstance();
        int YEAR = calendar.get(Calendar.YEAR);
        int MONTH = calendar.get(Calendar.MONTH);
        int DATE = calendar.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {

                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.YEAR, year);
                calendar1.set(Calendar.MONTH, month);
                calendar1.set(Calendar.DATE, date);
               eventDate = DateFormat.format("EEEE, MMM d, yyyy", calendar1).toString();

                txtShowDate.setText(eventDate);
            }
        }, YEAR, MONTH, DATE);

        datePickerDialog.show();




    }



    /**
     * This method handles the time button by taking an object from time picker dialog.
     */
    private void handleTimeButton() {
        Calendar calendar = Calendar.getInstance();
        int HOUR = calendar.get(Calendar.HOUR);
        int MINUTE = calendar.get(Calendar.MINUTE);
        boolean is24HourFormat = DateFormat.is24HourFormat(this);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                Log.i(TAG, "onTimeSet: " + hour + minute);
                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.HOUR, hour);
                calendar1.set(Calendar.MINUTE, minute);
                eventTime = DateFormat.format("h:mm a", calendar1).toString();
                txtShowTime.setText(eventTime);
            }
        }, HOUR, MINUTE, is24HourFormat);

        timePickerDialog.show();

    }

}
