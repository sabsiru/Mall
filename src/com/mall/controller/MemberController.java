package com.mall.controller;

import com.board.VO.BoardVO;
import com.mall.VO.CartVO;
import com.mall.VO.MemberVO;
import com.mall.VO.OrderVO;
import com.mall.dao.CartDao;
import com.mall.dao.MemberDao;
import com.mall.dao.OrderDao;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.mall.paging.Paging;

@Controller
public class MemberController {
	private int pageSize = 10;
	private int blockCount = 10;
	@Autowired
	private MemberDao memberDao;
	@Autowired
	OrderDao orderDao;
	@Autowired
	private CartDao cartDao;

	// loginCheck
	// 회원가입 페이지로 이동
	@RequestMapping("/mall/join.do")
	public String join() {
		return "/Join";
	}

	// id중복체크
	@RequestMapping(value = "/mall/idCheck.do", method = RequestMethod.POST)
	@ResponseBody
	public String idCheck(HttpServletRequest request, HttpServletResponse response, MemberVO vo) throws IOException {
		// Join.jsp에서 넘어온 id값을 받아옴
		String id = request.getParameter("id");
		// id값을 vo에 저장
		vo.setId(id);
		// id값을 DB에서 조회
		String result = "";
		int count = memberDao.idCheck(id);
		if (count == 1) {
			result = "used";
		}
		return result;
	}

	// phone중복체크
	@RequestMapping(value = "/mall/phoneCheck.do", method = RequestMethod.POST)
	@ResponseBody
	public String phoneCheck(HttpServletRequest request, HttpServletResponse response, MemberVO vo) throws IOException {
		// Join.jsp에서 넘어온 phone값을 받아옴
		String phone = request.getParameter("phone");
		// phone값을 vo에 저장
		vo.setPhone(phone);
		// phone값을 DB에서 조회
		String result = "";
		int count = memberDao.phoneCheck(phone);
		if (count == 1) {
			result = "used";
		}
		return result;
	}

	// 회원가입
	@RequestMapping(value = "/mall/join.do", method = RequestMethod.POST)
	public String join(MemberVO vo, HttpSession session, HttpServletRequest request) throws IOException {
		// Join.jsp에서 넘어온 값을 vo에 저장
		vo.setId(vo.getId());
		vo.setPass(vo.getPass());
		vo.setName(vo.getName());
		vo.setPhone(vo.getPhone());
		vo.setMail(vo.getMail());
		vo.setAddr(vo.getAddr());
		vo.setPostcode(vo.getPostcode());

		// DB에 저장
		this.memberDao.join(vo);
		// 회원가입 성공시 로그인 페이지로 이동
		return "redirect:/mall/login.do";
	}

	// 로그인 폼으로 이동
	@RequestMapping("/mall/login.do")
	public String login() {
		return "/Login";
	}

