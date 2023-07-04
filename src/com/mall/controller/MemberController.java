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
	// ȸ������ �������� �̵�
	@RequestMapping("/mall/join.do")
	public String join() {
		return "/Join";
	}

	// id�ߺ�üũ
	@RequestMapping(value = "/mall/idCheck.do", method = RequestMethod.POST)
	@ResponseBody
	public String idCheck(HttpServletRequest request, HttpServletResponse response, MemberVO vo) throws IOException {
		// Join.jsp���� �Ѿ�� id���� �޾ƿ�
		String id = request.getParameter("id");
		// id���� vo�� ����
		vo.setId(id);
		// id���� DB���� ��ȸ
		String result = "";
		int count = memberDao.idCheck(id);
		if (count == 1) {
			result = "used";
		}
		return result;
	}

	// phone�ߺ�üũ
	@RequestMapping(value = "/mall/phoneCheck.do", method = RequestMethod.POST)
	@ResponseBody
	public String phoneCheck(HttpServletRequest request, HttpServletResponse response, MemberVO vo) throws IOException {
		// Join.jsp���� �Ѿ�� phone���� �޾ƿ�
		String phone = request.getParameter("phone");
		// phone���� vo�� ����
		vo.setPhone(phone);
		// phone���� DB���� ��ȸ
		String result = "";
		int count = memberDao.phoneCheck(phone);
		if (count == 1) {
			result = "used";
		}
		return result;
	}

	// ȸ������
	@RequestMapping(value = "/mall/join.do", method = RequestMethod.POST)
	public String join(MemberVO vo, HttpSession session, HttpServletRequest request) throws IOException {
		// Join.jsp���� �Ѿ�� ���� vo�� ����
		vo.setId(vo.getId());
		vo.setPass(vo.getPass());
		vo.setName(vo.getName());
		vo.setPhone(vo.getPhone());
		vo.setMail(vo.getMail());
		vo.setAddr(vo.getAddr());
		vo.setPostcode(vo.getPostcode());

		// DB�� ����
		this.memberDao.join(vo);
		// ȸ������ ������ �α��� �������� �̵�
		return "redirect:/mall/login.do";
	}

	// �α��� ������ �̵�
	@RequestMapping("/mall/login.do")
	public String login() {
		return "/Login";
	}

	// �α���
	@RequestMapping(value = "/mall/login.do", method = RequestMethod.POST)
	public String login(MemberVO vo, HttpSession session, HttpServletRequest request) {

		// Login.jsp���� �Ѿ�� ���� vo�� ����
		vo.setId(vo.getId());
		vo.setPass(vo.getPass());
		// DB���� ��ȸ
		MemberVO result = memberDao.login(vo);

		// �α��� ������ msg�� �޽��� ��Ƽ� ������
		if (result != null) {
			session = request.getSession();
			String id = SessionConfig.getSessionidCheck("id", result.getId());
			// �������� �ٲ� �α��� ����
			session.setAttribute("id", result.getId());
			session.setAttribute("name", result.getName());
			session.setAttribute("phone", result.getPhone());
			session.setAttribute("mail", result.getMail());
			session.setAttribute("addr", result.getAddr());
			session.setAttribute("detailaddr", result.getDetailaddr());
			session.setAttribute("powerno", result.getPowerno());
			session.setAttribute("postcode", result.getPostcode());
			session.setAttribute("regdate", result.getRegdate());
			// �α��� ���� �ð� 30��
			session.setMaxInactiveInterval(1800);

			// powerno�� 5�� ��� �����ڴ� ȯ���մϴ�.
			if (result.getPowerno() == 5) {
				session.setAttribute("msg", "�����ڴ� ȯ���մϴ�.");
				return "redirect:/mall/main.do";
			} else {
				session.setAttribute("msg", result.getName() + "�� ȯ���մϴ�.");
			}
			return "redirect:/mall/main.do";
			// �Է°��� ������ msg�� �޽��� ��Ƽ� ������
		} else if (vo.getId().equals("") || vo.getPass().equals("")) {
			session.setAttribute("msg", "���̵� �Ǵ� ��й�ȣ�� �Է����ּ���.");
			return "redirect:/mall/login.do";
			// �Է°��� ������ msg�� �޽��� ��Ƽ� ������
			// session�� ���� id�� ������� ������ session �ʱ�ȭ

		} else {
			// ���н� msg�� �޽��� ��ƺ�����
			session.setAttribute("msg", "���̵� �Ǵ� ��й�ȣ�� ��ġ���� �ʽ��ϴ�.");
			return "redirect:/mall/login.do";
		}
	}

	// �α׾ƿ�
	@RequestMapping("/mall/logout.do")
	public String logout(HttpSession session) {
		session.setAttribute("msg", "�α׾ƿ� �Ǿ����ϴ�.");
		// ���� �ʱ�ȭ
		session.invalidate();
		return "redirect:/mall/main.do";
	}

	// findId
	@ResponseBody
	@RequestMapping(value = "/mall/findId.do", method = RequestMethod.POST)
	public String findId(MemberVO vo, HttpSession session, HttpServletRequest request) {
		// FindId.jsp���� �Ѿ�� ��name�� phone�� db��ȸ

		String name = request.getParameter("name");
		String phone = request.getParameter("phone");
		vo.setName(name);
		vo.setPhone(phone);
		// DB���� ��ȸ
		String id = memberDao.findId(vo);
		String result = "no";

		// ��ȸ ����� ������ "result�� ��Ƽ� ����
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
		// FindPass.jsp���� �Ѿ�� ��id�� phone�� db��ȸ

		String id = request.getParameter("id");
		String phone = request.getParameter("phone");
		vo.setId(id);
		vo.setPhone(phone);
		// DB���� ��ȸ
		String pass = memberDao.findPass(vo);
		String result = "no";

		// ��ȸ ����� ������ "result�� ��Ƽ� ����
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
		// ���ǿ� ����� id���� �޾ƿ�
		String id = (String) session.getAttribute("id");
		// ���ǰ��� ������ logindo�� �̵�
		if (id == null) {
			session.setAttribute("msg", "�α��� �� �̿����ּ���.");
			return "redirect:/mall/login.do";
		}
		// id���� vo�� ����
		vo.setId(id);
		// id���� DB���� ��ȸ
		MemberVO result = memberDao.myPage(vo);
		// ��ȸ ����� ������ ���ǿ� ����
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

	// getPass update���� �Ѿ�� pass���� db�� ����� pass���� ��
	@ResponseBody
	@RequestMapping(value = "/mall/getPass.do", method = RequestMethod.POST)
	public String getPass(MemberVO vo, HttpSession session, HttpServletRequest request) {
		// pass���� �޾ƿ�
		String pass = request.getParameter("pass");
		// ���ǿ� ����� id���� �޾ƿ�
		String id = (String) session.getAttribute("id");
		vo.setPass(pass);
		vo.setId(id);
		// id���� pass���� DB���� ��ȸ
		int count = memberDao.getPass(vo);
		String result = "no";
		// ��ȸ count�� 1�̸� ���������� ok
		if (count == 1) {
			result = "ok";
			return result;
		} else {
			return result;
		}
	}

	// getPhone update���� �Ѿ�� phone���� db�� ����� phone���� ��
	@ResponseBody
	@RequestMapping(value = "/mall/getPhone.do", method = RequestMethod.POST)
	public String getPhone(MemberVO vo, HttpSession session, HttpServletRequest request) {
		// phone���� �޾ƿ�
		String phone = request.getParameter("phone");
		// ���ǿ� ����� id���� �޾ƿ�
		String id = (String) session.getAttribute("id");
		vo.setPhone(phone);
		// id���� phone���� DB���� ��ȸ
		int count = memberDao.phoneCheck(phone);
		vo.setId(id);
		int count2 = memberDao.getPhone(vo);
		String result = "no";
		// ��ȸ count�� 1�̰� count2�� 1�̸� ���������� ok
		if (count == 1 && count2 == 1) {
			result = "same";
			return result;
			// count�� 0�̸� �ߺ��� �ƴ����� ok
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
		// myPage���� phone,email,postcode,addr,detailaddr �� �޾ƿ���
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
		// DB���� update
		int result = memberDao.updateMember(vo);
		// update ������ ���ǿ� ����
		if (result == 1) {
			session.setAttribute("msg", "ȸ�������� �����Ǿ����ϴ�.");
			return "redirect:/mall/myPage.do";
		} else {
			return "redirect:/mall/myPage.do";
		}
	}

	// updatePass�� �̵�
	@RequestMapping("/mall/updatePassForm.do")
	public String updatePassForm(MemberVO vo, HttpSession session, HttpServletRequest request) {
		// session�� id�� ������ login.do�� �̵�
		String id = (String) session.getAttribute("id");
		if (id == null) {
			session.setAttribute("msg", "�α��� �� �̿����ּ���.");
			return "redirect:/mall/login.do";
		}
		// updatePass�� �̵�
		return "/updatePassForm";
	}

	// updatePass
	@RequestMapping("/mall/updatePass.do")
	public String updatePass(MemberVO vo, HttpSession session, HttpServletRequest request) {
		// id�� ������
		String id = (String) session.getAttribute("id");
		// pass�� ������
		String pass = request.getParameter("pass");
		vo.setId(id);
		vo.setPass(pass);
		// DB���� update
		int result = memberDao.updatePass(vo);
		// update ������ ���ǿ� ����
		if (result == 1) {
			session.setAttribute("msg", "��й�ȣ�� �����Ǿ����ϴ�.");
			return "redirect:/mall/myPage.do";
		} else {
			return "redirect:/mall/myPage.do";
		}
	}

	// deleteMemberForm���� �̵�
	@RequestMapping("/mall/deleteMemberForm.do")
	public String deleteMemberForm(MemberVO vo, HttpSession session, HttpServletRequest request) {
		// session�� id�� ������ login.do�� �̵�
		String id = (String) session.getAttribute("id");
		if (id == null) {
			session.setAttribute("msg", "�α��� �� �̿����ּ���.");
			return "redirect:/mall/login.do";
		}
		// deleteMemberForm���� �̵�
		return "/deleteMemberForm";
	}

	// deleteMember
	@RequestMapping("/mall/deleteMember.do")
	public String deleteMember(MemberVO vo, HttpSession session, HttpServletRequest request) {
		// id�� ������
		String id = (String) session.getAttribute("id");
		// �Է¹��� pass��
		String pass = request.getParameter("pass");
		vo.setPass(pass);
		vo.setId(id);

		this.cartDao.deleteCartByMember(id);
		// id�� ������ login.do�� �̵�
		if (id == null || pass == null) {
			session.setAttribute("msg", "�α��� �� �̿����ּ���.");
			return "redirect:/mall/login.do";
		}

		// �ֹ����� Ȯ��
		int result2 = orderDao.selectOrderListAllCountByDelete(id);
		// �ֹ� ������ ������ Ż�� ���� �޽���
		if (result2 > 0) {
			session.setAttribute("msg", "�ֹ������� �־� Ż�� �Ұ����մϴ�.");
			return "redirect:/mall/myPage.do";
		} else {
			// �ֹ� ������ ������ Ż�� ���� �޽���
			int result = memberDao.deleteMember(vo);
			if (result == 1) {
				session.setAttribute("msg", "Ż��Ǿ����ϴ�.");
				session.invalidate();
				return "redirect:/mall/main.do";
			} else {
				session.setAttribute("msg", "��й�ȣ�� Ʋ�Ƚ��ϴ�.");
				return "redirect:/mall/myPage.do";
			}
		}

	}

	// orderList.do ������ �̵�
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
			session.setAttribute("msg", "�α��� �� �̿����ּ���.");
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
