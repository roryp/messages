package com.bbd.messages.test;

import com.bbd.messages.model.Message;
import org.junit.Assert;
import org.junit.Test;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Logger;

public class MessagesRestClientTest {

    private static final String REST_TARGET_URL = "http://localhost:8080/messages/rest/messages";
    private static final String MESSAGE_TEXT = "New Message";

    private Logger log = Logger.getLogger(MessagesRestClientTest.class.getName());

    @Test
    public void crudTest() {
        log.info("### CRUD tests ###");
        // 1 - drop all messages
        log.info("dropping all messages");
        Response response = ClientBuilder.newClient().target(REST_TARGET_URL).request().delete();
        Assert.assertEquals("All messages should be dropped", Response.ok().build().getStatus(), response.getStatus());

        // 2 - Create a new message
        log.info("creating a new message");
        Message c = new Message();
        c.setText(MESSAGE_TEXT);
        Message persistedMessage = ClientBuilder.newClient().target(REST_TARGET_URL).request().post(Entity.entity(c, MediaType.APPLICATION_JSON), Message.class);
        Assert.assertEquals("A Message should be persisted with Id=1!", (Long) 1L, (Long) persistedMessage.getId());

        // 3 - Fetch Message by Id
        log.info("fetching a message by id");
        Message fetchMessageById =
            ClientBuilder.newClient().target(REST_TARGET_URL).path("/{id}").resolveTemplate("id", persistedMessage.getId()).request().get(Message.class);
        Assert.assertEquals("Fetched message with Id=1!", (Long) 1L, (Long) fetchMessageById.getId());
        Assert.assertEquals("Fetched message with equal text", MESSAGE_TEXT, fetchMessageById.getText());

        // 4 - Fetch all Messages
        log.info("fetching all messages");
        GenericType<List<Message>> messagesListType = new GenericType<List<Message>>() {};
        List<Message> allMessages = ClientBuilder.newClient().target(REST_TARGET_URL).request().get(messagesListType);
        Assert.assertEquals("Should have a single message", 1, allMessages.size());

        // 5 - Delete a Message
        log.info("delete a message by id");
        response = ClientBuilder.newClient().target(REST_TARGET_URL).path("/{Id}").resolveTemplate("Id", persistedMessage.getId()).request().delete();
        Assert.assertEquals("Message 1 should be dropped", Response.ok().build().getStatus(), response.getStatus());
    }

}
