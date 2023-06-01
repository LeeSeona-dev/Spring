package com.smhrd.myapp.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WebMember {
	private String email;
	private String pw;
	private String tel;
	private String address;
	
	public WebMember(String email, String pw) {
		this.email=email;
		this.pw=pw;
	}

}
