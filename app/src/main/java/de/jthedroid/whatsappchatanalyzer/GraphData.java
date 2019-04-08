package de.jthedroid.whatsappchatanalyzer;

import android.os.Parcel;
import android.os.Parcelable;

public class GraphData implements Parcelable {    //TODO: more data
    public static final Parcelable.Creator<GraphData> CREATOR
            = new Parcelable.Creator<GraphData>() {
        public GraphData createFromParcel(Parcel in) {
            return new GraphData(in);
        }

        public GraphData[] newArray(int size) {
            return new GraphData[size];
        }
    };
    private float[] rawXData, rawYData;
    private float[] xData, yData;
    private String[] xDesc, yDesc;


    private GraphData(Parcel parcel) {
        rawXData = parcel.createFloatArray();
        rawYData = parcel.createFloatArray();
        xData = parcel.createFloatArray();
        yData = parcel.createFloatArray();
        xDesc = parcel.createStringArray();
        yDesc = parcel.createStringArray();
    }

    GraphData(float[] rawXData, float[] rawYData, String[] xDesc, String[] yDesc) {
        this.rawXData = rawXData;
        this.rawYData = rawYData;
        this.xDesc = xDesc;
        this.yDesc = yDesc;
        scale();
    }

    //Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeFloatArray(rawXData);
        parcel.writeFloatArray(rawYData);
        parcel.writeFloatArray(xData);
        parcel.writeFloatArray(yData);
        parcel.writeStringArray(xDesc);
        parcel.writeStringArray(yDesc);
    }

    /**
     * Scales x- and y-values to range [0,1]
     */
    void scale() {
        if (rawXData.length == 0) return;
        float minX, maxX, minY, maxY;
        minX = maxX = rawXData[0];
        minY = maxY = rawYData[0];
        for (int i = 1; i < rawXData.length; i++) {
            float x = rawXData[i], y = rawYData[i];
            if (x < minX) minX = x;
            else if (x > maxX) maxX = x;
            if (y < minY) minY = y;
            else if (y > maxY) maxY = y;
        }
        xData = new float[rawXData.length];
        yData = new float[rawYData.length];
        for (int i = 0; i < xData.length; i++) {
            xData[i] = map(rawXData[i], minX, maxX);
            yData[i] = map(rawYData[i], minY, maxY);
        }
    }

    private float map(float val, float min, float max) {
        if (max - min == 0) return 0;
        return (val - min) / (max - min);
    }

    float[] getXData() {
        return xData;
    }

    float[] getYData() {
        return yData;
    }

    float[] getRawXData() {
        return rawXData;
    }

    float[] getRawYData() {
        return rawYData;
    }

    String[] getXDesc() {
        return xDesc;
    }

    String[] getYDesc() {
        return yDesc;
    }
}
