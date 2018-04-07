package com.example.kallyruan.roommateexpense.UserPkg;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.example.kallyruan.roommateexpense.DB.DBQueries;
import com.example.kallyruan.roommateexpense.R;
import com.example.kallyruan.roommateexpense.UserPkg.LoginActivity;

public class SignupActivity extends AppCompatActivity {
    int user_icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        showIconImage();

        final EditText username = findViewById(R.id.emailField);
        username.setTextColor(Color.BLACK);

        final android.widget.EditText pw = findViewById(R.id.passwordField);
        pw.setTextColor(Color.BLACK);

        final Button signUpButton = findViewById(R.id.signUpButton);

        final EditText usernickname = findViewById(R.id.nicknameField);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String email = username.getText().toString();
                String password = pw.getText().toString();
                DBQueries db = DBQueries.getInstance();
                boolean success = db.signUp(email, password);


                String iconImage= Integer.toString(user_icon);
                String nickname = usernickname.getText().toString();
                boolean iconSuccess=db.setIcon(email,iconImage);
                boolean nicknameSuccess=db.setNickname(email,nickname);

                //Here test whether successfully add icon and nickname infor to user db
                if(!iconSuccess){
                    System.out.println("failed to add icon "+ email +" "+ iconImage);
                }

                if(!nicknameSuccess){
                    System.out.println("failed to add nickname "+ email +" "+ nickname);
                }


                Handler h = new Handler();
                if (success && iconSuccess && nicknameSuccess) {
                    // case 1: successful sign up
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Success! Redirecting to login page; please login with" +
                                    " your new account info.", Toast.LENGTH_LONG);
                    toast.show();
                    h.postDelayed(new Runnable() {
                        @Override
                        public void run() {startActivity(new Intent(getApplicationContext(),
                                LoginActivity.class));}
                    }, 500);
                } else {
                    // case 2: email address is already associated with existing account
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "There is already an account associated with this email address;" +
                                    " please use a different one.", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });


    }

    /* this method is to show icon images in a grid view*/
    public void showIconImage(){
        GridView gridview = (GridView) findViewById(R.id.user_icon_gridView);
        gridview.setAdapter(new ImageAdapter(this));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int location, long l) {
                user_icon=location;
            }
        });
    }
}

