package noah.memo.memogateway.gateway.filter.log;

import com.candao.irms.log.ThreadLocalBean;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import noah.memo.memoframework.bean.DistributedContext;
import noah.memo.memoframework.bean.log.LogEvent;
import noah.memo.memoframework.bean.log.LogFlag;
import noah.memo.memoframework.log.Logger;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author noah
 */
public class LogPreFilter extends ZuulFilter {

    static final Pattern localePattern = Pattern.compile("clientIp\\W+(\\d+.\\d+.\\d+.\\d+)");

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public Object run() {

        //ThreadLocal init
        DistributedContext.start();

        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest req = ctx.getRequest();
        String uri = req.getRequestURI();

        String reqData = getReqData(req);
        log(ctx, req, uri, reqData);

        return null;
    }

    /**
     * 获取请求数据
     *
     * @param req
     * @return
     */
    private String getReqData(HttpServletRequest req) {

        String reqData = "";
        String method = req.getMethod();
        DistributedContext distributedContext = DistributedContext.getContext();

        if ("post".equalsIgnoreCase(method) || "put".equalsIgnoreCase(method)) {
            StringBuffer sb = new StringBuffer();
            try {
                if (req.getContentLength() > 0) {
                    BufferedReader reader = req.getReader();
                    String valueString = null;
                    while ((valueString = reader.readLine()) != null) {
                        sb.append(valueString);
                    }
                    reqData = sb.toString();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            distributedContext.setReqDataString(reqData);

        } else {
            reqData = req.getQueryString();
            if (reqData != null) {
                URLDecoder.decode(reqData);
            }

            distributedContext.setReqDataString(req.getParameter("data"));
        }

        return reqData;
    }

    private void log(RequestContext ctx, HttpServletRequest req, String uri, String reqData) {
        try {
            LogEvent logEvent = new LogEvent();
            logEvent.time = System.currentTimeMillis();
            logEvent.clientIp = getClientIp(req, reqData);
            logEvent.flag = LogFlag.REQUEST.getFlag();
            logEvent.msg = reqData;
            logEvent.restUrl = uri.toString();
            String clientStr = getClientStr(uri);
            //todo:logEvent.clientType = clientStr;
            DistributedContext distributedContext = DistributedContext.getContext();
            distributedContext.setTime(logEvent.time);
            if (!req.getRequestURI().startsWith("/log")) {
                Logger.info(logEvent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getClientIp(HttpServletRequest req, String reqData) {
        String clientIp = "";
        try {
            Matcher matcher = localePattern.matcher(reqData);
            if (matcher.find()) {
                clientIp = matcher.group(1);
                if (StringUtils.isEmpty(clientIp)) {
                    clientIp = req.getRemoteAddr();
                }
            }
        } catch (Exception e) {
            System.out.println("getClientIp Err, reqData = " + reqData);
        }
        return clientIp;
    }

    /**
     * 根据uri获取请求来源
     *
     * @param uri
     * @return
     */
    public static String getClientStr(String uri) {
        if (uri != null) {
            String[] arr = uri.split("[/]");
            if (arr.length > 3) {
                return arr[2];
            }
        }
        return null;
    }

}