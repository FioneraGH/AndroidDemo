package com.fionera.demo.util;

import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.ViewDragHelper;

import java.lang.reflect.Field;

/**
 * Created by fionera on 16-2-14.
 */
public class DrawerLayoutHelper {

    public static void setDrawerLeftEdgeSize(DrawerLayout drawerLayout, float dp) {
        if (drawerLayout == null) {
            return;
        }
        try {
            Field leftDragField = drawerLayout.getClass().getDeclaredField("mLeftDragger");
            leftDragField.setAccessible(true);
            ViewDragHelper leftDragger = (ViewDragHelper) leftDragField.get(drawerLayout);
            Field edgeSizeField = leftDragger.getClass().getDeclaredField("mEdgeSize");
            edgeSizeField.setAccessible(true);
            int edgeSize = edgeSizeField.getInt(leftDragger);
            edgeSizeField.setInt(leftDragger, Math.max(edgeSize, DisplayUtils.dp2px(dp)));
        } catch (NoSuchFieldException e) {
            // ignore
        } catch (IllegalArgumentException e) {
            // ignore
        } catch (IllegalAccessException e) {
            // ignore
        }
    }
}
