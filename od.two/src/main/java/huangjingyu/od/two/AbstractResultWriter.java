package huangjingyu.od.two;

public abstract class AbstractResultWriter implements ResultWriter {

	@Override
	public void writeHeader() {
		writeHeader(new String[] { "Title", "Provider", "Feed URL", "PubDate" });
	}

	@Override
	public void append(Podcast podcast) {
		String[] row = new String[] { podcast.getTitle(), podcast.getProvider(), podcast.getRssUrl(),
				podcast.getPubDate() };
		append(row);
	}

	protected abstract void writeHeader(String[] header);

	protected abstract void append(String[] row);

}
