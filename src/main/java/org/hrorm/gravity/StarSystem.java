package org.hrorm.gravity;

import java.util.*;

public class StarSystem {

    // masses in kilograms
    // radius in meters
    private static final Body sun = Body.builder().name("sun").mass(1.9891e30).radius(6.957e8).build();
    private static final Body earth = Body.builder().name("earth").mass(5.972e24).radius(6.371e6).build();
    private static final Body moon = Body.builder().name("moon").mass(7.3476e22).radius(1.736e6).build();

    // distance in meters: earth 150 million kilometers = 1.5e11 meters
    // earth speed is 30 kilometers/second = 30,000 m/s
    private static final LocatedBody[] SUN_AND_EARTH =
            {   new LocatedBody(sun, new Vector(0,0,0), new Vector(0,0,0))
                    , new LocatedBody(earth, new Vector(1.51e11,0,0), new Vector(0,30000,0))
            };

    // moon is 385,000 kilometers from earth = 3.85e8 meters
    // moon travels about 1.022 kilometers/second = 1,022 m/s
    private static final LocatedBody[] EARTH_AND_MOON =
            {new LocatedBody(earth, new Vector(0, 0, 0), new Vector(0, 0, 0)),
                    new LocatedBody(moon, new Vector(3.85e8, 0, 0 ), new Vector(0, 1022,0))
            };

    private static final LocatedBody[] SUN_EARTH_AND_MOON =
            {new LocatedBody(sun, new Vector(0, 0, 0), new Vector(0, 0, 0))
                    , new LocatedBody(earth, new Vector(1.51e11, 0, 0), new Vector(0, 30000, 0))
                    , new LocatedBody(moon, new Vector(1.51e11 + 3.85e8, 0, 0), new Vector(0, 31022, 0))
            };

    // in seconds
    private final double tickSize = 1;
    private final Map<String,LocatedBody> bodies = new HashMap<>();

    public StarSystem(){
        this(SUN_EARTH_AND_MOON);
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
        return size * 1.2;
    }

    public List<LocatedBody> getLocatedBodies(){
        List<LocatedBody> list = new ArrayList<>();
        list.addAll(bodies.values());
        return list;
    }

    public static Vector forceCalculation(LocatedBody a, LocatedBody b){
        double distance = a.getLocation().distanceFrom(b.getLocation());
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
