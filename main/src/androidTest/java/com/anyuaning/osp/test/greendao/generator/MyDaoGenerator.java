package com.anyuaning.osp.test.greendao.generator;

import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

/**
 * Created by thom on 14-4-7.
 */
public class MyDaoGenerator {
    private static MyDaoGenerator ourInstance = new MyDaoGenerator();

    public static MyDaoGenerator getInstance() {
        return ourInstance;
    }

    private Schema schema;

    private MyDaoGenerator() {
        schema = new Schema(1, "com.anyuaning.osp.daotest");
        schema.setDefaultJavaPackageDao("com.anyuaning.osp.entity");

        addNote(schema);
    }

    private void addNote(Schema schema) {
        Entity note = schema.addEntity("Note");
        note.addIdProperty();
        note.addStringProperty("text").notNull();
        note.addDateProperty("date");

    }

    public boolean generator() throws Exception {
        de.greenrobot.daogenerator.DaoGenerator daoGenerator = new de.greenrobot.daogenerator.DaoGenerator();
        daoGenerator.generateAll(schema, "../gen", "../");
        return true;
    }
}
