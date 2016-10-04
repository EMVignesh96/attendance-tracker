package edu.mitinda.logger;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsActivityFragment extends Fragment {
    ArrayAdapter<String> mStudentsAdapter;
    ListView listView;

    public DetailsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_details, container, false);

        String data[] = {
                "2014506022\t\t100%",
                "2014506035\t\t50%",
                "2014506059\t\t80%",
                "2014506060\t\t96%",
                "2014506061\t\t50%"
        };
        ArrayList<String> students = new ArrayList<String>(Arrays.asList(data));

        mStudentsAdapter = new ArrayAdapter<String>(getActivity(), R.layout.details_listitem_textview,
                R.id.details_textview, students);
        listView = (ListView) view.findViewById(R.id.details_listview);
        listView.setAdapter(mStudentsAdapter);

        return view;
    }
}
