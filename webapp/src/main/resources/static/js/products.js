document.addEventListener("alpine:init", () => {
  Alpine.data("initData", (page) => ({
    page: page,
    products: {
      data: [],
    },
    errorMessage: "",

    init() {
      this.loadProducts(this.page)
        .then((products) => {
          this.products = products;
        })
        .catch((error) => {
          console.error("Error loading products:", error);
          this.errorMessage =
            "Failed to load products. Please try again later.";
        });
      updateCartItemCount();
    },
    async loadProducts(page) {
      const response = await fetch("/api/products?page=" + page);
      if (!response.ok) {
        throw new Error(
          `HTTP error status: ${response.status} ${response.statusText}`,
        );
      }
      return await response.json();
    },
    addToCart(product) {
      try {
        addProductToCart(product);
        updateCartItemCount();
      } catch (error) {
        console.error("Error adding product to cart:", error);
        this.errorMessage =
          "Failed to add product to cart. Please try again later.";
      }
    },
  }));
});
