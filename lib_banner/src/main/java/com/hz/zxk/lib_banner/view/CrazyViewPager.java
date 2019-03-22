package com.hz.zxk.lib_banner.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;


import com.hz.zxk.lib_banner.transformer.ScaleTransformer;

import java.lang.reflect.Field;

/**
 * @author zhengxiaoke
 * @date 2019/3/21 10:45 AM
 */
public class CrazyViewPager extends ViewPager {
    private int duration = 800;

    public CrazyViewPager(@NonNull Context context, int duration) {
        super(context);
        this.duration = duration;
        init(context);
    }

    public CrazyViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context) {
        // 通过反射，修改viewpager的mScroller
        // 使用自定义的Scroller,来改变滚动速度快慢
        try {
            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            ViewPagerScroller viewPagerScroller = new ViewPagerScroller(context, duration);
            mScroller.set(this, viewPagerScroller);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
