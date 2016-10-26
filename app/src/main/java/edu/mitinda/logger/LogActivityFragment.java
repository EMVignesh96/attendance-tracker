package edu.mitinda.logger;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

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

import static edu.mitinda.logger.DetailsActivityFragment.teacherCourseId;

/**
 * A placeholder fragment containing a simple view.
 */
public class LogActivityFragment extends Fragment {
    private ArrayAdapter<String> mNameAdapter;
    private ListView listView;
    private static String teacherCourseId;
    private int mSelectedItem = -1;

    @Override
    public void onStart() {
        super.onStart();
        updateList();
    }

    public LogActivityFragment() {
    }

    private void updateList() {
        Intent intent = getActivity().getIntent();
        teacherCourseId = intent.getStringExtra("teacherCourseId");
        FetchStudentsNameTask studentsNameTask = new FetchStudentsNameTask();
        String [] teacherCId = new String [2];
        teacherCId[0] = "haha";
        teacherCId[1] = teacherCourseId + "";
        studentsNameTask.execute(teacherCId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_log, container, false);

        mNameAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.attendance_listitem_textview,
                R.id.name_regno_textview, new ArrayList<String>());

        listView = (ListView) view.findViewById(R.id.name_regno_listview);
        listView.setAdapter(mNameAdapter);


        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_log, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_log) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            Toast.makeText(getActivity(), "Attendance logged successfully.", Toast.LENGTH_LONG).show();
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public class FetchStudentsNameTask extends AsyncTask<String[], Void, String[]> {

        private String[] getStudentsFromJson(String studentJsonString) {
            String name[] = null;
            String rollNo[] = null;
            String list[] = null;
            try {
                JSONArray jsonArray = new JSONArray(studentJsonString);
                JSONObject jsonObject;
                int length = jsonArray.length();
                name = new String[length];
                rollNo = new String [length];
                list = new String[length];

                for(int i = 0 ; i < length; i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    rollNo[i] = jsonObject.getString("regNo");
                    name[i] = jsonObject.optString("name").toString();
                    list[i] = rollNo[i] + "\t\t" + name[i];
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return list;
        }

        @Override
        protected String[] doInBackground(String[]... params) {
            if(params.length == 0) {
                //Log.v("ZZZZZZZZZZZ", params[0]);
                return null;
            }
            String tcid = params[0][1];

            Log.v("ZZZZZZZZZZZ", tcid);

            HttpURLConnection urlConnection = null;
            URL url = null;
            String studentJsonString = null;
            BufferedReader reader = null;

            try {
                //Log.v("SSSSSSSSSSSSSS", params[0]);
                url = new URL("http://vmobilebase.hol.es/tracker/select-students.php?teacherCourseId=" + params[0][1]);
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
                Log.v("JSON String", studentJsonString);

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
                mNameAdapter.clear();
                //int i = 0;
                for(String name : strings) {
                    mNameAdapter.add(name);
                    //Log.v("Helloooooooo", mStudentsAdapter.getItem(i++));
                }
            }
        }
    }


}
