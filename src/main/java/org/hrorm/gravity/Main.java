package org.hrorm.gravity;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOError;

public class Main {

    private final static int MINUTE = 60;
    private final static int HOUR = 60 * MINUTE;
    private final static int DAY = 24 * HOUR;

    private final static int FRAME_SIZE  = 900;
    private final static double DISPLAY_SIZE = 900;

    // millis between draws
    private final static long CADENCE = 25;

    public static void main(String[] args) throws Exception {

        final boolean[] running = {true};

        System.out.println("Starting");

        JFrame frame = new JFrame();
        frame.setSize(FRAME_SIZE,FRAME_SIZE);
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter(){
                    public void windowClosing(WindowEvent e) {
                        running[0] = false;
                        frame.dispose();
                    }
                });

        StarSystem system = new StarSystem();

        Display display = new Display(DISPLAY_SIZE, system.getSize());
        display.setVisible(true);

        frame.add(display);

        long lastDrawTime = System.currentTimeMillis();

        for(int idx = 0 ; idx<1000*DAY; idx ++){
            if( idx % HOUR == 0 ) {
                long now = System.currentTimeMillis();

                if( lastDrawTime + CADENCE > now ){
                    try {
                        Thread.sleep((lastDrawTime + CADENCE) - now);
                    } catch (InterruptedException ie){
                    }
                }

                String time  = timeOutput(idx);
                System.out.println(time+ " " + system.positionOutputs());
                System.out.println(system.velocityOutputs());
                display.setBodiesToDraw(system.getLocatedBodies());

                display.setTimeOutput(time);
                display.repaint();


                lastDrawTime = System.currentTimeMillis();

                if( ! running[0]){
                    break;
                }

            }
            system.nextAll();
        }

    }



    private static String timeOutput(int seconds){
        int days = seconds / DAY;
        int remainder = seconds % DAY;
        int hours = remainder / HOUR;
        remainder = remainder % HOUR;
        int minutes = remainder / MINUTE;
        int ss = remainder % MINUTE;

        return "" + days + " " + hours + ":" + minutes + ":" + ss;

    }

}
