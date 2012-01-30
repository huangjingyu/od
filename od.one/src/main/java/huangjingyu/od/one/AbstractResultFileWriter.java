package huangjingyu.od.one;

public abstract class AbstractResultFileWriter implements ResultFileWriter {
	public static final String[] HEADER = new String[] { "Restaurant Name", "Address", "Telephone", "Cuisine",
			"Most Recent Graded Inspection Date", "Grade" };

	@Override
	public void writeHeader() {
		writeHeader(HEADER);
	}

	@Override
	public void append(Restaurant restaurant) {
		String[] row = new String[HEADER.length];
		row[0] = restaurant.getName();
		row[1] = restaurant.getAddress();
		row[2] = restaurant.getTelephone();
		row[3] = restaurant.getCuisine();
		row[4] = restaurant.getRecentInspectionDate();
		row[5] = restaurant.getGrade();
		append(row);
	}

	protected abstract void writeHeader(String[] header);

	protected abstract void append(String[] row);

}
