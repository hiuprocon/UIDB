package com.example.hiu.uidb;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

//Javaのswingで言えばJFrameのような物
public class MainActivity extends Activity
{
    ListView listView; //リストビュー
    Button addButton;  //ボタン
    ArrayList<String> nameList = new ArrayList<String>(); //名前の一覧
    ArrayAdapter<String> adapter; //名前の一覧をリストビューと関連付けるための部品

    /* Activityが始めて生成された時に呼び出される */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //画面構成をmainにする
        setContentView(R.layout.main);
        //mainの中に作った部品をプログラムで使えるよう取り出す
        listView = (ListView)findViewById(R.id.listView);
        addButton = (Button)findViewById(R.id.button);//button1はメモした値を参照

        //リストビューをクリックした時の処理を設定
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openSubActivityForUpdate(position);
            }
        });
        //リストビューを長押しした時の処理を設定
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                deleteConfirm(position);
                return true;
            }
        });
        //Add Newボタンを押した時の処理を設定
        addButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                openSubActivityForNewData();
            }
        });

        //名前の一覧(ArrayList)をリストビューと関連付ける
        adapter = new ArrayAdapter<String>(this,R.layout.list_item,nameList);
        listView.setAdapter(adapter);

        //DBの初期化
        MyDBHelper.init(this);
    }

    //このActivityが最初に表示される前と、再表示される前に実行される
    @Override
    public void onResume() {
        super.onResume();
        //DBからデータ読み出して名前リストにセット
        cleanSetUpListView();
    }

    //リストビューにDBからデータを取り出してIDと名前の一覧をセットアップする
    void cleanSetUpListView() {
        adapter.clear();
        ArrayList<String> names = MyDBHelper.listUpIdAndName();
        for (String s : names) {
            adapter.add(s);
        }
    }

    //終了処理
    @Override
    public void onDestroy() {
        super.onDestroy();
        MyDBHelper.destroy();
    }

    //新しいデータを追加するためにSubActivityを開く
    void openSubActivityForNewData() {
        Intent intent = new Intent(MainActivity.this,SubActivity.class);
        MainActivity.this.startActivity(intent);
    }
    //既存のデータの書き換えのためにSubActivityを開く
    void openSubActivityForUpdate(int position) {
        String s = nameList.get(position);
        int id = Integer.parseInt(s.substring(0,s.indexOf(":")));

        Intent intent = new Intent(MainActivity.this,SubActivity.class);
        intent.putExtra("id", id);
        MainActivity.this.startActivity(intent);
    }
    //データ削除の確認をとってOKなら削除する
    void deleteConfirm(int position) {
        AlertDialog.Builder adBuilder = new AlertDialog.Builder(this);
        adBuilder.setTitle("削除確認");
        adBuilder.setMessage("のデータを削除しますか？");
        final int p = position;
        adBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                deleteData(p);
            }
        });
        adBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ;//DO NOTHING
            }
        });
        adBuilder.create().show();

    }
    //実際にデータを削除する処理
    void deleteData(int position) {
        String s = nameList.get(position);
        Log.d("debug","deleteData(). s="+s);
        int id = Integer.parseInt(s.substring(0,s.indexOf(":")));
        Log.d("debug","deleteData(). id="+id);
        MyDBHelper.deleteData(id);
        cleanSetUpListView();
    }
}
