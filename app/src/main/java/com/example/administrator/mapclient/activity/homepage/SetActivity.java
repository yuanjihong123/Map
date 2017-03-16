package com.example.administrator.mapclient.activity.homepage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.example.administrator.mapclient.R;
import com.example.administrator.mapclient.utils.CacheUtils;

/**
 * 设置
 */
public class SetActivity extends AppCompatActivity{
    ToggleButton toggleBtn,toggleBtnInform;//打开/关闭
    Button exitBtn;//退出
    LinearLayout clearCache;//清除缓存
    LinearLayout checkUpdate;//检查更新
    LinearLayout aboutUs;//关于我们
    TextView showCache;//显示缓存
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        initView();
    }

    private void initView() {
        findView();
        setListener();

    }

    private void setListener() {
        String totalCacheSize = CacheUtils.getTotalCacheSize(this);
        showCache.setText(totalCacheSize);
        toggleBtn.setChecked(true);
        toggleBtnInform.setChecked(true);
        toggleBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Toast.makeText(SetActivity.this,"已开启",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(SetActivity.this,"已关闭",Toast.LENGTH_SHORT).show();
                }
            }
        });
        toggleBtnInform.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Toast.makeText(SetActivity.this,"已开启",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(SetActivity.this,"已关闭",Toast.LENGTH_SHORT).show();
                }
            }
        });
        setMyListener setListener = new setMyListener();
        clearCache.setOnClickListener(setListener);
        checkUpdate.setOnClickListener(setListener);
        aboutUs.setOnClickListener(setListener);
    }
    private class setMyListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.clearCache:
                    Toast.makeText(SetActivity.this,"开始清理...",Toast.LENGTH_SHORT).show();
                    CacheUtils.clearAllCache(SetActivity.this);
                    final String s = CacheUtils.getTotalCacheSize(SetActivity.this);
                    if ("0.00KB".equals(s)){
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1*1000);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            showCache.setText(s);
                                            Toast.makeText(SetActivity.this,"垃圾清理完毕！",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                    break;
                case R.id.checkUpdate:
                    Toast.makeText(SetActivity.this,"当前是最新版本！",Toast.LENGTH_LONG).show();
                    break;
                case R.id.aboutUs:
                    startActivity(new Intent(SetActivity.this,AboutUsActivity.class));
                    break;
            }
        }
    }


    private void findView() {
        toggleBtn = (ToggleButton) findViewById(R.id.toggleBtn);
        exitBtn = (Button) findViewById(R.id.exitBtn);
        toggleBtnInform = (ToggleButton) findViewById(R.id.toggleBtnInform);
        clearCache  = (LinearLayout) findViewById(R.id.clearCache);
        checkUpdate  = (LinearLayout) findViewById(R.id.checkUpdate);
        aboutUs  = (LinearLayout) findViewById(R.id.aboutUs);
        showCache = (TextView) findViewById(R.id.showCache);
    }
}
