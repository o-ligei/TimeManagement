package com.oligei.timemanagement.utils.clusterutils;

// optimization: extends from UserNeo4j, prevents a series of read from NEO4J
public class Point {

    private float[] localArray;
    private int id;
    private int clusterId;
    private float dist;

    public Point(int id, float[] localArray) {
        this.id = id;
        this.localArray = localArray;
        normalize();
    }

    public Point(float[] localArray) {
        this.id = -1; //表示不属于任意一个类
        this.localArray = localArray;
        normalize();
    }

    public float[] getlocalArray() {
        return localArray;
    }

    public int getId() {
        return id;
    }

    public void setClusterId(int clusterId) {
        this.clusterId = clusterId;
    }

    public int getClusterid() {
        return clusterId;
    }

    public float getDist() {
        return dist;
    }

    public void setDist(float dist) {
        this.dist = dist;
    }

    private void normalize() {
        double length = DistanceCompute.getVectorLength(this);
        for (int i = 0; i < localArray.length; i++) { localArray[i] /= length; }
    }

    @Override
    public String toString() {
        String result = "Point_id=" + id + "  [";
        for (int i = 0; i < localArray.length; i++) {
            result += localArray[i] + " ";
        }
        return result.trim() + "] clusterId: " + clusterId + " dist: " + dist;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) { return false; }

        Point point = (Point) obj;
        if (point.localArray.length != localArray.length) { return false; }

        for (int i = 0; i < localArray.length; i++) {
            if (Float.compare(point.localArray[i], localArray[i]) != 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        float x = localArray[0];
        float y = localArray[localArray.length - 1];
        long temp = x != +0.0d ? Double.doubleToLongBits(x) : 0L;
        int result = (int) (temp ^ (temp >>> 32));
        temp = y != +0.0d ? Double.doubleToLongBits(y) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
