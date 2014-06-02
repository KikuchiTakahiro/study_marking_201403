/**
 *
 */
package jp.ktsystem.kadai201403.t_kikuchi.Models;

import jp.ktsystem.kadai201403.Exception.ErrorCode;

/**
 * ファイル出力モデル
 * @author TakahrioKikuchi
 *
 */
public class OutputModel {

	/**
	 * 日付
	 */
	private String outputDate ;




	/**
	 * その日に勤務時間
	 */
	private String outputWorkTime ;

	/**
	 * その日までの累計勤務時間
	 */
	private String outputTotalWorkTime;


	/**
	 * エラーコード
	 */
	private ErrorCode outputErrorCode  ;

	/**
	 * コンストラクタ
	 * @param anOutputDate 		日付
	 * @param anOutputWorkTime  勤務時間
	 */
	public OutputModel(String anOutputDate,String anOutputWorkTime)
	{
		this.outputDate = anOutputDate;
		this.outputWorkTime = anOutputWorkTime;
	}


	/** ファイル出力用文字列生成
	 * @return ファイル出力用文字列
	 */
	public String createOutputStr(){
		StringBuilder sb = new StringBuilder();
		// {"date":"20140101","
		sb.append("{\"date\":").append("\"").append(this.outputDate).append("\",\"");
		// workTime":480
		sb.append("workTime\":").append(this.outputWorkTime);
		// ,"total":480},
		sb.append(",\"total\":").append(this.outputTotalWorkTime).append("},");
		return sb.toString();
	}

	public void setOutputDate(String outputDate) {
		this.outputDate = outputDate;
	}

	public String getOutputDate() {
		return outputDate;
	}

	public void setOutputWorkTime(String outputWorkTime) {
		this.outputWorkTime = outputWorkTime;
	}

	public String getOutputWorkTime() {
		return outputWorkTime;
	}

	public String getOutputTotalWorkTime() {
		return outputTotalWorkTime;
	}

	public void setOutputTotalWorkTime(String outputTotalWorkTime) {
		this.outputTotalWorkTime = outputTotalWorkTime;
	}

	public ErrorCode getOutputErrorCode() {
		return outputErrorCode;
	}


	public void setOutputErrorCode(ErrorCode outputErrorCode) {
		this.outputErrorCode = outputErrorCode;
	}


}
