package com.anyuaning.osp.greedao.generator;

import java.io.IOException;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class ModelDaoGenerator {

    private static final String sfRootPackageName = "com.anyuaning.osp";

    public void generator() {
        Schema schema = new Schema(1, sfRootPackageName + ".model");
        schema.setDefaultJavaPackageDao(sfRootPackageName + ".dao");

        addTiming(schema);

        try {
            DaoGenerator daoGenerator = new DaoGenerator();
            daoGenerator.generateAll(schema, "./main/src/main/java");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void addTiming(Schema schema) {
        Entity timing = schema.addEntity("Timing");
        timing.setJavaPackage(sfRootPackageName + ".model.sport");
        timing.setJavaPackageDao(sfRootPackageName + ".dao.sport");
        timing.addIdProperty();
        timing.addDateProperty("startTime");
        timing.addDateProperty("endTime");
        timing.addLongProperty("length");
    }

}
