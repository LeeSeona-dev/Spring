package com.smhrd.myapp;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smhrd.myapp.domain.WebMember;
import com.smhrd.myapp.mapper.MemberMapper;

@Controller //View 반환 (앞 웹 인포 , 뒤에 jsp)
public class MemberController {
	
	//의존성주입(DI) : Mapper 객체(구현체)를 외부에서 생성하고 주입시켜서 사용
	@Autowired
	private MemberMapper mapper;
	
	@RequestMapping(value="/member/join", method=RequestMethod.POST)
	public String join(@RequestParam("email")String email, @RequestParam("pw") String pw,
					@RequestParam("tel") String tel , @RequestParam("address") String address) {
//		System.out.println(email);
//		System.out.println(pw);
//		System.out.println(tel);
//		System.out.println(address);
		WebMember wm = new WebMember(email,pw,tel,address);
		
		int cnt = mapper.join(wm);
		System.out.print(cnt);
		
		//member/join : 클라이언트가 요청 -> 주소창에 member/join라고 뜬다.
		
		if(cnt>0) {//회원가입 성공했을시
			//return "joinSuccess"; -> 주소창에 member/join
			return "redirect:/joinsuccess?email="+email;
		}else {//회원가입 실패시
			//return "index"; -> 주소창에 member/join
			return "redirect:/";
		}
		
	}
	
	@RequestMapping(value="/member/login", method=RequestMethod.POST)
	public String login(@RequestParam("email") String email, @RequestParam("pw") String pw, HttpSession session) {
		WebMember wm = new WebMember(email, pw);
		WebMember loginMember = mapper.login(wm);
		session.setAttribute("loginMember", loginMember);
		
		return "redirect:/";
	}
	
	@RequestMapping(value="/member/logout")
	public String logout(HttpSession session) {
		//session.invalidate();
		session.removeAttribute("loginMember");
		return "redirect:/";
	}
	
	@RequestMapping(value="/member/delete/{email}")
	public String delete(@PathVariable("email") String email) {
		//첫번째 방법 :RequestParam : 쿼리스트링, post 요청 packet 바디 데이터,
		//	데이터를 각각 받을 수 있음
		//PathVariable : 경로에 포함된 데이터 가지고 올때
		System.out.println(email);
		
		int cnt = mapper.delete(email);
		
		return "redirect:/select";
	}
	
	@RequestMapping(value="/member/update", method=RequestMethod.POST)
	public String update(@ModelAttribute WebMember wm, HttpSession session) {
		
		System.out.println(wm.getPw());
		System.out.println(wm.getTel());
		System.out.println(wm.getAddress());
		
		WebMember loginMember =(WebMember)session.getAttribute("loginMember");
		wm.setEmail(loginMember.getEmail());
		
		int cnt = mapper.update(wm);
		
		if(cnt>0) {//수정성공
			//변경된 세션 값 수정하기
			session.setAttribute("loginMember", wm);
			return "redirect:/";
		}else {//수정 실패시
			
			return "redirect:/update";
		}
	}
	//View 반환 x -> data(model)을 반환하는 메서드로 바꿔주어야함!
	//@Controller => View 반환하는 메서드
	//@Controller 안에 @ResponseBody => model을 반환하는 메서드
	@RequestMapping(value="/member/emailcheck")
	public @ResponseBody String emailCheck(@RequestParam("input") String email) {
		System.out.println(email);
		//email DB에 있는지 확인
		int result = mapper.emailCheck(email);
		System.out.println(result);
		
		if(result>0) {//이메일이 이미 있으므로 회원가입불가!
			return "fail"; //일반 문자열 (View 아님!)
		}else { //값이 없으므로 회원가입 가능한 이메일
			return "success";
		}
	}
}
