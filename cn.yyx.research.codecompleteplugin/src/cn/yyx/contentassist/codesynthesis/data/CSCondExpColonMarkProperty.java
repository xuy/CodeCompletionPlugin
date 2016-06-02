package cn.yyx.contentassist.codesynthesis.data;

import java.util.Stack;

import cn.yyx.contentassist.codepredict.CodeSynthesisException;

public class CSCondExpColonMarkProperty extends CSExtraProperty {
	
	public CSCondExpColonMarkProperty(CSExtraProperty csepnext) {
		super(csepnext);
	}

	@Override
	public void HandleStackSignalDetail(Stack<Integer> signals) throws CodeSynthesisException {
		signals.push(DataStructureSignalMetaInfo.ConditionExpressionColon);
	}
	
}