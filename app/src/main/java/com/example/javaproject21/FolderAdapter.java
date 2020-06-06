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

/**
 * The adapter class of folder.
 */
public class FolderAdapter extends FirestoreRecyclerAdapter<Folder, FolderAdapter.FolderHolder> {
    /**
     * The object of OnItemClickListener interface.
     */
    private OnItemClickListener listener;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions}* for configuration options.
     *
     * @param options the options
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

    /**
     * The holder class to hold the view in recycler view.
     */
    class FolderHolder extends RecyclerView.ViewHolder{
        /**
         * The TextView type variable folder name.
         */
        TextView txtFolderName;

        /**
         * Instantiates a new Folder holder.
         *
         * @param itemView the item view of the holder
         */
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

    /**
     * The interface On item click listener.
     */
    public interface  OnItemClickListener{
        /**
         * The abstract method onItemClick of the interface which will handle the function of the item
         * if it is clicked
         *
         * @param documentSnapshot the document snapshot
         * @param position         the position
         */
        void OnItemClick(DocumentSnapshot documentSnapshot,int position);
    }

    /**
     * Set on item click listener.
     *
     * @param listener the listener
     */
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener=listener;

    }
}
