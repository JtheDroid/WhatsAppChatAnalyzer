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
    private float[] xData, yData;

    private GraphData(Parcel parcel) {
        xData = parcel.createFloatArray();
        yData = parcel.createFloatArray();
    }

    GraphData(float[] xData, float[] yData) {
        this.xData = xData;
        this.yData = yData;
    }

    //Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeFloatArray(xData);
        parcel.writeFloatArray(yData);
    }

    /**
     * Scales x- and y-values to range [0,1]
     */
    void scale() {
        if (xData.length == 0) return;
        float minX, maxX, minY, maxY;
        minX = maxX = xData[0];
        minY = maxY = yData[0];
        for (int i = 1; i < xData.length; i++) {
            float x = xData[i], y = yData[i];
            if (x < minX) minX = x;
            else if (x > maxX) maxX = x;
            if (y < minY) minY = y;
            else if (y > maxY) maxY = y;
        }
        for (int i = 0; i < xData.length; i++) {
            xData[i] = map(xData[i], minX, maxX);
            yData[i] = map(yData[i], minY, maxY);
        }
    }

    private float map(float val, float min, float max) {
        if (max - min == 0) return 0;
        return (val - min) / (max - min);
    }

    float[] getxData() {
        return xData;
    }

    float[] getyData() {
        return yData;
    }
}
