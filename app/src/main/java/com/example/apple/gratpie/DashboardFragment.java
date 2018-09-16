package com.example.apple.gratpie;


import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.apple.gratpie.Database.PieChartData;
import com.example.apple.gratpie.Database.PieChartDatabase;
import com.example.apple.gratpie.Utils.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import androidx.navigation.Navigation;

import static android.content.Context.MODE_PRIVATE;


/**
 * Dashboard Fragment where user can see calendar and choose date
 */
public class DashboardFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    private CalendarView calendarView;
    private TextView thingsIAmGreatfulForTextView;
    private int counter;
    private String currentDate;
    private String currentDateWithoutZero;
    private Bundle bundle;
    boolean doubleBackToExitPressedOnce = false;

    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear,
                          int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        calendarView.setDate(calendar.getTimeInMillis());
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences prefs = getActivity().getSharedPreferences(Constants.CURRENT_DATE_PREF, MODE_PRIVATE);
        currentDate = prefs.getString("currentDate", null);
        if(currentDate != null) {
            String month = currentDate.substring(4, 6);
            if (month.contains("0")) {
                month = month.replace("0", "");
                currentDateWithoutZero = currentDate.substring(0, 4) + month + currentDate.substring(6);
            } else {
                currentDateWithoutZero = currentDate;
            }
        }
        fetchDataFromDB();
        initViews();
    }

    private void fetchDataFromDB() {
        AsyncTask asyncTask = new AsyncTask<Void, Void, Void>() {
            PieChartData[] pieChartData;

            @Override
            protected void onPostExecute(Void aVoid) {
                counter = 0;
                for (int i = 0; i < pieChartData.length; i++) {
                    counter = pieChartData[i].getCounter();
                }
                if (counter > 0) {
                    String grateful = getString(R.string.things_i_am_grateful_for);
                    thingsIAmGreatfulForTextView.setText(grateful + " " + counter);
                } else {
                    thingsIAmGreatfulForTextView.setText("No Entry on this day");
                }
            }

            @Override
            protected Void doInBackground(Void... voids) {
                pieChartData = PieChartDatabase.getInstance(getActivity())
                        .getPieChartDao()
                        .getPieChartData(currentDateWithoutZero);
                return null;
            }
        }.execute();
    }

    //Initializing views here
    private void initViews() {
        getActivity().findViewById(R.id.sharing_imageview).setVisibility(View.GONE);
        calendarView = getActivity().findViewById(R.id.calendar);
        thingsIAmGreatfulForTextView = getActivity().findViewById(R.id.things_i_am_greatful_count);
        Button gotoDate = getActivity().findViewById(R.id.button_goto_date);
        gotoDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
                new DatePickerDialog(getContext(), DashboardFragment.this, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarV, int year, int month, int day) {
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                String dateStr = day + "/" + month + "/" + year;
                SimpleDateFormat curFormater = new SimpleDateFormat("dd/MM/yyyy");
                Date dateObj = Calendar.getInstance().getTime();
                try {
                    dateObj = curFormater.parse(dateStr);
                    dateObj.setMonth(month);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String date = df.format(dateObj);
                calendar.set(year, month, day);
                int dayNumber = calendar.get(Calendar.DAY_OF_WEEK);
                month = month+1;
                bundle = new Bundle();
                bundle.putString(getString(R.string.date), Constants.EMPTY_STRING + year + month + day);
                bundle.putLong(getString(R.string.getTimeInMili), calendar.getTimeInMillis());
                bundle.putString(getString(R.string.formatted_date), date);
                bundle.putString(getString(R.string.day), Constants.dayInWeek[dayNumber - 1]);
                Navigation.findNavController(calendarView).navigate(R.id.pieFragment, bundle);
                SharedPreferences.Editor editor = getActivity().getSharedPreferences(Constants.CURRENT_DATE_PREF, MODE_PRIVATE).edit();

                if(month >0 && month  <10){
                    editor.putString("currentDate", Constants.EMPTY_STRING + year + "0" +month + day);
                } else {
                    editor.putString("currentDate", Constants.EMPTY_STRING + year + month + day);
                }
                editor.apply();
            }
        });

        // If counter is not null the assign it and set date
        if (currentDate != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, Integer.parseInt(currentDate.substring(0,4)));
            calendar.set(Calendar.MONTH, Integer.parseInt(currentDate.substring(4,6))-1);
            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(currentDate.substring(6)));
            calendarView.setDate(calendar.getTimeInMillis());
        }

        /**
         * Setting date in calendar from date picker
         */
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                calendarView.setDate(calendar.getTimeInMillis());
            }
        };
    }
}
