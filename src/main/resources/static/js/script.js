// document.addEventListener('DOMContentLoaded', function() {
//     // Инициализация обработчиков событий
//     var form = document.getElementById('getById-form');
//     if(form == null)
//         return;
//     form.addEventListener('submit', function(event) {
//         event.preventDefault();
//         getClothesById();
//     });
//     document.getElementById('deleteById-button').addEventListener('click', deleteClothesById);
//     //document.getElementById('editById-button').addEventListener('click', editClothes);
//     document.getElementById('addProduct-button').addEventListener('click', addProduct);
// });
function getProductById(idValue){
    if (idValue !== '') {
        let id = parseInt(idValue, 10);

        if (isNaN(id) || id < 0) {
            return;
        }

        let url = `/api/product/getProductById/${id}`;

        return fetch(url);
            // .then(response => response.json())
            // .then(json => {
            //     if(json.status === 'success')
            //         return json.data;
            //     else
            //         throw new Error(json.message);
            // });
    }
}
function deleteClothesById() {
    let idInput = document.getElementById('table-form-input-id');
    console.log(idInput);
    let idValue = idInput.value.trim();

    // Если поле не заполнено, не добавляем параметр
    if (idValue !== '') {
        let id = parseInt(idValue, 10);

        if (isNaN(id) || id < 0) {
            alert('ID должен быть положительным числом');
            return;
        }

        let url = `/api/clothes/deleteClothesById?id=${id}`;
        const request = new Request(url, {
            method: "POST",
        });

        fetch(request)
            .then(response => response.json())
            .then(json => {
                printMessage(json.message,json.status);
            });
    }
}
function editClothes() {
    // Собираем данные из формы
    const clothesData = {
        id: document.getElementById('input-id').value,
        name: document.getElementById('input-name').value,
        type: document.getElementById('input-type').value,
        size: document.getElementById('input-size').value,
        color: document.getElementById('input-color').value,
        brand: document.getElementById('input-brand').value,
        price: document.getElementById('input-price').value
    };

    fetch('/api/clothes/editClothesById', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'  // Важно!
        },
        body: JSON.stringify(clothesData)  // Преобразуем в JSON
    })
        .then(response => response.json())
        .then(json => {
            printMessage(json.message,json.status);
            if(json.status === 'success')
                showClothesProperties(json.data);
        });
}
function addProduct() {
    // Собираем данные из формы
    const productData = {
        name: document.getElementById('input-name').value,
        description: document.getElementById('input-description').value,
        price: document.getElementById('input-price').value
    };

    fetch('/api/product/addProduct', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'  // Важно!
        },
        body: JSON.stringify(productData)  // Преобразуем в JSON
    })
        .then(response => response.json())
        .then(json => {
            printMessage(json.message, json.status);
        });
}
function showClothesProperties(clothesData){
    // Проверяем, что данные существуют и являются объектом
    if (!clothesData || typeof clothesData !== 'object') {
        document.getElementById('input-id').value = '';
        document.getElementById('input-name').value = '';
        document.getElementById('input-type').value = '';
        document.getElementById('input-size').value = '';
        document.getElementById('input-color').value = '';
        document.getElementById('input-brand').value = '';
        document.getElementById('input-price').value = '';
        return;
    }

    var form = document.getElementById('editForm');
    if (form) {
        form.hidden = false;
    } else {
        console.error('Форма editForm не найдена');
        return;
    }

    console.log('Обрабатываем данные:', clothesData);

    // Получаем все элементы формы
    var input_id = document.getElementById('input-id');
    var input_name = document.getElementById('input-name');
    var input_type = document.getElementById('input-type');
    var input_size = document.getElementById('input-size');
    var input_color = document.getElementById('input-color');
    var input_brand = document.getElementById('input-brand');
    var input_price = document.getElementById('input-price');

    // Заполняем поля с проверкой наличия свойств
    if (input_id) input_id.value = clothesData.id || '';
    if (input_name) input_name.value = clothesData.name || '';
    if (input_type) input_type.value = clothesData.type || '';
    if (input_size) input_size.value = clothesData.size || '';
    if (input_color) input_color.value = clothesData.color || '';
    if (input_brand) input_brand.value = clothesData.brand || '';
    if (input_price) input_price.value = clothesData.price || '';
}
function printMessage(message, status){
    let container = document.getElementById('messageOut-span');
    container.innerText = message;
}
function setLike(commentId){
    let url = `/api/comments/setLike?id=${commentId}`;
    const request = new Request(url, {
        method: "POST",
    });

    fetch(request)
        .then(response => response.json())
        .then(json => {
            if(json.status === 'success'){
                //красим цифру в зелёный
            }
        });
}
function setDislike(commentId){
    let url = `/api/comments/setDislike?id=${commentId}`;
    const request = new Request(url, {
        method: "POST",
    });

    fetch(request)
        .then(response => response.json())
        .then(json => {
            if(json.status === 'success'){
                //красим цифру в красный
            }
        });
}
function loadReviews(productId){
    let url = `/api/reviews?id=${productId}`;
    const request = new Request(url, {
        method: "GET",
    });
    let reviewContainer = document.getElementById("Product-Reviews-container");
    fetch(request)
        .then(response => response.json())
        .then(json => {
            if(json.status === 'success'){
                json.data.forEach((review)=>{
                    let rev = getReviewCard(review);
                    reviewContainer.appendChild(rev);
                });
            }
        });
}
function getReviewCard(reviewData){
    let reviewCardhtml = `
    <div class="Review-card">
            <div class="comment-userinfo">
                        <img src="userProfileAvatar.jpg" alt="User-avatar">
                        <span>${reviewData.author.nickname}</span>
                    </div>
            <div class="Review-card-statline">
                <div class="comment-date">
                    <span>${reviewData.pubDate}</span>
                </div>
            </div>
            <div class="Review-card-text">
                <label for="Review-card-positiveSide">Достоинства</label>
                <div id="Review-card-positiveSide">
                    <span>
                        ${reviewData.review.positive}
                    </span>
                </div>
                <label for="Review-card-negativeSide">Недостатки</label>
                <div id="Review-card-negativeSide">
                    <span>
                        ${reviewData.review.negative}
                    </span>
                </div>
                <label for="Review-card-comment-author">Комментарий</label>
                <div id="Review-card-comment-author">
                    <span>
                        ${reviewData.review.comment}
                    </span>
                </div>
            </div>
            <div class="comment-controls">
                <button class="comment-controls-button-like">Like</button>
                <span class="comment-controls-comment-score">${reviewData.review.likeScore}</span>
                <button class="comment-controls-button-dislike">Dislike</button>
            </div>
            <div class="Review-card-comments">
            </div>
        </div>`;
}
//=============================Shopping cart and Wishlist============================================
const CART_KEY = 'shopping_cart';
const WISHLIST_KEY = 'wishlist';

