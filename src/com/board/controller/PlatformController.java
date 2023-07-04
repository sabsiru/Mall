package com.board.controller;

import java.io.*;
import java.util.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

@Controller
public class PlatformController {
	@SuppressWarnings("unused")
	private Logger log = Logger.getLogger(getClass());
//	private int pageSize = 10;
//	private int blockCount = 10;

	@Autowired
	private BoardDao boardDao;

	@RequestMapping({ "/board/list.do" })
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
			DataSet vo = new DataSet("BoardVO");
			// DataSet Column setting
			vo.addColumn("seq", DataTypes.INT, 4);
			vo.addColumn("name", DataTypes.STRING, 50);
			vo.addColumn("title", DataTypes.STRING, 100);
			vo.addColumn("content", DataTypes.STRING, 400);
			vo.addColumn("hit", DataTypes.INT, 4);
			vo.addColumn("regdate", DataTypes.DATE, 20);
			vo.addColumn("ref", DataTypes.INT, 256);
			vo.addColumn("step", DataTypes.INT, 256);
			vo.addColumn("depth", DataTypes.INT, 256);

			// DAO
			BoardVO boardVO;
			HashMap<String, Object> map = new HashMap();

			// �˻� ����
			map.put("keyField", keyField);
			map.put("keyWord", keyWord);

			// ����¡
			map.put("startCount", startCount);
			map.put("endCount", endCount);

