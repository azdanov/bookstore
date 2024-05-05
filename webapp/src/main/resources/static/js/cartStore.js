const BOOKSTORE_STATE_KEY = "BOOKSTORE_STATE";

function getCart() {
  const cart = localStorage.getItem(BOOKSTORE_STATE_KEY);
  return cart ? JSON.parse(cart) : { items: [] };
}

function saveCart(cart) {
  localStorage.setItem(BOOKSTORE_STATE_KEY, JSON.stringify(cart));
}

function clearCart() {
  localStorage.removeItem(BOOKSTORE_STATE_KEY);
}

function addProductToCart(product) {
  const cart = getCart();
  const cartItem = cart.items.find((item) => item.code === product.code);

  if (cartItem) {
    cartItem.quantity++;
  } else {
    cart.items.push({ ...product, quantity: 1 });
  }

  saveCart(cart);
}

function updateProductQuantity(code, quantity) {
  const cart = getCart();
  const cartItem = cart.items.find((item) => item.code === code);

  if (cartItem) {
    if (quantity < 1) {
      cart.items = cart.items.filter((item) => item.code !== code);
    } else {
      cartItem.quantity = quantity;
    }

    saveCart(cart);
  }
}

function updateCartItemCount() {
  const cart = getCart();
  const count = cart.items.reduce((total, item) => total + item.quantity, 0);
  document.getElementById("cart-item-count").textContent = `(${count})`;
}
