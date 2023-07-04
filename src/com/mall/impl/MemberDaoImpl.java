package com.mall.impl;

import com.mall.VO.MemberVO;
import com.mall.dao.MemberDao;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class MemberDaoImpl extends SqlSessionDaoSupport implements MemberDao {

    @Override
    public int join(MemberVO vo) {
        // join
        return getSqlSession().insert("join", vo);
    }

    @Override
    public int idCheck(String id) {
        // idCheck
        int result = getSqlSession().selectOne("idCheck", id);
        return result;
    }

    // phoneCheck
    @Override
    public int phoneCheck(String phone) {
        return getSqlSession().selectOne("phoneCheck", phone);
    }

    // getPass
    @Override
    public int getPass(MemberVO vo) {
        return getSqlSession().selectOne("getPass", vo);
    }

    // getPhone
    @Override
    public int getPhone(MemberVO vo) {
        return getSqlSession().selectOne("getPhone", vo);
    }

    // login
    @Override
    public MemberVO login(MemberVO vo) {
        return getSqlSession().selectOne("login", vo);
    }

    // findId
    @Override
    public String findId(MemberVO vo) {
        return getSqlSession().selectOne("findId", vo);
    }

    // findPass
    @Override
    public String findPass(MemberVO vo) {
        return getSqlSession().selectOne("findPass", vo);
    }

    // myPage
    @Override
    public MemberVO myPage(MemberVO vo) {
        return getSqlSession().selectOne("myPage", vo);
    }

    // update
    @Override
    public int updateMember(MemberVO vo) {
        return getSqlSession().update("updateMember", vo);
    }

    // updatePass
    @Override
    public int updatePass(MemberVO vo) {
        return getSqlSession().update("updatePass", vo);
    }

    // delete
    @Override
    public int deleteMember(MemberVO vo) {
        return getSqlSession().delete("deleteMember", vo);
    }

    // memeberList
    @Override
    public List<MemberVO> memberList(Map<String, Object> map) {
        return getSqlSession().selectList("memberList", map);
    }

    //memberCount
    @Override
    public int memberCount(Map<String, Object> map) {
        return getSqlSession().selectOne("memberCount", map);
    }

    //getMember
    @Override
    public MemberVO getMember(Integer mno) {
        return getSqlSession().selectOne("getMember", mno);

    }
}
