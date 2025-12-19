
function showLoginForm()
{
    templateBuilder.build('login-form', {}, 'login');
}

function showRegisterForm()
{
    templateBuilder.build('register-form', {}, 'login');
}

function hideModalForm()
{
    templateBuilder.clear('login');
}

function toggleHero(show)
{
    const heroSection = document.getElementById('hero-section');
    if (!heroSection) return; // exit if hero doesn't exist

    if (show) {
        heroSection.classList.remove('hidden');
    } else {
        heroSection.classList.add('hidden');
    }
}

function login()
{
    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    userService.login(username, password);
    hideModalForm()
}

function register()
{
    const username = document.getElementById("reg-username").value;
    const password = document.getElementById("reg-password").value;
    const confirm = document.getElementById("reg-confirm").value;

    userService.register(username, password, confirm);
    hideModalForm()
}

function showImageDetailForm(product, imageUrl)
{
    const imageDetail = {
        name: product,
        imageUrl: imageUrl
    };

    templateBuilder.build('image-detail',imageDetail,'login')
}

function loadHome()
{
    document.querySelector('main').style.display = 'grid';

    templateBuilder.build('home',{},'main')

    productService.search();
    categoryService.getAllCategories(loadCategories);

    toggleHero(true);
}

function editProfile()
{
    profileService.loadProfile();
    toggleHero(false);
}

function saveProfile()
{
    const firstName = document.getElementById("firstName").value;
    const lastName = document.getElementById("lastName").value;
    const phone = document.getElementById("phone").value;
    const email = document.getElementById("email").value;
    const address = document.getElementById("address").value;
    const city = document.getElementById("city").value;
    const state = document.getElementById("state").value;
    const zip = document.getElementById("zip").value;

    const profile = {
        firstName,
        lastName,
        phone,
        email,
        address,
        city,
        state,
        zip
    };

    profileService.updateProfile(profile);
}

function showCart()
{
    document.querySelector('main').style.display = 'block';
    cartService.loadCartPage();
    toggleHero(false);
}

function clearCart()
{
    cartService.clearCart();
    cartService.loadCartPage();
}

function handleCheckout() {
    ordersService.createOrder()
        .then(order => {
            const successData = {
                message: `Order #${order.orderId} placed successfully!`
            };

            // Show success using the existing message template
            templateBuilder.append("message", successData, "errors");

            cartService.clearCart();

            // clear local cart display
            const cartList = document.getElementById('cart-item-list');
            if (cartList) cartList.innerHTML = "<h3>Thank you for your order!</h3>";
            const totalEl = document.getElementById('cart-total');
            if (totalEl) totalEl.innerText = "$0.00";
        })
        .catch(error => {
            const data = { error: 'Checkout failed. Please try again.' };
            templateBuilder.append('error', data, 'errors');
            console.error(error);
        });
}

function setCategory(control)
{
    productService.addCategoryFilter(control.value);
    productService.search();

}

function setSubcategory(control)
{
    productService.addSubcategoryFilter(control.value);
    productService.search();

}

function setMinPrice(control)
{
    // const slider = document.getElementById("min-price");
    const label = document.getElementById("min-price-display")
    label.innerText = control.value;

    const value = control.value != 0 ? control.value : "";
    productService.addMinPriceFilter(value)
    productService.search();

}

function setMaxPrice(control)
{
    // const slider = document.getElementById("min-price");
    const label = document.getElementById("max-price-display")
    label.innerText = control.value;

    const value = control.value != 900 ? control.value : "";
    productService.addMaxPriceFilter(value)
    productService.search();

}

function closeError(control)
{
    setTimeout(() => {
        control.click();
    },3000);
}

document.addEventListener('DOMContentLoaded', () => {

    loadHome();
});
