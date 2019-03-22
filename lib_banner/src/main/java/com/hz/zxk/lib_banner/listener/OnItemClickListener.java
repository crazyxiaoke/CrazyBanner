package com.hz.zxk.lib_banner.listener;

import android.view.View;

/**
 * banner里的view点击事件
 * @author zhengxiaoke
 * @date 2019/3/21 4:31 PM
 */
public interface OnItemClickListener<T> {
    void onClick(View view, int position,T data);
}
