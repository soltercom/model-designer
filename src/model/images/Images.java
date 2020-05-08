package model.images;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Images {

    private static Image getImage(String path) {
        return new Image(Images.class.getResourceAsStream(path));
    }

    public final static ImageView ADD_IMAGE
        = new ImageView(getImage("add_18dp.png"));
    public final static ImageView REMOVE_IMAGE
        = new ImageView(getImage("remove_18dp.png"));
    public final static ImageView DOMAIN_IMAGE
        = new ImageView(getImage("domain_18dp.png"));
    public final static ImageView FOLDER_IMAGE
        = new ImageView(getImage("folder_18dp.png"));
    public final static ImageView CROP_IMAGE
        = new ImageView(getImage("crop_18dp.png"));

    public static ImageView getFolderImage() {
        return new ImageView(getImage("folder_18dp.png"));
    }
    public static ImageView getCropImage() {
        return new ImageView(getImage("crop_18dp.png"));
    }

}
