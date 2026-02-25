package id.riobagus.order.entity;

import id.riobagus.order.model.ProcessedOrder;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "orders")
public class OrderEntity extends PanacheEntityBase {

    @Id
    @Column(name = "order_id")
    public String orderId;

    @Column(name = "customer_id", nullable = false)
    public String customerId;

    @Column(name = "product_code", nullable = false)
    public String productCode;

    @Column(nullable = false)
    public int quantity;

    @Column(name = "total_amount", nullable = false, precision = 15, scale = 2)
    public BigDecimal totalAmount;

    @Column(nullable = false, length = 3)
    public String currency;

    @Column(nullable = false, length = 20)
    public String status;

    @Column(name = "processed_at", nullable = false)
    public Instant processedAt;

    // Static factory — hindari constructor berantakan
    public static OrderEntity from(ProcessedOrder p) {
        var e = new OrderEntity();
        e.orderId     = p.orderId();
        e.customerId  = p.customerId();
        e.productCode = p.productCode();
        e.quantity    = p.quantity();
        e.totalAmount = p.totalAmount();
        e.currency    = p.currency();
        e.status      = p.status();
        e.processedAt = p.processedAt();
        return e;
    }
}