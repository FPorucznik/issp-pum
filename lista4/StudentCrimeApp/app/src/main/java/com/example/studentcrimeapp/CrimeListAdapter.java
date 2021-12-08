package com.example.studentcrimeapp;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;

public class CrimeListAdapter extends RecyclerView.Adapter<CrimeListAdapter.CrimeViewHolder> {

    public final List<Crime> crimes;
    private LayoutInflater inflater;
    private Context context;

    public CrimeListAdapter(Context context, List<Crime> crimes){
        inflater = LayoutInflater.from(context);
        this.crimes = crimes;
        this.context = context;
    }

    class CrimeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView crimeName;
        public final TextView crimeDetails;
        public final ImageView crimeSolved;

        final CrimeListAdapter adapter;

        public CrimeViewHolder(@NonNull View itemView, CrimeListAdapter adapter) {
            super(itemView);
            crimeName = itemView.findViewById(R.id.crime_name);
            crimeDetails = itemView.findViewById(R.id.crime_details);
            crimeSolved = itemView.findViewById(R.id.crimeSolvedIcon);
            this.adapter = adapter;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();
            Crime current = crimes.get(position);
            Intent intent = new Intent(context, CrimeActivity.class);
            intent.putExtra("id", current.getId().toString());
            context.startActivity(intent);
            adapter.notifyItemChanged(position);
        }
    }

    @NonNull
    @Override
    public CrimeListAdapter.CrimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.crime_list_item, parent, false);
        return new CrimeViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull CrimeListAdapter.CrimeViewHolder holder, int position) {
        Crime current = crimes.get(position);
        holder.crimeName.setText(current.getTitle());
        DateFormat df = new SimpleDateFormat("E MMM dd HH:mm:ss zzz yyyy");
        holder.crimeDetails.setText(df.format(current.getDate()));
        if(current.getSolved()){
            holder.crimeSolved.setVisibility(View.INVISIBLE);
        }
        else{
            holder.crimeSolved.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return crimes.size();
    }
}
