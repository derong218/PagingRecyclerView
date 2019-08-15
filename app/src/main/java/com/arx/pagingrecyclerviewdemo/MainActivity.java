package com.arx.pagingrecyclerviewdemo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.arx.pagingrecyclerview.IndicatorView;
import com.arx.pagingrecyclerview.PagingRecyclerView;

import java.util.ArrayList;
/**
 * @author Zeng Derong (derong218@gmail.com)
 * on  2019-8-3 09:46
 */
public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final PagingRecyclerView rvTest = findViewById(R.id.rv_test);
        final TextView tvPage = findViewById(R.id.textView);
        final TextView tvPageCount = findViewById(R.id.textView2);
        final Button button2 = findViewById(R.id.button2);
        final EditText et = findViewById(R.id.editText);
        final EditText et2 = findViewById(R.id.editText2);

        final TestAdapter adapter = new TestAdapter();
        rvTest.setAdapter(adapter);

        final IndicatorView indicatorView = findViewById(R.id.indicator);

        indicatorView.setupWithPagingRecyclerView(rvTest);


        rvTest.addOnPageChangeListener(new PagingRecyclerView.OnPageChangeListener() {
            @Override
            public void onPageSelected(int page) {
                tvPage.setText(page + 1 + "");
            }

            @Override
            public void onPageCount(int pageCount) {
                tvPageCount.setText("" + pageCount);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                String strStart = et.getText().toString();
                String strEnd = et2.getText().toString();

                int start = Integer.valueOf(strStart);
                int end = Integer.valueOf(strEnd);

                ArrayList<Test> list2 = new ArrayList<>();
                for (int i = start; i < end; i++) {
                    list2.add(new Test("Name:" + i));
                }
                adapter.submitList(list2);
            }
        });
        ArrayList<Test> list = new ArrayList<>();
        for (
                int i = 0;
                i < 33; i++) {
            list.add(new Test("Name:" + i));
        }
        adapter.submitList(list);

    }
}
