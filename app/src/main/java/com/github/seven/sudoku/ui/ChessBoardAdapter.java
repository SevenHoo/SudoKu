package com.github.seven.sudoku.ui;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.seven.sudoku.R;
import com.github.seven.sudoku.utils.ChessBoardUtils;
import com.github.seven.sudoku.utils.MyLogger;

/**
 * Description: 展示数独棋盘的RecyclerView的数据适配器 <br/>
 * Date: 2017/1/10 <br/>
 *
 * @author seven.hu@ubtrobot.com
 */

public class ChessBoardAdapter extends RecyclerView.Adapter<ChessBoardAdapter.MyViewHolder> {

    private Context mContext;
    private int[] mData;
    private int mCheckIndex;
    private int mRow;

    private AdapterListener.OnItemClickListener mOnItemClickListener;
    private AdapterListener.OnItemLongClickListener mOnItemLongClickListener;

    public ChessBoardAdapter(Context context) {
        this.mContext = context;
        this.mCheckIndex = -1;
    }

    public void setRow(int row){
        this.mRow = row;
        this.mData = new int[row * row];
        this.mCheckIndex = -1;
    }

    public void setCheckedData(int value){
        if(mCheckIndex < 0 || mCheckIndex >= mData.length){
            MyLogger.log().e("mCheckIndex is error, mCheckIndex: " + mCheckIndex);
            return;
        }
        mData[mCheckIndex] = value;
        notifyDataSetChanged();
    }

    public int getRow(){
        return mRow;
    }

    public void setData(int[] data){

        if(data.length != mRow * mRow){
            return;
        }
        mData = data;
        notifyDataSetChanged();
    }

    public int[] getData(){
        return mData;
    }

    public void check(int index){
        this.mCheckIndex = index;
        notifyDataSetChanged();
    }

    /**
     * 设置Click监听
     * @param listener 监听器
     */
    public void setOnItemClickListener(AdapterListener.OnItemClickListener listener){
        this.mOnItemClickListener = listener;
    }

    /**
     * 设置Long-Click监听
     * @param listener 监听器
     */
    public void setOnItemLongClickListener(AdapterListener.OnItemLongClickListener listener){
        this.mOnItemLongClickListener = listener;
    }

    //该方法返回是ViewHolder，当有可复用View时，就不再调用
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View item = LayoutInflater.from(mContext).inflate(R.layout.item_layout, null, false);
        return new MyViewHolder(item);
    }

    //将数据绑定到子View，会自动复用View
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.chessGridView.setImageResource(ChessBoardUtils.getResId(mData[position]));
        if(mCheckIndex == position){
            holder.chessGridView.setBackgroundColor(ContextCompat.getColor(mContext,R.color.gray));
        }else {
            holder.chessGridView.setBackgroundColor(ContextCompat.getColor(mContext,R.color.transparent));
        }

        // 如果设置了回调，则设置点击事件
        if (mOnItemClickListener != null)
        {
            holder.chessGridView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    mOnItemClickListener.onItemClick( holder.chessGridView, position,null);
                }
            });
        }

        if(mOnItemLongClickListener != null){
            holder.chessGridView.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {
                    mOnItemLongClickListener.onItemLongClick( holder.chessGridView, position,null);
                    return true;
                }
            });
        }

    }

    //RecyclerView显示数据条数
    @Override
    public int getItemCount() {
        return (mData == null ? 0 : mData.length);
    }

    //自定义的ViewHolder,减少findViewById调用次数
    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView chessGridView;
        MyViewHolder(View view) {
            super(view);
            chessGridView = (ImageView) view.findViewById(R.id.chess_grid_image_view);
        }
    }
}
