package com.hz.zxk.crazybanner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.hz.zxk.lib_banner.CrazyBannerView;
import com.hz.zxk.lib_banner.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private CrazyBannerView bannerView;
    private MyAdapter myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bannerView=findViewById(R.id.viewpager);
        myAdapter=new MyAdapter(this,getImage());
        myAdapter.setOnItemClickListener(new OnItemClickListener<Integer>() {
            @Override
            public void onClick(View view, int position, Integer data) {
                Log.d("TAG","data="+data);
            }
        });
        bannerView.setAdapter(myAdapter);
        bannerView.start();
    }

    private List<Integer> getImage(){
        List<Integer> datas=new ArrayList<>();
        datas.add(R.drawable.img1);
        datas.add(R.drawable.img2);
        datas.add(R.drawable.img3);
       return datas;
    }
}
