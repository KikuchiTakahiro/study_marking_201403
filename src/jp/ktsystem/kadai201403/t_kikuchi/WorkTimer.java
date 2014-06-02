/**
 *
 */
package jp.ktsystem.kadai201403.t_kikuchi;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import jp.ktsystem.kadai201403.Exception.ErrorCode;
import jp.ktsystem.kadai201403.Exception.KadaiException;

/**
 * 勤務時間測定クラス
 * @author TakahrioKikuchi
 *
 */
public class WorkTimer {

	/**
	 * 出社時間
	 */
	private int workStartTime;

	/**
	 * 退社時間
	 */
	private int workEndTime;

	/**
	 * 開始時刻の時間部分
	 */
	private int hourOfWorkStratTime;
	/**
	 * 開始時刻の分部分
	 */
	private int minutesOfWorkStartTime;
	/**
	 * 終了時刻の時間部分
	 */
	private int hourOfWorkEndTime;
	/**
	 * 終了時刻の分部分
	 */
	private int minutesOfWorkEndTime;

	/**
	 * 引数文字列の時間部分開始インデックス
	 */
	private static int HOUR_START_INDEX = 0;

	/**
	 * 引数文字列の時間部分終了インデックス
	 */
	private static int HOUR_END_INDEX = 2;

	/**
	 * 時間を分に変換するときの定数
	 */
	private static int HOUR_2_MINUTES = 60;

	/**
	 * 分の上限値
	 */
	private static int MINUTES_PART_UPPER = 60;

	/**
	 * 休憩時間 key：休憩開始時間 value:休憩終了時間
	 */
	private HashMap<String, String> breakTimeMap = new HashMap<String, String>();

	/** コンストラクタ
	 * @param aStartTime :開始時間(HHmm形式)
	 * @param aEndTime :終了時間(HHmm形式)
	 */
	public WorkTimer(String aStartTime, String aEndTime)
	{
		// 開始時刻
		workStartTime = Integer.parseInt(aStartTime);
		hourOfWorkStratTime = Integer.parseInt(aStartTime.substring(HOUR_START_INDEX, HOUR_END_INDEX)) * HOUR_2_MINUTES;
		minutesOfWorkStartTime = Integer.parseInt(aStartTime.substring(HOUR_END_INDEX));

		// 終了時刻
		workEndTime = Integer.parseInt(aEndTime);
		hourOfWorkEndTime = Integer.parseInt(aEndTime.substring(HOUR_START_INDEX, HOUR_END_INDEX)) * HOUR_2_MINUTES;
		minutesOfWorkEndTime = Integer.parseInt(aEndTime.substring(HOUR_END_INDEX));

		// 休憩時間MAP作成
		breakTimeMap.put(KadaiConst.LUNCH_START_TIME, KadaiConst.LUNCH_END_TIME);
		breakTimeMap.put(KadaiConst.AFTERNOON_BREAK_START_TIME, KadaiConst.AFTERNOON_BREAK_END_TIME);

	}

	/**
	 * 就業時間計算
	 * @return 就業時間(分単位)
	 * @throws KadaiException
	 */
	public int calcWorkingTime() throws KadaiException
	{

		// エラーチェック //
		// 出社時間チェック
		// 0900以前の場合、9:00出社扱い
		if (KadaiConst.PRESCRIBED_WORK_START_TIME > workStartTime)
		{
			hourOfWorkStratTime = KadaiConst.PRESCRIBED_WORK_START_HOUR * HOUR_2_MINUTES;
			minutesOfWorkStartTime = 0;
		}

		// 出社時間が23:59を超えている場合エラー
		if (KadaiConst.MIDNIGHT <= workStartTime)
		{
			throw new KadaiException(ErrorCode.INVALID_TIME);
		}

		// 退社時間チェック 次の日の9時前には退社
		if (KadaiConst.PRESCRIBED_WORK_END_TIME <= workEndTime)
		{
			throw new KadaiException(ErrorCode.INVALID_TIME);
		}

		// 分部分のチェック
		if (MINUTES_PART_UPPER <= minutesOfWorkStartTime || MINUTES_PART_UPPER <= minutesOfWorkEndTime)
		{
			throw new KadaiException(ErrorCode.INVALID_TIME);
		}

		// 開始時刻＞退社時刻
		if (hourOfWorkStratTime + minutesOfWorkStartTime > hourOfWorkEndTime + minutesOfWorkEndTime) {
			throw new KadaiException(ErrorCode.STARTTIME_OVER_ENDTIME);
		}

		// 休み時間算出
		int recessTime = calcBreakTime(breakTimeMap);

		// 休憩時間が求められたので、出勤退勤時間から分計算する
		int workingTime = calcIntervalByMinutes(hourOfWorkEndTime, hourOfWorkStratTime, minutesOfWorkEndTime,
				minutesOfWorkStartTime) - recessTime;

		return workingTime;
	}

