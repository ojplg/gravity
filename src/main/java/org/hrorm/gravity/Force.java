package org.hrorm.gravity;public class Force {

    // meters^3/(kilogram * seconds^2)
    private static final double GRAVITATIONAL_CONSTANT = 6.67430e-11;

    // distance in meters
    public static double strength(Body a, Body b, double distance){
        return GRAVITATIONAL_CONSTANT * a.getMass() * b.getMass() / ( distance * distance );
    }

}
