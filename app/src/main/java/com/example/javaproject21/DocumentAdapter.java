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

/**
 * The adapter class for document.
 */
public class DocumentAdapter extends FirestoreRecyclerAdapter<Document, DocumentAdapter.DocumentHolder> {
    /**
     * The object of OnItemClickListener interface.
     */
private OnItemClickListener listener;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions}** for configuration options.
     *
     * @param options the options
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

    /**
     * This method deletes an item from the recycler view.
     *
     * @param position the position
     */
    public void deleteItem(int position){
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    /**
     * The holder class of Document.
     */
    class DocumentHolder extends RecyclerView.ViewHolder {
        /**
         * The TextView type variable for document name.
         */
        TextView docName;
        /**
         * The TextView type variable for date.
         */
        TextView docDate;
        /**
         * The TextView type variable for uploader.
         */
        TextView docUploader;
        /**
         * The ImageButton type variable for copy link.
         */
        ImageButton btnCopyLink;

        /**
         * Instantiates a new Document holder.
         *
         * @param itemView the item view
         */
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

    /**
     * The interface On item click listener.
     */
    public interface OnItemClickListener {
        /**
         * The abstract method onItemClick of the interface which will handle the function of the item
         * if it is clicked
         *
         * @param documentSnapshot the document snapshot
         * @param position         the position
         */
        void OnItemClick(DocumentSnapshot documentSnapshot, int position);

        /**
         * The abstract method onItemClick of the interface which will handle the function
         * if the copy button is clicked
         *
         * @param snapshot the snapshot
         * @param position the position
         */
        void onCopyButtonPressed(DocumentSnapshot snapshot, int position);
    }

    /**
     * Sets on item click listener.
     *
     * @param listener the object of inteface OnClickListener
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;

    }
}
