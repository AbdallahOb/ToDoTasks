package com.example.todotasks;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Calendar;

public class TaskActivity extends AppCompatActivity {

    ///////// Define fields /////////
    public static TextInputLayout title, description;
    public static TextView dueDateTextView;
    private CardView dueDateCardView;
    private Button addBtn, saveBtn, deleteBtn;;
    private Calendar calendar;
    private static LocalDateTime dueDate;
    private static CheckBox doneCB;
    public static TaskModel taskModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_task);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ///////// Connect fields /////////
        title = findViewById(R.id.title_editText);
        description = findViewById(R.id.description_editText);
        dueDateCardView = findViewById(R.id.due_date_cardview);
        dueDateTextView = findViewById(R.id.due_date_textview);
        addBtn = findViewById(R.id.addButton);
        calendar = Calendar.getInstance();
        doneCB = findViewById(R.id.status_cb);
        saveBtn = findViewById(R.id.saveButton);
        deleteBtn = findViewById(R.id.deleteButton);

        //////// Fetch the model data and binding the fields ////////
        Intent intent = getIntent();
        taskModel = (TaskModel) intent.getSerializableExtra("model");
        Log.e("Model", taskModel.toString());


        title.getEditText().setText(taskModel.getTitle());
        description.getEditText().setText(taskModel.getDescription());
        dueDate = taskModel.getDueDate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dueDateTextView.setText(dueDate.getYear()+"-"+dueDate.getMonth().getValue()+"-"+dueDate.getDayOfMonth()+" | "+dueDate.getHour()+":"+dueDate.getMinute());
        }

        //////// Updating process ////////
        dueDateCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePickerDialog();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String result = String.valueOf(new PutRequestTask().execute("http://10.0.2.2:8080/api/tasks"));
               startActivity(new Intent(TaskActivity.this, MainActivity.class));
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = String.valueOf(new DeleteRequestTask().execute("http://10.0.2.2:8080/api/tasks/"+taskModel.getId()));
                startActivity(new Intent(TaskActivity.this, MainActivity.class));
            }
        });




    }

    private static class PutRequestTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            try {
                // Create a URL object
                URL url = new URL(urls[0]);

                // Open a connection to the URL
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("PUT");
                connection.setRequestProperty("Content-Type", "application/json; utf-8");
                connection.setRequestProperty("Accept", "application/json");
                connection.setDoOutput(true);

                // Create the JSON object to send
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id",taskModel.getId());
                jsonObject.put("title",title.getEditText().getText().toString());
                jsonObject.put("description", description.getEditText().getText().toString());
                jsonObject.put("status", doneCB.isChecked());
                jsonObject.put("dueDate", dueDate);

                // Write JSON object to output stream
                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = jsonObject.toString().getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                // Get the response code
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                    // Read the response
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    reader.close();
                    response = stringBuilder.toString();
                } else {
                    response = "Error: " + responseCode;
                }
                connection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // Handle the response here, e.g., update UI
            System.out.println("Response: " + result);
        }
    }

    private static class DeleteRequestTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            try {
                // Create a URL object
                URL url = new URL(urls[0]);

                // Open a connection to the URL
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("DELETE");
                connection.setRequestProperty("Accept", "application/json");

                // Get the response code
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_NO_CONTENT) {
                    // Read the response
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    reader.close();
                    response = stringBuilder.toString();
                } else {
                    response = "Error: " + responseCode;
                }
                connection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // Handle the response here, e.g., update UI
            System.out.println("Response: " + result);
        }
    }

    private void showDateTimePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Update the calendar with the selected date
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        // Once the date is selected, show the time picker dialog
                        TimePickerDialog timePickerDialog = new TimePickerDialog(
                                TaskActivity.this,
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                        // Update the calendar with the selected time
                                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                        calendar.set(Calendar.MINUTE, minute);

                                        // Convert to LocalDateTime
                                        LocalDateTime selectedDateTime = null;
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                            selectedDateTime = LocalDateTime.of(
                                                    calendar.get(Calendar.YEAR),
                                                    calendar.get(Calendar.MONTH) + 1, // Month is 0-based
                                                    calendar.get(Calendar.DAY_OF_MONTH),
                                                    calendar.get(Calendar.HOUR_OF_DAY),
                                                    calendar.get(Calendar.MINUTE),
                                                    0
                                            );
                                        }

                                        // Do something with the selected date and time
                                        Log.e("Time","Selected Date and Time: " + selectedDateTime);
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                            dueDateTextView.setText(selectedDateTime.getYear()+"-"+selectedDateTime.getMonth().getValue()+"-"+selectedDateTime.getDayOfMonth()+" | "+selectedDateTime.getHour()+":"+selectedDateTime.getMinute());
                                        }
                                        dueDate = selectedDateTime;

                                    }
                                },
                                calendar.get(Calendar.HOUR_OF_DAY),
                                calendar.get(Calendar.MINUTE),
                                true
                        );
                        timePickerDialog.show();
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

}