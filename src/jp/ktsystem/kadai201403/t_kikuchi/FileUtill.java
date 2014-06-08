/**
 *
 */
package jp.ktsystem.kadai201403.t_kikuchi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import jp.ktsystem.kadai201403.Exception.ErrorCode;
import jp.ktsystem.kadai201403.Exception.KadaiException;

/**
 * ファイルのUtillクラス
 * @author TakahrioKikuchi
 *
 */
public class FileUtill {

	/**
	 * BOM文字
	 */
	public static final String UTF8_BOM = "\uFEFF";

	/**
	 * BOM取り除く
	 * @param s 対象文字列
	 * @return
	 */
	private static String removeUTF8BOM(String s) {
		if (s.startsWith(UTF8_BOM)) {
			s = s.substring(1);
		}
		return s;
	}


	/**
	 * ファイル読み込み
	 * @param aFileName  ファイルの絶対パス
	 * @return 読み込んだテキスト
	 * @throws KadaiException
	 */
	public static List<String> readFile(String aFileName) throws KadaiException
	{

		BufferedReader br = null;
		List<String> list = null;
		// ファイル存在確認
		if(! new File(aFileName).exists()){
			throw new KadaiException(ErrorCode.FILE_NOT_FOUND);
		}

		try {

			boolean firstLine = true;

			br = new BufferedReader(new InputStreamReader(new FileInputStream(aFileName), "UTF8"));
			String str;
			list = new ArrayList<String>();
			while ((str = br.readLine()) != null) {
				if (firstLine) {
					str = removeUTF8BOM(str);
					firstLine = false;
				}
				list.add(str);
			}
		} catch (IOException e) {
			throw new KadaiException(ErrorCode.FILE_INPPUT_FAILED);
		} finally
		{
			try {
				if (br != null) {
					br.close();
				}
			} catch (Exception e2) {
			}
		}
		return list;
	}

	/**
	 * ファイル書き込み(エラーコードのみ)
	 * @param aFilePath : 出力ファイルのディレクトリパス
	 * @param anErrorCode : 書き込ませるエラーコード
	 * @param aFileName : ファイル名
	 * @aFileName : ファイル名 (省略する場合は、NULLを指定)
	 * @throws KadaiException
	 */
	public static void writeErrorCodeFile(String aFilePath, ErrorCode anErrorCode,String aFileName) throws KadaiException
	{
		// ファイルパスチェック
		if(null ==aFilePath || "".equals(aFilePath)){
			throw new KadaiException(ErrorCode.OUTPUT_PATH_IS_NULL_OR_EMPTY);
		}

		// ファイルパス＋ ファイル名 からファイルの絶対パスを作成
		StringBuilder sb  =new StringBuilder();
		sb.append(aFilePath);

		if (null != aFileName){
			sb.append("\\").append(aFileName);
		}

		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(new File(sb.toString()),false));
			bw.write(anErrorCode.toString());
			bw.newLine();
			bw.close();
		} catch (IOException e) {
			throw new KadaiException(ErrorCode.FILE_OUTPUT_FAILD);
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (Exception ex) {
				}
			}
		}
	}


	/**
	 * ファイル書き込み
	 * @param aFilePath : 出力ファイルのディレクトリパス
	 * @param aList : 書き込ませる文字列
	 * @param aFileName : ファイル名
	 * @aFileName : ファイル名 (省略する場合は、NULLを指定)
	 * @throws KadaiException
	 */
	public static void writeFile(String aFilePath, List<String> aList,String aFileName) throws KadaiException
	{
		// ファイルパスチェック
		if(null ==aFilePath || "".equals(aFilePath)){
			throw new KadaiException(ErrorCode.OUTPUT_PATH_IS_NULL_OR_EMPTY);
		}

		// ファイルパス＋ ファイル名 からファイルの絶対パスを作成
		StringBuilder sb  =new StringBuilder();
		sb.append(aFilePath);

		if (null != aFileName){
			sb.append("\\").append(aFileName);
		}

		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(new File(sb.toString()),false));
			for (String str : aList) {
				bw.write(str);
				bw.newLine();
			}
			bw.close();
		} catch (IOException e) {
			throw new KadaiException(ErrorCode.FILE_OUTPUT_FAILD);
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (Exception ex) {
				}
			}
		}
	}

	/**
	 * ファイル書き込み
	 * @param aFilePath : 出力ファイルのディレクトリパス
	 * @param anInputStr : 書き込ませる文字列
	 * @param aFileName : ファイル名
	 * @aFileName : ファイル名 (省略する場合は、NULLを指定)
	 * @throws KadaiException
	 */
	public static void writeFile(String aFilePath, String anInputStr,String aFileName) throws KadaiException{
		// ファイルパスチェック
				if(null ==aFilePath || "".equals(aFilePath)){
					throw new KadaiException(ErrorCode.OUTPUT_PATH_IS_NULL_OR_EMPTY);
				}

				// ファイルパス＋ ファイル名 からファイルの絶対パスを作成
				StringBuilder sb  =new StringBuilder();
				sb.append(aFilePath);

				if (null != aFileName){
					sb.append("\\").append(aFileName);
				}

				BufferedWriter bw = null;
				try {
					bw = new BufferedWriter(new FileWriter(new File(sb.toString()),true));

						bw.write(anInputStr);
						bw.newLine();

					bw.close();
				} catch (IOException e) {
					throw new KadaiException(ErrorCode.FILE_OUTPUT_FAILD);
				} finally {
					if (bw != null) {
						try {
							bw.close();
						} catch (Exception ex) {
						}
					}
				}

	}
}
