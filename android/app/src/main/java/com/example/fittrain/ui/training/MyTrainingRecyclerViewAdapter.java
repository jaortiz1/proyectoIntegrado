package com.example.fittrain.ui.training;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.fittrain.R;
import com.example.fittrain.model.TrainingResponse;
import com.example.fittrain.ui.training.TrainingFragment.OnListFragmentInteractionListener;
import com.example.fittrain.ui.training.dummy.DummyContent.DummyItem;
import com.example.fittrain.util.UtilToken;

import java.util.List;

public class MyTrainingRecyclerViewAdapter extends RecyclerView.Adapter<MyTrainingRecyclerViewAdapter.ViewHolder> {

    private final List<TrainingResponse> mValues;
    private final Context ctx;
    String jwt;
    public MyTrainingRecyclerViewAdapter(Context context, List<TrainingResponse> trainingList) {
        this.ctx = context;
        this.mValues=trainingList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_training, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        jwt = UtilToken.getToken(ctx);
        if (holder.mItem.getPicture()!=null){
            Glide
                    .with(ctx)
                    .load(holder.mItem.getPicture())
                    .into(holder.imageViewPicture);
        }else{
            Glide
                    .with(ctx)
                    .load("https://www.eecs.utk.edu/wp-content/uploads/2016/02/Symonds_EECS.jpg")
                    .into(holder.imageViewPicture);
        }
        holder.textViewTotalExercises.setText(holder.mItem.getExercises().size());
        holder.textViewTitle.setText(holder.mItem.getName());
        holder.textViewTarget.setText(holder.mItem.getTarget());

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public TextView textViewTitle, textViewTotalExercises, textViewTarget;
        public ImageView imageViewPicture;
        public TrainingResponse mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            imageViewPicture = mView.findViewById(R.id.imageViewCover);
            textViewTarget = mView.findViewById(R.id.textViewObjetive);
            textViewTitle = mView.findViewById(R.id.textViewName);
            textViewTotalExercises = mView.findViewById(R.id.textViewTotalExercises);

        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}
