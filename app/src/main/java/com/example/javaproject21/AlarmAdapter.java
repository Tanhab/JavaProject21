package com.example.javaproject21;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AlarmAdapter extends RecyclerView.Adapter {

    private ArrayList<Alarm> mAlarms;
    private CallBack callBack;


    public AlarmAdapter(ArrayList<Alarm> mAlarms, CallBack callback)
    {
        this.mAlarms=mAlarms;
        this.callBack= callback;
    }
     void update(ArrayList<Alarm> alarms)
    {
        this.mAlarms= alarms;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.alarm_item, parent, false);
        return new AlarmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            final Alarm model = mAlarms.get(position);
            AlarmViewHolder alarmViewHolder= (AlarmViewHolder)holder;

            ((AlarmViewHolder) holder).time.setText(model.getTimeText());
            ((AlarmViewHolder) holder).title.setText(model.getTitle());
            ((AlarmViewHolder) holder).btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callBack.deleteAlarm(model);
                }
            });

    }

    @Override
    public int getItemCount() {
        return mAlarms.size();
    }

    private static class AlarmViewHolder extends RecyclerView.ViewHolder
    {
        TextView time,title;
        ImageButton btnDelete;

        public AlarmViewHolder(@NonNull View itemView) {
            super(itemView);
            time= itemView.findViewById(R.id.time_Alarm);
            title= itemView.findViewById(R.id.alarm_Name);
            btnDelete= itemView.findViewById(R.id.imgDelete);
        }
    }
    public interface CallBack{
        void deleteAlarm(Alarm alarm);
    }
}
