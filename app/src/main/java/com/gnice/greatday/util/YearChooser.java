package com.gnice.greatday.util;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Calendar;


public class YearChooser extends HorizontalScrollView implements View.OnClickListener {
    private Button[] yearButtons;
    private OnYearChooseListener onYearChooseListener;
    private int year;  // 当前年份

    private Calendar cal;// 用于计算 防止超出年份
    private LinearLayout yearsList;

    public YearChooser(Context context, int year)
    {
        super(context);
        this.year = year;
        cal = Calendar.getInstance();
        yearsList = new LinearLayout(context);
        createUI(context);
    }

    private void createUI(Context context)
    {
        setHorizontalScrollBarEnabled(false);
        setOverScrollMode(2);
        setSmoothScrollingEnabled(true);
        setBackgroundColor(Constant.backgroundColor);
        setFillViewport(true);

        ColorStateList colorStateList = new ColorStateList(new int[][]{{android.R.attr.state_selected}, {0}}, new int[]{Constant.text_seleted, Constant.text_unseleted});
//        LinearLayout.LayoutParams buttomLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams buttomLayoutParams = new LinearLayout.LayoutParams(UITool.dip2px(context, 42), UITool.dip2px(context, 48));
        int marginPX = UITool.dip2px(context, 1);  // 控制年份按钮之间间距
//        buttomLayoutParams.setMargins(marginPX, marginPX, marginPX*3/4, 0);
        buttomLayoutParams.setMargins(0,0,0,0);

        ArrayList arrayListTmp = new ArrayList();

        int currentYear = cal.get(Calendar.YEAR);
        for (int i = 2010; i <= currentYear; i++) {
            Button localButton = new Button(context);
            localButton.setBackgroundResource(0);
            localButton.setPadding(0, 0, 0, 0);
            localButton.setText(String.valueOf(i));
//            localButton.setTextColor(Constant.toolbarTextColor);
            localButton.setTextColor(colorStateList);
//            localButton.setTypeface(App.getFontArvil());
            localButton.setTextSize(UITool.dip2px(context, 5));
            localButton.setOnClickListener(this);
            localButton.setTag(i);

            if (i == this.year) {
                localButton.setSelected(true);
            }

            yearsList.addView(localButton, buttomLayoutParams);
            arrayListTmp.add(localButton);
        }

        yearButtons = (Button[]) arrayListTmp.toArray(new Button[arrayListTmp.size()]);
        YearChooser.LayoutParams listLayoutParms = new MonthChooser.LayoutParams(YearChooser.LayoutParams.MATCH_PARENT, YearChooser.LayoutParams.WRAP_CONTENT);
        addView(yearsList, listLayoutParms);

    }

    public void onClick(View v)
    {
        Integer tag = (Integer)v.getTag();
        if (this.onYearChooseListener != null)
            this.onYearChooseListener.yearSelected(tag);
    }

    public void setYearChooseListener(OnYearChooseListener OnYearChooseListener)
    {
        this.onYearChooseListener = OnYearChooseListener;
    }


    public static abstract interface OnYearChooseListener
    {
        public abstract void yearSelected(int paramInt);
    }
}
