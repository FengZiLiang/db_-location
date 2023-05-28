package cn.td.aiot.decoder.config;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.MybatisXMLLanguageDriver;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

//@Configuration
//@MapperScan(basePackages = "cn.td.aiot.decoder.dao", sqlSessionTemplateRef = "decoderSqlSessionTemplate")
public class MybatisConfig {
    @Autowired
    private DynamicRoutingDataSource dynamicRoutingDataSource;
    @Autowired
    @Qualifier("spring.datasource.dynamic.datasource.decoder")
    private DataSource psi;


/*    @Primary
    @Bean("decoderSqlSessionFactory")*/
    public SqlSessionFactory decoderSqlSessionFactory() throws Exception {
        MybatisSqlSessionFactoryBean factoryBean = new MybatisSqlSessionFactoryBean();
        factoryBean.setDataSource(dynamicRoutingDataSource.getDataSource("decoder"));

        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setDefaultScriptingLanguage(MybatisXMLLanguageDriver.class);
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        factoryBean.setConfiguration(configuration);
        //指定xml路径.
        factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/*/*.xml"));

        return factoryBean.getObject();
    }


/*    @Primary
    @Bean("decoderSqlSessionTemplate")*/
    public SqlSessionTemplate decoderSqlSessionTemplate() throws Exception {
        SqlSessionTemplate template = new SqlSessionTemplate(decoderSqlSessionFactory()); // 使用上面配置的Factory
        return template;
    }
}
