package huangjingyu.od.one;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

public class RestaurantFetcherImplTest {
	RestaurantFetcherImpl t = new RestaurantFetcherImpl();

	@Test
	public void testGetContext() {
		Context ctx = t.getContext(Borough.BROOKLYN);
		assertNotNull(ctx);
		assertNotNull(ctx.getXsrFPreventionTokenParameter());
		assertTrue(ctx.getScriptSessionId() > 0);
		assertTrue(ctx.getTotalCount() > 0);
	}
}
