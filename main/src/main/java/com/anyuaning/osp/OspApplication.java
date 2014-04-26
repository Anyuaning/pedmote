package com.anyuaning.osp;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.anyuaning.osp.dao.DaoMaster;
import com.anyuaning.osp.dao.DaoSession;

import static com.anyuaning.osp.dao.DaoMaster.*;

/**
 * Created by thom on 14-4-13.
 */
public class OspApplication extends Application {

    private SQLiteDatabase database;

    private DaoMaster daoMaster;

    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();


        DevOpenHelper dbHelper = new DevOpenHelper(this, "sport", null);
        database = dbHelper.getWritableDatabase();
        daoMaster = new DaoMaster(database);
        daoSession = daoMaster.newSession();
    }


    public DaoSession getDaoSession() {
        return daoSession;
    }


}
