package com.fionera.demo.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.fionera.demo.view.TheEmptyView;


public class EmptyViewUtil {

    @NonNull
    private static TheEmptyView genSimpleEmptyView(View view) {
        TheEmptyView emptyView = new TheEmptyView(view.getContext(), null);
        ViewParent parent = view.getParent();
        if (parent instanceof ViewGroup) {
            ((ViewGroup) parent).addView(emptyView);
            if (parent instanceof LinearLayout) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) emptyView
                        .getLayoutParams();
                lp.height = -1;
                lp.gravity = Gravity.CENTER;
                emptyView.setLayoutParams(lp);
            } else if (parent instanceof RelativeLayout) {
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) emptyView
                        .getLayoutParams();
                lp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                emptyView.setLayoutParams(lp);
            } else if (parent instanceof FrameLayout) {
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) emptyView
                        .getLayoutParams();
                lp.height = -1;
                lp.gravity = Gravity.CENTER;
                emptyView.setLayoutParams(lp);
            }
        }
        return emptyView;
    }

    @SuppressWarnings("unused")
    public static final class EmptyViewBuilder {
        private Context mContext;
        private ViewGroup.LayoutParams layoutParams;
        private String emptyText;
        private int iconSrc;
        private Drawable iconDrawable;
        private int emptyTextColor = -1;
        private int emptyTextSize;
        private ShowType mShowIcon = ShowType.DEFAULT;
        private ShowType mShowText = ShowType.DEFAULT;
        private ShowType mShowButton = ShowType.DEFAULT;
        private View.OnClickListener mAction;
        private String actionText;

        private enum ShowType {
            DEFAULT,
            SHOW,
            HIDE
        }

        public static EmptyViewBuilder getInstance(Context context) {
            return new EmptyViewBuilder(context);
        }

        public void bindView(final RecyclerView recyclerView) {

            final TheEmptyView emptyView = genSimpleEmptyView(recyclerView);
            final RecyclerView.Adapter adapter = recyclerView.getAdapter();
            if (adapter != null) {
                RecyclerView.AdapterDataObserver observer = new RecyclerView.AdapterDataObserver() {
                    @Override
                    public void onChanged() {
                        super.onChanged();
                        if (adapter.getItemCount() > 1) {
                            recyclerView.setVisibility(View.VISIBLE);
                            emptyView.setVisibility(View.GONE);
                        } else {
                            recyclerView.setVisibility(View.GONE);
                            emptyView.setVisibility(View.VISIBLE);
                        }
                    }
                };
                adapter.registerAdapterDataObserver(observer);
                observer.onChanged();
            } else {
                throw new RuntimeException(
                        "This RecyclerView has no adapter, you must call setAdapter first!");
            }
            setEmptyViewStyle(emptyView);

        }

        private void setEmptyViewStyle(TheEmptyView emptyView) {

            boolean canShowText = (mShowText == ShowType.SHOW || (mShowText == ShowType.DEFAULT &&
                    TheEmptyView.hasDefaultConfig() &&
                    TheEmptyView.getConfig().mShowText == ShowType.SHOW));
            emptyView.setShowText(canShowText);
            if (canShowText) {
                if (emptyTextColor != -1) {
                    emptyView.setTextColor(emptyTextColor);
                } else if (TheEmptyView.hasDefaultConfig() && TheEmptyView
                        .getConfig().emptyTextColor != -1) {
                    emptyView.setTextColor(TheEmptyView.getConfig().emptyTextColor);
                }

                if (emptyTextSize != 0) {
                    emptyView.setTextSize(emptyTextSize);
                } else if (TheEmptyView.hasDefaultConfig() && TheEmptyView
                        .getConfig().emptyTextSize != 0) {
                    emptyView.setTextSize(TheEmptyView.getConfig().emptyTextSize);
                }

                if (!TextUtils.isEmpty(emptyText)) {
                    emptyView.setEmptyText(emptyText);
                } else if (TheEmptyView.hasDefaultConfig() && !TextUtils.isEmpty(
                        TheEmptyView.getConfig().emptyText)) {
                    emptyView.setEmptyText(TheEmptyView.getConfig().emptyText);
                }
            }

            boolean canShowIcon = (mShowIcon == ShowType.SHOW || (mShowIcon == ShowType.DEFAULT &&
                    TheEmptyView.hasDefaultConfig() &&
                    TheEmptyView.getConfig().mShowIcon == ShowType.SHOW));
            emptyView.setShowIcon(canShowIcon);
            if (canShowIcon) {
                if (iconSrc != 0) {
                    emptyView.setIcon(iconSrc);
                } else if (TheEmptyView.hasDefaultConfig() && TheEmptyView.getConfig().iconSrc != 0) {
                    emptyView.setIcon(TheEmptyView.getConfig().iconSrc);
                }

                if (iconDrawable != null) {
                    emptyView.setIcon(iconDrawable);
                } else if (TheEmptyView.hasDefaultConfig() && TheEmptyView
                        .getConfig().iconDrawable != null) {
                    emptyView.setIcon(TheEmptyView.getConfig().iconDrawable);
                }
            }

            boolean canShowButton = (mShowButton == ShowType.SHOW || (mShowButton == ShowType
                    .DEFAULT &&
                    TheEmptyView.hasDefaultConfig() &&
                    TheEmptyView.getConfig().mShowButton == ShowType.SHOW));
            emptyView.setShowButton(canShowButton);
            if (canShowButton) {
                if (!TextUtils.isEmpty(actionText)) {
                    emptyView.setActionText(actionText);
                } else if (TheEmptyView.hasDefaultConfig() && !TextUtils.isEmpty(
                        TheEmptyView.getConfig().actionText)) {
                    emptyView.setActionText(TheEmptyView.getConfig().actionText);
                }

                if (mAction != null) {
                    emptyView.setAction(mAction);
                } else if (TheEmptyView.hasDefaultConfig() && TheEmptyView
                        .getConfig().mAction != null) {
                    emptyView.setAction(TheEmptyView.getConfig().mAction);
                }
            }

            if (layoutParams != null) {
                emptyView.setLayoutParams(layoutParams);
            } else if (TheEmptyView.hasDefaultConfig() && TheEmptyView
                    .getConfig().layoutParams != null) {
                emptyView.setLayoutParams(TheEmptyView.getConfig().layoutParams);
            }
        }

        private EmptyViewBuilder(Context context) {
            this.mContext = context;
        }

        public EmptyViewBuilder setEmptyText(String text) {
            this.emptyText = text;
            return this;
        }

        public EmptyViewBuilder setEmptyText(int textResID) {
            this.emptyText = mContext.getString(textResID);
            return this;
        }

        public EmptyViewBuilder setEmptyTextColor(int color) {
            this.emptyTextColor = color;
            return this;
        }

        public EmptyViewBuilder setEmptyTextSize(int textSize) {
            this.emptyTextSize = textSize;
            return this;
        }

        public EmptyViewBuilder setIconSrc(int iconSrc) {
            this.iconSrc = iconSrc;
            this.iconDrawable = null;
            return this;
        }

        public EmptyViewBuilder setIconDrawable(Drawable iconDrawable) {
            this.iconDrawable = iconDrawable;
            this.iconSrc = 0;
            return this;
        }

        public EmptyViewBuilder setLayoutParams(ViewGroup.LayoutParams layoutParams) {
            this.layoutParams = layoutParams;
            return this;
        }


        public EmptyViewBuilder setShowIcon(boolean mShowIcon) {
            this.mShowIcon = mShowIcon ? ShowType.SHOW : ShowType.HIDE;
            return this;
        }

        public EmptyViewBuilder setShowText(boolean showText) {
            this.mShowText = showText ? ShowType.SHOW : ShowType.HIDE;
            return this;
        }

        public EmptyViewBuilder setShowButton(boolean showButton) {
            this.mShowButton = showButton ? ShowType.SHOW : ShowType.HIDE;
            return this;
        }

        public EmptyViewBuilder setAction(View.OnClickListener onClickListener) {
            this.mAction = onClickListener;
            return this;
        }

        public EmptyViewBuilder setActionText(String actionText) {
            this.actionText = actionText;
            return this;
        }

    }
}
