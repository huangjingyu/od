package huangjingyu.od.one;

import java.util.List;

public interface PageResponseParser {
	List<Restaurant> parse(String responseText, String addressSuffix);
}
