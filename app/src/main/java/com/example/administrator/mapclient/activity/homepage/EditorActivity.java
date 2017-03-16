package com.example.administrator.mapclient.activity.homepage;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.mapclient.R;
import com.example.administrator.mapclient.bean.PersonalData;
import com.example.administrator.mapclient.view.RoundImageView;

import java.util.Calendar;

public class EditorActivity extends AppCompatActivity
        implements View.OnClickListener,
        RadioGroup.OnCheckedChangeListener,
        AdapterView.OnItemSelectedListener {
    private RoundImageView headPortraitImage;//头像变化控件
    private EditText EditText_nickname;//昵称输入控件
    private RadioGroup rg;
    private RadioButton rb1, rb2;//性别
    private EditText EditText_age;//年龄
    private TextView Editor_Birsthday;//生日选择控件
    private EditText EditText_personal_Profession;//个人职业
    private EditText EditText_personal_Email;//个人邮箱
    public static final String type = "image/*";//图片的类型
    public static final int GET_PHOTO = 1;//获取照片的请求码
    public static final int RESULT = 2;//处理返回图片的结果码
    private ImageView returnPersonalAcivity;//头部的返回控件
    private TextView personal_editor_finish;//头部点击编辑完成的控件
    private Bitmap bitmap;
    private PersonalData personalData;
    private String gender;//性别男
    private Spinner bloodType_Spinner;//血型下拉框
    private String bloodType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        init();//初始化
    }

    private void init() {
        findView();//找id0
        setListener();//设置绑定监听
        personalData = getData();//获取编辑的数据

    }

    private PersonalData getData() {
        personalData = new PersonalData();
        personalData.setPersonal_nickname(EditText_nickname.getText().toString());//将编辑的昵称赋值给对象
        personalData.setPersonal_sex(gender);
        personalData.setPersonal_age(EditText_age.getText().toString());//获取到年龄
        personalData.setPersonal_birthday(Editor_Birsthday.getText().toString());//获取到生日
        //personalData.setPersonal_bloodType(EditText_bloodType.getText().toString());//获取血型
        personalData.setPersonal_Profession(EditText_personal_Profession.getText().toString());//获取职业
        personalData.setEmail(EditText_personal_Email.getText().toString());//获取职业
        //Log.e("======",personalData.toString());
        return personalData;
    }

    private void setListener() {
        headPortraitImage.setOnClickListener(this);//绑定头像设置的监听
        returnPersonalAcivity.setOnClickListener(this);//绑定返回监听
        personal_editor_finish.setOnClickListener(this);//绑定编辑完成监听
        rg.setOnCheckedChangeListener(this);//性别选中的监听
        bloodType_Spinner.setOnItemSelectedListener(this);
    }

    private void findView() {
        returnPersonalAcivity = (ImageView) findViewById(R.id.returnPersonalAcivity);//头部的返回控件id
        personal_editor_finish = (TextView) findViewById(R.id.personal_editor_finish);//头部编辑完成id
        headPortraitImage = (RoundImageView) findViewById(R.id.headPortraitImage);//头像id
        EditText_nickname = (EditText) findViewById(R.id.EditText_nickname);//编辑昵称的id
        rg = (RadioGroup) findViewById(R.id.rg);//选择id
        rb1 = (RadioButton) findViewById(R.id.rb1);//男id
        rb2 = (RadioButton) findViewById(R.id.rb2);//女id
        EditText_age = (EditText) findViewById(R.id.EditText_age);//年龄id
        Editor_Birsthday = (TextView) findViewById(R.id.Editor_Birsthday);//生日选择控件id
        bloodType_Spinner = (Spinner) findViewById(R.id.bloodType_Spinner);//血型编辑下拉框控件id
        EditText_personal_Profession = (EditText) findViewById(R.id.EditText_personal_Profession);//个人职业编辑控件id
        EditText_personal_Email = (EditText) findViewById(R.id.EditText_personal_Email);//个人邮箱编辑控件id

    }

    //点击获取日期
    public void btnShowDate(View view) {
        Calendar calendar = Calendar.getInstance();
        //获取到当前的日历事件
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(//日期选择器的构建
                this,
                new OnDateSetListener() {
                    @Override
                    public void onDateSet(
                            DatePicker view,
                            int year,
                            int monthOfYear,
                            int dayOfMonth) {
                        //选中的日历的时间。monthOfYear＝:实际的月份-1。范围是［0，11］。
                        Toast.makeText(EditorActivity.this,
                                year + "-" + monthOfYear + "-" + dayOfMonth,
                                Toast.LENGTH_SHORT).show();
                        Editor_Birsthday.setText(monthOfYear + "-" + dayOfMonth);
                    }
                },
                year, month, day);//日历显示的默认时间
        //显示日期选择对话框
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.headPortraitImage://点击打开相册
                getPhoto();
                break;
            case R.id.returnPersonalAcivity://点击返回个人中心
                Intent intent = new Intent(EditorActivity.this, PersonalActivity.class);
                startActivity(intent);
                break;
            case R.id.personal_editor_finish:
                /**
                 * 1.将数据提交到服务器并且设置到个人资料
                 * 2.跳转到个人资料上面
                 */
                String s1 = EditText_nickname.getText().toString();
                // String s3 = EditText_bloodType.getText().toString();
                String s4 = EditText_personal_Profession.getText().toString();
                if (TextUtils.isEmpty(s1)) {
                    EditText_nickname.setError("输入不能为空！");
                    EditText_nickname.requestFocus();
                } else if (TextUtils.isEmpty(s4)) {
                    EditText_personal_Profession.setError("请输您的职业");
                    EditText_personal_Profession.requestFocus();
                } else if(bitmap==null){
                    Toast.makeText(this,"请设置你的头像",Toast.LENGTH_SHORT).show();
                    headPortraitImage.requestFocus();
                }else {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("PersonalData", getData());
                    //Log.e("======",getData().toString());
                    Intent intent3 = new Intent(EditorActivity.this, PersonalActivity.class);
                    intent3.putExtras(bundle);
                    intent3.putExtra("flag", 2);
                    intent3.putExtra("bitmap", bitmap);
                    intent3.putExtra("bloodType", bloodType);
                    startActivity(intent3);
                    finish();
                }
                break;

        }
    }

    /**
     * 从相册获取图片
     */
    private void getPhoto() {
        //使用意图去获取媒体资源
        Intent intent = new Intent(Intent.ACTION_PICK);
        //设置图片类型，进行图片资源的访问
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, type);
        //打开系统界面
        startActivityForResult(intent, GET_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {//为了防止没有选择图片就返回尓抱空指针异常
            return;
        }
        if (requestCode == GET_PHOTO) {//如果请求码等于GET_PHOTO的话说明是在相册获取到图片的返回
            Uri data1 = data.getData();//获取到图片的uri
            String path = findPath(data1);//根据uri去找图片地址
            //Log.e("======",path);
            startRESULT(data1);
        }
        if (requestCode == RESULT) {
            //Log.e("=======","iiiiii");
            Bundle bundle = data.getExtras();   //返回的是一个bundle
            bitmap = bundle.getParcelable("data");
            if(bitmap==null){
                /*headPortraitImage.setImageResource(R.drawable.nickimg);*/
                //bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.nickimg,null);
                /*Resources res=getResources();
                bitmap=BitmapFactory.decodeResource(res, R.drawable.nickimg);*/

                bitmap = ((BitmapDrawable)getResources().getDrawable(R.drawable.nickimg)).getBitmap();
            }
            //显示图片
            headPortraitImage.setImageBitmap(bitmap);


        }

    }


    private void startRESULT(Uri data1) {
        //设置Action
        Intent intent = new Intent("com.android.camera.action.CROP");
        //设置请求数据的类型
        intent.setDataAndType(data1, type);
        //设置是否需要裁剪
        intent.putExtra("crop", "true");
        //设置裁剪的宽和高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //设置输出图片的宽和高
        intent.putExtra("outputX", 500);
        intent.putExtra("outputY", 500);
        //设置是否需要返回数据
        intent.putExtra("return-data", true);
        //打开页面开始裁剪了...
        startActivityForResult(intent, RESULT);
    }

    /**
     * @param data1：图片资源的uri
     * @return 一个图片地址
     */
    private String findPath(Uri data1) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(data1, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(column_index);
        //Log.e("==========",path);
        return path;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb1:
                Toast.makeText(this, "选中了性别男", Toast.LENGTH_LONG).show();
                gender = rb1.getText().toString();
                break;
            case R.id.rb2:
                Toast.makeText(this, "选中了性别女", Toast.LENGTH_LONG).show();
                gender = rb2.getText().toString();
                break;

            default:
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        bloodType = (String) parent.getItemAtPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
