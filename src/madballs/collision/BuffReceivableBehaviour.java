/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.collision;

import javafx.scene.shape.Shape;
import madballs.Ball;
import madballs.GameObject;
import madballs.MadBalls;
import madballs.buffState.BuffState;
import madballs.multiplayer.BuffData;

/**
 * allows a Ball to receive BuffState
 * @author chim-
 */
public class BuffReceivableBehaviour extends StackedCollisionPassiveBehaviour{
    
    public BuffReceivableBehaviour(StackedCollisionPassiveBehaviour behaviour) {
        super(behaviour);
    }
    
    @Override
    public void uniqueGetAffected(GameObject source, GameObject target, StackedCollisionEffect effect, Shape collisionShape) {
        GiveBuffEffect receivedEffect = (GiveBuffEffect)effect;
        BuffState buffState = receivedEffect.getBuffState();
        buffState.castOn((Ball)target, 0);
        MadBalls.getMultiplayerHandler().sendData(new BuffData(buffState));
        ((Ball)target).addBuffState(buffState);
    }

    @Override
    protected boolean isConditionMet(GameObject source, GameObject target, StackedCollisionEffect effect, Shape collisionShape) {
        return MadBalls.isHost() && effect instanceof GiveBuffEffect;
    }
}
