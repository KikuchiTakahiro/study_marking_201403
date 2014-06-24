package jp.ktsystem.studymarking.kadai201403.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.ktsystem.kadai201403.Exception.ErrorCode;
import jp.ktsystem.kadai201403.Exception.KadaiException;
import jp.ktsystem.kadai201403.t_kikuchi.Kadai;
import jp.ktsystem.kadai201403.t_kikuchi.KadaiUtill;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * @author Y.Kimura
 * @since 2013/11/10
 */
@RunWith(JUnit4.class)
public class TestKadai201311 {

	/**
	 *
	 */
	@Test
	public void test_G01T101() {
		assertFail(null, "1800", ErrorCode.EMPTY);
	}

	/**
	 *
	 */
	@Test
	public void test_G01T102() {
		assertFail("0900", null, ErrorCode.EMPTY);
	}

	/**
	 *
	 */
	@Test
	public void test_G01T103() {
		assertFail("", "1800", ErrorCode.EMPTY);
	}

	/**
	 *
	 */
	@Test
	public void test_G01T104() {
		assertFail("0900", "", ErrorCode.EMPTY);
	}

	/**
	 *
	 */
	@Test
	public void test_G01T105() {
		assertFail("abcd", "1800", ErrorCode.INVALID_STRING);
	}

	/**
	 *
	 */
	@Test
	public void test_G01T106() {
		assertFail("0900", "18 0", ErrorCode.INVALID_STRING);
	}

	/**
	 *
	 */
	@Test
	public void test_G01T107() {
		assertFail("2400", "1800", ErrorCode.INVALID_TIME);
	}

	/**
	 *
	 */
	@Test
	public void test_G01T108() {
		assertFail("900", "1800", ErrorCode.INVALID_STRING);
	}

	/**
	 *
	 */
	@Test
	public void test_G01T109() {
		assertFail("0975", "1800", ErrorCode.INVALID_TIME);
	}

	/**
	 *
	 */
	@Test
	public void test_G01T110() {
		assertFail("0900", "3300", ErrorCode.INVALID_TIME);
	}

	/**
	 *
	 */
	@Test
	public void test_G01T111() {
		assertFail("0900", "180", ErrorCode.INVALID_STRING);
	}

	/**
	 *
	 */
	@Test
	public void test_G01T112() {
		assertFail("0900", "1899", ErrorCode.INVALID_TIME);
	}

	/**
	 *
	 */
	@Test
	public void test_G01T113() {
		assertFail("1200", "1000", ErrorCode.STARTTIME_OVER_ENDTIME);
	}

	/**
	 *
	 */
	@Test
	public void test_G01T201() {
		assertEquals("0900", "1800", "480");
	}

	/**
	 *
	 */
	@Test
	public void test_G01T202() {
		assertEquals("0830", "1830", "480");
	}

	/**
	 *
	 */
	@Test
	public void test_G01T203() {
		assertEquals("0900", "1845", "495");
	}

	/**
	 *
	 */
	@Test
	public void test_G01T204() {
		assertEquals("0859", "1930", "540");
	}

	/**
	 *
	 */
	@Test
	public void test_G01T205() {
		assertEquals("0900", "1245", "180");
	}

	/**
	 *
	 */
	@Test
	public void test_G01T206() {
		assertEquals("0900", "1500", "300");
	}

	/**
	 *
	 */
	@Test
	public void test_G01T207() {
		assertEquals("1200", "1820", "300");
	}

	/**
	 *
	 */
	@Test
	public void test_G01T208() {
		assertEquals("1300", "2100", "450");
	}

	/**
	 *
	 */
	@Test
	public void test_G01T209() {
		assertEquals("1800", "3000", "690");
	}

	/**
	 *
	 */
	@Test
	public void test_G01T210() {
		assertEquals("0830", "3259", "1349");
	}

	/**
	 *
	 */
	@Test
	public void test_G01T211() {
		assertEquals("2359", "3259", "540");
	}

	/**
	 *
	 */
	@Test
	public void test_G01T212() {
		assertFail("2400", "1800", ErrorCode.INVALID_TIME);
	}

