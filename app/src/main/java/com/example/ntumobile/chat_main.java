package com.example.ntumobile;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ntumobile.adapters.AdapterChatlist;
import com.example.ntumobile.adapters.AdapterGroupChatList;
import com.example.ntumobile.models.ModelChat;
import com.example.ntumobile.models.ModelChatlist;
import com.example.ntumobile.models.ModelGroupChatList;
import com.example.ntumobile.models.ModelUser;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class chat_main extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    RecyclerView recyclerView;
    List<ModelChatlist> chatList;
    //List<ModelUser> userList;
    ArrayList<Friends> friendList;
    DatabaseReference reference;
    FirebaseUser currentUser;
    AdapterChatlist adapterChatlist;
    DatabaseReference mUserRef;
    FirebaseUser mUser;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    FirebaseRecyclerOptions<Friends> options;
    FirebaseRecyclerAdapter<Friends, FriendViewHolder> adapter;
    DatabaseReference avatarRef;
    String currentUserID, hisUid;
    ImageView changeAvatar;
    TextView userStatusTv;
    private EditText searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_main);
        userStatusTv = findViewById(R.id.userStatusTv);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        recyclerView = findViewById(R.id.recyclerView);
        chatList = new ArrayList<>();

        database = FirebaseDatabase.getInstance("https://ntu-mobile-9eb73-default-rtdb.asia-southeast1.firebasedatabase.app/");
        mUserRef = database.getReference().child("Friends");
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        avatarRef = database.getReference("Users");
        currentUserID = mAuth.getCurrentUser().getUid();
        Intent intent = getIntent();
        hisUid = intent.getStringExtra("hisUid");

        reference = FirebaseDatabase.getInstance("https://ntu-mobile-9eb73-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Chatlist").child(currentUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatList.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    ModelChatlist chatlist = ds.getValue(ModelChatlist.class);
                    chatList.add(chatlist);
                }
                loadChats();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Button Home = (Button) findViewById(R.id.home_button);
        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(chat_main.this, Home.class);
                startActivity(intent);
            }
        });

        Button Booking = (Button) findViewById(R.id.booking);
        Booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(chat_main.this, booking_main.class);
                startActivity(intent);
            }
        });

        Button Community = (Button) findViewById(R.id.community);
        Community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(chat_main.this, community_main.class);
                startActivity(intent);
            }
        });

        Button buttonChat = (Button) findViewById(R.id.chat_button);
        buttonChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(chat_main.this, chat_grp.class);
                startActivity(intent);
            }
        });

        Button buttonGrp = (Button) findViewById(R.id.grp);
        buttonGrp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(chat_main.this, chat_grp.class);
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
                    searchChatsList(s.toString());
                }
                else
                {
                    loadChats();
                    LoadStatus();
                }
            }
        });
    }

    private void LoadStatus() {
        avatarRef.child(hisUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    String onlineStatus;
                    onlineStatus=snapshot.child("onlineStatus").getValue().toString();
                    userStatusTv.setText(onlineStatus);
                }
                else
                {
                    Toast.makeText(chat_main.this, "Data not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(chat_main.this, ""+error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadChats() {
        friendList = new ArrayList<>();
        reference = FirebaseDatabase.getInstance("https://ntu-mobile-9eb73-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                friendList.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    Friends user = ds.getValue(Friends.class);
                    for (ModelChatlist chatlist: chatList){
                        if (user.getUserID() != null && user.getUserID().equals(chatlist.getId())){
                            friendList.add(user);
                            break;
                        }
                    }
                    //adapter
                    adapterChatlist = new AdapterChatlist(chat_main.this, friendList);
                    //set adapter
                    recyclerView.setAdapter(adapterChatlist);
                    //set last message
                    for (int i=0; i<friendList.size(); i++){
                        lastMessage(friendList.get(i).getUserID());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void lastMessage(final String userId) {
        DatabaseReference reference = FirebaseDatabase.getInstance("https://ntu-mobile-9eb73-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String theLastMessage = "default";
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    ModelChat chat = ds.getValue(ModelChat.class);
                    if (chat==null){
                        continue;
                    }
                    String sender = chat.getSender();
                    String receiver = chat.getReceiver();
                    if (sender == null || receiver == null){
                        continue;
                    }
                    if (chat.getReceiver().equals(currentUser.getUid()) &&
                            chat.getSender().equals(userId) ||
                            chat.getReceiver().equals(userId) &&
                                    chat.getSender().equals(currentUser.getUid())){
                        //instead of displaying url in message show "sent photo"
                        if (chat.getType().equals("image")){
                            theLastMessage = "Sent a photo";
                        }
                        else {
                            theLastMessage = chat.getMessage();
                        }
                    }
                }
                adapterChatlist.setLastMessageMap(userId, theLastMessage);
                adapterChatlist.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void searchChatsList(final String query) {
        friendList = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance("https://ntu-mobile-9eb73-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Friends").child("UserID");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                friendList.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    if (ds.child("name").toString().toLowerCase().contains(query.toLowerCase())){
                        Friends model = ds.getValue(Friends.class);
                        friendList.add(model);
                    }

                }
                adapterChatlist = new AdapterChatlist(chat_main.this, friendList);
                recyclerView.setAdapter(adapterChatlist);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private class FriendViewHolder extends RecyclerView.ViewHolder {

        ImageView profile_image;
        TextView Name, bio;

        public FriendViewHolder(@NonNull View itemView) {
            super(itemView);
            profile_image=itemView.findViewById(R.id.imageView12);
            Name = itemView.findViewById(R.id.username);
            bio = itemView.findViewById(R.id.status);

        }
    }
}