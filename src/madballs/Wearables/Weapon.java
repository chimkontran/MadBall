/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.Wearables;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.scene.image.Image;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import madballs.Projectiles.Projectile;
import madballs.*;
import madballs.Collision.CollisionEffect;
import madballs.Collision.CollisionPassiveBehaviour;

/**
 *
 * @author Caval
 */
public abstract class Weapon extends GameObject{
    private Image projectileImage;
    private double projectileHitBoxSize;
    private Paint projectileColor;
    
    private CollisionEffect projectileCollisionEffect;
    private CollisionPassiveBehaviour projectileCollisionBehaviour;
    private LongProperty lastShotTime = new SimpleLongProperty(0);
    private double height, width;
    private Ball owner;
    private double damage = -1, fireRate = -1, range = -1, ammo = -1, projectileSpeed = -1;

    public Ball getOwner() {
        return owner;
    }

    public void setOwner(Ball owner) {
        this.owner = owner;
    }

    public double getDamage() {
        return damage;
    }

    final public void setDamage(double damage) {
        this.damage = damage;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public Paint getProjectileColor() {
        return projectileColor;
    }

    public void setProjectileColor(Paint projectileColor) {
        this.projectileColor = projectileColor;
    }
    
    
    public long getLastShotTime() {
        return lastShotTime.get();
    }

    public void setLastShotTime(long lastShotTime) {
        this.lastShotTime.set(lastShotTime);
    }

    public double getFireRate() {
        return fireRate;
    }

    final public void setFireRate(double fireRate) {
        this.fireRate = fireRate;
    }

    public double getRange() {
        return range;
    }

    final public void setRange(double range) {
        this.range = range;
    }

    public double getAmmo() {
        return ammo;
    }

    final public void setAmmo(double ammo) {
        this.ammo = ammo;
    }

    public double getProjectileSpeed() {
        return projectileSpeed;
    }

    final public void setProjectileSpeed(double projectileSpeed) {
        this.projectileSpeed = projectileSpeed;
    }

    public Weapon(Ball owner, double x, double y) {
        super(owner, x, y, true);
        this.owner = owner;
        setMoveBehaviour(new RotateBehaviour(this, -1));
    }
    public Weapon(Environment environment, double x, double y) {
        super(environment, x, y, true);
        setMoveBehaviour(new RotateBehaviour(this, -1));
    }

    public CollisionEffect getProjectileCollisionEffect() {
        return projectileCollisionEffect;
    }

    final public void setProjectileCollisionEffect(CollisionEffect projectileCollisionEffect) {
        this.projectileCollisionEffect = projectileCollisionEffect;
    }

    public CollisionPassiveBehaviour getProjectileCollisionBehaviour() {
        return projectileCollisionBehaviour;
    }

    final public void setProjectileCollisionBehaviour(CollisionPassiveBehaviour projectileCollisionBehaviour) {
        this.projectileCollisionBehaviour = projectileCollisionBehaviour;
    }

    public Image getProjectileImage() {
        return projectileImage;
    }

    final public void setProjectileImage(Image projectileImage) {
        this.projectileImage = projectileImage;
    }

    public double getProjectileHitBoxSize() {
        return projectileHitBoxSize;
    }

    final public void setProjectileHitBoxSize(double size) {
        this.projectileHitBoxSize = size;
    }
    
    public void attack(long now){
        if ((now - getLastShotTime()) / 1_000_000_000.0  >  1  / fireRate){
            if (getLastShotTime() == 0) setLastShotTime(getEnvironment().getLastUpdateTime());
            new Projectile(this, new Circle(projectileHitBoxSize, projectileColor), projectileImage);
            setLastShotTime(now);
        }
    };
    
    @Override
    public void update(long now) {
//        System.out.println(owner.getTranslateY());
//        System.out.println(getTranslateY());
        if (getMoveBehaviour().isMousePressed()) attack(now);
        getMoveBehaviour().move(now);
    }
}
