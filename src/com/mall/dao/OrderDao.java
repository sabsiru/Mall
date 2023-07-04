package com.mall.dao;

import com.mall.VO.OrderVO;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public interface OrderDao {
    //�ֹ��ϱ�
    public int insertOrder(OrderVO vo);

    //�ֹ�����
    public List<OrderVO> selectOrderList(Map<String, Object> paramMap);

    //ī��Ʈ
    public int selectOrderCount(OrderVO vo);

    //    �����
    public void updateRemain(OrderVO vo);

    //updateOrderidseq
    public int updateOrderidseq(OrderVO vo);
    //selectOrderListAll
    public List<OrderVO> selectOrderListAll(Map<String, Object> map);
    //
    public int selectOrderListAllCount(Map<String,Object> paramMap);
    //selectOrderListAllCountByDelete
    public int selectOrderListAllCountByDelete(String id);

    //orderConfirm
    public int orderConfirm(OrderVO vo);
    //updateRefundRequest
    public int updateRefundRequest(OrderVO vo);
    //cancelRequest
    public int cancelRequest(OrderVO vo);

    //updateOrderStatus
    public int updateOrderStatus(Map<String, Object> map);
    //updateOrderStatusByAdmin

}

