document.getElementById('loginForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    // Capturam de lo valores
    const usernameInput = document.getElementById('username').value;
    const passwordInput = document.getElementById('password').value;
    const loginAlert = document.getElementById('loginAlert');

    // Ocultamos la alerta de erro
    loginAlert.classList.add('d-none');

    // btoa transforma base 64 para srping security
    const credentialsBase64 = btoa(`${usernameInput}:${passwordInput}`);
    const authHeaderValue = `Basic ${credentialsBase64}`;

    try {
        // validar si las credenciales son correctas.
        const response = await fetch('http://localhost:8080/api/emails', {
            method: 'GET',
            headers: {
                'Authorization': authHeaderValue
            }
        });

        if (response.ok) {
            sessionStorage.setItem('userAuth', authHeaderValue);
            sessionStorage.setItem('username', usernameInput);

            // Redirigi a la pantalla del analizador
            window.location.href = 'analisador.html';
        } else {
            // El backend respondió pero no con un estado OK
            mostrarError("Incorrect email or password. Please try again.");
        }

    } catch (error) {
        // backend está apagado o hay un problema de red 
        console.error("Error de conexión:", error);
        mostrarError("Cannot connect to the server. Make sure your Spring Boot backend is running.");
    }
});

// mostrar la alerta de Bootstrap de manera dinámica
function mostrarError(mensaje) {
    const loginAlert = document.getElementById('loginAlert');
    const errorText = document.getElementById('errorText');
    errorText.innerText = mensaje;
    loginAlert.classList.remove('d-none');
}