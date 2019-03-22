package com.hz.zxk.lib_banner;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.hz.zxk.lib_banner.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public abstract class CrazyBannerAdapter<T> extends PagerAdapter {

    private boolean isCanLoop = true;
    private int mLoopCount = 1000;
    List<T> datas;
    private OnItemClickListener<T> onItemClickListener;

    public CrazyBannerAdapter() {
        datas = new ArrayList<>();
    }


    public CrazyBannerAdapter(List<T> datas) {
        this.datas = datas;
    }

    public List<T> getDatas() {
        return this.datas;
    }

    public T getItem(int position) {
        if (datas != null) {
            return datas.get(position);
        }
        return null;
    }

    @Override
    public int getCount() {
        return isCanLoop ? mLoopCount * getRealCount() : getRealCount();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        final View view = getView(container, position % getRealCount());
        container.addView(view);
        //view点击事件
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onClick(view, position % getRealCount()
                            , getItem(position % getRealCount()));
                }
            }
        });
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    /**
     * 设置是否开启循环
     *
     * @param canLoop
     */
    protected void setCanLoop(boolean canLoop) {
        this.isCanLoop = canLoop;
    }

    /**
     * 真实的item数量
     *
     * @return
     */
    public int getRealCount() {
        return datas == null ? 0 : datas.size();
    }

    /**
     * 初始位置
     *
     * @return
     */
    public int getCurrIndex() {
        return isCanLoop ? getCount() / 2 : 0;
    }

    /**
     * item点击事件
     *
     * @param onItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public abstract View getView(ViewGroup container, int position);

}