package com.github.seven.sudoku.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.seven.sudoku.R;
import com.github.seven.sudoku.core.Sudoku;
import com.github.seven.sudoku.utils.MyLogger;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Sudoku sudoku = new Sudoku(4);
        sudoku.fillChessBoardRow(1,0,0,1,0);
        sudoku.fillChessBoardRow(2,0,3,0,4);
        sudoku.fillChessBoardRow(3,3,0,4,0);
        sudoku.fillChessBoardRow(4,0,2,0,0);
        boolean success = sudoku.calculate();
        MyLogger.log().d("sudoku.calculate() ---> " + success);
        if(!success){
            return;
        }
    }
}
