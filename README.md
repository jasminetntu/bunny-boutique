# 🎀 Bunny Boutique
> **Capstone 3: E-Commerce API & Site**
>
> Year Up United (Bay Bytes) - _Technical Academy Fall 2025_
---

## 🐰 Description
A custom e-commerce application with a web interface, designed for a fictional clothing store named "Bunny Boutique." This tool allows customers to browse clothes, update their profile, add items to cart, and checkout their order.

Built with Java, HTML, CSS, and JavaScript to gain familiarity with APIs through Spring Boot, SQL/MySQL, and full-stack development.

---
### 🗝️ Key Features
- **Dynamic Product Catalog**: Users can browse a full catalog of clothing items categorized by type (e.g., hoodies, dresses) with real-time filtering by category, price range, and color.
- **User Profile Management**: A profile section where users can update their personal contact information and shipping addresses.
- **Persistent Shopping Cart**: A shopping cart system that tracks item quantities, calculates individual item totals, and maintains a running order subtotal.
- **Checkout System**: A full-stack checkout process that transitions cart items into the permanent database order history using Spring Boot and MySQL.
- **Restricted API Access**: Secure backend communication using Principal-based authentication to ensure users can only access their own shopping carts and order history.

### 💘 Future Features
- **Register/Sign-Up**: Allow users to easily register for an account on the web page.
- **Order History**: Allow users to view their past orders and tracking status.
- **Admin Dashboard**: An interface for store owners to add new products or update stock levels.
- **Stock Display**: Show which items are in stock and out of stock.
- **Discount Integration**: Implement discounts on items.
- **Payment Integration**: Allow users to input payment information before checking out.
- **Transition**: Add transitions between pages for a better UI experience.

---
## 🍀 Quick Start
Follow these steps to get both the backend API and the frontend website running on your local machine.

🌸 **1. Clone the Repository**
Open your terminal and run the following command to download the project:

``` bash
git clone https://github.com/jasminetntu/bunny-boutique.git
```

🌸 **2. Set Up the Database**
Before running the code, you must initialize the MySQL database.
- Locate the script: `api/database/create_database_bunnyboutique.sql`.
- Run this script in your preferred MySQL workbench or CLI to create the tables and populate with the initial data.

🌸 **3. Launch the Backend (API)**
It is important to open the api folder specifically as its own project in your IDE (e.g., IntelliJ) rather than opening the entire root folder.
- **Open Project**: Open the api directory in your IDE.
- **Configure Run Profile**: Create a new Application run configuration for `BunnyBoutiqueApplication.java`.
- **Set Environment Variables**: In the "Environment Variables" section of your run configuration, add your database credentials:
   - `DB_USERNAME=`(your MySQL username, e.g., root)
   - `DB_PASSWORD=`(your MySQL password)
- **Run**: Start the application. Keep this process running in the background to handle data requests from the frontend.

🌸 **4. Launch the Frontend (Web App)**
Now that the server is active, you can launch the interface.
- **Open Project**: Open the web-app folder in a code editor (like VS Code).
- **Launch**: Locate index.html in the root of the web-app folder.
- **View**: Launch the website through `index.html` on your IDE (right-click & "Open with Live Server" on VS Code).

🌸 **5. Test Credentials**
Once the site is loaded, you can log in using the following test account:
- **Username**: user
- **Password**: password

And you're done! Have fun playing around with the app.

---

## 💭 Interesting Piece of Code
🌟 **Backend**
``` java
// Line 36 in OrdersController.java

@PostMapping()
    public Order addOrder(Principal principal) {
        String userName = principal.getName(); // get currently logged in username
        User user = userDao.getByUserName(userName); // find db user by username
        int userId = user.getId(); // get userId

        Profile profile = profileDao.getByUserId(userId);
        ShoppingCart shoppingCart = shoppingCartDao.getByUserId(userId);

        if (shoppingCart.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "ERROR: Cart cannot be empty when adding order.");
        }

        Order order = ordersDao.create(profile, shoppingCart.getTotal());
        int orderId = order.getOrderId();

        shoppingCart.getItems().forEach((productId, cartItem) ->
                orderLineItemsDao.create(orderId, productId, cartItem));

        shoppingCartDao.delete(userId);

        return order;
    }
```

