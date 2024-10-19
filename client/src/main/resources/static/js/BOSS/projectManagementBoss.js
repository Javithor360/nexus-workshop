$(document).ready(async function() {
    let userToken = await getTokenRequest(); // Get the user's token

    await $.loadClients(userToken); // Load clients for the dropdown
    await $.loadEmployees(userToken); // Load employees for the dropdown
    await $.loadProjects(userToken); // Load existing projects

    $(document).on('click', '#submitProject',(function(){ // Submit form data
        if($.validateProjectForm()){
            const projectData = $.getFormData('create-project-form');

            $.createProject(projectData, userToken);
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

    $(document).on('click', '.filterSearch',(function(){ // Load projects by their id

        //-> If the clicked button has value for the category then load the      -> If the button has no value then call the
        //   list of projects filtered by their status                              the normal loadProjects function
         $(this).val() !== "" ? $.loadProjectsByCategory($(this).val(), userToken) : $.loadProjects(userToken);
    }));

});

$.validator.addMethod("greaterThan", function(value, element, param) { // Custom validator for due date
    return this.optional(element) || new Date(value) > new Date($(param).val());
}, "Due date must be after the start date");

$.extend({ // Creating a repository of utils functions to use in this file

    // ============ FORM UTILS METHODS ============
    validateProjectForm: function(){
        $("#create-project-form").validate({
            errorElement: 'div',
            rules: {
                projectName: {
                    required: true,
                    maxlength: 30 // Limit project name to 30 characters
                },
                description: {
                    required: true,
                    maxlength: 255 // Limit description to 255 characters
                },
                startDate: {
                    required: true,
                    date: true,
                    min: new Date().toISOString().split("T")[0] // Start date must be today or in the future
                },
                endDate: {
                    required: true,
                    date: true,
                    greaterThan: "#start-date" // End date must be after start date
                }
            },
            messages: {
                projectName: {
                    maxlength: "Project name can't exceed 30 characters."
                },
                description: {
                    maxlength: "Description can't exceed 255 characters."
                },
                startDate: {
                    date: "Please enter a valid date.",
                    min: "Start date must be today or later."
                },
                dueDate: {
                    date: "Please enter a valid date.",
                    greaterThan: "Due date must be after the start date."
                }
            }
        });

        return $("#create-project-form").valid();
    },

    getFormData: function(formId) { // Function to create the object to be sent in the request
        const formData = $(`#${formId}`).serializeArray().reduce((acc, { name, value }) => {
            acc[name] = value; // Assigns each field to an object
            return acc;
        }, {});

        // Reorganize the object
        return {
            client: { id: formData.clientId }, user: { id: formData.userId }, status: { id: '2' },
            title: formData.projectName, description: formData.projectDescription,
            startDate: formData.startDate, dueDate: formData.endDate, endDate: formData.endDate
        };
    },

    resetProjectForm: function(){
        $('#create-project-form')[0].reset();
        $('.form-input select').parent().removeClass('has-value');
        $('#create-project-form').validate().destroy();
        $('.form-input input, .form-input textarea').each(function () { // For each input this function is applied to delete the class 'has-value'
            if ($(this).val().length === 0) {
                $(this).parent().removeClass('has-value');
            }
        });
    },

    // ============ API METHODS ============
    loadClients: function(userToken){
        $.ajax({
            url: 'http://localhost:8081/api/clients',  // Server URL to get clients
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + userToken,
                'Content-Type': 'application/json'
            },
            success: function(clients) {
                const customerSelect = $('#customer'); // Select the client dropdown element
                customerSelect.empty(); // Clear previous options
                customerSelect.append('<option value="" selected disabled hidden></option>'); // Default option

                // Populate the select element with client names
                clients.forEach(client => {
                    customerSelect.append(`<option value="${client.id}" class="capitalize">${client.name}</option>`);
                });
            }
        });
    },

    loadEmployees: function(userToken){
        $.ajax({
            url: 'http://localhost:8081/api/users',  // Server URL to get clients
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + userToken,
                'Content-Type': 'application/json'
            },
            success: function(employees) {
                const employeeSelect = $('#employee'); // Select the client dropdown element
                employeeSelect.empty(); // Clear previous options
                employeeSelect.append('<option value="" selected disabled hidden></option>'); // Default option

                // Populate the select element with client names
                employees.forEach(employee => {
                    if(employee.role.id === 3){
                        employeeSelect.append(`<option value="${employee.id}" class="capitalize">${employee.username}</option>`);
                    }
                });
            }
        });
    },

    loadProjects: function(userToken){
        $.ajax({
            url: 'http://localhost:8081/api/projects',  // Server URL to get projects
            method: 'GET', // HTTP Method
            headers: {
                'Authorization': 'Bearer ' + userToken,
                'Content-Type': 'application/json'
            },
            success: function (projects) {
                const projectGrid = $('#scrollable-container'); // Project cards container
                projectGrid.empty(); // Clear previous projects

                // Initialize counters
                let totalProjects = 0, deliveredProjects = 0, inProgressProjects = 0;

                projects.forEach(project => { // Loop through each project and process it
                    // Increment total projects counter
                    totalProjects++;

                    // Set status color and update counters based on project status
                    let statusColor = '';
                    switch (project.status.name) {
                        case 'In Progress':
                            inProgressProjects++;
                            statusColor = 'bg-blue-600'; // In Progress
                            break;
                        case 'Completed':
                            deliveredProjects++;
                            statusColor = 'bg-green-500'; // Completed
                            break;
                        default:
                            statusColor = 'bg-gray-500'; // Default status
                            break;
                    }

                    // Create the project card
                    const projectCard = `
                        <div class="project-card bg-white p-5 rounded-lg border max-w-sm cursor-pointer" onclick="window.location.href='project/${project.id}'">
                            <div class="flex justify-between items-center">
                                <h3 class="text-xl font-bold text-gray-800">${project.title}</h3>
                                <span class="text-white text-sm font-medium px-2.5 py-0.5 rounded-full ${statusColor}">${project.status.name}</span>
                            </div>
                            <p class="text-sm text-gray-500 mt-2">Assigned to: ${project.user ? project.user.username : 'Not assigned'}</p>
                            <p class="text-sm text-gray-500">Client: ${project.client.name}</p>
                            <p class="text-sm text-gray-500">Due Date: ${project.endDate}</p>
                        </div>
                    `;
                    // Append the project card to the container
                    projectGrid.append(projectCard);
                });

                // Update counters on the interface
                $('#total-projects').text(totalProjects);
                $('#completed-projects').text(deliveredProjects);
                $('#in-progress-projects').text(inProgressProjects);
            }
        });
    },

    loadProjectsByCategory: function(projectCategory, userToken){
        $.ajax({
            url: 'http://localhost:8081/api/projects',  // Server URL to get projects
            method: 'GET', // HTTP Method
            headers: {
                'Authorization': 'Bearer ' + userToken,
                'Content-Type': 'application/json'
            },
            success: function (projects) {
                const projectGrid = $('#scrollable-container'); // Project cards container
                projectGrid.empty(); // Clear previous projects

                let hasProjects = false;
                projects.forEach((project) => { // Loop through each project and process it
                    let statusColor = '';
                    if(project.status.id == projectCategory){ // Comparing the status of the actual element with the searched category
                        switch (project.status.name) {
                            case 'In Progress': statusColor = 'bg-blue-600'; break;
                            case 'Completed': statusColor = 'bg-green-500'; break;
                        }

                        // Create the project card
                        const projectCard = `
                            <div class="project-card bg-white p-5 rounded-lg border max-w-sm cursor-pointer" onclick="window.location.href='project/${project.id}'">
                                <div class="flex justify-between items-center capitalize">
                                    <h3 class="text-xl font-bold text-gray-800">${project.title}</h3>
                                    <span class="text-white text-sm font-medium px-2.5 py-0.5 rounded-full ${statusColor}">${project.status.name}</span>
                                </div>
                                <p class="text-sm text-gray-500 mt-2">Assigned to: ${project.user ? project.user.username : 'Not assigned'}</p>
                                <p class="text-sm text-gray-500">Client: ${project.client.name}</p>
                                <p class="text-sm text-gray-500">Due Date: ${project.endDate}</p>
                            </div>
                        `;

                        projectGrid.append(projectCard); // Append the project card to the container
                        hasProjects = true; // Set the flag in true to determine that there is at least a match
                    }
                });
                if (!hasProjects) { // If there's no projects then...
                    projectGrid.append(`
                        <div class="flex items-center justify-center gap-3 text-center py-4 bg-light rounded" style="font-size: 1.2em; color: #6c757d;">
                            <i class="fas fa-times-circle" style="font-size: 2em; color: #dc3545;"></i>
                            <p class="mt-2">There are no projects available in this category yet...</p>
                        </div>
                    `);
                }
            }
        });
    },

    createProject(projectData, userToken) {
        $.ajax({
            url: 'http://localhost:8081/api/projects',  // Server URL to create a new project
            method: 'POST', // METHOD
            data: JSON.stringify(projectData), // Convert the object to JSON
            headers: { // Header with Token and set content-type JSON
                'Authorization': 'Bearer ' + userToken,
                'Content-Type': 'application/json'
            }, success: function(response) {
                $.resetProjectForm(); // Reset the form after successful submission
                $.loadProjects(userToken); // Reload projects to reflect the new addition

                $.toast({ // Toast indicating successful registration to the client
                    text: "Project saved successfully",
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
    }
})


