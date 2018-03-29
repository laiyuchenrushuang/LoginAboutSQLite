package com.example.administrator.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.user.UserData;
import com.example.user.UserDataManager;
import com.example.user.VerificationCodeUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.administrator.myapplication.R.id.surePassword;
import static com.example.administrator.myapplication.R.id.userName;

/**
 * 注册界面
 * Created by Administrator on 2018/3/22.
 */

public class RegistionActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "RegistionActivity";
    private static final int USERNAME_ONLINE = 0;
    private ImageView mVerificationCodeImageView;
    private Button mSureRegistion;
    private Button mCancelRegister;
    private VerificationCodeUtil mVerificationCodeUtil;
    private EditText mUserName;
    private EditText mPassword;
    private EditText mSurePassword;
    private EditText mVerificationCodeText;
    private static final int USERNAME_LENTH = 8;
    private static final int PASSWORD_LENTH = 6;
    private UserData mUserData;
    private UserDataManager udm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registion);
        init();

    }

    //初始化的参数
    private void init() {
        mVerificationCodeUtil = VerificationCodeUtil.getInstance();
        //账户密码信息
        mUserName = (EditText) findViewById(userName);//账号
        mPassword = (EditText) findViewById(R.id.passWord);//密码
        mSurePassword = (EditText) findViewById(surePassword);//确认密码
        mVerificationCodeText = (EditText) findViewById(R.id.verificationcodeText);//验证码


        mVerificationCodeImageView = (ImageView) findViewById(R.id.verificationcodeIv);
        mSureRegistion = (Button) findViewById(R.id.sureRegistion);
        mCancelRegister = (Button) findViewById(R.id.cancelRegistion);


        mVerificationCodeImageView.setImageBitmap(mVerificationCodeUtil.createVerificationCodeBitmap());
        //注册的操作
        mSureRegistion.setOnClickListener(this);
        //取消的操作
        mCancelRegister.setOnClickListener(this);
        //验证码刷新操作
        mVerificationCodeImageView.setOnClickListener(this);
        
        //数据库的初始化
        if(udm == null) {
            udm = new UserDataManager(this);
            udm.openDataBase();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sureRegistion:
                String userName = mUserName.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String surePassword = mSurePassword.getText().toString();

                checkPermission(userName,password,surePassword);
                break;
            case R.id.cancelRegistion:
                startActivity(new Intent().setClass(RegistionActivity.this, MainActivity.class));
                finish();
                break;
            case R.id.verificationcodeIv:
                mVerificationCodeImageView.setImageBitmap(mVerificationCodeUtil.createVerificationCodeBitmap());
                break;
            default:
                Log.e(TAG, "onClick many actions" );
        }
    }

    //检查登录权限
    private void checkPermission(String userName, String password, String surePassword) {
        Pattern p = Pattern.compile("[a-zA-Z][0-9]");
        Matcher m = p.matcher(userName);

        if (userName.equals("") || userName.isEmpty()
                || userName.length() < USERNAME_LENTH || !m.find()) {
            Toast.makeText(this, "请输入正确账户信息，长度应该大于8位，且包含字母数字", Toast.LENGTH_LONG).show();
            return;
        }

        if (password.length() < PASSWORD_LENTH || surePassword.length() < PASSWORD_LENTH) {
            Toast.makeText(this, "密码输入长度小于6位", Toast.LENGTH_LONG).show();
            return;
        }

        if (!(password.equals(surePassword))) {
            Toast.makeText(this, "您的两次密码输入不一样", Toast.LENGTH_LONG).show();
            return;
        }

        if (mVerificationCodeText.getText().toString().toLowerCase().equals(mVerificationCodeUtil.getVerificationCode().toLowerCase())) {
            boolean result = udm.findUserByName(userName);
            if (result) {
                Toast.makeText(this, "用户名字已经存在，请重新设置", Toast.LENGTH_LONG).show();
                return;
            }else {
                mUserData = new UserData(userName,password);
                udm.insertUserData(mUserData);
                startActivity(new Intent().setClass(RegistionActivity.this, MainActivity.class));
                finish();
            }
        } else {
            Toast.makeText(this, "验证码输入错误", Toast.LENGTH_LONG).show();
        }
    }
}
