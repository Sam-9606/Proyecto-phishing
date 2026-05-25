document.addEventListener("DOMContentLoaded", async () => {
  // VALIDACIÓN DE SESIÓN

  const authHeaderValue = sessionStorage.getItem("userAuth");

  if (!authHeaderValue) {
    window.location.href = "login.html";
    return;
  }

  // logout de nuevo

  const authBtn = document.getElementById("authBtn");

  authBtn.addEventListener("click", (e) => {
    e.preventDefault();

    sessionStorage.clear();

    window.location.href = "login.html";
  });

  // validacion boton Admin y acciones de admin

  const adminBtn = document.getElementById("adminBtn");
  const actionsHeader = document.getElementById('actionsHeader');

let isAdmin = false;

  try {
    const adminResponse = await fetch("http://localhost:8080/api/users", {
      method: "GET",
      headers: {
        Authorization: authHeaderValue,
      },
    });

    if (adminResponse.ok) {

    isAdmin = true;

    adminBtn.classList.remove('d-none');

    actionsHeader.classList.remove('d-none');
}
  } catch (error) {
    console.error(error);
  }

  //scrip del historial

  const tableBody = document.getElementById("emailTableBody");

  try {
    const response = await fetch("http://localhost:8080/api/emails", {
      method: "GET",
      headers: {
        Authorization: authHeaderValue,
      },
    });

    if (!response.ok) {
      tableBody.innerHTML = `
                <tr>
                    <td colspan="5" class="text-center text-danger py-4">
                        Failed to load email history
                    </td>
                </tr>
            `;

      return;
    }

    const emails = await response.json();

    if (emails.length === 0) {
      tableBody.innerHTML = `
                <tr>
                    <td colspan="5" class="text-center text-muted py-4">
                        No analyzed emails found
                    </td>
                </tr>
            `;

      return;
    }

    tableBody.innerHTML = "";

    emails.forEach((emailData) => {
      const riesgoBase = emailData.nivelRiesgo || 0;

      const riesgoCalculado = Math.min(riesgoBase * 10, 100);

      let badgeClass = "bg-success-subtle text-success";
      let statusText = "Safe";

      if (riesgoCalculado > 60) {
        badgeClass = "bg-danger-subtle text-danger";
        statusText = "Dangerous";
      } else if (riesgoCalculado > 40) {
        badgeClass = "bg-warning-subtle text-warning";
        statusText = "Risky";
      }

      const row = `
    <tr>

        <td class="fw-semibold text-dark">
            ${emailData.email}
        </td>

        <td>
            <span class="badge ${badgeClass} px-2 py-1">
                ${statusText}
            </span>
        </td>

        <td>
            ${riesgoCalculado}%
        </td>

        <td class="text-muted">
            ${new Date(emailData.fecha).toLocaleString()}
        </td>

        <td>
            <span class="badge ${
                emailData.source === 'MANUAL'
                    ? 'bg-warning text-dark'
                    : 'bg-light text-dark border'
            }">
                    ${emailData.source}
                </span>
            </td>

        ${isAdmin ? `
    <td class="text-center">

        <button
            class="btn btn-sm btn-success"
            onclick="markSafe(${emailData.id})"
            title="Mark as Safe">

            <i class="bi bi-flag-fill"></i>
        </button>

        <button
            class="btn btn-sm btn-danger"
            onclick="markDanger(${emailData.id})"
            title="Mark as Dangerous">

            <i class="bi bi-flag-fill"></i>
        </button>

        <button
            class="btn btn-sm btn-danger"
            onclick="deleteAnalysis(${emailData.id})"
            title="Delete Analysis">

            <i class="bi bi-trash"></i>
        </button>

    </td>
` : ''}

    </tr>
`;

      tableBody.innerHTML += row;
    });
  } catch (error) {
    console.error(error);

    tableBody.innerHTML = `
            <tr>
                <td colspan="5" class="text-center text-danger py-4">
                    Server connection error
                </td>
            </tr>
        `;
  }
});

async function deleteAnalysis(id) {

    const authHeaderValue = sessionStorage.getItem('userAuth');

    const confirmDelete = confirm(
        'Are you sure you want to delete this analysis record?'
    );

    if (!confirmDelete) {
        return;
    }

    try {

        const response = await fetch(
            `http://localhost:8080/api/emails/${id}`,
            {
                method: 'DELETE',
                headers: {
                    'Authorization': authHeaderValue
                }
            }
        );

        if (response.ok) {

            alert('Analysis deleted successfully');

            location.reload();

        } else {

            alert('Failed to delete analysis');
        }

    } catch (error) {

        console.error(error);

        alert('Server connection error');
    }
}

async function markSafe(id) {

    const authHeaderValue = sessionStorage.getItem('userAuth');

    try {

        const response = await fetch(
            `http://localhost:8080/api/emails/${id}/safe`,
            {
                method: 'PUT',
                headers: {
                    'Authorization': authHeaderValue
                }
            }
        );

        if (response.ok) {

            alert('Email marked as safe');

            location.reload();

        } else {

            alert('Failed to mark as safe');
        }

    } catch (error) {

        console.error(error);

        alert('Server connection error');
    }
}

async function markDanger(id) {

    const authHeaderValue = sessionStorage.getItem('userAuth');

    try {

        const response = await fetch(
            `http://localhost:8080/api/emails/${id}/danger`,
            {
                method: 'PUT',
                headers: {
                    'Authorization': authHeaderValue
                }
            }
        );

        if (response.ok) {

            alert('Email marked as dangerous');

            location.reload();

        } else {

            alert('Failed to mark as dangerous');
        }

    } catch (error) {

        console.error(error);

        alert('Server connection error');
    }
}
