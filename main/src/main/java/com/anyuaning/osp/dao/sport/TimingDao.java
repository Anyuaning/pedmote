package com.anyuaning.osp.dao.sport;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.anyuaning.osp.dao.DaoSession;

import com.anyuaning.osp.model.sport.Timing;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table TIMING.
*/
public class TimingDao extends AbstractDao<Timing, Long> {

    public static final String TABLENAME = "TIMING";

    /**
     * Properties of entity Timing.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property StartTime = new Property(1, java.util.Date.class, "startTime", false, "START_TIME");
        public final static Property EndTime = new Property(2, java.util.Date.class, "endTime", false, "END_TIME");
        public final static Property StartFormatterTime = new Property(3, String.class, "startFormatterTime", false, "START_FORMATTER_TIME");
        public final static Property EndFormatterTime = new Property(4, String.class, "endFormatterTime", false, "END_FORMATTER_TIME");
        public final static Property Length = new Property(5, Long.class, "length", false, "LENGTH");
    };


    public TimingDao(DaoConfig config) {
        super(config);
    }
    
    public TimingDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'TIMING' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'START_TIME' INTEGER," + // 1: startTime
                "'END_TIME' INTEGER," + // 2: endTime
                "'START_FORMATTER_TIME' TEXT," + // 3: startFormatterTime
                "'END_FORMATTER_TIME' TEXT," + // 4: endFormatterTime
                "'LENGTH' INTEGER);"); // 5: length
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'TIMING'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Timing entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        java.util.Date startTime = entity.getStartTime();
        if (startTime != null) {
            stmt.bindLong(2, startTime.getTime());
        }
 
        java.util.Date endTime = entity.getEndTime();
        if (endTime != null) {
            stmt.bindLong(3, endTime.getTime());
        }
 
        String startFormatterTime = entity.getStartFormatterTime();
        if (startFormatterTime != null) {
            stmt.bindString(4, startFormatterTime);
        }
 
        String endFormatterTime = entity.getEndFormatterTime();
        if (endFormatterTime != null) {
            stmt.bindString(5, endFormatterTime);
        }
 
        Long length = entity.getLength();
        if (length != null) {
            stmt.bindLong(6, length);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Timing readEntity(Cursor cursor, int offset) {
        Timing entity = new Timing( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : new java.util.Date(cursor.getLong(offset + 1)), // startTime
            cursor.isNull(offset + 2) ? null : new java.util.Date(cursor.getLong(offset + 2)), // endTime
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // startFormatterTime
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // endFormatterTime
            cursor.isNull(offset + 5) ? null : cursor.getLong(offset + 5) // length
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Timing entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setStartTime(cursor.isNull(offset + 1) ? null : new java.util.Date(cursor.getLong(offset + 1)));
        entity.setEndTime(cursor.isNull(offset + 2) ? null : new java.util.Date(cursor.getLong(offset + 2)));
        entity.setStartFormatterTime(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setEndFormatterTime(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setLength(cursor.isNull(offset + 5) ? null : cursor.getLong(offset + 5));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Timing entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Timing entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}