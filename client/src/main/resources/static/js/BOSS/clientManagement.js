$(document).ready(async function() {
    let userToken = await getTokenRequest(); // Getting user token with async function
    await $.loadClients(userToken);

    // ===== CRUD Functions =====
    $(document).on('click', '#submitBtn',(function(){ // Submit form data
        if($.validateClientForm()){ // Validating form
            const userData = $.getFormData('client-form'); // Obtaining data of the form to crate the body of the request

            if(userData.id === ''){ // If the id is undefined...
                delete userData.id; // Delete the id from the object to send a clean object to the AJAX function
                $.createClient(userData, userToken); // Call the function to CREATE a client
            }else{ // If the id exists...
                const id = userData.id; // Save the id to use it before delete
                delete userData.id; // Delete the id from the object to send a clean object to the AJAX function
                $.editClient(id, userData, userToken); // Call the function to EDIT a client
            }
        }else{
            $.toast({ // Toast indicating an error of validation
                text: "A validation error has occurred. Please try again",
                heading: 'Oops! An error has occurred',
                icon: 'error',
                showHideTransition: 'fade',
                allowToastClose: true,
                hideAfter: 3000,
                stack: false,
                loader: false,
                position: 'top-right',
            });
        }
    }));

    $(document).on('click', '.editBtn',(function(){ // Getting the data of a specified client and putting it in the form to edit
        $.loadClientById($(this).val(), userToken);
        $('input#id').val($(this).val()) // Setting the id to the hidden input
        $("#cancelSubmit").removeClass('hidden'); // Showing the cancel button
    }));

    $(document).on('click', '#cancelSubmit',(function(){ // Clearing data of the form if cancel the edit
        $.resetClientForm() // Resetting form
        $("#cancelSubmit").addClass('hidden'); // Hiding the cancel button again
    }));

    // ===== MODAL Functions for DELETE action =====
    $(document).on('click', '.deleteBtn',(function(){ // Open the modal
        $('#modalOverlay').removeClass('hidden'); // Show the overlay and content with animation
        $('#modalContent').css('opacity', '0').removeClass('scale-90')
            .animate({ opacity: 1, scale: 1 }, { duration: 300,
                step: function (now, fx) { if (fx.prop === 'scale') { $(this).css('transform', `scale(${now})`); }
                },
            });

        $('#confirmDeleteBtn').val($(this).val()); // Setting the id of the client to delete
    }));

    $('#cancelBtn, #modalOverlay').on('click', function (e) { // Close the modal cancel
        if (e.target.id === 'modalOverlay' || e.target.id === 'cancelBtn') {
            $('#modalContent').animate(
                { opacity: 0, scale: 0.9 },
                { duration: 300, step: function (now, fx) { if (fx.prop === 'scale') { $(this).css('transform', `scale(${now})`); } },
                    complete: function () { $('#modalOverlay').addClass('hidden'); }, }
            );
        }
        $('#confirmDeleteBtn').val(''); // Clearing the id of the button
    });

    $('#confirmDeleteBtn').on('click', function () {  // Confirming the delete
        $.deleteClient($(this).val(), userToken); // Call the function to DELETE a client

        $('#modalContent').animate(
            { opacity: 0, scale: 0.9 },
            {   duration: 300,
                step: function (now, fx) { if (fx.prop === 'scale') { $(this).css('transform', `scale(${now})`); } },
                complete: function () { $('#modalOverlay').addClass('hidden'); },
            }
        );
    });
});

