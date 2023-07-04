package com.mall.dao;

import com.mall.VO.MemberVO;

import java.util.List;
import java.util.Map;

public interface MemberDao {
    // join
    public int join(MemberVO vo);

    // idCheck
    public int idCheck(String id);

    // phoneCheck
    public int phoneCheck(String phone);

    // getPass
    public int getPass(MemberVO vo);

    // getPhone
    public int getPhone(MemberVO vo);

    // login
    public MemberVO login(MemberVO vo);

    // findId
    public String findId(MemberVO vo);

    // findPass
    public String findPass(MemberVO vo);

    // myPage
    public MemberVO myPage(MemberVO vo);

    // update
    public int updateMember(MemberVO vo);

    // updatePass
    public int updatePass(MemberVO vo);

    // delete
    public int deleteMember(MemberVO vo);

    // memeberList
    public List<MemberVO> memberList(Map<String, Object> map);

    // memberCount
    public int memberCount(Map<String, Object> map);
    //getMember
    public MemberVO getMember(Integer mno);
}