	/**
	 *
	 */
	@Test
	public void test_G01T213() {
		assertFail("0800", "0830", ErrorCode.STARTTIME_OVER_ENDTIME);
	}

	//// ここから2014/03のtest

	/**
	 * エラーコード Empty
	 */
	@Test
	public void test_G02T101()
	{

		assertFailLevel1(null, "C:\\workspace_study\\2014_3\\test\\Output\\hoge4.txt",
				ErrorCode.INPUT_PATH_IS_NULL_OR_EMPTY);
	}

	/**
	 * エラーコード Empty
	 */
	@Test
	public void test_G02T102()
	{

		assertFailLevel1("C:\\workspace_study\\2014_3\\test\\Input\\test1Data.txt", null,
				ErrorCode.OUTPUT_PATH_IS_NULL_OR_EMPTY);
	}

	/**
	 * エラーコード Empty
	 */
	@Test
	public void test_G02T103()
	{

		assertFailLevel1("", "hoge", ErrorCode.INPUT_PATH_IS_NULL_OR_EMPTY);
	}

	/**
	 * エラーコード Empty
	 */
	@Test
	public void test_G02T104()
	{

		assertFailLevel1("hoge", "", ErrorCode.OUTPUT_PATH_IS_NULL_OR_EMPTY);
	}

	/**
	 * 入力パスが存在しないパス
	 */
	@Test
	public void test_G02T105()
	{

		assertFailLevel1("c:\\hoge", "C:\\workspace_study\\2014_3\\test\\Output\\hoge4.txt", ErrorCode.FILE_NOT_FOUND);

	}

	/**
	 * 対象ファイルが指定のファイル形式ではない
	 * ⇒ エラーコードはファイルに出力されるので、エラーコードは返さない
	 * ⇒書き込まれるエラーコードはErrorCode.FILE_CONTAIN_CONTROL_WORD
	 */
	@Test
	public void test_G02T106()
	{
		assertEqualsForLevel1("C:\\workspace_study\\2014_3\\test\\Input\\LV1_json形式ではない.txt",
				"C:\\workspace_study\\2014_3\\test\\Output\\hoge4.txt");
	}

	/**
	 * 対象ファイルに制御文字を含む
	 * ⇒ファイルにエラーコード書き込む
	 * ⇒ErrorCode.FILE_CONTAIN_CONTROL_WORD
	 */
	@Test
	public void test_G02T107()
	{

		assertEqualsForLevel1("C:\\workspace_study\\2014_3\\test\\Input\\test1Data_制御文字含み.txt",
				"C:\\workspace_study\\2014_3\\test\\Output\\hoge4.txt");
	}

	/**
	 * 対象ファイルのkeyが足りない
	 */
	@Test
	public void test_G02T108()
	{

		assertEqualsForLevel1("C:\\workspace_study\\2014_3\\test\\Input\\test1Data_key足りない.txt",
				"C:\\workspace_study\\2014_3\\test\\Output\\hoge4.txt");
	}

	/**
	 * 対象ファイルのkeyが多い
	 */
	@Test
	public void test_G02T109()
	{

		assertEqualsForLevel1("C:\\workspace_study\\2014_3\\test\\Input\\test1Data_key多い.txt",
				"C:\\workspace_study\\2014_3\\test\\Output\\hoge5.txt");
	}

	/**
	 * 対象ファイルのkeyが重複
	 */
	@Test
	public void test_G02T110()
	{

		assertEqualsForLevel1("C:\\workspace_study\\2014_3\\test\\Input\\test1Data_key重複.txt",
				"C:\\workspace_study\\2014_3\\test\\Output\\hoge6.txt");
	}

	/**
	 * 対象ファイルのvalueがnull
	 */
	@Test
	public void test_G02T111()
	{

		assertEqualsForLevel1("C:\\workspace_study\\2014_3\\test\\Input\\test1Data_valueNULL.txt",
				"C:\\workspace_study\\2014_3\\test\\Output\\hoge7.txt");
	}

