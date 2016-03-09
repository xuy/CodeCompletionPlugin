package cn.yyx.contentassist.codeutils;

import java.util.Stack;

public class assignmentStatement extends expressionStatement{
	referedExpression left = null;
	String optr = null;
	referedExpression right = null;
	
	public assignmentStatement(referedExpression left, String optr, referedExpression right) {
		this.left = left;
		this.optr = optr;
		this.right = right;
	}
	
	@Override
	public boolean CouldThoughtSame(OneCode t) {
		if (t instanceof assignmentStatement)
		{
			if (optr.equals(((assignmentStatement) t).optr))
			{
				return true;
			}
		}
		return false;
	}
	
	@Override
	public double Similarity(OneCode t) {
		if (t instanceof assignmentStatement)
		{
			return 0.3*(left.Similarity(((assignmentStatement) t).left)) + 0.4*(optr.equals(((assignmentStatement) t).optr) ? 1 : 0) + 0.3*(right.Similarity(((assignmentStatement) t).right));
		}
		return 0;
	}
	
	@Override
	public void HandleOverSignal(Stack<Integer> cstack) {
	}
	
}