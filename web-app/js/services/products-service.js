let productService;

class ProductService {

    photos = [];


    filter = {
        cat: undefined,
        minPrice: undefined,
        maxPrice: undefined,
        subCategory: undefined,
        queryString: () => {
            let qs = "";
            if(this.filter.cat){ qs = `cat=${this.filter.cat}`; }
            if(this.filter.minPrice)
            {
                const minP = `minPrice=${this.filter.minPrice}`;
                if(qs.length>0) {   qs += `&${minP}`; }
                else { qs = minP; }
            }
            if(this.filter.maxPrice)
            {
                const maxP = `maxPrice=${this.filter.maxPrice}`;
                if(qs.length>0) {   qs += `&${maxP}`; }
                else { qs = maxP; }
            }
            if(this.filter.subCategory)
            {
                const sub = `subCategory=${this.filter.subCategory}`;
                if(qs.length>0) {   qs += `&${sub}`; }
                else { qs = sub; }
            }

            return qs.length > 0 ? `?${qs}` : "";
        }
    }

    constructor() {

        // Attempt to load a photos index if present (optional). Missing index is non-fatal.
        axios.get("/images/products/photos.json")
            .then(response => {
                this.photos = response.data;
            })
            .catch(() => {
                // ignore - we'll still compute imageSrc directly from config.imageBase
            });
    }

    hasPhoto(photo){
        return this.photos.filter(p => p == photo).length > 0;
    }

    addCategoryFilter(cat)
    {
        if(cat == 0) this.clearCategoryFilter();
        else this.filter.cat = cat;
    }
    addMinPriceFilter(price)
    {
        if(price == 0 || price == "") this.clearMinPriceFilter();
        else this.filter.minPrice = price;
    }
    addMaxPriceFilter(price)
    {
        if(price == 0 || price == "") this.clearMaxPriceFilter();
        else this.filter.maxPrice = price;
    }
    addSubcategoryFilter(subCategory)
    {
        if(subCategory == "") this.clearSubCategoryFilter();
        else this.filter.subCategory = subCategory;
    }

    clearCategoryFilter()
    {
        this.filter.cat = undefined;
    }
    clearMinPriceFilter()
    {
        this.filter.minPrice = undefined;
    }
    clearMaxPriceFilter()
    {
        this.filter.maxPrice = undefined;
    }
    clearSubCategoryFilter()
    {
        this.filter.subCategory = undefined;
    }

    search()
    {
        const url = `${config.baseUrl}/products${this.filter.queryString()}`;

        axios.get(url)
             .then(response => {
                 let data = {};
                 data.products = response.data;

                data.products.forEach(product => {
                    // if backend didn't provide image filename, use placeholder name
                    if(!product.imageUrl || product.imageUrl === null || product.imageUrl === undefined)
                    {
                        product.imageUrl = "no-image.jpg";
                    }

                    try {
                        if(product.imageUrl === 'no-image.jpg'){
                            product.imageSrc = `/images/products/new/no-image.jpg`;
                        }
                        else {
                            product.imageSrc = `${config.imageBase}/${product.imageUrl}`;
                        }
                    }
                    catch (e) {
                        // if config isn't available, fall back to legacy path
                        product.imageSrc = `/images/products/${product.imageUrl}`;
                    }
                })
                 templateBuilder.build('product', data, 'content', this.enableButtons);

             })
            .catch(error => {

                const data = {
                    error: "Searching products failed."
                };

                templateBuilder.append("error", data, "errors")
            });
    }

    enableButtons()
    {
        const buttons = [...document.querySelectorAll(".add-button")];

        if(userService.isLoggedIn())
        {
            buttons.forEach(button => {
                button.classList.remove("invisible")
            });
        }
        else
        {
            buttons.forEach(button => {
                button.classList.add("invisible")
            });
        }
    }

}





document.addEventListener('DOMContentLoaded', () => {
    productService = new ProductService();

});
