package jp.rouh.mahjong.app;

import jp.rouh.mahjong.score.HandType;
import jp.rouh.mahjong.score.Meld;
import jp.rouh.mahjong.table.Declaration;
import jp.rouh.mahjong.table.TableStrategy;
import jp.rouh.mahjong.table.action.CallAction;
import jp.rouh.mahjong.table.action.CallPhaseContext;
import jp.rouh.mahjong.table.action.TurnAction;
import jp.rouh.mahjong.table.action.TurnPhaseContext;
import jp.rouh.mahjong.tile.Side;
import jp.rouh.mahjong.tile.Tile;
import jp.rouh.mahjong.tile.Wind;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

public class ConsoleViewer implements TableStrategy{

    @Override
    public void handRevealed(Side side, List<Tile> handTiles){

    }
    @Override
    public void handRevealed(Side side, List<Tile> handTiles, Tile drawnTile){

    }
    @Override
    public void riverTileTaken(Side side){

    }
    @Override
    public void tiltMeldAdded(Side side, Side tilt, List<Tile> tiles){

    }
    @Override
    public void selfQuadAdded(Side side, List<Tile> tiles){

    }
    @Override
    public void meldTileAdded(Side side, int index, Tile added){

    }
    @Override
    public void readyBoneAdded(Side side){

    }
    @Override
    public void declared(Side side, Declaration declaration){

    }
    @Override
    public TurnAction askTurnAction(TurnPhaseContext context){
        System.out.println(context.getTiles());
        var choices = new StringBuilder();
        if(context.canDeclareWin()) choices.append("[W:ツモ]");
        if(context.canDeclareDraw()) choices.append("[D:九種九牌]");
        if(context.canDeclareReady()) choices.append("[Rxx:立直]");
        if(context.canDeclareQuad()) choices.append("[Qxx:カン]");
        choices.append("[xx:打牌]");
        System.out.println(choices);
        while(true){
            System.out.print("入力:");
            var br = new BufferedReader(new InputStreamReader(System.in));
            try{
                var input = br.readLine().trim();
                if(input.toUpperCase().equals("W")){
                    if(context.canDeclareWin()){
                        return TurnAction.ofWin();
                    }
                }else if(input.toUpperCase().equals("D")){
                    if(context.canDeclareDraw()){
                        return TurnAction.ofDraw();
                    }
                }else if(input.toUpperCase().startsWith("R")){
                    if(context.canDeclareReady()){
                        var index = Integer.parseInt(input.substring(1));
                        if(context.canDeclareReady(context.getTiles().get(index))){
                            return TurnAction.ofReady(context.getTiles().get(index));
                        }
                    }
                }else if(input.toUpperCase().startsWith("Q")){
                    if(context.canDeclareQuad()){
                        var index = Integer.parseInt(input.substring(1));
                        if(context.canDeclareAddQuad(context.getTiles().get(index))){
                            return TurnAction.ofAddQuad(context.getTiles().get(index));
                        }
                        if(context.canDeclareSelfQuad(context.getTiles().get(index))){
                            return TurnAction.ofSelfQuad(context.getTiles().get(index));
                        }
                    }
                }else{
                    var index = Integer.parseInt(input);
                    if(context.canDiscard(context.getTiles().get(index))){
                        return TurnAction.ofDiscard(context.getTiles().get(index));
                    }
                }
                System.out.println("コマンド入力エラー");
            }catch(IndexOutOfBoundsException | NumberFormatException e){
                System.out.println("数値入力エラー");
            }catch(IOException e){
                throw new RuntimeException(e);
            }
        }
    }
    @Override
    public CallAction askCallAction(CallPhaseContext context){
        System.out.println(context.getTiles()+" "+List.of(context.getCallTile()));
        var choices = new StringBuilder();
        if(context.canCallWin()) choices.append("[W:ロン]");
        if(context.canCallQuad()) choices.append("[Q:カン]");
        if(context.canCallTriple()) choices.append("[T:ポン]");
        if(context.canCallStraight()) choices.append("[S :チー]");
        choices.append("[X:パス]");
        System.out.println(choices);
        while(true){
            System.out.print("入力:");
            var br = new BufferedReader(new InputStreamReader(System.in));
            try{
                var input = br.readLine();
                if(input.toUpperCase().equals("W")){
                    if(context.canCallWin()){
                        return CallAction.ofWin();
                    }
                }else if(input.toUpperCase().startsWith("Q")){
                    if(context.canCallQuad()){
                        return CallAction.ofQuad(context.getTiles().stream().filter(context::canCallQuad).collect(Collectors.toList()));
                    }
                }
//                }else if(input.toUpperCase().startsWith("T")){
//
//
//                }else if(input.toUpperCase().startsWith("S")){
//
//                }


                System.out.println("コマンド入力エラー");
            }catch(NumberFormatException | IndexOutOfBoundsException e){
                System.out.println("数値入力エラー");
            }catch(IOException e){
                throw new RuntimeException(e);
            }
        }
    }
    @Override
    public void seatUpdated(Side side, Wind wind, String name, int score){
        System.out.println(side+": ["+wind+"]"+name+" ("+score+")");
    }
    @Override
    public void diceRolled(Side side, int dice1, int dice2){
        System.out.println(side+": サイコロ: "+dice1+" "+dice2);
    }
    @Override
    public void roundStarted(Wind wind, int count, int streak, int deposit){
        System.out.println();
        System.out.println("_＿__＿__＿__＿__＿__＿__＿__＿__＿__＿__＿__＿__＿__ _＿__");
        System.out.println("|一 |一 |③ |③ |④ |④ |⑤ |⑤ |Ⅴ |Ⅵ |Ⅶ |Ⅶ |Ⅷ | |Ⅷ |");
        System.out.println("|萬 |萬 |筒 |筒 |筒 |筒 |筒 |赤 |索 |索 |索 |索 |索 | |索 |");
        System.out.println("+ー-+ー-+ー-+ー-+ー-+ー-+ー-+ー-+ー-+ー-+ー-+ー-+ー-+ +ー-+");
        System.out.println(" 　1　 2　 3　 4　 5　 6　 7　 8　 9　10　11　12　13　  14 ");
        System.out.println();


        System.out.println();
        System.out.println("|一 |二 |三 |四 |五 |六 |七 |八 |九 |");
        System.out.println("|萬 |萬 |萬 |萬 |萬 |萬 |萬 |萬 |萬 |");

        System.out.println();
        System.out.println("|東 |南 |西 |北 |　 |發 |中 |");
        System.out.println("|　 |　 |　 |　 |　 |　 |　 |");

        System.out.println();
        System.out.println("|Ⅰ |Ⅱ |Ⅲ |Ⅳ |Ⅴ |Ⅵ |Ⅶ |Ⅷ |Ⅸ |");
        System.out.println("|索 |索 |索 |索 |索 |索 |索 |索 |索 |");

        System.out.println();
        System.out.println("|① |② |③ |④ |⑤ |⑥ |⑦ |⑧ |⑨ |");
        System.out.println("|筒 |筒 |筒 |筒 |筒 |筒 |筒 |筒 |筒 |");



    }
    @Override
    public void wallGenerated(){
    }
    @Override
    public void wallTileTaken(Side side, int column, int floor){

    }
    @Override
    public void wallTileRevealed(Side side, int column, int floor, Tile tile){
        System.out.println("ドラ表示牌: "+tile);
    }
    @Override
    public void tileDrawn(Side side){
        System.out.println(side+"牌をツモりました");
    }
    @Override
    public void tileDrawn(Tile tile){
        System.out.println(Side.SELF+"牌をツモりました: "+tile);
    }
    @Override
    public void handUpdated(Side side, int count){

    }
    @Override
    public void handUpdated(List<Tile> handTiles){
        System.out.println(handTiles);
    }
    @Override
    public void tileDiscarded(Side side, Tile tile){
        System.out.println(side+": 打 "+tile);
    }
    @Override
    public void tileDiscardedAsReady(Side side, Tile tile){
        System.out.println(side+": 打 "+tile+"(立直宣言牌)");
    }
    @Override
    public void roundSettled(String expression){

    }


}