	// 로그인
	@RequestMapping(value = "/mall/login.do", method = RequestMethod.POST)
	public String login(MemberVO vo, HttpSession session, HttpServletRequest request) {

		// Login.jsp에서 넘어온 값을 vo에 저장
		vo.setId(vo.getId());
		vo.setPass(vo.getPass());
		// DB에서 조회
		MemberVO result = memberDao.login(vo);

		// 로그인 성공시 msg에 메시지 담아서 보내기
		if (result != null) {
			session = request.getSession();
			String id = SessionConfig.getSessionidCheck("id", result.getId());
			// 페이지가 바뀌어도 로그인 유지
			session.setAttribute("id", result.getId());
			session.setAttribute("name", result.getName());
			session.setAttribute("phone", result.getPhone());
			session.setAttribute("mail", result.getMail());
			session.setAttribute("addr", result.getAddr());
			session.setAttribute("detailaddr", result.getDetailaddr());
			session.setAttribute("powerno", result.getPowerno());
			session.setAttribute("postcode", result.getPostcode());
			session.setAttribute("regdate", result.getRegdate());
			// 로그인 유지 시간 30분
			session.setMaxInactiveInterval(1800);

			// powerno가 5일 경우 관리자님 환영합니다.
			if (result.getPowerno() == 5) {
				session.setAttribute("msg", "관리자님 환영합니다.");
				return "redirect:/mall/main.do";
			} else {
				session.setAttribute("msg", result.getName() + "님 환영합니다.");
			}
			return "redirect:/mall/main.do";
			// 입력값이 없으면 msg에 메시지 담아서 보내기
		} else if (vo.getId().equals("") || vo.getPass().equals("")) {
			session.setAttribute("msg", "아이디 또는 비밀번호를 입력해주세요.");
			return "redirect:/mall/login.do";
			// 입력값이 없으면 msg에 메시지 담아서 보내기
			// session에 같은 id가 있을경우 기존의 session 초기화

		} else {
			// 실패시 msg에 메시지 담아보내기
			session.setAttribute("msg", "아이디 또는 비밀번호가 일치하지 않습니다.");
			return "redirect:/mall/login.do";
		}
	}

	// 로그아웃
	@RequestMapping("/mall/logout.do")
	public String logout(HttpSession session) {
		session.setAttribute("msg", "로그아웃 되었습니다.");
		// 세션 초기화
		session.invalidate();
		return "redirect:/mall/main.do";
	}

	// findId
	@ResponseBody
	@RequestMapping(value = "/mall/findId.do", method = RequestMethod.POST)
	public String findId(MemberVO vo, HttpSession session, HttpServletRequest request) {
		// FindId.jsp에서 넘어온 값name과 phone을 db조회

		String name = request.getParameter("name");
		String phone = request.getParameter("phone");
		vo.setName(name);
		vo.setPhone(phone);
		// DB에서 조회
		String id = memberDao.findId(vo);
		String result = "no";

		// 조회 결과가 있으면 "result에 담아서 저장
		if (id != null) {
			result = id;
			return result;
		} else {
			return result;
		}
	}

	// findPass
	@ResponseBody
	@RequestMapping(value = "/mall/findPass.do", method = RequestMethod.POST)
	public String findPass(MemberVO vo, HttpSession session, HttpServletRequest request) {
		// FindPass.jsp에서 넘어온 값id와 phone을 db조회

		String id = request.getParameter("id");
		String phone = request.getParameter("phone");
		vo.setId(id);
		vo.setPhone(phone);
		// DB에서 조회
		String pass = memberDao.findPass(vo);
		String result = "no";

		// 조회 결과가 있으면 "result에 담아서 저장
		if (pass != null) {
			result = pass;
			return result;
		} else {
			return result;
		}
	}

	// myPage
	@RequestMapping("/mall/myPage.do")
	public String myPage(MemberVO vo, HttpSession session, HttpServletRequest request) {
		// 세션에 저장된 id값을 받아옴
		String id = (String) session.getAttribute("id");
		// 세션값이 없으면 logindo로 이동
		if (id == null) {
			session.setAttribute("msg", "로그인 후 이용해주세요.");
			return "redirect:/mall/login.do";
		}
		// id값을 vo에 저장
		vo.setId(id);
		// id값을 DB에서 조회
		MemberVO result = memberDao.myPage(vo);
		// 조회 결과가 있으면 세션에 저장
		if (result != null) {
			session.setAttribute("id", result.getId());
			session.setAttribute("name", result.getName());
			session.setAttribute("phone", result.getPhone());
			session.setAttribute("mail", result.getMail());
			session.setAttribute("addr", result.getAddr());
			session.setAttribute("detailaddr", result.getDetailaddr());
			session.setAttribute("postcode", result.getPostcode());
			return "/myPage";
		} else {
			return "/myPage";
		}
	}

