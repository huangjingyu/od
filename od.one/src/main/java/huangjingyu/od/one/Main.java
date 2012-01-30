package huangjingyu.od.one;

import java.io.File;
import java.util.Arrays;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.lang.StringUtils;

public class Main {
	public static void main(String[] args) throws Exception {
		Options options = Utils.buildOptions();
		CommandLineParser parser = new PosixParser();
		CommandLine cmd;
		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) {
			System.err.println("error in executing with args" + Arrays.toString(args));
			e.printStackTrace();
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("java -jar crawlRest.jar", options);
			return;
		}
		Borough borough = null;
		try {
			borough = Borough.valueOf(cmd.getOptionValue("b").toUpperCase());
		} catch (Exception e) {
			System.err.println("borough value is invalid");
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("java -jar crawlRest.jar", options);
			return;
		}
		String outputPath = cmd.getOptionValue("o");
		File dest = new File(outputPath);
		FileType fileType = outputPath.toLowerCase().endsWith(".xls") ? FileType.xls : FileType.csv;
		RestaurantFetcher fetcher = new RestaurantFetcherImpl();
		if (StringUtils.isEmpty(cmd.getOptionValue("s")) && StringUtils.isEmpty(cmd.getOptionValue("e"))) {
			fetcher.fetchAll(borough, dest, fileType);
		} else {
			Context ctx = fetcher.getContext(borough);
			int startPage = 1;
			int endPage = Utils.getPageNumber(ctx.getTotalCount(), Constant.NUM_PER_PAGE);
			if (!StringUtils.isEmpty(cmd.getOptionValue("s"))) {
				try {
					startPage = Integer.parseInt(cmd.getOptionValue("s"));
				} catch (Exception e) {
					System.err.println("start page arg is invalid, should be an integer.");
					HelpFormatter formatter = new HelpFormatter();
					formatter.printHelp("java -jar crawlRest.jar", options);
					return;
				}
			}
			if (!StringUtils.isEmpty(cmd.getOptionValue("e"))) {
				try {
					endPage = Integer.parseInt(cmd.getOptionValue("e"));
				} catch (Exception e) {
					System.err.println("end page arg is invalid, should be an integer.");
					HelpFormatter formatter = new HelpFormatter();
					formatter.printHelp("java -jar crawlRest.jar", options);
					return;
				}
			}
			fetcher.fetch(borough, ctx, startPage, endPage, Constant.NUM_PER_PAGE, dest, fileType);
		}
	}
}
