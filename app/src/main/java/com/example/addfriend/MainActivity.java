package com.example.addfriend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    private TextView NavProfileName;

    private FirebaseAuth mAuth;
    private FirebaseDatabase db;
    private DatabaseReference UsersRef;

    String currentUserID;
    MeowBottomNavigation bottomNavigation;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null)
        {
            //CheckUserExistence();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser == null)
        {
            mAuth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(MainActivity.this, "Signed in anonymously", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        String message = task.getException().getMessage();
                        Toast.makeText(MainActivity.this, "Error occured: " + message, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else
        {
            currentUserID = mAuth.getCurrentUser().getUid();
        }

        db = FirebaseDatabase.getInstance();
        UsersRef = db.getReference().child("Users");


        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        View navView = navigationView.inflateHeaderView(R.layout.navigation_header);
        NavProfileName = (TextView) navView.findViewById(R.id.nav_user_full_name);

        bottomNavigation = findViewById(R.id.bottom_navigation);

        bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.home_1));
        bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.ic_profile));
        bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.ic_booking));
        bottomNavigation.add(new MeowBottomNavigation.Model(4, R.drawable.ic_settings));

        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                Fragment fragment = null;

                //Check condition
                switch (item.getId())
                {
                    case 1:
                        //When id is 1
                        //Initialize home fragment
                        fragment = new HomeFragment();
                        break;
                    case 2:
                        //When id is 2
                        //Initialize profile fragment
                        fragment = new ProfileFragment();
                        break;
                    case 3:
                        //When id is 3
                        //Initialize booking fragment
                        fragment = new BookingFragment();
                        break;
                    case 4:
                        //When id is 4
                        //Initialize settings fragment
                        fragment = new SettingsFragment();
                        break;
                }
                //Load fragment
                loadFragment(fragment);
            }
        });

        //Set notification count
        //bottomNavigation.setCount(1,"10");

        //Set home fragment initially selected
        bottomNavigation.show(1, true);

        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item)
            {
                if (mAuth.getCurrentUser().isAnonymous())
                {
                    showBottomSheetDialog();
                }
            }
        });

        bottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {

            }
        });


        if(currentUser != null)
        {
            UsersRef.child((currentUserID)).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot)
                {
                    if (snapshot.exists())
                    {
                        if(snapshot != null) {
                            String fullname = snapshot.child("fullname").getValue().toString();
                            NavProfileName.setText(fullname);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                UserMenuSelector(item);
                return false;
            }
        });
    }

    private void SendUserToTestRetrieveActivity() {
        Intent testIntent = new Intent(MainActivity.this, TestRetrieve.class);
        testIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(testIntent);
        finish();
    }

    private void showBottomSheetDialog() {
        BottomSheetDialog sheetDialog = new BottomSheetDialog(MainActivity.this,
                R.style.BottomSheetDialogTheme);
        View sheetView = LayoutInflater.from(getApplicationContext())
                .inflate(R.layout.bottomdialog, (LinearLayout)findViewById(R.id.dialog_container));
        sheetView.findViewById(R.id.menu_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sheetDialog.dismiss();
            }
        });
        sheetView.findViewById(R.id.menu_sign_up_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendUserToRegisterActivity();
                sheetDialog.dismiss();
            }
        });
        sheetView.findViewById(R.id.menu_login_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendUserToLoginActivity();
                sheetDialog.dismiss();
            }
        });
        sheetDialog.setContentView(sheetView);
        sheetDialog.show();
    }

    private void loadFragment(Fragment fragment) {
        //Replace fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .commit();
    }


    private void CheckUserExistence()
    {
        final String current_user_id = mAuth.getCurrentUser().getUid();

        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if (!snapshot.hasChild(current_user_id))
                {
                    SendUserToSetupActivity();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void UserMenuSelector(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.nav_profile:
                Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_settings:
                Toast.makeText(this, "Account Settings", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_book_facilities:
                Toast.makeText(this, "Book Facilities", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_add_friends:
                SendUserToSearchFriendsActivity();
                break;

            case R.id.nav_test_retrieve:
                SendUserToTestRetrieveActivity();
                break;

            case R.id.nav_test_upload:
                SendUserToTestUploadActivity();
                break;

            case R.id.nav_pdf_upload:
                SendUserToPDFUploadActivity();
                break;


            case R.id.nav_logout:
                if(!mAuth.getCurrentUser().isAnonymous())
                {
                    mAuth.signOut();
                    SendUserToLoginActivity();
                }
                else
                {
                    showBottomSheetDialog();
                }
                break;
        }
    }

    private void SendUserToTestUploadActivity() {
        Intent testUploadIntent = new Intent(MainActivity.this, TestUpload.class);
        testUploadIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(testUploadIntent);
        finish();
    }

    private void SendUserToPDFUploadActivity() {
        Intent PDFUploadIntent = new Intent(MainActivity.this, PDFUploadActivity.class);
        PDFUploadIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(PDFUploadIntent);
        finish();
    }

    private void SendUserToSearchFriendsActivity()
    {
        Intent searchFriendsIntent = new Intent(MainActivity.this, SearchFriendsActivity.class);
        searchFriendsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(searchFriendsIntent);
        finish();
    }

    private void SendUserToSetupActivity()
    {
        Intent setupIntent = new Intent(MainActivity.this, SetupActivity.class);
        setupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(setupIntent);
        finish();
    }

    private void SendUserToLoginActivity()
    {
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }
    private void SendUserToRegisterActivity()
    {
        Intent registerIntent = new Intent(MainActivity.this, RegisterActivity.class);
        registerIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(registerIntent);
        finish();
    }
}