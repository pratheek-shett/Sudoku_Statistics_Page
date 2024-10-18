package com.example.sudoku_statistics_page;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.net.ConnectivityManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.transition.TransitionManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

import app.futured.donut.DonutProgressView;
import app.futured.donut.DonutSection;

public class MainActivity extends AppCompatActivity {

    private DonutProgressView donutView;
    private DonutProgressView donutView2;
    private TextView winsText;
    private TextView timeText;
    private ProgressBar progressBar;
    private TextView totalGamesText;
    private TextView avgCompletionTimeText;
    private TextView avgMistakesText;
    private TextView totalHoursText;
    private DatabaseReference databaseReference;
    private static boolean isActivityVisible; // Flag to prevent multiple refreshes
    boolean isDataRefreshed = false; // Flag to check if data is refreshed
    private String userId = "userUID3"; // Example: Change to userUID1 or userUID2 as needed
    private NetworkChangeReceiver networkChangeReceiver; // Network receiver
    private View overlay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase database reference
        databaseReference = FirebaseDatabase.getInstance("https://sudoku-2abc2-default-rtdb.firebaseio.com/")
                .getReference("users").child(userId).child("sudokuStats");

        // Initialize views
        donutView = findViewById(R.id.donut_view);
        donutView2 = findViewById(R.id.donut_view2);
        winsText = findViewById(R.id.wins_text);
        timeText = findViewById(R.id.time_text);
        totalGamesText = findViewById(R.id.totalgames);
        avgCompletionTimeText = findViewById(R.id.avgcompletion);
        avgMistakesText = findViewById(R.id.avgmistakes);
        totalHoursText = findViewById(R.id.totalhours);
        progressBar = findViewById(R.id.progressbar);
        overlay = findViewById(R.id.overlay);

        // Back Button (optional functionality)
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());

        // Set placeholder text to avoid layout shifts before data loads
        setPlaceholderText();
      
        // Register network change receiver
        networkChangeReceiver = new NetworkChangeReceiver(this);
        registerReceiver(networkChangeReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        // Check network availability and fetch data accordingly
        if (!NetworkUtils.isNetworkAvailable(this)) {
            Toast.makeText(MainActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
            Intent in = new Intent(MainActivity.this, no_internet.class);
            startActivity(in);
            finish();
        } else {
            fetchDataFromFirebase();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkChangeReceiver); // Unregister to prevent leaks
    }

    @Override
    protected void onResume() {
        super.onResume();
        isActivityVisible = true; // Mark activity as visible
    }

    @Override
    protected void onPause() {
        super.onPause();
        isActivityVisible = false; // Mark activity as not visible
    }

    public static boolean isActivityVisible() {
        return isActivityVisible;
    }

    // Refresh data from Firebase
    public void refreshDataFromFirebase() {
        if (!isDataRefreshed) {
            fetchDataFromFirebase(); // Call your existing fetch method
            isDataRefreshed = true; // Set the flag to true to prevent looping
        }
    }

    private void fetchDataFromFirebase() {
        showLoading(true);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Integer totalWins = dataSnapshot.child("totalWins").getValue(Integer.class);
                    Float bestCompletionTime = dataSnapshot.child("bestCompletionTime").getValue(Float.class);
                    Integer totalGamesPlayed = dataSnapshot.child("totalGames").getValue(Integer.class);
                    Float averageCompletionTime = dataSnapshot.child("averageCompletionTime").getValue(Float.class);
                    Integer averageMistakes = dataSnapshot.child("averageMistakes").getValue(Integer.class);
                    Float totalHoursPlayed = dataSnapshot.child("totalHoursPlayed").getValue(Float.class);

                    if (totalWins != null && bestCompletionTime != null && totalGamesPlayed != null &&
                            averageCompletionTime != null && averageMistakes != null && totalHoursPlayed != null) {
                        Log.d("MainActivity", "Total Wins: " + totalWins + ", Best Completion Time: " + bestCompletionTime);
                        runOnUiThread(() -> updateUI(totalWins, bestCompletionTime, totalGamesPlayed, averageCompletionTime, averageMistakes, totalHoursPlayed));
                    } else {
                        redirectToNoDataPage();
                        Log.e("MainActivity", "Data is null!");
                    }
                } else {
                    redirectToNoDataPage();
                    Log.e("MainActivity", "Data does not exist!");
                }
                showLoading(false); // Hide progress bar after fetching
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("MainActivity", "Database error: " + databaseError.getMessage());
                progressBar.setVisibility(View.GONE); // Hide progress bar on error
            }
        });
    }

    private void updateUI(int totalWins, float bestCompletionTime, int totalGamesPlayed, float avgCompletionTime, int avgMistakes, float totalHoursPlayed) {
        winsText.setText(String.valueOf(totalWins));
        timeText.setText(String.valueOf(bestCompletionTime));
        totalGamesText.setText("Total Games Played: " + totalGamesPlayed);
        avgCompletionTimeText.setText("Average Completion Time: " + avgCompletionTime + " min");
        avgMistakesText.setText("Average Mistakes: " + avgMistakes);
        totalHoursText.setText("Total Hours Played: " + totalHoursPlayed + " hrs");

        // Update donut charts
        // Normalize the values to fit into the Donut chart
        float totalWinsNormalized = totalWins / 10f;  // Assuming the max wins is 10
        float bestTimeNormalized = Math.min(bestCompletionTime, 5f) / 5f;  // Assuming 5 minutes as the maximum time

        // Define two sections for the donut chart (for wins and time)
        DonutSection winsSection = new DonutSection(
                "wins_section",
                Color.parseColor("#3D52A0"),  // Color for wins
                totalWinsNormalized  // Wins data
        );

        DonutSection timeSection = new DonutSection(
                "time_section",
                Color.parseColor("#3D52A0"),  // Color for best time
                bestTimeNormalized  // Time data
        );

        // Use transitions for smooth layout updates
        TransitionManager.beginDelayedTransition(findViewById(android.R.id.content));

        // Set the cap (maximum) of the donut views (1.0 = 100%)
        donutView.setCap(1f);
        donutView2.setCap(1f);

        // Submit the sections with animation
        donutView.submitData(Arrays.asList(winsSection));
        donutView2.submitData(Arrays.asList(timeSection));
    }

    private void setPlaceholderText() {
        winsText.setText("0");
        timeText.setText("0.0");
        totalGamesText.setText("Loading...");
        avgCompletionTimeText.setText("Loading...");
        avgMistakesText.setText("Loading...");
        totalHoursText.setText("Loading...");
    }

    public void resetDataRefreshFlag() {
        isDataRefreshed = false;
    }

    private void showLoading(boolean isLoading) {
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
            overlay.setVisibility(View.VISIBLE); // Show the overlay
        } else {
            progressBar.setVisibility(View.GONE);
            overlay.setVisibility(View.GONE); // Hide the overlay
        }


    }
    private void redirectToNoDataPage() {
        Intent intent = new Intent(MainActivity.this, Nodatafound.class);
        startActivity(intent);
        finish();  // Finish the current activity so that the user cannot go back to it
    }
}

