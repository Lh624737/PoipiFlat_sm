package com.pospi.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.pospi.dto.Tablebeen;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "TABLEBEEN".
*/
public class TablebeenDao extends AbstractDao<Tablebeen, Long> {

    public static final String TABLENAME = "TABLEBEEN";

    /**
     * Properties of entity Tablebeen.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property TId = new Property(1, String.class, "tId", false, "T_ID");
        public final static Property Number = new Property(2, String.class, "number", false, "NUMBER");
        public final static Property Status = new Property(3, int.class, "status", false, "STATUS");
        public final static Property EatingNumber = new Property(4, String.class, "eatingNumber", false, "EATING_NUMBER");
        public final static Property PersonName = new Property(5, String.class, "personName", false, "PERSON_NAME");
    }


    public TablebeenDao(DaoConfig config) {
        super(config);
    }
    
    public TablebeenDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"TABLEBEEN\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"T_ID\" TEXT," + // 1: tId
                "\"NUMBER\" TEXT," + // 2: number
                "\"STATUS\" INTEGER NOT NULL ," + // 3: status
                "\"EATING_NUMBER\" TEXT," + // 4: eatingNumber
                "\"PERSON_NAME\" TEXT);"); // 5: personName
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"TABLEBEEN\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Tablebeen entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String tId = entity.getTId();
        if (tId != null) {
            stmt.bindString(2, tId);
        }
 
        String number = entity.getNumber();
        if (number != null) {
            stmt.bindString(3, number);
        }
        stmt.bindLong(4, entity.getStatus());
 
        String eatingNumber = entity.getEatingNumber();
        if (eatingNumber != null) {
            stmt.bindString(5, eatingNumber);
        }
 
        String personName = entity.getPersonName();
        if (personName != null) {
            stmt.bindString(6, personName);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Tablebeen entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String tId = entity.getTId();
        if (tId != null) {
            stmt.bindString(2, tId);
        }
 
        String number = entity.getNumber();
        if (number != null) {
            stmt.bindString(3, number);
        }
        stmt.bindLong(4, entity.getStatus());
 
        String eatingNumber = entity.getEatingNumber();
        if (eatingNumber != null) {
            stmt.bindString(5, eatingNumber);
        }
 
        String personName = entity.getPersonName();
        if (personName != null) {
            stmt.bindString(6, personName);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Tablebeen readEntity(Cursor cursor, int offset) {
        Tablebeen entity = new Tablebeen( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // tId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // number
            cursor.getInt(offset + 3), // status
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // eatingNumber
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5) // personName
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Tablebeen entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setTId(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setNumber(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setStatus(cursor.getInt(offset + 3));
        entity.setEatingNumber(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setPersonName(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Tablebeen entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Tablebeen entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Tablebeen entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}