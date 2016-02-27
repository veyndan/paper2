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
                R.drawable.profile,
                descriptions
        ));
        return posts;
    }

}
