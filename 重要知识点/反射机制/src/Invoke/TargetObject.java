/*
反射操作类
 */
package Invoke;

public class TargetObject {

    private String value;
    public TargetObject(){
        this.value="JavaGuide";
    }

    public void publicMethod(String s){
        System.out.println("i love " + s);
    }

    private void privateMethod(){
        System.out.println("value is " + this.value);
    }
}
