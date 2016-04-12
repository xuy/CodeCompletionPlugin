package cn.yyx.contentassist.codeutils;

import java.util.LinkedList;
import java.util.List;

import cn.yyx.contentassist.codepredict.CodeSynthesisException;
import cn.yyx.contentassist.codesynthesis.CSFlowLineQueue;
import cn.yyx.contentassist.codesynthesis.CSStatementHandler;
import cn.yyx.contentassist.codesynthesis.data.CSFlowLineData;
import cn.yyx.contentassist.codesynthesis.flowline.FlowLineNode;
import cn.yyx.contentassist.codesynthesis.typeutil.TypeComputationKind;

public class stringLiteral extends literal{

	@Override
	public boolean CouldThoughtSame(OneCode t) {
		if (t instanceof stringLiteral)
		{
			return true;
		}
		return false;
	}

	@Override
	public double Similarity(OneCode t) {
		if (t instanceof stringLiteral)
		{
			return 1;
		}
		return 0;
	}

	/*@Override
	public boolean HandleCodeSynthesis(CodeSynthesisQueue squeue, Stack<TypeCheck> expected, SynthesisHandler handler,
			CSNode result, AdditionalInfo ai) {
		result.setContenttype(CSNodeType.SymbolMark);
		result.AddOneData("\"String Content\"", null);
		return false;
	}*/

	@Override
	public List<FlowLineNode<CSFlowLineData>> HandleCodeSynthesis(CSFlowLineQueue squeue, CSStatementHandler smthandler)
			throws CodeSynthesisException {
		List<FlowLineNode<CSFlowLineData>> result = new LinkedList<FlowLineNode<CSFlowLineData>>();
		result.add(new FlowLineNode<CSFlowLineData>(new CSFlowLineData(squeue.GenerateNewNodeId(), smthandler.getSete(), "\"String Content\"", null, null, false, TypeComputationKind.NoOptr, squeue.GetLastHandler()), smthandler.getProb()));
		return result;
	}

}
