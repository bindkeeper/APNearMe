package com.ap.bindkeeper.apnearme;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by User on 09/06/2016.
 */
public class Fragment_Search extends Fragment implements  SearchView.OnQueryTextListener, LocationListener {

    SearchAdapter recyclerAdapter;
    RecyclerView recyclerView;
    SearchView mSearchView;
    DBHelper helper;

    String searchCriteria;

    private LocationManager locationManager;

    public Fragment_Search() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_search, container, false);

        helper = new DBHelper(this.getContext());

        mSearchView = (SearchView) v.findViewById(R.id.searchView1);
        mSearchView.setOnQueryTextListener(this);

        locationManager = (LocationManager) this.getContext().getSystemService(getContext().LOCATION_SERVICE);

        // create the receiver object to read the broadcast
        PlacesReceiver receiver = new PlacesReceiver();
        // create IntentFilter ==> the connection to a specific action
        IntentFilter filter = new IntentFilter("com.ap.bindkeeper.apnearme.SEARCH_RESULT");
        // register the receiver ==> start listening
        LocalBroadcastManager.getInstance(this.getContext()).registerReceiver(receiver, filter);

        return v;
    }

    private void setAdapter (ArrayList<Place> places) {
        recyclerAdapter = new SearchAdapter(places);
        recyclerView = (RecyclerView) this.getView().findViewById(R.id.searchResults);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(recyclerAdapter);
    }

    @Override
    public void onResume() {
        mSearchView.onActionViewExpanded();
        super.onResume();
    }


    @Override
    public boolean onQueryTextSubmit(String query) {

        try{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
          //  locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, this, Looper.getMainLooper());
        }
        catch (SecurityException ex){
            Toast.makeText(this.getContext(), "Error - no permission", Toast.LENGTH_SHORT).show();
        }

        searchCriteria = query;
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onLocationChanged(Location location) {

        try{
            locationManager.removeUpdates(this);
        }
        catch (SecurityException e){}

        Toast.makeText(getContext(), "GPS Changed", Toast.LENGTH_SHORT).show();

        double lat = location.getLatitude();
        double lon = location.getLongitude();

        // 7> don't listen to any more updates!


        Intent in=new Intent(this.getContext(),SearchIntentService.class);
        in.putExtra("search",searchCriteria);
        in.putExtra("lat", lat);
        in.putExtra("lon", lon);
        this.getContext().startService(in);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    private class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder>{


        private ArrayList<Place> places;


        public SearchAdapter (ArrayList<Place> strings) {
            this.places = strings;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View v = inflater.inflate(R.layout.search_item, parent, false);
            return new MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.bindText( places.get(position));
        }

        @Override
        public int getItemCount() {
            return places.size();
        }

        public class MyViewHolder  extends RecyclerView.ViewHolder{

            private TextView name;
            private TextView address;


            public MyViewHolder(View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.item_Search_Name);
                address = (TextView) itemView.findViewById(R.id.item_Search_Address);
            }

            public void bindText(Place place) {
                name.setText(place.getName());
                address.setText(place.getAddress());
            }
        }
    }

    public class PlacesReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            ArrayList<Place> places = (ArrayList<Place>) intent.getSerializableExtra("places");
            Toast.makeText(Fragment_Search.this.getContext(),  places.size() +"Places Received", Toast.LENGTH_SHORT).show();
            helper.setSearch(places);
            setAdapter(helper.getSearch());

        }
    }
}
