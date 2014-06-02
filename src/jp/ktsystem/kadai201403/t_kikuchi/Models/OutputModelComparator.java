/**
 *
 */
package jp.ktsystem.kadai201403.t_kikuchi.Models;

import java.util.Comparator;

/**
 * ファイル出力モデルのソート用クラス
 * @author TakahrioKikuchi
 *
 */
public class OutputModelComparator implements Comparator<OutputModel> {

	@Override
	public int compare(OutputModel o1, OutputModel o2) {
		int outputData1 = Integer.parseInt( o1.getOutputDate());
		int  outputData2 =Integer.parseInt( o2.getOutputDate());

		if(outputData1 > outputData2)
		{
			return 1;
		}else if(outputData1 == outputData2)
		{
			return 0;
		}else {
			return -1;
		}
	}

}
