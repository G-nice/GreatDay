package com.gnice.greatday;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.gnice.greatday.util.AndroidBug5497Workaround;
import com.gnice.greatday.util.DiaryItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EditorActivity extends AppCompatActivity {

    private final String[] weekdays = {"--", "SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY"};
    private final String[] monthString = { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" };

    private EditText editor;
    private TextView weekday_title;
    private TextView rest_title;

    private String dateStr;
//    private String str;
    private Calendar cal = Calendar.getInstance();

    private DiaryItem diaryItem;
//    private String saveContent;

    private ImageButton addtime;
    private ImageButton done;

    private int diaryitemIndex = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editor);
        AndroidBug5497Workaround.assistActivity(this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        editor = (EditText) findViewById(R.id.editor_edittext);
        weekday_title = (TextView) findViewById(R.id.weekday_title);
        rest_title = (TextView) findViewById(R.id.rest_title);
        addtime = (ImageButton) findViewById(R.id.add_time);
        done = (ImageButton) findViewById(R.id.done_edit);

        Intent intent = getIntent();

        diaryitemIndex = Integer.parseInt(intent.getStringExtra("date")) - 1;
        // 应用赋值  没有对象复制
        Log.i("diaryitemIndex: ", "" + diaryitemIndex);
        diaryItem = MainActivity.listItems.get(diaryitemIndex);
        dateStr = String.valueOf(diaryItem.getDate());


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = simpleDateFormat.parse(diaryItem.getDateFullStr());
//            Log.i("editor date", diaryItem.getDateFullStr());
            cal.setTime(date);
        } catch (ParseException pe) {
            Log.e("Parse date", pe.getMessage());
        }

        int week = cal.get(Calendar.DAY_OF_WEEK);
        String weekday = weekdays[week];
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" / ").append(monthString[cal.get(Calendar.MONTH)]).append(" ").append(cal.
                get(Calendar.DAY_OF_MONTH)).append(" ").append(" / ").append(cal.get(Calendar.YEAR));

        weekday_title.setText(weekday);
        if (week == 1) {
            weekday_title.setTextColor(Color.parseColor("#a84545"));
        }
        rest_title.setText(stringBuilder.toString());

        editor.setText(diaryItem.getContent());


        addtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appendTime();
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();

//        saveContent = editor.getText().toString();

        // 内容发生更改
//        if (!saveContent.equals(diaryItem.getContent())) {
        if (!diaryItem.getContent().equals(editor.getText().toString())) {
            // 新增
            if (diaryItem.getContent().isEmpty()) {
                diaryItem.setContent(editor.getText().toString());
                MainActivity.databaseManager.add(diaryItem);
                Log.i("editor add", "editor add");
            }
            // 清空
            else if (editor.getText().toString().isEmpty()) {
                diaryItem.setContent(editor.getText().toString());
                MainActivity.databaseManager.delete(diaryItem);
                Log.i("editor delete", "editor delete");
            }
            // 修改
            else {
                diaryItem.setContent(editor.getText().toString());
                MainActivity.databaseManager.update(diaryItem);
                Log.i("editor update", "editor modify");
            }

//            diaryItem.setContent(editor.getText().toString());
            //            MainActivity.listItems.get(Integer.parseInt(dateStr) - 1).setContent(editor.getText().toString());
        }





//        Log.i("content", MainActivity.listItems.get(Integer.parseInt(dateStr) - 1).getContent());
//        Log.i("onPause", "onPause");
//        if (editor.getText().toString().isEmpty()) {
//            Log.i("empty", "empty");
//        } else {
//            Log.i("show context", editor.getText().toString() + "23");
//        }
    }

    private void appendTime()
    {
        String str = new SimpleDateFormat("h:mma ", Locale.ENGLISH).format(new Date()).toLowerCase();
//        String str = new SimpleDateFormat("h:mma ", Locale.CHINA).format(new Date()).toLowerCase();
        int i = Math.max(this.editor.getSelectionStart(), 0);
        int j = Math.max(this.editor.getSelectionEnd(), 0);
        this.editor.getText().replace(Math.min(i, j), Math.max(i, j), str, 0, str.length());
    }
}
