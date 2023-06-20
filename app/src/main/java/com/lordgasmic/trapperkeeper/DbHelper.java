package com.lordgasmic.trapperkeeper;

import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class DbHelper extends SQLiteOpenHelper {

    private static DbHelper instance;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Comic.db";

    private final String CREATE_TEMP_COMIC;
    private final String CREATE_COMIC;
    private final List<String> DROP_ALL;

    public static synchronized void createInstance(Context context, Resources resources) {
        if (instance == null) {
            instance = new DbHelper(context, resources);
        }
    }

    public static synchronized DbHelper getInstance() {
        return instance;
    }

    private DbHelper(Context context, Resources resources) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        try {
            CREATE_TEMP_COMIC = readSql(resources.openRawResource(R.raw.create_temp_comic));
            CREATE_COMIC = readSql(resources.openRawResource(R.raw.create_comic));
            DROP_ALL = readSqlMultiLine(resources.openRawResource(R.raw.drop_all));
        } catch (IOException e) {
            throw new IllegalStateException("Unable to read sql files");
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TEMP_COMIC);
        db.execSQL(CREATE_COMIC);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        execSqlMultiLine(db, DROP_ALL);
        onCreate(db);
    }

    public void query() {

    }

    private String readSql(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();

        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();

        return sb.toString();
    }

    private List<String> readSqlMultiLine(InputStream inputStream) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            return br.lines().collect(toList());
        }
    }

    private void execSqlMultiLine(SQLiteDatabase db, List<String> commands) {
        commands.forEach(db::execSQL);
    }
}