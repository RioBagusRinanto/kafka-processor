package id.riobagus.order.model;

import java.math.BigDecimal;

public record RawOrder(
        String orderId,
        String customerId,
        String productCode,
        int quantity,
        BigDecimal unitPrice,
        String currency
) {

}
