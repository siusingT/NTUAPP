package com.example.ntumobile.adapters;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ntumobile.ChatActivity;
import com.example.ntumobile.Friends;
import com.example.ntumobile.R;
import com.example.ntumobile.chat_main;
import com.example.ntumobile.models.ModelGroupChatList;
import com.example.ntumobile.models.ModelUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class AdapterChatlist extends RecyclerView.Adapter<AdapterChatlist.MyHolder> {

    Context context;
    List<ModelUser> userList; //get user info
    ArrayList<Friends> friendList;
    private HashMap<String, String> lastMessageMap;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference usersDbRef;


    //constructor
    public AdapterChatlist(Context context, ArrayList<Friends> friendList) {
        this.context = context;
        //this.userList = userList;
        this.friendList = friendList;
        lastMessageMap = new HashMap<>();
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //inflate layout row_chatlist.xml
        View view = LayoutInflater.from(context).inflate(R.layout.row_chatlist, viewGroup, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, final int i) {
        //get data
        Friends model = friendList.get(i);
        final String hisUid = friendList.get(i).getUserID();
        String userImage = friendList.get(i).getImage();
        String userName = friendList.get(i).getName();
        String lastMessage = lastMessageMap.get(hisUid);
        firebaseDatabase = FirebaseDatabase.getInstance("https://ntu-mobile-9eb73-default-rtdb.asia-southeast1.firebasedatabase.app/");
        usersDbRef = firebaseDatabase.getReference("Users");

        //set data
        myHolder.nameTv.setText(userName);
       /*if (lastMessage==null || lastMessage.equals("default")){
            myHolder.lastMessageTv.setVisibility(View.GONE);
        }
        else {
            myHolder.lastMessageTv.setVisibility(View.VISIBLE);
            myHolder.lastMessageTv.setText(lastMessage);
        }*/
        try {
            Picasso.get().load(userImage).placeholder(R.drawable.gee_me_001).into(myHolder.profileIv);
        }
        catch (Exception e){
            myHolder.profileIv.setImageResource(R.drawable.gee_me_001);
        }
        if (friendList.get(i).getOnlineStatus().equals("online")){
            //online
            myHolder.onlineStatusIv.setImageResource(R.drawable.circle_online);
            myHolder.userStatusTv.setText("online");

        }
        else {
            //offline
            myHolder.onlineStatusIv.setImageResource(R.drawable.circle_offline);
            /*Calendar cal = Calendar.getInstance(Locale.ENGLISH);
            String dateTime = "Last seen at " + DateFormat.format("dd/MM/yyyy hh:mm aa", cal).toString();
            /*String onlineStatus;
            String hisStatus = "" + usersDbRef.child(hisUid);
            onlineStatus=hisStatus.child("onlineStatus").getValue().toString();*/
            //myHolder.userStatusTv.setText(onlineStatus);
            usersDbRef.child(hisUid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists())
                    {
                        String onlineStatus;
                        onlineStatus=snapshot.child("onlineStatus").getValue().toString();
                        myHolder.userStatusTv.setText(onlineStatus);
                    }
                    else
                    {
                        Toast.makeText(context, "Data not found", Toast.LENGTH_SHORT).show();
                    }

                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }

        /*private void checkOnlineStatus(String status) {
            DatabaseReference dbRef = FirebaseDatabase.getInstance("https://ntu-mobile-9eb73-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users").child(currentUserID);
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("onlineStatus", status);
            //update value of onlineStatus of current user
            dbRef.updateChildren(hashMap);
        }*/

        //handle click of user in chatlist
        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start chat activity with that user
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("hisUid", hisUid);
                context.startActivity(intent);
            }
        });
    }

    public void setLastMessageMap(String userId, String lastMessage){
        lastMessageMap.put(userId, lastMessage);
    }

    @Override
    public int getItemCount() {
        return friendList.size(); //size of the list
    }


    class MyHolder extends RecyclerView.ViewHolder{
        ImageView profileIv, onlineStatusIv;
        TextView nameTv, lastMessageTv, timeTv, userStatusTv;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            profileIv = itemView.findViewById(R.id.profileIv);
            onlineStatusIv = itemView.findViewById(R.id.onlineStatusIv);
            nameTv = itemView.findViewById(R.id.nameTv);
            userStatusTv = itemView.findViewById(R.id.userStatusTv);
            //lastMessageTv = itemView.findViewById(R.id.lastMessageTv);
            //timeTv = itemView.findViewById(R.id.timeTv);
        }
    }
}
