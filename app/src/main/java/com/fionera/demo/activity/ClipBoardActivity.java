package com.fionera.demo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fionera.demo.R;
import com.fionera.demo.service.ListenClipboardService;

public final class ClipBoardActivity
        extends BaseActivity {
    public static final String CLIPBOARD_CONTENT = "clipboard_content";

    private Button button;

    public static void startForContent(Context context, String content) {
        Intent intent = new Intent(context, ClipBoardActivity.class);
        intent.putExtra(CLIPBOARD_CONTENT, content);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clip_board);

        button = (Button) findViewById(R.id.btn_clip_board_test);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListenClipboardService.startForTest(mContext, "test");
            }
        });

        Intent intent = getIntent();
        tryToShowContent(intent);
        ListenClipboardService.start(mContext);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        tryToShowContent(intent);
    }

    private void tryToShowContent(Intent intent) {
        String content = intent.getStringExtra(CLIPBOARD_CONTENT);
        if (!TextUtils.isEmpty(content)) {
            button.setText(content);
        }
    }
}