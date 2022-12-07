package utils.http;

import okhttp3.*;

import java.io.IOException;
import java.util.logging.*;

public class HttpClientUtil {

    private final static SimpleCookieManager simpleCookieManager = new SimpleCookieManager();
    public final static OkHttpClient HTTP_CLIENT =
            new OkHttpClient.Builder()
                    .cookieJar(simpleCookieManager)
                  //  .followRedirects(false)
                    .build();




    public static void set(){
        Logger.getLogger(OkHttpClient.class.getName()).setLevel(Level.FINE);
    }


    public static void runAsync(String finalUrl, Callback callback) {
        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        Call call = HttpClientUtil.HTTP_CLIENT.newCall(request);

        call.enqueue(callback);
    }

    public static Response runSync(String finalUrl) {
        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        Call call = HttpClientUtil.HTTP_CLIENT.newCall(request);
        try {
            Response response = call.execute();
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void runUploadFile(String finalUrl, RequestBody body, Callback callback) {
        Request request  = new Request.Builder()
                .url(finalUrl)
                //.method("POST", body)
                .post(body)
                .build();

        Call call = HttpClientUtil.HTTP_CLIENT.newCall(request);

        call.enqueue(callback);
    }

    public static void runRequestWithBody(String finalUrl, RequestBody body, Callback callback) {
        Request request  = new Request.Builder()
                .url(finalUrl)
                .method("POST", body)
                //.post(body)
                .build();

        Call call = HttpClientUtil.HTTP_CLIENT.newCall(request);

        call.enqueue(callback);
    }



    public static RequestBody createRequestBody(String mediaTypeString, String content){
        MediaType mediaType = MediaType.parse(mediaTypeString);
        RequestBody body = RequestBody.create(mediaType, content);

        return body;
    }


    public static void runAsyncWithBody(String finalUrl, RequestBody body, Callback callback) {
        Request request  = new Request.Builder()
                .url(finalUrl)
                .method("POST", body)
                //.post(body)
                .build();

        Call call = HttpClientUtil.HTTP_CLIENT.newCall(request);

        call.enqueue(callback);
    }
    public static void shutdown() {
        System.out.println("Shutting down HTTP CLIENT");
        HTTP_CLIENT.dispatcher().executorService().shutdown();
        HTTP_CLIENT.connectionPool().evictAll();
    }
}
