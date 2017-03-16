package com.example.administrator.mapclient.activity.loginorregister;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.administrator.mapclient.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * 忘记密码 点击重置密码
 */

public class ChangPaswActivity extends AppCompatActivity implements View.OnClickListener,TextWatcher{
    private EditText phonick;
    private EditText auth_code;
    private EditText register_pasw;
    private Button butt_register;
    private ImageView returlogin;
    private int i = 30; // 设置短信验证提示时间为30s
    private Button butt_code;
    private EditText thou_pasw; //再次输入密码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pasw);
        initSDK();
        initdate();
    }
    //初始化SMSSDK
    private void initSDK() {
        SMSSDK.initSDK(this, "1b58ec643cd56","854118d03f46876931fe059778c712ac");
        EventHandler eventHandler = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler.sendMessage(msg);
            }
        };
        SMSSDK.registerEventHandler(eventHandler); // 注册回调监听接口  
    }
    private void initdate() {
        findview();
        butt_code.setOnClickListener(this);
        butt_register.setOnClickListener(this);
        returlogin.setOnClickListener(this);
        phonick.addTextChangedListener(this);
        auth_code.addTextChangedListener(this);
        register_pasw.addTextChangedListener(this);
    }
    private void findview() {
        phonick = (EditText) findViewById(R.id.phonick);
        auth_code = (EditText) findViewById(R.id.auth_code);
        register_pasw = (EditText) findViewById(R.id.register_pasw);
        butt_register = (Button) findViewById(R.id.butt_register);
        returlogin = (ImageView) findViewById(R.id.returlogin);
        butt_code = (Button) findViewById(R.id.butt_code);
        thou_pasw = (EditText) findViewById(R.id.thou_pasw);
    }
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == -9) {
                butt_code.setText("重新发送(" + i + ")");
            } else if (msg.what == -8) {
                butt_code.setText("获取验证码");
                butt_code.setClickable(true); // 设置可点击  
                i = 30;
            } else {
                int event = msg.arg1;
               
                int result = msg.arg2;
                Object data = msg.obj;
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // 短信注册成功后，返回MainActivity,然后提示  
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {// 提交验证码成功
                        Log.e("=======","-----"+SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE+"------");
                        Toast.makeText(ChangPaswActivity.this, "提交验证码成功",
                                Toast.LENGTH_SHORT).show();
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        Toast.makeText(ChangPaswActivity.this, "验证码已经发送",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        ((Throwable) data).printStackTrace();
                    }
                }
            }
        }
    };
        @Override
        public void onClick(View v) {
            String phoneNums = phonick.getText().toString().trim();
           // Log.e("-----","----"+ phoneNums+"---");
            int key = v.getId();
            switch (key) {
                case R.id.butt_register://提交
                    SMSSDK.submitVerificationCode("86", phoneNums, auth_code.getText()
                            .toString());
                    registeropition();// 验证成功后跳转主界面
                    break;
                case R.id.returlogin://返回
                    Intent intent1 = new Intent(ChangPaswActivity.this,LoginActivity.class);
                    startActivity(intent1);
                    finish();
                    break;
                
                case R.id.butt_code: //验证码;
                    if (!judgePhoneNums(phoneNums)) {// 判断输入号码是否正确  
                        return;
                    }else {
                        SMSSDK.getVerificationCode("86",phoneNums); // 调用sdk发送短信验证
                        Toast.makeText(ChangPaswActivity.this, "验证码已经发送",
                                Toast.LENGTH_SHORT).show();
                        butt_code.setClickable(false);// 设置按钮不可点击 显示倒计时  
                        butt_code.setText("重新发送(" + i + ")");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                for (i = 60; i > 0; i--) {
                                    handler.sendEmptyMessage(-9);
                                    if (i <= 0) {
                                        break;
                                    }
                                    try {
                                        Thread.sleep(1000);// 线程休眠实现读秒功能  
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                handler.sendEmptyMessage(-8);// 在30秒后重新显示为获取验证码  
                            }
                        }).start();
                    }
                break;
            }
        }
    private void registeropition() {
        if (register_pasw.getText() == null){
            register_pasw.setError("你还未输入密码");
            register_pasw.requestFocus();
        }else if (!thou_pasw.getText().toString().equals(register_pasw.getText().toString())){
            thou_pasw.setError("两次输入密码不一致");
            thou_pasw.requestFocus();
        }else if (register_pasw.getText().length()>=6){
            // 验证成功后跳转主界面
            createProgressBar();
            Intent intent = new Intent(ChangPaswActivity.this,LoginActivity.class);
            intent.putExtra("phon", phonick.getText().toString().trim());
            intent.putExtra("pasw",register_pasw.getText().toString().trim());
            setResult(111, intent);
            //startActivityForResult(intent,111);
            Toast.makeText(ChangPaswActivity.this, "提交成功",
                    Toast.LENGTH_SHORT).show();
            ChangPaswActivity.this.finish();// 成功跳转之后销毁当前页面
        }else {
            Toast.makeText(ChangPaswActivity.this, "提交失败！",
                    Toast.LENGTH_SHORT).show();
        }
    }
    private void createProgressBar() {
        FrameLayout layout = (FrameLayout) findViewById(android.R.id.content);
        LayoutParams layoutParams = new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        ProgressBar mProBar = new ProgressBar(this);
        mProBar.setLayoutParams(layoutParams);
        mProBar.setVisibility(View.VISIBLE);
        layout.addView(mProBar);
    }
    /**
         * 判断手机号码是否合理 
         *
         * @param phoneNums
         */
        private boolean judgePhoneNums(String phoneNums) {
            if (isMatchLength(phoneNums,phoneNums.length()) && isMobileNO(phoneNums)) {
                return true;
            }
            Toast.makeText(ChangPaswActivity.this, "手机号码输入有误！", Toast.LENGTH_SHORT).show();
            return false;
        }
        public  boolean isMatchLength(String str, int length) {
            if (str.isEmpty()) {
                return false;
            } else {
                return str.length() == length ? true : false;
            }
        }
        public  boolean isMobileNO(String mobileNums) {
            String telRegex = "^[1][34578][0-9]{9}$";//[0-9]* "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。  
            if (TextUtils.isEmpty(mobileNums))
                return false;
            else
                return mobileNums.matches(telRegex);
        }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();
       
    }
    /**
     * 正则表达式判断输入的内容只能是数字
     * @param str
     * @return
     */
    /*public boolean isNumeric(String str){
               Pattern pattern = Pattern.compile("[0-9]*");
               Matcher isNum = pattern.matcher(str);
               if( !isNum.matches() ){
                   return false;
                  }
                   return true;
             }*/
    /**
     * 正则表达式判断输入的内容只能是数字或字母
     * @param str
     * @return
     */
    public boolean isNumericAZ(String str){
        Pattern pattern = Pattern.compile("[0-9a-zA-Z]{0,30}");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches()){
            return false;
        }
        return true;
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        
    }
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
           if (!isNumericAZ(register_pasw.getText().toString().trim())){
               register_pasw.setError("只能输入字母或数字");
               register_pasw.requestFocus();
           }
    }
    @Override
    public void afterTextChanged(Editable s) {
        if (phonick.getText() !=null && phonick.getText().length()==11){
            butt_code.setBackgroundResource(R.drawable.verify_background);
            if (register_pasw.getText().length()>=6
                    &&register_pasw.getText().length()<=16
                    &&auth_code.getText().toString().trim().length()>=4) {
                butt_register.setBackgroundResource(R.drawable.rigst_background);
                butt_register.setClickable(true);
            }else {
                butt_register.setBackgroundResource(R.drawable.rigst_background1);
                butt_register.setClickable(false);
            }
        }else {
            butt_code.setBackgroundResource(R.drawable.verify_background1);
        }
       
    }
}














