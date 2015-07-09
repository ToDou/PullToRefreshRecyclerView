package com.todou.pulltorefreshrecyclerview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.loopeer.pulltorefreshrecyclerview.view.PullToRefreshRecyclerView;
import com.todou.pulltorefreshrecyclerview.adapter.MainAdapter;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends AppCompatActivity {

    @InjectView(R.id.recycler_view)
    PullToRefreshRecyclerView mRecyclerView;

    @InjectView(R.id.text_refresh)
    TextView mText;

    private MainAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        setUpRecyclerView();

    }

    private void setUpRecyclerView() {
        mAdapter = new MainAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setScrollType(PullToRefreshRecyclerView.OverScrollType.TOP);
        mRecyclerView.setTextView(mText);
        ArrayList<String> datas = createTestDatas();
        mAdapter.updateData(datas);
    }

    private ArrayList<String> createTestDatas() {
        ArrayList<String> datas = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            datas.add("...data: " + i);
        }
        return datas;
    }


}
