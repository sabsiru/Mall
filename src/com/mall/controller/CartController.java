package com.mall.controller;

import com.mall.VO.CartVO;
import com.mall.VO.ProductVO;
import com.mall.dao.CartDao;
import com.mall.dao.ProductDao;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
public class CartController {
    @SuppressWarnings("unused")
    private Logger log = Logger.getLogger(this.getClass());
    private int pageSize = 10;
    private int blockCount = 10;

    @Autowired
    private CartDao cartDao;
    @Autowired
    private ProductDao productDao;

    // ��ٱ��� �ֱ�
    @RequestMapping("/mall/insertCart.do")
    public String insertCart(HttpServletRequest request, Model model, CartVO vo) {
        String id = (String) request.getSession().getAttribute("id");
        int pno = Integer.parseInt(request.getParameter("pno"));
        int price = Integer.parseInt(request.getParameter("price"));
        String pname = request.getParameter("pname");
        String pimage = request.getParameter("pimage");
        int cartcount = Integer.parseInt(request.getParameter("cartcount"));
        String optionname = request.getParameter("optionname");

        int remain = Integer.parseInt(request.getParameter("remain"));
        System.out.println("remain = " + remain);

        vo.setId(id);
        vo.setPno(pno);
        vo.setPrice(price);
        vo.setPname(pname);
        vo.setPimage(pimage);
        vo.setCartcount(cartcount);
        vo.setOptionname(optionname);

        String data = "";

        int count = cartDao.CheckOption(vo);
        if (count == 1) {
            cartDao.updateCartCount2(vo);
        } else {
            cartDao.insertCart(vo);
        }
        // ���������� �ӹ���
        return "redirect:/mall/productDetail.do?pno=" + pno;
    }

    // ��ٱ��Ϸ� �̵�
    @RequestMapping("/mall/listCart.do")
    public ModelAndView cartList(HttpServletRequest request, Model model) {
        String id = (String) request.getSession().getAttribute("id");
        // ���ǿ� id�� ������� �α��� �������� �̵�
        if (id == null) {
            return new ModelAndView("redirect:/mall/login.do");
        }
        int count = cartDao.selectCartCount(id);
        ModelAndView mav = new ModelAndView();
        List<CartVO> cartList = null;
        if (count > 0) {
            cartList = this.cartDao.selectCartList(id);
        }

        //mav�� delinfo ������
        mav.setViewName("CartList");
        mav.addObject("cartList", cartList);
        return mav;
    }

    // ���� ����
    @RequestMapping("/mall/updateCart.do")
    @ResponseBody
    public String updateCart(HttpServletRequest request, Model model, CartVO vo) {
        int cartid = Integer.parseInt(request.getParameter("cartid"));
        int cartcount = Integer.parseInt(request.getParameter("cartcount"));
        vo.setCartid(cartid);
        vo.setCartcount(cartcount);

        cartDao.updateCartCount(vo);

        return "redirect:/mall/listCart.do";

    }

    // ��ٱ��� ����
    @RequestMapping("/mall/deleteCart.do")
    public String deleteCart(HttpServletRequest request, Model model) {
        // ajax�� cartid�� �޾ƿ�
        int cartid = Integer.parseInt(request.getParameter("cartid"));
        cartDao.deleteCart(cartid);
        return "redirect:/mall/listCart.do";

    }

    // order.do �������� cartList�� ������
    // ajax�� cartIdArray ���� �迭�� �޾ƿ� -> cartIdArray ���� cartList�� �ҷ���
    @RequestMapping("/mall/order.do")
    public ModelAndView order(HttpServletRequest request, Model model) {
        String id = (String) request.getSession().getAttribute("id");

        String[] check = request.getParameterValues("selectedItems");

        ModelAndView mav = new ModelAndView();

        Map<String, Object> cartIdArray2 = new HashMap<String, Object>();
        List<CartVO> cartList = null;
        cartIdArray2.put("cartIdArray", check);
        System.out.println("cartIdArray2 = " + cartIdArray2);
        cartList = this.cartDao.selectCart(cartIdArray2);
        System.out.println("cartList = " + cartList);

        mav.setViewName("Order");
        mav.addObject("cartList", cartList);

        return mav;
    }

}
