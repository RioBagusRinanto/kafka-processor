package id.riobagus.order.model;

import java.math.BigDecimal;
import java.time.Instant;

public record ProcessedOrder(
        String orderId,
        String customerId,
        String productCode,
        int quantity,
        BigDecimal totalAmount,
        String currency,
        String status,
        Instant processedAt
) {
}
