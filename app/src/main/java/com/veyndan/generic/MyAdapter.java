package com.veyndan.generic;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private String[] dataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView name;
        final TextView about;
        final TextView description;
        final ImageView profile;
        final ImageView image;
        final AppCompatImageButton heart;

        public ViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.name);
            about = (TextView) v.findViewById(R.id.about);
            description = (TextView) v.findViewById(R.id.description);
            heart = (AppCompatImageButton) v.findViewById(R.id.heart);
            profile = (ImageView) v.findViewById(R.id.profile);
            image = (ImageView) v.findViewById(R.id.image);

            heart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    heart.setSelected(!heart.isSelected());
                }
            });

            // Make profile picture black and white
            ColorMatrix matrix = new ColorMatrix();
            matrix.setSaturation(0);
            profile.setColorFilter(new ColorMatrixColorFilter(matrix));
        }
    }

    public MyAdapter(String[] myDataset) {
        dataset = myDataset;
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return dataset.length;
    }
}