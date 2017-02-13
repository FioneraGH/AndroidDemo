package com.fionera.demo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.fionera.base.activity.BaseActivity;
import com.fionera.demo.R;
import com.fionera.demo.view.SeatTableLite;

/**
 * SeatTableActivity
 * Created by fionera on 17-2-13 in AndroidDemo.
 */

public class SeatTableActivity
        extends BaseActivity {

    private SeatTableLite stTest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_table);
        initView();

        stTest.setMaxSelected(3);
        stTest.setSeatChecker(new SeatTableLite.SeatChecker() {
            @Override
            public boolean isValidSeat(int row, int column) {
                return true;
            }

            @Override
            public boolean isSold(int row, int column) {
                return column == 3;
            }

            @Override
            public void checked(int row, int column) {

            }

            @Override
            public void unCheck(int row, int column) {

            }

            @Override
            public String[] checkedSeatTxt(int row, int column) {
                return null;
            }
        });

        stTest.setData(80, 80);
    }

    private void initView() {
        stTest = (SeatTableLite) findViewById(R.id.st_test);
    }
}
