package huangjingyu.od.one;

public class Context {
	private int scriptSessionId;

	private String xsrFPreventionTokenParameter;

	private int totalCount;

	private int batchId = 1;

	public int getScriptSessionId() {
		return scriptSessionId;
	}

	public void setScriptSessionId(int scriptSessionId) {
		this.scriptSessionId = scriptSessionId;
	}

	public String getXsrFPreventionTokenParameter() {
		return xsrFPreventionTokenParameter;
	}

	public void setXsrFPreventionTokenParameter(String xsrFPreventionTokenParameter) {
		this.xsrFPreventionTokenParameter = xsrFPreventionTokenParameter;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getBatchId() {
		return batchId++;
	}

	public void setBatchId(int batchId) {
		this.batchId = batchId;
	}

	@Override
	public String toString() {
		return String.format("scriptSessionId=%d, XSRFPreventionTokenParameter=%s, totalCount=%d", scriptSessionId,
				xsrFPreventionTokenParameter, totalCount);
	}

}
