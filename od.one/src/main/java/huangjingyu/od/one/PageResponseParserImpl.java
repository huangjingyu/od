package huangjingyu.od.one;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.script.ScriptEngine;

import org.apache.commons.lang.StringUtils;

public class PageResponseParserImpl implements PageResponseParser {

	@Override
	public List<Restaurant> parse(String responseText, String addressSuffix) {
		try {
			int index = responseText.indexOf("dwr.engine");
			if (index > -1) {
				responseText = responseText.substring(0, index);
			}
			List<Restaurant> list = new ArrayList<Restaurant>();
			ScriptEngine engine = Utils.getJavascriptEngine();
			engine.eval(responseText);
			for (int i = 0; i < 20; i++) {
				Object o = engine.get("s" + i);
				if (o == null && i < 19) {
					System.out.println("warn: s" + i + " is null, last page?");
				}
				if (o == null)
					break;
				Map<String, Object> map = Utils.jsObj2Map(o);

				Restaurant rest = new Restaurant();
				String restBuilding = removeExtraSpace((String) map.get("restBuilding"));
				String stName = removeExtraSpace((String) map.get("stName"));
				String restZipCode = removeExtraSpace((String) map.get("restZipCode"));
				String address = String.format("%s %s %s, %s", restBuilding, stName, addressSuffix, restZipCode);
				rest.setAddress(address);
				rest.setCuisine(removeExtraSpace((String) map.get("cuisineType")));
				rest.setGrade(removeExtraSpace((String) map.get("restCurrentGrade")));
				rest.setName(removeExtraSpace((String) map.get("restaurantName")));
				rest.setRecentInspectionDate(removeExtraSpace((String) map.get("lastInspectedDate")));
				rest.setTelephone(formatTele(removeExtraSpace((String) map.get("telephone"))));
				list.add(rest);
			}
			return list;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static final Pattern PAT = Pattern.compile("\\s+");

	protected String removeExtraSpace(String s) {
		s = StringUtils.trim(s);
		s = PAT.matcher(s).replaceAll(" ");
		return s;
	}

	protected String formatTele(String s) {
		if (StringUtils.isEmpty(s))
			return s;
		if (s.length() > 6) {
			return new StringBuilder().append(s.substring(0, 3)).append("-").append(s.substring(3, 6)).append("-")
					.append(s.substring(6)).toString();
		}
		return s;
	}

}
