package cn.td.aiot.common.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.stereotype.Component;
import org.springframework.util.StringValueResolver;

/**
 * Spring Context 工具类
 * 
 * @author MrBird
 *
 */
@Component
public class SpringContextUtil implements ApplicationContextAware, EmbeddedValueResolverAware {
	private static ApplicationContext applicationContext;
	private static StringValueResolver stringValueResolver;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringContextUtil.applicationContext = applicationContext;
	}

	public static Object getBean(String name) {
		return applicationContext.getBean(name);
	}
	public static <T> T getBean(Class<T> clazz){
		return applicationContext.getBean(clazz);
	}

	public static <T> T getBean(String name, Class<T> requiredType) {
		return applicationContext.getBean(name, requiredType);
	}

	public static boolean containsBean(String name) {
		return applicationContext.containsBean(name);
	}

	public static boolean isSingleton(String name) {
		return applicationContext.isSingleton(name);
	}

	public static Class<?> getType(String name) {
		return applicationContext.getType(name);
	}

	/**
	 * 动态获取配置文件中的值
	 * @param name
	 * @return
	 */
	public static String getPropertiesValue(String name) {
			name = "${" + name + "}";
			return stringValueResolver.resolveStringValue(name);
	}



	@Override
	public void setEmbeddedValueResolver(StringValueResolver stringValueResolver) {
		SpringContextUtil.stringValueResolver = stringValueResolver;
	}
}