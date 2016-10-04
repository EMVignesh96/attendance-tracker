package edu.mitinda.logger;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * A placeholder fragment containing a simple view.
 */
public class LogActivityFragment extends Fragment {

    Spinner spinner;

    public LogActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_log, container, false);

        String spinnerArray[] = {
                "IT8501",
                "IT8007",
                "IT8513"
        };
        spinner = (Spinner) view.findViewById(R.id.course_spinner);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(), R.layout.log_spinner_view, spinnerArray);
        spinnerAdapter.setDropDownViewResource(R.layout.log_spinner_view);
        spinner.setAdapter(spinnerAdapter);

        return view;
    }
}
