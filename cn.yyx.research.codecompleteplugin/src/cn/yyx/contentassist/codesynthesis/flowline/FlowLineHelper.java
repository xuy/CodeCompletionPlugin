package cn.yyx.contentassist.codesynthesis.flowline;

import java.util.LinkedList;
import java.util.List;

import cn.yyx.contentassist.codepredict.Sentence;
import cn.yyx.contentassist.codesynthesis.data.CSFlowLineData;
import cn.yyx.contentassist.codeutils.statement;
import cn.yyx.contentassist.commonutils.CheckUtil;

public class FlowLineHelper {
	
	public static List<Sentence> LastNeededSentenceQueue(int needsize, FlowLineNode<?> fln, PreTryFlowLineNode<Sentence> pretrylast) {
		List<Sentence> result = new LinkedList<Sentence>();
		int size = LastNeededSentenceQueueWithResultNeeded(needsize, fln, result);
		if (size > 0 && pretrylast != null)
		{
			LastNeededSentenceQueueWithResultNeeded(size, pretrylast, result);
		}
		return result;
	}
	
	public static int LastNeededSentenceQueueWithResultNeeded(int needsize, FlowLineNode<?> fln, List<Sentence> result)
	{
		FlowLineNode<?> tempfln = fln;
		while (tempfln != null && needsize > 0) {
			needsize--;
			Sentence sete = null;
			Object obj = tempfln.getData();
			if (obj instanceof Sentence)
			{
				sete = (Sentence) obj;
			} else {
				if (obj instanceof CSFlowLineData)
				{
					sete = ((CSFlowLineData) obj).getSete();
				} else {
					CheckUtil.ErrorAndStop("What the fuck? the element of FlowLineNode is not Sentence or CSFlowLineData?");
				}
			}
			result.add(0, sete);
			tempfln = tempfln.getPrev();
		}
		return needsize;
	}
	
	public static List<statement> LastToFirstStatementQueue(FlowLineNode<Sentence> fln) {
		List<statement> result = new LinkedList<statement>();
		FlowLineNode<Sentence> tempfln = fln;
		while (tempfln != null) {
			result.add(0, tempfln.getData().getSmt());
			tempfln = tempfln.getPrev();
		}
		return result;
	}
	
	public static List<statement> LastToFirstStatementQueueWithAddedStatement(FlowLineNode<Sentence> fln, statement smt) {
		List<statement> result = LastToFirstStatementQueue(fln);
		result.add(smt);
		return result;
	}
	
	// very simple cache. LastNeededSentenceQueue method used only.
	private static List<Sentence> setelistref = null;
	private static Sentence seteref = null;
	
	public static List<Sentence> LastNeededSentenceQueue(FlowLineNode<CSFlowLineData> tail, CodeSynthesisFlowLines csfl,
			int needsize) {
		CSFlowLineData data = tail.getData();
		if (seteref != null && seteref == data.getSete())
		{
			assert setelistref != null;
			return setelistref;
		}
		List<Sentence> result = new LinkedList<Sentence>();
		FlowLineNode<CSFlowLineData> tmp = tail;
		while (needsize > 0 && tmp != null)
		{
			result.add(0, tmp.getData().getSete());
			tmp = tmp.getPrev();
			needsize--;
		}
		if (needsize > 0)
		{
			String id = tmp.getData().getId();
			FlowLineNode<Sentence> cnct = csfl.GetConnect(id);
			List<Sentence> tmpres = LastNeededSentenceQueue(needsize, cnct, null);
			result.addAll(0, tmpres);
		}
		return result;
	}
	
}