			// �Խñ� �� ����
			totalCount = boardDao.getCount(map);
			System.out.println("totalcount = " + totalCount);
			// strErrorMsg = 0;
			List<BoardVO> list = null;
			if (totalCount > 0) {
				list = this.boardDao.list(map);
			} else {
				list = Collections.emptyList();
			}
			// ResultSet -> Show the Row sets (XML) : browser
			for (int i = 0; i < list.size(); i++) {

				int row = vo.newRow(); // new Row feed

				boardVO = list.get(i);
				vo.set(row, "seq", boardVO.getSeq());
				vo.set(row, "name", boardVO.getName());
				vo.set(row, "title", boardVO.getTitle());
				vo.set(row, "content", boardVO.getContent());
				vo.set(row, "hit", boardVO.getHit());
				vo.set(row, "regdate", boardVO.getRegdate());
				vo.set(row, "ref", boardVO.getRef());
				vo.set(row, "step", boardVO.getStep());
				vo.set(row, "depth", boardVO.getDepth());
				// name�� ������ Ȯ��

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
//            System.out.println("�˻��� �� ���� = " + totalCount);
//            System.out.println("���� ������ = " + pageNum);
//            System.out.println("���������� �� ����  = " + pageSize);
//            System.out.println("�Ѹ��� �� ������ ���� = " + contentPerPage);
//            System.out.println("��ü ������ ������ = " + totalPage);
//            System.out.println("���� ī��Ʈ  = " + startCount);
//            System.out.println("������ ī��Ʈ = " + endCount);
//            System.out.println("���� ������ = " + startPage);
//            System.out.println("������ ������ = " + endPage);

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

	// �� �ۼ�
	@RequestMapping("/board/write.do")
	public void write(HttpServletRequest request, HttpServletResponse response) throws PlatformException, IOException {
		PlatformData pdata = new PlatformData();

		int nErrorCode = 0;
		String strErrorMsg = "START";

		// receive client request
		// not need to receive

		// create HttpPlatformRequest for receive data from client
		HttpPlatformRequest req = new HttpPlatformRequest(request);

		req.receiveData();
		pdata = req.getData();

		DataSet ds = pdata.getDataSet("article");

		System.out.println("name : " + ds.getString(0, "name"));
		System.out.println("title : " + ds.getString(0, "title"));
		System.out.println("content : " + ds.getString(0, "content"));
		System.out.println("pass : " + ds.getString(0, "pass"));

		String title = ds.getString(0, "title");
		String name = ds.getString(0, "name");
		String content = ds.getString(0, "content");
		String password = ds.getString(0, "pass");
		int seq = 0;
		// ��ȿ�� �˻�
		if (title == null || title.trim().isEmpty() || title.contains(" ")) {
			nErrorCode = -2;
			strErrorMsg = "������ �Է��ϼ���.";
		} else if (name == null || name.trim().isEmpty() || name.contains(" ")) {
			nErrorCode = -3;
			strErrorMsg = "�ۼ��� �̸��� �Է��ϼ���.";
		} else if (content == null || content.trim().isEmpty()) {
			nErrorCode = -4;
			strErrorMsg = "������ �Է��ϼ���.";
		} else if (password == null || password.trim().isEmpty()) {
			nErrorCode = -5;
			strErrorMsg = "��й�ȣ�� �Է��ϼ���.";
		} else if (title.length() > 100) {
			nErrorCode = -6;
			strErrorMsg = "������ 100�� ���Ϸ� �ۼ����ּ���.";
		} else if (name.length() > 50) {
			nErrorCode = -7;
			strErrorMsg = "�ۼ��ڴ� 50�� ���Ϸ� �ۼ����ּ���.";
		} else if (content.length() > 400) {
			nErrorCode = -8;
			strErrorMsg = "������ 400�� ���Ϸ� �ۼ����ּ���.";
		} else if (password.length() > 10) {
			nErrorCode = -9;
			strErrorMsg = "��й�ȣ�� 10�� ���Ϸ� �Է����ּ���.";
		} else {

			BoardVO vo = new BoardVO();

			vo.setName(ds.getString(0, "name"));
			vo.setPass(ds.getString(0, "pass"));
			vo.setTitle(ds.getString(0, "title"));
			vo.setContent(ds.getString(0, "content"));
			seq = this.boardDao.getSeq();
			vo.setSeq(seq);
			System.out.println("seq = " + seq);
			this.boardDao.insert(vo);

			// set the ErrorCode and ErrorMsg about success
			nErrorCode = 0;
			strErrorMsg = "SUCC";

		}

		// save the ErrorCode and ErrorMsg for sending Client
		VariableList varList = pdata.getVariableList();

		if (nErrorCode != 0) {
			varList.add("ErrorCode", nErrorCode);
		} else {
			varList.add("ErrorCode", seq);
		}

		varList.add("ErrorMsg", strErrorMsg);

		// send the result data(XML) to Client
		HttpPlatformResponse res = new HttpPlatformResponse(response, PlatformType.CONTENT_TYPE_XML, "UTF-8");
		res.setData(pdata);
		res.sendData(); // Send Data

	}

	// ���� ���ε�
	@RequestMapping("/board/fileUpload.do")
	public void fileUpload(HttpServletRequest request, HttpServletResponse response)
			throws PlatformException, IOException {
		System.out.println("fileupload.do");
		int nErrorCode = 0;
		String sHeader = request.getHeader("Content-Type");
		if (sHeader == null) {
			return;
		}
		request.setCharacterEncoding("utf-8");
		String sRealPath = request.getSession().getServletContext().getRealPath("/");
		String sPath = request.getParameter("PATH");
		String seq = request.getParameter("seq");

		System.out.println("seq= " + seq);
		String sUpPath = sRealPath + sPath;
		int nMaxSize = 500 * 1024 * 1024; // �ִ� ���ε� ���� ũ�� 500MB(�ް�)�� ����
		System.out.println(sUpPath);
		PlatformData resData = new PlatformData();
		VariableList resVarList = resData.getVariableList();
		HttpPlatformRequest req = new HttpPlatformRequest(request);

		String sMsg = " A ";

		MultipartRequest multi = new MultipartRequest(request, sUpPath, nMaxSize, "utf-8",
				new DefaultFileRenamePolicy());
		Enumeration files = multi.getFileNames();

		sMsg += "B ";
		DataSet ds = new DataSet("fileuploadVO");
		System.out.println("parent");
		ds.addColumn(new ColumnHeader("seq", DataTypes.INT));
		ds.addColumn(new ColumnHeader("fileno", DataTypes.INT));
		ds.addColumn(new ColumnHeader("orgfile", DataTypes.STRING));
		ds.addColumn(new ColumnHeader("storefile", DataTypes.STRING));
		ds.addColumn(new ColumnHeader("filepath", DataTypes.STRING));

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
			stype = multi.getContentType(sName);

			// �ý��ۿ� ����� �̸�(�ٲ��̸�)
			System.out.println("child");
			System.out.println(sFName);

			// ���� �̸�
			System.out.println(multi.getOriginalFileName(sName));
			// fileno ds���� ��������

			String fileno = multi.getParameter("fileno");
			System.out.println("fileno= " + fileno);
			String orgfile = multi.getOriginalFileName(sName);
			String storefile = sFName;
			String filepath = sUpPath;

			int nRow = ds.newRow();

			ds.set(nRow, "seq", seq);
			ds.set(nRow, "fileno", fileno);
			ds.set(nRow, "orgfile", orgfile);
			ds.set(nRow, "storefile", storefile);
			ds.set(nRow, "filepath", filepath);

			BoardVO vo = new BoardVO();
			int intSeq = Integer.parseInt(seq);

			vo.setSeq(intSeq);

			vo.setOrgfile(orgfile);
			vo.setStorefile(storefile);
			vo.setFilepath(filepath);

			this.boardDao.fileUpload(vo);

			f = multi.getFile(sName);

			sMsg += nRow + " ";
		}

		resData.addDataSet(ds);
		resVarList.add("ErrorCode", nErrorCode);
		resVarList.add("ErrorMsg", sUpPath + "/" + sFName);

		HttpPlatformResponse res = new HttpPlatformResponse(response);
		res.setData(resData);
		res.sendData();
	}

