package com.example.fittrain.ui.training;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.fittrain.R;
import com.example.fittrain.model.ResponseContainer;
import com.example.fittrain.model.TrainingResponse;
import com.example.fittrain.model.UserResponse;
import com.example.fittrain.retrofit.generator.AuthType;
import com.example.fittrain.retrofit.generator.ServiceGenerator;
import com.example.fittrain.retrofit.services.TrainingService;
import com.example.fittrain.ui.auth.LoginActivity;
import com.example.fittrain.ui.common.DashboardActivity;
import com.example.fittrain.ui.training.dummy.DummyContent;
import com.example.fittrain.ui.training.dummy.DummyContent.DummyItem;
import com.example.fittrain.util.ViewModelUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TrainingFragment extends Fragment {

    private ViewModelUser mViewModel;
    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    Context ctx;
    String token;
    private UserResponse user;
    private int height, weight, trainingYears;
    MyTrainingRecyclerViewAdapter adapter;
    List<TrainingResponse> trainingList = new ArrayList<>();
    private TrainingService trainingService;
    Map<String, String> options = new HashMap<>();

    public TrainingFragment() {
    }

    @SuppressWarnings("unused")
    public static TrainingFragment newInstance(int columnCount) {
        TrainingFragment fragment = new TrainingFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = ViewModelProviders.of(getActivity()).get(ViewModelUser.class);
        mViewModel.getUserSelected().observe(getActivity(),
                userReceived -> {
                    user = userReceived;
                });

        user = mViewModel.getUserSelected().getValue();


        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_training_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            ctx = getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(ctx));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(ctx, mColumnCount));
            }

            //TODO AQUI SE HARIA UNA PETICION U OTRA DEPENDIENDO DE SUS DATOS
            //double result = calculateImc();

        }
        return view;
    }
    public double calculateImc(){
        int comparative=0, divisor=100;
        double heighReceived=0.0;

     double imc=0;
     if (height!=comparative && weight!=comparative ){
         heighReceived = height/divisor;
         heighReceived = heighReceived*heighReceived;
         imc = weight / heighReceived;
     }


     return imc;
    }






    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(DummyItem item);
    }


    public void loadTraining(RecyclerView recyclerView){
        trainingService= ServiceGenerator.createService(TrainingService.class);
        Call<ResponseContainer<TrainingResponse>> call = trainingService.listAll();
        call.enqueue(new Callback<ResponseContainer<TrainingResponse>>() {
            @Override
            public void onResponse(Call<ResponseContainer<TrainingResponse>> call, Response<ResponseContainer<TrainingResponse>> response) {
                if (!response.isSuccessful()) {
                    Log.e("error response", "code error");
                    Toast.makeText(getActivity(), "Error in request", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("successful response", "code error");

                    trainingList = response.body().getRows();

                    adapter = new MyTrainingRecyclerViewAdapter(
                            ctx,
                            trainingList);
                    recyclerView.setAdapter(adapter);


                }
            }

            @Override
            public void onFailure(Call<ResponseContainer<TrainingResponse>> call, Throwable t) {
                Log.e("failure", "failure in petition");
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }
}
