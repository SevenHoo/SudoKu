package com.github.seven.sudoku.core;

import android.util.Log;

import com.github.seven.sudoku.utils.MyLogger;

import java.util.Stack;

/**
 * Description: 数独游戏 <br/>
 * Date: 2017/10/20 <br/>
 *
 * @author seven.hu@ubtrobot.com
 */

public class Sudoku {

    private final static String TAG = Sudoku.class.getSimpleName();

    private final static int ROW = 0;
    private final static int COLUMN = 1;
    /**
     * 用一个一维数组表示棋盘
     */
    private int[] mChessBoard;
    /**
     * 棋盘行数，说明这是一个mRow * mRow的数独游戏
     */
    private int mRow;
    /**
     * 操作栈，记录尝试的操作，用于回溯
     */
     private Stack<SudoKuRecord> mSudoKuOperatorStack;

    /**
     * 初始数独棋盘,指定这是一个 row * row的数独游戏,最大支持到31 * 31
     */
    public Sudoku(int row){
        this.mRow = row;
        initChessBoard(row);
    }

    private void initChessBoard(int row){

        if(!checkRow(row)){
            Log.e(TAG,"calculate fail, error: mRow = " + mRow);
            return;
        }

        mChessBoard = new int[row * row];
        for(int i = 0; i < mChessBoard.length; i ++){
            mChessBoard[i] = (int)Math.pow(2,row) - 1; //表示每个格子可能填的数字为 1 ~ row, 例如：0000 0101 表示该格可能填1或者3.
        }
    }

    private boolean checkRow(int row){

        if(row <= 0 || row > 31){
            return false;
        }
        return true;
    }

    public int[] getChessBoard() {
        return mChessBoard;
    }


    public void fillChessBoard(int[] data){

        if(data == null || data.length <= 0){
            Log.e(TAG,"fillChessBoardRow fail, error: data is invalid.");
            return;
        }

        if(data.length != mRow * mRow){
            Log.e(TAG,"fillChessBoardRow fail, error: data length is not match mRow.");
            return;
        }

        for (int i = 0; i < data.length; i ++){
            if(data[i] <= 0){
                continue;
            }
            mChessBoard[i] = -data[i];
        }
    }

    /**
     * 填写某行的数字
     * @param row 填写某行
     * @param args 从左至右，此行的数字
     */
    public void fillChessBoardRow(int row, int... args){

        if(row > mRow || row <= 0){
            Log.e(TAG,"fillChessBoardRow fail, error: row is wrong.");
            return;
        }

        if(args == null || args.length < row){
            Log.e(TAG,"fillChessBoardRow fail, error: args is wrong.");
            return;
        }

        for (int i = 0; i < args.length; i ++){
            setChessGrid(row, i + 1, args[i]);
        }
    }

    /**
     * 填写第row行第column列数字为number
     */
    private void setChessGrid(int row, int column, int number){
        int index = getArrayIndex(row,column);
        //棋盘内部，负数代表该位置已填写，0表示该位置无解，正数按比特位表示其填写可能性
        if(number <= 0){
            return;
        }
        mChessBoard[index] = -number;
    }

    /**
     * 根据行列表示方式获取一维数组对应的索引
     */
    private int getArrayIndex(int row, int column){
        return (row - 1) * mRow + (column - 1);
    }

    private int getRow(int index){
        return index / mRow + 1;

    }

    private int getColumn(int index){
        return index % mRow + 1;
    }

    /**
     * 填写完初始数据后，需要刷新一下棋盘状态
     * @return 负数表示棋盘数据有误，正整数表示还有多少未知格，0代表成功
     */
    private int refreshChessBoard(int [] chessBoard){

        if(!checkRow(mRow)){
            Log.e(TAG,"refreshChessBoard fail, error: mRow = " + mRow);
            return -1;
        }

        int value;
        for (int i = 1; i <= mRow; i ++){
            for (int j = 1; j <= mRow; j ++){

                value = calculateChessGirdValue(chessBoard,i,j);
                Log.d(TAG,"refreshChessBoard: row = " + i + " column = " + j + " value = " + value);
                if(!fillChessGrid(chessBoard,i,j,value)){
                    return -1;
                }
            }
        }

        return getBlankCount(chessBoard);
    }

