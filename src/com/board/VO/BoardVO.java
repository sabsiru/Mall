package com.board.VO;

import java.sql.Date;

public class BoardVO {
	private int seq;
	private String name;
	private String title;
	private String content;
	private String pass;
	private int hit;
	private Date regdate;
	private int ref;
	private int step;
	private int depth;
	private int fileno;
	private String orgfile;
	private String storefile;
	private String filepath;
	public int getSeq() {
		return seq;
	}
	public void setSeq(int seq) {
		this.seq = seq;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	public int getHit() {
		return hit;
	}
	public void setHit(int hit) {
		this.hit = hit;
	}
	public Date getRegdate() {
		return regdate;
	}
	public void setRegdate(Date regdate) {
		this.regdate = regdate;
	}
	public int getRef() {
		return ref;
	}
	public void setRef(int ref) {
		this.ref = ref;
	}
	public int getStep() {
		return step;
	}
	public void setStep(int step) {
		this.step = step;
	}
	public int getDepth() {
		return depth;
	}
	public void setDepth(int depth) {
		this.depth = depth;
	}
	public String getOrgfile() {
		return orgfile;
	}
	public void setOrgfile(String orgfile) {
		this.orgfile = orgfile;
	}
	public int getFileno() {
		return fileno;
	}
	public void setFileno(int fileno) {
		this.fileno = fileno;
	}
	public String getFilepath() {
		return filepath;
	}
	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}
	public String getStorefile() {
		return storefile;
	}
	public void setStorefile(String storefile) {
		this.storefile = storefile;
	}
	
	
	
	
	

	
}
