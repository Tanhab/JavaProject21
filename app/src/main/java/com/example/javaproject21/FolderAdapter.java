package com.example.javaproject21;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class FolderAdapter extends FirestoreRecyclerAdapter<Folder, FolderAdapter.FolderHolder> {


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

        }
    }
}
