package madballs;

import javafx.scene.image.Image;
import madballs.gameFX.MediaHandler;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hainguyen on 8/16/16.
 */
public class ImageGenerator {
    private static ImageGenerator imageGenerator = new ImageGenerator();

    private Map<String, Image> images = new HashMap<>();
    private String[] imageNames = new String[]
            {"ak47", "awp", "bazooka", "flameThrower", "m4a1", "minigun", "pistol", "shotgun", "uzi"};
    private Image ak47, awp, bazooka, flameThrower, m4a1, minigun, pistol, shotgun, uzi;

    public static ImageGenerator getInstance(){
        return imageGenerator;
    }

    private ImageGenerator(){
        for( String imageName: imageNames){
            Image gunImage = new Image(new File("assets/img/"+ imageName + ".png").toURI().toString());
            images.put(imageName, gunImage);
        }
    }

    public Image getImage(String imageName){
        if (imageName == null){
            return null;
        }
        return images.get(imageName);

    }
}
