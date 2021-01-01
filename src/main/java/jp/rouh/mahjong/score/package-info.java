/**
 * 点数計算パッケージ。
 *
 * <h2>手牌のモデリング</h2>
 * <p>プレイヤーの手は大別すると門前手牌と副露面子(暗槓を含む)に分類可能です。
 * 前者は牌のリスト{@code List<Tile>}として, 後者は面子{@link jp.rouh.mahjong.score.Meld}クラス
 * を利用して表現します。
 *
 * <h2>点数計算の手法</h2>
 * <p>麻雀の点数計算は複雑です。特にある手牌に対する役の決定は, 以下のプロセスを踏まなければなりません。
 * <ol><li>手牌を並べ替える</li>
 *     <li>並べ替えた手牌ひとつひとつに対し役と符を計算する</li>
 *     <li>最も高い点数のものを最終的な得点として計上する</li></ol>
 * <p>複数並べ替えパターンが発生するパターンを考えてみます。
 * 面子手は雀頭・順子・刻子の３つの要素で構成されています。
 * これらの要素は以下の時に複合して複数の解釈を生み出します。
 * <h6>三連刻形 (順子/順子/順子 vs 刻子/刻子/刻子)</h6>
 * <pre>
 *     [1 1 1 2 2 2 3 3 3]
 *     +-> [1 1 1][2 2 2][3 3 3]
 *     +-> [1 2 3][1 2 3][1 2 3]
 * </pre>
 * <h6>両面刻子形 (雀頭/順子/刻子 vs 刻子/順子/雀頭)</h6>
 * <pre>
 *     [1 1 1 2 3 4 4 4]
 *     +-> [1 1][1 2 3][4 4 4]
 *     +-> [4 4][1 1 1][1 2 3]
 * </pre>
 * <h6>四連対子形 (雀頭/順子/順子 vs 順子/順子/雀頭)</h6>
 * <pre>
 *     [1 1 2 2 3 3 4 4]
 *     +-> [4 4][1 2 3][1 2 3]
 *     +-> [1 1][2 3 4][2 3 4]
 * </pre>
 * <h6>二盃口形 (雀頭/順子/順子/順子/順子 vs 七対子)</h6>
 * <pre>
 *     [1 1 2 2 3 3 7 7 8 8 9 9]
 *     +-> [1 2 3][1 2 3][7 8 9][7 8 9]
 *     +-> [1 1][2 2][3 3][7 7][8 8][9 9]
 * </pre>
 * <p>また, 上記の二盃口形と両面刻子形が複合した大車輪形と呼ばれる形が最多の並べ替えパターンを持ち,
 * 一つの門前手牌から七対子形を含めると4パターンの並べ替えパターンが可能となります。
 * <pre>
 *     [2 2 3 3 4 4 5 5 6 6 7 7 8 8]
 *     +-> [2 2][3 3][4 4][5 5][6 6][7 7][8 8]
 *     +-> [2 2][3 4 5][3 4 5][6 7 8][6 7 8]
 *     +-> [5 5][2 3 4][2 3 4][2 3 4][2 3 4]
 *     +-> [8 8][2 3 4][2 3 4][5 6 7][5 6 7]
 * </pre>
 * <p>重要であることは, これらの並べ替えパターンを確定させないと, 三色同順や三暗刻, 七対子など
 * 確定できない役が存在することです。また, 符も並べ替えに依存します。
 * <p>さらに厄介なことに, 待ちに影響される役も存在します。平和や四暗刻, 三暗刻などです。
 * 三暗刻や四暗刻が待ちに影響される理由は, ロンによって成立した刻子が明刻扱いになるためです。
 * <p>このように手牌の点数計算には事前に複数パターンを確定させる必要があります。
 * このパッケージでは, 並べ替えパターン及び待ちパターンを確定させた手を
 * 整形済み手牌{@link jp.rouh.mahjong.score.FormattedHand}クラスが表現します。
 * 待ちは{@link jp.rouh.mahjong.score.Wait}クラスが表現します。
 *
 * <h2>点数計算機能の利用</h2>
 * <p>点数を導出するには手牌と和了牌, 副露した面子, その他の勝利時の状況といった情報が必要です。
 * 手牌と和了牌, 副露した面子を内包するクラスとして和了手{@link jp.rouh.mahjong.score.WinningHand}
 * クラスが用意されています。和了手{@code WinningHand}は整形済み手牌{@code FormattedHand}クラスへの
 * 変換のための整形{@link jp.rouh.mahjong.score.WinningHand#format()}メソッドが用意されています。
 * また, 立直や一発, 海底摸月など勝利時の状況に依存する役の判定のため,
 * 勝利コンテキスト{@link jp.rouh.mahjong.score.WinningContext}インターフェースが用意されています。
 * <p>点数計算には点数計算機能{@link jp.rouh.mahjong.score.HandScoreCalculator}インターフェースと
 * その実装である{@link jp.rouh.mahjong.score.type.TieredHandScoreCalculator}クラスを利用します。
 * <pre>{@code
 *     HandScore handScore = new TieredHandScoreCalculator().calculate(winningHand, winningContext);
 *     List<HandType> handTypes = handScore.getHandTypes();
 *     String winningExpression = handScore.getExpression();
 *     handTypes.stream().forEach(System.out::println);
 *     System.out.println(winningExpression);
 * }</pre>
 * <p>一般に麻雀アプリケーションでは, 和了の結果は単に取得した点数を表示するだけでなく,
 * 手牌の役名とその翻数, 合計翻数, 符数を表示するほか, 満貫や跳満といった点数区分を表示します。
 * このため, これらの情報を一手に扱う手牌得点{@link jp.rouh.mahjong.score.HandScore}クラスが用意されており,
 * {@link jp.rouh.mahjong.score.HandScore#getHandTypes()}メソッドによって, 成立した役の一覧を
 * 役を表す{@link jp.rouh.mahjong.score.HandType}クラスのリスト形式で取得可能です。
 *
 * @see jp.rouh.mahjong.score.Meld
 * @see jp.rouh.mahjong.score.Wait
 * @see jp.rouh.mahjong.score.FormattedHand
 * @see jp.rouh.mahjong.score.WinningHand
 * @see jp.rouh.mahjong.score.WinningContext
 * @see jp.rouh.mahjong.score.HandScoreCalculator
 * @see jp.rouh.mahjong.score.type.TieredHandScoreCalculator
 * @see jp.rouh.mahjong.score.HandScore
 * @see jp.rouh.mahjong.score.HandType
 * @author Rouh
 * @version 1.0
 */
package jp.rouh.mahjong.score;
