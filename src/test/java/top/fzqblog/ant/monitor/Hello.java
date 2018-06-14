package top.fzqblog.ant.monitor;

public class Hello implements HelloMBean {
//    private String name;
//
//    @Override
//    public String getName() {
//        return name;
//    }
//
//    @Override
//    public void setName(String name) {
//        this.name = name;
//    }
  
    @Override
    public String printHello() {
        return "Hello ";
    }
  
    @Override
    public String printHello(String whoName) {
        return "Hello  " + whoName;
    }
}