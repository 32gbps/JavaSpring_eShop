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

function getClothesById() {
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

        let url = `/api/clothes/getClothesById?id=${id}`;

        fetch(url)
            .then(response => response.json())
            .then(json => {
                printMessage(json.message,json.status);
                showClothesProperties(json.data);
            });
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
function showComments(reviewId){
    let revDiv = document.getElementById(`review-id-${reviewId}`);
    //===
    //сначала добавить форму для отправки комментария
    //revDiv.append()
    //===
}
function getReviewComments(reviewId){

}