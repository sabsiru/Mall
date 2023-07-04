package com.mall.dao;

import com.mall.VO.ProductVO;

import java.util.Map;
import java.util.List;

public abstract interface ProductDao {
    public abstract List<ProductVO> productList(Map<String, Object> paramMap);
    //productListDelete
    public abstract List<ProductVO> productListDelete(Map<String, Object> paramMap);

    public abstract int productCount(Map<String, Object> paramMap);
    //상품 읽기
    public abstract ProductVO productDetail(Integer pno);

    //옵션
    public abstract List<ProductVO> optionList(int pno);

    //optionCount
    public abstract int optionCount(int pno);
    //updateRemainCart
    public abstract int updateRemainCart(ProductVO vo);
    //checkRemain
    public abstract int checkRemain(ProductVO vo);
    //checkRemainNoOption2
    public abstract int checkRemainNoOption2(ProductVO vo);

    //getPoption
    public abstract ProductVO getPoption(ProductVO vo);
    //insertProduct
    public abstract int insertProduct(ProductVO vo);
    //insertOption
    public abstract int insertOption(ProductVO vo);
    //updateProduct
    public abstract int updateProduct(ProductVO vo);
    //deleteOption
    public abstract int deleteOption(Integer pno);
    //insertOptionUpdate
    public abstract int insertOptionUpdate(ProductVO vo);
    //deleteProduct
    public abstract int deleteProduct(Integer pno);
    //deleteProductOption
    public abstract int deleteProductOption(Integer pno);
}
