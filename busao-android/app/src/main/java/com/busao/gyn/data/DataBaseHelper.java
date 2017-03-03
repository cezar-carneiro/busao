package com.busao.gyn.data;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by cezar on 18/01/17.
 */

public class DataBaseHelper extends SQLiteOpenHelper {

    private static String TAG = "DataBaseHelper"; // Tag just for the LogCat window

    private static String DB_NAME ="database.db";// Database name
    private static Integer VERSION = 1;

    private String mDbPath;
    private SQLiteDatabase mDataBase;
    private Context mContext;

    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, VERSION);// 1? Its mDatabase Version
        this.mContext = context;
        if (android.os.Build.VERSION.SDK_INT >= 17) {
            this.mDbPath = context.getApplicationInfo().dataDir + "/databases/";
        } else {
            this.mDbPath = "/data/data/" + context.getPackageName() + "/databases/";
        }
    }

    public void createIfNotExists() throws IOException {
        boolean mDataBaseExist = fileExists();
        if (!mDataBaseExist) {
            this.getReadableDatabase();
            this.close();
            copyDataBase();
            Log.e(TAG, "createDatabase mDatabase created");

        }
    }

    private boolean fileExists() {
        File dbFile = new File(mDbPath + DB_NAME);
        return dbFile.exists();
    }

    private void copyDataBase() throws IOException {
        InputStream mInput = mContext.getAssets().open(DB_NAME);
        String outFileName = mDbPath + DB_NAME;
        OutputStream mOutput = new FileOutputStream(outFileName);
        byte[] mBuffer = new byte[4096];
        int mLength;
        while ((mLength = mInput.read(mBuffer)) > 0) {
            mOutput.write(mBuffer, 0, mLength);
        }
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }

    public boolean openDataBase() throws SQLException {
        String mPath = mDbPath + DB_NAME;
        mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        return mDataBase != null;
    }

    @Override
    public synchronized void close() {
        if(mDataBase != null) {
            mDataBase.close();
        }
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //TODO auto-generated method stub
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO auto-generated method stub
    }

}
