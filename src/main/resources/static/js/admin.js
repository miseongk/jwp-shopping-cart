const modal = document.getElementById('modal');

const showAddModal = () => {
    modal.dataset.formType = 'add';
    modal.style.display = 'block';
};

const showEditModal = (product) => {
    const elements = modal.getElementsByTagName('input');
    for (const element of elements) {
        element.value = product[element.getAttribute('name')];
    }
    modal.dataset.formType = 'edit';
    modal.dataset.productId = product.id;
    modal.style.display = 'block';
};

const hideAddModal = () => {
    modal.style.display = 'none';
    const elements = modal.getElementsByTagName('input');
    for (const element of elements) {
        element.value = '';
    }
}

const form = document.getElementById('form');

form.addEventListener('submit', (event) => {
    event.preventDefault();

    const formData = new FormData(event.target);
    let product = {};
    for (const entry of formData.entries()) {
        const [key, value] = entry;
        product[key] = value;
    }

    if (modal.dataset.formType === 'edit') {
        product['id'] = modal.dataset.productId;
        updateProduct(product);
        return;
    }

    createProduct(product);
});

const createProduct = (product) => {
    axios.request({
        url: '/products',
        method: 'post',
        data: JSON.stringify(product),
        headers: {
            "Content-Type": "application/json; charset=utf-8"
        }
    }).then((response) => {
        window.location.replace(response.headers.location);
    }).catch((error) => {
        console.error(error);
    });
};

const updateProduct = (product) => {
    const id = product['id'];

    axios.request({
        method: 'put',
        url: '/products/' + id,
        data: JSON.stringify(product),
        headers: {
            "Content-Type": "application/json; charset=utf-8"
        }
    }).then((response) => {
        window.location.reload();
    }).catch((error) => {
        console.error(error);
    });
};

const deleteProduct = (id) => {
    axios.request({
        url: '/products/' + id,
        method: 'delete'
    }).then((response) => {
        window.location.reload();
    }).catch((error) => {
        console.error(error);
    });
};
