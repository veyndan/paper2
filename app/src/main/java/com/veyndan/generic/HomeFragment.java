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
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

        HomeAdapter adapter = new HomeAdapter(getActivity(), initPosts());
        recyclerView.setAdapter(adapter);

        return recyclerView;
    }

    private List<Post> initPosts() {

        // TODO: Gif loading in Android M in the recyclerview problem
        // https://stackoverflow.com/questions/33363107/warning-using-glide-in-recyclerview

        List<Post> posts = new ArrayList<>();

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
