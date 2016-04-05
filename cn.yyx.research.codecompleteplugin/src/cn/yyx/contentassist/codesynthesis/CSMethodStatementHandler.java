package cn.yyx.contentassist.codesynthesis;

import java.util.Map;
import java.util.TreeMap;

import cn.yyx.contentassist.codesynthesis.typeutil.MethodTypeSignature;

public class CSMethodStatementHandler extends CSStatementHandler{
	
	private int argsize = -1;
	private String methodname = null;
	private Map<Integer, MethodTypeSignature> mtsmap = new TreeMap<Integer, MethodTypeSignature>();
	
	public CSMethodStatementHandler(String methodname, CSStatementHandler csh) {
		super(csh.getSete(), csh.getProb());
		this.setMethodname(methodname);
	}
	
	public String getMethodname() {
		return methodname;
	}
	
	public void setMethodname(String methodname) {
		this.methodname = methodname;
	}
	
	public MethodTypeSignature GetMethodTypeSigById(Integer id)
	{
		return mtsmap.get(id);
	}
	
	public void AddMethodTypeSigById(Integer id, MethodTypeSignature mts)
	{
		mtsmap.put(id, mts);
	}

	public int getArgsize() {
		return argsize;
	}

	public void setArgsize(int argsize) {
		this.argsize = argsize;
	}
	
}