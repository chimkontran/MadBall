/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.item;

import javafx.scene.paint.Color;
import madballs.Environment;
import madballs.buffState.Rejuvenation;
import madballs.map.SpawnLocation;

/**
 *
 * @author caval
 */
public class MiniHealthFlask extends BuffItem{
    
    public MiniHealthFlask(Environment environment, SpawnLocation spawnLocation) {
        super(environment, spawnLocation, new Rejuvenation(null, 5, 30));
    }
    
    
    
    @Override
    public void setDisplayComponents(){
        setColor(Color.LIGHTGREEN);
        super.setDisplayComponents();
    }
    
}
