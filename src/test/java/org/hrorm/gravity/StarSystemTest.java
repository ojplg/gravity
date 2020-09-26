package org.hrorm.gravity;

import org.junit.Assert;
import org.junit.Test;

import java.awt.*;
import java.util.Map;

public class StarSystemTest {

    @Test
    public void testForceCalculation_EqualMasses(){

        Body a = new Body("a", 1e10, 1e6, Color.RED);
        Body b = new Body("b", 1e10, 1e6, Color.RED);

        Vector aLocation = new Vector(0,0,0);
        Vector bLocation = new Vector(1e12, 0, 0);

        LocatedBody aLocatedBody = new LocatedBody(a, aLocation, new Vector());
        LocatedBody bLocatedBody = new LocatedBody(b, bLocation, new Vector());

        Vector force = StarSystem.forceCalculation(aLocatedBody, bLocatedBody);

        // No forces along y or z axis
        Assert.assertEquals(force.yComponent(),0.0, 0e-12);
        Assert.assertEquals(force.zComponent(),0.0, 0e-12);

        Assert.assertTrue(force.xComponent() > 0);
    }

    @Test
    public void testForceCalculation_EqualMasses_ThreeDimensions(){

        Body a = new Body("a", 1e10, 1e6, Color.WHITE);
        Body b = new Body("b", 1e10, 1e6, Color.WHITE);

        Vector aLocation = new Vector(0,0,0);
        Vector bLocation = new Vector(1e10, 1e10, 1e10);

        LocatedBody aLocatedBody = new LocatedBody(a, aLocation, new Vector());
        LocatedBody bLocatedBody = new LocatedBody(b, bLocation, new Vector());

        StarSystem starSystem = new StarSystem(aLocatedBody, bLocatedBody);

        Map<String, Vector> forces = starSystem.computeForces();

        Vector forceOnA = forces.get("a");
        Vector forceOnB = forces.get("b");

        Assert.assertTrue(forceOnA.xComponent() > 0.0);
        Assert.assertEquals(forceOnA.xComponent(), forceOnA.yComponent(), 1e-100);
        Assert.assertEquals(forceOnA.xComponent(), forceOnA.zComponent(), 1e-100);

        Assert.assertEquals(forceOnA.xComponent(), -forceOnB.xComponent(), 1e-100);
        Assert.assertEquals(forceOnA.yComponent(), -forceOnB.yComponent(), 1e-100);
        Assert.assertEquals(forceOnA.zComponent(), -forceOnB.zComponent(), 1e-100);

    }

    @Test
    public void testForceCalculation_ThreeMasses(){

        // three masses in an isoceles triangle
        // at the vertex is one with a larger mass

        Body a = new Body("a", 8e10, 1e6, Color.DARK_GRAY);
        Body b = new Body("b", 1e10, 1e6, Color.DARK_GRAY);
        Body c = new Body("c", 1e10, 1e6, Color.DARK_GRAY);

        Vector aLocation = new Vector(0,5e9,0);
        Vector bLocation = new Vector(1e10, 0, 0);
        Vector cLocation = new Vector(1e10, 1e10, 0);

        LocatedBody aLocatedBody = new LocatedBody(a, aLocation, new Vector());
        LocatedBody bLocatedBody = new LocatedBody(b, bLocation, new Vector());
        LocatedBody cLocatedBody = new LocatedBody(c, cLocation, new Vector());

        StarSystem starSystem = new StarSystem(aLocatedBody, bLocatedBody, cLocatedBody);

        Map<String, Vector> forces = starSystem.computeForces();

        Vector forceOnA = forces.get("a");
        Vector forceOnB = forces.get("b");
        Vector forceOnC = forces.get("c");

        // force on b and c should have equal magnitude
        Assert.assertEquals(forceOnB.absoluteValue(), forceOnC.absoluteValue(), 1e-100);

        // force on a should be larger in magnitude
        Assert.assertTrue(forceOnA.absoluteValue() > forceOnB.absoluteValue());

        // Force on A should have no y or z component, just a positive pull in the x dimension
        Assert.assertTrue(forceOnA.xComponent() > 0);
        Assert.assertEquals(forceOnA.yComponent(), 0.0 , 1e-100);
        Assert.assertEquals(forceOnA.zComponent(), 0.0 , 1e-100);

        // Force on B and C should have an equal negative component in the x direction
        Assert.assertTrue(forceOnB.xComponent() < 0.0 );
        Assert.assertEquals(forceOnB.xComponent() , forceOnC.xComponent(), 1e-100 );

        // Force on B and C in the y direction should be equal size and opposite
        Assert.assertTrue (forceOnB.yComponent() > 0);
        Assert.assertEquals(forceOnB.yComponent() , -forceOnC.yComponent(), 1e-100 );

        // No force on B or C in the Z direction
        Assert.assertEquals(forceOnB.zComponent(), 0.0, 1e-100);
        Assert.assertEquals(forceOnC.zComponent(), 0.0, 1e-100);
    }


}