    /**
     * 填写指定格
     */
    private boolean fillChessGrid(int [] chessBoard, final int row, final int column, int value){

        Log.d(TAG,"fillChessGrid row = " + row + " column = " + column
                + " current =  " + chessBoard[getArrayIndex(row,column)] + " value = " + value);

        //已经填写
        if(chessBoard[getArrayIndex(row,column)] == value){
            return true;
        }

        chessBoard[getArrayIndex(row,column)] = value;
        printChessBoard(chessBoard);

        if(value > 0){
            return true;
        }

        if(value == 0){
            return false;
        }

        //填写一个确认数字

        //全部填写完，返回成功
        if(getBlankCount(chessBoard) == 0){
            return true;
        }

        //填写确定数字，刷新关联行其他格的值并填写
        for (int i = 1; i <= mRow; i ++){
            if (column == i){
                continue;
            }
            value = calculateValue(chessBoard,row,i,ROW);
            if(!fillChessGrid(chessBoard,row,i,value)){
                return false;
            }
        }

        if(getBlankCount(chessBoard) == 0){
            return true;
        }

        //刷新关联列
        for (int i = 1; i <= mRow; i ++){
            if (row == i){
                continue;
            }
            value = calculateValue(chessBoard,i,column,COLUMN);
            if(!fillChessGrid(chessBoard,i,column,value)){
                return false;
            }
        }

        return true;
    }

    /**
     * 计算第row行第column列格的值
     * @return 计算结果
     */
    private int calculateChessGirdValue(int[] chessBoard, int row, int column){

        int index = getArrayIndex(row,column);
        int value = chessBoard[index];
        //已确定值，或无解
        if(value <= 0){
            return value;
        }

        value = calculateValue(chessBoard,row,column,ROW);
        //说明棋盘数据已经出错
        if(0 == value){
            return 0;
        }
        //已填写好数字
        if(value < 0){
            return value;
        }

        chessBoard[index] = value;
        return calculateValue(chessBoard,row,column,COLUMN);
    }

    private int calculateValue(int[] chessBoard, int row, int column, int relation){

        int index = getArrayIndex(row,column);
        int value = chessBoard[index];

        if(value < 0){
            return value;
        }

        for(int i = 1; i <= mRow; i ++){
            //本身跳过
            if(i == (relation == ROW ? column : row)){
                continue;
            }
            index = (relation == ROW ? getArrayIndex(row,i) : getArrayIndex(i,column));
            value = calculateChessGirdValue(value,chessBoard[index]);
            //说明棋盘数据已经出错
            if(0 == value){
                return 0;
            }
//            //必须全部计算
//            if(value < 0){
//                return value;
//            }
        }
        return value;
    }


    /**
     * 根据指定格的情况更新当前格
     */
    private int calculateChessGirdValue(int mode, int reference){

        if(mode <= 0){

            if(mode == reference){
                return 0;
            }

            return mode;
        }

        //参考值也不确定
        if(reference > 0){
            return mode;
        }

        //参考值已填写，排除格子填写reference值的可能性
        if(reference < 0){
            int result = exclude(mode, reference);
            //只存在一种可能性
            if(1 == getPossibleCount(result)){
                int number = 0;
                while (result > 0){
                    result = result >> 1;
                    number ++;
                }
                //返回确定值
                return -number;
            }
            return result;
        }

        //参考值已经无解了
        return 0;
    }

    /**
     * 在模态值中移除number的可能性
     */
    private int exclude(int mode, int number){
        number = Math.abs(number);
        int valueMode = 1 << (number - 1);
        return mode & (~valueMode);
    }

    /**
     * 获取潜在数字的个数
     */
    private int getPossibleCount(int mode){
        int count = 0;
        while(mode > 0){
            mode = mode & (mode - 1);
            count ++;
        }
        return count;
    }

