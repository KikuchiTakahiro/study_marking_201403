package jp.ktsystem.kadai201403.t_kikuchi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	 * 改行、空白、タブが含まれているかチェック
	 * @param aStr 対象文字列
	 * @return 改行、空白、タブが含まれている場合、ture
	 */
	public static boolean checkNewLineTabSpace(String aStr){
		// 引数が 空白、タブ、改行 をから文字に置換
		Matcher arrowControlStr = ARROW_CONTROL_CARACTOR_PATTERN.matcher(aStr);
		return arrowControlStr.find();
	}

	/**
	 *  一日分のデータをkey:valueのセットの文字列リストに成形
	 * @param aDayStr  一日分のデータの文字列
	 * @return key:value のセットの文字列が格納されたリスト
	 */
	public static List<String> getBeanList(String aDayStr){

		Pattern aBeanPattern = Pattern.compile("\"[a-zA-Z]*+\"\\s*?:\\s*?\"[\\d]*+\"");
		Matcher aBeanMatcher = aBeanPattern.matcher(aDayStr);

		// 日毎に整形した文字列を日ごとビーンに整形 ⇒ "date":"20140421","start":"0900","end":"1800"
		List<String> aFileStrListByDayBeanList = new  ArrayList<String>();
		while(aBeanMatcher.find()){
			aFileStrListByDayBeanList.add(aBeanMatcher.group());
		}

		return aFileStrListByDayBeanList;

	}


	/**
	 * 空白、タブ、改行をから文字で置換
	 * @param aList 空白、タブ、改行が入っている可能性がある文字列list
	 * @return 空白、タブ、改行をから文字で置換した文字列
	 */
	public static String replaceSpaceTabNewLine(List<String> aList)
	{
		StringBuilder sb = new StringBuilder();;
		for (String str : aList ){
			// 引数が 空白、タブ、改行 をから文字に置換
			Matcher arrowControlStr = ARROW_CONTROL_CARACTOR_PATTERN.matcher(str);
			sb.append(arrowControlStr.replaceAll(""));
		}
		return sb.toString() ;
	}


	/**
	 * 文字列を正規表現で区切る
	 * @param aStr 文字列
	 * @param aRegex 区切る正規表現文字列
	 * @return 対象文字列をい正規表現で区切ったlist
	 */
	public static List<String> splitStrByRegex (String aStr,String aRegex)
	{
		// 返却リスト
		List<String> aList  =  new ArrayList<String>();
		// 引数を]でスプリット
		// 月ごとに分割する
		aList = Arrays.asList(aStr.split(".*\\]")) ;
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
	public static boolean checkAmongStr(int aStartIndex ,int anEndIndex ,String aStr,String aRegexStr){

		String checkStr = aStr.substring(aStartIndex,anEndIndex);
		Pattern checkPattern = Pattern.compile(aRegexStr);
		Matcher checkMatcher = checkPattern.matcher(checkStr);
		return checkMatcher.matches();
	}


}