	// getPass update에서 넘어온 pass값과 db에 저장된 pass값을 비교
	@ResponseBody
	@RequestMapping(value = "/mall/getPass.do", method = RequestMethod.POST)
	public String getPass(MemberVO vo, HttpSession session, HttpServletRequest request) {
		// pass값을 받아옴
		String pass = request.getParameter("pass");
		// 세션에 저장된 id값을 받아옴
		String id = (String) session.getAttribute("id");
		vo.setPass(pass);
		vo.setId(id);
		// id값과 pass값을 DB에서 조회
		int count = memberDao.getPass(vo);
		String result = "no";
		// 조회 count가 1이면 본인임으로 ok
		if (count == 1) {
			result = "ok";
			return result;
		} else {
			return result;
		}
	}

	// getPhone update에서 넘어온 phone값과 db에 저장된 phone값을 비교
	@ResponseBody
	@RequestMapping(value = "/mall/getPhone.do", method = RequestMethod.POST)
	public String getPhone(MemberVO vo, HttpSession session, HttpServletRequest request) {
		// phone값을 받아옴
		String phone = request.getParameter("phone");
		// 세션에 저장된 id값을 받아옴
		String id = (String) session.getAttribute("id");
		vo.setPhone(phone);
		// id값과 phone값을 DB에서 조회
		int count = memberDao.phoneCheck(phone);
		vo.setId(id);
		int count2 = memberDao.getPhone(vo);
		String result = "no";
		// 조회 count가 1이고 count2가 1이면 본인임으로 ok
		if (count == 1 && count2 == 1) {
			result = "same";
			return result;
			// count가 0이면 중복이 아님으로 ok
		} else if (count == 0) {
			result = "ok";
			return result;
		} else {
			return result;
		}

	}

	// update
	@RequestMapping("/mall/updateMember.do")
	public String updateMember(MemberVO vo, HttpSession session, HttpServletRequest request) throws Exception {
		// myPage에서 phone,email,postcode,addr,detailaddr 값 받아오기
		String id = request.getParameter("id");
		String phone = request.getParameter("phone");
		String mail = request.getParameter("mail");
		String postcode = request.getParameter("postcode");
		String addr = request.getParameter("addr");
		String detailaddr = request.getParameter("detailaddr");

		System.out.println("id = " + id);
		System.out.println("phone = " + phone);
		vo.setPhone(phone);
		vo.setMail(mail);
		vo.setPostcode(postcode);
		vo.setAddr(addr);
		vo.setDetailaddr(detailaddr);
		vo.setId(id);
		// DB에서 update
		int result = memberDao.updateMember(vo);
		// update 성공시 세션에 저장
		if (result == 1) {
			session.setAttribute("msg", "회원정보가 수정되었습니다.");
			return "redirect:/mall/myPage.do";
		} else {
			return "redirect:/mall/myPage.do";
		}
	}

	// updatePass로 이동
	@RequestMapping("/mall/updatePassForm.do")
	public String updatePassForm(MemberVO vo, HttpSession session, HttpServletRequest request) {
		// session에 id가 없으면 login.do로 이동
		String id = (String) session.getAttribute("id");
		if (id == null) {
			session.setAttribute("msg", "로그인 후 이용해주세요.");
			return "redirect:/mall/login.do";
		}
		// updatePass로 이동
		return "/updatePassForm";
	}

	// updatePass
	@RequestMapping("/mall/updatePass.do")
	public String updatePass(MemberVO vo, HttpSession session, HttpServletRequest request) {
		// id값 가져옴
		String id = (String) session.getAttribute("id");
		// pass값 가져옴
		String pass = request.getParameter("pass");
		vo.setId(id);
		vo.setPass(pass);
		// DB에서 update
		int result = memberDao.updatePass(vo);
		// update 성공시 세션에 저장
		if (result == 1) {
			session.setAttribute("msg", "비밀번호가 수정되었습니다.");
			return "redirect:/mall/myPage.do";
		} else {
			return "redirect:/mall/myPage.do";
		}
	}

