package com.laolarou.helloworld;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class FindPrimesActivity extends AppCompatActivity {
    private TextView lastCheckedNumberText;
    private TextView lastFoundPrimeNumberText;
    private Button stopButton;
    private volatile boolean isRunning = false;
    private boolean stopRequested = false;
    private volatile int lastCheckedNumber = 3;
    private volatile int lastPrimeNumber = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_find_primes);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        lastCheckedNumberText = findViewById(R.id.lastCheckedNumberText);
        lastFoundPrimeNumberText = findViewById(R.id.lastFoundPrimeNumberText);

        if (savedInstanceState != null) {
            lastCheckedNumber = savedInstanceState.getInt("lastCheckedNumber", 3);
            lastPrimeNumber = savedInstanceState.getInt("lastPrimeNumber", 2);

            if (lastCheckedNumber != 3 && lastPrimeNumber != 2)
                startPrimeThread();
        }

        this.getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (!isRunning) return;

                new AlertDialog.Builder(FindPrimesActivity.this)
                        .setTitle("Confirm Exit")
                        .setMessage("Are you sure you want to terminate the search?")
                        .setPositiveButton("Yes", (dialog, which) -> finish())
                        .setNegativeButton("No", null)
                        .show();
            }
        });
    }

    public void startSearch(View v) {
        startPrimeThread();
    }

    public void stopSearch(View v) {
        stopPrimeSearch();
    }

    private void resetState() {
        if (!stopRequested) return;

        lastCheckedNumber = 3;
        lastPrimeNumber = 2;
        stopRequested = false;
    }

    private void startPrimeThread() {
        isRunning = true;
        resetState();

        var primeThread = new Thread(() -> {
            for (int i = lastCheckedNumber; isRunning; i += 2) { // Skip even numbers
                if (isPrime(i)) {
                    lastPrimeNumber = i;
                    updateUI(String.valueOf(lastCheckedNumber), String.valueOf(i));
                }
                lastCheckedNumber = i;
            }
        });
        primeThread.start();
    }

    private void stopPrimeSearch() {
        isRunning = false;
        stopRequested = true;
        lastCheckedNumber = 3;
        lastPrimeNumber = 2;
    }

    private boolean isPrime(int num) {
        if (num < 2) return false;
        for (int i = 2; i * i <= num; i++) {
            if (num % i == 0) return false;
        }
        return true;
    }

    private void updateUI(String lastVerifiedNumber, String lastPrimeNumber) {
        getMainExecutor().execute(new Runnable() {
            @Override
            public void run() {
                lastCheckedNumberText.setText(lastVerifiedNumber);
                lastFoundPrimeNumberText.setText(lastPrimeNumber);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        resetState();

        isRunning = false;

        outState.putInt("lastCheckedNumber", lastCheckedNumber);
        outState.putInt("lastPrimeNumber", lastPrimeNumber);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isRunning = false; // Stop the thread safely
    }
}