package com.smhrd.myapp;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.smhrd.myapp.domain.WebMember;
import com.smhrd.myapp.mapper.MemberMapper;

// Spring에게 해당 Class가 Controller 역할을 한다고 알려주기 위해 사용하는 Annotation
// Controller 의 기본적 역할-> view를 반환하는 역할(.jsp , .html..)
@Controller
public class HomeController {
	
	@Autowired
	private MemberMapper mapper;
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	//실제 요청 경로와 value값이 일치하면 해당 메서드가 실행되도록 만들어주는 Annotation 
	//value : 요청 경로를 의미, method : HTTP Method(Get, Post 방식 지정..)
	//context-root(혹은 path라고도 함) : 서버안에서 동작하는 프로젝트를 식별하기 위한 값
	//http://localhost:8085/myapp(context-root)/ 를 의미함!!
	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		// "home" -> view
		// ViewResolver 가 view 찾아줌
		// ViewResolver : webapp/WEB-INF/spring/appServlet/servlet-context.xml
		// 여기에 관련 정보 작성!
		// prefix : /WEB-INF/views/
		// suffic : .jsp
		// -> /WEB-INF/views/home.jsp
		//화면 구성(.jsp, .js, .css..) : 기본 경로가 webapp 아래
		return "home";
	}
	
	//index.jsp(view) 를 return하는 메서드 만들기!
	@RequestMapping(value="/", method=RequestMethod.GET)
	public String index() {
		
		return "index";
	}
	//GET 요청 받아줄때에는 method생략가능
	@RequestMapping(value="/joinsuccess")
	public String joinSuccess(@RequestParam("email") String email,Model model) {
		//email 값을 view에 출력해주려면 어딘가에 저장이 되어있어야함
		//forward : request 영역에 값 저장
		//Model : 데이터를 저장할 수 있는 공간 , request랑 비슷하게 쓰임
		//request : 한페이지 한번만 데이터 이동할 때 , session : 페이지 여러번 이동해야할 때
		//로그인 -> 세션 , 한번만 넘길때는 Model 사용
		model.addAttribute("joinEmail",email);
		//request.setAttribute() 랑 비슷한 역할
		return "joinSuccess"; //view만 반환(이메일 데이터 x)
		
	}
	
	@RequestMapping(value="/select")
	public String select(Model model) {
		//DB에 있는 데이터들 가져오기~ mapper이용
		List<WebMember> list = mapper.select();
		model.addAttribute("list",list);
		return "select";
		
	}
	
	@RequestMapping(value="/update")
	public String update() {
		//세션에 남아있음
		return "update";
	}
}
