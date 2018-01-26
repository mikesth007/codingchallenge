package com.bridj.codingchallenge;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {

    private final OnEventClickListener onEventClickListener;

    interface OnEventClickListener{
        void onClickEvent(Event event);
    }
    List<Event> events;

    public EventsAdapter(List<Event> objects, OnEventClickListener onEventClickListener) {
        events = objects;
        this.onEventClickListener = onEventClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_row, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Event item = events.get(position);
        if (item != null) {
            holder.tvName.setText(item.getName());
            holder.tvVenue.setText(item.getVenue());
            holder.tvDate.setText(String.format(Locale.getDefault(), "%s\n%s", new SimpleDateFormat("dd", Locale.getDefault()).format(item.getDate()), new SimpleDateFormat("MMM", Locale.getDefault()).format(item.getDate())));
            holder.tvPrice.setText(String.format(Locale.getDefault(), "$ %.1f", item.getPrice()));
            StringBuilder labelsBuilder = new StringBuilder();
            for (String label : item.getLabels()) {
                labelsBuilder.append(label);
                labelsBuilder.append(" , ");
            }
            labelsBuilder.delete(labelsBuilder.length() - 2, labelsBuilder.length() - 1);
            holder.tvLabels.setText(labelsBuilder.toString());
            holder.tvSeatsRemaining.setText(String.format(Locale.getDefault(), "%d Seats Available", item.getAvailableSeats()));
        }

    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName;
        public TextView tvVenue;
        public TextView tvDate;
        public TextView tvPrice;
        public TextView tvLabels;
        public TextView tvSeatsRemaining;


        public ViewHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.tvName);
            tvVenue = view.findViewById(R.id.tvVenue);
            tvDate = view.findViewById(R.id.tvDate);
            tvPrice = view.findViewById(R.id.tvPrice);
            tvLabels = view.findViewById(R.id.tvLabels);
            tvSeatsRemaining = view.findViewById(R.id.tvAvailableSeats);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onEventClickListener != null)
                        onEventClickListener.onClickEvent(events.get(getAdapterPosition()));
                }
            });
        }

    }
}