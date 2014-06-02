/**
 *
 */
package jp.ktsystem.kadai201403.Exception;

/**
 * @author TakahrioKikuchi
 *
 */

public enum ErrorCode
{
  /**
 * 入力文字列がnull、または空文字
 */
EMPTY,
/**
 * 入力文字列が不正
 */
INVALID_STRING,
/**
 * 入力文字列が出社時刻、退社時刻として不正
 */
INVALID_TIME,
/**
 * 退社時刻が出社時刻以前
 */
STARTTIME_OVER_ENDTIME,
/**
 * データのカラム数が不正
 */
INVALID_COLUMN_DIGIT,
/**
 * 入力ファイルのパスがnullまたは空文字
 */
INPUT_PATH_IS_NULL_OR_EMPTY,
/**
 * 入力ファイルが存在しない
 */
FILE_NOT_FOUND,

/**
 * 入力ファイルの読み込みに失敗した
 */
FILE_INPPUT_FAILED,
/**
 * 出力ファイルのパスがnullまたは空文字
 */
OUTPUT_PATH_IS_NULL_OR_EMPTY,
/**
 * ファイルの出力に失敗した
 */
FILE_OUTPUT_FAILD,
/**
 * ファイル内に制御文字（改行、タブ、スペースを除く）が含まれる場合
 */
FILE_CONTAIN_CONTROL_WORD,
/**
 * 読み込みファイルの日付の値が重複する場合
 */
DATE_OVERLAP
}



