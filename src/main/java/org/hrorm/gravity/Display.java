package org.hrorm.gravity;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class Display extends JPanel {

    private double distanceBoundary;
    private double pixelDimension;

    private List<LocatedBody> bodiesToDraw = new ArrayList<>();

    private List<Point2D.Double> pointsToDraw = new ArrayList<>();

    private List<Shape> shapesToDraw = new ArrayList<>();

    public Display(double pixelDimension, double distanceBoundary){
        this.pixelDimension = pixelDimension;
        this.distanceBoundary = distanceBoundary * 1.2;
        this.setOpaque(true);
        this.setBackground(Color.DARK_GRAY);
    }

    @Override
    public void paintComponent(Graphics graphics){
        super.paintComponent(graphics);

        this.setBackground(Color.DARK_GRAY);
        Graphics2D g2 = (Graphics2D) graphics;

        for( LocatedBody body : bodiesToDraw) {
            g2.setColor(body.getColor());
            Vector location = body.getLocation();
            Ellipse2D.Double ellipse = newEllipse(body.getRadius(),location.xComponent(), location.yComponent());
            g2.draw(ellipse);
        }

        for( Point2D.Double point : pointsToDraw){
            g2.setColor(Color.LIGHT_GRAY);
            g2.drawLine((int) point.x, (int) point.y,(int)  point.x,(int)  point.y);
        }
    }

    private Ellipse2D.Double newEllipse(double radius, double xDistance, double yDistance){

        double size = Math.max( 5.0 , pixelDimension * radius / distanceBoundary);

        double x = distanceToCoordinate(xDistance);
        double y = distanceToCoordinate(yDistance);

        pointsToDraw.add(new Point2D.Double(x,y));

        Ellipse2D.Double ellipse = new Ellipse2D.Double(x - (size/2), y - (size/2), size,  size);

        return ellipse;
    }

    private double distanceToCoordinate(double distance){
        return (pixelDimension + pixelDimension*distance/(distanceBoundary))/2;
    }

    public void setBodiesToDraw(List<LocatedBody> bodiesToDraw) {
        this.shapesToDraw.clear();



        this.bodiesToDraw = bodiesToDraw;
    }
}
