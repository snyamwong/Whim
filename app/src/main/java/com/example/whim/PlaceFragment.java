package com.example.whim;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yelp.fusion.client.models.Business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PlaceFragment extends Fragment {

    private Map<String, String> fields;

    public PlaceFragment()
    {
        fields = new HashMap<>();

        Bundle bundle = this.getArguments();

        if (bundle != null)
        {
            fields.put("price", Objects.requireNonNull(bundle.getString("price")));
            fields.put("longitude", Objects.requireNonNull(bundle.getString("longitude")));
            fields.put("latitude", Objects.requireNonNull(bundle.getString("latitude")));
        }

        YelpFusion yelpFusion = new YelpFusion(fields);

        try
        {
            yelpFusion.start();

            yelpFusion.join();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        ArrayList<Business> businesses = yelpFusion.getBusinesses();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_place, container, false);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener
    {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
