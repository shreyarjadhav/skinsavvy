package com.cs407.skinsavvy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.internal.GoogleSignInOptionsExtensionParcelable;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class HomePage extends AppCompatActivity {
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    Button signOutBtn;
    TextView welcomeMsg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        signOutBtn = findViewById(R.id.signOut);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        gsc = GoogleSignIn.getClient(this,gso);
        String name = "";

        welcomeMsg = findViewById(R.id.welcome);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if(acct!=null){
            name = acct.getDisplayName();
            welcomeMsg.setText("Welcome " + name);

        }
        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
    }
    void signOut(){
        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> task) {
                finish();
                startActivity(new Intent(HomePage.this,MainActivity.class));
            }
        });

    }

    public void navigateToCameraScan(View view){
        Intent intent = new Intent(this, CameraScan.class);
        startActivity(intent);
    }

    public void navigateToProfile(View view){
        Intent intent = new Intent(this, ProfilePage.class);
        intent.putExtra("intent_identifier", "home");
        startActivity(intent);
    }

    public void navigateToManualSearch(View view){
        Intent intent = new Intent(this, ManualSearch.class);
        startActivity(intent);
    }
    }
