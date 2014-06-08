package jp.ktsystem.kadai201403.t_kikuchi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.ktsystem.kadai201403.Exception.ErrorCode;
import jp.ktsystem.kadai201403.Exception.KadaiException;
import jp.ktsystem.kadai201403.t_kikuchi.Models.OutputModel;
import jp.ktsystem.kadai201403.t_kikuchi.Models.TargetStrModel;

/**
 * 課題Utillクラス
 * @author TakahrioKikuchi
 *
 */
public class KadaiUtill {

	/**
	 * 改行、空白、タブの正規表現
	 */
	private static final Pattern ARROW_CONTROL_CARACTOR_PATTERN = Pattern.compile("\\s+");

	/**
	 * ダブルコーテーション内に改行、空白、タブの正規表現
	 */
	private static final Pattern ARROW_CONTROL_CARACTOR_IN_DOUBLE_QUOTATION_PATTERN = Pattern
			.compile("\"[a-zA-Z\\d]*\\s+\"");

	/**
	 * 改行、空白、タブが含まれているかチェック
	 * @param aStr 対象文字列
	 * @return 改行、空白、タブが含まれている場合、ture
	 */
	public static boolean checkNewLineTabSpace(String aStr) {
		// 引数が 空白、タブ、改行 をから文字に置換
		Matcher arrowControlStr = ARROW_CONTROL_CARACTOR_IN_DOUBLE_QUOTATION_PATTERN.matcher(aStr);
		return arrowControlStr.find();
	}

	/**
	 * 空白、タブ、改行をから文字で置換
	 * @param aStr 空白、タブ、改行が入っている可能性がある文字列list
	 * @return 空白、タブ、改行をから文字で置換した文字列
	 */
	public static String replaceSpaceTabNewLine(String aStr)
	{
		StringBuilder sb = new StringBuilder();

		// 引数が 空白、タブ、改行 をから文字に置換
		Matcher arrowControlStr = ARROW_CONTROL_CARACTOR_PATTERN.matcher(aStr);
		sb.append(arrowControlStr.replaceAll(""));

		return sb.toString();
	}

	/**
	 * 勤務時間算出
	 * @param aStartTime 出勤時間
	 * @param aEndTime 退社時間
	 * @return 勤務時間(分単位)
	 * @throws KadaiException
	 */
	public static String calcWorkTime(String aStartTime, String aEndTime) throws KadaiException
	{
		// 引数のNullorEmptyチェック
		// 出勤時間
		if (ErrorCheck.isNullOrEmpty(aStartTime))
		{
			throw new KadaiException(ErrorCode.EMPTY);
		}
		// 退社時間
		if (ErrorCheck.isNullOrEmpty(aEndTime))
		{
			throw new KadaiException(ErrorCode.EMPTY);
		}

		// 使える文字かどうかチェック(HHmm形式で表示されているかチェック)
		// 出勤時間
		if (ErrorCheck.checkEnableWord(aStartTime))
		{
			throw new KadaiException(ErrorCode.INVALID_STRING);
		}
		// 退社時間
		if (ErrorCheck.checkEnableWord(aEndTime))
		{
			throw new KadaiException(ErrorCode.INVALID_STRING);
		}

		// これ以降は、引数として正しい文字しか来ていない「HHmm」形式しか通らない+
		WorkTimer workTime = new WorkTimer(aStartTime, aEndTime);

		return Integer.toString(workTime.calcWorkingTime());
	}

