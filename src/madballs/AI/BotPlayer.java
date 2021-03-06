package madballs.AI;

import javafx.animation.AnimationTimer;
import madballs.Environment;
import madballs.GameObject;
import madballs.MadBalls;
import madballs.multiplayer.Data;
import madballs.multiplayer.Server;
import madballs.player.Player;
import madballs.projectiles.Projectile;

import java.util.*;

/**
 * the AI Player that controls its Ball by itself
 * Created by caval on 27/08/2016.
 */
public class BotPlayer extends Player {
    private static ArrayList<BotPlayer> botPlayers = new ArrayList<>();
    private final double THOUGHTS_PER_SECONDS = 20;
    private BotClient botClient = new BotClient();
    private ArrayList<Strategy> strategies = new ArrayList<>();
    private long lastThoughtTime = 0;

    public ArrayList<Strategy> getStrategies() {
        return strategies;
    }

    public long getLastThoughtTime() {
        return lastThoughtTime;
    }

    public static ArrayList<BotPlayer> getBotPlayers() {
        return botPlayers;
    }

    private final AnimationTimer animation = new AnimationTimer() {
        @Override
        public void handle(long now) {
            if (getBall().isDead()){
                // reset the objective to reach center of MoveStrategy
                for (Strategy strategy: strategies){
                    if (strategy instanceof MoveStrategy){
                        ((MoveStrategy) strategy).setCenterReached(false);
                        break;
                    }
                }
                return;
            }
            if (now - lastThoughtTime >= 1000000000 / THOUGHTS_PER_SECONDS){
                prepareStrategies();
                lastThoughtTime = now;

                Environment environment = getBall().getEnvironment();
                for (Integer id: getRelevantObjIDs()){
                    for (Strategy strategy: strategies){
                        GameObject object = environment.getObject(id);
                        if (object != null && object != getBall() && object != getBall().getWeapon()){
                            strategy.consider(object);
                        }
                    }
                }

                updateStrategies();
                makeActions();
            }
        }
    };

    /**
     * call the prepare() method of all strategies
     */
    private void prepareStrategies(){
        for (Strategy strategy: strategies){
            strategy.prepare();
        }
    }

    /**
     * update the importance of all strategies and sort them by their importance.
     * the most important strategy would be placed at the last so that its actions can override the less important strategies' actions
     */
    private void updateStrategies(){
        for (Strategy strategy: strategies){
            strategy.updateImportance();
        }

        Collections.sort(strategies, Comparator.comparingDouble(strategy -> strategy.getImportance()));
    }

    /**
     * make actions with each strategy
     */
    private void makeActions(){
        for (Strategy strategy: strategies){
            strategy.act();
        }
    }

    public BotClient getBotClient() {
        return botClient;
    }

    /**
     * start processing the environment and make actions
     */
    public void play(){
        for (Strategy strategy: strategies){
            if (strategy instanceof MoveStrategy){
                ((MoveStrategy) strategy).setMap(getBall().getEnvironment().getMap());
            }
        }
        animation.start();
    }

    /**
     * stop playing
     */
    public void stop(){
        animation.stop();
        getRelevantObjIDs().clear();
    }

    public BotPlayer() {
        super(null, false);
        botPlayers.add(this);
        botClient.setLocalPlayer(this);
        strategies.add(new DodgeStrategy(this));
        strategies.add(new AvoidObstacleStrategy(this));
        strategies.add(new AttackStrategy(this));
        strategies.add(new MoveStrategy(this));
    }

    @Override
    public void sendData(Data data){
//        System.out.println("bot send " + data.getType());
        Server server = (Server) MadBalls.getMultiplayerHandler();
        server.handleData(this, data);
    }
}
