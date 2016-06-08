package com.ap.bindkeeper.apnearme;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by User on 08/06/2016.
 */
public class Frag_Favorites extends Fragment{




    public Frag_Favorites() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_favorites, container, false);

        ArrayList<Place> list = new ArrayList<>();
        list.add(new Place("MCdonalds", "Tel Aviv"));
        list.add(new Place("Deda", "Givataim"));
        list.add(new Place("Arugula", "Ramat Gan"));

        MyAdapter recyclerAdapter = new MyAdapter(list);
        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recyclerList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(recyclerAdapter);
        return v;
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        private ArrayList<Place> strings;
        public MyAdapter (ArrayList<Place> strings) {
            this.strings = strings;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View v = inflater.inflate(R.layout.favourite_item, parent, false);
            return new MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.bindText( strings.get(position));
        }

        @Override
        public int getItemCount() {
            return strings.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder{
            private TextView name;
            private TextView address;

            public MyViewHolder(View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.item_Name);
                address = (TextView) itemView.findViewById(R.id.item_Address);

            }

            public void bindText(Place place) {
                name.setText(place.getName());
                address.setText(place.getAddress());
            }
        }
    }



}
