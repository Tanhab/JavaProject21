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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CreateClassRoutineActivity extends AppCompatActivity {
    private static final String TAG = "CreateClassRoutine";
    private EditText edtSection;
    private ImageButton btnAddSection;
    private Button btnAddDate,btnOpenDialog,btnSendRoutine;
    private TextView txtRoutineDate,txtSection,txtRoutine;
    String finalDate,section,startTime,finishTime,className,classDescription,finalText="Classes :\n";
    private ClassRoutine classRoutine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_class_routine);
        edtSection=findViewById(R.id.txtSectionInput);
       btnOpenDialog=findViewById(R.id.btnOpenDialog);
        btnAddDate=findViewById(R.id.btnEnterDate);
        btnAddSection=findViewById(R.id.btnAddSection);

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
        FirebaseFirestore.getInstance().collection("cse18").document(classRoutine.getDate()+classRoutine.getSection())
                .set(classRoutine).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
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
}

