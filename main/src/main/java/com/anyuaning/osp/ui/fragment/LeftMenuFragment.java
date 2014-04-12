package com.anyuaning.osp.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.anyuaning.osp.R;
import com.anyuaning.osp.ui.fragment.music.MusicBrowserFragment;
import com.anyuaning.osp.ui.fragment.stopwatch.CountDownFragment;
import com.anyuaning.osp.ui.fragment.stopwatch.StopWatchFragment;
import com.anyuaning.osp.ui.home.OpenMainActivity;

/**
 * Created by thom on 14-4-4.
 */
public class LeftMenuFragment extends Fragment {

    private Context mContext;

    private ListView mListViewMenu;

    public LeftMenuFragment() {
        //
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View menuView = inflater.inflate(R.layout.left_menu_content, container, false);

        mListViewMenu = (ListView) menuView.findViewById(R.id.menu_list);

        String[] listItem  = new String[] {
            getString(R.id.label_menu_sports),
            getString(R.id.label_menu_map),
            getString(R.id.label_menu_music),
            getString(R.id.label_menu_statistics),
            getString(R.id.label_menu_settings)
        };
        final Fragment[] listFragment = new Fragment[] {
                new StopWatchFragment(),
                new ItemFragment(),
                new MusicBrowserFragment(),
                new ItemFragment(),
                new CountDownFragment()
        };
        ListAdapter adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1,
                android.R.id.text1, listItem);
        mListViewMenu.setAdapter(adapter);

        mListViewMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OpenMainActivity mainActivity = (OpenMainActivity) mContext;
                mainActivity.switchContent(listFragment[position]);

            }
        });


        return menuView;
    }
}
