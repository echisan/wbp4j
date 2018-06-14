package cn.echisan.wpb4j.Entity.upload;

/**
 * Created by echisan on 2018/6/14
 */
public class Pic_1 {

    private int width;

    private int size;

    private int ret;

    private int height;

    private String name;

    private String pid;

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getSize() {
        return this.size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getRet() {
        return this.ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPid() {
        return this.pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    @Override
    public String toString() {
        return "Pic_1{" +
                "width=" + width +
                ", size=" + size +
                ", ret=" + ret +
                ", height=" + height +
                ", name='" + name + '\'' +
                ", pid='" + pid + '\'' +
                '}';
    }
}
