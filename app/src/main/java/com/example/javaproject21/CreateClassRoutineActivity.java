package com.example.javaproject21;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.res.ResourcesCompat;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The class for Create class routine activity.
 */
public class CreateClassRoutineActivity extends AppCompatActivity {
    /**
     * The constant variable for logcat.
     */
    private static final String TAG = "CreateClassRoutine";
    /**
     * The SpeedDialView variable.
     */
private SpeedDialView mSpeedDialView;
    /**
     * The add section button.
     */
private ImageButton btnAddSection;
    /**
     * The back button.
     */
    private ImageButton  btnBack;
    /**
     * The Button for add date.
     */
private Button btnAddDate;
    /**
     * The Button for open dialog.
     */
    private Button  btnOpenDialog;
    /**
     * The Button for send routine.
     */
    private Button btnSendRoutine;
    /**
     * The TextView for routine date.
     */
private TextView txtRoutineDate;
    /**
     * The TextView for section.
     */
    private TextView txtSection;
    /**
     * The TextView for routine.
     */
    private TextView txtRoutine;
    /**
     * The String for Final date.
     */
    String finalDate;
    /**
     * The String for Section.
     */
    String section;
    /**
     * The String for Start time.
     */
    String startTime;
    /**
     * The String for Finish time.
     */
    String finishTime;
    /**
     * The String for Class name.
     */
    String className;
    /**
     * The String for Class description.
     */
    String classDescription;
    /**
     * The String for Final text.
     */
    String finalText="Classes :\n";
    /**
     * The ClassRoutine object.
     */
private ClassRoutine classRoutine;
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
        setContentView(R.layout.activity_create_class_routine);

       //btnOpenDialog=findViewById(R.id.btnOpenDialog);

        mRequestQue = Volley.newRequestQueue(this);
        txtRoutineDate=findViewById(R.id.txtRoutineDate);
        txtSection=findViewById(R.id.txtSection);
        txtRoutine=findViewById(R.id.txtRoutine);
        btnSendRoutine=findViewById(R.id.btnSendRoutine);
        classRoutine= new ClassRoutine();
        btnBack=findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });




        btnSendRoutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(finalDate.length()<1||section.length()<1)
                {
                    Toast.makeText(CreateClassRoutineActivity.this, "Date and Section can not be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                uploadRoutine();
            }
        });
        initSpeedDial(savedInstanceState == null);



    }

    /**
     * This method handles the functionality of the floating action button where speedDial
     * has been set.3 items: edit classes, edit section and edit date are included
     *
     * @param addActionItems boolean variable if true the action items will be added
     */
private void initSpeedDial(boolean addActionItems) {{
        mSpeedDialView = findViewById(R.id.speedDial);

        if (addActionItems) {


            Drawable drawable = AppCompatResources.getDrawable(getApplicationContext(), R.drawable.paper);
            mSpeedDialView.addActionItem(new SpeedDialActionItem.Builder(R.id
                    .fab_add_document, drawable)
                    .setFabImageTintColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimaryDark, getTheme()))
                    .setLabel("Edit Classes")
                    .setLabelColor(Color.WHITE)
                    .setLabelBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorAccent,
                            getTheme()))
                    .create());


            drawable = AppCompatResources.getDrawable(getApplicationContext(), R.drawable.ic_add_section);
            mSpeedDialView.addActionItem(new SpeedDialActionItem.Builder(R.id.fab_add_section, drawable)
                    .setFabBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.yellow,
                            getTheme()))
                    .setLabel("Edit section")
                    .setLabelBackgroundColor(Color.WHITE)
                    .create());

            mSpeedDialView.addActionItem(new SpeedDialActionItem.Builder(R.id.fab_add_date, R.drawable
                    .ic_calender)
                    .setFabBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.material_on_background_emphasis_high_type,
                            getTheme()))
                    .setLabel("Edit date")
                    .setLabelBackgroundColor(Color.YELLOW)
                    .create());

        }

        //Set main action clicklistener.
        mSpeedDialView.setOnChangeListener(new SpeedDialView.OnChangeListener() {
            @Override
            public boolean onMainActionSelected() {

                return false; // True to keep the Speed Dial open
            }

            @Override
            public void onToggleChanged(boolean isOpen) {
                Log.d(TAG, "Speed dial toggle state changed. Open = " + isOpen);
            }
        });

        //Set option fabs clicklisteners.
        mSpeedDialView.setOnActionSelectedListener(new SpeedDialView.OnActionSelectedListener() {
            @Override
            public boolean onActionSelected(SpeedDialActionItem actionItem) {
                switch (actionItem.getId()) {
                    case R.id.fab_add_document:
                        showDialog();
                         // To close the Speed Dial with animation
                        return true; // false will close it without animation
                    case R.id.fab_add_section:
                            addSectionDialog();

                        return false;
                    case R.id.fab_add_date:
                        handleDateButton();
                        return false; // closes without animation (same as mSpeedDialView.close(false); return false;)

                    default:
                        break;
                }
                return true; // To keep the Speed Dial open
            }
        });

    }
    }

    /**
     * This method creates an alert dialog to enter the section of the classes
     * and sets the section into the class routine.
     */
