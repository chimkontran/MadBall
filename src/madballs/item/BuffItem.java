/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.item;

import javafx.scene.shape.Circle;
import madballs.Environment;
import madballs.collision.GiveBuffEffect;
import madballs.buffState.BuffState;
import madballs.map.SpawnLocation;

/**
 *
 * @author haing
 */
public class BuffItem extends Item{
  protected GiveBuffEffect giveBuffEffect;
  
  public BuffItem(Environment environment, SpawnLocation spawnLocation, BuffState effectState, Integer id) {
    super(environment, spawnLocation, id);
    giveBuffEffect = new GiveBuffEffect(null, effectState);
    setCollisionEffect(giveBuffEffect);
  }  
  
  @Override
  public void setDisplayComponents() {
    setSize(15);
    setHitBox(new Circle(getSize(), getColor()));
  }
}
