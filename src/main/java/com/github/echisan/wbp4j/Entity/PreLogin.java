package com.github.echisan.wbp4j.Entity;

/**
 * Created by echisan on 2018/6/13
 */
public class PreLogin {

    private Integer retcode;
    private Long servertime;
    private String pcid;
    private String nonce;
    private String pubkey;
    private String rsakv;
    private Integer is_openlock;
    private Integer showpin;
    private Integer exectime;

    public Integer getRetcode() {
        return retcode;
    }

    public void setRetcode(Integer retcode) {
        this.retcode = retcode;
    }

    public Long getServertime() {
        return servertime;
    }

    public void setServertime(Long servertime) {
        this.servertime = servertime;
    }

    public String getPcid() {
        return pcid;
    }

    public void setPcid(String pcid) {
        this.pcid = pcid;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getPubkey() {
        return pubkey;
    }

    public void setPubkey(String pubkey) {
        this.pubkey = pubkey;
    }

    public String getRsakv() {
        return rsakv;
    }

    public void setRsakv(String rsakv) {
        this.rsakv = rsakv;
    }

    public Integer getIs_openlock() {
        return is_openlock;
    }

    public void setIs_openlock(Integer is_openlock) {
        this.is_openlock = is_openlock;
    }

    public Integer getShowpin() {
        return showpin;
    }

    public void setShowpin(Integer showpin) {
        this.showpin = showpin;
    }

    public Integer getExectime() {
        return exectime;
    }

    public void setExectime(Integer exectime) {
        this.exectime = exectime;
    }

    @Override
    public String toString() {
        return "PreLogin{" +
                "retcode=" + retcode +
                ", servertime=" + servertime +
                ", pcid='" + pcid + '\'' +
                ", nonce='" + nonce + '\'' +
                ", pubkey='" + pubkey + '\'' +
                ", rsakv='" + rsakv + '\'' +
                ", is_openlock=" + is_openlock +
                ", showpin=" + showpin +
                ", exectime=" + exectime +
                '}';
    }
}
