/**
 *
 */
package jp.ktsystem.kadai201403.t_kikuchi;

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
	 * カンマのみ許可パターン
	 */
	public static final String ONLY_COMMA_PATTERN ="\\s*:\\s*";

	/**
	 * 左中かっこのみ許可パターン
	 */
	public static final String ONLY_LEFT_MIDDLE_PARENTHESIS ="\\s*\\{\\s*";

	/**
	 * 右中かっこのみ許可パターン
	 */
	public static final String ONLY_RIGHT_MIDDLE_PARENTHESIS ="\\s*\\}\\s*";

	/**
	 * レベル１用の日ごとの分割パターン
	 */
	public static final String LEVEL1_DAILY_PATTERN = "\\{[\\s\\S]*?\\}";

	/**
	 * レベル２用の月毎の分割パターン
	 * \s*"\d{6}"\s*:\s*[(\[].+?])\]]
	 */
	public static final String LEVEL2_MONTHLY_PATTERN ="\\s*\"\\d{6}\"\\s*:\\s*[(\\[].+*[)\\]]";

	/**
	 * レベル２用の日ごと分割パターン
	 * "20140101" : { "start":"0900", "end":"1800" }
	 */
	public static final String LEVEL2_DAILY_PATTERN = "\"\\d{8}\"\\s*:\\s*\\{\\s*[\":a-zA-Z\\d,\\s]*}";

	/**
	 * レベル２の日付部分取得パターン
	 * 例) "20140101"
	 */
	public static final String LEVEL2_DAY_STR_PATTERN = "\"\\d{8}\"";


	/**
	 * beanのパターン
	 * 例) "start":"0900" みたいなの
	 */
	public static final String BEAN_PATTERN ="\\s*\"[a-zA-Z]+\"\\s*:\\s*\"\\d+\"";



}
