$(document).ready(function() { loadUsers(); });

function loadUsers() {
    $.get("client/get", function(clients) {
        const tbody = $('#clientsTable tbody');
        tbody.empty();

        let i = 1;

        clients.forEach(client => { // Procesar todos los usuarios
            const newRow = `
                <tr class="border-b-2">
                    <td class="p-2 py-3">${i++}</td>
                    <td class="p-2 py-3">${client.username}</td>
                    <td class="p-2 py-3">${client.email}</td>
                    <td class="p-2 py-3 flex flex-col md:flex-row justify-center items-center">
                        <a class="bg-yellow-400 px-2 py-1 rounded text-black w-20 mb-2 md:mb-0 md:mr-2">Editar</a>
                        <a class="bg-red-500 text-white px-2 py-1 rounded w-20 mt-2 md:mt-0 md:ml-2">Eliminar</a>
                    </td>
                </tr>
            `;

            tbody.append(newRow);
        });
    });
}
