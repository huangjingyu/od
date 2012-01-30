package huangjingyu.od.one;

import java.io.File;
import java.util.List;

import org.apache.commons.io.IOUtils;

public abstract class AbstractRestaurantFetcher implements RestaurantFetcher {
	protected PageResponseParser parser;

	@Override
	public void fetchAll(Borough borough, File resultFile, FileType fileType) {
		if (resultFile.exists()) {
			resultFile.delete();
		}
		fetchAll(borough, ResultFileWriterFactory.getResultFileWriter(resultFile, fileType));
	}

	@Override
	public void fetchAll(Borough borough, ResultFileWriter writer) {
		Context ctx = getContext(borough);
		int pageNum = Utils.getPageNumber(ctx.getTotalCount(), Constant.NUM_PER_PAGE);
		fetch(borough, ctx, 1, pageNum, Constant.NUM_PER_PAGE, writer);
	}

	@Override
	public void fetch(Borough borough, Context ctx, int startPage, int endPage, int numPerPage, File resultFile,
			FileType fileType) {
		fetch(borough, ctx, startPage, endPage, numPerPage,
				ResultFileWriterFactory.getResultFileWriter(resultFile, fileType));

	}

	@Override
	public void fetch(Borough borough, Context ctx, int startPage, int endPage, int numPerPage, ResultFileWriter writer) {
		try {
			writer.writeHeader();
			for (int i = startPage; i <= endPage; i++) {
				List<Restaurant> list = fetch(borough, ctx, i, numPerPage);
				for (Restaurant r : list) {
					writer.append(r);
				}
				System.out.println("fetched page " + i);
			}
		} finally {			
			IOUtils.closeQuietly(writer);
		}
	}

	@Override
	public void setPageResponseParser(PageResponseParser parser) {
		this.parser = parser;
	}

}
