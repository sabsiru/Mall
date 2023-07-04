package com.mall.VO;

public class CartVO {
   //cartid
    private int cartid;
    //id
    private String id;
    //pname
    private String pname;
    //optionname
    private String optionname;
    //pimage
    private String pimage;
    //price
    private int price;
    //cartcount
    private int cartcount;
    //pno
    private int pno;
    //remain
    private int remain;
    //delinfo
    private String delinfo;

    public int getCartid() {
        return cartid;
    }

    public void setCartid(int cartid) {
        this.cartid = cartid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getOptionname() {
        return optionname;
    }

    public void setOptionname(String optionname) {
        this.optionname = optionname;
    }

    public String getPimage() {
        return pimage;
    }

    public void setPimage(String pimage) {
        this.pimage = pimage;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getCartcount() {
        return cartcount;
    }

    public void setCartcount(int cartcount) {
        this.cartcount = cartcount;
    }

    public int getPno() {
        return pno;
    }

    public void setPno(int pno) {
        this.pno = pno;
    }

    public int getRemain() {
        return remain;
    }

    public void setRemain(int remain) {
        this.remain = remain;
    }

    public String getDelinfo() {
        return delinfo;
    }

    public void setDelinfo(String delinfo) {
        this.delinfo = delinfo;
    }
}
