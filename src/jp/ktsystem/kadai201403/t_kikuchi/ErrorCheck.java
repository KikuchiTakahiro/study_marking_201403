package jp.ktsystem.kadai201403.t_kikuchi;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.ktsystem.kadai201403.Exception.KadaiException;


/**
 * エラーチェッククラス
 * @author TakahrioKikuchi
 *
 */
public class ErrorCheck {

	/**
	 * 数字の正規表現パターン
	 */
	private static final Pattern ENABLED_NUM_PATTERN = Pattern.compile("^[\\d]{4}$");

	/**
	 *  日毎データにマッチするパターン
	 *  {"start":"0900", "end":"1800" }
	 *  \s*"[a-zA-Z]+"\s*:\s*"\d+"\s*
	 */
	private static final Pattern ENABLED_DAY_PATTERN = Pattern.compile("\\s*\"[a-zA-Z]+\"\\s*:\\s*\"\\d+\"\\s*");
//	private static final Pattern ENABLED_DAY_PATTERN = Pattern.compile("^\\{((\\\"[a-zA-Z]*\\\"):(\\\"\\d*\\\"),*)*\\}$");




	/**  エラーチェック （入力文字列がnullまたは空(文字列長0)
	 * @param anStr  対象文字列
	 * @return NullOrEmptyの時にtrueを返す
	 * @throws KadaiException
	 */
	public static boolean isNullOrEmpty(String anStr) throws KadaiException
	{
		return ((null == anStr) || (KadaiConst.EMPTY_STR.equals(anStr)));
	}

	/**  エラーチェック（HHmm形式のみOK）
	 *   ⇒それ以外は、はじく
	 * @param anStr 対象文字列
	 * @author TakahrioKikuchi
	 * @return 引数が4桁でない場合は、true
	 * @throws KadaiException
	 */
	public static boolean checkEnableWord(String anStr) throws KadaiException
	{
		// 引数が 4桁数字の場合、
		Matcher calculationIsEnable = ENABLED_NUM_PATTERN.matcher(anStr);
		// 数字4桁ではない場合、true
		return !calculationIsEnable.matches();
	}

	/**
	 * 日ごとデータの形かどうか
	 * @param anStr 対象文字列
	 * @return 引数が日ごとデータの形ではない場合、true を返す
	 * @throws KadaiException
	 */
	public static boolean checkEnableDayData(String anStr ) throws KadaiException
	{

		// 引数が 日ごとデータの形の場合、、
		Matcher enabledDayData = ENABLED_DAY_PATTERN.matcher(anStr);
		//日ごとデータの形ではない場合、 true
		return !enabledDayData.matches();
	}

}
