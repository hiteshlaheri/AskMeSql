package in.mbs.nl2sql.AskMeSQL.configurations;

import in.mbs.nl2sql.AskMeSQL.services.AskMeSqlService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.transformers.TransformersEmbeddingModel;
import org.springframework.ai.vectorstore.PgVectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static org.springframework.ai.vectorstore.PgVectorStore.PgDistanceType.COSINE_DISTANCE;

@Configuration
public class GeneralConfig {
    @Bean
    public EmbeddingModel embeddingModel1() {
        TransformersEmbeddingModel embeddingModel = new TransformersEmbeddingModel();
        embeddingModel.setTokenizerResource("classpath:tokenizer.json");
        embeddingModel.setModelResource("classpath:model.onnx");
        embeddingModel.setResourceCacheDirectory("/tmp/onnx-zoo");
        embeddingModel.setTokenizerOptions(Map.of("padding","true"));
        embeddingModel.setModelOutputName("token_embeddings");
        return embeddingModel;
    }
    @Value("${GROQ_AI_KEY}")
    private String GROQ_AI_KEY;
    @Bean
    PgVectorStore pgVectorStore(@Qualifier("primaryJdbcTemplate")JdbcTemplate jdbcTemplate){
        return new PgVectorStore("askmesql",jdbcTemplate,embeddingModel1(),384,COSINE_DISTANCE,false, PgVectorStore.PgIndexType.HNSW,true);
    }

    @Bean(name="chatClient")
    ChatClient chatClient(@Qualifier("openaiChatmodel")OpenAiChatModel openAiChatModel){

        return ChatClient.create(openAiChatModel);
    }
    @Bean(name="openaiChatmodel")
    OpenAiChatModel openAiChatModel(){
        var openai = new OpenAiApi("https://api.groq.com/openai",GROQ_AI_KEY);
        var openAiChatOptions = OpenAiChatOptions.builder()
                .withModel("llama3-70b-8192")
                .withTemperature(0.4)
                .withMaxTokens(200)
                .build();
       return new OpenAiChatModel(openai,openAiChatOptions);
    }
    @Bean(name = "primaryDataSource")
    public DataSource primaryDataSource(
            @Value("${spring.datasource.primary.url}") String url,
            @Value("${spring.datasource.primary.username}") String username,
            @Value("${spring.datasource.primary.password}") String password) {

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean(name = "primaryJdbcTemplate")
    public JdbcTemplate primaryJdbcTemplate(@Qualifier("primaryDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    // Secondary Database Configuration
    @Bean(name = "secondaryDataSource")
    public DataSource secondaryDataSource(
            @Value("${spring.datasource.secondary.url}") String url,
            @Value("${spring.datasource.secondary.username}") String username,
            @Value("${spring.datasource.secondary.password}") String password) {

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean(name = "secondaryJdbcTemplate")
    public JdbcTemplate secondaryJdbcTemplate(@Qualifier("secondaryDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }


}
