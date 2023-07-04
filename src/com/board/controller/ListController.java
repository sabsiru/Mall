package com.board.controller;

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
import com.board.paging.Paging;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

@Controller
public class ListController {
	/*
	 * @SuppressWarnings("unused") private Logger log =
	 * Logger.getLogger(getClass()); private int pageSize = 10; private int
	 * blockCount = 10;
	 * 
	 * @Autowired private BoardDao boardDao;
	 * 
	 * @RequestMapping({ "/board/list.do" }) public ModelAndView
	 * process(@RequestParam(value = "pageNum", defaultValue = "1") int currentPage,
	 * 
	 * @RequestParam(value = "keyField", defaultValue = "") String keyField,
	 * 
	 * @RequestParam(value = "keyWord", defaultValue = "") String keyWord) { String
	 * pagingHtml = "";
	 * 
	 * @SuppressWarnings({ "unchecked", "rawtypes" }) HashMap<String, Object> map =
	 * new HashMap(); map.put("keyField", keyField); map.put("keyWord", keyWord);
	 * 
	 * int count = this.boardDao.getCount(map);
	 * 
	 * Paging page = new Paging(keyField, keyWord, currentPage, count,
	 * this.pageSize, this.blockCount, "list.do");
	 * 
	 * pagingHtml = page.getPagingHtml().toString();
	 * 
	 * map.put("startCount", Integer.valueOf(page.getStartCount()));
	 * map.put("endCount", Integer.valueOf(page.getEndCount()));
	 * 
	 * List<BoardVO> list = null; if (count > 0) { list = this.boardDao.list(map); }
	 * else { list = Collections.emptyList(); } int number = count - (currentPage -
	 * 1) * this.pageSize;
	 * 
	 * ModelAndView mav = new ModelAndView(); mav.setViewName("boardList");
	 * mav.addObject("count", Integer.valueOf(count)); mav.addObject("currentPage",
	 * Integer.valueOf(currentPage)); mav.addObject("list", list);
	 * mav.addObject("pagingHtml", pagingHtml); mav.addObject("number",
	 * Integer.valueOf(number));
	 * 
	 * return mav; }
	 * 
	 * // �� ���� ������ �̵�
	 * 
	 * @RequestMapping({ "/board/write.do" }) public ModelAndView process() {
	 * System.out.println("write form"); ModelAndView mav = new ModelAndView();
	 * mav.setViewName("write");
	 * 
	 * return mav; }
	 * 
	 * 
	 * // �� ����
	 * 
	 * @RequestMapping("/board/insert.do") public String write(BoardVO vo, Model m)
	 * { try { this.boardDao.insert(vo); m.addAttribute("msg", "WRT_OK"); return
	 * "redirect:/board/list.do";
	 * 
	 * } catch (Exception e) { e.printStackTrace(); return "/write"; } }
	 * 
	 * // �� ����
	 * 
	 * @RequestMapping("/board/insert.do") public String write(HttpServletRequest
	 * request) throws IOException { System.out.println("�� ���");
	 * 
	 * String savePath = request.getServletContext().getRealPath("upload");
	 * System.out.println("request"); int sizeLimit = 1024 * 1024 * 15;
	 * request.setCharacterEncoding("utf-8");
	 * 
	 * MultipartRequest multi = new MultipartRequest(request, savePath, sizeLimit,
	 * "utf-8", new DefaultFileRenamePolicy());
	 * 
	 * String name = multi.getParameter("name"); System.out.println(name); String
	 * title = multi.getParameter("title"); String content =
	 * multi.getParameter("content"); String pass = multi.getParameter("pass");
	 * String orgfile = multi.getOriginalFileName("orgfile"); String storefile =
	 * multi.getFilesystemName("orgfile");
	 * 
	 * BoardVO vo = new BoardVO();
	 * 
	 * vo.setName(name); vo.setTitle(title); vo.setContent(content);
	 * vo.setPass(pass); vo.setOrgfile(orgfile); vo.setStorefile(storefile);
	 * 
	 * this.boardDao.insert(vo); return "redirect:/board/list.do";
	 * 
	 * }
	 * 
	 * // �Խ��� �б�
	 * 
	 * @RequestMapping("/board/read.do") public ModelAndView read(int seq) { //
	 * boardDao.read BoardVO vo = this.boardDao.read(seq);
	 * this.boardDao.hitIncrease(seq);
	 * 
	 * ModelAndView mav = new ModelAndView();
	 * 
	 * mav.setViewName("read"); mav.addObject("vo", vo);
	 * 
	 * return mav; }
	 * 
	 * // ���� �ٿ�ε�
	 * 
	 * @RequestMapping("/board/download.do") public void download(HttpServletRequest
	 * request, HttpServletResponse response) throws IOException {
	 * System.out.println("download");
	 * response.setContentType("application/octet-stream");
	 * 
	 * String savePath = request.getServletContext().getRealPath("upload"); String
	 * orgfile = request.getParameter("orgfile"); String storefile =
	 * request.getParameter("storefile"); System.out.println("orgfile " + orgfile);
	 * System.out.println("storefile " + storefile); String fullPath = savePath +
	 * "/" + storefile;
	 * 
	 * // �ٿ������ �ѱ� ���� ���� ���ڵ� CharsetEncoder encoder =
	 * Charset.forName("ISO-8859-1").newEncoder(); String encodedOrgFile =
	 * encoder.canEncode(orgfile) ? orgfile : new
	 * String(orgfile.getBytes(StandardCharsets.UTF_8), "ISO-8859-1");
	 * 
	 * response.setHeader("Content-Disposition", "attachment; filename=\"" +
	 * encodedOrgFile + "\";");
	 * 
	 * byte[] b = new byte[4096]; FileInputStream fis = new
	 * FileInputStream(fullPath); ServletOutputStream out =
	 * response.getOutputStream();
	 * 
	 * int numRead; while ((numRead = fis.read(b, 0, b.length)) != -1) {
	 * out.write(b, 0, numRead); } out.flush(); out.close(); fis.close(); }
	 * 
	 * // ��й�ȣ Ȯ��
	 * 
	 * @RequestMapping(value = "/board/passCheck.do", method = RequestMethod.POST)
	 * 
	 * @ResponseBody public String passCheck(BoardVO vo, HttpSession session) {
	 * String data = ""; String pass = vo.getPass(); int seq = vo.getSeq(); BoardVO
	 * vo2 = this.boardDao.read(seq); String pass2 = vo2.getPass(); if
	 * (pass.equals(pass2)) { data = "true"; session.setAttribute("pass", pass); }
	 * else if (pass == "") { data = "empty"; } else { data = "no"; } return data; }
	 * 
	 * // ��й�ȣ Ȯ���� �ٷ� ����(��� ����)
	 * 
	 * @RequestMapping(value = "/board/deletePass.do", method = RequestMethod.POST)
	 * 
	 * @ResponseBody public String deletePass(BoardVO vo, HttpSession session) {
	 * String data = ""; String pass = vo.getPass(); int seq = vo.getSeq(); BoardVO
	 * vo2 = this.boardDao.read(seq); String pass2 = vo2.getPass(); if
	 * (pass.equals(pass2)) { this.boardDao.delete(seq); data = "true";
	 * session.setAttribute("pass", pass); } else if (pass == "") { data = "empty";
	 * } else { data = "no"; } return data; }
	 * 
	 * // �� ������ �����ϸ� �ش� �÷��� ÷�����ϵ� ���� ����
	 * 
	 * @RequestMapping("/board/delete.do") public String deleteFile(int seq,
	 * HttpServletRequest request) { System.out.println("deleteFile"); BoardVO vo =
	 * this.boardDao.read(seq); String savePath =
	 * request.getServletContext().getRealPath("upload"); String fullPath = savePath
	 * + "/" + vo.getStorefile(); File file = new File(fullPath); if (file.exists()
	 * == true) { file.delete(); } this.boardDao.delete(seq); return
	 * "redirect:/board/list.do"; }
	 * 
	 * // ��й�ȣ Ȯ���� ���������� �̵��ϴ� modifyPass.do(������)
	 * 
	 * @RequestMapping(value = "/board/modifyPass.do", method = RequestMethod.POST)
	 * 
	 * @ResponseBody public String modifyPass(BoardVO vo, HttpSession session) {
	 * String data = ""; String pass = vo.getPass(); int seq = vo.getSeq(); BoardVO
	 * vo2 = this.boardDao.read(seq); String pass2 = vo2.getPass(); if
	 * (pass.equals(pass2)) { data = "true"; session.setAttribute("pass", pass); }
	 * else if (pass == "") { data = "empty"; } else { data = "no"; } return data; }
	 * 
	 * // ���� ������ �̵�
	 * 
	 * @RequestMapping("/board/modify.do") public ModelAndView modify(int seq) {
	 * BoardVO vo = this.boardDao.read(seq);
	 * 
	 * ModelAndView mav = new ModelAndView(); mav.setViewName("modify");
	 * mav.addObject("vo", vo);
	 * 
	 * return mav;
	 * 
	 * }
	 * 
	 * // �� ���� �� ÷������ ���� �� ÷������ ����
	 * 
	 * @RequestMapping("/board/update.do") public String update(HttpServletRequest
	 * request) throws IOException { System.out.println("�� ����");
	 * 
	 * String savePath = request.getServletContext().getRealPath("upload"); int
	 * sizeLimit = 1024 * 1024 * 15; request.setCharacterEncoding("utf-8");
	 * 
	 * MultipartRequest multi = new MultipartRequest(request, savePath, sizeLimit,
	 * "utf-8", new DefaultFileRenamePolicy());
	 * 
	 * int seq = Integer.parseInt(multi.getParameter("seq")); String title =
	 * multi.getParameter("title"); String content = multi.getParameter("content");
	 * String orgfile = multi.getOriginalFileName("orgfile"); String storefile =
	 * multi.getFilesystemName("orgfile");
	 * 
	 * BoardVO vo = this.boardDao.read(seq);
	 * 
	 * vo.setSeq(seq); vo.setTitle(title); vo.setContent(content);
	 * System.out.println("seq" + seq + "title" + title + "content" + content);
	 * 
	 * // ���ο� ������ �ִ��� Ȯ�� if (orgfile != null && storefile != null) { // ������ ������ ����
	 * ���� String existingStorefile = vo.getStorefile(); System.out.println("exit " +
	 * existingStorefile); if (existingStorefile != null) { String filePath =
	 * savePath + "/" + existingStorefile; File file = new File(filePath); if
	 * (file.exists()) { file.delete(); } } // ���ο� ÷�����ϸ� ��� vo.setOrgfile(orgfile);
	 * vo.setStorefile(storefile);
	 * 
	 * } // ÷������ ���� ��ư�� ������ �������� delete�� �ް� ���� String confirmdelete =
	 * multi.getParameter("confirmdelete"); if (confirmdelete != null &&
	 * confirmdelete.equals("delete")) {
	 * 
	 * String existingStorefile = vo.getStorefile(); System.out.println("exit " +
	 * existingStorefile); if (existingStorefile != null) { String filePath =
	 * savePath + "/" + existingStorefile; File file = new File(filePath); if
	 * (file.exists()) { file.delete(); } }
	 * 
	 * vo.setOrgfile(null); vo.setStorefile(null); } else {
	 * 
	 * vo.setOrgfile(vo.getOrgfile()); vo.setStorefile(vo.getStorefile()); String
	 * existingStorefile = vo.getStorefile(); System.out.println("org" + orgfile +
	 * ", store " + storefile); }
	 * 
	 * this.boardDao.modify(vo); return "redirect:/board/list.do"; }
	 * 
	 * // �� �������� filedelete ��ư�� ������ ������ ����� ������ �����ϰ� ÷������ ������ �����.
	 * 
	 * @RequestMapping("/board/filedelete.do") public String filedelete(int seq,
	 * String storefile, HttpServletRequest request) { String savePath =
	 * request.getServletContext().getRealPath("upload"); String fullPath = savePath
	 * + "/" + storefile; File file = new File(fullPath); if (file.exists()) {
	 * file.delete(); } // this.boardDao.filedelete(seq); return
	 * "redirect:/board/modify.do?seq=" + seq; }
	 * 
	 * // ��� ���� ������ �̵�
	 * 
	 * @RequestMapping("/board/reply.do") public ModelAndView
	 * process2(@RequestParam("seq") int seq) { System.out.println("reply form");
	 * BoardVO vo = this.boardDao.read(seq); ModelAndView mav = new ModelAndView();
	 * mav.setViewName("reply"); mav.addObject("vo", vo);
	 * System.out.println("title = " + vo.getTitle());
	 * 
	 * return mav; }
	 * 
	 * // ��� �ۼ�
	 * 
	 * @RequestMapping("/board/replay.do") public String replay(BoardVO vo,
	 * HttpServletRequest request) throws Exception { BoardVO parent = vo; String
	 * savePath = request.getServletContext().getRealPath("upload");
	 * System.out.println("request"); int sizeLimit = 1024 * 1024 * 15;
	 * request.setCharacterEncoding("utf-8");
	 * 
	 * MultipartRequest multi = new MultipartRequest(request, savePath, sizeLimit,
	 * "utf-8", new DefaultFileRenamePolicy());
	 * 
	 * int ref = Integer.parseInt(multi.getParameter("ref")); int step =
	 * Integer.parseInt(multi.getParameter("step")); int depth =
	 * Integer.parseInt(multi.getParameter("depth")); int seq =
	 * Integer.parseInt(multi.getParameter("seq"));
	 * 
	 * 
	 * 
	 * // ref���� seq���� ������ �θ���� ����̱� ������ step, depth�� 1�� �ο��ϰ� replyUpdate�� �Ѵ�.
	 * if(ref==seq) { vo.setRef(ref); step = 1; depth = 1; vo.setStep(step);
	 * this.boardDao.replyUpdate(vo); vo.setDepth(depth); }
	 * 
	 * //ref���� seq���� �ٸ��� replyUpdate�� �����ϰ� step�� depth�� ���� 1�� ������Ų��. else {
	 * vo.setRef(ref); vo.setStep(step); vo.setDepth(depth);
	 * this.boardDao.replyUpdate(vo); depth++; step++;
	 * 
	 * 
	 * } vo.setStep(step); vo.setDepth(depth); System.out.println(step);
	 * System.out.println(depth);
	 * 
	 * String name = multi.getParameter("name"); String title =
	 * multi.getParameter("title"); String content = multi.getParameter("content");
	 * String pass = multi.getParameter("pass"); String orgfile =
	 * multi.getOriginalFileName("orgfile"); String storefile =
	 * multi.getFilesystemName("orgfile");
	 * 
	 * vo.setName(name); vo.setTitle(title); vo.setContent(content);
	 * vo.setPass(pass); vo.setOrgfile(orgfile); vo.setStorefile(storefile);
	 * 
	 * 
	 * 
	 * this.boardDao.reply(vo);
	 * 
	 * return "redirect:/board/list.do"; }
	 */
}
