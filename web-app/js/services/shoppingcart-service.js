let cartService;

class ShoppingCartService {

    cart = {
        items:[],
        total:0
    };

    addToCart(productId)
    {
        const url = `${config.baseUrl}/cart/products/${productId}`;
        const headers = userService.getHeaders();

        axios.post(url, {}, {headers})
            .then(response => {
                const successData = {
                    message: `Added item to order.`
                };

                this.setCart(response.data)

                templateBuilder.append("message", successData, "errors");

                this.updateCartDisplay()

            })
            .catch(error => {

                const data = {
                    error: "Add to cart failed."
                };

                templateBuilder.append("error", data, "errors")
            })
    }

    setCart(data)
    {
        this.cart = {
            items: [],
            total: 0
        }

        this.cart.total = data.total;

        for (const [key, value] of Object.entries(data.items)) {
            this.cart.items.push(value);
        }
    }

    loadCart()
    {

        const url = `${config.baseUrl}/cart`;

        axios.get(url)
            .then(response => {
                this.setCart(response.data)

                this.updateCartDisplay()

            })
            .catch(error => {

                const data = {
                    error: "Load cart failed."
                };

                templateBuilder.append("error", data, "errors")
            })

    }

    loadCartPage()
    {
        // templateBuilder.build("cart", this.cart, "main");

        const main = document.getElementById("main")
        main.innerHTML = "";

        let div = document.createElement("div");
        div.classList="filter-box";
        main.appendChild(div);

        const contentDiv = document.createElement("div")
        contentDiv.id = "content";
        contentDiv.classList.add("content-form");

        const cartHeader = document.createElement("div")
        cartHeader.classList.add("cart-header")

        const h1 = document.createElement("h1")
        h1.innerText = "Your Cart";
        cartHeader.appendChild(h1);

        const button = document.createElement("button");
        button.classList.add("btn")
        button.classList.add("btn-danger")
        button.innerText = "Clear";
        button.addEventListener("click", () => this.clearCart());
        cartHeader.appendChild(button)

        contentDiv.appendChild(cartHeader)
        main.appendChild(contentDiv);

        // create 2-column wrapper: left = items, right = summary
        const flexWrapper = document.createElement("div");
        flexWrapper.classList.add("cart-flex-wrapper");

        // left: item list
        const itemsLeft = document.createElement("div");
        itemsLeft.id = "cart-item-list";
        itemsLeft.classList.add("cart-items-left");

        // populate items into left column
        this.cart.items.forEach(item => {
            this.buildItem(item, itemsLeft)
        });

        // right: summary
        const summaryWrapper = document.createElement("div");
        summaryWrapper.classList.add("cart-summary-right");

        const summaryBox = document.createElement("div");
        summaryBox.classList.add("summary-box");

        const h3 = document.createElement("h3");
        h3.innerText = "Order Summary";
        summaryBox.appendChild(h3);

        const hr = document.createElement("hr");
        summaryBox.appendChild(hr);

        const summaryRow = document.createElement("div");
        summaryRow.classList.add("summary-row");
        const labelSpan = document.createElement("span");
        labelSpan.innerText = "Total:";
        const totalSpan = document.createElement("span");
        totalSpan.id = "cart-total";
        totalSpan.innerText = `$${this.cart.total.toFixed(2)}`;
        summaryRow.appendChild(labelSpan);
        summaryRow.appendChild(totalSpan);
        summaryBox.appendChild(summaryRow);

        const checkoutBtn = document.createElement("button");
        checkoutBtn.classList.add("btn","btn-success","w-100","mt-3");
        checkoutBtn.innerText = "Checkout";
        checkoutBtn.addEventListener('click', () => handleCheckout());
        summaryBox.appendChild(checkoutBtn);

        summaryWrapper.appendChild(summaryBox);

        // assemble and append to content
        flexWrapper.appendChild(itemsLeft);
        flexWrapper.appendChild(summaryWrapper);
        contentDiv.appendChild(flexWrapper);
    }

