package com.example.apple.gratpie;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.apple.gratpie.Database.PieChartData;
import com.example.apple.gratpie.Database.PieChartDatabase;
import com.example.apple.gratpie.Utils.Constants;

import java.util.ArrayList;

import androidx.navigation.Navigation;


/**
 * A simple {@link Fragment} subclass.
 */
public class PieFragment extends Fragment implements View.OnClickListener {

    Button attachMomentButton;
    TextView dayOfMonthTextView;
    TextView formattedDateTextView;
    Bundle bundle;
    TextView moment1;
    TextView moment2;
    TextView moment3;
    TextView moment4;
    TextView moment5;
    ImageView momentFile1;
    ImageView momentFile2;
    ImageView momentFile3;
    ImageView momentFile4;
    ImageView momentFile5;
    private String formattedDate;
    private String day;
    private int counter = 0;
    protected ArrayList<String> mAttachUrl = new ArrayList<>();
    protected ArrayList<String> mMomentDescription = new ArrayList<>();
    private String date;
    private int selectDateRequestCode = 191;
    private PieChartData[] pieChartData;
    private boolean isNotEdited = true;
    private long getTimeInMili;

    public PieFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pie, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (counter >= 5) {
            attachMomentButton.setVisibility(View.GONE);
        }
        initViews();
        fetchDataFromDB();
    }

    //Initializing Views here
    private void initViews() {
        getActivity().findViewById(R.id.sharing_imageview).setVisibility(View.VISIBLE);
        RelativeLayout pieLayout = getActivity().findViewById(R.id.relative_pie);
        Animation aniSlide = AnimationUtils.loadAnimation(getContext(), R.anim.zoom_in);
        pieLayout.startAnimation(aniSlide);
        attachMomentButton = getActivity().findViewById(R.id.attach_moment_button);
        attachMomentButton.setOnClickListener(this);
        dayOfMonthTextView = getActivity().findViewById(R.id.day_textview);
        formattedDateTextView = getActivity().findViewById(R.id.date_textview);
        moment1 = getActivity().findViewById(R.id.moment1);
        moment2 = getActivity().findViewById(R.id.moment2);
        moment3 = getActivity().findViewById(R.id.moment3);
        moment4 = getActivity().findViewById(R.id.moment4);
        moment5 = getActivity().findViewById(R.id.moment5);

        momentFile1 = getActivity().findViewById(R.id.moment_file1);
        momentFile2 = getActivity().findViewById(R.id.moment_file2);
        momentFile3 = getActivity().findViewById(R.id.moment_file3);
        momentFile4 = getActivity().findViewById(R.id.moment_file4);
        momentFile5 = getActivity().findViewById(R.id.moment_file5);

        day = getArguments().getString(getString(R.string.day));
        date = getArguments().getString(getString(R.string.date));
        getTimeInMili = getArguments().getLong(getString(R.string.getTimeInMili));
        dayOfMonthTextView.setText(day);
        formattedDate = getArguments().getString(getString(R.string.formatted_date));
        formattedDateTextView.setText(formattedDate);
    }

    private void fetchDataFromDB() {
        AsyncTask asyncTask = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPostExecute(Void aVoid) {
                if (pieChartData.length == 0 && isNotEdited) {
                    momentAttached();
                }
                mMomentDescription.clear();
                mAttachUrl.clear();
                for (int i = 0; i < pieChartData.length; i++) {
                    counter = pieChartData[i].getCounter();
                    if (counter > 0) {
                        mMomentDescription.add(pieChartData[i].getMomentDesc());
                        if(null == pieChartData[i] || null == pieChartData[i].getAttachedUrl()){
                            mAttachUrl.add("null");
                        } else {
                            mAttachUrl.add(pieChartData[i].getAttachedUrl());
                        }
                        setValues(mMomentDescription, mAttachUrl);
                    }
                    if (counter >= 5) {
                        attachMomentButton.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            protected Void doInBackground(Void... voids) {
                pieChartData = PieChartDatabase.getInstance(getActivity())
                        .getPieChartDao()
                        .getPieChartData(date);
                return null;
            }
        }.execute();
    }

    public void setValues(ArrayList<String> val, ArrayList<String> url) {
        if (val.size() >= 1) {
            moment1.setText(val.get(0));
            if (url.size() >= 1 && url.get(0)!= null && !url.get(0).contains("null")) {
                momentFile1.setVisibility(View.VISIBLE);
            }
            getActivity().findViewById(R.id.relative_moment1).setOnClickListener(this);
        }
        if (val.size() >= 2) {
            moment2.setText(val.get(1));
            if (url.size() >= 2 && url.get(1)!= null && !url.get(1).contains("null")) {
                momentFile2.setVisibility(View.VISIBLE);
            }
            getActivity().findViewById(R.id.relative_moment2).setOnClickListener(this);
        }
        if (val.size() >= 3) {
            moment3.setText(val.get(2));
            if (url.size() >= 3 && url.get(2)!= null && !url.get(2).contains("null")) {
                momentFile3.setVisibility(View.VISIBLE);
            }
            getActivity().findViewById(R.id.relative_moment3).setOnClickListener(this);
        }
        if (val.size() >= 4) {
            moment4.setText(val.get(3));
            if (url.size() >= 4 && url.get(3)!= null && !url.get(3).contains("null")) {
                momentFile4.setVisibility(View.VISIBLE);
            }
            getActivity().findViewById(R.id.relative_moment4).setOnClickListener(this);
        }
        if (val.size() >= 5) {
            moment5.setText(val.get(4));
            if (url.size() >= 5 && url.get(4)!= null && !url.get(4).contains("null")) {
                momentFile5.setVisibility(View.VISIBLE);
            }
            getActivity().findViewById(R.id.relative_moment5).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.relative_moment1:
                showMomentActivity(0);
                break;
            case R.id.relative_moment2:
                showMomentActivity(1);
                break;
            case R.id.relative_moment3:
                showMomentActivity(2);
                break;
            case R.id.relative_moment4:
                showMomentActivity(3);
                break;
            case R.id.relative_moment5:
                showMomentActivity(4);
                break;
            case R.id.attach_moment_button:
                momentAttached();
                break;
        }
    }

    private void showMomentActivity(int i) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.MOMENT_DESCRIPTION, mMomentDescription.get(i) + "");
        bundle.putString(Constants.MOMENT_ATTACH_FILE, mAttachUrl.get(i) + "");
        bundle.putString(getString(R.string.day), day);
        bundle.putString(getString(R.string.formatted_date), formattedDate);
        bundle.putString(getString(R.string.date), date);
        bundle.putLong(getString(R.string.getTimeInMili), getTimeInMili);
        bundle.putInt(getString(R.string.counter), i);
        Navigation.findNavController(attachMomentButton).navigate(R.id.showMomentFragment, bundle);
    }

    public void momentAttached() {
        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.day), day);
        bundle.putString(getString(R.string.formatted_date), formattedDate);
        bundle.putString(getString(R.string.date), date);
        bundle.putLong(getString(R.string.getTimeInMili), getTimeInMili);
        isNotEdited = false;
        Navigation.findNavController(attachMomentButton).navigate(R.id.editMomentFragment, bundle);
    }
}
