package jp.rouh.mahjong.app.bitv;

import jp.rouh.mahjong.app.bitv.image.TableImages;
import jp.rouh.mahjong.tile.Wind;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public final class TableLabels{
    private TableLabels(){
        throw new AssertionError("no instance for you");
    }
    public static TableLabel ofReadyBar(Direction d){
        var image = TableImages.ofReadyBar(d);
        var label = new TableLabel();
        label.setIcon(new ImageIcon(image));
        label.setBorder(new LineBorder(Color.BLACK));
        label.setSize(image.getWidth(), image.getHeight());
        return label;
    }

    public static TableLabel ofText(Direction d, String text, int width, int height, int margin){
        var label = new TableLabel();
        var image = TableImages.ofText(d, text, width, height, margin);
        label.setIcon(new ImageIcon(image));
        var rotatedHeight = d.isSideways()? width:height;
        var rotatedWidth = d.isSideways()? height:width;
        label.setSize(rotatedWidth, rotatedHeight);
        return label;
    }

    public static TableLabel ofPlayerName(Direction d, String name){
        return ofText(d, name, 60, 20, 5);
    }

    public static TableLabel ofPlayerScore(Direction d, int score){
        return ofText(d, String.format("%6d", score), 50, 20, 0);
    }

    public static TableLabel ofPlayerWind(Direction d, Wind wind){
        return ofText(d, wind.toString(), 20, 20, 0);
    }

}
