document.addEventListener('DOMContentLoaded', () => {


    // validacion de secion
    const authHeaderValue = sessionStorage.getItem('userAuth');

    if (!authHeaderValue) {
        window.location.href = 'login.html';
        return;
    }


    // LOGOUT reutilizado
    
    const authBtn = document.getElementById('authBtn');

    if (authBtn) {
        authBtn.addEventListener('click', (e) => {
            e.preventDefault();
            
            // Destruimos las credenciales almacenadas
            sessionStorage.clear(); 
            
            // Redirigimos manualmente al login
            window.location.href = 'login.html';
        });
    }

 
    //FORM CREAR USUARIO
  
    const formCreateUser = document.getElementById('formCreateUser');

    formCreateUser.addEventListener('submit', async (e) => {
        e.preventDefault();

        const data = {
            username: document.getElementById('new-username').value,
            email: document.getElementById('new-email').value,
            password: document.getElementById('new-password').value,
            role: document.getElementById('new-role').value
        };

        try {

            const response = await fetch('http://localhost:8080/api/users/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': authHeaderValue
                },
                body: JSON.stringify(data)
            });

            const message = await response.text();

            if (response.ok) {

                alert('Usuario creado correctamente');

                formCreateUser.reset();

            } else {

                alert(message);
            }

        } catch (error) {

            console.error(error);

            alert('Error de conexión con el servidor');
        }
    });


    //FORM ELIMINAR USUARIO

    const formDeleteUser = document.getElementById('formDeleteUser');

    formDeleteUser.addEventListener('submit', async (e) => {

        e.preventDefault();

        const email = document.getElementById('delete-email').value;

        const confirmDelete = confirm(`¿Eliminar usuario ${email}?`);

        if (!confirmDelete) {
            return;
        }

        try {

            const response = await fetch(`http://localhost:8080/api/users/email/${email}`, {
                method: 'DELETE',
                headers: {
                    'Authorization': authHeaderValue
                }
            });

            const message = await response.text();

            if (response.ok) {

                alert(message);

                formDeleteUser.reset();

            } else {

                alert(message);
            }

        } catch (error) {

            console.error(error);

            alert('Error de conexión con el servidor');
        }
    });


    // 5. FORM CAMBIAR PASSWORD
    const formChangePass = document.getElementById('formChangePass');

    formChangePass.addEventListener('submit', async (e) => {

        e.preventDefault();

        const data = {
            email: document.getElementById('target-email').value,
            newPassword: document.getElementById('new-password-change').value
        };

        try {

            const response = await fetch('http://localhost:8080/api/users/password', {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': authHeaderValue
                },
                body: JSON.stringify(data)
            });

            const message = await response.text();

            if (response.ok) {

                alert('Contraseña actualizada correctamente');

                formChangePass.reset();

            } else {

                alert(message);
            }

        } catch (error) {

            console.error(error);

            alert('Error de conexión con el servidor');
        }
    });

});


// secciones

function showAdminSection(section) {

    document.getElementById('section-create').classList.add('d-none');
    document.getElementById('section-delete').classList.add('d-none');
    document.getElementById('section-password').classList.add('d-none');

    document.getElementById('section-' + section).classList.remove('d-none');
}