package com.duckyshine.app.math;

public class Vector3 {
    private double x, y, z;

    public Vector3() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void add(Vector3 other) {
        this.x += other.x;
        this.y += other.y;
        this.z += other.z;
    }

    public void subtract(Vector3 other) {
        this.x -= other.x;
        this.y -= other.y;
        this.z -= other.z;
    }

    public void multiply(double t) {
        this.x *= t;
        this.y *= t;
        this.z *= t;
    }

    public void divide(double t) {
        this.x /= t;
        this.y /= t;
        this.z /= t;
    }

    public void cross(Vector3 other) {
        double dx = this.y * other.z - this.z * other.y;
        double dy = this.z * other.x - this.x * other.z;
        double dz = this.x * other.y - this.y * other.x;

        this.x = dx;
        this.y = dy;
        this.z = dz;
    }

    public double getMagnitude() {
        return Math.sqrt(x * this.x + this.y * this.y + this.z * this.z);
    }

    public void normalise() {
        double magnitude = this.getMagnitude();

        // Might have to +- epsilon
        if (magnitude == 0) {
            return;
        }

        this.x /= magnitude;
        this.y /= magnitude;
        this.z /= magnitude;
    }

    public static Vector3 add(Vector3 u, Vector3 v) {
        return new Vector3(u.x + v.x, u.y + v.y, u.z + v.z);
    }

    public static Vector3 subtract(Vector3 u, Vector3 v) {
        return new Vector3(u.x - v.x, u.y - v.y, u.z - v.z);
    }

    public static Vector3 multiply(Vector3 u, double t) {
        return new Vector3(u.x * t, u.y * t, u.z * t);
    }

    public static Vector3 divide(Vector3 u, double t) {
        return new Vector3(u.x / t, u.y / t, u.z / t);
    }

    public static Vector3 cross(Vector3 u, Vector3 v) {
        double dx = u.y * v.z - u.z * v.y;
        double dy = u.z * v.x - u.x * v.z;
        double dz = u.x * v.y - u.y * v.x;

        return new Vector3(dx, dy, dz);
    }

    public static double getMagnitude(Vector3 u) {
        return Math.sqrt(u.x * u.x + u.y * u.y + u.z * u.z);
    }

    public static Vector3 normalised(Vector3 u) {
        double magnitude = Vector3.getMagnitude(u);

        if (magnitude == 0) {
            return u;
        }

        return new Vector3(u.x / magnitude, u.y / magnitude, u.z / magnitude);
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public String toString() {
        return "[" + this.x + ", " + this.y + ", " + this.z + "]";
    }
}
