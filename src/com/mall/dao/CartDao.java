package com.mall.dao;

import com.mall.VO.CartVO;

import java.util.List;
import java.util.Map;

public interface CartDao {
    //장바구니 넣기
    public int insertCart(CartVO vo);

    //장바구니 리스트
    public List<CartVO> selectCartList(String id);
    //장바구니 카운트
    public int selectCartCount(String id);
    //장바구니 수량 변경
    public int updateCartCount(CartVO vo);
    //장바구니 삭제
    public int deleteCart(int cartid);
    //장바구니 중복확인
    public int CheckOption(CartVO vo);
    //updateCartCount2
    public int updateCartCount2(CartVO vo);
    //selectCart
    public List<CartVO> selectCart(Map<String, Object> cartIdArray);
    //selectCartOrder
    public CartVO selectCartOrder(int  cartid);
    //updateCartRemain
    public int updateCartRemain(CartVO vo);
    //updateCountAfterOrder
    public int updateCountAfterOrder(CartVO vo);
    //selectCountAfterOrder
    public int selectCountAfterOrder(CartVO vo);
    //deleteCartByMember
    public int deleteCartByMember(String id);
    //deleteProductCart
    public int deleteProductCart(int pno);
}




