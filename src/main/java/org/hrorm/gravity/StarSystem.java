package org.hrorm.gravity;

import java.awt.*;
import java.util.*;
import java.util.List;

public class StarSystem {

    // masses in kilograms
    // radius in meters
    private static final Body sun = Body.builder().name("sun").mass(1.9891e30).radius(6.957e8).build();
    private static final Body earth = Body.builder().name("earth").mass(5.972e24).radius(6.371e6).build();
    private static final Body moon = Body.builder().name("moon").mass(7.3476e22).radius(1.736e6).build();
    private static final Body venus = Body.builder().name("venus").mass(4.8675e24).radius(6.0518e6).build();
    private static final Body mercury = Body.builder().name("mercury").mass(3.3011e23).radius(4.88e3).build();
    private static final Body mars = Body.builder().name("mars").mass(6.4171e23).radius(3.389e3).build();
    private static final Body phobos = Body.builder().name("phobos").mass(1.0659e16).radius(1.12667e6).build();
    private static final Body deimos = Body.builder().name("deimos").mass(1.4762e15).radius(6.2e5).build();

    // distance in meters: earth 150 million kilometers = 1.5e11 meters
    // earth speed is 30 kilometers/second = 30,000 m/s
    // moon is 385,000 kilometers from earth = 3.85e8 meters
    // moon travels about 1.022 kilometers/second = 1,022 m/s

    // venus orbit about 225 days
    // mars orbit about 687
    // mercury orbit 89 days

    private static final LocatedBody[] INNER_PLANETS =
            {new LocatedBody(sun, new Vector(0, 0, 0), new Vector(0, 0, 0))
                    , new LocatedBody(earth, new Vector(1.512e11, 0, 0), new Vector(0, 29290, 0))
                    , new LocatedBody(moon, new Vector(1.512e11 + 3.85e8, 0, 0), new Vector(0, 29290 + 1022, 0))
                    , new LocatedBody(venus, new Vector(1.0894e11, 0, 0), new Vector(0,34780, 0))
                    , new LocatedBody(mercury, new Vector(6.9816e10, 0, 0), new Vector(0,38860, 0))
                    , new LocatedBody(mars, new Vector(2.492e11, 0, 0), new Vector(0,22000, 0))
                    , new LocatedBody(phobos, new Vector(2.492e11 + 9.51758e6, 0, 0), new Vector(0,22000 + 2138, 0))
                    , new LocatedBody(deimos, new Vector(2.492e11 + 2.3455e7, 0, 0), new Vector(0,22000 + 1351, 0))
            };

    private static final Body alpha = Body.builder().name("alpha").mass(2.1e30).radius(7e8).color(Color.RED).build();
    private static final Body beta = Body.builder().name("beta").mass(1.7e30).radius(6e8).color(Color.YELLOW).build();
    private static final Body green = Body.builder().name("green").mass(5e24).radius(6e6).color(Color.GREEN).build();

    private static final LocatedBody[] BINARY_SYSTEM =
            {
                    new LocatedBody( alpha, new Vector(2e9, 0, 0 ), new Vector(0,56000, 0))
                  ,  new LocatedBody( beta, new Vector(-5e9, 0, 0 ), new Vector(0,-70000, 0))
                  ,  new LocatedBody( green, new Vector(2e11, 0, 0 ), new Vector(0,38000, 0))
            };


    // in seconds
    private final double tickSize = 1;
    private final Map<String,LocatedBody> bodies = new HashMap<>();

    public StarSystem(){
        this(BINARY_SYSTEM);
    }

    public StarSystem(LocatedBody ... bods){
        for(LocatedBody body : bods){
            bodies.put(body.getBody().getName(), body);
        }
    }

    public String positionOutputs() {
        StringBuffer buf = new StringBuffer();
        buf.append("Positions: ");
        for(Map.Entry<String, LocatedBody> entry : bodies.entrySet()){
            buf.append(entry.getKey());
            buf.append(" ");
            buf.append(entry.getValue().getLocation());
        }

        return buf.toString();
    }

    public String velocityOutputs() {
        StringBuffer buf = new StringBuffer();
        buf.append("Velocities: ");
        for(Map.Entry<String, LocatedBody> entry : bodies.entrySet()) {
            buf.append(entry.getKey());
            buf.append(" ");
            buf.append(entry.getValue().getVelocity());
        }
        return buf.toString();
    }


    public double getSize(){
        double size = 1;
        for(LocatedBody lb : bodies.values()){
            double distance = lb.getLocation().absoluteValue();
            if (distance > size){
                size = distance;
            }
        }
        return size * 1.1;
    }

    public List<LocatedBody> getLocatedBodies(){
        List<LocatedBody> list = new ArrayList<>();
        list.addAll(bodies.values());
        return list;
    }

    public static Vector forceCalculation(LocatedBody a, LocatedBody b){
        double distance = a.getLocation().distanceFrom(b.getLocation());

        if( distance <= a.getRadius()+ b.getRadius() ){
            throw new RuntimeException("CRASHED.");
        }

        double xDistance = b.getLocation().xDifference(a.getLocation());
        double yDistance = b.getLocation().yDifference(a.getLocation());
        double zDistance = b.getLocation().zDifference(a.getLocation());

        double forceMagnitude = Force.strength(a.getBody(), b.getBody(), distance);
        double xForce = forceMagnitude * xDistance / distance;
        double yForce = forceMagnitude * yDistance / distance;
        double zForce = forceMagnitude * zDistance / distance;

        return new Vector(xForce, yForce, zForce);
    }

    public void nextAll(){
        Map<String, Vector> forces = computeForces();

        for(LocatedBody locatedBody : bodies.values()){
            Vector velocity = locatedBody.getVelocity();
            Body body = locatedBody.getBody();
            Vector location = locatedBody.getLocation();
            String name = body.getName();
            Vector force = forces.get(name);

            double xVelocityDelta = velocityDelta(force.xComponent(), tickSize, body.getMass());
            double yVelocityDelta = velocityDelta(force.yComponent(), tickSize, body.getMass());
            double zVelocityDelta = velocityDelta(force.zComponent(), tickSize, body.getMass());

            velocity.update(xVelocityDelta, yVelocityDelta, zVelocityDelta);

            double xPositionUpdate = tickSize * velocity.xComponent();
            double yPositionUpdate = tickSize * velocity.yComponent();
            double zPositionUpdate = tickSize * velocity.zComponent();
            location.update(xPositionUpdate, yPositionUpdate, zPositionUpdate);
        }
    }

    public Map<String, Vector> computeForces(){

        List<String> names = new ArrayList<>(bodies.keySet());

        Map<String, Vector> forces = new HashMap();
        for(String name : names){
            forces.put(name, new Vector());
        }

        for(int idx = 0; idx<names.size() -1; idx++ ){
            for(int jdx=idx+1;jdx<names.size(); jdx++){
                String aName = names.get(idx);
                String bName = names.get(jdx);

                if( aName.equals( bName) ){
                    throw new RuntimeException("Name issue");
                }
                LocatedBody a = bodies.get(aName);
                LocatedBody b = bodies.get(bName);

                Vector pairwiseForce = forceCalculation(a, b);

                forces.compute (aName, (name, force) -> Vector.sum(force, pairwiseForce));
                forces.compute (bName, (name, force) -> Vector.sum(force, pairwiseForce.negate()));
            }
        }
        return forces;
    }

    private double velocityDelta(double force, double time, double mass){
        return force * time / mass;
    }

}
