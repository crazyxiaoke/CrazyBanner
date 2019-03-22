package com.hz.zxk.lib_banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.hz.zxk.lib_banner.transformer.ScaleTransformer;
import com.hz.zxk.lib_banner.view.CrazyViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * 实现无限自动滚动轮播图
 * @author zhengxiaoke
 * @date 2019/3/21 9:52 AM
 */
public class CrazyBannerView extends RelativeLayout
    implements ViewPager.OnPageChangeListener {

    private static final int DEFAULT_INDICATOR_LAYOUT=R.layout.item_indicator;

    //默认滚动速度
    private static final int DEFAULT_DURATION_TIME=800;
    //默认开启自动滚动
    private static final boolean DEFAULT_AUTO_SCROLL=true;
    //默认不开启clipChildren
    private static final boolean DEFAULT_CLIPCHILDREN=false;
    //默认开启无限循环
    private static final boolean DEFAULT_CAN_LOOP=true;

    private static final int STATUS_AUTO_SCROLL_STOP=0;  //停止
    private static final int STATUS_AUTO_SCROLL_START=1; //启动

    //indicator位置
    public enum IndicatorAlgin{
        LEFT,  //居左
        CENTER, //居中
        RIGHT  //居右
    }

    //indicator样式
    private int mIndicatorLayoutId=DEFAULT_INDICATOR_LAYOUT;
    //indicator对齐方式
    private int mIndicatorAlign=IndicatorAlgin.CENTER.ordinal();
    //滚动速度
    private int mDuraction=DEFAULT_DURATION_TIME;
    //是否启动自动滚动
    private boolean isAutoScroll=DEFAULT_AUTO_SCROLL;
    //是否开启clipChildren
    private boolean isClipChildren=DEFAULT_CLIPCHILDREN;
    //是否开启无限循环
    private boolean isCanLoop=DEFAULT_CAN_LOOP;


    private CrazyViewPager mViewPager;
    private LinearLayout mIndicatorLayout;
    private List<View> mIndicators; //存放指示器

    private int mCurrentItem;
    private Handler mLoopHandler;
    private long delayTime=3000; //view停顿时间
    private int mAutoScrollStatus=STATUS_AUTO_SCROLL_STOP; //自动滚动状态
    private CrazyBannerAdapter mAdapter;

    private ViewPager.OnPageChangeListener onPageChangeListener;

    public CrazyBannerView(Context context) {
        this(context, null);
    }

    public CrazyBannerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CrazyBannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public CrazyBannerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setBackgroundColor(Color.GRAY);
        initAttrs(context,attrs);
        mLoopHandler=new Handler();
        mIndicators=new ArrayList<>();
        initViewPager(context);
        initIndicator(context);
    }

    /**
     * 获取attrs参数
     * @param attrs
     */
    private void initAttrs(Context context,AttributeSet attrs){
        TypedArray typedArray=context.obtainStyledAttributes(attrs,R.styleable.CrazyBannerView);
        try{
            mIndicatorLayoutId=typedArray.getResourceId(R.styleable.CrazyBannerView_indicator_layout,DEFAULT_INDICATOR_LAYOUT);
            mIndicatorAlign=typedArray.getInt(R.styleable.CrazyBannerView_indicator_align,IndicatorAlgin.CENTER.ordinal());
            mDuraction=typedArray.getInt(R.styleable.CrazyBannerView_duration,DEFAULT_DURATION_TIME);
            isAutoScroll=typedArray.getBoolean(R.styleable.CrazyBannerView_autoScroll,DEFAULT_AUTO_SCROLL);
            isClipChildren=typedArray.getBoolean(R.styleable.CrazyBannerView_clipChildren,DEFAULT_CLIPCHILDREN);
            isCanLoop=typedArray.getBoolean(R.styleable.CrazyBannerView_canLoop,DEFAULT_CAN_LOOP);
        }finally {
            typedArray.recycle();
        }
    }

    /**
     * 初始化viewpager
     * @param context
     */
    private void initViewPager(Context context) {
        setClipChildren(isClipChildren);
        mViewPager = new CrazyViewPager(context,mDuraction);
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setClipChildren(isClipChildren);
        if(isClipChildren){
            lp.leftMargin=dip2px(context,60);
            lp.rightMargin=dip2px(context,60);
            mViewPager.setPageTransformer(false,new ScaleTransformer());
        }
        mViewPager.setLayoutParams(lp);
        addView(mViewPager);
    }

    /**
     * 初始化指示器
     * @param context
     */
    private void initIndicator(Context context) {
        mIndicatorLayout = new LinearLayout(context);
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.bottomMargin = dip2px(context,16);
        lp.addRule(ALIGN_PARENT_BOTTOM);
        if(mIndicatorAlign==IndicatorAlgin.LEFT.ordinal()){
            lp.addRule(ALIGN_PARENT_LEFT);
            lp.leftMargin=dip2px(context,16);
        }else if(mIndicatorAlign==IndicatorAlgin.CENTER.ordinal()){
            lp.addRule(CENTER_HORIZONTAL);
        }else if(mIndicatorAlign==IndicatorAlgin.RIGHT.ordinal()){
            lp.addRule(ALIGN_PARENT_RIGHT);
            lp.rightMargin=dip2px(context,16);
        }
        mIndicatorLayout.setLayoutParams(lp);
        addView(mIndicatorLayout);
    }

    /**
     * 添加指示器
     */
    private void addIndicator(){
        //清空mIndicatorLayout
        mIndicatorLayout.removeAllViews();
        //当只有一个child时，不显示indicator
        if(mAdapter!=null&&mAdapter.getRealCount()>1){
            for(int i=0;i<mAdapter.getRealCount();i++){
                View indicator=LayoutInflater.from(getContext())
                        .inflate(mIndicatorLayoutId,null);
                LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                if(i<mAdapter.getRealCount()-1){
                    layoutParams.rightMargin=dip2px(getContext(),10);
                }
                indicator.setLayoutParams(layoutParams);
                if(i==mCurrentItem%mAdapter.getRealCount()){
                    //默认选中
                    indicator.setSelected(true);
                }
                mIndicatorLayout.addView(indicator);
                mIndicators.add(indicator);
            }
        }
    }

    /**
     * 自动滚动
     */
    private Runnable mAutoScrollTask=new Runnable() {
        @Override
        public void run() {
            //当只有一个child时，不滚动
            if(mAdapter!=null&&mAdapter.getRealCount()>1
                    &&mCurrentItem<mAdapter.getCount()){
                mViewPager.setCurrentItem(mCurrentItem+1);
                mLoopHandler.postDelayed(mAutoScrollTask,delayTime);
            }

        }
    };

    /**
     * 设置数据
     * @param adapter
     */
    public void setAdapter(CrazyBannerAdapter adapter){
        if(adapter!=null) {
            this.mAdapter = adapter;
            adapter.setCanLoop(isCanLoop);
            mViewPager.setAdapter(adapter);
            mViewPager.setCurrentItem(adapter.getCurrIndex());
            addIndicator();
        }
    }


    /**
     * 设置滚动监听
     * @param onPageChangeListener
     */
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
    }

    /**
     * 开始自动滚动
     */
    public void start(){
        Log.d("TAG","轮播开始");
        if(mAutoScrollStatus==STATUS_AUTO_SCROLL_STOP
                &&isAutoScroll&&isCanLoop) {
            //避免重复启动
            if (mLoopHandler != null && mAutoScrollTask != null) {
                mLoopHandler.postDelayed(mAutoScrollTask, delayTime);
                mAutoScrollStatus=STATUS_AUTO_SCROLL_START;
            }
        }
    }

    /**
     * 停止自动滚动
     */
    public void pause(){
        Log.d("TAG","轮播停止");
        if(mAutoScrollStatus==STATUS_AUTO_SCROLL_START
                &&isAutoScroll&&isCanLoop){
            if(mLoopHandler!=null&&mAutoScrollTask!=null){
                mLoopHandler.removeCallbacks(mAutoScrollTask);
                mAutoScrollStatus=STATUS_AUTO_SCROLL_STOP;
            }
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                //触摸监听
                //当手指按下或者滑动时，停止自动滚动
                pause();
                break;
            case MotionEvent.ACTION_UP:
                //手指松开后开启自动滚动
                start();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
         int realPosition=position%mAdapter.getRealCount();
         if(onPageChangeListener!=null){
             onPageChangeListener.onPageScrolled(realPosition,positionOffset,positionOffsetPixels);
         }
    }

    @Override
    public void onPageSelected(int position) {
        mCurrentItem=position;
        int realPosition=position%mAdapter.getRealCount();
        if(mIndicators!=null){
            for(int i=0;i<mIndicators.size();i++){
                if(i==realPosition){
                    mIndicators.get(i).setSelected(true);
                }else{
                    mIndicators.get(i).setSelected(false);
                }
            }
        }

        if(onPageChangeListener!=null){
            onPageChangeListener.onPageSelected(realPosition);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if(onPageChangeListener!=null){
            onPageChangeListener.onPageScrollStateChanged(state);
        }
    }


    private int dip2px(Context context,float dip){
        return (int)(context.getResources().getDisplayMetrics().density*dip+0.5f);
    }

}
