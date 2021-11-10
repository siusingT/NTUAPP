package com.example.ntumobile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
//import com.google.firebase.firestore.auth.User;
//import com.google.protobuf.Value;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class viewFriendProfile extends AppCompatActivity {

    DatabaseReference mUserRef, mUserRef1, requestRef, friendRef;;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseDatabase database;
    StorageReference StorageRef;
    DatabaseReference avatarRef;
    private String currentUserID;

    String name,profileImageUrl;
    ImageView changeAvatar;

    ImageView profileImage;
    TextView Name;
    private Button btnSendFriendRequest, btnDeleteFriend;


    String CurrentState= "nothing_happen";

    String myProfileImageUrl,myName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewfriendprofile);
        final String userID=getIntent().getStringExtra("userKey");
        Toast.makeText(this, ""+userID, Toast.LENGTH_SHORT).show();

        database = FirebaseDatabase.getInstance("https://ntu-mobile-9eb73-default-rtdb.asia-southeast1.firebasedatabase.app/");
        mUserRef = database.getReference().child("Users").child(userID);
        mUserRef1 = database.getReference().child("Users");
        requestRef= database.getReference().child("Friend Request");
        friendRef= database.getReference().child("Friends");
        StorageRef = FirebaseStorage.getInstance("gs://ntu-mobile-9eb73.appspot.com/").getReference("User's Avatar");
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        avatarRef = database.getReference("Users");
        currentUserID = mAuth.getCurrentUser().getUid();


        profileImage = findViewById(R.id.imageView12);
        Name = findViewById(R.id.outputUsername);

        btnSendFriendRequest = findViewById(R.id.btnSendFriendRequest);
        btnDeleteFriend = findViewById(R.id.btnDeleteFriend);

        LoadUser();

        LoadMyProfile();

        btnSendFriendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PerformAction(userID);
            }
        });

        CheckUserExistence(userID);

        btnDeleteFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteFriend(userID);
            }
        });

        avatarRef.child(currentUserID).child("Avatar Selected").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    if (snapshot!=null)
                    {
                        String stringAvatarID = snapshot.child("AvatarID").getValue().toString();
                        int avatarID = Integer.parseInt(stringAvatarID);
                        changeAvatar= findViewById(R.id.imageView12);
                        changeAvatar.setImageResource(avatarID);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void DeleteFriend(String userID) {
        if (CurrentState.equals("friend"))
        {
            friendRef.child(mUser.getUid()).child(userID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        //two conditions needed because friends saved from both sides
                        friendRef.child(userID).child(mUser.getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    Toast.makeText(viewFriendProfile.this, "You have deleted friend", Toast.LENGTH_SHORT).show();
                                    CurrentState="nothing_happen";
                                    btnSendFriendRequest.setText("Send Friend Request");
                                    btnDeleteFriend.setVisibility(View.GONE);
                                }

                            }
                        });
                    }

                }
            });
        }
        if(CurrentState.equals("he_sent_pending"))
        {
            HashMap hashMap=new HashMap();
            hashMap.put("status","decline");
            requestRef.child(userID).child(mUser.getUid()).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(viewFriendProfile.this, "You have declined Friend Request", Toast.LENGTH_SHORT).show();
                        CurrentState="he_sent_decline";
                        btnSendFriendRequest.setVisibility(View.GONE);
                        btnDeleteFriend.setVisibility(View.GONE);
                    }
                }
            });
        }

    }


    //Accepting Friend Request
    private void CheckUserExistence(String userID) {
        friendRef.child(mUser.getUid()).child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    CurrentState="friend";
                    btnSendFriendRequest.setText("Friends");
                    btnDeleteFriend.setText("Delete Friend");
                    btnDeleteFriend.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        friendRef.child(userID).child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    CurrentState="friend";
                    btnSendFriendRequest.setText("Friends");
                    btnDeleteFriend.setText("Delete Friend");
                    btnDeleteFriend.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        requestRef.child(mUser.getUid()).child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    if(snapshot.child("status").getValue().toString().equals("pending"))
                    {
                        CurrentState="I_sent_pending";
                        btnSendFriendRequest.setText("Cancel Friend Request");
                        btnDeleteFriend.setVisibility(View.GONE);
                    }
                    if(snapshot.child("status").getValue().toString().equals("decline"))
                    {
                        CurrentState="I_sent_decline";
                        btnSendFriendRequest.setText("Cancel Friend Request");
                        btnDeleteFriend.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        requestRef.child(userID).child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    if(snapshot.child("status").getValue().toString().equals("pending"))
                    {
                        CurrentState="he_sent_pending";
                        btnSendFriendRequest.setText("Accept Friend Request");
                        btnDeleteFriend.setText("Decline Friend");
                        btnDeleteFriend.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        if(CurrentState.equals("nothing_happen"))
        {
            CurrentState="nothing_happen";
            btnSendFriendRequest.setText("Send Friend Request");
            btnDeleteFriend.setVisibility(View.GONE);
        }
    }

    //Send Friend Request
    private void PerformAction(String userID) {
        if(CurrentState.equals("nothing_happen"))
        {
            HashMap hashMap = new HashMap();
            hashMap.put("status","pending");
            requestRef.child(mUser.getUid()).child(userID).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(viewFriendProfile.this, "You have sent a friend request", Toast.LENGTH_SHORT).show();
                        CurrentState = "I_sent_pending";
                        btnSendFriendRequest.setText("Cancel Friend Request");

                    }
                    else
                    {
                        Toast.makeText(viewFriendProfile.this, ""+task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        if(CurrentState.equals("I_sent_pending") || CurrentState.equals("I_sent_decline"))
        {
            requestRef.child(mUser.getUid()).child(userID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(viewFriendProfile.this, "You have cancelled Friend Request", Toast.LENGTH_SHORT).show();
                        CurrentState="nothing_happen";
                        btnSendFriendRequest.setText("Add Friend");
                    }
                    else
                    {
                        Toast.makeText(viewFriendProfile.this, ""+task.getException().toString(), Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
        if(CurrentState.equals("he_sent_pending"))
        {
            requestRef.child(userID).child(mUser.getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        HashMap hashMap=new HashMap();
                        //hashMap.put("status","friend");
                        hashMap.put("name", name);
                        //hashMap.put("profileImageUrl", profileImageUrl);


                        final HashMap hashMap1=new HashMap();
                        //hashMap1.put("status","friend");
                        hashMap1.put("name", myName);
                        //hashMap1.put("profileImageUrl", myProfileImageUrl);

                        friendRef.child(mUser.getUid()).child(userID).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if(task.isSuccessful())
                                {
                                    friendRef.child(userID).child(mUser.getUid()).updateChildren(hashMap1).addOnCompleteListener(new OnCompleteListener() {
                                        @Override
                                        public void onComplete(@NonNull Task task) {
                                            Toast.makeText(viewFriendProfile.this, "you added a friend", Toast.LENGTH_SHORT).show();
                                            CurrentState="friend";
                                            btnSendFriendRequest.setText("Send sms");
                                            btnDeleteFriend.setVisibility(View.VISIBLE);

                                        }
                                    });
                                }
                            }
                        });
                    }
                }
            });
        }
        if(CurrentState.equals("friend"))
        {
            //
        }


    }

    private void LoadUser() {
        mUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    //profileImageUrl=snapshot.child("profileImage").getValue().toString();
                    name=snapshot.child("name").getValue().toString();

                    //Picasso.get().load(profileImageUrl).into(profileImage);
                    Name.setText(name);

                }
                else
                {
                    Toast.makeText(viewFriendProfile.this, "Data not found", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(viewFriendProfile.this, ""+error.getMessage().toString(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void LoadMyProfile() {
        mUserRef1.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    //myProfileImageUrl=snapshot.child("profileImage").getValue().toString();
                    myName=snapshot.child("name").getValue().toString();

                    //Picasso.get().load(myProfileImageUrl).into(profileImage);
                    Name.setText(name);
                }
                else
                {
                    Toast.makeText(viewFriendProfile.this, "Data not found", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(viewFriendProfile.this, ""+error.getMessage().toString(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}
