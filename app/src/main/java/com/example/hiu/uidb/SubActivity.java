package com.example.hiu.uidb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

//Javaのswingで言えばJFrameのような物
public class SubActivity extends Activity
{
    TextView nameTV;
    TextView phoneTV;
    Button okButton;
    Button cancelButton;
    boolean forUpdate;
    int id = -1;

    /* Activityが始めて生成された時に呼び出される */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub);
        nameTV = (TextView)findViewById(R.id.editText);
        phoneTV = (TextView)findViewById(R.id.editText2);
        okButton = (Button)findViewById(R.id.button2);
        cancelButton = (Button)findViewById(R.id.button3);

        //OKボタンを押した時の処理
        okButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                processOkRequest();
            }
        });
        //OKボタンを押した時の処理
        cancelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                processCancelRequest();
            }
        });

        Intent intent = getIntent();
        if (intent!=null) {
            int id = intent.getIntExtra("id", -1);
            if (id!=-1) {
                this.id = id;
                forUpdate = true;
                String[] nameAndPhone = new String[2];
                MyDBHelper.getData(id, nameAndPhone);
                nameTV.setText(nameAndPhone[0]);
                phoneTV.setText(nameAndPhone[1]);
            } else {
                this.id = -1;
                forUpdate = false;
            }
        }
    }

    //
    void processOkRequest() {
        if (forUpdate==true) {
            String name = nameTV.getText().toString();
            String phone = phoneTV.getText().toString();
            MyDBHelper.updateData(id, name, phone);
        } else {
            String name = nameTV.getText().toString();
            String phone = phoneTV.getText().toString();
            MyDBHelper.insertData(name, phone);
        }
        finish();
    }
    void processCancelRequest() {
        finish();
    }
}
