document.addEventListener('DOMContentLoaded', function () {

    const modal = document.getElementById('confirmModal');

    modal.addEventListener('show.bs.modal', function (event) {

        const button = event.relatedTarget;

        const id = button.getAttribute('data-id');
        const nombre = button.getAttribute('data-nombre');

        document.getElementById('modalId').value = id;
        document.getElementById('modalNombre').textContent = nombre;

    });

});