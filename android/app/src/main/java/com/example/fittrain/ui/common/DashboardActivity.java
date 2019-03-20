package com.example.fittrain.ui.common;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.fittrain.R;
import com.example.fittrain.model.UserResponse;
import com.example.fittrain.ui.auth.LoginActivity;
import com.example.fittrain.ui.gym.GymFragment;
import com.example.fittrain.ui.profile.ProfileFragment;
import com.example.fittrain.ui.training.TrainingFragment;
import com.example.fittrain.util.UtilToken;
import com.example.fittrain.util.ViewModelUser;

import java.util.HashMap;
import java.util.Map;

public class DashboardActivity extends AppCompatActivity {
    private ViewModelUser mViewModel;
    private Fragment fragmentGym, fragmentTraining, fragmentProfile;
    UserResponse uPass;
    Map<String, String> options = new HashMap<>();
    private EditText editTextTitleTraining;
    private Spinner spinnerTarget;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {

                case R.id.navigation_training:
                    getSupportActionBar().setTitle(R.string.title_training);
                    goToFragment(fragmentTraining);
                    return true;
                case R.id.navigation_gym:
                    getSupportActionBar().setTitle(R.string.title_gym);
                    goToFragment(fragmentGym);
                    return true;
                case R.id.navigation_profile:
                    getSupportActionBar().setTitle(R.string.title_profile);
                    goToFragment(fragmentProfile);

                    return true;
            }
            return false;


        }
        //en oncreate fragment
        /*mViewModel = ViewModelProviders.of(getActivity()).get(ColorViewModel.class);
        mViewModel.getSelectedColor().observe(getActivity(),
                color -> layout.setBackgroundColor(Color.parseColor(color)));*/
    };
    public void goToFragment(Fragment f){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.contenedor, f)
                .commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        getSupportActionBar().setTitle(R.string.title_training);



        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        UserResponse userToPass =   (UserResponse) getIntent().getExtras().getSerializable("user");

        Bundle bundle = new Bundle();
        bundle.putSerializable("user", userToPass);


        fragmentTraining = new TrainingFragment();
        fragmentGym = new GymFragment();
        fragmentProfile = new ProfileFragment();
        fragmentTraining.setArguments(bundle);

        goToFragment(fragmentTraining);



    }
    public void createAndShowUserDates(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.userDates)
                .setMessage(R.string.userDatesMessage)
                .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });

        // Create the AlertDialog object and return it
        builder.create();
        builder.show();
    }
    public  void logout(){
        UtilToken.clearAll(this);
        Intent iLogin = new Intent(this, LoginActivity.class);
        startActivity(iLogin);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_filter:
                searchOptions();
                return false;
            case R.id.action_logout:
                createAndShowLogoutDialog();

                return true;

            default:
                break;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items, menu);
        return true;

    }
    public void searchOptions () {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        @SuppressLint("ResourceType")
        View dialogLayout = inflater.inflate(R.layout.activity_search, null);
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);


        builder.setView(dialogLayout);
        //find items
        editTextTitleTraining=dialogLayout.findViewById(R.id.editTextSearchTitle);
        spinnerTarget = dialogLayout.findViewById(R.id.spinnerTarget);
        //find items



        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setPositiveButton(R.string.accept, (dialog, which) -> {
            options = new HashMap<>();
            if (!editTextTitleTraining.getText().toString().equals("") || !editTextTitleTraining.getText().toString().isEmpty())
                options.put("name", editTextTitleTraining.getText().toString());
            if (!spinnerTarget.getSelectedItem().toString().equals("") || !spinnerTarget.getSelectedItem().toString().isEmpty())
                options.put("target", spinnerTarget.getSelectedItem().toString());
            goToFragment(new TrainingFragment(options));


        });
        builder.setNegativeButton(R.string.cancel, (dialog, id) -> {
            Log.d("Back", "Going back");
        });
        android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.show();



    }
    public void createAndShowLogoutDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.logoutDialog)
                .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        logout();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });

        // Create the AlertDialog object and return it
        builder.create();
        builder.show();
    }
}