	@RequestMapping("/board/fileinfo.do")
	public void fileInfo(HttpServletRequest request, HttpServletResponse response)
			throws PlatformException, IOException {
		int nErrorCode = 0;
		String sHeader = request.getHeader("Content-Type");
		if (sHeader == null) {
			return;
		}
		request.setCharacterEncoding("utf-8");
		String sRealPath = request.getSession().getServletContext().getRealPath("/");
		String sPath = request.getParameter("PATH");
		String seq = request.getParameter("seq");

		System.out.println("seq= " + seq);
		String sUpPath = sRealPath + sPath;
		int nMaxSize = 500 * 1024 * 1024; // �ִ� ���ε� ���� ũ�� 500MB(�ް�)�� ����
		System.out.println(sUpPath);
		PlatformData resData = new PlatformData();
		VariableList resVarList = resData.getVariableList();
		HttpPlatformRequest req = new HttpPlatformRequest(request);

		String sMsg = " A ";

		MultipartRequest multi = new MultipartRequest(request, sUpPath, nMaxSize, "utf-8",
				new DefaultFileRenamePolicy());
		Enumeration files = multi.getFileNames();

		sMsg += "B ";
		DataSet ds = new DataSet("fileuploadVO");
		ds.addColumn(new ColumnHeader("seq", DataTypes.INT));
		ds.addColumn(new ColumnHeader("fileno", DataTypes.INT));
		ds.addColumn(new ColumnHeader("orgfile", DataTypes.STRING));
		ds.addColumn(new ColumnHeader("storefile", DataTypes.STRING));
		ds.addColumn(new ColumnHeader("filepath", DataTypes.STRING));

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
			stype = multi.getContentType(sName);

			// �ý��ۿ� ����� �̸�(�ٲ��̸�)
			System.out.println("child");
			System.out.println(sFName);

			// ���� �̸�
			System.out.println(multi.getOriginalFileName(sName));
			// fileno ds���� ��������

			String fileno = multi.getParameter("fileno");
			System.out.println("fileno= " + fileno);
			String orgfile = multi.getOriginalFileName(sName);
			String storefile = sFName;
			String filepath = sUpPath;

			int nRow = ds.newRow();

			ds.set(nRow, "seq", seq);
			ds.set(nRow, "fileno", fileno);
			ds.set(nRow, "orgfile", orgfile);
			ds.set(nRow, "storefile", storefile);
			ds.set(nRow, "filepath", filepath);

			BoardVO vo = new BoardVO();
			int intSeq = Integer.parseInt(seq);

			vo.setSeq(intSeq);

			vo.setOrgfile(orgfile);
			vo.setStorefile(storefile);
			vo.setFilepath(filepath);

			this.boardDao.fileUpload(vo);

			f = multi.getFile(sName);

			sMsg += nRow + " ";
		}

