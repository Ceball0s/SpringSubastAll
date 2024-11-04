document.querySelector('.login-form').addEventListener('submit', function(event) {
    event.preventDefault();
    alert('¡Bienvenido a SubastALL!');
});
document.querySelector('.login-form').addEventListener('submit', function(event) {
    event.preventDefault();
    alert('¡Bienvenido a SubastALL!');
    window.location.href = '/'; // Redirige a la pantalla principal
});

// Seleccionar el botón por su clase
const createAccountButton = document.querySelector('.create-account');

// Agregar un evento de clic
createAccountButton.addEventListener('click', function() {
    // Redirigir a la página de registro
    window.location.href = '/signup'; // Asegúrate de que esta sea la URL correcta
});