// Получить корзину (всегда возвращает массив)
function getProductArray(arrayKey) {
    const array = localStorage.getItem(arrayKey);
    return array ? JSON.parse(array) : [];
}
function getCart() {return getProductArray(CART_KEY);}
function getWishlist() {return getProductArray(WISHLIST_KEY);}
// Сохранить корзину
function saveProductArray(key, array) {
    localStorage.setItem(key, JSON.stringify(array));
}
function saveCart(cart) {
    saveProductArray(CART_KEY, cart);
}
function saveWishlist(wishlist) {
    saveProductArray(WISHLIST_KEY, wishlist);
}
// Добавить или удалить товар
function toggleProduct(arrayKey, productId) {
    //arrayKey - CART_KEY || WISHLIST_KEY - ключ локального хранилища
    let array = [];

    if (arrayKey === CART_KEY)
        array = getCart();
    else if (arrayKey === WISHLIST_KEY)
        array = getWishlist();
    else
        return;

    const index = array.indexOf(productId);

    if (index === -1) {
        // Товара нет - добавляем
        array.push(productId);
        console.log(`Товар ${productId} добавлен в ${arrayKey}`);
    } else {
        // Товар есть - удаляем
        array.splice(index, 1);
        console.log(`Товар ${productId} удален из ${arrayKey}`);
    }
    saveProductArray(arrayKey, array);
}
function toggleCartProduct(productId){toggleProduct(CART_KEY, productId);}
function toggleWishlistProduct(productId){toggleProduct(WISHLIST_KEY, productId);}
// Проверить, есть ли товар в корзине
function isInCart(productId) {
    const cart = getCart();
    return cart.includes(productId);
}
function isInWishlist(productId) {
    const wishlist = getWishlist();
    return wishlist.includes(productId);
}
//==================================================================================
//=============================Wishlist============================================
function initCatalog(catalogId, ProductIdArray) {
    try {
        console.log(`initCatalog is activate! \n args: catalogId = ${catalogId}; ProductIdArray = ${ProductIdArray}`);
        if(!Array.isArray(ProductIdArray))
            return;
        let container = document.getElementById(catalogId);
        console.log(`container: ${container}`);
        if(container == null)
            return;

        ProductIdArray.forEach(c => {
            console.log(`c: ${c}`);
            if(!Number.isInteger(c))
            {
                return;
            }

            let productData = getProductById(c);
            productData
                .then(response => response.json())
                .then(json => {
                    if(json.status === 'success'){
                        const widget = getProductWidget(json.data);
                        container.appendChild(widget);
                    }
                    else
                        console.log(json.message);
                });
        })
    }
    catch (e) {
        console.log(e);
    }
}
function wishlistSetOff(productId) {
    toggleProduct(productId);
    const elements = document.querySelectorAll(`[data-widget-id="${productId}"]`);
    elements.forEach(el=>{
        el.remove();
    })
}
function getProductWidget(productData) {
    const div = document.createElement('div');
    div.className = 'product-widget';
    div.setAttribute('data-widget-id', productData.id)
    div.innerHTML = `
        <div class="block-major media">
            <div class="block-minor">
                <img src="/PhotoIconTemplate.png" alt="productPhoto">
            </div>
        </div>
        <div class="block-major info">
            <div class="block-minor info">
                <a href="/product/${productData.id}">
                    <span class="product-name-text">
                        ${escapeHtml(productData.name)}
                    </span>
                </a>
            </div>
            <div class="block-minor rating">
                <div class="block-micro">
                    <span> *rating* </span>
                </div>
                <div class="block-micro">
                    <span> *reserved* </span>
                </div>
            </div>
        </div>
        <div class="block-major buyActions">
            <div class="block-minor">
                <div class="block-micro price">
                    <span>${productData.price} ₽</span>
                </div>
            </div>
            <div class="block-minor">
                <div class="block-micro buy">
                    <button class="buy-btn" onclick="toggleCartProduct(${productData.id})">Купить</button>
                </div>
                <div class="block-micro wishlist">
                    <button class="toggleWishlist-btn active" onclick="toggleWishlistProduct(${productData.id})">
                        <svg class="wishlist_icon"
                             xmlns="http://www.w3.org/2000/svg"
                             xmlns:xlink="http://www.w3.org/1999/xlink"
                             viewBox="0 0 176 157"
                             width="40"
                             height="40">
                            <path d="M148.136,84.904 L94.518,138.471 C92.748,140.239 90.562,141.338 88.275,141.771 C84.480,142.518 80.394,141.425 77.453,138.487 L23.835,84.920 C6.685,67.786 6.685,40.006 23.835,22.872 C40.986,5.738 68.792,5.738 85.942,22.872 L85.977,22.907 L86.029,22.855 C103.179,5.721 130.985,5.721 148.136,22.855 C165.286,39.989 165.286,67.769 148.136,84.904 Z"/>
                        </svg>
                    </button>
                </div>
                <div class="block-micro buycheck">
                    <!-- TODO: Добавить обработчик -->
                    <input type="checkbox" name="addToBuyCheckbox"/>
                </div>
            </div>
        </div>
    `;
    return div;
}
// Функция для защиты от XSS
function escapeHtml(str) {
    if (!str) return '';
    return str
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;')
        .replace(/'/g, '&#39;');
}
//=================================================================================
//=============================ShoppingCart============================================
function initShoppingCart() {
    try {
        let cart = getCart();
        let container = document.getElementById('wishlist-catalog');
        if(container == null)
            return;

        cart.forEach(c => {
            let productData = getProductById(c);
            productData
                .then(response => response.json())
                .then(json => {
                    if(json.status === 'success'){
                        const widget = getProductWidget(json.data);
                        container.appendChild(widget);
                    }
                    else
                        console.log(json.message);
                });
        })
    }
    catch (e) {
        console.log(e);
    }
}
//=================================================================================
function showComments(reviewId){
    let revDiv = document.getElementById(`review-id-${reviewId}`);
    //===
    //сначала добавить форму для отправки комментария
    //revDiv.append()
    //===
}
function getReviewComments(reviewId){

}