    /**
     * 开始计算
     * @return false表示棋盘数据有问题
     */
    public boolean calculate(){

        if(!checkRow(mRow)){
            Log.e(TAG,"calculate fail, error: mRow = " + mRow);
            return false;
        }

        int unknown = refreshChessBoard(mChessBoard);
        printChessBoard(mChessBoard);
        if(unknown < 0){
            Log.e(TAG,"calculate fail, error: the chessBoard has wrong data.");
            return false;
        }

        if(unknown == 0){
            Log.e(TAG,"calculate success, the chessBoard is already complete.");
            return true;
        }

        if(unknown > 0){
            Log.d(TAG,"calculate start, the chessBoard has " + unknown + " unknown blanks.");
        }


        if(mSudoKuOperatorStack == null){
            mSudoKuOperatorStack = new Stack<>();
        }

        return calculate(mChessBoard);
    }

    private boolean calculate(int[] chessBoard){

        int currentGridIndex = findMinPossibilityIndex(chessBoard);
        //存在无解的方格
        if(currentGridIndex < 0){
            Log.d(TAG,"already tried all possibility.");
            return false;
        }
        //记录操作之前的状态
        mSudoKuOperatorStack.push(new SudoKuRecord(chessBoard,currentGridIndex));
        //填写第一个可能的数字
        int number = getPossibilityNumber(chessBoard[currentGridIndex],1);
        Log.d(TAG,"try to fill the no." + currentGridIndex + " grid with " + number);
        //校验填写是否正确
        boolean success = fillChessGrid(chessBoard,getRow(currentGridIndex),getColumn(currentGridIndex),number);

        //填写错误，重新尝试下个可能数字
        if(!success){
            Log.d(TAG,"no." + currentGridIndex + " grid is not allowed to fill the  " + number);
            SudoKuRecord record = mSudoKuOperatorStack.pop();
            chessBoard = record.getChessBoard();
            currentGridIndex = record.getCurrentIndex();
            //去除尝试的数字可能性
            chessBoard[currentGridIndex] = exclude(chessBoard[currentGridIndex],number);
            printChessBoard(chessBoard);
            return calculate(chessBoard);
        }

        if(getBlankCount(chessBoard) == 0){
            mChessBoard = chessBoard;
            return true;
        }

        //寻找下一个可能性最小的格子填写
        return calculate(chessBoard);
    }

    private int getBlankCount(int[] chessBoard){
        int count = 0;
        for(int grid : chessBoard){
            if(grid >= 0){
                count ++;
            }
        }

        return count;
    }

    /**
     * 查找存在可能性最少的数字
     */
    private int findMinPossibilityIndex(int[] chessBoard){

        int index;
        int value;
        int possibility;
        int mMinIndex = -1;
        int minPossibility = mRow;

        for (int i = 1; i <= mRow; i ++) {
            for (int j = 1; j <= mRow; j++) {
                index = getArrayIndex(i,j);
                value = chessBoard[index];
                if(value == 0){
                    return -1;
                }
                if(value < 0){
                    continue;
                }
                possibility = getPossibleCount(value);
                if(possibility < minPossibility){
                    minPossibility = possibility;
                    mMinIndex = index;
                }
            }
        }

        return mMinIndex;
    }

    /**
     * 取出第index个可能的数字
     * @return 返回0表示已尝试完所有可能性
     */
    private int getPossibilityNumber(int number, int index){
        int count = getPossibleCount(number);
        if(index > count){
          return 0;
        }

        int result = 1;
        int times = mRow - 1;

        do {
            if(1 == (number & 1)){
                index --;
                if(0 == index){
                    return -result;
                }
            }
            number = number >> 1;
            result ++;
            times --;
        } while (times > 0);

        return 0;
    }

    public void printChessBoard(int[] chessBoard){

        if(!checkRow(mRow)){
            Log.e(TAG,"printChessBoard fail, error: mRow = " + mRow);
            return;
        }

        int row = 0;
        String message = "";
        for (int i = 0; i < chessBoard.length; i ++){

            if(row == i / mRow){
                message = message + chessBoard[i] + ",";
                continue;
            }

            Log.d(TAG,message);
            message = "";
            row ++;
            message = message + chessBoard[i] + ",";
        }
        Log.d(TAG,message);
        Log.d(TAG,"****************************************");


    }


}
