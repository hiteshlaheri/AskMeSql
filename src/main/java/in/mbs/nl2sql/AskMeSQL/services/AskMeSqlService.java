package in.mbs.nl2sql.AskMeSQL.services;

import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.model.function.FunctionCallback;
import org.springframework.ai.vectorstore.PgVectorStore;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Description;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.function.Function;


@Service
@BrowserCallable
@AnonymousAllowed
public class AskMeSqlService {
    @Autowired
    @Qualifier("chatClient")
    ChatClient chatClient;
    @Autowired
    PgVectorStore pgVectorStore;
    @Value("classpath:sqlprompt.st")
    private Resource systemResource;


    public Map<String,String> getChatResponse(String message){

        StringBuilder tableContent= new StringBuilder();
        pgVectorStore.doSimilaritySearch(SearchRequest.query(message).withTopK(5)).forEach(document -> {
            tableContent.append(document.getContent());
        });

        PromptTemplate promptTemplate = new PromptTemplate(systemResource);
        Prompt prompt = promptTemplate.create(Map.of("top_k",10,"table_info",tableContent.toString(),"input",message));

        String chatResponse= this.chatClient.prompt(prompt)
                .user(message)
                .advisors(new SimpleLoggerAdvisor())
                .functions(FunctionCallback.builder()
                        .function("executeSQLFunction", executeSQLFunction()) // (1) function name and instance)
                        .inputType(SqlResultService.SqlRequest.class) // (3) input type to build the JSON schema
                        .build())
                .call()
                .content();

        return Map.of("Result",chatResponse);
    }
    @Autowired
    @Qualifier("secondaryJdbcTemplate")JdbcTemplate secondaryJdbcTemplate;
    public  class SqlResultService implements Function<SqlResultService.SqlRequest, SqlResultService.SqlResponse> {

        public record SqlRequest(String sqlquery) {}
        public record SqlResponse(java.util.List<Map<String, Object>> response, int noofrows) {}

        @Override
        public SqlResponse apply(SqlRequest request) {
            System.out.println(request.sqlquery);

            return new SqlResponse(secondaryJdbcTemplate.queryForList(request.sqlquery), secondaryJdbcTemplate.queryForList(request.sqlquery).size());
        }

    }
    @Bean
    @Description("Execute SQL Query and get result")
    public Function<SqlResultService.SqlRequest, SqlResultService.SqlResponse> executeSQLFunction() {
        return new SqlResultService();
    }


}
