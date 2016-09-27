package com.gnice.greatday;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.gnice.greatday.database.DatabaseHelper;
import com.gnice.greatday.database.DatabaseManager;
import com.gnice.greatday.util.Constant;
import com.gnice.greatday.util.DiaryItem;
import com.gnice.greatday.util.ItemAdapter;
import com.gnice.greatday.util.ItemAdapterStory;
import com.gnice.greatday.util.MonthChooser;
import com.gnice.greatday.util.UITool;
import com.gnice.greatday.util.YearChooser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends Activity {

    public static DatabaseManager databaseManager;

    private final int LIST_STYLE_BOX = 1;
    private final int LIST_STYLE_STORY = 2;
    private int showStyle = LIST_STYLE_BOX;

    private int year = 2016;
    private int monthIndex = 8;


    // 纯数据  从数据库读取  非空 一个月数据
    //    private static HashMap<String, String> storedItems = new HashMap<>();
    // 为itemAdapter提供一个月的数据  含有空DiaryItem
    public static ArrayList<DiaryItem> listItems = new ArrayList<>();

    private ListView diaryList;
    private RelativeLayout toolbar;
    private RelativeLayout first_page;

    private Button selectYear;
    private Button selectMonth;
    private ImageButton addoday;
    private ImageButton toggleListStyle;
    private MonthChooser monthChooser;
    private YearChooser yearChooser;
    private View coverView;

    private ItemAdapter itemAdapter;
    private ItemAdapterStory itemAdapterStory;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseManager = new DatabaseManager(getApplicationContext());


        diaryList = (ListView) findViewById(R.id.main_list);
        toolbar = (RelativeLayout) findViewById(R.id.toolbar);
        first_page = (RelativeLayout) findViewById(R.id.first_page);
        selectMonth = (Button) findViewById(R.id.choose_month);
        selectYear = (Button) findViewById(R.id.choose_yrar);
        addoday = (ImageButton) findViewById(R.id.add_today);
        toggleListStyle = (ImageButton) findViewById(R.id.toggleListStyle);


        Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        monthIndex = cal.get(Calendar.MONTH);

        selectYear.setText(String.valueOf(year));
        selectMonth.setText(Constant.monthString[monthIndex]);

        init_data(year, monthIndex);

        itemAdapter = new ItemAdapter(this, listItems, Calendar.getInstance());
        itemAdapterStory = new ItemAdapterStory(this, listItems, Calendar.getInstance());
        diaryList.setAdapter(itemAdapter);

        diaryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //点击后在标题上显示点击了第几行
                //                     setTitle("你点击了第"+position+"行");
                //                Toast.makeText(getApplicationContext(), "你点击了第"+position+"行", Toast.LENGTH_SHORT).show();

                //                month = monthIndex + 1;
                //                StringBuilder stringbuffer = new StringBuilder();
                //                stringbuffer.append(year).append("-").append(month).append("-").append(position + 1);

                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                //                intent.putExtra("date", stringbuffer.toString());

                Log.i("listItems size", "" + listItems.size());
                intent.putExtra("date", String.valueOf(position + 1));
                //                if (showStyle == LIST_STYLE_BOX)
                //                    intent.putExtra("date", String.valueOf(position + 1));
                //                else
                //                    intent.putExtra("date", String.valueOf(listItems.get(position).getDate()));

                startActivity(intent);
            }
        });

        selectMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showParentCover();
                monthChooser = new MonthChooser(getApplicationContext(), year);
                monthChooser.setSelectedMonth(monthIndex);

                monthChooser.setMonthChooseListener(new MonthChooser.OnMonthChooseListener() {
                    public void monthSelected(int month) {
                        monthIndex = month;
                        if (showStyle == LIST_STYLE_STORY) {
                            loadListDataStory(year, monthIndex);
                        } else if (showStyle == LIST_STYLE_BOX) {
                            loadListData(year, monthIndex);
                        }
                        //                        loadListData(year, monthIndex);

                        //                        itemAdapter.notifyDataSetChanged(year, monthIndex);
                        selectMonth.setText(Constant.monthString[monthIndex]);
                        hideParentCover(coverView);
                    }
                });

                RelativeLayout.LayoutParams listLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);  // 设置HorizontalScrollView的宽及高  但是高设置不了

                listLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                listLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

                int marginPX = UITool.px2dip(getApplicationContext(), 64);
                listLayoutParams.setMargins(marginPX, 0, marginPX, marginPX);
                listLayoutParams.setMargins(0, 0, 0, 0);

                first_page.addView(monthChooser, listLayoutParams);
                // TODO: 2016/9/24 add
                //                monthChooser.smoothScrollBy();
                //                monthChooser.scrollBy(200, 0);
                //                toolbar.addView(monthChooser);

                AlphaAnimation localAlphaAnimation = new AlphaAnimation(0.0F, 1.0F);
                localAlphaAnimation.setDuration(350L);
                monthChooser.startAnimation(localAlphaAnimation);
            }
        });

        // // TODO: 2016/9/24 add
        diaryList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("LongClick", "long click diary list item date" + (position + 1));
                return true;
            }
        });

        // TODO: 2016/9/24 add
        selectYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showParentCover();
                yearChooser = new YearChooser(getApplicationContext(), year);

                yearChooser.setYearChooseListener(new YearChooser.OnYearChooseListener() {
                    public void yearSelected(int yearGet) {
                        year = yearGet;
                        if (yearGet == Constant.getCurrentYear()) {
                            monthIndex = Constant.getCurrentMonthIndex();
                        }
                        if (showStyle == LIST_STYLE_STORY) {
                            loadListDataStory(year, monthIndex);
                        } else if (showStyle == LIST_STYLE_BOX) {
                            loadListData(year, monthIndex);
                        }
                        //                        loadListData(year, monthIndex);
                        //                        itemAdapter.notifyDataSetChanged(year, monthIndex);
                        selectYear.setText(String.valueOf(yearGet));

                        hideParentCover(coverView);
                    }
                });

                RelativeLayout.LayoutParams listLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);  // 设置HorizontalScrollView的宽及高  但是高设置不了

                listLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                listLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

                int marginPX = UITool.px2dip(getApplicationContext(), 64);
                listLayoutParams.setMargins(marginPX, 0, marginPX, marginPX);
                listLayoutParams.setMargins(0, 0, 0, 0);

                first_page.addView(yearChooser, listLayoutParams);
                // TODO: 2016/9/24 add
                //                monthChooser.smoothScrollBy();
                //                monthChooser.scrollBy(200, 0);
                //                toolbar.addView(monthChooser);

                AlphaAnimation localAlphaAnimation = new AlphaAnimation(0.0F, 1.0F);
                localAlphaAnimation.setDuration(350L);
                yearChooser.startAnimation(localAlphaAnimation);

                // TODO: 2016/9/27 add
                //                yearChooser.smoothScrollBy(20, 0);
            }
        });

        addoday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date());
                if (monthIndex != (int) cal.get(Calendar.MONTH) || year != (int) cal.get(Calendar.YEAR)) {
                    monthIndex = cal.get(Calendar.MONTH);
                    year = cal.get(Calendar.YEAR);

                    if (showStyle == LIST_STYLE_STORY) {
                        loadListDataStory(year, monthIndex);
                    } else if (showStyle == LIST_STYLE_BOX) {
                        loadListData(year, monthIndex);
                    }
                    //                    loadListData(year, monthIndex);
                    //                    diaryList.deferNotifyDataSetChanged();
                    selectMonth.setText(Constant.monthString[monthIndex]);
                    selectYear.setText(String.valueOf(year));
                }


                //                Log.i("add today", "" + month);

                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                if (showStyle == LIST_STYLE_STORY) {
                    int index = 0;

                    String str = simpleDateFormat.format(cal.getTime());
                    Log.i("datestr", str);
                    for (DiaryItem di : listItems) {
                        if (di.getDateFullStr().equals(str)) {
                            break;
                        }
                        index++;
                    }
                    intent.putExtra("date", String.valueOf(index + 1));
                }
                else if (showStyle == LIST_STYLE_BOX) {
                    intent.putExtra("date", String.valueOf(cal.get(Calendar.DATE)));
                }

                startActivity(intent);
            }
        });


        toggleListStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Change View", "Change View");
                //                diaryList.removeAllViews();

                if (showStyle == LIST_STYLE_BOX) {
                    loadListDataStory(year, monthIndex);
                    diaryList.setAdapter(itemAdapterStory);
                    itemAdapterStory.notifyDataSetChanged(year, monthIndex);
                    showStyle = LIST_STYLE_STORY;
                } else if (showStyle == LIST_STYLE_STORY) {
                    loadListData(year, monthIndex);
                    diaryList.setAdapter(itemAdapter);
                    itemAdapter.notifyDataSetChanged(year, monthIndex);
                    showStyle = LIST_STYLE_BOX;
                }

            }
        });

    }

    @Override
    public void onBackPressed() {
        if (this.coverView != null)
            hideParentCover(this.coverView);
        else
            super.onBackPressed();
    }

    @Override
    protected void onResume() {
        if (showStyle == LIST_STYLE_STORY) {
            loadListDataStory(year, monthIndex);
            //            itemAdapterStory.notifyDataSetChanged(year, monthIndex);
        } else if (showStyle == LIST_STYLE_BOX) {
            loadListData(year, monthIndex);
            //            itemAdapter.notifyDataSetChanged(year, monthIndex);
        }
        //        itemAdapter.notifyDataSetChanged(year, monthIndex);
        //        Log.i("onResume", "Resume");
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        databaseManager.close();
        Log.i("onDestory", "OnDestory");
        // TODO: 2016/9/26 add
        super.onDestroy();
        //应用的最后一个Activity关闭时应释放DB
        //        mgr.closeDB();
    }

    //    @Override
    //    protected void onStop() {
    //        super.onStop();
    //
    //    }

    //


    //


    private void init_data(int year, int monthIndex) {
        Cursor cursor = databaseManager.getAMonth(year, monthIndex);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constant.dateFormat);
        Date date = new Date();

        if (!listItems.isEmpty()) {
            listItems.clear();
        }

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, monthIndex);

        cal.set(Calendar.DAY_OF_MONTH, 1);
        int maxday = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 1; i <= maxday; i++) {
            DiaryItem diaryItem = new DiaryItem(cal);
            listItems.add(diaryItem);
            cal.add(Calendar.DATE, 1);  // 到最后会使得加到下一个月
        }

        Log.i("cursor.getCount()=", "" + cursor.getCount());
        if (cursor.getCount() != 0 && cursor.moveToFirst()) {
            do {
                try {
                    date = simpleDateFormat.parse(cursor.getString(cursor.getColumnIndex(DatabaseHelper.DIARY_DATE)));

                } catch (ParseException pe) {
                    Log.e("Parse date", pe.getMessage());
                }

                cal.setTime(date);
                listItems.get(cal.get(Calendar.DAY_OF_MONTH) - 1).setContent(cursor.getString(cursor.getColumnIndex(DatabaseHelper.DIARY_CONTEXT)));
            } while (cursor.moveToNext());

        }
    }


    private void loadListData(int year, int monthIndex) {
        Cursor cursor = databaseManager.getAMonth(year, monthIndex);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constant.dateFormat);
        Date date = new Date();

        if (!listItems.isEmpty()) {
            listItems.clear();
        }

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, monthIndex);

        cal.set(Calendar.DAY_OF_MONTH, 1);
        int maxday = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 1; i <= maxday; i++) {
            DiaryItem diaryItem = new DiaryItem(cal);
            listItems.add(diaryItem);
            cal.add(Calendar.DATE, 1);  // 到最后会使得加到下一个月
        }

        if (cursor.getCount() != 0 && cursor.moveToFirst()) {
            do {
                try {
                    date = simpleDateFormat.parse(cursor.getString(cursor.getColumnIndex(DatabaseHelper.DIARY_DATE)));

                } catch (ParseException pe) {
                    Log.e("Parse date", pe.getMessage());
                }

                cal.setTime(date);
                listItems.get(cal.get(Calendar.DAY_OF_MONTH) - 1).setContent(cursor.getString(cursor.getColumnIndex(DatabaseHelper.DIARY_CONTEXT)));
            } while (cursor.moveToNext());

        }
        //        itemAdapter.notifyDataSetChanged(cal);
        itemAdapter.notifyDataSetChanged(year, monthIndex);
        //        if (showStyle == LIST_STYLE_STORY) {
        //            itemAdapterStory.notifyDataSetChanged(year, monthIndex);
        //        }
        //        else if (showStyle == LIST_STYLE_BOX) {
        //            itemAdapter.notifyDataSetChanged(year, monthIndex);
        //        }
    }

    private void loadListDataStory(int year, int monthIndex) {
        Cursor cursor = databaseManager.getAMonth(year, monthIndex);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constant.dateFormat);
        Date date = new Date();

        if (!listItems.isEmpty()) {
            listItems.clear();
        }

        if (cursor.getCount() != 0 && cursor.moveToFirst()) {
            do {
                DiaryItem diaryItem = new DiaryItem(cursor.getString(cursor.getColumnIndex(DatabaseHelper.DIARY_DATE)), cursor.getString(cursor.getColumnIndex(DatabaseHelper.DIARY_CONTEXT)));
                listItems.add(diaryItem);
            } while (cursor.moveToNext());
        }
        //        itemAdapter.notifyDataSetChanged(cal);
        itemAdapterStory.notifyDataSetChanged(year, monthIndex);
        //        if (showStyle == LIST_STYLE_STORY) {
        //            itemAdapterStory.notifyDataSetChanged(year, monthIndex);
        //        }
        //        else if (showStyle == LIST_STYLE_BOX) {
        //            itemAdapter.notifyDataSetChanged(year, monthIndex);
        //        }
    }

    private void showParentCover() {
        coverView = new View(getApplicationContext());
        //        coverView = new View(toolbar.getContext());
        coverView.setClickable(true);
        coverView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                hideParentCover(v);
            }
        });
        coverView.setTag("cover");
        //        Color.parseColor("#6A0400FF")
        //        coverView.setBackground(getDrawable(R.color.show_frame));   // API 21
        //        coverView.setBackgroundColor(Color.parseColor("#6A0400FF"));
        ((ViewGroup) toolbar.getParent()).addView(coverView);
    }

    private boolean hideParentCover(final View paramView) {
        ((ViewGroup) toolbar.getParent()).removeView(paramView);  // 移除cover

        if (monthChooser != null) {
            first_page.removeView(this.monthChooser);
        }
        if (yearChooser != null) {
            first_page.removeView(this.yearChooser);
        }

        //        toolbar.removeView(this.monthChooser);
        monthChooser = null;
        yearChooser = null;
        coverView = null;

        //        if (monthChooser != null);
        //        {
        //            AlphaAnimation localAlphaAnimation = new AlphaAnimation(1.0F, 0.0F);
        //            localAlphaAnimation.setDuration(300L);
        //            localAlphaAnimation.setAnimationListener(new Animation.AnimationListener()
        //            {
        //                public void onAnimationEnd(Animation paramAnonymousAnimation) {
        //                    first_page.removeView(monthChooser);
        //                    monthChooser = null;
        ////                    yearChooser = null;
        //                    coverView = null;
        //                }
        //                public void onAnimationRepeat(Animation paramAnonymousAnimation) {
        //                }
        //
        //                public void onAnimationStart(Animation paramAnonymousAnimation) {
        //                }
        //            });
        //            paramView.startAnimation(localAlphaAnimation);
        //        }
        return true;
    }
}

