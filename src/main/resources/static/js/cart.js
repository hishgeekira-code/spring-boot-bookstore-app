document.querySelectorAll('form.confirm-delete').forEach(function (form) {
    form.addEventListener('submit', function (event) {
        if (!window.confirm('Are you sure?')) {
            event.preventDefault();
        }
    });
});

document.querySelectorAll('.cart-qty').forEach(function (input) {
    input.addEventListener('change', function () {
        this.closest('form').submit();
    });
});