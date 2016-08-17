/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.wearables;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import madballs.collision.DamageEffect;
import madballs.collision.DisappearBehaviour;
import madballs.collision.PushBackEffect;
import madballs.collision.PushableBehaviour;
import madballs.collision.WeaponIgnoredBehaviour;
import madballs.GameObject;

public class M4A1 extends Weapon{
    private final double WIDTH = 40;
    private final double HEIGHT = 5;
    
    public M4A1(GameObject owner) {
        super(owner, 
                owner.getHitBox().getBoundsInLocal().getWidth() * 0.25,
                owner.getHitBox().getBoundsInLocal().getHeight() * 0.25);
        
        setCollisionEffect(new PushBackEffect(null, -1));
        setCollisionPassiveBehaviour(new PushableBehaviour(null));
        
        setDamage(15);
        setAmmo(-1);
        setFireRate(4);
        setRange(600);
        setProjectileSpeed(500);
        setProjectileHitBoxSize(3);
        setProjectileColor(Paint.valueOf("gray"));
        
        setFireSoundFX("M4A1");
        setProjectileCollisionEffect(new DamageEffect(null, getDamage()));
        setProjectileCollisionBehaviour(new WeaponIgnoredBehaviour(new DisappearBehaviour(null)));
    }
    
//    public M4A1(Environment environment, int X, int Y) {
//        super(environment, X, Y);
//        
//        setCollisionEffect(new PushBackEffect(null, -1));
//        setCollisionPassiveBehaviour(new PushableBehaviour(null));
//        
//        setDamage(100);
//        setAmmo(-1);
//        setFireRate(1);
//        setRange(800);
//        setProjectileSpeed(800);
//        setProjectileHitBoxSize(5);
//        setProjectileColor(Paint.valueOf("yellow"));
//        
//        setProjectileCollisionEffect(new DamageEffect(null, getDamage()));
//        setProjectileCollisionBehaviour(new WeaponIgnoredBehaviour(new DisappearBehaviour(null)));
//    }

    @Override
    public void setDisplayComponents() {
        setWidth(WIDTH);
        setHeight(HEIGHT);
        setHitBox(new Rectangle(getWidth(), getHeight(), Paint.valueOf("gray")));
    }
    
}
