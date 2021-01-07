package com.oligei.timemanagement.utils.clusterutils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cluster {

    private int id;
    private Point center;
    private Map<Integer, Point> members = new HashMap<>();

    public Cluster() {}

    public Cluster(int id, Point center) {
        this.id = id;
        this.center = center;
    }

    public Cluster(int id, Point center, Map<Integer, Point> members) {
        this.id = id;
        this.center = center;
        this.members = members;
    }

    public void addPoint(Point newPoint) {
        if (!members.containsKey(newPoint.getId())) {
            members.put(newPoint.getId(), newPoint);
        } else {
            System.out.println("样本数据点 {" + newPoint.toString() + "} 已经存在！");
        }
    }

    public int getId() {
        return id;
    }

    public Point getCenter() {
        return center;
    }

    public void setCenter(Point center) {
        this.center = center;
    }

    public Map<Integer, Point> getMembers() {
        return members;
    }

    @Override
    public String toString() {
        String toString = "Cluster \n" + "Cluster_id=" + this.id + ", center:{" + this.center.toString() + "}";
        for (Point point : members.values()) {
            toString += "\n" + point.toString();
        }
        return toString + "\n";
    }
}
