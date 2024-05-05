document.addEventListener("alpine:init", () => {
  Alpine.data("initData", () => ({
    orders: [],
    errorMessage: "",
    init() {
      this.loadOrders()
        .then((orders) => {
          this.orders = orders;
        })
        .catch((error) => {
          console.error("Error loading orders:", error);
          this.errorMessage = "Failed to load orders. Please try again later.";
        });
      updateCartItemCount();
    },
    async loadOrders() {
      const response = await fetch("/api/orders");
      if (!response.ok) {
        throw new Error(
          `HTTP error status: ${response.status} ${response.statusText}`,
        );
      }
      return await response.json();
    },
  }));
});
