package net.wuxianjie.springbootweb.test;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * 测试相关的业务处理。
 *
 * @author 吴仙杰
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class TestService {
}
