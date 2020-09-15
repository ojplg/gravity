package org.hrorm.gravity;public class LocatedBody {

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
}