	/**
	 * 時間計算式
	 * @param anHourFrom 時間部分_前
	 * @param anHourTo   時間部分_後ろ
	 * @param aMinuteFrom  分部分_前
	 * @param aMinuteTo    分部分_後ろ
	 * @return
	 */
	private int calcIntervalByMinutes(int anHourFrom, int anHourTo, int aMinuteFrom, int aMinuteTo)
	{
		return (anHourFrom - anHourTo) + (aMinuteFrom - aMinuteTo);
	}

	/**
	 * 休み時間計算
	 * @param aMap key:休み開始時刻 value:休み終了時間
	 * @return 休んだ時間（分単位）
	 */
	private int calcBreakTime(HashMap<String, String> aMap)
	{

		// 休み時間考慮(分単位)
		int recessTime = 0;

		//すべてのキー値を取得
		Set<String> keySets = aMap.keySet();
		Iterator<String> keyIte = keySets.iterator();
		while (keyIte.hasNext()) { //ループ。反復子iteratorによる　キー　取得
			String key = keyIte.next();
			String value = aMap.get(key); //キーよりvalueを取得

			int breakStartTime = Integer.parseInt(key);
			int breakEndTime = Integer.parseInt(value);
			int hourOfBreakStartTime = Integer.parseInt(key.substring(HOUR_START_INDEX, HOUR_END_INDEX))
					* HOUR_2_MINUTES;
			int minutesOfBreakStartTime = Integer.parseInt(key.substring(HOUR_END_INDEX));

			int hourOfBreakEndTime = Integer.parseInt(value.substring(HOUR_START_INDEX, HOUR_END_INDEX))
					* HOUR_2_MINUTES;
			int minutesOfBreakEndTime = Integer.parseInt(value.substring(HOUR_END_INDEX));

			/* 休み時間がない場合、
			* 仕事終了時間が休み時間開始前、 例）休み時間(12:00~13:00)で、9:00から11:30勤務の場合、
			* 仕事開始時間が休み時間終了後、 例）休み時間(12:00~13:00)で、13:00から18:00勤務の場合、
			* 処理しない
			*/
			if (breakEndTime < workStartTime || workEndTime < breakStartTime)

			{
				continue;
			}

			// 仕事終了時間が休み時間中の場合、例）休み時間(12:00~13:00)で、9:00から12：45勤務の場合、
			if (workStartTime < breakStartTime && workEndTime <= breakEndTime && breakStartTime < workEndTime)

			{
				recessTime += calcIntervalByMinutes(hourOfWorkEndTime, hourOfBreakStartTime, minutesOfWorkEndTime,
						minutesOfBreakStartTime);
			}

			// 休み時間に来て帰った場合、
			else if (breakStartTime <= workStartTime && workEndTime <= breakEndTime)
			{
				recessTime += calcIntervalByMinutes(hourOfWorkEndTime, hourOfWorkStratTime, minutesOfWorkEndTime,
						minutesOfWorkStartTime);
			}

			// 仕事開始時間が休み時間中の場合、例）休み時間(12:00~13:00)で、12:30から18:00勤務の場合、
			else if (breakStartTime <= workStartTime && workStartTime < breakEndTime)
			{
				recessTime += calcIntervalByMinutes(hourOfBreakEndTime, hourOfWorkStratTime, minutesOfBreakEndTime,
						minutesOfWorkStartTime);
			}

			// 仕事開始時間が休憩開始時間より前、仕事終了時間が休憩時間終了後の場合、例）休み時間(12:00~13:00)で、9：00から18：00勤務の場合、
			else if (workStartTime < breakStartTime && breakEndTime <= workEndTime)
			{
				recessTime += calcIntervalByMinutes(hourOfBreakEndTime, hourOfBreakStartTime, minutesOfBreakEndTime,
						minutesOfBreakStartTime);
			}
		}
		return recessTime;
	}

}
