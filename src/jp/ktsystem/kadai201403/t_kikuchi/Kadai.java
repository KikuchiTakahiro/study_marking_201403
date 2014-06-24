/**
 *
 */
package jp.ktsystem.kadai201403.t_kikuchi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;

import jp.ktsystem.kadai201403.Exception.ErrorCode;
import jp.ktsystem.kadai201403.Exception.KadaiException;
import jp.ktsystem.kadai201403.t_kikuchi.Models.OutputModel;
import jp.ktsystem.kadai201403.t_kikuchi.Models.OutputModelComparator;
import jp.ktsystem.kadai201403.t_kikuchi.Models.TargetStrModel;

/**
 * 2014_03 教育課題 課題クラス
 * @author TakahrioKikuchi
 *
 */

public class Kadai {

	/**
	 * 読み取りデータのキー date
	 */
	public static final String KEY_DATE = "date";

	/**
	 * 読み取りデータのキー start
	 */
	public static final String KEY_START = "start";
	/**
	 * 読み取りデータのキー end
	 */
	public static final String KEY_END = "end";

	/**
	 * デフォルト出力パス
	 */
	private static String defalutOutputPath;

	/**
	 * レベル1
	* @param anInputPath 	読み込みファイルパス
	* @param anOutputPath  出力ファイルパス
	* @throws KadaiException
	*/
	public static void parseWorkTimeData(String anInputPath, String anOutputPath) throws KadaiException
	{

		// 引数チェック
		if (ErrorCheck.isNullOrEmpty(anInputPath)) {

			throw new KadaiException(ErrorCode.INPUT_PATH_IS_NULL_OR_EMPTY);
		}
		else if (ErrorCheck.isNullOrEmpty(anOutputPath)) {
			throw new KadaiException(ErrorCode.OUTPUT_PATH_IS_NULL_OR_EMPTY);
		}

		// ファイル読み込み(BOM除去付)
		List<String> aFileStrList = FileUtill.readFile(anInputPath);

		String aFileStr = null;
		StringBuilder tempSb = new StringBuilder();
		for (String temStr : aFileStrList) {
			tempSb.append(temStr);
		}
		// ファイルの文字列
		aFileStr = tempSb.toString();

		// 読み込んだ文字列を日毎に整形	 例)   {"date":"20140101", "start":"0900", "end":"1800" }, {"date":"20140101", "start":"0900", "end":"1800" },... } ],
		// 日毎のパターンに合うのを取り出す
		TargetStrModel dailyModel = KadaiUtill.getTargetList(aFileStr, KadaiConst.LEVEL1_DAILY_PATTERN,
				KadaiConst.ONLY_LEFT_LARGE_PARNTHESIS_PATTERN,
				KadaiConst.ONLY_COMMA_PATTERN, KadaiConst.ONLY_RIGHT_LARGE_PARNTHESIS_PATTERN);

		// 日毎パターン文字列が取得できない場合、また、取得中にエラーがあった場合、
		if (0 >= dailyModel.getTargetList().size() || null != dailyModel.getTargetErrorCode()  ) {
			// エラーコードを指定ファイルに出力
			FileUtill.writeErrorCodeFile(anOutputPath, dailyModel.getTargetErrorCode(), null);
			return;
		}

		dailyModel.getTargetErrorCode();

		// トータル勤務時間
		Integer totalWorkTime = 0;

		// 出力用モデルリスト
		List<OutputModel> outputModelList = new ArrayList<OutputModel>();

		// 日毎をdate、start、endに分解⇒計算
		for (String aStr : dailyModel.getTargetList())
		{
			OutputModel resultModel = new OutputModel(null, null);
			try {

				// 日毎に整形した文字列をビーンリストに整形  "date":"20140421","start":"0900","end":"1800"の三つkey:value のセットリストを形成
				TargetStrModel aBeanList = KadaiUtill.getTargetList(aStr, KadaiConst.BEAN_PATTERN,
						KadaiConst.ONLY_LEFT_MIDDLE_PARENTHESIS_PATTERN, KadaiConst.ONLY_COMMA_PATTERN,
						KadaiConst.ONLY_RIGHT_MIDDLE_PARENTHESIS_PATTERN);

				// ビーンリスト生成時にエラーが発生した場合はスロー
				if (null != aBeanList.getTargetErrorCode()) {
					throw new KadaiException(aBeanList.getTargetErrorCode());
				}

				// 1日分のデータから出力用データ算出
				resultModel = KadaiUtill.getOutputModel(aBeanList.getTargetList(), KadaiConst.BEAN_KEY_LIST_LEVEL1, null);
				// 累積勤務時間を設定
				totalWorkTime += Integer.valueOf(resultModel.getOutputWorkTime());
				resultModel.setOutputTotalWorkTime(totalWorkTime.toString());

			} catch (KadaiException ex)
			{
				resultModel.setOutputErrorCode(ex.getErrorCode());
			}

			outputModelList.add(resultModel);
			if (null != resultModel.getOutputErrorCode())
				break;
		}

		List<String> outputArrayList = new ArrayList<String>();
		for (OutputModel model : outputModelList) {
			if (null == model.getOutputErrorCode()) {
				outputArrayList.add(model.createOutputStr());
			} else {
				outputArrayList.add(model.getOutputErrorCode().toString());

			}
		}

		// ファイル出力
		FileUtill.writeFile(anOutputPath, outputArrayList, null);

	}

