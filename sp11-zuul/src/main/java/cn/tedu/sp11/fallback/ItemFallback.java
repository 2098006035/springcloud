package cn.tedu.sp11.fallback;

import cn.tedu.web.util.JsonResult;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Component
public class ItemFallback implements FallbackProvider {
    /*
    返回 service id
    针对哪个服务，应用当前降级类，
    如果返回"*"或null，对所有服务，都执行当前降级类
     */
    @Override
    public String getRoute() {
        return "item-service";
    }

    /*
    封装降级响应的对象
     */
    @Override
    public ClientHttpResponse fallbackResponse(String route, Throwable cause) {
        return new ClientHttpResponse() {
            @Override
            public HttpStatus getStatusCode() throws IOException {
                return HttpStatus.OK;//封装状态码和状态文本
            }

            @Override
            public int getRawStatusCode() throws IOException {
                return HttpStatus.OK.value();//取出状态码
            }

            @Override
            public String getStatusText() throws IOException {
                return HttpStatus.OK.getReasonPhrase();//取出状态文本
            }

            @Override
            public void close() {
            }

            //设置响应的协议体数据
            @Override
            public InputStream getBody() throws IOException {
                // JsonResult:  {code:200, msg:"调用远程服务失败",data:null}
                String json = JsonResult.err().msg("调用商品服务失败").toString();
                //json封装到流
                ByteArrayInputStream in = new ByteArrayInputStream(json.getBytes("UTF-8"));
                return in;
            }
            //设置响应的协议体数据
            @Override
            public HttpHeaders getHeaders() {
                //在协议体中添加内容类型属性，设置成 json 类型
                // Content-Type: application/json
                HttpHeaders h = new HttpHeaders();
                h.setContentType(MediaType.APPLICATION_JSON);
                return h;
            }
        };
    }
}
