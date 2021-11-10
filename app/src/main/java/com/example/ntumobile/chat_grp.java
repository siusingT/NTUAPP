package com.example.ntumobile;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ntumobile.adapters.AdapterGroupChatList;
import com.example.ntumobile.models.ModelGroupChatList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class chat_grp extends AppCompatActivity {
    private RecyclerView groupsRv;
    private FirebaseAuth firebaseAuth;
    private ArrayList<ModelGroupChatList> groupChatLists;
    private AdapterGroupChatList adapterGroupChatList;
    private EditText searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_grp);
        groupsRv = findViewById(R.id.groupsRv);
        firebaseAuth = FirebaseAuth.getInstance();
        loadGroupChatsList();

        Button createGroupBtn = (Button) findViewById(R.id.action_create_group);
        createGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(chat_grp.this, GroupCreateActivity.class);
                startActivity(intent);
            }
        });

        Button Home = (Button) findViewById(R.id.home_button);
        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(chat_grp.this, Home.class);
                startActivity(intent);
            }
        });

        Button Booking = (Button) findViewById(R.id.booking);
        Booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(chat_grp.this, booking_main.class);
                startActivity(intent);
            }
        });

        Button Community = (Button) findViewById(R.id.community);
        Community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(chat_grp.this, community_main.class);
                startActivity(intent);
            }
        });

        Button buttonChat = (Button) findViewById(R.id.chat_button);
        buttonChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(chat_grp.this, chat_grp.class);
                startActivity(intent);
            }
        });

        Button buttonInd = (Button) findViewById(R.id.individual);
        buttonInd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(chat_grp.this, chat_main.class);
                startActivity(intent);
            }
        });

        searchView = (EditText) findViewById(R.id.inputSearch);
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString()!=null)
                {
                    searchGroupChatsList(s.toString());
                }
                else
                {
                    loadGroupChatsList();
                }
            }
        });
    }

    private void loadGroupChatsList() {
        groupChatLists = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance("https://ntu-mobile-9eb73-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Groups");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                groupChatLists.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    //if current user's uid exists in participants lis of group then show that group
                    if (ds.child("Participants").child(firebaseAuth.getUid()).exists()){
                        ModelGroupChatList model = ds.getValue(ModelGroupChatList.class);
                        groupChatLists.add(model);
                    }
                }
                adapterGroupChatList = new AdapterGroupChatList(chat_grp.this, groupChatLists);
                groupsRv.setAdapter(adapterGroupChatList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void searchGroupChatsList(final String query) {
        groupChatLists = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance("https://ntu-mobile-9eb73-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Groups");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                groupChatLists.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    //if current user's uid exists in participants list of grp then show that grp
                    if (ds.child("Participants").child(firebaseAuth.getUid()).exists()){
                        if (ds.child("groupTitle").toString().toLowerCase().contains(query.toLowerCase())){
                            ModelGroupChatList model = ds.getValue(ModelGroupChatList.class);
                            groupChatLists.add(model);
                        }

                    }
                }
                adapterGroupChatList = new AdapterGroupChatList(chat_grp.this, groupChatLists);
                groupsRv.setAdapter(adapterGroupChatList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}