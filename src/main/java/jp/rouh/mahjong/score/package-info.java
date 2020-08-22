/**
 * 点数計算パッケージ。
 *
 * <p>与えられた手牌から, 役判定, 符判定, 高点法の判断等の処理を経て,対応する点数を
 * 導出することを目的としています。導出には{@link jp.rouh.mahjong.score.HandScorer}
 * インターフェースを利用します。
 *
 * <pre>{@code
 *     HandScore handScore = HandScorer.evaluate(handTiles, openMelds, context);
 *     System.out.println(handScore.getScoreExpression());
 * }</pre>
 *
 * <p>一般に麻雀アプリケーションでは, 和了の結果として
 * 単に得点を表示するだけでなく,　その導出に必要な役名, 合計翻数, 符数を表示するほか,
 * 麻雀の得点は単に数値だけではなく, 満貫や跳満といった点数区分によって表されます。
 * このため導出する値は数値ではなく, これら和了結果の表示に必要となる情報を提供する
 * {@link jp.rouh.mahjong.score.HandScore}インターフェースとなります。
 *
 * <p>点数計算に必要な情報は手牌だけでなく, 和了時の局やプレイヤーの状況が必要となります。
 * これらの情報を表すため勝利状況{@link jp.rouh.mahjong.score.WinningContext}
 * インターフェースが提供されます。点数計算機能を利用する場合, 勝利状況インターフェースを
 * 実装したオブジェクトを引数に渡す必要があります。
 *
 * <p>麻雀の性質上, 手牌を四面子一雀頭の形に並べ替える場合, その並べ替え方や待ちは必ずしも
 * 一つになるとは限りません。複数の解釈パターンが可能な場合, それぞれの解釈パターンは,
 * 整形済み手牌{@link jp.rouh.mahjong.hand.FormattedHand}として, 面子構成と待ちが
 * 確定されたオブジェクトに変換されます。これら一つ一つのパターンに対する点数の導出は,
 * {@link jp.rouh.mahjong.score.FormattedHandScorer}インターフェースが行います。
 * 手牌計算機能は, 整形済み手牌計算機能に高点法ルールの処理を加えたものです。
 * 高点法機能の実装として, {@link jp.rouh.mahjong.score.ParallelHandScorer}が
 * 提供されており, 整形済み手牌計算機能を実装し, この中間実装を継承することで,
 * 手牌計算機能を持ったクラスを作成することが可能です。
 *
 * <p>計算機能の実装は{@link jp.rouh.mahjong.score.scorer}パッケージが担当します。
 * デフォルトの実装として{@link jp.rouh.mahjong.score.HandScorer#evaluate}
 * メソッドでは{@link jp.rouh.mahjong.score.scorer.StandardHandScorer}が利用
 * されています。
 *
 * @see jp.rouh.mahjong.hand
 * @see jp.rouh.mahjong.score.scorer
 * @author Rouh
 * @version 1.0
 */
package jp.rouh.mahjong.score;
