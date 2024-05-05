document.addEventListener("alpine:init", () => {
  Alpine.data("initData", (orderNumber) => ({
    orderNumber: orderNumber,
    orderDetails: {
      items: [],
      customer: {},
      deliveryAddress: {},
    },
    errorMessage: "",
    init() {
      updateCartItemCount();
      this.getOrderDetails(this.orderNumber)
        .then((orderDetails) => {
          this.orderDetails = orderDetails;
        })
        .catch((error) => {
          console.error("Error fetching order details:", error);
          this.errorMessage =
            "Failed to fetch order details. Please try again later.";
        });
    },
    async getOrderDetails(orderNumber) {
      const response = await fetch("/api/orders/" + orderNumber);
      if (!response.ok) {
        throw new Error(
          `HTTP error status: ${response.status} ${response.statusText}`,
        );
      }
      return await response.json();
    },
  }));
});
