package com.example.administrator.mapclient.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * 引导页pagerAdapter适配器
 * Created by Administrator on 2017/2/14.
 */
public class ViewPagerAdapter extends PagerAdapter {
    private List<View> viewList;
    /**
     * 界面列表
     **/
    public ViewPagerAdapter(List<View> viewList){
       this.viewList = viewList;
    }
    /**
     * 删除界面
     * @param container
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(viewList.get(position));
    }
    /**
     * 获取当前界面数
     * @return
     */
    @Override
    public int getCount() {
        if (viewList != null){
         return viewList.size();   
        }
        return 0;
    }
    /**
     * 初始化界面container的位置
     * @param container
     * @param position
     * @return
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(viewList.get(position),0);
        return viewList.get(position);
    }
    /**
     * 判断是否由界面生成对象
     * @param view
     * @param object
     * @return
     */
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    } 
}
