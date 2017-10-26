package com.github.seven.sudoku.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.seven.sudoku.R;
import com.github.seven.sudoku.core.Sudoku;
import com.github.seven.sudoku.utils.MyLogger;

public class MainActivity extends AppCompatActivity implements AdapterListener.OnItemClickListener{

    private RecyclerView mChessBoardView;
    private GridLayoutManager mGridLayoutManager;
    private ChessBoardAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mChessBoardView = (RecyclerView) findViewById(R.id.chess_board_recycler_view);
        mGridLayoutManager = new GridLayoutManager(this,4,GridLayoutManager.VERTICAL,false);
        mChessBoardView.setLayoutManager(mGridLayoutManager);
        mChessBoardView.setOverScrollMode(View.OVER_SCROLL_NEVER); //去除边界的蓝色阴影
        //mUserRecyclerView.addItemDecoration(new UserItemDecoration());
        //mUserRecyclerView.addOnScrollListener(mOnScrollListener);

        mAdapter = new ChessBoardAdapter(this);
        mAdapter.setData(new int[4 * 4]);
        mAdapter.setOnItemClickListener(this);
        mChessBoardView.setAdapter(mAdapter);
    }

    @Override
    public void onItemClick(View view, int position, Object extra) {

    }

    private void test(){

        Sudoku sudoku = new Sudoku(4);
        sudoku.fillChessBoardRow(1,0,0,1,0);
        sudoku.fillChessBoardRow(2,0,3,0,4);
        sudoku.fillChessBoardRow(3,3,0,4,0);
        sudoku.fillChessBoardRow(4,0,2,0,0);
        boolean success = sudoku.calculate();
        MyLogger.log().d("sudoku.calculate() ---> " + success);
    }
}