	/**
	 * レベル2
	 * @param anInputPath 入力ファイルパス
	 * @param anOutputPath 出力ファイルパス
	 * @throws KadaiException
	 */
	public static void parseWorkTimeDataLv2(String anInputPath, String anOutputPath) throws KadaiException
	{
		// メンバに設定
		defalutOutputPath = anOutputPath;

		// ファイル読み込み(BOM除去付)
		List<String> aFileStrList = FileUtill.readFile(anInputPath);
		String aFileStr = null;
		StringBuilder tempSb = new StringBuilder();

		for (String temStr : aFileStrList) {
			tempSb.append(temStr);
		}
		// BOM除去後の文字列
		aFileStr = tempSb.toString();

		TargetStrModel monthlyModel = KadaiUtill.getTargetList(aFileStr, KadaiConst.LEVEL2_MONTHLY_PATTERN,
				KadaiConst.ONLY_LEFT_MIDDLE_PARENTHESIS_PATTERN,
				KadaiConst.ONLY_COMMA_PATTERN, KadaiConst.ONLY_RIGHT_MIDDLE_PARENTHESIS_PATTERN);

		monthlyModel.getTargetErrorCode();

		// 月文字列ループ
		for (String monthlyStr : monthlyModel.getTargetList()) {
			// 月文字列処理
			calcMonthlyStr(monthlyStr);
		}

	}

	/**
	 *  レベル２用の1月分文字列処理
	 * @param aMonthlyStr 1月分の文字列
	 * @throws KadaiException
	 */
	private static void calcMonthlyStr(String aMonthlyStr) throws KadaiException
	{
		// 月を取得
		Matcher aMonthMatcher = KadaiConst.MONTH_STR_PATTERN.matcher(aMonthlyStr);

		if (!aMonthMatcher.find()) {
			/// 月文字列が存在しなかった⇒エラーを投げるだけでファイルには出力しません
			throw new KadaiException(ErrorCode.FILE_CONTAIN_CONTROL_WORD);
		}
		String monthDataStr = aMonthMatcher.group();
		// 月の文字列の両端のダブルコーテーションを削除
		monthDataStr = monthDataStr.substring(1, monthDataStr.length() - 1);

		// 月文字列と大かっこの間のチェックは不要 ⇒ 引数の時点でチェックされているため

		/// 日毎文字列データ取得
		TargetStrModel dailyModel = KadaiUtill.getTargetList(aMonthlyStr, KadaiConst.LEVEL2_DAILY_PATTERN,
				KadaiConst.LEVEL2_SPACE_OF_DAY_AND_BEAN_PATTERN, KadaiConst.ONLY_COMMA_PATTERN,
				KadaiConst.ONLY_RIGHT_MIDDLE_PARENTHESIS_PATTERN);

		// 対象リストが取得できなかったらエラー
		if (0 >= dailyModel.getTargetList().size()) {
			// ファイルにエラーを出力
			FileUtill.writeErrorCodeFile(defalutOutputPath, ErrorCode.FILE_CONTAIN_CONTROL_WORD,
					String.format("%s.txt", monthDataStr));
			return;
		}

		List<OutputModel> outputModelList = new ArrayList<OutputModel>();
		List<String> outputDateList = new ArrayList<String>();

		// 日毎文字列ループ
		for (String aDailyStr : dailyModel.getTargetList()) {
			// 日毎処理
			OutputModel oneDayModel = calcDailyDatas(monthDataStr, aDailyStr, outputDateList);

			outputModelList.add(oneDayModel);
			if (null != oneDayModel.getOutputErrorCode()) {
				break;
			}
		}

		// 日付で昇順ソート
		Collections.sort(outputModelList, new OutputModelComparator());

		// 累計勤務時間
		int totalWorkTime = 0;
		List<String> outputArrayList = new ArrayList<String>();

		Iterator<OutputModel> it = outputModelList.iterator();
		while (it.hasNext()) {
			OutputModel model = it.next();

			if (null == model.getOutputErrorCode()) {
				totalWorkTime += Integer.parseInt(model.getOutputWorkTime());
				model.setOutputTotalWorkTime(String.valueOf(totalWorkTime));
				// 出力文字列に追加
				outputArrayList.add(model.createOutputStr());

			} else {
				outputArrayList.add(model.getOutputErrorCode().toString());

			}

		}

		// 出力ファイル名を生成
		String fileName = String.format("%s.txt", monthDataStr);

		// ファイル出力
		FileUtill.writeFile(defalutOutputPath, outputArrayList, fileName);

	}

