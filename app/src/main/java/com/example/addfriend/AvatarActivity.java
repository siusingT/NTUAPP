package com.example.addfriend;

import androidx.appcompat.app.AppCompatActivity;

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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class AvatarActivity extends AppCompatActivity {

    private GridView gridView;
    private ImageView selectedImage;
    int[] images = {R.drawable.gee_me_001, R.drawable.gee_me_002, R.drawable.gee_me_003, R.drawable.gee_me_004, R.drawable.gee_me_005,
            R.drawable.gee_me_006, R.drawable.gee_me_007, R.drawable.gee_me_008, R.drawable.gee_me_009, R.drawable.gee_me_010};

    private FirebaseAuth mAuth;
    private FirebaseDatabase db;
    private DatabaseReference UsersRef;
    private StorageReference StorageRef;
    private String currentUserID;
    private Button saveButton;
    private int num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avatar);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        UsersRef = db.getReference().child("Users").child(currentUserID);
        StorageRef = FirebaseStorage.getInstance().getReference().child("User's Avatar");

        saveButton = (Button) findViewById(R.id.saveBtn);

        gridView = (GridView) findViewById(R.id.gridView);
        selectedImage = (ImageView) findViewById(R.id.selectedAvatar);

        CustomAdapter customAdapter = new CustomAdapter(images, this);

        gridView.setAdapter(customAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedImage.setImageResource(images[i]);
                num = i;
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveAvatar();
                SendUserToMainActivity();
            }
        });
    }

    private void SendUserToMainActivity() {
        Intent testIntent = new Intent(AvatarActivity.this, MainActivity.class);
        testIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(testIntent);
        finish();
    }

    private void SaveAvatar() {
        UploadTask uploadTask;
        Uri imageUri = Uri.parse("android.resource://com.example.addfriend/drawable/gee_mee_002.png");

        final String key = UsersRef.push().getKey();
        uploadTask = StorageRef.child(key + ".jpg").putFile(imageUri);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                StorageRef.child(key + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        HashMap hashMap = new HashMap();
                        hashMap.put("ImageUrl", uri.toString());

                        UsersRef.child("Avatar").child(key).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(AvatarActivity.this, "Data successfully uploaded", Toast.LENGTH_SHORT).show();
                            }
                        });
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
}