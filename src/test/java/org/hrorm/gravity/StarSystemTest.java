package org.hrorm.gravity;

import org.junit.Assert;
import org.junit.Test;

public class StarSystemTest {

    @Test
    public void testForceCalculation_EqualMasses(){

        Body a = new Body("a", 1e10, 1e6);
        Body b = new Body("b", 1e10, 1e6);

        Vector aLocation = new Vector(0,0,0);
        Vector bLocation = new Vector(1e12, 0, 0);

        LocatedBody aLocatedBody = new LocatedBody(a, aLocation);
        LocatedBody bLocatedBody = new LocatedBody(b, bLocation);

        Vector force = StarSystem.forceCalculation(aLocatedBody, bLocatedBody);

        // No forces along y or z axis
        Assert.assertEquals(force.yComponent(),0.0, 0e-12);
        Assert.assertEquals(force.zComponent(),0.0, 0e-12);

        Assert.assertTrue(force.xComponent() > 0);
    }

}
