package com.example.javaproject21;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ExamRoutineAdapter extends FirestoreRecyclerAdapter<ExamRoutine, ExamRoutineAdapter.ExamRoutineholder> {

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ExamRoutineAdapter(@NonNull FirestoreRecyclerOptions<ExamRoutine> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ExamRoutineholder holder, int position, @NonNull ExamRoutine model) {
        holder.txtDate.setText(model.getExamDate());
        holder.txtTime.setText(model.getExamTime());
        holder.txtExamName.setText("Exam Name :"+model.getExamName());
        holder.txtExamSyllabus.setText(model.getSyllabus());
        if(model.getResources()!=null) holder.txtResources.setText(model.getResources());

    }

    @NonNull
    @Override
    public ExamRoutineholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.exam_routine_row,parent,false);
       return new ExamRoutineholder(view);
    }

    class ExamRoutineholder extends RecyclerView.ViewHolder{
        TextView txtDate,txtTime,txtExamName,txtExamSyllabus,txtResources;

        public ExamRoutineholder(@NonNull View itemView) {
            super(itemView);
            txtDate=itemView.findViewById(R.id.txtDate);
            txtExamName=itemView.findViewById(R.id.txtExamNameRow);
            txtTime=itemView.findViewById(R.id.txtTimeRow);
            txtExamSyllabus=itemView.findViewById(R.id.txtExamSyllabusRow);
            txtResources=itemView.findViewById(R.id.txtResourcesRow);


        }
    }
}
