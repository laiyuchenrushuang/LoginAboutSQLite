package com.example.administrator.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.UserData;
import com.example.user.UserDataManager;

public class FixPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mFixCancelButton;
    private EditText mFixUserName;
    private EditText mOriginalPassword;
    private EditText mFixPasswordOnce;
    private EditText mFixPasswordTwice;
    private Button mConfirmSummit;
    private UserDataManager mUserDataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_fix_password);
        //初始化
        mFixCancelButton = (TextView) findViewById(R.id.resetpwd_btn_cancel);
        mFixUserName = (EditText) findViewById(R.id.resetpwd_edit_name);
        mOriginalPassword = (EditText) findViewById(R.id.resetpwd_edit_pwd_old);
        mFixPasswordOnce = (EditText) findViewById(R.id.resetpwd_edit_pwd_new);
        mFixPasswordTwice = (EditText) findViewById(R.id.resetpwd_edit_pwd_check);
        mConfirmSummit = (Button) findViewById(R.id.resetpwd_btn_sure);

        if (mUserDataManager == null) {
            mUserDataManager = new UserDataManager(this);
            mUserDataManager.openDataBase();
        }

        //监听按钮
        mFixCancelButton.setOnClickListener(this);
        mConfirmSummit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.resetpwd_btn_cancel:
                startActivity(intent.setClass(FixPasswordActivity.this, MainActivity.class));
                finish();
                break;
            case R.id.resetpwd_btn_sure:
                String userName = mFixUserName.getText().toString();
                String oldPassword = mOriginalPassword.getText().toString();
                String newPassword = mFixPasswordOnce.getText().toString();
                String checkNewPassword = mFixPasswordTwice.getText().toString();

                if (checkUserInfoIsCorrect(userName, oldPassword, newPassword, checkNewPassword)) {
                    fixDataAndSaveData(userName, newPassword);
                    startActivity(intent.setClass(FixPasswordActivity.this, MainActivity.class));
                    finish();
                }
                break;
        }
    }

    //修改密码保存新密码
    private void fixDataAndSaveData(String userName, String newPassword) {
        mUserDataManager.updateUserData(userName, newPassword);
    }

    //确保数据库能够找到，且密码规范>6
    private boolean checkUserInfoIsCorrect(String userName, String oldPassword, String newPassword, String checkNewPassword) {
        if (mUserDataManager.findUserByNameAndPwd(userName, oldPassword) == 1) {
            if (!newPassword.equals(null) && !checkNewPassword.equals(null) && newPassword.equals(checkNewPassword)) {
                if (newPassword.length() > 5 && checkNewPassword.length() > 5) {
                    return true;
                } else {
                    Toast.makeText(this, "新密码长度应该大于6，请重新输入", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "新密码为空或者不相等，请重新输入", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "原密码输入不正确，请重新输入", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    protected void onResume() {
        if (mUserDataManager == null) {
            mUserDataManager = new UserDataManager(this);
            mUserDataManager.openDataBase();
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        if (mUserDataManager != null) {
            mUserDataManager.closeDataBase();
            mUserDataManager = null;
        }
        super.onPause();
    }
}
