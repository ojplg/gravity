package org.hrorm.gravity;public class LocatedBody {

    private final Body body;

    private final Vector location;

    public LocatedBody(Body body, Vector location) {
        this.body = body;
        this.location = location;
    }

    public Body getBody() {
        return body;
    }

    public Vector getLocation() {
        return location;
    }


}
