package com.ldp.reader.model.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.ldp.reader.model.gen.DaoMaster;
import com.ldp.reader.model.local.update.Update2Helper;

import org.greenrobot.greendao.database.Database;

/**
 * Created by ldp on 2017/10/9.
 */

public class MyOpenHelper extends DaoMaster.DevOpenHelper{
    public MyOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        // 跨版本更新策略
        switch (oldVersion){
            case 1:
                // 暂无 1.0
            case 2:
                // 更新数据到 3.0
                Update2Helper.getInstance().update(db);
            default:
                break;
        }
    }
}
