package com.hz.zxk.crazybanner;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hz.zxk.lib_banner.CrazyBannerAdapter;

import java.util.List;

/**
 * @author zhengxiaoke
 * @date 2019/3/21 11:46 AM
 */
public class MyAdapter extends CrazyBannerAdapter<Integer> {
    private Context context;
    public MyAdapter(Context context,List<Integer> datas){
        super(datas);
        this.context=context;
    }

    @Override
    public View getView(ViewGroup container, int position) {
        ImageView imageView=new ImageView(context);
        ViewGroup.LayoutParams layoutParams=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        imageView.setLayoutParams(layoutParams);
        imageView.setImageResource(getDatas().get(position));
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    }
}
