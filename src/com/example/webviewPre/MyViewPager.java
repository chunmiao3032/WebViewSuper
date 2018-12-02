package com.example.webviewPre;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;

public class MyViewPager extends ViewPager
{
    
    public MyViewPager(Context context)
    {
        super(context);
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0)
    {
        // TODO Auto-generated method stub
        return false;
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent arg0)
    {
        // TODO Auto-generated method stub
        return false;
    }
}
