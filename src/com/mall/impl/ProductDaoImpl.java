package com.mall.impl;

import com.mall.VO.ProductVO;
import com.mall.dao.ProductDao;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ProductDaoImpl extends SqlSessionDaoSupport implements ProductDao {

    @Override
    public List<ProductVO> productList(Map<String, Object> map) {
        List<ProductVO> productList = getSqlSession().selectList("productList", map);
        return productList;
    }
    //productListDelete
    @Override
    public List<ProductVO> productListDelete(Map<String, Object> map) {
        List<ProductVO> productListDelete = getSqlSession().selectList("productListDelete", map);
        return productListDelete;
    }

    @Override
    public int productCount(Map<String, Object> map) {
        return ((Integer) getSqlSession().selectOne("productCount", map)).intValue();
    }

    @Override
    public ProductVO productDetail(Integer pno) {
        return getSqlSession().selectOne("productDetail", pno);
    }

    @Override
    public List<ProductVO> optionList(int pno) {
        List<ProductVO> optionList=getSqlSession().selectList("optionList", pno);
        return optionList;
    }
    @Override
    public int optionCount(int pno) {
        return ((Integer) getSqlSession().selectOne("getOptionCount", pno)).intValue();
    }
    @Override
    public int updateRemainCart(ProductVO vo) {
        return getSqlSession().update("updateRemainCart", vo);
    }
    @Override
    public int checkRemain(ProductVO vo) {
        return ((Integer) getSqlSession().selectOne("checkRemain", vo)).intValue();
    }
    //checkRemainNoOption2
    @Override
    public int checkRemainNoOption2(ProductVO vo) {
        return ((Integer) getSqlSession().selectOne("checkRemainNoOption2", vo)).intValue();
    }
    @Override
    public ProductVO getPoption(ProductVO vo) {
        return getSqlSession().selectOne("getPoption", vo);
    }
    //insertProduct
    @Override
    public int insertProduct(ProductVO vo) {
        return getSqlSession().insert("insertProduct", vo);
    }
    //insertOption
    @Override
    public int insertOption(ProductVO vo) {
        return getSqlSession().insert("insertOption", vo);
    }
    //updateProduct
    @Override
    public int updateProduct(ProductVO vo) {
        return getSqlSession().update("updateProduct", vo);
    }
    //deleteOption
    @Override
    public int deleteOption(Integer pno) {
        return getSqlSession().delete("deleteOption", pno);
    }
    //insertOptionUpdate
    @Override
    public int insertOptionUpdate(ProductVO vo) {
        return getSqlSession().insert("insertOptionUpdate", vo);
    }
    //deleteProduct
    @Override
    public int deleteProduct(Integer pno) {
        return getSqlSession().update("deleteProduct", pno);
    }
    //deleteProductOption
    @Override
    public int deleteProductOption(Integer pno) {
        return getSqlSession().update("deleteProductOption", pno);
    }

}
