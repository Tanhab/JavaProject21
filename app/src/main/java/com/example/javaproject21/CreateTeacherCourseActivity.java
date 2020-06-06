package com.example.javaproject21;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * The class for Create teacher course activity.
 */
public class CreateTeacherCourseActivity extends AppCompatActivity {

    /**
     * The EditText for course name.
     */
private EditText txtCourseName;
    /**
     * The EditText for course teacher.
     */
private EditText txtCourseTeacher;
    /**
     * The EditText for course code.
     */
private EditText txtCourseCode;
    /**
     * The EditText for course credit.
     */
private EditText txtCourseCredit;
    /**
     * The EditText for post.
     */
private EditText txtPost;
    /**
     * The Button for Submit.
     */
private Button submitBtn;
    /**
     * The ImageButton for back.
     */
private ImageButton bckBtn;
    /**
     * The long variable for Priority.
     */
    long priority;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_teacher_course);

        txtCourseName=findViewById(R.id.txtCourseNameInput);
        txtCourseTeacher=findViewById(R.id.txtTeacherNameInput);
        txtCourseCode=findViewById(R.id.txtCourseCodeInput);
        txtCourseCredit=findViewById(R.id.txtCourseCreditInput);
        txtPost=findViewById(R.id.txtTeacherDesignationInput);


        submitBtn=findViewById(R.id.btnSubmit);
        bckBtn=findViewById(R.id.btnBack);

        bckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtCourseName.getText().toString().trim().length()<1){
                    txtCourseName.setError("Course name can not be empty");
                    txtCourseName.setFocusable(true);
                }
                else if(txtCourseTeacher.getText().toString().trim().length()<1){
                    txtCourseTeacher.setError("Course teacher name can not be empty");
                    txtCourseTeacher.setFocusable(true);
                }
                else{
                    uploadInfo();
                }

            }
        });
    }

    /**
     * This method uploads all the information of teacher and course to the database under the classroom.
     */
private void uploadInfo() {

        String cName = txtCourseName.getText().toString().trim();
        String cTeacher = txtCourseTeacher.getText().toString().trim();
        String cCode = txtCourseCode.getText().toString().trim();
        String cCredit = txtCourseCredit.getText().toString().trim();
        String post= txtPost.getText().toString().toUpperCase();

        TeacherCourse teacherCourse =new TeacherCourse(cName,cTeacher,cCode,cCredit,post,priority);
        FirebaseFirestore.getInstance().collection("Classrooms").document(Utils.getClassName()).collection("Teachers&courses").document(teacherCourse.getCourseName()+teacherCourse.getCourseTeacher()+teacherCourse.getCourseCode()+teacherCourse.getCourseCredit())
                .set(teacherCourse).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                txtCourseCode.setText("");
                txtCourseCredit.setText("");
                txtCourseName.setText("");
                txtCourseTeacher.setText("");
                txtPost.setText("");

                Toast.makeText(CreateTeacherCourseActivity.this, "Course info Updated.", Toast.LENGTH_SHORT).show();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CreateTeacherCourseActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
