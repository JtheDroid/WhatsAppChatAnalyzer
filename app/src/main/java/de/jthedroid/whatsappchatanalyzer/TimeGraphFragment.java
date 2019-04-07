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

        GraphView(Context context, GraphData graphData, FrameLayout fl) {
            super(context);
            this.graphData = graphData;
            this.valuesX = graphData.getxData();
            this.valuesY = graphData.getyData();
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
        }

        private float map(float val, float from, float to) {
            return from + (to - from) * val;
        }

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
                Bitmap b = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
                Canvas c = new Canvas(b);
                p.setColor(Color.RED);
                p.setStrokeWidth(3);
                if (valuesX.length != valuesY.length) {
                    Log.e("GraphView onDraw", "value arrays are not the same size!");
                    return;
                }
                int fromX = 50, toX = w - 50;
                int fromY = h - 50, toY = 50;
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
