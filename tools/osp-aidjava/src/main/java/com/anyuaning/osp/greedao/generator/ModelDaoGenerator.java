package com.anyuaning.osp.greedao.generator;

import java.io.IOException;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class ModelDaoGenerator {

    private static final String sfRootPackageName = "com.anyuaning.osp";

    private static final int SCHEMA_VERSION = 3;

    public void generator() {
        Schema schema = new Schema(SCHEMA_VERSION, sfRootPackageName + ".model");
        schema.setDefaultJavaPackageDao(sfRootPackageName + ".dao");

        addTiming(schema);
        addJogging(schema);

        try {
            DaoGenerator daoGenerator = new DaoGenerator();
            daoGenerator.generateAll(schema, "./main/src/main/java");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * timing data
     * @param schema
     */
    private void addTiming(Schema schema) {
        Entity timing = schema.addEntity("Timing");
        timing.setJavaPackage(sfRootPackageName + ".model.sport");
        timing.setJavaPackageDao(sfRootPackageName + ".dao.sport");
        timing.addIdProperty();
        timing.addDateProperty("startTime");
        timing.addDateProperty("endTime");
        timing.addStringProperty("startFormatterTime");
        timing.addStringProperty("endFormatterTime");
        timing.addLongProperty("length");
    }

    /**
     * step data
     * @param schema
     */
    private void addJogging(Schema schema) {
        Entity jogging = schema.addEntity("Jogging");
        jogging.setJavaPackage(sfRootPackageName + ".model.sport");
        jogging.setJavaPackageDao(sfRootPackageName + ".dao.sport");
        jogging.addIdProperty();
        jogging.addLongProperty("steps");
        jogging.addFloatProperty("distance"); // miles
        jogging.addLongProperty("pace"); // stepsPerMinute
        jogging.addFloatProperty("speed"); // milesPerHour
        jogging.addFloatProperty("calories"); // caloriesBurned
    }

}
