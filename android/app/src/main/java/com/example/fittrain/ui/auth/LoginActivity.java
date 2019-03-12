package com.example.fittrain.ui.auth;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.fittrain.R;

public class LoginActivity extends AppCompatActivity {
    private FragmentTransaction fragmentChanger;
    private Fragment signInFragment, signUpFragment;
    private Button btnSignIn, btnGoSignUp, btnSignUp, btnGoSignIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loadItems();
        loadDefaultFragment();


    }
    public void loadItems(){
        //btnGoSignIn = findViewById(R.id.);
        /*btnGoSignUp = findViewById(R.id.buttonGoRegister);
        btnSignIn = findViewById(R.id.buttonLogin);*/
        //btnSignUp = ;
        signInFragment = new SignInFragment();
        signUpFragment = new SignUpFragment();

        /*btnGoSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goSignUp();
            }
        });*/
        /*btnGoSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goSignIn();
            }
        });*/
    }
    public void loadDefaultFragment(){
        fragmentChanger = getSupportFragmentManager().beginTransaction().replace(R.id.containerLogin, signInFragment);
        fragmentChanger.commit();
    }
    public void goSignUp(){

    }
    public void goSignIn(){

    }

}
