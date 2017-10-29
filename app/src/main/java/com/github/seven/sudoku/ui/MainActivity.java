package com.github.seven.sudoku.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.github.seven.sudoku.R;
import com.github.seven.sudoku.core.SudoKuRecord;
import com.github.seven.sudoku.core.Sudoku;
import com.github.seven.sudoku.utils.ChessBoardUtils;
import com.github.seven.sudoku.utils.MyLogger;

public class MainActivity extends AppCompatActivity implements AdapterListener.OnItemClickListener{

    private RecyclerView mChessBoardView;
    private GridLayoutManager mGridLayoutManager;
    private ChessBoardAdapter mAdapter;
    private Sudoku mSudoKu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mChessBoardView = (RecyclerView) findViewById(R.id.chess_board_recycler_view);
        mChessBoardView.setOverScrollMode(View.OVER_SCROLL_NEVER); //去除边界的蓝色阴影

        createOrResetChessBoard(4);
    }

    @Override
    public void onItemClick(View view, int position, Object extra) {
        mAdapter.check(position);
    }

    private boolean calculate(){

        int row = mAdapter.getRow();
        int [] value = mAdapter.getData();

        mSudoKu = new Sudoku(row);
        mSudoKu.fillChessBoard(value);
        return mSudoKu.calculate();
    }


    public void switchMode(View view){
        switch (view.getId()){
            case R.id.easy_mode_button:
                createOrResetChessBoard(4);
                break;

            case R.id.hard_mode_button:
                createOrResetChessBoard(5);
                break;
        }
    }

    public void fill(View view){

        switch (view.getId()){

            case R.id.angle_button:
                mAdapter.setCheckedData(ChessBoardUtils.RES_INDEX_ANGLE);
                break;

            case R.id.rect_button:
                mAdapter.setCheckedData(ChessBoardUtils.RES_INDEX_RECT);
                break;

            case R.id.circle_button:
                mAdapter.setCheckedData(ChessBoardUtils.RES_INDEX_CIRCLE);
                break;

            case R.id.ten_button:
                mAdapter.setCheckedData(ChessBoardUtils.RES_INDEX_CROSS);
                break;

            case R.id.star_button:
                mAdapter.setCheckedData(ChessBoardUtils.RES_INDEX_STAR);
                break;
        }
    }

    public void control(View view){

        switch (view.getId()){

            case R.id.start_calculate_button:

                if(!calculate()){
                    Toast.makeText(this,"此题无解",Toast.LENGTH_LONG).show();
                    return;
                }

                //转换显示出来
                int chessBoard[] = mSudoKu.getChessBoard();
                for (int i = 0; i < chessBoard.length; i ++){
                    chessBoard[i] = - chessBoard[i];
                }

                mAdapter.setData(chessBoard);
                break;

            case R.id.clear_data_button:
                createOrResetChessBoard(mAdapter.getRow());
                break;
        }
    }


    private void createOrResetChessBoard(int row){

        if(mGridLayoutManager == null){
            mGridLayoutManager = new GridLayoutManager(this,row,GridLayoutManager.VERTICAL,false);
            mChessBoardView.setLayoutManager(mGridLayoutManager);

            mAdapter = new ChessBoardAdapter(this);
            mAdapter.setRow(row);
            mAdapter.setOnItemClickListener(this);

            mChessBoardView.setAdapter(mAdapter);
        }

        //只需重置数据即可
        if(mAdapter.getRow() == row){
            mAdapter.setRow(row);
            mAdapter.notifyDataSetChanged();
           return;
        }

        //刷新整个棋盘
        mAdapter.setRow(row);
        mGridLayoutManager.setSpanCount(row);
        mChessBoardView.setAdapter(mAdapter);
    }


}
