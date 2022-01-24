package com.amane.seckill.utils;

import com.amane.seckill.pojo.User;
import com.amane.seckill.vo.RespBean;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

/**
 * 生成用户工具类
 * <p>
 * 乐字节：专注线上IT培训
 * 答疑老师微信：lezijie
 *
 * @author zhoubin
 * @since 1.0.0
 */
public class UserUtil {
	private static void createUser(int count) throws Exception {
		List<User> users = new ArrayList<>(count);
		//生成用户
		for (int i = 0; i < count; i++) {
			User user = new User();
			user.setPhone(15000000000L+i);
			user.setPassword("XTxt1234");
			user.setIdentity(String.valueOf(i));
			user.setName("user:"+i);
			users.add(user);
		}
		int res = users.size();
		res++;
		System.out.println("create user");
		 // //插入数据库
		 Connection conn = getConn();
		 String sql = "insert into t_user(phone, password, name, identity)values(?,?,?,?)";
		 PreparedStatement pstmt = conn.prepareStatement(sql);
		 for (int i = 0; i < users.size(); i++) {
		 	User user = users.get(i);
		 	pstmt.setLong(1, user.getPhone());
		 	pstmt.setString(2, user.getPassword());
		 	pstmt.setString(3, user.getName());
		 	pstmt.setString(4, user.getIdentity());
		 	pstmt.addBatch();
		 }
		 pstmt.executeBatch();
		 pstmt.close();
		 conn.close();
		 System.out.println("insert to db");
		//登录，生成userTicket
		String urlString = "http://localhost:8080/login/doLogin";
		File file = new File("C:\\Users\\\\amane\\Desktop\\config.txt");
		if (file.exists()) {
			file.delete();
		}
		RandomAccessFile raf = new RandomAccessFile(file, "rw");
		file.createNewFile();
		raf.seek(0);
		for (int i = 0; i < users.size(); i++) {
			User user = users.get(i);
			URL url = new URL(urlString);
			HttpURLConnection co = (HttpURLConnection) url.openConnection();
			co.setRequestMethod("POST");
			co.setDoOutput(true);
			OutputStream out = co.getOutputStream();
			String params = "phone=" + user.getPhone() + "&password=" + user.getPassword();
			out.write(params.getBytes());
			out.flush();
			InputStream inputStream = co.getInputStream();
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			byte buff[] = new byte[1024];
			int len = 0;
			while ((len = inputStream.read(buff)) >= 0) {
				bout.write(buff, 0, len);
			}
			inputStream.close();
			bout.close();
			String response = new String(bout.toByteArray());
			ObjectMapper mapper = new ObjectMapper();
			RespBean respBean = mapper.readValue(response, RespBean.class);
			String userTicket = ((String) respBean.getObj());
			System.out.println("create userTicket : " + user.getPhone());

			String row = user.getPhone() + "," + userTicket;
			raf.seek(raf.length());
			raf.write(row.getBytes());
			raf.write("\r\n".getBytes());
			System.out.println("write to file : " + user.getPhone());
		}
		raf.close();

		System.out.println("over");
	}

	private static Connection getConn() throws Exception {
		String url = "jdbc:mysql://localhost:3306/bankms?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai";
		String username = "root";
		String password = "XTxt1234";
		String driver = "com.mysql.cj.jdbc.Driver";
		Class.forName(driver);
		return DriverManager.getConnection(url, username, password);
	}

	public static void main(String[] args) throws Exception {
		createUser(5000);
	}
}
