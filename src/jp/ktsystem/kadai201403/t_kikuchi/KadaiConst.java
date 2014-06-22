/**
 *
 */
package jp.ktsystem.kadai201403.t_kikuchi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 課題用定数クラス
 * @author TakahrioKikuchi
 *
 */
public class KadaiConst {

	/**
	 * 空白文字
	 */
	public static final String EMPTY_STR = "";

	/**
	 * 規定仕事開始時間
	 */
	public static final int PRESCRIBED_WORK_START_TIME = 900;

	/**
	 * 規定仕事開始時刻
	 */
	public static final int PRESCRIBED_WORK_START_HOUR = 9;

	/**
	 * お昼休憩開始時間
	 */
	public static final String LUNCH_START_TIME = "1200";

	/**
	 * お昼休憩終了時間
	 */
	public static final String LUNCH_END_TIME = "1300";

	/**
	 * 夕方休憩開始時間
	 */
	public static final String AFTERNOON_BREAK_START_TIME = "1800";

	/**
	 * 夕方休憩終了時間
	 */
	public static final String AFTERNOON_BREAK_END_TIME = "1830";

	/**
	 * 規定仕事終了時間
	 */
	public static final int PRESCRIBED_WORK_END_TIME = 3300;

	/**
	 * 深夜0時
	 */
	public static final int MIDNIGHT = 2400;


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
	 * 左大かっこのみ許可正規表現
	 */
	public static final String ONLY_LEFT_LARGE_PARNTHESIS_REGEX = "\\s*\\[\\s*";


	/**
	 * 左大かっこのみ許可パターン
	 */
	public static final Pattern ONLY_LEFT_LARGE_PARNTHESIS_PATTERN = Pattern.compile(ONLY_LEFT_LARGE_PARNTHESIS_REGEX);

	/**
	 * 右大かっこのみ許可正規表現
	 */
	public static final String ONLY_RIGHT_LARGE_PARNTHESIS_REGEX = "\\s*\\]\\s*";


	/**
	 * 右大かっこのみ許可パターン
	 */
	public static final Pattern ONLY_RIGHT_LARGE_PARNTHESIS_PATTERN = Pattern.compile(ONLY_RIGHT_LARGE_PARNTHESIS_REGEX);




	/**
	 * カンマのみ許可正規表現
	 */
	public static final String ONLY_COMMA_REGEX ="\\s*,\\s*";


	/**
	 * カンマの意味許可パターン
	 */
	public static final Pattern ONLY_COMMA_PATTERN  = Pattern.compile(ONLY_COMMA_REGEX );

	/**
	 * 左中かっこのみ許可正規表現
	 */
	public static final String ONLY_LEFT_MIDDLE_PARENTHESIS ="\\s*\\{\\s*";


	/**
	 * 左中かっこのみ許可パターン
	 */
	public static final Pattern ONLY_LEFT_MIDDLE_PARENTHESIS_PATTERN = Pattern.compile(ONLY_LEFT_MIDDLE_PARENTHESIS);

	/**
	 * 右中かっこのみ許可正規表現
	 */
	public static final String ONLY_RIGHT_MIDDLE_PARENTHESIS ="\\s*\\}\\s*";

	/**
	 * 右中かっこのみ許可パターン
	 */
	public static final Pattern ONLY_RIGHT_MIDDLE_PARENTHESIS_PATTERN = Pattern.compile(ONLY_RIGHT_MIDDLE_PARENTHESIS);


    /**
     * コロンと左中かっこのみ許容正規表現
     */
    public static final String ONLY_COLON_AND_RIGHT_MIDDLE_PARENTHESIS = "\\s*:\\s\\{\\s*";

    /**
     * コロンと左中かっこのみ許容パターン
     */
    public static final Pattern ONLY_COLON_AND_RIGHT_MIDDLE_PARENTHESIS_PATTERN = Pattern.compile(ONLY_COLON_AND_RIGHT_MIDDLE_PARENTHESIS);



	/**
	 * レベル１用の日ごとの分割正規表現
	 */
	public static final String LEVEL1_DAILY_REGEX = "\\{[\\s\\S]*?\\}";

	/**
	 * レベル１用の日ごとの分割パターン
	 * 中かっこから中かっこまで
	 */
	public static  Pattern LEVEL1_DAILY_PATTERN =  Pattern.compile(LEVEL1_DAILY_REGEX);



