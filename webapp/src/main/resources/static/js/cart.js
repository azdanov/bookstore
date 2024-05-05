document.addEventListener("alpine:init", () => {
  Alpine.data("initData", () => ({
    submitting: false,
    cart: { items: [] },
    orderForm: {
      customer: {
        name: "Anton Ždanov",
        email: "azdanov@example.com",
        phone: "999999999999",
      },
      deliveryAddress: {
        addressLine1: "Pikk 12",
        addressLine2: "Kesklinna linnaosa",
        city: "Tallinn",
        state: "Harju maakond",
        zipCode: "10123",
        country: "Estonia",
      },
    },
    errorMessage: "",

    init() {
      this.loadCart();
      updateCartItemCount();
    },
    loadCart() {
      try {
        this.cart = getCart();
      } catch (error) {
        console.error("Error loading cart:", error);
        this.errorMessage = "Failed to load cart. Please try again later.";
      }
    },
    updateItemQuantity(code, quantity) {
      try {
        updateProductQuantity(code, quantity);
        updateCartItemCount();
        this.loadCart();
      } catch (error) {
        console.error("Error updating item quantity:", error);
        this.errorMessage =
          "Failed to update item quantity. Please try again later.";
      }
    },
    getCartTotal() {
      return this.cart.items.reduce(
        (total, item) => total + item.price * item.quantity,
        0,
      );
    },
    clearCart() {
      try {
        clearCart();
        updateCartItemCount();
        this.cart = { items: [] };
      } catch (error) {
        console.error("Error removing cart:", error);
        this.errorMessage = "Failed to remove cart. Please try again later.";
      }
    },
    async createOrder() {
      this.submitting = true;
      let order = { ...this.orderForm, items: this.cart.items };

      try {
        const response = await fetch("/api/orders", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify(order),
        });

        if (!response.ok) {
          throw new Error(
            `HTTP error status: ${response.status} ${response.statusText}`,
          );
        }

        const json = await response.json();
        this.clearCart();
        window.location = "/orders/" + json.orderNumber;
      } catch (error) {
        console.error("Order Creation Error:", error);
        this.errorMessage = "Order creation failed. Please try again later.";
      } finally {
        this.submitting = false;
      }
    },
  }));
});