//for(int i = 1;i <= maxday; i++) {
//        DiaryItem diaryItem = new DiaryItem(cal);
//        if(random.nextBoolean())
//        diaryItem.setContent(String.valueOf(i * 101));
//
//        else
//        diaryItem.setContent("");
//
//        listItems.add(diaryItem);  // important
//        cal.add(Calendar.DATE, 1);
//        }

//    private void init_data2() {
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(new Date());
//
//        monthIndex = cal.get(Calendar.MONTH);
//        year = cal.get(Calendar.YEAR);
//        //        month = monthIndex +1;
//
//        cal.set(Calendar.DAY_OF_MONTH, 1);
//        Random random = new Random();
//        int maxday = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
//        //        Log.i("maxday", ""+maxday);
//
//        if (!listItems.isEmpty()) {
//            listItems.clear();
//        }
//        if (!storedItems.isEmpty()) {
//            storedItems.clear();
//        }
//
//
//        // 构造从数据库读出的数据
//        for (int i = 1; i <= maxday; i++) {
//            if (random.nextBoolean()) {
//                storedItems.put(simpleDateFormat.format(cal.getTime()), String.valueOf(i * 101));
//                //                Log.i("DATE STR", simpleDateFormat.format((cal.getTime())));
//            }
//            cal.add(Calendar.DATE, 1);
//        }
//
//        cal.set(Calendar.MONTH, monthIndex);  // 前面的add将cal加到了10月
//        cal.set(Calendar.DAY_OF_MONTH, 1);
//        for (int i = 1; i <= maxday; i++) {
//            DiaryItem diaryItem = new DiaryItem(cal);
//            listItems.add(diaryItem);
//            cal.add(Calendar.DATE, 1);
//        }
//
//        Calendar caltmp = Calendar.getInstance();
//        Iterator<Map.Entry<String, String>> iter = storedItems.entrySet().iterator();
//        while (iter.hasNext()) {
//            Map.Entry<String, String> entry = iter.next();
//
//            try {
//                Date date = simpleDateFormat.parse(entry.getKey());
//                caltmp.setTime(date);
//            } catch (ParseException pe) {
//                Log.e("Parse date", pe.getMessage());
//            }
//
//            listItems.get(caltmp.get(Calendar.DAY_OF_MONTH) - 1).setContent(entry.getValue());
//        }
//    }

