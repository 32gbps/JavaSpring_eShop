
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
async function getProductList(page, size) {
    if (page == null || Number.isNaN(page))
        page = current_page;
    if (size == null || Number.isNaN(size))
        size = current_size;

    let url = `/api/product/list?page=${page}&size=${size}`;

    // Убираем return и await вместе с then
    const response = await fetch(url);
    const js = await response.json();


    if (js.status !== 'success') {
        console.log('getProductList');
        console.log(js);
        return [];
    }

    return js.data;
}
function addProduct() {
    //Проверяем активность "формы"
    const body = document.getElementById('product-constructor');
    if(body == null)
        return;
    // Собираем данные из формы
    const name = document.getElementById('product-input-name').value;
    const description = document.getElementById('product-input-description').value;
    const price = document.getElementById('product-input-price').value;

    const attributesCatalog = document.querySelector(`[data-product-constructor="attributes-catalog"]`);
    const attributesArray = document.querySelectorAll(`[data-product-attributes="attribute-container"]`);

    //TODO: Добавить получение ID продавца для привязки товара к продавцу на стороне сервера
    const venId = -1;

    const productData = {
        name: name,
        description: description,
        price: price,
        vendorId: venId,
        attributes: []
    };
    //TODO: Добавить проверки: инициализация, корректность и т.д.
    for(let div of attributesArray)
    {
        let key = div.querySelector(`[data-product-attribute="key"]`).value;
        let value = div.querySelector(`[data-product-attribute="value"]`).value;
        productData.attributes[key]=value;
    }

    console.log(productData);

    fetch('/api/product/addProduct', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(productData)
    })
        .then(response => response.json())
        .then(json => {
            let text = `Status code: ${json.status}\n
                                Message: ${json.message}`
            printMessage(text);
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
    let widget = document.querySelector(`[data-widget-id="${productId}"]`);
    if(widget == null)
        widget = document.getElementById('Product-ShortParameters-container');

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
        case 2:
        {
            productData = await getProductArrayById(getLSProductIdArray(CART_KEY));
            initCatalog(productData);
            initSideContainer(productData);
            return;
        }
        default: return;
    }
    console.log('initPage')
    console.log('productData')
    console.log(productData);
    initCatalog(productData);
}
function initSideContainer(data) {
    const sideContainer = document.getElementById('side-container');
    if(sideContainer == null)
        return;

    let count = 0;
    let totalAmount = 0;
    data.forEach(product=>{
        ++count;
        totalAmount += product.price;
    })

    const form = getCartForm(count, totalAmount);
    sideContainer.appendChild(form);
}
function initCatalog(data) {
    try {
        if(data == null)
            return;

        const mainCatalog = document.createElement('div');
        mainCatalog.setAttribute('id', 'main-catalog');
        mainCatalog.classList.add('catalog');
        const mainContainer = document.getElementById('main-container');
        mainContainer.appendChild(mainCatalog);

        const wishlistIdArray = getLSProductIdArray(WISHLIST_KEY);
        console.log('initCatalog');
        console.log(data);
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
function getCartForm(productCount, totalAmount) {
    const div = document.createElement('div');
    div.id = "order-form";
    div.innerHTML =
        `<div id="order-info-block">
            <span id="order-info-label">Итого:</span>
            <div id="order-info-total-data">
                <div id="order-info-count">
                    <span>Товаров: ${productCount}</span>
                </div>
                <div id="order-info-totalAmount">
                    <span>${totalAmount} Р</span>
                </div>
            </div>
        </div>
        <div id="order-action-block">
            <button id="order-form-doOrder-button" onClick="doOrder()">Оформить заказ</button>
        </div>`

    return div;
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
function getUserId() {
    const cookieString = document.cookie;

    if (!cookieString || cookieString.length === 0) {
        console.error('Данные не найдены');
        return -1;
    }

    // Разбиваем строку на отдельные куки
    const cookieArrayString = cookieString.split(';');

    // Ищем куку, которая содержит 'personId'
    const cookieItem = cookieArrayString.find(s => s.trim().startsWith('personId='));

    if (!cookieItem) {
        console.error('personId не найден в куках');
        return -1;
    }

    // Извлекаем значение после знака '='
    const value = cookieItem.split('=')[1];

    if (!value) {
        console.error('Значение personId пустое');
        return -1;
    }

    const id = parseInt(value, 10);
    return id > 0 ? id : -1;
}
function doOrder() {
    const cart = getLSProductIdArray(CART_KEY);

    let requestBody =
    {
        personId: `${getUserId()}`,
        items: []
    };
    cart.forEach(id=>{
        requestBody.items.push({productId: id, quantity: 1})
    })
    let url = `/api/orders/createOrder`;
    const request = new Request(url, {
        method: "POST",
        headers: {
            'Content-Type': 'application/json'  // Важно!
        },
        body:JSON.stringify(requestBody)
    });
    return fetch(request)
        .then(response =>{
            if(response.ok)
                return response.json();
            else
                alert(`Error code: ${response.status}`);
            return null;
        })
        .then(js=>{
            if(js.status === 'success')
            {
                console.log(js.body);
                printMessage(`Заказ оформлен! ID: ${js.data.id}`)
                clearProductCart();
                document.getElementById('main-container').innerHTML = ``;
            }
            else
                console.error(js.message);
        });
}
function clearProductCart() {
    localStorage.removeItem(CART_KEY);
    const cartString = localStorage.getItem(CART_KEY);
    if(cartString == null || cartString.length === 0)
        console.log('Корзина очищена!');
}
//=================================================================================
function showPersonInfo() {

}
function showOrders() {
    let url = `/api/orders/person/${getUserId()}`;
    const request = new Request(url, {
        method: "GET"
    });
    return fetch(request)
        .then(response =>{
            if(response.ok)
                return response.json();
            else
                alert(`Error code: ${response.status}`);
            return null;
        })
        .then(js=>{
            if(js.status === 'success')
            {
                const main = document.getElementById('main-container');
                main.innerHTML = ``;
                console.log("raw data");
                console.log(js.data);

                js.data.forEach(order=>{
                    const widget = getOrderWidget(order.id, order.status, order.orderDate, order.totalAmount);
                    let itemsList = widget.querySelector(`[data-widget="items"]`);
                    itemsList.style.display='none';
                    let btnSwitchVisibility = widget.querySelector(`[data-widget-order="switchVisibility"]`);
                    btnSwitchVisibility.addEventListener('click', ()=>{
                        let state = itemsList.style.display;
                        if(state === 'none'){
                            itemsList.style.display = 'grid';
                            btnSwitchVisibility.innerText = 'close';
                        }
                        else{
                            itemsList.style.display = 'none';
                            btnSwitchVisibility.innerText = 'open';
                        }
                    });

                    for(let item of order.items){
                        const itemDiv = getOrderItemWidget(item.productName, item.quantity, item.subtotal);
                        itemsList.appendChild(itemDiv);
                    }
                    main.appendChild(widget);
                })
            }
            else
                console.error(js.message);
        });
}
// ================================== Order ===============================================
function getOrderWidget(orderId, status, date, amount) {
    const div = document.createElement('div');
    div.classList.add('order-widget');
    div.setAttribute('data-widget-order-id', orderId);
    div.innerHTML = `<div class="p6 flex" data-widget="data-top">
                            <span data-widget="order-status">${status}</span>
                            <span class="alignEnd" data-widget="order-date">${date}</span>
                        </div>
                        <div class="p6 alignEnd flex">
                            <button data-widget-order="switchVisibility">open</button>
                        </div>
                        <div class="p4 itemsList" data-widget="items">
                        </div>
                        <div class="p6" data-widget="data-bottom">
                            <span class="alignEnd" data-widget="order-totalAmount">${amount} ₽</span>
                        </div>`;
    return div;
}
function getOrderItemWidget(name, quantity, cost) {
    const div = document.createElement('div');
    div.classList.add('order-item');
    div.classList.add('p3');
    div.setAttribute('data-widget', 'item');
    div.innerHTML = `<div class="data-top" data-widget-item="data-top">
                        <span data-order-item="product-name">${name}</span>
                    </div>
                    <div class="flex sb" data-widget-item="data-bottom">
                        <span data-order-item="product-quantity">${quantity}</span>
                        <span data-order-item="product-cost">${cost} ₽</span>
                    </div>`;
    return div;
}
// ==================================== Product =================================================
function showProductConstructor(){
    const catalog = document.getElementById('main-container');
    catalog.innerHTML = ``;
    catalog.appendChild(getConstructorDiv());
}
function getConstructorDiv(){
    const div = document.createElement('div');
    div.classList.add('productConstructor');
    div.setAttribute('id', 'product-constructor');

    div.innerHTML = `<div class="alignCenter p6">
                    <button onclick="addProduct()">Добавить товар</button>
                    <button onclick="productPreview()">Предпросмотр</button>
                </div>
                <div id="preview-body">
                </div>
                <div id="constructor-body" class="">
                    <div class="alignCenter">
                        <h4>Основные характеристики</h4>
                        <div class="flex alignCenter">
                            <div class="p6 cols alignCenter">
                                <label for="input-product-name">Название товара</label>
                                <input id="product-input-name" name="input-product-name" type="text" placeholder="product name" size="100">
                            </div>
                            <div class="p6 cols alignCenter">
                                <label for="input-product-price">Цена товара</label>
                                <input id="product-input-price" name="input-product-price" type="number" min=0>
                            </div>
                        </div>
                        <div class="p6 cols alignCenter">
                            <label for="product-input-description">Описание товара</label>
                            <textarea id="product-input-description" minlength="0" maxlength="512" cols="100" rows="10"></textarea>
                        </div>
                    </div>
                    <div class="alignCenter" data-product-constructor="attributes-catalog-container">
                        <h4>Атрибуты</h3>
                        <div class="alignCenter" data-product-constructor="attributes-catalog">
                            <div class="attributeContainer alignCenter" data-product-attributes="attribute-container">
                                <div class="p3">
                                    <label for="attributeKey">Свойство (название)</label>
                                    <input name="attributeKey" data-product-attribute="key" type="text" minlength="3" maxlength="32" size="32">
                                </div>
                                <span class="alignCenter p3">:</span>
                                <div class="p3" data-product-attributes="attribute">
                                    <label for="attributeValue">Значение (свойства)</label>
                                    <input name="attributeValue" data-product-attribute="value" type="text" minlength="3" maxlength="64" size="64">
                                </div>
                            </div>
                        </div>
                        <hr>
                        <div class="attributeContainer alignCenter" data-product-constructor="action-container">
                            <button onclick="addAtribute()">Добавить атрибут</button>
                        </div>
                    </div>
                </div>`;
    return div;
}
function addAtribute(){
    const catalog = document.querySelector(`[data-product-constructor="attributes-catalog"]`);
    catalog.appendChild(getAttributeDiv());
}
function getAttributeDiv(){
    const div = document.createElement('div');
    div.classList.add('attributeContainer');
    div.classList.add('alignCenter');
    div.setAttribute('data-product-attributes', 'attribute-container');
    div.innerHTML = `<div class="p3">
                        <label for="attributeKey">Свойство (название)</label>
                        <input name="attributeKey" data-product-attribute="key" type="text" minlength="3" maxlength="32" size="32">
                    </div>
                    <span class="alignCenter p3">:</span>
                    <div class="p3" data-product-attributes="attribute">
                        <label for="attributeValue">Значение (свойства)</label>
                        <input name="attributeValue" data-product-attribute="value" type="text" minlength="3" maxlength="64" size="64">
                    </div>`;
    return div;
}