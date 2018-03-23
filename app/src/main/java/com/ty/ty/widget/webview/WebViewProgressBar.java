package com.ty.ty.widget.webview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.ty.ty.R;
import com.ty.ty.utils.UiUtils;


/**
 * @author Airsaid
 * @github https://github.com/airsaid
 * @date 2017/5/22
 * @desc 自定义 WebView 加载进度条
 */
public class WebViewProgressBar extends View {

    private final static int HEIGHT = 3;// 进度条高度为 5
    private int progress = 1;// 进度默认为 1
    private Paint paint;// 进度条的画笔

    public WebViewProgressBar(Context context) {
        this(context, null);
    }

    public WebViewProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WebViewProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint() {
        paint = new Paint(Paint.DITHER_FLAG);
        paint.setStyle(Paint.Style.STROKE);                     // 填充方式为描边
        paint.setStrokeWidth(HEIGHT);                           // 设置画笔的宽度
        paint.setAntiAlias(true);                               // 抗锯齿
        paint.setDither(true);                                  // 使用抖动效果
        paint.setColor(UiUtils.getColor(R.color.colorAccent));  // 画笔设置颜色
    }

    /**
     * 设置进度
     * @param progress 进度值
     */
    public void setProgress(int progress) {
        this.progress = progress;
        // 刷新画笔
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 绘制矩形从（0.0）开始到（progress,height）的区域
        canvas.drawRect(0, 0, getWidth() * progress / 100, HEIGHT, paint);
    }
}