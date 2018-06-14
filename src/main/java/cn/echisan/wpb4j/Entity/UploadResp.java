package cn.echisan.wpb4j.Entity;

import cn.echisan.wpb4j.Entity.upload.Data;

/**
 * Created by echisan on 2018/6/14
 */
public class UploadResp {

    private String code;
    private Data data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "UploadResp{" +
                "code='" + code + '\'' +
                ", data=" + data +
                '}';
    }
}
