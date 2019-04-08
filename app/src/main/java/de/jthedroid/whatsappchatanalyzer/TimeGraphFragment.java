package de.jthedroid.whatsappchatanalyzer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;


public class TimeGraphFragment extends Fragment {
    private static final String GRAPH_DATA = "graphData";
    private GraphData graphData;

    public TimeGraphFragment() {
    }

    public static TimeGraphFragment newInstance(GraphData graphData) {
        TimeGraphFragment fragment = new TimeGraphFragment();
        Bundle args = new Bundle();
        args.putParcelable(GRAPH_DATA, graphData);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            graphData = getArguments().getParcelable(GRAPH_DATA);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_time_graph, container, false);
        FrameLayout fl = v.findViewById(R.id.flGraph);
        fl.addView(new GraphView(getActivity(), graphData, fl));
        return v;
    }

    private class GraphView extends View {  //TODO: add touch interaction: (scrolling, zooming?), displaying data points
        final Point display;
        final GraphData graphData;
        final float[] valuesX, valuesY;
        private final Paint p;
        Bitmap bitmap = null;
        Thread thread;
        GraphViewRunnable runnable;
        FrameLayout fl;
        float lastW, lastH;
        float x, y;
        boolean showTap = false;
        int highlightIndex;
        float padding = 50;

        GraphView(Context context, GraphData graphData, FrameLayout fl) {
            super(context);
            this.graphData = graphData;
            this.valuesX = graphData.getXData();
            this.valuesY = graphData.getYData();
            p = new Paint();
            display = new Point();
            display.set(500, 250);
            runnable = new GraphViewRunnable();
            thread = new Thread(runnable);
            this.fl = fl;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            int w = getWidth();
            int h = getHeight();
            if (bitmap == null || bitmap.getWidth() != w || bitmap.getHeight() != h) {
                fl.findViewById(R.id.progressBarGraphLoading).setVisibility(VISIBLE);
                runnable.set(w, h);
                if (!thread.isAlive()) thread.start();
            } else {
                canvas.drawBitmap(bitmap, 0, 0, null);
                fl.findViewById(R.id.progressBarGraphLoading).setVisibility(GONE);
            }
            if (showTap) {
                p.setColor(Color.argb(100, 150, 150, 150));
                canvas.drawLine(x, padding, x, h - padding, p);
                float yHighlight = map(valuesY[highlightIndex], h - padding, padding);
                float xHighlight = map(valuesX[highlightIndex], padding, w - padding);
                canvas.drawLine(padding, yHighlight, w - padding, yHighlight, p);
                p.setColor(Color.BLACK);
                canvas.drawCircle(xHighlight, yHighlight, 5, p);
                p.setTextAlign(Paint.Align.CENTER);
                p.setTextSize(30);
                canvas.drawText(graphData.getXDesc()[highlightIndex], w / 2f, h - padding / 2, p);  //TODO: move with x?
                canvas.drawText(graphData.getYDesc()[highlightIndex], xHighlight, yHighlight - padding / 4, p);
            }
            lastW = w;
            lastH = h;
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            showTap = true;
            x = event.getX();
            y = event.getY();
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

            void set(int w, int h) {
                this.w = w;
                this.h = h;
            }

            @Override
            public void run() {  //TODO: add styling, text etc.
                if (valuesX.length != valuesY.length) {
                    Log.e("GraphView onDraw", "value arrays are not the same size!");
                    return;
                }
                Bitmap b = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
                Canvas c = new Canvas(b);
                p.setColor(Color.RED);
                p.setStrokeWidth(3);
                float fromX = padding, toX = w - padding;
                float fromY = h - padding, toY = padding;
                float lastX = map(valuesX[0], fromX, toX), lastY = map(valuesY[0], fromY, toY);
                for (int i = 1; i < valuesX.length; i++) {
                    c.drawLine(lastX, lastY, lastX = map(valuesX[i], fromX, toX), lastY = map(valuesY[i], fromY, toY), p);
                }
                bitmap = b;
                postInvalidate();
            }
        }
    }

}