    buildItem(item, parent)
    {
        // outer card
        let outerDiv = document.createElement("div");
        outerDiv.classList.add("cart-item");

        // left: photo
        let photoDiv = document.createElement("div");
        photoDiv.classList.add("photo")
        let img = document.createElement("img");
        img.src = `/images/products/${item.product.imageUrl}`
        img.addEventListener("click", () => {
            showImageDetailForm(item.product.name, img.src)
        })
        photoDiv.appendChild(img)

        // right: details (name, price, description, quantity)
        let detailsDiv = document.createElement("div");
        detailsDiv.classList.add("details");

        // header: name and subtotal
        let headerDiv = document.createElement("div");
        headerDiv.classList.add("item-header");

        let nameDiv = document.createElement("div");
        nameDiv.classList.add("item-name");
        nameDiv.innerText = item.product.name;

        let subtotalDiv = document.createElement("div");
        subtotalDiv.classList.add("item-subtotal");
        const subtotal = (item.product.price || 0) * (item.quantity || 0);
        subtotalDiv.innerText = `$${subtotal.toFixed(2)}`;

        headerDiv.appendChild(nameDiv);
        headerDiv.appendChild(subtotalDiv);

        // price (below header)
        let priceDiv = document.createElement("div");
        priceDiv.classList.add("price");
        priceDiv.innerText = `$${item.product.price}`;

        // description (under price)
        let descriptionDiv = document.createElement("div");
        descriptionDiv.classList.add("description");
        descriptionDiv.innerText = item.product.description;

        // quantity (under description)
        let quantityDiv = document.createElement("div");
        quantityDiv.classList.add("quantity");

        let quantityLabel = document.createElement("label")
        quantityLabel.innerText = "Quantity: ";

        let quantityInput = document.createElement("input")
        quantityInput.type = "number";
        quantityInput.min = "0";
        quantityInput.max = "999"; // limit to 3 digits
        quantityInput.value = item.quantity;

        quantityInput.addEventListener('change', (event) => {
            const newQuantity = parseInt(event.target.value)
            cartService.updateItemQuantity(item.product.productId, newQuantity);
        });

        quantityDiv.appendChild(quantityLabel);
        quantityDiv.appendChild(quantityInput);

        detailsDiv.appendChild(headerDiv);
        detailsDiv.appendChild(priceDiv);
        detailsDiv.appendChild(descriptionDiv);
        detailsDiv.appendChild(quantityDiv);

        outerDiv.appendChild(photoDiv);
        outerDiv.appendChild(detailsDiv);

        parent.appendChild(outerDiv);
    }

    clearCart()
    {

        const url = `${config.baseUrl}/cart`;

        axios.delete(url)
             .then(response => {
                 this.cart = {
                     items: [],
                     total: 0
                 }

                 this.cart.total = response.data.total;

                 for (const [key, value] of Object.entries(response.data.items)) {
                     this.cart.items.push(value);
                 }

                 this.updateCartDisplay()
                 this.loadCartPage()

             })
             .catch(error => {

                 const data = {
                     error: "Empty cart failed."
                 };

                 templateBuilder.append("error", data, "errors")
             })
    }

    updateCartDisplay()
    {
        try {
            const itemCount = this.cart.items.length;
            const cartControl = document.getElementById("cart-items")

            cartControl.innerText = itemCount;
            // update displayed total if present
            const totalEl = document.getElementById("cart-total");
            if (totalEl) {
                totalEl.innerText = `$${(this.cart.total || 0).toFixed(2)}`;
            }
        }
        catch (e) {

        }
    }

    updateItemQuantity(productId, newQuantity) {
        const url = `${config.baseUrl}/cart/products/${productId}`;

        const headers = userService.getHeaders();

        const body = {
            quantity: newQuantity
        };

        // preserve scroll position of the items column so updating quantity doesn't jump
        const itemsEl = document.getElementById('cart-item-list');
        const prevScroll = itemsEl ? itemsEl.scrollTop : 0;

        axios.put(url, body, {headers})
             .then(response => {
                 this.setCart(response.data)

                 this.updateCartDisplay()
                 this.loadCartPage()

                 // restore scroll position after rebuilding the list
                 const newItemsEl = document.getElementById('cart-item-list');
                 if (newItemsEl) {
                     newItemsEl.scrollTop = prevScroll;
                 }

             })
             .catch(error => {

                 const data = {
                     error: "Update cart quantity failed."
                 };

                 templateBuilder.append("error", data, "errors")
             })
    }


}


document.addEventListener('DOMContentLoaded', () => {
    cartService = new ShoppingCartService();

    if(userService.isLoggedIn())
    {
        cartService.loadCart();
    }

});
