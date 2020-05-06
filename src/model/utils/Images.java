package model.utils;

import javafx.scene.image.ImageView;

public class Images {

    public final static ImageView ADD_IMAGE = new ImageView("file:images/add_18dp.png");
    public final static ImageView REMOVE_IMAGE = new ImageView("file:images/remove_18dp.png");
    public final static ImageView DOMAIN_IMAGE = new ImageView("file:images/domain_18dp.png");
    public final static ImageView FOLDER_IMAGE = new ImageView("file:images/folder_18dp.png");
    public final static ImageView CROP_IMAGE = new ImageView("file:images/crop_18dp.png");

    public static ImageView getFolderImage() {
        return new ImageView("file:images/folder_18dp.png");
    }
    public static ImageView getCropImage() {
        return new ImageView("file:images/crop_18dp.png");
    }

}
