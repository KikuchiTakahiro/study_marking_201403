/**
 *
 */
package jp.ktsystem.kadai201403.t_kikuchi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.ktsystem.kadai201403.Exception.ErrorCode;
import jp.ktsystem.kadai201403.Exception.KadaiException;
import jp.ktsystem.kadai201403.t_kikuchi.Models.OutputModel;
import jp.ktsystem.kadai201403.t_kikuchi.Models.OutputModelComparator;

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

	//^\{\s*\"(.*?)\"\s*:\s*\[?\s*(.*?)\s*\]?\s*\}$

	// \s*\"(.+?)\"\s*:\s*\[?\s*\{(.*?)\}(?!\s*,\s*\{)\s*\]?\s*

	/** 勤務時間算出
	 * @param aStartTime 出勤時間
	 * @param aEndTime 退社時間
	 * @return 勤務時間(分単位)
	 * @throws KadaiException
	 */
	public static String calcWorkTime(String aStartTime, String aEndTime) throws KadaiException
	{
		// 引数のNullorEmptyチェック
		// 出勤時間
		if (ErrorCheck.checkNullOrEmpty(aStartTime))
		{
			throw new KadaiException(ErrorCode.EMPTY);
		}
		// 退社時間
		if (ErrorCheck.checkNullOrEmpty(aEndTime))
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
	 * レベル1
	* @param anInputPath 	読み込みファイルパス
	* @param anOutputPath  出力ファイルパス
	* @throws KadaiException
	*/
	public static void parseWorkTimeData(String anInputPath, String anOutputPath) throws KadaiException
	{
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
		Pattern aByDayPattern = Pattern.compile(KadaiConst.LEVEL1_DAILY_PATTERN);
		Matcher aByDayMatcher = aByDayPattern.matcher(aFileStr);

		List<String> aFileStrListByDay = new ArrayList<String>();

		// パターンに合うのを取り出す
		while (aByDayMatcher.find())
		{
			aFileStrListByDay.add(aByDayMatcher.group());
		}

		// トータル勤務時間
		Integer totalWorkTime = 0;

		List<OutputModel> outputModelList = new ArrayList<OutputModel>();

		// 日毎をdate、start、endに分解⇒計算
		for (String aStr : aFileStrListByDay)
		{
			OutputModel resultModel = new OutputModel(null, null);
			try {

				// 日毎に整形した文字列をビーンリストに整形  "date":"20140421","start":"0900","end":"1800"の三つkey:value のセットリストを形成
				List<String> aFileStrListByDayBeanList = KadaiUtill.getBeanList(aStr);

				HashMap<String, String> aBeanMap = new HashMap<String, String>();
				for (String aBean : aFileStrListByDayBeanList)
				{
					String[] tempArray = new String[2];
					tempArray = aBean.split("\\s*:\\s", 0);

					// ダブルコーテンション内の文字列内の改行、タブ、スペースが入っている場合は、エラー ⇒ファイルの入力文字エラー
					if (KadaiUtill.checkNewLineTabSpace(tempArray[0]) || KadaiUtill.checkNewLineTabSpace(tempArray[1]))
					{
						throw new KadaiException(ErrorCode.INVALID_STRING);
					}

					// keyとvalueは両端にダブルコーテーションを含むので、削除
					tempArray[0] = tempArray[0].substring(1, tempArray[0].length() - 1);
					tempArray[1] = tempArray[1].substring(1, tempArray[1].length() - 1);

					// key  の中身を判別
					if (KEY_DATE.equals(tempArray[0]) || KEY_START.equals(tempArray[0]) || KEY_END.equals(tempArray[0]))
					{
						aBeanMap.put(tempArray[0], tempArray[1]);
					} else {
						throw new KadaiException(ErrorCode.INVALID_STRING);
					}
				}
				// 勤怠時間を計算
				String myWorkTime = calcWorkTime(aBeanMap.get(KEY_START), aBeanMap.get(KEY_END));
				totalWorkTime += Integer.parseInt(myWorkTime);

				// ファイル出力文字列リスト作成
				resultModel.setOutputDate(aBeanMap.get(KEY_DATE));
				resultModel.setOutputWorkTime(myWorkTime.toString());
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
			outputArrayList.add(model.createOutputStr());
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
		try {
			String aFileStr = null;
			StringBuilder tempSb = new StringBuilder();

			for (String temStr : aFileStrList) {
				tempSb.append(temStr);
			}
			// BOM除去後の文字列
			aFileStr = tempSb.toString();

			// 読み込んだ文字列の月文字列を取得 ⇒   \s*\d{6}"\s*:\s*[\[].+*[\]]
			// "201401": [ { "20140101" : { "start":"0900", "end":"1800" }, ... } ],"201402": [ { "20140201" : { "start":"0900", "end":"1800" }, ... } ], ⇒ 月毎に分解
			Pattern aMonthlyPattern = Pattern.compile(KadaiConst.LEVEL2_MONTHLY_PATTERN);
			Matcher aMonthlyMatcher = aMonthlyPattern.matcher(aFileStr);

			// 月データの間チェック
			// 一つ前の終了インデックス
			int oneBeforeEndIndex = -1;
			while (aMonthlyMatcher.find()) {
				int startIndex = aMonthlyMatcher.start();
				if (-1 != oneBeforeEndIndex) {
					// 前回ennと今回startの間に入っている文字列達をチェック処理
					if (!KadaiUtill.checkAmongStr(oneBeforeEndIndex, startIndex, aFileStr,
							KadaiConst.ONLY_COMMA_PATTERN)) {
						// コンマ1個以外に何かあった⇒ エラー ⇒エラーコード11を投げる(制御文字列でなくてもOKとする)
						throw new KadaiException(ErrorCode.FILE_CONTAIN_CONTROL_WORD);
					}
				}
				// 初回の時、最初にマッチしたインデックス以前の文字列をチェック
				else {
					if (!KadaiUtill.checkAmongStr(0, startIndex, aFileStr, KadaiConst.ONLY_LEFT_MIDDLE_PARENTHESIS)) {
						// 中かっこ以外の値が入っていたのでエラーを投げる ⇒エラーコード11を投げる(制御文字列でなくてもOKとする)
						throw new KadaiException(ErrorCode.FILE_CONTAIN_CONTROL_WORD);
					}
				}
				// 月文字列の処理 ← とりあえず、月：[]という形の文字列を切り出したことになる
				calcMonthlyStr(aMonthlyMatcher.group());
				oneBeforeEndIndex = aMonthlyMatcher.end();
			}

			// 最後にfindした位置と末尾チェック
			if (!KadaiUtill.checkAmongStr(oneBeforeEndIndex, aFileStr.length(), aFileStr,
					KadaiConst.ONLY_RIGHT_MIDDLE_PARENTHESIS)) {
				// コンマ1個以外に何かあった⇒ エラー ⇒エラーコード11を投げる(制御文字列でなくてもOKとする)
				throw new KadaiException(ErrorCode.FILE_CONTAIN_CONTROL_WORD);
			}
		} catch (Exception ex) {
			ex.printStackTrace();

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
		Pattern aMonthPattern = Pattern.compile("\"\\d{6}\"");
		Matcher aMonthMatcher = aMonthPattern.matcher(aMonthlyStr);

		if (!aMonthMatcher.find()) {
			/// 月文字列が存在しなかった⇒エラーを投げるだけでファイルには出力しません

		}
		String monthDataStr = aMonthMatcher.group().toString();
		// 月の文字列の両端のダブルコーテーションを削除
		monthDataStr = monthDataStr.substring(1, monthDataStr.length() - 1);

		// 月文字列と大かっこの間のチェックは不要 ⇒ 引数の時点でチェックされているため

		// 日ごとデータに分解　
		// level2の月毎のデータを日毎に取り出し
		// { "20140101" : { "start":"0900", "end":"1800" }, "20140102" : { "start":"0900", "end":"1800" } ⇒ "20140101" : { "start":"0900", "end":"1800" } と "20140102" : { "start":"0900", "end":"1800" } に分ける
		Pattern dailyPattern = Pattern.compile(KadaiConst.LEVEL2_DAILY_PATTERN);
		Matcher dailyMatcher = dailyPattern.matcher(aMonthlyStr);

		List<OutputModel> outputModelList = new ArrayList<OutputModel>();
		List<String> outputDateList = new ArrayList<String>();
		OutputModel errorModel = null;
		// 一つ前のendのindex
		int oneBeforeEndIndex = -1;

		try {
			while (dailyMatcher.find()) {
				int startIndex = dailyMatcher.start();
				if (-1 != oneBeforeEndIndex) {
					// 前回ennと今回startの間に入っている文字列達をチェック処理
					if (!KadaiUtill.checkAmongStr(oneBeforeEndIndex, startIndex, aMonthlyStr,
							KadaiConst.ONLY_COMMA_PATTERN)) {
						// エラーコード11を投げる(制御文字列でなくてもOKとする)
						throw new KadaiException(ErrorCode.FILE_CONTAIN_CONTROL_WORD);
					}
				}
				// 初回の時、最初にマッチしたインデックス以前の文字列をチェック
				else {
					if (!KadaiUtill.checkAmongStr(0, startIndex, aMonthlyStr, KadaiConst.LEVEL2_SPACE_OF_DAY_AND_BEAN)) {
						//エラーコード11を投げる(制御文字列でなくてもOKとする)
						throw new KadaiException(ErrorCode.FILE_CONTAIN_CONTROL_WORD);
					}

				}
				// エンドを取得
				oneBeforeEndIndex = dailyMatcher.end();

				// 日毎のデータを取得した
				String dailyStr = dailyMatcher.group();
				// 1日分の計算処理
				OutputModel oneDayModel = calcDailyDatas(monthDataStr, dailyStr, outputDateList);

				if (null == oneDayModel.getOutputErrorCode())
				{
					outputModelList.add(oneDayModel);

				} else {
					errorModel = oneDayModel;
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

				totalWorkTime += Integer.parseInt(model.getOutputWorkTime());
				model.setOutputTotalWorkTime(String.valueOf(totalWorkTime));
				// 出力文字列に追加
				outputArrayList.add(model.createOutputStr());
			}

			if (null != errorModel) {
				outputArrayList.add(errorModel.getOutputErrorCode().toString());
			}

			// ファイル出力
			FileUtill.writeFile(defalutOutputPath, outputArrayList, monthDataStr);

		} catch (KadaiException ex) {
			// ファイル出力
			FileUtill.writeFile(defalutOutputPath, ex.getErrorCode().toString(), monthDataStr);
		}

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
		Pattern aDailyPattern = Pattern.compile(KadaiConst.LEVEL2_DAY_STR_PATTERN);
		Matcher aDailyMatcher = aDailyPattern.matcher(aDailyStr);

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

			// 日文字列と中かっこまでのチェックは引数時点でOK

			Pattern beanPattern = Pattern.compile(KadaiConst.BEAN_PATTERN);
			Matcher beanMatcher = beanPattern.matcher(aDailyStr);

			HashMap<String, String> aBeanMap = new HashMap<String, String>();
			while (beanMatcher.find()) {
				String beanStr = beanMatcher.group();
				// 日ごとに分けた文字列で制御文字が入っていないかチェック
				if (ErrorCheck.checkEnableDayData(beanStr))
				{
					throw new KadaiException(ErrorCode.FILE_CONTAIN_CONTROL_WORD);
				}

				String[] tempArray = new String[2];
				tempArray = beanStr.split("\\\".*?\\\"");

				// keyが「start」「end」のデータを格納
				if (KEY_START.equals(tempArray[0]) || KEY_END.equals(tempArray[0]))
				{
					aBeanMap.put(tempArray[0], tempArray[1]);
				} else {
					throw new KadaiException(ErrorCode.INVALID_STRING);
				}
			}

			// 勤怠時間を計算
			String myWorkTime = calcWorkTime(aBeanMap.get(KEY_START), aBeanMap.get(KEY_END));

			// 出力モデルに日付と勤務時間を設定
			resultOutputModel.setOutputDate(aBeanMap.get(KEY_DATE));
			resultOutputModel.setOutputWorkTime(myWorkTime.toString());

		} catch (KadaiException kadaiEx) {
			resultOutputModel.setOutputErrorCode(kadaiEx.getErrorCode());
		}

		return resultOutputModel;

	}

}
