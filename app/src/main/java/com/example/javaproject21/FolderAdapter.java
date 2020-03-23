package com.example.javaproject21;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class FolderAdapter extends FirestoreRecyclerAdapter<Folder, FolderAdapter.FolderHolder> {
    private OnItemClickListener listener;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public FolderAdapter(@NonNull FirestoreRecyclerOptions<Folder> options) {
        super(options);
    }
    @NonNull
    @Override
    public FolderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.folder_layout,parent,false);
        return new FolderHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull FolderHolder holder, int position, @NonNull Folder model) {
        holder.txtFolderName.setText(model.getFolderName());


    }

    class FolderHolder extends RecyclerView.ViewHolder{
        TextView txtFolderName;

        public FolderHolder(@NonNull View itemView) {
            super(itemView);
            txtFolderName=itemView.findViewById(R.id.txtFolderName);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION && listener!=null){
                        listener.OnItemClick(getSnapshots().getSnapshot(position),position);
                    }

                }
            });

        }
    }
    public interface  OnItemClickListener{
        void OnItemClick(DocumentSnapshot documentSnapshot,int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener=listener;

    }
}
