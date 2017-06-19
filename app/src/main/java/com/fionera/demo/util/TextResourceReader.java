package com.fionera.demo.util;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * TextResourceReader
 * Created by fionera on 17-6-19 in AndroidDemo.
 */

public class TextResourceReader {
    public static String readTextFileFromResource(Context context, int resId) throws IOException {
        StringBuilder body = new StringBuilder();
        InputStream inputStream = context.getResources().openRawResource(resId);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String nextLine;
        while ((nextLine = bufferedReader.readLine()) != null) {
            body.append(nextLine).append('\n');
        }
        return body.toString();
    }
}
