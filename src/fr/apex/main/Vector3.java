package fr.apex.main;

public class Vector3 {

    protected double x;
    protected double y;
    protected double z;

    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public Vector3 add(Vector3 vec) {
        this.x += vec.x;
        this.y += vec.y;
        this.z += vec.z;
        return this;
    }

    public Vector3 addX(double x) {
        this.x += x;
        return this;
    }

    public Vector3 addY(double y) {
        this.y += y;
        return this;
    }

    public Vector3 addZ(double z) {
        this.z += z;
        return this;
    }

    public Vector3 subtract(Vector3 vec) {
        this.x -= vec.x;
        this.y -= vec.y;
        this.z -= vec.z;
        return this;
    }

    public double length() {
        return Math.sqrt(lengthSquared());
    }

    public double lengthSquared() {
        return x * x + y * y + z * z;
    }

    public double distanceSquared(Vector3 o) {
        double difX = x - o.x;
        double difY = y - o.y;
        double difZ = z - o.z;
        return difX * difX + difY * difY + difZ * difZ;
    }

    public Vector3 getMidpoint(Vector3 other) {
        double x = (this.x + other.x) / 2.0D;
        double y = (this.y + other.y) / 2.0D;
        double z = (this.z + other.z) / 2.0D;
        return new Vector3(x, y, z);
    }
    public Vector3 multiply(float m) {
        this.x *= m;
        this.y *= m;
        this.z *= m;
        return this;
    }

    //En radian
    public Vector3 rotate(double angle, Vector3 axis) {
        Quat.buildQuatFrom3DVector(axis.clone().normalize(), angle).rotateWithMagnitude(this);
        return this;
    }

    public Vector3 normalize() {
        double length = length();
        this.x /= length;
        this.y /= length;
        this.z /= length;
        return this;
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

    public Vector3 clone() {
        return new Vector3(x, y, z);
    }
    public static class Quat {

        public double i;
        public double j;
        public double k;
        public double s;

        public Quat() {
            this(1D);
        }

        public Quat(double zeroMag) {
            this.s = zeroMag;
            this.i = 0D;
            this.j = 0D;
            this.k = 0D;
        }

        public Quat(double w, double i, double j, double k) {
            this.i = i;
            this.j = j;
            this.k = k;
            this.s = w;
        }

        public static Quat buildQuatWithAngle(double ax, double ay, double az, double angle) {
            angle *= 0.5D;
            double d4 = Math.sin(angle);
            return new Quat(Math.cos(angle), ax * d4, ay * d4, az * d4);
        }

        public double mag() {
            return Math.sqrt(this.i * this.i + this.j * this.j + this.k * this.k + this.s * this.s);
        }

        public void rotateWithMagnitude(Vector3 vec) {
            double d = -this.i * vec.x - this.j * vec.y - this.k * vec.z;
            double d1 = this.s * vec.x + this.j * vec.z - this.k * vec.y;
            double d2 = this.s * vec.y - this.i * vec.z + this.k * vec.x;
            double d3 = this.s * vec.z + this.i * vec.y - this.j * vec.x;
            vec.x = (d1 * this.s - d * this.i - d2 * this.k + d3 * this.j);
            vec.y = (d2 * this.s - d * this.j + d1 * this.k - d3 * this.i);
            vec.z = (d3 * this.s - d * this.k - d1 * this.j + d2 * this.i);
        }


        public static Quat buildQuatFrom3DVector(Vector3 axis, double angle) {
            return buildQuatWithAngle(axis.x, axis.y, axis.z, angle);
        }

    }

}