import {useState} from "react";
import {MessageList, MessageListItem,MessageInput} from '@vaadin/react-components';
import {AskMeSqlService} from "Frontend/generated/endpoints";
export default function HomeView() {
 const [messages, setMessages] = useState<MessageListItem[]>([]);
    async function sendMessage(message: string) {
            setMessages(messages => [...messages, {
                text: message,
                userName: 'You',
                time: new Date().toLocaleTimeString(),

            }]);

            const response = await AskMeSqlService.getChatResponse(message);
            //const responseText = typeof response === 'string' ? response : JSON.stringify(response);
            if (response) {
                setMessages(messages => [...messages, {
                    text: response["Result"],
                    userName: 'Assistant',
                    time: new Date().toLocaleTimeString()
                }]);
            }
        }

        return (
          <div className="p-m flex flex-col h-full box-border">
              <MessageList items={messages} className="flex-grow"/>
              <MessageInput onSubmit={e => sendMessage(e.detail.value)}/>
          </div>
        );
}

