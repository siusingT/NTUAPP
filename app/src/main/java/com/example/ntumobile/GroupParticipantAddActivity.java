package com.example.ntumobile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ntumobile.adapters.AdapterParticipantAdd;
import com.example.ntumobile.models.ModelUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GroupParticipantAddActivity extends AppCompatActivity {

    private RecyclerView usersRv;

    private ActionBar actionBar;

    private FirebaseAuth firebaseAuth;

    private String groupId;
    private String myGroupRole;

    private ArrayList<ModelUser> userList;
    private AdapterParticipantAdd adapterParticipantAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_participant_add);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firebaseAuth = FirebaseAuth.getInstance();
        usersRv = findViewById(R.id.usersRv);
        groupId = getIntent().getStringExtra("groupId");
        loadGroupInfo();
    }

    private void getAllUsers() {
        userList = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance("https://ntu-mobile-9eb73-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    ModelUser modelUser = ds.getValue(ModelUser.class);
                    //get all users accept currently signed in
                    if (!firebaseAuth.getUid().equals(modelUser.getUid())){
                        //not my uid
                        userList.add(modelUser);
                    }
                }
                //setup adapter
                adapterParticipantAdd = new AdapterParticipantAdd(GroupParticipantAddActivity.this, userList, ""+groupId, ""+myGroupRole);
                //set adapter to recyclerview
                usersRv.setAdapter(adapterParticipantAdd);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void loadGroupInfo() {
        final DatabaseReference ref1 = FirebaseDatabase.getInstance("https://ntu-mobile-9eb73-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Groups");

        DatabaseReference ref = FirebaseDatabase.getInstance("https://ntu-mobile-9eb73-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Groups");
        ref.orderByChild("groupId").equalTo(groupId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    String groupId = ""+ds.child("groupId").getValue();
                    final String groupTitle = ""+ds.child("groupTitle").getValue();
                    String groupDescription = ""+ds.child("groupDescription").getValue();
                    String groupIcon = ""+ds.child("groupIcon").getValue();
                    String createdBy = ""+ds.child("createdBy").getValue();
                    String timestamp = ""+ds.child("timestamp").getValue();

                    ref1.child(groupId).child("Participants").child(firebaseAuth.getUid())
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()){
                                        myGroupRole = ""+dataSnapshot.child("role").getValue();
                                        //toolbar.setTitle(groupTitle + "("+myGroupRole+")");

                                        getAllUsers();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
