package com.example.whim;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.LOCATION_SERVICE;

public class FilterFragment extends Fragment
{

    private OnFragmentInteractionListener mListener;

    private LocationManager locationManager;

    String[] distance = {"0.5 Mile", "1 Mile", "2.5 Miles", "5 Miles", "10 Miles"};

    public FilterFragment()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FilterFragment.
     */
    public static FilterFragment newInstance()
    {
        FilterFragment fragment = new FilterFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        /*
        Spinner dropdown = getView().findViewById(R.id.distance_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,distance);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(this);
        */
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_filter, container, false);

        Button buttonSubmit = view.findViewById(R.id.submit);

        buttonSubmit.setOnClickListener(listener -> {

            Fragment fragment = new PlaceFragment();

            FragmentTransaction transaction = getFragmentManager().beginTransaction();

            transaction.replace(R.id.main_content, fragment).commit();
        });

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

        if (context instanceof OnFragmentInteractionListener)
        {
            mListener = (OnFragmentInteractionListener) context;
        }
        else
        {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    private void createLocationManager()
    {
        LocationManager locationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener()
        {
            @Override
            public void onLocationChanged(Location location)
            {

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras)
            {

            }

            @Override
            public void onProviderEnabled(String provider)
            {

            }

            @Override
            public void onProviderDisabled(String provider)
            {

            }
        });
    }

    /**
     *
     * @return
     */
    private Map<String, String> getFields()
    {
        Map<String, String> fields = new HashMap<>();

        if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        Double latitude = location.getLatitude();
        Double longitude = location.getLongitude();
        String dollarParam = getPriceParam();
        String starParam = getStarParam();

        fields.put("latitude", latitude.toString());
        fields.put("longitude", longitude.toString());
        fields.put("price", dollarParam);
        fields.put("star", starParam);

        return fields;
    }

    /**
     *
     * @return
     */
    private String getPriceParam()
    {
        ArrayList<String> price = new ArrayList<>();

        CheckBox fourDollar = getView().findViewById(R.id.four_dollar);
        CheckBox threeDollar = getView().findViewById(R.id.three_dollar);
        CheckBox twoDollar = getView().findViewById(R.id.two_dollar);
        CheckBox oneDollar = getView().findViewById(R.id.one_dollar);

        if (fourDollar.isChecked())
        {
            price.add("4");
        }
        if(threeDollar.isChecked())
        {
            price.add("3");
        }
        if(twoDollar.isChecked())
        {
            price.add("2");
        }
        if(oneDollar.isChecked())
        {
            price.add("1");
        }

        String priceParam = String.join(",", price);

        return priceParam;
    }

    /**
     *
     * @return
     */
    private String getStarParam()
    {
        ArrayList<String> star = new ArrayList<>();

        CheckBox fiveStar = getView().findViewById(R.id.five_star);
        CheckBox fourStar = getView().findViewById(R.id.four_star);
        CheckBox threeStar = getView().findViewById(R.id.three_star);
        CheckBox twoStar = getView().findViewById(R.id.two_star);
        CheckBox oneStar = getView().findViewById(R.id.one_star);

        if(fiveStar.isChecked())
        {
            star.add("5");
        }
        if(fourStar.isChecked())
        {
            star.add("4");
        }
        if(threeStar.isChecked())
        {
            star.add("3");
        }
        if(twoStar.isChecked())
        {
            star.add("2");
        }
        if(oneStar.isChecked())
        {
            star.add("1");
        }

        String starParam = String.join(",", star);

        return starParam;
    }

    private void createButtonListener()
    {

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
