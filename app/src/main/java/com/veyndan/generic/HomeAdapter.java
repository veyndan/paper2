package com.veyndan.generic;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    @SuppressWarnings("unused")
    private static final String TAG = LogUtils.makeLogTag(HomeAdapter.class);
    private final Context context;
    private final List<Post> posts;
    private final Resources res;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @SuppressWarnings("unused")
        private static final String TAG = LogUtils.makeLogTag(ViewHolder.class);

        final TextView name, about;
        final ImageView profile;
        final LinearLayout description;
        final ToggleButton heart, code, basket;
        final AppCompatImageButton more;

        public ViewHolder(View v, Context context) {
            super(v);
            name = (TextView) v.findViewById(R.id.name);
            about = (TextView) v.findViewById(R.id.about);
            description = (LinearLayout) v.findViewById(R.id.description);
            profile = (ImageView) v.findViewById(R.id.profile);
            heart = (ToggleButton) v.findViewById(R.id.heart);
            code = (ToggleButton) v.findViewById(R.id.code);
            basket = (ToggleButton) v.findViewById(R.id.basket);
            more = (AppCompatImageButton) v.findViewById(R.id.more);

            // Make profile picture black and white
            ColorMatrix matrix = new ColorMatrix();
            matrix.setSaturation(0);
            profile.setColorFilter(new ColorMatrixColorFilter(matrix));

            // Popup menu for QAB overflow
            final PopupMenu menu = new PopupMenu(context, more);
            menu.getMenu().add("titleRes");

            more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    menu.show();
                }
            });
        }
    }

    public HomeAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
        res = context.getResources();
    }

    @Override
    public HomeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);
        return new ViewHolder(v, context);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Post post = posts.get(position);
        Glide.with(context).load(post.getProfile()).into(holder.profile);
        holder.name.setText(post.getName());
        holder.about.setText(context.getString(R.string.about, post.getDate(), post.getVisibility()));

        TextView paragraph = (TextView) LayoutInflater.from(holder.description.getContext())
                .inflate(R.layout.description_paragraph, holder.description, false);
        holder.description.addView(paragraph);

        ImageView image = (ImageView) LayoutInflater.from(holder.description.getContext())
                .inflate(R.layout.description_image, holder.description, false);
        holder.description.addView(image);

        paragraph.setText(post.getDescriptions().get(0).getBody());
        Glide.with(context).load(post.getDescriptions().get(1).getBody()).into(image);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }
}