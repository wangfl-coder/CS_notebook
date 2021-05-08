# Java枚举数据结构

## 概览

Enum关键字是Java5引入的一种特殊的类，继承java.lang.Enum类，在实际应用中一般使用枚举来替代常量，在编译时会被编译成一个类，每个枚举值就是一个类实例，这样比起定义常量枚举有以下优势：1.代码更具有可读性；2.允许编译时检查，预先记录可接受值的列表；3.避免由于传入无效值而引起的意外行为。



举例定义枚举类型Pizza订单状态，共有三种：ORDERED，READY，DELIVERED。通过以下代码避免定义状态常量，统一将pizza的状态常量放到一个枚举类型中。

```java
package Enum;

public class EnumTest {

    public enum PizzaStatus {
        ORDERED,
        READY,
        DELIVERED;
    }
    public static void main(String[] args) {
        System.out.println(PizzaStatus.ORDERED.name());//ORDERED
        System.out.println(PizzaStatus.ORDERED);//ORDERED
        System.out.println(PizzaStatus.ORDERED.name().getClass());//class java.lang.String
        System.out.println(PizzaStatus.ORDERED.getClass());//class Enum.EnumTest$PizzaStatus

    }
}
```

## 自定义枚举方法

在枚举上定义一些额外的API方法

```java
package Enum;

import lombok.Data;

@Data
public class Pizza {
    private PizzaStatus status;
    //为何在声明PizzaStatus前可以使用PizzaStatus
		//因为enum类型实际上是定义一个类，被编译成final class Xxx extend Enum{}
    //每个枚举值就是一个类实例    
  	public enum PizzaStatus {
        ORDERED,
        READY,
        DELIVERED;
    }
		//自定义API
    public Boolean isDeliverable() {
        return getStatus() == PizzaStatus.READY;
    }

    public static void main(String[] args) {
        System.out.println(PizzaStatus.ORDERED.name());//ORDERED
        System.out.println(PizzaStatus.ORDERED);//ORDERED
        System.out.println(PizzaStatus.ORDERED.name().getClass());//class java.lang.String
        System.out.println(PizzaStatus.ORDERED.getClass());//class Enum.EnumTest$PizzaStatus
    }
}
```

## 使用==比较枚举类型

枚举类型属于一种特殊的引用类型，一般引用类型使用equals()进行比较，但是枚举类型在jvm中有唯一常量实例，所以==可以放心使用。看下面代码。

```java
Pizza.PizzaStatus pizza = null;
System.out.println(pizza.equals(Pizza.PizzaStatus.DELIVERED));//空指针异常
System.out.println(pizza == Pizza.PizzaStatus.DELIVERED);//正常运行
```

## switch语句中使用枚举类型

```java
public int getDeliveryTimeInDays(){
        switch (status) {
            //这里不要引用PizzaStatus.ORDERED
            //因为status是PizzaStatus类型的
            case ORDERED:
                return 5;
            case READY:
                return 2;
            case DELIVERED:
                return 0;
        }
        return 0;
    }
```

## 枚举类型的属性、方法和构造函数

我们可以在枚举类型中定义属性、方法和构造函数，让它变得更强大。并且能够摆脱if和switch语句的使用，源码见src。

## EnumSet和EnumMap

### EnumSet

EnumSet是专门为枚举类型设计的set类型，与hashSet相比，使用了内部位向量，因此它是Enum常量集非常有效且紧凑的表达方式。它提供了类型安全的替代方法，替代传统的基于int的“位标志”，使我们能够编写更易读和更易于维护的简洁代码。EnumSet是抽象类，其有两个实现：`RegularEnumSet` 、`JumboEnumSet`，选择哪一个取决于实例化时枚举中常量的数量。在很多枚举常量集合操作（取子集、增加、删除），使用EnumSet非常合适，；如果想迭代所有可能的常量则使用Enum.values()。应用用例见src/Enum/MyPizza目录。

### EnumMap

`EnumMap`是一个专门化的映射实现，用于将枚举常量用作键。与对应的 `HashMap` 相比，它是一个高效紧凑的实现，并且在内部表示为一个数组.