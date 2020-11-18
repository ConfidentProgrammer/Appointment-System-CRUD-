package ca.sheridancollege.pate2406.database;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

@Configuration
public class DatabaseConfig {

	@Bean
	public NamedParameterJdbcTemplate namedParemterJdbcTemplate(DataSource dataSource) {
	return new NamedParameterJdbcTemplate(dataSource);
	}
	
	
	@Bean
	public DataSource loadSchema() {
		return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).addScript("classpath:Data.sql").build();
				//You can include additional .addScript() for multiple sql files
				//.build();
	}
	
}

//EmbeddedDatabasBuilder the method that help to integrate the database like H2, setType(EmbeddedDatabaseType.H2),addScript()