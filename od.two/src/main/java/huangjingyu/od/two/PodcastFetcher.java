package huangjingyu.od.two;

import java.io.File;
import java.util.List;

public interface PodcastFetcher {
	public static final String SOURCE_URL = "http://www.npr.org/rss/podcast/podcast_directory.php?type=title&value=all";

	public void fetchAll(ResultWriter writer) throws Exception;

	public void fetchAll(File outputFile, FileType fileType) throws Exception;

	public List<Podcast> fetchAll() throws Exception;

	public String fetchRssUrl(String podcastDetailPageUrl) throws Exception;
}
