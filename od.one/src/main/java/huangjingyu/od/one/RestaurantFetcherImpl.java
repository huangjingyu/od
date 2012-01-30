package huangjingyu.od.one;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Random;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.io.IOUtils;

public class RestaurantFetcherImpl extends AbstractRestaurantFetcher {
	@Override
	public Context getContext(Borough borough) {
		try {
			Context ctx = new Context();
			ctx.setScriptSessionId(new Random().nextInt(998) + 1);
			String xsrToken = getXSRFPreventionTokenParameter();
			if (xsrToken == null) {
				throw new Exception(
						"can't get XSRFPreventionTokenParameter, maybe the website is re-designed, if so, we need to rewrite the code");
			}
			ctx.setXsrFPreventionTokenParameter(xsrToken);
			int totalCount = getTotalCount(borough, ctx);
			ctx.setTotalCount(totalCount);
			System.out.println(ctx); //TODO
			return ctx;
		} catch (Exception e) {
			throw new RuntimeException("unexpected error happens, try again if caused by network error", e);
		}
	}

	protected String getXSRFPreventionTokenParameter() throws Exception {
		HttpClient hc = new HttpClient();
		GetMethod get = new GetMethod(Constant.BROWSE_URL);
		int status = hc.executeMethod(get);
		if (status != 200) {
			throw new Exception("unexpected http response: " + status);
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(get.getResponseBodyAsStream(),
				get.getResponseCharSet()));
		String line = null;
		try {
			String lookingFor = Constant.XSR_TOKEN_PARAM + "=";
			while ((line = br.readLine()) != null) {
				int index = line.indexOf(lookingFor);
				if (index > -1) {
					String s = line.substring(index + lookingFor.length()); //"48333";
					s = s.trim();
					s = s.substring(1); // 48333";
					s = s.substring(0, s.indexOf("\"")); // 48333
					return s;
				}
			}
			return null;
		} finally {
			IOUtils.closeQuietly(br);
			get.releaseConnection();
		}
	}

	protected int getTotalCount(Borough borough, Context ctx) throws Exception {
		HttpClient hc = new HttpClient();
		PostMethod post = new PostMethod(Constant.TOTAL_COUNT_URL);
		try {
			post.addParameter("callCount", "1");
			post.addParameter("page", Constant.BROWSE_RELATIVE_URL);
			post.addParameter("httpSessionId", "");
			post.addParameter("scriptSessionId", "${scriptSessionId}" + ctx.getScriptSessionId());
			post.addParameter("c0-scriptName", "RestaurantSpringService");
			post.addParameter("c0-methodName", "getTotalCountCriteria");
			post.addParameter("c0-id", "0");
			post.addParameter("c0-param0", "string:boroughCode :_" + borough.getCode());
			post.addParameter("batchId", "1");
			int status = hc.executeMethod(post);
			if (status != 200) {
				throw new Exception("unexpected status code when get total count, status code is " + status);
			}
			String s = post.getResponseBodyAsString(); //dwr.engine._remoteHandleCallback('1','0',5745);
			s = s.substring(s.lastIndexOf(",") + 1); // 5745);
			s = s.substring(0, s.indexOf(")"));
			return Integer.parseInt(s);
		} finally {
			post.releaseConnection();
		}
	}

	@Override
	public List<Restaurant> fetch(Borough borough, Context ctx, int page, int numPerPage) {
		HttpClient hc = new HttpClient();
		PostMethod post = new PostMethod(Constant.RESULT_URL);
		try {
			post.addParameter("callCount", "1");
			post.addParameter("page", Constant.RESULT_RELATIVE_URL);
			post.addParameter("httpSessionId", "");
			post.addParameter("scriptSessionId", "${scriptSessionId}" + ctx.getScriptSessionId());
			post.addParameter("c0-scriptName", "RestaurantSpringService");
			post.addParameter("c0-methodName", "getResultsSrchCriteria");
			post.addParameter("c0-id", "0");
			post.addParameter("c0-param0", "string:boroughCode :_" + borough.getCode());
			post.addParameter("c0-param1", "string:displayOrder");
			post.addParameter("c0-param2", "boolean:true");
			post.addParameter("c0-param3", "number:" + page);
			post.addParameter("c0-param4", "number:" + numPerPage);
			post.addParameter("batchId", String.valueOf(ctx.getBatchId()));
			int status = hc.executeMethod(post);
			if (status != 200) {
				throw new Exception("unexpected status code when get page result, status code is " + status);
			}
			String responseText = post.getResponseBodyAsString();
			if (parser == null)
				parser = new PageResponseParserImpl();
			return parser.parse(responseText, borough.getAddressSuffix());
		} catch (Exception e) {
			throw new RuntimeException("unexpected error happens, try again if caused by network error", e);
		} finally {
			post.releaseConnection();
		}
	}
}
