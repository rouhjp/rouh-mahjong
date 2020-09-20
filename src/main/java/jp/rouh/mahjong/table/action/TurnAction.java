package jp.rouh.mahjong.table.action;

import jp.rouh.mahjong.tile.Tile;

import java.util.NoSuchElementException;

/*
 * 設計メモ:
 * ターン入力をCommandパターンとして実装すると,
 * ポリモーフィズムによる一元的な処理の記述が可能ですが,
 * メソッドの分割が必要となり, かえって可読性が下がると
 * 判断し, 継承なしのタグ付けクラスによる実装をしています。
 */
/**
 * プレイヤーのターン時の行動を表すクラス。
 *
 * <p>このクラスはプレイヤーの入力を受け取ったビューが
 * モデルに行動を入力する際の型として利用されます。
 *
 * <p>プレイヤーのターン時の行動には以下の項目が考えられます。
 * <ul>
 * <li>九種九牌の宣言</li>
 * <li>ツモの宣言</li>
 * <li>加槓の構築(牌を指定する)</li>
 * <li>暗槓の構築(牌を指定する)</li>
 * <li>リーチ宣言牌の打牌(牌を指定する)</li>
 * <li>通常の打牌(牌を指定する)</li>
 * </ul>
 * <p>槓子の構築は自分ターンが継続しますが,嶺上牌のツモというモデル側の処理
 * が必要であるためターンの行動の一つと考えます。
 * <p>九種九牌宣言およびツモ宣言以外は, いずれの行動も牌を指定する必要があります。
 * 暗槓は本来は4つの牌の指定が必要ですが, うちどれか一つの牌を指定すれば, 自ずと
 * 他の3枚の牌も確定するため, 牌の指定は一つのみで十分です。
 *
 * @author Rouh
 * @version 1.0
 */
public class TurnAction{
    private enum Type{
        /** 九種九牌を宣言します */
        DECLARE_DRAW,

        /** ツモを宣言します */
        DECLARE_WIN,

        /** カンを宣言し加槓を作成します */
        MAKE_ADD_QUAD,

        /** カンを宣言し暗槓を作成します */
        MAKE_SELF_QUAD,

        /** リーチを宣言し打牌します */
        DISCARD_TILE_AS_READY,

        /** 打牌します */
        DISCARD_TILE
    }
    private final Type type;
    private final Tile tile;
    private TurnAction(Type type, Tile tile){
        this.type = type;
        this.tile = tile;
    }

    /**
     * この行動が九種九牌宣言かどうか検査します。
     * @return true  九種九牌宣言の場合
     *         false 九種九牌宣言でない場合
     */
    public boolean isNineTilesDrawDeclaration(){
        return type==Type.DECLARE_DRAW;
    }

    /**
     * この行動がツモ宣言かどうか検査します。
     * @return true  ツモ宣言の場合
     *         false ツモ宣言でない場合
     */
    public boolean isSelfDrawWinDeclaration(){
        return type==Type.DECLARE_WIN;
    }

    /**
     * この行動がカン宣言(加槓/暗槓)かどうか検査します。
     * @return true  カン宣言(加槓/暗槓)の場合
     *         false カン宣言(加槓/暗槓)でない場合
     */
    public boolean isQuadDeclaration(){
        return isAddQuadDeclaration() || isSelfQuadDeclaration();
    }

    /**
     * この行動がカン宣言(加槓)かどうか検査します。
     * @return true  カン宣言(加槓)の場合
     *         false カン宣言(加槓)でない場合
     */
    public boolean isAddQuadDeclaration(){
        return type==Type.MAKE_ADD_QUAD;
    }

    /**
     * この行動がカン宣言(暗槓)かどうか検査します。
     * @return true  カン宣言(暗槓)の場合
     *         false カン宣言(暗槓)でない場合
     */
    public boolean isSelfQuadDeclaration(){
        return type==Type.MAKE_SELF_QUAD;
    }

    /**
     * この行動がリーチ宣言打牌かどうか検査します。
     * @return true  リーチ宣言打牌の場合
     *         false リーチ宣言打牌でない場合
     */
    public boolean isReadyDeclaration(){
        return type==Type.DISCARD_TILE_AS_READY;
    }

    /**
     * 選択された牌を取得します。
     *
     * @throws NoSuchElementException この行動が九種九牌またはツモの場合
     * @return 選択された牌
     */
    public Tile getSelectedTile(){
        if(tile==null){
            throw new NoSuchElementException();
        }
        return tile;
    }

    /**
     * 九種九牌宣言のターン行動オブジェクトを生成します。
     * @return 九種九牌宣言のターン行動オブジェクト
     */
    public static TurnAction ofDraw(){
        return new TurnAction(Type.DECLARE_DRAW, null);
    }

    /**
     * ツモ宣言のターン行動オブジェクトを生成します。
     * @return ツモ宣言のターン行動オブジェクト
     */
    public static TurnAction ofWin(){
        return new TurnAction(Type.DECLARE_WIN, null);
    }

    /**
     * カン宣言(加槓)のターン行動オブジェクトを生成します。
     * @param selected カン対象牌
     * @return カン宣言(加槓)のターン行動オブジェクト
     */
    public static TurnAction ofAddQuad(Tile selected){
        return new TurnAction(Type.MAKE_ADD_QUAD, selected);
    }

    /**
     * カン宣言(暗槓)のターン行動オブジェクトを生成します。
     * @param selected カン対象牌
     * @return カン宣言(暗槓)のターン行動オブジェクト
     */
    public static TurnAction ofSelfQuad(Tile selected){
        return new TurnAction(Type.MAKE_SELF_QUAD, selected);
    }

    /**
     * リーチ宣言打牌のターン行動オブジェクトを生成します。
     * @param selected 立直宣言牌
     * @return リーチ宣言打牌のターン行動オブジェクト
     */
    public static TurnAction ofReady(Tile selected){
        return new TurnAction(Type.DISCARD_TILE_AS_READY, selected);
    }

    /**
     * 打牌のターン行動オブジェクトを生成します。
     * @param selected 捨て牌
     * @return 打牌のターン行動オブジェクト
     */
    public static TurnAction ofDiscard(Tile selected){
        return new TurnAction(Type.DISCARD_TILE, selected);
    }
}
