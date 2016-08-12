/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs;

import java.util.ArrayList;
import java.util.List;
import javafx.animation.AnimationTimer;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import madballs.item.Spawner;
import madballs.map.Map;

/**
 *
 * @author Caval
 */
public class Environment {
    private ArrayList<GameObject> gameObjects;
    private LongProperty lastUpdateTime = new SimpleLongProperty(0);
    private Spawner itemSpawner;
    private Pane root;
    private Map map;
    private Ground ground;
    private Quadtree quadtree;
    private Scene scene;
    private int updateIndex = 0;
    
    public int getNumObjects(){
        return gameObjects.size();
    }
    
    public GameObject getObject(int index){
        return gameObjects.get(index);
    }
    
    public int getObjectIndex(GameObject object){
        return gameObjects.indexOf(object);
    }
    
    public Spawner getItemSpawner() {
      return itemSpawner;
    }
    public long getLastUpdateTime() {
        return lastUpdateTime.get();
    }
    
    public int getUpdateIndex(){
        return updateIndex;
    }
    
    public Ground getGround() {
        return ground;
    }
    
    final AnimationTimer animation = new AnimationTimer() {
        @Override
        public void handle(long now) {
            lastUpdateTime.set(now);
            update(now);
        }
    };
    
    /**
     * check through all game objs in the environment to see which obj has collided with one another
     */
    private void update(long now){
//        boolean isHost = MadBalls.getMultiplayerHandler().getLocalPlayer().isHost();
      
        ArrayList<GameObject> copiedGameObjects = new ArrayList<>(gameObjects);
//        ArrayList<GameObject> deadGameObjects = new ArrayList<>();
//        copiedGameObjects.addAll(gameObjects.subList(0, gameObjects.size()));
        quadtree.clear();
        
        for (GameObject obj : copiedGameObjects){
            obj.update(now);
//            obj.updateBoundsRectangle();
            if (obj.isDead()) {
//                deadGameObjects.add(obj);
            }
            else {
                quadtree.insert(obj);
            }
        }
        
//        for (GameObject obj : deadGameObjects){
//            removeGameObj(obj);
//            copiedGameObjects.remove(obj);
//        }
        
//        copiedGameObjects = new ArrayList<>(gameObjects);
//        if (!isHost) return;
        //spawn items
//        itemSpawner.spawn(now);
        List<GameObject> collidableObjects = new ArrayList();
        ArrayList<GameObject> checked = new ArrayList<>();
//        ArrayList<GameObject> collidedObjects = new ArrayList<>();
        
//        boolean isUncollided = false;
        for (GameObject checking : copiedGameObjects){
            if (checking.isDead() || checking instanceof Obstacle) continue;
            collidableObjects.clear();
            quadtree.retrieve(collidableObjects, checking);
            for (GameObject target : collidableObjects){
//                if (checking instanceof Ball)System.out.println(target.getClass() + " x: " + target.getDisplay().getBoundsInParent().getMinX() + "; y: " + target.getDisplay().getBoundsInParent().getMinY());
//  if(checking instanceof Item){System.out.println("CHECKING ITEM");}
                if (target != checking && !checked.contains(target) && !target.hasChild(checking) && !target.hasOwner(checking)){
                    checking.checkCollisionWith(target);
//                        if (checking.checkCollisionWith(target)) {
//                            collidedObjects.add(target);
//                            collidedObjects.add(checking);
//                            GameObject owner = target.getOwner();
//                            while (owner != null){
//                                collidedObjects.add(owner);
//                                owner = owner.getOwner();
//                            }
//                            owner = checking.getOwner();
//                            while (owner != null){
//                                collidedObjects.add(owner);
//                                owner = owner.getOwner();
//                            }
//                        }
                }
            }
                checked.add(checking);
        }
        updateIndex++;
        
//        for (GameObject obj : copiedGameObjects){
//            if (collidedObjects.contains(obj)) return;
////            obj.setOldDirection(Math.toRadians(obj.getRotateAngle()));
////            if (obj instanceof Ball) {
////                System.out.println("");
////                System.out.println(obj.getLastStableX());
////                System.out.println(obj.getOldX());
////            }
//            obj.setLastStableX(obj.getTranslateX());
//            obj.setLastStableY(obj.getTranslateY());
//        }
    }

  public Map getMap() {
    return map;
  }
    

    public Environment(Pane display){
        this.itemSpawner = new Spawner(this);
        this.root = display;
        
        quadtree = new Quadtree(0, new Rectangle(-25, -25, MadBalls.RESOLUTION_X + 25, MadBalls.RESOLUTION_Y + 25));
        gameObjects = new ArrayList<>();
        ground = new Ground(this, 0, 0);
        
//        animation.start();
    }
    
    public void loadMap(Map map){
        this.map = map;
        //add the obstacles 
        for (int i = 0; i < 30; i++){
            for (int j = 0; j < 30; j++){
                if (map.getMAP_ARRAY()[i][j] == 1) {
                    new Obstacle(this, 
                        j * map.getLENGTH()/30, i * map.getHEIGHT()/20,
                        30, 30); 
                }
            }
        }
    }
    
    public void startAnimation(){
        animation.start();
    }
    
    /**
     * add new obj to the environment
     * @param obj 
     */
    public void registerGameObj(GameObject obj, boolean shouldAddDisplay){
        gameObjects.add(obj);
//        System.out.println(getObjectIndex(obj));
        if (shouldAddDisplay) root.getChildren().add(obj.getDisplay());
    }
    
    /**
     * remove an obj from the environment
     * @param obj 
     */
    public void removeGameObj(GameObject obj){
//        gameObjects.remove(obj);
        root.getChildren().remove(obj.getDisplay());
    }
}
