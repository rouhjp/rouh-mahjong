package jp.rouh.mahjong.app.bitv.image;

import jp.rouh.mahjong.app.bitv.Direction;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;

public final class TableImages{
    private TableImages(){
        throw new AssertionError("no instance for you");
    }
    public static BufferedImage ofReadyBar(Direction d){
        try{
            var filePath = "/img/bar1000.png";
            var resourceUri = BlockImageFactory.class.getResource(filePath).toURI();
            var image = ImageIO.read(Paths.get(resourceUri).toFile());
            return ImageFunctions.rotate(image, d);
        }catch(IOException|URISyntaxException e){
            throw new RuntimeException(e);
        }
    }
    public static BufferedImage ofText(Direction d, String text, int width, int height, int margin){
        var image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        var g = image.getGraphics();
        g.setColor(Color.BLACK);
        g.drawString(text, margin, height - 5);
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return ImageFunctions.rotate(image, d.reversed());
    }
}