	/**
	 * ビーンリストから出力データを取得
	 * @param aBeanList 日毎データを 「～：～」で分割したリスト
	 * @param aBeanKeyStrList keyに入ってあるだろう文字リスト 例) 「start」
	 * @param anOutputDate 出力用モデル
	 * @return 出力用モデル
	 * @throws KadaiException
	 */
	public static OutputModel getOutputModel(List<String> aBeanList, List<String> aBeanKeyStrList, String anOutputDate)
			throws KadaiException
	{
		// 出力モデル
		OutputModel resultModel = new OutputModel(null, null);

		// Beanのkeyとvalueのマップ
		HashMap<String, String> aBeanMap = new HashMap<String, String>();

		for (String aBean : aBeanList)
		{
			String[] tempArray = new String[2];
			tempArray = aBean.split("\\s*:\\s*", 0);

			// ダブルコーテンション内の文字列内の改行、タブ、スペースが入っている場合は、エラー ⇒ファイルの入力文字エラー
			if (KadaiUtill.checkNewLineTabSpace(tempArray[0])
					|| KadaiUtill.checkNewLineTabSpace(tempArray[1]))
			{
				throw new KadaiException(ErrorCode.INVALID_STRING);
			}

			// ダブルコーテーション内にはタブ等が入っていないので、それ以外の場所に入っていた時のために置換
			tempArray[0] = KadaiUtill.replaceSpaceTabNewLine(tempArray[0]);
			tempArray[1] = KadaiUtill.replaceSpaceTabNewLine(tempArray[1]);

			// keyとvalueは両端にダブルコーテーションを含むので、削除
			tempArray[0] = tempArray[0].substring(1, tempArray[0].length() - 1);
			tempArray[1] = tempArray[1].substring(1, tempArray[1].length() - 1);

			if (aBeanKeyStrList.contains(tempArray[0]) && ! aBeanMap.containsKey(tempArray[0]))
			{
				aBeanMap.put(tempArray[0], tempArray[1]);
			} else {
				throw new KadaiException(ErrorCode.INVALID_STRING);
			}
		}

		// 指定したkeyが存在するかチェック
		for(String beanKey : aBeanKeyStrList){
			// 足りない場合は、エラーを投げる
			if(! aBeanMap.containsKey(beanKey))throw new KadaiException(ErrorCode.INVALID_STRING);
		}

		// 勤怠時間を計算
		String myWorkTime = KadaiUtill.calcWorkTime(aBeanMap.get(KadaiConst.KEY_START),
				aBeanMap.get(KadaiConst.KEY_END));

		// ファイル出力文字列リスト作成

		resultModel.setOutputDate(null != anOutputDate ? anOutputDate : aBeanMap.get(KadaiConst.KEY_DATE));

		//		resultModel.setOutputDate(aBeanMap.get(KadaiConst.KEY_DATE));
		resultModel.setOutputWorkTime(myWorkTime.toString());
		// 累積勤務時間は呼び出し側で設定

		return resultModel;
	}

	/**
	 * ビーンリストから出力データを取得
	 * @param aBeanList 日毎データを 「～：～」で分割したリスト
	 * @param aBeanKeyStrList keyに入ってあるだろう文字リスト 例) 「start」
	 * @return 出力用モデル
	 * @throws KadaiException
	 */
	public static OutputModel getOutputModelForLevel2(List<String> aBeanList, List<String> aBeanKeyStrList)
			throws KadaiException
	{
		// 出力モデル
		OutputModel resultModel = new OutputModel(null, null);

		// Beanのkeyとvalueのマップ
		HashMap<String, String> aBeanMap = new HashMap<String, String>();

		for (String aBean : aBeanList)
		{
			String[] tempArray = new String[2];
			tempArray = aBean.split("\\s*:\\s", 0);

			// ダブルコーテンション内の文字列内の改行、タブ、スペースが入っている場合は、エラー ⇒ファイルの入力文字エラー
			if (KadaiUtill.checkNewLineTabSpace(tempArray[0])
					|| KadaiUtill.checkNewLineTabSpace(tempArray[1]))
			{
				throw new KadaiException(ErrorCode.INVALID_STRING);
			}

			// ダブルコーテーション内にはタブ等が入っていないので、入っていた時のために置換
			tempArray[0] = KadaiUtill.replaceSpaceTabNewLine(tempArray[0]);
			tempArray[1] = KadaiUtill.replaceSpaceTabNewLine(tempArray[1]);

			// keyとvalueは両端にダブルコーテーションを含むので、削除
			tempArray[0] = tempArray[0].substring(1, tempArray[0].length() - 1);
			tempArray[1] = tempArray[1].substring(1, tempArray[1].length() - 1);

			if (aBeanKeyStrList.contains(tempArray[0]))
			{
				aBeanMap.put(tempArray[0], tempArray[1]);
			} else {
				throw new KadaiException(ErrorCode.INVALID_STRING);
			}
		}
		// 勤怠時間を計算
		String myWorkTime = KadaiUtill.calcWorkTime(aBeanMap.get(KadaiConst.KEY_START),
				aBeanMap.get(KadaiConst.KEY_END));

		// ファイル出力文字列リスト作成
		resultModel.setOutputWorkTime(myWorkTime.toString());
		// 日付と 累積勤務時間は呼び出し側で設定

		return resultModel;
	}

