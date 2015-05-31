package com.android.xpc.zxing.decode.object;

import com.alibaba.fastjson.JSONObject;

public class ContactEncoderObject implements EncoderObject{

	private int id;
	private String name;
	private String phoneNumber;
	private String email;
	
	
	@Override
	public String toJsonString() {
		// TODO Auto-generated method stub
		return JSONObject.toJSONString(this);
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getPhoneNumber() {
		return phoneNumber;
	}


	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}

}
