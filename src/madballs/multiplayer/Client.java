package madballs.multiplayer;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import madballs.*;
import madballs.buffState.BuffState;
import madballs.map.Map;
import madballs.map.SpawnLocation;
import madballs.player.Player;
import madballs.scenes.Navigation;
import madballs.scenes.ScenesFactory;
import madballs.scenes.controller.GameRoomController;
import madballs.wearables.Weapon;
import madballs.wearables.XM1104;

/**
 * Client class handles major socket operations on the client side
 * @author Caval
 */
public class Client extends MultiplayerHandler{

    public Client() {
        super(false);
    }
    
    /**
     * connect to the server as client via socket
     */
    public void init(){
        // get the server ip
        String address = Navigation.getInstance().getTextResponse("Start game", "Join a game", "Enter host's server address", "127.0.0.1");
        setService(new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        try {
                            // connect socket
//                            System.out.println("why");
                            getLocalPlayer().setSocket(new Socket(address, 8099));
                            sendData(new PlayerData(getLocalPlayer(), false));
                                    
                                // maintain the socket connection
                                while(true){
                                    try {
//                                        System.out.println("handle");
                                        handleData(getLocalPlayer().readData());
//                                        getLocalPlayer().sendData(new Data("haha"));
                                    }
                                    catch (Exception ex){
                                        return null;
                                    }
                                    finally {
                                        
                                    }
                                }
                        }
                        catch (IOException ex){
                            // retry if could not connect to server
                            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        return null;
                    }
                };
            }
        });
        getService().start();
    }

    @Override
    public void sendData(Data data) {
        getLocalPlayer().sendData(data);
    }
    
    @Override
    public void handleData(Data data){
        super.handleData(data);
        try {
            if (data.getType().equals("player")){
                PlayerData playerData = (PlayerData) data;
                Player player;
                if (playerData.isLocal()){
                    player = getLocalPlayer();
                }
                else {
                    player = new Player(null, false);
                    player.setName(playerData.getName());
                }
                player.setPlayerNum(playerData.getNumber());
                player.setTeamNum(playerData.getTeamNumber());
                getPlayers().add(player);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        ((GameRoomController) ScenesFactory.getInstance().getFxmlLoader().getController()).displayPlayer(player);
                    }
                });
            }
            else if (data.getType().equals("prepare")){
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        MadBalls.newGame(false);
                        Navigation.getInstance().navigate(MadBalls.getMainScene());
                    }
                });
            }
            else if (data.getType().equals("check_ready")){
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("ready");
                        sendData(new ReadyData(MadBalls.getAnimationScene().getWidth(), MadBalls.getAnimationScene().getHeight()));
                    }
                });
            }
            else if (data.getType().equals("start")){
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        MadBalls.getMainEnvironment().startAnimation();
                    }
                });
            }
            else if (data.getType().equals("restart")){
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        MadBalls.getMainEnvironment().stopAnimation();
                        Navigation.getInstance().navigate(ScenesFactory.getInstance().newScene("prepare"));
                    }
                });
            }
            else if (data.getType().equals("latency")){

            }
            else if (data.getType().equals("state")){
                StateData stateData = (StateData)data;
                Integer objID = stateData.getState().getObjID();
                if (objID < MadBalls.getMainEnvironment().getCurrentObjID()){
                    try{
                        StateLoader stateLoader = MadBalls.getMainEnvironment().getObject(objID).getStateLoader();
                        stateLoader.addServerState(stateData.getState());
                    }
                    catch (NullPointerException ex){
//                        if (stateData.getState().isDead()){
//                            return;
//                        }
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                GameObject object = MadBalls.getMainEnvironment().resurrectGameObj(objID);
                                if (object == null){
                                    return;
                                }
                                else {
                                    object.getStateLoader().addServerState(stateData.getState());
                                }
                            }
                        });

                    }
                }
                else {
                    Service<Void> service = new Service<Void>() {
                        @Override
                        protected Task<Void> createTask() {
                            return new Task<Void>() {
                                @Override
                                protected Void call() throws Exception {
                                    handleData(data);
                                    return null;
                                }
                            };
                        }
                    };
                    service.start();
                }
                

            }
            else if (data.getType().equals("spawn")){
                spawn((SpawnData)data);
            }
            else if (data.getType().equals("choose_map")){
                System.out.println("map");
                Map map = new Map(((MapData)data).getMapNumber());
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        MadBalls.getMainEnvironment().loadMap(map);
                    }
                });
            }
            else if (data.getType().equals("fire")){
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            FireData fireData = (FireData)data;
                            Weapon weapon = ((Weapon)MadBalls.getMainEnvironment().getObject(fireData.getWeaponID()));
                            if (weapon instanceof XM1104){
                                ((XM1104)weapon).forceFire(fireData.getProjectileID(), fireData.getDirection());
                            }
                            else {
                                weapon.forceFire(fireData.getProjectileID());
                            }
                        }
                        catch(NullPointerException exception){
                            MadBalls.getMainEnvironment().setCurrentObjID(MadBalls.getMainEnvironment().getCurrentObjID()+1);
                        }
                    }
                });
            }
            else if (data.getType().equals("get_weapon")){
                final GetWeaponData getWeaponData = (GetWeaponData) data;
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Class<Weapon> weaponClass = (Class<Weapon>) Class.forName(getWeaponData.getWeaponClassName());
                            ((Ball)MadBalls.getMainEnvironment().getObject(getWeaponData.getBallID())).setWeapon(weaponClass, getWeaponData.getWeaponID());
                        } catch (ClassNotFoundException ex) {
                            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (NullPointerException ex) {
                            Service<Void> service = new Service<Void>() {
                                @Override
                                protected Task<Void> createTask() {
                                    return new Task<Void>() {
                                        @Override
                                        protected Void call() throws Exception {
                                            handleData(data);
                                            return null;
                                        }
                                    };
                                }
                            };
                            service.start();
                        }
                    }
                });
            }
            else if (data.getType().equals("buff")){
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        BuffData buffData = (BuffData) data;
                        Ball ball = (Ball) MadBalls.getMainEnvironment().getObject(buffData.getBallID());
                        BuffState buffState = BuffState.recreateBuffState(buffData);
                        buffState.castOn(ball, 0);
                        ball.addEffectState(buffState);
                    }
                });
            }
        }
        catch (Exception ex){
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            Platform.exit();
            System.exit(0);
        }
        
    }
    
    private void spawn(SpawnData data){
        try {
            if (data.getSpawntype().equals("ball")){
                for (Player player : getPlayers()){
                    if (player.getPlayerNum() == data.getTypeNum()){
                        player.setSpawnLocation(data.getSpawnLocation());
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                player.generateBall(MadBalls.getMainEnvironment(), data.getId());
//                        Player newPlayer = new Player(null, false);
//                        newPlayer.setTeamNum(data.getTypeNum());
//                        newPlayer.setSpawnLocation(new SpawnLocation(data.getX(), data.getY(), data.getSpawntype(), data.getTypeNum()));
//                        getPlayers().add(newPlayer);
//                        newPlayer.generateBall(MadBalls.getMainEnvironment(), data.getId());
                            }
                        });
                    }
                }

            }
            else if (data.getSpawntype().equals("ball_local")){
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {

//                        getLocalPlayer().setTeamNum(data.getTypeNum());
                        getLocalPlayer().setSpawnLocation(new SpawnLocation(data.getX(), data.getY(), data.getSpawntype(), data.getTypeNum()));
                        getPlayers().add(getLocalPlayer());
                        getLocalPlayer().generateBall(MadBalls.getMainEnvironment(), data.getId());
                    }
                });
            }
            else if (data.getSpawntype().equals("weapon")){
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        MadBalls.getMainEnvironment().getItemSpawner().spawnWeapon(data.getSpawnLocation(), data.getTypeNum(), data.getId());
                    }
                });
            }
            else if (data.getSpawntype().equals("item")){
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        MadBalls.getMainEnvironment().getItemSpawner().spawnItem(data.getSpawnLocation(), data.getTypeNum(), data.getId());
                    }
                });
            }
            else if (data.getSpawntype().equals("explosion")){
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        new Explosion(MadBalls.getMainEnvironment(), data.getParameters()[0], data.getParameters()[1], data.getParameters()[2], data.getParameters()[3], data.getId());
                    }
                });
            }
        }
        catch (Exception ex){
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            Platform.exit();
        }
    }
//    private void moveObject(MoveData data){
//        try {
//            GameObject obj = Environment.getInstance().getObject(data.getObjIndex());
//            obj.getMoveBehaviour().setLastMoveTime(data.getLastMoveTime());
//            
//            if (data.getMoveType() == 1){
//                obj.setOldX(data.getOldX());
//                obj.setOldY(data.getOldY());
//                obj.getMoveBehaviour().setNewX(data.getNewX());
//                obj.getMoveBehaviour().setNewY(data.getNewY());
//            }
//            else if (data.getMoveType() == 2){
//                obj.setRotate(data.getNewRotate());
//                obj.setOldDirection(data.getOldRotate());
//            }
//        }
//        catch (Exception ex){
//            Logger.getLogger(MultiplayerHandler.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        
//    }

}
