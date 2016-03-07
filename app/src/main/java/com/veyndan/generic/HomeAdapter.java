package com.veyndan.generic;

import android.content.Context;
import android.content.res.Resources;
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
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.VH> {
    @SuppressWarnings("unused")
    private static final String TAG = LogUtils.makeLogTag(HomeAdapter.class);
    
    private Firebase ref;

    boolean firstPass = false;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private final Context context;
    private final List<Post> posts;
    private final Resources res;

    public HomeAdapter(Context context) {
        this.context = context;
        this.posts = new ArrayList<>();
        res = context.getResources();

        ref = new Firebase("https://sweltering-heat-8337.firebaseio.com/users");

        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
                HomeAdapter.this.posts.add(0, snapshot.getValue(Post.class));
                for (Post post : posts) {
                    Log.d(TAG, post.getDescriptions().toString());
                }
                notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot snapshot, String previousChildName) {

            }

            @Override
            public void onChildRemoved(DataSnapshot snapshot) {
                HomeAdapter.this.posts.remove(snapshot.getValue(Post.class));
                notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot snapshot, String previousChildName) {

            }

            @Override
            public void onCancelled(FirebaseError error) {

            }
        });
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
    public void onBindViewHolder(final VH holder, int position) {
        if (holder instanceof VHHeader) {
            if (firstPass) return;
            firstPass = true;
            final VHHeader vhHeader = (VHHeader) holder;
            Glide.with(context).load("https://scontent-lhr3-1.xx.fbcdn.net/hphotos-frc3/v/t1.0-9/1098101_1387041911520027_1668446817_n.jpg?oh=85cb27b32003fb5080e73e18d03bbbc4&oe=574FB4F9").into(vhHeader.profile);
            vhHeader.name.setText("Veyndan Stuart");
            vhHeader.date.setText(context.getString(R.string.date, "Now"));

            vhHeader.description.removeAllViews();
            EditText paragraph = (EditText) LayoutInflater.from(vhHeader.description.getContext())
                    .inflate(R.layout.description_paragraph_new, vhHeader.description, false);
            vhHeader.description.addView(paragraph);

            vhHeader.post.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick");
                    List<Post.Description> descriptions = new ArrayList<>();
                    for (int i = 0; i < vhHeader.description.getChildCount(); i++) {
                        View child = vhHeader.description.getChildAt(i);
                        if (child instanceof EditText) {
                            descriptions.add(new Post.Description(
                                    ((EditText) child).getText().toString(),
                                    Post.Description.TYPE_PARAGRAPH
                            ));
                        }
                    }
                    ref.push().setValue(new Post(
                            vhHeader.name.getText().toString(),
                            "Now",
                            vhHeader.visibility.getSelectedItem().toString(),
                            res.getQuantityString(R.plurals.pins, 0, 0),
                            "https://scontent-lhr3-1.xx.fbcdn.net/hphotos-frc3/v/t1.0-9/1098101_1387041911520027_1668446817_n.jpg?oh=85cb27b32003fb5080e73e18d03bbbc4&oe=574FB4F9",
                            descriptions
                    ));
                }
            });
        } else if (holder instanceof VHItem) {
            VHItem vhItem = (VHItem) holder;
            Post post = getPost(position - 1);
            if (post == null) return;
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
        return posts.size() + 1;
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

            UIUtils.grayscale(profile);
        }
    }

    public static class VHHeader extends VH {
        @SuppressWarnings("unused")
        private static final String TAG = LogUtils.makeLogTag(VHItem.class);

        final TextView date;
        final Button post;
        final Spinner visibility;

        public VHHeader(View v, Context context) {
            super(v);
            date = (TextView) v.findViewById(R.id.date);
            post = (Button) v.findViewById(R.id.post);
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