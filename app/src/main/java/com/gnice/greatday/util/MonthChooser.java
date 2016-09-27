package com.gnice.greatday.util;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.gnice.greatday.R;

import java.util.Calendar;
import java.util.Date;

public class MonthChooser extends HorizontalScrollView implements Runnable{

    private ImageButton[] monthButtons;
    private OnMonthChooseListener monthChooseListener;
    private int year;

    private Calendar cal = Calendar.getInstance();  // 用于计算 防止当前年超出月份
    private LinearLayout monthsList;

//    LayoutInflater inflater;
//    LinearLayout groupPollingAddress = (LinearLayout)inflater.inflate(R.layout.fragment_field_list, null);


    public MonthChooser(Context context, int year) {
        super(context);
        this.year = year;
//        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

//        monthsList = (LinearLayout)findViewById(R.id.months_list);
//        monthsList = (LinearLayout)inflater.inflate(R.layout.months_list, this);
        monthsList = new LinearLayout(context);
        createUI(context);
    }



    private void createUI(Context context) {
        setHorizontalScrollBarEnabled(false);
        setOverScrollMode(2);
        setSmoothScrollingEnabled(true);
        setBackgroundColor(Color.parseColor("#f2f1ed"));
        setFillViewport(true);


        int[] month_buttons = new int[12];
        month_buttons[0]  = R.drawable.month_1_button;
        month_buttons[1]  = R.drawable.month_2_button;
        month_buttons[2]  = R.drawable.month_3_button;
        month_buttons[3]  = R.drawable.month_4_button;
        month_buttons[4]  = R.drawable.month_5_button;
        month_buttons[5]  = R.drawable.month_6_button;
        month_buttons[6]  = R.drawable.month_7_button;
        month_buttons[7]  = R.drawable.month_8_button;
        month_buttons[8]  = R.drawable.month_9_button;
        month_buttons[9]  = R.drawable.month_10_button;
        month_buttons[10] = R.drawable.month_11_button;
        month_buttons[11] = R.drawable.month_12_button;

        cal.setTime(new Date());
        monthButtons = new ImageButton[month_buttons.length];


        int currentMonth = 13;
        if (cal.get(Calendar.YEAR) == this.year)
            currentMonth = cal.get(Calendar.MONTH) + 1;

        LinearLayout.LayoutParams buttomLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int marginPX = UITool.dip2px(context, 2);  // 控制月份按钮之间间距
        buttomLayoutParams.setMargins(marginPX, marginPX/3, marginPX, 0);
//        buttomLayoutParams.height = UITool.dip2px(context, 72);  // 强制设置月份按钮高度

        for (int i = 0; i < 12; i++) {
            ImageButton localImageButton = new ImageButton(context);
            localImageButton.setBackgroundResource(0);
            localImageButton.setImageResource(month_buttons[i]);
            localImageButton.setPadding(0, 0, 0, 0);
            localImageButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer tag = (Integer)v.getTag();
                    if (MonthChooser.this.monthChooseListener != null)
                        MonthChooser.this.monthChooseListener.monthSelected(tag);
                }
            });
            localImageButton.setTag(i);
            if (i >= currentMonth)
                localImageButton.setEnabled(false);

            monthButtons[i] = localImageButton;
            monthsList.addView(localImageButton, buttomLayoutParams);  // 设置月份按钮样式
        }

        MonthChooser.LayoutParams listLayoutParms = new MonthChooser.LayoutParams(MonthChooser.LayoutParams.MATCH_PARENT, MonthChooser.LayoutParams.WRAP_CONTENT);
//        MonthChooser.LayoutParams listLayoutParms = new MonthChooser.LayoutParams(UITool.dip2px(context, 72), UITool.dip2px(context, 72));
////        listLayoutParms.setMargins(15, -1, 15, 5);
//        listLayoutParms.height = UITool.dip2px(context, 128);
        setFillViewport(true);
//        monthsList.setLayoutParams(listLayoutParms);

//        setLayoutParams(new HorizontalScrollView.LayoutParams(800, 1));

        addView(monthsList, listLayoutParms);
//        addView(monthsList);
    }

    // // TODO: 2016/9/26 fix
    public boolean smoothScrollBy() {
//        for (int i = 0; i < monthsList.getChildCount(); i++) {
//            View localView = monthsList.getChildAt(i);
//            if (localView.isSelected()) {
//                super.smoothScrollBy(Math.max(0, localView.getLeft() - UITool.dip2px(getContext(), 60)), 0);
////                getMeasuredWidth() / 2
//                break;
//            }
//            super.smoothScrollBy(-UITool.dip2px(getContext(), 20), 0);
//            Log.i("getLeft", "" + localView.getLeft());
//            Log.i("getChildCount", "" + monthsList.getChildCount());
//        }
        return true;
    }

    public void run() {
        smoothScrollBy(UITool.dip2px(getContext(), 20), 0);
    }

    public OnMonthChooseListener getMonthChooseListener()
    {
        return this.monthChooseListener;
    }

    // 决定了当前View的大小
    protected void onMeasure(int paramInt1, int paramInt2)
    {
        super.onMeasure(paramInt1, paramInt2);
        setMeasuredDimension(View.MeasureSpec.getSize(paramInt1), UITool.dip2px(getContext(),48));
//        setMeasuredDimension(View.MeasureSpec.getSize(paramInt1), 32);
    }

    public void setMonthChooseListener(OnMonthChooseListener paramOnMonthChooseListener)
    {
        this.monthChooseListener = paramOnMonthChooseListener;
    }

    public void setSelectedMonth(int paramInt)
    {
        this.monthButtons[paramInt].setSelected(true);
    }


    public static abstract interface OnMonthChooseListener
    {
        public abstract void monthSelected(int month);
    }
}
