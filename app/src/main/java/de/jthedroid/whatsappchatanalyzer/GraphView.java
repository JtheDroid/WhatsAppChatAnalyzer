package de.jthedroid.whatsappchatanalyzer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

public class GraphView extends View {  //TODO: add touch interaction: (scrolling, zooming?)
    private final Point display;
    private final Paint p;
    private GraphData graphData;
    private float[] valuesX, valuesY;
    private Bitmap bitmap = null;
    private final Thread thread;
    private final GraphViewRunnable runnable;
    private View loadingView;
    private float lastW, lastH;
    private float x, y;
    private boolean showTap = false;
    private int highlightIndex;
    private final float padding = 50, textPadding = 5;
    private final boolean darkTheme;
    private final Rect textXPos, textXBox, textYPos, textYBox;
    private final Observer<GraphData> graphDataObserver;
    private String key = null;

    public GraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
        p = new Paint();
        display = new Point();
        display.set(500, 250);
        runnable = new GraphViewRunnable();
        thread = new Thread(runnable);
        darkTheme = getContext().getSharedPreferences(getContext().getString(R.string.preference_file_key), Context.MODE_PRIVATE).getBoolean(getContext().getString(R.string.preference_key_theme), false);
        textXPos = new Rect();
        textXBox = new Rect();
        textYPos = new Rect();
        textYBox = new Rect();
        if (isInEditMode()) {
            valuesX = new float[10];
            valuesY = new float[10];
            for (int i = 0; i < 10; i++) {
                valuesX[i] = 0.1f * i;
                valuesY[i] = (float) java.lang.Math.random();
            }
        }
        graphDataObserver = new Observer<GraphData>() {
            @Override
            public void onChanged(@Nullable GraphData graphData) {
                if (graphData != null) {
                    init(graphData, loadingView);
                    postInvalidate();
                }
            }
        };
    }

    public Observer<GraphData> getGraphDataObserver() {
        return graphDataObserver;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void init(GraphData graphData, View loadingView) {
        this.graphData = graphData;
        if (graphData != null) {
            this.valuesX = graphData.getXData();
            this.valuesY = graphData.getYData();
        }
        this.loadingView = loadingView;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (graphData == null && !isInEditMode()) {
            setLoadingVisible(true);
            if (key != null) {
                GraphData graphData = DataStorage.getInstance().getData(key);
                if (graphData != null) {
                    init(graphData, loadingView);
                    postInvalidate();
                }
            }
            return;
        }
        int w = getWidth();
        int h = getHeight();
        if (bitmap == null || bitmap.getWidth() != w || bitmap.getHeight() != h) {
            setLoadingVisible(true);
            runnable.set(w, h);
            if (!runnable.running && !thread.isAlive()) thread.start();
        } else {
            canvas.drawBitmap(bitmap, 0, 0, null);
            setLoadingVisible(false);
        }

        if (showTap || isInEditMode()) {
            p.setColor(getTransparentColor(50, false));
            if (isInEditMode()) {
                highlightIndex = findNearestIndex(valuesX, (float) java.lang.Math.random());
            }
            float yHighlight = map(valuesY[highlightIndex], h - padding, padding);
            float xHighlight = map(valuesX[highlightIndex], padding, w - padding);
            canvas.drawLine(xHighlight, padding, xHighlight, h - padding, p);
            canvas.drawLine(padding, yHighlight, w - padding, yHighlight, p);
            p.setColor(getColor(false));
            canvas.drawCircle(xHighlight, yHighlight, 5, p);
            String textX, textY;
            if (!isInEditMode()) {
                textX = graphData.getXDesc()[highlightIndex];
                textY = graphData.getYDesc()[highlightIndex];
            } else textX = textY = "Sample data";
            getTextXBounds(textX, xHighlight, h - padding / 2, w, p);
            getTextYBounds(textY, xHighlight, yHighlight, w, h, p);
            p.setColor(getTransparentColor(100, true));
            canvas.drawRect(textXBox, p);
            canvas.drawRect(textYBox, p);
            p.setTextAlign(Paint.Align.CENTER);
            p.setTextSize(30);
            p.setColor(getColor(false));
            canvas.drawText(textX, textXPos.exactCenterX(), textXPos.bottom, p);
            canvas.drawText(textY, textYPos.exactCenterX(), textYPos.bottom, p);
        }
        lastW = w;
        lastH = h;
    }

    private void setLoadingVisible(boolean show) {
        if (loadingView != null) loadingView.setVisibility(show ? VISIBLE : GONE);

    }

    private int getColor(boolean invert) {
        return (darkTheme ^ invert) ? Color.WHITE : Color.BLACK;
    }

    private int getTransparentColor(int alpha, boolean invert) {
        return (darkTheme ^ invert) ?
                Color.argb(alpha, 255, 255, 255) :
                Color.argb(alpha, 0, 0, 0);
    }

    private void getTextXBounds(String text, float xPos, float yPos, int w, Paint p) {  //for TextAlign Paint.Align.CENTER
        p.getTextBounds(text, 0, text.length(), textXPos);
        float textW = textXPos.width();
        xPos -= textW / 2;
        if (xPos < textPadding) xPos = textPadding;
        else if (xPos > w - textPadding - textW) xPos = w - textPadding - textW;
        textXPos.offset((int) xPos, (int) (yPos + textXPos.height() / 2f));
        textXBox.set(textXPos);
        textXBox.inset(-5, -5);
    }

    private void getTextYBounds(String text, float xPos, float yPos, int w, int h, Paint p) {  //for TextAlign Paint.Align.CENTER
        p.getTextBounds(text, 0, text.length(), textYPos);
        float textW = textYPos.width();
        xPos = xPos > w / 2f ? textPadding : w - textPadding - textW;
        if (yPos < textPadding) yPos = textPadding;
        if (yPos > h - textPadding) yPos = h - textPadding;
        textYPos.offset((int) xPos, (int) (yPos + textYPos.height() / 2f));
        textYBox.set(textYPos);
        textYBox.inset(-5, -5);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        showTap = true;
        x = event.getX();
        y = event.getY();
        if (graphData != null)
            highlightIndex = findNearestIndex(valuesX, unmap(x, padding, lastW - padding));
        invalidate();
        return performClick();
    }


    @Override
    public boolean performClick() {
        return super.performClick();
    }

    private float map(float val, float from, float to) {
        return from + (to - from) * val;
    }

    private float unmap(float val, float from, float to) {
        return (val - from) / (to - from);
    }

    private int findNearestIndex(float[] arr, float val) {  //TODO: improve performance (binary search?), linear is really inefficient
        if (arr.length == 0) return -1;
        float minDiff = diff(arr[0], val);
        int index = 0;
        for (int i = 1; i < arr.length; i++) {
            float diff = diff(arr[i], val);
            if (diff < minDiff) {
                minDiff = diff;
                index = i;
            }
        }
        return index;
    }

    private float diff(float f1, float f2) {
        return f1 > f2 ? f1 - f2 : f2 - f1;
    }

        /*
        from + (to - from) * val = newV     // - from
        val * (to - from) = newV - from     // /(to-from)
        val = (newV - from) / (to - from)
         */

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//            Log.v("[GraphView] onMeasure w", MeasureSpec.toString(widthMeasureSpec));
//            Log.v("[GraphView] onMeasure h", MeasureSpec.toString(heightMeasureSpec));
        if (getDisplay() != null) {
            getDisplay().getRealSize(display);
        }
        int resolvedW = resolveSize(display.x, widthMeasureSpec);
        int resolvedH = resolveSize(resolvedW / 2, heightMeasureSpec);
//            Log.v("[GraphView] resolved w", Integer.toString(resolvedW));
//            Log.v("[GraphView] resolved h", Integer.toString(resolvedH));
        setMeasuredDimension(resolvedW, resolvedH);
    }

    private class GraphViewRunnable implements Runnable {
        int w, h;
        boolean running = false;

        void set(int w, int h) {
            this.w = w;
            this.h = h;
        }

        @Override
        public void run() {  //TODO: add styling, text etc.
            running = true;
            if (valuesX.length != valuesY.length) {
                Log.e("GraphView onDraw", "value arrays are not the same size!");
                return;
            }
            Bitmap b = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(b);
            GraphType graphType = isInEditMode() ? GraphType.DEFAULT : graphData.getGraphType();
            switch (graphType) {
                case DEFAULT:
                    drawDefault(c);
                    break;
                case BARGRAPH:
                    drawBarGraph(c);
                    break;
            }
            bitmap = b;
            postInvalidate();
            running = false;
        }

        private void drawDefault(Canvas c) {
            p.setColor(getResources().getColor(R.color.colorGraph, null));
            p.setStrokeWidth(3);
            float fromX = padding, toX = w - padding;
            float fromY = h - padding, toY = padding;
            float lastX = map(valuesX[0], fromX, toX), lastY = map(valuesY[0], fromY, toY);
            for (int i = 1; i < valuesX.length; i++) {
                c.drawLine(lastX, lastY, lastX = map(valuesX[i], fromX, toX), lastY = map(valuesY[i], fromY, toY), p);
            }
        }

        private void drawBarGraph(Canvas c) {
            p.setColor(getResources().getColor(R.color.colorGraph, null));
            p.setStrokeWidth(3);
            float fromX = padding, toX = w - padding;
            float fromY = h - padding, toY = padding;
            for (int i = 1; i < valuesX.length; i++) {
                float x = map(valuesX[i], fromX, toX), y = map(valuesY[i], fromY, toY);
                c.drawLine(x, fromY, x, y, p);  //TODO: maybe increase width if necessary
            }
        }
    }
}
