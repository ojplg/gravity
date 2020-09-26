package org.hrorm.gravity;

import java.awt.*;

@lombok.Builder
@lombok.Data
public class Body {

    private static final Color DEFAULT_COLOR = Color.WHITE;

    private final String name;
    // kilograms
    private final double mass;
    // meters
    private final double radius;

    private final Color color;

    public Color getColor(){
        if( color == null){
            return DEFAULT_COLOR;
        }
        return color;
    }
}
