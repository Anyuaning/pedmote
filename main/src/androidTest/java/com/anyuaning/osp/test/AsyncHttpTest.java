//package com.anyuaning.osp.test;
//
//import com.loopj.android.http.AsyncHttpClient;
//import com.loopj.android.http.AsyncHttpResponseHandler;
//
//import org.apache.http.Header;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import static org.fest.assertions.api.Assertions.assertThat;
//
///**
// * Created by thom on 13-11-5.
// */
//@RunWith(RobolectricGradleTestRunner.class)
//public class AsyncHttpTest {
//
//    @Test
//    public void testRequest() {
//        AsyncHttpClient client = new AsyncHttpClient();
//        client.get("http://www.baidu.com", new AsyncHttpResponseHandler() {
//
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                super.onSuccess(statusCode, headers, responseBody);
////                assertThat(statusCode).isEqualTo(200);
//                assertThat(statusCode).isEqualTo(500);
//            }
//        });
//        assertThat(client.getTimeout()).isEqualTo(10000);
//    }
//}
