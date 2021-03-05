package com.example.einzelbeispiel;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class mainScreen extends AppCompatActivity {

    EditText mEdit;
    TextView tcpResult;
    TextView featureResult;

    @SuppressLint("StaticFieldLeak")
    public class NetworkCallTCP extends AsyncTask<String, int[], String> {
        @Override
        protected String doInBackground(String... params) {
            String resultFromTCPRequest = "";
            if (params.length != 0) {
                Socket clientSocket = null;
                try {
                    clientSocket = new Socket("se2-isys.aau.at", 53212);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                DataOutputStream outToServer = null;
                try {
                    outToServer = new DataOutputStream(clientSocket.getOutputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                BufferedReader inFromServer = null;
                try {
                    inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    outToServer.writeBytes(params[0] + '\n');
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    resultFromTCPRequest = inFromServer.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return resultFromTCPRequest;
        }

        @Override
        protected void onPostExecute(String resultFromTCPRequest) {
            super.onPostExecute(resultFromTCPRequest);
            tcpResult = (TextView) findViewById(R.id.textViewResponseTCPResult);
            tcpResult.setText(resultFromTCPRequest);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        mEdit = (EditText) findViewById(R.id.editTextNumberSigned2);
    }

    public void runTCP(View v) {
        new NetworkCallTCP().execute(mEdit.getText().toString());
    }

    public void runFeature(View v) {
        
    }
}