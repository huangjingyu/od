package huangjingyu.od.one;

import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;

public class Utils {
	private static ScriptEngineManager scriptFactory = new ScriptEngineManager();

	public static int getPageNumber(int totalCount, int numPerPage) {
		int ret = totalCount / numPerPage;
		int remainder = totalCount % numPerPage;
		return remainder > 0 ? ret + 1 : ret;
	}

	public static ScriptEngine getJavascriptEngine() {
		return scriptFactory.getEngineByName("JavaScript");
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> jsObj2Map(Object o) throws Exception {
		ScriptEngine engine = getJavascriptEngine();
		engine.put("foo", o);
		String script = "var map = new java.util.HashMap();for(var key in foo) {map.put(key, foo[key]);}";
		engine.eval(script);
		return (Map<String, Object>) engine.get("map");
	}

	@SuppressWarnings("static-access")
	public static Options buildOptions() {
		Options options = new Options();
		options.addOption(OptionBuilder
				.withLongOpt("borough")
				.withDescription(
						"the selected borough, accepted values are(case insensitive): TheBronx, Brooklyn, Manhattan, Queens, StatenIsland.")
				.hasArg().withArgName("BOROUGH").isRequired(true).create("b"));
		options.addOption(OptionBuilder
				.withLongOpt("output")
				.withDescription(
						"full path of the output xls/csv file, e.g. /Users/test/brooklyn.xls, /Users/test/brooklyn.csv")
				.hasArg().withArgName("OUTPUT").isRequired(true).create("o"));
		options.addOption(OptionBuilder.withLongOpt("start").withDescription("start page number, 1 based, inclusive.")
				.hasArg().withArgName("START").isRequired(false).create("s"));
		options.addOption(OptionBuilder.withLongOpt("end").withDescription("end page number, inclusive.").hasArg()
				.withArgName("END").isRequired(false).create("e"));
		return options;
	}
}
