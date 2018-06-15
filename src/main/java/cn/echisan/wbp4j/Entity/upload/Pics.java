package cn.echisan.wbp4j.Entity.upload;

/**
 * Created by echisan on 2018/6/14
 */
public class Pics {
    private Pic_1 pic_1;

    public void setPic_1(Pic_1 pic_1) {
        this.pic_1 = pic_1;
    }

    public Pic_1 getPic_1() {
        return this.pic_1;
    }

    @Override
    public String toString() {
        return "Pics{" +
                "pic_1=" + pic_1 +
                '}';
    }
}