private void addSectionDialog() {
androidx.appcompat.app.AlertDialog.Builder builder= new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Enter Section");
        LinearLayout linearLayout= new LinearLayout(this);
        final EditText edtEmail = new EditText(this);
        edtEmail.setHint("Section");
        edtEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        edtEmail.setMinEms(16);

        linearLayout.addView(edtEmail);
        linearLayout.setPadding(20,10,20,10);
        builder.setView(linearLayout);
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String email= edtEmail.getText().toString().trim().toUpperCase();
                section= "Section: "+email;
                txtSection.setText(section);
                classRoutine.setSection(section);

            }
        });builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /**
     * This method  uploads the class routine to the database by
     * the date priority and calls a method for sending notification.
     */
private void uploadRoutine() {
        Log.d(TAG, "uploadRoutine: started");
        //TODO: cse18 er bodole class name ber kora lagbe
        classRoutine.setClasses(finalText);
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
            //public ClassRoutine(String routineDate, String section, String classes, String sender, String imageUri, String message, String date, String postID, long priority) ;

            ClassRoutine classRoutine1= new ClassRoutine(finalDate,section,finalText,Utils.getUserName(),Utils.getImageUri(),
                    "",date,postId,a);
        setPriority();
        final String finalDate1 = date;
        final long finalA = a;
        FirebaseFirestore.getInstance().collection("Classrooms").document(Utils.getClassName()).collection("ClassRoutines").document(classRoutine.getDate()+classRoutine.getSection())
                .set(classRoutine1).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                sendNotificationData("Class Routine for "+classRoutine.getDate(), finalDate1, finalA,finalText);

                section="";
                finalText="Classes: \n";
                finalDate="";
                classRoutine.setClasses("");
                classRoutine.setSection("");
                classRoutine.setDate("");

                txtRoutine.setText(finalText);
                txtRoutineDate.setText("Date");
                txtSection.setText("Section");
                Toast.makeText(CreateClassRoutineActivity.this, "Class Routine Added.", Toast.LENGTH_SHORT).show();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    /**
     * This method handles the priority of the class routine.The new
     * routine comes first.
     */
private void setPriority() {
        Calendar calendar= Calendar.getInstance();
        int YEAR = calendar.get(Calendar.YEAR);
        int MONTH = calendar.get(Calendar.MONTH);
        int DATE = calendar.get(Calendar.DATE);
        long a= YEAR*100+MONTH;
        a= a*100+DATE;
        classRoutine.setPriority(a);

    }

    /**
     * This methods handles the date of the class routine by creating datePickerDialog.
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
                String dateText = DateFormat.format("EEEE, MMM d, yyyy", calendar1).toString();
                txtRoutineDate.setText(dateText);
                classRoutine.setDate(dateText);
                finalDate=dateText;
            }
        }, YEAR, MONTH, DATE);

        datePickerDialog.show();

    }

    /**
     * This method creates an alert dialog for entering the classes with time
     * in a certain date for the class routine.
     */
    void showDialog() {

        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.add_class_alertdialog, null);

        Button acceptButton = view.findViewById(R.id.btnAddNewClass);
        Button cancelButton = view.findViewById(R.id.btnCancel);
        final EditText edtClasses;
      //  edtClassName=view.findViewById(R.id.txtClassInput);
      //  edtDesc=view.findViewById(R.id.txtDescInput);
       // edtStartTime=view.findViewById(R.id.edtStartingTime);
       edtClasses=view.findViewById(R.id.txtAllCLasses);
        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();



        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "onClick: accept button");
                if(edtClasses.getText().toString().length()<1)
                {
                    edtClasses.setError("Can't be empty");
                }else
                {
                    /*className=edtClassName.getText().toString().trim();
                    classDescription=edtDesc.getText().toString().trim();
                    startTime=edtStartTime.getText().toString().trim();
                    finishTime=edtFinishTime.getText().toString().trim();
                    if(classDescription.length()>0)
                        finalText= finalText+ startTime+" - "+finishTime+" : " + className +" ["+classDescription+"]\n";
                    else finalText= finalText+ startTime+" - "+finishTime+" : " + className +"\n";*/
                    finalText=edtClasses.getText().toString();

                    txtRoutine.setText(finalText);
                    /*List<String> tempClasses= classRoutine.getClasses();
                    tempClasses.add(finalText);
                    classRoutine.setClasses(tempClasses);*/
                    Log.d(TAG, "onClick: started final text= "+finalText);
                    alertDialog.dismiss();


                }


            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "onClick: cancel button" );

                alertDialog.dismiss();
            }
        });

        alertDialog.show();

    }

    /**
     * This method sends the all the data of the class routine to an object of notification class
     * and the user is taken to the ClassRoutineActivity
     *
     * @param s        the title of the notification for class routine
     * @param date     the date of the class routine
     * @param priority the priority of the class routine
     * @param message  the body of the notification for class routine
     */
private void sendNotificationData(final String s, String date, long priority, final String message) {
        Notification notification= new Notification(s,date,Utils.getUserName(),Utils.getImageUri(),"ClassRoutine",priority);
        FirebaseFirestore.getInstance().collection("Classrooms").document(Utils.getClassName()).collection("Notifications")
                .add(notification).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                sendNotification(s,message);
                Intent intent= new Intent(getApplicationContext(),ClassRoutineActivity.class);
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
     * This method sends notification to all the students under the classroom.
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