	// deleteMemberForm으로 이동
	@RequestMapping("/mall/deleteMemberForm.do")
	public String deleteMemberForm(MemberVO vo, HttpSession session, HttpServletRequest request) {
		// session에 id가 없으면 login.do로 이동
		String id = (String) session.getAttribute("id");
		if (id == null) {
			session.setAttribute("msg", "로그인 후 이용해주세요.");
			return "redirect:/mall/login.do";
		}
		// deleteMemberForm으로 이동
		return "/deleteMemberForm";
	}

	// deleteMember
	@RequestMapping("/mall/deleteMember.do")
	public String deleteMember(MemberVO vo, HttpSession session, HttpServletRequest request) {
		// id값 가져옴
		String id = (String) session.getAttribute("id");
		// 입력받은 pass값
		String pass = request.getParameter("pass");
		vo.setPass(pass);
		vo.setId(id);

		this.cartDao.deleteCartByMember(id);
		// id가 없을시 login.do로 이동
		if (id == null || pass == null) {
			session.setAttribute("msg", "로그인 후 이용해주세요.");
			return "redirect:/mall/login.do";
		}

		// 주문내역 확인
		int result2 = orderDao.selectOrderListAllCountByDelete(id);
		// 주문 내역이 있으면 탈퇴 실패 메시지
		if (result2 > 0) {
			session.setAttribute("msg", "주문내역이 있어 탈퇴가 불가능합니다.");
			return "redirect:/mall/myPage.do";
		} else {
			// 주문 내역이 없으면 탈퇴 성공 메시지
			int result = memberDao.deleteMember(vo);
			if (result == 1) {
				session.setAttribute("msg", "탈퇴되었습니다.");
				session.invalidate();
				return "redirect:/mall/main.do";
			} else {
				session.setAttribute("msg", "비밀번호가 틀렸습니다.");
				return "redirect:/mall/myPage.do";
			}
		}

	}

	// orderList.do 폼으로 이동
	@RequestMapping({ "/mall/orderList.do" })
	public ModelAndView process(@RequestParam(value = "pageNum", defaultValue = "1") int currentPage,
			@RequestParam(value = "keyField", defaultValue = "") String keyField,
			@RequestParam(value = "keyWord", defaultValue = "") String keyWord, HttpSession session) {
		String pagingHtml = "";
		@SuppressWarnings({ "unchecked", "rawtypes" })
		HashMap<String, Object> map = new HashMap();
		ModelAndView mav = new ModelAndView();
		String id = (String) session.getAttribute("id");
		if (id == null) {
			session.setAttribute("msg", "로그인 후 이용해주세요.");
			mav.setViewName("redirect:/mall/login.do");
			return mav;
		}
		map.put("keyField", keyField);
		map.put("keyWord", keyWord);
		map.put("id", id);

		System.out.println("id = " + id);
		int count = this.orderDao.selectOrderListAllCount(map);
		System.out.println("count = " + count);

		Paging page = new Paging(keyField, keyWord, currentPage, count, this.pageSize, this.blockCount, "orderList.do");

		pagingHtml = page.getPagingHtml().toString();

		map.put("startCount", Integer.valueOf(page.getStartCount()));
		map.put("endCount", Integer.valueOf(page.getEndCount()));
		System.out.println("map = " + map);
		List<OrderVO> list = null;
		if (count > 0) {
			list = this.orderDao.selectOrderListAll(map);
		} else {
			list = Collections.emptyList();
		}
		int number = count - (currentPage - 1) * this.pageSize;

		mav.setViewName("OrderList");
		mav.addObject("count", Integer.valueOf(count));
		mav.addObject("currentPage", Integer.valueOf(currentPage));
		mav.addObject("orderList", list);
		mav.addObject("pagingHtml", pagingHtml);
		mav.addObject("number", Integer.valueOf(number));

		return mav;
	}

}
