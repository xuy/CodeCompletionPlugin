package cn.yyx.contentassist.codeutils;

import java.util.Stack;

import cn.yyx.contentassist.codesynthesis.CSNode;
import cn.yyx.contentassist.codesynthesis.CSNodeHelper;
import cn.yyx.contentassist.codesynthesis.CodeSynthesisHelper;
import cn.yyx.contentassist.codesynthesis.CodeSynthesisQueue;
import cn.yyx.contentassist.commonutils.AdditionalInfo;
import cn.yyx.contentassist.commonutils.CSNodeType;
import cn.yyx.contentassist.commonutils.SynthesisHandler;
import cn.yyx.contentassist.commonutils.TypeCheck;

public class superClassMemberInvoke extends classInvoke{
	
	referedExpression rexp = null;
	
	public superClassMemberInvoke(referedExpression rexp) {
		this.rexp = rexp;
	}

	@Override
	public boolean HandleCodeSynthesis(CodeSynthesisQueue squeue, Stack<TypeCheck> expected, SynthesisHandler handler,
			CSNode result, AdditionalInfo ai) {
		String mcode = ai.getMethodName();
		CSNode recs = new CSNode(CSNodeType.HalfFullExpression);
		if (rexp != null)
		{
			boolean conflict = rexp.HandleCodeSynthesis(squeue, expected, handler, recs, ai);
			if (conflict)
			{
				return true;
			}
		}
		CSNode spec = new CSNode(CSNodeType.HalfFullExpression);
		boolean conflict = CodeSynthesisHelper.HandleMethodSpecificationInfer(squeue, expected, handler, spec, ai, mcode);
		if (conflict)
		{
			return true;
		}
		result.SetCSNodeContent(CSNodeHelper.ConcatTwoNodes(recs, spec, ".super.", -1));
		// result.AddOneData(mcode, null);
		return false;
	}

	@Override
	public boolean CouldThoughtSame(OneCode t) {
		if (t instanceof superClassMemberInvoke)
		{
			return true;
		}
		return false;
	}

	@Override
	public double Similarity(OneCode t) {
		if (t instanceof superClassMemberInvoke)
		{
			return 1;
		}
		return 0;
	}
	
}