package madballs;

import javafx.scene.image.Image;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * return Image by parameter (factory pattern)
 * Created by hainguyen on 8/16/16.
 */
public class ImageGenerator {
    private static ImageGenerator imageGenerator = new ImageGenerator();

    private Map<String, Image> images = new HashMap<>();
    private String[] imageNames = new String[]
    {"ak47", "awp", "bazooka","grenade_launcher", "trap_launcher", "flameThrower", "m4a1", "minigun", "pistol", "shotgun", "uzi", "shield",
            "bullet1","bullet2","bullet3","bullet4","grenade_ammo","trap",
            "purple_potion", "teal_potion", "red_potion", "blue_syringe", "kevlar", "plasma_ammo", "lightning",
            "flag1", "flag2", "flag3", "flag4",
            "ball1", "ball2", "ball3","ball4","ball5","ball6","ball7","ball8",
            "blue_badge", "crosshair",
            "map_desert", "map_warehouse", "map_warehouse_flag",
            "map_arena", "map_arena_team",
            "map_jungle", "map_jungle_flag",
            "bullet1","bullet2","bullet3","bullet4","grenade_ammo", "trap", "explosion",
            "ball1", "ball2", "ball3","ball4","ball5","ball6","ball7","ball8",
            "map_desert", "map_warehouse_team", "map_warehouse_flag", "map_arena", "map_arena", "map_arena_team"
    };

    public static ImageGenerator getInstance(){
        return imageGenerator;
    }

    private ImageGenerator(){
        for( String imageName: imageNames){
            Image image = new Image(new File("assets/img/"+ imageName + ".png").toURI().toString());
            images.put(imageName, image);
        }
    }

    public Image getImage(String imageName){
        if (imageName == null){
            return null;
        }
        return images.get(imageName);

    }
}
