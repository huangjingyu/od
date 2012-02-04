package huangjingyu.od.two;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.Tag;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.InputTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.Span;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.tags.TableRow;
import org.htmlparser.tags.TableTag;
import org.htmlparser.util.NodeList;

public class PodcastFetcherImpl implements PodcastFetcher {

	private RssFetcher rssFetcher = new RssFetcherImpl();

	@Override
	public void fetchAll(final ResultWriter writer) throws Exception {
		try {
			writer.writeHeader();
			fetchAll(new RowProcessor() {
				@Override
				public void process(TableRow row) throws Exception {
					Podcast podcast = getPodcast(row);
					writer.append(podcast);
				}
			});
		} finally {
			writer.close();
		}
	}

	@SuppressWarnings("serial")
	protected TableTag getPodcastTable() throws Exception {
		Parser parser = new Parser(PodcastFetcher.SOURCE_URL);
		NodeList list = parser.parse(new NodeFilter() {
			@Override
			public boolean accept(Node node) {
				if (!(node instanceof TableTag))
					return false;
				TableTag table = (TableTag) node;
				String tableCssClass = table.getAttribute("class");
				if ("titleTbl".equals(tableCssClass))
					return true;
				return false;
			}
		});
		if (list == null || list.size() == 0)
			return null;
		return (TableTag) list.elementAt(0);
	}

	@Override
	public void fetchAll(File outputFile, FileType fileType) throws Exception {
		// TODO
		fetchAll(new XlsResultWriter(outputFile));
	}

	@Override
	public List<Podcast> fetchAll() throws Exception {
		final List<Podcast> list = new ArrayList<Podcast>();
		fetchAll(new RowProcessor() {
			@Override
			public void process(TableRow row) throws Exception {
				Podcast podcast = getPodcast(row);
				list.add(podcast);
			}
		});
		return list;
	}

	@Override
	public String fetchRssUrl(String podcastDetailPageUrl) throws Exception {
		Parser parser = new Parser(podcastDetailPageUrl);

		@SuppressWarnings("serial")
		NodeList list = parser.parse(new NodeFilter() {
			@Override
			public boolean accept(Node node) {
				return (node instanceof InputTag) && "podurl".equals(((InputTag) node).getAttribute("id"));
			}
		});

		InputTag input = (InputTag) list.elementAt(0);
		return input.getAttribute("value");
	}

	protected void fetchAll(RowProcessor rowProcessor) throws Exception {
		TableTag podcastTable = getPodcastTable();
		Tag tag = (Tag) podcastTable.getChild(1);
		NodeList trNodes = tag.getChildren();
		for (int i = 0; i < trNodes.size(); i++) {
			TableRow tr = (TableRow) trNodes.elementAt(i);
			rowProcessor.process(tr);
		}
	}

	protected Podcast getPodcast(TableRow row) throws Exception {
		Podcast ret = new Podcast();

		TableColumn titleTd = (TableColumn) row.getColumns()[0];
		LinkTag titleLink = (LinkTag) searchFirst(titleTd, LinkTag.class);
		Span titleSpan = (Span) searchFirst(titleLink, Span.class);
		String detailPageUrl = titleLink.getAttribute("href");
		String title = searchFirst(titleSpan, TextNode.class).getText();

		TableColumn providerTd = (TableColumn) row.getColumns()[2];
		LinkTag providerLink = (LinkTag) searchFirst(providerTd, LinkTag.class);
		String provider = searchFirst(providerLink, TextNode.class).getText();

		String rssUrl = fetchRssUrl(detailPageUrl);
		String pubDate = rssFetcher.fetchPubDate(rssUrl);

		ret.setTitle(title);
		ret.setProvider(provider);
		ret.setRssUrl(rssUrl);
		ret.setPubDate(pubDate);
		return ret;
	}

	private Node searchFirst(Node node, Class<? extends Node> clazz) {
		Node next = node.getFirstChild();
		while (next != null && !clazz.equals(next.getClass())) {
			next = next.getNextSibling();
		}
		return next;
	}

	public void setRssFetcher(RssFetcher rssFetcher) {
		this.rssFetcher = rssFetcher;
	}

	public static interface RowProcessor {
		void process(TableRow row) throws Exception;
	}
}
