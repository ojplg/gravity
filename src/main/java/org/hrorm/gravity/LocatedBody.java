package org.hrorm.gravity;

import java.awt.*;

public class LocatedBody {

    private final Body body;

    private final Vector location;

    private final Vector velocity;

    public LocatedBody(Body body, Vector location, Vector velocity) {
        this.body = body;
        this.location = location;
        this.velocity = velocity;
    }

    public Body getBody() {
        return body;
    }

    public Vector getLocation() {
        return location;
    }

    public Vector getVelocity() {
        return velocity;
    }

    public double getRadius(){
        return body.getRadius();
    }

    public String getName(){
        return body.getName();
    }

    public Color getColor(){
        return body.getColor();
    }
}
