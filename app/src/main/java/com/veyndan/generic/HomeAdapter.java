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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.VH> {
    @SuppressWarnings("unused")
    private static final String TAG = LogUtils.makeLogTag(HomeAdapter.class);

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private final Context context;
    private final List<Post> posts;
    private final Resources res;

    public HomeAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
        res = context.getResources();
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_HEADER:
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.header, parent, false);
                return new VHHeader(v, context);
            case TYPE_ITEM:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item, parent, false);
                return new VHItem(v, context);
            default:
                throw new IllegalStateException("No type that matches " + viewType);
        }
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        if (holder instanceof VHHeader) {
            VHHeader vhHeader = (VHHeader) holder;
            Post post = getPost(position);
            Glide.with(context).load(post.getProfile()).into(vhHeader.profile);
            vhHeader.name.setText(post.getName());
            vhHeader.date.setText(context.getString(R.string.date, post.getDate()));

            EditText paragraph = (EditText) LayoutInflater.from(vhHeader.description.getContext())
                    .inflate(R.layout.description_paragraph_new, vhHeader.description, false);
            vhHeader.description.addView(paragraph);
        } else if (holder instanceof VHItem) {
            VHItem vhItem = (VHItem) holder;
            Post post = getPost(position);
            Glide.with(context).load(post.getProfile()).into(vhItem.profile);
            vhItem.name.setText(post.getName());
            vhItem.about.setText(context.getString(R.string.about, post.getDate(), post.getVisibility()));

            try {
                int pinCount = Integer.parseInt(post.getPins());
                vhItem.pins.setText(res.getQuantityString(R.plurals.pins, pinCount, pinCount));
            } catch (NumberFormatException e) {
                vhItem.pins.setText(res.getQuantityString(R.plurals.pins, -1, post.getPins()));
            }

            for (Post.Description description : post.getDescriptions()) {
                switch (description.getType()) {
                    case Post.Description.TYPE_PARAGRAPH:
                        TextView paragraph = (TextView) LayoutInflater.from(vhItem.description.getContext())
                                .inflate(R.layout.description_paragraph, vhItem.description, false);
                        vhItem.description.addView(paragraph);
                        paragraph.setText(description.getBody());
                        break;
                    case Post.Description.TYPE_IMAGE:
                        ImageView image = (ImageView) LayoutInflater.from(vhItem.description.getContext())
                                .inflate(R.layout.description_image, vhItem.description, false);
                        vhItem.description.addView(image);
                        Glide.with(context).load(description.getBody()).into(image);
                        break;
                    default:
                        Log.e(TAG, String.format("Unknown description type: %d", description.getType()));
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? TYPE_HEADER : TYPE_ITEM;
    }

    private Post getPost(int position) {
        return posts.get(position);
    }

    public static class VH extends RecyclerView.ViewHolder {

        final LinearLayout description;
        final TextView name;
        final ImageView profile;

        public VH(View v) {
            super(v);
            description = (LinearLayout) v.findViewById(R.id.description);
            name = (TextView) v.findViewById(R.id.name);
            profile = (ImageView) v.findViewById(R.id.profile);

            // Make profile picture black and white
            ColorMatrix matrix = new ColorMatrix();
            matrix.setSaturation(0);
            profile.setColorFilter(new ColorMatrixColorFilter(matrix));
        }
    }

    public static class VHHeader extends VH {
        @SuppressWarnings("unused")
        private static final String TAG = LogUtils.makeLogTag(VHItem.class);

        final TextView date;
        final Spinner visibility;

        public VHHeader(View v, Context context) {
            super(v);
            date = (TextView) v.findViewById(R.id.date);
            visibility = (Spinner) v.findViewById(R.id.visibility);

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                    R.array.visibility, R.layout.spinner_visibility);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            visibility.setAdapter(adapter);

        }
    }

    public static class VHItem extends VH {
        @SuppressWarnings("unused")
        private static final String TAG = LogUtils.makeLogTag(VHItem.class);

        final TextView about;
        final Button pins;
        final LinearLayout description;
        final ToggleButton heart, code, basket;
        final AppCompatImageButton more;

        public VHItem(View v, Context context) {
            super(v);
            about = (TextView) v.findViewById(R.id.about);
            pins = (Button) v.findViewById(R.id.pins);
            description = (LinearLayout) v.findViewById(R.id.description);
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
}