	/**
	 * 月文字列取得正規表現
	 * 201401みたいなの
	 */
	public static final String MONTH_STR_REGEX = "\"\\d{6}\"";

	/**
	 * 月文字列取得パターン
	 */
	public static final Pattern MONTH_STR_PATTERN = Pattern.compile(MONTH_STR_REGEX );



	/**
	 * ダブルコーテーションで囲われている文字列を取得正規表現
	 *  "start":"0900"
	 */
	public static final String BETWEEN_DOUBLE_QUOTATION_WORD_REGEX = "\".*?\"";

	/**
	 * ダブルコーテーションで囲われている文字列取得パターン
	 */
	public static final Pattern BETWEEN_DOUBLE_QUOTATION_WORD_PATTERN = Pattern.compile(BETWEEN_DOUBLE_QUOTATION_WORD_REGEX );


	/**
	 * レベル２用の月毎の分割正規表現
	 * \s*"\d{6}"\s*:\s*[(\[].+?])\]]
	 * \s*"\d{6}"\s*:\s*[\[].+?[\]]
	 */
	public static final String LEVEL2_MONTHLY_REGEX = "\\s*\"\\d{6}\"\\s*:\\s*[\\[].+?[\\]]";


	/**
	 * レベル２用の月毎の分割パターン
	 */
	public static final Pattern LEVEL2_MONTHLY_PATTERN = Pattern.compile(LEVEL2_MONTHLY_REGEX);


	/**
	 * レベル２用の日ごと分割正規表現
	 * "20140101" : { "start":"0900", "end":"1800" }
	 */
	public static final String LEVEL2_DAILY_REGEX = "\"\\d{8}\"\\s*:\\s*\\{\\s*[\":a-zA-Z\\d,\\s]*}";


	/**
	 * レベル２用の日ごと分割パターン
	 */
	public static final Pattern LEVEL2_DAILY_PATTERN = Pattern.compile(LEVEL2_DAILY_REGEX);


	/**
	 * レベル２の日付部分取得正規表現
	 * 例) "20140101"
	 */
	public static final String LEVEL2_DAY_STR_REGEX = "\"\\d{8}\"";


	/**
	 * レベル２の日付部分取得パターン
	 */
	public static final Pattern LEVEL2_DAY_STR_PATTERN = Pattern.compile(LEVEL2_DAY_STR_REGEX );


	/**
	 * レベル２の日ごとデータの日付を抜いた部分 正規表現
	 * "201402": [ {  に一致
	 * "\d{6}"\s*:\s*\[\s*\{\s*
	 */
	public static final String LEVEL2_SPACE_OF_DAY_AND_BEAN_REGEX = "\\s*\"\\d{6}\"\\s*:\\s*\\[\\s*\\{\\s*";

	/**
	 * レベル２の日ごとデータの日付を抜いた部分 パターン
	 */
	public static final Pattern LEVEL2_SPACE_OF_DAY_AND_BEAN_PATTERN = Pattern.compile(LEVEL2_SPACE_OF_DAY_AND_BEAN_REGEX);


	/**
	 * beanの正規表現
	 * 例) "start":"0900" みたいなの
	 */
	public static final String BEAN_REGEX ="\\s*\"[a-zA-Z]+\"\\s*:\\s*\"\\d+\"";


	/**
	 * beanのパターン
	 */
	public static final Pattern BEAN_PATTERN = Pattern.compile(BEAN_REGEX);

	public static final String SPLIT_COLON_REGEX = "\\s*:\\s*";




	/**
	 * beanのkeyリスト レベル1
	 */
	public static final List<String> BEAN_KEY_LIST_LEVEL1 ;
	static {
		List<String> list = new ArrayList<String>();
		list.add(KEY_DATE);
		list.add(KEY_START);
		list.add(KEY_END);
		// 要素を追加できないようにしておく
		BEAN_KEY_LIST_LEVEL1 = Collections.unmodifiableList(list);
	}


	/**
	 * beanのkeyリスト レベル2
	 */
	public static final List<String> BEAN_KEY_LIST_LEVEL2;
	static {
		List<String> list = new ArrayList<String>();
		list.add(KEY_START);
		list.add(KEY_END);
		// 要素を追加できないようにしておく
		BEAN_KEY_LIST_LEVEL2 = Collections.unmodifiableList(list);
	}



}
