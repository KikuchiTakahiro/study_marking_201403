package jp.ktsystem.studymarking.kadai201403.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.ktsystem.kadai201403.Exception.ErrorCode;
import jp.ktsystem.kadai201403.Exception.KadaiException;
import jp.ktsystem.kadai201403.t_kikuchi.Kadai;

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

	/**
	 *
	 */
	@Test
	public void test_G02T101()
	{

		assertEqualsForLevel1("C:\\workspace_study\\2014_3\\test\\Input\\testData.txt",
							"C:\\workspace_study\\2014_3\\test\\Output\test.txt");

	}

	/**
	 * レベル1
	 */
	@Test
	public void test_G02T102()
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
		while(aMonthMatcher.find()){

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
			Assert.assertEquals(expected, Kadai.calcWorkTime(aStartTime, aEndTime));
		} catch (KadaiException e) {
			throw new RuntimeException(e);
		}
	}

	private void assertEqualsForLevel1(String anInputFilePath, String anOutputFilePath) {

		try {
			 Kadai.parseWorkTimeData(anInputFilePath, anOutputFilePath);
		} catch (KadaiException e) {
			throw new RuntimeException(e);
		}
	}


	private void assertEqualsForLevel2(String anInputFilePath, String anOutputFolderPath) {

		try {
			 Kadai.parseWorkTimeDataLv2(anInputFilePath, anOutputFolderPath);
		} catch (KadaiException e) {
			throw new RuntimeException(e);
		}
	}



	/**
	 * �ُ퓮��m�F
	 * @param aStartTime �Ζ��J�n����
	 * @param aEndTime �Ζ��I������
	 * @param expecteds throw�����G���[�R�[�h
	 */
	private void assertFail(String aStartTime, String aEndTime, ErrorCode expected) {

		try {
			Kadai.calcWorkTime(aStartTime, aEndTime);
			Assert.fail();
		} catch (KadaiException e) {
			Assert.assertEquals(expected, e.getErrorCode());
		}
	}

}
