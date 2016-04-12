package cn.yyx.contentassist.codeutils;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import cn.yyx.contentassist.codepredict.CodeSynthesisException;
import cn.yyx.contentassist.codesynthesis.CSArgTypeStatementHandler;
import cn.yyx.contentassist.codesynthesis.CSFlowLineHelper;
import cn.yyx.contentassist.codesynthesis.CSFlowLineQueue;
import cn.yyx.contentassist.codesynthesis.CSStatementHandler;
import cn.yyx.contentassist.codesynthesis.data.CSFlowLineData;
import cn.yyx.contentassist.codesynthesis.flowline.FlowLineNode;
import cn.yyx.contentassist.codesynthesis.typeutil.TypeComputationKind;

public class argTypeList implements OneCode {
	
	private List<argType> tps = new LinkedList<argType>();
	
	public argTypeList() {
	}
	
	public void AddToFirst(argType re)
	{
		tps.add(0, re);
	}
	
	@Override
	public boolean CouldThoughtSame(OneCode t) {
		if (t instanceof argTypeList)
		{
			return true;
		}
		return false;
	}

	@Override
	public double Similarity(OneCode t) {
		if (t instanceof argTypeList)
		{
			return 1;
		}
		return 0;
	}

	@Override
	public List<FlowLineNode<CSFlowLineData>> HandleCodeSynthesis(CSFlowLineQueue squeue, CSStatementHandler smthandler)
			throws CodeSynthesisException {
		CSArgTypeStatementHandler csats = new CSArgTypeStatementHandler('A', smthandler);
		Iterator<argType> itr = tps.iterator();
		argType tp = itr.next();
		List<FlowLineNode<CSFlowLineData>> tpls = tp.HandleCodeSynthesis(squeue, csats);
		while (itr.hasNext())
		{
			argType ntp = itr.next();
			List<FlowLineNode<CSFlowLineData>> ntpls = ntp.HandleCodeSynthesis(squeue, csats);
			tpls = CSFlowLineHelper.ConcateTwoFlowLineNodeList(null, tpls, ",", ntpls, null, TypeComputationKind.NoOptr, squeue, csats, null);
		}
		return tpls;
	}
	
}