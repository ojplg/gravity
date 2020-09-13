package org.hrorm.gravity;public class Vector {

    private double x;
    private double y;
    private double z;

    public Vector(double x, double y, double z){
         this.x = x;
         this.y = y;
         this.z = z;
    }

    public Vector(){
        this(0,0,0);
    }

    public void update(double dx, double dy, double dz){
        this.x += dx;
        this.y += dy;
        this.z += dz;
    }

    public static Vector sum(Vector a, Vector b){
        return new Vector(a.x+b.x, a.y+b.y, a.z+b.z);
    }

    public Vector invert(){
        return new Vector(-x,-y,-z);
    }

    public double xComponent(){
        return this.x;
    }

    public double yComponent(){
        return this.y;
    }

    public double zComponent(){
        return this.z;
    }

    public double xDifference(Vector other){
        return (this.x - other.x);
    }

    public double yDifference(Vector other){
        return (this.y - other.y);
    }

    public double zDifference(Vector other){
        return (this.z - other.z);
    }

    public double distanceFrom(Vector other){
        return Math.sqrt(
                xDifference(other) * xDifference(other)
                + yDifference(other) * yDifference(other)
                + zDifference(other) * zDifference(other)
        );
    }

    public String toString(){
        return "(" + x  + ", " + y + ", " + z + ")";
    }
}
