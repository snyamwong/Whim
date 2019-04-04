package com.example.whim;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class SearchFragment extends Fragment
{
    private OnFragmentInteractionListener mListener;

    private View.OnClickListener buttonListener = v ->
    {
        String tag = (String) v.getTag();

        Fragment fragment = new FilterFragment();
        Bundle bundle = new Bundle();
        bundle.putString("cuisine", tag);
        fragment.setArguments(bundle);

        MainActivity mainActivity = (MainActivity) this.getActivity();
        mainActivity.hideTabLayout();

        assert getFragmentManager() != null;
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction.hide(this);
        transaction.add(R.id.main_content, fragment);
        transaction.commit();
    };

    public SearchFragment()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance()
    {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        View view = inflater.inflate(R.layout.fragment_search, container, false);

        Button burgerButton = view.findViewById(R.id.burger);
        burgerButton.setTag("burger");
        burgerButton.setOnClickListener(buttonListener);

        Button italianButton = view.findViewById(R.id.italian);
        italianButton.setTag("italian");
        italianButton.setOnClickListener(buttonListener);

        Button mexicanButton = view.findViewById(R.id.mexican);
        mexicanButton.setTag("mexican");
        mexicanButton.setOnClickListener(buttonListener);

        Button japaneseButton = view.findViewById(R.id.japanese);
        japaneseButton.setTag("japanese");
        japaneseButton.setOnClickListener(buttonListener);

        Button chineseButton = view.findViewById(R.id.chinese);
        chineseButton.setTag("chinese");
        chineseButton.setOnClickListener(buttonListener);

        Button thaiButton = view.findViewById(R.id.thai);
        thaiButton.setTag("thai");
        thaiButton.setOnClickListener(buttonListener);

        Button pizzaButton = view.findViewById(R.id.pizza);
        pizzaButton.setTag("pizza");
        pizzaButton.setOnClickListener(buttonListener);

        // Inflate the layout for this fragment
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri)
    {
        if (mListener != null)
        {
            mListener.onFragmentInteraction(uri);
        }
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

    public interface OnFragmentInteractionListener
    {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