	/**
	 * 対象単位の文字列取得
	 * @param aTargetStr 対象文字列
	 * @param aTargetPattern 取得したい対象単位のパターン
	 * @param aFirstPattern 最初のヒット時に使うチェックのパターン
	 * @param aMiddlePattern 2回目以降のヒット時に使うチェックのパターン
	 * @param anEndPatern 最後にヒットした後に使うチェックのパターン
	 * @return 対象文字列クラス
	 */
	public static TargetStrModel getTargetList(String aTargetStr, Pattern aTargetPattern, Pattern aFirstPattern,
			Pattern aMiddlePattern, Pattern anEndPatern) {

		// 返却リスト
		List<String> outputList = new ArrayList<String>();
		// 返却用エラーコード
		ErrorCode outputErrorCode = null;

		Matcher targetMatcher = aTargetPattern.matcher(aTargetStr);

		// 一つ前の終了インデックス
		int oneBeforeEndIndex = -1;
		while (targetMatcher.find())
		{
			int startIndex = targetMatcher.start();
			if (-1 != oneBeforeEndIndex) {
				// 前回ennと今回startの間に入っている文字列達をチェック処理
				if (!KadaiUtill.checkAmongStr(oneBeforeEndIndex, startIndex, aTargetStr,
						aMiddlePattern)) {
					// エラーコード11を投げる(制御文字列でなくてもOKとする)
					outputErrorCode = ErrorCode.FILE_CONTAIN_CONTROL_WORD;
					break;
				}
			}
			// 初回の時、最初にマッチしたインデックス以前の文字列をチェック
			else {
				if (!KadaiUtill.checkAmongStr(0, startIndex, aTargetStr,
						aFirstPattern)) {
					// エラーコード11を投げる(制御文字列でなくてもOKとする)
					outputErrorCode = ErrorCode.FILE_CONTAIN_CONTROL_WORD;
					break;
				}
			}
			// 日毎リストに追加
			outputList.add(targetMatcher.group());
			oneBeforeEndIndex = targetMatcher.end();
		}
		// もし既にエラーがあれば無駄チェックになるので
		if (null == outputErrorCode) {
			// 最後マッチ後のチェック
			// 最後にfindした位置と末尾チェック
			if (!KadaiUtill.checkAmongStr(oneBeforeEndIndex, aTargetStr.length(), aTargetStr,
					anEndPatern)) {
				// エラー ⇒エラーコード11を投げる(制御文字列でなくてもOKとする)
				outputErrorCode = ErrorCode.FILE_CONTAIN_CONTROL_WORD;
			}
		}
		// 返却用データ作成
		TargetStrModel myTargetStrModel = new TargetStrModel();
		myTargetStrModel.setTargetList(outputList);
		myTargetStrModel.setTargetErrorCode(outputErrorCode);

		return myTargetStrModel;
	}

	/**
	 *  一日分のデータをkey:valueのセットの文字列リストに成形
	 * @param aDayStr  一日分のデータの文字列
	 * @return key:value のセットの文字列が格納されたリスト
	 */
	public static List<String> getBeanList(String aDayStr) {

		Pattern aBeanPattern = Pattern.compile("\"[a-zA-Z]*+\"\\s*?:\\s*?\"[\\d]*+\"");
		Matcher aBeanMatcher = aBeanPattern.matcher(aDayStr);

		// 日毎に整形した文字列を日ごとビーンに整形 ⇒ "date":"20140421","start":"0900","end":"1800"
		List<String> aFileStrListByDayBeanList = new ArrayList<String>();
		while (aBeanMatcher.find()) {
			aFileStrListByDayBeanList.add(aBeanMatcher.group());
		}

		return aFileStrListByDayBeanList;

	}

	/**
	 * 文字列を正規表現で区切る
	 * @param aStr 文字列
	 * @param aRegex 区切る正規表現文字列
	 * @return 対象文字列をい正規表現で区切ったlist
	 */
	public static List<String> splitStrByRegex(String aStr, String aRegex)
	{
		// 返却リスト
		List<String> aList = new ArrayList<String>();
		// 引数を]でスプリット
		// 月ごとに分割する
		aList = Arrays.asList(aStr.split(".*\\]"));
		return aList;
	}

	/**
	 * 不正文字列が入っていないかチェック
	 * @param aStartIndex 検索開始インデックス
	 * @param anEndIndex  検索終了インデックス
	 * @param aStr   	  対象文字列
	 * @param aRegexStr	  マッチさせる正規表現
	 * @return 対象文字列の間に、指定のパターンにマッチする場合は、true
	 */
	public static boolean checkAmongStr(int aStartIndex, int anEndIndex, String aStr, String aRegexStr) {

		String checkStr = aStr.substring(aStartIndex, anEndIndex);
		Pattern checkPattern = Pattern.compile(aRegexStr);
		Matcher checkMatcher = checkPattern.matcher(checkStr);
		return checkMatcher.matches();
	}

	/**
	 * 不正文字列が入っていないかチェック
	 * @param aStartIndex 検索開始インデックス
	 * @param anEndIndex  検索終了インデックス
	 * @param aStr   	  対象文字列
	 * @param aCheckPattern	  マッチさせるパターン
	 * @return 対象文字列の間に、指定のパターンにマッチする場合は、true
	 */
	public static boolean checkAmongStr(int aStartIndex, int anEndIndex, String aStr, Pattern aCheckPattern) {

		String checkStr = aStr.substring(aStartIndex, anEndIndex);
		Matcher checkMatcher = aCheckPattern.matcher(checkStr);
		return checkMatcher.matches();
	}

}