//        private void loadListData2(int year, int month) {
//        Calendar cal = Calendar.getInstance();
//        cal.set(Calendar.YEAR, year);
//        cal.set(Calendar.MONTH, month);
//
//
//        if (!listItems.isEmpty()) {
//            listItems.clear();
//        }
//        if (!storedItems.isEmpty()) {
//            storedItems.clear();
//        }
//
//        cal.set(Calendar.DAY_OF_MONTH, 1);
//        Random random = new Random();
//        int maxday = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
//        // 模拟构造从数据库读出的数据
//        for (int i = 1; i <= maxday; i++) {
//            if (random.nextBoolean()) {
//                storedItems.put(simpleDateFormat.format(cal.getTime()), String.valueOf(i * 101));
//            }
//            cal.add(Calendar.DATE, 1);
//        }
//
//        cal.set(Calendar.MONTH, monthIndex);  // 前面的add将cal加到了下个月
//        cal.set(Calendar.DAY_OF_MONTH, 1);
//        for (int i = 1; i <= maxday; i++) {
//            DiaryItem diaryItem = new DiaryItem(cal);
//            listItems.add(diaryItem);
//            cal.add(Calendar.DATE, 1);
//        }
//
//        Calendar caltmp = Calendar.getInstance();
//        Iterator<Map.Entry<String, String>> iter = storedItems.entrySet().iterator();
//        while (iter.hasNext()) {
//            Map.Entry<String, String> entry = iter.next();
//
//            try {
//                Date date = simpleDateFormat.parse(entry.getKey());
//                caltmp.setTime(date);
//            } catch (ParseException pe) {
//                Log.e("Parse date", pe.getMessage());
//            }
//            listItems.get(caltmp.get(Calendar.DAY_OF_MONTH) - 1).setContent(entry.getValue());
//        }
//        itemAdapter.notifyDataSetChanged(cal);
//    }