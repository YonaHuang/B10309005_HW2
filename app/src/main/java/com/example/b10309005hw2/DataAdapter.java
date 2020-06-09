package com.example.b10309005hw2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import android.widget.TextView;

import com.google.android.material.shape.MaterialShapeDrawable;

import java.util.ArrayList;

public class DataAdapter extends RecyclerView.Adapter<ViewHolder>{
    public int cl;
    public MaterialShapeDrawable bg;
    public ArrayList<Guest> guests;

    public class GuestViewHolder extends ViewHolder {
        public Guest guest;
        public GuestViewHolder(View itemView)
        {
            super(itemView);
        }

        public void bind(Guest guest)
        {
            TextView text_number = (TextView)this.itemView.findViewById(R.id.list_item_number);
            text_number.setText(guest.number);
            ViewCompat.setBackground(text_number, bg);
            // text_number.setBackgroundColor(cl);

            TextView text_name = (TextView)this.itemView.findViewById(R.id.list_item_text);
            text_name.setText(guest.name);

            this.guest = guest;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GuestViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GuestViewHolder h = (GuestViewHolder)holder;
        h.bind(guests.get(position));
    }

    @Override
    public int getItemCount() {
        return guests.size();
    }
}
