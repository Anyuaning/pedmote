package com.anyuaning.osp.ui.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.actionbarsherlock.app.SherlockFragment;
import com.anyuaning.osp.R;
import com.anyuaning.osp.ui.widget.PullToRefreshDragSortListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.mobeta.android.dslv.DragSortListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thom on 13-10-11.
 */
public class PullListFragment extends SherlockFragment implements
        PullToRefreshBase.OnRefreshListener<DragSortListView> {

    private PullToRefreshDragSortListView mPullToRefreshDragSortListView;

    private DragSortListView mListView;

//    private ListAdapter mAdapter;
    private ArrayAdapter<String> mAdapter;

    private List<String> listItem;

    private int pageIndex;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View convertView = inflater.inflate(R.layout.fragment_pull_list, container, false);

        setupData();
        setupView(convertView);

        return convertView;
    }

    private void setupData() {
        listItem = getData(0);
    }

    private List<String> getData(int pageIndex) {
        List<String> listStr = new ArrayList<String>();
        for (int i=0; i<10; i++) {
            listStr.add("string" + ((pageIndex * 10) + i));
        }
        return listStr;

    }

    private void setupView(View convertView) {
        mPullToRefreshDragSortListView = (PullToRefreshDragSortListView) convertView.findViewById(R.id.pull_refresh_list);
        mPullToRefreshDragSortListView.setOnRefreshListener(this);

        mListView = mPullToRefreshDragSortListView.getRefreshableView();
//        mAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_expandable_list_item_1,
//                android.R.id.text1, listItem);  // notifyDataSetChanged not effect
//        mAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_expandable_list_item_1, listItem);
        mAdapter = new ArrayAdapter<String>(getActivity(), R.layout.fragment_pull_list_item, R.id.text, listItem);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onRefresh(PullToRefreshBase<DragSortListView> refreshView) {
        // driectly to use onRefreshComplete, no effect

        new AsyncTask<String, Integer, String>() {

            @Override
            protected String doInBackground(String... params) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            protected void onPostExecute(String result) {
                pageIndex ++;
                listItem = getData(pageIndex);
                mAdapter.notifyDataSetChanged();
                mPullToRefreshDragSortListView.onRefreshComplete();
            }
        }.execute();
    }
}
