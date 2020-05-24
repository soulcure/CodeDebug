package com.example.core.http;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.example.core.utils.StreamUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * Created by colin on 2017/6/6.
 * http utils
 */

public class HttpConnector {

    private static final String TAG = HttpConnector.class.getSimpleName();


    private HttpConnector() {
        throw new AssertionError();
    }

    /**
     * AsyncTask to get data by HttpURLConnection
     * 2016-9-13
     */

    public static void httpGet(String url, IGetListener request) {
        httpGet(null, url, null, request);
    }


    public static void httpGet(ContentValues header, String url,
                               IGetListener request) {
        httpGet(header, url, null, request);
    }

    public static void httpGet(String url, ContentValues params,
                               IGetListener request) {
        httpGet(null, url, params, request);
    }


    public static void httpGet(ContentValues headers, String url,
                               ContentValues params, IGetListener request) {
        HttpGetAsyncTask task = new HttpGetAsyncTask(request);
        task.setParams(params);
        task.setHeaders(headers);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);

    }


    public static void httpPost(ContentValues header, String url,
                                IPostListener request) {
        httpPost(url, null, header, null, request);
    }


    public static void httpPost(String url, ContentValues params,
                                IPostListener request) {
        httpPost(url, params, null, null, request);
    }

    public static void httpPost(ContentValues header, String url,
                                ContentValues params, IPostListener request) {
        httpPost(url, params, header, null, request);
    }


    public static void httpPost(String url, String postBoby,
                                IPostListener request) {
        httpPost(url, null, null, postBoby, request);
    }

    public static void httpPost(String url, ContentValues params,
                                ContentValues headers, String postBoby,
                                IPostListener request) {
        HttpPostAsyncTask task = new HttpPostAsyncTask(request);
        task.setParams(params);
        task.setHeaders(headers);
        task.setPostBoby(postBoby);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
    }





    private static class HttpGetAsyncTask extends
            AsyncTask<String, Void, String> {

        private IGetListener mRequest;
        private ContentValues mParams;
        private ContentValues mHeaders;

        public HttpGetAsyncTask(IGetListener request) {
            mRequest = request;
        }

        public void setHeaders(ContentValues headers) {
            mHeaders = headers;
        }


        public void setParams(ContentValues headers) {
            mParams = headers;
        }


        @Override
        protected String doInBackground(String... params) {
            String url = params[0];
            if (TextUtils.isEmpty(url)) {
                throw new IllegalArgumentException("url is null");
            } else {
                return doGet(mHeaders, url, mParams);
            }
        }

        @Override
        protected void onPostExecute(String response) {
            Log.v(TAG, "httpGet response =" + response);
            if (mRequest != null) {
                mRequest.httpReqResult(response);
            }
        }

    }




    private static class HttpPostAsyncTask extends
            AsyncTask<String, Void, String> {

        private IPostListener mRequest;
        private ContentValues mParams;
        private ContentValues mHeaders;
        private String mPostBoby;


        public HttpPostAsyncTask(IPostListener request) {
            mRequest = request;
        }

        public void setParams(ContentValues headers) {
            mParams = headers;
        }


        public void setHeaders(ContentValues headers) {
            mHeaders = headers;
        }

        public void setPostBoby(String postBoby) {
            this.mPostBoby = postBoby;
        }

        @Override
        protected String doInBackground(String... params) {
            String url = params[0];
            if (TextUtils.isEmpty(url)) {
                throw new IllegalArgumentException("url is null");
            }

            if (!TextUtils.isEmpty(mPostBoby)) {
                return doPost(url, mHeaders, mPostBoby);
            } else {
                return doPost(url, mHeaders, mParams);
            }

        }

        @Override
        protected void onPostExecute(String response) {
            Log.v(TAG, "http post  response:" + response);
            if (mRequest != null) {
                mRequest.httpReqResult(response);
            }
        }
    }


    /**
     * http get
     *
     * @param headers
     * @param path
     * @param urlParams
     * @return
     */
    public static String doGet(ContentValues headers, String path, ContentValues urlParams) {
        String response = null;
        HttpURLConnection connection;

        try {
            if (urlParams != null && urlParams.size() > 0) {
                path = path + "?" + getParams(urlParams, true);
            }

            Log.v(TAG, "httpGet url =" + path);
            URL url = new URL(path);

            connection = (HttpURLConnection) url
                    .openConnection();

            if (headers != null && headers.size() > 0) {
                for (Map.Entry<String, Object> entry : headers.valueSet()) {
                    String key = entry.getKey(); // name
                    String value = entry.getValue().toString(); // value
                    connection.setRequestProperty(key, value);
                }
            }

            int code = connection.getResponseCode();
            if (code >= 200 && code < 300) {
                response = StreamUtils.readStream(connection.getInputStream());
            } else {
                response = StreamUtils.readStream(connection.getErrorStream());
            }
            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
            response = e.toString();
        }

        return response;
    }


    /**
     * @param path    http host
     * @param params  http url携带的参数
     * @param headers http请求头信息
     * @return
     */


    public static String doPost(String path, ContentValues headers, ContentValues params) {
        String response = null;
        try {
            URL url = new URL(path);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setConnectTimeout(30000); // 链接超时
            connection.setReadTimeout(20000); // 读取超时

            connection.setDoOutput(true); // 是否输入参数
            connection.setDoInput(true); // 是否向HttpUrLConnection读入，默认true.
            connection.setRequestMethod("POST"); // 提交模式
            connection.setUseCaches(false); // 不能缓存
            connection.setInstanceFollowRedirects(true); // 连接是否尊重重定向
            //connection.setRequestProperty("Content-Type", "application/json"); // 设定传入内容是可序列化的java对象
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            if (headers != null && headers.size() > 0) {
                for (Map.Entry<String, Object> entry : headers.valueSet()) {
                    String key = entry.getKey(); // name
                    String value = entry.getValue().toString(); // value
                    connection.setRequestProperty(key, value);
                }
            }

            connection.connect(); // 连接，所有connection的配置信息必须在connect()方法前提交。

            OutputStream os = connection.getOutputStream();

            if (params != null && params.size() > 0) {
                String postBoby = getParams(params, false);
                if (!TextUtils.isEmpty(postBoby)) {
                    byte[] content = postBoby.getBytes(Charset.forName("UTF-8"));
                    os.write(content, 0, content.length);
                }
            }

            os.flush();
            os.close();

            //服务器修改，错误类型封装到了 response json code里面，此处取消 http ResponseCode 判断
            int code = connection.getResponseCode();
            if (code >= 200 && code < 300) {
                response = StreamUtils.readStream(connection.getInputStream());
            } else {
                response = StreamUtils.readStream(connection.getErrorStream());
            }

            Log.v(TAG, path + " : post response = " + response);
            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
            response = e.toString();
        }
        return response;
    }


    /**
     * @param path     http host
     * @param headers  http请求头信息
     * @param postBoby http内容
     * @return
     */


    public static String doPost(String path, ContentValues headers, String postBoby) {
        String response = null;
        try {

            URL url = new URL(path);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setConnectTimeout(30000); // 链接超时
            connection.setReadTimeout(20000); // 读取超时

            connection.setDoOutput(true); // 是否输入参数
            connection.setDoInput(true); // 是否向HttpUrLConnection读入，默认true.
            connection.setRequestMethod("POST"); // 提交模式
            connection.setUseCaches(false); // 不能缓存
            connection.setInstanceFollowRedirects(true); // 连接是否尊重重定向
            connection.setRequestProperty("Content-Type", "application/json"); // 设定传入内容是可序列化的java对象
            //connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            if (headers != null && headers.size() > 0) {
                for (Map.Entry<String, Object> entry : headers.valueSet()) {
                    String key = entry.getKey(); // name
                    String value = entry.getValue().toString(); // value
                    connection.setRequestProperty(key, value);
                }
            }

            connection.connect(); // 连接，所有connection的配置信息必须在connect()方法前提交。

            OutputStream os = connection.getOutputStream();

            if (!TextUtils.isEmpty(postBoby)) {
                byte[] content = postBoby.getBytes(Charset.forName("UTF-8"));
                os.write(content, 0, content.length);
            }
            os.flush();
            os.close();


            //服务器修改，错误类型封装到了 response json code里面，此处取消 http ResponseCode 判断
            int code = connection.getResponseCode();
            if (code >= 200 && code < 300) {
                response = StreamUtils.readStream(connection.getInputStream());
            } else {
                response = StreamUtils.readStream(connection.getErrorStream());
            }

            Log.v(TAG, path + " : post response = " + response);
            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
            response = e.toString();
        }
        return response;
    }


    /**
     * 组装参数
     *
     * @param params
     * @param isEncoder
     * @return
     * @throws UnsupportedEncodingException
     */
    private static String getParams(ContentValues params, boolean isEncoder) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        boolean first = true;

        for (Map.Entry<String, Object> entry : params.valueSet()) {
            String key = entry.getKey(); // name
            String value = entry.getValue().toString(); // value
            if (first) {
                first = false;
                //sb.append("?");
            } else {
                sb.append("&");
            }

            if (isEncoder) {
                sb.append(URLEncoder.encode(key, "UTF-8"));
            } else {
                sb.append(key);
            }

            sb.append("=");

            if (isEncoder) {
                sb.append(URLEncoder.encode(value, "UTF-8"));
            } else {
                sb.append(value);
            }
        }

        return sb.toString();
    }

    /**
     * 组装参数
     *
     * @param params
     * @param isEncoder
     * @return
     * @throws UnsupportedEncodingException
     */
    private static String getParams(Map<String, String> params, boolean isEncoder) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        boolean first = true;

        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey(); // name
            String value = entry.getValue(); // value
            if (first) {
                first = false;
                //sb.append("?");
            } else {
                sb.append("&");
            }

            if (isEncoder) {
                sb.append(URLEncoder.encode(key, "UTF-8"));
            } else {
                sb.append(key);
            }

            sb.append("=");

            if (isEncoder) {
                sb.append(URLEncoder.encode(value, "UTF-8"));
            } else {
                sb.append(value);
            }
        }

        return sb.toString();
    }

    public static Response httpPost1(String path, Map<String, String> headers,
                                     Map<String, String> params,
                                     String postBoby) {

        Response response = new Response();
        try {

            URL url = new URL(path);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setConnectTimeout(30000); // 链接超时
            connection.setReadTimeout(20000); // 读取超时

            connection.setDoOutput(true); // 是否输入参数
            connection.setDoInput(true); // 是否向HttpUrLConnection读入，默认true.
            connection.setRequestMethod("POST"); // 提交模式
            connection.setUseCaches(false); // 不能缓存
            connection.setInstanceFollowRedirects(true); // 连接是否尊重重定向
            connection.setRequestProperty("Content-Type", "application/json"); // 设定传入内容是可序列化的java对象
            //connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            if (headers != null && headers.size() > 0) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    String key = entry.getKey(); // name
                    String value = entry.getValue(); // value
                    connection.setRequestProperty(key, value);
                }
            }

            connection.connect(); // 连接，所有connection的配置信息必须在connect()方法前提交。

            OutputStream os = connection.getOutputStream();

            if (!TextUtils.isEmpty(postBoby)) {
                byte[] content = postBoby.getBytes(Charset.forName("UTF-8"));
                os.write(content, 0, content.length);
            }
            os.flush();
            os.close();


            //服务器修改，错误类型封装到了 response json code里面，此处取消 http ResponseCode 判断
            int code = connection.getResponseCode();
            response.setCode(code);
            response.setContent(StreamUtils.readStream(connection.getInputStream()));

            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
            response = null;
        }
        return response;

    }


    public static class Response {
        int code;
        String content;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }


    public static Response httpGet1(String path, Map<String, String> headers,
                                    Map<String, String> params) {
        Response response = new Response();
        try {
            if (params != null && params.size() > 0) {
                path = path + "?" + getParams(params, true);
            }

            URL url = new URL(path);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setConnectTimeout(30000); // 链接超时
            connection.setReadTimeout(20000); // 读取超时

            connection.setDoOutput(true); // 是否输入参数
            connection.setDoInput(true); // 是否向HttpUrLConnection读入，默认true.
            connection.setRequestMethod("POST"); // 提交模式
            connection.setUseCaches(false); // 不能缓存
            connection.setInstanceFollowRedirects(true); // 连接是否尊重重定向
            //connection.setRequestProperty("Content-Type", "application/json"); // 设定传入内容是可序列化的java对象
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            if (headers != null && headers.size() > 0) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    String key = entry.getKey(); // name
                    String value = entry.getValue(); // value
                    connection.setRequestProperty(key, value);
                }
            }

            connection.connect(); // 连接，所有connection的配置信息必须在connect()方法前提交。

            OutputStream os = connection.getOutputStream();

            if (params != null && params.size() > 0) {
                String postBoby = getParams(params, false);
                if (!TextUtils.isEmpty(postBoby)) {
                    byte[] content = postBoby.getBytes(Charset.forName("UTF-8"));
                    os.write(content, 0, content.length);
                }
            }

            os.flush();
            os.close();

            //服务器修改，错误类型封装到了 response json code里面，此处取消 http ResponseCode 判断
            int code = connection.getResponseCode();
            response.setCode(code);
            response.setContent(StreamUtils.readStream(connection.getInputStream()));

            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
            response = null;
        }

        return response;

    }
}



