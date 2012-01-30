package huangjingyu.od.one;

import static org.testng.Assert.assertEquals;

import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class PageResponseParserImplTest {
	PageResponseParserImpl t = new PageResponseParserImpl();
	private String script;

	@BeforeMethod
	public void beforeMethod() {
		script = "var s0={};var s1={};s0.brghCode=\"3\";s0.cuisineType=\"Juice, Smoothies, Fruit Salads\";"
				+ "s0.lastInspectedDate=\"11/30/2011\";s0.lastInspectionDate=null;s0.restBuilding=\"482\";"
				+ "s0.restCamis=\"41063464\";s0.restCurrentGrade=\"A\";s0.restGoldenApple=null;s0.restProgram=\"FS\";"
				+ "s0.restViolationGroup=\"1\";s0.restZipCode=\"11203\";s0.restaurantName=\"3 STAR JUICE CENTER\";"
				+ "s0.restaurantSearchName=\"3 STAR JUICE CENTER\";s0.scoreViolations=11;s0.stName=\"UTICA AVENUE\";"
				+ "s0.telephone=\"7184930099\";\n"
				+ "s1.brghCode=\"3\";s1.cuisineType=\"American \";s1.lastInspectedDate=\"04/07/2011\";"
				+ "s1.lastInspectionDate=null;s1.restBuilding=\"31\";s1.restCamis=\"40814203\";"
				+ "s1.restCurrentGrade=\"A\";s1.restGoldenApple=null;s1.restProgram=\"FS\";s1.restViolationGroup=\"1\";"
				+ "s1.restZipCode=\"11217\";s1.restaurantName=\"31 ROCKWELLS\";s1.restaurantSearchName=\"31 ROCKWELL\\'S\";"
				+ "s1.scoreViolations=10;s1.stName=\"ROCKWELL PLACE\";s1.telephone=\"7184889879\";";
	}

	@Test
	public void testParse() {
		List<Restaurant> list = t.parse(script, "suffix");
		assertEquals(list.size(), 2);

		Restaurant rest = list.get(1);
		assertEquals(rest.getAddress(), "31 ROCKWELL PLACE suffix, 11217");
		assertEquals(rest.getCuisine(), "American");
		assertEquals(rest.getGrade(), "A");
		assertEquals(rest.getName(), "31 ROCKWELLS");
		assertEquals(rest.getRecentInspectionDate(), "04/07/2011");
		assertEquals(rest.getTelephone(), "718-488-9879");
	}

	@Test
	public void testRemoveExtraSpace() {
		String s = "a  b	c \n d";
		s = t.removeExtraSpace(s);
		assertEquals(s, "a b c d");
	}
}
