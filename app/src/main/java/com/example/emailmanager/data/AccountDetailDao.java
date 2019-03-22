package com.example.emailmanager.data;

import java.util.List;
import java.util.ArrayList;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.SqlUtils;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "ACCOUNT_DETAIL".
*/
public class AccountDetailDao extends AbstractDao<AccountDetail, Long> {

    public static final String TABLENAME = "ACCOUNT_DETAIL";

    /**
     * Properties of entity AccountDetail.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, long.class, "id", true, "_id");
        public final static Property Account = new Property(1, String.class, "account", false, "ACCOUNT");
        public final static Property Pwd = new Property(2, String.class, "pwd", false, "PWD");
        public final static Property EmailCategoryId = new Property(3, int.class, "emailCategoryId", false, "EMAIL_CATEGORY_ID");
        public final static Property EmailCategory = new Property(4, String.class, "emailCategory", false, "EMAIL_CATEGORY");
        public final static Property Enable = new Property(5, boolean.class, "enable", false, "ENABLE");
        public final static Property CustomId = new Property(6, long.class, "customId", false, "CUSTOM_ID");
    }

    private DaoSession daoSession;


    public AccountDetailDao(DaoConfig config) {
        super(config);
    }
    
    public AccountDetailDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"ACCOUNT_DETAIL\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ," + // 0: id
                "\"ACCOUNT\" TEXT," + // 1: account
                "\"PWD\" TEXT," + // 2: pwd
                "\"EMAIL_CATEGORY_ID\" INTEGER NOT NULL ," + // 3: emailCategoryId
                "\"EMAIL_CATEGORY\" TEXT," + // 4: emailCategory
                "\"ENABLE\" INTEGER NOT NULL ," + // 5: enable
                "\"CUSTOM_ID\" INTEGER NOT NULL );"); // 6: customId
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"ACCOUNT_DETAIL\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, AccountDetail entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
 
        String account = entity.getAccount();
        if (account != null) {
            stmt.bindString(2, account);
        }
 
        String pwd = entity.getPwd();
        if (pwd != null) {
            stmt.bindString(3, pwd);
        }
        stmt.bindLong(4, entity.getEmailCategoryId());
 
        String emailCategory = entity.getEmailCategory();
        if (emailCategory != null) {
            stmt.bindString(5, emailCategory);
        }
        stmt.bindLong(6, entity.getEnable() ? 1L: 0L);
        stmt.bindLong(7, entity.getCustomId());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, AccountDetail entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
 
        String account = entity.getAccount();
        if (account != null) {
            stmt.bindString(2, account);
        }
 
        String pwd = entity.getPwd();
        if (pwd != null) {
            stmt.bindString(3, pwd);
        }
        stmt.bindLong(4, entity.getEmailCategoryId());
 
        String emailCategory = entity.getEmailCategory();
        if (emailCategory != null) {
            stmt.bindString(5, emailCategory);
        }
        stmt.bindLong(6, entity.getEnable() ? 1L: 0L);
        stmt.bindLong(7, entity.getCustomId());
    }

    @Override
    protected final void attachEntity(AccountDetail entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.getLong(offset + 0);
    }    

    @Override
    public AccountDetail readEntity(Cursor cursor, int offset) {
        AccountDetail entity = new AccountDetail( //
            cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // account
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // pwd
            cursor.getInt(offset + 3), // emailCategoryId
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // emailCategory
            cursor.getShort(offset + 5) != 0, // enable
            cursor.getLong(offset + 6) // customId
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, AccountDetail entity, int offset) {
        entity.setId(cursor.getLong(offset + 0));
        entity.setAccount(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setPwd(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setEmailCategoryId(cursor.getInt(offset + 3));
        entity.setEmailCategory(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setEnable(cursor.getShort(offset + 5) != 0);
        entity.setCustomId(cursor.getLong(offset + 6));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(AccountDetail entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(AccountDetail entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(AccountDetail entity) {
        throw new UnsupportedOperationException("Unsupported for entities with a non-null key");
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getReceiverDao().getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T1", daoSession.getSendDao().getAllColumns());
            builder.append(" FROM ACCOUNT_DETAIL T");
            builder.append(" LEFT JOIN RECEIVER T0 ON T.\"CUSTOM_ID\"=T0.\"_id\"");
            builder.append(" LEFT JOIN SEND T1 ON T.\"CUSTOM_ID\"=T1.\"_id\"");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected AccountDetail loadCurrentDeep(Cursor cursor, boolean lock) {
        AccountDetail entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        Receiver receiver = loadCurrentOther(daoSession.getReceiverDao(), cursor, offset);
         if(receiver != null) {
            entity.setReceiver(receiver);
        }
        offset += daoSession.getReceiverDao().getAllColumns().length;

        Send send = loadCurrentOther(daoSession.getSendDao(), cursor, offset);
         if(send != null) {
            entity.setSend(send);
        }

        return entity;    
    }

    public AccountDetail loadDeep(Long key) {
        assertSinglePk();
        if (key == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(getSelectDeep());
        builder.append("WHERE ");
        SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
        String sql = builder.toString();
        
        String[] keyArray = new String[] { key.toString() };
        Cursor cursor = db.rawQuery(sql, keyArray);
        
        try {
            boolean available = cursor.moveToFirst();
            if (!available) {
                return null;
            } else if (!cursor.isLast()) {
                throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount());
            }
            return loadCurrentDeep(cursor, true);
        } finally {
            cursor.close();
        }
    }
    
    /** Reads all available rows from the given cursor and returns a list of new ImageTO objects. */
    public List<AccountDetail> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<AccountDetail> list = new ArrayList<AccountDetail>(count);
        
        if (cursor.moveToFirst()) {
            if (identityScope != null) {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try {
                do {
                    list.add(loadCurrentDeep(cursor, false));
                } while (cursor.moveToNext());
            } finally {
                if (identityScope != null) {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }
    
    protected List<AccountDetail> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<AccountDetail> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