		resData.addDataSet(ds);
		resVarList.add("ErrorCode", nErrorCode);
		resVarList.add("ErrorMsg", sUpPath + "/" + sFName);

		HttpPlatformResponse res = new HttpPlatformResponse(response);
		res.setData(resData);
		res.sendData();

	}

	// �� �б�
	@RequestMapping("/board/read.do")
	public void read(HttpServletRequest request, HttpServletResponse response) throws PlatformException {
		PlatformData pdata = new PlatformData();

		int nErrorCode = 0;
		String strErrorMsg = "START";
		try {

			// create HttpPlatformRequest for receive data from client
			HttpPlatformRequest req = new HttpPlatformRequest(request);
			System.out.println("read");
			req.receiveData();
			pdata = req.getData();
			DataSetList datasetlist = new DataSetList();

			// DataSet
			DataSet bVo = new DataSet("BoardVO");
			// �۷ι� ���� seq ��������
			DataSet ds1 = pdata.getDataSet("seqVO");

			// DataSet Column setting
			bVo.addColumn("seq", DataTypes.INT, 4);
			bVo.addColumn("name", DataTypes.STRING, 50);
			bVo.addColumn("title", DataTypes.STRING, 100);
			bVo.addColumn("content", DataTypes.STRING, 400);
			bVo.addColumn("ref", DataTypes.INT, 4);
			bVo.addColumn("step", DataTypes.INT, 4);
			bVo.addColumn("depth", DataTypes.INT, 4);

			int seq = Integer.parseInt(ds1.getString(0, "seq"));

			BoardVO vo1;

			// ResultSet -> Show the Row sets (XML) : browser

			vo1 = this.boardDao.read(seq);
			this.boardDao.hitIncrease(seq);

			bVo.newRow(); // new Row feed

			// dataset boardVO�� ������ �ֱ�
			bVo.set(0, "seq", vo1.getSeq());
			bVo.set(0, "name", vo1.getName());
			bVo.set(0, "title", vo1.getTitle());
			bVo.set(0, "content", vo1.getContent());
			bVo.set(0, "ref", vo1.getRef());
			bVo.set(0, "step", vo1.getStep());
			bVo.set(0, "depth", vo1.getDepth());
			System.out.println("ref =" + vo1.getRef());

			// ==============================================================//
			DataSet fileuploadVO = new DataSet("fileuploadVO");
			fileuploadVO.addColumn("seq", DataTypes.INT, 4);
			fileuploadVO.addColumn("fileno", DataTypes.INT, 4);
			fileuploadVO.addColumn("orgfile", DataTypes.STRING, 100);
			fileuploadVO.addColumn("storefile", DataTypes.STRING, 100);
			fileuploadVO.addColumn("filepath", DataTypes.STRING, 100);

			BoardVO fileVO = new BoardVO();

			List<BoardVO> selectFile = this.boardDao.selectFile(seq);

			for (int i = 0; i < selectFile.size(); i++) {
				int row = fileuploadVO.newRow();

				fileVO = selectFile.get(i);
				fileuploadVO.set(row, "seq", vo1.getSeq());
				fileuploadVO.set(row, "fileno", fileVO.getFileno());
				fileuploadVO.set(row, "orgfile", fileVO.getOrgfile());
				fileuploadVO.set(row, "storefile", fileVO.getStorefile());
				fileuploadVO.set(row, "filepath", fileVO.getFilepath());
			}

			datasetlist.add(bVo);
			datasetlist.add(fileuploadVO);

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

	// ��й�ȣ Ȯ��
	@RequestMapping("/board/passCheck.do")
	public void passCheck(HttpServletRequest request, HttpServletResponse response) throws PlatformException {
		PlatformData pdata = new PlatformData();

		int nErrorCode = 0;
		String strErrorMsg = "START";
		String checkMsg = "";
		try { // create HttpPlatformRequest for receive data from client
			HttpPlatformRequest req = new HttpPlatformRequest(request);
			req.receiveData();
			pdata = req.getData();

			DataSet ds = pdata.getDataSet("BoardVO");
			DataSet ps = pdata.getDataSet("passVO");
			System.out.println("seq = " + ps.getString(0, "seq"));
			String pass = ps.getString(0, "pass");
			System.out.println("pass = " + pass);

			// db���� �н����尪 �����ͼ� pass2�� �ֱ�
			BoardVO vo = new BoardVO();
			vo.setSeq(Integer.parseInt(ps.getString(0, "seq")));
			BoardVO vo2 = this.boardDao.read(vo.getSeq());
			String pass2 = vo2.getPass();
			System.out.println("pass2 = " + pass2);

			if (pass.equals(pass2)) {
				nErrorCode = 0;
				checkMsg = "SUCC";
			} else {
				nErrorCode = -1;
				checkMsg = "FAIL";
			}

		} catch (Throwable th) {
			// set the ErrorCode and ErrorMsg about fail
			nErrorCode = -1;
			strErrorMsg = th.getMessage();

		} // try
		VariableList varList = pdata.getVariableList();

		varList.add("ErrorCode", nErrorCode);
		varList.add("ErrorMsg", strErrorMsg);

		// send the result data(XML) to Client
		HttpPlatformResponse res = new HttpPlatformResponse(response, PlatformType.CONTENT_TYPE_XML, "UTF-8");
		res.setData(pdata);
		res.sendData();
	}

	// �� ����
	@RequestMapping("/board/delete.do")
	public void delete(HttpServletRequest request, HttpServletResponse response) throws PlatformException {
		PlatformData pdata = new PlatformData();

		int nErrorCode = 0;
		String strErrorMsg = "START";

		try {

			// create HttpPlatformRequest for receive data from client
			HttpPlatformRequest req = new HttpPlatformRequest(request);
			System.out.println("delete start");
			req.receiveData();
			pdata = req.getData();
			DataSetList datasetlist = new DataSetList();

			System.out.println("2");

			// DataSet
			DataSet vo = new DataSet("BoardVO");
			// �۷ι� ���� seq ��������
			DataSet ds1 = pdata.getDataSet("seqVO");

			// seq���� �����ͼ� delete sql �����ϱ�

			int seq = Integer.parseInt(ds1.getString(0, "seq"));
			System.out.println("first seq = " + seq);

			// ���� ���� ����
			// seq���� �޾ƿͼ� boardfile ���̺��� seq���� �ش��ϴ� ���������� �����´�.
			List<BoardVO> selectFile = this.boardDao.selectFile(seq);
			// db�� �ִ� filepath�� storefile�� �����ͼ� ������ �����Ѵ�.
			for (int i = 0; i < selectFile.size(); i++) {
				String path = selectFile.get(i).getFilepath();
				String name = selectFile.get(i).getStorefile();
				System.out.println("path = " + path);
				System.out.println("name = " + name);

				File file = new File(path + "/" + name);
				if (file.exists() == true) {
					file.delete();
				}
			}
			// boardfile ���̺��� seq���� �ش��ϴ� ���������� �����Ѵ�.
			this.boardDao.fileDelete(seq);

			this.boardDao.delete(seq);

			datasetlist.add(vo);

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

	// �� ����
	@RequestMapping("/board/modify.do")
	public void modify(HttpServletRequest request, HttpServletResponse response) throws PlatformException, IOException {
		// Xplatform basic object creation
		System.out.println("modify");
		PlatformData pdata = new PlatformData();

		int nErrorCode = 0;
		String strErrorMsg = "START";

		HttpPlatformRequest req = new HttpPlatformRequest(request);

		req.receiveData();
		pdata = req.getData();

		DataSet ds = pdata.getDataSet("BoardVO");

		System.out.println("title : " + ds.getString(0, "title"));
		System.out.println("content : " + ds.getString(0, "content"));

		// DAO
		BoardVO vo = new BoardVO();
		int seq = Integer.parseInt(ds.getString(0, "seq"));
		vo.setSeq(seq);
		System.out.println("seq = " + seq);

		String title = ds.getString(0, "title");
		String content = ds.getString(0, "content");
		if (title == null || title.trim().isEmpty() || title.contains(" ")) {
			nErrorCode = -2;
			strErrorMsg = "������ �Է��ϼ���.";
		} else if (content == null || content.trim().isEmpty()) {
			nErrorCode = -4;
			strErrorMsg = "������ �Է��ϼ���.";
		} else if (title.length() > 100) {
			nErrorCode = -6;
			strErrorMsg = "������ 100�� ���Ϸ� �ۼ����ּ���.";
		} else if (content.length() > 400) {
			nErrorCode = -8;
			strErrorMsg = "������ 400�� ���Ϸ� �ۼ����ּ���.";
		} else {
			vo.setTitle(ds.getString(0, "title"));
			vo.setContent(ds.getString(0, "content"));
			// ��ȿ�� �˻�

			this.boardDao.modify(vo);
			ds.set(0, "title", vo.getTitle());
			ds.set(0, "content", vo.getContent());

			// ===================== ���� ���� ���� =====================//
			DataSet ds1 = pdata.getDataSet("deleteINFO");
			System.out.println("deleteFile");
			Map<String, Object> map = new HashMap<String, Object>();

			if (ds1.getRowCount() > 0) {

				for (int i = 0; i < ds1.getRowCount(); i++) {
					String filepath = ds1.getString(i, "filepath");
					String storefile = ds1.getString(i, "storefile");
					System.out.println("modify path = " + filepath);
					System.out.println("modify name = " + storefile);

					File file = new File(filepath + "/" + storefile);
					if (file.exists() == true) {
						file.delete();
						map.put("filepath", filepath);
						map.put("storefile", storefile);
						map.put("seq", seq);
						this.boardDao.fileModifyDelete(map);
						/* this.boardDao.fileDelete(seq); */
					}
				}
			}
			// =======================================================//

			// set the ErrorCode and ErrorMsg about success
			nErrorCode = 0;
			strErrorMsg = "SUCC";
		}
		// try

		// save the ErrorCode and ErrorMsg for sending Client
		VariableList varList = pdata.getVariableList();
		if (nErrorCode != 0) {
			varList.add("ErrorCode", nErrorCode);
		} else {
			varList.add("ErrorCode", seq);
		}
		varList.add("ErrorMsg", strErrorMsg);

		// send the result data(XML) to Client
		HttpPlatformResponse res = new HttpPlatformResponse(response, PlatformType.CONTENT_TYPE_XML, "UTF-8");
		res.setData(pdata);
		res.sendData(); // Send Data

	}

	// ��� �ۼ�
	@RequestMapping("/board/reply.do")
	public void reply(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// Xplatform basic object creation
		System.out.println("reply");
		PlatformData pdata = new PlatformData();

		int nErrorCode = 0;
		String strErrorMsg = "START";

		// receive client request
		// not need to receive

		// create HttpPlatformRequest for receive data from client
		HttpPlatformRequest req = new HttpPlatformRequest(request);

		req.receiveData();
		pdata = req.getData();

		DataSet ds = pdata.getDataSet("BoardVO");
		DataSet ds1 = pdata.getDataSet("seqVO");
		// read.do���� ������ �������� seq���� �����´�.

		int seq = Integer.parseInt(ds1.getString(0, "seq"));

		System.out.println("seq = " + seq);

		// ref,step,depth�� ����
		BoardVO vo1;
		vo1 = this.boardDao.read(seq);

		BoardVO parent = new BoardVO();
		int ref = vo1.getRef();
		int step = vo1.getStep();
		int depth = vo1.getDepth();
		System.out.println("ref = " + ref);
		System.out.println("step = " + step);
		System.out.println("depth = " + depth);

		// ref���� seq���� ������ �θ���� ����̱� ������ step, depth�� 1�� �ο��ϰ� replyUpdate�� �Ѵ�.
		if (ref == seq) {
			step = 1;
			depth = 1;
			this.boardDao.replyUpdate(vo1);
		}
		// ref���� seq���� �ٸ��� replyUpdate�� �����ϰ� step�� depth�� ���� 1 �� ������Ų��.
		else {
			this.boardDao.replyUpdate(vo1);
			step++;
			depth++;
		}
		String title = ds.getString(0, "title");
		String name = ds.getString(0, "name");
		String content = ds.getString(0, "content");
		String password = ds.getString(0, "pass");
		if (title == null || title.trim().isEmpty() || title.contains(" ")) {
			nErrorCode = -2;
			strErrorMsg = "������ �Է��ϼ���.";
		} else if (name == null || name.trim().isEmpty() || name.contains(" ")) {
			nErrorCode = -3;
			strErrorMsg = "�ۼ��� �̸��� �Է��ϼ���.";
		} else if (content == null || content.trim().isEmpty()) {
			nErrorCode = -4;
			strErrorMsg = "������ �Է��ϼ���.";
		} else if (password == null || password.trim().isEmpty()) {
			nErrorCode = -5;
			strErrorMsg = "��й�ȣ�� �Է��ϼ���.";
		} else if (title.length() > 100) {
			nErrorCode = -6;
			strErrorMsg = "������ 100�� ���Ϸ� �ۼ����ּ���.";
		} else if (name.length() > 50) {
			nErrorCode = -7;
			strErrorMsg = "�ۼ��ڴ� 50�� ���Ϸ� �ۼ����ּ���.";
		} else if (content.length() > 400) {
			nErrorCode = -8;
			strErrorMsg = "������ 400�� ���Ϸ� �ۼ����ּ���.";
		} else if (password.length() > 10) {
			nErrorCode = -9;
			strErrorMsg = "��й�ȣ�� 10�� ���Ϸ� �Է����ּ���.";
		} else {
			BoardVO vo = new BoardVO();
			seq = this.boardDao.getSeq();
			vo.setName(ds.getString(0, "name"));
			vo.setPass(ds.getString(0, "pass"));
			vo.setTitle(ds.getString(0, "title"));
			vo.setContent(ds.getString(0, "content"));
			vo.setSeq(seq);
			vo.setRef(ref);
			vo.setStep(step);
			vo.setDepth(depth);

			this.boardDao.reply(vo);

			// set the ErrorCode and ErrorMsg about success
			nErrorCode = 0;
			strErrorMsg = "SUCC";
		}

		// save the ErrorCode and ErrorMsg for sending Client
		VariableList varList = pdata.getVariableList();
		if (nErrorCode != 0) {
			varList.add("ErrorCode", nErrorCode);
		} else {
			varList.add("ErrorCode", seq);
		}
		varList.add("ErrorMsg", strErrorMsg);

		// send the result data(XML) to Client
		HttpPlatformResponse res = new HttpPlatformResponse(response, PlatformType.CONTENT_TYPE_XML, "UTF-8");
		res.setData(pdata);
		res.sendData(); // Send Data
	}

	// ���� �ٿ�ε�
	@RequestMapping("/board/download.do")
	public void download(HttpServletRequest request, HttpServletResponse response) throws IOException {
		System.out.println("download");
		String sRealPath = request.getSession().getServletContext().getRealPath("upload");

		String sPath = sRealPath;
		String sName = request.getParameter("orgfile");
		String sFile = new String(sName.getBytes("iso8859-1"), "UTF-8");
		// String sFile=sName;
		byte[] buffer = new byte[1024];

		BufferedInputStream in_stream = null;
		File fis = new File(sPath + "/" + sFile);
		System.out.println("sfile=" + sFile);
		System.out.println("spath=" + sPath);
		if (fis.exists()) {
			try {
				response.setContentType("application/octet-stream;charset=UTF-8");
				response.setHeader("Content-Disposition", "attachment;filename=" + sFile);
				ServletOutputStream out_stream = response.getOutputStream();
				in_stream = new BufferedInputStream(new FileInputStream(fis));
				int nCnt = 0;
				while ((nCnt = in_stream.read(buffer, 0, 1024)) != -1) {
					out_stream.write(buffer, 0, nCnt);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (in_stream != null) {
					try {
						in_stream.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} else {
			response.sendRedirect("unknownfile");
		}
	}
}