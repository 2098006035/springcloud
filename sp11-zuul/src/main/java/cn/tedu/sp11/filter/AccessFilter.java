package cn.tedu.sp11.filter;

import cn.tedu.web.util.JsonResult;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class AccessFilter extends ZuulFilter {
    /*
    过滤器类型： pre, post, routing, error
     */
    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    /*
    顺序号
    在默认过滤器中，第5个过滤器在上下文对象中添加了 service-id，
    在第5个过滤器之后，层能从上下文对象访问到 service-id
     */
    @Override
    public int filterOrder() {
        return 6;
    }

    /*
    对当前请求，是否要进行过滤，
    如果这个方法返回true，会执行下面的过滤方法run()，
    反之，跳过过滤代码，继续执行后面的流程
     */
    @Override
    public boolean shouldFilter() {
        // 判断用户调用的是否是商品服务
        // 如果是商品服务进行过滤，其他服务不过滤

        //当前请求的上下文对象
        RequestContext ctx = RequestContext.getCurrentContext();

        //从上下文对象获得客户端调用的 service id
        String serviceId = (String) ctx.get(FilterConstants.SERVICE_ID_KEY);

        return "item-service".equals(serviceId);
    }

    /*
    过滤代码
    它的返回值，在当前zuul版本中，没有启用，返回任何数据都无效
     */
    @Override
    public Object run() throws ZuulException {
        // http://localhost:3001/item-service/45yt4t3?token=45yt443
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();

        String token = request.getParameter("token");
        if (StringUtils.isBlank(token)) {
            // 没有token，阻止这次调用继续执行
            ctx.setSendZuulResponse(false);
            // 在这里，直接向客户端发送响应 JsonResult: {code:400, msg:not log in, data:null}
            ctx.setResponseStatusCode(JsonResult.NOT_LOGIN);
            ctx.setResponseBody(
                    JsonResult.err()
                            .code(JsonResult.NOT_LOGIN)
                            .msg("not log in")
                            .toString());
        }
        return null;
    }
}
