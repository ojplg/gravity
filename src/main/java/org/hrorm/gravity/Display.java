package org.hrorm.gravity;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.awt.*;

public class Display extends Panel {

    private double distanceBoundary;
    private double pixelDimension;

    private List<LocatedBody> bodiesToDraw = new ArrayList<>();

    private List<Point2D.Double> pointsToDraw = new ArrayList<>();

    public Display(double pixelDimension, double distanceBoundary){
        this.pixelDimension = pixelDimension;
        this.distanceBoundary = distanceBoundary * 1.2;
    }

    @Override
    public void paint(Graphics graphics){
        Graphics2D g2 = (Graphics2D) graphics;

        for( LocatedBody body : bodiesToDraw) {
            Vector location = body.getLocation();
            Ellipse2D.Double ellipse = newEllipse(location.xComponent(), location.yComponent());
            g2.draw(ellipse);
        }

        for( Point2D.Double point : pointsToDraw){
            g2.drawLine((int) point.x, (int) point.y,(int)  point.x,(int)  point.y);
        }
    }

    private Ellipse2D.Double newEllipse(double xDistance, double yDistance){

        double x = distanceToCoordinate(xDistance);
        double y = distanceToCoordinate(yDistance);

        pointsToDraw.add(new Point2D.Double(x,y));

        System.out.println("Drawing at "  + x +"," + y);

        Ellipse2D.Double ellipse = new Ellipse2D.Double(x, y, 5.0, 5.0);

        return ellipse;
    }

    private double distanceToCoordinate(double distance){
        return (pixelDimension + pixelDimension*distance/(distanceBoundary))/2;
    }

    public void setBodiesToDraw(List<LocatedBody> bodiesToDraw) {
        this.bodiesToDraw = bodiesToDraw;
    }
}
