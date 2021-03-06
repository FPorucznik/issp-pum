package com.example.countriesmvvm.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.countriesmvvm.R;
import com.example.countriesmvvm.model.Country;
import com.example.countriesmvvm.viewmodel.CountriesViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Country> listCountries = new ArrayList<>();

    private RecyclerView recyclerView;
    private CountriesAdapter adapter;

    private CountriesViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = new ViewModelProvider(this).get(CountriesViewModel.class);
        viewModel.initRepo(this.getApplication());

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CountriesAdapter();
        recyclerView.setAdapter(adapter);

        observeViewModel();
    }

    private void observeViewModel(){
        viewModel.getCountries().observe(this, countries -> {
            if(listCountries != null){
                listCountries.clear();
                listCountries.addAll(countries);
                adapter.notifyDataSetChanged();
            }
        });

        viewModel.getCountryError().observe(this, error -> {
            if(error){
                Toast.makeText(this, "failed to connect", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStop() {
        viewModel.onDispose();
        super.onStop();
    }

    public class CountriesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        public CountriesAdapter(){
        }

        private class CountriesViewHolder extends RecyclerView.ViewHolder {

            public CountriesViewHolder(@NonNull View itemView) {
                super(itemView);
            }
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new CountriesViewHolder(
                    LayoutInflater.from(MainActivity.this).inflate(
                            R.layout.item_view,
                            parent,
                            false
                    )
            );
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            Country country = listCountries.get(position);

            TextView nameView = holder.itemView.findViewById(R.id.countryName);
            TextView capitalView = holder.itemView.findViewById(R.id.countryCapital);
            nameView.setText(country.countryName);
            capitalView.setText(country.countryCapital);
        }

        @Override
        public int getItemCount() {
            return listCountries.size();
        }
    }
}