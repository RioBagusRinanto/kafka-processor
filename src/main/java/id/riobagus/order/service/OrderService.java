package id.riobagus.order.service;

import id.riobagus.order.entity.OrderEntity;
import id.riobagus.order.model.ProcessedOrder;
import id.riobagus.order.model.RawOrder;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Optional;

@ApplicationScoped
public class OrderService {

    private static final Logger LOG = Logger.getLogger(OrderService.class);

    @Transactional
    public Optional<ProcessedOrder> process(RawOrder raw) {

        if (OrderEntity.count("orderId", raw.orderId()) > 0) {
            LOG.warnf("Duplicate order skipped: %s", raw.orderId());
            return Optional.empty();
        }


        var processed = new ProcessedOrder(
                raw.orderId(),
                raw.customerId(),
                raw.productCode(),
                raw.quantity(),
                calculateTotal(raw),
                resolveCurrency(raw.currency()),
                resolveStatus(calculateTotal(raw)),
                Instant.now()
        );

        OrderEntity.persist(OrderEntity.from(processed));
        LOG.infof("Order saved: %s | status: %s | total: %s",
                processed.orderId(), processed.status(), processed.totalAmount());

        return Optional.of(processed);
    }

    private BigDecimal calculateTotal(RawOrder raw) {
        return raw.unitPrice()
                .multiply(BigDecimal.valueOf(raw.quantity()))
                .setScale(2, RoundingMode.HALF_UP);
    }

    private String resolveCurrency(String currency) {
        return (currency == null || currency.isBlank()) ? "IDR" : currency.toUpperCase();
    }

    private String resolveStatus(BigDecimal total) {
        if (total.compareTo(BigDecimal.valueOf(1_000_000)) >= 0) return "HIGH_VALUE";
        if (total.compareTo(BigDecimal.valueOf(100_000))   >= 0) return "NORMAL";
        return "LOW_VALUE";
    }
}