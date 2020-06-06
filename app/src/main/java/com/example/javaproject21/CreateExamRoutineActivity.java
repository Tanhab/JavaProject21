package com.example.javaproject21;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.res.ResourcesCompat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * The class for Create exam routine activity.
 */
public class CreateExamRoutineActivity extends AppCompatActivity {
    /**
     * The constant variable for logcat.
     */
private static final String TAG = "CreateExamRoutine";
    /**
     * The SpeedDialView variable.
     */
private SpeedDialView exSpeedDialView;
    /**
     * The ImageButton for back.
     */
private ImageButton btnBack;
    /**
     * The ImageButton for schedule.
     */
private Button btnSchedule;
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
     * The String for Exam section.
     */
    String examSection;
    /**
     * The String for Start time.
     */
    String startTime;
    /**
     * The String for Finish time.
     */
    String finishTime;
    /**
     * The String for Exam name.
     */
    String examName;
    /**
     * The String for Exam syllabus.
     */
    String examSyllabus;
    /**
     * The String for Final text.
     */
    String finalText="\nExams :\n";
    /**
     * The Exam routine object.
     */
private ExamRoutine examRoutine;
    /**
     * The long variable for Priority.
     */
    long priority;
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
        setContentView(R.layout.activity_create_exam_routine);


        mRequestQue = Volley.newRequestQueue(this);
        txtRoutineDate=findViewById(R.id.txtRoutineDate);
        txtSection=findViewById(R.id.txtSection);
        txtRoutine=findViewById(R.id.txtRoutine);
        btnSchedule=findViewById(R.id.btnSendRoutine);
        examRoutine= new ExamRoutine();
        btnBack=findViewById(R.id.backButton);






        btnSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(finalDate.length()<1)
                {
                    Toast.makeText(CreateExamRoutineActivity.this, "Date and Section can not be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                createExamRoutine();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        initSpeedDial(savedInstanceState == null);


    }

    /**
     * This method handles the functionality of the floating action button where speedDial
     * has been set.2 items: add an exam and edit date are included
     *
     * @param addActionItems boolean variable if true the action items will be added
     */
//private
    void initSpeedDial(boolean addActionItems) {

        exSpeedDialView = findViewById(R.id.speedDial);
        if (addActionItems) {

            Drawable drawable = AppCompatResources.getDrawable(getApplicationContext(), R.drawable.paper);
            exSpeedDialView.addActionItem(new SpeedDialActionItem.Builder(R.id
                    .fab_add_document, drawable)
                    .setFabImageTintColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimaryDark, getTheme()))
                    .setLabel("Add an exam")
                    .setLabelColor(Color.BLACK)
                    .setLabelBackgroundColor(Color.WHITE)
                    .create());


            exSpeedDialView.addActionItem(new SpeedDialActionItem.Builder(R.id.fab_add_date, R.drawable
                    .ic_calender)
                    .setFabBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimaryDark,
                            getTheme()))
                    .setLabel("Edit date")
                    .setLabelColor(Color.BLACK)
                    .setLabelBackgroundColor(Color.WHITE)
                    .create());

        }

        //Set main action clicklistener.
        exSpeedDialView.setOnChangeListener(new SpeedDialView.OnChangeListener() {
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
        exSpeedDialView.setOnActionSelectedListener(new SpeedDialView.OnActionSelectedListener() {
            @Override
            public boolean onActionSelected(SpeedDialActionItem actionItem) {
                switch (actionItem.getId()) {
                    case R.id.fab_add_document:
                        showDialog();
                        // To close the Speed Dial with animation
                        return true; // false will close it without animation
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

    /*private void addSectionDialog() {
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
                examRoutine.setExamSection(section);

            }
        });builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }*/

    /**
     * This method creates an alert dialog for entering the exam with starting time,
     * finishing time,section,syllabus for the exam
     * in a certain date for the exam routine.
     */
private void showDialog() {

        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.add_exam_alertdialog, null);

        Button acceptButton = view.findViewById(R.id.btnAddNewExam);
        Button cancelButton = view.findViewById(R.id.btnCancel);
        final EditText edtExamName,edtSyllabus,edtSection;
        final EditText edtStartTime,edtFinishTime;
        edtExamName=view.findViewById(R.id.txtClassInput);
        edtSection=view.findViewById(R.id.txtSectionInput);
        edtSyllabus=view.findViewById(R.id.txtSyllabusInput);
       // edtRes=view.findViewById(R.id.txtResInput);
        edtStartTime=view.findViewById(R.id.edtStartingTime);
        edtFinishTime=view.findViewById(R.id.edtFinishingTime);
        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();



        acceptButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View view) {
                Log.e(TAG, "onClick: accept button");
                if(edtStartTime.getText().toString().length()<1)
                {
                    edtStartTime.setError("Can't be empty");
                }else if(edtFinishTime.getText().toString().length()<1)
                {
                    edtFinishTime.setError("Can't be empty");
                }else if(edtExamName.getText().toString().length()<1)
                {
                    edtExamName.setError("Can't be empty");
                }else if(edtSection.getText().toString().length()<1)
                {
                    edtSection.setError("Can't be empty");
                }
                else
                {
                    examName=edtExamName.getText().toString().trim();
                    examSyllabus=edtSyllabus.getText().toString().trim();

                    examSection=edtSection.getText().toString().trim();
                    startTime=edtStartTime.getText().toString().trim();
                    finishTime=edtFinishTime.getText().toString().trim();
                    finalText= finalText+ startTime+" - "+finishTime+" : " + examName +"\n"  + "Section: "+examSection +"\n" + "Syllabus: " +examSyllabus + "\n";
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
     * This method  uploads the exam routine to the database by
     * the date priority and calls a method for sending notification.
     */
private void createExamRoutine() {
        Log.d(TAG, "createExamRoutine: started");

        examRoutine.setExams(finalText);
        setPriority();
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
        //    public ExamRoutine(String exams, String examDate, long priority, String sender, String imageUri, String message, String date, String postID) {
        ExamRoutine examRoutine2=new ExamRoutine(finalText,finalDate,a,Utils.getUserName(),Utils.getImageUri(),"",date,postId);
        FirebaseFirestore.getInstance().collection("Classrooms").document(Utils.getClassName()).collection("ExamRoutine")
                .document(postId)
                .set(examRoutine2).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(CreateExamRoutineActivity.this, "Exam Schedule Updated.", Toast.LENGTH_SHORT).show();
                sendNotificationData(" Exam routine for "+ examRoutine.getExamDate(),finalDate,priority,finalText);

                finalText="Exams: \n";
                finalDate="";
                examRoutine.setExams("");
                examRoutine.setExamDate("");

                txtRoutine.setText(finalText);
                txtRoutineDate.setText("Date");

                Toast.makeText(CreateExamRoutineActivity.this, "Exam Routine Added.", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CreateExamRoutineActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    /**
     * This method handles the priority of the exam routine.The new
     * routine comes first.
     */
private void setPriority() {
        Calendar calendar= Calendar.getInstance();
        int YEAR = calendar.get(Calendar.YEAR);
        int MONTH = calendar.get(Calendar.MONTH);
        int DATE = calendar.get(Calendar.DATE);
        long a= YEAR*100+MONTH;
        a= a*100+DATE;
        examRoutine.setPriority(a);
    }

    /**
     * This methods handles the date of the exam routine by creating datePickerDialog.
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
                examRoutine.setExamDate(dateText);
                finalDate=dateText;
            }
        }, YEAR, MONTH, DATE);

        datePickerDialog.show();


    }


/* private void handleTimeButton() {
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
                 startTime = DateFormat.format("h:mm a", calendar1).toString();
                 txtExamTime.setText(startTime);
             }
         }, HOUR, MINUTE, is24HourFormat);

         timePickerDialog.show();

     }*/
    /**
     * This method sends the all the data of the exam routine to an object of notification class
     * and the user is taken to the ExamRoutineActivity
     *
     * @param s        the title of the notification for exam routine
     * @param date     the date of the exam routine
     * @param priority the priority of the exam routine
     * @param message  the body of the notification for exam routine
     */
    private void sendNotificationData(final String s, String date, long priority, final String message) {
        Notification notification= new Notification(s,date,Utils.getUserName(),Utils.getImageUri(),"ExamRoutine",priority);
        FirebaseFirestore.getInstance().collection("Classrooms").document(Utils.getClassName()).collection("Notifications")
                .add(notification).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                sendNotification(s,message);
                Intent intent= new Intent(getApplicationContext(),ExamRoutineActivity.class);
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
