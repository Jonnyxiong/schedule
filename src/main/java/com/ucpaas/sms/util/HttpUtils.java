package com.ucpaas.sms.util;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ucpaas.sms.util.rest.utils.JsonUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ucpaas.sms.util.encrypt.EncryptUtils;
import com.ucpaas.sms.util.rest.SSLHttpClient;



@SuppressWarnings("deprecation")
public class HttpUtils {

	private static Logger logger = LoggerFactory.getLogger(HttpUtils.class);
	
	/**
	 * Http 请求方法
	 * @param url
	 * @param content
	 * @return
	 */
	public static String httpPost(String url, String content, boolean needSSL) {
		// 创建HttpPost
		String result = null;
		HttpClient httpClient = getHttpClient(needSSL, StrUtils.getHostFromURL(url));
		try {
			HttpPost httpPost = new HttpPost(url);
			httpPost.setHeader("Content-Type", ContentType.APPLICATION_JSON + ";charset=utf-8");
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(2000).setConnectTimeout(2000).build();
			httpPost.setConfig(requestConfig);
			BasicHttpEntity requestBody = new BasicHttpEntity();
			requestBody.setContent(new ByteArrayInputStream(content.getBytes("utf-8")));
			requestBody.setContentLength(content.getBytes("utf-8").length);
			httpPost.setEntity(requestBody);
			// 执行客户端请求
			HttpEntity entity = httpClient.execute(httpPost).getEntity();
			
			if (entity != null) {
				result = EntityUtils.toString(entity, "utf-8");
				EntityUtils.consume(entity);
			}

		} catch (Throwable e) {
			logger.error("【HTTP请求失败】: url={}, content={}", url, content );
		}
		
		return result;
	}
	
	
	public static DefaultHttpClient getHttpClient(boolean sslClient, String host){
		DefaultHttpClient httpclient=null;
		if (sslClient) {
			try {
				SSLHttpClient chc = new SSLHttpClient();
				InetAddress address = null;
				String ip;
				try {
				   address = InetAddress.getByName(host);
				   ip = address.getHostAddress().toString();
				   httpclient = chc.registerSSL(ip,"TLS",443,"https");
				} catch (UnknownHostException e) {
				   logger.error("获取请求服务器地址失败：host = {} " + host);
				   e.getStackTrace().toString();
				}
				HttpParams hParams=new BasicHttpParams();
				hParams.setParameter("https.protocols", "SSLv3,SSLv2Hello");
				httpclient.setParams(hParams);
			} catch (KeyManagementException e) {
				logger.error(e.getStackTrace().toString());
			}catch (NoSuchAlgorithmException e) {
				logger.error(e.getStackTrace().toString());
			}
		}else {
			httpclient=new DefaultHttpClient();
		}
		return httpclient;
	}
	
	
	/**
	 * http Get 请求时附带 Authorization
	 * <br> Hearder中的Authorization = encodeBase64(username + ":" + password)
	 * @param url
	 * @param username
	 * @param password
	 * @return responseString
	 */
	public static String httpGetWithBasicAuthorization(String url, String username, String password){
		
		HttpGet request = new HttpGet(url);
		request.addHeader(HttpHeaders.AUTHORIZATION, getBasicAuthorization(username, password));
		
		String response = "";
		CloseableHttpClient closeableHttpClient = HttpClientBuilder.create().build();
		try {
			CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(request);
			HttpEntity entity = closeableHttpResponse.getEntity();
			if (entity != null) {
				response = EntityUtils.toString(closeableHttpResponse.getEntity());
			}
			
		} catch (ClientProtocolException e) {
			logger.error("【HTTP请求失败】： ", e);
		} catch (IOException e) {
			logger.error("【HTTP请求失败】： ", e);
		} finally {
			try {
				closeableHttpClient.close();
			} catch (IOException e) {
				logger.error("【HTTP请求失败】： ", e);
			}
		}
		
		return response;
		
	}
	
	/**
	 * 构建HTTP BasicAuthorization
	 * @param username
	 * @param password
	 * @return 返回字符串等于encodeBase64(username + ":" + password)
	 */
	private static String getBasicAuthorization(String username, String password){
		String basicAuthorization = "";
		String authHeader = "Basic ";
		String authContent = EncryptUtils.encodeBase64(username + ":" + password);
		basicAuthorization = authHeader + authContent;
		
		return basicAuthorization;
	}

}
