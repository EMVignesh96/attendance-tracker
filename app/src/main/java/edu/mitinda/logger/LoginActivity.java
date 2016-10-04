package edu.mitinda.logger;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {
    EditText mUsername;
    EditText mPassword;
    LocalDB localDB;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUsername = (EditText) findViewById(R.id.username);
        mPassword = (EditText) findViewById(R.id.password);
        localDB = new LocalDB(this);
    }

    public void onSignInClick(View view) {
        String username = mUsername.getText().toString();
        String password = mPassword.getText().toString();

        User user = new User(username, password);
        authenticate(user);
    }

    private void authenticate(final User user) {
        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.fetchDataInBackground(user, new GetUserCallback() {
            @Override
            public void done(User returnedUser) {
                if (returnedUser == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setMessage("Username/Password incorrect");
                    builder.setPositiveButton("OK", null);
                    builder.show();
                }
                else {
                    localDB.storeData(returnedUser);
                    localDB.setUserLoggedIn(true);

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("tregNo", user.username);
                    startActivity(intent);
                }
            }
        });
    }
}