$.extend({ // Creating a repository of utils functions to use in this file

    // ============ FORM UTILS METHODS ============
    getFormData: function(formId) { // Function to create the object to be sent in the request
        const formData = $(`#${formId}`).serializeArray().reduce((acc, { name, value }) => {
            acc[name] = value; // Assigns each field to an object
            return acc;
        }, {});

        // Concatenate the names in a single field 'name'
        const fullName = [formData.firstName, formData.secondName,
                                formData.middleName, formData.lastName].filter(Boolean).join(' ').trim();

        // Reorganize the object
        return { id: formData.id, name: fullName, email: formData.email, address: formData.address, phone: formData.phone };
    },

    validateClientForm: function (){ // Function to validate form
        $("#client-form").validate({
            errorElement: 'div',
            rules:{
                firstName: { required: true },
                secondName: { required: true },
                middleName: { required: true },
                lastName: { required: true },
                email: { required: true, email: true },
                address: { required: true },
                phone: { required: true, range: [ 10000000, 99999999 ] },
            },
            messages:{
                phone: { digits: "Only digits accepted.", range: "Enter a valid phone number.", number: "Invalid data." },
            }
        });

        return $("#client-form").valid();
    },

    resetClientForm: function (){
        $('#client-form')[0].reset(); // Clearing all the inputs
        $('input#id').val(''); // Clearing the id in the hidden input
        $('#client-form').validate().destroy(); // Clearing the validate process

        $('.form-input input').each(function () { // For each input this function is applied to delete the class 'has-value'
            if ($(this).val().length === 0) {
                $(this).parent().removeClass('has-value');
            }
        });
    },

    // ============ API METHODS ============
    loadClients: function(userToken) { // GET Method to list all the clients
        $.ajax({
            url: 'http://localhost:8081/api/clients',  // Server URL
            method: 'GET', // Method
            headers: { // Header with Token and set content-type Json
                'Authorization': 'Bearer ' + userToken,
                'Content-Type': 'application/json'
            },success: function (clients) {
                const tbody = $('#clientsTable tbody');
                tbody.empty();

                let i = 1;

                clients.forEach(client => { // Process all the clients
                    const newRow = `
                        <tr class="border-b-2">
                            <td class="p-2 py-3">${i++}</td>
                            <td class="p-2 py-3">${client.name}</td>
                            <td class="p-2 py-3 hidden xl:table-cell">${client.email}</td>
                            <td class="p-2 py-3 hidden md:table-cell">${client.address}</td>
                            <td class="p-2 py-3">${client.phone}</td>
                            <td class="p-2 py-3 flex flex-col md:flex-row justify-center items-center">
                                <button type="button" class="editBtn bg-amber-300 hover:bg-amber-400  px-2 py-1 rounded text-black w-20 mb-2 md:mb-0 md:mr-2" value="${client.id}">Edit</button>
                                <button type="button" class="deleteBtn bg-red-500 hover:bg-red-600  text-white px-2 py-1 rounded w-20 mt-2 md:mt-0 md:ml-2" value="${client.id}">Delete</button>
                            </td>
                        </tr>
                    `;

                    tbody.append(newRow); // Adding the new row of the table
                });
            }
        });
    },

    loadClientById: function(id, userToken) { // GET Method to get details of a client to edit
        $.ajax({
            url: 'http://localhost:8081/api/clients/'+ id,  // Server URL with the param ID
            method: 'GET', // Method
            headers: { // Header with Token and set content-type Json
                'Authorization': 'Bearer ' + userToken,
                'Content-Type': 'application/json'
            },success: function (clientData) { // Setting the data of the client in the form
                const fullName = clientData.name;
                const nameParts = fullName.split(' '); // Split the name into parts

                // Assign the values to the corresponding inputs
                $('#id').val(clientData.id);
                $('#firstName').val(nameParts[0] || '');
                $('#secondName').val(nameParts[1] || '');
                $('#middleName').val(nameParts[2] || '');
                $('#lastName').val(nameParts[3] || '');
                $('#email').val(clientData.email);
                $('#address').val(clientData.address);
                $('#phone').val(clientData.phone);

                $('.form-input input').each(function () { // For each input this function is applied to add the class 'has-value'
                    if ($(this).val().length > 0) {
                        $(this).parent().addClass('has-value');
                    }
                });
            }
        });
    },

    createClient: function(userData, userToken){
        $.ajax({
            url: 'http://localhost:8081/api/clients',  // Server URL
            type: 'POST', // Method
            data: JSON.stringify(userData), // Convert the object to JSON
            headers: { // Header with Token and set content-type JSON
                'Authorization': 'Bearer ' + userToken,
                'Content-Type': 'application/json'
            }, success: function () {
                $.resetClientForm() // Resetting form
                $.loadClients(userToken); // Reloading list of clients

                $.toast({ // Toast indicating successful registration to the client
                    text: "Client saved successfully",
                    heading: 'Success!',
                    icon: 'success',
                    showHideTransition: 'fade',
                    allowToastClose: true,
                    hideAfter: 3000,
                    stack: false,
                    loader: false,
                    position: 'top-right',
                });
            }
        });
    },

    editClient: function(id, userData, userToken){
        $.ajax({
            url: 'http://localhost:8081/api/clients/'+ id,  // Server URL
            type: 'PUT', // Method
            data: JSON.stringify(userData), // Convert the object to JSON
            headers: { // Header with Token and set content-type JSON
                'Authorization': 'Bearer ' + userToken,
                'Content-Type': 'application/json'
            }, success: function () {
                $.resetClientForm() // Resetting form
                $.loadClients(userToken); // Reloading list of clients
                $("#cancelSubmit").addClass('hidden'); // Hiding the cancel button

                $.toast({ // Toast indicating successful edit of the client
                    text: "Client saved successfully",
                    heading: 'Success!',
                    icon: 'success',
                    showHideTransition: 'fade',
                    allowToastClose: true,
                    hideAfter: 3000,
                    stack: false,
                    loader: false,
                    position: 'top-right',
                });
            }
        });
    },

    deleteClient: function(id, userToken){
        $.ajax({
            url: 'http://localhost:8081/api/clients/'+ id,  // Server URL
            type: 'DELETE', // Method
            headers: { // Header with Token and set content-type JSON
                'Authorization': 'Bearer ' + userToken,
                'Content-Type': 'application/json'
            }, success: function () {
                $.loadClients(userToken); // Reloading list of clients
                $('#confirmDeleteBtn').val(''); // Clearing the id of the button

                $.toast({ // Toast indicating successful deleted to the client
                    text: "Client deleted successfully",
                    heading: 'Success!',
                    icon: 'success',
                    showHideTransition: 'fade',
                    allowToastClose: true,
                    hideAfter: 3000,
                    stack: false,
                    loader: false,
                    position: 'top-right',
                });
            }, error: function (response){
                $.toast({ // Toast indicating warning of deleting to the client
                    text: "Client currently in use on assigned projects, cannot be removed",
                    heading: 'Warning!',
                    icon: 'warning',
                    showHideTransition: 'fade',
                    allowToastClose: true,
                    hideAfter: 5000,
                    stack: false,
                    loader: false,
                    position: 'top-right',
                });
            }
        });
    }
})
