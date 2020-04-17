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
import com.google.firebase.firestore.FirebaseFirestore;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CreateExamRoutineActivity extends AppCompatActivity {
    private static final String TAG = "CreateExamRoutine";
    private SpeedDialView exSpeedDialView;
    private ImageButton btnBack;
    private Button btnSchedule;
    private TextView txtRoutineDate,txtSection,txtRoutine;
    String finalDate,examSection,startTime,finishTime,examName,examSyllabus,examResource,finalText="Exams :\n";
    private ExamRoutine examRoutine;
    long priority;
    private RequestQueue mRequestQue;
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

    private void initSpeedDial(boolean addActionItems) {

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

    private void showDialog() {

        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.add_exam_alertdialog, null);

        Button acceptButton = view.findViewById(R.id.btnAddNewExam);
        Button cancelButton = view.findViewById(R.id.btnCancel);
        final EditText edtExamName,edtSyllabus,edtRes,edtSection;
        final EditText edtStartTime,edtFinishTime;
        edtExamName=view.findViewById(R.id.txtClassInput);
        edtSection=view.findViewById(R.id.txtSectionInput);
        edtSyllabus=view.findViewById(R.id.txtSyllabusInput);
        edtRes=view.findViewById(R.id.txtResInput);
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
                    examResource=edtRes.getText().toString().trim();
                    examSection=edtSection.getText().toString().trim();
                    startTime=edtStartTime.getText().toString().trim();
                    finishTime=edtFinishTime.getText().toString().trim();
                    if(examResource.length()>0)
                        finalText= finalText+ startTime+" - "+finishTime+" : " + examName +"\n"
                                + "Section: "+examSection +"\n"
                                + examSyllabus + "\n" + examResource + "\n";
                    else finalText= finalText+ startTime+" - "+finishTime+" : " + examName +"\n"  + "Section: "+examSection +"\n" + "Syllabus: " +examSyllabus + "\n";
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



    private void createExamRoutine() {
        Log.d(TAG, "createExamRoutine: started");

        examRoutine.setExams(finalText);
        setPriority();
        FirebaseFirestore.getInstance().collection(Utils.getClassName()).document("Data").collection("ExamRoutines")
                .document(String.valueOf(System.currentTimeMillis()))
                .set(examRoutine).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(CreateExamRoutineActivity.this, "Exam Schedule Updated.", Toast.LENGTH_SHORT).show();
                sendNotification(" Exam routine for "+ examRoutine.getExamDate(),finalText);

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

    private void setPriority() {
        Calendar calendar= Calendar.getInstance();
        int YEAR = calendar.get(Calendar.YEAR);
        int MONTH = calendar.get(Calendar.MONTH);
        int DATE = calendar.get(Calendar.DATE);
        long a= YEAR*100+MONTH;
        a= a*100+DATE;
        examRoutine.setPriority(a);
    }

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
    private void sendNotification(String title, String body) {

        JSONObject json = new JSONObject();
        try {
            json.put("to","/topics/"+Utils.getClassName());
            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title",title);
            notificationObj.put("body",body);

            JSONObject extraData = new JSONObject();
            extraData.put("category","examRoutine");
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
