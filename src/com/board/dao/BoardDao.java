package com.board.dao;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.board.VO.BoardVO;

public abstract interface BoardDao {

	public abstract List<BoardVO> list(Map<String, Object> paramMap);

	public abstract int getCount(Map<String, Object> paramMap);

	public abstract int getSeq();

	// �� ����
	public abstract int insert(BoardVO vo);

	// �� �б�
	public abstract BoardVO read(Integer seq);

	// ���� �б�
	public abstract List<BoardVO> selectFile(int seq);

	// ��ȸ�� ����
	public abstract int hitIncrease(Integer seq);

	// �� ����
	public abstract int delete(Integer seq);

	// ���� ����
	public abstract int fileDelete(Integer seq);
	//���� ������ ����
	public abstract List<BoardVO> fileModifyDelete(Map<String, Object> paramMap);
	// �� ����
	public abstract int modify(BoardVO vo);

	// ���
	public abstract int reply(BoardVO vo) throws Exception;

	// �Խñ� ���� ������Ʈ
	public abstract int replyUpdate(BoardVO vo);

	// ���� ���ε�
	public abstract int fileUpload(BoardVO vo);

}
