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

	// �� ����
	@Override
	public int insert(BoardVO vo) {
		return getSqlSession().insert("insert", vo);
	}

	// �� �б�
	@Override
	public BoardVO read(Integer seq) {
		return getSqlSession().selectOne("select", seq);
	}
	public List<BoardVO> selectFile(int seq) {
		List<BoardVO> selectFile = getSqlSession().selectList("selectFile", seq);
		return selectFile;
	}
	
	// �� ����
	@Override
	public int delete(Integer seq) {
		return getSqlSession().delete("delete", seq);
	}

	// �� ����
	@Override
	public int modify(BoardVO vo) {
		System.out.println("updateDao");
		return getSqlSession().update("update", vo);
	}

	// ��� �ۼ�
	@Override
	public int reply(BoardVO vo) throws Exception {
		// TODO Auto-generated method stub
		return getSqlSession().insert("reply", vo);
	}

	// ��� �ۼ��� �� ���� ������Ʈ
	@Override
	public int replyUpdate(BoardVO vo) {
		return getSqlSession().update("replyUpdate", vo);
	}

	// ��ȸ�� ����
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
