package com.example.qct.util;

public class Tpdxx implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer pdid;
	private Short jjrid;
	private String jjrtel;
	private String jjrname;
	private String jjraddr;
	private String sjrtel;
	private String sjrname;
	private String sjraddr;
	private Short part;
	private String lrrq;
	private String lrsj;
	private String xfrq;
	private String opname;
	private String xfsj;
	private Integer lsy;
	private String memo;
	private String jsrq;
	private String jssj;
	private String fkrq;
	private String fksj;
	private Short flag;

	// Constructors

	/** default constructor */
	public Tpdxx() {
	}

	/** full constructor */
	public Tpdxx(Short jjrid, String jjrtel, String jjrname, String jjraddr,
			Short part, String lrrq, String lrsj, String xfrq, String opname,
			String xfsj, Integer lsy, String memo, String jsrq, String jssj,
			String fkrq, String fksj, Short flag) {
		this.jjrid = jjrid;
		this.jjrtel = jjrtel;
		this.jjrname = jjrname;
		this.jjraddr = jjraddr;
		this.part = part;
		this.lrrq = lrrq;
		this.lrsj = lrsj;
		this.xfrq = xfrq;
		this.opname = opname;
		this.xfsj = xfsj;
		this.lsy = lsy;
		this.memo = memo;
		this.jsrq = jsrq;
		this.jssj = jssj;
		this.fkrq = fkrq;
		this.fksj = fksj;
		this.flag = flag;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPdid() {
		return pdid;
	}

	public void setPdid(Integer pdid) {
		this.pdid = pdid;
	}

	public Short getJjrid() {
		return this.jjrid;
	}

	public void setJjrid(Short jjrid) {
		this.jjrid = jjrid;
	}

	public String getJjrtel() {
		return this.jjrtel;
	}

	public void setJjrtel(String jjrtel) {
		this.jjrtel = jjrtel;
	}

	public String getJjrname() {
		return this.jjrname;
	}

	public void setJjrname(String jjrname) {
		this.jjrname = jjrname;
	}

	public String getJjraddr() {
		return this.jjraddr;
	}

	public void setJjraddr(String jjraddr) {
		this.jjraddr = jjraddr;
	}

	public Short getPart() {
		return this.part;
	}

	public void setPart(Short part) {
		this.part = part;
	}

	public String getLrrq() {
		return this.lrrq;
	}

	public void setLrrq(String lrrq) {
		this.lrrq = lrrq;
	}

	public String getLrsj() {
		return this.lrsj;
	}

	public void setLrsj(String lrsj) {
		this.lrsj = lrsj;
	}

	public String getXfrq() {
		return this.xfrq;
	}

	public void setXfrq(String xfrq) {
		this.xfrq = xfrq;
	}

	public String getOpname() {
		return this.opname;
	}

	public void setOpname(String opname) {
		this.opname = opname;
	}

	public String getXfsj() {
		return this.xfsj;
	}

	public void setXfsj(String xfsj) {
		this.xfsj = xfsj;
	}

	public Integer getLsy() {
		return this.lsy;
	}

	public void setLsy(Integer lsy) {
		this.lsy = lsy;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getJsrq() {
		return this.jsrq;
	}

	public void setJsrq(String jsrq) {
		this.jsrq = jsrq;
	}

	public String getJssj() {
		return this.jssj;
	}

	public void setJssj(String jssj) {
		this.jssj = jssj;
	}

	public String getFkrq() {
		return this.fkrq;
	}

	public void setFkrq(String fkrq) {
		this.fkrq = fkrq;
	}

	public String getFksj() {
		return this.fksj;
	}

	public void setFksj(String fksj) {
		this.fksj = fksj;
	}

	public Short getFlag() {
		return this.flag;
	}

	public void setFlag(Short flag) {
		this.flag = flag;
	}

	public String getSjrtel() {
		return sjrtel;
	}

	public void setSjrtel(String sjrtel) {
		this.sjrtel = sjrtel;
	}

	public String getSjrname() {
		return sjrname;
	}

	public void setSjrname(String sjrname) {
		this.sjrname = sjrname;
	}

	public String getSjraddr() {
		return sjraddr;
	}

	public void setSjraddr(String sjraddr) {
		this.sjraddr = sjraddr;
	}

	@Override
	public String toString() {
		return "Tpdxx [id=" + id + ", pdid=" + pdid + ", jjrid=" + jjrid + ", jjrtel=" + jjrtel + ", jjrname=" + jjrname + ", jjraddr=" + jjraddr + ", sjrtel=" + sjrtel
				+ ", sjrname=" + sjrname + ", sjraddr=" + sjraddr + ", part=" + part + ", lrrq=" + lrrq + ", lrsj=" + lrsj + ", xfrq=" + xfrq + ", opname=" + opname + ", xfsj="
				+ xfsj + ", lsy=" + lsy + ", memo=" + memo + ", jsrq=" + jsrq + ", jssj=" + jssj + ", fkrq=" + fkrq + ", fksj=" + fksj + ", flag=" + flag + "]";
	}

	


}