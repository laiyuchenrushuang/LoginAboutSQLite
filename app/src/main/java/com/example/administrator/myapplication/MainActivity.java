package com.example.administrator.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.UserDataManager;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int CORRECT_DB = 1;
    private Button mRegisterButton;//注册按钮
    private Button mLoginButton;
    private EditText mUserName;
    private EditText mPassword;
    private UserDataManager udm;
    private SharedPreferences login_sp;
    private TextView mFixPasswor;
    private CheckBox mMermoryPassword;
    private SharedPreferences.Editor editor;
    private boolean mCanRemenber = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        if (udm == null) {
            udm = new UserDataManager(this);
            udm.openDataBase();
        }

        mRegisterButton = (Button) findViewById(R.id.button);
        mLoginButton = (Button) findViewById(R.id.button3);
        mUserName = (EditText) findViewById(R.id.editText);
        mPassword = (EditText) findViewById(R.id.editText2);
        mFixPasswor = (TextView) findViewById(R.id.login_text_change_pwd);
        mMermoryPassword = (CheckBox) findViewById(R.id.Login_Remember);


        mRegisterButton.setOnClickListener(mListener);
        mLoginButton.setOnClickListener(mListener);
        mFixPasswor.setOnClickListener(mListener);
        mMermoryPassword.setOnClickListener(mListener);
        login_sp = getSharedPreferences("userInfo", 0);
        editor=login_sp.edit();
        String name=login_sp.getString("USER_NAME", "");
        String pwd =login_sp.getString("PASSWORD", "");
        boolean choseRemember =login_sp.getBoolean("mRememberCheck", false);
        boolean choseAutoLogin =login_sp.getBoolean("mAutologinCheck", false);
        //如果上次选了记住密码，那进入登录页面也自动勾选记住密码，并填上用户名和密码
        if(choseRemember){
            mUserName.setText(name);
            mPassword.setText(pwd);
            mMermoryPassword.setChecked(true);
        }
    }

    OnClickListener mListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            switch (v.getId()) {
                case R.id.button:

                    intent.setClass(MainActivity.this, RegistionActivity.class);
                    startActivity(intent);
                    finish();
                    break;

                case R.id.button3:
                    userLogin();
                    break;
                case R.id.login_text_change_pwd:
                    intent.setClass(MainActivity.this, FixPasswordActivity.class);
                    startActivity(intent);
                    finish();
                    break;
            }
        }

        private void userLogin() {
            String userName = mUserName.getText().toString().trim();
            String password = mPassword.getText().toString().trim();
            int result = udm.findUserByNameAndPwd(userName, password);
            if (isUserNameAndPwdValid()) {
                if (result == CORRECT_DB) {
                    mCanRemenber = true;
                    editor.putString("USER_NAME", userName);
                    editor.putString("PASSWORD", password);
                    if(mMermoryPassword.isChecked()){
                        editor.putBoolean("mRememberCheck", true);
                    }else{
                        editor.putBoolean("mRememberCheck", false);
                    }
                    editor.commit();
                    Intent intent = new Intent(MainActivity.this, EveryOneActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }


        public boolean isUserNameAndPwdValid() {
            if (mUserName.getText().toString().trim().equals("")) {
                Toast.makeText(MainActivity.this, getString(R.string.account_empty),
                        Toast.LENGTH_SHORT).show();
                return false;
            } else if (mPassword.getText().toString().trim().equals("")) {
                Toast.makeText(MainActivity.this, getString(R.string.pwd_empty),
                        Toast.LENGTH_SHORT).show();
                return false;
            }
            return true;
        }
    };

    @Override
    protected void onResume() {
        if (udm == null) {
            udm = new UserDataManager(this);
            udm.openDataBase();
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (udm != null) {
            udm.closeDataBase();
            udm = null;
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        if (udm != null) {
            udm.closeDataBase();
            udm = null;
        }
        super.onPause();
    }
}
