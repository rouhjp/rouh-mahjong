package jp.rouh.mahjong.app.bitv.image;

import jp.rouh.mahjong.app.bitv.table.Direction;
import jp.rouh.mahjong.tile.Tile;
import jp.rouh.mahjong.tile.Tiles;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static jp.rouh.mahjong.app.bitv.image.BlockImageFactory.*;

/**
 * 牌の画像を供給するユーティリティクラス。
 *
 * <p>このクラスがロードされたタイミングで描画処理を実行し,
 * 生成した画像をキャッシュとして保持します。
 * @author Rouh
 * @version 1.0
 */
public final class BlockImages{
    private static final Map<Direction, Map<Tile, BufferedImage>> FACE_UP_IMAGES;
    private static final Map<Direction, BufferedImage> FACE_DOWN_IMAGES;
    private static final Map<Tile, BufferedImage> PLAYER_HAND_IMAGES;
    private static final Map<Direction, BufferedImage> OPPONENT_HAND_IMAGES;
    static{
        var faceUpImages = new HashMap<Direction, Map<Tile, BufferedImage>>();
        var faceDownImages = new HashMap<Direction, BufferedImage>();
        var playerHandImages = new HashMap<Tile, BufferedImage>();
        var opponentHandImages = new HashMap<Direction, BufferedImage>();
        for(Direction d: Direction.values()){
            faceDownImages.put(d, createFaceDownBlockImage(d));
            faceUpImages.put(d, new HashMap<>());
            for(Tile t: Tiles.values()){
                faceUpImages.get(d).put(t, createFaceUpBlockImage(d, t.toString()));
            }
        }
        for(Tile t: Tiles.values()){
            playerHandImages.put(t, createHandBlockImage(t.toString()));
        }
        for(Direction d: List.of(Direction.LEFT, Direction.BOTTOM, Direction.RIGHT)){
            opponentHandImages.put(d, createHandBlockImage(d));
        }
        FACE_UP_IMAGES = faceUpImages;
        FACE_DOWN_IMAGES = faceDownImages;
        PLAYER_HAND_IMAGES = playerHandImages;
        OPPONENT_HAND_IMAGES = opponentHandImages;
    }
    private BlockImages(){
        throw new AssertionError("no instance for you!");
    }

    /**
     * 表向きに倒された牌の画像を取得します。
     *
     * <p>牌の画像は, 例えば河に捨てられた牌やドラ表示牌などに使用されます。
     * @param d 牌の向き
     * @param t 牌
     * @return 牌の画像
     */
    public static BufferedImage ofFaceUp(Direction d, Tile t){
        return FACE_UP_IMAGES.get(d).get(t);
    }

    /**
     * 裏向きに倒された牌の画像を取得します。
     *
     * <p>牌の画像は, 例えば山牌や暗槓の牌に使用されます。
     * @param d 牌の向き
     * @return 牌の画像
     */
    public static BufferedImage ofFaceDown(Direction d){
        return FACE_DOWN_IMAGES.get(d);
    }

    /**
     * 手前に向いた立ててある牌の画像を取得します。
     *
     * <p>牌の画像は, プレイヤーの手牌に使用されます。
     * @param t 牌
     * @return 牌の画像
     */
    public static BufferedImage ofPlayerHand(Tile t){
        return PLAYER_HAND_IMAGES.get(t);
    }

    /**
     * 指定した方向に向いた立ててある牌の画像を取得します。
     *
     * <p>牌の画像は, 他プレイヤーの手牌に使用されます。
     * プレイヤー自身の手牌は牌の表面が見えるため, 牌の種類を
     * 引数に持つ{@link #ofPlayerHand}メソッドを変わりに使用します。
     * @param d 倒した時の牌の向き
     * @throws java.util.NoSuchElementException 向きが{@code Side.SELF}の場合
     * @return 牌の画像
     */
    public static BufferedImage ofOpponentHand(Direction d){
        if(d==Direction.TOP) throw new NoSuchElementException("you must select opponent side");
        return OPPONENT_HAND_IMAGES.get(d);
    }
}
