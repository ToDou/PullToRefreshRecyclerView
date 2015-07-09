package com.todou.pulltorefreshrecyclerview.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.todou.pulltorefreshrecyclerview.R;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by loopeer on 2015/7/9.
 */
public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final Context mContext;
    private final LayoutInflater mLayoutInflater;
    private ArrayList<String> mData;

    public MainAdapter(Context context) {
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(mContext);
        this.mData = new ArrayList<>();
    }

    public void updateData(ArrayList<String> data) {
        setData(data);
        notifyDataSetChanged();
    }

    private void setData(ArrayList<String> data) {
        this.mData.clear();
        this.mData.addAll(data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = mLayoutInflater.inflate(R.layout.list_item_main, parent, false);
        return new MainItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((MainItemViewHolder) holder).bind(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public static class MainItemViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.text)
        TextView mText;

        public MainItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }

        public void bind(String string) {
            mText.setText(string);
        }
    }

}
