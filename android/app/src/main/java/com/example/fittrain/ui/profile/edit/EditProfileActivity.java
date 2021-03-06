package com.example.fittrain.ui.profile.edit;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.fittrain.R;
import com.example.fittrain.dto.UserEditDto;
import com.example.fittrain.model.UserResponse;
import com.example.fittrain.retrofit.generator.AuthType;
import com.example.fittrain.retrofit.generator.ServiceGenerator;
import com.example.fittrain.retrofit.services.UserService;
import com.example.fittrain.ui.common.DashboardActivity;
import com.example.fittrain.util.UtilToken;
import com.example.fittrain.util.Validator;

import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {
    private TextView textViewName, textViewEmail;
    private ImageView imageViewPicture;
    private EditText editTextYearsOld, editTextTrainingYears, editTextWeight, editTextHeight, editTextName;
    private Spinner spinnerGender;
    private UserResponse myUser;
    private UserService userService;
    private String jwt;
    private Button btn_save_profile;
    private ProgressBar progressBarFoto;
    Uri uriSelected;
    private static final int READ_REQUEST_CODE = 42;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        loadItems();
        getMe();
    }
    public void loadItems(){
        jwt= UtilToken.getToken(getApplicationContext());
        textViewName = findViewById(R.id.textViewNameEdit);
        textViewEmail=findViewById(R.id.textViewEmailWrittenEdit);
        imageViewPicture=findViewById(R.id.profile_image);
        editTextHeight = findViewById(R.id.editTextHeight);
        editTextWeight=findViewById(R.id.editTextWeight);
        editTextTrainingYears=findViewById(R.id.editTextTrainingYears);
        editTextYearsOld=findViewById(R.id.editTextYearsOld);
        spinnerGender=findViewById(R.id.spinnerSex);
        editTextName=findViewById(R.id.editTextName);
        btn_save_profile=findViewById(R.id.btn_save_profile);
        progressBarFoto = findViewById(R.id.progressBarFoto);
    }
    public void getMe(){

            userService = ServiceGenerator.createService(UserService.class, jwt , AuthType.JWT);
            Call<UserResponse> call = userService.getMe();
            call.enqueue(new Callback<UserResponse>() {
                @Override
                public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                    if (!response.isSuccessful()) {
                        Log.e("error response", "code error");
                        Toast.makeText(getBaseContext(), "Error in request", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e("successful response", "code error");
                        myUser = response.body();
                        setItems();


                    }
                }

                @Override
                public void onFailure(Call<UserResponse> call, Throwable t) {
                    Log.e("failure", "failure in petition");

                }
            });



    }
    public void activateProgressBar(){
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        progressBarFoto.setVisibility(View.VISIBLE);
    }
    public void desactivateProgressBar(){
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        progressBarFoto.setVisibility(View.GONE);
    }
    public void uploadPhoto(){
        if (uriSelected != null) {


            activateProgressBar();
            //progressBarFoto.setProgress(0);
            userService = ServiceGenerator.createService(UserService.class, jwt , AuthType.JWT);

            try {
                InputStream inputStream = getContentResolver().openInputStream(uriSelected);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                int cantBytes;
                byte[] buffer = new byte[1024*4];

                while ((cantBytes = bufferedInputStream.read(buffer,0,1024*4)) != -1) {
                    baos.write(buffer,0,cantBytes);
                }


                RequestBody requestFile =
                        RequestBody.create(
                                MediaType.parse(getContentResolver().getType(uriSelected)), baos.toByteArray());


                MultipartBody.Part body =
                        MultipartBody.Part.createFormData("photo", "photo", requestFile);



                Call<UserResponse> callRegister = userService.changePhoto(body, UtilToken.getId(this));

                callRegister.enqueue(new Callback<UserResponse>() {
                    @Override
                    public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                        desactivateProgressBar();
                        if (response.isSuccessful()) {
                            Log.d("Photo changed", "Éxito");
                            Log.d("Uploaded", response.body().toString());
                        } else {
                            Log.e("Upload error", response.errorBody().toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<UserResponse> call, Throwable t) {
                        Log.e("Upload error", t.getMessage());
                        progressBarFoto.setVisibility(View.GONE);

                    }
                });


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }
    public void performFileSearch() {


        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);


        intent.addCategory(Intent.CATEGORY_OPENABLE);


        intent.setType("image/*");

        startActivityForResult(intent, READ_REQUEST_CODE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {

        // The ACTION_OPEN_DOCUMENT intent was sent with the request code
        // READ_REQUEST_CODE. If the request code seen here doesn't match, it's the
        // response to some other intent, and the code below shouldn't run at all.

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using resultData.getData().
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                Log.i("Filechooser URI", "Uri: " + uri.toString());
                //showImage(uri);
                Glide
                        .with(this)
                        .load(uri)
                        .into(imageViewPicture);
                uriSelected = uri;
               uploadPhoto();
            }
        }
    }

    public void setItems(){
        String gender= selectAGender(),male="Male",female="Female";
        int position=0;
        if (gender.equals(female)){
            position=1;
        }
        spinnerGender.setSelection(position);
        textViewName.setText(myUser.getName());
        textViewEmail.setText(myUser.getEmail());

        editTextHeight.setText(String.valueOf(myUser.getHeight()));
        editTextWeight.setText(String.valueOf(myUser.getWeight()));
        editTextTrainingYears.setText(String.valueOf(myUser.getTrainingYears()));
        editTextYearsOld.setText(String.valueOf(myUser.getAge()));
        editTextName.setText(myUser.getName());


        //uploading picture...
        if (myUser.getPicture()!=null){
            Glide
                    .with(this)
                    .load(myUser.getPicture())
                    .centerCrop()
                    .into(imageViewPicture);
        }else{
            Glide
                    .with(this)

                    .load("https://www.eecs.utk.edu/wp-content/uploads/2016/02/Symonds_EECS.jpg")
                    .centerCrop()
                    .into(imageViewPicture);
        }

        //btn click
        btn_save_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate())
                    updateMyProfile();
            }
        });
        imageViewPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            performFileSearch();
            }
        });
    }
    public String selectAGender(){
        boolean isMale=myUser.isGender();
        String gender="Female";
        if (isMale)
            gender = "Male";
        return gender;

    }
    public boolean validate(){
        int minYearsOld=1, maxYearsOld=120, minHeight=50, maxHeight=300, minName=1, maxName=20, minTrainingYears=0, maxTrainingYears=60, minWeight=1, maxWeight=500;
        boolean isValid=true;

        Validator.clearError(editTextYearsOld);
        Validator.clearError(editTextHeight);
        Validator.clearError(editTextName);
        Validator.clearError(editTextTrainingYears);
        Validator.clearError(editTextWeight);

        //empty
        String empty = getString(R.string.empty), size=getString(R.string.size_password), samePassword=getString(R.string.isNotSame);
        if (!Validator.isNotEmpty(editTextYearsOld)){
            isValid=false;
            Validator.setError(editTextYearsOld, empty);
        }else{
            if (Validator.isLessThanNumber(editTextYearsOld, minYearsOld) || Validator.isGreaterThanNumber(editTextYearsOld, maxYearsOld)){
                isValid=false;
                Validator.setError(editTextYearsOld, "Between 1 - 120.");
            }
        }
        if (!Validator.isNotEmpty(editTextHeight)){
            isValid=false;
            Validator.setError(editTextHeight, empty);
        }else{
            if (Validator.isLessThanNumber(editTextHeight, minHeight) || Validator.isGreaterThanNumber(editTextHeight, maxHeight)){
                isValid=false;
                Validator.setError(editTextHeight, "Between 50 - 300");
            }
        }
        if (!Validator.isNotEmpty(editTextName)){
            isValid=false;
            Validator.setError(editTextName, empty);
        }else{

            if (Validator.isLessThan(editTextName, minName) || Validator.isGreaterThan(editTextName, maxName)){
                isValid=false;
                Validator.setError(editTextName, "Between 1-20");
            }
        }
        if (!Validator.isNotEmpty(editTextTrainingYears)){
            isValid=false;
            Validator.setError(editTextTrainingYears, empty);
        }else{
            if (Validator.isLessThanNumber(editTextTrainingYears, minTrainingYears) || Validator.isGreaterThanNumber(editTextTrainingYears, maxTrainingYears)){
                isValid=false;
                Validator.setError(editTextTrainingYears, "Between 1 - 60");
            }
        }
        if (!Validator.isNotEmpty(editTextWeight)){
            isValid=false;
            Validator.setError(editTextWeight, empty);
        }else{

            if (Validator.isLessThanNumber(editTextWeight, minWeight) || Validator.isGreaterThanNumber(editTextWeight, maxHeight)){
                isValid=false;
                Validator.setError(editTextWeight, "Between 1-500kg");
            }
        }









        return isValid;
    }
    public UserEditDto reformatMyUserDto(){
        String female="Female";
        UserEditDto userToEdit= new UserEditDto();
        userToEdit.setAge(Integer.parseInt(editTextYearsOld.getText().toString()));
        userToEdit.setHeight(Integer.parseInt(editTextHeight.getText().toString()));
        userToEdit.setName(editTextName.getText().toString());
        userToEdit.setTrainingYears(Integer.parseInt(editTextTrainingYears.getText().toString()));
        userToEdit.setWeight(Integer.parseInt(editTextWeight.getText().toString()));
        userToEdit.setPoints(myUser.getPoints());
        //gender
        if (spinnerGender.getSelectedItem().toString().equals(female)){
            userToEdit.setGender(false);
        }else {
            userToEdit.setGender(true);
        }
        //gender
        return userToEdit;

    }
    public void updateMyProfile(){

        Call<UserResponse> callEdit = userService.edit(myUser.getId(), reformatMyUserDto());
        callEdit.enqueue(new Callback<UserResponse>() {

            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getBaseContext(), "User edited", Toast.LENGTH_SHORT).show();
                    //refresh();
                    UtilToken.setTrainingYears(getBaseContext(), response.body().getTrainingYears());
                    UtilToken.setHeight(getBaseContext(), response.body().getHeight());
                    UtilToken.setWeight(getBaseContext(), response.body().getWeight());

                } else {
                    Toast.makeText(getBaseContext(), "Error updating user", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(getBaseContext(), "Failure", Toast.LENGTH_SHORT).show();
            }
        });
    };

    public void refresh() {
        Intent iRefresh = new Intent(this, EditProfileActivity.class);
        startActivity(iRefresh);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, DashboardActivity.class);
        startActivity(i);
        super.onBackPressed();
    }
}
