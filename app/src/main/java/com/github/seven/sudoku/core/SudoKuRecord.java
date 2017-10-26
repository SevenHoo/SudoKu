package com.github.seven.sudoku.core;

import java.util.Arrays;

/**
 * Description: 数独计算堆栈里面保存的操作 <br/>
 * Date: 2017/10/21 <br/>
 *
 * @author seven.hu@ubtrobot.com
 */

public class SudoKuRecord {
    /**
     * 数独数组
     */
    private int[] mChessBoard;
    /**
     * 当前数组最少可能数的单元格的下标
     */
    private int mCurrentIndex;

    public SudoKuRecord(int[] chessboard, int gridIndex){

        this.mChessBoard = Arrays.copyOf(chessboard,chessboard.length);
        this.mCurrentIndex = gridIndex;
    }

    public int[] getChessBoard() {
        return mChessBoard;
    }

    public int getCurrentIndex() {
        return mCurrentIndex;
    }
}
