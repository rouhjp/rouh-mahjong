package jp.rouh.mahjong.score;

/**
 * 役を表すインターフェース。
 *
 * @author Rouh
 * @version 1.0
 */
public interface HandType{

    /**
     * 役の名称を返します
     * @return 役名
     */
    String getName();

    /**
     * この役が役満かどうか検査します。
     * @return true  役満の場合
     *         false 通常役の場合
     */
    boolean isLimit();

    /**
     * 役の翻数, 役満であれば13, ダブル役満の場合は26を返します。
     * @return 翻数 通常役の場合
     *         13 役満の場合
     *         26 ダブル役満の場合
     */
    int getDoubles();
}
