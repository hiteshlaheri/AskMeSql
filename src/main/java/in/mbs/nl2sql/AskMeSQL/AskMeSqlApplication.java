package in.mbs.nl2sql.AskMeSQL;

import in.mbs.nl2sql.AskMeSQL.configurations.GeneralConfig;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Description;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.component.page.AppShellConfigurator;

@SpringBootApplication
@Theme("my-theme")
public class AskMeSqlApplication implements AppShellConfigurator {

    @Autowired
    VectorStore vectorStore;

    @Qualifier("primaryJdbcTemplate")
    @Autowired
    JdbcTemplate primaryJdbcTemplate;

    @Qualifier("secondaryJdbcTemplate")
    @Autowired
    JdbcTemplate secondaryJdbcTemplate;

    @Value("classpath:databaseschema.txt")
    Resource resource;

    public static void main(String[] args) {
        SpringApplication.run(AskMeSqlApplication.class, args);
    }

    @Bean
    CommandLineRunner runner() {
        return args -> {
            if (primaryJdbcTemplate.queryForObject("""
                select count(*)
                from askmesql
                """, Integer.class) == 0) {
                List<Document> schemalist = new ArrayList<>();
//                TextReader reader = new TextReader(resource);
//                schemalist.addAll(reader.read());
                // find documents from query
                secondaryJdbcTemplate.queryForList("""
                            select E'Database Table Name is '||tt.tablename||', its Window name is '||f.name||' and columns and its datatype are '||E'\\n'||
                            	array_to_string(array(select c.column_name||' '|| c.data_type|| ' '|| trim(coalesce( to_char(character_maximum_length),'')) ||' --'||a.name||E'\\n'
                                                                            	from ad_field a
                                                                            	inner join ad_column aa on aa.ad_column_id = a.ad_column_id
                            													inner join information_schema.columns c on c.table_name=lower(tt.tablename) and c.column_name=lower(aa.columnname)
                                                                            	where  a.ad_tab_id = t.ad_tab_id),',','') as info
                            	,tt.tablename as table1,t.name as tablename           	
                            	from ad_tab t
                            	inner join ad_table tt on tt.ad_table_id=t.ad_table_id
                            	inner join ad_window f on f.ad_window_id=t.ad_window_id
                            --	where lower(tt.tablename)= 'a_amortization'
                            	group by f.name,tt.tablename,t.ad_tab_id
                            	order by tt.tablename
                        """).forEach(info -> {
                    //System.out.println(info.get("info"));
                    schemalist.add(new Document(info.get("info").toString(), Map.of("table", info.get("table1"),"TableName",info.get("tablename"))));
                });
                //TokenTextSplitter splitter = new TokenTextSplitter(2000, 400, 10, 10000, true);
                vectorStore.add(schemalist);
            }

//           List<Document> documentList = vectorStore.similaritySearch(SearchRequest.query("how to get list of products ").withTopK(10));
//            documentList.forEach(e->{System.out.println(e.getContent());});
        };
    }


}
