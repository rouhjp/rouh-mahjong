package jp.rouh.mahjong.app.bitv.table;

import jp.rouh.mahjong.app.bitv.image.BlockImages;
import jp.rouh.mahjong.tile.Tile;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 牌画像ラベルクラス。
 * @author Rouh
 * @version 1.0
 */
public class BlockLabel extends TableLabel{
    private BlockLabel(BufferedImage image){
        setIcon(new ImageIcon(image));
        setHorizontalAlignment(CENTER);
        setBorder(new LineBorder(Color.BLACK));
        setSize(image.getWidth(), image.getHeight());
    }

    /**
     * 裏向きに倒された牌ラベルを取得します。
     * @param d 牌の向き
     * @return 牌ラベル
     */
    static BlockLabel ofFaceDown(Direction d){
        return new BlockLabel(BlockImages.ofFaceDown(d));
    }

    /**
     * 裏向きに倒された牌ラベルを取得します。
     * @param d 牌の向き
     * @return 牌ラベル
     */
    static BlockLabel ofFaceUp(Direction d, Tile t){
        return new BlockLabel(BlockImages.ofFaceUp(d, t));
    }

    /**
     * 手前に向いた立ててある牌ラベルを取得します。
     * @param t 牌
     * @return 牌ラベル
     */
    static BlockLabel ofPlayerHand(Tile t){
        return new BlockLabel(BlockImages.ofPlayerHand(t));
    }

    /**
     * 指定した方向に向いた立ててある牌ラベルを取得します。
     * @param d 倒した時の牌の向き
     * @return 牌ラベル
     */
    static BlockLabel ofOpponentHand(Direction d){
        return new BlockLabel(BlockImages.ofOpponentHand(d));
    }
}
