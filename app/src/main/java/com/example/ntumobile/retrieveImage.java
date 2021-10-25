package com.example.ntumobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class retrieveImage extends AppCompatActivity {
    private EditText inputSearch;
    RecyclerView recyclerView;
    FirebaseRecyclerOptions<uploadImage_get> options;
    FirebaseRecyclerAdapter<uploadImage_get, ImageViewHolder> adapter;
    DatabaseReference DataRef;
    Button homeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_imageretrieve);

        inputSearch = findViewById(R.id.inputSearch);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        DataRef = FirebaseDatabase.getInstance().getReference().child("Test");
        homeBtn = findViewById(R.id.test_homeBtn);

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendUserToMainActivity();
            }
        });

        LoadData();
    }

    private void SendUserToMainActivity() {
        Intent mainIntent = new Intent(retrieveImage.this, Home.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

    private void LoadData() {

        options = new FirebaseRecyclerOptions.Builder<uploadImage_get>().setQuery(DataRef, uploadImage_get.class).build();
        adapter = new FirebaseRecyclerAdapter<uploadImage_get, ImageViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ImageViewHolder holder, int position, @NonNull uploadImage_get model) {
                holder.textView.setText(model.getImageName());
                Picasso.get().load(model.getImageUrl()).into(holder.imageView);
            }

            @NonNull
            @Override
            public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_single_view, parent, false);
                return new ImageViewHolder(v);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
}