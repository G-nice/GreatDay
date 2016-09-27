package com.gnice.greatday.util;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gnice.greatday.R;

import java.util.Calendar;
import java.util.List;


public class ItemAdapterStory extends BaseAdapter {


    private static final int EMPTY = 0;
    private static final int FILLED = 1;

    private Context context;
    private List<DiaryItem> data;

    private int firstSunday = 1;


    // 构造函数
    // 提供数据以及绘制月份年份
    public ItemAdapterStory(Context context, List<DiaryItem> data, Calendar calendar) {
        super();
        this.context = context;
        this.data = data;
        firstSunday = getFirstSunday(calendar);
    }

    // 判断当日是否有内容
    @Override
    public int getItemViewType(int position) {
        int result;
        if (data.get(position).getContent().isEmpty()) {
            result = EMPTY;
        } else {
            result = FILLED;
        }
        //        Log.i("judge type", "" + result);
        return result;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //创建两种不同种类的viewHolder变量
        ViewHolder_filled filled = null;
        //        ViewHolder_empty empty = null;

        //根据position获得View的type
        int type = getItemViewType(position);

        if (convertView == null) {
            //实例化
            //            empty = new ViewHolder_empty();
            filled = new ViewHolder_filled();

            //根据不同的type 来inflate不同的item layout
            //然后设置不同的tag
            //这里的tag设置是用的资源ID作为Key

            switch (type) {
                //                case EMPTY:
                //                    convertView = View.inflate(context, R.layout.empty_item, null);
                //                    empty.dot = (ImageView) convertView.findViewById(R.id.empty_item_dot);
                //                    convertView.setTag(R.id.tag_empty, empty);
                //                    break;
                case FILLED:
                    convertView = View.inflate(context, R.layout.list_item_story, null);
                    filled.content = (TextView) convertView.findViewById(R.id.item_story);
                    convertView.setTag(R.id.tag_filled, filled);
                    break;
            }
        } else {
            //根据不同的type来获得tag
            switch (type) {
                //                case EMPTY:
                //                    empty = (ViewHolder_empty) convertView.getTag(R.id.tag_empty);
                //                    //                    convertView.setTag(R.id.tag_empty, empty);
                //                    break;
                case FILLED:
                    filled = (ViewHolder_filled) convertView.getTag(R.id.tag_filled);
                    //                    convertView.setTag(R.id.tag_filled, filled);
                    break;
            }
        }

        //        HashMap<String, String> map = (HashMap<String, String>) data.get(position);
        //根据不同的type设置数据  填充
        switch (type) {
            //            case EMPTY:
            //                if ((position + 1 - firstSunday) % 7 == 0) {
            //                    empty.dot.setImageResource(R.drawable.button_add_dot_red);
            //                    //                    Log.i("red dot, position: ", "" + position);
            //                } else
            //                    empty.dot.setImageResource(R.drawable.button_add_dot);
            //                break;

            case FILLED:
                //                filled.weekday.setText(data.get(position).getWeekDayStr());
                //                filled.date.setText(String.valueOf(data.get(position).getDate()));
                StringBuilder str = new StringBuilder();

                str.append("<b>" + data.get(position).getDate() + "</b>");
                str.append(" ");
                if ((data.get(position).getDate() - firstSunday) % 7 == 0) {
                    str.append("<b><font color='#a03f3f'>" + data.get(position).getWeekDayFullStr() + "</font></b>");

//                    <font color='#FF0000'>红颜色</font>
                } else {
                    str.append("<b>" + data.get(position).getWeekDayFullStr() + "</b>");
                }

                str.append(" / ");
                str.append(data.get(position).getContent());

                //                filled.content.setText(data.get(position).getContent());
//                filled.content.setText(str.toString());
                filled.content.setText(Html.fromHtml(str.toString()));

                // TODO: 2016/9/27 set color

                // convertView 会进行重用 必须重绘所有元素
                //                if ((position + 1 - firstSunday) % 7 == 0) {
                //                    filled.date.setTextColor(Color.parseColor("#a84545"));
                //                    //                    Log.i("set date red position: ", "" + position);
                //                } else
                //                    filled.date.setTextColor(Color.parseColor("#000000"));


                break;

        }

        //        Log.i("position", "" + position);

        return convertView;
    }

    //    @Override
    public void notifyDataSetChanged(Calendar calendar) {
        firstSunday = getFirstSunday(calendar);
        super.notifyDataSetChanged();
    }

    public void notifyDataSetChanged(int year, int monthIndex) {
        firstSunday = getFirstSunday(year, monthIndex);
        super.notifyDataSetChanged();
    }

    private static class ViewHolder_filled {
        //        TextView weekday;
        //        TextView date;
        TextView content;
    }

    //    private static class ViewHolder_empty {
    //        ImageView dot;
    //    }

    private int getFirstSunday(Calendar calendar) {
        Calendar cal = calendar;
        cal.set(Calendar.DAY_OF_MONTH, 1);
        while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY)
            cal.add(Calendar.DATE, 1);
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    private int getFirstSunday(int year, int monthIndex) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, monthIndex);

        return getFirstSunday(cal);
    }

}

