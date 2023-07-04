package com.mall.impl;

import com.mall.VO.CartVO;
import com.mall.dao.CartDao;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class CartDaoImpl extends SqlSessionDaoSupport implements CartDao {
    @Override
    public int insertCart(CartVO vo) {
        return getSqlSession().insert("insertCart", vo);
    }

    // 장바구니 리스트
    @Override
    public List<CartVO> selectCartList(String id) {
        List<CartVO> cartList = getSqlSession().selectList("selectCartList", id);
        return cartList;
    }

    // 장바구니 카운트
    @Override
    public int selectCartCount(String id) {
        return getSqlSession().selectOne("selectCartCount", id);
    }

    // 장바구니 수량 변경
    @Override
    public int updateCartCount(CartVO vo) {
        return getSqlSession().update("updateCartCount", vo);
    }

    // 장바구니 삭제
    @Override
    public int deleteCart(int cartid) {
        return getSqlSession().delete("deleteCart", cartid);
    }

    // 장바구니 중복확인
    @Override
    public int CheckOption(CartVO vo) {
        int result = getSqlSession().selectOne("CheckOption", vo);
        return result;
    }

    // updateCartCount2
    @Override
    public int updateCartCount2(CartVO vo) {
        return getSqlSession().update("updateCartCount2", vo);
    }

    // selectCart
    @Override
    public List<CartVO> selectCart(Map<String, Object> cartIdArray) {
        return getSqlSession().selectList("selectCart", cartIdArray);
    }

    // selectCartOrder
    @Override
    public CartVO selectCartOrder(int cartid) {
        return getSqlSession().selectOne("selectCartOrder", cartid);
    }

    // updateCartRemain
    @Override
    public int updateCartRemain(CartVO vo) {
        return getSqlSession().update("updateCartRemain", vo);
    }
    //updateCountAfterOrder
    @Override
    public int updateCountAfterOrder(CartVO vo) {
        return getSqlSession().update("updateCountAfterOrder", vo);
    }
    //selectCountAfterOrder
    @Override
    public int selectCountAfterOrder(CartVO vo) {
        return getSqlSession().selectOne("selectCountAfterOrder", vo);
    }
    //deleteCartByMember
    @Override
    public int deleteCartByMember(String id) {
        return getSqlSession().delete("deleteCartByMember", id);
    }
    //deleteProductCart
    @Override
    public int deleteProductCart(int pno) {
        return getSqlSession().update("deleteProductCart", pno);
    }
}
