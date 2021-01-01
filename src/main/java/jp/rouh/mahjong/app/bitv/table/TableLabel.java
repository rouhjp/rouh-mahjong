package jp.rouh.mahjong.app.bitv.table;

import javax.swing.*;
import java.awt.*;

/**
 * 麻雀卓描画用のラベルクラス。
 * @author Rouh
 * @version 1.0
 */
class TableLabel extends JLabel{

    /**
     * 指定した座標に中心が来るよう移動します。
     *
     * <p>{@link Component#setLocation}メソッドでは
     * コンポーネントの左上を指定した座標に合わせる形で移動を行いますが,
     * このメソッドでは, 画像の中心が座標に合わせられます。
     * @param x 移動先のx座標
     * @param y 移動先のy座標
     */
    public void setLocationCentered(int x, int y){
        super.setLocation(x - getWidth()/2, y - getHeight()/2);
    }
}
