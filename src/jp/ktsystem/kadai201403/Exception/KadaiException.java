package jp.ktsystem.kadai201403.Exception;



/**
 * 課題エクセプションクラス
 * @author TakahrioKikuchi
 *
 */
public class KadaiException extends Exception{


private static final long serialVersionUID = 1L;

private ErrorCode errorCode;

/**
 * エラー
 * @param errorCode  エラーコード
 */
public KadaiException(ErrorCode errorCode)
{
  this.errorCode = errorCode;
}

/**
 * @return エラーコード
 */
public ErrorCode getErrorCode() {
  return this.errorCode;
}

}
