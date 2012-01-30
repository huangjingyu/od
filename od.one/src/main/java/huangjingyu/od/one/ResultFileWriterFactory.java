package huangjingyu.od.one;

import java.io.File;

public class ResultFileWriterFactory {
	public static ResultFileWriter getResultFileWriter(File resultFile, FileType fileType) {
		if (FileType.csv.equals(fileType)) {
			return new CsvResultFileWriter(resultFile);
		} else if (FileType.xls.equals(fileType)) {
			return new XlsResultFileWriter(resultFile);
		}
		return null;
	}
}
