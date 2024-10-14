package hello.itemservice.apply;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@SpringBootTest
public class TxBasicTest {

    @Autowired
    BasicService basicService;

    @Test
    void proxyCheck() {
        //proxy 적용 유무
        log.info("aop class = {}" ,basicService.getClass());
        Assertions.assertThat(AopUtils.isAopProxy(basicService)).isTrue();
    }

    @Test
    void txTest() {
        basicService.tx();
        basicService.nonTx();
    }

    @TestConfiguration
    static class TxApplyBasicConfig {

        @Bean
        BasicService basicService () {
            return new BasicService();
        }
    }

    static class BasicService {

        //transaction 적용
        @Transactional
        public void tx() {
            log.info("call tx");
            boolean txActive =
                    TransactionSynchronizationManager.isActualTransactionActive(); // transaction 활성화 유무
            log.info("tx Active = {}", txActive);
        }

        //transaction 적용 X
        public void nonTx() {
            log.info("call nonTx");
            boolean txActive =
                    TransactionSynchronizationManager.isActualTransactionActive(); // transaction 활성화 유무
            log.info("tx active = {}", txActive);
        }
    }
}