The backend code snippet is interesting to me because it shows how everything is connected. This was my first time learning APIs and Spring Boot, so it was cool to see how the different components/DAOs are interconnected through the Controller class. This promotes the concept of encapsulation and cohesion.

🌟 **Frontend**
``` html
<!-- Line 12 in profile.html -->

<div class="col-md-6">
  <div class="form-floating mb-3">
    <input type="text" class="form-control" id="firstName" value='{{firstName}}'>
    <label for="firstName">First Name</label>
  </div>
</div>

<div class="col-md-6">
  <div class="form-floating mb-3">
    <input type="text" class="form-control" id="lastName" value='{{lastName}}'>
    <label for="lastName">Last Name</label>
  </div>
</div>
```

The frontend code snippet is interesting to me because I'm not too familiar with frontend development, so it was fun to figure out how to make the profile fields better aligned. The snippet shows how the first and last name boxes are side by side horizontally.

---

## 📁 File Structure
```
bunny-boutique/
├── api/                                              // backend directory
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── org.yearup/
│   │   │   │       ├── configurations/               // security and app config
│   │   │   │       │   └── DatabaseConfig.java
│   │   │   │       │
│   │   │   │       ├── controllers/                  // API endpoints
│   │   │   │       │
│   │   │   │       ├── data/                         // DAO layer
│   │   │   │       │   └── mysql/
│   │   │   │       │
│   │   │   │       ├── models/                       // data models
│   │   │   │       │   └── authentication/
│   │   │   │       │
│   │   │   │       ├── security/                     // JWT and auth logic
│   │   │   │       │
│   │   │   │       └── BunnyBoutiqueApplication.java // backend entry point
│   │   │   │
│   │   │   └── resources/                            // app properties
│   │   │
│   │   └── test/                                     // unit and integration tests
│   │
│   ├── pom.xml                                       // Maven dependencies
│   └── database/                                     // database scripts
│
├── web-app/                                          // frontend directory
│   ├── css/                                          // stylesheets
│   │
│   ├── images/                                       // static assets
│   │   └── products/
│   │   │   ├── new/                                  // custom images for bunny bouqtiue
│   │   │   └── starter/                              // starter images (unused)
│   │
│   ├── js/                                           // frontend Logic
│   │   ├── services/                                 // API service calls
│   │   ├── application.js
│   │   └── config.js
│   │
│   ├── templates/                                    // HTML files
│   │
│   └── index.html                                    // frontend entry point
│
├── README.md                                         // project documentation
└── .gitignore
```

---
## 📸 Screenshots
### .☘︎ ݁˖ Home
<img width="1512" height="825" alt="image" src="https://github.com/user-attachments/assets/8bd2131a-8b65-43ff-905b-dcf70725b4a3" />
<img width="1512" height="825" alt="image" src="https://github.com/user-attachments/assets/06300289-e4e0-4ffc-ae11-0e18da56919b" />

### .☘︎ ݁˖ Filter Example
<img width="1512" height="825" alt="image" src="https://github.com/user-attachments/assets/a1daa98f-494e-4548-a468-866b9f0c7a6a" />

### .☘︎ ݁˖ Profile
<img width="1512" height="825" alt="image" src="https://github.com/user-attachments/assets/6eaec869-72ca-475f-86bc-30af86189d96" />

### .☘︎ ݁˖ Cart
<img width="1512" height="825" alt="image" src="https://github.com/user-attachments/assets/efbe065d-2244-4e3e-b323-55629c6cad7e" />

---
## 💌 Acknowledgements
This project was developed using starter files as a foundation. It has been built upon, expanded, and polished with love by me! :)
