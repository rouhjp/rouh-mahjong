package jp.rouh.mahjong.app.bitv;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;

class ControlPanel extends JPanel{
    private final JButton callChiButton = new ActionButton("チー");
    private final JButton callPonButton = new ActionButton("ポン");
    private final JButton callKanButton = new ActionButton("カン");
    private final JButton callRonButton = new ActionButton("ロン");
    private final JButton callPassButton = new ActionButton("パス");
    private final JButton turnKyushuButton = new ActionButton("九種九牌");
    private final JButton turnKanButton = new ActionButton("カン");
    private final JButton turnReadyButton = new ActionButton("立直");
    private final JButton turnTsumoButton = new ActionButton("ツモ");
    private final JPanel handPanel = new JPanel();
    private final List<JLabel> handButtons = new ArrayList<>(14);
    private static class ActionButton extends JButton{
        private ActionButton(String text){
            super(text);
            setSize(new Dimension(80, 20));
            setBorder(new LineBorder(Color.BLACK));
            setEnabled(false);
        }
    }
    private static class TileButton extends JLabel{
        private TileButton(BufferedImage image){
            super(new ImageIcon(image));
            System.out.println(getIcon().getIconHeight());
            System.out.println(getIcon().getIconWidth());
            setSize(new Dimension(40, 60));
            setBorder(new LineBorder(Color.BLACK));
        }
    }
    ControlPanel(){
        setLayout(null);
        setSize(new Dimension(580, 120));
        callChiButton.setLocation(0, 0);
        callPonButton.setLocation(80, 0);
        callKanButton.setLocation(160, 0);
        callRonButton.setLocation(240, 0);
        callPassButton.setLocation(500, 0);
        turnKyushuButton.setLocation(0, 100);
        turnKanButton.setLocation(80, 100);
        turnReadyButton.setLocation(160, 100);
        turnTsumoButton.setLocation(240, 100);
        add(callChiButton);
        add(callPonButton);
        add(callKanButton);
        add(callRonButton);
        add(callPassButton);
        add(turnKyushuButton);
        add(turnKanButton);
        add(turnReadyButton);
        add(turnTsumoButton);
        handPanel.setLocation(0, 30);
        handPanel.setSize(new Dimension(580, 80));
        handPanel.setLayout(null);
        add(handPanel);
    }

    void updateHand(List<String> codes){
        handButtons.clear();
        for(int i = 0; i<codes.size(); i++){
            var code = codes.get(i);
            var button = getButton(code);
            button.setLocation(i*40, 0);
            handButtons.add(button);
            handPanel.add(button);
        }
    }
    private static JLabel getButton(String tileCode){
        try{
            var filePath = "/img/tiles/B" + tileCode + ".jpg";
            var resourceUri = ControlPanel.class.getResource(filePath).toURI();
            var tileImage = ImageIO.read(Paths.get(resourceUri).toFile());
            return new TileButton(tileImage);
        }catch(IOException|URISyntaxException e){
            throw new RuntimeException(e);
        }
    }
}
