/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.multiplayer;

/**
 * the Data of Mouse input
 * @author caval
 */
public class MouseInputData extends Data{
    private String eventType;
    private double x, y, scale;
//
    public double getScale() {
        return scale;
    }

    public String getEventType() {
        return eventType;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public MouseInputData(String eventType, double x, double y, double scale) {
        super("input_mouse");
//        System.out.println(eventType);
        this.eventType = eventType;
        this.x = x;
        this.y = y;
        this.scale = scale;
    }
    
}
