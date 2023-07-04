package com.mall.controller;

import com.mall.VO.CartVO;
import com.mall.VO.OrderVO;
import com.mall.VO.ProductVO;
import com.mall.dao.CartDao;
import com.mall.dao.OrderDao;
import com.mall.dao.ProductDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Random;

@Controller
public class OrderController {
    @Autowired
    OrderDao orderDao;
    @Autowired
    CartDao cartDao;
    @Autowired
    ProductDao productDao;


    @RequestMapping(value = "/mall/insertOrder.do", method = RequestMethod.POST)
    public String insertOrder(HttpServletRequest request, HttpSession session) {
        String id = (String) session.getAttribute("id");
        if(id == null){
            session.setAttribute("msg","로그인 후 이용해주세요.");
            return "redirect:/mall/login.do";
        }
        String name = (String) session.getAttribute("name");
        String phone = (String) session.getAttribute("phone");
        String addr = (String) session.getAttribute("addr") + " " + (String) session.getAttribute("detailaddr");
        OrderVO vo = new OrderVO();
        System.out.println("id = " + id);
        System.out.println("name = " + name);
        System.out.println("phone = " + phone);
        System.out.println("addr = " + addr);

        vo.setId(id);
        vo.setName(name);
        vo.setPhone(phone);
        vo.setAddr(addr);
        String[] cartArray = request.getParameterValues("cartid");
        int size = cartArray.length;
        //현재날짜를 int값으로 만들기
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String formattedDate = currentDate.format(formatter);

        Random random = new Random();
        //1번만 random값 1번만 실행


        int randomNumber = random.nextInt(9000) + 1000;

        String orderNumber = formattedDate + randomNumber;
        vo.setOrderidseq(orderNumber);
        System.out.println("orderNumber = " + orderNumber);
        int remain=0;
        for (int i = 0; i < size; i++) {
            int cartid = Integer.parseInt(cartArray[i]);
            System.out.println("cartid2 = " + cartid);
            //cartid2로 상품 정보를 가져온다
            CartVO cVo = cartDao.selectCartOrder(cartid);
            System.out.println("cVo = " + cVo);
            vo.setPno(cVo.getPno());
            vo.setPname(cVo.getPname());
            vo.setPimage(cVo.getPimage());
            vo.setCartid(cartid);
            String optionname = cVo.getOptionname();
            System.out.println("optionname = " + optionname);
            //optionvalue을 -로 구분 했을때 사이즈가 1이면 optionvalue1만 있고 2이면 optionvalue1,2가 있다
            String[] optionArray = optionname.split("-");
            System.out.println("optionArray = " + Arrays.toString(optionArray));
            ProductVO pVo = new ProductVO();
            if(optionArray.length <2){
                String optionvalue1 = optionArray[0];
                System.out.println("optionvalue1 = " + optionvalue1);
                pVo.setOptionvalue1(optionvalue1);
                pVo.setOptionvalue2("nooption2");

            }
            else{
                String optionvalue1 = optionArray[0];
                String optionvalue2 = optionArray[1];
                pVo.setOptionvalue1(optionvalue1);
                pVo.setOptionvalue2(optionvalue2);

            }
            String pname = cVo.getPname();
            System.out.println("pname = " + pname);
            int cartcount = cVo.getCartcount();
            System.out.println("cartcount = " + cartcount);
            //재고확인
            pVo.setPno(cVo.getPno());
            remain=productDao.checkRemain(pVo);

            //재고가 없으면 msg에 재고가 없습니다를 담아서 리턴
            //cartcount와 remain 비교
            if (remain == 0 || cartcount > remain) {
                session.setAttribute("msg", "주문 실패 상품" + pname + "의 옵션" + optionname + "이/가 품절입니다.");
                return "redirect:/mall/main.do";
            } else {
                session.setAttribute("msg", "주문 성공.");

                System.out.println("value2="+pVo.getOptionvalue2());
                pVo.setPno(cVo.getPno());
                pVo.setCartcount(cVo.getCartcount());
                productDao.updateRemainCart(pVo);




                cVo.setOptionname(optionname);
                cVo.setCartcount(cVo.getCartcount());
                cVo.setPno(cVo.getPno());
                cartDao.updateCartRemain(cVo);
                cartDao.updateCountAfterOrder(cVo);

                vo.setOptionname(cVo.getOptionname());
                vo.setStockcount(cVo.getCartcount());
                orderDao.insertOrder(vo);
                cartDao.deleteCart(cartid);
            }
        }

        return "redirect:/mall/main.do";
    }

