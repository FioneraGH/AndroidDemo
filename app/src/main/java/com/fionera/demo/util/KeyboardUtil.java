package com.fionera.demo.util;

import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.Keyboard.Key;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.fionera.demo.R;

import java.lang.reflect.Method;
import java.util.List;

public class KeyboardUtil {
    private KeyboardView keyboardView;
    private Keyboard k1;// 字母键盘
    private Keyboard k2;// 数字键盘
    private boolean isNum = false;// 是否数据键盘
    private boolean isUpper = false;// 是否大写


    public KeyboardUtil(Context ctx, KeyboardView view, final EditText edit) {
        k1 = new Keyboard(ctx, R.xml.char_keyboard);
        k2 = new Keyboard(ctx, R.xml.num_keyboard);
        keyboardView = view;
        keyboardView.setKeyboard(k1);
        keyboardView.setEnabled(true);
        keyboardView.setPreviewEnabled(false);
        keyboardView.setOnKeyboardActionListener(new OnKeyboardActionListener() {
            @Override
            public void swipeUp() {
            }

            @Override
            public void swipeRight() {
            }

            @Override
            public void swipeLeft() {
            }

            @Override
            public void swipeDown() {
            }

            @Override
            public void onText(CharSequence text) {
            }

            @Override
            public void onRelease(int primaryCode) {
            }

            @Override
            public void onPress(int primaryCode) {
            }

            @Override
            public void onKey(int primaryCode, int[] keyCodes) {
                Editable editable = edit.getText();
                int start = edit.getSelectionStart();
                if (primaryCode == Keyboard.KEYCODE_CANCEL) {// 完成
                    hideKeyboard();
                } else if (primaryCode == Keyboard.KEYCODE_DELETE) {// 回退
                    if (editable != null && editable.length() > 0) {
                        if (start > 0) {
                            editable.delete(start - 1, start);
                        }
                    }
                } else if (primaryCode == Keyboard.KEYCODE_SHIFT) {// 大小写切换
                    changeKey();
                    keyboardView.setKeyboard(k1);
                } else if (primaryCode == Keyboard.KEYCODE_MODE_CHANGE) {// 数字键盘切换
                    if (isNum) {
                        isNum = false;
                        keyboardView.setKeyboard(k1);
                    } else {
                        isNum = true;
                        keyboardView.setKeyboard(k2);
                    }
                } else if (primaryCode == 57419) { // go left
                    if (start > 0) {
                        edit.setSelection(start - 1);
                    }
                } else if (primaryCode == 57421) { // go right
                    if (start < edit.length()) {
                        edit.setSelection(start + 1);
                    }
                } else {
                    editable.insert(start, Character.toString((char) primaryCode));
                }
            }
        });

    }

    /**
     * 键盘大小写切换
     */
    private void changeKey() {
        List<Key> keyList = k1.getKeys();
        if (isUpper) {//大写切换小写
            isUpper = false;
            for (Key key : keyList) {
                if (key.label != null && isWord(key.label.toString())) {
                    key.label = key.label.toString().toLowerCase();
                    key.codes[0] = key.codes[0] + 32;
                }
            }
        } else {//小写切换大写
            isUpper = true;
            for (Key key : keyList) {
                if (key.label != null && isWord(key.label.toString())) {
                    key.label = key.label.toString().toUpperCase();
                    key.codes[0] = key.codes[0] - 32;
                }
            }
        }
    }

    //EditText的点击监听事件
    public void registerEditText(final EditText editText) {
        editText.setOnClickListener(view -> {
            int visibility = keyboardView.getVisibility();
            if (visibility == View.GONE || visibility == View.INVISIBLE) {
                showKeyboard();
            } else {
                hideKeyboard();
            }
        });
        editText.setOnFocusChangeListener((view, b) -> disableShowSoftInput(editText));
    }

    //显示键盘
    private void showKeyboard() {
        int visibility = keyboardView.getVisibility();
        if (visibility == View.GONE || visibility == View.INVISIBLE) {
            keyboardView.setVisibility(View.VISIBLE);
            keyboardView.setEnabled(true);
        }
    }

    //隐藏键盘
    private void hideKeyboard() {
        int visibility = keyboardView.getVisibility();
        if (visibility == View.VISIBLE) {
            keyboardView.setVisibility(View.INVISIBLE);
        }
    }

    private boolean isWord(String str) {
        return "abcdefghijklmnopqrstuvwxyz".contains(str.toLowerCase());
    }


    /**
     * 禁止Edittext弹出软件盘，光标依然正常显示。
     */
    private void disableShowSoftInput(EditText editText) {
        Class<EditText> cls = EditText.class;
        Method method;
        try {
            method = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
            method.setAccessible(true);
            method.invoke(editText, false);
        } catch (Exception e) {
            Log.e("setShowSoftInputOnFocus", e.toString());
        }

        try {
            method = cls.getMethod("setSoftInputShownOnFocus", boolean.class);
            method.setAccessible(true);
            method.invoke(editText, false);
        } catch (Exception e) {
            Log.e("SoftInputShownOnFocus", e.toString());
        }
    }
}
