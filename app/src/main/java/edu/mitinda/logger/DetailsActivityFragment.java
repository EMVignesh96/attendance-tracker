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
    String course;
    String tregNo;
    String teacherCourseId = null;

    public DetailsActivityFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        updateStudentNameListView();
    }

    private void updateStudentNameListView() {
        FetchStudentsTask studentsName = new FetchStudentsTask();
        Intent intent = getActivity().getIntent();
        tregNo = intent.getStringExtra("tregNo");
        course = intent.getStringExtra("course");
        studentsName.execute(new String[] {
                tregNo,
                course
        });
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
                R.id.details_textview, new ArrayList<String>());
        listView = (ListView) view.findViewById(R.id.details_listview);
        listView.setAdapter(mStudentsAdapter);

        return view;
    }


    public class FetchStudentsTask extends AsyncTask<String[], Void, String[]> {

        private String[] getStudentsFromJson(String studentJsonString) {
            String course[] = null;
            try {
                JSONArray jsonArray = new JSONArray(studentJsonString);
                JSONObject jsonObject;
                int length = jsonArray.length();
                course = new String[length];

                for(int i = 0 ; i < length; i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    course[i] = jsonObject.optString("name").toString();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return course;
        }

        @Override
        protected String[] doInBackground(String[]... params) {
            if(params.length == 0) {
                return null;
            }

            HttpURLConnection urlConnection = null;
            URL url = null;
            String studentJsonString = null;
            BufferedReader reader = null;

            try {
                url = new URL("http://vmobilebase.hol.es/tracker/select-students.php?teacherCourseId=" + params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                if(inputStream == null) {
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;

                while((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if(buffer.length() == 0) {
                    return null;
                }

                studentJsonString = buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(urlConnection != null) {
                    urlConnection.disconnect();
                }

                if(reader != null) {
                    try {
                        reader.close();
                    } catch (Exception e) {

                    }
                }
            }

            try {
                return getStudentsFromJson(studentJsonString);
            }
            catch (Exception e) {

            }

            return null;
        }

        @Override
        protected void onPostExecute(String[] strings) {
            super.onPostExecute(strings);
            if(strings != null) {
                mStudentsAdapter.clear();
                int i = 0;
                for(String name : strings) {
                    mStudentsAdapter.add(name);
                    Log.v("Helloooooooo", mStudentsAdapter.getItem(i++));
                }
            }
        }
    }
}
