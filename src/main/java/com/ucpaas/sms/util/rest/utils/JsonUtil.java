package com.ucpaas.sms.util.rest.utils;


import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class JsonUtil {
	public static String toJsonStr(Object obj) {
		Gson gson = new Gson();
		return gson.toJson(obj);
	}
//	public static List<String> jsonStrToArray(String str){
//		Gson gson = new Gson();
//		List<String> ps = gson.fromJson(str, new TypeToken<List<String>>(){}.getType());
//		return ps;
//	}
//	public static Object fromJson(String num ,Object o){
//		Gson gson = new Gson();
//		return gson.fromJson(num, o.getClass());
//	}
	public static JSONObject toJsonObj(String str) throws JSONException {
		JSONObject jb = new JSONObject(str);
		return jb;
	}
	@SuppressWarnings("rawtypes")
	public static String ArrayToJsonStr(List list){
		String jsonStr = null ;
		if(list!=null && list.size()>0){
			JSONArray json = new JSONArray(list);
			jsonStr = json.toString();
		}
		return jsonStr ;
	}

	public static void main(String[] args) {
		//language=JSON
		String tet = "{\"file\": 1211, \"file1\" : {\"asdf\": 222}}";
		String test = "";
	}

}
