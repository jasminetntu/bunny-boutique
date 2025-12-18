let ordersService;

class OrdersService {
    createOrder() {
        const url = `${config.baseUrl}/orders`;
        const headers = userService.getHeaders();

        return axios.post(url, {}, {headers})
            .then(response => {
                return response.data;
            })
            .catch(error => {
                const data = {
                    error: "Checkout cart order failed."
                };

                templateBuilder.append("error", data, "errors");
                // rethrow so callers can handle promise rejections
                throw error;
            });
    }

}

document.addEventListener('DOMContentLoaded', () => {
    ordersService = new OrdersService();
});