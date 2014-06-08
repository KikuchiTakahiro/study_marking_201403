/**
 *
 */
package jp.ktsystem.kadai201403.t_kikuchi.Models;

import java.util.List;

import jp.ktsystem.kadai201403.Exception.ErrorCode;

/**
 * ターゲット文字列クラス
 * @author TakahrioKikuchi
 *
 */
public class TargetStrModel {

	/**
	 * 対象文字列
	 */
	private List<String> targetList;

	/**
	 * エラーコード
	 */
	private ErrorCode targetErrorCode;


	public List<String> getTargetList() {
		return targetList;
	}

	public void setTargetList(List<String> targetList) {
		this.targetList = targetList;
	}

	public ErrorCode getTargetErrorCode() {
		return targetErrorCode;
	}

	public void setTargetErrorCode(ErrorCode targetErrorCode) {
		this.targetErrorCode = targetErrorCode;
	}

}
