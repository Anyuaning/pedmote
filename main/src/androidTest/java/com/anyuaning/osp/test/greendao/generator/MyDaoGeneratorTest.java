package com.anyuaning.osp.test.greendao.generator;


import org.junit.Test;

import java.io.File;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.DaoUtil;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;

import static junit.framework.Assert.assertFalse;
import static junit.framework.TestCase.assertEquals;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by thom on 14-4-7.
 */
public class MyDaoGeneratorTest {

    @Test
    public void testGenerator() {
//        MyDaoGenerator dao = MyDaoGenerator.getInstance();
//        assertThat(dao.generator());
        assertThat(true);
    }

    @Test
    public void testMinimalSchema() throws Exception {
        Schema schema = new Schema(1, "de.greenrobot.testdao");
        Entity adressTable = schema.addEntity("Adresse");
        Property idProperty = adressTable.addIdProperty().getProperty();
        adressTable.addIntProperty("count").index();
        adressTable.addIntProperty("dummy").notNull();
        assertEquals(1, schema.getEntities().size());
        assertEquals(3, adressTable.getProperties().size());

        File daoFile = new File("test-out/de/greenrobot/testdao/" + adressTable.getClassName() + "Dao.java");
        daoFile.delete();
        assertFalse(daoFile.exists());

        new DaoGenerator().generateAll(schema, "test-out");

        assertEquals("PRIMARY KEY", idProperty.getConstraints());
        assertTrue(daoFile.toString(), daoFile.exists());
    }

    @Test
    public void testDbName() {
        assertEquals("NORMAL", DaoUtil.dbName("normal"));
        assertEquals("NORMAL", DaoUtil.dbName("Normal"));
        assertEquals("CAMEL_CASE", DaoUtil.dbName("CamelCase"));
        assertEquals("CAMEL_CASE_THREE", DaoUtil.dbName("CamelCaseThree"));
        assertEquals("CAMEL_CASE_XXXX", DaoUtil.dbName("CamelCaseXXXX"));
    }


}
