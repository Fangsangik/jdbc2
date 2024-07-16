package hello.itemservice;

import hello.itemservice.domain.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {

    Item save(Item item);

    //Id에 관한 부분에 update 해라
    void update(Long itemId, ItemUpdateDto updateParam);

    Optional<Item> findById(Long id);

    //검색조건이 넘어간다.
    List<Item> findAll(ItemSearchCond cond);

}
