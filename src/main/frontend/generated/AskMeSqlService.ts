import { EndpointRequestInit as EndpointRequestInit_1 } from "@vaadin/hilla-frontend";
import client_1 from "./connect-client.default.js";
async function executeSQLFunction_1(init?: EndpointRequestInit_1): Promise<unknown> { return client_1.call("AskMeSqlService", "executeSQLFunction", {}, init); }
async function getChatResponse_1(message: string | undefined, init?: EndpointRequestInit_1): Promise<Record<string, string | undefined> | undefined> { return client_1.call("AskMeSqlService", "getChatResponse", { message }, init); }
export { executeSQLFunction_1 as executeSQLFunction, getChatResponse_1 as getChatResponse };
