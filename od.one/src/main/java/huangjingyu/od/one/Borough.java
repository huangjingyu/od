package huangjingyu.od.one;

public enum Borough {
	THEBRONX(2, "BRONX"), BROOKLYN(3, "BROOKLYN"), MANHATTAN(1, "MANHATTAN"), QUEENS(4, "QUEENS"), STATENISLAND(5,
			"STATEN ISLAND");

	private int code;
	private String addressSuffix;

	Borough(int code, String addressSuffix) {
		this.code = code;
		this.addressSuffix = addressSuffix;
	}

	public int getCode() {
		return code;
	}

	public String getAddressSuffix() {
		return addressSuffix;
	}
}
