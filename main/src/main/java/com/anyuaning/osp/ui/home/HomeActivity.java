package com.anyuaning.osp.ui.home;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.anyuaning.osp.R;

/**
 * Created by thom on 14-1-5.
 */
public class HomeActivity extends Activity {

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.activity_home);

        Toast.makeText(this, "jenkins gerrit build", Toast.LENGTH_SHORT).show();
    }
}
