package com.example.addfriend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class SearchFriendsActivity extends AppCompatActivity {

    private EditText searchField;
    private Button homeBtn;

    private RecyclerView resultList;

    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef;

    private FirebaseRecyclerOptions<Users> options;
    private FirebaseRecyclerAdapter<Users, UsersViewHolder> adapter;

    String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friends);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");

        searchField = (EditText) findViewById(R.id.friends_search_field);
        homeBtn = findViewById(R.id.friends_home_btn);

        resultList = (RecyclerView) findViewById(R.id.friends_result_list);
        resultList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        resultList.setHasFixedSize(true);

        LoadData("");

        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                if(editable.toString() != null)
                {
                    LoadData(editable.toString());
                }
                else
                {
                    LoadData("");
                }
            }
        });

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendUserToMainActivity();
            }
        });

    }

    private void SendUserToMainActivity()
    {
        Intent mainIntent = new Intent(SearchFriendsActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

    private void LoadData(String data)
    {
        Query query = UsersRef.orderByChild("username").startAt(data).endAt(data + "\uf8ff");

        options = new FirebaseRecyclerOptions.Builder<Users>().setQuery(query, Users.class).build();
        adapter = new FirebaseRecyclerAdapter<Users, UsersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UsersViewHolder holder, int position, @NonNull Users model) {

                if(!mAuth.getCurrentUser().getUid().equals(getRef(position).getKey().toString()))
                {
                    holder.userName.setText(model.getUsername());
                    holder.fullName.setText(model.getFullname());
                    holder.school.setText(model.getSchool());
                }
                else
                {
                    holder.itemView.setVisibility(View.GONE);
                    holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0,0));
                }


            }

            @NonNull
            @Override
            public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.friends_list_layout, parent, false);

                return new UsersViewHolder(v);
            }
        };
        adapter.startListening();
        resultList.setAdapter(adapter);
    }
}