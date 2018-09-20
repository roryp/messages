package com.bbd.messages.rest;

import com.bbd.messages.model.Message;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Path("/messages")
public class MessageResourceRESTService {

    private static Map<Long, Message> messagesRepository = new HashMap<>();

    /**
     * Creates a new message from the values provided and will return a JAX-RS response with either 200 ok, or 400 (BAD REQUEST)
     * in case of errors.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createMessage(Message message) {

        Response.ResponseBuilder builder = null;
        Long nextId = messagesRepository.keySet().size() + 1L;
        try {
            // Store the message
            message.setId(nextId);
            messagesRepository.put(nextId, message);

            // Create an "ok" response with the persisted message
            builder = Response.ok(message);
        } catch (Exception e) {
            // Handle generic exceptions
            builder = Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage());
        }

        return builder.build();
    }

    // delete all Messages
    @DELETE
    public Response removeAllMessages() {
        messagesRepository.clear();
        return Response.ok().build();
    }

    // delete a specific Message
    @DELETE
    @Path("/{id}")
    public Response removeMessage(final @PathParam("id") Long id) {
        messagesRepository.remove(id);
        return Response.ok().build();
    }

    // Fetch all Messages
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        Collection<Message> allmessages = messagesRepository.values();
        return Response.ok(allmessages).build();
    }

    // Fetch a specific Message
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(final @PathParam("id") Long id) {
        Message message = messagesRepository.get(id);
        return Response.ok(message).build();
    }
}
