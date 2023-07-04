package com.mall.controller;

import java.io.*;
import java.util.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mall.VO.MemberVO;
import com.mall.VO.OrderVO;
import com.mall.VO.ProductVO;
import com.mall.dao.CartDao;
import com.mall.dao.MemberDao;
import com.mall.dao.OrderDao;
import com.mall.dao.ProductDao;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.board.VO.BoardVO;
import com.board.dao.BoardDao;
import com.board.dao.BoardDaoImpl;
import com.tobesoft.xplatform.data.ColumnHeader;
import com.tobesoft.xplatform.data.DataSet;
import com.tobesoft.xplatform.data.DataSetList;
import com.tobesoft.xplatform.data.DataTypes;
import com.tobesoft.xplatform.data.PlatformData;
import com.tobesoft.xplatform.data.VariableList;
import com.tobesoft.xplatform.data.datatype.DataType;
import com.tobesoft.xplatform.tx.HttpPlatformRequest;
import com.tobesoft.xplatform.tx.HttpPlatformResponse;
import com.tobesoft.xplatform.tx.PlatformException;
import com.tobesoft.xplatform.tx.PlatformType;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AdminController {
    @SuppressWarnings("unused")
    private Logger log = Logger.getLogger(getClass());
    // private int pageSize = 10;
//	private int blockCount = 10;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private CartDao cartDao;

    // insertProduct
    @RequestMapping(value = "/mall/insertProduct.do", method = RequestMethod.POST)
    public void insertProduct(HttpServletRequest request, HttpServletResponse response)
            throws PlatformException, IOException {
        PlatformData pdata = new PlatformData();
        System.out.println("insertProduct");
        int nErrorCode = 0;
        String strErrorMsg = "START";

        HttpPlatformRequest req = new HttpPlatformRequest(request);

        req.receiveData();
        pdata = req.getData();

        DataSet ds = pdata.getDataSet("PRODUCT");
        DataSet optionDs = pdata.getDataSet("POPTION");

        // product
        String pname = ds.getString(0, "pname");
        String price = ds.getString(0, "price");
        String pcontent = ds.getString(0, "pcontent");
        String pimage = ds.getString(0, "pimage");
        String detailimage = ds.getString(0, "detailimage");
        String option1 = optionDs.getString(0, "option1");
        String optionvalue1 = optionDs.getString(0, "optionvalue1");
        int remain = optionDs.getInt(0, "remain");
        int size = optionDs.getRowCount();
        ProductVO pVo = new ProductVO();
        //유효성 검사

        System.out.println("pname = " + pname);
        System.out.println("price = " + price);
        System.out.println("pcontent = " + pcontent);
        System.out.println("pimage = " + pimage);
        System.out.println("detailimage = " + detailimage);

        pVo.setPname(pname);
        pVo.setPrice(Integer.parseInt(price));
        pVo.setPcontent(pcontent);
        pVo.setPimage(pimage);
        pVo.setDetailimage(detailimage);
        pVo.setDelinfo("no");

        System.out.println("size=" + size);

        productDao.insertProduct(pVo);
        for (int i = 0; i < size; i++) {
            option1 = optionDs.getString(i, "option1");
            optionvalue1 = optionDs.getString(i, "optionvalue1");
            String option2 = optionDs.getString(i, "option2");
            String optionvalue2 = optionDs.getString(i, "optionvalue2");
            remain = optionDs.getInt(i, "remain");

            pVo.setOption1(option1);
            pVo.setOptionvalue1(optionvalue1);
            pVo.setOption2(option2);
            pVo.setOptionvalue2(optionvalue2);
            pVo.setRemain((remain));

            productDao.insertOption(pVo);
            nErrorCode = 0;
            strErrorMsg = "SUCC";
        }

        // save the ErrorCode and ErrorMsg for sending Client
        VariableList varList = pdata.getVariableList();

        varList.add("ErrorCode", nErrorCode);
        varList.add("ErrorMsg", strErrorMsg);

        // send the result data(XML) to Client
        HttpPlatformResponse res = new HttpPlatformResponse(response, PlatformType.CONTENT_TYPE_XML, "UTF-8");
        res.setData(pdata);
        res.sendData(); // Send Data
    }

    // imageUpload
    @RequestMapping("/mall/imageUpload.do")
    public void imageUpload(HttpServletRequest request, HttpServletResponse response)
            throws PlatformException, IOException {
        System.out.println("imageUPload.do");
        int nErrorCode = 0;
        String sHeader = request.getHeader("Content-Type");
        if (sHeader == null) {
            System.out.println("sHeader=" + sHeader);
            return;
        }
        request.setCharacterEncoding("utf-8");
        String sRealPath = request.getSession().getServletContext().getRealPath("/");
        String sPath = request.getParameter("PATH");
        System.out.println(request.getMethod());

        String sUpPath = sRealPath + "upload";
        int nMaxSize = 10 * 1024 * 1024; // 최대 업로드 파일 크기 10MB(메가)로 제한
        System.out.println(sUpPath);
        PlatformData resData = new PlatformData();
        VariableList resVarList = resData.getVariableList();
        HttpPlatformRequest req = new HttpPlatformRequest(request);

        String sMsg = " A ";
        String main = request.getParameter("main");
        System.out.println("main=" + main);
        String sub = request.getParameter("sub");
        System.out.println("sub=" + sub);
        //main이 no가 아니면 기존의 파일을 삭제
        if (!main.equals("no") && !sub.equals("no")) {
            File file = new File(sUpPath + "/" + main);
            File file2 = new File(sUpPath + "/" + sub);

            file.delete();
            file2.delete();

        }
        if (!main.equals("no")) {
            File file = new File(sUpPath + "/" + main);

            file.delete();

        }
        if (!sub.equals("no")) {
            File file = new File(sUpPath + "/" + sub);

            file.delete();

        }


        MultipartRequest multi = new MultipartRequest(request, sUpPath, nMaxSize, "utf-8",
                new DefaultFileRenamePolicy());
        System.out.println("multipart");
        Enumeration files = multi.getFileNames();

        sMsg += "B ";
        DataSet ds = new DataSet("PRODUCT");
        DataSet getName=new DataSet("getName");
        System.out.println("parent");


        ds.addColumn(new ColumnHeader("pimage", DataTypes.STRING));
        ds.addColumn(new ColumnHeader("detailimage", DataTypes.STRING));

        sMsg += "C ";
        String sFName = "";
        String sName = "";
        String stype = "";
        File f = null;

        while (files.hasMoreElements()) {
            System.out.println("filecall");
            sMsg += "D ";
            sName = (String) files.nextElement();
            sFName = multi.getFilesystemName(sName);
            // 시스템에 저장된 이름(바뀐이름)
            System.out.println("sName=" + sName);
            System.out.println("sFName=" + sFName);
            f = multi.getFile(sName);

        }
        getName.addColumn(new ColumnHeader("getName", DataTypes.STRING));
        getName.newRow();
        getName.set(0, "getName", sFName);
        resData.addDataSet(getName);
        resVarList.add("ErrorCode", nErrorCode);
        resVarList.add("ErrorMsg", sFName);

        HttpPlatformResponse res = new HttpPlatformResponse(response);
        res.setData(resData);
        res.sendData();
    }

    @RequestMapping({"/mall/productList.do"})
    public void list(HttpServletRequest request, HttpServletResponse response) throws PlatformException {
        PlatformData pdata = new PlatformData();

        int nErrorCode = 0;
        String strErrorMsg = "START";

        int totalCount = 0;
        try {

            // create HttpPlatformRequest for receive data from client

            HttpPlatformRequest req = new HttpPlatformRequest(request);
            req.receiveData();
            pdata = req.getData();
            DataSetList datasetlist = new DataSetList();

            DataSet test = pdata.getDataSet("paging");

            int pageNum = Integer.parseInt(test.getString(0, "pageNum"));
            int contentPerPage = Integer.parseInt(test.getString(0, "contentPerPage"));
            int pageSize = Integer.parseInt(test.getString(0, "pageSize"));
            String keyWord = test.getString(0, "keyWord");
            String keyField = test.getString(0, "keyField");

            int startCount;
            int endCount;

            System.out.println("pageNum = " + pageNum);
            System.out.println("contentPerPage = " + contentPerPage);
            System.out.println("pageSize = " + pageSize);
            System.out.println("keyWord = " + keyWord);
            System.out.println("keyField = " + keyField);
            startCount = ((pageNum - 1) * pageSize + 1);
            endCount = (pageNum * pageSize);

            // DataSet
            DataSet vo = new DataSet("PRODUCT");
            // DataSet Column setting
            vo.addColumn("pno", DataTypes.INT, 50);
            vo.addColumn("pimage", DataTypes.STRING, 50);
            vo.addColumn("detailimage", DataTypes.STRING, 50);
            vo.addColumn("pname", DataTypes.STRING, 50);
            vo.addColumn("price", DataTypes.INT, 50);
            vo.addColumn(("pcontent"), DataTypes.STRING, 50);
            vo.addColumn(("prdate"), DataTypes.STRING, 50);
            vo.addColumn(("delinfo"), DataTypes.STRING, 50);

            ProductVO pVO;
            HashMap<String, Object> map = new HashMap();

            // 검색 조건
            map.put("keyField", keyField);
            map.put("keyWord", keyWord);

            // 페이징
            map.put("startCount", startCount);
            map.put("endCount", endCount);

            // 게시글 총 갯수
            totalCount = productDao.productCount(map);
            System.out.println("totalcount = " + totalCount);
            // strErrorMsg = 0;
            List<ProductVO> list = null;
            if (totalCount > 0) {
                list = this.productDao.productList(map);
            } else {
                list = Collections.emptyList();
            }
            // ResultSet -> Show the Row sets (XML) : browser
            for (int i = 0; i < list.size(); i++) {

                int row = vo.newRow(); // new Row feed

                pVO = list.get(i);
                vo.set(row, "pno", pVO.getPno());
                vo.set(row, "pimage", pVO.getPimage());
                vo.set(row, "detailimage", pVO.getDetailimage());
                vo.set(row, "pname", pVO.getPname());
                vo.set(row, "price", pVO.getPrice());
                vo.set(row, "pcontent", pVO.getPcontent());
                vo.set(row, "delinfo", pVO.getDelinfo());
            } // for

            int totalPage = (int) Math.ceil((double) totalCount / pageSize);
            if (totalPage == 0) {
                totalPage = 1;
            }
            if (pageNum > totalPage) {
                pageNum = totalPage;
            }

            int startPage = (pageNum - 1) / contentPerPage * contentPerPage + 1;
            int endPage = startPage + contentPerPage - 1;
            if (endPage > totalPage) {
                endPage = totalPage;
            }
//            System.out.println("검색된 글 갯수 = " + totalCount);
//            System.out.println("현재 페이지 = " + pageNum);
//            System.out.println("한페이지당 글 갯수  = " + pageSize);
//            System.out.println("한면의 총 페이지 갯수 = " + contentPerPage);
//            System.out.println("전체 페이지 사이즈 = " + totalPage);
//            System.out.println("시작 카운트  = " + startCount);
//            System.out.println("마지막 카운트 = " + endCount);
//            System.out.println("시작 페이지 = " + startPage);
//            System.out.println("마지막 페이지 = " + endPage);

            DataSet voPaging = new DataSet("paging");

            // DataSet Column setting
            voPaging.addColumn("pageNum", DataTypes.INT, 4);
            voPaging.addColumn("startPage", DataTypes.INT, 4);
            voPaging.addColumn("endPage", DataTypes.STRING, 256);
            voPaging.addColumn("totalPage", DataTypes.INT, 4);
            voPaging.addColumn("count", DataTypes.INT, 4);
            voPaging.addColumn("contentPerPage", DataTypes.INT, 4);
            voPaging.addColumn("pageSize", DataTypes.INT, 4);
            voPaging.addColumn("startCount", DataTypes.INT, 4);
            voPaging.addColumn("endCount", DataTypes.INT, 4);
            voPaging.addColumn("keyField", DataTypes.STRING, 256);
            voPaging.addColumn("keyWord", DataTypes.STRING, 256);
            voPaging.newRow();

            voPaging.set(0, "pageNum", pageNum);
            voPaging.set(0, "startPage", startPage);
            voPaging.set(0, "endPage", endPage);
            voPaging.set(0, "count", totalCount);
            voPaging.set(0, "totalPage", totalPage);
            voPaging.set(0, "contentPerPage", contentPerPage);
            voPaging.set(0, "pageSize", pageSize);
            voPaging.set(0, "startCount", startCount);
            voPaging.set(0, "endCount", endCount);
            voPaging.set(0, "keyWord", keyWord);
            voPaging.set(0, "keyField", keyField);

            // pdata.addDataSet(vo);
            // pdata.addDataSet(voPaging);
            datasetlist.add(vo);
            datasetlist.add(voPaging);
            pdata.setDataSetList(datasetlist);
            // set the ErrorCode and ErrorMsg about success
            nErrorCode = 0;
            strErrorMsg = "SUCC";

        } catch (Throwable th) {
            // set the ErrorCode and ErrorMsg about fail
            nErrorCode = -1;
            strErrorMsg = th.getMessage();

        } // try

        // save the ErrorCode and ErrorMsg for sending Client
        VariableList varList = pdata.getVariableList();

        varList.add("ErrorCode", nErrorCode);
        varList.add("ErrorMsg", strErrorMsg);

        // send the result data(XML) to Client
        HttpPlatformResponse res = new HttpPlatformResponse(response, PlatformType.CONTENT_TYPE_XML, "UTF-8");
        res.setData(pdata);
        res.sendData();
    }

    // productDetailAdmin
    @RequestMapping(value = "/mall/productDetailAdmin.do")
    public void productDetailAdmin(HttpServletRequest request, HttpServletResponse response) throws PlatformException {
        PlatformData pdata = new PlatformData();

        int nErrorCode = 0;
        String strErrorMsg = "START";
        try {
            HttpPlatformRequest req = new HttpPlatformRequest(request);
            System.out.println("productDetailAdmin");
            req.receiveData();
            pdata = req.getData();
            DataSetList datasetlist = new DataSetList();

            DataSet pVo = pdata.getDataSet("PRODUCT");
            DataSet poVo = pdata.getDataSet("POPTION");
            int pno = Integer.parseInt(request.getParameter("pno"));
            System.out.println("pno=" + pno);
            ProductVO vo;
            vo = productDao.productDetail(pno);
            String detailimage = vo.getDetailimage();
            String pname = vo.getPname();
            int price = vo.getPrice();
            String pcontent = vo.getPcontent();
            String pimage = vo.getPimage();

            System.out.println("pimage=" + pimage);
            System.out.println("detailimage = " + detailimage);
            System.out.println("pname = " + pname);
            System.out.println("price = " + price);
            System.out.println("pcontent = " + pcontent);

            pVo.newRow();
            pVo.set(0, "pimage", vo.getPimage());
            pVo.set(0, "detailimage", vo.getDetailimage());
            pVo.set(0, "pname", vo.getPname());
            pVo.set(0, "price", vo.getPrice());
            pVo.set(0, "pcontent", vo.getPcontent());
            pVo.set(0, "delinfo", vo.getDelinfo());


            System.out.println("get option start");
            List<ProductVO> getOption = this.productDao.optionList(pno);
            System.out.println("get option start2");
            System.out.println("getOption.size()=" + getOption.size());

            for (int i = 0; i < getOption.size(); i++) {
                poVo.newRow();

                poVo.set(i, "option1", getOption.get(i).getOption1());
                System.out.println("option1=" + getOption.get(i).getOption1());
                poVo.set(i, "optionvalue1", getOption.get(i).getOptionvalue1());
                poVo.set(i, "option2", getOption.get(i).getOption2());
                poVo.set(i, "optionvalue2", getOption.get(i).getOptionvalue2());
                poVo.set(i, "remain", getOption.get(i).getRemain());
            }

            datasetlist.add(pVo);
            datasetlist.add(poVo);

            pdata.setDataSetList(datasetlist);
        } catch (Throwable th) {
            // set the ErrorCode and ErrorMsg about fail
            nErrorCode = -1;
            strErrorMsg = th.getMessage();

        } // try
        // save the ErrorCode and ErrorMsg for sending Client
        VariableList varList = pdata.getVariableList();

        varList.add("ErrorCode", nErrorCode);
        varList.add("ErrorMsg", strErrorMsg);

        // send the result data(XML) to Client
        HttpPlatformResponse res = new HttpPlatformResponse(response, PlatformType.CONTENT_TYPE_XML, "UTF-8");
        res.setData(pdata);
        res.sendData();
    }

    //memberList
    @RequestMapping(value = "/mall/memberList.do")
    public void MemberList(HttpServletRequest request, HttpServletResponse response) throws PlatformException {
        PlatformData pdata = new PlatformData();

        int nErrorCode = 0;
        String strErrorMsg = "START";

        int totalCount = 0;
        try {
            HttpPlatformRequest req = new HttpPlatformRequest(request);
            req.receiveData();
            pdata = req.getData();
            DataSetList datasetlist = new DataSetList();

            DataSet test = pdata.getDataSet("paging");
            int pageNum = Integer.parseInt(test.getString(0, "pageNum"));
            int contentPerPage = Integer.parseInt(test.getString(0, "contentPerPage"));
            int pageSize = Integer.parseInt(test.getString(0, "pageSize"));
            String keyWord = test.getString(0, "keyWord");
            String keyField = test.getString(0, "keyField");

            int startCount;
            int endCount;

            System.out.println("pageNum = " + pageNum);
            System.out.println("contentPerPage = " + contentPerPage);
            System.out.println("pageSize = " + pageSize);
            System.out.println("keyWord = " + keyWord);
            System.out.println("keyField = " + keyField);
            startCount = ((pageNum - 1) * pageSize + 1);
            endCount = (pageNum * pageSize);

            DataSet vo = new DataSet("MEMBER");
            //addcolumn
            vo.addColumn("mno", DataTypes.INT, 256);
            vo.addColumn("id", DataTypes.STRING, 256);
            vo.addColumn("name", DataTypes.STRING, 256);
            vo.addColumn("phone", DataTypes.STRING, 256);
            vo.addColumn("mail", DataTypes.STRING, 256);
            vo.addColumn("postcode", DataTypes.STRING, 256);
            vo.addColumn("addr", DataTypes.STRING, 256);
            vo.addColumn("detailaddr", DataTypes.STRING, 256);
            vo.addColumn("powerno", DataTypes.INT, 256);
            vo.addColumn("regdate", DataTypes.STRING, 256);


            MemberVO mVo;
            HashMap<String, Object> map = new HashMap();
            // 검색 조건
            map.put("keyField", keyField);
            map.put("keyWord", keyWord);

            // 페이징
            map.put("startCount", startCount);
            map.put("endCount", endCount);

            // 게시글 총 갯수
            totalCount = memberDao.memberCount(map);
            System.out.println("totalcount = " + totalCount);

            List<MemberVO> list = null;
            if (totalCount > 0) {
                list = this.memberDao.memberList(map);
            } else {
                list = Collections.emptyList();
            }
            for (int i = 0; i < list.size(); i++) {
                int row = vo.newRow();

                mVo = list.get(i);
                vo.set(row, "mno", mVo.getMno());
                vo.set(row, "id", mVo.getId());
                vo.set(row, "name", mVo.getName());
                vo.set(row, "phone", mVo.getPhone());
                vo.set(row, "mail", mVo.getMail());
                vo.set(row, "postcode", mVo.getPostcode());
                vo.set(row, "addr", mVo.getAddr());
                vo.set(row, "detailaddr", mVo.getDetailaddr());
                vo.set(row, "powerno", mVo.getPowerno());
                vo.set(row, "regdate", mVo.getRegdate());
            }
            int totalPage = (int) Math.ceil((double) totalCount / pageSize);
            if (totalPage == 0) {
                totalPage = 1;
            }
            if (pageNum > totalPage) {
                pageNum = totalPage;
            }

            int startPage = (pageNum - 1) / contentPerPage * contentPerPage + 1;
            int endPage = startPage + contentPerPage - 1;
            if (endPage > totalPage) {
                endPage = totalPage;
            }
//            System.out.println("검색된 글 갯수 = " + totalCount);
//            System.out.println("현재 페이지 = " + pageNum);
//            System.out.println("한페이지당 글 갯수  = " + pageSize);
//            System.out.println("한면의 총 페이지 갯수 = " + contentPerPage);
//            System.out.println("전체 페이지 사이즈 = " + totalPage);
//            System.out.println("시작 카운트  = " + startCount);
//            System.out.println("마지막 카운트 = " + endCount);
//            System.out.println("시작 페이지 = " + startPage);
//            System.out.println("마지막 페이지 = " + endPage);

            DataSet voPaging = new DataSet("paging");

            // DataSet Column setting
            voPaging.addColumn("pageNum", DataTypes.INT, 4);
            voPaging.addColumn("startPage", DataTypes.INT, 4);
            voPaging.addColumn("endPage", DataTypes.STRING, 256);
            voPaging.addColumn("totalPage", DataTypes.INT, 4);
            voPaging.addColumn("count", DataTypes.INT, 4);
            voPaging.addColumn("contentPerPage", DataTypes.INT, 4);
            voPaging.addColumn("pageSize", DataTypes.INT, 4);
            voPaging.addColumn("startCount", DataTypes.INT, 4);
            voPaging.addColumn("endCount", DataTypes.INT, 4);
            voPaging.addColumn("keyField", DataTypes.STRING, 256);
            voPaging.addColumn("keyWord", DataTypes.STRING, 256);
            voPaging.newRow();

            voPaging.set(0, "pageNum", pageNum);
            voPaging.set(0, "startPage", startPage);
            voPaging.set(0, "endPage", endPage);
            voPaging.set(0, "count", totalCount);
            voPaging.set(0, "totalPage", totalPage);
            voPaging.set(0, "contentPerPage", contentPerPage);
            voPaging.set(0, "pageSize", pageSize);
            voPaging.set(0, "startCount", startCount);
            voPaging.set(0, "endCount", endCount);
            voPaging.set(0, "keyWord", keyWord);
            voPaging.set(0, "keyField", keyField);

            // pdata.addDataSet(vo);
            // pdata.addDataSet(voPaging);
            datasetlist.add(vo);
            datasetlist.add(voPaging);
            pdata.setDataSetList(datasetlist);
            // set the ErrorCode and ErrorMsg about success
            nErrorCode = 0;
            strErrorMsg = "SUCC";

        } catch (Throwable th) {
            nErrorCode = -1;
            strErrorMsg = th.getMessage();
        }
        // save the ErrorCode and ErrorMsg for sending Client
        VariableList varList = pdata.getVariableList();

        varList.add("ErrorCode", nErrorCode);
        varList.add("ErrorMsg", strErrorMsg);

        // send the result data(XML) to Client
        HttpPlatformResponse res = new HttpPlatformResponse(response, PlatformType.CONTENT_TYPE_XML, "UTF-8");
        res.setData(pdata);
        res.sendData();
    }

    //getMember
    @RequestMapping(value = "/mall/getMember.do")
    public void getMember(HttpServletRequest request, HttpServletResponse response) throws PlatformException {
        // get the parameter from Client
        PlatformData pdata = new PlatformData();

        int nErrorCode = 0;
        String strErrorMsg = "";

        int totalCount = 0;
        try {
            HttpPlatformRequest req = new HttpPlatformRequest(request);
            System.out.println("getMemberAdmin");
            String id = request.getParameter("id");
            System.out.println("id = " + id);
            req.receiveData();
            pdata = req.getData();
            DataSetList datasetlist = new DataSetList();

            DataSet test = pdata.getDataSet("paging");
            int pageNum = Integer.parseInt(test.getString(0, "pageNum"));
            int contentPerPage = Integer.parseInt(test.getString(0, "contentPerPage"));
            int pageSize = Integer.parseInt(test.getString(0, "pageSize"));
            String keyWord = test.getString(0, "keyWord");
            String keyField = test.getString(0, "keyField");

            int startCount;
            int endCount;

            System.out.println("pageNum = " + pageNum);
            System.out.println("contentPerPage = " + contentPerPage);
            System.out.println("pageSize = " + pageSize);
            System.out.println("keyWord = " + keyWord);
            System.out.println("keyField = " + keyField);
            startCount = ((pageNum - 1) * pageSize + 1);
            endCount = (pageNum * pageSize);

            DataSet orderList = new DataSet("ORDERLIST");
            orderList.addColumn("id", DataTypes.STRING, 256);
            orderList.addColumn("orderidseq", DataTypes.STRING, 256);
            orderList.addColumn("pno", DataTypes.INT, 256);
            orderList.addColumn("pname", DataTypes.STRING, 256);
            orderList.addColumn("pimage", DataTypes.STRING, 256);
            orderList.addColumn("optionname", DataTypes.STRING, 256);
            orderList.addColumn("name", DataTypes.STRING, 256);
            orderList.addColumn("phone", DataTypes.STRING, 256);
            orderList.addColumn("addr", DataTypes.STRING, 256);
            orderList.addColumn("status", DataTypes.STRING, 256);
            orderList.addColumn("refund", DataTypes.STRING, 256);
            orderList.addColumn("receive", DataTypes.STRING, 256);
            orderList.addColumn("orderdate", DataTypes.STRING, 256);

            OrderVO oVo;
            HashMap<String, Object> map = new HashMap();
            // 검색 조건
            map.put("keyField", keyField);
            map.put("keyWord", keyWord);

            // 페이징
            map.put("startCount", startCount);
            map.put("endCount", endCount);

            //id값 가져오기
            map.put("id", id);

            totalCount = orderDao.selectOrderListAllCount(map);
            System.out.println("totalCount = " + totalCount);
            List<OrderVO> list = null;
            if (totalCount > 0) {
                list = orderDao.selectOrderListAll(map);
            } else {
                list = Collections.emptyList();
            }
            for (int i = 0; i < list.size(); i++) {
                int row = orderList.newRow();
                oVo = list.get(i);

                orderList.set(row, "id", oVo.getId());
                orderList.set(row, "orderidseq", oVo.getOrderidseq());
                orderList.set(row, "pno", oVo.getPno());
                orderList.set(row, "pname", oVo.getPname());
                orderList.set(row, "pimage", oVo.getPimage());
                orderList.set(row, "optionname", oVo.getOptionname());
                orderList.set(row, "name", oVo.getName());
                orderList.set(row, "phone", oVo.getPhone());
                orderList.set(row, "addr", oVo.getAddr());
                orderList.set(row, "status", oVo.getStatus());
                orderList.set(row, "refund", oVo.getRefund());
                orderList.set(row, "receive", oVo.getReceive());
                orderList.set(row, "orderdate", oVo.getOrderdate());
            }

            int totalPage = (int) Math.ceil((double) totalCount / pageSize);
            if (totalPage == 0) {
                totalPage = 1;
            }
            if (pageNum > totalPage) {
                pageNum = totalPage;
            }

            int startPage = (pageNum - 1) / contentPerPage * contentPerPage + 1;
            int endPage = startPage + contentPerPage - 1;
            if (endPage > totalPage) {
                endPage = totalPage;
            }
//            System.out.println("검색된 글 갯수 = " + totalCount);
//            System.out.println("현재 페이지 = " + pageNum);
//            System.out.println("한페이지당 글 갯수  = " + pageSize);
//            System.out.println("한면의 총 페이지 갯수 = " + contentPerPage);
//            System.out.println("전체 페이지 사이즈 = " + totalPage);
//            System.out.println("시작 카운트  = " + startCount);
//            System.out.println("마지막 카운트 = " + endCount);
//            System.out.println("시작 페이지 = " + startPage);
//            System.out.println("마지막 페이지 = " + endPage);

            DataSet voPaging = new DataSet("paging");

            // DataSet Column setting
            voPaging.addColumn("pageNum", DataTypes.INT, 4);
            voPaging.addColumn("startPage", DataTypes.INT, 4);
            voPaging.addColumn("endPage", DataTypes.STRING, 256);
            voPaging.addColumn("totalPage", DataTypes.INT, 4);
            voPaging.addColumn("count", DataTypes.INT, 4);
            voPaging.addColumn("contentPerPage", DataTypes.INT, 4);
            voPaging.addColumn("pageSize", DataTypes.INT, 4);
            voPaging.addColumn("startCount", DataTypes.INT, 4);
            voPaging.addColumn("endCount", DataTypes.INT, 4);
            voPaging.addColumn("keyField", DataTypes.STRING, 256);
            voPaging.addColumn("keyWord", DataTypes.STRING, 256);
            voPaging.newRow();

            voPaging.set(0, "pageNum", pageNum);
            voPaging.set(0, "startPage", startPage);
            voPaging.set(0, "endPage", endPage);
            voPaging.set(0, "count", totalCount);
            voPaging.set(0, "totalPage", totalPage);
            voPaging.set(0, "contentPerPage", contentPerPage);
            voPaging.set(0, "pageSize", pageSize);
            voPaging.set(0, "startCount", startCount);
            voPaging.set(0, "endCount", endCount);
            voPaging.set(0, "keyWord", keyWord);
            voPaging.set(0, "keyField", keyField);

            // pdata.addDataSet(vo);
            // pdata.addDataSet(voPaging);
            datasetlist.add(orderList);
            datasetlist.add(voPaging);
            pdata.setDataSetList(datasetlist);
            // set the ErrorCode and ErrorMsg about success
            nErrorCode = 0;
            strErrorMsg = "SUCC";
        } catch (Throwable th) {
            nErrorCode = -1;
            strErrorMsg = th.getMessage();
        }
        // save the ErrorCode and ErrorMsg for sending Client
        VariableList varList = pdata.getVariableList();
        varList.add("ErrorCode", nErrorCode);
        varList.add("ErrorMsg", strErrorMsg);

        // send the result data(XML) to Client
        HttpPlatformResponse res = new HttpPlatformResponse(response, PlatformType.CONTENT_TYPE_XML, "UTF-8");
        res.setData(pdata);
        res.sendData();
    }

    //updateOrderStatus
    @RequestMapping(value = "/mall/updateOrderStatus.do", method = RequestMethod.POST)
    public void updateOrderStatus(HttpServletRequest request, HttpServletResponse response) throws Exception {
        PlatformData pdata = new PlatformData();
        int nErrorCode = 0;
        String strErrorMsg = "";
        try {
            HttpPlatformRequest req = new HttpPlatformRequest(request);
            req.receiveData();
            pdata = req.getData();
            DataSetList datasetlist = pdata.getDataSetList();

            DataSet orderList = pdata.getDataSet("ORDERLIST");
            DataSet sendStatus = pdata.getDataSet("SENDSTATUS");

            String seq = sendStatus.getString(0, "seq");
            String key = sendStatus.getString(0, "key");
            String value = sendStatus.getString(0, "value");
            int row = sendStatus.getInt(0, "row");
            //id, pno, opionname, pname 받아오기
            String id=orderList.getString(row, "id");
            String pno=orderList.getString(row, "pno");
            String optionname=orderList.getString(row, "optionname");
            String pname=orderList.getString(row, "pname");

            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("orderidseq", seq);
            map.put("key", key);
            map.put("value", value);
            map.put("id", id);
            map.put("pno", pno);
            map.put("optionname", optionname);
            map.put("pname", pname);

            System.out.println("seq = " + seq);
            System.out.println("key = " + key);
            System.out.println("value = " + value);
            System.out.println("row = " + row);

            if (key.equals("status")) {
                orderList.set(row, "status", value);
            } else if (key.equals("refund")) {
                orderList.set(row, "refund", value);
            } else if (key.equals("receive")) {
                orderList.set(row, "receive", value);
            }

            OrderVO vo;
            this.orderDao.updateOrderStatus(map);


        } catch (Throwable th) {
            nErrorCode = -1;
            strErrorMsg = th.getMessage();
        }

        // save the ErrorCode and ErrorMsg for sending Client
        VariableList varList = pdata.getVariableList();
        varList.add("ErrorCode", nErrorCode);
        varList.add("ErrorMsg", strErrorMsg);

        // send the result data(XML) to Client
        HttpPlatformResponse res = new HttpPlatformResponse(response, PlatformType.CONTENT_TYPE_XML, "UTF-8");
        res.setData(pdata);
        res.sendData();
    }

    //상품 수정
    @RequestMapping(value = "/mall/updateProduct.do")
    public void updateProduct(HttpServletRequest request, HttpServletResponse response) throws PlatformException {
        PlatformData pdata = new PlatformData();

        int nErrorCode = 0;
        String strErrorMsg = "START";

        HttpPlatformRequest req = new HttpPlatformRequest(request);

        req.receiveData();
        pdata = req.getData();

        DataSet ds = pdata.getDataSet("PRODUCT");
        DataSet optionDs = pdata.getDataSet("POPTION");

        // product
        String pname = ds.getString(0, "pname");
        String price = ds.getString(0, "price");
        String pcontent = ds.getString(0, "pcontent");
        String pimage = ds.getString(0, "pimage");
        String detailimage = ds.getString(0, "detailimage");
        String option1 = optionDs.getString(0, "option1");
        String optionvalue1 = optionDs.getString(0, "optionvalue1");
        int pno = Integer.parseInt(request.getParameter("pno"));
        int remain = optionDs.getInt(0, "remain");
        int size = optionDs.getRowCount();
        ProductVO pVo = new ProductVO();
        for (int i = 0; i < size; i++) {
            if (option1 == null || option1.equals("")) {
                nErrorCode = -1;
                strErrorMsg = "옵션을 입력하세요.";
                return;
            } else if (optionvalue1 == null || optionvalue1.equals("")) {
                nErrorCode = -1;
                strErrorMsg = "옵션값을 입력하세요.";
                return;
            } else if (remain == 0 || remain < 0) {
                nErrorCode = -111;
                strErrorMsg = "재고를 입력하세요.";
                return;
            }
        }
        if (pname == null || pname.equals("")) {
            nErrorCode = -1;
            strErrorMsg = " 상품명을 입력하세요.";
        } else if (price == null || price.equals("")) {
            nErrorCode = -2;
            strErrorMsg = " 가격을 입력하세요.";
        } else if (pcontent == null || pcontent.equals("")) {
            nErrorCode = -3;
            strErrorMsg = " 상품설명을 입력하세요.";
        } else if (pimage == null || pimage.equals("")) {
            nErrorCode = -4;
            strErrorMsg = " 상품이미지를 입력하세요.";
        } else if (detailimage == null || detailimage.equals("")) {
            nErrorCode = -5;
            strErrorMsg = " 상품상세이미지를 입력하세요.";
        } else {
            System.out.println("pname = " + pname);
            System.out.println("price = " + price);
            System.out.println("pcontent = " + pcontent);
            System.out.println("pimage = " + pimage);
            System.out.println("detailimage = " + detailimage);
            pVo.setPno(pno);
            pVo.setPname(pname);
            pVo.setPrice(Integer.parseInt(price));
            pVo.setPcontent(pcontent);
            pVo.setPimage(pimage);
            pVo.setDetailimage(detailimage);

            System.out.println("size=" + size);
        }
        //deleteoption
        this.productDao.deleteOption(pno);
        this.productDao.updateProduct(pVo);
        //장바구니 재고값 변경해야함,
        for (int i = 0; i < size; i++) {
            option1 = optionDs.getString(i, "option1");
            optionvalue1 = optionDs.getString(i, "optionvalue1");
            String option2 = optionDs.getString(i, "option2");
            String optionvalue2 = optionDs.getString(i, "optionvalue2");
            remain = optionDs.getInt(i, "remain");
            pVo.setPno(pno);
            pVo.setOption1(option1);
            pVo.setOptionvalue1(optionvalue1);
            pVo.setOption2(option2);
            pVo.setOptionvalue2(optionvalue2);
            pVo.setRemain((remain));

            productDao.insertOptionUpdate(pVo);
            nErrorCode = 0;
            strErrorMsg = "SUCC";
        }
        VariableList varList = pdata.getVariableList();

        if (nErrorCode != 0) {
            varList.add("ErrorCode", nErrorCode);
        } else {
            varList.add("ErrorCode", nErrorCode);
        }

        varList.add("ErrorMsg", strErrorMsg);

        // send the result data(XML) to Client
        HttpPlatformResponse res = new HttpPlatformResponse(response, PlatformType.CONTENT_TYPE_XML, "UTF-8");
        res.setData(pdata);
        res.sendData(); // Send Data
    }

    //deleteProduct
    @RequestMapping(value = "/mall/deleteProduct.do")
    public void deleteProduct(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws PlatformException {
        PlatformData pdata = new PlatformData();

        int nErrorCode = 0;
        String strErrorMsg = "START";
        String id = session.getId();

        try {
            HttpPlatformRequest req = new HttpPlatformRequest(request);
            System.out.println("deleteProduct");
            req.receiveData();
            pdata = req.getData();
            DataSetList dataSetList = pdata.getDataSetList();

            DataSet ds = pdata.getDataSet("PRODUCT");
            int pno = Integer.parseInt(request.getParameter("pno"));

            this.productDao.deleteProduct(pno);
            this.productDao.deleteProductOption(pno);
            this.cartDao.deleteProductCart(pno);

        } catch (Throwable th) {
            nErrorCode = -1;
            strErrorMsg = th.getMessage();
        }
        strErrorMsg = "SUCC";
        VariableList varList = pdata.getVariableList();

        varList.add("ErrorCode", nErrorCode);
        varList.add("ErrorMsg", strErrorMsg);

        // send the result data(XML) to Client
        HttpPlatformResponse res = new HttpPlatformResponse(response, PlatformType.CONTENT_TYPE_XML, "UTF-8");
        res.setData(pdata);
        res.sendData();

    }

    @RequestMapping({"/mall/productListDelete.do"})
    public void deleteList(HttpServletRequest request, HttpServletResponse response) throws PlatformException {
        PlatformData pdata = new PlatformData();

        int nErrorCode = 0;
        String strErrorMsg = "START";

        int totalCount = 0;
        try {

            // create HttpPlatformRequest for receive data from client

            HttpPlatformRequest req = new HttpPlatformRequest(request);
            req.receiveData();
            pdata = req.getData();
            DataSetList datasetlist = new DataSetList();

            DataSet test = pdata.getDataSet("paging");

            int pageNum = Integer.parseInt(test.getString(0, "pageNum"));
            int contentPerPage = Integer.parseInt(test.getString(0, "contentPerPage"));
            int pageSize = Integer.parseInt(test.getString(0, "pageSize"));
            String keyWord = test.getString(0, "keyWord");
            String keyField = test.getString(0, "keyField");

            int startCount;
            int endCount;

            System.out.println("pageNum = " + pageNum);
            System.out.println("contentPerPage = " + contentPerPage);
            System.out.println("pageSize = " + pageSize);
            System.out.println("keyWord = " + keyWord);
            System.out.println("keyField = " + keyField);
            startCount = ((pageNum - 1) * pageSize + 1);
            endCount = (pageNum * pageSize);

            // DataSet
            DataSet vo = new DataSet("PRODUCT");
            // DataSet Column setting
            vo.addColumn("pno", DataTypes.INT, 50);
            vo.addColumn("pimage", DataTypes.STRING, 50);
            vo.addColumn("detailimage", DataTypes.STRING, 50);
            vo.addColumn("pname", DataTypes.STRING, 50);
            vo.addColumn("price", DataTypes.INT, 50);
            vo.addColumn(("pcontent"), DataTypes.STRING, 50);
            vo.addColumn(("prdate"), DataTypes.STRING, 50);
            vo.addColumn(("delinfo"), DataTypes.STRING, 50);

            ProductVO pVO;
            HashMap<String, Object> map = new HashMap();

            // 검색 조건
            map.put("keyField", keyField);
            map.put("keyWord", keyWord);

            // 페이징
            map.put("startCount", startCount);
            map.put("endCount", endCount);

            // 게시글 총 갯수
            totalCount = productDao.productCount(map);
            System.out.println("totalcount = " + totalCount);
            // strErrorMsg = 0;
            List<ProductVO> list = null;
            if (totalCount > 0) {
                list = this.productDao.productListDelete(map);
            } else {
                list = Collections.emptyList();
            }
            // ResultSet -> Show the Row sets (XML) : browser
            for (int i = 0; i < list.size(); i++) {

                int row = vo.newRow(); // new Row feed

                pVO = list.get(i);
                vo.set(row, "pno", pVO.getPno());
                vo.set(row, "pimage", pVO.getPimage());
                vo.set(row, "detailimage", pVO.getDetailimage());
                vo.set(row, "pname", pVO.getPname());
                vo.set(row, "price", pVO.getPrice());
                vo.set(row, "pcontent", pVO.getPcontent());
                vo.set(row, "delinfo", pVO.getDelinfo());
            } // for

            int totalPage = (int) Math.ceil((double) totalCount / pageSize);
            if (totalPage == 0) {
                totalPage = 1;
            }
            if (pageNum > totalPage) {
                pageNum = totalPage;
            }

            int startPage = (pageNum - 1) / contentPerPage * contentPerPage + 1;
            int endPage = startPage + contentPerPage - 1;
            if (endPage > totalPage) {
                endPage = totalPage;
            }
//            System.out.println("검색된 글 갯수 = " + totalCount);
//            System.out.println("현재 페이지 = " + pageNum);
//            System.out.println("한페이지당 글 갯수  = " + pageSize);
//            System.out.println("한면의 총 페이지 갯수 = " + contentPerPage);
//            System.out.println("전체 페이지 사이즈 = " + totalPage);
//            System.out.println("시작 카운트  = " + startCount);
//            System.out.println("마지막 카운트 = " + endCount);
//            System.out.println("시작 페이지 = " + startPage);
//            System.out.println("마지막 페이지 = " + endPage);

            DataSet voPaging = new DataSet("paging");

            // DataSet Column setting
            voPaging.addColumn("pageNum", DataTypes.INT, 4);
            voPaging.addColumn("startPage", DataTypes.INT, 4);
            voPaging.addColumn("endPage", DataTypes.STRING, 256);
            voPaging.addColumn("totalPage", DataTypes.INT, 4);
            voPaging.addColumn("count", DataTypes.INT, 4);
            voPaging.addColumn("contentPerPage", DataTypes.INT, 4);
            voPaging.addColumn("pageSize", DataTypes.INT, 4);
            voPaging.addColumn("startCount", DataTypes.INT, 4);
            voPaging.addColumn("endCount", DataTypes.INT, 4);
            voPaging.addColumn("keyField", DataTypes.STRING, 256);
            voPaging.addColumn("keyWord", DataTypes.STRING, 256);
            voPaging.newRow();

            voPaging.set(0, "pageNum", pageNum);
            voPaging.set(0, "startPage", startPage);
            voPaging.set(0, "endPage", endPage);
            voPaging.set(0, "count", totalCount);
            voPaging.set(0, "totalPage", totalPage);
            voPaging.set(0, "contentPerPage", contentPerPage);
            voPaging.set(0, "pageSize", pageSize);
            voPaging.set(0, "startCount", startCount);
            voPaging.set(0, "endCount", endCount);
            voPaging.set(0, "keyWord", keyWord);
            voPaging.set(0, "keyField", keyField);

            // pdata.addDataSet(vo);
            // pdata.addDataSet(voPaging);
            datasetlist.add(vo);
            datasetlist.add(voPaging);
            pdata.setDataSetList(datasetlist);
            // set the ErrorCode and ErrorMsg about success
            nErrorCode = 0;
            strErrorMsg = "SUCC";

        } catch (Throwable th) {
            // set the ErrorCode and ErrorMsg about fail
            nErrorCode = -1;
            strErrorMsg = th.getMessage();

        } // try

        // save the ErrorCode and ErrorMsg for sending Client
        VariableList varList = pdata.getVariableList();

        varList.add("ErrorCode", nErrorCode);
        varList.add("ErrorMsg", strErrorMsg);

        // send the result data(XML) to Client
        HttpPlatformResponse res = new HttpPlatformResponse(response, PlatformType.CONTENT_TYPE_XML, "UTF-8");
        res.setData(pdata);
        res.sendData();
    }
}
