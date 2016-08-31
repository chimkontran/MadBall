/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.wearables;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import madballs.collision.DamageEffect;
import madballs.collision.PushBackEffect;
import madballs.collision.PushableBehaviour;
import madballs.GameObject;

/**
 *
 * @author Caval
 */
public class Pistol extends Weapon{
    private final double WIDTH = 20;
    private final double HEIGHT = 7;

    public Pistol(GameObject owner, Integer id) {
        super(owner, 
                owner.getHitBox().getBoundsInLocal().getWidth() * 0.25,
                owner.getHitBox().getBoundsInLocal().getHeight() * 0.25, id);
        
        setCollisionEffect(new PushBackEffect(null, -1));
        setCollisionPassiveBehaviour(new PushableBehaviour(null));
        
        setDamage(15);
        setAmmo(-1);
        setFireRate(1);
        setRange(700);
        setProjectileSpeed(500);
        setProjectileHitBoxSize(3);
        setProjectileColor(Paint.valueOf("red"));
        setProjectileImageName("bullet1");

        setFireSoundFX("pistol");
        setProjectileCollisionEffect(new DamageEffect(null, getDamage(), owner.getID()));

    }
    
    @Override
    public void setDisplayComponents() {
        setWidth(WIDTH);
        setHeight(HEIGHT/2);
        setHitBox(new Rectangle(getWidth(), getHeight(), Paint.valueOf("red")));
        setImage("pistol");
        configImageView(0, -HEIGHT/2, HEIGHT, WIDTH);
    }
    
}
