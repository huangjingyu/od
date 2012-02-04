package huangjingyu.od.two;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import org.htmlparser.Parser;
import org.htmlparser.tags.TableRow;
import org.htmlparser.tags.TableTag;
import org.htmlparser.util.NodeList;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class PodcastFetcherImplTest {
	private PodcastFetcherImpl t;

	@BeforeMethod
	public void beforeMethod() {
		t = new PodcastFetcherImpl();
	}

	@Test
	public void testGetPodcastTable() throws Exception {
		TableTag table = t.getPodcastTable();
		assertNotNull(table);
		assertEquals(table.getAttribute("class"), "titleTbl");
	}

	@Test
	public void testGetPodcast() throws Exception {
		String rowHtml = "<tr>\n"
				+ "                  <td class=\"colTitle\">\n"
				+ "  <a href=\"/templates/rss/podcast/podcast_detail.php?siteId=4857512\"><span class=\"titleLnk\">\"Poverty with a View\"</span></a>\n"
				+ "                  </td>\n"
				+ "                  <td class=\"colTopic\">News</td>\n"
				+ "                  <td class=\"colProducer\"><a target=\"_blank\" href=\"http://www.knau.org/\">KNAU</a>                  </td>\n"
				+ "  </tr>";
		Parser parser = Parser.createParser(rowHtml, "utf-8");
		NodeList list = parser.parse(null);
		TableRow row = (TableRow) list.elementAt(0);
		Podcast podcast = t.getPodcast(row);

		assertEquals(podcast.getTitle(), "\"Poverty with a View\"");
		assertEquals(podcast.getProvider(), "KNAU");
	}

	@Test
	public void testFetchRssUrl() throws Exception {
		assertEquals(t.fetchRssUrl("http://www.npr.org/rss/podcast/podcast_detail.php?siteId=4857512"),
				"http://feeds.feedburner.com/knaupodcast");
	}
}