    //directOrder.do로 이동
    @RequestMapping(value = "/mall/directOrder.do", method = RequestMethod.POST)
    public ModelAndView directOrder(
            @RequestParam("pno") int pno,
            @RequestParam("price") double price,
            @RequestParam("pname") String pname,
            @RequestParam("pimage") String pimage,
            @RequestParam("optionname") String optionname,
            @RequestParam("remain") int remain,
            @RequestParam("count") int count, HttpSession session) {

        String id = (String) session.getAttribute("id");
        if(id == null){
            session.setAttribute("msg","로그인 후 이용해주세요.");
            return new ModelAndView("redirect:/mall/login.do");
        }
        String name = (String) session.getAttribute("name");
        String phone = (String) session.getAttribute("phone");
        String addr = (String) session.getAttribute("addr") + " " + (String) session.getAttribute("detailaddr");
        OrderVO vo = new OrderVO();
        System.out.println("id = " + id);
        System.out.println("name = " + name);
        System.out.println("phone = " + phone);
        System.out.println("addr = " + addr);

        vo.setId(id);
        vo.setName(name);
        vo.setPhone(phone);
        vo.setAddr(addr);
        String[] optionArray = optionname.split("-");
        System.out.println("optionArray = " + Arrays.toString(optionArray));
        ProductVO pVo = new ProductVO();
        if(optionArray.length <2){
            String optionvalue1 = optionArray[0];
            System.out.println("optionvalue1 = " + optionvalue1);
            pVo.setOptionvalue1(optionvalue1);
            pVo.setOptionvalue2("nooption2");

        }
        else{
            String optionvalue1 = optionArray[0];
            String optionvalue2 = optionArray[1];
            pVo.setOptionvalue1(optionvalue1);
            pVo.setOptionvalue2(optionvalue2);
        }
        //pno와 optionvalue1,2로 상품 정보를 가져온다.
        pVo.setPno(pno);
//        this.productDao.getPoption(pVo);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("DirectOrder");
        mav.addObject("pno", pno);
        mav.addObject("price", price);
        mav.addObject("pname", pname);
        mav.addObject("pimage", pimage);
        mav.addObject("optionname", optionname);
        mav.addObject("remain", remain);
        mav.addObject("count", count);

        return mav;
    }

