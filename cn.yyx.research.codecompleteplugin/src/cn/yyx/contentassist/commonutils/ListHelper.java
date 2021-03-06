package cn.yyx.contentassist.commonutils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import cn.yyx.contentassist.codepredict.Sentence;
import cn.yyx.contentassist.codesynthesis.data.CSExtraProperty;
import cn.yyx.contentassist.codesynthesis.data.CSFlowLineData;
import cn.yyx.contentassist.codesynthesis.flowline.FlowLineNode;
import cn.yyx.contentassist.codesynthesis.flowline.PreTryFlowLineNode;
import cn.yyx.contentassist.codesynthesis.typeutil.CCType;
import cn.yyx.contentassist.codesynthesis.typeutil.TypeSameJudger;
import cn.yyx.contentassist.codesynthesis.typeutil.computations.TypeComputationKind;

public class ListHelper {
	
	public static boolean WholeKeyContains(List<PreTryFlowLineNode<Sentence>> fls, PreTryFlowLineNode<Sentence> pop)
	{
		Iterator<PreTryFlowLineNode<Sentence>> fitr = fls.iterator();
		while (fitr.hasNext())
		{
			PreTryFlowLineNode<Sentence> ptfn = fitr.next();
			if (ptfn.getWholekey().contains(pop.getWholekey()))
			{
				return true;
			}
		}
		return false;
	}
	
	public static String ConcateJoin(List<Sentence> ls, int start, int stop)
	{
		StringBuilder sb = new StringBuilder("");
		Iterator<Sentence> itr = ls.iterator();
		int i=0;
		while (itr.hasNext())
		{
			Sentence sete = itr.next();
			if (i >= start)
			{
				String sp = " ";
				if (i == stop-1)
				{
					sp = "";
				}
				sb.append(sete.getSentence() + sp);
			}
			i++;
			if (i == stop)
			{
				break;
			}
		}
		return sb.toString();
	}
	
	public static void AddExtraPropertyToAllListNodes(List<FlowLineNode<CSFlowLineData>> alls, CSExtraProperty csep) {
		if (alls == null || alls.size() == 0) {
			return;
		}
		Iterator<FlowLineNode<CSFlowLineData>> itr = alls.iterator();
		while (itr.hasNext()) {
			FlowLineNode<CSFlowLineData> fln = itr.next();
			fln.getData().setCsep(csep);
		}
	}
	

	public static void SetTcksToAllListNodes(List<FlowLineNode<CSFlowLineData>> alls, TypeComputationKind tck) {
		if (alls == null || alls.size() == 0) {
			return;
		}
		Iterator<FlowLineNode<CSFlowLineData>> itr = alls.iterator();
		while (itr.hasNext()) {
			FlowLineNode<CSFlowLineData> fln = itr.next();
			fln.getData().setTck(tck);
		}
	}

	public static void SetDclsToAllListNodes(List<FlowLineNode<CSFlowLineData>> alls, CCType dcls) {
		if (alls == null || alls.size() == 0) {
			return;
		}
		Iterator<FlowLineNode<CSFlowLineData>> itr = alls.iterator();
		while (itr.hasNext()) {
			FlowLineNode<CSFlowLineData> fln = itr.next();
			fln.getData().setDcls(dcls);
		}
	}

	public static boolean DetailContains(List<FlowLineNode<CSFlowLineData>> results, CSFlowLineData data) {
		Iterator<FlowLineNode<CSFlowLineData>> itr = results.iterator();
		while (itr.hasNext()) {
			FlowLineNode<CSFlowLineData> fln = itr.next();
			CSFlowLineData dt = fln.getData();
			String dtcnt = dt.getData();
			CCType dtcct = dt.getDcls();
			if (dtcnt.equals(data.getData()) && TypeSameJudger.TwoTypesSame(dtcct, data.getDcls())) {
				return true;
			}
		}
		return false;
	}

	public static List<Boolean> InitialBooleanArray(int size) {
		List<Boolean> result = new LinkedList<Boolean>();
		for (int i = 0; i < size; i++) {
			result.add(false);
		}
		return result;
	}

	public static void PrintList(ArrayList<String> result) {
		System.out.println("Element Begin:");
		Iterator<String> itr = result.iterator();
		while (itr.hasNext()) {
			String ele = itr.next();
			System.out.println("Element:" + ele);
		}
		System.out.println("Element End.");
	}

	public static String ConcatJoinLast(int size, List<Sentence> analysislist) {
		StringBuffer sb = new StringBuffer("");
		int end = analysislist.size() - 1;
		int start = end + 1 - size;
		Iterator<Sentence> itr = analysislist.iterator();
		for (int i = 0; i < start; i++) {
			itr.next();
		}
		for (int i = start; i <= end; i++) {
			Sentence sete = itr.next();
			String split = " ";
			if (i == end) {
				split = "";
			}
			sb.append(sete.getSentence() + split);
		}
		return sb.toString().trim();
	}

	public static TKey ConcatJoin(List<Sentence> analysislist) {
		String trim1key = null;
		String key = null;
		StringBuffer sb = new StringBuffer("");
		Iterator<Sentence> itr = analysislist.iterator();
		Sentence sete = itr.next();
		sb.append(sete.getSentence());
		int keylen = 1;
		while (itr.hasNext()) {
			sete = itr.next();
			String split = " ";
			if (!itr.hasNext()) {
				trim1key = sb.toString();
			}
			sb.append(split + sete.getSentence());
			keylen++;
		}
		key = sb.toString();
		return new TKey(key, keylen, trim1key);
	}
	
}