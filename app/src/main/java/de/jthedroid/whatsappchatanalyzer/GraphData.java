package de.jthedroid.whatsappchatanalyzer;

class GraphData implements ChatData {
    final private float[] rawXData, rawYData;
    private float[] xData, yData;
    final private String[] xDesc, yDesc;
    final private GraphType graphType;

    GraphData(float[] rawXData, float[] rawYData, String[] xDesc, String[] yDesc) {
        this(rawXData, rawYData, xDesc, yDesc, GraphType.DEFAULT);
    }

    GraphData(float[] rawXData, float[] rawYData, String[] xDesc, String[] yDesc, GraphType graphType) {
        this.rawXData = rawXData;
        this.rawYData = rawYData;
        this.xDesc = xDesc;
        this.yDesc = yDesc;
        this.graphType = graphType;
        scale();
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

    GraphType getGraphType() {
        return graphType;
    }
}

enum GraphType {
    DEFAULT,
    BARGRAPH
    //HISTOGRAM
}