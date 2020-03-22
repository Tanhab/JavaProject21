package com.example.javaproject21;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateClassRoutineActivity extends AppCompatActivity {
    private static final String TAG = "CreateClassRoutine";
    private EditText edtSection;
    private ImageButton btnAddSection;
    private Button btnAddDate,btnOpenDialog,btnSendRoutine;
    private TextView txtRoutineDate,txtSection,txtRoutine;
    String finalDate,section,startTime,finishTime,className,classDescription,finalText="Classes :\n";
    private ClassRoutine classRoutine;
    private RequestQueue mRequestQue;
    private String URL = "https://fcm.googleapis.com/fcm/send";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_class_routine);
        edtSection=findViewById(R.id.txtSectionInput);
       btnOpenDialog=findViewById(R.id.btnOpenDialog);
        btnAddDate=findViewById(R.id.btnEnterDate);
        btnAddSection=findViewById(R.id.btnAddSection);
        mRequestQue = Volley.newRequestQueue(this);
        txtRoutineDate=findViewById(R.id.txtRoutineDate);
        txtSection=findViewById(R.id.txtSection);
        txtRoutine=findViewById(R.id.txtRoutine);

        btnSendRoutine=findViewById(R.id.btnSendRoutine);
        classRoutine= new ClassRoutine();



        btnAddSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                section= "Section: "+edtSection.getText().toString().trim();
                txtSection.setText(section);
                classRoutine.setSection(section);
            }
        });
        btnAddDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleDateButton();

            }
        });
        btnOpenDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
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


    }

    private void uploadRoutine() {
        Log.d(TAG, "uploadRoutine: started");
        //TODO: cse18 er bodole class name ber kora lagbe
        classRoutine.setClasses(finalText);
        setPriority();
        FirebaseFirestore.getInstance().collection("cse18").document("Data").collection("classRoutines").document(classRoutine.getDate()+classRoutine.getSection())
                .set(classRoutine).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                sendNotification("Class Routine for "+classRoutine.getDate()+" "+classRoutine.getSection(),finalText);
                edtSection.setText("");
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

    private void setPriority() {
        Calendar calendar= Calendar.getInstance();
        int YEAR = calendar.get(Calendar.YEAR);
        int MONTH = calendar.get(Calendar.MONTH);
        int DATE = calendar.get(Calendar.DATE);
        long a= YEAR*100+MONTH;
        a= a*100+DATE;
        classRoutine.setPriority(a);

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
                classRoutine.setDate(dateText);
                finalDate=dateText;
            }
        }, YEAR, MONTH, DATE);

        datePickerDialog.show();

    }
    private String handleTimeButton() {
        final String[] time = {""};

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
                time[0] = DateFormat.format("h:mm a", calendar1).toString();
                //txt.setText(dateText);

            }
        }, HOUR, MINUTE, is24HourFormat);

        timePickerDialog.show();
        return time[0];

    }
    void showDialog() {

        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.add_class_alertdialog, null);

        Button acceptButton = view.findViewById(R.id.btnAddNewClass);
        Button cancelButton = view.findViewById(R.id.btnCancel);
        final EditText edtClassName,edtDesc;
                final EditText edtStartTime,edtFinishTime;
        edtClassName=view.findViewById(R.id.txtClassInput);
        edtDesc=view.findViewById(R.id.txtDescInput);
        edtStartTime=view.findViewById(R.id.edtStartingTime);
        edtFinishTime=view.findViewById(R.id.edtFinishingTime);
        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();



        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "onClick: accept button");
                if(edtStartTime.getText().toString().length()<1)
                {
                    edtStartTime.setError("Can't be empty");
                }else if(edtFinishTime.getText().toString().length()<1)
                {
                    edtFinishTime.setError("Can't be empty");
                }else if(edtClassName.getText().toString().length()<1)
                {
                    edtClassName.setError("Can't be empty");
                }else
                {
                    className=edtClassName.getText().toString().trim();
                    classDescription=edtDesc.getText().toString().trim();
                    startTime=edtStartTime.getText().toString().trim();
                    finishTime=edtFinishTime.getText().toString().trim();
                    if(classDescription.length()>0)
                        finalText= finalText+ startTime+" - "+finishTime+" : " + className +" ["+classDescription+"]\n";
                    else finalText= finalText+ startTime+" - "+finishTime+" : " + className +"\n";
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
    private void sendNotification(String title, String body) {

        JSONObject json = new JSONObject();
        try {
            json.put("to","/topics/"+"cse18");
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

