package org.hrorm.gravity;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {

    private final static int MINUTE = 60;
    private final static int HOUR = 60 * MINUTE;
    private final static int DAY = 24 * HOUR;

    private final static int FRAME_SIZE  = 900;
    private final static double DISPLAY_SIZE = 900;

    public static void main(String[] args) throws Exception {
        System.out.println("Starting");

        Frame frame = new Frame();
        frame.setSize(FRAME_SIZE,FRAME_SIZE);
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter(){
                    public void windowClosing(WindowEvent e) {
                        frame.dispose();
                    }
                });

        StarSystem system = new StarSystem();

        Display display = new Display(DISPLAY_SIZE, system.getSize());
        display.setVisible(true);

        frame.add(display);

        for(int idx = 0 ; idx<365*DAY; idx ++){
            if( idx % DAY == 0 ) {
                String time  = timeOutput(idx);
                System.out.println(time+ " " + system.positionOutputs());
                System.out.println(system.velocityOutputs());
                display.setBodiesToDraw(system.getLocatedBodies());
                display.repaint();

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
