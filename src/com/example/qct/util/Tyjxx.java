package com.example.qct.util;

public class Tyjxx implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer pdid;
	private String tm;
	private Short jjrid;
	private String jjrtel;
	private String jjrname;
	private String jjraddr;
	private String sjrtel;
	private String sjrname;
	private String sjraddr;
	private String zl;
	private String zf;
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
	private String bjje;
	private String bjf;
	private String dshk;
	private String sjrff;
	private String qtf;
	private String zfy;
	private String jz;
	private String ywzl;
	private String fkrq;
	private String fksj;
	private Short flag;
	private int fjfw;
	private String hztm;

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

	public String getZl() {
		return zl;
	}

	public void setZl(String zl) {
		this.zl = zl;
	}

	public String getZf() {
		return zf;
	}

	public void setZf(String zf) {
		this.zf = zf;
	}

	public Short getJjrid() {
		return jjrid;
	}

	public void setJjrid(Short jjrid) {
		this.jjrid = jjrid;
	}

	public String getJjrtel() {
		return jjrtel;
	}

	public void setJjrtel(String jjrtel) {
		this.jjrtel = jjrtel;
	}

	public String getJjrname() {
		return jjrname;
	}

	public void setJjrname(String jjrname) {
		this.jjrname = jjrname;
	}

	public String getJjraddr() {
		return jjraddr;
	}

	public void setJjraddr(String jjraddr) {
		this.jjraddr = jjraddr;
	}

	public Short getPart() {
		return part;
	}

	public void setPart(Short part) {
		this.part = part;
	}

	public String getLrrq() {
		return lrrq;
	}

	public void setLrrq(String lrrq) {
		this.lrrq = lrrq;
	}

	public String getLrsj() {
		return lrsj;
	}

	public void setLrsj(String lrsj) {
		this.lrsj = lrsj;
	}

	public String getXfrq() {
		return xfrq;
	}

	public void setXfrq(String xfrq) {
		this.xfrq = xfrq;
	}

	public String getOpname() {
		return opname;
	}

	public void setOpname(String opname) {
		this.opname = opname;
	}

	public String getXfsj() {
		return xfsj;
	}

	public void setXfsj(String xfsj) {
		this.xfsj = xfsj;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getJsrq() {
		return jsrq;
	}

	public void setJsrq(String jsrq) {
		this.jsrq = jsrq;
	}

	public String getJssj() {
		return jssj;
	}

	public void setJssj(String jssj) {
		this.jssj = jssj;
	}

	public String getFkrq() {
		return fkrq;
	}

	public void setFkrq(String fkrq) {
		this.fkrq = fkrq;
	}

	public String getFksj() {
		return fksj;
	}

	public void setFksj(String fksj) {
		this.fksj = fksj;
	}

	public Short getFlag() {
		return flag;
	}

	public void setFlag(Short flag) {
		this.flag = flag;
	}

	

	public Tyjxx() {
		super();
	}

	@Override
	public String toString() {
		return "Tyjxx [id=" + id + ", pdid=" + pdid + ", tm=" + tm + ", jjrid=" + jjrid + ", jjrtel=" + jjrtel
				+ ", jjrname=" + jjrname + ", jjraddr=" + jjraddr + ", sjrtel=" + sjrtel + ", sjrname=" + sjrname
				+ ", sjraddr=" + sjraddr + ", zl=" + zl + ", zf=" + zf + ", part=" + part + ", lrrq=" + lrrq
				+ ", lrsj=" + lrsj + ", xfrq=" + xfrq + ", opname=" + opname + ", xfsj=" + xfsj + ", lsy=" + lsy
				+ ", memo=" + memo + ", jsrq=" + jsrq + ", jssj=" + jssj + ", bjje=" + bjje + ", bjf=" + bjf
				+ ", dshk=" + dshk + ", sjrff=" + sjrff + ", qtf=" + qtf + ", zfy=" + zfy + ", jz=" + jz + ", ywzl="
				+ ywzl + ", fkrq=" + fkrq + ", fksj=" + fksj + ", flag=" + flag + "]";
	}

	public Tyjxx(Integer pdid, String tm, Integer lsy) {
		super();
		this.pdid = pdid;
		this.tm = tm;
		this.lsy = lsy;
	}

	public String getTm() {
		return tm;
	}

	public void setTm(String tm) {
		this.tm = tm;
	}

	public Integer getId() {
		return id;
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

	public Integer getLsy() {
		return lsy;
	}

	public void setLsy(Integer lsy) {
		this.lsy = lsy;
	}

	public String getBjje() {
		return bjje;
	}

	public void setBjje(String bjje) {
		this.bjje = bjje;
	}

	public String getBjf() {
		return bjf;
	}

	public void setBjf(String bjf) {
		this.bjf = bjf;
	}

	public String getDshk() {
		return dshk;
	}

	public void setDshk(String dshk) {
		this.dshk = dshk;
	}

	public String getSjrff() {
		return sjrff;
	}

	public void setSjrff(String sjrff) {
		this.sjrff = sjrff;
	}

	public String getQtf() {
		return qtf;
	}

	public void setQtf(String qtf) {
		this.qtf = qtf;
	}

	public String getZfy() {
		return zfy;
	}

	public void setZfy(String zfy) {
		this.zfy = zfy;
	}

	public String getJz() {
		return jz;
	}

	public void setJz(String jz) {
		this.jz = jz;
	}

	public String getYwzl() {
		return ywzl;
	}

	public void setYwzl(String ywzl) {
		this.ywzl = ywzl;
	}

	public int getFjfw() {
		return fjfw;
	}

	public void setFjfw(int fjfw) {
		this.fjfw = fjfw;
	}

	public String getHztm() {
		return hztm;
	}

	public void setHztm(String hztm) {
		this.hztm = hztm;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if (obj instanceof Tyjxx) {   
            Tyjxx ty = (Tyjxx) obj;   
            return this.tm.equals(ty.tm);   
        } 
		return super.equals(obj);
	}

}