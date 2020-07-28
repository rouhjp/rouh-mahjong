package jp.rouh.mahjong.score.scorer;

import jp.rouh.mahjong.score.HandType;

public class PrisedTile implements HandType{
    private final int n;
    private PrisedTile(int n){
        this.n = n;
    }
    @Override
    public String getName(){
        return "ドラ"+n;
    }
    @Override
    public boolean isEpic(){
        return false;
    }
    @Override
    public int getTypePoint(){
        return n;
    }
    private static class HiddenPrisedTile extends PrisedTile{
        private HiddenPrisedTile(int n){
            super(n);
        }
        @Override
        public String getName(){
            return "裏ドラ"+super.n;
        }
    }
    private static class PrisedRedTile extends PrisedTile{
        private PrisedRedTile(int n){
            super(n);
        }
        @Override
        public String getName(){
            return "赤ドラ"+super.n;
        }
    }
    static PrisedTile of(int n){
        return new PrisedTile(n);
    }
    static PrisedTile ofHidden(int n){
        return new HiddenPrisedTile(n);
    }
    static PrisedTile ofRedTile(int n){
        return new PrisedRedTile(n);
    }
}
