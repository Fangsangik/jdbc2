package hello.itemservice.repository.jpa;


import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemSearchCond;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

import static hello.itemservice.domain.QItem.item;

@Repository
@Transactional
public class ItemQueryRepositoryV2 {
    private final JPAQueryFactory query;

    public ItemQueryRepositoryV2(EntityManager em) {
        //QueryDsl은 결과적으로 jpa의 jpql을 만들어주는 빌더 역할
        this.query = new JPAQueryFactory(em);
    }

    public List<Item> findAll(ItemSearchCond cond) {
        String itemName = cond.getItemName();
        Integer maxPrice = cond.getMaxPrice();

        List<Item> rst = query
                .select(item)
                .from(item)
                .where(likeItemName(itemName), maxPrice(maxPrice))
                .fetch();

        return rst;
    }

    //동적쿼리 부분을 따로 메서드로 빼서 진행
    private BooleanExpression likeItemName(String itemName) {
        if (StringUtils.hasText(itemName)) {
            return item.itemName.like("%" + itemName + "%");
        }

        return null;
    }

    private BooleanExpression maxPrice(Integer maxPrice) {
        if (maxPrice != null) {
            return item.price.loe(maxPrice);
        }
        return null;
    }
}
