package cn.yyx.contentassist.codepredict;

import java.util.LinkedList;
import java.util.List;

import cn.yyx.contentassist.aerospikehandle.AeroLifeCycle;
import cn.yyx.contentassist.aerospikehandle.PredictProbPair;
import cn.yyx.contentassist.codecompletion.PredictMetaInfo;
import cn.yyx.contentassist.commonutils.ListHelper;

public class PredictHelper {
	
	public static List<PredictProbPair> PredictSentences(AeroLifeCycle alc, List<Sentence> ls, int neededSize) {
		List<PredictProbPair> result = new LinkedList<PredictProbPair>();
		int maxsize = Math.min(ls.size() - 1, PredictMetaInfo.NgramMaxSize-1);
		for (int i = maxsize; i > 0; i--) {
			String key = ListHelper.ConcatJoinLast(i, ls);
			List<PredictProbPair> predicts = alc.AeroModelPredict(key, neededSize, i);
			result.addAll(predicts);
			neededSize -= predicts.size();
			if (neededSize <= 0)
			{
				break;
			}
		}
		return result;
	}
	
}