package madballs.AI;

import javafx.geometry.Bounds;
import madballs.GameObject;
import madballs.collision.PushBackEffect;
import madballs.collision.StackedCollisionEffect;
import madballs.moveBehaviour.StraightMove;

/**
 * the strategy to prevent the Ball from being cornered to a Obstacle
 * Created by caval on 29/08/2016.
 */
public class AvoidObstacleStrategy extends Strategy {
    private double myX, myY, velocityX, velocityY, radius = 15;
    private GameObject pushBackObj;
    private double secondsUntilCollision = -1;
    private final double REACTION_TIME_LIMIT = 0.3;
    private boolean isNoCollision = true;
    private GameObject lastConsiderableObj;
    private long changeConsiderationTime = 0;

    public AvoidObstacleStrategy(BotPlayer bot) {
        super(bot);
        setImportance(12);
    }

    @Override
    public void prepare() {
        // get the Ball's movement and coordinates
        StraightMove straightMove = (StraightMove) getBot().getBall().getMoveBehaviour();
        myX = getBot().getBall().getTranslateX();
        myY = getBot().getBall().getTranslateY();
        velocityX = straightMove.getVelocityX();
        velocityY = straightMove.getVelocityY();
        secondsUntilCollision = -1;
        isNoCollision = true;
    }

    @Override
    public void consider(GameObject obj) {
        if (velocityX == 0 && velocityY == 0 )return;
        if (obj.getCollisionEffect() != null && ((StackedCollisionEffect)obj.getCollisionEffect()).hasCollisionEffect(PushBackEffect.class)){

            Bounds objBounds = obj.getDisplay().getBoundsInParent();
            double objX = obj.getTranslateX();
            double objY = obj.getTranslateY();

            // check if the Ball is going to collide with this PushBack obj

            if (Math.abs(velocityY) >= Math.abs(velocityX)){
                if (objY < myY) objY = objBounds.getMaxY();
                double deltaTime = (objY - myY) / velocityY;
                if (isDeltaTimeConsiderable(deltaTime)){

                    double objLeftWidth = objX - objBounds.getMinX();
                    double objRightWidth = objBounds.getMaxX() - objX;
                    double expectedX  = myX + velocityX * deltaTime;
                    if ((expectedX - radius >= objX - objLeftWidth && expectedX - radius <= objX + objRightWidth)
                            || (expectedX + radius >= objX - objLeftWidth && expectedX + radius <= objX + objRightWidth)){
                        updatePushBackObj(obj, deltaTime);
                    }
                }
            }
            else {
                if (objX < myX) objX = objBounds.getMaxX();
                double deltaTime = (objX - myX) / velocityX;
                if (isDeltaTimeConsiderable(deltaTime)){
                    double objTopHeight = objY - objBounds.getMinY();
                    double objBottomHeight = objBounds.getMaxY() - objY;
                    double expectedY = myY + velocityY * deltaTime;
                    if ((expectedY - radius >= objY - objTopHeight && expectedY - radius <= objY + objBottomHeight)
                            || (expectedY + radius >= objY - objTopHeight && expectedY + radius <= objY + objBottomHeight)){

                        updatePushBackObj(obj, deltaTime);
                    }
                }
            }
        }
    }

    private void updatePushBackObj(GameObject obj, double deltaTime){
        if (obj == pushBackObj) {
            setImportance(getImportance() + 1);
        }
        pushBackObj = obj;
        secondsUntilCollision = deltaTime;
        isNoCollision = false;
    }

    private boolean isDeltaTimeConsiderable(double deltaTime){
        double timeLimit = REACTION_TIME_LIMIT;
        if ((getBot().getLastThoughtTime() - changeConsiderationTime) < 1000000000){
            timeLimit /= 3;
        }
        if (deltaTime < 0 || deltaTime > timeLimit) {
            return false;
        }
        else if (secondsUntilCollision != -1 && deltaTime > secondsUntilCollision){
            return false;
        }
        return true;
    }

    @Override
    public void act() {
        if (isNoCollision) {
            pushBackObj = null;
            setImportance(12);
        }
        else {
            if (lastConsiderableObj == null || lastConsiderableObj != pushBackObj){
                changeConsiderationTime = getBot().getLastThoughtTime();
                lastConsiderableObj = pushBackObj;
            }
        }
        if (pushBackObj != null) {
            StraightMove straightMove = (StraightMove) getBot().getBall().getMoveBehaviour();
            straightMove.setVelocityX(-velocityX);
            straightMove.setVelocityY(-velocityY);
        }
    }

    @Override
    public void updateImportance() {
        setImportance(getImportance() / secondsUntilCollision);
    }
}
