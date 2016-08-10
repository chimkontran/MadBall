/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs;

import java.io.Serializable;

/**
 *
 * @author caval
 */
public class GameObjState implements Serializable{
    private double translateX;
    private double translateY;
    private double oldX, oldY;
    private double direction;
    private double oldDirection;
    private double hp;
    private int objectIndex;
    private int updateIndex;
    
    public int getObjectIndex(){
        return objectIndex;
    }
    
    public int getUpdateIndex(){
        return updateIndex;
    }

    public double getTranslateX() {
        return translateX;
    }

    public double getTranslateY() {
        return translateY;
    }

    public double getDirection() {
        return direction;
    }

    public double getOldX() {
        return oldX;
    }

    public double getOldY() {
        return oldY;
    }

    public double getOldDirection() {
        return oldDirection;
    }

    public double getHp() {
        return hp;
    }
    
    public boolean isSimilarTo(GameObjState state){
        return (
                state.translateX == translateX
                && state.translateY == translateY
                && state.oldX == oldX
                && state.oldY == oldY
                && state.direction == direction
                && state.oldDirection == oldDirection
                && state.hp == hp
                && state.objectIndex == objectIndex
            );
    }
    
    public GameObjState(GameObject obj){
        updateIndex = obj.getEnvironment().getUpdateIndex();
        translateX = obj.getTranslateX();
        translateY = obj.getTranslateY();
        direction = Math.toRadians(obj.getRotateAngle());
        oldX = obj.getOldX();
        oldY = obj.getOldY();
        oldDirection = obj.getOldDirection();
        hp = obj.getHpValue();
        objectIndex = obj.getIndex();
    }
}