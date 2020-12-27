package jp.rouh.mahjong.app.bitv;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;

public class TableViewerTester extends JFrame{
    private BlockImageTable table = new BlockImageTable();
    private JTextArea commandListArea = new JTextArea();
    private JTextField commandField = new JTextField();
    private TableViewerTester(){
        setTitle("Table Viewer Tester");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setPreferredSize(new Dimension(780, 580));
        pack();
        setResizable(false);
        setLayout(null);
        commandListArea.setLocation(0, 0);
        commandListArea.setSize(new Dimension(200, 550));
        commandListArea.setEditable(false);
        commandField.setLocation(0, 550);
        commandField.setSize(new Dimension(200, 30));
        commandField.addActionListener(enterPressed->{
            if(commandField.getText().isEmpty()) return;
            commandListArea.append(" ");
            commandListArea.append(commandField.getText());
            commandListArea.append(System.lineSeparator());
            if(commandListArea.getLineCount()>35 /* max line */){
                try{
                   var offset = commandListArea.getLineEndOffset(0);
                   commandListArea.replaceRange("", 0, offset);
               }catch(BadLocationException neverHappens){
                   throw new RuntimeException(neverHappens);
               }
           }
           commandField.setText("");
        });
        table.setLocation(200, 0);
        add(commandListArea);
        add(commandField);
        add(table);
        setVisible(true);
    }
    public static void main(String[] args) throws Exception{
        new TableViewerTester();
    }
}
