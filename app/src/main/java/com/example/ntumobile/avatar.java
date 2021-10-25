package com.example.ntumobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.HashMap;

public class avatar extends AppCompatActivity {

    private GridView gridView;
    private ImageView selectedImage;
    int[] maleAvatar = {R.drawable.gee_me_001, R.drawable.gee_me_002, R.drawable.gee_me_003, R.drawable.gee_me_004, R.drawable.gee_me_005,
            R.drawable.gee_me_006, R.drawable.gee_me_007, R.drawable.gee_me_008, R.drawable.gee_me_009, R.drawable.gee_me_010,
            R.drawable.gee_me_011, R.drawable.gee_me_012};
    int[] femaleAvatar = {R.drawable.gee_me_013, R.drawable.gee_me_014, R.drawable.gee_me_015, R.drawable.gee_me_016, R.drawable.gee_me_017,
            R.drawable.gee_me_018, R.drawable.gee_me_019, R.drawable.gee_me_020, R.drawable.gee_me_021, R.drawable.gee_me_022,
            R.drawable.gee_me_023, R.drawable.gee_me_024};

    private FirebaseAuth mAuth;
    private FirebaseDatabase db;
    private DatabaseReference UsersRef;
    private StorageReference StorageRef;
    private String currentUserID;
    private Button saveButton, maleButton, femaleButton;
    int num;
    boolean maleBtnClicked = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avatar);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance("https://ntu-mobile-9eb73-default-rtdb.asia-southeast1.firebasedatabase.app/");
        currentUserID = mAuth.getCurrentUser().getUid();
        UsersRef = db.getReference().child("Users").child(currentUserID);
        StorageRef = FirebaseStorage.getInstance("gs://ntu-mobile-9eb73.appspot.com/").getReference("User's Avatar");

        saveButton = (Button) findViewById(R.id.saveBtn);
        maleButton = (Button) findViewById(R.id.maleBtn);
        femaleButton = (Button) findViewById(R.id.femaleBtn);

        gridView = (GridView) findViewById(R.id.gridView);
        selectedImage = (ImageView) findViewById(R.id.selectedAvatar);

        CustomAdapter maleCustomAdapter = new CustomAdapter(maleAvatar, this);
        CustomAdapter femaleCustomAdapter = new CustomAdapter(femaleAvatar, this);

        gridView.setAdapter(maleCustomAdapter);

        maleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                maleBtnClicked = true;
                gridView.setAdapter(maleCustomAdapter);
                selectedImage.setImageResource(maleAvatar[0]);
            }
        });

        femaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                maleBtnClicked = false;
                gridView.setAdapter(femaleCustomAdapter);
                selectedImage.setImageResource(femaleAvatar[0]);
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(maleBtnClicked)
                {
                    selectedImage.setImageResource(maleAvatar[i]);
                    num = i+2;
                    toastMessage(String.valueOf(maleAvatar[i]));
                }else
                {
                    selectedImage.setImageResource(femaleAvatar[i]);
                    num = i+2 + maleAvatar.length;
                }

            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                maleBtnClicked = true;
                SaveAvatar();
                SendUserToMainActivity();
            }
        });
    }

    private void SendUserToMainActivity() {
        Intent testIntent = new Intent(avatar.this, profile.class);
        testIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        int avatarID = 2131165331 + num; // gee_me_001 has an id of 2131230842, 002 has id of 2131230843 so on and so forth. num is w.r.t to grid position +2
        testIntent.putExtra("avatarID", avatarID);  // pass your values and retrieve them in the other Activity using keyName
        startActivity(testIntent);
        finish();
    }

    private void SaveAvatar() {
        int avatarID = 2131165331 + num; // gee_me_001 has an id of 2131230842, 002 has id of 2131230843 so on and so forth. num is w.r.t to grid position +2
        Uri imageUri = Uri.parse("android.resource://com.example.ntumobile/" + String.valueOf(avatarID));

        //String key = UsersRef.push().getKey();

        String key = "Avatar Selected";
        StorageRef.child(key).putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!urlTask.isSuccessful());
                        Uri downloadUrl = urlTask.getResult();
                        String stringAvatarID = String.valueOf(avatarID);

                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("AvatarID", stringAvatarID);
                        hashMap.put("Avatar Url", downloadUrl.toString());

                        UsersRef.child(key).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                toastMessage("Success");
                            }
                        });
                    }
                });
    }

    public class CustomAdapter extends BaseAdapter{

        private int[] imagesPhoto;
        private Context context;
        private LayoutInflater layoutInflater;

        public CustomAdapter(int[] imagesPhoto, Context context) {
            this.imagesPhoto = imagesPhoto;
            this.context = context;
            this.layoutInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return imagesPhoto.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            if (view == null)
            {
                view = layoutInflater.inflate(R.layout.row_items, viewGroup, false);
            }

            ImageView imageView = view.findViewById(R.id.avatarView);
            imageView.setImageResource(imagesPhoto[i]);

            return view;
        }
    }
    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}