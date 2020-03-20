package com.example.javaproject21;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ClassRoutineAdapter extends FirestoreRecyclerAdapter<ClassRoutine, ClassRoutineAdapter.ClassRoutineHolder> {

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ClassRoutineAdapter(@NonNull FirestoreRecyclerOptions<ClassRoutine> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ClassRoutineHolder holder, int position, @NonNull ClassRoutine model) {
        holder.txtDate.setText(model.getDate());
        holder.txtSection.setText(model.getSection());
        holder.txtClasses.setText(model.getClasses());


    }

    @NonNull
    @Override
    public ClassRoutineHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.class_routine_row,parent,false);
        return new ClassRoutineHolder(view);
    }

    class ClassRoutineHolder extends RecyclerView.ViewHolder{
        TextView txtDate,txtSection,txtClasses;

        public ClassRoutineHolder(@NonNull View itemView) {
            super(itemView);
            txtDate=itemView.findViewById(R.id.txtDate);
            txtSection=itemView.findViewById(R.id.txtSectionRow);
            txtClasses=itemView.findViewById(R.id.txtClasses);

        }
    }
}
