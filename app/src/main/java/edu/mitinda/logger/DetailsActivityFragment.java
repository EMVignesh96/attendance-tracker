package edu.mitinda.logger;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsActivityFragment extends Fragment {
    ArrayAdapter<String> mStudentsAdapter;
    ListView listView;
    String params[];
    String teacherCourseId = null;

    public DetailsActivityFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
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
