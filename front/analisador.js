document.addEventListener('DOMContentLoaded', () => {

  const authHeaderValue = sessionStorage.getItem('userAuth');
    
    // Si no hay token guardado, expulsa de inmediato al usuario al login
    if (!authHeaderValue) {
        window.location.href = 'login.html';
        return; 
    }

    // verifica rol
    const adminBtn = document.getElementById('adminBtn');
    
    if (adminBtn) {
        // Ocultamos el botón de admin por defecto hasta validar
        adminBtn.classList.add('d-none');

        async function verificarRolAdmin() {
            try {
                // Petición silenciosa a la ruta protegida de usuarios
                const response = await fetch('http://localhost:8080/api/users', {
                    method: 'GET',
                    headers: {
                        'Authorization': authHeaderValue
                    }
                });

                if (response.ok) {
                    // Es ADMIN: mostramos el botón
                    adminBtn.classList.remove('d-none');
                } else if (response.status === 403) {
                    // Es USER: mantenemos el botón oculto
                    console.log("Acceso de usuario estándar: Panel de administración restringido.");
                }
            } catch (error) {
                // Error: se mantiene oculto
                console.error("Error al verificar los privilegios de administrador:", error);
            }
        }
        
        verificarRolAdmin();
    }

    // 3. esto escanea
    const formAnalyze = document.getElementById('formAnalyze');
    const riskBadge = document.getElementById('riskBadge');
    const threatLevelText = document.getElementById('threatLevelText');
    const riskProgressBar = document.getElementById('riskProgressBar');
    const riskScoreNum = document.getElementById('riskScoreNum');

    if (formAnalyze) {
        formAnalyze.addEventListener('submit', async (e) => {
            e.preventDefault(); 

            const emailInput = document.getElementById('senderEmail').value;

            // Modificamos la interfaz para mostrar el estado de "Cargando"
            riskBadge.className = "badge bg-secondary px-3 py-2 fs-6 mb-2";
            riskBadge.innerText = "Scanning...";
            threatLevelText.className = "fw-bold text-muted";
            threatLevelText.innerText = "Analyzing security patterns...";

            try {
                const response = await fetch('http://localhost:8080/api/emails', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': authHeaderValue
                    },
                    body: JSON.stringify({
                        email: emailInput
                    })
                });

                if (response.ok) {
                    const data = await response.json();
                    
                    // Obtenemos el valor exacto desde la entidad Java
                    let riesgoBase = data.nivelRiesgo || 0; 
                    
                    // Multiplicar por 10 y limitar a 100 para ser mas vistoso dando un porcentaje teorico, no real
                    let riesgoCalculado = riesgoBase * 10;
                    let finalScore = Math.min(riesgoCalculado, 100);

                    // los valores numéricos y la barra
                    riskScoreNum.textContent = finalScore;
                    riskProgressBar.style.width = finalScore + '%';
                    riskProgressBar.className = "progress-bar";

                    // estilos según tus rangos de severidad
                    if (finalScore <= 40) {
                        // RANGO SEGURO (0-40)
                        riskBadge.className = "badge bg-success px-3 py-2 fs-6 mb-2";
                        riskBadge.textContent = 'Safe';
                        threatLevelText.className = "fw-bold text-success";
                        threatLevelText.textContent = 'Low Probability of Phishing';
                        riskProgressBar.classList.add('bg-success');
                        
                    } else if (finalScore <= 60) {
                        // RANGO EN RIESGO (41-60)
                        riskBadge.className = "badge bg-warning text-dark px-3 py-2 fs-6 mb-2";
                        riskBadge.textContent = 'Risky';
                        threatLevelText.className = "fw-bold text-warning";
                        threatLevelText.textContent = 'Moderate Phishing Risk';
                        riskProgressBar.classList.add('bg-warning');
                        
                    } else {
                        // RANGO PELIGROSO (61-100)
                        riskBadge.className = "badge bg-danger px-3 py-2 fs-6 mb-2";
                        riskBadge.textContent = 'Dangerous';
                        threatLevelText.className = "fw-bold text-danger";
                        threatLevelText.textContent = 'High Probability of Phishing';
                        riskProgressBar.classList.add('bg-danger');
                    }

                } else {
                    riskBadge.className = "badge bg-secondary px-3 py-2 fs-6 mb-2";
                    riskBadge.innerText = "Error";
                    threatLevelText.className = "fw-bold text-danger";
                    threatLevelText.innerText = "The server rejected the analysis request. status: " + response.status;
                }

            } catch (error) {
                console.error("Error al conectar con la API de análisis:", error);
                riskBadge.className = "badge bg-secondary px-3 py-2 fs-6 mb-2";
                riskBadge.innerText = "Offline";
                threatLevelText.className = "fw-bold text-danger";
                threatLevelText.innerText = "Could not communicate with the scanner engine.";
            }
        });
    }


    //BOTÓN DE LOG OUT reutilizable siii
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
});