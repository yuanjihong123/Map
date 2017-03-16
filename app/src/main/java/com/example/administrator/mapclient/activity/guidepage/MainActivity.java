package com.example.administrator.mapclient.activity.guidepage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.administrator.mapclient.R;
import com.example.administrator.mapclient.activity.loginorregister.LoginActivity;
import com.example.administrator.mapclient.adapter.ViewPagerAdapter;
import com.example.administrator.mapclient.bean.BasePath;
import java.util.ArrayList;
import java.util.List;

/**
 * 引导页
 */
public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    private ViewPager mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;
    private List<View> viewList;
    private Button mButton;
    private LinearLayout gpage;
  
    private BasePath mBasePath;
    private int currenIndex; //记录当前位置
    private ImageView[] dots;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gpage = (LinearLayout) findViewById(R.id.gpage);
        mBasePath = new BasePath();
        initView();
        viewList = new ArrayList<View>();
        // 为引导图片提供布局参数
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        // 初始化引导图片列表
        for (int i = 0; i < mBasePath.pics.length; i++) {
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(mParams);
            iv.setImageResource(mBasePath.pics[i]);
            viewList.add(iv);
        }
        
        // 初始化Adapter
        mViewPagerAdapter = new ViewPagerAdapter(viewList);
        mViewPager.setAdapter(mViewPagerAdapter);
        // 绑定回调
        mViewPager.setOnPageChangeListener(this);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, LoginActivity.class);
                MainActivity.this.startActivity(intent);
                finish();
            }
        });

       
        
    }
    private void initView() {
        mButton = (Button) findViewById(R.id.button);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        // 初始化底部小圆点
        initDots();
    }
    private void initDots() {
        LinearLayout gpage = (LinearLayout) findViewById(R.id.gpage);
        dots = new ImageView[mBasePath.pics.length];
        // 循环取得小点图片
        for (int i = 0; i < mBasePath.pics.length; i++) {
            dots[i] = (ImageView) gpage.getChildAt(i);
            dots[i].setEnabled(false);// 都设为灰色
            dots[i].setTag(i);// 设置位置tag，方便取出与当前位置对应
        }
        currenIndex = 0;
        dots[currenIndex].setEnabled(true);// 设置为白色，即选中状态
    }

    /**
     * 改变当前引导小点颜色ؿ
     */
    private void setCurDot(int positon) {
        if (positon < 0 || positon > mBasePath.pics.length - 1 || currenIndex == positon) {
            return;
        }
        dots[positon].setEnabled(true);
        //此时的currentIndex指的是上一个圆点
        dots[currenIndex].setEnabled(false);
        //现在的currentIndex指的是当前的小圆点
        currenIndex = positon;
    }

    // 当滑动状态改变时调用
    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    // 当前页面被滑动时调用
    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    // 当新的页面被选中时调用
    @Override
    public void onPageSelected(int arg0) {
        // 设置底部小点选中状态
        setCurDot(arg0);
        if (arg0 == 3) {
            mButton.setVisibility(View.VISIBLE);
            gpage.setVisibility(View.GONE);
        } else {
            mButton.setVisibility(View.GONE);
            gpage.setVisibility(View.VISIBLE);
        }
    }
}
