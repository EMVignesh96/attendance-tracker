package edu.mitinda.logger;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private ArrayAdapter<String> mCourseAdapter;
    private ListView listView;


    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_main, container, false);

        String data[] = {
                "IT8501 Computer Networks",
                "IT8007 Graph Theory",
                "IT8513 Socially Relevant Project"
        };
        ArrayList<String> courses = new ArrayList<String>(Arrays.asList(data));

        mCourseAdapter = new ArrayAdapter<String>(getActivity(), R.layout.courses_textview,
                R.id.courses_viewtext, courses/*new ArrayList<String>()*/);
        listView = (ListView) root.findViewById(R.id.courses_listview);
        listView.setAdapter(mCourseAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                startActivity(intent);
            }
        });

        return root;
    }
}
