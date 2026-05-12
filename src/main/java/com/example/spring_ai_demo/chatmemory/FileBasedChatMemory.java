package com.example.spring_ai_demo.chatmemory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import org.objenesis.strategy.StdInstantiatorStrategy;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;

public class FileBasedChatMemory implements ChatMemory{

    private static final Kryo KRYO = new Kryo();
    private final String BASE_DIR;

    static{
        KRYO.setRegistrationRequired(false);
        KRYO.setInstantiatorStrategy(new StdInstantiatorStrategy());
    }

    public FileBasedChatMemory(String dir){
        this.BASE_DIR = dir;
        File baseDir = new File(dir);
        if(!baseDir.exists()){
            baseDir.mkdirs();
        }
    }

    @Override
    public void add(String conversationId, List<Message> messages) {
        // TODO Auto-generated method stub
        List<Message> conversationMessages = getOrCreateConversation(conversationId);
        conversationMessages.addAll(messages);
        saveConversation(conversationId, conversationMessages);
    }

    @Override
    public List<Message> get(String conversationId) {
        // TODO Auto-generated method stub
        return getOrCreateConversation(conversationId);
    }

    @Override
    public void clear(String conversationId) {
        // TODO Auto-generated method stub
        File file =  getConversationFile(conversationId);
        if(file.exists()){
            file.delete();
        }
    }

    private List<Message> getOrCreateConversation(String conversationId) {
        List<Message> list = new ArrayList<>();
        // TODO Auto-generated method stub
        File file = getConversationFile(conversationId);
        if(!file.exists()){
            return list;
        }
        try( Input input = new Input(new FileInputStream(file))){
            list = KRYO.readObject(input, ArrayList.class);
        }catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }

    private void saveConversation(String conversationId, List<Message> conversationMessages) {
        // TODO Auto-generated method stub
        File file = getConversationFile(conversationId);
        try(Output output = new Output(new FileOutputStream(file))){
            KRYO.writeObject(output, conversationMessages);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private File getConversationFile(String conversationId) {
        // TODO Auto-generated method stub
        return new File(BASE_DIR,conversationId+".kryo");
    }
    
}
