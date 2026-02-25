package id.riobagus.order.routes;

import id.riobagus.order.entity.OrderEntity;
import id.riobagus.order.model.RawOrder;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Path("/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderRoute {

    @Inject
    @Channel("orders-raw-out")
    Emitter<RawOrder> emitter;

    @POST
    public Response submit(RawOrder order) {
        emitter.send(order);
        return Response.accepted(Map.of("orderId", order.orderId(), "message", "submitted")).build();
    }

    @POST
    @Path("/demo/{n}")
    public Response demo(@PathParam("n") int n) {
        for (int i = 0; i < n; i++) {
            emitter.send(new RawOrder(
                    UUID.randomUUID().toString(),
                    "CUST-" + (i % 5),
                    "PROD-" + (char) ('A' + i % 4),
                    i % 10 + 1,
                    BigDecimal.valueOf(25_000L * (i + 1)),
                    i % 3 == 0 ? null : "IDR"
            ));
        }
        return Response.accepted(Map.of("submitted", n)).build();
    }

    @GET
    public Response list() {
        return Response.ok(OrderEntity.listAll()).build();
    }
}
