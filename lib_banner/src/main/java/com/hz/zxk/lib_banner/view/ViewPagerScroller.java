package com.hz.zxk.lib_banner.view;

import android.content.Context;
import android.widget.Scroller;

/**
 * 自定义Scroller
 * 修改scroller的滚动时间
 * @author zhengxiaoke
 * @date 2019/3/21 2:09 PM
 */
public class ViewPagerScroller extends Scroller {
    private int mCustomDuration;
    public ViewPagerScroller(Context context,int duration) {
        super(context);
        this.mCustomDuration=duration;
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy,mCustomDuration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, mCustomDuration);
    }
}
