package com.example.todotasks;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements TaskRecyclerView{

    RecyclerView recyclerView;
    TasksAdapter tasksAdapter;
    ArrayList<TaskModel> taskModels;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.view_tasks);
        taskModels = new ArrayList<>();
        try {
            JSONArray result = new GetAllRequestTask().execute("http://10.0.2.2:8080/api/tasks").get();
            if (result != null) {
                // Loop through the JSON array
                for (int i = 0; i < result.length(); i++) {
                    try {
                        JSONObject jsonObject = result.getJSONObject(i);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            taskModels.add(
                                    new TaskModel(
                                            jsonObject.getInt("id"),
                                            jsonObject.getString("title"),
                                            jsonObject.getString("description"),
                                            jsonObject.getBoolean("status"),
                                            LocalDateTime.parse(jsonObject.getString("dueDate"))
                                    ));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                tasksAdapter = new TasksAdapter(this , this, taskModels, R.layout.card_item_layout);
                recyclerView.setAdapter(tasksAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            }

        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }



        Button addButton = findViewById(R.id.add_btn);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddFormActivity.class));
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(MainActivity.this, TaskActivity.class);
        intent.putExtra("model", taskModels.get(position));
        startActivity(intent);
    }



    // Retrieve single task
    private static class GetRequestTask extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... urls) {
            String response = "";
            JSONObject jsonObject = null;
            try {
                // Create a URL object
                URL url = new URL(urls[0]);

                // Open a connection to the URL
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                // Get the response code
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Read the response
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    reader.close();
                    response = stringBuilder.toString();

                    // Parse the JSON response
                    jsonObject = new JSONObject(response);
                } else {
                    response = "Error: " + responseCode;
                }
                connection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }


            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);
            // Handle the JSON response here, e.g., update UI
            if (result != null) {
                try {
                    Log.e("Id", result.getString("id"));
                    Log.e("Title", result.getString("title"));
                    Log.e("Description", result.getString("description"));
                    Log.e("Status", result.getString("status"));
                    Log.e("Due Date", result.getString("dueDate"));
                    Log.e("Creation Date", result.getString("creationDate"));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    // Retrieve all tasks
    private static class GetAllRequestTask extends AsyncTask<String, Void, JSONArray> {

        @Override
        protected JSONArray doInBackground(String... urls) {
            String response = "";
            JSONArray jsonArray = null;
            try {
                // Create a URL object
                URL url = new URL(urls[0]);

                // Open a connection to the URL
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                // Get the response code
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Read the response
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    reader.close();
                    response = stringBuilder.toString();

                    // Parse the JSON response
                    jsonArray = new JSONArray(response);
                } else {
                    response = "Error: " + responseCode;
                }
                connection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return jsonArray;
        }

        @Override
        protected void onPostExecute(JSONArray result) {
            super.onPostExecute(result);
            // Handle the JSON response here, e.g., update UI
            if (result != null) {
                // Loop through the JSON array
                for (int i = 0; i < result.length(); i++) {
                    try {
                        JSONObject jsonObject = result.getJSONObject(i);
                        Log.e("Id", jsonObject.getString("id"));
                        Log.e("Title", jsonObject.getString("title"));
                        Log.e("Description", jsonObject.getString("description"));
                        Log.e("Status", jsonObject.getString("status"));
                        Log.e("Due Date", jsonObject.getString("dueDate"));
                        Log.e("Creation Date", jsonObject.getString("creationDate"));
                        ArrayList<TaskModel> tasks = new ArrayList<>();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            tasks.add(
                                    new TaskModel(
                                            jsonObject.getInt("id"),
                                            jsonObject.getString("title"),
                                            jsonObject.getString("description"),
                                            jsonObject.getBoolean("status"),
                                            LocalDateTime.parse(jsonObject.getString("dueDate"))
                                            ));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


}