package madballs.multiplayer;

/**
 * Created by caval on 24/08/2016.
 */
public class ReadyData extends Data {
    private double sceneWidth, sceneHeight;

    public double getSceneWidth() {
        return sceneWidth;
    }

    public double getSceneHeight() {
        return sceneHeight;
    }

    public ReadyData(double sceneWidth, double sceneHeight) {
        super("ready");
        this.sceneWidth = sceneWidth;
        this.sceneHeight = sceneHeight;
    }
}
