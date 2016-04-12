package cn.yyx.contentassist.codeutils;

import java.util.List;

import cn.yyx.contentassist.codepredict.CodeSynthesisException;
import cn.yyx.contentassist.codesynthesis.CSFlowLineHelper;
import cn.yyx.contentassist.codesynthesis.CSFlowLineQueue;
import cn.yyx.contentassist.codesynthesis.CSStatementHandler;
import cn.yyx.contentassist.codesynthesis.data.CSFlowLineData;
import cn.yyx.contentassist.codesynthesis.flowline.FlowLineNode;
import cn.yyx.contentassist.codesynthesis.flowline.FlowLineStack;
import cn.yyx.contentassist.codesynthesis.typeutil.TypeComputationKind;

public class enhancedForStatement extends statement{
	
	type tp = null;
	referedExpression rexp = null;
	
	public enhancedForStatement(type rt, referedExpression rexp) {
		this.tp = rt;
		this.rexp = rexp;
	}

	@Override
	public boolean CouldThoughtSame(OneCode t) {
		if (t instanceof enhancedForStatement)
		{
			return true;
		}
		return false;
	}

	@Override
	public double Similarity(OneCode t) {
		if (t instanceof enhancedForStatement)
		{
			return 0.4 + 0.6*(0.6*tp.Similarity(((enhancedForStatement) t).tp) + 0.4*(rexp.Similarity(((enhancedForStatement) t).rexp)));
		}
		return 0;
	}
	
	/*@Override
	public boolean HandleCodeSynthesis(CodeSynthesisQueue squeue, Stack<TypeCheck> expected, SynthesisHandler handler,
			CSNode result, AdditionalInfo ai) {
		CSNode tpcs = new CSNode(CSNodeType.HalfFullExpression);
		tp.HandleCodeSynthesis(squeue, expected, handler, tpcs, null);
		tpcs.setMaytypereplacer(true);
		tpcs.setPrefix("for (");
		tpcs.setPostfix(" et : ");
		CSNode rexpcs = new CSNode(CSNodeType.HalfFullExpression);
		rexp.HandleCodeSynthesis(squeue, expected, handler, rexpcs, null);
		rexpcs.setPrefix("");
		rexpcs.setPostfix(") {\n}");
		return false;
	}*/

	@Override
	public List<FlowLineNode<CSFlowLineData>> HandleCodeSynthesis(CSFlowLineQueue squeue, CSStatementHandler smthandler)
			throws CodeSynthesisException {
		List<FlowLineNode<CSFlowLineData>> tpls = tp.HandleCodeSynthesis(squeue, smthandler);
		List<FlowLineNode<CSFlowLineData>> rels = rexp.HandleCodeSynthesis(squeue, smthandler);
		return CSFlowLineHelper.ConcateTwoFlowLineNodeList("for (", tpls, " et: ", rels, "){\n\n}", TypeComputationKind.NoOptr, squeue, smthandler, null);
	}

	@Override
	public boolean HandleOverSignal(FlowLineStack cstack) throws CodeSynthesisException {
		cstack.EnsureAllSignalNull();
		return true;
	}
	
}