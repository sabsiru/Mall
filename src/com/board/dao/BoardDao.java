package com.board.dao;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.board.VO.BoardVO;

public abstract interface BoardDao {

	public abstract List<BoardVO> list(Map<String, Object> paramMap);

	public abstract int getCount(Map<String, Object> paramMap);

	public abstract int getSeq();

	// 글 쓰기
	public abstract int insert(BoardVO vo);

	// 글 읽기
	public abstract BoardVO read(Integer seq);

	// 파일 읽기
	public abstract List<BoardVO> selectFile(int seq);

	// 조회수 증가
	public abstract int hitIncrease(Integer seq);

	// 글 삭제
	public abstract int delete(Integer seq);

	// 파일 삭제
	public abstract int fileDelete(Integer seq);
	//파일 수정시 삭제
	public abstract List<BoardVO> fileModifyDelete(Map<String, Object> paramMap);
	// 글 수정
	public abstract int modify(BoardVO vo);

	// 답글
	public abstract int reply(BoardVO vo) throws Exception;

	// 게시글 순서 업데이트
	public abstract int replyUpdate(BoardVO vo);

	// 파일 업로드
	public abstract int fileUpload(BoardVO vo);

}
