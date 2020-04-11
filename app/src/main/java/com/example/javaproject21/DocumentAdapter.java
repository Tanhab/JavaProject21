package com.example.javaproject21;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class DocumentAdapter extends FirestoreRecyclerAdapter<Document, DocumentAdapter.DocumentHolder> {
    private OnItemClickListener listener;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public DocumentAdapter(@NonNull FirestoreRecyclerOptions<Document> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull DocumentHolder holder, int position, @NonNull Document model) {
        holder.docDate.setText(model.getDate());
        holder.docName.setText(model.getName());
        holder.docUploader.setText("Uploader: " + model.getUploader());


    }

    @NonNull
    @Override
    public DocumentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.document_layout, parent, false);
        return new DocumentHolder(view);
    }
    public void deleteItem(int position){
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    class DocumentHolder extends RecyclerView.ViewHolder {
        TextView docName, docDate, docUploader;
        ImageButton btnCopyLink;

        public DocumentHolder(@NonNull View itemView) {
            super(itemView);
            docName = itemView.findViewById(R.id.docName);
            docDate = itemView.findViewById(R.id.docDate);
            docUploader = itemView.findViewById(R.id.docUploadedBy);
            btnCopyLink=itemView.findViewById(R.id.btnCopyLink);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION && listener!=null){
                        listener.OnItemClick(getSnapshots().getSnapshot(position),position);
                    }
                }
            });
            btnCopyLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION && listener!=null){
                        listener.onCopyButtonPressed(getSnapshots().getSnapshot(position),position);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void OnItemClick(DocumentSnapshot documentSnapshot, int position);
        void onCopyButtonPressed(DocumentSnapshot snapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;

    }
}
