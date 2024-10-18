$(document).ready(async function() {
    let userToken = await getTokenRequest(); // Getting user token with async function
    await $.loadUsers(userToken);

    // ===== CRUD Functions =====
    $(document).on('click', '#submitBtn',(function(){ // Submit form data

        if($.validateUserForm()){ // Validating form
            const userData = $.getFormData('employee-form'); // Obtaining data of the form to crate the body of the request

            if(userData.id == null){ // If the id is undefined...
                $.createUser(userData, userToken); // Call the function to CREATE a client
            }else{ // If the id exists...
                const id = userData.id; // Save the id to use it before delete
                delete userData.id; // Delete the id from the object to send a clean object to the AJAX function
                $.editUser(id, userData, userToken); // Call the function to EDIT a client
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
        $.loadUserById($(this).val(), userToken);
        $('input#id').val($(this).val()) // Setting the id to the hidden input
        $("#cancelSubmit").removeClass('hidden'); // Showing the cancel button
    }));

    $(document).on('click', '#cancelSubmit',(function(){ // Clearing data of the form if cancel the edit
        $.resetUserForm() // Resetting form
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
        $.deleteUser($(this).val(), userToken); // Call the function to DELETE a client

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
        return {
            id: formData.id,
            name: fullName,
            dui: formData.dui,
            birthday: formData.birthday,
            gender: formData.gender,
            username: formData.username,
            email: formData.email,
            password: formData.password,
            confirmPassword: formData.confirmPassword,
            roleId: formData.roleId
        };
    },

    validateUserForm: function (){ // Function to validate form
        $("#employee-form").validate({
            errorElement: 'div',
            rules:{
                firstName: { required: true },
                secondName: { required: true },
                middleName: { required: true },
                lastName: { required: true },
                dui: { required: true, minlength: 9, maxlength: 9 },
                birthday: { required: true},
                // phone: { required: true, range: [ 10000000, 99999999 ] },
                gender: {required: true },
                username: { required: true },
                email: { required: true, email: true },
                password: { required: true, minlength: 6 },
                confirmPassword: { required: true, equalTo: "#password" },
                roleId: { required: true, range: [2, 3] }
            },
            messages:{
                // phone: { digits: "Only digits accepted.", range: "Enter a valid phone number.", number: "Invalid data." },
                dui: { minlength: "It must contains 9 characters", maxlength: "It must contains 9 characters"},
                confirmPassword: { required: "Please confirm your password", equalTo: "Both passwords must match" }
            }
        });

        return $("#employee-form").valid();
    },

    resetUserForm: function (){
        $('#employee-form')[0].reset(); // Clearing all the inputs

        $('.form-input input').each(function () { // For each input this function is applied to delete the class 'has-value'
            if ($(this).val().length === 0) {
                $(this).parent().removeClass('has-value');
            }
        });
    },

    // ============ API METHODS ============
    loadUsers: function(userToken) { // GET Method to list all the employees
        $.ajax({
            url: 'http://localhost:8081/api/users',  // Server URL
            method: 'GET', // Method
            headers: { // Header with Token and set content-type Json
                'Authorization': 'Bearer ' + userToken,
                'Content-Type': 'application/json'
            },success: function (users) {
                console.log(users);
                const tbody = $('#employeesTable tbody');
                tbody.empty();

                let i = 1;

                users.forEach(user => { // Process all the clients
                    const newRow = `
                        <tr class="border-b-2">
                            <td class="p-2 py-3">${i++}</td>
                            <td class="p-2 py-3">${user.name}</td>
                            <td class="p-2 py-3 hidden xl:table-cell">${user.email}</td>
                            <td class="p-2 py-3 hidden md:table-cell">${user.username}</td>
                            <td class="p-2 py-3">${user.role.name}</td>
                            <td class="p-2 py-3 flex flex-col md:flex-row justify-center items-center">
                                <button type="button" class="editBtn bg-amber-300 hover:bg-amber-400  px-2 py-1 rounded text-black w-20 mb-2 md:mb-0 md:mr-2" value="${user.id}">Edit</button>
                                <button type="button" class="deleteBtn bg-red-500 hover:bg-red-600  text-white px-2 py-1 rounded w-20 mt-2 md:mt-0 md:ml-2" value="${user.id}">Delete</button>
                            </td>
                        </tr>
                    `;

                    tbody.append(newRow); // Adding the new row of the table
                });
            }
        });
    },

    loadUserById: function(id, userToken) { // GET Method to get details of a client to edit
        $.ajax({
            url: 'http://localhost:8081/api/users/'+ id,  // Server URL with the param ID
            method: 'GET', // Method
            headers: { // Header with Token and set content-type Json
                'Authorization': 'Bearer ' + userToken,
                'Content-Type': 'application/json'
            },success: function (userData) { // Setting the data of the client in the form
                const fullName = userData.name;
                const nameParts = fullName.split(' '); // Split the name into parts

                // Assign the values to the corresponding inputs
                $('#id').val(userData.id);
                $('#firstName').val(nameParts[0] || '');
                $('#secondName').val(nameParts[1] || '');
                $('#middleName').val(nameParts[2] || '');
                $('#lastName').val(nameParts[3] || '');
                $('#dui').val(userData.dui);
                $('#birthday').val(userData.birthday);
                $('#gender').val(userData.gender);

                $('#username').val(userData.username);
                $('#email').val(userData.email);
                $('#password').val('');
                $('#confirmPassword').val('');
                $('#roleId').val(userData.role_id);

                $('.form-input input').each(function () { // For each input this function is applied to add the class 'has-value'
                    if ($(this).val().length > 0) {
                        $(this).parent().addClass('has-value');
                    }
                });
            }
        });
    },

    createUser: function(userData, userToken){
        $.ajax({
            url: 'http://localhost:8081/api/users',  // Server URL
            type: 'POST', // Method
            data: JSON.stringify(userData), // Convert the object to JSON
            headers: { // Header with Token and set content-type JSON
                'Authorization': 'Bearer ' + userToken,
                'Content-Type': 'application/json'
            }, success: function () {
                $.resetUserForm() // Resetting form
                $.loadUsers(userToken); // Reloading list of clients

                $.toast({ // Toast indicating successful registration to the client
                    text: "Users saved successfully",
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

    editUser: function(id, userData, userToken){
        $.ajax({
            url: 'http://localhost:8081/api/users/'+ id,  // Server URL
            type: 'PUT', // Method
            data: JSON.stringify(userData), // Convert the object to JSON
            headers: { // Header with Token and set content-type JSON
                'Authorization': 'Bearer ' + userToken,
                'Content-Type': 'application/json'
            }, success: function () {
                $.resetUserForm() // Resetting form
                $.loadUsers(userToken); // Reloading list of clients
                $("#cancelSubmit").addClass('hidden'); // Hiding the cancel button

                $.toast({ // Toast indicating successful edit of the client
                    text: "User saved successfully",
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

    deleteUser: function(id, userToken){
        $.ajax({
            url: 'http://localhost:8081/api/users/'+ id,  // Server URL
            type: 'DELETE', // Method
            headers: { // Header with Token and set content-type JSON
                'Authorization': 'Bearer ' + userToken,
                'Content-Type': 'application/json'
            }, success: function () {
                $.loadUsers(userToken); // Reloading list of clients
                $('#confirmDeleteBtn').val(''); // Clearing the id of the button

                $.toast({ // Toast indicating successful deleted to the client
                    text: "User deleted successfully",
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
                    text: "User currently in use on assigned projects, cannot be removed",
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