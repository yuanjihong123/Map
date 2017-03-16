package com.example.administrator.mapclient.activity.homepage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.mapclient.R;
import com.example.administrator.mapclient.bean.PersonalData;
import com.example.administrator.mapclient.view.RoundImageView;

/**
 * 个人中心页面
 */
public class PersonalActivity extends AppCompatActivity
        implements View.OnClickListener {
    private TextView mTextView;//编辑控件
    private ImageView mImageView;//个人中心的头部返回按钮
    private RoundImageView headPortraitImage;//获取头像图片
    private TextView personal_nickname;//昵称
    private TextView TextView_personal_sex;//性别
    private TextView TextView_personal_age;//年龄
    private TextView TextView_personal_birthday;//生日
    private TextView TextView_personal_bloodType;//血型
    private TextView TextView_personal_Profession;//职业
    private TextView TextView_email;//邮箱
    private PersonalData personalData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        init();//初始化
    }

    private void init() {
        findview();//找id
        setListener();//绑定监听
        Intent intent = getIntent();
        int flag = intent.getIntExtra("flag", 0);
        if (flag != 1) {
            setData();//设置编辑完成后的资料
        }


    }

    private void setData() {
        personalData = new PersonalData();
        Intent intent = getIntent();
        //intent.getExtras();
        //获取里面的PersonalData数据
        Bitmap bitmap = intent.getParcelableExtra("bitmap");
        personalData = (PersonalData) intent.getSerializableExtra("PersonalData");
        headPortraitImage.setImageBitmap(bitmap);
        personal_nickname.setText(personalData.getPersonal_nickname());
        //Log.e("=====",personalData.toString());
        TextView_personal_sex.setText(personalData.getPersonal_sex());
        TextView_personal_age.setText(personalData.getPersonal_age());
        TextView_personal_birthday.setText(personalData.getPersonal_birthday());
        String bloodType = intent.getStringExtra("bloodType");
        TextView_personal_bloodType.setText(bloodType);
        TextView_personal_Profession.setText(personalData.getPersonal_Profession());
        TextView_email.setText(personalData.getEmail());
    }

    private void setListener() {
        mTextView.setOnClickListener(this);
        mImageView.setOnClickListener(this);
    }

    private void findview() {
        mTextView = (TextView) findViewById(R.id.personal_editor);//编辑个人信息的idhahhhhhh
        mImageView = (ImageView) findViewById(R.id.HomeActivity);
        headPortraitImage = (RoundImageView) findViewById(R.id.headPortraitImage);//头部头像id
        personal_nickname = (TextView) findViewById(R.id.TextView_personal_nickname);//昵称
        TextView_personal_sex = (TextView) findViewById(R.id.TextView_personal_sex);//性别
        TextView_personal_age = (TextView) findViewById(R.id.TextView_personal_age);//年龄
        TextView_personal_birthday = (TextView) findViewById(R.id.TextView_personal_birthday);//生日
        TextView_personal_bloodType = (TextView) findViewById(R.id.TextView_personal_bloodType);//血型
        TextView_personal_Profession = (TextView) findViewById(R.id.TextView_personal_Profession);//职业
        TextView_email = (TextView) findViewById(R.id.TextView_email);//邮箱
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.personal_editor://跳转到编辑个人信息页面
                Intent intent = new Intent(PersonalActivity.this, EditorActivity.class);
                //startActivityForResult(intent,1);
                startActivity(intent);
                break;
            case R.id.HomeActivity://点击跳转到主页面
                Intent intent1 = new Intent(PersonalActivity.this, HomeActivity.class);
                startActivity(intent1);
                finish();
                break;
        }
    }


}
