/*

 This is the source code of exteraGram for Android.

 We do not and cannot prevent the use of our code,
 but be respectful and credit the original author.

 Copyright @immat0x1, 2023

*/

// Edited by @ushastoe, 2025

package org.ushastoe.fluffy.activities.elements;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.FrameLayout;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.Easings;
import org.telegram.ui.Components.LayoutHelper;
import org.ushastoe.fluffy.fluffyConfig;

import java.util.Objects;

@SuppressLint("ViewConstructor")
public class ChatListPreviewCell extends FrameLayout {

    private final FrameLayout preview;

    private final RectF rect = new RectF();
    private final TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    private final Paint outlinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private float statusProgress;
    private float titleProgress;
    private float centeredTitleProgress;
    private String titleText;

    private ValueAnimator animator;

    private float showStoriesProgress = fluffyConfig.showStories ? 1f : 0f;

    public ChatListPreviewCell(Context context) {
        super(context);
        setWillNotDraw(false);
        setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));

        outlinePaint.setStyle(Paint.Style.STROKE);
        outlinePaint.setColor(ColorUtils.setAlphaComponent(Theme.getColor(Theme.key_switchTrack), 0x3F));
        outlinePaint.setStrokeWidth(Math.max(2, AndroidUtilities.dp(1f)));

        preview = new FrameLayout(context) {
            @SuppressLint("DrawAllocation")
            @Override
            protected void onDraw(Canvas canvas) {
                int color = Theme.getColor(Theme.key_switchTrack);
                int r = Color.red(color);
                int g = Color.green(color);
                int b = Color.blue(color);
                float w = getMeasuredWidth();
                float h = getMeasuredHeight();

                textPaint.setColor(ColorUtils.blendARGB(0x00, Theme.getColor(Theme.key_windowBackgroundWhiteBlackText), titleProgress));
                textPaint.setTextSize(AndroidUtilities.dp(20));
                titleText = (String) TextUtils.ellipsize(titleText, textPaint, w - AndroidUtilities.dp(130 + 35 * statusProgress), TextUtils.TruncateAt.END);
                textPaint.setTextSize(AndroidUtilities.dp(18 + 2 * titleProgress));
                textPaint.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));




                rect.set(0, 0, w, h);
                Theme.dialogs_onlineCirclePaint.setColor(Color.argb(20, r, g, b));
                canvas.drawRoundRect(rect, AndroidUtilities.dp(8), AndroidUtilities.dp(8), Theme.dialogs_onlineCirclePaint);


                float stroke = outlinePaint.getStrokeWidth() / 2;
                rect.set(stroke, stroke, w - stroke, h - stroke);
                canvas.drawRoundRect(rect, AndroidUtilities.dp(8), AndroidUtilities.dp(8), outlinePaint);

                Drawable search = ContextCompat.getDrawable(context, R.drawable.ic_ab_search).mutate();
                search.setColorFilter(new PorterDuffColorFilter(Color.argb(204, r, g, b), PorterDuff.Mode.MULTIPLY));
                search.setBounds((int) w - AndroidUtilities.dp(39), AndroidUtilities.dp(22), (int) w - AndroidUtilities.dp(12), AndroidUtilities.dp(49));
                search.draw(canvas);

                if (showStoriesProgress > 0) {
                    float circleX = AndroidUtilities.dp(60);
                    float circleY = AndroidUtilities.dp(35);
                    float circleRadius = AndroidUtilities.dp(12);
                    float strokeWidth = AndroidUtilities.dp(2);

                    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                    paint.setStyle(Paint.Style.STROKE);
                    paint.setStrokeWidth(strokeWidth);
                    paint.setColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));

                    canvas.drawCircle(circleX, circleY, circleRadius * showStoriesProgress, paint);

                    Drawable drawable = context.getResources().getDrawable(R.drawable.avatar);
                    if (drawable != null) {
                        int drawableSize = (int) (circleRadius * 1.5f * showStoriesProgress);
                        int left = (int) (circleX - drawableSize / 2);
                        int top = (int) (circleY - drawableSize / 2);
                        int right = left + drawableSize;
                        int bottom = top + drawableSize;

                        drawable.setBounds(left, top, right, bottom);
                        drawable.setAlpha((int) (255 * showStoriesProgress)); // Добавляем плавное скрытие
                        drawable.draw(canvas);
                    }
                }

                Theme.dialogs_onlineCirclePaint.setColor(Color.argb(204, r, g, b));
                for (int i = 0; i < 3; i++) {
                    float start = 28 + 6.1f * i;
                    canvas.drawRoundRect(
                            AndroidUtilities.dpf2(20),
                            AndroidUtilities.dpf2(start),
                            AndroidUtilities.dpf2(20 + 20),
                            AndroidUtilities.dpf2(start + 2.8f),
                            AndroidUtilities.dp(10),
                            AndroidUtilities.dp(10),
                            Theme.dialogs_onlineCirclePaint
                    );
                }

                float width = textPaint.measureText(titleText);
                float titleStart = centeredTitleProgress * ((w - width - AndroidUtilities.dp(30) * statusProgress) / 2 - AndroidUtilities.dp(78)) + AndroidUtilities.dp(78);
                float titleEnd = titleStart + width;

                Theme.dialogs_onlineCirclePaint.setColor(ColorUtils.blendARGB(0x00, ColorUtils.setAlphaComponent(Theme.getColor(Theme.key_switchTrack), 0x5F), titleProgress * statusProgress));
                canvas.drawRoundRect(titleEnd + AndroidUtilities.dp(5), AndroidUtilities.dp(22), titleEnd + AndroidUtilities.dp(30), AndroidUtilities.dp(47), AndroidUtilities.dp(4), AndroidUtilities.dp(4), Theme.dialogs_onlineCirclePaint);
                canvas.drawText(titleText, titleStart, AndroidUtilities.dp(42), textPaint);
            }
        };
        preview.setWillNotDraw(false);
        addView(preview, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT, Gravity.TOP | Gravity.CENTER, 21, 15, 21, 21));
        updateCenteredTitle(false);
        updateTitle(false);
    }


    public void updateStories(boolean animate) {
        float to = fluffyConfig.showStories ? 1f : 0f;
        if (to == showStoriesProgress && animate)
            return;

        if (animate) {
            animator = ValueAnimator.ofFloat(showStoriesProgress, to).setDuration(250);
            animator.setInterpolator(Easings.easeInOutQuad);
            animator.addUpdateListener(animation -> {
                showStoriesProgress = (Float) animation.getAnimatedValue();
                invalidate();
            });
            animator.start();
        } else {
            showStoriesProgress = to;
            invalidate();
        }
    }

    public void updateCenteredTitle(boolean animate) {
        float to = fluffyConfig.centerTitle ? 1 : 0;
        if (to == centeredTitleProgress && animate)
            return;
        if (animate) {
            animator = ValueAnimator.ofFloat(centeredTitleProgress, to).setDuration(250);
            animator.setInterpolator(Easings.easeInOutQuad);
            animator.addUpdateListener(animation -> {
                centeredTitleProgress = (Float) animation.getAnimatedValue();
                invalidate();
            });
            animator.start();
        } else {
            centeredTitleProgress = to;
            invalidate();
        }
    }

    public void updateTitle(boolean animate) {
        if (Objects.equals(titleText, fluffyConfig.getTitleHeader()) && animate)
            return;
        if (animate) {
            animator = ValueAnimator.ofFloat(1f, 0f).setDuration(250);
            animator.setInterpolator(Easings.easeInOutQuad);
            animator.addUpdateListener(animation -> {
                titleProgress = (Float) animation.getAnimatedValue();
                invalidate();
            });
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    titleText = fluffyConfig.getTitleHeader();
                    animator.setFloatValues(0f, 1f);
                    animator.removeAllListeners();
                    animator.start();
                }
            });
            animator.start();
        } else {
            titleText =  fluffyConfig.getTitleHeader();
            titleProgress = 1f;
            invalidate();
        }
    }

    @Override
    public void invalidate() {
        super.invalidate();
        preview.invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(72 + 15 + 21), MeasureSpec.EXACTLY));
    }
}
