package Enum;

import lombok.Data;
import org.junit.Test;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;

@Data
public class MyPizza {
    private static EnumSet<MyPizzaStatus> undeliveredPizzaStatus = EnumSet.of(MyPizzaStatus.ORDERED,MyPizzaStatus.READY);

    private MyPizzaStatus status;
    public enum MyPizzaStatus{
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
        private int timeToDelivery;
        public boolean isOrdered(){ return false; }
        public boolean isReady() { return false; }
        public boolean isDelivered() {return false; }
        MyPizzaStatus(int timeToDelivery){
            this.timeToDelivery = timeToDelivery;
        }
    }
    public boolean isDeliverable(){
        return this.status.isReady();
    }
    public void printTimeToDelivery(){
        System.out.println("Time to delivery is " + this.status.timeToDelivery + " days");
    }

    public static List<MyPizza> getAllUndeliveredPizzas(List<MyPizza> input){
        return input.stream().filter((s) -> undeliveredPizzaStatus.contains(s.getStatus())).collect(Collectors.toList());
    }

    @Test
    public void givenPizaOrders_whenRetrievingUnDeliveredPzs_thenCorrectlyRetrieved() {
        List<MyPizza> pzList = new ArrayList<>();
        MyPizza pz1 = new MyPizza();
        pz1.setStatus(MyPizza.MyPizzaStatus.DELIVERED);

        MyPizza pz2 = new MyPizza();
        pz2.setStatus(MyPizza.MyPizzaStatus.ORDERED);

        MyPizza pz3 = new MyPizza();
        pz3.setStatus(MyPizza.MyPizzaStatus.ORDERED);

        MyPizza pz4 = new MyPizza();
        pz4.setStatus(MyPizza.MyPizzaStatus.READY);

        pzList.add(pz1);
        pzList.add(pz2);
        pzList.add(pz3);
        pzList.add(pz4);

        List<MyPizza> undeliveredPzs = MyPizza.getAllUndeliveredPizzas(pzList);
        assertTrue(undeliveredPzs.size() == 3);
    }


}
