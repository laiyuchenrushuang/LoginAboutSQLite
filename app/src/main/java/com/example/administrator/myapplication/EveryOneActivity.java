package com.example.administrator.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Administrator on 2018/3/25.
 */

public class EveryOneActivity extends AppCompatActivity {
    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.everyone_main);
        TextView tv = (TextView) findViewById(R.id.text1);
//        UserDataManager udm = new UserDataManager(this);
//        udm.openDataBase();
//        udm.fetchAllUserDatas();
//        String username = "userName";
//        String s = udm.getStringByColumnName(username,1);
//        tv.setText(s);
        TextView tx = (TextView) findViewById(R.id.text1);
        tx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClass(getApplication(),MainActivity.class);
                startActivity(i);
            }
        });
    }
}
