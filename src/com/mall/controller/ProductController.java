package com.mall.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mall.VO.ProductVO;
import com.mall.dao.ProductDao;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.board.VO.BoardVO;
import com.board.dao.BoardDao;
import com.mall.paging.Paging;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

@Controller
public class ProductController {
	@SuppressWarnings("unused")
	private Logger log = Logger.getLogger(this.getClass());
	private int pageSize = 10;
	private int blockCount = 10;

	@Autowired
	private ProductDao productDao;

	@RequestMapping({ "/mall/main.do" })
	public ModelAndView process(@RequestParam(value = "pageNum", defaultValue = "1") int currentPage,
			@RequestParam(value = "keyField", defaultValue = "") String keyField,
			@RequestParam(value = "keyWord", defaultValue = "") String keyWord) {
		String pagingHtml = "";
		@SuppressWarnings({ "unchecked", "rawtypes" })
		HashMap<String, Object> map = new HashMap();
		map.put("keyField", keyField);
		map.put("keyWord", keyWord);

		int count = this.productDao.productCount(map);

		Paging page = new Paging(keyField, keyWord, currentPage, count, this.pageSize, this.blockCount, "main.do");

		pagingHtml = page.getPagingHtml().toString();

		map.put("startCount", Integer.valueOf(page.getStartCount()));
		map.put("endCount", Integer.valueOf(page.getEndCount()));
		System.out.println("count=" + count);
		List<ProductVO> productList = null;
		if (count > 0) {
			productList = this.productDao.productList(map);
		} else {
			productList = Collections.emptyList();
		}
		int number = count - (currentPage - 1) * this.pageSize;

		ModelAndView mav = new ModelAndView();
		mav.setViewName("Main");
		mav.addObject("count", Integer.valueOf(count));
		mav.addObject("currentPage", Integer.valueOf(currentPage));
		mav.addObject("productList", productList);
		mav.addObject("pagingHtml", pagingHtml);
		mav.addObject("number", Integer.valueOf(number));

		return mav;
	}

	// productDetail
	@RequestMapping({ "/mall/productDetail.do" })
	public ModelAndView productDetail(ProductVO vo, HttpSession session, HttpServletRequest request) {
		// pno값 가져오기
		int pno = Integer.parseInt(request.getParameter("pno"));
		vo.setPno(pno);
		// 상품 읽기
		ProductVO prVO = this.productDao.productDetail(pno);

		// 옵션카운트가 1보다 클 경우 옵션리스트 가져오기
		int optionCount = this.productDao.optionCount(pno);
		List<ProductVO> optionList = null;
		if (optionCount > 0) {
			optionList = this.productDao.optionList(pno);
		} else {
			optionList = Collections.emptyList();
		}
		// 상품이미지 가져오기

		ModelAndView mav = new ModelAndView();
		mav.setViewName("ProductDetail");
		mav.addObject("prVO", prVO);
		mav.addObject("optionList", optionList);

		return mav;

	}

}
