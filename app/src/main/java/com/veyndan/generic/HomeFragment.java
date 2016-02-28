package com.veyndan.generic;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

        HomeAdapter adapter = new HomeAdapter(getActivity(), initPosts());
        recyclerView.setAdapter(adapter);

        return view;
    }

    private List<Post> initPosts() {

        // TODO: Gif loading in Android M in the recyclerview problem
        // https://stackoverflow.com/questions/33363107/warning-using-glide-in-recyclerview

        List<Post> posts = new ArrayList<>();

        // Header
        posts.add(new Post(
                "Veyndan Stuart",
                "Now",
                "Public",
                null,
                "https://scontent-lhr3-1.xx.fbcdn.net/hphotos-frc3/v/t1.0-9/1098101_1387041911520027_1668446817_n.jpg?oh=85cb27b32003fb5080e73e18d03bbbc4&oe=574FB4F9",
                null
        ));

        // Items
        List<Post.Description> descriptions = new ArrayList<>();
        descriptions.add(new Post.Description(
                "Headed out on the road for a couple weeks with these two guys.",
                Post.Description.TYPE_PARAGRAPH
        ));
        descriptions.add(new Post.Description(
                "https://pbs.twimg.com/media/CcKdUFtWIAATHrg.jpg:small",
                Post.Description.TYPE_IMAGE
        ));
        descriptions.add(new Post.Description(
                "Hello world",
                Post.Description.TYPE_PARAGRAPH
        ));
        posts.add(new Post(
                "Mindy Kaling",
                "Yesterday",
                "Public",
                "40K",
                "https://scontent-lhr3-1.xx.fbcdn.net/hphotos-xfp1/t31.0-8/333069_254128021295538_1439569292_o.jpg",
                descriptions
        ));
        return posts;
    }

}
