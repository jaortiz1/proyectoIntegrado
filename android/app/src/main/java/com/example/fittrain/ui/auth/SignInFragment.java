package com.example.fittrain.ui.auth;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fittrain.R;
import com.example.fittrain.model.AuthResponse;
import com.example.fittrain.model.UserResponse;
import com.example.fittrain.retrofit.generator.ServiceGenerator;
import com.example.fittrain.retrofit.services.LoginService;
import com.example.fittrain.ui.common.DashboardActivity;
import com.example.fittrain.util.UtilToken;
import com.example.fittrain.util.Validator;
import com.example.fittrain.util.ViewModelUser;

import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignInFragment extends Fragment {
    private FragmentTransaction fragmentChanger;
    private Fragment signUpFragment;
    private Button btnSignIn, btnGoSignUp;
    private EditText editTextEmail, editTextPassword;
    private Context ctx;
    private ViewModelUser mViewModel;


    public SignInFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sign_in, container, false);
        loadItems(v);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void loadItems(View v) {
        ctx = getContext();
        signUpFragment = new SignUpFragment();
        btnGoSignUp = v.findViewById(R.id.buttonGoRegister);
        btnSignIn = v.findViewById(R.id.buttonLogin);
        editTextEmail = v.findViewById(R.id.editTextEmail);
        editTextPassword = v.findViewById(R.id.editTextPassword);
        btnGoSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentChanger = getFragmentManager().beginTransaction().replace(R.id.containerLogin, signUpFragment);
                fragmentChanger.commit();
            }
        });
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLogin();
            }
        });

    }


    public void doLogin() {
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        if (validate()) {
            String credentials = Credentials.basic(email, password);
            LoginService service = ServiceGenerator.createService(LoginService.class);
            Call<AuthResponse> login = service.doLogin(credentials);
            login.enqueue(new Callback<AuthResponse>() {
                @Override
                public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                    if (response.code() != 201) {
                        // error
                        System.out.println("AQUI");
                        Log.e("RequestError", response.message());
                        Toast.makeText(ctx, "Error while trying to login", Toast.LENGTH_SHORT).show();
                    } else {
                        //set view model user

                        // exito
                        UtilToken.setToken(ctx, response.body().getToken());
                        UtilToken.setId(ctx, response.body().getUser().getId());
                        UtilToken.setEmail(ctx, response.body().getUser().getEmail());
                        UtilToken.setWeight(ctx, response.body().getUser().getWeight());
                        UtilToken.setHeight(ctx, response.body().getUser().getHeight());
                        UtilToken.setTrainingYears(ctx, response.body().getUser().getTrainingYears());
                        UtilToken.setPoints(ctx, response.body().getUser().getPoints());
                        Intent i =new Intent(getActivity(), DashboardActivity.class);
                        i.putExtra("user", response.body().getUser());


                        startActivity(i);

                    }
                }

                @Override
                public void onFailure(Call<AuthResponse> call, Throwable t) {
                    Log.e("NetworkFailure", t.getMessage());
                    Toast.makeText(ctx, "Error. Can't connect to server", Toast.LENGTH_SHORT).show();
                }
            });
        }


    }
    public void setViewModel(UserResponse u){
        mViewModel.setUser(u);

    }
    public boolean validate(){
        int passMinSize=6, passMaxSize=15;
        Validator.clearError(editTextEmail);
        Validator.clearError(editTextPassword);
        String incorrectEmail, incorrectPassword;
        incorrectEmail = getString(R.string.incorrect_email);
        incorrectPassword = getString(R.string.size_password);
        boolean isValid=true;
        if (!Validator.isNotEmpty(editTextEmail) || !Validator.checkEmail(editTextEmail)){
            isValid=false;
            Validator.setError(editTextEmail, incorrectEmail);
        }

        if (Validator.isLessThan(editTextPassword, passMinSize) || Validator.isGreaterThan(editTextPassword, passMaxSize)){
            isValid=false;

            Validator.setError(editTextPassword, incorrectPassword);
        }


        return isValid;

    }
}
