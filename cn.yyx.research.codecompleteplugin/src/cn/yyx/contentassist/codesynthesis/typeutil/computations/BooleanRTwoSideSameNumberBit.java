package cn.yyx.contentassist.codesynthesis.typeutil.computations;

import cn.yyx.contentassist.codesynthesis.typeutil.CCType;
import cn.yyx.contentassist.codesynthesis.typeutil.InferredCCType;
import cn.yyx.contentassist.codesynthesis.typeutil.TypeComputer;
import cn.yyx.contentassist.codesynthesis.typeutil.TypeConflictException;

public class BooleanRTwoSideSameNumberBit extends TypeComputationKind {
	
	@Override
	public void HandlePre(CCType pre) throws TypeConflictException {
		this.setPre(pre);
		if (pre != null)
		{
			if (!TypeComputer.IsStrictNumberBit(pre.getCls()) && !(pre instanceof InferredCCType))
			{
				throw new TypeConflictException("left of BooleanRTwoSideSameNumberBit not number bit. is:" + pre);
			}
		}
	}
	
	@Override
	public void HandlePost(CCType post) throws TypeConflictException {
		this.setPost(post);
		if (post != null)
		{
			if (!TypeComputer.IsStrictNumberBit(post.getCls()) && !(post instanceof InferredCCType))
			{
				throw new TypeConflictException("right of inBooleanRTwoSideSameNumberBitherit not number bit. is:" + post);
			}
		}
	}
	
	@Override
	public CCType HandleResult() throws TypeConflictException {
		if (getPre() instanceof InferredCCType || getPost() instanceof InferredCCType)
		{
			return new InferredCCType();
		}
		return new CCType(boolean.class, "boolean");
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		BooleanRTwoSideSameNumberBit tcmp = new BooleanRTwoSideSameNumberBit();
		CCType postc = post == null ? null : (CCType) post.clone();
		CCType prec = pre == null ? null : (CCType) pre.clone();
		tcmp.setPost(postc);
		tcmp.setPre(prec);
		return tcmp;
	}
	
}