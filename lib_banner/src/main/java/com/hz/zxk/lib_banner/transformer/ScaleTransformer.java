package com.hz.zxk.lib_banner.transformer;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * @author zhengxiaoke
 * @date 2019/3/21 9:46 PM
 */
public class ScaleTransformer implements ViewPager.PageTransformer {
    private static final float MIN_SCALE=0.8f;
    private static final float MIN_ALPHA=0.5f;
    @Override
    public void transformPage(@NonNull View view, float position) {

        if(position<-1){
            view.setScaleY(MIN_SCALE);
//            view.setScaleX(MIN_SCALE);
            view.setAlpha(MIN_ALPHA);
        }else if(position<=1){
            float scale=Math.max(MIN_SCALE,1-Math.abs(position));
//            view.setScaleX(scale);
            view.setScaleY(scale);
            view.setAlpha(Math.max(MIN_ALPHA,1-Math.abs(position)));

        }else{
            view.setScaleY(MIN_SCALE);
//            view.setScaleX(MIN_SCALE);
            view.setAlpha(MIN_ALPHA);
        }
    }
}
