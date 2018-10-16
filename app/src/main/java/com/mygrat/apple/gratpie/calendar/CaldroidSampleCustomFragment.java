package com.mygrat.apple.gratpie.calendar;

import com.mygrat.apple.gratpie.caldroid.CaldroidFragment;
import com.mygrat.apple.gratpie.caldroid.CaldroidGridAdapter;

public class CaldroidSampleCustomFragment extends CaldroidFragment {

    @Override
    public CaldroidGridAdapter getNewDatesGridAdapter(int month, int year) {
        // TODO Auto-generated method stub
        return new CaldroidSampleCustomAdapter(getActivity(), month, year,
                getCaldroidData(), extraData);
    }

}