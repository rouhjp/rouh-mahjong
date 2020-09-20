package jp.rouh.mahjong.score.impl;

import jp.rouh.mahjong.score.HandType;

/**
 * 役としてのドラを表すクラス。
 *
 * @author Rouh
 * @version 1.0
 */
public class PrisedTileHandType implements HandType{
    private final int n;
    private PrisedTileHandType(int n){
        this.n = n;
    }
    @Override
    public String getName(){
        return "ドラ"+n;
    }
    @Override
    public boolean isLimit(){
        return false;
    }
    @Override
    public int getDoubles(){
        return n;
    }

    /**
     * 役としての裏ドラを表すクラス。
     */
    private static class HiddenPrisedTile extends PrisedTileHandType{
        private HiddenPrisedTile(int n){
            super(n);
        }
        @Override
        public String getName(){
            return "裏ドラ"+super.n;
        }
    }

    /**
     * 役としての赤ドラを表すクラス。
     */
    private static class PrisedRedTile extends PrisedTileHandType{
        private PrisedRedTile(int n){
            super(n);
        }
        @Override
        public String getName(){
            return "赤ドラ"+super.n;
        }
    }

    /**
     * 指定した係数のドラの役を生成します。
     * @param n 係数
     * @return ドラ役
     */
    static PrisedTileHandType of(int n){
        return new PrisedTileHandType(n);
    }

    /**
     * 指定した係数の裏ドラの役を生成します。
     * @param n 係数
     * @return 裏ドラ役
     */
    static PrisedTileHandType ofHidden(int n){
        return new HiddenPrisedTile(n);
    }

    /**
     * 指定した係数の赤ドラの役を生成します。
     * @param n 係数
     * @return 赤ドラ役
     */
    static PrisedTileHandType ofRedTile(int n){
        return new PrisedRedTile(n);
    }
}
