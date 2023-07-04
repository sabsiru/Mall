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
	 * // 글 쓰기 폼으로 이동
	 * 
	 * @RequestMapping({ "/board/write.do" }) public ModelAndView process() {
	 * System.out.println("write form"); ModelAndView mav = new ModelAndView();
	 * mav.setViewName("write");
	 * 
	 * return mav; }
	 * 
	 * 
	 * // 글 쓰기
	 * 
	 * @RequestMapping("/board/insert.do") public String write(BoardVO vo, Model m)
	 * { try { this.boardDao.insert(vo); m.addAttribute("msg", "WRT_OK"); return
	 * "redirect:/board/list.do";
	 * 
	 * } catch (Exception e) { e.printStackTrace(); return "/write"; } }
	 * 
	 * // 글 쓰기
	 * 
	 * @RequestMapping("/board/insert.do") public String write(HttpServletRequest
	 * request) throws IOException { System.out.println("글 등록");
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
	 * // 게시판 읽기
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
	 * // 파일 다운로드
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
	 * // 다운받을때 한글 깨짐 방지 인코딩 CharsetEncoder encoder =
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
	 * // 비밀번호 확인
	 * 
	 * @RequestMapping(value = "/board/passCheck.do", method = RequestMethod.POST)
	 * 
	 * @ResponseBody public String passCheck(BoardVO vo, HttpSession session) {
	 * String data = ""; String pass = vo.getPass(); int seq = vo.getSeq(); BoardVO
	 * vo2 = this.boardDao.read(seq); String pass2 = vo2.getPass(); if
	 * (pass.equals(pass2)) { data = "true"; session.setAttribute("pass", pass); }
	 * else if (pass == "") { data = "empty"; } else { data = "no"; } return data; }
	 * 
	 * // 비밀번호 확인후 바로 삭제(사용 안함)
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
	 * // 글 삭제를 실행하면 해당 컬럼의 첨부파일도 같이 삭제
	 * 
	 * @RequestMapping("/board/delete.do") public String deleteFile(int seq,
	 * HttpServletRequest request) { System.out.println("deleteFile"); BoardVO vo =
	 * this.boardDao.read(seq); String savePath =
	 * request.getServletContext().getRealPath("upload"); String fullPath = savePath
	 * + "/" + vo.getStorefile(); File file = new File(fullPath); if (file.exists()
	 * == true) { file.delete(); } this.boardDao.delete(seq); return
	 * "redirect:/board/list.do"; }
	 * 
	 * // 비밀번호 확인후 수정폼으로 이동하는 modifyPass.do(사용안함)
	 * 
	 * @RequestMapping(value = "/board/modifyPass.do", method = RequestMethod.POST)
	 * 
	 * @ResponseBody public String modifyPass(BoardVO vo, HttpSession session) {
	 * String data = ""; String pass = vo.getPass(); int seq = vo.getSeq(); BoardVO
	 * vo2 = this.boardDao.read(seq); String pass2 = vo2.getPass(); if
	 * (pass.equals(pass2)) { data = "true"; session.setAttribute("pass", pass); }
	 * else if (pass == "") { data = "empty"; } else { data = "no"; } return data; }
	 * 
	 * // 수정 폼으로 이동
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
	 * // 글 수정 및 첨부파일 삭제 및 첨부파일 수정
	 * 
	 * @RequestMapping("/board/update.do") public String update(HttpServletRequest
	 * request) throws IOException { System.out.println("글 수정");
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
	 * // 새로운 파일이 있는지 확인 if (orgfile != null && storefile != null) { // 있으면 기존의 파일
	 * 삭제 String existingStorefile = vo.getStorefile(); System.out.println("exit " +
	 * existingStorefile); if (existingStorefile != null) { String filePath =
	 * savePath + "/" + existingStorefile; File file = new File(filePath); if
	 * (file.exists()) { file.delete(); } } // 새로운 첨부파일명 등록 vo.setOrgfile(orgfile);
	 * vo.setStorefile(storefile);
	 * 
	 * } // 첨부파일 삭제 버튼을 누르면 보내지는 delete를 받고 삭제 String confirmdelete =
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
	 * // 글 수정에서 filedelete 버튼을 누르면 지정된 경로의 파일을 삭제하고 첨부파일 정보를 지운다.
	 * 
	 * @RequestMapping("/board/filedelete.do") public String filedelete(int seq,
	 * String storefile, HttpServletRequest request) { String savePath =
	 * request.getServletContext().getRealPath("upload"); String fullPath = savePath
	 * + "/" + storefile; File file = new File(fullPath); if (file.exists()) {
	 * file.delete(); } // this.boardDao.filedelete(seq); return
	 * "redirect:/board/modify.do?seq=" + seq; }
	 * 
	 * // 답글 쓰기 폼으로 이동
	 * 
	 * @RequestMapping("/board/reply.do") public ModelAndView
	 * process2(@RequestParam("seq") int seq) { System.out.println("reply form");
	 * BoardVO vo = this.boardDao.read(seq); ModelAndView mav = new ModelAndView();
	 * mav.setViewName("reply"); mav.addObject("vo", vo);
	 * System.out.println("title = " + vo.getTitle());
	 * 
	 * return mav; }
	 * 
	 * // 답글 작성
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
	 * // ref값과 seq값이 같으면 부모글의 답글이기 때문에 step, depth는 1을 부여하고 replyUpdate를 한다.
	 * if(ref==seq) { vo.setRef(ref); step = 1; depth = 1; vo.setStep(step);
	 * this.boardDao.replyUpdate(vo); vo.setDepth(depth); }
	 * 
	 * //ref값과 seq값이 다르면 replyUpdate를 실행하고 step과 depth의 값을 1씩 증가시킨다. else {
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
