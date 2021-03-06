package com.pospi.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

import com.pospi.pai.yunpos.R;

import java.util.ArrayList;

/**
 * Created by Qiyan on 2016/5/3.
 */
public class MyChartView extends View {
    private ArrayList<MyChartItem> items;
    private String unit;
    private String yFormat = "0.#";

    private Context context;

    public void SetTuView(ArrayList<MyChartItem> list, String unitInfo) {
        this.items = list;
        this.unit = unitInfo;
    }

    public MyChartView(Context ct) {
        super(ct);
        this.context = ct;
    }

    public MyChartView(Context ct, AttributeSet attrs) {
        super(ct, attrs);
        this.context = ct;
    }

    public MyChartView(Context ct, AttributeSet attrs, int defStyle) {
        super(ct, attrs, defStyle);
        this.context = ct;
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (items == null) {
            return;
        }

        int height = getHeight();
        int width = getWidth();

        int split = dip2px(context, 8);
        int marginl = width / 12;
        int margint = dip2px(context, 60);
        int margint2 = dip2px(context, 25);
        int bheight = height - margint - 2 * split;

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.parseColor("#aaaaaa"));
        paint.setStrokeWidth(4);
//        paint.setStyle(Style.STROKE);
        canvas.drawLine(split, margint2, width - split, margint2, paint);
        canvas.drawLine(split, height - split, width - split, height - split,
                paint);

        // 画单位
        Paint p = new Paint();
        p.setAlpha(0x0000ff);
        p.setTextSize(sp2px(context, 10));
        p.setColor(Color.parseColor("#111111"));
        canvas.drawText(unit, split, margint2 + split * 2, p);

        // 画X坐标
        ArrayList<Integer> xlist = new ArrayList<Integer>();
        paint.setColor(Color.BLACK);
        for (int i = 0; i < items.size(); i++) {
            int span = (width - 2 * marginl) / items.size();
            int x = marginl + span / 2 + span * i;
            xlist.add(x);
            drawText(items.get(i).getX(), x, split * 2, canvas);
        }


        float max = Float.MIN_VALUE;
        float min = Float.MAX_VALUE;
        for (int i = 0; i < items.size(); i++) {
            float y = items.get(i).getY();
            if (y > max) {
                max = y;
            }
            if (y < min) {
                min = y;
            }
        }

        float span = max - min;
        if (span == 0) {
            span = 6.0f;
        }
        max = max + span / 6.0f;
        min = min - span / 6.0f;

        // 获取点集合
        Point[] mPoints = getPoints(xlist, max, min, bheight, margint);

        // 画线 连接起各个点的线
        paint.setColor(getResources().getColor(R.color.blue_sale));
//        paint.setStyle(Style.STROKE);
        paint.setStrokeWidth(2);
        drawLine(mPoints, canvas, paint);

        // //各个画点的颜色
        paint.setColor(getResources().getColor(R.color.black));
//        paint.setStyle(Style.FILL);
        for (int i = 0; i < mPoints.length; i++) {
            canvas.drawCircle(mPoints[i].x, mPoints[i].y, 2, paint);
            String yText = new java.text.DecimalFormat(yFormat).format(items
                    .get(i).getY());
            drawText(yText, mPoints[i].x,
                    mPoints[i].y - dip2px(context,2), canvas);
        }
    }

    private Point[] getPoints(ArrayList<Integer> xlist, float max, float min,
                              int h, int top) {
        Point[] points = new Point[items.size()];
        for (int i = 0; i < items.size(); i++) {
            int ph = top + h
                    - (int) (h * ((items.get(i).getY() - min) / (max - min)));
            points[i] = new Point(xlist.get(i), ph);
        }


        return points;
    }

    private void drawLine(Point[] ps, Canvas canvas, Paint paint) {
        Point startp = new Point();
        Point endp = new Point();
        for (int i = 0; i < ps.length - 1; i++) {
            startp = ps[i];
            endp = ps[i + 1];
            canvas.drawLine(startp.x, startp.y, endp.x, endp.y, paint);
        }
    }

    private void drawText(String text, int x, int y, Canvas canvas) {
        Paint p = new Paint();
        p.setAlpha(0x0000ff);
        p.setTextSize(sp2px(context, 14));
        p.setTextAlign(Paint.Align.CENTER);
        p.setColor(Color.BLACK);
        canvas.drawText(text, x, y, p);
    }

    public ArrayList<MyChartItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<MyChartItem> items) {
        this.items = items;
    }

    public String getyFormat() {
        return yFormat;
    }

    public void setyFormat(String yFormat) {
        this.yFormat = yFormat;
    }


    public int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
