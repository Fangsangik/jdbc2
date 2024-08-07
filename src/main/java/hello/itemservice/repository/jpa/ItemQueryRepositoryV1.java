package hello.itemservice.repository.jpa;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hello.itemservice.domain.Item;
import hello.itemservice.domain.QItem;
import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static hello.itemservice.domain.QItem.item;

@Repository
@Transactional
public class ItemQueryRepositoryV1 implements ItemRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public ItemQueryRepositoryV1(EntityManager em) {
        this.em = em;
        //QueryDsl은 결과적으로 jpa의 jpql을 만들어주는 빌더 역할
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public Item save(Item item) {
        em.persist(item);
        return item;
    }

    @Override
    public void update(Long itemId, ItemUpdateDto updateParam) {
        Item findItem = findById(itemId).orElseThrow();
        findItem.setItemName(updateParam.getItemName());
        findItem.setPrice(updateParam.getPrice());
        findItem.setQuantity(updateParam.getQuantity());
    }

    public List<Item> findAllOld(ItemSearchCond itemSearch) {
        String itemName = itemSearch.getItemName();
        Integer maxPrice = itemSearch.getMaxPrice();

        //QItem item = new QItem("i");
        QItem item = QItem.item;

        //동적 쿼리 해결
        BooleanBuilder builder = new BooleanBuilder();
        if (StringUtils.hasText(itemName)) {
            builder.and(item.itemName.like("%" + itemName + "%"));
        }

        if (maxPrice != null) {
            builder.and(item.price.loe(maxPrice)); //loe -> 작거나 같다.
        }

        List<Item> result = query
                .select(item)
                .from(item)
                .where(builder)
                .fetch();
        return result;
    }

    @Override
    public Optional<Item> findById(Long id) {
        Item item = em.find(Item.class, id);
        return Optional.ofNullable(item);
    }

    @Override
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
