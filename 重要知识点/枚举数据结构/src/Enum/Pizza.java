package Enum;

import lombok.Data;
import org.junit.Test;


import static org.junit.Assert.assertTrue;

@Data
public class Pizza {
    private PizzaStatus status;
    //为何在声明PizzaStatus前可以使用PizzaStatus
    //因为enum类型实际上是定义一个类，被编译成final class Xxx extend Enum{}
    //每个枚举值就是一个类实例
    public enum PizzaStatus {
        ORDERED (5){
            @Override
            public boolean isOrdered(){
                return true;
            }
        },
        READY (2){
            @Override
            public boolean isReady(){
                return true;
            }
        },
        DELIVERED (0){
            @Override
            public boolean isDelivered(){
                return true;
            }
        };

        public boolean isOrdered(){ return false; }
        public boolean isReady() { return false; }
        public boolean isDelivered() {return false; }
        private int timeToDelivery;

        public int getTimeToDelivery(){
            return timeToDelivery;
        }
        PizzaStatus(int timeToDelivery) {
            this.timeToDelivery = timeToDelivery;
        }
    }

    public Boolean isDeliverable() {
        return this.status.isReady();
    }

    public boolean printTimeToDeliver() {
        System.out.println("Time to delivery is " + this.getStatus().getTimeToDelivery());
        return false;
    }

    //在switch语句中使用枚举类型
    public int getDeliveryTimeInDays(){
        switch (status) {
            case ORDERED:
                return 5;
            case READY:
                return 2;
            case DELIVERED:
                return 0;
        }
        return 0;
    }



    @Test
    public void givenPizaOrder_whenReady_thenDeliverable() {
        Pizza testPz = new Pizza();
        testPz.setStatus(Pizza.PizzaStatus.READY);
        assertTrue(testPz.isDeliverable());
    }

    public static void main(String[] args) {
        System.out.println(PizzaStatus.ORDERED.name());//ORDERED
        System.out.println(PizzaStatus.ORDERED);//ORDERED
        System.out.println(PizzaStatus.ORDERED.name().getClass());//class java.lang.String
        System.out.println(PizzaStatus.ORDERED.getClass());//class Enum.EnumTest$PizzaStatus
    }
}
