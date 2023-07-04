package com.mall.impl;

import com.mall.VO.OrderVO;
import com.mall.dao.OrderDao;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class OrderDaoImpl extends SqlSessionDaoSupport implements OrderDao {


    //주문
    @Override
    public int insertOrder(OrderVO vo) {
    return getSqlSession().insert("insertOrder",vo);
    }

    //주문목록
    @Override
    public List<OrderVO> selectOrderList(Map<String,Object> map) {
        List<OrderVO> orderList = getSqlSession().selectList("selectOrderList",map);
        return orderList;
    }

    @Override
    public int selectOrderCount(OrderVO vo) {
        return 0;
    }

    @Override
    public void updateRemain(OrderVO vo) {

    }
    //updateOrderidseq
    @Override
    public int updateOrderidseq(OrderVO vo) {
        return getSqlSession().update("updateOrderidseq",vo);
    }
    //selectOrderListAll
    @Override
    public List<OrderVO> selectOrderListAll(Map<String, Object> map) {
        return getSqlSession().selectList("selectOrderListAll",map);
    }
    //selectOrderListAllCount
    @Override
    public int selectOrderListAllCount(Map<String,Object> map) {
        return ((Integer)getSqlSession().selectOne("selectOrderListAllCount",map)).intValue();

    }
    //selectOrderListAllCountByDelete
    @Override
    public int selectOrderListAllCountByDelete(String id) {
        return ((Integer)getSqlSession().selectOne("selectOrderListAllCountByDelete",id)).intValue();
    }
    //orderConfirm
    @Override
    public int orderConfirm(OrderVO vo) {
        return getSqlSession().update("orderConfirm",vo);
    }
    //updateRefundRequest
    @Override
    public int updateRefundRequest(OrderVO vo) {
        return getSqlSession().update("updateRefundRequest",vo);
    }
    //cancelRequest
    @Override
    public int cancelRequest(OrderVO vo) {
        return getSqlSession().update("cancelRequest",vo);
    }
    //updateOrderStatus
    @Override
    public int updateOrderStatus(Map<String, Object> map) {
        return getSqlSession().update("updateOrderStatus",map);
    }
}
