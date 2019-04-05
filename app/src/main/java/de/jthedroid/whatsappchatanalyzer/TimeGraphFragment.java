package de.jthedroid.whatsappchatanalyzer;

import android.content.Context;
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
    private static final String VALUES_X = "valuesX", VALUES_Y = "valuesY";
    private float[] valuesX, valuesY;

    public TimeGraphFragment() {
    }

    public static TimeGraphFragment newInstance(float[] valuesX, float[] valuesY) {
        TimeGraphFragment fragment = new TimeGraphFragment();
        Bundle args = new Bundle();
        args.putFloatArray(VALUES_X, valuesX);
        args.putFloatArray(VALUES_Y, valuesY);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            valuesX = getArguments().getFloatArray(VALUES_X);
            valuesY = getArguments().getFloatArray(VALUES_Y);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_time_graph, container, false);
        FrameLayout fl = v.findViewById(R.id.flGraph);
        fl.addView(new GraphView(getActivity(), valuesX, valuesY));
        return v;
    }

    private class GraphView extends View {
        final Point display;
        final float[] valuesX, valuesY;
        private final Paint p;

        GraphView(Context context, float[] valuesX, float[] valuesY) {
            super(context);
            this.valuesX = valuesX;
            this.valuesY = valuesY;
            p = new Paint();
            display = new Point();
            display.set(500, 250);
        }

        @Override
        protected void onDraw(Canvas canvas) {  //TODO: add styling, text etc.
            super.onDraw(canvas);
            int w = getWidth();
            int h = getHeight();
            p.setColor(Color.RED);
            if (valuesX.length != valuesY.length) {
                Log.e("GraphView onDraw", "value arrays are not the same size!");
                return;
            }
            int fromX = 50, toX = w - 50;
            int fromY = h - 50, toY = 50;
            float lastX = map(valuesX[0], fromX, toX), lastY = map(valuesY[0], fromY, toY);
            for (int i = 1; i < valuesX.length; i++) {
                canvas.drawLine(lastX, lastY, lastX = map(valuesX[i], fromX, toX), lastY = map(valuesY[i], fromY, toY), p);
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
    }
}
