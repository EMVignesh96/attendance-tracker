package edu.mitinda.logger;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private ArrayAdapter<String> mCourseAdapter;
    private ListView listView;
    private String tregNo;


    public MainActivityFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        updateCourseListView();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void updateCourseListView() {
        FetchCoursesTask coursesTask = new FetchCoursesTask();
        Intent intent = getActivity().getIntent();
        tregNo = intent.getStringExtra("tregNo");
        coursesTask.execute(tregNo);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_main, container, false);

        /*String data[] = {
                "IT8501 Computer Networks",
                "IT8007 Graph Theory",
                "IT8513 Socially Relevant Project"
        };*/
        //ArrayList<String> courses = new ArrayList<String>(Arrays.asList(data));

        mCourseAdapter = new ArrayAdapter<String>(getActivity(), R.layout.courses_textview,
                R.id.courses_viewtext, new ArrayList<String>());
        listView = (ListView) root.findViewById(R.id.courses_listview);
        listView.setAdapter(mCourseAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra("course", mCourseAdapter.getItem(position));
                intent.putExtra("tregNo", tregNo);
                startActivity(intent);
            }
        });


        return root;
    }

    public class FetchCoursesTask extends AsyncTask<String, Void, String[]> {

        private String[] getCoursesDataFromJson(String coursesJsonString) {
            String course[] = null;
            try {
                JSONArray jsonArray = new JSONArray(coursesJsonString);
                JSONObject jsonObject;
                int length = jsonArray.length();
                course = new String[length];

                for(int i = 0 ; i < length; i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    course[i] = jsonObject.optString("course").toString();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return course;
        }


        @Override
        protected String[] doInBackground(String... params) {

            if(params.length == 0) {
                return null;
            }

            HttpURLConnection urlConnection = null;
            URL url = null;
            String coursesJsonString = null;
            BufferedReader reader = null;

            try {
                url = new URL("http://vmobilebase.hol.es/tracker/select-teacher-courses.php?tregNo=" + params[0]);
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

                coursesJsonString = buffer.toString();

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
                return getCoursesDataFromJson(coursesJsonString);
            }
            catch (Exception e) {

            }

            return null;
        }

        @Override
        protected void onPostExecute(String[] strings) {
            super.onPostExecute(strings);
            if(strings != null) {
                mCourseAdapter.clear();
                for(String courses : strings) {
                    mCourseAdapter.add(courses);
                }
            }
        }
    }
}
