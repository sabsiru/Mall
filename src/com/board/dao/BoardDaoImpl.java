package com.board.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.board.VO.BoardVO;

@Component
public class BoardDaoImpl extends SqlSessionDaoSupport implements BoardDao {
	 //----------------------x-platform---------------------//


    //----------------------x-platform---------------------//

	public List<BoardVO> list(Map<String, Object> map) {
		List<BoardVO> list = getSqlSession().selectList("boardList", map);
		return list;
	}

	public int getCount(Map<String, Object> map) {
		return ((Integer) getSqlSession().selectOne("boardCount", map)).intValue();
	}

	// 글 쓰기
	@Override
	public int insert(BoardVO vo) {
		return getSqlSession().insert("insert", vo);
	}

	// 글 읽기
	@Override
	public BoardVO read(Integer seq) {
		return getSqlSession().selectOne("select", seq);
	}
	public List<BoardVO> selectFile(int seq) {
		List<BoardVO> selectFile = getSqlSession().selectList("selectFile", seq);
		return selectFile;
	}
	
	// 글 삭제
	@Override
	public int delete(Integer seq) {
		return getSqlSession().delete("delete", seq);
	}

	// 글 수정
	@Override
	public int modify(BoardVO vo) {
		System.out.println("updateDao");
		return getSqlSession().update("update", vo);
	}

	// 답글 작성
	@Override
	public int reply(BoardVO vo) throws Exception {
		// TODO Auto-generated method stub
		return getSqlSession().insert("reply", vo);
	}

	// 답글 작성시 글 순서 업데이트
	@Override
	public int replyUpdate(BoardVO vo) {
		return getSqlSession().update("replyUpdate", vo);
	}

	// 조회수 증가
	public int hitIncrease(Integer seq) {
		return getSqlSession().update("hitIncrease", seq);
	}
	 @Override
	    public int fileUpload(BoardVO vo) {
	        return getSqlSession().insert("fileUpload", vo);
	 }

	public int getSeq() {
		// TODO Auto-generated method stub
		return ((Integer) getSqlSession().selectOne("getSeq")).intValue();
	}

	public int fileDelete(Integer seq) {
		// TODO Auto-generated method stub
		return getSqlSession().delete("fileDelete",seq);
	}
	@Override
    public List<BoardVO> fileModifyDelete(Map<String, Object> map) {
        List<BoardVO> fileModifyDelete = getSqlSession().selectList("fileModifyDelete", map);
        return fileModifyDelete;
    }
}
