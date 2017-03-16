package com.example.administrator.mapclient.utils;

import com.alibaba.fastjson.JSON;
import com.example.administrator.mapclient.bean.User;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public class JavaTest {
	static Socket soc;
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		/**
		 * 测试数据库连接（成功）
		 */

		/**
		 * 测试注册/登录（成功）
		 */
		User user = new User(1,"18387390390","abc123");
		JsonBean  jsonBean = new JsonBean(200, user.getUid(),"","");
		jsonBean.setUser(user);
		soc = new Socket();
		soc.connect(new InetSocketAddress("10.7.184.56", 10090),100);
		PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(soc.getOutputStream(),"utf-8"),true);
	    printWriter.println(JSON.toJSONString(jsonBean)+"\n");
		
	   BufferedReader bufferedReader = new BufferedReader(
	    		new InputStreamReader(soc.getInputStream()));
	    String readLine = bufferedReader.readLine();
	   // System.out.println(readLine);
	}

}
