package hello.itemservice.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Item {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "item_name")
    private String itemName;
    private Integer price;
    private Integer quantity;

    //JPA는 public 또는 protected 기본 생성자 생성 필수
    //기본 생성자가 있어야지 proxy 기술 같은거 사용하기 용이
    //까먹지 말자.
    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
