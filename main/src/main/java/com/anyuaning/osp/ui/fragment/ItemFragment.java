package com.anyuaning.osp.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.anyuaning.osp.R;

/**
 * Created by thom on 13-10-6.
 */
public class ItemFragment extends SherlockFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle bundle) {
        View convertView = inflater.inflate(R.layout.fragment_item, container, false);
        TextView tv = (TextView) convertView.findViewById(R.id.tv_item);

        Bundle argBundle = getArguments();
        String title = argBundle.getString("arg");

        tv.setText(title);

        return convertView;
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
    }
}
