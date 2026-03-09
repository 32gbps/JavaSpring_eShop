document.addEventListener('DOMContentLoaded', function() {
    // Инициализация обработчиков событий
    document.getElementById('getById-form').addEventListener('submit', function(event) {
        event.preventDefault();
        getClothesById();
    });
    document.getElementById('deleteById-button').addEventListener('click', deleteClothesById);
    document.getElementById('editById-button').addEventListener('click', editClothes);
    document.getElementById('addClothes-button').addEventListener('click', addClothes);
});

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
            .then(data => showClothesProperties(data.data))
            .then(data => console.log(data))
            .catch(error => console.error('Ошибка:', error));
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
            .then(data => console.log(data))
            .catch(error => console.error('Ошибка:', error));
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
        .then(response => {
            if (!response.ok) {
                return response.json().then(err => { throw err; });
            }
            return response.json();
        })
        .then(apiResponse => {
            if (apiResponse.status === 'success') {
                alert(apiResponse.message);
                getClothesById(); // Обновляем таблицу
            } else if (apiResponse.status === 'fail') {
                alert('Ошибка: ' + apiResponse.message);
            }
        })
        .catch(error => {
            console.error('Ошибка:', error);
            alert('Произошла ошибка при сохранении');
        });
}
function addClothes() {
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

    fetch('/api/clothes/addClothes', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'  // Важно!
        },
        body: JSON.stringify(clothesData)  // Преобразуем в JSON
    })
        .then(response => {
            if (!response.ok) {
                return response.json().then(err => { throw err; });
            }
            return response.json();
        })
        .then(apiResponse => {
            if (apiResponse.status === 'success') {
                alert(apiResponse.message);

            } else if (apiResponse.status === 'fail') {
                alert('Ошибка: ' + apiResponse.message);
            }
        })
        .catch(error => {
            console.error('Ошибка:', error);
            alert('Произошла ошибка при сохранении');
        });
}
function showClothesProperties(clothesData){
    //var clothesData = JSON.parse(data);

    console.log('typeof data:', typeof clothesData);
    // Проверяем, что данные существуют и являются объектом
    if (!clothesData || typeof clothesData !== 'object') {
        console.error('Некорректные данные:', clothesData);
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