    //insertDirectOrder.do 실행
    @RequestMapping(value = "/mall/insertDirectOrder.do", method = RequestMethod.POST)
    public String insertDirectOrder(HttpServletRequest request, HttpSession session) {
        //pno가져오기
        int pno = Integer.parseInt(request.getParameter("pno"));
        String id = (String) session.getAttribute("id");
        if(id==null){
            session.setAttribute("msg", "로그인 후 이용해주세요.");
            return "redirect:/mall/main.do";
        }
        String name = (String) session.getAttribute("name");
        String phone = (String) session.getAttribute("phone");
        String addr = (String) session.getAttribute("addr") + " " + (String) session.getAttribute("detailaddr");
        //폼에서보내온 상품정보
        String optionname = request.getParameter("optionname");
        String pname = request.getParameter("pname");
        String pimage = request.getParameter("pimage");
        int cartid=0;
        int stockcount=Integer.parseInt(request.getParameter("cartcount"));

        System.out.println("id = " + id);
        System.out.println("name = " + name);
        System.out.println("phone = " + phone);
        System.out.println("addr = " + addr);
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String formattedDate = currentDate.format(formatter);

        Random random = new Random();
        int randomNumber = random.nextInt(9000) + 1000;

        OrderVO vo = new OrderVO();
        ProductVO pVo = new ProductVO();
        CartVO cVo=new CartVO();

        //주문번호
        String orderNumber = formattedDate + randomNumber;
        int length=optionname.split("-").length;
        if(length<2){
            String optionvalue1 = optionname.split("-")[0];
            String optionvalue2 = "nooption2";
            pVo.setOptionvalue1(optionvalue1);
            pVo.setOptionvalue2(optionvalue2);
        }
        else{
            String optionvalue1 = optionname.split("-")[0];
            String optionvalue2 = optionname.split("-")[1];
            pVo.setOptionvalue1(optionvalue1);
            pVo.setOptionvalue2(optionvalue2);
        }

        vo.setId(id);
        vo.setName(name);
        vo.setPhone(phone);
        vo.setAddr(addr);
        vo.setPno(pno);
        vo.setPname(pname);
        vo.setPimage(pimage);
        vo.setCartid(cartid);
        vo.setOptionname(optionname);
        vo.setStockcount(stockcount);
        vo.setOrderidseq(orderNumber);

        //재고 확인
        pVo.setPno(pno);
        pVo.setCartcount(stockcount);
        int remain = productDao.checkRemain(pVo);
        //재고가 없거나 재고보다 많은 수량을 주문하면 msg에 재고가 없습니다를 담아서 상품상세페이지로 이동
        if (remain == 0 || stockcount > remain) {
            session.setAttribute("msg", "주문 실패 상품" + pname + "의 옵션" + optionname + "이/가 품절입니다.");
            return "redirect:/mall/productDetail.do?pno="+ pno;
        } else {
            session.setAttribute("msg", "주문 성공.");
            productDao.updateRemainCart(pVo);

            cVo.setOptionname(optionname);
            cVo.setCartcount(stockcount);
            cVo.setPno(pno);
            cartDao.updateCartRemain(cVo);
            cartDao.updateCountAfterOrder(cVo);


            orderDao.insertOrder(vo);
            cartDao.deleteCart(cartid);
        }
        return "redirect:/mall/main.do";
    }
    //updateRefundRequest
    @RequestMapping(value = "/mall/refundRequest.do", method = RequestMethod.POST)
    public String updateRefundRequest(HttpServletRequest request, HttpSession session) {
        String id = (String) session.getAttribute("id");
        if(id==null){
            session.setAttribute("msg", "로그인 후 이용해주세요.");
            return "redirect:/mall/main.do";
        }
        String orderidseq = request.getParameter("orderidseq");
        String pname=request.getParameter("pname");
        String optionname=request.getParameter("optionname");

        System.out.println("orderidseq = " + orderidseq);
        OrderVO vo = new OrderVO();
        vo.setId(id);
        vo.setPname(pname);
        vo.setOptionname(optionname);
        vo.setOrderidseq(orderidseq);
        orderDao.updateRefundRequest(vo);
        return "redirect:/mall/myPage.do";
    }
    //orderConfirm
    @RequestMapping(value = "/mall/orderConfirm.do", method = RequestMethod.POST)
    public String orderConfirm(HttpServletRequest request, HttpSession session) {
        String id = (String) session.getAttribute("id");
        if(id==null){
            session.setAttribute("msg", "로그인 후 이용해주세요.");
            return "redirect:/mall/main.do";
        }
        String orderidseq = request.getParameter("orderidseq");
        String pname=request.getParameter("pname");
        String optionname=request.getParameter("optionname");
        System.out.println("orderidseq = " + orderidseq);
        OrderVO vo = new OrderVO();
        vo.setId(id);
        vo.setPname(pname);
        vo.setOptionname(optionname);
        vo.setOrderidseq(orderidseq);

        orderDao.orderConfirm(vo);
        return "redirect:/mall/myPage.do";
    }
    //cancelRequest
    @RequestMapping(value = "/mall/cancelRequest.do", method = RequestMethod.POST)
    public String cancelRequest(HttpServletRequest request, HttpSession session) {
        String id = (String) session.getAttribute("id");
        if(id==null){
            session.setAttribute("msg", "로그인 후 이용해주세요.");
            return "redirect:/mall/main.do";
        }
        String orderidseq = request.getParameter("orderidseq");
        String pname=request.getParameter("pname");
        String optionname=request.getParameter("optionname");
        System.out.println("orderidseq = " + orderidseq);
        OrderVO vo = new OrderVO();
        vo.setId(id);
        vo.setPname(pname);
        vo.setOptionname(optionname);
        vo.setOrderidseq(orderidseq);
        orderDao.cancelRequest(vo);
        return "redirect:/mall/myPage.do";
    }
}
