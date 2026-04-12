
async function getProductById(idValue){
    if (idValue !== '') {
        let id = parseInt(idValue, 10);

        if (isNaN(id) || id < 0) {
            return;
        }

        let url = `/api/product/getProductById/${id}`;

        return fetch(url);
    }
}
async function getProductArrayById(IdArray){
    if (!isArrayOfIntegers(IdArray))
        return []
    let productDataArray = [];
    for (const id of IdArray) {
        await getProductById(id)
            .then(response =>response.json())
            .then(js=>{
                if(js.status ==='success')
                    productDataArray.push(js.data);
            });
    }
    return productDataArray;
}
function isArrayOfIntegers(arr) {
    // Проверка, что это массив
    if (!Array.isArray(arr)) {
        return false;
    }

    // Проверка, что каждый элемент - целое число
    return arr.every(item => Number.isInteger(item));
}
let current_page = 0;
let current_size= 10;
async function getProductList(page, size){
    if(page == null || Number.isNaN(page))
        page = current_page;
    if(size == null || Number.isNaN(size))
        size = current_size;

    let url = `/api/product/list?page=${page}&size=${size}`;
    const request = new Request(url, {
        method: "GET"
    });
    return await fetch(request)
        .then(response =>response.json())
        .then(js=>{
            if(js.status !== 'success')
            {
                console.error(js.message);
                return  [];
            }
            else
                return js.data;
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
function printMessage(message){
    let container = document.getElementById('messageOut-span');
    container.innerText = message;
}
//=============================Shopping cart and Wishlist============================================
const CART_KEY = 'shopping_cart';
const WISHLIST_KEY = 'wishlist';

function getLSProductIdArray(key) {
    try {
        const LSString = localStorage.getItem(key);

        if (!LSString) {
            return [];
        }

        const parsed = JSON.parse(LSString);

        // Проверяем, что результат - массив
        if (Array.isArray(parsed)) {
            return parsed;
        }

        return [];
    } catch (error) {
        console.error('Ошибка при чтении из localStorage:', error);
        return [];
    }
}
// Сохранить корзину
function saveIdArrayToLS(key, array) {
    if(!Array.isArray(array))
        console.error('Некорректные данные');


    localStorage.setItem(key, JSON.stringify(array));
}
// Добавить или удалить товар
function toggleProduct(arrayKey, productId) {
    //arrayKey - CART_KEY || WISHLIST_KEY - ключ локального хранилища
    let array = [];

    if (arrayKey === CART_KEY || arrayKey === WISHLIST_KEY)
        array = getLSProductIdArray(arrayKey);
    else
        return;

    console.log(`productId: ${productId}`);
    console.log(`array: `);
    console.log(array);
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
    saveIdArrayToLS(arrayKey, array);
}
function toggleCartProduct(productId){toggleProduct(CART_KEY, productId);}
function toggleWishlistProduct(productId){
    toggleProduct(WISHLIST_KEY, productId);
    const widget = document.querySelector(`[data-widget-id="${productId}"]`);
    const button = widget.querySelector(`[data-widget-element="button-wishlist"]`);
    if(isInWishlist(productId) || !button.classList.contains('active'))
        button.classList.add('active');
    else
        button.classList.remove('active');
}

function isInWishlist(productId) {
    const arr = getLSProductIdArray(WISHLIST_KEY);
    return arr.includes(productId);
}
//=============================Wishlist============================================
async function initPage(methodNumber) {
    let productData = [];
    switch (methodNumber) {
        case 0: productData = await getProductList(); break;
        case 1: productData = await getProductArrayById(getLSProductIdArray(WISHLIST_KEY)); break;
        case 2: productData = await getProductArrayById(getLSProductIdArray(CART_KEY)); break;
        default: return;
    }
    console.log(productData);
    initCatalog(productData);
}
function initCatalog(data) {
    try {
        console.log(data);
        if(data == null)
            return;

        const mainCatalog = document.createElement('div');
        mainCatalog.setAttribute('id', 'main-catalog');
        mainCatalog.classList.add('catalog');
        const mainContainer = document.getElementById('main-container');
        mainContainer.appendChild(mainCatalog);

        const wishlistIdArray = getLSProductIdArray(WISHLIST_KEY);

        data.forEach(product => {
            const widget = getProductWidget(product);

            let wishlistBtn = widget.querySelector(`[data-widget-element="button-wishlist"]`);
            if(wishlistIdArray.includes(product.id))
                if(!wishlistBtn.classList.contains('active'))
                    wishlistBtn.classList.add('active');
            mainCatalog.appendChild(widget);
        })
    }
    catch (e) {
        console.error(e);
    }
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
                    <button class="toggleWishlist-btn" data-widget-element="button-wishlist" onclick="toggleWishlistProduct(${productData.id})">
                        <svg class="wishlist_icon"
                             xmlns="http://www.w3.org/2000/svg"
                             viewBox="0 0 176 157"
                             width="40"
                             height="40">
                            <path d="M148.136,84.904 L94.518,138.471 C92.748,140.239 90.562,141.338 88.275,141.771 C84.480,142.518 80.394,141.425 77.453,138.487 L23.835,84.920 C6.685,67.786 6.685,40.006 23.835,22.872 C40.986,5.738 68.792,5.738 85.942,22.872 L85.977,22.907 L86.029,22.855 C103.179,5.721 130.985,5.721 148.136,22.855 C165.286,39.989 165.286,67.769 148.136,84.904 Z"/>
                        </svg>
                    </button>
                </div>
                <div class="block-micro buycheck">
                    <!-- TODO: Добавить обработчик -->
                    <input type="checkbox" onchange="" name="addToBuyCheckbox"/>
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

//=================================================================================
