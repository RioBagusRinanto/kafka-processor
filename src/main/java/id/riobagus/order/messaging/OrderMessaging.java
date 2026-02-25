package id.riobagus.order.messaging;

import id.riobagus.order.model.ProcessedOrder;
import id.riobagus.order.model.RawOrder;
import id.riobagus.order.service.OrderService;
import io.smallrye.reactive.messaging.annotations.Blocking;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

import java.util.Optional;

@ApplicationScoped
public class OrderMessaging {

    private static final Logger LOG = Logger.getLogger(OrderMessaging.class);

    @Inject
    OrderService orderService;

    @Incoming("orders-raw")
    @Outgoing("orders-processed")
    @Blocking
    public Optional<ProcessedOrder> handle(RawOrder raw) {
        LOG.debugf("Incoming order: %s", raw.orderId());
        try {
            return orderService.process(raw);
        } catch (Exception e) {
            // Log & skip — jangan sampai satu pesan rusak menghentikan consumer
            LOG.errorf("Failed to process order %s: %s", raw.orderId(), e.getMessage());
            return Optional.empty();
        }
    }
}