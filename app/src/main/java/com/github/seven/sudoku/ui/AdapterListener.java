package com.github.seven.sudoku.ui;

import android.view.View;

/**
 * Description: 定义Adapter接口方法 <br/>
 * Date: 2017/2/10 <br/>
 *
 * @author seven.hu@ubtrobot.com
 */

public interface AdapterListener {

     interface OnItemClickListener {
        void onItemClick(View view, int position, Object extra);
    }

     interface OnItemLongClickListener {
        void onItemLongClick(View view, int position, Object extra);
    }
}
