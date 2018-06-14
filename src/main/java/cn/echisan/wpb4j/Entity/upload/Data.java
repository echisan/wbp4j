package cn.echisan.wpb4j.Entity.upload;

/**
 * Created by echisan on 2018/6/14
 */
public class Data {
    private int count;

    private String data;

    private Pics pics;

    public void setCount(int count){
        this.count = count;
    }
    public int getCount(){
        return this.count;
    }
    public void setData(String data){
        this.data = data;
    }
    public String getData(){
        return this.data;
    }
    public void setPics(Pics pics){
        this.pics = pics;
    }
    public Pics getPics(){
        return this.pics;
    }

    @Override
    public String toString() {
        return "Data{" +
                "count=" + count +
                ", data='" + data + '\'' +
                ", pics=" + pics +
                '}';
    }
}