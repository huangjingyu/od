package huangjingyu.od.one;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

import au.com.bytecode.opencsv.CSVWriter;

public class CsvResultFileWriter extends AbstractResultFileWriter {
	protected CSVWriter cw;

	public CsvResultFileWriter(File file) {
		try {
			cw = new CSVWriter(new FileWriter(file));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void close() throws IOException {
		cw.flush();
		IOUtils.closeQuietly(cw);
	}

	@Override
	protected void writeHeader(String[] header) {
		cw.writeNext(header);
	}

	@Override
	protected void append(String[] row) {
		cw.writeNext(row);
	}

}
