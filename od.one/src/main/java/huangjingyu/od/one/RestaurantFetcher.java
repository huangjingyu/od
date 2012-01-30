package huangjingyu.od.one;

import java.io.File;
import java.util.List;

public interface RestaurantFetcher {
	Context getContext(Borough borough);

	void fetchAll(Borough borough, File resultFile, FileType fileType);

	void fetchAll(Borough borough, ResultFileWriter writer);

	List<Restaurant> fetch(Borough borough, Context ctx, int page, int numPerPage);

	void fetch(Borough borough, Context ctx, int startPage, int endPage, int numPerPage, File resultFile,
			FileType fileType);

	void fetch(Borough borough, Context ctx, int startPage, int endPage, int numPerPage, ResultFileWriter writer);

	void setPageResponseParser(PageResponseParser parser);
}
