package com.example.hiu.uidb;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;

//DBを管理するためのクラス
public class MyDBHelper extends SQLiteOpenHelper {
    static final String DB = "phone_db.db";
    static final int DB_VERSION = 1;

    static MyDBHelper helper;
    static SQLiteDatabase db;

    //DBを初期化する。1つ作って共有するようにする。
    public static void init(Context c) {
        if (helper==null) {
            helper = new MyDBHelper(c);
            db = helper.getWritableDatabase();
        }
    }

    //コンストラクタはprivateにしておく
    private MyDBHelper(Context c) {
        super(c,DB,null,DB_VERSION);
    }

    //DBの作成
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "create table phone ( id integer primary key autoincrement, name text not null, phone test not null );";
        db.execSQL(CREATE_TABLE);
    }

    //DBのアップグレード。まじめに書いてない。
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_TABLE = "drop table phone;";
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }

    //DBを閉じる処理
    public static void destroy() {
        //TODO
    }

    //DBからid:nameの一覧をリストアップする
    public static ArrayList<String> listUpIdAndName() {
        ArrayList<String> results = new ArrayList<String>();
        Cursor cursor = db.query("phone", new String[] {"id","name"}, null,null,null,null,"id ASC");
        cursor.moveToFirst();
        for (int i=0;i<cursor.getCount();i++) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            results.add(id+":"+name);
            cursor.moveToNext();
        }
        return results;
    }

    //DBに新しいデータを入れる。
    public static void insertData(String name, String phone) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("phone", phone);
        db.insert("phone", null, values);
    }

    //指定したidのレコードのデータをDBから取り出す
    public static void getData(int id, String[] out) {
        Cursor cursor = db.query("phone", new String[] {"name","phone"}, "id = "+id, null, null, null, null);
        cursor.moveToFirst();
        String name = cursor.getString(cursor.getColumnIndex("name"));
        String phone = cursor.getString(cursor.getColumnIndex("phone"));
        out[0] = name;
        out[1] = phone;
    }

    //DBのデータを更新
    public static void updateData(int id,String name,String phone) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("phone", phone);
        db.update("phone", values, "id = "+id, null);
    }

    //DBからデータを消す
    public static void deleteData(int id) {
        db.delete("phone", "id = "+id, null);
    }
}
