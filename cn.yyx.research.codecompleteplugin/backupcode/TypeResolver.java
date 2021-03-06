package cn.yyx.contentassist.codesynthesis.typeutil;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.ui.text.java.JavaContentAssistInvocationContext;

import cn.yyx.contentassist.codesynthesis.CSFlowLineQueue;
import cn.yyx.contentassist.codesynthesis.data.CSFlowLineData;
import cn.yyx.contentassist.codesynthesis.flowline.FlowLineNode;
import cn.yyx.contentassist.codesynthesis.statementhandler.CSStatementHandler;
import cn.yyx.contentassist.codeutils.type;
import cn.yyx.contentassist.commonutils.YJCache;
import cn.yyx.contentassist.parsehelper.ComplexParser;
import cn.yyx.contentassist.specification.SearchSpecificationOfAReference;
import cn.yyx.contentassist.specification.TypeMember;

public class TypeResolver {
	
	public static final int MaxTryTimes = 3;
	
	public static YJCache<LinkedList<CCType>> classcache = new YJCache<LinkedList<CCType>>();
	
	// Class<?>
	// TODO all specification must be sorted according to their own feature.
	// TODO the type string must be parsed to type, then use interface HandleCodeSynthesis such that.
	public static LinkedList<CCType> ResolveType(String type, CSFlowLineQueue squeue, CSStatementHandler smthandler)
	{
		type tp = ComplexParser.GetType(type);
		LinkedList<CCType> clss = classcache.GetCachedContent(type);
		if (clss != null)
		{
			return clss;
		}
		List<FlowLineNode<CSFlowLineData>> tpls = tp.HandleCodeSynthesis(squeue, smthandler);
		// List<TypeMember> tmlist = SearchSpecificationOfAReference.SearchTypeSpecificationByPrefix(type, javacontext, null);
		if (tmlist == null || tmlist.size() == 0)
		{
			LinkedList<CCType> res = new LinkedList<CCType>();
			CCType cct = new CCType(Object.class, "Object");
			res.add(cct);
			return res;
		}
		return CCType.CCTypeList(tmlist);
	}
	
	public static List<LinkedList<CCType>> ResolveType(List<String> types, CSFlowLineQueue squeue,
			CSStatementHandler smthandler)
	{
		List<LinkedList<CCType>> result = new LinkedList<LinkedList<CCType>>();
		Iterator<String> itr = types.iterator();
		while (itr.hasNext())
		{
			String tp = itr.next();
			result.add(ResolveType(tp, squeue, smthandler));
		}
		return result;
	}
	
}