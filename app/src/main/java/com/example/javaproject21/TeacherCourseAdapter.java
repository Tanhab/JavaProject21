package com.example.javaproject21;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

/**
 * The class for Teacher course adapter.
 */
public class TeacherCourseAdapter extends FirestoreRecyclerAdapter<TeacherCourse, TeacherCourseAdapter.TeacherCourseHolder> {


    /**
     * Instantiates a new Teacher course adapter.
     *
     * @param options the options
     */
    public TeacherCourseAdapter(@NonNull FirestoreRecyclerOptions<TeacherCourse> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull TeacherCourseAdapter.TeacherCourseHolder holder, int position, @NonNull TeacherCourse model) {
        holder.txtCourseName.setText(model.getCourseName());
        holder.txtCourseTeacher.setText("Course Teacher: " + model.getCourseTeacher());
        holder.txtCourseCode.setText("Course Code: " + model.getCourseCode());
        holder.txtCourseCredit.setText("Course Credit: " + model.getCourseCredit());
        holder.txtDesignation.setText("Teacher's Designation: "+model.getTeacherDesignation());

    }

    @NonNull
    @Override
    public TeacherCourseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.teacher_course_row,parent,false);
        return new TeacherCourseHolder(view);
    }


    /**
     * The holder class of TeacherCourseAdapter which extends RecyclerView.ViewHolder.
     */
    class TeacherCourseHolder extends RecyclerView.ViewHolder{
        /**
         * The TextView for course name.
         */
        TextView txtCourseName;
        /**
         * The TextView for course teacher.
         */
        TextView txtCourseTeacher;
        /**
         * The TextView for course code.
         */
        TextView txtCourseCode;
        /**
         *The TextView for course credit.
         */
        TextView txtCourseCredit;
        /**
         * The TextView for designation.
         */
        TextView txtDesignation;

        /**
         * Instantiates a new Teacher course holder.
         *
         * @param itemView the item view
         */
        public TeacherCourseHolder(@NonNull View itemView) {
            super(itemView);
            txtCourseName=itemView.findViewById(R.id.courseName);
            txtCourseTeacher=itemView.findViewById(R.id.teacherName);
            txtCourseCode=itemView.findViewById(R.id.courseCode);
            txtCourseCredit=itemView.findViewById(R.id.courseCredit);
            txtDesignation=itemView.findViewById(R.id.teacherDesignation);
        }
    }

}
