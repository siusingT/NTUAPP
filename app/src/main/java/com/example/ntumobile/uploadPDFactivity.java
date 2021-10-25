package com.example.ntumobile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class uploadPDFactivity extends AppCompatActivity {

    private EditText editPDFName;
    private Button btnUpload;

    StorageReference StorageRef;
    DatabaseReference DataRef;
    String currentUserID;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_pdfupload);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        editPDFName = (EditText) findViewById(R.id.txt_pdfName);
        btnUpload = (Button) findViewById(R.id.btn_upload);

        DataRef = FirebaseDatabase.getInstance("https://ntu-mobile-9eb73-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users").child(currentUserID);
        StorageRef = FirebaseStorage.getInstance().getReference();

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectPDFFile();
            }
        });
    }

    private void selectPDFFile() {

        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select PDF File"),1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK
                && data != null && data.getData() != null)
        {
            uploadPDFFile(data.getData());
        }
    }

    private void uploadPDFFile(Uri data) {

        final String key = DataRef.push().getKey();
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        StorageReference storageReference = StorageRef.child("PDF Test/" + editPDFName.getText().toString() + ".pdf");
        storageReference.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uri.isComplete());
                        Uri url = uri.getResult();
                        //String foldername = "abc";

                        uploadPDF uploadPDF = new uploadPDF(editPDFName.getText().toString(), url.toString());
                        //DataRef.setValue(foldername);
                        DataRef.child("Notes").child(key).setValue(uploadPDF);

                        Toast.makeText(uploadPDFactivity.this, "File Uploaded", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                double progress = (100.0*snapshot.getBytesTransferred())/snapshot.getTotalByteCount();

                progressDialog.setMessage("Uploaded: " + (int)progress + "%");
            }
        });
    }

    public void btn_action(View view) {

        startActivity(new Intent(getApplicationContext(), viewPDFfiles.class));
    }
}