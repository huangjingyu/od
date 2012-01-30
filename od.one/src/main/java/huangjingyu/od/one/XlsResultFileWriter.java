package huangjingyu.od.one;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class XlsResultFileWriter extends AbstractResultFileWriter {
	protected Workbook wb;
	protected Sheet sheet;
	protected File outputFile;

	public XlsResultFileWriter(File file) {
		outputFile = file;
		wb = new HSSFWorkbook();
		sheet = wb.createSheet("Sheet1");
	}

	@Override
	public void close() throws IOException {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(outputFile);
			wb.write(out);
		} finally {
			IOUtils.closeQuietly(out);
		}
	}

	@Override
	protected void writeHeader(String[] header) {
		Row row = sheet.createRow(0);
		for (int i = 0; i < header.length; i++) {
			Cell cell = row.createCell(i);
			cell.setCellValue(header[i]);
		}
	}

	@Override
	protected void append(String[] row) {
		int rowNum = sheet.getLastRowNum() + 1;
		Row r = sheet.createRow(rowNum);
		for (int i = 0; i < row.length; i++) {
			Cell cell = r.createCell(i);
			cell.setCellValue(row[i]);
		}
	}
}