	/**
	 * 日ごとデータ計算処理
	 * @param aDailyStr  日ごと文字列
	 * @param anDateList 日付リスト
	 * @throws KadaiException
	 */
	private static OutputModel calcDailyDatas(String aMonthDataStr, String aDailyStr, List<String> anDateList)
			throws KadaiException
	{
		// 出力モデル
		OutputModel resultOutputModel = new OutputModel(null, null);

		// 日付部を取得
		Matcher aDailyMatcher = KadaiConst.LEVEL2_DAY_STR_PATTERN.matcher(aDailyStr);

		try {

			if (!aDailyMatcher.find()) {
				// 日付部が取得できなかった場合⇒エラー
				throw new KadaiException(ErrorCode.INVALID_STRING);
			}

			// 日付文字列
			String dayStr = aDailyMatcher.group();
			dayStr = dayStr.substring(1, dayStr.length() - 1);

			// 日付が同じ月内のデータかチェック
			if (!aMonthDataStr.equals(dayStr.substring(0, dayStr.length() - 2)))
			{
				// 不正文字列エラーとする
				throw new KadaiException(ErrorCode.INVALID_STRING);
			}

			// 日付の重複チェック
			if (anDateList.contains(dayStr)) {
				throw new KadaiException(ErrorCode.DATE_OVERLAP);
			}
			anDateList.add(dayStr);

			// 日毎文字列から日付部を除いた { "20140101" : { "start":"0900", "end":"1800" } ⇒ : { "start":"0900", "end":"1800" }
			aDailyStr = aDailyStr.substring(aDailyMatcher.end() + 1);

			TargetStrModel beanModel = KadaiUtill.getTargetList(aDailyStr, KadaiConst.BEAN_PATTERN,
					KadaiConst.ONLY_COLON_AND_RIGHT_MIDDLE_PARENTHESIS_PATTERN,
					KadaiConst.ONLY_COMMA_PATTERN, KadaiConst.ONLY_RIGHT_MIDDLE_PARENTHESIS_PATTERN);

			// ビーン取得中にエラーがあったらそれをファイル出力
			if (null != beanModel.getTargetErrorCode()) {
				// ファイル出力
				throw new KadaiException(beanModel.getTargetErrorCode());
			}

			// ビーンのkeyを指定し、出力モデルを取得
			resultOutputModel = KadaiUtill.getOutputModel(beanModel.getTargetList(), KadaiConst.BEAN_KEY_LIST_LEVEL2, dayStr);

		} catch (KadaiException kadaiEx) {
			resultOutputModel.setOutputErrorCode(kadaiEx.getErrorCode());
		}

		return resultOutputModel;

	}

}
