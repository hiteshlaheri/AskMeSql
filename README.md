# AskMeSQL

AskMeSQL is a full-stack AI-powered application built using Spring AI and Java. It provides natural language to SQL (NL2SQL) query generation and execution capabilities. The project integrates advanced AI features using OpenAI APIs, PostgreSQL as the database, and Vaadin for the frontend.
file:///home/openbravo/Pictures/2projectstructure.png![image](https://github.com/user-attachments/assets/5a71cbb1-3409-485f-b0c8-c8c926381618)

## Features
- **Natural Language to SQL Query Conversion:** Converts user inputs into SQL queries.
- **Database Interaction:** Utilizes PostgreSQL and pgvectorstore for vector storage and query execution.
- **AI Model Integration:** Employs Groq Chat Model and Hugging Face Sentence Similarity Embedding Model.
- **Vaadin Frontend:** Offers an interactive web-based chat interface.
- **Function Calling AI:** Leverages Spring AI for dynamic function invocation.

## Application Properties
The application configuration is managed through the `application.properties` file. Below are the key properties:

```properties
vaadin.launch-browser=true
spring.application.name=AskMeSQL
spring.ai.openai.api-key=${GROQ_AI_KEY}
#spring.ai.openai.base-url=https://api.groq.com/openai
#spring.ai.openai.chat.options.model=llama3-70b-8192
#spring.ai.openai.chat.options.temperature=0.7
logging.level.org.springframework.ai.chat.client.advisor=DEBUG
spring.jpa.hibernate.ddl-auto=update
spring.jpa.hibernate.show-sql=true
spring.datasource.primary.url=jdbc:postgresql://localhost:5432/askmesql
spring.datasource.primary.username=tad
spring.datasource.primary.password=tad
spring.datasource.secondary.url=jdbc:postgresql://localhost:5432/prism_db
spring.datasource.secondary.username=tad
spring.datasource.secondary.password=tad
logging.level.org.springframework = debug
```

## Project Structure

### Backend
- **Spring Boot Application:** Main entry point for the application is `AskMeSqlApplication.java`.
- **Service Layer:**
  - `AskMeSqlService` handles chat responses, similarity searches, and SQL function execution.
  - `SqlResultService` dynamically executes SQL queries using `JdbcTemplate`.
- **Vector Storage:** Utilizes `PgVectorStore` for managing vectorized database schemas and queries.
- **AI Models:** Integrates OpenAI API and Hugging Face ONNX models for embedding generation and query enhancement.

### Frontend
- **Vaadin Framework:** Implements a browser-based chat interface.
- **Interactive Chat:** Built using Vaadin's `MessageList` and `MessageInput` components.
- **Frontend Logic:** React-based `tsx` file handles user interactions and communicates with backend services via Vaadin endpoints.
- file:///home/openbravo/Pictures/1Vaadin.png![image](https://github.com/user-attachments/assets/b7f939c4-177a-461b-9387-3d1c2382e6a9)


### Key Components
- **Vector Store Initialization:** The schema and data are vectorized and stored at application startup if no pre-existing data is found.
- **AI-Driven SQL Execution:** Processes user input, generates relevant SQL queries, executes them, and returns results in real time.

## Setup Instructions

### Prerequisites
- **Java 21**
- **PostgreSQL Database**
- **20 Node version** (for Vaadin frontend)
- **API Key for Groq/OpenAI**

### Steps to Run
1. **Clone the Repository:**
   ```bash
   git clone https://github.com/hiteshlaheri/AskMeSql.git
   cd AskMeSql
   ```
2. **Configure the Database:**
   - Set up PostgreSQL with two databases:
     - `askmesql` for vector storage.
     - `prism_db` for query execution.
   - Update credentials in `application.properties`.

3. **Build the Project:**
   ```bash
   ./gradlew build
   ```

4. **Run the Application:**
   ```bash
   java -jar build/libs/AskMeSql-<version>.jar
   ```

5. **Access the Application:**
   Open [http://localhost:8080](http://localhost:8080) in your browser.

### Running the Frontend
Vaadin automatically serves the frontend as part of the Spring Boot application. No separate configuration is required.

## Technologies Used
- **Backend:**
  - Spring Boot
  - Spring AI
  - PostgreSQL
  - pgvector
- **AI Models:**
  - Groq Chat Model
  - Hugging Face Sentence Similarity Embedding Model
- **Frontend:**
  - Vaadin Framework
  - React
- **Build Tools:** Gradle

## Future Enhancements
- Add support for additional AI models and providers.
- Implement user authentication and role-based access control.
- Enhance the frontend with advanced analytics and reporting features.

## Contributing
Contributions are welcome! Please fork the repository and submit a pull request with your changes. Ensure all code changes are tested and documented.

## License
This project is licensed under the MIT License. See the LICENSE file for details.

## Author
Developed by Hitesh Laheri ([GitHub Repository](https://github.com/hiteshlaheri/AskMeSql)).

