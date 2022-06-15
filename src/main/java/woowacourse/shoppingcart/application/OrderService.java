package woowacourse.shoppingcart.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import woowacourse.shoppingcart.dao.CartItemDao;
import woowacourse.shoppingcart.dao.OrderDao;
import woowacourse.shoppingcart.dao.OrdersDetailDao;
import woowacourse.shoppingcart.dao.ProductDao;
import woowacourse.shoppingcart.domain.OrderDetail;
import woowacourse.shoppingcart.domain.Orders;
import woowacourse.shoppingcart.domain.Product;
import woowacourse.shoppingcart.dto.request.OrderRequest;
import woowacourse.shoppingcart.dto.response.OrdersResponse;
import woowacourse.exception.notFound.InvalidOrderException;
import woowacourse.exception.notFound.InvalidProductException;

@Service
@Transactional
public class OrderService {

    private final OrderDao orderDao;
    private final OrdersDetailDao ordersDetailDao;
    private final CartItemDao cartItemDao;
    private final ProductDao productDao;

    public OrderService(OrderDao orderDao, OrdersDetailDao ordersDetailDao,
                        CartItemDao cartItemDao, ProductDao productDao) {
        this.orderDao = orderDao;
        this.ordersDetailDao = ordersDetailDao;
        this.cartItemDao = cartItemDao;
        this.productDao = productDao;
    }

    public Long addOrder(final List<OrderRequest> orderDetailRequests, final Long customerId) {
        final Long ordersId = orderDao.addOrders(customerId);

        for (final OrderRequest orderDetail : orderDetailRequests) {
            final Long cartId = orderDetail.getCartId();
            final Long productId = cartItemDao.findProductIdById(cartId);
            final int quantity = orderDetail.getQuantity();

            ordersDetailDao.addOrdersDetail(ordersId, productId, quantity);
            cartItemDao.deleteCartItem(cartId);
        }

        return ordersId;
    }

    @Transactional(readOnly = true)
    public List<OrdersResponse> findOrdersByCustomerId(final Long customerId) {
        final List<Long> orderIds = orderDao.findOrderIdsByCustomerId(customerId);

        return orderIds.stream()
                .map(orderId -> findOrderResponseDtoByOrderId(orderId))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public OrdersResponse findOrderById(final Long customerId, final Long orderId) {
        validateOrderIdByCustomerName(customerId, orderId);
        return findOrderResponseDtoByOrderId(orderId);
    }

    private OrdersResponse findOrderResponseDtoByOrderId(final Long orderId) {
        final List<OrderDetail> ordersDetails = new ArrayList<>();
        for (final OrderDetail productQuantity : ordersDetailDao.findOrdersDetailsByOrderId(orderId)) {
            final Product product = productDao.findProductById(productQuantity.getProductId())
                    .orElseThrow(InvalidProductException::new);

            final int quantity = productQuantity.getQuantity();
            ordersDetails.add(new OrderDetail(product, quantity));
        }

        Orders orders = new Orders(orderId, ordersDetails);
        return orders.toOrdersResponse();
    }

    private void validateOrderIdByCustomerName(final Long customerId, final Long orderId) {
        orderDao.findOrderIdsByCustomerIdAndOrderId(customerId, orderId)
                .orElseThrow(() -> new InvalidOrderException("유저에게는 해당 order_id가 없습니다."));
    }
}
