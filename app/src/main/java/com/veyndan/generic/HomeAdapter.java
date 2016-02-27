package com.veyndan.generic;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

        final TextView name, about, pins;
        final ImageView profile;
        final LinearLayout description;
        final ToggleButton heart, code, basket;
        final AppCompatImageButton more;

        public ViewHolder(View v, Context context) {
            super(v);
            name = (TextView) v.findViewById(R.id.name);
            about = (TextView) v.findViewById(R.id.about);
            pins = (TextView) v.findViewById(R.id.pins);
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

        try {
            int pinCount = Integer.parseInt(post.getPins());
            holder.pins.setText(res.getQuantityString(R.plurals.pins, pinCount, pinCount));
        } catch (NumberFormatException e) {
            holder.pins.setText(res.getQuantityString(R.plurals.pins, -1, post.getPins()));
        }

        for (Post.Description description : post.getDescriptions()) {
            switch (description.getType()) {
                case Post.Description.TYPE_PARAGRAPH:
                    TextView paragraph = (TextView) LayoutInflater.from(holder.description.getContext())
                            .inflate(R.layout.description_paragraph, holder.description, false);
                    holder.description.addView(paragraph);
                    paragraph.setText(description.getBody());
                    break;
                case Post.Description.TYPE_IMAGE:
                    ImageView image = (ImageView) LayoutInflater.from(holder.description.getContext())
                            .inflate(R.layout.description_image, holder.description, false);
                    holder.description.addView(image);
                    Glide.with(context).load(description.getBody()).into(image);
                    break;
                default:
                    Log.e(TAG, String.format("Unknown description type: %d", description.getType()));
            }
        }
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }
}