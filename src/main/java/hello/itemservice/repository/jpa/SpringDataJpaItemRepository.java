package hello.itemservice.repository.jpa;

import hello.itemservice.domain.Item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SpringDataJpaItemRepository extends JpaRepository<Item, Long> {

    //ItemName 찾는 것
    List<Item> findByItemNameLike(String itemName);

    //가격으로 조회
    List<Item> findByPriceLessThanEqual(Integer price);

    //쿼리 직접 실행
    List<Item> findByItemNameLikeAndPriceLessThanEqual(String itemName, Integer price);

    @Query("select i from Item i where i.itemName like :itemName and i.price <= :price")
        //파라미터를 넣을때 @param을 넣어주어야 한다.
    List<Item> findItems(@Param("itemName") String itemName, @Param("price") Integer price);

}
