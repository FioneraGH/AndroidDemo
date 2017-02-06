package com.fionera.base.activity;

import android.os.Bundle;
import android.support.annotation.IntDef;
import android.view.View;
import android.widget.LinearLayout;

import com.fionera.base.R;
import com.fionera.base.widget.TitleBar;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class TitleBarActivity
        extends BaseActivity {
    @IntDef({View.VISIBLE, View.INVISIBLE, View.GONE})
    @Retention(RetentionPolicy.SOURCE)
    @interface Visibility {
    }

    public TitleBar mTitleBar;
    private LinearLayout mLayoutContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLayoutContent = (LinearLayout) View.inflate(mContext, R.layout.layout_title_bar_content,
                null);
        mTitleBar = (TitleBar) mLayoutContent.getChildAt(0);
    }

    @Override
    public View findViewById(int id) {
        View view = mLayoutContent.findViewById(id);
        if (view == null) {
            return super.findViewById(id);
        }
        return view;
    }

    @Override
    public void setContentView(int layoutResID) {
        View mContent = getLayoutInflater().inflate(layoutResID, null);
        mLayoutContent.addView(mContent, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        super.setContentView(mLayoutContent);
    }

    public void setTitleBarText(String title) {
        mTitleBar.setTitleBarText(title);
    }

    public void setTitleBarTitleClick(View.OnClickListener listener) {
        mTitleBar.setTitleBarTitleClick(listener);
    }
}
