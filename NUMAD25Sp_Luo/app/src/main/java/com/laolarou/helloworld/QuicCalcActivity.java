package com.laolarou.helloworld;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class QuicCalcActivity extends AppCompatActivity {
    private final List<String> _pendingCalc = new ArrayList<>();
    private TextView _resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quic_calc);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        _resultTextView = findViewById(R.id.resultText);
    }

    @SuppressLint("SetTextI18n")
    private void refreshTextView(){
        if (_pendingCalc.isEmpty()){
            _resultTextView.setText("Calc");
            return;
        }

        _resultTextView.setText(String.join("", _pendingCalc));
    }

    public void inputNum(View view){
        if (!(view instanceof Button)) return;

        var btnCh = ((Button) view).getText().toString();

        _pendingCalc.add(btnCh);
        refreshTextView();
    }

    public void backspace(View view){
        if (_pendingCalc.isEmpty()){
            _resultTextView.setText("Calc");
            return;
        }

        _pendingCalc.remove(_pendingCalc.size() - 1);
        refreshTextView();
    }

    @SuppressLint("SetTextI18n")
    public void calc(View view){
        if (_pendingCalc.isEmpty()) return;

        var result = 0;
        var currentNumber = 0;
        var sign = 1;

        for (var op : _pendingCalc){
            var ch = op.charAt(0);

            if (Character.isDigit(ch)){
                currentNumber = currentNumber * 10 + (ch - '0');
                continue;
            }

            result += sign * currentNumber;
            currentNumber = 0;
            sign = (ch == '+') ? 1 : -1;
        }

        result += sign * currentNumber;

        _pendingCalc.clear();
        _resultTextView.setText(String.valueOf(result));
    }
}