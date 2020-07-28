package jp.rouh.mahjong.app.bitv.image;

import jp.rouh.mahjong.app.bitv.Direction;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;

/**
 * 麻雀牌の画像を生成するユーティリティクラス。
 * @author Rouh
 * @version 1.0
 */
final class BlockImageFactory{
    /** 牌の裏面及び側面の色 */
    private static final Color BACK_COLOR = Color.LIGHT_GRAY;

    /**
     * 裏向きの牌の画像を生成します。
     *
     * <p>裏向きの画像として, 表面のベタ塗り画像と側面のベタ塗り画像を結合した
     * 画像を生成します。
     * @param d 牌の向き
     * @return 牌の画像
     */
    static BufferedImage createFaceDownBlockImage(Direction d){
        var faceImage = ImageFunctions.rotate(drawBackFaceImage(), d);
        var edgeImage = ImageFunctions.upSideDown(drawEdgeImage(d.isSideways()));
        return ImageFunctions.concat(faceImage, ImageFunctions.adaptUpperLine(edgeImage));
    }

    /**
     * 表向きの牌の画像を生成します。
     *
     * <p>表向きの画像として, リソースから読み込んだ牌の画像と側面のベタ塗り画像を結合した
     * 画像を生成します。
     * @param d 牌の向き
     * @param tileCode 牌コード
     * @return 牌の画像
     */
    static BufferedImage createFaceUpBlockImage(Direction d, String tileCode){
        var faceImage = ImageFunctions.rotate(readTileFaceImage(tileCode), d);
        var edgeImage = drawEdgeImage(d.isSideways());
        return ImageFunctions.concat(faceImage, ImageFunctions.adaptUpperLine(edgeImage));
    }

    /**
     * 立てられた牌の画像を生成します。
     *
     * <p>引数に, 倒したときに牌が向く方向を指定します。
     * なお, 上向きは牌の表面が見えるため, このメソッドではなく
     * {@link #createFaceUpBlockImage}メソッドを利用します。
     * 左または右を指定した場合, 上側面と側面のベタ塗り画像を結合して画像を生成します。
     * 下を指定した場合, 上側面と裏面のベタ塗り画像を結合して画像を生成します。
     * @param d 倒した際の牌の向き
     * @throws IllegalArgumentException 上向きが指定された場合
     * @return 牌の画像
     */
    static BufferedImage createHandBlockImage(Direction d){
        if(d==Direction.TOP) throw new IllegalArgumentException("");
        if(d.isSideways()){
            var upperImage = ImageFunctions.rotate(drawEdgeImage(false), d);
            var lowerImage = ImageFunctions.rotate(drawEdgeImage(true), d);
            return ImageFunctions.concat(ImageFunctions.adaptLowerLine(upperImage), lowerImage);
        }else{
            var edgeImage = drawEdgeImage(false);
            var faceImage = drawBackFaceImage();
            return ImageFunctions.concat(ImageFunctions.adaptLowerLine(edgeImage), faceImage);
        }
    }

    /**
     * 手前向きに立てられた牌の画像を生成します。
     *
     * <p>上側面のベタ塗り画像とリソースから読み込んだ牌の画像を結合して画像を生成します。
     * @param tileCode 牌コード
     * @return 牌の画像
     */
    static BufferedImage createHandBlockImage(String tileCode){
        var edgeImage = ImageFunctions.upSideDown(drawEdgeImage(false));
        var faceImage = readTileFaceImage(tileCode);
        return ImageFunctions.concat(ImageFunctions.adaptLowerLine(edgeImage), faceImage);
    }

    /**
     * リソースから牌画像を読み込みます。
     * @param tileCode 牌コード
     * @return 牌画像
     */
    private static BufferedImage readTileFaceImage(String tileCode){
        try{
            // this will read src/main/resources/img/tiles/*.jpg
            var filePath = "/img/tiles/" + tileCode + ".jpg";
            var resourceUri = BlockImageFactory.class.getResource(filePath).toURI();
            return ImageIO.read(Paths.get(resourceUri).toFile());
        }catch(IOException|URISyntaxException e){
            throw new RuntimeException(e);
        }
    }

    /**
     * 裏面のベタ塗り画像を生成します。
     * @return 裏面のベタ塗り画像
     */
    private static BufferedImage drawBackFaceImage(){
        int width = 20;
        int height = 30;
        var backImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        var g = backImage.getGraphics();
        g.setColor(BACK_COLOR);
        g.fillRect(0, 0, width, height);
        g.drawImage(backImage, 0, 0, null);
        g.dispose();
        return backImage;
    }

    /**
     * 側面のベタ塗り画像を生成します。
     * @return 側面のベタ塗り画像
     */
    private static BufferedImage drawEdgeImage(boolean sideways){
        int width = sideways? 30:20;
        int height = 10;
        var edgeImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        var g = edgeImage.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height/2);
        g.setColor(BACK_COLOR);
        g.fillRect(0, height/2, width, height/2);
        g.drawImage(edgeImage, 0, 0, null);
        g.dispose();
        return edgeImage;
    }
}