	/**
	 * 出力ファイルが存在しない
	 */
	@Test
	public void test_G02T117()
	{

		assertFailLevel1("C:\\workspace_study\\2014_3\\test\\Input\\test1Data.txt",
				"C:\\workspace_study\\2014_3\\test\\hogepiyot\\hoge7.txt",ErrorCode.FILE_OUTPUT_FAILD);
	}
	//test_ReadOnly
	/**
	 * 出力ファイルが読み取り専用
	 */
	@Test
	public void test_G02T118()
	{

		assertFailLevel1("C:\\workspace_study\\2014_3\\test\\Input\\test1Data.txt",
				"C:\\workspace_study\\2014_3\\test\\Output\\test_ReadOnly.txt",ErrorCode.FILE_OUTPUT_FAILD);
	}

	//test_notUTF8
	/**
	 * 出力ファイルが存在し、UTF8以外
	 * ⇒ファイルが普通作られるだけ
	 */
	@Test
	public void test_G02T119()
	{

		assertEqualsForLevel1("C:\\workspace_study\\2014_3\\test\\Input\\test1Data_utf8以外.txt",
				"C:\\workspace_study\\2014_3\\test\\Output\\test_notUTF8.txt");
	}




	/**
	 * レベル1
	 */
	@Test
	public void test_G02T210()
	{

		assertEqualsForLevel1("C:\\workspace_study\\2014_3\\test\\Input\\test1Data.txt",
				"C:\\workspace_study\\2014_3\\test\\Output\\hoge4.txt");

	}

	/**
	 *レベル２のテスト
	 */
	@Test
	public void test_G02T201()
	{

		assertEqualsForLevel2("C:\\workspace_study\\2014_3\\test\\Input\\testLevel2Data.txt",
				"C:\\workspace_study\\2014_3\\test\\Output");

	}

	// testLevel2Data.txt

	/**
	 *
	 */
	@Test
	public void test_G02T777()
	{

		Pattern aMonthPattern = Pattern.compile(",");
		Matcher aMonthMatcher = aMonthPattern.matcher(",,,,,,");
		while (aMonthMatcher.find()) {

			System.out.println(aMonthMatcher.groupCount());

		}

	}

	/**
	 * ���퓮��m�F
	 * @param aStartTime �Ζ��J�n����
	 * @param aEndTime �Ζ��I������
	 * @param expecteds �Ζ�����
	 */
	private void assertEquals(String aStartTime, String aEndTime, String expected) {

		try {
			Assert.assertEquals(expected, KadaiUtill.calcWorkTime(aStartTime, aEndTime));
		} catch (KadaiException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * レベル１の正常系テスト
	 *
	 * @param anInputFilePath
	 * @param anOutputFilePath
	 */
	private void assertEqualsForLevel1(String anInputFilePath, String anOutputFilePath) {

		try {
			Kadai.parseWorkTimeData(anInputFilePath, anOutputFilePath);
		} catch (KadaiException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * レベル２の正常系テスト
	 * @param anInputFilePath
	 * @param anOutputFolderPath
	 */
	private void assertEqualsForLevel2(String anInputFilePath, String anOutputFolderPath) {

		try {
			Kadai.parseWorkTimeDataLv2(anInputFilePath, anOutputFolderPath);
		} catch (KadaiException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 勤怠のテスト
	 * @param aStartTime �Ζ��J�n����
	 * @param aEndTime �Ζ��I������
	 * @param expecteds throw�����G���[�R�[�h
	 */
	private void assertFail(String aStartTime, String aEndTime, ErrorCode expected) {

		try {
			KadaiUtill.calcWorkTime(aStartTime, aEndTime);
			Assert.fail();
		} catch (KadaiException e) {
			Assert.assertEquals(expected, e.getErrorCode());
		}
	}

	/**
	 * レベル１のファイル出力以外のエラーを出すパターン
	 * @param aStartTime
	 * @param aEndTime
	 * @param expecteds throw
	 */
	private void assertFailLevel1(String anInputFilePath, String anOutputFolderPath, ErrorCode expected) {

		try {
			Kadai.parseWorkTimeData(anInputFilePath, anOutputFolderPath);
			Assert.fail();
		} catch (KadaiException e) {
			Assert.assertEquals(expected, e.getErrorCode());
		}
	}

}
