package com.github.seven.sudoku.utils;

import com.github.seven.sudoku.R;

/**
 * Created by Seven on 2017/10/29.
 */

public class ChessBoardUtils {

    private static final int[] RES_ID = {R.mipmap.ic_question_mark, R.mipmap.ic_angle,
            R.mipmap.ic_rect, R.mipmap.ic_circle, R.mipmap.ic_x, R.mipmap.ic_star};

    public static final int RES_INDEX_QUESTION = 0;
    public static final int RES_INDEX_ANGLE = 1;
    public static final int RES_INDEX_RECT = 2;
    public static final int RES_INDEX_CIRCLE = 3;
    public static final int RES_INDEX_CROSS = 4;
    public static final int RES_INDEX_STAR = 5;


    public static int getResId(int index){

        if(index < RES_INDEX_QUESTION || index > RES_INDEX_STAR){
            return RES_ID[RES_INDEX_QUESTION];
        }

        return RES_ID[index];
    }
}
