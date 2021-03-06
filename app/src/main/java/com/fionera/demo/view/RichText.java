package com.fionera.demo.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.text.style.URLSpan;
import android.util.AttributeSet;
import android.view.View;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.fionera.demo.R;
import com.fionera.multipic.common.GlideApp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

/**
 * @author fionera
 */
public class RichText
        extends AppCompatTextView {

    private static Pattern IMAGE_TAG_PATTERN = Pattern.compile("<img(.*?)>");
    private static Pattern IMAGE_WIDTH_PATTERN = Pattern.compile("width=\"(.*?)\"");
    private static Pattern IMAGE_HEIGHT_PATTERN = Pattern.compile("height=\"(.*?)\"");
    private static Pattern IMAGE_SRC_PATTERN = Pattern.compile("src=\"(.*?)\"");

    /**
     * 占位图，错误图
     */
    private Drawable placeHolder, errorImage;
    /**
     * 图片点击回调
      */
    private OnImageClickListener onImageClickListener;
    /**
     * 超链接 点击回调
      */
    private OnUrlClickListener onUrlClickListener;
    private HashMap<String, ImageHolder> mImages = new HashMap<>();
    private ImageFixListener mImageFixListener;
    private int dW = 200;
    private int dH = 200;

    public RichText(Context context) {
        this(context, null);
    }

    public RichText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RichText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RichText);
        placeHolder = typedArray.getDrawable(R.styleable.RichText_place_holder);
        errorImage = typedArray.getDrawable(R.styleable.RichText_error_image);

        dW = typedArray.getDimensionPixelSize(R.styleable.RichText_default_width, dW);
        dH = typedArray.getDimensionPixelSize(R.styleable.RichText_default_height, dH);

        if (placeHolder == null) {
            placeHolder = new ColorDrawable(Color.BLUE);
        }
        placeHolder.setBounds(0, 0, dW, dH);
        if (errorImage == null) {
            errorImage = new ColorDrawable(Color.BLUE);
        }
        errorImage.setBounds(0, 0, dW, dH);
        typedArray.recycle();
    }

    /**
     * 设置富文本
     *
     * @param text 富文本
     */
    public void setRichText(String text) {
        matchImages(text);

        Spanned spanned = Html.fromHtml(text, asyncImageGetter, null);
        SpannableStringBuilder spannableStringBuilder;
        if (spanned instanceof SpannableStringBuilder) {
            spannableStringBuilder = (SpannableStringBuilder) spanned;
        } else {
            spannableStringBuilder = new SpannableStringBuilder(spanned);
        }

        // 处理图片得点击事件
        ImageSpan[] imageSpans = spannableStringBuilder
                .getSpans(0, spannableStringBuilder.length(), ImageSpan.class);
        final List<String> imageUrls = new ArrayList<>();

        for (int i = 0, size = imageSpans == null ? 0 : imageSpans.length; i < size; i++) {
            ImageSpan imageSpan = imageSpans[i];
            String imageUrl = imageSpan.getSource();
            int start = spannableStringBuilder.getSpanStart(imageSpan);
            int end = spannableStringBuilder.getSpanEnd(imageSpan);
            imageUrls.add(imageUrl);

            final int finalI = i;
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
                    if (onImageClickListener != null) {
                        onImageClickListener.imageClicked(imageUrls, finalI);
                    }
                }
            };
            ClickableSpan[] clickableSpans = spannableStringBuilder
                    .getSpans(start, end, ClickableSpan.class);
            if (clickableSpans != null && clickableSpans.length != 0) {
                for (ClickableSpan cs : clickableSpans) {
                    spannableStringBuilder.removeSpan(cs);
                }
            }
            spannableStringBuilder
                    .setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        // 处理超链接点击事件
        URLSpan[] urlSpans = spannableStringBuilder
                .getSpans(0, spannableStringBuilder.length(), URLSpan.class);

        for (int i = 0, size = urlSpans == null ? 0 : urlSpans.length; i < size; i++) {
            URLSpan urlSpan = urlSpans[i];

            int start = spannableStringBuilder.getSpanStart(urlSpan);
            int end = spannableStringBuilder.getSpanEnd(urlSpan);

            spannableStringBuilder.removeSpan(urlSpan);
            spannableStringBuilder
                    .setSpan(new CallableUrlSpan(urlSpan.getURL(), onUrlClickListener), start, end,
                             Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        super.setText(spanned);
        setMovementMethod(LinkMovementMethod.getInstance());
    }

    /**
     * 从文本中拿到<img/>标签,并获取图片url和宽高
     */
    private void matchImages(String text) {
        mImages.clear();
        ImageHolder holder;
        Matcher imageMatcher, srcMatcher, widthMatcher, heightMatcher;
        imageMatcher = IMAGE_TAG_PATTERN.matcher(text);
        while (imageMatcher.find()) {
            String image = imageMatcher.group().trim();
            srcMatcher = IMAGE_SRC_PATTERN.matcher(image);
            String src = null;
            if (srcMatcher.find()) {
                src = getTextBetweenQuotation(srcMatcher.group().trim().substring(4));
            }
            if (TextUtils.isEmpty(src)) {
                continue;
            }
            holder = new ImageHolder(src);

            widthMatcher = IMAGE_WIDTH_PATTERN.matcher(image);
            if (widthMatcher.find()) {
                holder.width = parseStringToInteger(
                        getTextBetweenQuotation(widthMatcher.group().trim().substring(6)));
            }

            heightMatcher = IMAGE_HEIGHT_PATTERN.matcher(image);
            if (heightMatcher.find()) {
                holder.height = parseStringToInteger(
                        getTextBetweenQuotation(heightMatcher.group().trim().substring(6)));
            }

            mImages.put(holder.src, holder);
        }
    }

    private int parseStringToInteger(String integerStr) {
        int result = -1;
        if (!TextUtils.isEmpty(integerStr)) {
            try {
                result = Integer.parseInt(integerStr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private static final Pattern POINT_PATTERN = Pattern.compile("\"(.*?)\"");

    /**
     *  从双引号之间取出字符串
     */
    @Nullable
    private static String getTextBetweenQuotation(String text) {
        Matcher matcher = POINT_PATTERN.matcher(text);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private Html.ImageGetter asyncImageGetter = new Html.ImageGetter() {
        @Override
        public Drawable getDrawable(String source) {
            final UrlDrawable urlDrawable = new UrlDrawable(getResources(), null);
            ImageHolder holder = mImages.get(source);
            if (mImageFixListener != null && holder != null) {
                mImageFixListener.onFix(holder);
                if (holder.width != -1 && holder.height != -1) {
                    GlideApp.with(getContext()).asBitmap().load(source).override(holder.width,
                            holder.height).placeholder(placeHolder).error(errorImage).into(
                            new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource,
                                                            Transition<? super Bitmap> transition) {
                                    Drawable drawable = new BitmapDrawable(getResources(),
                                            resource);
                                    drawable.setBounds(0, 0, resource.getWidth(),
                                            resource.getHeight());
                                    urlDrawable.setBounds(0, 0, resource.getWidth(),
                                            resource.getHeight());
                                    urlDrawable.setDrawable(drawable);
                                    RichText.this.setText(getText());
                                }
                            });
                }
            }
            return urlDrawable;
        }
    };

    private static final class UrlDrawable
            extends BitmapDrawable {
        private Drawable drawable;

        UrlDrawable(Resources res, Bitmap bitmap) {
            super(res, bitmap);
        }

        @Override
        public void draw(Canvas canvas) {
            if (drawable != null) {
                drawable.draw(canvas);
            }
        }

        public void setDrawable(Drawable drawable) {
            this.drawable = drawable;
        }
    }

    private static class CallableUrlSpan
            extends URLSpan {

        private OnUrlClickListener onUrlClickListener;

        CallableUrlSpan(String url, OnUrlClickListener onUrlClickListener) {
            super(url);
            this.onUrlClickListener = onUrlClickListener;
        }

        @Override
        public void onClick(View widget) {
            if (onUrlClickListener != null && onUrlClickListener.urlClicked(getURL())) {
                return;
            }
            super.onClick(widget);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(@NonNull Parcel dest, int flags) {
        }

        CallableUrlSpan(Parcel in) {
            super(in);
        }

        public static final Creator<CallableUrlSpan> CREATOR = new Creator<CallableUrlSpan>() {
            @Override
            public CallableUrlSpan createFromParcel(Parcel source) {
                return new CallableUrlSpan(source);
            }

            @Override
            public CallableUrlSpan[] newArray(int size) {
                return new CallableUrlSpan[size];
            }
        };
    }

    public static class ImageHolder {
        public static final int DEFAULT = 0;
        public static final int CENTER_CROP = 1;
        public static final int CENTER_INSIDE = 2;

        @IntDef({DEFAULT, CENTER_CROP, CENTER_INSIDE})
        @interface ScaleType {
        }

        private final String src;
        private int width = -1, height = -1;
        private int scaleType = DEFAULT;

        ImageHolder(String src) {
            this.src = src;
        }

        public int getHeight() {
            return height;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        @SuppressWarnings("unused")
        @ScaleType
        public int getScaleType() {
            return scaleType;
        }

        public void setScaleType(@ScaleType int scaleType) {
            this.scaleType = scaleType;
        }
    }

    @SuppressWarnings("unused")
    public ImageHolder getImageHolder(String url) {
        return mImages.get(url);
    }

    @SuppressWarnings("unused")
    public void setPlaceHolder(Drawable placeHolder) {
        this.placeHolder = placeHolder;
        this.placeHolder.setBounds(0, 0, dW, dH);
    }

    @SuppressWarnings("unused")
    public void setErrorImage(Drawable errorImage) {
        this.errorImage = errorImage;
        this.errorImage.setBounds(0, 0, dW, dH);
    }

    public void setImageFixListener(ImageFixListener mImageFixListener) {
        this.mImageFixListener = mImageFixListener;
    }

    public void setOnUrlClickListener(OnUrlClickListener onUrlClickListener) {
        this.onUrlClickListener = onUrlClickListener;
    }

    public void setOnImageClickListener(OnImageClickListener onImageClickListener) {
        this.onImageClickListener = onImageClickListener;
    }

    public interface ImageFixListener {
        /**
         * 修复图片尺寸的方法
         *
         * @param holder ImageHolder对象
         */
        void onFix(ImageHolder holder);
    }

    public interface OnUrlClickListener {

        /**
         * 超链接点击得回调方法
         *
         * @param url 点击得url
         * @return true：已处理，false：未处理（会进行默认处理）
         */
        boolean urlClicked(String url);
    }

    public interface OnImageClickListener {
        /**
         * 图片被点击后的回调方法
         *
         * @param imageUrls 本篇富文本内容里的全部图片
         * @param position  点击处图片在imageUrls中的位置
         */
        void imageClicked(List<String> imageUrls, int position);
    }
}

