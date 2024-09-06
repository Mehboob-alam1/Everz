package com.example.everz;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment implements SliderAdapter.OnSliderButtonClickListener{

    private ViewPager2 viewPager2;
    private Adapter adapter;
    private DatabaseReference databaseReference;
    private ArrayList<Blog> list;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home, container, false);
        viewPager2 = view.findViewById(R.id.viewPager);
        recyclerView= view.findViewById(R.id.homeRecyclerView);

        List<SliderItem> sliderItems = new ArrayList<>();
        sliderItems.add(new SliderItem(R.drawable.slider1, "Secure your tomorrow", "Intelligence that\nprotects"));
        sliderItems.add(new SliderItem(R.drawable.slider2, "Navigate the future", "AI-Driven\nTransformation"));
        sliderItems.add(new SliderItem(R.drawable.slider3, "Beyond Boundaries", "Pioneering the\nDigital Frontier7"));

        viewPager2.setAdapter(new SliderAdapter(sliderItems,this));

        autoSlide();
        databaseReference= FirebaseDatabase.getInstance().getReference("blogs").child("Home");
        list=new ArrayList<>();


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    list.clear();

                    for (DataSnapshot snap: snapshot.getChildren()){

                        Blog blog =snap.getValue(Blog.class);
                        list.add(blog);
                    }

                    adapter= new Adapter(getContext(),list);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerView.setAdapter(adapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return  view;
    }

    private void autoSlide() {
        final Handler handler = new Handler();
        final Runnable runnable = () -> {
            if (viewPager2.getCurrentItem() < 2) {
                viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
            } else {
                viewPager2.setCurrentItem(0);
            }
        };

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(runnable);
            }
        }, 4000, 4000); // Auto slide every 4 seconds
    }

    @Override
    public void onSliderButtonClick() {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, new AboutFragment());
        transaction.addToBackStack(null); // Optional: Add this transaction to the back stack
        transaction.commit();

        // Notify the Bottom Navigation
        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_about);
    }
}