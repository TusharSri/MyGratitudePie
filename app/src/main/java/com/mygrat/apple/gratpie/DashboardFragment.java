package com.mygrat.apple.gratpie;


import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mygrat.apple.gratpie.Database.PieChartData;
import com.mygrat.apple.gratpie.Database.PieChartDatabase;
import com.mygrat.apple.gratpie.Utils.Constants;
import com.mygrat.apple.gratpie.caldroid.CaldroidFragment;
import com.mygrat.apple.gratpie.caldroid.CaldroidListener;
import com.mygrat.apple.gratpie.caldroid.DateTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import androidx.navigation.Navigation;

import static android.content.Context.MODE_PRIVATE;


/**
 * Dashboard Fragment where user can see calendar and choose date
 */
public class DashboardFragment extends Fragment {

    public static final String TAG = DashboardFragment.class.getSimpleName();
    private TextView thingsIAmGreatfulForTextView;
    private int counter;
    private String currentDate;
    private Bundle bundle;
    private CaldroidFragment caldroidFragment;
    private CaldroidFragment dialogCaldroidFragment;
    private Map<DateTime, Integer> dateTextColors = new HashMap<>();
    private ArrayList<DateTime> boldDates = new ArrayList<>();
    final SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
    private boolean isDialogOpen = false;
    private Date dt;
    private int selectedMonth;
    private int selectedYear;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dt = new Date();
        dt = Calendar.getInstance().getTime();
        selectedMonth = dt.getMonth() + 1;
        selectedYear = dt.getYear() + 1900;
    }

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
        caldroidFragment = new CaldroidFragment();
    }

    /**
     * Save current states of the Caldroid here
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);

        if (caldroidFragment != null) {
            caldroidFragment.saveStatesToKey(outState, "CALDROID_SAVED_STATE");
        }

        if (dialogCaldroidFragment != null) {
            dialogCaldroidFragment.saveStatesToKey(outState,
                    "DIALOG_CALDROID_SAVED_STATE");
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (caldroidFragment != null)
            caldroidFragment.restoreStatesFromKey(savedInstanceState, "CALDROID_SAVED_STATE");
        if (dialogCaldroidFragment != null)
            dialogCaldroidFragment.restoreStatesFromKey(savedInstanceState, "DIALOG_CALDROID_SAVED_STATE");
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences prefs = getActivity().getSharedPreferences(Constants.CURRENT_DATE_PREF, MODE_PRIVATE);
        currentDate = prefs.getString("currentDate", null);
        getGratitudeCountForMonth(dt.getMonth() + 1, dt.getYear() + 1900);
    }

    private void setCalendar() {
        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.THEME_RESOURCE, R.style.CaldroidTrans);
        args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
        args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);
        args.putInt(CaldroidFragment.MONTH, selectedMonth);
        args.putInt(CaldroidFragment.YEAR, selectedYear);
        caldroidFragment.setArguments(args);
        caldroidFragment.setTextColorForDateTimes(dateTextColors);
        caldroidFragment.setBoldDates(boldDates);
        // Attach to the activity
        FragmentTransaction t = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
        t.setCustomAnimations(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
        t.replace(R.id.calendar, caldroidFragment);
        t.commit();

        // Setup listener
        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {
                dt = date;
                selectedMonth = date.getMonth()+1;
                selectedYear = date.getYear()+1900;
                int day = date.getDate();
                int month = date.getMonth();
                int year = date.getYear() + 1900;
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day);
                int dayNumber = calendar.get(Calendar.DAY_OF_WEEK);
                month = month + 1;
                bundle = new Bundle();
                bundle.putString(getString(R.string.date), Constants.EMPTY_STRING + year + month + day);
                bundle.putLong(getString(R.string.getTimeInMili), date.getTime());
                bundle.putString(getString(R.string.formatted_date), formatter.format(date));
                bundle.putString(getString(R.string.day), Constants.dayInWeek[dayNumber - 1]);
                if (isDialogOpen) {
                    isDialogOpen = false;
                    dialogCaldroidFragment.dismiss();
                }
                Navigation.findNavController(thingsIAmGreatfulForTextView).navigate(R.id.pieFragment, bundle);
                SharedPreferences.Editor editor = getActivity().getSharedPreferences(Constants.CURRENT_DATE_PREF, MODE_PRIVATE).edit();

                String finalDate = "";
/*                if (month > 0 && month < 10) {
                    finalDate = year + "0" + month;
                } else {
                    finalDate = year + "" + month;
                }

                if (day > 0 && day < 10) {
                    finalDate = finalDate + "0" + day;
                } else {
                    finalDate = finalDate + "" + day;
                }*/
                finalDate = year + "" + month;
                finalDate = finalDate + "" + day;
                editor.putString("currentDate", finalDate);
                editor.apply();

            }

            @Override
            public void onChangeMonth(int month, int year) {
                String text = "month: " + month + " year: " + year;
                /*Toast.makeText(getActivity(), text,
                        Toast.LENGTH_SHORT).show();*/
                caldroidFragment.setBackgroundDrawableForDate(null,dt);
                dt.setMonth(month - 1);
                dt.setYear(year - 1900);

                int day = dt.getDate();

                String finalDate = "";
                finalDate = year + "" + month;
                finalDate = finalDate + "" + day;
                SharedPreferences.Editor editor = getActivity().getSharedPreferences(Constants.CURRENT_DATE_PREF, MODE_PRIVATE).edit();
                editor.putString("currentDate", finalDate);
                editor.apply();
                currentDate = finalDate;

                getGratitudeCountForMonth(month, year);
            }

            @Override
            public void onLongClickDate(Date date, View view) {
               /* Toast.makeText(getActivity(),
                        "Long click " + formatter.format(date),
                        Toast.LENGTH_SHORT).show();*/
            }

            @Override
            public void onCaldroidViewCreated() {
                if (caldroidFragment.getLeftArrowButton() != null) {
                  /*  Toast.makeText(getActivity(),
                            "Caldroid view is created", Toast.LENGTH_SHORT)
                            .show();*/
                }
            }

        };

        // Setup Caldroid
        caldroidFragment.setCaldroidListener(listener);
        getActivity().findViewById(R.id.sharing_imageview).setVisibility(View.GONE);
        thingsIAmGreatfulForTextView = getActivity().findViewById(R.id.things_i_am_greatful_count);

        caldroidFragment.setBackgroundDrawableForDate(Objects.requireNonNull(getContext()).getResources().getDrawable(R.drawable.red_border_dark), dt);
        caldroidFragment.refreshView();
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
                caldroidFragment.moveToDate(dt);
            }

            @Override
            protected Void doInBackground(Void... voids) {
                pieChartData = PieChartDatabase.getInstance(getActivity())
                        .getPieChartDao()
                        .getPieChartData(currentDate);
                return null;
            }
        }.execute();
    }

    private void getGratitudeCountForMonth(int month, int year) {
        new AsyncTask<Integer, Void, Void>() {
            Calendar calendar = Calendar.getInstance();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dateTextColors.clear();
                boldDates.clear();
            }

            @Override
            protected Void doInBackground(Integer... args) {
                int selectedMonth = args[0];
                int selectedYear = args[1];
                int currentMonth = calendar.get(Calendar.MONTH) + 1;
                int dateCount = 0;
                if (selectedMonth < currentMonth) {
                    switch (selectedMonth) {
                        case 1:
                        case 3:
                        case 5:
                        case 7:
                        case 8:
                        case 10:
                        case 12:
                            dateCount = 31;
                            break;
                        case 2:
                            if (selectedYear % 400 == 0 || (selectedYear % 4 == 0 && selectedYear % 100 == 0)) {
                                dateCount = 29;
                            } else {
                                dateCount = 28;
                            }
                            break;
                        default:
                            dateCount = 30;
                    }
                } else {
                    dateCount = calendar.get(Calendar.DAY_OF_MONTH);
                }
                for (int i = 1; i <= dateCount; i++) {
                    String dateText = String.format("%d%d%d", selectedYear, selectedMonth, i);
                    PieChartData[] pieChartData = PieChartDatabase.getInstance(getActivity()).getPieChartDao().getPieChartData(dateText);
                    if (pieChartData.length > 0) {
                        int gratitudeCount = pieChartData[pieChartData.length - 1].getCounter();
                        if (gratitudeCount > 0) {
                            DateTime dateTime = new DateTime(selectedYear, selectedMonth, i, 0, 0, 0, 0);
                            dateTextColors.put(dateTime, R.color.caldroid_white);
                            boldDates.add(dateTime);
                        }
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                setCalendar();
                fetchDataFromDB();
            }
        }.execute(month, year);
    }
}


