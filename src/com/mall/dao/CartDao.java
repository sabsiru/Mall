package com.mall.dao;

import com.mall.VO.CartVO;

import java.util.List;
import java.util.Map;

public interface CartDao {
    //��ٱ��� �ֱ�
    public int insertCart(CartVO vo);

    //��ٱ��� ����Ʈ
    public List<CartVO> selectCartList(String id);
    //��ٱ��� ī��Ʈ
    public int selectCartCount(String id);
    //��ٱ��� ���� ����
    public int updateCartCount(CartVO vo);
    //��ٱ��� ����
    public int deleteCart(int cartid);
    //��ٱ��� �ߺ�Ȯ��
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




